package com.acvount.user.bean;

import com.baomidou.mybatisplus.annotation.TableId;
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
    @TableId
    private Long userId;
    private Long steamId;
    private String nickName;
    private boolean gender;
    private String avatar;
    private String thumbnailAvatar;
    private Boolean disableFlag;
    private Boolean serverOwnerFlag;
    private LocalDateTime createTime;
    private LocalDateTime modifyTime;
}
