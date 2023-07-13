package com.acvount.sms.code.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author : acfan
 * date : create in 2023/7/1 13:09
 * description :
 **/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendPhoneCodeSmsDTO implements Serializable {
    private String phone;
    private String signName;
    private String templateCode;
    private String code;
    private String outId;

}
