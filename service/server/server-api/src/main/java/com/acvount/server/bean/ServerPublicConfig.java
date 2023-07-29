package com.acvount.server.bean;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author : acfan
 * date : create in 2023/7/29 15:43
 * description :
 **/

@Data
public class ServerPublicConfig {
    private Long id;
    private String configName;
    private String configValue;
    private String configCnName;
    private LocalDateTime createTime;
    private Long createBy;
}
