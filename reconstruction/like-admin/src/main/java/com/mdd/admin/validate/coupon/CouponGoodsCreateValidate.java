package com.mdd.admin.validate.coupon;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 次数券商品新增校验（#45 / T28.9）。
 *
 * <p>字段与 la_coupon_goods 对齐：价格/次数/有效期/适用范围/限购/角标/排序/上下架。
 * 商品编码 code 是数据种子的幂等键，创建后不可修改（编辑接口也会校验）。</p>
 */
public class CouponGoodsCreateValidate implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "商品编码不能为空")
    @Size(max = 50, message = "商品编码不能超过50个字符")
    @Pattern(regexp = "^[a-z0-9][a-z0-9_-]{0,49}$",
            message = "商品编码只能包含小写字母、数字、下划线和短横线，且不能以符号开头")
    private String code;

    @NotBlank(message = "券名称不能为空")
    @Size(max = 100, message = "券名称不能超过100个字符")
    private String title;

    @Size(max = 255, message = "商品简介不能超过255个字符")
    private String description;

    @NotNull(message = "售价不能为空")
    @DecimalMin(value = "0.00", message = "售价不能小于0元")
    @Digits(integer = 8, fraction = 2, message = "售价最多8位整数、2位小数")
    private BigDecimal price;

    @NotNull(message = "包含次数不能为空")
    @Min(value = 1, message = "包含次数不能小于1次")
    @Max(value = 9999, message = "包含次数不能超过9999次")
    private Integer times;

    @NotNull(message = "有效天数不能为空")
    @Min(value = 0, message = "有效天数不能小于0（0 表示长期有效）")
    @Max(value = 3650, message = "有效天数不能超过3650天")
    private Integer validDays;

    @Size(max = 100, message = "适用范围文案不能超过100个字符")
    private String scope;

    @NotBlank(message = "适用范围类型不能为空")
    @Pattern(regexp = "^(all|group|private|trial)$",
            message = "适用范围类型只能是 all（全部）/group（团课）/private（私教）/trial（体验）")
    private String scopeType;

    @Size(max = 1000, message = "限定课程不能超过1000个字符")
    private String courseIds;

    @Size(max = 1000, message = "限定小区不能超过1000个字符")
    private String areaIds;

    @Min(value = 0, message = "单账号限购不能小于0（0 表示不限）")
    @Max(value = 9999, message = "单账号限购不能超过9999份")
    private Integer perUser;

    @Min(value = 0, message = "单学员限购不能小于0（0 表示不限）")
    @Max(value = 9999, message = "单学员限购不能超过9999份")
    private Integer perStudent;

    @Size(max = 30, message = "角标不能超过30个字符")
    private String tag;

    @Min(value = 0, message = "排序值不能小于0")
    @Max(value = 9999, message = "排序值不能超过9999")
    private Integer sort;

    @Min(value = 0, message = "上下架状态只能是 0 或 1")
    @Max(value = 1, message = "上下架状态只能是 0 或 1")
    private Integer status;

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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
