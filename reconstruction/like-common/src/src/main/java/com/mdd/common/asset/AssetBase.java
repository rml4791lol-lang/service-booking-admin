package com.mdd.common.asset;

/**
 * 对外资源地址的「当前 base」（#122 P3）。
 *
 * <p>这是 {@link AssetRef} 唯一的输入依赖：把「域名/前缀从哪来」与「相对↔绝对怎么换算」
 * 两件事分开，换算逻辑就能脱离 Spring / 请求上下文被单测直接驱动。</p>
 *
 * <p>四个字段的来源与语义：</p>
 * <ul>
 *   <li>{@code publicBase}：配置的对外基址（{@code like.url}），未配置时回落请求 Host。
 *       可能带部署前缀，例如 {@code https://an520.xin/lxt-v2}。渲染本地存储的图片、
 *       {@code /api/static/} 资源都用它。</li>
 *   <li>{@code requestBase}：仅由当前请求推导的 origin（无部署前缀）。历史上
 *       {@code UrlUtils.toAdminAbsoluteUrl} 对 {@code /api/static/} 用的是它而不是
 *       {@code publicBase}，本类**原样保留**这一处不一致（见 {@link AssetRef#renderAdmin}），
 *       避免本票顺手改掉既有出网地址。</li>
 *   <li>{@code storageDomain}：对象存储引擎（qiniu/aliyun/qcloud）的访问域名；
 *       本地存储时为空串。</li>
 *   <li>{@code publicPrefix} / {@code adminPublicPrefix}：上传目录的对外路径前缀，
 *       分别对应小程序端与后台端（两者映射同一个磁盘目录）。</li>
 * </ul>
 *
 * <p>不可变；所有字符串入参在构造期归一（去尾部斜杠），空值一律存空串而不是 null。</p>
 */
public final class AssetBase {

    private final String engine;
    private final String publicBase;
    private final String requestBase;
    private final String storageDomain;
    private final String publicPrefix;
    private final String adminPublicPrefix;

    public AssetBase(String engine,
                     String publicBase,
                     String requestBase,
                     String storageDomain,
                     String publicPrefix,
                     String adminPublicPrefix) {
        this.engine = normalize(engine).isEmpty() ? "local" : normalize(engine);
        this.publicBase = trimTrailingSlash(publicBase);
        this.requestBase = trimTrailingSlash(requestBase);
        this.storageDomain = trimTrailingSlash(storageDomain);
        this.publicPrefix = trimSlashes(publicPrefix);
        this.adminPublicPrefix = trimSlashes(adminPublicPrefix);
    }

    /** 本地存储：资源由本服务的上传目录直接提供。 */
    public boolean isLocal() {
        return "local".equals(engine);
    }

    public String engine() {
        return engine;
    }

    /** 配置的对外基址（可能带部署前缀，如 {@code https://host/lxt-v2}）；未知时为空串。 */
    public String publicBase() {
        return publicBase;
    }

    /** 仅由请求推导的 origin（无部署前缀）；非请求线程下为空串。 */
    public String requestBase() {
        return requestBase;
    }

    /** 对象存储访问域名；本地存储时为空串。 */
    public String storageDomain() {
        return storageDomain;
    }

    /** 小程序端上传路径前缀，默认 {@code api/uploads}。 */
    public String publicPrefix() {
        return publicPrefix;
    }

    /** 后台端上传路径前缀，默认 {@code adminapi/uploads}。 */
    public String adminPublicPrefix() {
        return adminPublicPrefix;
    }

    private static String normalize(String value) {
        return value == null ? "" : value;
    }

    private static String trimTrailingSlash(String value) {
        String out = normalize(value);
        while (out.endsWith("/")) {
            out = out.substring(0, out.length() - 1);
        }
        return out;
    }

    private static String trimSlashes(String value) {
        String out = normalize(value);
        while (out.startsWith("/")) {
            out = out.substring(1);
        }
        while (out.endsWith("/")) {
            out = out.substring(0, out.length() - 1);
        }
        return out;
    }
}
