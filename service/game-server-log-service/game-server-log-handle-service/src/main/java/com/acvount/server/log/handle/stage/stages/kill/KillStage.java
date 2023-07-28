package com.acvount.server.log.handle.stage.stages.kill;

import com.acvount.server.log.dto.LogMessage;
import com.acvount.server.log.handle.stage.LogStage;
import com.acvount.server.log.lose.service.LoseLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author : acfan
 * date : create in 2023/7/25 21:49
 * description :
 **/

@Component
public class KillStage implements LogStage {
    private final LoseLogService loseLogService;

    @Autowired
    public KillStage(LoseLogService loseLogService) {
        this.loseLogService = loseLogService;
    }

    @Override
    public void consumer(LogMessage logMessage) {
        splitMessage(logMessage.getContent()).forEach(e -> {
            loseLogService.addLoseLog(logMessage.getServerId(), e, "kill");
        });
    }
}
