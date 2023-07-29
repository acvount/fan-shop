package com.acvount.server.log.api.domain.ftp.mapper;

import com.acvount.server.log.api.domain.ftp.entity.ServerEnabledFtpLogListener;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author : acfan
 * date : create in 2023/7/22 17:08
 * description :
 **/

@Mapper
public interface ServerEnabledFtpLogListenerMapper extends BaseMapper<ServerEnabledFtpLogListener> {

    @Select("select count(0) from server_enabled_ftp_log_listener where task_status = 1 and ftp_id = #{ftpId}")
    Boolean isStarted(@Param("ftpId") Long ftpId);

    @Select("select * from server_enabled_ftp_log_listener where  ftp_id = #{ftpId}")
    ServerEnabledFtpLogListener selectByFtpId(Long id);
}
