/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson2.JSONArray
 *  com.alibaba.fastjson2.JSONReader$Feature
 *  org.apache.commons.lang3.StringUtils
 *  org.springframework.util.AntPathMatcher
 */
package com.mdd.common.util;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.util.AntPathMatcher;

public class StringUtils
extends org.apache.commons.lang3.StringUtils {
    private static final String NULL_STR = "";
    private static final char SEPARATOR = '_';

    public static <T> T nvl(T value, T defaultValue) {
        return value != null ? value : defaultValue;
    }

    public static boolean isEmpty(Collection<?> coll) {
        return StringUtils.isNull(coll) || coll.isEmpty();
    }

    public static boolean isNotEmpty(Collection<?> coll) {
        return !StringUtils.isEmpty(coll);
    }

    public static boolean isEmpty(Object[] objects) {
        return StringUtils.isNull(objects) || objects.length == 0;
    }

    public static boolean isNotEmpty(Object[] objects) {
        return !StringUtils.isEmpty(objects);
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return StringUtils.isNull(map) || map.isEmpty();
    }

    public static boolean isNotEmpty(Map<?, ?> map) {
        return !StringUtils.isEmpty(map);
    }

    public static boolean isEmpty(String str) {
        return StringUtils.isNull(str) || NULL_STR.equals(str.trim());
    }

    public static boolean isNotEmpty(String str) {
        return !StringUtils.isEmpty(str);
    }

    public static boolean isNull(Object object) {
        return object == null;
    }

    public static boolean isNotNull(Object object) {
        return !StringUtils.isNull(object);
    }

    public static boolean isArray(Object object) {
        return StringUtils.isNotNull(object) && object.getClass().isArray();
    }

    public static String trim(String str) {
        return str == null ? NULL_STR : str.trim();
    }

    public static String substring(String str, int start) {
        if (str == null) {
            return NULL_STR;
        }
        if (start < 0) {
            start = str.length() + start;
        }
        if (start < 0) {
            start = 0;
        }
        if (start > str.length()) {
            return NULL_STR;
        }
        return str.substring(start);
    }

    public static String substring(String str, int start, int end) {
        if (str == null) {
            return NULL_STR;
        }
        if (end < 0) {
            end = str.length() + end;
        }
        if (start < 0) {
            start = str.length() + start;
        }
        if (end > str.length()) {
            end = str.length();
        }
        if (start > end) {
            return NULL_STR;
        }
        if (start < 0) {
            start = 0;
        }
        if (end < 0) {
            end = 0;
        }
        return str.substring(start, end);
    }

    public static Set<String> str2Set(String str, String sep) {
        return new HashSet<String>(StringUtils.str2List(str, sep, true, false));
    }

    public static List<String> str2List(String str, String sep, boolean filterBlank, boolean trim) {
        String[] split;
        ArrayList<String> list = new ArrayList<String>();
        if (StringUtils.isEmpty(str)) {
            return list;
        }
        if (filterBlank && StringUtils.isBlank((CharSequence)str)) {
            return list;
        }
        for (String string : split = str.split(sep)) {
            if (filterBlank && StringUtils.isBlank((CharSequence)string)) continue;
            if (trim) {
                string = string.trim();
            }
            list.add(string);
        }
        return list;
    }

    public static boolean containsAnyIgnoreCase(CharSequence cs, CharSequence ... searchCharSequences) {
        if (StringUtils.isEmpty((CharSequence)cs) || StringUtils.isEmpty(searchCharSequences)) {
            return false;
        }
        for (CharSequence testStr : searchCharSequences) {
            if (!StringUtils.containsIgnoreCase((CharSequence)cs, (CharSequence)testStr)) continue;
            return true;
        }
        return false;
    }

    public static String toUnderScoreCase(String str) {
        if (str == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        boolean preCharIsUpperCase = true;
        boolean cureCharIsUpperCase = true;
        boolean nextCharIsUpperCase = true;
        for (int i = 0; i < str.length(); ++i) {
            char c = str.charAt(i);
            preCharIsUpperCase = i > 0 ? Character.isUpperCase(str.charAt(i - 1)) : false;
            cureCharIsUpperCase = Character.isUpperCase(c);
            if (i < str.length() - 1) {
                nextCharIsUpperCase = Character.isUpperCase(str.charAt(i + 1));
            }
            if (preCharIsUpperCase && cureCharIsUpperCase && !nextCharIsUpperCase) {
                sb.append('_');
            } else if (i != 0 && !preCharIsUpperCase && cureCharIsUpperCase) {
                sb.append('_');
            }
            sb.append(Character.toLowerCase(c));
        }
        return sb.toString();
    }

    public static boolean inStringIgnoreCase(String str, String ... strArr) {
        if (str != null && strArr != null) {
            for (String s : strArr) {
                if (!str.equalsIgnoreCase(StringUtils.trim(s))) continue;
                return true;
            }
        }
        return false;
    }

    public static String convertToCamelCase(String name) {
        String[] camels;
        StringBuilder result = new StringBuilder();
        if (name == null || name.isEmpty()) {
            return NULL_STR;
        }
        if (!name.contains("_")) {
            return name.substring(0, 1).toUpperCase() + name.substring(1);
        }
        for (String camel : camels = name.split("_")) {
            if (camel.isEmpty()) continue;
            result.append(camel.substring(0, 1).toUpperCase());
            result.append(camel.substring(1).toLowerCase());
        }
        return result.toString();
    }

    public static String toCamelCase(String s) {
        if (s == null) {
            return null;
        }
        s = s.toLowerCase();
        StringBuilder sb = new StringBuilder(s.length());
        boolean upperCase = false;
        for (int i = 0; i < s.length(); ++i) {
            char c = s.charAt(i);
            if (c == '_') {
                upperCase = true;
                continue;
            }
            if (upperCase) {
                sb.append(Character.toUpperCase(c));
                upperCase = false;
                continue;
            }
            sb.append(c);
        }
        return sb.toString();
    }

    public static boolean matches(String str, List<String> strArr) {
        if (StringUtils.isEmpty(str) || StringUtils.isEmpty(strArr)) {
            return false;
        }
        for (String pattern : strArr) {
            if (!StringUtils.isMatch(pattern, str)) continue;
            return true;
        }
        return false;
    }

    public static boolean isMatch(String pattern, String url) {
        AntPathMatcher matcher = new AntPathMatcher();
        return matcher.match(pattern, url);
    }

    public static String padL(Number num, int size) {
        return StringUtils.padL(num.toString(), size, '0');
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static String padL(String s, int size, char c) {
        StringBuilder sb = new StringBuilder(size);
        if (s != null) {
            int len = s.length();
            if (s.length() > size) return s.substring(len - size, len);
            for (int i = size - len; i > 0; --i) {
                sb.append(c);
            }
            sb.append(s);
            return sb.toString();
        } else {
            for (int i = size; i > 0; --i) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static String format(String strPattern, Object ... argArray) {
        String EMPTY_JSON = "{}";
        char C_BACKSLASH = '\\';
        char C_DELIM_START = '{';
        if (StringUtils.isEmpty(argArray) || StringUtils.isEmpty(strPattern)) {
            return strPattern;
        }
        int strPatternLength = strPattern.length();
        StringBuilder sbuf = new StringBuilder(strPatternLength + 50);
        int handledPosition = 0;
        for (int argIndex = 0; argIndex < argArray.length; ++argIndex) {
            int delimIndex = strPattern.indexOf(EMPTY_JSON, handledPosition);
            if (delimIndex == -1) {
                if (handledPosition == 0) {
                    return strPattern;
                }
                sbuf.append(strPattern, handledPosition, strPatternLength);
                return sbuf.toString();
            }
            if (delimIndex > 0 && strPattern.charAt(delimIndex - 1) == C_BACKSLASH) {
                if (delimIndex > 1 && strPattern.charAt(delimIndex - 2) == C_BACKSLASH) {
                    sbuf.append(strPattern, handledPosition, delimIndex - 1);
                    sbuf.append(argArray[argIndex]);
                    handledPosition = delimIndex + 2;
                    continue;
                }
                --argIndex;
                sbuf.append(strPattern, handledPosition, delimIndex - 1);
                sbuf.append(C_DELIM_START);
                handledPosition = delimIndex + 1;
                continue;
            }
            sbuf.append(strPattern, handledPosition, delimIndex);
            sbuf.append(argArray[argIndex]);
            handledPosition = delimIndex + 2;
        }
        sbuf.append(strPattern, handledPosition, strPattern.length());
        return sbuf.toString();
    }

    public static boolean isJsonArrayString(String jsonString) {
        if (StringUtils.isNull(jsonString) || StringUtils.isEmpty(jsonString)) {
            return false;
        }
        try {
            JSONArray.parseArray((String)jsonString, (JSONReader.Feature[])new JSONReader.Feature[0]);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
}

