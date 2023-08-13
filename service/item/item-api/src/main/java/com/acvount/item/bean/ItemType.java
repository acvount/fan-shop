package com.acvount.item.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author : acfan
 * date : create in 2023/8/9 15:13
 * description :
 **/

@Data
public class ItemType {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    private Long parentId;
    private String typeName;
    private String prefix;
    private String icon;
}
