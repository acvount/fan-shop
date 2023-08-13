package com.acvount.item.base.service;

import com.acvount.item.base.mapper.ItemCacheVersionMapper;
import com.acvount.item.bean.ItemCacheVersion;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @author : acfan
 * date : create in 2023/8/12 15:25
 * description :
 **/

@Service
public class ItemCacheVersionService {

    @Resource
    private ItemCacheVersionMapper itemCacheVersionMapper;

    public ItemCacheVersion getNewVersion() {
        return itemCacheVersionMapper.selectList(Wrappers.lambdaQuery(ItemCacheVersion.class)
                .eq(ItemCacheVersion::getForceFlag, Boolean.TRUE)
                .last("limit 1")).stream().findFirst().orElseThrow();
    }
}
