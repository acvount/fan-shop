package com.acvount.user.bean;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author : acfan
 * date : create in 2023/7/1 19:27
 * description :
 **/

@Data
@Builder
public class UserAuthorization implements Serializable {
    private Long userId;
    private String passwordHash;
    private String loginPhone;
    private String salt;
    private String encryptionType;
    private LocalDateTime createTime;
    private LocalDateTime modifyTime;
}
