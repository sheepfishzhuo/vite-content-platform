package com.platform.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.platform.backend.entity.Announcement;
import java.util.List;

public interface AnnouncementService extends IService<Announcement> {
    List<Announcement> getEnabledAnnouncements();
}
