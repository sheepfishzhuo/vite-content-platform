package com.platform.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platform.backend.entity.Article;
import com.platform.backend.mapper.ArticleMapper;
import com.platform.backend.service.ArticleService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Override
    public List<Article> getArticlesBySectionId(Long sectionId) {
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Article::getSectionId, sectionId)
               .eq(Article::getStatus, 1)
               .orderByDesc(Article::getIsTop)
               .orderByDesc(Article::getCreatedTime)
               .last("LIMIT 5");
        return list(wrapper);
    }

    @Override
    public List<Article> getAllArticlesBySectionId(Long sectionId) {
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Article::getSectionId, sectionId)
               .eq(Article::getStatus, 1)
               .orderByDesc(Article::getIsTop)
               .orderByDesc(Article::getCreatedTime);
        return list(wrapper);
    }
}
