package com.acvount.auth.login.service.login;

import cn.dev33.satoken.stp.StpUtil;
import com.acvount.auth.login.service.register.RegisterService;
import com.acvount.auth.permission.service.PermissionService;
import com.acvount.auth.permission.service.RoleService;
import com.acvount.user.api.UserServiceApi;
import com.acvount.user.bean.UserInfo;
import com.alibaba.nacos.shaded.com.google.common.collect.Lists;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author : acfan
 * date : create in 2023/7/1 18:48
 * description : 登陆服务
 **/

@Service
@Slf4j
public class LoginService {
    @Resource
    private RegisterService registerService;
    @Resource
    private PermissionService permissionService;
    @Resource
    private RoleService roleService;
    @Resource
    private RedisTemplate<String, String> redisTemplate;

    private static final Integer LIST_SPLIT_SIZE = 50;
    @DubboReference
    @SuppressWarnings("unused")
    private UserServiceApi userServiceApi;

    public UserInfo checkAccountPhoneAndCreateIfNeeded(String phone) {
        UserInfo user = userServiceApi.getUserByMobile(phone);
        if (Objects.isNull(user) || Objects.equals(user.getUserId(), NumberUtils.LONG_ZERO)) {
            return registerService.createAccount(phone);
        }
        return user;
    }

    public String login(Long userId) {
        return login(userId, "PC");
    }

    public String login(Long userId, String type) {
        StpUtil.login(userId, type);
        new Thread(() -> writeRoleAndPermissionToCache(userId,type)).start();
        log.info("用户「{}」「{}」登陆成功", userId, type);
        return StpUtil.getTokenValueByLoginId(userId);
    }

    private void writeRoleAndPermissionToCache(Long userId,String type) {
        String permissionKey = "%d-%s-permission:".formatted(userId,type);
        String roleKey = "%d-%s-role:".formatted(userId,type);
        List<String> permissions = permissionService.getPermissionNamesByUserId(userId);
        List<String> roles = roleService.getRoleNamesByUserId(userId);
        Lists.partition(permissions, LIST_SPLIT_SIZE).forEach(e -> redisTemplate.opsForList().rightPushAll(permissionKey, e));
        Lists.partition(roles, LIST_SPLIT_SIZE).forEach(e -> redisTemplate.opsForList().rightPushAll(roleKey, e));
        log.info("「{}」用户「{}」权限已成功写入缓存，权限列表：「{}」，角色列表：「{}」", Thread.currentThread().getName(), userId, permissions.size(), roles.size());
    }
}
