package com.mdd.common.settlement;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 班级内按实际收款教练拆分的课时收入。
 *
 * <p>一个班级可能有主讲/代课/替补，`la_instructor_account_log.instructor_id`
 * 才是实际收款人；`SettlementItem.instructorId` 只表示班级主教练。</p>
 */
public class SettlementCoachShare implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer instructorId;
    private String instructorName = "";
    private BigDecimal shareAmount = BigDecimal.ZERO;

    public SettlementCoachShare() {
    }

    public SettlementCoachShare(Integer instructorId, String instructorName, BigDecimal shareAmount) {
        this.instructorId = instructorId;
        this.instructorName = instructorName == null ? "" : instructorName;
        this.shareAmount = shareAmount == null ? BigDecimal.ZERO : shareAmount;
    }

    public Integer getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(Integer instructorId) {
        this.instructorId = instructorId;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public BigDecimal getShareAmount() {
        return shareAmount;
    }

    public void setShareAmount(BigDecimal shareAmount) {
        this.shareAmount = shareAmount;
    }
}
