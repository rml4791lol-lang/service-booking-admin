/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.toolkit.Assert
 */
package com.mdd.common.util;

import com.baomidou.mybatisplus.core.toolkit.Assert;
import java.math.BigDecimal;

public class AmountUtil {
    private AmountUtil() {
    }

    public static int yuan2Fen(String yuan) {
        int value;
        try {
            BigDecimal var1 = new BigDecimal(yuan);
            BigDecimal var2 = new BigDecimal(100);
            BigDecimal var3 = var1.multiply(var2);
            value = Integer.parseInt(var3.stripTrailingZeros().toPlainString());
        }
        catch (Exception e) {
            throw new IllegalArgumentException(String.format("\u975e\u6cd5\u91d1\u989d[%s]", yuan));
        }
        Assert.isTrue((value >= 0 ? 1 : 0) != 0, (String)String.format("\u975e\u6cd5\u91d1\u989d[%s]", yuan), (Object[])new Object[0]);
        return value;
    }

    public static String fen2Yuan(int fen) {
        BigDecimal var1 = new BigDecimal(fen);
        BigDecimal var2 = new BigDecimal(100);
        BigDecimal var3 = var1.divide(var2, 3);
        return var3.stripTrailingZeros().toPlainString();
    }

    public static BigDecimal fen2YuanDecimal(int fen) {
        BigDecimal var1 = new BigDecimal(fen);
        BigDecimal var2 = new BigDecimal(100);
        return var1.divide(var2, 3);
    }
}

