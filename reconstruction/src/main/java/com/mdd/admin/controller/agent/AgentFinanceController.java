/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.aop.Log
 *  com.mdd.admin.controller.agent.AgentFinanceController
 *  com.mdd.admin.service.agent.IAgentFinanceService
 *  com.mdd.admin.validate.agent.AgentWithdrawalApplyValidate
 *  com.mdd.admin.validate.agent.AgentWithdrawalAuditValidate
 *  com.mdd.admin.validate.commons.PageValidate
 *  com.mdd.admin.vo.agent.AdminAgentWithdrawalStatsVo
 *  com.mdd.admin.vo.agent.AgentEarningLogVo
 *  com.mdd.admin.vo.agent.AgentFinanceStatsVo
 *  com.mdd.admin.vo.agent.AgentWithdrawalVo
 *  com.mdd.common.aop.NotPower
 *  com.mdd.common.core.AjaxResult
 *  com.mdd.common.core.PageResult
 *  io.swagger.annotations.Api
 *  io.swagger.annotations.ApiOperation
 *  javax.annotation.Resource
 *  org.springframework.validation.annotation.Validated
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.RequestBody
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RequestParam
 *  org.springframework.web.bind.annotation.RestController
 */
package com.mdd.admin.controller.agent;

import cn.dev33.satoken.stp.StpUtil;
import com.mdd.admin.LikeAdminThreadLocal;
import com.mdd.admin.aop.Log;
import com.mdd.admin.service.agent.IAgentFinanceService;
import com.mdd.admin.validate.agent.AgentWithdrawalApplyValidate;
import com.mdd.admin.validate.agent.AgentWithdrawalAuditValidate;
import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.vo.agent.AdminAgentWithdrawalStatsVo;
import com.mdd.admin.vo.agent.AgentEarningLogVo;
import com.mdd.admin.vo.agent.AgentFinanceStatsVo;
import com.mdd.admin.vo.agent.AgentWithdrawalVo;
import com.mdd.common.aop.NotPower;
import com.mdd.common.core.AjaxResult;
import com.mdd.common.core.PageResult;
import com.mdd.common.enums.ErrorEnum;
import com.mdd.common.exception.LoginException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/adminapi/agent/finance"})
@Api(tags={"\u4ee3\u7406\u8d22\u52a1\u7ba1\u7406"})
public class AgentFinanceController {
    @Resource
    IAgentFinanceService iAgentFinanceService;

    /**
     * 代理财务统计（#71）：不再对任意后台账号开放。
     *
     * <p>重建后台没有「代理财务」页面菜单（T30-17 已清理失效菜单），路由 perms
     * {@code agent/finance/stats} 无法在菜单侧直接授权，故与 #58
     * {@code FinanceRefundController#stat} 采用同一模式：保留 {@code @NotPower}
     * 绕过路由级校验，方法内校验页面权限点；权限点由
     * {@code tools/ddl/migrations/20261024-add-agent-finance-perm.sql} 补齐
     * （只增 A 权限，不默认授权）。超级管理员（admin id=1）放行。</p>
     */
    @NotPower
    @GetMapping(value={"/stats"})
    @ApiOperation(value="\u8d22\u52a1\u7edf\u8ba1")
    public AjaxResult<AgentFinanceStatsVo> stats() {
        this.checkPagePermission("agent/finance/stats");
        AgentFinanceStatsVo stats = this.iAgentFinanceService.stats();
        return AjaxResult.success(stats);
    }

    /**
     * 代理提现统计（#71）：平台侧月度提现汇总属财务数据，口径同 {@link #stats()}。
     */
    @NotPower
    @GetMapping(value={"/adminStats"})
    @ApiOperation(value="\u7ba1\u7406\u5458\u7684\u63d0\u73b0\u7edf\u8ba1")
    public AjaxResult<AdminAgentWithdrawalStatsVo> adminStats() {
        this.checkPagePermission("agent/finance/adminStats");
        AdminAgentWithdrawalStatsVo stats = this.iAgentFinanceService.adminStats();
        return AjaxResult.success(stats);
    }

    /**
     * 校验页面权限点：超级管理员放行，其余账号必须由角色授予对应 perms。
     * 与 {@code FinanceRefundController#checkRecordPermission} 保持同一 403 语义
     * （{@code code=0} + 「无相关权限」，由 {@code StpInException} 统一出口）。
     */
    private void checkPagePermission(String perms) {
        Integer adminId = LikeAdminThreadLocal.getAdminId();
        if (adminId != null && adminId == 1) {
            return;
        }
        boolean allowed;
        try {
            allowed = StpUtil.hasPermission(perms);
        } catch (Exception e) {
            allowed = false;
        }
        if (!allowed) {
            throw new LoginException(ErrorEnum.NO_PERMISSION.getCode(), ErrorEnum.NO_PERMISSION.getMsg());
        }
    }

    @NotPower
    @GetMapping(value={"/isAgent"})
    @ApiOperation(value="\u5224\u65ad\u5f53\u524d\u7528\u6237\u662f\u5426\u662f\u4ee3\u7406")
    public AjaxResult<Boolean> isAgent() {
        try {
            this.iAgentFinanceService.stats();
            return AjaxResult.success(true);
        }
        catch (Exception e) {
            return AjaxResult.success(false);
        }
    }

    @GetMapping(value={"/earningList"})
    @ApiOperation(value="\u4ee3\u7406\u6536\u76ca\u660e\u7ec6")
    public AjaxResult<PageResult<AgentEarningLogVo>> earningList(@Validated PageValidate pageValidate, @RequestParam(required=false) Integer agentId) {
        PageResult list = this.iAgentFinanceService.earningList(pageValidate, agentId);
        return AjaxResult.success(list);
    }

    @GetMapping(value={"/withdrawalList"})
    @ApiOperation(value="\u63d0\u73b0\u5217\u8868")
    public AjaxResult<PageResult<AgentWithdrawalVo>> withdrawalList(@Validated PageValidate pageValidate, @RequestParam(required=false) Integer status, @RequestParam(required=false) String agentName, @RequestParam(required=false) String startTime, @RequestParam(required=false) String endTime) {
        PageResult list = this.iAgentFinanceService.withdrawalList(pageValidate, status, agentName, startTime, endTime);
        return AjaxResult.success(list);
    }

    @Log(title="\u7533\u8bf7\u63d0\u73b0")
    @PostMapping(value={"/applyWithdrawal"})
    @ApiOperation(value="\u7533\u8bf7\u63d0\u73b0")
    public AjaxResult<Object> applyWithdrawal(@Validated @RequestBody AgentWithdrawalApplyValidate applyValidate) {
        this.iAgentFinanceService.applyWithdrawal(applyValidate);
        return AjaxResult.success();
    }

    @Log(title="\u5ba1\u6838\u63d0\u73b0")
    @PostMapping(value={"/auditWithdrawal"})
    @ApiOperation(value="\u5ba1\u6838\u63d0\u73b0")
    public AjaxResult<Object> auditWithdrawal(@Validated @RequestBody AgentWithdrawalAuditValidate auditValidate) {
        this.iAgentFinanceService.auditWithdrawal(auditValidate);
        return AjaxResult.success();
    }
}

