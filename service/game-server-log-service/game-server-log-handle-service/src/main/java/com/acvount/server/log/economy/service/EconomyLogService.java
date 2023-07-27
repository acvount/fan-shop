package com.acvount.server.log.economy.service;

import com.acvount.server.log.api.ecnonmy.domain.EconomyLog;
import com.acvount.server.log.economy.mapper.EconomyLogMapper;
import com.acvount.server.log.lose.service.LoseLogService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author : acfan
 * date : create in 2023/7/27 15:46
 * description :
 **/

@Slf4j
@Service
public class EconomyLogService {

    @Resource
    private EconomyLogMapper economyLogMapper;

    @Resource
    private LoseLogService loseLogService;

    public void insertOrUpdate(EconomyLog economyLog) {
        try {
            EconomyLog dbEntry = economyLogMapper.selectOne(Wrappers.lambdaQuery(
                            EconomyLog.class).eq(EconomyLog::getLogTime, economyLog.getLogTime())
                    .eq(EconomyLog::getServerId, economyLog.getServerId())
                    .eq(EconomyLog::getTrader, economyLog.getTrader())
                    .eq(EconomyLog::getRegion, economyLog.getRegion())
                    .eq(EconomyLog::getPlayerSteamId, economyLog.getPlayerSteamId()));
            if (dbEntry != null) {
                economyLog.setId(dbEntry.getId());
                economyLogMapper.updateById(economyLog);
            } else {
                economyLogMapper.insert(economyLog);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            loseLogService.addLoseLog(economyLog.getServerId(), economyLog.toString(), "economy exist");
        }
    }
}
