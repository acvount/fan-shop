package com.acvount.server.log.handle.stage.stages.violations;

import com.acvount.server.log.dto.LogMessage;
import com.acvount.server.log.handle.stage.LogStage;
import org.springframework.stereotype.Component;

/**
 * @author : acfan
 * date : create in 2023/7/25 21:49
 * description :
 **/

@Component
public class ViolationsStage implements LogStage {
    @Override
    public void consumer(LogMessage logMessage) {

    }
}