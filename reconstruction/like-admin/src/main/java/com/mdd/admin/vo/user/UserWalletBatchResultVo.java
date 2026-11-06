package com.mdd.admin.vo.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 余额批量调整汇总（#批量导入）。
 *
 * <p>整体不做大事务：单行失败只记入 errors，不影响其他行。</p>
 */
@ApiModel(value = "余额批量调整汇总")
public class UserWalletBatchResultVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "提交行数")
    private Integer total;

    @ApiModelProperty(value = "成功行数")
    private Integer success;

    @ApiModelProperty(value = "失败行数")
    private Integer failed;

    @ApiModelProperty(value = "失败明细（行号+手机号+原因）")
    private List<UserWalletBatchErrorVo> errors = new ArrayList<>();

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public Integer getFailed() {
        return failed;
    }

    public void setFailed(Integer failed) {
        this.failed = failed;
    }

    public List<UserWalletBatchErrorVo> getErrors() {
        return errors;
    }

    public void setErrors(List<UserWalletBatchErrorVo> errors) {
        this.errors = errors;
    }
}
