package com.mdd.admin.vo.coupon;

import java.io.Serializable;

/**
 * 已发券列表项（#213 C2）。
 *
 * <p>供后台「已发券」抽屉核对与撤回：券号、券名、家长手机号、持券学员、
 * 状态文案、剩余次数与发放时间。revocable 与后端撤回校验同口径
 * （status=1 且 remaining=total），前端据此禁用按钮。</p>
 */
public class CouponGrantedVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 券实例ID */
    private Integer id;
    /** 券号 */
    private String sn;
    /** 券名称快照 */
    private String title;
    /** 持券用户ID */
    private Integer userId;
    /** 家长手机号 */
    private String mobile;
    /** 持券学员ID（0=账号级） */
    private Integer studentId;
    /** 持券学员姓名（账号级为空） */
    private String studentName;
    /** 状态：1=有效，2=已用完，3=已过期，4=已作废 */
    private Integer status;
    /** 状态文案 */
    private String statusText;
    /** 总次数 */
    private Integer total;
    /** 剩余次数 */
    private Integer remaining;
    /** 是否可撤回 */
    private Boolean revocable;
    /** 发放来源：purchase/grant/compensate */
    private String source;
    /** 备注 */
    private String remark;
    /** 创建时间 */
    private String createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getRemaining() {
        return remaining;
    }

    public void setRemaining(Integer remaining) {
        this.remaining = remaining;
    }

    public Boolean getRevocable() {
        return revocable;
    }

    public void setRevocable(Boolean revocable) {
        this.revocable = revocable;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
