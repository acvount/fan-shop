package com.acvount.auth.permission.mapper;

import com.acvount.auth.permission.domain.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author acfan
* description 针对表【role(角色表)】的数据库操作Mapper
* createDate 2023-07-13 21:15:16
* Entity com.acvount.auth.permission.domain.Role
*/
@Mapper
public interface RoleMapper extends BaseMapper<Role> {

    List<String> selectRoleNamesByUserId(@Param("userId") Long userId);
}




