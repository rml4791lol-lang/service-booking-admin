package com.mdd.admin.controller.region;

import com.mdd.admin.aop.Log;
import com.mdd.admin.service.region.IAreaService;
import com.mdd.admin.validate.commons.IdValidate;
import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.validate.region.AreaCreateValidate;
import com.mdd.admin.validate.region.AreaSearchValidate;
import com.mdd.admin.validate.region.AreaUpdateValidate;
import com.mdd.admin.vo.region.AreaListedVo;
import com.mdd.admin.vo.region.AreaOptionVo;
import com.mdd.common.core.AjaxResult;
import com.mdd.common.core.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/adminapi/area")
@Api(tags = "小区管理")
public class AreaController {
    @Resource
    IAreaService iAreaService;

    @GetMapping(value = "/list")
    @ApiOperation(value = "小区列表")
    public AjaxResult<PageResult<AreaListedVo>> list(@Validated PageValidate pageValidate,
                                                     @Validated AreaSearchValidate searchValidate) {
        return AjaxResult.success(iAreaService.list(pageValidate, searchValidate));
    }

    @GetMapping(value = "/detail")
    @ApiOperation(value = "小区详情")
    public AjaxResult<AreaListedVo> detail(@RequestParam(value = "id") Integer id) {
        return AjaxResult.success(iAreaService.detail(id));
    }

    @GetMapping(value = "/all")
    @ApiOperation(value = "小区下拉列表")
    public AjaxResult<List<AreaOptionVo>> all(
            @RequestParam(value = "communityId", required = false) Integer communityId,
            @RequestParam(value = "status", required = false) Integer status) {
        return AjaxResult.success(iAreaService.all(communityId, status));
    }

    @Log(title = "新增小区")
    @PostMapping(value = "/add")
    @ApiOperation(value = "新增小区")
    public AjaxResult<Map<String, Integer>> add(@Validated @RequestBody AreaCreateValidate createValidate) {
        Integer id = iAreaService.add(createValidate);
        LinkedHashMap<String, Integer> data = new LinkedHashMap<>();
        data.put("id", id);
        return AjaxResult.success(data);
    }

    @Log(title = "编辑小区")
    @PostMapping(value = "/edit")
    @ApiOperation(value = "编辑小区")
    public AjaxResult<Object> edit(@Validated @RequestBody AreaUpdateValidate updateValidate) {
        iAreaService.edit(updateValidate);
        return AjaxResult.success();
    }

    @Log(title = "删除小区")
    @PostMapping(value = "/del")
    @ApiOperation(value = "删除小区")
    public AjaxResult<Object> del(@Validated @RequestBody IdValidate idValidate) {
        iAreaService.del(idValidate.getId());
        return AjaxResult.success();
    }
}
