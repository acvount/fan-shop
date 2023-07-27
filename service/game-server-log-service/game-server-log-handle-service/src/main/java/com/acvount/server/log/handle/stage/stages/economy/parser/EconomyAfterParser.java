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

    private static final String LOG_PATTERN = "(?<loggerDate>\\d|.*?):.*?to trader(?<trader>[^,]+)_(?<region>\\w+).*?player(?<playerName>.*?)\\((?<playerSteamID>\\d*)\\).*?(?<afterCash>\\d.*?)cash,.*?(?<afterAccount>\\d.*?)account.*?(?<afterGold>\\d).*?(?<afterTraderMoney>\\d+).";
    private static final Pattern logPattern = Pattern.compile(LOG_PATTERN);

    @Override
    public void consumer(String logText, Long serverId) {
        Matcher matcher = logPattern.matcher(logText);
        if (matcher.find()) {
            EconomyLog economyLog = new EconomyLog();
            economyLog.setLogTime(parseLogTime(matcher.group(1)));
            economyLog.setTrader(matcher.group("trader"));
            economyLog.setRegion(matcher.group("region"));
            economyLog.setPlayerName(matcher.group("playerName"));
            economyLog.setPlayerSteamId(matcher.group("playerSteamID"));
            economyLog.setAfterCash(matcher.group("afterCash"));
            economyLog.setAfterAccount(matcher.group("afterAccount"));
            economyLog.setAfterGold(matcher.group("afterGold"));
            economyLog.setAfterTraderMoney(matcher.group("afterTraderMoney"));
            economyLog.setServerId(serverId);
            if (economyLogMapper.selectCount(Wrappers.lambdaQuery(
                            EconomyLog.class).eq(EconomyLog::getLogTime, economyLog.getLogTime())
                    .eq(EconomyLog::getServerId, serverId)
                    .eq(EconomyLog::getTrader, economyLog.getTrader())
                    .eq(EconomyLog::getRegion, economyLog.getRegion())
                    .eq(EconomyLog::getPlayerSteamId, economyLog.getPlayerSteamId())
            ) > 0) {
                economyLogMapper.updateById(economyLog);
            } else {
                economyLogMapper.insert(economyLog);
            }
            log.info("consumer economy after player steam:{} server:{}", economyLog.getPlayerSteamId(), economyLog.getServerId());
        }
    }

    private static LocalDateTime parseLogTime(String logTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd-HH.mm.ss");
        return LocalDateTime.parse(logTime, formatter);
    }
}
