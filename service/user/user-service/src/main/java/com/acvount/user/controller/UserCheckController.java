package com.acvount.user.controller;

import com.acvount.common.core.exception.BaseException;
import com.acvount.common.core.result.Result;
import com.acvount.user.service.UserCheckService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : acfan
 * date : create in 2023/7/30 11:39
 * description :
 **/

@RestController
@RequestMapping("guide")
public class UserCheckController {

    @Resource
    private UserCheckService userCheckService;

    @GetMapping("is-need-renew-password-check")
    public Result<Boolean> checkIsNeedRenewPassword() throws BaseException {
        return Result.success(userCheckService.checkLoginUserIsNeedRenewPassword());
    }

}
