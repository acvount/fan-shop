package com.acvount.log.show.service;

import com.acvount.log.show.mapper.LoseLogMapper;
import com.acvount.server.log.api.lose.domain.LoseLog;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : acfan
 * date : create in 2023/7/30 21:02
 * description :
 **/

@Service
public class LoseLogService {

    @Resource
    private LoseLogMapper loseLogMapper;

    public List<LoseLog> selectList(Integer size, Long lastId) {
        return loseLogMapper.selectList(Wrappers.lambdaQuery(LoseLog.class)
                .lt(LoseLog::getId, lastId)
                .orderByDesc(LoseLog::getCreateTime)
                .last("limit %s".formatted(size)));
    }
}
