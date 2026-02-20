package com.platform.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.platform.backend.entity.Article;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface ArticleMapper extends BaseMapper<Article> {
    @Select("SELECT * FROM article WHERE menu_id = #{menuId} ORDER BY created_time DESC LIMIT #{limit}")
    List<Article> selectByMenuIdLimit(Long menuId, int limit);
}
