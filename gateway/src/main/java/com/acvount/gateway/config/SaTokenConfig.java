package com.acvount.gateway.config;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.context.model.SaRequest;
import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.reactor.filter.SaReactorFilter;
import cn.dev33.satoken.router.SaHttpMethod;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.router.SaRouterStaff;
import cn.dev33.satoken.stp.StpUtil;
import com.acvount.common.core.exception.BaseException;
import com.acvount.common.core.exception.config.ExceptionType;
import com.acvount.common.core.result.Result;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;


/**
 * @author : acfan
 * date : create in 2023/6/13 20:36
 * description :
 **/

@Slf4j
@Configuration
public class SaTokenConfig {

    @Resource
    private RedisTemplate<String, Object> redis;
    @Resource
    private SecureConfig secureConfig;

    /**
     * 注册 Sa-Token全局过滤器
     */
    @Bean
    public SaReactorFilter getSaReactorFilter() {
        return new SaReactorFilter()
                // 拦截地址
                .addInclude("/**").addExclude(secureConfig.getApiWhiteUris().toArray(new String[0]))
                .addExclude("/", "/favicon.ico", "/error", "/csrf", "/webjars/**", "/swagger-resources/**", "/v2/**", "/doc.html/**")
                // 鉴权方法：每次访问进入
                .setAuth(obj -> {
                    SaRequest request = SaHolder.getRequest();
                    SaRouterStaff routerStaff = SaRouter.match("/**",  r -> StpUtil.checkLogin());
                    if (routerStaff.isHit) {
                        String token = request.getHeader("token");
                        if (StringUtils.isBlank(token)) {
                            token = request.getParam("token");
                            if (Boolean.FALSE.equals(redis.hasKey(token))) {
                                throw new RuntimeException(ExceptionType.Token_Invalid.getMessage());
                            }
                        }
                    }
                })
                // 异常处理方法：每次setAuth函数出现异常时进入
                .setError(e -> Result.fail(e.getMessage()))
                // 前置函数：在每次认证函数之前执行
                .setBeforeAuth(o -> this.configCross());
    }


    /**
     * 配置跨域
     */
    private void configCross() {
        // ---------- 设置跨域响应头 ----------
        SaHolder.getResponse()
                // 是否可以在iframe显示视图： DENY=不可以 | SAMEORIGIN=同域下可以 | ALLOW-FROM uri=指定域名下可以
                .setHeader("X-Frame-Options", "SAMEORIGIN")
                // 是否启用浏览器默认XSS防护： 0=禁用 | 1=启用 | 1; mode=block 启用, 并在检查到XSS攻击时，停止渲染页面
                .setHeader("X-XSS-Protection", "1; mode=block")
                // 禁用浏览器内容嗅探
                .setHeader("X-Content-Type-Options", "nosniff")
                // 允许指定域访问跨域资源
                .setHeader("Access-Control-Allow-Origin", "*")
                // 允许所有请求方式
                .setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE")
                // 有效时间
                .setHeader("Access-Control-Max-Age", "3600")
                // 允许的header参数
                .setHeader("Access-Control-Allow-Headers", "*");

        // 如果是预检请求，则立即返回到前端
        SaRouter.match(SaHttpMethod.OPTIONS).free(r -> log.warn("--------OPTIONS预检请求")).back();
    }

}
