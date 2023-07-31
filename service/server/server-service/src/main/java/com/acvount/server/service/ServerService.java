package com.acvount.server.service;

import com.acvount.server.bean.Server;
import com.acvount.server.mapper.ServerMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @author : acfan
 * date : create in 2023/7/29 22:47
 * description :
 **/

@Service
public class ServerService {

    @Resource
    private ServerMapper serverMapper;



    public Integer addServer(Server serverDO) {
        return serverMapper.insert(serverDO);
    }


}
