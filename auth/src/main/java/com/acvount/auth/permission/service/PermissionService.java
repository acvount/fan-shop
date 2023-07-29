package com.acvount.auth.permission.service;

import com.acvount.auth.permission.domain.Permission;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author acfan
* description 针对表【permission(权限表)】的数据库操作Service
* createDate 2023-07-13 20:50:05
*/
public interface PermissionService extends IService<Permission> {
    List<String> getPermissionNamesByUserId(Long userId);
}
