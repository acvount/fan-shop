package com.acvount.server.bean;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author : acfan
 * date : create in 2023/7/29 15:35
 * description :
 **/

@Data
public class ServerConfig {
    private Long id;
    private Long serverId;
    private Long publicConfigId;
    private Boolean usePublicFlag;
    private Integer valueType;
    private String configValue;
    private Long createBy;
    private LocalDateTime createTime;
    private Long modifyBy;
    private LocalDateTime modifyTime;
}
