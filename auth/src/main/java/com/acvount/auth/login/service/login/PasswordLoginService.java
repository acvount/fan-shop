package com.acvount.auth.login.service.login;

import com.acvount.auth.login.bean.vo.LoginVO;
import com.acvount.auth.login.service.password.strategy.PasswordEncryptionStrategy;
import com.acvount.common.core.exception.BaseException;
import com.acvount.common.core.exception.config.ExceptionType;
import com.acvount.user.api.UserServiceApi;
import com.acvount.user.bean.UserAuthorization;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author : acfan
 * date : create in 2023/7/9 21:54
 * description : 密码登陆
 **/

@Service
public class PasswordLoginService {

    private final static String REDIS_KEY_PREFIX = "login_password_";
    private final static String ERROR_COUNT_KEY = "error_count";
    private final static String FROZEN_FLAG_KEY = "frozen_flag";
    private final static Integer MAX_ERROR_COUNT = 10;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Resource
    private UserServiceApi userServiceApi;

    @Resource
    private LoginService loginService;


    public String passwordLogin(LoginVO loginVO) throws BaseException {
        String redisKey = REDIS_KEY_PREFIX + loginVO.getPhone();
        Boolean frozenFlag = (Boolean) redisTemplate.opsForHash().get(redisKey, FROZEN_FLAG_KEY);
        Integer errorCount = (Integer) redisTemplate.opsForHash().get(redisKey, ERROR_COUNT_KEY);
        if (Boolean.TRUE.equals(frozenFlag) || (errorCount != null && errorCount >= MAX_ERROR_COUNT)) {
            if (!Boolean.TRUE.equals(frozenFlag)) {
                redisTemplate.opsForHash().put(redisKey, FROZEN_FLAG_KEY, true);
            }
            throw new BaseException(ExceptionType.User_Password_Error_Count_Gt_10);
        }

        UserAuthorization userAuthorizationByMobile = userServiceApi.getUserAuthorizationByMobile(loginVO.getPhone());
        if (PasswordEncryptionStrategy.getStrategy(userAuthorizationByMobile.getEncryptionType()).checkPassword(loginVO.getPassword(), userAuthorizationByMobile.getSalt(), userAuthorizationByMobile.getPasswordHash())) {
            return loginService.login(userAuthorizationByMobile.getUserId());
        }
        addErrorCount(redisKey, errorCount);
        throw new BaseException(ExceptionType.User_Password_Error);
    }

    private void addErrorCount(String redisKey, Integer errorCount) {
        errorCount = errorCount == null ? 1 : errorCount + 1;
        redisTemplate.opsForHash().put(redisKey, ERROR_COUNT_KEY, errorCount);
    }
}
