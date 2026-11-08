/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.controller.channel.ChannelOaCallBackController
 *  com.mdd.admin.service.IChannelOaCallBackService
 *  com.mdd.common.aop.NotLogin
 *  io.swagger.annotations.Api
 *  io.swagger.annotations.ApiOperation
 *  javax.annotation.Resource
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.RequestBody
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RequestParam
 *  org.springframework.web.bind.annotation.RestController
 */
package com.mdd.admin.controller.channel;

import com.mdd.admin.service.IChannelOaCallBackService;
import com.mdd.common.aop.NotLogin;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/adminapi/channel/oa/callback"})
@Api(tags={"\u516c\u4f17\u53f7\u670d\u52a1\u5668\u9a8c\u8bc1\u53ca\u6d88\u606f\u56de\u590d"})
public class ChannelOaCallBackController {
    @Resource
    private IChannelOaCallBackService iChannelOaCallBackService;

    @NotLogin
    @GetMapping(produces={"text/plain;charset=utf-8"})
    @ApiOperation(value="\u516c\u4f17\u53f7\u670d\u52a1\u5668\u5730\u5740\u9a8c\u8bc1")
    public String authGet(@RequestParam(name="signature", required=false) String signature, @RequestParam(name="timestamp", required=false) String timestamp, @RequestParam(name="nonce", required=false) String nonce, @RequestParam(name="echostr", required=false) String echostr) {
        return this.iChannelOaCallBackService.checkSignature(signature, timestamp, nonce, echostr);
    }

    @NotLogin
    @PostMapping(produces={"application/xml; charset=UTF-8"})
    @ApiOperation(value="\u516c\u4f17\u53f7\u6d88\u606f\u56de\u590d")
    public String post(@RequestBody String requestBody, @RequestParam(value="signature") String signature, @RequestParam(value="timestamp") String timestamp, @RequestParam(value="nonce") String nonce, @RequestParam(name="encrypt_type", required=false) String encType, @RequestParam(name="msg_signature", required=false) String msgSignature) {
        return this.iChannelOaCallBackService.post(requestBody, signature, timestamp, nonce, encType, msgSignature);
    }
}

