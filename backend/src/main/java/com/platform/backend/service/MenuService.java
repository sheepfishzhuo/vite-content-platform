package com.platform.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.platform.backend.entity.Menu;
import java.util.List;

public interface MenuService extends IService<Menu> {
    List<Menu> getMenuTree();
    List<Menu> getMenuTreeByType(String menuType);
    List<Menu> getChildrenByParentId(Long parentId);
}
