package com.acvount.log.show.service;

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

    public LogService(LoseLogService loseLogService, EconomyLogService economyLogService) {
        this.loseLogService = loseLogService;
        this.economyLogService = economyLogService;
    }

    public Object getLogByType(String type, Integer size, Long lastId) {
        if (lastId == null) {
            lastId = Long.MAX_VALUE;
        }
        if (size == null || size > 50) {
            size = 20;
        }
        switch (type) {
            case "lose":
                return loseLogService.selectList(size, lastId);
            case "economy":
                return economyLogService.selectList(size, lastId);
            default:
                return Collections.emptyList();
        }
    }
}
