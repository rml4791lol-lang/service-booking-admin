/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.toolkit.StringUtils
 *  javax.validation.ConstraintValidator
 *  javax.validation.ConstraintValidatorContext
 */
package com.mdd.common.validator;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.mdd.common.validator.annotation.StringContains;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class StringContainsValidator
implements ConstraintValidator<StringContains, String> {
    private Set<String> limitValues;

    public void initialize(StringContains stringContains) {
        this.limitValues = Arrays.stream(stringContains.values()).collect(Collectors.toSet());
    }

    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StringUtils.isBlank((CharSequence)value)) {
            return true;
        }
        return this.limitValues.contains(value.trim());
    }
}

