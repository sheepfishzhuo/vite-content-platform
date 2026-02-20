package com.platform.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.platform.backend.entity.Section;
import java.util.List;

public interface SectionService extends IService<Section> {
    List<Section> getHomepageSections();
}
