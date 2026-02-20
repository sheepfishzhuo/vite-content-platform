package com.platform.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.platform.backend.entity.QuickEntry;
import java.util.List;

public interface QuickEntryService extends IService<QuickEntry> {
    List<QuickEntry> getEnabledEntries();
}
