package com.platform.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.platform.backend.entity.Announcement;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AnnouncementMapper extends BaseMapper<Announcement> {
}
