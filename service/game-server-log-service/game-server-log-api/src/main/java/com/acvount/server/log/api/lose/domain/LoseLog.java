package com.acvount.server.log.api.lose.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author : acfan
 * date : create in 2023/7/27 15:44
 * description :
 **/

@Data
@TableName("lose_log")
public class LoseLog {
    private Long id;
    private Long serverId;
    private String type;
    private String logContext;
    private LocalDateTime createTime;

}
