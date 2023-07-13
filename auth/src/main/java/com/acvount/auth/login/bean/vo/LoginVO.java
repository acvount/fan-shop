package com.acvount.auth.login.bean.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author : acfan
 * date : create in 2023/7/1 15:35
 * description :
 **/

@Data

public class LoginVO implements Serializable {
    String appid;
    String appversion;
    String imei;
    String sign;
    String phone;
    String password;
    String code;
}
