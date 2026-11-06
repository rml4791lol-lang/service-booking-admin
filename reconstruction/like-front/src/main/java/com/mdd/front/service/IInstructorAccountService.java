package com.mdd.front.service;

import com.mdd.front.validate.instructor.InstructorPasswordChangeValidate;
import com.mdd.front.vo.login.LoginTokenVo;
import com.mdd.front.vo.user.InstructorAccountInfoVo;

/**
 * 教练端账号自助（#31）：查看账号状态、首次登录/日常修改密码。
 */
public interface IInstructorAccountService {

    InstructorAccountInfoVo info(Integer instructorId);

    LoginTokenVo changePassword(Integer instructorId, InstructorPasswordChangeValidate validate, String ip);
}
