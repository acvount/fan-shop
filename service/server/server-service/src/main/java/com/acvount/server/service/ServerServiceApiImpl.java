package com.acvount.server.service;

import com.acvount.server.api.ServerServiceApi;
import com.acvount.server.bean.Server;
import com.acvount.server.mapper.ServerMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;

import java.util.List;

/**
 * @author : acfan
 * date : create in 2023/7/31 23:19
 * description :
 **/

@DubboService
@SuppressWarnings("unused")
public class ServerServiceApiImpl implements ServerServiceApi {

    @Resource
    private ServerMapper serverMapper;

    @Override
    public List<Server> selectByOwnerId(Long ownerId) {
        return serverMapper.selectList(Wrappers.lambdaQuery(Server.class).eq(Server::getServerOwner, ownerId));
    }
}
