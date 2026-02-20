package com.platform.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platform.backend.entity.Menu;
import com.platform.backend.mapper.MenuMapper;
import com.platform.backend.service.MenuService;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Override
    public List<Menu> getMenuTree() {
        List<Menu> allMenus = baseMapper.selectAllOrderBySort();
        Map<Long, List<Menu>> menuMap = allMenus.stream()
                .collect(Collectors.groupingBy(Menu::getParentId));
        
        List<Menu> rootMenus = menuMap.getOrDefault(0L, new ArrayList<>());
        buildTree(rootMenus, menuMap);
        return rootMenus;
    }

    @Override
    public List<Menu> getMenuTreeByType(String menuType) {
        List<Menu> allMenus = baseMapper.selectAllOrderBySort();
        
        Set<Long> targetIds = allMenus.stream()
                .filter(menu -> menuType.equals(menu.getMenuType()))
                .map(Menu::getId)
                .collect(Collectors.toSet());
        
        Set<Long> visibleIds = new HashSet<>(targetIds);
        for (Menu menu : allMenus) {
            if (targetIds.contains(menu.getId())) {
                Long parentId = menu.getParentId();
                while (parentId != null && parentId != 0L) {
                    visibleIds.add(parentId);
                    Long finalParentId = parentId;
                    Menu parent = allMenus.stream()
                            .filter(m -> m.getId().equals(finalParentId))
                            .findFirst()
                            .orElse(null);
                    parentId = parent != null ? parent.getParentId() : null;
                }
            }
        }
        
        List<Menu> filteredMenus = allMenus.stream()
                .filter(menu -> visibleIds.contains(menu.getId()))
                .collect(Collectors.toList());
        
        Map<Long, List<Menu>> menuMap = filteredMenus.stream()
                .collect(Collectors.groupingBy(Menu::getParentId));
        
        List<Menu> rootMenus = menuMap.getOrDefault(0L, new ArrayList<>());
        buildTree(rootMenus, menuMap);
        return rootMenus;
    }

    @Override
    public List<Menu> getChildrenByParentId(Long parentId) {
        return baseMapper.selectByParentId(parentId);
    }

    private void buildTree(List<Menu> menus, Map<Long, List<Menu>> menuMap) {
        if (menus == null) return;
        for (Menu menu : menus) {
            List<Menu> children = menuMap.get(menu.getId());
            if (children != null && !children.isEmpty()) {
                menu.setChildren(children);
                buildTree(children, menuMap);
            }
        }
    }
}
