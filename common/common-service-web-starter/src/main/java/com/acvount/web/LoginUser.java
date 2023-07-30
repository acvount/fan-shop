package com.acvount.web;

import com.acvount.common.core.exception.BaseException;
import com.acvount.common.core.exception.config.ExceptionType;
import com.acvount.web.util.SpringContextHolder;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author : acfan
 * date : create in 2023/7/29 23:38
 * description : 静态获取当前登陆者
 **/

@Component
public class LoginUser {
    private static final String TOKEN_NAME = "Authorization";
    private static final String LOGIN_USER_REDIS_PREFIX = TOKEN_NAME + ":login:token:";


    @SuppressWarnings(value = "all")
    public static Long getLoginUserID() throws BaseException {
        try {
            String token = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader(TOKEN_NAME);
            return Long.parseLong(SpringContextHolder.getBean("stringRedisTemplate", RedisTemplate.class).opsForValue().get(LOGIN_USER_REDIS_PREFIX + token).toString());
        } catch (NullPointerException nullPointerException) {
            throw new BaseException(ExceptionType.Token_Not_Found);
        }
    }
}
