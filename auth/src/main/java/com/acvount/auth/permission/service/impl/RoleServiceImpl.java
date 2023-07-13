package com.acvount.auth.permission.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.acvount.auth.permission.domain.Role;
import com.acvount.auth.permission.service.RoleService;
import com.acvount.auth.permission.mapper.RoleMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author acfan
* @description 针对表【role(角色表)】的数据库操作Service实现
* @createDate 2023-07-13 21:15:16
*/
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role>
    implements RoleService{

    @Resource
    private RoleMapper roleMapper;

    @Override
    public List<String> getRoleNamesByUserId(Long userId) {
        return roleMapper.selectRoleNamesByUserId(userId);
    }
}




