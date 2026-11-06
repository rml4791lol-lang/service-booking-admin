/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.toolkit.Assert
 *  com.mdd.common.aop.NotPower
 *  com.mdd.common.core.AjaxResult
 *  com.mdd.common.core.PageResult
 *  com.mdd.common.exception.OperateException
 *  com.mdd.common.util.StringUtils
 *  com.mdd.common.util.YmlUtils
 *  com.mdd.common.validator.annotation.IDMust
 *  javax.annotation.Resource
 *  javax.servlet.http.HttpServletResponse
 *  org.apache.commons.io.IOUtils
 *  org.springframework.validation.annotation.Validated
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.RequestBody
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RequestParam
 *  org.springframework.web.bind.annotation.RestController
 */
package com.mdd.generator.controller;

import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.mdd.common.aop.NotPower;
import com.mdd.common.core.AjaxResult;
import com.mdd.common.core.PageResult;
import com.mdd.common.exception.OperateException;
import com.mdd.common.util.StringUtils;
import com.mdd.common.util.YmlUtils;
import com.mdd.common.validator.annotation.IDMust;
import com.mdd.generator.service.IGenerateService;
import com.mdd.generator.validate.GenParam;
import com.mdd.generator.validate.PageParam;
import com.mdd.generator.vo.DbColumnVo;
import com.mdd.generator.vo.DbTableVo;
import com.mdd.generator.vo.GenTableVo;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/adminapi/gen"})
public class GenController {
    @Resource
    IGenerateService iGenerateService;

    @GetMapping(value={"/db"})
    public AjaxResult<PageResult<DbTableVo>> db(@Validated PageParam pageParam, @RequestParam Map<String, String> params) {
        PageResult<DbTableVo> list = this.iGenerateService.db(pageParam, params);
        return AjaxResult.success(list);
    }

    @NotPower
    @GetMapping(value={"/dbAll"})
    public AjaxResult<List<DbTableVo>> dbAll(@RequestParam Map<String, String> params) {
        List<DbTableVo> list = this.iGenerateService.dbAll(params);
        return AjaxResult.success(list);
    }

    @NotPower
    @GetMapping(value={"/dbColumn"})
    public AjaxResult<List<DbColumnVo>> dbColumn(@RequestParam String tableName) {
        List<DbColumnVo> list = this.iGenerateService.dbColumn(tableName);
        return AjaxResult.success(list);
    }

    @GetMapping(value={"/list"})
    public AjaxResult<PageResult<GenTableVo>> list(@Validated PageParam pageParam, @RequestParam Map<String, String> params) {
        PageResult<GenTableVo> list = this.iGenerateService.list(pageParam, params);
        return AjaxResult.success(list);
    }

    @GetMapping(value={"/detail"})
    public AjaxResult<Map<String, Object>> detail(@Validated @IDMust @RequestParam(value="id") Integer id) {
        Map<String, Object> maps = this.iGenerateService.detail(id);
        return AjaxResult.success((Integer)200, (String)"\u6210\u529f", maps);
    }

    @PostMapping(value={"/importTable"})
    public AjaxResult<Object> importTable(String tables) {
        Assert.notNull((Object)tables, (String)"\u8bf7\u9009\u62e9\u8981\u5bfc\u5165\u7684\u8868", (Object[])new Object[0]);
        String[] tableNames = tables.split(",");
        this.iGenerateService.importTable(tableNames);
        return AjaxResult.success((String)"\u6210\u529f", (Integer)0);
    }

    @PostMapping(value={"/editTable"})
    public AjaxResult<Object> editTable(@Validated @RequestBody GenParam genParam) {
        this.iGenerateService.editTable(genParam);
        return AjaxResult.success((String)"\u6210\u529f", (Integer)0);
    }

    @PostMapping(value={"/delTable"})
    public AjaxResult<Object> deleteTable(@Validated(value={GenParam.delete.class}) @RequestBody GenParam genParam) {
        this.iGenerateService.deleteTable(genParam.getIds());
        return AjaxResult.success((String)"\u6210\u529f", (Integer)0);
    }

    @PostMapping(value={"/syncTable"})
    public AjaxResult<Object> syncTable(@Validated @IDMust @RequestParam(value="id") Integer id) {
        this.iGenerateService.syncTable(id);
        return AjaxResult.success((String)"\u6210\u529f", (Integer)0);
    }

    @GetMapping(value={"/previewCode"})
    public AjaxResult<Map<String, String>> previewCode(@Validated @IDMust @RequestParam(value="id") Integer id) {
        Map<String, String> map = this.iGenerateService.previewCode(id);
        return AjaxResult.success(map);
    }

    @GetMapping(value={"/genCode"})
    public AjaxResult<Object> genCode(String tables) {
        String[] tableNames;
        String production = YmlUtils.get((String)"like.production");
        if (StringUtils.isNotEmpty((String)production) && production.equals("true")) {
            throw new OperateException("\u62b1\u6b49,\u6f14\u793a\u73af\u5883\u4e0d\u5141\u8bb8\u64cd\u4f5c\uff01");
        }
        Assert.notNull((Object)tables, (String)"\u8bf7\u9009\u62e9\u8981\u751f\u6210\u7684\u8868", (Object[])new Object[0]);
        for (String tableName : tableNames = tables.split(",")) {
            this.iGenerateService.genCode(tableName);
        }
        return AjaxResult.success();
    }

    @GetMapping(value={"/downloadCode"})
    public void downloadCode(HttpServletResponse response, String tables) throws IOException {
        Assert.notNull((Object)tables, (String)"\u8bf7\u9009\u62e9\u8981\u751f\u6210\u7684\u8868", (Object[])new Object[0]);
        String[] tableNames = tables.split(",");
        byte[] data = this.iGenerateService.downloadCode(tableNames);
        response.reset();
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Expose-Headers", "Content-Disposition");
        response.setHeader("Content-Disposition", "attachment; filename=\"likeadmin-curd.zip\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");
        IOUtils.write((byte[])data, (OutputStream)response.getOutputStream());
    }
}

