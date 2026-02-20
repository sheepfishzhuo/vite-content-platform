package com.platform.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platform.backend.entity.Section;
import com.platform.backend.mapper.SectionMapper;
import com.platform.backend.service.SectionService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SectionServiceImpl extends ServiceImpl<SectionMapper, Section> implements SectionService {

    @Override
    public List<Section> getHomepageSections() {
        LambdaQueryWrapper<Section> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Section::getIsEnabled, 1)
               .orderByAsc(Section::getSortOrder);
        return list(wrapper);
    }
}
