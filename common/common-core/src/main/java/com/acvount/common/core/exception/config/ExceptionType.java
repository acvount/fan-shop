package com.acvount.common.core.exception.config;

import java.io.Serializable;

/**
 * @author : 小凡
 * @date : create in 2021/7/22 14:48
 * description :
 **/
public enum ExceptionType implements Serializable {

    /***
     * 参数
     */
    Parameter_Error(10100, "入参错误"),


    /***
     * Token
     */
    Token_Invalid(10200, "Token无效"),
    Token_Not_Found(10201, "缺失Token | 未登录"),
    Token_Expire(10202,"token过期" ),

    Code_Not_Found(10300,"请先发送验证码"),
    Code_Mismatch(10301,"验证码不匹配"),
    Code_Error_Count_Gt_5(10302,"验证码错误五次"),
    Code_Error_Time_Out(10303,"验证码超时"),

    User_Password_Error_Count_Gt_10(10400,"用户密码错误次数过多已被暂时禁止登陆"),

    User_Password_Error(10401, "用户密码错误，最大重试次数为10次");

    private int code;
    private String message;

    ExceptionType(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public ExceptionType setMessage(String message) {
        this.message = message;
        return this;
    }


}
