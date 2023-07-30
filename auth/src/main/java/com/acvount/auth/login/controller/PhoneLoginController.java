package com.acvount.auth.login.controller;

import com.acvount.auth.login.bean.vo.LoginVO;
import com.acvount.auth.login.service.login.PhoneLoginService;
import com.acvount.common.core.exception.BaseException;
import com.acvount.common.core.result.Result;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * @author : acfan
 * date : create in 2023/7/1 12:05
 * description :
 **/

@RestController
@RequestMapping("/login/phone")
public class PhoneLoginController {

    @Resource
    private PhoneLoginService phoneLoginService;

    @GetMapping("sendCode")
    public Result<Boolean> sendCode(@RequestParam("phone") String phone) {
        return Result.success(phoneLoginService.sendCode(phone));
    }

    @PostMapping("checkCode")
    public Result<String> checkCode(@RequestBody LoginVO loginVO) throws BaseException {
        return Result.success(phoneLoginService.checkCode(loginVO));
    }
}
