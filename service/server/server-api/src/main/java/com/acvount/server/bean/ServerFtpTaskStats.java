package com.acvount.server.bean;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author : acfan
 * date : create in 2023/7/29 15:41
 * description :
 **/

@Data
@TableName("server_ftp_task_stats")
public class ServerFtpTaskStats {
    private Long ftpId;
    private String threadId;
    private LocalDateTime createTime;
    private String metadata;
    private LocalDateTime modifyTime;
}
