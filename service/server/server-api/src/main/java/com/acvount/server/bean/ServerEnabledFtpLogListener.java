package com.acvount.server.bean;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author : acfan
 * date : create in 2023/7/29 15:39
 * description :
 **/

@Data
public class ServerEnabledFtpLogListener {
    private Long serverId;
    private Long ftpId;
    private Integer taskStatus;
    private LocalDateTime createTime;

}
