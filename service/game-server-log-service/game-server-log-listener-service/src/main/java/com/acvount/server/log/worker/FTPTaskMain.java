package com.acvount.server.log.worker;

import com.acvount.common.core.id.IDUtils;
import com.acvount.common.core.security.CalculateUtils;
import com.acvount.server.log.api.domain.ftp.entity.ServerFTP;
import com.acvount.server.log.api.domain.ftp.mapper.ServerEnabledFtpLogListenerMapper;
import com.acvount.server.log.api.domain.ftp.mapper.ServerFTPTaskStatsMapper;
import com.acvount.server.log.consts.RedisCacheConsts;
import com.acvount.server.log.worker.thread.FTPLogThread;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.*;

/**
 * @author : acfan
 * date : create in 2023/7/23 00:31
 * description : Worker 节点的任务主入口
 **/

@Slf4j
@Component
public class FTPTaskMain {
    private static final int MAX_THREAD_POOL_SIZE = 20;
    private static final int BATCH_SIZE = 5;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Resource
    private ServerFTPTaskStatsMapper serverFTPTaskStatsMapper;

    @Resource
    private ServerEnabledFtpLogListenerMapper serverEnabledFtpLogListenerMapper;

    @Resource
    private StreamBridge streamBridge;

    @Resource
    private RedissonClient redissonClient;

    private ExecutorService threadPoolExecutor;

    @PostConstruct
    private void initialize() {
        threadPoolExecutor = new ThreadPoolExecutor(MAX_THREAD_POOL_SIZE, MAX_THREAD_POOL_SIZE,
                0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
    }


    //获取自身任务并分配线程 一秒一次，
    @Scheduled(fixedRate = 5000L)
    private void assignThread() {
        try {
            if (threadPoolExecutor == null || threadPoolExecutor.isTerminated() || threadPoolExecutor.isShutdown()) {
                log.debug("thread renew");
                initialize();
            }

            String curTaskRedisKey = getCurTaskRedisKey();
            Long size = redisTemplate.opsForList().size(curTaskRedisKey);
            if (size == null || size.intValue() == 0) {
                log.debug("no task return");
                return;
            }
            ListOperations<String, String> listOps = redisTemplate.opsForList();
            int index = 0;
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());

            do {
                List<String> batch = listOps.range(curTaskRedisKey, index, index + BATCH_SIZE);
                if (batch == null || batch.isEmpty()) {
                    break;
                }
                for (String ftpServer : batch) {
                    // 检查ftpServer是否已经在处理中，如果是，则跳过
                    try {
                        ServerFTP serverFTP = mapper.readValue(ftpServer, ServerFTP.class);
                        if (checkCurTask(serverFTP.getId(), ftpServer)) {
                            continue;
                        }

                        threadPoolExecutor.execute(() -> {
                            RLock lock = redissonClient.getLock("thread-lock-" + CalculateUtils.md5(serverFTP));
                            try {
                                if (lock.tryLock(0, TimeUnit.MICROSECONDS)) {
                                    try {
                                        long l = System.currentTimeMillis();
                                        log.debug("ftp {} task commit locked ", serverFTP.getIp());
                                        FTPLogThread ftpLogThread = new FTPLogThread(serverFTP, redisTemplate, serverFTPTaskStatsMapper, streamBridge);
                                        ftpLogThread.run();
                                        log.debug("ftp {} task success run time :{} unlocked", serverFTP.getIp(), System.currentTimeMillis() - l);
                                    } finally {
                                        lock.unlock();
                                    }
                                }
                            } catch (InterruptedException e) {
                                log.error(e.getMessage());
                            }
                        });
                    } catch (Exception e) {
                        log.error("commit task error {}", e.getMessage());
                    }
                }
                index++;
            } while (true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean checkCurTask(Long taskId, String data) {
        if (serverEnabledFtpLogListenerMapper.selectByFtpId(taskId).getTaskStatus() != 1) {
            log.info("FTP{}已被取消任务，stats not 1", taskId);
            redisTemplate.opsForList().remove(getCurTaskRedisKey(), 1, data);
            return true;
        }
        return false;
    }

    private String getCurTaskRedisKey() {
        return IDUtils.programID() + RedisCacheConsts.TaskSuffix;
    }

}
