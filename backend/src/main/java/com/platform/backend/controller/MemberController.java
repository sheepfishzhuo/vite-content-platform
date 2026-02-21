package com.platform.backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.backend.common.Result;
import com.platform.backend.entity.Member;
import com.platform.backend.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Tag(name = "前台认证管理")
@RestController
@RequestMapping("/api/member")
public class MemberController {

    @Autowired
    private MemberService memberService;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public Result<Map<String, Object>> register(@RequestBody Member member) {
        if (member.getStudentId() == null || member.getStudentId().trim().isEmpty()) {
            return Result.error("学号不能为空");
        }
        if (member.getRealName() == null || member.getRealName().trim().isEmpty()) {
            return Result.error("姓名不能为空");
        }
        if (member.getPassword() == null || member.getPassword().length() < 6) {
            return Result.error("密码长度不能少于6位");
        }

        Member existStudent = memberService.getByStudentId(member.getStudentId());
        if (existStudent != null) {
            return Result.error("学号已被注册");
        }

        member.setPassword(passwordEncoder.encode(member.getPassword()));
        member.setStatus(1);
        memberService.save(member);

        String token = UUID.randomUUID().toString().replace("-", "");

        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("id", member.getId());
        userInfo.put("studentId", member.getStudentId());
        userInfo.put("realName", member.getRealName());
        userInfo.put("grade", member.getGrade());
        userInfo.put("graduateType", member.getGraduateType());
        userInfo.put("major", member.getMajor());
        userInfo.put("email", member.getEmail());
        userInfo.put("phone", member.getPhone());
        data.put("user", userInfo);

        return Result.success(data);
    }

    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody Map<String, String> params) {
        String studentId = params.get("studentId");
        String password = params.get("password");

        if (studentId == null || password == null) {
            return Result.error("学号和密码不能为空");
        }

        Member member = memberService.getByStudentId(studentId);
        if (member == null) {
            return Result.error("用户不存在");
        }

        if (member.getStatus() != 1) {
            return Result.error("账号已被禁用");
        }

        if (!passwordEncoder.matches(password, member.getPassword())) {
            return Result.error("密码错误");
        }

        String token = UUID.randomUUID().toString().replace("-", "");

        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("id", member.getId());
        userInfo.put("studentId", member.getStudentId());
        userInfo.put("realName", member.getRealName());
        userInfo.put("grade", member.getGrade());
        userInfo.put("graduateType", member.getGraduateType());
        userInfo.put("major", member.getMajor());
        userInfo.put("email", member.getEmail());
        userInfo.put("phone", member.getPhone());
        data.put("user", userInfo);

        return Result.success(data);
    }

    @Operation(summary = "检查学号是否存在")
    @GetMapping("/check-student-id")
    public Result<Boolean> checkStudentId(@RequestParam String studentId) {
        Member member = memberService.getByStudentId(studentId);
        return Result.success(member != null);
    }

    @Operation(summary = "获取前台用户列表")
    @GetMapping("/list")
    public Result<Page<Member>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String keyword) {
        Page<Member> pageParam = new Page<>(page, pageSize);
        LambdaQueryWrapper<Member> wrapper = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.trim().isEmpty()) {
            wrapper.like(Member::getStudentId, keyword)
                   .or().like(Member::getRealName, keyword)
                   .or().like(Member::getEmail, keyword)
                   .or().like(Member::getPhone, keyword);
        }
        wrapper.orderByDesc(Member::getCreatedTime);
        Page<Member> result = memberService.page(pageParam, wrapper);
        result.getRecords().forEach(m -> m.setPassword(null));
        return Result.success(result);
    }

    @Operation(summary = "更新前台用户状态")
    @PutMapping("/{id}/status")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestBody Map<String, Integer> params) {
        Member member = memberService.getById(id);
        if (member == null) {
            return Result.error("用户不存在");
        }
        Integer status = params.get("status");
        if (status == null || (status != 0 && status != 1)) {
            return Result.error("状态值无效");
        }
        member.setStatus(status);
        memberService.updateById(member);
        return Result.success();
    }

    @Operation(summary = "删除前台用户")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        memberService.removeById(id);
        return Result.success();
    }

    @Operation(summary = "获取前台用户详情")
    @GetMapping("/{id}")
    public Result<Member> getById(@PathVariable Long id) {
        Member member = memberService.getById(id);
        if (member != null) {
            member.setPassword(null);
        }
        return Result.success(member);
    }

    @Operation(summary = "更新前台用户信息")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody Member member) {
        Member exist = memberService.getById(id);
        if (exist == null) {
            return Result.error("用户不存在");
        }
        member.setId(id);
        member.setPassword(null);
        memberService.updateById(member);
        return Result.success();
    }
}
