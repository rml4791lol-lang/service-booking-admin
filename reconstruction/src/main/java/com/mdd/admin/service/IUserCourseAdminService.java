package com.mdd.admin.service;

import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.validate.user.UserCourseAdjustValidate;
import com.mdd.admin.validate.user.UserCourseSearchValidate;
import com.mdd.admin.vo.user.UserCourseListVo;
import com.mdd.common.core.PageResult;

/**
 * 学员课时账户（老课包 la_user_course）后台管理。
 */
public interface IUserCourseAdminService {

    PageResult<UserCourseListVo> list(PageValidate pageValidate, UserCourseSearchValidate searchValidate);

    void adjust(UserCourseAdjustValidate adjustValidate);
}
