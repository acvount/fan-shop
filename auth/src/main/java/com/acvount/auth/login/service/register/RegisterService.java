package com.acvount.auth.login.service.register;

import com.acvount.auth.login.service.password.strategy.PasswordEncryptionStrategy;
import com.acvount.common.core.id.SnowflakeIdGenerator;
import com.acvount.user.api.UserServiceApi;
import com.acvount.user.bean.UserAuthorization;
import com.acvount.user.bean.UserInfo;
import io.micrometer.common.util.StringUtils;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

/**
 * @author : acfan
 * date : create in 2023/7/9 21:55
 * description :
 **/

@Service
public class RegisterService {

    @Resource
    private SnowflakeIdGenerator snowflakeIdGenerator;

    @DubboReference()
    @SuppressWarnings("unused")
    private UserServiceApi userServiceApi;

    public UserInfo createAccount(String phone) {
        return createAccount(phone, Strings.EMPTY);
    }

    public UserInfo createAccount(String phone, String password) {
        long userId = snowflakeIdGenerator.nextId();
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(userId);
        UserAuthorization userAuthorization;
        userAuthorization = StringUtils.isNotBlank(phone) ? generPhoneCreateAccount(phone) : generPasswordCreateAccount(phone, password);
        return userServiceApi.createUser(userInfo, userAuthorization);
    }

    private UserAuthorization generPasswordCreateAccount(String phone, String password) {
        UserAuthorization userAuthorization = generPhoneCreateAccount(phone);
        PasswordEncryptionStrategy strategy = PasswordEncryptionStrategy.getStrategy();
        userAuthorization.setEncryptionType(strategy.getEncryptionType());
        String[] result = strategy.encryptPassword(password);
        userAuthorization.setSalt(result[0]);
        userAuthorization.setPasswordHash(result[1]);
        return userAuthorization;
    }

    private UserAuthorization generPhoneCreateAccount(String phone) {
        return UserAuthorization.builder().loginPhone(phone).build();
    }
}
