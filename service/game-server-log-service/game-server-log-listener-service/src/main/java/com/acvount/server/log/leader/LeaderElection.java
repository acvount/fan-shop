package com.acvount.server.log.leader;

import com.acvount.common.core.id.IDUtils;
import com.acvount.server.log.consts.RedisCacheConsts;
import io.micrometer.common.util.StringUtils;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author : acfan
 * date : create in 2023/7/22 10:19
 * description : Leader 竞选
 **/

@Slf4j
@Component
@DependsOn("redissonConfig")
public class LeaderElection {

    private final static String ID = IDUtils.programID();
    private final String leaderKey = RedisCacheConsts.LeaderKey;
    private static volatile boolean isLeader = false;
    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Resource
    private RedissonClient redissonClient;

    public boolean getLeaderPermission() {
        //double-check
        String remoteLeader = redisTemplate.opsForValue().get(leaderKey);
        remoteLeader = StringUtils.isBlank(remoteLeader) ? "" : remoteLeader;
        return isLeader && Boolean.TRUE.equals(remoteLeader.equals(ID));
    }


    @PostConstruct
    private void generID() {
        //强占Leader，先入为主
        Boolean leaderFlag = redisTemplate.opsForValue().setIfAbsent(leaderKey, ID,3L,TimeUnit.SECONDS);
        redisTemplate.opsForList().rightPush(RedisCacheConsts.ServerListKey,ID+RedisCacheConsts.TaskSuffix);
        isLeader = Boolean.TRUE.equals(leaderFlag);
        log.info("程序启动成功，竞选Leader 已结束 结果：{} ->", isLeader);
    }

    //开启心跳包
    @Scheduled(fixedRate = 1000L)private void heathTask() {
        try {
            redisTemplate.opsForValue().set(RedisCacheConsts.ServicePrefix + ID, String.valueOf(System.currentTimeMillis()), 4L, TimeUnit.SECONDS);
            log.debug("pod heath breath");
        } catch (Exception e) {
            log.error("leader 心跳 异常 放弃Leader 身份 message: {}", e.getMessage());
            isLeader = false;
        }
    }

    //同步Leader ID并检查Leader心跳
    @Scheduled(fixedRate = 3000L)
    private void syncLeaderTask() {
        try {
            String remoteLeaderKey = redisTemplate.opsForValue().get(leaderKey);
            isLeader = ID.equals(remoteLeaderKey);
            // 如果当前实例已是Leader，无需参与竞选
            if (isLeader) {
                redisTemplate.expire(leaderKey,4L,TimeUnit.SECONDS);
                log.debug("当前实例已是Leader，续期Leader Key。");
                return;
            }

            String leaderHeartbeat = redisTemplate.opsForValue().get(RedisCacheConsts.ServicePrefix + remoteLeaderKey);
            // 当前Leader仍然活跃，未超过心跳超时，暂不进行竞选
            if (leaderHeartbeat != null && (System.currentTimeMillis() - Long.parseLong(leaderHeartbeat)) <= 3000) {
                log.debug("当前Leader仍然活跃，未超过心跳超时，暂不进行竞选。");
                return;
            }

            RLock lock = redissonClient.getLock(RedisCacheConsts.LeaderLockPrefix + leaderKey);
            if (!lock.tryLock(0, 3, TimeUnit.SECONDS)) {
                log.info("竞选Leader失败，锁已被其他线程持有。");
                return;
            }

            try {
                redisTemplate.opsForValue().set(leaderKey, ID, 5L, TimeUnit.SECONDS);
                isLeader = true;
                log.info("成功竞选为Leader，当前Leader ID为：{}", ID);
            } finally {
                lock.unlock();
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("同步Leader任务发生异常：{}", e.getMessage());
        }
    }
}
