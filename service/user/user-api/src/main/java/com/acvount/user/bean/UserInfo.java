package com.acvount.user.bean;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author : acfan
 * date : create in 2023/7/1 19:24
 * description :
 **/

@Data
@Builder
public class UserInfo implements Serializable {
    private Long userId;
    private Long steamId;
    private String nickName;
    private boolean gender;
    private String avatar;
    private Boolean disableFlag;
    private LocalDateTime createTime;
    private LocalDateTime modifyTime;
}
