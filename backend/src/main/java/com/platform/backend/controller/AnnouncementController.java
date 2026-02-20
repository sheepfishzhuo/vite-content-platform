package com.platform.backend.controller;

import com.platform.backend.common.Result;
import com.platform.backend.entity.Announcement;
import com.platform.backend.service.AnnouncementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "公告管理")
@RestController
@RequestMapping("/api/announcements")
public class AnnouncementController {

    @Autowired
    private AnnouncementService announcementService;

    @Operation(summary = "获取启用的公告列表")
    @GetMapping
    public Result<List<Announcement>> getAnnouncements() {
        return Result.success(announcementService.getEnabledAnnouncements());
    }

    @Operation(summary = "获取所有公告（管理用）")
    @GetMapping("/all")
    public Result<List<Announcement>> getAllAnnouncements() {
        return Result.success(announcementService.list());
    }

    @Operation(summary = "创建公告")
    @PostMapping
    public Result<Announcement> createAnnouncement(@RequestBody Announcement announcement) {
        announcementService.save(announcement);
        return Result.success(announcement);
    }

    @Operation(summary = "更新公告")
    @PutMapping("/{id}")
    public Result<Announcement> updateAnnouncement(@PathVariable Long id, @RequestBody Announcement announcement) {
        announcement.setId(id);
        announcementService.updateById(announcement);
        return Result.success(announcement);
    }

    @Operation(summary = "删除公告")
    @DeleteMapping("/{id}")
    public Result<Void> deleteAnnouncement(@PathVariable Long id) {
        announcementService.removeById(id);
        return Result.success();
    }
}
