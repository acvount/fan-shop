package com.acvount.auth.permission.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.acvount.auth.permission.domain.Permission;
import com.acvount.auth.permission.service.PermissionService;
import com.acvount.auth.permission.mapper.PermissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author acfan
* @description 针对表【permission(权限表)】的数据库操作Service实现
* @createDate 2023-07-13 20:50:05
*/
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission>
    implements PermissionService{

    private final PermissionMapper permissionMapper;

    @Autowired
    public PermissionServiceImpl(PermissionMapper permissionMapper){
        this.permissionMapper = permissionMapper;
    }
    @Override
    public List<String> getPermissionNamesByUserId(Long userId) {
        return permissionMapper.selectPermissionNamesByUserId(userId);
    }
}




