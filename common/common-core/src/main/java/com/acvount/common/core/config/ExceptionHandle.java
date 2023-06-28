package com.acvount.common.core.config;

import com.acvount.common.core.exception.BaseException;
import com.acvount.common.core.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : 小凡
 * @date : create in 2021/9/12 11:26
 * description :
 **/
@RestController
@ControllerAdvice
@Slf4j
public class ExceptionHandle {

    @ExceptionHandler({Exception.class})
    public Result<Object> exceptionHandler(Exception e) {
        e.printStackTrace();
        log.error(e.getMessage());
        return Result.fail(e.getMessage());
    }

    @ExceptionHandler({BaseException.class})
    public Result<Object> baseExceptionHandler(BaseException e) {
        log.error(e.getMessage());
        if (e.getException() != null) {
            e.getException().printStackTrace();
        }
        return Result.fail(e.getMessage());
    }
}
