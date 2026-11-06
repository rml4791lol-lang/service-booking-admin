package com.mdd.common.util;

import com.mdd.common.asset.AssetRef;
import com.mdd.common.asset.PublicUrl;

/**
 * 对外资源地址工具（历史公开 API）。
 *
 * <p>自 #122 P3 起，所有换算都委托给 {@link PublicUrl} / {@link AssetRef}：
 * 「base 从哪来」在 {@code PublicUrl}，「相对↔绝对怎么换算」在 {@code AssetRef}。
 * 本类只保留既有签名，保证 200+ 调用点零改动。</p>
 *
 * <p>出网结果与改造前一致（含历史怪癖，例如后台端 {@code /api/static/} 用请求 origin
 * 而不是配置基址——见 {@link AssetRef#renderAdmin} 注释）。唯一有意的行为变化是
 * {@link #toRelativeUrl(String)}：它现在按上传路径前缀识别「自家资源」，因此老域名
 * 绝对地址也会被归一成相对 key，而旧实现会拼出 {@code https://oldhostimage/x.png} 这类脏值。</p>
 */
public class UrlUtils {

    public static String toAbsoluteUrl(String url) {
        return PublicUrl.toAbsoluteUrl(url);
    }

    public static String toAdminAbsoluteUrl(String url) {
        return PublicUrl.toAdminAbsoluteUrl(url);
    }

    public static String toRelativeUrl(String url) {
        return PublicUrl.toRelativeUrl(url);
    }

    public static String domain() {
        return PublicUrl.domain();
    }

    public static String localDomain(String url) {
        return PublicUrl.requestBase() + url;
    }

    /** 配置的对外基址（{@code like.url}），未配置时回落请求 origin。 */
    public static String getRequestUrl() {
        return PublicUrl.publicBase();
    }
}
