package com.acvount.item.base.service;

import com.acvount.item.base.mapper.ItemInfoMapper;
import com.acvount.item.bean.ItemInfo;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : acfan
 * date : create in 2023/8/11 20:06
 * description :
 **/

@Service
public class ItemInfoService {

    @Resource
    private ItemInfoMapper itemInfoMapper;

    public List<ItemInfo> getListByType(Long typeId) {
        return itemInfoMapper.selectList(Wrappers.lambdaQuery(ItemInfo.class)
                .eq(ItemInfo::getType, typeId));
    }

    public List<ItemInfo> getLists() {
        return itemInfoMapper.selectList(Wrappers.emptyWrapper());
    }
}
