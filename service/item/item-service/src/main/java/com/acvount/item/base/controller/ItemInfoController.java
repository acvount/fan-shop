package com.acvount.item.base.controller;

import com.acvount.common.core.result.Result;
import com.acvount.item.base.service.ItemService;
import com.acvount.item.bean.ItemInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author : acfan
 * date : create in 2023/8/11 20:15
 * description :
 **/

@RestController
@RequestMapping("open/info")
public class ItemInfoController {

    @Resource
    private ItemService itemService;

    @GetMapping("by{type}")
    public Result<List<ItemInfo>> getListByType(@PathVariable Long type) {
        return Result.success(itemService.getListByType(type));
    }

    @GetMapping("load_all")
    public Result<List<ItemInfo>> loadAll() {
        return Result.success(itemService.getLists());
    }
}
