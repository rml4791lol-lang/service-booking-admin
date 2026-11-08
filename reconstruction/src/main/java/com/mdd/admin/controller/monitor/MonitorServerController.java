/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.aop.Log
 *  com.mdd.admin.controller.monitor.MonitorServerController
 *  com.mdd.common.core.AjaxResult
 *  com.mdd.common.core.ServerResult
 *  io.swagger.annotations.Api
 *  io.swagger.annotations.ApiOperation
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RestController
 */
package com.mdd.admin.controller.monitor;

import com.mdd.admin.aop.Log;
import com.mdd.common.core.AjaxResult;
import com.mdd.common.core.ServerResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"adminapi/setting.system.system"})
@Api(tags={"\u76d1\u63a7\u670d\u52a1\u7ba1\u7406"})
public class MonitorServerController {
    @Log(title="\u670d\u52a1\u76d1\u63a7")
    @GetMapping(value={"/info"})
    @ApiOperation(value="\u670d\u52a1\u76d1\u63a7")
    public AjaxResult<Map<String, Object>> info() {
        ServerResult server = new ServerResult();
        return AjaxResult.success(server.copyTo());
    }
}

