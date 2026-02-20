package com.platform.backend.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("quick_entry")
public class QuickEntry {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String path;
    private String icon;
    private String color;
    private String backgroundImage;
    private Integer sortOrder;
    private Integer isEnabled;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;
}
