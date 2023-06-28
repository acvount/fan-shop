package com.acvount.common.core.result.config;

/**
 * @author : 小凡
 * @date : create in 2021/7/22 15:14
 * description :
 **/
public enum ResultStatus {

    /***
     *  成功
     */
    SUCCESS(200, "成功"),
    FAIL(500, "失败");

    public int code;
    public String message;

    ResultStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }


    public ResultStatus setMessage(String message) {
        this.message = message;
        return this;
    }
}
