package com.acvount.server.log.leader;

import com.acvount.server.log.consts.RedisCacheConsts;
import com.acvount.server.log.api.domain.ftp.entity.ServerFTP;
import com.acvount.server.log.api.domain.ftp.mapper.ServerFTPMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.micrometer.common.util.StringUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author : acfan
 * date : create in 2023/7/22 23:45
 * description : 取消任务
 **/
@Slf4j
@Component
public class CancelCenter {

    private final Long GapTime = TimeUnit.SECONDS.toMillis(30L);


    @Resource
    private ServerFTPMapper serverFTPMapper;

    @Resource
    private RedisTemplate<String, String> ftpRedisTemplate;

    @Scheduled(fixedRate = 5000L)
    private void cancelTask() {
        try {
            cancelTask(System.currentTimeMillis());
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }

    private void cancelTask(Long startTimestamp) {
        List<String> services = ftpRedisTemplate.opsForList().range(RedisCacheConsts.ServerListKey, 0, -1);
        if (services == null || services.isEmpty()) {
            return;
        }
        services.stream().filter(service -> isBadNode(service, startTimestamp)).forEach(service -> {
            List<String> taskJsonList;
            while ((taskJsonList = ftpRedisTemplate.opsForList().leftPop(service, 9)) != null && !taskJsonList.isEmpty()) {
                for (String taskJson : taskJsonList) {
                    ServerFTP ftp = parseServerFTP(taskJson);
                    if (ftp != null) {
                        log.info("{}FTP:{}已被取消", service, ftp.getIp());
                        serverFTPMapper.disableTaskById(ftp.getId());
                    }
                }
            }
            ftpRedisTemplate.opsForList().remove(RedisCacheConsts.ServerListKey, 1, service);
            ftpRedisTemplate.delete(service);
            log.info("{}任务列表清除成功", service);
        });

    }

    private ServerFTP parseServerFTP(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            return mapper.readValue(json, ServerFTP.class);
        } catch (JsonProcessingException e) {
            log.error("parse error {}", e.getMessage());
            return null;
        }
    }

    private boolean isBadNode(String serviceKey, Long startTimestamp) {
        serviceKey = getNodeIDByServiceKey(serviceKey);
        String heathStrTimestamp = ftpRedisTemplate.opsForValue().get(serviceKey);
        if (StringUtils.isBlank(heathStrTimestamp)) {
            return true;
        }
        try {
            return Math.abs(startTimestamp - Long.parseLong(heathStrTimestamp)) > GapTime;
        } catch (Exception e) {
            log.error("calc bad node exception : {}", e.getMessage());
        }
        return false;
    }

    private String getNodeIDByServiceKey(String serviceKey) {
        return RedisCacheConsts.ServicePrefix + serviceKey.replace(RedisCacheConsts.TaskSuffix, "");
    }
}
