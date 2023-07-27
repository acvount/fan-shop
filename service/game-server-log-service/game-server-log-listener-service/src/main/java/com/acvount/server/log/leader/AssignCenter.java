package com.acvount.server.log.leader;

import com.acvount.server.log.consts.RedisCacheConsts;
import com.acvount.server.log.api.domain.ftp.entity.ServerFTP;
import com.acvount.server.log.api.domain.ftp.mapper.ServerFTPMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author : acfan
 * date : create in 2023/7/22 23:43
 * description : 分配任务
 **/

@Slf4j
@Component
public class AssignCenter {

    @Resource
    private LeaderElection leaderElection;

    @Resource
    private ServerFTPMapper serverFTPMapper;

    @Resource
    private RedisTemplate<String, String> ftpRedisTemplate;

    //20秒分配一次任务
    @Scheduled(fixedRate = 2000L)
    private void checkAndAssign() {
        try {
            if (Boolean.FALSE.equals(leaderElection.getLeaderPermission())) {
                return;
            }
            List<ServerFTP> enabledFTP = serverFTPMapper.getEnabledFTP();
            if ((enabledFTP.size() > 0)) {
                log.info("有待分配任务， 正在分配中");
                assign(enabledFTP);
            }
        } catch (Exception e) {
            log.error("分配任务时发生异常：{}", e.getMessage());
            e.printStackTrace();
        }

    }

    private void assign(List<ServerFTP> ftpList) {
        List<String> services = ftpRedisTemplate.opsForList().range(RedisCacheConsts.ServerListKey, 0, -1);
        if (services == null || services.isEmpty()) {
            log.error("未发现服务");
            return;
        }
        //初始化各个服务的任务数量
        Map<String, Integer> serverSizeMap = services.stream()
                .collect(Collectors.toMap(
                        key -> key,
                        key -> Optional.ofNullable(ftpRedisTemplate.opsForList().size(key)).orElse(0L).intValue()
                ));

        Iterator<ServerFTP> iterator = ftpList.iterator();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        //平均分配任务
        serverSizeMap.entrySet().stream().sorted(Map.Entry.comparingByValue()).map(Map.Entry::getKey).forEach(key -> {
            try {
                if (!iterator.hasNext()) {
                    return;
                }
                ServerFTP next = iterator.next();
                serverFTPMapper.enableTaskById(next.getId());
                log.info("FTP:{}已分配给{}节点", next.getIp(), key);
                ftpRedisTemplate.opsForList().rightPush(key, mapper.writeValueAsString(next));
            } catch (Exception e) {
                log.error("assign task error : {}", e.getMessage());
            }
        });

    }

}
