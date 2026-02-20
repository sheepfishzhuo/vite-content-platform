package com.platform.backend.controller;

import com.platform.backend.common.Result;
import com.platform.backend.entity.Menu;
import com.platform.backend.service.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "菜单管理")
@RestController
@RequestMapping("/api/menus")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @Operation(summary = "获取菜单树")
    @GetMapping
    public Result<List<Menu>> getMenuTree(@RequestParam(required = false) String type) {
        if (type != null) {
            return Result.success(menuService.getMenuTreeByType(type));
        }
        return Result.success(menuService.getMenuTree());
    }

    @Operation(summary = "获取前台菜单树")
    @GetMapping("/frontend")
    public Result<List<Menu>> getFrontendMenuTree() {
        return Result.success(menuService.getMenuTreeByType("frontend"));
    }

    @Operation(summary = "获取后台菜单树")
    @GetMapping("/admin")
    public Result<List<Menu>> getAdminMenuTree() {
        return Result.success(menuService.getMenuTreeByType("admin"));
    }

    @Operation(summary = "获取子菜单")
    @GetMapping("/{id}/children")
    public Result<List<Menu>> getChildren(@PathVariable Long id) {
        return Result.success(menuService.getChildrenByParentId(id));
    }

    @Operation(summary = "创建菜单")
    @PostMapping
    public Result<Menu> createMenu(@RequestBody Menu menu) {
        menuService.save(menu);
        return Result.success(menu);
    }

    @Operation(summary = "更新菜单")
    @PutMapping("/{id}")
    public Result<Menu> updateMenu(@PathVariable Long id, @RequestBody Menu menu) {
        menu.setId(id);
        menuService.updateById(menu);
        return Result.success(menu);
    }

    @Operation(summary = "删除菜单")
    @DeleteMapping("/{id}")
    public Result<Void> deleteMenu(@PathVariable Long id) {
        menuService.removeById(id);
        return Result.success();
    }
}
