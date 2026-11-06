package com.mdd.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mdd.admin.LikeAdminThreadLocal;
import com.mdd.admin.mapper.UserCourseQueryMapper;
import com.mdd.admin.service.IUserCourseAdminService;
import com.mdd.admin.service.IUserCourseAccountService;
import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.validate.user.UserCourseAdjustValidate;
import com.mdd.admin.validate.user.UserCourseSearchValidate;
import com.mdd.admin.vo.user.UserCourseListVo;
import com.mdd.common.core.PageResult;
import com.mdd.common.entity.course.CourseClass;
import com.mdd.common.entity.course.UserCourse;
import com.mdd.common.entity.course.UserCourseAdjustLog;
import com.mdd.common.entity.student.Student;
import com.mdd.common.entity.user.User;
import com.mdd.common.exception.OperateException;
import com.mdd.common.mapper.course.CourseClassMapper;
import com.mdd.common.mapper.course.UserCourseAdjustLogMapper;
import com.mdd.common.mapper.course.UserCourseMapper;
import com.mdd.common.mapper.student.StudentMapper;
import com.mdd.common.mapper.user.UserMapper;
import com.mdd.common.service.CourseConsumeEffectiveService;
import com.mdd.common.util.CourseDerivedBalance;
import com.mdd.common.util.TimeUtils;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserCourseAdminServiceImpl implements IUserCourseAdminService {

    private static final Logger log = LoggerFactory.getLogger(UserCourseAdminServiceImpl.class);

    @Resource
    UserCourseQueryMapper userCourseQueryMapper;
    @Resource
    UserCourseMapper userCourseMapper;
    @Resource
    UserMapper userMapper;
    @Resource
    StudentMapper studentMapper;
    @Resource
    CourseClassMapper courseClassMapper;
    @Resource
    IUserCourseAccountService userCourseAccountService;
    @Resource
    CourseConsumeEffectiveService courseConsumeEffectiveService;
    @Resource
    UserCourseAdjustLogMapper userCourseAdjustLogMapper;

    @Override
    public PageResult<UserCourseListVo> list(PageValidate pageValidate, UserCourseSearchValidate searchValidate) {
        Page<UserCourse> page = new Page<>(pageValidate.getPage_no(), pageValidate.getPage_size());
        IPage<UserCourse> iPage = userCourseQueryMapper.page(page, searchValidate);
        List<UserCourse> rows = iPage.getRecords();
        if (rows.isEmpty()) {
            return PageResult.iPageHandle(iPage.getTotal(), iPage.getCurrent(), iPage.getSize(),
                    Collections.emptyList());
        }

        Map<Integer, User> userMap = loadUsers(rows);
        Map<Integer, Student> studentMap = loadStudents(rows);
        Map<Integer, CourseClass> classMap = loadClasses(rows);
        Map<Integer, CourseDerivedBalance> derivedMap = loadDerivedBalances(rows);

        LinkedList<UserCourseListVo> list = new LinkedList<>();
        for (UserCourse uc : rows) {
            list.add(toVo(uc, userMap, studentMap, classMap, derivedMap));
        }
        return PageResult.iPageHandle(iPage.getTotal(), iPage.getCurrent(), iPage.getSize(), list);
    }

    /**
     * 调整课时（#222 流水口径）：以「流水派生已用」为锚点写调整流水
     * （la_user_course_adjust_log，含调整前后值与原因），再同步 la_user_course 三列
     * （字段保留为展示缓存，派生值优先）。派生重放 fifo + Σchange_used = 写入 used。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void adjust(UserCourseAdjustValidate adjustValidate) {
        UserCourse uc = userCourseMapper.selectByIdForUpdate(adjustValidate.getId());
        if (uc == null) {
            throw new OperateException("课包不存在或已删除");
        }
        int oldTotal = nonNegative(uc.getTotalLessons());
        int oldRemain = nonNegative(uc.getRemainLessons());
        int fifoUsed = courseConsumeEffectiveService.countEffectiveUsedLessonsForEnrollment(uc);
        int priorDelta = courseConsumeEffectiveService.sumAdjustUsedDelta(uc.getId());
        UserCourseAdjustDerivedPlan.Plan plan;
        try {
            plan = UserCourseAdjustDerivedPlan.resolve(oldTotal, oldRemain, fifoUsed, priorDelta,
                    adjustValidate.getTotalLessons(), adjustValidate.getRemainLessons());
        } catch (IllegalArgumentException e) {
            throw new OperateException(e.getMessage());
        }
        long now = TimeUtils.timestamp();

        int rawDerivedUsed = fifoUsed + priorDelta;
        // 快照用于审计展示：clamp 到合法区间；Δused 保持未 clamp 锚点，保证派生重放精确
        int beforeUsed = Math.max(0, Math.min(rawDerivedUsed, oldTotal));
        int beforeRemain = Math.max(oldTotal - beforeUsed, 0);

        UserCourseAdjustLog adjustLog = new UserCourseAdjustLog();
        adjustLog.setUserId(uc.getUserId());
        adjustLog.setStudentId(uc.getStudentId());
        adjustLog.setUserCourseId(uc.getId());
        adjustLog.setCourseClassId(uc.getCourseClassId());
        adjustLog.setBeforeTotal(oldTotal);
        adjustLog.setBeforeUsed(beforeUsed);
        adjustLog.setBeforeRemain(beforeRemain);
        adjustLog.setAfterTotal(plan.getTotalLessons());
        adjustLog.setAfterUsed(plan.getUsedLessons());
        adjustLog.setAfterRemain(plan.getRemainLessons());
        adjustLog.setChangeTotal(plan.getTotalLessons() - oldTotal);
        adjustLog.setChangeUsed(plan.getChangeUsed());
        adjustLog.setChangeRemain(plan.getRemainLessons() - beforeRemain);
        adjustLog.setRemark(adjustValidate.getRemark());
        adjustLog.setAdminId(LikeAdminThreadLocal.getAdminId());
        adjustLog.setCreateTime(Long.valueOf(now));
        userCourseAdjustLogMapper.insert(adjustLog);

        uc.setTotalLessons(plan.getTotalLessons());
        uc.setUsedLessons(plan.getUsedLessons());
        uc.setRemainLessons(plan.getRemainLessons());
        uc.setUpdateTime(Long.valueOf(now));
        userCourseMapper.updateById(uc);
    }

    private Map<Integer, User> loadUsers(List<UserCourse> rows) {
        List<Integer> ids = rows.stream().map(UserCourse::getUserId)
                .filter(id -> id != null && id > 0).distinct().collect(Collectors.toList());
        if (ids.isEmpty()) {
            return Collections.emptyMap();
        }
        return userMapper.selectBatchIds(ids).stream()
                .collect(Collectors.toMap(User::getId, Function.identity(), (a, b) -> a));
    }

    private Map<Integer, Student> loadStudents(List<UserCourse> rows) {
        List<Integer> ids = rows.stream().map(UserCourse::getStudentId)
                .filter(id -> id != null && id > 0).distinct().collect(Collectors.toList());
        if (ids.isEmpty()) {
            return Collections.emptyMap();
        }
        return studentMapper.selectBatchIds(ids).stream()
                .filter(s -> s.getDeleteTime() == null)
                .collect(Collectors.toMap(Student::getId, Function.identity(), (a, b) -> a));
    }

    private Map<Integer, CourseClass> loadClasses(List<UserCourse> rows) {
        List<Integer> ids = rows.stream().map(UserCourse::getCourseClassId)
                .filter(id -> id != null && id > 0).distinct().collect(Collectors.toList());
        if (ids.isEmpty()) {
            return Collections.emptyMap();
        }
        return courseClassMapper.selectBatchIds(ids).stream()
                .collect(Collectors.toMap(CourseClass::getId, Function.identity(), (a, b) -> a));
    }

    /**
     * 「流水派生」余额（#222）：消课流水 FIFO + 调整流水增量，used/remain 展示值以此为准。
     * 与 {@code UserController#account} 同源；失败时降级为空表，不影响账面列表展示。
     */
    private Map<Integer, CourseDerivedBalance> loadDerivedBalances(List<UserCourse> rows) {
        try {
            List<Integer> userIds = rows.stream().map(UserCourse::getUserId)
                    .filter(id -> id != null && id > 0).distinct().collect(Collectors.toList());
            if (userIds.isEmpty()) {
                return Collections.emptyMap();
            }
            // FIFO 分配需要同一用户下的全部课包，不能只看当前分页行
            List<UserCourse> enrollments = userCourseMapper.selectList(
                    new QueryWrapper<UserCourse>()
                            .in("user_id", userIds.toArray()).isNull("delete_time"));
            return courseConsumeEffectiveService.deriveBalanceForUserRows(enrollments);
        } catch (RuntimeException e) {
            log.warn("老课包列表计算流水派生课时失败，已降级为空", e);
            return Collections.emptyMap();
        }
    }

    private UserCourseListVo toVo(UserCourse uc, Map<Integer, User> userMap,
                                  Map<Integer, Student> studentMap, Map<Integer, CourseClass> classMap,
                                  Map<Integer, CourseDerivedBalance> derivedMap) {
        int total = nonNegative(uc.getTotalLessons());
        int fieldRemain = Math.min(nonNegative(uc.getRemainLessons()), total);
        int fieldUsed = total - fieldRemain;
        CourseDerivedBalance derived = derivedMap.get(uc.getId());
        int derivedUsed = derived != null ? derived.getUsedLessons() : fieldUsed;
        int derivedRemain = derived != null ? derived.getRemainLessons() : fieldRemain;
        boolean refunded = uc.getStatus() != null && uc.getStatus() == 2;

        User user = userMap.get(uc.getUserId());
        Student student = studentMap.get(uc.getStudentId());
        CourseClass courseClass = classMap.get(uc.getCourseClassId());

        UserCourseListVo vo = new UserCourseListVo();
        vo.setId(uc.getId());
        vo.setUserId(uc.getUserId());
        vo.setStudentId(uc.getStudentId());
        vo.setStudentName(resolveStudentName(student, user, uc.getUserId()));
        vo.setMobile(user != null && user.getMobile() != null ? user.getMobile() : "");
        vo.setCourseClassId(uc.getCourseClassId());
        vo.setClassName(courseClass != null && courseClass.getName() != null ? courseClass.getName() : "");
        vo.setTotalLessons(total);
        // 展示值 = 流水派生（已退课的结转账面保持不变）
        vo.setUsedLessons(refunded ? fieldUsed : derivedUsed);
        vo.setRemainLessons(refunded ? fieldRemain : derivedRemain);
        vo.setFieldUsedLessons(fieldUsed);
        vo.setFieldRemainLessons(fieldRemain);
        vo.setFieldMismatch(!refunded && (derivedUsed != fieldUsed || derivedRemain != fieldRemain));
        vo.setSystemUsedLessons(refunded ? fieldUsed : derivedUsed);
        vo.setSystemRemainLessons(refunded ? fieldRemain : derivedRemain);
        vo.setStatus(uc.getStatus());
        vo.setStatusText(statusText(uc.getStatus()));
        vo.setCreateTime(TimeUtils.timestampToDate(uc.getCreateTime()));
        vo.setUpdateTime(TimeUtils.timestampToDate(uc.getUpdateTime()));
        return vo;
    }

    private String resolveStudentName(Student student, User user, Integer userId) {
        if (student != null && isNotBlank(student.getName())) {
            return student.getName();
        }
        if (user != null && isNotBlank(user.getRealName())) {
            return user.getRealName();
        }
        if (user != null && isNotBlank(user.getNickname())) {
            return user.getNickname();
        }
        return "用户" + (userId != null ? userId : "");
    }

    private String statusText(Integer status) {
        if (status == null || status == 0) {
            return "学习中";
        }
        if (status == 1) {
            return "已完成";
        }
        if (status == 2) {
            return "已退课";
        }
        return "未知";
    }

    private static boolean isNotBlank(String value) {
        return value != null && value.trim().length() > 0;
    }

    private static int nonNegative(Integer value) {
        return value != null && value > 0 ? value : 0;
    }
}
