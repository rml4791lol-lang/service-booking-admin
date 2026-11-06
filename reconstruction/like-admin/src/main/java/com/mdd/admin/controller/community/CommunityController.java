/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.aop.Log
 *  com.mdd.admin.controller.community.CommunityController
 *  com.mdd.admin.service.community.ICommunityService
 *  com.mdd.admin.validate.commons.IdValidate
 *  com.mdd.admin.validate.commons.PageValidate
 *  com.mdd.admin.validate.community.CommunityCreateValidate
 *  com.mdd.admin.validate.community.CommunitySearchValidate
 *  com.mdd.admin.validate.community.CommunityUpdateValidate
 *  com.mdd.admin.vo.community.CommunityDetailVo
 *  com.mdd.admin.vo.community.CommunityEarningLogVo
 *  com.mdd.admin.vo.community.CommunityListedVo
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
package com.mdd.admin.controller.community;

import com.mdd.admin.aop.Log;
import com.mdd.admin.service.community.ICommunityService;
import com.mdd.admin.validate.commons.IdValidate;
import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.validate.community.CommunityCreateValidate;
import com.mdd.admin.validate.community.CommunitySearchValidate;
import com.mdd.admin.validate.community.CommunityUpdateValidate;
import com.mdd.admin.vo.community.CommunityDetailVo;
import com.mdd.admin.vo.community.CommunityEarningLogVo;
import com.mdd.admin.vo.community.CommunityListedVo;
import com.mdd.admin.vo.community.CommunityOptionVo;
import com.mdd.common.core.AjaxResult;
import com.mdd.common.core.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/adminapi/community"})
@Api(tags={"\u793e\u533a\u7ba1\u7406"})
public class CommunityController {
    @Resource
    ICommunityService iCommunityService;

    @GetMapping(value={"/list"})
    @ApiOperation(value="\u793e\u533a\u5217\u8868")
    public AjaxResult<PageResult<CommunityListedVo>> list(@Validated PageValidate pageValidate, @Validated CommunitySearchValidate searchValidate) {
        PageResult list = this.iCommunityService.list(pageValidate, searchValidate);
        return AjaxResult.success(list);
    }

    @GetMapping(value={"/all"})
    @ApiOperation(value="\u793e\u533a\u4e0b\u62c9\u5217\u8868")
    public AjaxResult<List<CommunityOptionVo>> all(
            @RequestParam(value="regionId", required=false) Integer regionId,
            @RequestParam(value="keyword", required=false) String keyword) {
        return AjaxResult.success(this.iCommunityService.all(regionId, keyword));
    }

    @GetMapping(value={"/detail"})
    @ApiOperation(value="\u793e\u533a\u8be6\u60c5")
    public AjaxResult<CommunityDetailVo> detail(@RequestParam(value="id") Integer id) {
        CommunityDetailVo detail = this.iCommunityService.detail(id);
        return AjaxResult.success(detail);
    }

    @Log(title="\u65b0\u589e\u793e\u533a")
    @PostMapping(value={"/add"})
    @ApiOperation(value="\u65b0\u589e\u793e\u533a")
    public AjaxResult<Object> add(@Validated @RequestBody CommunityCreateValidate createValidate) {
        Integer id = this.iCommunityService.add(createValidate);
        LinkedHashMap<String, Integer> data = new LinkedHashMap<String, Integer>();
        data.put("id", id);
        return AjaxResult.success(data);
    }

    @Log(title="\u7f16\u8f91\u793e\u533a")
    @PostMapping(value={"/edit"})
    @ApiOperation(value="\u7f16\u8f91\u793e\u533a")
    public AjaxResult<Object> edit(@Validated @RequestBody CommunityUpdateValidate updateValidate) {
        this.iCommunityService.edit(updateValidate);
        return AjaxResult.success();
    }

    @Log(title="\u5220\u9664\u793e\u533a")
    @PostMapping(value={"/del"})
    @ApiOperation(value="\u5220\u9664\u793e\u533a")
    public AjaxResult<Object> del(@Validated @RequestBody IdValidate idValidate) {
        this.iCommunityService.del(idValidate.getId());
        return AjaxResult.success();
    }

    @GetMapping(value={"/earningList"})
    @ApiOperation(value="\u793e\u533a\u5206\u6210\u660e\u7ec6")
    public AjaxResult<PageResult<CommunityEarningLogVo>> earningList(@Validated PageValidate pageValidate, @RequestParam(value="communityId", required=false) Integer communityId) {
        PageResult list = this.iCommunityService.earningList(pageValidate, communityId);
        return AjaxResult.success(list);
    }

    @Log(title="\u7ed1\u5b9a\u4ee3\u7406")
    @PostMapping(value={"/bindAgents"})
    @ApiOperation(value="\u7ed1\u5b9a\u4ee3\u7406")
    public AjaxResult<Object> bindAgents(@RequestBody Map<String, Object> params) {
        Integer communityId = (Integer)params.get("communityId");
        List agentIds = (List)params.get("agentIds");
        this.iCommunityService.bindAgents(communityId, agentIds);
        return AjaxResult.success();
    }

    @GetMapping(value={"/agentIds"})
    @ApiOperation(value="\u83b7\u53d6\u793e\u533a\u5df2\u7ed1\u5b9a\u7684\u4ee3\u7406ID\u5217\u8868")
    public AjaxResult<List<Integer>> getAgentIds(@RequestParam(value="communityId") Integer communityId) {
        return AjaxResult.success(this.iCommunityService.getAgentIds(communityId));
    }

    @GetMapping(value={"/stats"})
    @ApiOperation(value="\u793e\u533a\u7edf\u8ba1\u6570\u636e")
    public AjaxResult<Map<String, Object>> stats() {
        return AjaxResult.success(this.iCommunityService.stats());
    }
}

