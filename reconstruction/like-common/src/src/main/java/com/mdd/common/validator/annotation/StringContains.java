/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.validation.Constraint
 *  javax.validation.Payload
 */
package com.mdd.common.validator.annotation;

import com.mdd.common.validator.StringContainsValidator;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy={StringContainsValidator.class})
@Target(value={ElementType.PARAMETER, ElementType.FIELD})
@Retention(value=RetentionPolicy.RUNTIME)
public @interface StringContains {
    public String message() default "\u5b57\u7b26\u4e32\u4e0d\u7b26\u5408\u89c4\u5219";

    public String[] values() default {};

    public Class<?>[] groups() default {};

    public Class<? extends Payload>[] payload() default {};
}

