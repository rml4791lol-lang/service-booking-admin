package com.mdd.admin.vo.activity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * 活动报名超员告警详情（#61 / #7 复审）。
 *
 * <p>继承列表字段，追加 {@code signups}；字段与原 Map 的 JSON key 完全一致。</p>
 */
@ApiModel(value = "活动报名超员告警详情响应实体")
public class ActivityEnrollAlertDetailVo extends ActivityEnrollAlertListVo {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "报名明细")
    private List<ActivityEnrollAlertSignupVo> signups = new ArrayList<>();

    public List<ActivityEnrollAlertSignupVo> getSignups() { return signups; }
    public void setSignups(List<ActivityEnrollAlertSignupVo> signups) {
        this.signups = signups == null ? new ArrayList<>() : signups;
    }
}
