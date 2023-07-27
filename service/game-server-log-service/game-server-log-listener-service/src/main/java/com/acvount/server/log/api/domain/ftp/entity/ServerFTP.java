package com.acvount.server.log.api.domain.ftp.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author : acfan
 * date : create in 2023/7/22 17:06
 * description :
 **/

@Data
@TableName("server_ftp")
public class ServerFTP implements Serializable {
    private Long id;
    private Long serverId;
    private Long ownerId;
    private String ip;
    private Integer port;
    private String username;
    private String password;
    private LocalDateTime createTime;

}
