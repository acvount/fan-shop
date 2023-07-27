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
@TableName("economy_item_log")
public class EconomyItemLog {
    @TableId
    private Long id;
    private Long serverId;
    private Long economyId;
    private String item;
    private String cnItemName;
    private Integer tradeCount;
    private String containInfo;
    private Integer oldCount;
    private Integer newCount;
    private LocalDateTime logTime;
    private LocalDateTime createTime;
}
