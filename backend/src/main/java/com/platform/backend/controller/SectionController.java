package com.platform.backend.controller;

import com.platform.backend.common.Result;
import com.platform.backend.entity.Article;
import com.platform.backend.entity.Section;
import com.platform.backend.service.ArticleService;
import com.platform.backend.service.SectionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "首页区块")
@RestController
@RequestMapping("/api/sections")
public class SectionController {

    @Autowired
    private SectionService sectionService;

    @Autowired
    private ArticleService articleService;

    @Operation(summary = "获取首页区块")
    @GetMapping("/homepage")
    public Result<List<Section>> getHomepageSections() {
        return Result.success(sectionService.getHomepageSections());
    }

    @Operation(summary = "获取所有区块（管理用）")
    @GetMapping("/all")
    public Result<List<Section>> getAllSections() {
        return Result.success(sectionService.list());
    }

    @Operation(summary = "获取区块内容")
    @GetMapping("/{id}/items")
    public Result<Map<String, Object>> getSectionItems(@PathVariable Long id) {
        Section section = sectionService.getById(id);
        if (section == null) {
            return Result.error("区块不存在");
        }
        
        List<Article> articles = articleService.getAllArticlesBySectionId(id);
        
        Map<String, Object> result = new HashMap<>();
        result.put("title", section.getTitle());
        result.put("description", section.getDescription());
        result.put("items", articles);
        result.put("total", articles.size());
        return Result.success(result);
    }

    @Operation(summary = "创建区块")
    @PostMapping
    public Result<Section> createSection(@RequestBody Section section) {
        sectionService.save(section);
        return Result.success(section);
    }

    @Operation(summary = "更新区块")
    @PutMapping("/{id}")
    public Result<Section> updateSection(@PathVariable Long id, @RequestBody Section section) {
        section.setId(id);
        sectionService.updateById(section);
        return Result.success(section);
    }

    @Operation(summary = "删除区块")
    @DeleteMapping("/{id}")
    public Result<Void> deleteSection(@PathVariable Long id) {
        sectionService.removeById(id);
        return Result.success();
    }
}
