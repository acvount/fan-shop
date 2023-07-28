package com.acvount.server.log.lose.service;

import com.acvount.server.log.api.lose.domain.LoseLog;
import com.acvount.server.log.lose.mapper.LoseLogMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author : acfan
 * date : create in 2023/7/27 15:50
 * description :
 **/

@Slf4j
@Service
public class LoseLogService {

    @Resource
    private LoseLogMapper loseLogMapper;

    public void addLoseLog(Long serverId, String context, String type) {
        LoseLog loseLog = new LoseLog();
        loseLog.setType(type);
        loseLog.setServerId(serverId);
        loseLog.setLogContext(context);
        loseLogMapper.insert(loseLog);
        log.info("丢失一条消息{},{},{}",serverId,type,context);
    }
}
