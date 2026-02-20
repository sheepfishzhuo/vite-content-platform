package com.platform.backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.backend.common.Result;
import com.platform.backend.entity.Article;
import com.platform.backend.service.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "文章管理")
@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Operation(summary = "分页查询文章")
    @GetMapping
    public Result<Page<Article>> getArticles(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Long sectionId,
            @RequestParam(required = false) Integer status) {
        Page<Article> pageParam = new Page<>(page, pageSize);
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        if (sectionId != null) {
            wrapper.eq(Article::getSectionId, sectionId);
        }
        if (status != null) {
            wrapper.eq(Article::getStatus, status);
        }
        wrapper.orderByDesc(Article::getIsTop)
               .orderByDesc(Article::getCreatedTime);
        return Result.success(articleService.page(pageParam, wrapper));
    }

    @Operation(summary = "获取文章详情")
    @GetMapping("/{id}")
    public Result<Article> getArticle(@PathVariable Long id) {
        return Result.success(articleService.getById(id));
    }

    @Operation(summary = "创建文章")
    @PostMapping
    public Result<Article> createArticle(@RequestBody Article article) {
        article.setViewCount(0);
        article.setStatus(1);
        articleService.save(article);
        return Result.success(article);
    }

    @Operation(summary = "更新文章")
    @PutMapping("/{id}")
    public Result<Article> updateArticle(@PathVariable Long id, @RequestBody Article article) {
        article.setId(id);
        articleService.updateById(article);
        return Result.success(article);
    }

    @Operation(summary = "删除文章")
    @DeleteMapping("/{id}")
    public Result<Void> deleteArticle(@PathVariable Long id) {
        articleService.removeById(id);
        return Result.success();
    }
}
