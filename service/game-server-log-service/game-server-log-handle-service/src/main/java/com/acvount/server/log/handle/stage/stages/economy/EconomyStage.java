package com.acvount.server.log.handle.stage.stages.economy;

import com.acvount.server.log.dto.LogMessage;
import com.acvount.server.log.handle.stage.LogStage;
import com.acvount.server.log.handle.stage.stages.economy.parser.EconomyParserChecker;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * @author : acfan
 * date : create in 2023/7/25 21:49
 * description :
 **/

@Component
public class EconomyStage implements LogStage {

    @Resource
    private EconomyParserChecker parserChecker;

    @Override
    public void consumer(LogMessage logMessage) {
        for (String s : splitMessage(logMessage.getContent())) {
            parserChecker.getEconomyParser(s).consumer(s, logMessage.getServerId());
        }
    }


}
