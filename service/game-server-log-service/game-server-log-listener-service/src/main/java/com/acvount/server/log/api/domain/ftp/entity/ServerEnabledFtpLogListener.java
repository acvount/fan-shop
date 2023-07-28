package com.acvount.server.log.api.domain.ftp.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author : acfan
 * date : create in 2023/7/28 23:44
 * description :
 **/

@Data
@TableName("server_enabled_ftp_log_listener")
public class ServerEnabledFtpLogListener {
    private Long serverId;
    private Long ftpId;
    private Integer taskStatus;
    private LocalDateTime createTime;
}
