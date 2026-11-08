/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.validation.Constraint
 *  javax.validation.Payload
 */
package com.mdd.common.validator.annotation;

import com.mdd.common.validator.IDMustValidator;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy={IDMustValidator.class})
@Target(value={ElementType.PARAMETER, ElementType.FIELD})
@Retention(value=RetentionPolicy.RUNTIME)
public @interface IDMust {
    public String message() default "id\u53c2\u6570\u5fc5\u987b\u5b58\u5728\u4e14\u5927\u4e8e0";

    public Class<?>[] groups() default {};

    public Class<? extends Payload>[] payload() default {};
}

