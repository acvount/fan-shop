package com.acvount.auth.password.controller;

import com.acvount.auth.password.service.PasswordService;
import com.acvount.common.core.exception.BaseException;
import com.acvount.common.core.result.Result;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : acfan
 * date : create in 2023/7/31 13:43
 * description :
 **/

@RestController("password")
public class PasswordController {

    @Resource
    private PasswordService passwordService;

    @PutMapping("change-password")
    public Result<Boolean> changePassword(String newPassword) throws BaseException {
        return Result.success(passwordService.changePassword(newPassword));
    }
}
