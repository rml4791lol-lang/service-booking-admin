/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.common.core.PageResult
 *  com.mdd.front.service.IUserAccountLogService
 *  com.mdd.front.validate.common.PageValidate
 *  com.mdd.front.validate.users.UserAccountLogSearchValidate
 *  com.mdd.front.vo.user.UserAccountListVo
 */
package com.mdd.front.service;

import com.mdd.common.core.PageResult;
import com.mdd.front.validate.common.PageValidate;
import com.mdd.front.validate.users.UserAccountLogSearchValidate;
import com.mdd.front.vo.user.UserAccountListVo;

public interface IUserAccountLogService {
    public PageResult<UserAccountListVo> lists(PageValidate var1, UserAccountLogSearchValidate var2);
}

