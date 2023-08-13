package com.acvount.item.base.service;

import com.acvount.item.base.mapper.ItemTypeMapper;
import com.acvount.item.bean.ItemType;
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
public class ItemTypeService {

    @Resource
    private ItemTypeMapper itemTypeMapper;

    public List<ItemType> getAllType() {
        return itemTypeMapper.selectList(Wrappers.emptyWrapper());
    }
}
