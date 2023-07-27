package com.acvount.server.log.handle.stage.stages.economy.parser;

import com.acvount.server.log.economy.mapper.EconomyLogMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * @author : acfan
 * date : create in 2023/7/26 22:13
 * description : 金额日志 发生之前数据记录
 **/

@Component
public class EconomyBeforeParser implements EconomyParser{
    @Resource
    private EconomyLogMapper economyLogMapper;
    @Override
    public void consumer(String log,Long serverId) {

    }
}
