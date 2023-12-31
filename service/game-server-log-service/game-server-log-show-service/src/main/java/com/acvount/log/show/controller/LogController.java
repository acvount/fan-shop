package com.acvount.log.show.controller;

import com.acvount.common.core.exception.BaseException;
import com.acvount.common.core.result.Result;
import com.acvount.log.show.service.LogService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : acfan
 * date : create in 2023/7/30 21:01
 * description :
 **/

@RestController
@RequestMapping("log")
public class LogController {

    @Resource
    private LogService logService;

    @GetMapping("type/{type}")
    public Result<Object> getLogs(@PathVariable String type, Integer size, Long lastId, Long serverId) throws BaseException {
        Object logByType = logService.getLogByType(type, size, lastId, serverId);
        return Result.success(logByType);
    }
}
