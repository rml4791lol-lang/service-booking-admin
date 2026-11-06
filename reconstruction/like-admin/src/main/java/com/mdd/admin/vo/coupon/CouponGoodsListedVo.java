package com.mdd.admin.vo.coupon;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 次数券商品列表项（#45 / T28.9）。
 *
 * <p>price 为元；status 1=上架 0=下架；courseNameText/areaNameText 供列表展示，
 * 编辑表单仍使用原始 courseIds/areaIds 逗号串。</p>
 */
public class CouponGoodsListedVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String code;
    private String title;
    private String description;
    private BigDecimal price;
    private Integer times;
    private Integer validDays;
    private String scope;
    private String scopeType;
    private String courseIds;
    private String areaIds;
    private String courseNameText;
    private String areaNameText;
    private Integer perUser;
    private Integer perStudent;
    private Integer status;
    private String tag;
    private Integer sort;
    private Long createTime;
    private Long updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getTimes() {
        return times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }

    public Integer getValidDays() {
        return validDays;
    }

    public void setValidDays(Integer validDays) {
        this.validDays = validDays;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getScopeType() {
        return scopeType;
    }

    public void setScopeType(String scopeType) {
        this.scopeType = scopeType;
    }

    public String getCourseIds() {
        return courseIds;
    }

    public void setCourseIds(String courseIds) {
        this.courseIds = courseIds;
    }

    public String getAreaIds() {
        return areaIds;
    }

    public void setAreaIds(String areaIds) {
        this.areaIds = areaIds;
    }

    public String getCourseNameText() {
        return courseNameText;
    }

    public void setCourseNameText(String courseNameText) {
        this.courseNameText = courseNameText;
    }

    public String getAreaNameText() {
        return areaNameText;
    }

    public void setAreaNameText(String areaNameText) {
        this.areaNameText = areaNameText;
    }

    public Integer getPerUser() {
        return perUser;
    }

    public void setPerUser(Integer perUser) {
        this.perUser = perUser;
    }

    public Integer getPerStudent() {
        return perStudent;
    }

    public void setPerStudent(Integer perStudent) {
        this.perStudent = perStudent;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }
}
