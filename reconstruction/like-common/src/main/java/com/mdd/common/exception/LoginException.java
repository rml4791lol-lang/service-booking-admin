/*
 * Decompiled with CFR 0.152.
 */
package com.mdd.common.exception;

import com.mdd.common.enums.ErrorEnum;
import com.mdd.common.exception.BaseException;

public class LoginException
extends BaseException {
    public LoginException(Integer code, String msg) {
        super(code, msg, ErrorEnum.SHOW_MSG.getCode());
    }

    public LoginException(Integer code, String msg, Integer show) {
        super(code, msg, show);
    }
}

