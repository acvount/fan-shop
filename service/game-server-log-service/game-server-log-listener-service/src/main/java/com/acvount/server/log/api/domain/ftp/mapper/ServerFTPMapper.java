package com.acvount.server.log.api.domain.ftp.mapper;

import com.acvount.server.log.api.domain.ftp.entity.ServerFTP;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author : acfan
 * date : create in 2023/7/22 17:08
 * description :
 **/

@Mapper
public interface ServerFTPMapper extends BaseMapper<ServerFTP> {

    @Select("select * from server_ftp where id in (select ftp_id from server_enabled_ftp_log_listener where task_status = 0 )")
    List<ServerFTP> getEnabledFTP();

    @Update("update server_enabled_ftp_log_listener set task_status = 0 where ftp_id = #{id} and task_status = 1")
    void disableTaskById(@Param("id") Long id);

    @Update("update server_enabled_ftp_log_listener set task_status = 1 where ftp_id = #{id} and task_status = 0")
    void enableTaskById(@Param("id") Long id);
}
