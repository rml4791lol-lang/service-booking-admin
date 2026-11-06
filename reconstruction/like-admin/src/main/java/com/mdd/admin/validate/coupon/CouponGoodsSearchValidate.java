package com.mdd.admin.validate.coupon;

import java.io.Serializable;

/**
 * 次数券商品列表筛选（#45 / T28.9）。
 */
public class CouponGoodsSearchValidate implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 名称/编码关键字 */
    private String keyword;

    /** 上下架：0=下架，1=上架 */
    private Integer status;

    /** 适用范围类型：all/group/private/trial */
    private String scopeType;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getScopeType() {
        return scopeType;
    }

    public void setScopeType(String scopeType) {
        this.scopeType = scopeType;
    }
}
