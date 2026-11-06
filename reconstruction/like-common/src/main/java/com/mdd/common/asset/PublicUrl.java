package com.mdd.common.asset;

import com.mdd.common.config.GlobalConfig;
import com.mdd.common.util.ConfigUtils;
import com.mdd.common.util.RequestUtils;
import com.mdd.common.util.StringUtils;
import com.mdd.common.util.YmlUtils;

import java.util.Map;

/**
 * 对外 URL 的唯一入口（#122 P3）：把「当前 base 从哪来」收在这一处。
 *
 * <p>{@link AssetRef} 负责换算（纯函数），本类负责取配置与请求上下文，
 * 因此改域只改配置：{@code like.url}（部署模板里的 {@code PUBLIC_BASE_URL}）
 * 或存储引擎域名 {@code la_config.storage.<engine>.domain}。</p>
 *
 * <p>既有公开 API {@code UrlUtils} 全部委托到这里，200+ 调用点零改动。</p>
 */
public final class PublicUrl {

    private PublicUrl() {
    }

    /** 存储引擎：{@code la_config} 的 {@code storage.default}，缺失或空串都按 {@code local}。 */
    private static String storageEngine() {
        String engine = ConfigUtils.get("storage", "default", "local");
        if (engine == null || engine.isEmpty()) {
            return "local";
        }
        return engine;
    }

    /**
     * 解析当前生效的 base：存储引擎 + 对外基址 + 请求 origin + 上传前缀。
     */
    public static AssetBase base() {
        String engine = storageEngine();
        String storageDomain = "";
        if (!"local".equals(engine)) {
            Map<String, String> config = ConfigUtils.getMap("storage", engine);
            if (config != null) {
                String domain = config.get("domain");
                storageDomain = domain == null ? "" : domain;
            }
        }
        return new AssetBase(engine,
                publicBase(),
                requestBase(),
                storageDomain,
                GlobalConfig.publicPrefix,
                GlobalConfig.adminPublicPrefix);
    }

    /**
     * 配置的对外基址：{@code like.url} 有值就用它，否则回落当前请求 origin。
     *
     * <p>与旧实现一致：这里只判断 {@code null}，配置成空串时返回空串
     * （部署模板必须给 {@code PUBLIC_BASE_URL} 赋值，否则出网地址会缺少域名）。</p>
     */
    public static String publicBase() {
        String configured = YmlUtils.get("like.url");
        if (StringUtils.isNull(configured)) {
            return requestBase();
        }
        return configured;
    }

    /** 当前请求的 origin（无部署前缀）；非请求线程返回空串。 */
    public static String requestBase() {
        String uri = RequestUtils.uri();
        return uri == null ? "" : uri;
    }

    /** 小程序端出网地址。 */
    public static String toAbsoluteUrl(String url) {
        if (AssetRef.isPassThrough(url)) {
            // 旧实现的快路径：空值/绝对地址直接返回，不读配置（配置读取是 Redis 往返）
            return url == null ? "" : url;
        }
        return AssetRef.render(url, base());
    }

    /** 后台端出网地址。 */
    public static String toAdminAbsoluteUrl(String url) {
        if (AssetRef.isPassThrough(url)) {
            return url == null ? "" : url;
        }
        return AssetRef.renderAdmin(url, base());
    }

    /** 入库值（相对）。 */
    public static String toRelativeUrl(String url) {
        return AssetRef.store(url, base());
    }

    /**
     * 资源域名（带尾斜杠）：本地存储用请求 origin，对象存储用其配置域名。
     *
     * <p>与旧 {@code UrlUtils.domain()} 一致——**不**跟随 {@code like.url}，
     * 因为它的消费点是前端 {@code oss_domain} 展示，语义是「上传后的文件由谁提供」。
     * 存储配置缺失时返回空串（与旧实现一致）。</p>
     */
    public static String domain() {
        String engine = storageEngine();
        if ("local".equals(engine)) {
            return requestBase() + "/";
        }
        Map<String, String> config = ConfigUtils.getMap("storage", engine);
        if (config != null) {
            String domain = config.get("domain");
            return (domain == null ? "" : domain) + "/";
        }
        return "";
    }
}
