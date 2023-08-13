package com.acvount.item.api;

import com.acvount.item.bean.ItemInfo;

import java.util.List;

/**
 * @author : acfan
 * date : create in 2023/8/9 15:12
 * description :
 **/

public interface ItemServiceAPI {
    public List<ItemInfo> getListByType(Long typeId);
}
