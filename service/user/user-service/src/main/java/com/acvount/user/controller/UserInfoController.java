package com.acvount.user.controller;

import com.acvount.common.core.exception.BaseException;
import com.acvount.common.core.result.Result;
import com.acvount.user.bean.UserInfo;
import com.acvount.user.service.UserInfoService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author : acfan
 * date : create in 2023/7/30 11:39
 * description :
 **/

@RestController
@RequestMapping("info")
public class UserInfoController {

    @Resource
    private UserInfoService userInfoService;

    @GetMapping("get-detail")
    public Result<UserInfo> getUserDetail() throws BaseException {
        return Result.success(userInfoService.getUserDetail());
    }

    @PostMapping("change-header-image")
    public Result<UserInfo> uploadHeadImage(@RequestBody MultipartFile file) throws BaseException {
        return userInfoService.changeHeaderImage(file);
    }

}
