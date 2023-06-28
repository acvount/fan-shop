package com.acvount.gateway.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : acfan
 * date : create in 2023/6/13 20:40
 * description : 网关白名单
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Component
@ConfigurationProperties(prefix = "secure")
public class SecureConfig {
    /**
     * 限制ip每秒访问次数
     */
    private Long limitIpSecondCount;
    private Long limitIpMinuteCount;
    private Long limitIpHourCount;
    /**
     * ip 白名单
     */
    private List<String> ipWhites;
    /**
     * ip黑名单
     */
    private List<String> ipBlacks;
    /**
     * 白名单
     */
    private List<String> apiWhiteUris = new ArrayList<>();
    /**
     * xss 黑名单
     */
    private List<String> xssBlackUris;
}
