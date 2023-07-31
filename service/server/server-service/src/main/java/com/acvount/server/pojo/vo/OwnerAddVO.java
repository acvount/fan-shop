package com.acvount.server.pojo.vo;

import com.acvount.server.bean.Server;
import com.acvount.server.bean.ServerFtp;
import lombok.Data;

/**
 * @author : acfan
 * date : create in 2023/7/30 11:46
 * description :
 **/

@Data
public class OwnerAddVO {
    private String serverName;
    private String serverDescript;
    private String area;
    private Integer maxNumber;
    private String ftpIp;
    private Integer ftpPort;
    private String ftpUsername;
    private String ftpPassword;

    public Server getServerDO(Long ownerId) {
        Server server = new Server();
        server.setServerOwner(ownerId);
        server.setServerName(serverName);
        server.setServerDescript(serverDescript);
        server.setArea(area);
        server.setMaxNumber(maxNumber);
        return server;
    }

    public ServerFtp getFtpDO(Long ownerId, Long serverId) {
        ServerFtp serverFtp = new ServerFtp();
        serverFtp.setOwnerId(ownerId);
        serverFtp.setServerId(serverId);
        serverFtp.setIp(ftpIp);
        serverFtp.setPort(ftpPort);
        serverFtp.setUsername(ftpUsername);
        serverFtp.setPassword(ftpPassword);
        return serverFtp;
    }
}
