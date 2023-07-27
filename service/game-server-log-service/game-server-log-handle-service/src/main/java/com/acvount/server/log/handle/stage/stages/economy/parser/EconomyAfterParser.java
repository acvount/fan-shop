package com.acvount.server.log.handle.stage.stages.economy.parser;

import com.acvount.server.log.api.ecnonmy.domain.EconomyLog;
import com.acvount.server.log.economy.mapper.EconomyLogMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author : acfan
 * date : create in 2023/7/26 22:13
 * description :
 **/

@Slf4j
@Component
public class EconomyAfterParser implements EconomyParser {

    @Resource
    private EconomyLogMapper economyLogMapper;

    private static final String LOG_PATTERN = "(\\d{4}\\.\\d{2}\\.\\d{2}-\\d{2}\\.\\d{2}\\.\\d{2}): \\[trade\\] .* after tradeable sale to trader (?<trader>.*?), player (?<playerName>.*?)\\((?<playerSteamID>\\d+)\\) .* has (?<afterCash>\\d+) cash, (?<afterAccount>\\d+) account balance and (?<afterGold>\\d+) gold and trader has (?<afterTraderMoney>\\d+) funds\\.";
    private static final Pattern logPattern = Pattern.compile(LOG_PATTERN);

    @Override
    public void consumer(String logText, Long serverId) {
        System.out.println(logText);
        Matcher matcher = logPattern.matcher(logText);
        if (matcher.find()) {
            EconomyLog economyLog = new EconomyLog();
            economyLog.setLogTime(parseLogTime(matcher.group(1)));
            economyLog.setTrader(matcher.group("trader"));
            economyLog.setPlayerName(matcher.group("playerName"));
            economyLog.setPlayerSteamID(matcher.group("playerSteamID"));
            economyLog.setAfterCash(matcher.group("afterCash"));
            economyLog.setAfterAccount(matcher.group("afterAccount"));
            economyLog.setAfterGold(matcher.group("afterGold"));
            economyLog.setAfterTraderMoney(matcher.group("afterTraderMoney"));
            if (economyLogMapper.selectCount(Wrappers.lambdaQuery(EconomyLog.class)
                    .eq(EconomyLog::getLogTime, economyLog.getLogTime())
                    .eq(EconomyLog::getPlayerSteamID, economyLog.getPlayerSteamID())) > 0) {
                economyLogMapper.updateById(economyLog);
            } else {
                economyLogMapper.insert(economyLog);
            }
            log.info("consumer economy after player steam:{} server:{}", economyLog.getPlayerSteamID(), economyLog.getServerId());
        }
    }

    private static LocalDateTime parseLogTime(String logTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd-HH.mm.ss");
        return LocalDateTime.parse(logTime, formatter);
    }
}
