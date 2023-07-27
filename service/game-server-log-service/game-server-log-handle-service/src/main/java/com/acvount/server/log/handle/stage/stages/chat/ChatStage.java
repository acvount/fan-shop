package com.acvount.server.log.handle.stage.stages.chat;

import com.acvount.server.log.dto.LogMessage;
import com.acvount.server.log.handle.stage.LogStage;
import org.springframework.stereotype.Component;

/**
 * @author : acfan
 * date : create in 2023/7/25 21:48
 * description :
 **/

@Component
public class ChatStage implements LogStage {
    @Override
    public void consumer(LogMessage logMessage) {

    }
}
