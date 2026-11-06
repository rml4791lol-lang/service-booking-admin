package com.mdd.front.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mdd.common.entity.LeaveApplication;
import com.mdd.common.entity.RechargeOrder;
import com.mdd.common.entity.course.CourseBooking;
import com.mdd.common.entity.course.CourseConsumeLog;
import com.mdd.common.entity.course.CourseManualConsume;
import com.mdd.common.entity.course.UserCourse;
import com.mdd.common.entity.student.Student;
import com.mdd.common.exception.OperateException;
import com.mdd.common.mapper.LeaveApplicationMapper;
import com.mdd.common.mapper.RechargeOrderMapper;
import com.mdd.common.mapper.course.CourseBookingMapper;
import com.mdd.common.mapper.course.CourseConsumeLogMapper;
import com.mdd.common.mapper.course.CourseManualConsumeMapper;
import com.mdd.common.mapper.course.UserCourseMapper;
import com.mdd.common.mapper.student.StudentMapper;
import com.mdd.common.util.StringUtils;
import com.mdd.common.util.TimeUtils;
import com.mdd.common.util.UrlUtils;
import com.mdd.front.service.IStudentService;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 家长端学员管理。
 *
 * <p>业务口径（对齐小程序 services/db.js 与需求 v2 4.1）：</p>
 * <ul>
 *     <li>每个家长账号最多 3 名有效学员，第 4 名返回明确错误；</li>
 *     <li>所有查询/修改都带 user_id，不能跨账号操作；</li>
 *     <li>有订单、预约、消课/请假、券等业务引用的学员只能软删（停用），无引用才物理删除；</li>
 *     <li>软删后 la_student.delete_time 非空，列表不再返回，历史业务行保留。</li>
 * </ul>
 */
@Service
public class StudentServiceImpl implements IStudentService {

    private static final String MAX_STUDENT_MESSAGE = "每个家长账号最多添加 3 名学员";
    private static final String DEACTIVATE_MESSAGE = "该学员已有上课或预约记录，已停用，历史记录保留";
    private static final String DELETE_MESSAGE = "学员已删除";

    @Resource
    StudentMapper studentMapper;
    @Resource
    UserCourseMapper userCourseMapper;
    @Resource
    CourseBookingMapper courseBookingMapper;
    @Resource
    CourseConsumeLogMapper courseConsumeLogMapper;
    @Resource
    CourseManualConsumeMapper courseManualConsumeMapper;
    @Resource
    LeaveApplicationMapper leaveApplicationMapper;
    @Resource
    RechargeOrderMapper rechargeOrderMapper;

    @Override
    public List<Map<String, Object>> list(Integer userId) {
        List<Student> students = this.studentMapper.selectList(new QueryWrapper<Student>()
                .eq("user_id", (Object) userId)
                .isNull("delete_time")
                .orderByDesc("is_default")
                .orderByAsc("id"));
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        for (Student student : students) {
            result.add(this.buildStudentMap(student));
        }
        return result;
    }

    @Override
    public Map<String, Object> getDefault(Integer userId) {
        Student student = (Student) this.studentMapper.selectOne(new QueryWrapper<Student>()
                .eq("user_id", (Object) userId)
                .eq("is_default", (Object) 1)
                .isNull("delete_time")
                .last("limit 1"));
        return student == null ? null : this.buildStudentMap(student);
    }

    @Override
    @Transactional
    public synchronized Map<String, Object> add(Integer userId, Map<String, Object> params) {
        String name = this.requireName(params);
        Integer gender = this.parseGender(params.get("gender"), 0);
        String birthday = this.optionalString(params, "birthday");
        Integer age = this.parseAge(params.get("age"));
        this.validateBirthdayAndAge(birthday, age);
        String idCard = this.optionalString(params, "idCard");
        this.validateIdCard(idCard, false);
        String note = this.optionalString(params, "note");
        this.validateNote(note);
        String avatar = this.optionalString(params, "avatar");

        Long activeCount = this.studentMapper.selectCount(new QueryWrapper<Student>()
                .eq("user_id", (Object) userId)
                .isNull("delete_time"));
        if (activeCount != null && activeCount >= 3L) {
            throw new OperateException(MAX_STUDENT_MESSAGE);
        }

        Student student = new Student();
        student.setUserId(userId);
        student.setName(name);
        student.setIdCard(idCard);
        student.setGender(gender);
        student.setBirthday(birthday);
        student.setAge(this.resolveAge(birthday, age));
        student.setAvatar(avatar);
        student.setNote(note);
        student.setCreateTime(TimeUtils.timestamp());
        student.setUpdateTime(TimeUtils.timestamp());
        student.setIsDefault(Integer.valueOf(activeCount == null || activeCount == 0L ? 1 : 0));
        this.studentMapper.insert(student);

        LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>();
        result.put("id", String.valueOf(student.getId()));
        return result;
    }

    @Override
    @Transactional
    public void edit(Integer userId, Map<String, Object> params) {
        Integer id = this.requireId(params.get("id"));
        Student student = this.getStudentByIdAndUser(id, userId);

        if (params.containsKey("name")) {
            student.setName(this.requireName(params));
        }
        if (params.containsKey("idCard")) {
            String idCard = this.optionalString(params, "idCard");
            this.validateIdCard(idCard, true);
            student.setIdCard(idCard);
        }
        if (params.containsKey("gender")) {
            student.setGender(this.parseGender(params.get("gender"), student.getGender() == null ? 0 : student.getGender()));
        }
        String birthday = params.containsKey("birthday")
                ? this.optionalString(params, "birthday")
                : (student.getBirthday() == null ? "" : student.getBirthday());
        Integer inputAge = params.containsKey("age") ? this.parseAge(params.get("age")) : student.getAge();
        this.validateBirthdayAndAge(birthday, inputAge);
        student.setBirthday(birthday);
        student.setAge(this.resolveAge(birthday, inputAge));
        if (params.containsKey("avatar")) {
            student.setAvatar(this.optionalString(params, "avatar"));
        }
        if (params.containsKey("note")) {
            String note = this.optionalString(params, "note");
            this.validateNote(note);
            student.setNote(note);
        }
        student.setUpdateTime(TimeUtils.timestamp());
        this.studentMapper.updateById(student);
    }

    @Override
    @Transactional
    public Map<String, Object> del(Integer userId, Map<String, Object> params) {
        Integer id = this.requireId(params.get("id"));
        Student student = this.getStudentByIdAndUser(id, userId);
        boolean wasDefault = student.getIsDefault() != null && student.getIsDefault().intValue() == 1;
        boolean referenced = this.hasBusinessReference(id);

        if (referenced) {
            Student update = new Student();
            update.setId(id);
            update.setDeleteTime(TimeUtils.timestamp());
            update.setUpdateTime(TimeUtils.timestamp());
            this.studentMapper.updateById(update);
        } else {
            this.studentMapper.deleteById(id);
        }
        if (wasDefault) {
            this.resetDefault(userId);
        }

        LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>();
        result.put("ok", Boolean.TRUE);
        result.put("deactivated", Boolean.valueOf(referenced));
        result.put("message", referenced ? DEACTIVATE_MESSAGE : DELETE_MESSAGE);
        return result;
    }

    @Override
    @Transactional
    public void setDefault(Integer userId, Map<String, Object> params) {
        Integer id = this.requireId(params.get("id"));
        this.getStudentByIdAndUser(id, userId);

        Student clear = new Student();
        clear.setIsDefault(Integer.valueOf(0));
        this.studentMapper.update(clear, new QueryWrapper<Student>()
                .eq("user_id", (Object) userId)
                .isNull("delete_time"));

        Student target = new Student();
        target.setId(id);
        target.setIsDefault(Integer.valueOf(1));
        target.setUpdateTime(TimeUtils.timestamp());
        this.studentMapper.updateById(target);
    }

    private boolean hasBusinessReference(Integer studentId) {
        return this.countReference(this.userCourseMapper.selectCount(new QueryWrapper<UserCourse>()
                .eq("student_id", (Object) studentId)))
                || this.countReference(this.courseBookingMapper.selectCount(new QueryWrapper<CourseBooking>()
                .eq("student_id", (Object) studentId)))
                || this.countReference(this.courseConsumeLogMapper.selectCount(new QueryWrapper<CourseConsumeLog>()
                .eq("student_id", (Object) studentId)))
                || this.countReference(this.courseManualConsumeMapper.selectCount(new QueryWrapper<CourseManualConsume>()
                .eq("student_id", (Object) studentId)))
                || this.countReference(this.leaveApplicationMapper.selectCount(new QueryWrapper<LeaveApplication>()
                .eq("student_id", (Object) studentId)))
                || this.countReference(this.rechargeOrderMapper.selectCount(new QueryWrapper<RechargeOrder>()
                .eq("student_id", (Object) studentId)))
                || this.studentMapper.countCouponFlow(studentId) > 0L
                || this.studentMapper.countCouponInstance(studentId) > 0L;
    }

    private boolean countReference(Long count) {
        return count != null && count > 0L;
    }

    private void resetDefault(Integer userId) {
        Student first = (Student) this.studentMapper.selectOne(new QueryWrapper<Student>()
                .eq("user_id", (Object) userId)
                .isNull("delete_time")
                .orderByAsc("id")
                .last("limit 1"));
        if (first != null) {
            Student update = new Student();
            update.setId(first.getId());
            update.setIsDefault(Integer.valueOf(1));
            update.setUpdateTime(TimeUtils.timestamp());
            this.studentMapper.updateById(update);
        }
    }

    private Student getStudentByIdAndUser(Integer id, Integer userId) {
        Student student = (Student) this.studentMapper.selectOne(new QueryWrapper<Student>()
                .eq("id", (Object) id)
                .eq("user_id", (Object) userId)
                .isNull("delete_time")
                .last("limit 1"));
        if (student == null) {
            throw new OperateException("学员不存在");
        }
        return student;
    }

    private Map<String, Object> buildStudentMap(Student student) {
        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("id", String.valueOf(student.getId()));
        map.put("name", student.getName());
        map.put("idCard", student.getIdCard() == null ? "" : student.getIdCard());
        map.put("gender", this.genderText(student.getGender()));
        map.put("age", this.resolveAge(student.getBirthday(), student.getAge()));
        map.put("birthday", student.getBirthday() == null ? "" : student.getBirthday());
        map.put("status", Integer.valueOf(student.getDeleteTime() == null ? 1 : 0));
        map.put("avatar", UrlUtils.toAbsoluteUrl(student.getAvatar() == null ? "" : student.getAvatar()));
        map.put("color", this.colorFor(student.getId()));
        map.put("note", student.getNote() == null ? "" : student.getNote());
        map.put("isDefault", student.getIsDefault());
        return map;
    }

    private String requireName(Map<String, Object> params) {
        String name = this.optionalString(params, "name");
        if (name.isEmpty()) {
            throw new OperateException("学员姓名不能为空");
        }
        if (name.length() > 50) {
            throw new OperateException("学员姓名最多 50 个字符");
        }
        return name;
    }

    private Integer requireId(Object value) {
        if (value == null || value.toString().trim().isEmpty()) {
            throw new OperateException("学员ID不能为空");
        }
        try {
            return Integer.valueOf(value.toString().trim());
        } catch (NumberFormatException e) {
            throw new OperateException("学员ID参数错误");
        }
    }

    private Integer parseGender(Object value, Integer defaultValue) {
        if (value == null || value.toString().trim().isEmpty()) {
            return defaultValue == null ? Integer.valueOf(0) : defaultValue;
        }
        String text = value.toString().trim();
        if ("1".equals(text) || "男".equals(text) || "male".equalsIgnoreCase(text)) {
            return Integer.valueOf(1);
        }
        if ("2".equals(text) || "女".equals(text) || "female".equalsIgnoreCase(text)) {
            return Integer.valueOf(2);
        }
        if ("0".equals(text) || "未知".equals(text)) {
            return Integer.valueOf(0);
        }
        throw new OperateException("学员性别参数错误");
    }

    private Integer parseAge(Object value) {
        if (value == null || value.toString().trim().isEmpty()) {
            return null;
        }
        try {
            return Integer.valueOf(value.toString().trim());
        } catch (NumberFormatException e) {
            throw new OperateException("请填写 3-16 岁的年龄");
        }
    }

    private void validateBirthdayAndAge(String birthday, Integer age) {
        Integer computedAge = null;
        if (StringUtils.isNotEmpty(birthday)) {
            LocalDate date;
            try {
                date = LocalDate.parse(birthday, DateTimeFormatter.ISO_LOCAL_DATE);
            } catch (Exception e) {
                throw new OperateException("请填写正确的出生日期");
            }
            if (date.isAfter(LocalDate.now())) {
                throw new OperateException("出生日期不能晚于今天");
            }
            computedAge = Period.between(date, LocalDate.now()).getYears();
            if (computedAge < 3 || computedAge > 16) {
                throw new OperateException("请填写 3-16 岁的年龄");
            }
        }
        Integer checkedAge = computedAge != null ? computedAge : age;
        if (checkedAge != null && (checkedAge < 3 || checkedAge > 16)) {
            throw new OperateException("请填写 3-16 岁的年龄");
        }
    }

    private Integer resolveAge(String birthday, Integer storedAge) {
        if (StringUtils.isNotEmpty(birthday)) {
            try {
                LocalDate date = LocalDate.parse(birthday, DateTimeFormatter.ISO_LOCAL_DATE);
                return Integer.valueOf(Math.max(Period.between(date, LocalDate.now()).getYears(), 0));
            } catch (Exception ignored) {
                // 历史数据生日格式异常时回退到已存年龄
            }
        }
        return storedAge == null ? Integer.valueOf(0) : storedAge;
    }

    private void validateIdCard(String idCard, boolean required) {
        if (StringUtils.isEmpty(idCard)) {
            if (required) {
                throw new OperateException("身份证号不能为空");
            }
            return;
        }
        if (!idCard.matches("(^\\d{15}$)|(^\\d{17}[\\dXx]$)")) {
            throw new OperateException("请输入正确的身份证号");
        }
    }

    private void validateNote(String note) {
        if (note != null && note.length() > 100) {
            throw new OperateException("备注最多 100 个字符");
        }
    }

    private String optionalString(Map<String, Object> params, String key) {
        Object value = params.get(key);
        return value == null ? "" : value.toString().trim();
    }

    private String genderText(Integer gender) {
        if (gender == null) {
            return "";
        }
        if (gender.intValue() == 1) {
            return "男";
        }
        if (gender.intValue() == 2) {
            return "女";
        }
        return "";
    }

    private String colorFor(Integer id) {
        String[] colors = new String[]{"#FF6B35", "#3B7CFF", "#1FA971"};
        int index = id == null ? 0 : Math.abs(id.intValue()) % colors.length;
        return colors[index];
    }
}
