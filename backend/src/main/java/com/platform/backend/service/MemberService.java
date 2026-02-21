package com.platform.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.platform.backend.entity.Member;

public interface MemberService extends IService<Member> {
    Member getByStudentId(String studentId);
}
