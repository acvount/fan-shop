package com.acvount.server.bean;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author : acfan
 * date : create in 2023/7/29 15:40
 * description :
 **/

@Data
public class ServerFtp {
    private Long id;
    private Long serverId;
    private Long ownerId;
    private String ip;
    private Integer port;
    private String username;
    private String password;
    private LocalDateTime createTime;
}
