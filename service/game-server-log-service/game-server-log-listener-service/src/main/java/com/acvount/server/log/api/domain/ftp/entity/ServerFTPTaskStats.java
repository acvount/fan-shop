package com.acvount.server.log.api.domain.ftp.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author : acfan
 * date : create in 2023/7/23 12:23
 * description :
 **/

@Data
@TableName("server_ftp_task_stats")
public class ServerFTPTaskStats {
    @TableId
    private Long ftpId;
    private String threadId;
    private LocalDateTime createTime;
    private LocalDateTime modifyTime;
    private String metadata;
}
