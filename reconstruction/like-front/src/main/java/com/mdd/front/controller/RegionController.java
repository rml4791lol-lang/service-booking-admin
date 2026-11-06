package com.mdd.front.controller;

import com.mdd.common.aop.NotLogin;
import com.mdd.common.core.AjaxResult;
import com.mdd.front.service.IRegionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 小程序地区只读接口：城市 / 分区 / 街道 / 社区。
 *
 * 与 pages/areas、pages/area-detail 的 mock 契约一一对应，公开只读，不要求登录。
 */
@RestController
@RequestMapping(value = "/api/region")
@Api(tags = "地区层级（小程序只读）")
public class RegionController {

    @Resource
    IRegionService iRegionService;

    @NotLogin
    @GetMapping(value = "/cities")
    @ApiOperation(value = "城市列表")
    public AjaxResult<List<Map<String, Object>>> cities() {
        return AjaxResult.success(iRegionService.cities());
    }

    @NotLogin
    @GetMapping(value = "/districts")
    @ApiOperation(value = "分区列表")
    public AjaxResult<List<Map<String, Object>>> districts(
            @RequestParam(value = "cityId", required = false) Integer cityId) {
        return AjaxResult.success(iRegionService.districts(cityId));
    }

    @NotLogin
    @GetMapping(value = "/streets")
    @ApiOperation(value = "街道列表（areaCount 只统计可见小区）")
    public AjaxResult<List<Map<String, Object>>> streets(
            @RequestParam(value = "districtId", required = false) Integer districtId) {
        return AjaxResult.success(iRegionService.streets(districtId));
    }

    @NotLogin
    @GetMapping(value = "/communities")
    @ApiOperation(value = "社区列表")
    public AjaxResult<List<Map<String, Object>>> communities(
            @RequestParam(value = "streetId", required = false) Integer streetId) {
        return AjaxResult.success(iRegionService.communities(streetId));
    }
}
