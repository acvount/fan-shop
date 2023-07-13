package com.acvount.gateway.config;

import cn.dev33.satoken.reactor.filter.SaReactorFilter;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : acfan
 * date : create in 2023/7/6 22:01
 * description :
 **/

@Configuration
@Slf4j
public class SaTokenConfigure {
    private static final String[] INCLUDE_PATH = {"/**"};

    private static final String[] EXCLUDE_PATH = {
            "/auth-service/login/phone/checkCode",
            "/auth-service/login/phone/sendCode"
    };

    @Bean
    public SaReactorFilter getSaReactorFilter() {
        return new SaReactorFilter()
                .addInclude(INCLUDE_PATH)
                .addExclude(EXCLUDE_PATH)
                .setAuth(obj -> {
                    SaRouter.match("/**", StpUtil::checkLogin);
                    SaRouter.match("/user-service/**", r -> StpUtil.checkPermission("user"));
                })
                .setError(e -> {
                    log.error("😭sa-token error ->：{}", e.getMessage());
                    return SaResult.error(e.getMessage());
                });
    }
}
