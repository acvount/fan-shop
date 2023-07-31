package com.acvount.user.service;

import com.acvount.user.bean.UserInfo;
import com.acvount.user.mapper.UserInfoMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @author : acfan
 * date : create in 2023/7/30 17:51
 * description :
 **/

@Service
public class UserService {
    @Resource
    private UserInfoMapper userInfoMapper;

    public void updateUserById(UserInfo userInfo) {
        userInfoMapper.updateById(userInfo);
    }

    public UserInfo selectById(Long userId) {
        return userInfoMapper.selectById(userId);
    }
}
