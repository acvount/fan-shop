package com.acvount.user.mapper;

import com.acvount.user.bean.UserInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author : acfan
 * date : create in 2023/7/2 16:47
 * description :
 **/

@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfo> {
    @Select("select * from user_info where user_id = (select user_id from user_authorization where login_phone = #{phone}) ")
    UserInfo selectByPhone(@Param("phone") String phone);
}
