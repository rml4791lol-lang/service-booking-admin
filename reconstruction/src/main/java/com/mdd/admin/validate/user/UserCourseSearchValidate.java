package com.mdd.admin.validate.user;

import java.io.Serializable;

/**
 * 老课包列表筛选：学员姓名 / 手机号 / 课程名 / 状态。
 */
public class UserCourseSearchValidate implements Serializable {
    private static final long serialVersionUID = 1L;

    private String studentName;
    private String mobile;
    private String className;
    private Integer status;

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
