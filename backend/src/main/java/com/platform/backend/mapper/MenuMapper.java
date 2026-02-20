package com.platform.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.platform.backend.entity.Menu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface MenuMapper extends BaseMapper<Menu> {
    @Select("SELECT * FROM menu WHERE parent_id = #{parentId} ORDER BY sort_order ASC")
    List<Menu> selectByParentId(Long parentId);
    
    @Select("SELECT * FROM menu ORDER BY sort_order ASC")
    List<Menu> selectAllOrderBySort();
}
