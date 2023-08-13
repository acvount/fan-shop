package com.acvount.item.base.controller;

import com.acvount.common.core.result.Result;
import com.acvount.item.base.service.ItemCacheVersionService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : acfan
 * date : create in 2023/8/12 15:25
 * description :
 **/

@RestController
@RequestMapping("open/version")
public class ItemCacheVersionController {

    @Resource
    private ItemCacheVersionService itemCacheVersionService;

    @GetMapping("new_version")
    public Result<String> getNewVersion() {
        return Result.success(itemCacheVersionService.getNewVersion().getVersion());
    }
}
