package com.mdd.front.controller;

import com.mdd.common.core.AjaxResult;
import com.mdd.front.LikeFrontThreadLocal;
import com.mdd.front.service.IInstructorCertService;
import com.mdd.front.validate.instructor.InstructorCertSaveValidate;
import com.mdd.front.validate.instructor.InstructorCertSubmitValidate;
import com.mdd.front.vo.instructor.InstructorCertVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 教练端资质证件（#49）。
 *
 * <p>URI 位于 /api/instructor 下，由 like-front 拦截器统一校验教练登录态、
 * 停用/待审核状态与首登改密；Controller 只从 ThreadLocal 取教练 ID，
 * 不接受客户端传入的 coachId，避免越权。</p>
 */
@RestController
@RequestMapping(value = {"/api/instructor/certificates"})
@Api(tags = {"教练资质证件(前台)"})
public class InstructorCertController {

    @Resource
    IInstructorCertService instructorCertService;

    @GetMapping(value = {"", "/list"})
    @ApiOperation(value = "我的资质列表")
    public AjaxResult<List<InstructorCertVo>> list() {
        Integer instructorId = LikeFrontThreadLocal.getUserId();
        return AjaxResult.success(instructorCertService.list(instructorId));
    }

    @PostMapping(value = {"/save"})
    @ApiOperation(value = "保存资质草稿")
    public AjaxResult<InstructorCertVo> save(@Validated @RequestBody InstructorCertSaveValidate validate) {
        Integer instructorId = LikeFrontThreadLocal.getUserId();
        return AjaxResult.success(instructorCertService.save(instructorId, validate));
    }

    @PostMapping(value = {"/submit"})
    @ApiOperation(value = "提交资质审核(可多张)")
    public AjaxResult<Map<String, Object>> submit(@Validated @RequestBody InstructorCertSubmitValidate validate) {
        Integer instructorId = LikeFrontThreadLocal.getUserId();
        return AjaxResult.success(instructorCertService.submit(instructorId, validate.getIds()));
    }
}
