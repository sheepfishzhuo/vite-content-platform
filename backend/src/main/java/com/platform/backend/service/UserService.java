package com.platform.backend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.platform.backend.entity.User;

public interface UserService extends IService<User> {
    User getByUsername(String username);
    Page<User> getPage(int page, int pageSize);
    boolean create(User user);
    boolean update(Long id, User user);
}
