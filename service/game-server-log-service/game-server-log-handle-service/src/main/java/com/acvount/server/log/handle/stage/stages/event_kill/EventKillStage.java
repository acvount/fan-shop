package com.acvount.server.log.handle.stage.stages.event_kill;

import com.acvount.server.log.dto.LogMessage;
import com.acvount.server.log.handle.stage.LogStage;
import org.springframework.stereotype.Component;

/**
 * @author : acfan
 * date : create in 2023/7/25 21:49
 * description :
 **/

@Component
public class EventKillStage implements LogStage {
    @Override
    public void consumer(LogMessage logMessage) {

    }
}
