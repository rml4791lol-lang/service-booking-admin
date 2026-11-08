/*
 * Decompiled with CFR 0.152.
 */
package com.mdd.common.exception;

import com.mdd.common.enums.ErrorEnum;
import com.mdd.common.exception.BaseException;

public class OperateException
extends BaseException {
    public OperateException(String msg) {
        super(ErrorEnum.FAILED.getCode(), msg, ErrorEnum.SHOW_MSG.getCode());
    }

    public OperateException(String msg, Integer errCode) {
        super(errCode, msg, ErrorEnum.SHOW_MSG.getCode());
    }

    public OperateException(String msg, Integer errCode, Integer showCode) {
        super(errCode, msg, showCode);
    }
}

