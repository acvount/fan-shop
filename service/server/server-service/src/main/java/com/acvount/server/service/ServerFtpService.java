package com.acvount.server.service;

import com.acvount.server.bean.ServerFtp;
import com.acvount.server.mapper.ServerFtpMapper;
import org.springframework.stereotype.Service;

/**
 * @author : acfan
 * date : create in 2023/7/30 11:59
 * description :
 **/

@Service
public class ServerFtpService {

    private ServerFtpMapper serverFtpMapper;

    public Integer addFtp(ServerFtp serverFtp) {
        return serverFtpMapper.insert(serverFtp);
    }
}
