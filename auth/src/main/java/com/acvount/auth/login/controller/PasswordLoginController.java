package com.acvount.auth.login.controller;

import com.acvount.auth.login.bean.vo.LoginVO;
import com.acvount.auth.login.service.login.PasswordLoginService;
import com.acvount.common.core.exception.BaseException;
import com.acvount.common.core.result.Result;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : acfan
 * date : create in 2023/7/9 22:43
 * description : 密码登陆 Controller
 **/

@RestController
@RequestMapping("login/password")
public class PasswordLoginController {

    @Resource
    private PasswordLoginService passwordLoginService;

    @PostMapping("check")
    public Result<String> checkPassword(@RequestBody LoginVO loginVO) throws BaseException {
        return Result.ok(passwordLoginService.passwordLogin(loginVO));
    }
}
