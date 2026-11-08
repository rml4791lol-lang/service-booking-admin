package com.mdd.admin.controller.instructor;

import com.mdd.admin.LikeAdminThreadLocal;
import com.mdd.admin.service.IInstructorAccountService;
import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.validate.instructor.InstructorAccountDisableValidate;
import com.mdd.admin.validate.instructor.InstructorAccountEnableValidate;
import com.mdd.admin.validate.instructor.InstructorAccountOpenValidate;
import com.mdd.admin.validate.instructor.InstructorAccountResetValidate;
import com.mdd.admin.vo.instructor.InstructorAccountAuditVo;
import com.mdd.admin.vo.instructor.InstructorAccountVo;
import com.mdd.common.core.AjaxResult;
import com.mdd.common.core.PageResult;
import com.mdd.common.util.IpUtils;
import com.mdd.common.validator.annotation.IDMust;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 教练账号管理（#31）。
 *
 * <p>权限说明：所有接口的 URI 会由 likeadmin 拦截器按
 * {@code instructor/account/*} 做权限校验；前端按钮同时使用同名的 v-perms。</p>
 */
@RestController
@RequestMapping(value = {"/adminapi/instructor/account"})
@Api(tags = {"教练账号管理"})
public class InstructorAccountController {
    @Resource
    IInstructorAccountService instructorAccountService;

    @GetMapping({"/status"})
    @ApiOperation("教练账号状态")
    public AjaxResult<InstructorAccountVo> status(@Validated @IDMust @RequestParam("id") Integer id) {
        return AjaxResult.success(instructorAccountService.status(id));
    }

    @PostMapping({"/open"})
    @ApiOperation("开通教练账号")
    public AjaxResult<IInstructorAccountService.InstructorAccountCredential> open(
            @Validated @RequestBody InstructorAccountOpenValidate validate) {
        IInstructorAccountService.InstructorAccountCredential result = instructorAccountService.open(
                validate.getId(), validate.getInitialPassword(), adminId(), adminName(), IpUtils.getIpAddress());
        return AjaxResult.success(result);
    }

    @PostMapping({"/reset"})
    @ApiOperation("重置教练密码")
    public AjaxResult<IInstructorAccountService.InstructorAccountCredential> reset(
            @Validated @RequestBody InstructorAccountResetValidate validate) {
        IInstructorAccountService.InstructorAccountCredential result = instructorAccountService.reset(
                validate.getId(), validate.getInitialPassword(), adminId(), adminName(), IpUtils.getIpAddress());
        return AjaxResult.success(result);
    }

    @PostMapping({"/disable"})
    @ApiOperation("停用教练账号")
    public AjaxResult<Object> disable(@Validated @RequestBody InstructorAccountDisableValidate validate) {
        instructorAccountService.disable(validate.getId(), validate.getReason(),
                adminId(), adminName(), IpUtils.getIpAddress());
        return AjaxResult.success();
    }

    @PostMapping({"/enable"})
    @ApiOperation("启用教练账号")
    public AjaxResult<Object> enable(@Validated @RequestBody InstructorAccountEnableValidate validate) {
        instructorAccountService.enable(validate.getId(), adminId(), adminName(), IpUtils.getIpAddress());
        return AjaxResult.success();
    }

    @GetMapping({"/audits"})
    @ApiOperation("教练账号操作审计")
    public AjaxResult<PageResult<InstructorAccountAuditVo>> audits(
            @Validated PageValidate pageValidate,
            @Validated @IDMust @RequestParam("id") Integer id,
            @RequestParam(value = "action", required = false) String action) {
        return AjaxResult.success(instructorAccountService.audits(id, action, pageValidate));
    }

    private Integer adminId() {
        return LikeAdminThreadLocal.getAdminId();
    }

    private String adminName() {
        Object name = LikeAdminThreadLocal.get("username");
        return name == null ? "" : name.toString();
    }
}
