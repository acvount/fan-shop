package com.acvount.item.base.service;

import com.acvount.item.base.mapper.ItemSuggestMapper;
import com.acvount.item.bean.ItemSuggest;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : acfan
 * date : create in 2023/8/12 15:35
 * description :
 **/

@Service
public class ItemSuggestService {
    @Resource
    private ItemSuggestMapper itemSuggestMapper;

    public List<ItemSuggest> getLists() {
        return itemSuggestMapper.selectList(Wrappers.emptyWrapper());
    }
}
