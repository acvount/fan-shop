package com.acvount.server.bean;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author : acfan
 * date : create in 2023/7/29 15:33
 * description :
 **/

@Data
public class Server {
    private Long id;
    private String serverName;
    private String serverDescript;
    private Long serverOwner;
    private String area;
    private Integer maxNumber;
    private Integer weight;
    private Boolean expires;
    private Boolean hiddenFlag;
    private Boolean deleteFlag;
    private LocalDateTime createTime;
    private LocalDateTime modifyTime;
}
