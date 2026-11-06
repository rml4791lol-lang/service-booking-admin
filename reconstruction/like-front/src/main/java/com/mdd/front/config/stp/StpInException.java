/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  cn.dev33.satoken.exception.NotLoginException
 *  cn.dev33.satoken.exception.NotPermissionException
 *  com.mdd.common.core.AjaxResult
 *  com.mdd.common.enums.ErrorEnum
 *  com.mdd.front.config.stp.StpInException
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.http.HttpStatus
 *  org.springframework.web.bind.annotation.ControllerAdvice
 *  org.springframework.web.bind.annotation.ExceptionHandler
 *  org.springframework.web.bind.annotation.ResponseBody
 *  org.springframework.web.bind.annotation.ResponseStatus
 */
package com.mdd.front.config.stp;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import com.mdd.common.core.AjaxResult;
import com.mdd.common.enums.ErrorEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class StpInException {
    private static final Logger log = LoggerFactory.getLogger(StpInException.class);

    @ResponseStatus(value=HttpStatus.OK)
    @ExceptionHandler(value={NotLoginException.class})
    @ResponseBody
    public AjaxResult<Object> handleNotLoginException(NotLoginException e) {
        String msg = e.getMessage().startsWith("Token\u65e0\u6548") ? "\u5c1a\u672a\u767b\u5f55,\u8bf7\u5148\u767b\u5f55!" : e.getMessage();
        return AjaxResult.failed((Integer)ErrorEnum.TOKEN_INVALID.getCode(), (String)msg);
    }

    @ResponseStatus(value=HttpStatus.OK)
    @ExceptionHandler(value={NotPermissionException.class})
    @ResponseBody
    public AjaxResult<Object> handleNotPermissionException() {
        return AjaxResult.failed((Integer)ErrorEnum.NO_PERMISSION.getCode(), (String)ErrorEnum.NO_PERMISSION.getMsg());
    }
}

