/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.controller.channel.ChannelOaReplyController
 *  com.mdd.admin.service.IChannelOaReplyService
 *  com.mdd.admin.validate.channel.ChannelRpValidate
 *  com.mdd.admin.validate.commons.IdValidate
 *  com.mdd.admin.validate.commons.PageValidate
 *  com.mdd.admin.vo.channel.ChannelRpVo
 *  com.mdd.common.core.AjaxResult
 *  com.mdd.common.core.PageResult
 *  com.mdd.common.validator.annotation.IDMust
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
package com.mdd.admin.controller.channel;

import com.mdd.admin.service.IChannelOaReplyService;
import com.mdd.admin.validate.channel.ChannelRpValidate;
import com.mdd.admin.validate.commons.IdValidate;
import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.vo.channel.ChannelRpVo;
import com.mdd.common.core.AjaxResult;
import com.mdd.common.core.PageResult;
import com.mdd.common.validator.annotation.IDMust;
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
@RequestMapping(value={"/adminapi/channel.official_account_reply"})
@Api(tags={"\u516c\u4f17\u53f7\u56de\u590d"})
public class ChannelOaReplyController {
    @Resource
    IChannelOaReplyService iChannelOaReplyService;

    @GetMapping(value={"/lists"})
    @ApiOperation(value="\u9ed8\u8ba4\u56de\u590d\u5217\u8868")
    public AjaxResult<PageResult<ChannelRpVo>> list(@Validated PageValidate pageValidate, @RequestParam(value="reply_type") Integer replyType) {
        PageResult list = this.iChannelOaReplyService.list(pageValidate, replyType);
        return AjaxResult.success(list);
    }

    @GetMapping(value={"/detail"})
    @ApiOperation(value="\u9ed8\u8ba4\u56de\u590d\u8be6\u60c5")
    public AjaxResult<ChannelRpVo> detail(@Validated @IDMust @RequestParam(value="id") Integer id) {
        ChannelRpVo vo = this.iChannelOaReplyService.detail(id);
        return AjaxResult.success(vo);
    }

    @PostMapping(value={"/add"})
    @ApiOperation(value="\u9ed8\u8ba4\u56de\u590d\u65b0\u589e")
    public AjaxResult<Object> add(@Validated @RequestBody ChannelRpValidate defaultValidate) {
        this.iChannelOaReplyService.add(defaultValidate);
        return AjaxResult.success();
    }

    @PostMapping(value={"/edit"})
    @ApiOperation(value="\u9ed8\u8ba4\u56de\u590d\u7f16\u8f91")
    public AjaxResult<Object> edit(@Validated @RequestBody ChannelRpValidate defaultValidate) {
        this.iChannelOaReplyService.edit(defaultValidate);
        return AjaxResult.success();
    }

    @PostMapping(value={"/delete"})
    @ApiOperation(value="\u9ed8\u8ba4\u56de\u590d\u5220\u9664")
    public AjaxResult<Object> del(@Validated @RequestBody IdValidate idValidate) {
        this.iChannelOaReplyService.del(idValidate.getId());
        return AjaxResult.success();
    }

    @PostMapping(value={"/status"})
    @ApiOperation(value="\u9ed8\u8ba4\u56de\u590d\u72b6\u6001")
    public AjaxResult<Object> status(@Validated @RequestBody IdValidate idValidate) {
        this.iChannelOaReplyService.status(idValidate.getId());
        return AjaxResult.success();
    }
}

