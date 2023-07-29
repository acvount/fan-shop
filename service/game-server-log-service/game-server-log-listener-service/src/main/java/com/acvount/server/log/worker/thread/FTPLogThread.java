package com.acvount.server.log.worker.thread;

import com.acvount.common.core.id.IDUtils;
import com.acvount.common.core.security.CalculateUtils;
import com.acvount.server.log.api.domain.ftp.entity.ServerFTP;
import com.acvount.server.log.api.domain.ftp.entity.ServerFTPTaskStats;
import com.acvount.server.log.api.domain.ftp.mapper.ServerFTPTaskStatsMapper;
import com.acvount.server.log.consts.RedisCacheConsts;
import com.acvount.server.log.consts.RocketMQConsts;
import com.acvount.server.log.dto.LogMessage;
import com.acvount.server.log.worker.thread.dto.FTPMetadata;
import com.acvount.server.log.worker.thread.dto.FileMD5DTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.collect.Lists;
import io.micrometer.common.util.StringUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * @author : acfan
 * date : create in 2023/7/23 00:36
 * description : FTP 的线程类
 **/

@Slf4j
public class FTPLogThread implements Runnable {

    private final ServerFTP serverFTP;
    private final RedisTemplate<String, String> redisTemplate;
    private final ServerFTPTaskStatsMapper serverFTPTaskStatsMapper;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    private final static String RemoteFolderPath = "/SCUM/Saved/SaveFiles/Logs/";
    private FTPClient ftpClient;
    private volatile boolean BEGIN_FLAG = true;

    private final StreamBridge streamBridge;

    private final static Integer MAX_ERROR_COUNT = 5;


    public FTPLogThread(ServerFTP serverFTP,
                        RedisTemplate<String, String> redisTemplate,
                        ServerFTPTaskStatsMapper serverFTPTaskStatsMapper,
                        StreamBridge streamBridge) {

        this.serverFTP = serverFTP;
        this.redisTemplate = redisTemplate;
        this.serverFTPTaskStatsMapper = serverFTPTaskStatsMapper;
        this.streamBridge = streamBridge;
    }

    @Override
    public void run() {
        try {
            FTPMetadata ftpMetadata = curFTPCacheOrDBStats();
            FTPClient ftpClient = getFtpClient();
            if (BEGIN_FLAG && ftpClient != null) {
                start(ftpClient, ftpMetadata);
                try {
                    ftpClient.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                    log.info(e.getMessage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("run task fail {}", e.getMessage());
        }

    }

    private void cancelTaskByState(int state) {
        BEGIN_FLAG = false;
        serverFTPTaskStatsMapper.changeStateAndUnTask(serverFTP.getId(), state);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        try {
            redisTemplate.opsForList().remove(IDUtils.programID() + RedisCacheConsts.TaskSuffix, 1, mapper.writeValueAsString(serverFTP));
        } catch (JsonProcessingException e) {
            log.error("json processing exception : {}", e.getMessage());
        }
        log.info("task canceled id:{} ip:{} state:{}", IDUtils.programID(), serverFTP.getIp(), state);
    }

    private FTPClient getFtpClient() {
        FTPClient ftpClient = new FTPClient();
        ftpClient.setDataTimeout(10000);
        try {
            ftpClient.connect(serverFTP.getIp(), serverFTP.getPort());
        } catch (IOException e) {
            log.debug("can't get ftp connection {}", serverFTP.getIp());
            return null;
        }
        try {
            boolean login = ftpClient.login(serverFTP.getUsername(), serverFTP.getPassword());
            if (!login) {
                ftpClient.logout();
                cancelTaskByState(5);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ftpClient.enterLocalPassiveMode();
        ftpClient.setControlEncoding("UTF-8");
        this.ftpClient = ftpClient;
        return ftpClient;
    }

    private boolean start(FTPClient ftpClient) {
        if (!ftpClient.isAvailable()) {
            return false;
        }
        log.debug("服务器：「{}」获取到FTP连接，正在进行初始化任务", serverFTP.getIp());
        FTPMetadata ftpMetadata = new FTPMetadata();
        ftpMetadata.setServerFTP(serverFTP);
        ftpMetadata.setThreadId(IDUtils.programID());
        ftpMetadata.setFileTypes(initFileTypeMap());
        ftpMetadata.setCreateTime(LocalDateTime.now());
        ftpMetadata.setLastRunTime(LocalDateTime.now());
        return start(ftpClient, ftpMetadata);
    }

    private Map<String, FileMD5DTO> initFileTypeMap() {
        return Stream.of("admin", "chat", "economy", "event_kill", "famepoints", "gameplay", "kill", "login", "violations")
                .collect(Collectors.toMap(Function.identity(), this::initFileMD5DTO));
    }

    private FileMD5DTO initFileMD5DTO(String type) {
        LocalDateTime localDateTime = LocalDateTime.now().toLocalDate().atStartOfDay();
        FileMD5DTO fileMD5DTO = new FileMD5DTO();
        fileMD5DTO.setType(type);
        fileMD5DTO.setLastFileName(type + "_" + formatter.format(localDateTime) + ".log");
        fileMD5DTO.setLastFileMd5("-");
        fileMD5DTO.setLastLength(0L);
        fileMD5DTO.setLastTime(localDateTime);
        ServerFTPTaskStats serverFTPTaskStats = new ServerFTPTaskStats();
        serverFTPTaskStats.setFtpId(serverFTP.getId());
        serverFTPTaskStats.setThreadId(IDUtils.programID());
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            String s = mapper.writeValueAsString(fileMD5DTO);
            redisTemplate.opsForValue().set(getFTPMetadataKey(), s, 10, TimeUnit.MINUTES);
            serverFTPTaskStatsMapper.insert(serverFTPTaskStats);
        } catch (Exception e) {
            log.error("初始化MD5DTO InsertDB ｜ 设置Cache 失败");
        }
        return fileMD5DTO;
    }

    private boolean start(FTPClient ftpClient, FTPMetadata ftpMetadata) {
        if (ftpMetadata == null) {
            return start(ftpClient);
        }
        log.debug("服务器：「{}」获取到任务元数据 线程ID:「{}」", serverFTP.getIp(), ftpMetadata.getThreadId());
        Map<String, FileMD5DTO> fileTypes = ftpMetadata.getFileTypes();
        FTPFile[] ftpFiles = null;
        try {
            ftpFiles = ftpClient.listFiles(RemoteFolderPath);
            log.debug("获取到文件列表：{}.length", ftpFiles.length);
        } catch (IOException e) {
            if (e instanceof SocketTimeoutException || e instanceof SocketException) {
                log.debug("task get log ftp socket error : {}", serverFTP.getIp());
                return false;
            } else {
                log.error("run task error metadata : {}", e.getMessage());
                return false;
            }
        }
        Map<String, List<FTPFile>> typeToFileListMap = Arrays.stream(ftpFiles).filter(e -> (e.getSize() > 98))
                .collect(Collectors.groupingBy(
                        file -> file.getName().substring(0, file.getName().lastIndexOf("_"))
                ));
        typeToFileListMap.forEach((k, v) -> {
            FileMD5DTO fileMD5DTO = fileTypes.get(k);
            List<FTPFile> changeFiles = filterFile(v, fileMD5DTO);
            boolean processFlag = processFile(changeFiles, fileMD5DTO);
            if (processFlag) {
                FTPFile lastFile = getLastFile(v);
                if (lastFile != null) {
                    fileMD5DTO.setLastFileName(lastFile.getName());
                    fileMD5DTO.setLastFileMd5("-");
                    fileMD5DTO.setLastTime(LocalDateTime.now());
                    fileMD5DTO.setType(k);
                    fileMD5DTO.setLastLength(lastFile.getSize());
                }
                fileTypes.put(k, fileMD5DTO);
            }
        });
        ftpMetadata.setFileTypes(fileTypes);
        ftpMetadata.setThreadId(IDUtils.programID());
        ftpMetadata.setServerFTP(serverFTP);
        ftpMetadata.setLastRunTime(LocalDateTime.now());
        ftpMetadata.setFileTypes(fileTypes);
        updateCacheAndDB(ftpMetadata);
        return true;
    }

    private FTPFile getLastFile(List<FTPFile> changeFiles) {
        changeFiles = changeFiles == null ? Lists.newArrayList() : sortFiles(changeFiles);
        return !changeFiles.isEmpty() ? changeFiles.get(changeFiles.size() - 1) : null;
    }

    private List<FTPFile> sortFiles(List<FTPFile> changeFiles) {
        return changeFiles.stream()
                .sorted(Comparator.comparing(FTPFile::getName))
                .collect(Collectors.toList());
    }

    private List<FTPFile> filterFile(List<FTPFile> files, FileMD5DTO fileMD5DTO) {
        return files.stream().filter(file -> {
            String logtimeStr = file.getName().replace(fileMD5DTO.getType() + "_", "").replace(".log", "");
            LocalDateTime logTime = LocalDateTime.parse(logtimeStr, formatter);
            return fileMD5DTO.getLastTime().isBefore(logTime) ||                                         //日志时间>最后读取时间
                    (fileMD5DTO.getLastFileName().equals(file.getName()) &&                             //最后一次文件名字==现在的文件名字
                            fileMD5DTO.getLastLength() != file.getSize());    // 且文件长度不一致
        }).collect(Collectors.toList());
    }

    private boolean processFile(List<FTPFile> files, FileMD5DTO fileMD5DTO) {
        if (files == null || files.isEmpty()) {
            return true;
        }

        for (FTPFile file : files) {
            processSingleFile(file, fileMD5DTO);
        }
        return true;
    }

    private void processSingleFile(FTPFile file, FileMD5DTO fileMD5DTO) {
        Long start = 0L;
        if (file.getName().equalsIgnoreCase(fileMD5DTO.getLastFileName())) {
            //继续上一次的文件读。
            start = fileMD5DTO.getLastLength();
        }
        try {
            if (ftpClient.isAvailable()) {
                InputStream inputStream = ftpClient.retrieveFileStream(RemoteFolderPath + file.getName());
                String content = readFTPFileByStart(inputStream, start);
                simpleProcessContent(content, fileMD5DTO.getType()).forEach(context -> {
                    if (Boolean.FALSE.equals(redisTemplate.hasKey(CalculateUtils.md5(context)))) {
                        redisTemplate.opsForValue().set(CalculateUtils.md5(context), "repeat", 5, TimeUnit.MINUTES);
                        redisTemplate.opsForHash().put(serverFTP.getIp() + "-repeat", content, "");
                        streamBridge.send(RocketMQConsts.GameLogTopic, assembleMessageBody(fileMD5DTO.getType(), context));
                        log.info("FTP: {} 消费一次消息成功 File :{}", serverFTP.getIp(), file.getName());
                    }
                });
            }
        } catch (IOException e) {
            if ("Connection closed without indication.".equalsIgnoreCase(e.getMessage()) || "Broken pipe".equalsIgnoreCase(e.getMessage())) {
                getFtpClient();
                processSingleFile(file, fileMD5DTO);
            } else {
                log.error("消费一次任务失败: {}", e.getMessage());
            }
        }
    }

    private List<String> simpleProcessContent(String content, String type) {
        if ("economy".equals(type)) {
            return List.of(content);
        }
        return Arrays.stream(content.split("\n")).filter(StringUtils::isNotBlank).collect(Collectors.toList());
    }

    private void updateCacheAndDB(FTPMetadata ftpMetadata) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        try {
            String s = mapper.writeValueAsString(ftpMetadata);
            redisTemplate.opsForValue().set(getFTPMetadataKey(), s, 10, TimeUnit.MINUTES);
            ServerFTPTaskStats serverFTPTaskStats = new ServerFTPTaskStats();
            serverFTPTaskStats.setFtpId(serverFTP.getId());
            serverFTPTaskStats.setMetadata(s);
            serverFTPTaskStatsMapper.updateById(serverFTPTaskStats);
        } catch (JsonProcessingException e) {
            log.error("设置缓存元数据失败，{}", e.getMessage());
        }

    }

    private String readFTPFileByStart(InputStream inputStream, Long start) {
        if (inputStream == null) {
            return null;
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;

        try {
            long bytesToSkip = start;
            while (bytesToSkip > 0) {
                bytesToSkip -= inputStream.skip(bytesToSkip);
            }

            while ((len = inputStream.read(buffer, 0, buffer.length)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            return outputStream.toString(StandardCharsets.UTF_16LE);
        } catch (IOException e) {
            log.error("读取文件出错{}", e.getMessage());
            return "读取文件出错,线程ID:" + IDUtils.programID() + "元信息：" + e.getMessage();
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @SneakyThrows
    private FTPMetadata curFTPCacheOrDBStats() {
        String cacheJson = redisTemplate.opsForValue().get(getFTPMetadataKey());
        String json = StringUtils.isBlank(cacheJson) ? getDBMetadata() : cacheJson;
        if (StringUtils.isBlank(json)) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper.readValue(json, FTPMetadata.class);
    }

    private String getDBMetadata() {
        ServerFTPTaskStats serverFTPTaskStats = serverFTPTaskStatsMapper.selectById(serverFTP.getId());
        return serverFTPTaskStats != null ? serverFTPTaskStats.getMetadata() : null;
    }


    private String getFTPMetadataKey() {
        return RedisCacheConsts.FTP_Metadata_Prefix + serverFTP.getId();
    }

    private Message<LogMessage> assembleMessageBody(String type, String content) {
        LogMessage logMessage = new LogMessage();
        logMessage.setContent(content);
        logMessage.setType(type);
        logMessage.setOwnerId(serverFTP.getOwnerId());
        logMessage.setServerId(serverFTP.getServerId());
        logMessage.setFtpId(serverFTP.getId());
        return MessageBuilder.withPayload(logMessage)
                .setHeader(RocketMQConsts.ROCKET_MQ_GROUP_HEADER, RocketMQConsts.ROCKET_MQ_GROUP_HEADER)
                .setHeader(RocketMQConsts.ROCKET_MQ_CONTENT_TYPE_HEADER_NAME, RocketMQConsts.ROCKET_MQ_APPLICATION_JSON)
                .build();
    }
}