package com.acvount.user.service;

import com.acvount.user.bean.UserAuthorization;
import com.acvount.user.mapper.UserAuthorizationMapper;
import jakarta.annotation.Resource;

/**
 * @author : acfan
 * date : create in 2023/7/31 13:35
 * description :
 **/

@Resource
public class UserAuthorizationService {

    @Resource
    private UserAuthorizationMapper userAuthorizationMapper;

    public UserAuthorization selectById(Long userId) {
        return userAuthorizationMapper.selectById(userId);
    }
}
