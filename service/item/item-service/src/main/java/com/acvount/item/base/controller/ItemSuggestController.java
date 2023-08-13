package com.acvount.item.base.controller;

import com.acvount.common.core.result.Result;
import com.acvount.item.base.service.ItemSuggestService;
import com.acvount.item.bean.ItemSuggest;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author : acfan
 * date : create in 2023/8/12 15:33
 * description : 物品关联表（推荐）
 **/

@RestController
@RequestMapping("open/suggest")
public class ItemSuggestController {

    @Resource
    private ItemSuggestService itemSuggestService;

    @GetMapping("load_all")
    public Result<List<ItemSuggest>> loadAllSuggest(){
        return Result.success(itemSuggestService.getLists());
    }

    
}
