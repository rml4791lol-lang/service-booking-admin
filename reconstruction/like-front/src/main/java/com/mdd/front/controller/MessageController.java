/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.common.core.AjaxResult
 *  com.mdd.front.LikeFrontThreadLocal
 *  com.mdd.front.controller.MessageController
 *  com.mdd.front.service.IMessageService
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
package com.mdd.front.controller;

import com.mdd.common.core.AjaxResult;
import com.mdd.front.LikeFrontThreadLocal;
import com.mdd.front.service.IMessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/api/message"})
@Api(tags={"\u7ad9\u5185\u4fe1\u901a\u77e5"})
public class MessageController {
    @Resource
    IMessageService iMessageService;

    @GetMapping(value={"/unreadCount"})
    @ApiOperation(value="\u83b7\u53d6\u672a\u8bfb\u6d88\u606f\u603b\u6570")
    public AjaxResult<Object> unreadCount() {
        Integer userId = LikeFrontThreadLocal.getUserId();
        Integer count = this.iMessageService.unreadCount(userId);
        LinkedHashMap<String, Integer> result = new LinkedHashMap<String, Integer>();
        result.put("count", count);
        return AjaxResult.success(result);
    }

    @GetMapping(value={"/list"})
    @ApiOperation(value="\u6d88\u606f\u5206\u9875\u5217\u8868")
    public AjaxResult<Map<String, Object>> list(@RequestParam(defaultValue="1") Integer pageNo, @RequestParam(defaultValue="20") Integer pageSize) {
        Integer userId = LikeFrontThreadLocal.getUserId();
        Map map = this.iMessageService.list(pageNo, pageSize, userId);
        return AjaxResult.success(map);
    }

    @PostMapping(value={"/read"})
    @ApiOperation(value="\u6807\u8bb0\u5355\u6761\u6d88\u606f\u4e3a\u5df2\u8bfb")
    public AjaxResult<Object> read(@RequestBody Map<String, Integer> params) {
        Integer userId = LikeFrontThreadLocal.getUserId();
        Integer messageId = params.get("messageId");
        if (messageId != null) {
            this.iMessageService.read(messageId, userId);
        }
        return AjaxResult.success();
    }
}

