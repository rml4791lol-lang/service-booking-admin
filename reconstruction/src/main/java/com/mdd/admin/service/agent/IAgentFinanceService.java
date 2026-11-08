/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.service.agent.IAgentFinanceService
 *  com.mdd.admin.validate.agent.AgentWithdrawalApplyValidate
 *  com.mdd.admin.validate.agent.AgentWithdrawalAuditValidate
 *  com.mdd.admin.validate.commons.PageValidate
 *  com.mdd.admin.vo.agent.AdminAgentWithdrawalStatsVo
 *  com.mdd.admin.vo.agent.AgentEarningLogVo
 *  com.mdd.admin.vo.agent.AgentFinanceStatsVo
 *  com.mdd.admin.vo.agent.AgentWithdrawalVo
 *  com.mdd.common.core.PageResult
 */
package com.mdd.admin.service.agent;

import com.mdd.admin.validate.agent.AgentWithdrawalApplyValidate;
import com.mdd.admin.validate.agent.AgentWithdrawalAuditValidate;
import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.vo.agent.AdminAgentWithdrawalStatsVo;
import com.mdd.admin.vo.agent.AgentEarningLogVo;
import com.mdd.admin.vo.agent.AgentFinanceStatsVo;
import com.mdd.admin.vo.agent.AgentWithdrawalVo;
import com.mdd.common.core.PageResult;

public interface IAgentFinanceService {
    public AgentFinanceStatsVo stats();

    public AdminAgentWithdrawalStatsVo adminStats();

    public PageResult<AgentEarningLogVo> earningList(PageValidate var1, Integer var2);

    public PageResult<AgentWithdrawalVo> withdrawalList(PageValidate var1, Integer var2, String var3, String var4, String var5);

    public void applyWithdrawal(AgentWithdrawalApplyValidate var1);

    public void auditWithdrawal(AgentWithdrawalAuditValidate var1);
}

