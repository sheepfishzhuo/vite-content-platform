package com.platform.backend.controller;

import com.platform.backend.common.Result;
import com.platform.backend.entity.QuickEntry;
import com.platform.backend.service.QuickEntryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "快速入口")
@RestController
@RequestMapping("/api/quick-entries")
public class QuickEntryController {

    @Autowired
    private QuickEntryService quickEntryService;

    @Operation(summary = "获取启用的快速入口")
    @GetMapping
    public Result<List<QuickEntry>> getQuickEntries() {
        return Result.success(quickEntryService.getEnabledEntries());
    }

    @Operation(summary = "获取所有快速入口（管理用）")
    @GetMapping("/all")
    public Result<List<QuickEntry>> getAllQuickEntries() {
        return Result.success(quickEntryService.list());
    }

    @Operation(summary = "创建快速入口")
    @PostMapping
    public Result<QuickEntry> createQuickEntry(@RequestBody QuickEntry quickEntry) {
        quickEntryService.save(quickEntry);
        return Result.success(quickEntry);
    }

    @Operation(summary = "更新快速入口")
    @PutMapping("/{id}")
    public Result<QuickEntry> updateQuickEntry(@PathVariable Long id, @RequestBody QuickEntry quickEntry) {
        quickEntry.setId(id);
        quickEntryService.updateById(quickEntry);
        return Result.success(quickEntry);
    }

    @Operation(summary = "删除快速入口")
    @DeleteMapping("/{id}")
    public Result<Void> deleteQuickEntry(@PathVariable Long id) {
        quickEntryService.removeById(id);
        return Result.success();
    }
}
