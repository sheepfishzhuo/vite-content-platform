package com.platform.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platform.backend.entity.QuickEntry;
import com.platform.backend.mapper.QuickEntryMapper;
import com.platform.backend.service.QuickEntryService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class QuickEntryServiceImpl extends ServiceImpl<QuickEntryMapper, QuickEntry> implements QuickEntryService {

    @Override
    public List<QuickEntry> getEnabledEntries() {
        LambdaQueryWrapper<QuickEntry> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(QuickEntry::getIsEnabled, 1)
               .orderByAsc(QuickEntry::getSortOrder);
        return list(wrapper);
    }
}
