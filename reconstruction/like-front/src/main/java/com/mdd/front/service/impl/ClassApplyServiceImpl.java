package com.mdd.front.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.mdd.common.entity.activity.ClassApply;
import com.mdd.common.entity.activity.ClassApplyGroup;
import com.mdd.common.entity.region.Region;
import com.mdd.common.entity.student.Student;
import com.mdd.common.exception.OperateException;
import com.mdd.common.mapper.activity.ClassApplyGroupMapper;
import com.mdd.common.mapper.activity.ClassApplyMapper;
import com.mdd.common.mapper.region.RegionMapper;
import com.mdd.common.mapper.student.StudentMapper;
import com.mdd.common.util.TimeUtils;
import com.mdd.front.service.IClassApplyService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 开班申请实现（T29.7 / #27）。
 *
 * <p>与小程序 mock 金样一致：</p>
 * <ul>
 *   <li>合并键 = 街道 + 课程类型（{@code uk_street_course} 唯一键兜底并发）；</li>
 *   <li>joined 是聚合组计数，达到 target=5 自动置 following；</li>
 *   <li>不收款、不扣券、不生成预约；正式课程后走买券/预约/核销主闭环。</li>
 * </ul>
 */
@Service
public class ClassApplyServiceImpl implements IClassApplyService {

    private static final int TARGET_JOINED = 5;
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Resource
    ClassApplyGroupMapper classApplyGroupMapper;
    @Resource
    ClassApplyMapper classApplyMapper;
    @Resource
    StudentMapper studentMapper;
    @Resource
    RegionMapper regionMapper;

    @Override
    public List<Map<String, Object>> applications(Integer userId) {
        List<Map<String, Object>> result = new ArrayList<>();
        if (userId == null || userId <= 0) {
            return result;
        }
        List<ClassApply> applies = classApplyMapper.selectList(new QueryWrapper<ClassApply>()
                .eq("user_id", userId)
                .isNull("delete_time")
                .orderByDesc("id"));
        if (applies.isEmpty()) {
            return result;
        }

        Set<Integer> groupIds = new HashSet<>();
        Set<Integer> studentIds = new HashSet<>();
        for (ClassApply apply : applies) {
            groupIds.add(apply.getGroupId());
            if (apply.getStudentId() != null && apply.getStudentId() > 0) {
                studentIds.add(apply.getStudentId());
            }
        }
        Map<Integer, ClassApplyGroup> groups = new HashMap<>();
        if (!groupIds.isEmpty()) {
            for (ClassApplyGroup group : classApplyGroupMapper.selectBatchIds(groupIds)) {
                groups.put(group.getId(), group);
            }
        }
        Set<Integer> streetIds = new HashSet<>();
        for (ClassApplyGroup group : groups.values()) {
            if (group.getStreetId() != null && group.getStreetId() > 0) {
                streetIds.add(group.getStreetId());
            }
        }
        Map<Integer, Region> regions = mapRegions(streetIds);
        Map<Integer, String> studentNames = new HashMap<>();
        if (!studentIds.isEmpty()) {
            for (Student student : studentMapper.selectBatchIds(studentIds)) {
                studentNames.put(student.getId(), student.getName());
            }
        }

        for (ClassApply apply : applies) {
            ClassApplyGroup group = groups.get(apply.getGroupId());
            int streetId = group == null || group.getStreetId() == null ? 0 : group.getStreetId();
            Region street = regions.get(streetId);
            Region district = street == null ? null : regions.get(street.getParentId());
            int joined = group == null || group.getJoined() == null ? 0 : group.getJoined();
            int target = group == null || group.getTarget() == null ? TARGET_JOINED : group.getTarget();
            String status = group == null ? ClassApplyGroup.STATUS_PENDING : group.getStatus();

            Map<String, Object> item = new LinkedHashMap<>();
            item.put("id", String.valueOf(apply.getId()));
            item.put("groupId", String.valueOf(apply.getGroupId()));
            item.put("areaId", String.valueOf(apply.getAreaId() == null ? 0 : apply.getAreaId()));
            item.put("areaName", nvl(apply.getAreaName()));
            item.put("streetId", String.valueOf(streetId));
            item.put("streetName", street == null ? "" : nvl(street.getName()));
            item.put("districtName", district == null ? "" : nvl(district.getName()));
            item.put("communityName", "");
            item.put("studentId", String.valueOf(apply.getStudentId()));
            item.put("studentName", studentNames.getOrDefault(apply.getStudentId(), ""));
            item.put("courseType", group == null ? "" : nvl(group.getCourseType()));
            item.put("expectTime", nvl(apply.getExpectTime()));
            item.put("phone", nvl(apply.getPhone()));
            item.put("note", nvl(apply.getNote()));
            item.put("status", status);
            item.put("statusText", statusText(status));
            item.put("createdAt", formatTime(apply.getCreateTime()));
            item.put("joined", joined);
            item.put("need", Math.max(target - joined, 0));
            result.add(item);
        }
        return result;
    }

    @Override
    @Transactional
    public Map<String, Object> create(Integer userId, Map<String, Object> input) {
        if (userId == null || userId <= 0) {
            throw new OperateException("请先登录");
        }
        if (input == null) {
            throw new OperateException("请填写开班申请");
        }
        Integer streetId = intValue(input.get("streetId"));
        Region street = streetId == null || streetId <= 0 ? null : regionMapper.selectById(streetId);
        if (street == null || (street.getLevel() != null && street.getLevel() != 3)
                || (street.getStatus() != null && street.getStatus() == 0)
                || street.getDeleteTime() != null) {
            throw new OperateException("请选择街道");
        }
        Integer studentId = intValue(input.get("studentId"));
        Student student = studentId == null || studentId <= 0 ? null : studentMapper.selectById(studentId);
        if (student == null || student.getDeleteTime() != null || !userId.equals(student.getUserId())) {
            throw new OperateException("学员不存在或不属于当前账号");
        }
        String courseType = requiredText(input.get("courseType"), "请选择期望课程", 30);
        String expectTime = requiredText(input.get("expectTime"), "请选择期望时间", 30);
        String phone = trimToEmpty(input.get("phone"));
        if (!phone.matches("^1\\d{10}$")) {
            throw new OperateException("请填写正确手机号");
        }
        String areaName = trimToEmpty(input.get("areaName"));
        if (areaName.length() > 64) {
            areaName = areaName.substring(0, 64);
        }
        String note = trimToEmpty(input.get("note"));
        if (note.length() > 255) {
            note = note.substring(0, 255);
        }
        Integer areaId = intValue(input.get("areaId"));
        long now = TimeUtils.timestamp();

        ClassApplyGroup group = classApplyGroupMapper.selectOne(new QueryWrapper<ClassApplyGroup>()
                .eq("street_id", streetId)
                .eq("course_type", courseType)
                .last("limit 1"));
        boolean merged = group != null;
        if (group == null) {
            group = new ClassApplyGroup();
            group.setStreetId(streetId);
            group.setCourseType(courseType);
            group.setJoined(0);
            group.setTarget(TARGET_JOINED);
            group.setStatus(ClassApplyGroup.STATUS_PENDING);
            group.setCreateTime(now);
            group.setUpdateTime(now);
            try {
                classApplyGroupMapper.insert(group);
            } catch (DuplicateKeyException e) {
                // 并发首单：唯一键兜底，回读同一聚合组后继续累加。
                group = classApplyGroupMapper.selectOne(new QueryWrapper<ClassApplyGroup>()
                        .eq("street_id", streetId)
                        .eq("course_type", courseType)
                        .last("limit 1"));
                merged = true;
            }
        }
        if (group == null) {
            throw new OperateException("开班申请提交失败，请稍后重试");
        }
        classApplyGroupMapper.update(null, new UpdateWrapper<ClassApplyGroup>()
                .setSql("joined = joined + 1")
                .set("update_time", now)
                .eq("id", group.getId()));
        group = classApplyGroupMapper.selectById(group.getId());
        int joined = group.getJoined() == null ? 0 : group.getJoined();
        if (joined >= TARGET_JOINED && ClassApplyGroup.STATUS_PENDING.equals(group.getStatus())) {
            group.setStatus(ClassApplyGroup.STATUS_FOLLOWING);
            group.setUpdateTime(now);
            classApplyGroupMapper.updateById(group);
        }

        ClassApply apply = new ClassApply();
        apply.setGroupId(group.getId());
        apply.setUserId(userId);
        apply.setStudentId(studentId);
        apply.setAreaId(areaId == null || areaId < 0 ? 0 : areaId);
        apply.setAreaName(areaName);
        apply.setExpectTime(expectTime);
        apply.setPhone(phone);
        apply.setNote(note);
        apply.setStatus(group.getStatus());
        apply.setCreateTime(now);
        apply.setUpdateTime(now);
        classApplyMapper.insert(apply);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("ok", true);
        result.put("merged", merged);
        result.put("id", apply.getId());
        result.put("groupId", group.getId());
        result.put("joined", joined);
        result.put("need", Math.max(TARGET_JOINED - joined, 0));
        result.put("status", group.getStatus());
        result.put("statusText", statusText(group.getStatus()));
        return result;
    }

    private Map<Integer, Region> mapRegions(Set<Integer> ids) {
        Map<Integer, Region> regions = new HashMap<>();
        Set<Integer> queryIds = new HashSet<>(ids);
        if (queryIds.isEmpty()) {
            return regions;
        }
        List<Region> streets = regionMapper.selectBatchIds(queryIds);
        for (Region region : streets) {
            regions.put(region.getId(), region);
        }
        Set<Integer> parentIds = new HashSet<>();
        for (Region street : streets) {
            if (street.getParentId() != null && street.getParentId() > 0) {
                parentIds.add(street.getParentId());
            }
        }
        if (!parentIds.isEmpty()) {
            for (Region district : regionMapper.selectBatchIds(parentIds)) {
                regions.put(district.getId(), district);
            }
        }
        return regions;
    }

    private String statusText(String status) {
        if (ClassApplyGroup.STATUS_FOLLOWING.equals(status)) {
            return "客服跟进中";
        }
        if (ClassApplyGroup.STATUS_CLOSED.equals(status)) {
            return "已开班";
        }
        return "招募中";
    }

    private String requiredText(Object value, String message, int maxLength) {
        String text = trimToEmpty(value);
        if (text.isEmpty()) {
            throw new OperateException(message);
        }
        return text.length() > maxLength ? text.substring(0, maxLength) : text;
    }

    private String formatTime(Long timestamp) {
        if (timestamp == null || timestamp <= 0) {
            return "";
        }
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneId.systemDefault()).format(TIME_FMT);
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

    private String trimToEmpty(Object value) {
        return value == null ? "" : String.valueOf(value).trim();
    }

    private String nvl(String text) {
        return text == null ? "" : text;
    }
}
