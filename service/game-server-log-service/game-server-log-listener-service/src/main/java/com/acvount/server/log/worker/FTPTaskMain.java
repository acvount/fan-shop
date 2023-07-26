package com.acvount.server.log.worker;

import com.acvount.common.core.id.IDUtils;
import com.acvount.server.log.consts.RedisCacheConsts;
import com.acvount.server.log.domain.ftp.entity.ServerFTP;
import com.acvount.server.log.domain.ftp.mapper.ServerFTPTaskStatsMapper;
import com.acvount.server.log.worker.thread.FTPLogThread;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author : acfan
 * date : create in 2023/7/23 00:31
 * description : Worker 节点的任务主入口
 **/

@Slf4j
@Component
public class FTPTaskMain {
    private static final int MAX_THREAD_POOL_SIZE = 5;
    private static final int BATCH_SIZE = 5;

    private ExecutorService threadPool;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Resource
    private ServerFTPTaskStatsMapper serverFTPTaskStatsMapper;

    @Resource
    private StreamBridge streamBridge;


    //获取自身任务并分配线程 一秒一次，
    @Scheduled(fixedRate = 1000L)
    private void assignThread() {
        if (threadPool != null && !threadPool.isTerminated()) {
            // 上一个线程未结束，暂不执行新任务
            return;
        }
        threadPool = Executors.newFixedThreadPool(MAX_THREAD_POOL_SIZE);
        String curTaskRedisKey = getCurTaskRedisKey();
        Long size = redisTemplate.opsForList().size(curTaskRedisKey);
        if (size == null || size.intValue() == 0) {
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
                try {
                    threadPool.execute(new FTPLogThread(mapper.readValue(ftpServer, ServerFTP.class), redisTemplate, serverFTPTaskStatsMapper, streamBridge));
                } catch (Exception e) {
                    log.error("commit task error {}", e.getMessage());
                }
            }
            index++;
        } while (true);
        threadPool.shutdown();
    }


    private ExecutorService createThreadPool(Long currentTaskCount) {
        int threadPoolSize = currentTaskCount.intValue() / 2;
        threadPoolSize = Math.min(threadPoolSize, MAX_THREAD_POOL_SIZE);
        threadPoolSize = Math.max(threadPoolSize, 1);
        return Executors.newFixedThreadPool(threadPoolSize);
    }

    private String getCurTaskRedisKey() {
        return IDUtils.programID() + RedisCacheConsts.TaskSuffix;
    }

}
