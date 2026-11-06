package com.mdd.common.entity.coupon;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 次数券商品（la_coupon_goods）。对应小程序「购买券」的 8 档商品。
 */
@TableName("la_coupon_goods")
public class CouponGoods implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 商品ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /** 商品编码（数据种子幂等键） */
    private String code;
    /** 券名称 */
    private String title;
    /** 商品简介（前端 desc） */
    private String description;
    /** 售价（元） */
    private BigDecimal price;
    /** 含次数 */
    private Integer times;
    /** 有效天数（0=长期有效） */
    private Integer validDays;
    /** 适用范围文案 */
    private String scope;
    /** 适用范围类型：all/group/private/trial */
    private String scopeType;
    /** 限定课程ID，逗号分隔；空=不限 */
    private String courseIds;
    /** 限定小区ID，逗号分隔；空=不限 */
    private String areaIds;
    /** 单账号限购份数（0=不限） */
    private Integer perUser;
    /** 单学员限购份数（0=不限） */
    private Integer perStudent;
    /** 上下架：0=下架，1=上架 */
    private Integer status;
    /** 前端角标 */
    private String tag;
    /** 排序，小在前 */
    private Integer sort;
    /** 创建时间 */
    private Long createTime;
    /** 更新时间 */
    private Long updateTime;
    /** 软删除时间 */
    private Long deleteTime;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getTimes() {
        return this.times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }

    public Integer getValidDays() {
        return this.validDays;
    }

    public void setValidDays(Integer validDays) {
        this.validDays = validDays;
    }

    public String getScope() {
        return this.scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getScopeType() {
        return this.scopeType;
    }

    public void setScopeType(String scopeType) {
        this.scopeType = scopeType;
    }

    public String getCourseIds() {
        return this.courseIds;
    }

    public void setCourseIds(String courseIds) {
        this.courseIds = courseIds;
    }

    public String getAreaIds() {
        return this.areaIds;
    }

    public void setAreaIds(String areaIds) {
        this.areaIds = areaIds;
    }

    public Integer getPerUser() {
        return this.perUser;
    }

    public void setPerUser(Integer perUser) {
        this.perUser = perUser;
    }

    public Integer getPerStudent() {
        return this.perStudent;
    }

    public void setPerStudent(Integer perStudent) {
        this.perStudent = perStudent;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getTag() {
        return this.tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Integer getSort() {
        return this.sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Long getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public Long getDeleteTime() {
        return this.deleteTime;
    }

    public void setDeleteTime(Long deleteTime) {
        this.deleteTime = deleteTime;
    }
}
