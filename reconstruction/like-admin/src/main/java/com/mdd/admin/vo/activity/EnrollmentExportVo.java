package com.mdd.admin.vo.activity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 报名名单导出（T30.6 / #38）。
 *
 * <p>列顺序与后台报名名单一致；去重口径：同场次 + 手机号 + 学员名。</p>
 */
@ApiModel(value = "报名名单导出Vo")
public class EnrollmentExportVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "提交时间")
    @ExcelProperty(value = {"提交时间"})
    @ColumnWidth(22)
    private String createdAt;

    @ApiModelProperty(value = "报名链接ID")
    @ExcelProperty(value = {"报名链接ID"})
    @ColumnWidth(12)
    private Integer linkId;

    @ApiModelProperty(value = "链接令牌")
    @ExcelProperty(value = {"链接令牌"})
    @ColumnWidth(26)
    private String token;

    @ApiModelProperty(value = "场次ID")
    @ExcelProperty(value = {"场次ID"})
    @ColumnWidth(10)
    private Integer sessionId;

    @ApiModelProperty(value = "课程")
    @ExcelProperty(value = {"课程"})
    @ColumnWidth(28)
    private String courseTitle;

    @ApiModelProperty(value = "日期")
    @ExcelProperty(value = {"日期"})
    @ColumnWidth(14)
    private String date;

    @ApiModelProperty(value = "时段")
    @ExcelProperty(value = {"时段"})
    @ColumnWidth(16)
    private String time;

    @ApiModelProperty(value = "小区")
    @ExcelProperty(value = {"小区"})
    @ColumnWidth(18)
    private String areaName;

    @ApiModelProperty(value = "家长姓名")
    @ExcelProperty(value = {"家长姓名"})
    @ColumnWidth(14)
    private String parentName;

    @ApiModelProperty(value = "手机号")
    @ExcelProperty(value = {"手机号"})
    @ColumnWidth(16)
    private String phone;

    @ApiModelProperty(value = "学员姓名")
    @ExcelProperty(value = {"学员姓名"})
    @ColumnWidth(14)
    private String studentName;

    @ApiModelProperty(value = "性别")
    @ExcelProperty(value = {"性别"})
    @ColumnWidth(8)
    private String gender;

    @ApiModelProperty(value = "年龄")
    @ExcelProperty(value = {"年龄"})
    @ColumnWidth(8)
    private Integer age;

    @ApiModelProperty(value = "状态")
    @ExcelProperty(value = {"状态"})
    @ColumnWidth(12)
    private String statusText;

    @ApiModelProperty(value = "备注")
    @ExcelProperty(value = {"备注"})
    @ColumnWidth(30)
    private String note;

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public Integer getLinkId() { return linkId; }
    public void setLinkId(Integer linkId) { this.linkId = linkId; }
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public Integer getSessionId() { return sessionId; }
    public void setSessionId(Integer sessionId) { this.sessionId = sessionId; }
    public String getCourseTitle() { return courseTitle; }
    public void setCourseTitle(String courseTitle) { this.courseTitle = courseTitle; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
    public String getAreaName() { return areaName; }
    public void setAreaName(String areaName) { this.areaName = areaName; }
    public String getParentName() { return parentName; }
    public void setParentName(String parentName) { this.parentName = parentName; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
    public String getStatusText() { return statusText; }
    public void setStatusText(String statusText) { this.statusText = statusText; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}
