package com.acvount.auth.permission.mapper;

import com.acvount.auth.permission.domain.Permission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author acfan
* @description 针对表【permission(权限表)】的数据库操作Mapper
* @createDate 2023-07-13 20:50:05
* @Entity com.acvount.auth.permission.domain.Permission
*/
@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {

    List<String> selectPermissionNamesByUserId(@Param("userId") Long userId);
}




