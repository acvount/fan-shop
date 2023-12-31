package com.acvount.server.log.api.domain.ftp.mapper;

import com.acvount.server.log.api.domain.ftp.entity.ServerFTPTaskStats;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * @author : acfan
 * date : create in 2023/7/22 17:08
 * description :
 **/

@Mapper
public interface ServerFTPTaskStatsMapper extends BaseMapper<ServerFTPTaskStats> {

    @Update("update server_enabled_ftp_log_listener set task_status = #{state} where ftp_id = #{id}")
    void changeStateAndUnTask(@Param("id") Long id,@Param("state")Integer state);

}
