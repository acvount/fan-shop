package com.acvount.user.controller;

import com.acvount.common.core.exception.BaseException;
import com.acvount.common.core.result.Result;
import com.acvount.user.bean.UserInfo;
import com.acvount.user.service.UserInfoService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

    @PostMapping("change-header-image")
    public Result<UserInfo> uploadHeadImage(@RequestBody MultipartFile file) throws BaseException {
        return userInfoService.changeHeaderImage(file);
    }

}
