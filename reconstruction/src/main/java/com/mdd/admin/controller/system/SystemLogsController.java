/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.controller.system.SystemLogsController
 *  com.mdd.admin.service.system.ISystemLogsServer
 *  com.mdd.admin.validate.commons.PageValidate
 *  com.mdd.admin.validate.system.SystemSearchLoginsValidate
 *  com.mdd.admin.validate.system.SystemSearchOperateValidate
 *  com.mdd.admin.vo.system.SystemLogLoginVo
 *  com.mdd.admin.vo.system.SystemLogOperateVo
 *  com.mdd.common.core.AjaxResult
 *  com.mdd.common.core.PageResult
 *  io.swagger.annotations.Api
 *  io.swagger.annotations.ApiOperation
 *  javax.annotation.Resource
 *  org.springframework.validation.annotation.Validated
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RestController
 */
package com.mdd.admin.controller.system;

import com.mdd.admin.service.system.ISystemLogsServer;
import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.validate.system.SystemSearchLoginsValidate;
import com.mdd.admin.validate.system.SystemSearchOperateValidate;
import com.mdd.admin.vo.system.SystemLogLoginVo;
import com.mdd.admin.vo.system.SystemLogOperateVo;
import com.mdd.common.core.AjaxResult;
import com.mdd.common.core.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/adminapi/setting.system.log"})
@Api(tags={"\u7cfb\u7edf\u65e5\u5fd7\u7ba1\u7406"})
public class SystemLogsController {
    @Resource
    ISystemLogsServer iSystemLogsServer;

    @GetMapping(value={"/lists"})
    @ApiOperation(value="\u7cfb\u7edf\u64cd\u4f5c\u65e5\u5fd7")
    public AjaxResult<PageResult<SystemLogOperateVo>> lists(@Validated PageValidate pageValidate, @Validated SystemSearchOperateValidate searchValidate) {
        PageResult list = this.iSystemLogsServer.operate(pageValidate, searchValidate);
        return AjaxResult.success(list);
    }

    @GetMapping(value={"/login"})
    @ApiOperation(value="\u7cfb\u7edf\u767b\u5f55\u65e5\u5fd7")
    public AjaxResult<PageResult<SystemLogLoginVo>> login(@Validated PageValidate pageValidate, @Validated SystemSearchLoginsValidate searchValidate) {
        PageResult list = this.iSystemLogsServer.login(pageValidate, searchValidate);
        return AjaxResult.success(list);
    }
}

