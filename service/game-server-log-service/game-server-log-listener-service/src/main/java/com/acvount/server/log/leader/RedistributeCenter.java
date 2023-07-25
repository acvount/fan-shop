package com.acvount.server.log.leader;

import com.acvount.server.log.consts.RedisCacheConsts;
import io.micrometer.common.util.StringUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author : acfan
 * date : create in 2023/7/22 23:48
 * description : 重新分配任务
 **/

@Slf4j
@Component
public class RedistributeCenter {
    @Resource
    private RedisTemplate<String, String> redisTemplate;

    //两小时执行一次
    @Scheduled(fixedRate = 7200000)
    public void redistributeTasks() {
        Map<String, Integer> taskDiffMap = getTaskDiffMap();
        if (taskDiffMap == null || taskDiffMap.isEmpty()) {
            return;
        }
        int avgTaskCount = calculateAverageTaskCount(taskDiffMap);
        int smoothFactor = calculateSmoothFactor(avgTaskCount);

        redistributeTasksBasedOnSmoothFactor(taskDiffMap, avgTaskCount, smoothFactor);
    }


    // 计算所有服务的平均任务数量
    private int calculateAverageTaskCount(Map<String, Integer> serverTaskCount) {
        int totalTaskCount = serverTaskCount.values().stream().mapToInt(Integer::intValue).sum();
        return totalTaskCount / serverTaskCount.size();
    }


    private Map<String, Integer> getTaskDiffMap() {
        List<String> services = redisTemplate.opsForList().range(RedisCacheConsts.ServerListKey, 0, -1);
        if (services == null || services.isEmpty()) {
            return null;
        }
        return services.stream()
                .collect(Collectors.toMap(
                        key -> key,
                        key -> Optional.ofNullable(redisTemplate.opsForList().size(key)).orElse(0L).intValue()
                ));
    }

    private int calculateSmoothFactor(int avgTaskCount) {
        return Math.max(1, avgTaskCount / 10); // 简单地将平滑因子设为平均任务数的10%，具体值后续再说
    }

    private void redistributeTasksBasedOnSmoothFactor(Map<String, Integer> taskDiffMap, int avgTaskCount, int smoothFactor) {
        LinkedList<Map.Entry<String, Integer>> overLoadedServers = filterOverLoadedServers(taskDiffMap, avgTaskCount, smoothFactor);
        LinkedList<Map.Entry<String, Integer>> underLoadedServers = filterUnderLoadedServers(taskDiffMap, avgTaskCount, smoothFactor);

        while (!overLoadedServers.isEmpty() && !underLoadedServers.isEmpty()) {
            Map.Entry<String, Integer> overLoadedServer = overLoadedServers.getFirst();
            Map.Entry<String, Integer> underLoadedServer = underLoadedServers.getFirst();

            migrateTask(overLoadedServer.getKey(), underLoadedServer.getKey());

            overLoadedServer.setValue(overLoadedServer.getValue() - 1);
            underLoadedServer.setValue(underLoadedServer.getValue() + 1);

            if (overLoadedServer.getValue() <= avgTaskCount + smoothFactor) {
                overLoadedServers.removeFirst();
            }
            if (underLoadedServer.getValue() >= avgTaskCount - smoothFactor) {
                underLoadedServers.removeFirst();
            }
        }
    }

    private LinkedList<Map.Entry<String, Integer>> filterOverLoadedServers(Map<String, Integer> taskDiffMap, int avgTaskCount, int smoothFactor) {
        return taskDiffMap.entrySet().stream()
                .filter(entry -> entry.getValue() > avgTaskCount + smoothFactor)
                .collect(Collectors.toCollection(LinkedList::new));
    }

    private LinkedList<Map.Entry<String, Integer>> filterUnderLoadedServers(Map<String, Integer> taskDiffMap, int avgTaskCount, int smoothFactor) {
        return taskDiffMap.entrySet().stream()
                .filter(entry -> entry.getValue() < avgTaskCount - smoothFactor)
                .collect(Collectors.toCollection(LinkedList::new));
    }

    private void migrateTask(String fromServer, String toServer) {
        // 从源服务的任务列表中移除一个任务
        String task = redisTemplate.opsForList().rightPop(fromServer);
        if (StringUtils.isNotBlank(task)) {
            // 将这个任务添加到目标服务的任务列表中
            redisTemplate.opsForList().rightPush(toServer, task);
        }
    }
}
