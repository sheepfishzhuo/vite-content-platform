package com.platform.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.platform.backend.entity.Member;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper extends BaseMapper<Member> {
}
