package com.mdd.front.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mdd.common.entity.activity.EnrollLink;
import com.mdd.common.entity.activity.Enrollment;
import com.mdd.common.exception.OperateException;
import com.mdd.common.mapper.activity.EnrollLinkMapper;
import com.mdd.common.mapper.activity.EnrollmentMapper;
import com.mdd.common.util.StringUtils;
import com.mdd.common.util.TimeUtils;
import com.mdd.front.service.ICourseCatalogService;
import com.mdd.front.service.IEnrollService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 客服报名链接 + 表单提交实现（T29.7 / #27）。
 *
 * <p>去重口径与小程序 mock 金样一致：{@code sessionId + phone + studentName}。
 * 服务端先查重给出中文提示，数据库唯一键 {@code uk_session_phone_student} 兜底并发。</p>
 */
@Service
public class EnrollServiceImpl implements IEnrollService {

    private static final int AGE_MIN = 3;
    private static final int AGE_MAX = 16;

    @Resource
    EnrollLinkMapper enrollLinkMapper;
    @Resource
    EnrollmentMapper enrollmentMapper;
    @Resource
    ICourseCatalogService iCourseCatalogService;

    @Override
    public Map<String, Object> detail(String token, String sessionId) {
        Integer lessonId = null;
        String normalizedToken = trimToEmpty(token);
        if (!normalizedToken.isEmpty()) {
            EnrollLink link = enrollLinkMapper.selectOne(new QueryWrapper<EnrollLink>()
                    .eq("token", normalizedToken)
                    .eq("status", 1)
                    .last("limit 1"));
            if (link == null) {
                return null;
            }
            if (link.getExpireTime() != null && link.getExpireTime() > 0
                    && link.getExpireTime() < TimeUtils.timestamp()) {
                return null;
            }
            lessonId = link.getSessionId();
        } else {
            lessonId = intValue(sessionId);
        }
        if (lessonId == null || lessonId <= 0) {
            return null;
        }
        Map<String, Object> session = iCourseCatalogService.session(lessonId);
        if (session == null) {
            return null;
        }
        Map<String, Object> result = new LinkedHashMap<>(session);
        int capacity = intValue(result.get("capacity"), 0);
        int booked = intValue(result.get("booked"), 0);
        int confirmed = enrollmentCount(lessonId, 1);
        int remaining = Math.max(capacity - booked - confirmed, 0);
        result.put("confirmedEnrollments", confirmed);
        result.put("enrollRemaining", remaining);
        result.put("remaining", remaining);
        result.put("full", capacity <= 0 || remaining <= 0);
        result.put("token", normalizedToken);
        return result;
    }

    @Override
    public Map<String, Object> submit(Integer userId, Map<String, Object> input) {
        if (input == null) {
            throw new OperateException("请填写报名信息");
        }
        String token = trimToEmpty(input.get("token"));
        Integer sessionId = intValue(input.get("sessionId"));

        EnrollLink link = null;
        if (!token.isEmpty()) {
            link = enrollLinkMapper.selectOne(new QueryWrapper<EnrollLink>()
                    .eq("token", token)
                    .eq("status", 1)
                    .last("limit 1"));
            if (link == null) {
                throw new OperateException("报名链接已失效，请联系客服重新获取");
            }
            if (link.getExpireTime() != null && link.getExpireTime() > 0
                    && link.getExpireTime() < TimeUtils.timestamp()) {
                throw new OperateException("报名链接已过期，请联系客服重新获取");
            }
            sessionId = link.getSessionId();
        }
        if (sessionId == null || sessionId <= 0) {
            throw new OperateException("报名链接不完整，请联系客服重新获取");
        }
        Map<String, Object> session = iCourseCatalogService.session(sessionId);
        if (session == null) {
            throw new OperateException("课程场次不存在或已下架，请联系客服");
        }

        String parentName = requiredText(input.get("parentName"), "请填写家长姓名", 50);
        String phone = trimToEmpty(input.get("phone"));
        if (!phone.matches("^1\\d{10}$")) {
            throw new OperateException("请填写正确的手机号");
        }
        String studentName = requiredText(input.get("studentName"), "请填写学员姓名", 50);
        String gender = trimToEmpty(input.get("gender"));
        if (!"男".equals(gender) && !"女".equals(gender)) {
            gender = "男";
        }
        Integer age = intValue(input.get("age"));
        if (age == null || age < AGE_MIN || age > AGE_MAX) {
            throw new OperateException("请填写 3-16 岁的年龄");
        }
        String note = trimToEmpty(input.get("note"));
        if (note.length() > 255) {
            note = note.substring(0, 255);
        }

        Long duplicate = enrollmentMapper.selectCount(new QueryWrapper<Enrollment>()
                .eq("session_id", sessionId)
                .eq("phone", phone)
                .eq("student_name", studentName));
        if (duplicate != null && duplicate > 0) {
            throw new OperateException("该学员已提交过本场次报名信息");
        }

        // 链接有效 ≠ 场次可报名：#38 补充场次状态、报名截止与满员提示。
        // 余位口径与课程详情一致（已被预约占用），另加客服已确认的报名人数；
        // 待确认的报名不占余位，避免同一链接多人提交时互相挤占。
        String sessionStatus = trimToEmpty(session.get("status"));
        if ("cancelled".equals(sessionStatus)) {
            throw new OperateException("本场次已取消，暂无法报名，请联系客服");
        }
        if ("finished".equals(sessionStatus)) {
            throw new OperateException("本场次已结束，无法报名，请联系客服");
        }
        String lessonDate = trimToEmpty(session.get("date"));
        String today = java.time.LocalDate.now().toString();
        if (!lessonDate.isEmpty() && lessonDate.compareTo(today) < 0) {
            throw new OperateException("本场次报名已截止，请联系客服了解其他场次");
        }
        int capacity = intValue(session.get("capacity"), 0);
        int booked = intValue(session.get("booked"), 0);
        int confirmed = enrollmentCount(sessionId, 1);
        if (capacity <= 0 || booked + confirmed >= capacity) {
            throw new OperateException("本场次名额已满，请联系客服安排其他场次");
        }

        long now = TimeUtils.timestamp();
        Enrollment enrollment = new Enrollment();
        enrollment.setLinkId(link == null ? 0 : link.getId());
        enrollment.setToken(token);
        enrollment.setSessionId(sessionId);
        enrollment.setCourseId(intValue(session.get("courseId")));
        enrollment.setParentName(parentName);
        enrollment.setPhone(phone);
        enrollment.setStudentName(studentName);
        enrollment.setGender(gender);
        enrollment.setAge(age);
        enrollment.setNote(note);
        enrollment.setStatus(0);
        enrollment.setUserId(userId == null || userId <= 0 ? 0 : userId);
        enrollment.setCreateTime(now);
        enrollment.setUpdateTime(now);
        try {
            enrollmentMapper.insert(enrollment);
        } catch (DuplicateKeyException e) {
            // 并发提交由唯一键兜底，文案与先查重一致。
            throw new OperateException("该学员已提交过本场次报名信息");
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("ok", true);
        result.put("id", enrollment.getId());
        result.put("sessionId", sessionId);
        result.put("courseId", enrollment.getCourseId());
        result.put("studentName", studentName);
        return result;
    }

    @Override
    public java.util.List<Map<String, Object>> mySubmissions(Integer userId, Integer sessionId) {
        java.util.List<Map<String, Object>> result = new java.util.ArrayList<>();
        if (userId == null || userId <= 0) {
            return result;
        }
        QueryWrapper<Enrollment> query = new QueryWrapper<Enrollment>()
                .eq("user_id", userId)
                .orderByDesc("id");
        if (sessionId != null && sessionId > 0) {
            query.eq("session_id", sessionId);
        }
        for (Enrollment enrollment : enrollmentMapper.selectList(query)) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("id", String.valueOf(enrollment.getId()));
            item.put("sessionId", String.valueOf(enrollment.getSessionId()));
            item.put("courseId", String.valueOf(enrollment.getCourseId()));
            item.put("parentName", enrollment.getParentName());
            item.put("phone", enrollment.getPhone());
            item.put("studentName", enrollment.getStudentName());
            item.put("gender", enrollment.getGender());
            item.put("age", enrollment.getAge());
            item.put("note", enrollment.getNote());
            item.put("status", enrollment.getStatus());
            item.put("createdAt", formatTime(enrollment.getCreateTime()));
            result.add(item);
        }
        return result;
    }

    private String formatTime(Long timestamp) {
        if (timestamp == null || timestamp <= 0) {
            return "";
        }
        return java.time.LocalDateTime.ofInstant(java.time.Instant.ofEpochSecond(timestamp),
                java.time.ZoneId.systemDefault())
                .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    private String requiredText(Object value, String message, int maxLength) {        String text = trimToEmpty(value);
        if (text.isEmpty()) {
            throw new OperateException(message);
        }
        return text.length() > maxLength ? text.substring(0, maxLength) : text;
    }

    private Integer intValue(Object value) {
        if (value == null) {
            return null;
        }
        try {
            String text = String.valueOf(value).trim();
            return text.isEmpty() ? null : Integer.parseInt(text);
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    private int intValue(Object value, int defaultValue) {
        Integer parsed = intValue(value);
        return parsed == null ? defaultValue : parsed;
    }

    /** 某场次指定状态的报名数（0=待确认，1=已确认，2=已拒绝；null=全部）。 */
    private int enrollmentCount(Integer sessionId, Integer status) {
        if (sessionId == null || sessionId <= 0) {
            return 0;
        }
        QueryWrapper<Enrollment> query = new QueryWrapper<Enrollment>().eq("session_id", sessionId);
        if (status != null) {
            query.eq("status", status);
        }
        Long count = enrollmentMapper.selectCount(query);
        return count == null ? 0 : count.intValue();
    }

    private String trimToEmpty(Object value) {
        return value == null ? "" : String.valueOf(value).trim();
    }
}
