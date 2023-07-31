package com.acvount.log.show.service;

import com.acvount.log.show.mapper.EconomyLogMapper;
import com.acvount.server.log.api.ecnonmy.domain.EconomyLog;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : acfan
 * date : create in 2023/7/30 21:16
 * description :
 **/

@Service
public class EconomyLogService {
    @Resource
    private EconomyLogMapper economyLogMapper;

    public List<EconomyLog> selectList(Integer size, Long lastId) {
        return economyLogMapper.selectList(Wrappers.lambdaQuery(EconomyLog.class)
                .lt(EconomyLog::getId, lastId)
                .orderByDesc(EconomyLog::getCreateTime)
                .last("limit %d".formatted(size)));
    }
}
