package com.acvount.item.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author : acfan
 * date : create in 2023/8/12 15:22
 * description :
 **/

@Data
public class ItemCacheVersion {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    private String version;
    private Boolean forceFlag;
    private String createTime;
}
