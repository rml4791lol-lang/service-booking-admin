/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.service.community.ICommunityService
 *  com.mdd.admin.validate.commons.PageValidate
 *  com.mdd.admin.validate.community.CommunityCreateValidate
 *  com.mdd.admin.validate.community.CommunitySearchValidate
 *  com.mdd.admin.validate.community.CommunityUpdateValidate
 *  com.mdd.admin.vo.community.CommunityDetailVo
 *  com.mdd.admin.vo.community.CommunityEarningLogVo
 *  com.mdd.admin.vo.community.CommunityListedVo
 *  com.mdd.common.core.PageResult
 */
package com.mdd.admin.service.community;

import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.validate.community.CommunityCreateValidate;
import com.mdd.admin.validate.community.CommunitySearchValidate;
import com.mdd.admin.validate.community.CommunityUpdateValidate;
import com.mdd.admin.vo.community.CommunityDetailVo;
import com.mdd.admin.vo.community.CommunityEarningLogVo;
import com.mdd.admin.vo.community.CommunityListedVo;
import com.mdd.admin.vo.community.CommunityOptionVo;
import com.mdd.common.core.PageResult;
import java.util.List;
import java.util.Map;

public interface ICommunityService {
    public PageResult<CommunityListedVo> list(PageValidate var1, CommunitySearchValidate var2);

    public CommunityDetailVo detail(Integer var1);

    public Integer add(CommunityCreateValidate var1);

    public void edit(CommunityUpdateValidate var1);

    public void del(Integer var1);

    public PageResult<CommunityEarningLogVo> earningList(PageValidate var1, Integer var2);

    public void bindAgents(Integer var1, List<Integer> var2);

    public List<Integer> getAgentIds(Integer var1);

    public Map<String, Object> stats();

    public List<CommunityOptionVo> all(Integer regionId, String keyword);
}

