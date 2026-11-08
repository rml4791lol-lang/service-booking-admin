/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.validation.Constraint
 *  javax.validation.Payload
 */
package com.mdd.common.validator.annotation;

import com.mdd.common.validator.IntArrayEmptyValidator;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy={IntArrayEmptyValidator.class})
@Target(value={ElementType.PARAMETER, ElementType.FIELD})
@Retention(value=RetentionPolicy.RUNTIME)
public @interface IntArrayEmpty {
    public String message() default "\u6570\u7ec4\u4e0d\u5141\u8bb8\u4e3a\u7a7a";

    public Class<?>[] groups() default {};

    public Class<? extends Payload>[] payload() default {};
}

