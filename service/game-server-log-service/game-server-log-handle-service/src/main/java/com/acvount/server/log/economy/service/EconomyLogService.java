package com.acvount.server.log.economy.service;

import com.acvount.server.log.api.ecnonmy.domain.EconomyLog;
import com.acvount.server.log.economy.mapper.EconomyLogMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @author : acfan
 * date : create in 2023/7/27 15:46
 * description :
 **/

@Service
public class EconomyLogService {

    @Resource
    private EconomyLogMapper economyLogMapper;

    public void insertOrUpdate(EconomyLog economyLog) {
        if (economyLogMapper.selectCount(Wrappers.lambdaQuery(
                        EconomyLog.class).eq(EconomyLog::getLogTime, economyLog.getLogTime())
                .eq(EconomyLog::getServerId, economyLog.getServerId())
                .eq(EconomyLog::getTrader, economyLog.getTrader())
                .eq(EconomyLog::getRegion, economyLog.getRegion())
                .eq(EconomyLog::getPlayerSteamId, economyLog.getPlayerSteamId())) > 0) {
            economyLogMapper.updateById(economyLog);
        } else {
            economyLogMapper.insert(economyLog);
        }
    }
}
