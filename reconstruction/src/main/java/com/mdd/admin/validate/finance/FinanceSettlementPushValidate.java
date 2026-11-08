package com.mdd.admin.validate.finance;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value = "分账数据上报参数")
public class FinanceSettlementPushValidate implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "开始日期 yyyy-MM-dd（含）")
    private String periodStart;

    @ApiModelProperty(value = "结束日期 yyyy-MM-dd（含）")
    private String periodEnd;

    @ApiModelProperty(value = "社区ID；为空表示全部社区")
    private Integer communityId;

    @ApiModelProperty(value = "演练模式，默认 true；必须显式传 false 才会真正出站")
    private Boolean dryRun = Boolean.TRUE;

    @ApiModelProperty(value = "批次类型：NORMAL / REVERSAL，默认 NORMAL")
    private String batchType;

    @ApiModelProperty(value = "冲正时必填：原批次号")
    private String originalBatchNo;

    public String getPeriodStart() {
        return periodStart;
    }

    public void setPeriodStart(String periodStart) {
        this.periodStart = periodStart;
    }

    public String getPeriodEnd() {
        return periodEnd;
    }

    public void setPeriodEnd(String periodEnd) {
        this.periodEnd = periodEnd;
    }

    public Integer getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Integer communityId) {
        this.communityId = communityId;
    }

    public Boolean getDryRun() {
        return dryRun;
    }

    public void setDryRun(Boolean dryRun) {
        this.dryRun = dryRun;
    }

    public String getBatchType() {
        return batchType;
    }

    public void setBatchType(String batchType) {
        this.batchType = batchType;
    }

    public String getOriginalBatchNo() {
        return originalBatchNo;
    }

    public void setOriginalBatchNo(String originalBatchNo) {
        this.originalBatchNo = originalBatchNo;
    }
}
