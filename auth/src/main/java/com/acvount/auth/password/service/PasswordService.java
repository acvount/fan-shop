package com.acvount.auth.password.service;

import com.acvount.auth.login.service.password.strategy.PasswordEncryptionStrategy;
import com.acvount.common.core.exception.BaseException;
import com.acvount.user.api.UserService;
import com.acvount.user.bean.UserAuthorization;
import com.acvount.web.LoginUser;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

/**
 * @author : acfan
 * date : create in 2023/7/31 13:44
 * description :
 **/

@Service
public class PasswordService {

    @DubboReference
    @SuppressWarnings("unused")
    private UserService userService;

    public Boolean changePassword(String newPassword) throws BaseException {
        UserAuthorization userAuthorization = userService.getUserAuthorizationById(LoginUser.getLoginUserID());
        PasswordEncryptionStrategy strategy = PasswordEncryptionStrategy.getStrategy();
        String[] result = strategy.encryptPassword(newPassword);
        userAuthorization.setSalt(result[0]);
        userAuthorization.setPasswordHash(result[1]);
        userAuthorization.setEncryptionType(strategy.getEncryptionType());
        Integer i = userService.updateUserAuthorizationById(userAuthorization);
        return i > 0;
    }

}
