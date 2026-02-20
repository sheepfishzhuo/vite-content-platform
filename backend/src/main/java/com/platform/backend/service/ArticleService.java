package com.platform.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.platform.backend.entity.Article;
import java.util.List;

public interface ArticleService extends IService<Article> {
    List<Article> getArticlesBySectionId(Long sectionId);
    List<Article> getAllArticlesBySectionId(Long sectionId);
}
