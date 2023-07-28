package com.acvount.server.log.handle.stage.stages.login;

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
public class LoginStage implements LogStage {
    private final LoseLogService loseLogService;
    @Autowired
    public LoginStage(LoseLogService loseLogService) {
        this.loseLogService = loseLogService;
    }
    @Override
    public void consumer(LogMessage logMessage) {
        splitMessage(logMessage.getContent()).forEach(e -> {
            loseLogService.addLoseLog(logMessage.getServerId(), e, "login");
        });
    }
}
