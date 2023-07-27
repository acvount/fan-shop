package com.acvount.server.log.handle.stage.stages.economy.parser;

import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author : acfan
 * date : create in 2023/7/27 13:30
 * description :
 **/

public class TestStatic {

    public static void main(String[] args) {
        String logText = "2023.07.25-14.16.50: [trade] after tradeable sale to trader z_3_armory, player 比奇堡粉红星星(76561198376789206) has 8469 cash, 244680 account balance and 5 gold and trader has 100000 funds.";
        String pattern = "(\\d{4}\\.\\d{2}\\.\\d{2}-\\d{2}\\.\\d{2}\\.\\d{2}): \\[Trade] After tradeable sale to trader (?<trader>.*?), player (?<playerName>.*?)\\((?<playerSteamID>\\d+)\\) has (?<afterCash>\\d+) cash, (?<afterAccount>\\d+) account balance and (?<afterGold>\\d+) gold and trader has (?<afterTraderMoney>\\d+) funds\\.";

        Pattern logPattern = Pattern.compile(pattern);
        Matcher matcher = logPattern.matcher(logText);

        if (matcher.find()) {
            String date = matcher.group(1);
            String trader = matcher.group("trader");
            String playerName = matcher.group("playerName");
            String playerSteamID = matcher.group("playerSteamID");
            String afterCash = matcher.group("afterCash");
            String afterAccount = matcher.group("afterAccount");
            String afterGold = matcher.group("afterGold");
            String afterTraderMoney = matcher.group("afterTraderMoney");

            // 打印匹配结果
            System.out.println("Date: " + date);
            System.out.println("Trader: " + trader);
            System.out.println("Player Name: " + playerName);
            System.out.println("Player SteamID: " + playerSteamID);
            System.out.println("After Cash: " + afterCash);
            System.out.println("After Account: " + afterAccount);
            System.out.println("After Gold: " + afterGold);
            System.out.println("After Trader Money: " + afterTraderMoney);
        } else {
            System.out.println("No match found.");
        }
    }
}
