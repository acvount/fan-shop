package com.acvount.server.log.handle.stage.stages.economy.parser;

import com.acvount.server.log.api.ecnonmy.domain.EconomyLog;
import com.acvount.server.log.economy.service.EconomyLogService;
import com.acvount.server.log.lose.service.LoseLogService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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
    private EconomyLogService economyLogService;

    @Resource
    private LoseLogService loseLogService;

    private static final String LOG_PATTERN = "(?<loggerDate>\\d|.*?):.*?tradeable(?<type>.*?)to trader(?<trader>[^,]+)_(?<region>\\w+).*?player(?<playerName>.*?)\\((?<playerSteamID>\\d*)\\).*?(?<afterCash>\\d.*?)cash,.*?(?<afterAccount>\\d.*?)account.*?(?<afterGold>\\d).*?(?<afterTraderMoney>\\d+).";
    private static final Pattern logPattern = Pattern.compile(LOG_PATTERN);

    @Override
    @SuppressWarnings("all")
    public void consumer(String logText, Long serverId) {
        Matcher matcher = logPattern.matcher(logText);
        if (matcher.find()) {
            EconomyLog economyLog = new EconomyLog();
            economyLog.setLogTime(parseLogTime(matcher.group(1)));
            economyLog.setTrader(matcher.group("trader"));
            economyLog.setType(matcher.group("type").trim());
            economyLog.setRegion(matcher.group("region"));
            economyLog.setPlayerName(matcher.group("playerName"));
            economyLog.setPlayerSteamId(matcher.group("playerSteamID"));
            economyLog.setAfterCash(matcher.group("afterCash"));
            economyLog.setAfterAccount(matcher.group("afterAccount"));
            economyLog.setAfterGold(matcher.group("afterGold"));
            economyLog.setAfterTraderMoney(matcher.group("afterTraderMoney"));
            economyLog.setServerId(serverId);
            economyLogService.insertOrUpdate(economyLog);
            log.info("consumer economy after player steam:{} server:{}", economyLog.getPlayerSteamId(), economyLog.getServerId());
        } else {
            loseLogService.addLoseLog(serverId, logText, "economy_after");
        }
    }


}
