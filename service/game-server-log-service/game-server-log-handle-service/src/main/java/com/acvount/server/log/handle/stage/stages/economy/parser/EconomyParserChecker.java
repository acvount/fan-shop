package com.acvount.server.log.handle.stage.stages.economy.parser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;

/**
 * @author : acfan
 * date : create in 2023/7/26 22:12
 * description :
 **/

@Component
public class EconomyParserChecker {

    private final Map<LogType, EconomyParser> economyParsers;

    @Autowired
    public EconomyParserChecker(EconomyBeforeParser economyBeforeParser,
                                EconomyTradeableParser economyTradeableParser,
                                EconomyAfterParser economyAfterParser) {
        economyParsers = new EnumMap<>(LogType.class);
        economyParsers.put(LogType.BEFORE, economyBeforeParser);
        economyParsers.put(LogType.TRADEABLE, economyTradeableParser);
        economyParsers.put(LogType.AFTER, economyAfterParser);

    }

    public EconomyParser getEconomyParser(String content) {
        for (LogType logType : LogType.values()) {
            if (content.toLowerCase().matches(".*\\[trade\\] " + logType + ".*")) {
                return economyParsers.get(logType);
            }
        }
        return null;
    }

    private enum LogType {
        BEFORE,
        TRADEABLE,
        AFTER;

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }
}
