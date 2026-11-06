package com.mdd.admin.controller.instructor;

import com.mdd.admin.LikeAdminThreadLocal;
import com.mdd.admin.aop.Log;
import com.mdd.admin.service.IInstructorCertService;
import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.validate.instructor.InstructorCertAuditValidate;
import com.mdd.admin.validate.instructor.InstructorCertSearchValidate;
import com.mdd.admin.vo.instructor.InstructorCertListedVo;
import com.mdd.common.core.AjaxResult;
import com.mdd.common.core.PageResult;
import com.mdd.common.util.IpUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 后台教练资质审核（#49）。
 *
 * <p>权限按 URI 校验：instructor/cert/list（C 菜单）、instructor/cert/detail、
 * instructor/cert/audit；教练下拉沿用 instructor/all。前端按钮使用同名 v-perms。</p>
 */
@RestController
@RequestMapping(value = {"/adminapi/instructor/cert"})
@Api(tags = {"教练资质审核"})
public class InstructorCertController {

    @Resource
    IInstructorCertService instructorCertService;

    @GetMapping({"/list"})
    @ApiOperation("教练资质列表")
    public AjaxResult<PageResult<InstructorCertListedVo>> list(
            @Validated PageValidate pageValidate,
            @Validated InstructorCertSearchValidate searchValidate) {
        return AjaxResult.success(instructorCertService.list(pageValidate, searchValidate));
    }

    @GetMapping({"/detail"})
    @ApiOperation("教练资质详情与审核留痕")
    public AjaxResult<Map<String, Object>> detail(@RequestParam("id") Long id) {
        return AjaxResult.success(instructorCertService.detail(id));
    }

    @Log(title = "教练资质审核")
    @PostMapping({"/audit"})
    @ApiOperation("审核通过/驳回")
    public AjaxResult<Object> audit(@Validated @RequestBody InstructorCertAuditValidate validate) {
        instructorCertService.audit(validate.getId(), validate.getAction(), validate.getRemark(),
                LikeAdminThreadLocal.getAdminId(), adminName(), IpUtils.getIpAddress());
        return AjaxResult.success();
    }

    private String adminName() {
        Object name = LikeAdminThreadLocal.get("username");
        return name == null ? "" : name.toString();
    }
}
