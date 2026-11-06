/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.service.agent.IAgentService
 *  com.mdd.admin.validate.agent.AgentCreateValidate
 *  com.mdd.admin.validate.agent.AgentSearchValidate
 *  com.mdd.admin.validate.agent.AgentUpdateValidate
 *  com.mdd.admin.validate.commons.PageValidate
 *  com.mdd.admin.vo.agent.AgentDetailVo
 *  com.mdd.admin.vo.agent.AgentListedVo
 *  com.mdd.common.core.PageResult
 */
package com.mdd.admin.service.agent;

import com.mdd.admin.validate.agent.AgentCreateValidate;
import com.mdd.admin.validate.agent.AgentSearchValidate;
import com.mdd.admin.validate.agent.AgentUpdateValidate;
import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.vo.agent.AgentDetailVo;
import com.mdd.admin.vo.agent.AgentListedVo;
import com.mdd.common.core.PageResult;

public interface IAgentService {
    public PageResult<AgentListedVo> list(PageValidate var1, AgentSearchValidate var2);

    public AgentDetailVo detail(Integer var1);

    public void add(AgentCreateValidate var1);

    public void edit(AgentUpdateValidate var1);

    public void del(Integer var1);
}

