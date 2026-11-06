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
 * 小程序小区只读接口：列表 / 详情。
 *
 * 列表的 onlyLive=true 对应“有已上架课程且有可约场次”的导航/地图口径；
 * 不传 onlyLive 时返回后台启用的全部小区（个人资料等下拉场景）。
 */
@RestController
@RequestMapping(value = "/api/area")
@Api(tags = "小区（小程序只读）")
public class AreaController {

    @Resource
    IRegionService iRegionService;

    @NotLogin
    @GetMapping(value = "/list")
    @ApiOperation(value = "小区列表")
    public AjaxResult<List<Map<String, Object>>> list(
            @RequestParam(value = "onlyLive", required = false) Boolean onlyLive,
            @RequestParam(value = "districtId", required = false) Integer districtId,
            @RequestParam(value = "streetId", required = false) Integer streetId,
            @RequestParam(value = "communityId", required = false) Integer communityId,
            @RequestParam(value = "keyword", required = false) String keyword) {
        return AjaxResult.success(iRegionService.areas(onlyLive, districtId, streetId, communityId, keyword));
    }

    @NotLogin
    @GetMapping(value = "/detail")
    @ApiOperation(value = "小区详情（含课程与教练）")
    public AjaxResult<Map<String, Object>> detail(@RequestParam(value = "id") Integer id) {
        return AjaxResult.success(iRegionService.areaDetail(id));
    }
}
