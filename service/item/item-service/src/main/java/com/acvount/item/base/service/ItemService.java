package com.acvount.item.base.service;

import com.acvount.item.api.ItemServiceAPI;
import com.acvount.item.bean.ItemInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : acfan
 * date : create in 2023/8/11 20:05
 * description :
 **/

@Service
public class ItemService implements ItemServiceAPI {

    @Resource
    private ItemInfoService itemInfoService;

    @Override
    public List<ItemInfo> getListByType(Long typeId) {
        return itemInfoService.getListByType(typeId);
    }

    public List<ItemInfo> getLists() {
        return itemInfoService.getLists();
    }
}
