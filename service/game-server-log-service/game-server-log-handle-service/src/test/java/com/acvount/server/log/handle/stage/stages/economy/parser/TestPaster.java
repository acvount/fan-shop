//package com.acvount.server.log.handle.stage.stages.economy.parser;
//
//import com.acvount.server.log.dto.LogMessage;
//import com.acvount.server.log.handle.stage.stages.economy.EconomyStage;
//import jakarta.annotation.Resource;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
///**
// * @author : acfan
// * date : create in 2023/7/27 13:06
// * description :
// **/
//
//
//@SpringBootTest
//public class TestPaster {
//
//
//    @Resource
//    private EconomyStage economyStage;
//
//
//    @Test
//    public void testEconomyStage() {
//
//        LogMessage logMessage = new LogMessage();
//
//        logMessage.setServerId(1L);
//        String content = "\n2023.07.25-14.16.07: [Trade] Before selling tradeables to trader Z_3_Armory, player 比奇堡粉红星星(76561198376789206) had 4213 cash, 244680 account balance and 5 gold and trader had 100000 funds." +
//                "\n2023.07.25-14.16.42: [Trade] Before selling tradeables to trader Z_3_Hospital, player yukikiki(76561198301050685) had 0 cash, 226097 account balance and 0 gold and trader had 100000 funds." +
//                "\n2023.07.25-14.16.50: [Trade] Before selling tradeables to trader Z_3_Armory, player 比奇堡粉红星星(76561198376789206) had 5710 cash, 244680 account balance and 5 gold and trader had 100000 funds." +
//                "\n2023.07.25-14.16.51: [Trade] Before selling tradeables to trader Z_3_Trader, player Mi(76561198449849129) had 11063 cash, 141896 account balance and 0 gold and trader had 100000 funds." +
//                "\n2023.07.25-14.17.00: [Trade] Before selling tradeables to trader Z_3_Trader, player Mi(76561198449849129) had 11063 cash, 142392 account balance and 0 gold and trader had 100000 funds." +
//                "\n2023.07.25-14.17.00: [Trade] Before selling tradeables to trader Z_3_Armory, player 比奇堡粉红星星(76561198376789206) had 8469 cash, 244680 account balance and 5 gold and trader had 100000 funds." +
//                "\n2023.07.25-14.17.21: [Trade] Before purchasing tradeales from trader Z_3_Armory, player 8g272r(76561199339471681) had 0 cash, 12391 account balance and 0 gold and trader had 100000 funds."+
//                "\n2023.07.25-14.16.07: [Trade] After tradeable sale to trader Z_3_Armory, player 比奇堡粉红星星(76561198376789206) has 5710 cash, 244680 account balance and 5 gold and trader has 100000 funds." +
//                "\n2023.07.25-14.16.42: [Trade] After tradeable sale to trader Z_3_Hospital, player yukikiki(76561198301050685) has 0 cash, 226197 account balance and 0 gold and trader has 100000 funds." +
//                "\n2023.07.25-14.16.50: [Trade] After tradeable sale to trader Z_3_Armory, player 比奇堡粉红星星(76561198376789206) has 8469 cash, 244680 account balance and 5 gold and trader has 100000 funds." +
//                "\n2023.07.25-14.16.51: [Trade] After tradeable sale to trader Z_3_Trader, player Mi(76561198449849129) has 11063 cash, 142392 account balance and 0 gold and trader has 100000 funds." +
//                "\n2023.07.25-14.17.00: [Trade] After tradeable sale to trader Z_3_Trader, player Mi(76561198449849129) has 11063 cash, 143363 account balance and 0 gold and trader has 100000 funds." +
//                "\n2023.07.25-14.17.00: [Trade] After tradeable sale to trader Z_3_Armory, player 比奇堡粉红星星(76561198376789206) has 9465 cash, 244680 account balance and 5 gold and trader has 100000 funds." +
//                "\n2023.07.25-14.17.21: [Trade] After tradeable purchase from trader Z_3_Armory, player 8g272r(76561199339471681) has 0 cash, 8991 bank account balance and 0 gold and trader has 100000 funds.";
//        logMessage.setContent(content);
//        logMessage.setType("economy");
//        economyStage.consumer(logMessage);
//    }
//    /*
//    *       id 1
//        trader Armory
//        region Z_3
//        playerSteamID 76561198376789206
//        playerName 比奇堡粉红星星
//        type sale
//        before_cash 5710
//        before_account  244680
//        before_gold 5
//        before_trader_money 100000
//        items [
//                {
//                "item":"Drill",
//                "cn_item_name":"钻头",
//                "count":"2",
//                "contain_info":"for 546 (546 + 0 worth of contained items)"
//                },{
//                "item":"MetalDetector",
//                "cn_item_name":"钻头",
//                "count":"2",
//                "contain_info":"for 546 (546 + 0 worth of contained items)"
//                },{
//                "item":"Drill",
//                "cn_item_name":"钻头",
//                "count":"2",
//                "contain_info":"for 546 (546 + 0 worth of contained items)"
//                },
//              ]
//        after_cash 5710,
//        after_account  244680,
//        after_gold 5,
//        after_trader_money 100000,
//        current_online_count 5,
//        log_time:"2023.07.26-10.01.33",
//        create_time:"2023.07.26 21:26:00"
//
//"\n" +
//"\n2023.07.25-14.16.07: [Trade] Before selling tradeables to trader Z_3_Armory, player 比奇堡粉红星星(76561198376789206) had 4213 cash, 244680 account balance and 5 gold and trader had 100000 funds." +
//"\n2023.07.25-14.16.42: [Trade] Before selling tradeables to trader Z_3_Hospital, player yukikiki(76561198301050685) had 0 cash, 226097 account balance and 0 gold and trader had 100000 funds." +
//"\n2023.07.25-14.16.50: [Trade] Before selling tradeables to trader Z_3_Armory, player 比奇堡粉红星星(76561198376789206) had 5710 cash, 244680 account balance and 5 gold and trader had 100000 funds." +
//"\n2023.07.25-14.16.51: [Trade] Before selling tradeables to trader Z_3_Trader, player Mi(76561198449849129) had 11063 cash, 141896 account balance and 0 gold and trader had 100000 funds." +
//"\n2023.07.25-14.17.00: [Trade] Before selling tradeables to trader Z_3_Trader, player Mi(76561198449849129) had 11063 cash, 142392 account balance and 0 gold and trader had 100000 funds." +
//"\n2023.07.25-14.17.00: [Trade] Before selling tradeables to trader Z_3_Armory, player 比奇堡粉红星星(76561198376789206) had 8469 cash, 244680 account balance and 5 gold and trader had 100000 funds." +
//"\n2023.07.25-14.17.21: [Trade] Before purchasing tradeales from trader Z_3_Armory, player 8g272r(76561199339471681) had 0 cash, 12391 account balance and 0 gold and trader had 100000 funds." +
//
//* "\n2023.07.25-14.16.07: [Trade] Tradeable (Weapon_AK15 (x1) sold by 比奇堡粉红星星(76561198376789206) for 1497 (1497 + 0 worth of contained items) to trader Z_3_Armory, old amount in store is -1, new amount is -1, and effective users online: 5" +
//"\n2023.07.25-14.16.42: [Trade] Tradeable (Isopropyl_alcohol (x1)) sold by yukikiki(76561198301050685) for 100 (100 + 0 worth of contained items) to trader Z_3_Hospital, old amount in store is -1, new amount is -1, and effective users online: 3" +
//"\n2023.07.25-14.16.50: [Trade] Tradeable (Weapon_RPK-74 (x1)) sold by 比奇堡粉红星星(76561198376789206) for 1668 (1668 + 0 worth of contained items) to trader Z_3_Armory, old amount in store is -1, new amount is -1, and effective users online: 3" +
//"\n2023.07.25-14.16.50: [Trade] Tradeable (M1_Medical_Helmet (x1)) sold by 比奇堡粉红星星(76561198376789206) for 245 (245 + 0 worth of contained items) to trader Z_3_Armory, old amount in store is -1, new amount is -1, and effective users online: 3" +
//"\n2023.07.25-14.16.51: [Trade] Tradeable (Tool_Box_Small (x3)) sold by Mi(76561198449849129) for 257 (257 + 0 worth of contained items) to trader Z_3_Trader, old amount in store is -1, new amount is -1, and effective users online: 3" +
//"\n2023.07.25-14.16.51: [Trade] Tradeable (Gun_Powder (x1)) sold by Mi(76561198449849129) for 13 (13 + 0 worth of contained items) to trader Z_3_Trader, old amount in store is -1, new amount is -1, and effective users online: 3" +
//"\n2023.07.25-14.16.51: [Trade] Tradeable (Raincoat_02 (x1)) sold by Mi(76561198449849129) for 121 (121 + 0 worth of contained items) to trader Z_3_Trader, old amount in store is -1, new amount is -1, and effective users online: 3" +
//"\n2023.07.25-14.16.51: [Trade] Tradeable (Raincoat_01 (x1)) sold by Mi(76561198449849129) for 105 (105 + 0 worth of contained items) to trader Z_3_Trader, old amount in store is -1, new amount is -1, and effective users online: 3" +
//"\n2023.07.25-14.17.00: [Trade] Tradeable (Chainsaw (x1)) sold by Mi(76561198449849129) for 971 (971 + 0 worth of contained items) to trader Z_3_Trader, old amount in store is -1, new amount is -1, and effective users online: 3" +
//"\n2023.07.25-14.17.00: [Trade] Tradeable (Weapon_98k_Karabiner (x1)) sold by 比奇堡粉红星星(76561198376789206) for 996 (996 + 0 worth of contained items) to trader Z_3_Armory, old amount in store is -1, new amount is -1, and effective users online: 3" +
//"\n2023.07.25-14.17.21: [Trade] Tradeable (WeaponScope_ACOG_01 (x1)) purchased by 8g272r(76561199339471681) for 3400 money from trader Z_3_Armory, old amount in store was -1, new amount is -1, and effective users online: 3" +
//"\n2023.07.25-14.16.50: [Trade] Tradeable (Weapon_MosinNagant (x1)) sold by 比奇堡粉红星星(76561198376789206) for 846 (846 + 0 worth of contained items) to trader Z_3_Armory, old amount in store is -1, new amount is -1, and effective users online: 3" +
//
//
//
//"\n2023.07.25-14.16.07: [Trade] After tradeable sale to trader Z_3_Armory, player 比奇堡粉红星星(76561198376789206) has 5710 cash, 244680 account balance and 5 gold and trader has 100000 funds." +
//"\n2023.07.25-14.16.42: [Trade] After tradeable sale to trader Z_3_Hospital, player yukikiki(76561198301050685) has 0 cash, 226197 account balance and 0 gold and trader has 100000 funds." +
//"\n2023.07.25-14.16.50: [Trade] After tradeable sale to trader Z_3_Armory, player 比奇堡粉红星星(76561198376789206) has 8469 cash, 244680 account balance and 5 gold and trader has 100000 funds." +
//"\n2023.07.25-14.16.51: [Trade] After tradeable sale to trader Z_3_Trader, player Mi(76561198449849129) has 11063 cash, 142392 account balance and 0 gold and trader has 100000 funds." +
//"\n2023.07.25-14.17.00: [Trade] After tradeable sale to trader Z_3_Trader, player Mi(76561198449849129) has 11063 cash, 143363 account balance and 0 gold and trader has 100000 funds." +
//"\n2023.07.25-14.17.00: [Trade] After tradeable sale to trader Z_3_Armory, player 比奇堡粉红星星(76561198376789206) has 9465 cash, 244680 account balance and 5 gold and trader has 100000 funds." +
//"\n2023.07.25-14.17.21: [Trade] After tradeable purchase from trader Z_3_Armory, player 8g272r(76561199339471681) has 0 cash, 8991 bank account balance and 0 gold and trader has 100000 funds."
//
//    *
//    *
//    *
//    * */
//
//}
