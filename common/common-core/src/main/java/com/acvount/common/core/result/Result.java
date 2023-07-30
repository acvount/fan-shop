package com.acvount.common.core.result;

import com.acvount.common.core.result.config.ResultStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;

/**
 * @author : 小凡
 * @date : create in 2021/7/22 15:11
 * description : 返回类
 **/
@Getter
@Builder
@Data
@AllArgsConstructor
public class Result<T> implements Serializable {

    public T data;
    public int code;
    public String msg;

    public Result(T data) {
        ResultStatus resultStatus = ResultStatus.SUCCESS;
        code = resultStatus.code;
        msg = resultStatus.message;
        this.data = data;
    }

    public Result(ResultStatus resultStatus, T data) {
        code = resultStatus.code;
        msg = resultStatus.message;
        this.data = data;
    }

    public static <T> Result<T> success(T data) {
        return Result.<T>builder().code(ResultStatus.SUCCESS.code).msg(ResultStatus.SUCCESS.message).data(data).build();
    }

    public static <T> Result<T> success(T data, String msg) {
        return Result.<T>builder().code(ResultStatus.SUCCESS.code).msg(msg).data(data).build();
    }

    public static <T> Result<T> fail(String msg) {
        return Result.<T>builder().code(ResultStatus.FAIL.code).msg(msg).build();
    }

    public static <T> Result<T> fail() {
        return Result.<T>builder().code(ResultStatus.FAIL.code).msg(ResultStatus.FAIL.message).build();
    }


    public void setData(T data) {
        this.data = data;
    }
}
