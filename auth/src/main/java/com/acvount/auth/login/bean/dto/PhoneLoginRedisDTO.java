package com.acvount.auth.login.bean.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author : acfan
 * date : create in 2023/7/1 18:57
 * description :
 **/

@Data
@Builder
@AllArgsConstructor
public class PhoneLoginRedisDTO implements Serializable {

    private String code;
    private Integer errorCount;
    private Long createTimestamp;
}
