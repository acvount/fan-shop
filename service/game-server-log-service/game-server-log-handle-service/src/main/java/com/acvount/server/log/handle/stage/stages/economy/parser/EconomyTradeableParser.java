package com.acvount.server.log.handle.stage.stages.economy.parser;

import com.acvount.server.log.dto.LogMessage;
import com.acvount.server.log.lose.service.LoseLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author : acfan
 * date : create in 2023/7/26 22:13
 * description :
 **/

@Component
public class EconomyTradeableParser implements EconomyParser {
    private final LoseLogService loseLogService;

    @Autowired
    public EconomyTradeableParser(LoseLogService loseLogService) {
        this.loseLogService = loseLogService;
    }

    @Override
    public void consumer(String log, Long serverId) {
        loseLogService.addLoseLog(serverId, log, "tradeable");
    }
}
