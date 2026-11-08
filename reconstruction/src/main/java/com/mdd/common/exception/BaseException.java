/*
 * Decompiled with CFR 0.152.
 */
package com.mdd.common.exception;

public class BaseException
extends RuntimeException {
    private Integer code;
    private String msg;
    private Integer show;

    public BaseException(Integer code, String msg, Integer show) {
        this.code = code;
        this.msg = msg;
        this.show = show;
    }

    public Integer getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
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

    public void setShow(Integer show) {
        this.show = show;
    }

    @Override
    public String toString() {
        return "BaseException(code=" + this.getCode() + ", msg=" + this.getMsg() + ", show=" + this.getShow() + ")";
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof BaseException)) {
            return false;
        }
        BaseException other = (BaseException)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (!super.equals(o)) {
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
        return !(this$msg == null ? other$msg != null : !this$msg.equals(other$msg));
    }

    protected boolean canEqual(Object other) {
        return other instanceof BaseException;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = super.hashCode();
        Integer $code = this.getCode();
        result = result * 59 + ($code == null ? 43 : ((Object)$code).hashCode());
        Integer $show = this.getShow();
        result = result * 59 + ($show == null ? 43 : ((Object)$show).hashCode());
        String $msg = this.getMsg();
        result = result * 59 + ($msg == null ? 43 : $msg.hashCode());
        return result;
    }
}

