/*
 * Decompiled with CFR 0.152.
 */
package com.mdd.common.exception;

import com.mdd.common.enums.ErrorEnum;
import com.mdd.common.exception.BaseException;

public class PaymentException
extends BaseException {
    public PaymentException(String msg) {
        super(ErrorEnum.FAILED.getCode(), msg, ErrorEnum.SHOW_MSG.getCode());
    }

    public PaymentException(String msg, Integer errCode) {
        super(errCode, msg, ErrorEnum.SHOW_MSG.getCode());
    }
}

