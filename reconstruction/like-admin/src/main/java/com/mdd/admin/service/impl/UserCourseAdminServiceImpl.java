package com.mdd.admin.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
import com.mdd.common.entity.student.Student;
import com.mdd.common.entity.user.User;
import com.mdd.common.exception.OperateException;
import com.mdd.common.mapper.course.CourseClassMapper;
import com.mdd.common.mapper.course.UserCourseMapper;
import com.mdd.common.mapper.student.StudentMapper;
import com.mdd.common.mapper.user.UserMapper;
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
        Map<Integer, Integer> systemUsedMap = loadSystemUsed(rows);

        LinkedList<UserCourseListVo> list = new LinkedList<>();
        for (UserCourse uc : rows) {
            list.add(toVo(uc, userMap, studentMap, classMap, systemUsedMap));
        }
        return PageResult.iPageHandle(iPage.getTotal(), iPage.getCurrent(), iPage.getSize(), list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void adjust(UserCourseAdjustValidate adjustValidate) {
        UserCourse uc = userCourseMapper.selectByIdForUpdate(adjustValidate.getId());
        if (uc == null) {
            throw new OperateException("课包不存在或已删除");
        }
        UserCourseLessonAdjustment.Result result;
        try {
            result = UserCourseLessonAdjustment.resolve(
                    nonNegative(uc.getTotalLessons()),
                    nonNegative(uc.getRemainLessons()),
                    adjustValidate.getTotalLessons(),
                    adjustValidate.getRemainLessons());
        } catch (IllegalArgumentException e) {
            throw new OperateException(e.getMessage());
        }
        uc.setTotalLessons(result.getTotalLessons());
        uc.setUsedLessons(result.getUsedLessons());
        uc.setRemainLessons(result.getRemainLessons());
        uc.setUpdateTime(TimeUtils.timestamp());
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
     * 「学员详情」口径的已上/剩余课时：按消课流水 + 退款状态 + FIFO 归属重算，
     * 与 {@code UserController#account} 同源；失败时降级为空表，不影响账面列表展示。
     */
    private Map<Integer, Integer> loadSystemUsed(List<UserCourse> rows) {
        try {
            List<Integer> userIds = rows.stream().map(UserCourse::getUserId)
                    .filter(id -> id != null && id > 0).distinct().collect(Collectors.toList());
            return userCourseAccountService.countEffectiveUsedLessonsByUserIds(userIds);
        } catch (RuntimeException e) {
            log.warn("老课包列表计算系统已上课时失败，已降级为空", e);
            return Collections.emptyMap();
        }
    }

    private UserCourseListVo toVo(UserCourse uc, Map<Integer, User> userMap,
                                  Map<Integer, Student> studentMap, Map<Integer, CourseClass> classMap,
                                  Map<Integer, Integer> systemUsedMap) {
        int total = nonNegative(uc.getTotalLessons());
        int remain = Math.min(nonNegative(uc.getRemainLessons()), total);
        int systemUsed = Math.min(nonNegative(systemUsedMap.get(uc.getId())), total);

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
        vo.setUsedLessons(total - remain);
        vo.setRemainLessons(remain);
        vo.setSystemUsedLessons(systemUsed);
        vo.setSystemRemainLessons(Math.max(total - systemUsed, 0));
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
