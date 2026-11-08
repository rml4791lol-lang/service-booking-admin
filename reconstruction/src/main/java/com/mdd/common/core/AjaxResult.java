/*
 * Decompiled with CFR 0.152.
 */
package com.mdd.common.core;

import com.mdd.common.enums.ErrorEnum;
import java.util.ArrayList;

public class AjaxResult<T> {
    private Integer code;
    private String msg;
    private T data;
    private Integer show;

    protected AjaxResult() {
    }

    public AjaxResult(Integer code, String msg, T data, Integer show) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.show = show;
    }

    public static AjaxResult<Object> success() {
        return new AjaxResult<Object>(ErrorEnum.SUCCESS.getCode(), ErrorEnum.SUCCESS.getMsg(), new ArrayList(), ErrorEnum.SHOW_MSG.getCode());
    }

    public static AjaxResult<Object> success(Integer code) {
        return new AjaxResult<Object>(code, ErrorEnum.SUCCESS.getMsg(), new ArrayList(), ErrorEnum.SHOW_MSG.getCode());
    }

    public static AjaxResult<Object> success(String msg) {
        return new AjaxResult<Object>(ErrorEnum.SUCCESS.getCode(), msg, new ArrayList(), ErrorEnum.SHOW_MSG.getCode());
    }

    public static AjaxResult<Object> success(String msg, Integer show) {
        return new AjaxResult<Object>(ErrorEnum.SUCCESS.getCode(), msg, new ArrayList(), show);
    }

    public static <T> AjaxResult<T> success(T data) {
        return new AjaxResult<T>(ErrorEnum.SUCCESS.getCode(), ErrorEnum.SUCCESS.getMsg(), data, ErrorEnum.HIDE_MSG.getCode());
    }

    public static AjaxResult<Object> success(Integer code, String msg) {
        return new AjaxResult<Object>(code, msg, new ArrayList(), ErrorEnum.SHOW_MSG.getCode());
    }

    public static AjaxResult<Object> success(Integer code, String msg, Integer show) {
        return new AjaxResult<Object>(code, msg, new ArrayList(), show);
    }

    public static <T> AjaxResult<T> success(Integer code, T data, Integer show) {
        return new AjaxResult<T>(code, ErrorEnum.SUCCESS.getMsg(), data, show);
    }

    public static <T> AjaxResult<T> success(String msg, T data) {
        return new AjaxResult<T>(ErrorEnum.SUCCESS.getCode(), msg, data, ErrorEnum.SHOW_MSG.getCode());
    }

    public static <T> AjaxResult<T> success(Integer code, String msg, T data) {
        return new AjaxResult<T>(code, msg, data, ErrorEnum.SHOW_MSG.getCode());
    }

    public static AjaxResult<Object> failed(Integer code) {
        return new AjaxResult<Object>(code, ErrorEnum.FAILED.getMsg(), new ArrayList(), ErrorEnum.SHOW_MSG.getCode());
    }

    public static AjaxResult<Object> failed(String msg) {
        return new AjaxResult<Object>(ErrorEnum.FAILED.getCode(), msg, new ArrayList(), ErrorEnum.SHOW_MSG.getCode());
    }

    public static <T> AjaxResult<T> failed(T data) {
        return new AjaxResult<T>(ErrorEnum.FAILED.getCode(), ErrorEnum.FAILED.getMsg(), data, ErrorEnum.SHOW_MSG.getCode());
    }

    public static AjaxResult<Object> failed(Integer code, String msg) {
        return new AjaxResult<Object>(code, msg, new ArrayList(), ErrorEnum.SHOW_MSG.getCode());
    }

    public static <T> AjaxResult<T> failed(Integer code, String msg, T data) {
        return new AjaxResult<T>(code, msg, data, ErrorEnum.SHOW_MSG.getCode());
    }

    public static <T> AjaxResult<T> failed(Integer code, String msg, T data, Integer show) {
        return new AjaxResult<T>(code, msg, data, show);
    }

    public Integer getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }

    public T getData() {
        return this.data;
    }

    public Integer getShow() {
        return this.show;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setShow(Integer show) {
        this.show = show;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AjaxResult)) {
            return false;
        }
        AjaxResult other = (AjaxResult)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Integer this$code = this.getCode();
        Integer other$code = other.getCode();
        if (this$code == null ? other$code != null : !((Object)this$code).equals(other$code)) {
            return false;
        }
        Integer this$show = this.getShow();
        Integer other$show = other.getShow();
        if (this$show == null ? other$show != null : !((Object)this$show).equals(other$show)) {
            return false;
        }
        String this$msg = this.getMsg();
        String other$msg = other.getMsg();
        if (this$msg == null ? other$msg != null : !this$msg.equals(other$msg)) {
            return false;
        }
        Object this$data = this.getData();
        Object other$data = other.getData();
        return !(this$data == null ? other$data != null : !this$data.equals(other$data));
    }

    protected boolean canEqual(Object other) {
        return other instanceof AjaxResult;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $code = this.getCode();
        result = result * 59 + ($code == null ? 43 : ((Object)$code).hashCode());
        Integer $show = this.getShow();
        result = result * 59 + ($show == null ? 43 : ((Object)$show).hashCode());
        String $msg = this.getMsg();
        result = result * 59 + ($msg == null ? 43 : $msg.hashCode());
        T $data = this.getData();
        result = result * 59 + ($data == null ? 43 : $data.hashCode());
        return result;
    }

    public String toString() {
        return "AjaxResult(code=" + this.getCode() + ", msg=" + this.getMsg() + ", data=" + this.getData() + ", show=" + this.getShow() + ")";
    }
}

