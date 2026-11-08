package com.mdd.admin.vo.coupon;

import java.io.Serializable;
import java.util.List;

/**
 * 后台发次数券回执（#206）。
 *
 * <p>couponSns 只回传前 5 个券编号，避免一次发 99 张时响应过大；完整张数见
 * quantity/couponSnTotal。给谁 = mobile + userId（账号级时 studentId=0、studentName 为空）。</p>
 */
public class CouponGrantVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 实际发放张数 */
    private Integer quantity;
    /** 券 sn 摘要（最多 5 个） */
    private List<String> couponSns;
    /** 券 sn 总数（=quantity） */
    private Integer couponSnTotal;
    /** 家长手机号 */
    private String mobile;
    /** 家长账号ID */
    private Integer userId;
    /** 学员ID（0=账号级） */
    private Integer studentId;
    /** 学员姓名（账号级为空） */
    private String studentName;
    /** 券商品ID */
    private Integer goodsId;
    /** 券名称快照 */
    private String goodsTitle;

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public List<String> getCouponSns() {
        return couponSns;
    }

    public void setCouponSns(List<String> couponSns) {
        this.couponSns = couponSns;
    }

    public Integer getCouponSnTotal() {
        return couponSnTotal;
    }

    public void setCouponSnTotal(Integer couponSnTotal) {
        this.couponSnTotal = couponSnTotal;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsTitle() {
        return goodsTitle;
    }

    public void setGoodsTitle(String goodsTitle) {
        this.goodsTitle = goodsTitle;
    }
}
