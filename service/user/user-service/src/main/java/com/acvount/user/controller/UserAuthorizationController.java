package com.acvount.user.controller;

import com.acvount.common.core.result.Result;
import com.acvount.user.bean.UserInfo;
import com.acvount.user.service.UserAuthorizationService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : acfan
 * date : create in 2023/7/31 13:38
 * description :
 **/

@RestController
@RequestMapping("authorization")
public class UserAuthorizationController {

    @Resource
    private UserAuthorizationService userAuthorizationService;
}
