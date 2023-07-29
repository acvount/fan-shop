package com.acvount.server.bean;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author : acfan
 * date : create in 2023/7/29 15:42
 * description :
 **/

@Data
public class ServerItemPrice {
    private Long id;
    private Long itemId;
    private Long serverId;
    private BigDecimal itemPrice;
}
