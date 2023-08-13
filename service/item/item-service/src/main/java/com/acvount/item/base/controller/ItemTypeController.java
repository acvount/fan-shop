package com.acvount.item.base.controller;

import com.acvount.common.core.result.Result;
import com.acvount.item.base.service.ItemTypeService;
import com.acvount.item.bean.ItemType;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author : acfan
 * date : create in 2023/8/11 20:06
 * description :
 **/

@RestController
@RequestMapping("open/type")
public class ItemTypeController {

    @Resource
    private ItemTypeService itemTypeService;

    @GetMapping("all_type")
    public Result<List<ItemType>> getAllType() {
        return Result.success(itemTypeService.getAllType());
    }
}
