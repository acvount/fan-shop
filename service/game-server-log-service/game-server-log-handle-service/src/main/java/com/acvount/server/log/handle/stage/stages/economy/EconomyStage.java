package com.acvount.server.log.handle.stage.stages.economy;

import com.acvount.server.log.dto.LogMessage;
import com.acvount.server.log.handle.stage.LogStage;
import com.acvount.server.log.handle.stage.stages.economy.parser.EconomyParser;
import com.acvount.server.log.handle.stage.stages.economy.parser.EconomyParserChecker;
import com.acvount.server.log.lose.service.LoseLogService;
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
    @Resource
    private LoseLogService loseLogService;

    @Override
    public void consumer(LogMessage logMessage) {
        for (String s : splitMessage(logMessage.getContent())) {
            EconomyParser economyParser = parserChecker.getEconomyParser(s);
            if (economyParser != null) {
                economyParser.consumer(s, logMessage.getServerId());
            } else {
                loseLogService.addLoseLog(logMessage.getServerId(), logMessage.getContent(), "economy_no_get_checker");
            }
        }
    }


}
