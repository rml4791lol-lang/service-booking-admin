/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.excel.annotation.ExcelProperty
 *  com.alibaba.excel.annotation.write.style.ColumnWidth
 *  com.mdd.admin.vo.user.UserListExportVo
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.admin.vo.user;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;

@ApiModel(value="\u7528\u6237\u8bb0\u5f55\u5217\u8868Vo")
public class UserListExportVo
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="\u7528\u6237ID")
    @ExcelProperty(value={"ID"})
    @ColumnWidth(value=50)
    private Integer id;
    @ApiModelProperty(value="\u7528\u6237\u7f16\u7801")
    @ExcelProperty(value={"\u7528\u6237\u7f16\u7801"})
    @ColumnWidth(value=50)
    private Integer sn;
    @ApiModelProperty(value="\u7528\u6237\u5934\u50cf")
    @ExcelProperty(value={"\u7528\u6237\u5934\u50cf"})
    @ColumnWidth(value=50)
    private String avatar;
    @ApiModelProperty(value="\u771f\u5b9e\u59d3\u540d")
    @ExcelProperty(value={"\u771f\u5b9e\u59d3\u540d"})
    @ColumnWidth(value=50)
    private String realName;
    @ApiModelProperty(value="\u7528\u6237\u6635\u79f0")
    @ExcelProperty(value={"\u7528\u6237\u6635\u79f0"})
    @ColumnWidth(value=50)
    private String nickname;
    @ApiModelProperty(value="\u767b\u5f55\u8d26\u53f7")
    @ExcelProperty(value={"\u767b\u5f55\u8d26\u53f7"})
    @ColumnWidth(value=50)
    private String account;
    @ApiModelProperty(value="\u624b\u673a\u53f7\u7801")
    @ExcelProperty(value={"\u624b\u673a\u53f7\u7801"})
    @ColumnWidth(value=50)
    private String mobile;
    @ApiModelProperty(value="\u7528\u6237\u6027\u522b")
    @ExcelProperty(value={"\u7528\u6237\u6027\u522b"})
    @ColumnWidth(value=50)
    private String sex;
    @ApiModelProperty(value="\u6ce8\u518c\u6e20\u9053")
    @ExcelProperty(value={"\u6ce8\u518c\u6e20\u9053"})
    @ColumnWidth(value=50)
    private String channel;
    @ApiModelProperty(value="\u6700\u540e\u767b\u5f55IP")
    @ExcelProperty(value={"\u6700\u540e\u767b\u5f55IP"})
    @ColumnWidth(value=50)
    private String loginIp;
    @ApiModelProperty(value="\u6700\u540e\u767b\u5f55\u65f6\u95f4")
    @ExcelProperty(value={"\u6700\u540e\u767b\u5f55\u65f6\u95f4"})
    @ColumnWidth(value=50)
    private String loginTime;
    @ApiModelProperty(value="\u521b\u5efa\u65f6\u95f4")
    @ExcelProperty(value={"\u521b\u5efa\u65f6\u95f4"})
    @ColumnWidth(value=50)
    private String createTime;
    // #168 学员导出补列：券数两列只统计老抵扣券 la_coupon——
    // 已核销券数=status=1 计数，未核销券数=status=0 计数（status=2 已过期不计入任一列），
    // 软删券（delete_time 非空）不计。关联键 la_coupon.user_id。
    // 注意：券数是「老券口径」，不含次数券；金额两列则是双 sheet 对账口径（见下）。
    @ApiModelProperty(value="\u5df2\u6838\u9500\u5238\u6570")
    @ExcelProperty(value={"\u5df2\u6838\u9500\u5238\u6570"})
    @ColumnWidth(value=50)
    private Integer usedCouponCount;
    @ApiModelProperty(value="\u672a\u6838\u9500\u5238\u6570")
    @ExcelProperty(value={"\u672a\u6838\u9500\u5238\u6570"})
    @ColumnWidth(value=50)
    private Integer unusedCouponCount;
    // #168 金额两列 = 第二 sheet「券明细」该用户所有明细行金额之和（老抵扣券 + 次数券），
    // 四舍五入 2 位，保证同一用户「主 sheet 金额 = 明细 sheet 金额合计」可对账。
    @ApiModelProperty(value="\u5df2\u6838\u9500\u5238\u91d1\u989d(\u5143)")
    @ExcelProperty(value={"\u5df2\u6838\u9500\u5238\u91d1\u989d(\u5143)"})
    @ColumnWidth(value=50)
    private BigDecimal usedCouponAmount;
    @ApiModelProperty(value="\u672a\u6838\u9500\u5238\u91d1\u989d(\u5143)")
    @ExcelProperty(value={"\u672a\u6838\u9500\u5238\u91d1\u989d(\u5143)"})
    @ColumnWidth(value=50)
    private BigDecimal unusedCouponAmount;

    public void setSex(Integer sex) {
        switch (sex) {
            case 0: {
                this.sex = "\u672a\u77e5";
                break;
            }
            case 1: {
                this.sex = "\u7537";
                break;
            }
            case 2: {
                this.sex = "\u5973";
            }
        }
    }

    public Integer getId() {
        return this.id;
    }

    public Integer getSn() {
        return this.sn;
    }

    public String getAvatar() {
        return this.avatar;
    }

    public String getRealName() {
        return this.realName;
    }

    public String getNickname() {
        return this.nickname;
    }

    public String getAccount() {
        return this.account;
    }

    public String getMobile() {
        return this.mobile;
    }

    public String getSex() {
        return this.sex;
    }

    public String getChannel() {
        return this.channel;
    }

    public String getLoginIp() {
        return this.loginIp;
    }

    public String getLoginTime() {
        return this.loginTime;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setSn(Integer sn) {
        this.sn = sn;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Integer getUsedCouponCount() {
        return this.usedCouponCount;
    }

    public void setUsedCouponCount(Integer usedCouponCount) {
        this.usedCouponCount = usedCouponCount;
    }

    public Integer getUnusedCouponCount() {
        return this.unusedCouponCount;
    }

    public void setUnusedCouponCount(Integer unusedCouponCount) {
        this.unusedCouponCount = unusedCouponCount;
    }

    public BigDecimal getUsedCouponAmount() {
        return this.usedCouponAmount;
    }

    public void setUsedCouponAmount(BigDecimal usedCouponAmount) {
        this.usedCouponAmount = usedCouponAmount;
    }

    public BigDecimal getUnusedCouponAmount() {
        return this.unusedCouponAmount;
    }

    public void setUnusedCouponAmount(BigDecimal unusedCouponAmount) {
        this.unusedCouponAmount = unusedCouponAmount;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof UserListExportVo)) {
            return false;
        }
        UserListExportVo other = (UserListExportVo)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        Integer this$sn = this.getSn();
        Integer other$sn = other.getSn();
        if (this$sn == null ? other$sn != null : !((Object)this$sn).equals(other$sn)) {
            return false;
        }
        String this$avatar = this.getAvatar();
        String other$avatar = other.getAvatar();
        if (this$avatar == null ? other$avatar != null : !this$avatar.equals(other$avatar)) {
            return false;
        }
        String this$realName = this.getRealName();
        String other$realName = other.getRealName();
        if (this$realName == null ? other$realName != null : !this$realName.equals(other$realName)) {
            return false;
        }
        String this$nickname = this.getNickname();
        String other$nickname = other.getNickname();
        if (this$nickname == null ? other$nickname != null : !this$nickname.equals(other$nickname)) {
            return false;
        }
        String this$account = this.getAccount();
        String other$account = other.getAccount();
        if (this$account == null ? other$account != null : !this$account.equals(other$account)) {
            return false;
        }
        String this$mobile = this.getMobile();
        String other$mobile = other.getMobile();
        if (this$mobile == null ? other$mobile != null : !this$mobile.equals(other$mobile)) {
            return false;
        }
        String this$sex = this.getSex();
        String other$sex = other.getSex();
        if (this$sex == null ? other$sex != null : !this$sex.equals(other$sex)) {
            return false;
        }
        String this$channel = this.getChannel();
        String other$channel = other.getChannel();
        if (this$channel == null ? other$channel != null : !this$channel.equals(other$channel)) {
            return false;
        }
        String this$loginIp = this.getLoginIp();
        String other$loginIp = other.getLoginIp();
        if (this$loginIp == null ? other$loginIp != null : !this$loginIp.equals(other$loginIp)) {
            return false;
        }
        String this$loginTime = this.getLoginTime();
        String other$loginTime = other.getLoginTime();
        if (this$loginTime == null ? other$loginTime != null : !this$loginTime.equals(other$loginTime)) {
            return false;
        }
        String this$createTime = this.getCreateTime();
        String other$createTime = other.getCreateTime();
        return !(this$createTime == null ? other$createTime != null : !this$createTime.equals(other$createTime));
    }

    protected boolean canEqual(Object other) {
        return other instanceof UserListExportVo;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $sn = this.getSn();
        result = result * 59 + ($sn == null ? 43 : ((Object)$sn).hashCode());
        String $avatar = this.getAvatar();
        result = result * 59 + ($avatar == null ? 43 : $avatar.hashCode());
        String $realName = this.getRealName();
        result = result * 59 + ($realName == null ? 43 : $realName.hashCode());
        String $nickname = this.getNickname();
        result = result * 59 + ($nickname == null ? 43 : $nickname.hashCode());
        String $account = this.getAccount();
        result = result * 59 + ($account == null ? 43 : $account.hashCode());
        String $mobile = this.getMobile();
        result = result * 59 + ($mobile == null ? 43 : $mobile.hashCode());
        String $sex = this.getSex();
        result = result * 59 + ($sex == null ? 43 : $sex.hashCode());
        String $channel = this.getChannel();
        result = result * 59 + ($channel == null ? 43 : $channel.hashCode());
        String $loginIp = this.getLoginIp();
        result = result * 59 + ($loginIp == null ? 43 : $loginIp.hashCode());
        String $loginTime = this.getLoginTime();
        result = result * 59 + ($loginTime == null ? 43 : $loginTime.hashCode());
        String $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : $createTime.hashCode());
        return result;
    }

    public String toString() {
        return "UserListExportVo(id=" + this.getId() + ", sn=" + this.getSn() + ", avatar=" + this.getAvatar() + ", realName=" + this.getRealName() + ", nickname=" + this.getNickname() + ", account=" + this.getAccount() + ", mobile=" + this.getMobile() + ", sex=" + this.getSex() + ", channel=" + this.getChannel() + ", loginIp=" + this.getLoginIp() + ", loginTime=" + this.getLoginTime() + ", createTime=" + this.getCreateTime() + ")";
    }
}

