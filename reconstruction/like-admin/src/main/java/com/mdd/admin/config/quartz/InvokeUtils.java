/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.config.quartz.InvokeUtils
 *  com.mdd.common.entity.DevCrontab
 *  com.mdd.common.util.SpringUtils
 *  com.mdd.common.util.StringUtils
 */
package com.mdd.admin.config.quartz;

import com.mdd.common.entity.DevCrontab;
import com.mdd.common.util.SpringUtils;
import com.mdd.common.util.StringUtils;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

/*
 * Exception performing whole class analysis ignored.
 */
public class InvokeUtils {
    public static void invokeMethod(DevCrontab crontab) throws Exception {
        String invokeTarget = crontab.getCommand();
        String beanName = InvokeUtils.getBeanName((String)invokeTarget);
        String methodName = InvokeUtils.getMethodName((String)invokeTarget);
        List methodParams = InvokeUtils.getMethodParams((String)invokeTarget);
        if (!InvokeUtils.isValidClassName((String)beanName)) {
            Object bean = SpringUtils.getBean((String)beanName);
            InvokeUtils.invokeMethod((Object)bean, (String)methodName, (List)methodParams);
        } else {
            Object bean = Class.forName(beanName).newInstance();
            InvokeUtils.invokeMethod(bean, (String)methodName, (List)methodParams);
        }
    }

    private static void invokeMethod(Object bean, String methodName, List<Object[]> methodParams) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (StringUtils.isNotNull(methodParams) && methodParams.size() > 0) {
            Method method = bean.getClass().getMethod(methodName, InvokeUtils.getMethodParamsType(methodParams));
            method.invoke(bean, InvokeUtils.getMethodParamsValue(methodParams));
        } else {
            Method method = bean.getClass().getMethod(methodName, new Class[0]);
            method.invoke(bean, new Object[0]);
        }
    }

    public static boolean isValidClassName(String invokeTarget) {
        return StringUtils.countMatches((CharSequence)invokeTarget, (CharSequence)".") > 1;
    }

    public static String getBeanName(String invokeTarget) {
        String beanName = StringUtils.substringBefore((String)invokeTarget, (String)"(");
        return StringUtils.substringBeforeLast((String)beanName, (String)".");
    }

    public static String getMethodName(String invokeTarget) {
        String methodName = StringUtils.substringBefore((String)invokeTarget, (String)"(");
        return StringUtils.substringAfterLast((String)methodName, (String)".");
    }

    public static List<Object[]> getMethodParams(String invokeTarget) {
        String methodStr = StringUtils.substringBetween((String)invokeTarget, (String)"(", (String)")");
        if (StringUtils.isEmpty((String)methodStr)) {
            return null;
        }
        String[] methodParams = methodStr.split(",(?=([^\"']*[\"'][^\"']*[\"'])*[^\"']*$)");
        LinkedList<Object[]> clazz = new LinkedList<Object[]>();
        for (String methodParam : methodParams) {
            String str = StringUtils.trimToEmpty((String)methodParam);
            if (StringUtils.startsWithAny((CharSequence)str, (CharSequence[])new CharSequence[]{"'", "\""})) {
                clazz.add(new Object[]{StringUtils.substring((String)str, (int)1, (int)(str.length() - 1)), String.class});
                continue;
            }
            if ("true".equalsIgnoreCase(str) || "false".equalsIgnoreCase(str)) {
                clazz.add(new Object[]{Boolean.valueOf(str), Boolean.class});
                continue;
            }
            if (StringUtils.endsWith((CharSequence)str, (CharSequence)"L")) {
                clazz.add(new Object[]{Long.valueOf(StringUtils.substring((String)str, (int)0, (int)(str.length() - 1))), Long.class});
                continue;
            }
            if (StringUtils.endsWith((CharSequence)str, (CharSequence)"D")) {
                clazz.add(new Object[]{Double.valueOf(StringUtils.substring((String)str, (int)0, (int)(str.length() - 1))), Double.class});
                continue;
            }
            clazz.add(new Object[]{Integer.valueOf(str), Integer.class});
        }
        return clazz;
    }

    public static Class<?>[] getMethodParamsType(List<Object[]> methodParams) {
        Class[] clazz = new Class[methodParams.size()];
        int index = 0;
        for (Object[] os : methodParams) {
            clazz[index] = (Class)os[1];
            ++index;
        }
        return clazz;
    }

    public static Object[] getMethodParamsValue(List<Object[]> methodParams) {
        Object[] clazz = new Object[methodParams.size()];
        int index = 0;
        for (Object[] os : methodParams) {
            clazz[index] = os[0];
            ++index;
        }
        return clazz;
    }
}

