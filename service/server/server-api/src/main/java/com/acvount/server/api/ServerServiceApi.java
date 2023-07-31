package com.acvount.server.api;

import com.acvount.server.bean.Server;

import java.util.List;

/**
 * @author : acfan
 * date : create in 2023/7/31 23:17
 * description :
 **/

public interface ServerServiceApi {
    List<Server> selectByOwnerId(Long ownerId);
}
