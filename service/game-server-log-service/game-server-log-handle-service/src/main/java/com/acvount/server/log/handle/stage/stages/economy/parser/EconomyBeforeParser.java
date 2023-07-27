package com.acvount.server.log.handle.stage.stages.economy.parser;

import com.acvount.server.log.api.ecnonmy.domain.EconomyLog;
import com.acvount.server.log.economy.mapper.EconomyLogMapper;
import com.acvount.server.log.economy.service.EconomyLogService;
import com.acvount.server.log.lose.service.LoseLogService;
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
 * description : 金额日志 发生之前数据记录
 **/

@Slf4j
@Component
public class EconomyBeforeParser implements EconomyParser {
    @Resource
    private EconomyLogService economyLogService;

    @Resource
    private LoseLogService loseLogService;

//    2023.07.25-14.17.21: [trade] before purchasing tradeales from trader z_3_armory, player 8g272r(76561199339471681) had 0 cash, 12391 account balance and 0 gold and trader had 100000 funds.
    private static final String LOG_PATTERN = "(?<loggerDate>\\d|.*?):.*?before (?<type>.*?) .*? trader(?<trader>[^,]+)_(?<region>\\w+).*?player(?<playerName>.*?)\\((?<playerSteamID>\\d*)\\).*?(?<beforeCash>\\d.*?)cash,.*?(?<beforeAccount>\\d.*?)account.*?(?<beforeGold>\\d).*?(?<beforeTraderMoney>\\d+).";
    private static final Pattern logPattern = Pattern.compile(LOG_PATTERN);

    @Override
    @SuppressWarnings("all")
    public void consumer(String logText, Long serverId) {
        Matcher matcher = logPattern.matcher(logText);
        if (matcher.find()) {
            EconomyLog economyLog = new EconomyLog();
            economyLog.setLogTime(parseLogTime(matcher.group(1)));
            economyLog.setTrader(matcher.group("trader"));
            economyLog.setType(matcher.group("type").trim().replace("selling", "sell").replace("purchasing", "purchase"));
            economyLog.setRegion(matcher.group("region"));
            economyLog.setPlayerName(matcher.group("playerName"));
            economyLog.setPlayerSteamId(matcher.group("playerSteamID"));
            economyLog.setBeforeCash(matcher.group("beforeCash"));
            economyLog.setBeforeAccount(matcher.group("beforeAccount"));
            economyLog.setBeforeGold(matcher.group("beforeGold"));
            economyLog.setBeforeTraderMoney(matcher.group("beforeTraderMoney"));
            economyLog.setServerId(serverId);
            economyLogService.insertOrUpdate(economyLog);
            log.info("consumer economy before player steam:{} server:{}", economyLog.getPlayerSteamId(), economyLog.getServerId());
        } else {
            loseLogService.addLoseLog(serverId, logText, "economy_before");
        }

    }


}