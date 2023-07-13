package com.acvount.auth.permission.service;

import com.acvount.auth.permission.domain.Role;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author acfan
* @description 针对表【role(角色表)】的数据库操作Service
* @createDate 2023-07-13 21:15:16
*/
public interface RoleService extends IService<Role> {
    List<String> getRoleNamesByUserId(Long userId);
}
