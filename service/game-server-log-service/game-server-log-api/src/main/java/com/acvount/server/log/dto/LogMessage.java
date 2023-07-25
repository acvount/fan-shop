package com.acvount.server.log.dto;

import lombok.Data;

/**
 * @author : acfan
 * date : create in 2023/7/24 20:55
 * description :
 **/

@Data
public class LogMessage {

    private Long ftpId;
    private Long serverId;
    private Long ownerId;
    private String type;
    private String content;
}
