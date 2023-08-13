package com.acvount.item.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author : acfan
 * date : create in 2023/8/9 15:15
 * description :
 **/

@Data
public class ItemInfo {
    @TableId(type = IdType.ASSIGN_ID)
    private Long itemId;
    private String item_name;
    private String item_code;
    private String item_pic;
    private String create_time;
    private String type;
}
