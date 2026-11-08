/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.aop.Log
 *  com.mdd.admin.controller.agent.AgentController
 *  com.mdd.admin.service.agent.IAgentService
 *  com.mdd.admin.validate.agent.AgentCreateValidate
 *  com.mdd.admin.validate.agent.AgentSearchValidate
 *  com.mdd.admin.validate.agent.AgentUpdateValidate
 *  com.mdd.admin.validate.commons.IdValidate
 *  com.mdd.admin.validate.commons.PageValidate
 *  com.mdd.admin.vo.agent.AgentDetailVo
 *  com.mdd.admin.vo.agent.AgentListedVo
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

import com.mdd.admin.aop.Log;
import com.mdd.admin.service.agent.IAgentService;
import com.mdd.admin.validate.agent.AgentCreateValidate;
import com.mdd.admin.validate.agent.AgentSearchValidate;
import com.mdd.admin.validate.agent.AgentUpdateValidate;
import com.mdd.admin.validate.commons.IdValidate;
import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.vo.agent.AgentDetailVo;
import com.mdd.admin.vo.agent.AgentListedVo;
import com.mdd.common.core.AjaxResult;
import com.mdd.common.core.PageResult;
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
@RequestMapping(value={"/adminapi/agent"})
@Api(tags={"\u4ee3\u7406\u7ba1\u7406"})
public class AgentController {
    @Resource
    IAgentService iAgentService;

    @GetMapping(value={"/list"})
    @ApiOperation(value="\u4ee3\u7406\u5217\u8868")
    public AjaxResult<PageResult<AgentListedVo>> list(@Validated PageValidate pageValidate, @Validated AgentSearchValidate searchValidate) {
        PageResult list = this.iAgentService.list(pageValidate, searchValidate);
        return AjaxResult.success(list);
    }

    @GetMapping(value={"/detail"})
    @ApiOperation(value="\u4ee3\u7406\u8be6\u60c5")
    public AjaxResult<AgentDetailVo> detail(@RequestParam(value="id") Integer id) {
        AgentDetailVo detail = this.iAgentService.detail(id);
        return AjaxResult.success(detail);
    }

    @Log(title="\u65b0\u589e\u4ee3\u7406")
    @PostMapping(value={"/add"})
    @ApiOperation(value="\u65b0\u589e\u4ee3\u7406")
    public AjaxResult<Object> add(@Validated @RequestBody AgentCreateValidate createValidate) {
        this.iAgentService.add(createValidate);
        return AjaxResult.success();
    }

    @Log(title="\u7f16\u8f91\u4ee3\u7406")
    @PostMapping(value={"/edit"})
    @ApiOperation(value="\u7f16\u8f91\u4ee3\u7406")
    public AjaxResult<Object> edit(@Validated @RequestBody AgentUpdateValidate updateValidate) {
        this.iAgentService.edit(updateValidate);
        return AjaxResult.success();
    }

    @Log(title="\u5220\u9664\u4ee3\u7406")
    @PostMapping(value={"/del"})
    @ApiOperation(value="\u5220\u9664\u4ee3\u7406")
    public AjaxResult<Object> del(@Validated @RequestBody IdValidate idValidate) {
        this.iAgentService.del(idValidate.getId());
        return AjaxResult.success();
    }
}

