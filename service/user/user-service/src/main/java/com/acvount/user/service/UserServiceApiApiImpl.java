package com.acvount.user.service;

import com.acvount.user.api.UserServiceApi;
import com.acvount.user.bean.UserAuthorization;
import com.acvount.user.bean.UserInfo;
import com.acvount.user.mapper.UserAuthorizationMapper;
import com.acvount.user.mapper.UserInfoMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author : acfan
 * date : create in 2023/7/2 16:33
 * description :
 **/

@DubboService
@SuppressWarnings("unused")
public class UserServiceApiApiImpl implements UserServiceApi {

    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private UserAuthorizationMapper userAuthorizationMapper;

    @Override
    @Transactional
    public UserInfo createUser(UserInfo userInfo, UserAuthorization userAuthorization) {
        userInfoMapper.insert(userInfo);
        userAuthorization.setUserId(userInfo.getUserId());
        userAuthorizationMapper.insert(userAuthorization);
        return userInfo;
    }

    @Override
    public UserInfo getUserByMobile(String phone) {
        return userInfoMapper.selectByPhone(phone);
    }

    @Override
    public UserAuthorization getUserAuthorizationById(Long id) {
        return userAuthorizationMapper.selectById(id);
    }

    @Override
    public UserAuthorization getUserAuthorizationByMobile(String phone) {
        return userAuthorizationMapper.selectOne(Wrappers.lambdaQuery(UserAuthorization.class).eq(UserAuthorization::getLoginPhone, phone));
    }

    @Override
    public Integer modifyServerFlag(Long userId) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(userId);
        userInfo.setServerOwnerFlag(true);
        return userInfoMapper.updateById(userInfo);
    }

    @Override
    public Integer updateUserAuthorizationById(UserAuthorization userAuthorization) {
        return userAuthorizationMapper.updateById(userAuthorization);
    }
}
