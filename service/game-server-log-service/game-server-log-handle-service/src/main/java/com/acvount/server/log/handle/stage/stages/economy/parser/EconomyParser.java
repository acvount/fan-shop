package com.acvount.server.log.handle.stage.stages.economy.parser;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author : acfan
 * date : create in 2023/7/26 22:12
 * description :
 **/

public interface EconomyParser {

    void consumer(String log, Long serverId);

    default LocalDateTime parseLogTime(String logTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd-HH.mm.ss");
        return LocalDateTime.parse(logTime, formatter);
    }
}
