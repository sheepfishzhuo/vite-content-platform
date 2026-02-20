package com.platform.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platform.backend.entity.Announcement;
import com.platform.backend.mapper.AnnouncementMapper;
import com.platform.backend.service.AnnouncementService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AnnouncementServiceImpl extends ServiceImpl<AnnouncementMapper, Announcement> implements AnnouncementService {

    @Override
    public List<Announcement> getEnabledAnnouncements() {
        LambdaQueryWrapper<Announcement> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Announcement::getIsEnabled, 1)
               .orderByDesc(Announcement::getIsTop)
               .orderByAsc(Announcement::getSortOrder)
               .orderByDesc(Announcement::getCreatedTime);
        return list(wrapper);
    }
}
