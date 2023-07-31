package com.acvount.log.show.service;

import com.acvount.common.core.exception.BaseException;
import com.acvount.server.api.ServerServiceApi;
import com.acvount.web.LoginUser;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * @author : acfan
 * date : create in 2023/7/30 21:03
 * description :
 **/

@Service
public class LogService {

    private final LoseLogService loseLogService;
    private final EconomyLogService economyLogService;
    @DubboReference
    @SuppressWarnings("unused")
    private ServerServiceApi serverServiceApi;

    public LogService(LoseLogService loseLogService, EconomyLogService economyLogService) {
        this.loseLogService = loseLogService;
        this.economyLogService = economyLogService;
    }

    public Object getLogByType(String type, Integer size, Long lastId, Long serverId) throws BaseException {
        if (lastId == null) {
            lastId = Long.MAX_VALUE;
        }
        if (size == null || size > 50) {
            size = 20;
        }
        if (serverServiceApi.selectByOwnerId(LoginUser.getLoginUserID()).stream().anyMatch(e -> e.getServerOwner().equals(serverId))) {
            switch (type) {
                case "lose":
                    return loseLogService.selectList(size, lastId);
                case "economy":
                    return economyLogService.selectList(size, lastId);
            }
        }
        return Collections.emptyList();
    }
}
