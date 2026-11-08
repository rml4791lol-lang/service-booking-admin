/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson2.JSONObject
 *  com.mdd.admin.service.IUserService
 *  com.mdd.admin.validate.commons.PageValidate
 *  com.mdd.admin.validate.user.UserAddValidate
 *  com.mdd.admin.validate.user.UserSearchValidate
 *  com.mdd.admin.validate.user.UserUpdateValidate
 *  com.mdd.admin.validate.user.UserWalletValidate
 *  com.mdd.admin.vo.user.UserVo
 *  com.mdd.common.core.PageResult
 */
package com.mdd.admin.service;

import com.alibaba.fastjson2.JSONObject;
import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.validate.user.UserAddValidate;
import com.mdd.admin.validate.user.UserSearchValidate;
import com.mdd.admin.validate.user.UserUpdateValidate;
import com.mdd.admin.validate.user.UserWalletValidate;
import com.mdd.admin.vo.user.UserVo;
import com.mdd.common.core.PageResult;
import java.util.Map;

public interface IUserService {
    public PageResult<UserVo> list(PageValidate var1, UserSearchValidate var2);

    public UserVo detail(Integer var1);

    public void edit(UserUpdateValidate var1);

    public void adjustWallet(UserWalletValidate var1);

    /**
     * 余额调整（带操作人）：批量导入逐行调用，adminId 写入流水 extra 便于追溯；
     * 单条接口仍走 {@link #adjustWallet(UserWalletValidate)}（不记操作人，保持原口径）。
     */
    public void adjustWallet(UserWalletValidate var1, Integer adminId);

    public JSONObject getExportData(PageValidate var1, UserSearchValidate var2);

    public void add(UserAddValidate var1);

    public Map<String, Object> stats();

    public Map<String, Object> export(UserSearchValidate var1);
}

