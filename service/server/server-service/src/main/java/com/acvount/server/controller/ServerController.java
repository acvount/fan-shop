package com.acvount.server.controller;

import com.acvount.common.core.exception.BaseException;
import com.acvount.common.core.result.Result;
import com.acvount.server.bean.Server;
import com.acvount.server.bean.ServerFtp;
import com.acvount.server.pojo.vo.OwnerAddVO;
import com.acvount.server.service.ServerFtpService;
import com.acvount.server.service.ServerService;
import com.acvount.user.api.UserService;
import com.acvount.web.LoginUser;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author : acfan
 * date : create in 2023/7/29 22:46
 * description : 服主Controller
 **/

@RestController
@RequestMapping("owner")
public class ServerController {

    @DubboReference
    @SuppressWarnings("unused")
    private UserService userService;

    private final ServerService serverService;
    private final ServerFtpService serverFtpService;

    @Autowired
    public ServerController(ServerService serverService, ServerFtpService serverFtpService) {
        this.serverService = serverService;
        this.serverFtpService = serverFtpService;
    }

    @PostMapping("add-server")
    public Result<Boolean> ownerAddServer(@RequestBody OwnerAddVO ownerAddVO) throws BaseException {
        Long loginUserID = LoginUser.getLoginUserID();
        Server serverDO = ownerAddVO.getServerDO(loginUserID);
        Integer i = serverService.addServer(serverDO);
        ServerFtp ftpDO = ownerAddVO.getFtpDO(loginUserID, serverDO.getId());
        i += serverFtpService.addFtp(ftpDO);
        i += userService.modifyServerFlag(loginUserID);
        return Result.success(i > 0);
    }
}
