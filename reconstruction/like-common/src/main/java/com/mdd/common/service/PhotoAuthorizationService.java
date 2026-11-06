package com.mdd.common.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mdd.common.entity.photo.PhotoAuthorization;
import com.mdd.common.mapper.photo.PhotoAuthorizationMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * 课堂照片授权台账公共能力（#214 P0）。
 *
 * <p>家长端（like-front）写授权/撤回、教练核销读授权状态；后台（like-admin）只读展示，
 * 两边共用本服务，保证同一份口径。一个学员一行（uk_student_id），
 * 授权与撤回都是 upsert，不做历史多行——字段 authorized_at / revoked_at 记录最近一次动作时间。</p>
 */
@Service
public class PhotoAuthorizationService {

    public static final String OPERATOR_PARENT = "parent";
    public static final String OPERATOR_COACH = "coach";
    public static final String OPERATOR_ADMIN = "admin";

    public static final String SOURCE_MINIPROGRAM = "miniprogram";
    public static final String SOURCE_COACH_CHECKIN = "coach_checkin";
    public static final String SOURCE_ADMIN = "admin";

    /** 教练核销跳过照片的统一提示（小程序弹窗与后端响应共用同一文案）。 */
    public static final String PHOTO_SKIP_MESSAGE = "该学员未授权课堂照片，已跳过照片";

    @Resource
    PhotoAuthorizationMapper photoAuthorizationMapper;

    /**
     * 查询学员最新授权状态，无台账行时返回 null（调用方按未授权处理）。
     */
    public PhotoAuthorization get(Integer studentId) {
        if (studentId == null || studentId <= 0) {
            return null;
        }
        return photoAuthorizationMapper.selectOne(new QueryWrapper<PhotoAuthorization>()
                .eq("student_id", studentId)
                .last("limit 1"));
    }

    /**
     * 是否已授权；无台账行 = 未授权（PIPL 默认关闭口径）。
     */
    public boolean isAuthorized(Integer studentId) {
        PhotoAuthorization row = get(studentId);
        return row != null && row.getAuthorized() != null && row.getAuthorized() == 1;
    }

    /**
     * 批量判断授权状态，供核销名单/提交一次性读取，避免逐人查库。
     *
     * @return studentId → 是否已授权（未出现在返回 map 的学员按未授权处理）
     */
    public Map<Integer, Boolean> authorizedByStudentIds(Collection<Integer> studentIds) {
        if (studentIds == null || studentIds.isEmpty()) {
            return Collections.emptyMap();
        }
        Set<Integer> ids = new LinkedHashSet<>();
        for (Integer studentId : studentIds) {
            if (studentId != null && studentId > 0) {
                ids.add(studentId);
            }
        }
        if (ids.isEmpty()) {
            return Collections.emptyMap();
        }
        Map<Integer, Boolean> result = new HashMap<>();
        for (PhotoAuthorization row : photoAuthorizationMapper.selectList(
                new QueryWrapper<PhotoAuthorization>().in("student_id", ids))) {
            result.put(row.getStudentId(), row.getAuthorized() != null && row.getAuthorized() == 1);
        }
        return result;
    }

    /**
     * upsert 授权状态。授权：authorized=1、authorized_at=now、清空 revoked_at；
     * 撤回：authorized=0、revoked_at=now（保留 authorized_at）。
     *
     * <p>并发：方法级 synchronized + 事务内 {@code SELECT ... FOR UPDATE} 串行化；
     * 表上 uk_student_id 是最终防线（多实例部署时唯一键兜底）。</p>
     */
    @Transactional(rollbackFor = Exception.class)
    public synchronized PhotoAuthorization save(Integer studentId, Integer userId, boolean authorized,
                                                String operatorType, Integer operatorId, String source) {
        int student = studentId == null ? 0 : studentId;
        if (student <= 0) {
            throw new IllegalArgumentException("studentId 不能为空");
        }
        int now = (int) (System.currentTimeMillis() / 1000L);
        PhotoAuthorization row = photoAuthorizationMapper.selectOne(new QueryWrapper<PhotoAuthorization>()
                .eq("student_id", student)
                .last("limit 1 for update"));
        if (row == null) {
            row = new PhotoAuthorization();
            row.setUserId(userId == null ? 0 : userId);
            row.setStudentId(student);
            row.setAuthorized(authorized ? 1 : 0);
            if (authorized) {
                row.setAuthorizedAt(now);
                row.setRevokedAt(null);
            } else {
                row.setRevokedAt(now);
            }
            row.setOperatorType(operatorType == null ? OPERATOR_PARENT : operatorType);
            row.setOperatorId(operatorId == null ? 0 : operatorId);
            row.setSource(source == null ? SOURCE_MINIPROGRAM : source);
            row.setCreateTime(now);
            row.setUpdateTime(now);
            photoAuthorizationMapper.insert(row);
            return row;
        }
        if (userId != null && userId > 0) {
            row.setUserId(userId);
        }
        row.setAuthorized(authorized ? 1 : 0);
        if (authorized) {
            row.setAuthorizedAt(now);
            row.setRevokedAt(null);
        } else {
            row.setRevokedAt(now);
        }
        row.setOperatorType(operatorType == null ? OPERATOR_PARENT : operatorType);
        row.setOperatorId(operatorId == null ? 0 : operatorId);
        row.setSource(source == null ? SOURCE_MINIPROGRAM : source);
        row.setUpdateTime(now);
        photoAuthorizationMapper.updateById(row);
        return row;
    }

    /**
     * 统一响应结构（小程序/后台共用）。字段名与接口契约保持一致：
     * studentId 字符串、authorized 布尔、时间戳为秒（未发生为 null）。
     */
    public Map<String, Object> toMap(PhotoAuthorization row) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("studentId", row == null || row.getStudentId() == null
                ? "" : String.valueOf(row.getStudentId()));
        map.put("userId", row == null || row.getUserId() == null
                ? "" : String.valueOf(row.getUserId()));
        map.put("authorized", row != null && row.getAuthorized() != null && row.getAuthorized() == 1);
        map.put("authorizedAt", row == null ? null : row.getAuthorizedAt());
        map.put("revokedAt", row == null ? null : row.getRevokedAt());
        map.put("operatorType", row == null ? "" : nvl(row.getOperatorType()));
        map.put("operatorId", row == null || row.getOperatorId() == null
                ? "" : String.valueOf(row.getOperatorId()));
        map.put("source", row == null ? "" : nvl(row.getSource()));
        map.put("updateTime", row == null ? null : row.getUpdateTime());
        return map;
    }

    private String nvl(String text) {
        return text == null ? "" : text;
    }
}
