/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.validation.ConstraintValidator
 *  javax.validation.ConstraintValidatorContext
 */
package com.mdd.common.validator;

import com.mdd.common.validator.annotation.IntArrayEmpty;
import java.lang.annotation.Annotation;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IntArrayEmptyValidator
implements ConstraintValidator<IntArrayEmpty, int[]> {
    public void initialize(IntArrayEmpty constraintAnnotation) {
    }

    public boolean isValid(int[] value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        return value.length > 0;
    }
}

