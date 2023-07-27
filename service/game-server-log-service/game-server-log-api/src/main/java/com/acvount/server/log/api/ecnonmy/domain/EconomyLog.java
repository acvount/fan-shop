package com.acvount.server.log.api.ecnonmy.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author : acfan
 * date : create in 2023/7/26 21:34
 * description :
 **/

@Data
@TableName("economy_log")
public class EconomyLog {
    @TableId
    private Long id;
    private Long serverId;
    private String trader;
    private String region;
    private String playerSteamId;
    private String playerName;
    private String type;
    private String beforeCash;
    private String beforeAccount;
    private String beforeGold;
    private String beforeTraderMoney;
    private String afterCash;
    private String afterAccount;
    private String afterGold;
    private String afterTraderMoney;
    private Integer currentOnlinePlayer;
    private LocalDateTime logTime;
    private LocalDateTime createTime;
}
