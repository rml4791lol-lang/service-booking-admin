/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.validation.ConstraintValidator
 *  javax.validation.ConstraintValidatorContext
 */
package com.mdd.common.validator;

import com.mdd.common.validator.annotation.IntegerContains;
import java.util.HashSet;
import java.util.Set;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IntegerContainsValidator
implements ConstraintValidator<IntegerContains, Integer> {
    private Set<Integer> limitValues;

    public void initialize(IntegerContains integerContains) {
        HashSet<Integer> set = new HashSet<Integer>();
        for (int i : integerContains.values()) {
            set.add(i);
        }
        this.limitValues = set;
    }

    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return this.limitValues.contains(value);
    }
}

