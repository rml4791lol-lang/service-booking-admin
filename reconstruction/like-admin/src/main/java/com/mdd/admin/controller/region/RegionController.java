package com.mdd.admin.controller.region;

import com.mdd.admin.service.region.IRegionService;
import com.mdd.admin.vo.region.RegionStreetVo;
import com.mdd.common.aop.NotPower;
import com.mdd.common.core.AjaxResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping(value = "/adminapi/region")
@Api(tags = "区域基础数据")
public class RegionController {
    @Resource
    IRegionService iRegionService;

    /**
     * 街道下拉（基础字典）：社区列表、区域维护、班级管理等页面共用。
     * 2026-11-04 修复：此前无 @NotPower 且菜单表没有 region/streets 权限点，
     * 非超管角色一调就「无相关权限」，页面 Promise.all 初始化崩溃成白屏。
     * 街道数据不含敏感信息，登录即可放行。
     */
    @NotPower
    @GetMapping(value = "/streets")
    @ApiOperation(value = "街道下拉列表")
    public AjaxResult<List<RegionStreetVo>> streets() {
        return AjaxResult.success(iRegionService.streets());
    }
}
