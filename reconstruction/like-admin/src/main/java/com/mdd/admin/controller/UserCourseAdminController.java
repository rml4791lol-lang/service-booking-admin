package com.mdd.admin.controller;

import com.mdd.admin.aop.Log;
import com.mdd.admin.service.IUserCourseAdminService;
import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.validate.user.UserCourseAdjustValidate;
import com.mdd.admin.validate.user.UserCourseSearchValidate;
import com.mdd.admin.vo.user.UserCourseListVo;
import com.mdd.common.core.AjaxResult;
import com.mdd.common.core.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 学员课时账户（老课包）管理：只读写 la_user_course，不触碰券体系。
 */
@RestController
@RequestMapping({"adminapi/user_course"})
@Api(tags = {"学员课时账户（老课包）"})
public class UserCourseAdminController {

    @Resource
    IUserCourseAdminService userCourseAdminService;

    @GetMapping({"/list"})
    @ApiOperation("老课包列表")
    public AjaxResult<PageResult<UserCourseListVo>> list(@Validated PageValidate pageValidate,
                                                         @Validated UserCourseSearchValidate searchValidate) {
        return AjaxResult.success(userCourseAdminService.list(pageValidate, searchValidate));
    }

    @Log(title = "调整学员课时")
    @PostMapping({"/adjust"})
    @ApiOperation("调整学员课时")
    public AjaxResult<Object> adjust(@Validated @RequestBody UserCourseAdjustValidate adjustValidate) {
        userCourseAdminService.adjust(adjustValidate);
        return AjaxResult.success();
    }
}
