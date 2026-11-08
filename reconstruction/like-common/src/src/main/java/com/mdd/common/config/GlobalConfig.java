/*
 * Decompiled with CFR 0.152.
 */
package com.mdd.common.config;

public class GlobalConfig {
    /**
     * 调试开关：默认关闭，生产必须保持 false。
     * 由 {@link GlobalConfigBinder} 从 like.debug 配置读取，本地开发可显式设为 true。
     * 注意：异常响应始终只返回通用文案，不会因为该开关而泄露内部信息。
     */
    public static Boolean debug = false;
    /** 日志脱敏开关（like.log.*）：默认开启，生产必须保持开启。 */
    public static Boolean maskOpenid = true;
    public static Boolean maskMobile = true;
    public static Boolean maskAmount = true;
    public static Boolean isAddressEnabled = false;
    public static String version = "v1.9.1";
    public static String secret = "xK9mQ7vR3bT5wL2nP8jF4hD6gA1sY0eZ";
    public static String tablePrefix = "la_";
    public static String redisPrefix = "like:";
    public static String publicPrefix = "api/uploads";
    public static String adminPublicPrefix = "adminapi/uploads";
    public static Integer uploadImageSize = 0xA00000;
    public static Integer uploadVideoSize = 0x1E00000;
    public static String[] uploadImageExt = new String[]{"png", "jpg", "jpeg", "gif", "ico", "bmp"};
    public static String[] uploadVideoExt = new String[]{"mp4", "mp3", "avi", "flv", "rmvb", "mov"};
}

