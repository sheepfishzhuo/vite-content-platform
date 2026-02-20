package com.platform.backend.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("section")
public class Section {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String title;
    private String description;
    private String displayStyle;
    private Integer sortOrder;
    private Integer isEnabled;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;
}
