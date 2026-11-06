package com.mdd.common.asset;

/**
 * 对外资源地址引用（#122 P3，架构笔记「候选 3」）。
 *
 * <p>把「资源地址」当成一个值对象处理，而不是一堆字符串拼接：</p>
 * <ul>
 *   <li>{@link #parse(String, AssetBase)} —— 识别形态（空/外部绝对/静态/后台路径/相对 key）；</li>
 *   <li>{@link #render(AssetBase)} —— 出网绝对地址（小程序端）；</li>
 *   <li>{@link #renderAdmin(AssetBase)} —— 出网绝对地址（后台端）；</li>
 *   <li>{@link #store(AssetBase)} —— 入库值（永远相对，除非本来就是外部绝对地址）。</li>
 * </ul>
 *
 * <p>核心性质（契约测试断言）：对任何「已归一」的存储值 {@code x}，
 * {@code store(render(x)) == x}；对未归一的输入，{@code store} 是一次幂等归一
 * （{@code store(render(store(render(x)))) == store(render(x))}）。</p>
 *
 * <p>本票只做「委托」：{@code UrlUtils} 的公开签名与出网结果保持不变，
 * 内部换算全部落到本类，改域从此只需改一处配置。</p>
 *
 * <p>纯函数、无 Spring 依赖：base 由 {@link PublicUrl} 从配置/请求解析后显式传入，
 * 因此换算规则可以在没有容器、没有 HTTP 请求的情况下被直接验证。</p>
 */
public final class AssetRef {

    /** 资源地址形态。 */
    public enum Kind {
        /** 空值。 */
        EMPTY,
        /** 外部绝对地址（第三方域名 / 外链），原样保留，绝不改写。 */
        ABSOLUTE,
        /** {@code /api/static/...}：随程序发布的静态资源。 */
        STATIC,
        /** {@code /adminapi/static/...}：后台静态资源。 */
        ADMIN_STATIC,
        /** {@code /adminapi/...}（非 static）：后台路径，小程序端出网时映射成 {@code /api/...}。 */
        ADMIN_PATH,
        /** 其他以 {@code /} 开头的路径。 */
        PATH,
        /** 相对存储 key（如 {@code image/20260101/x.png}）：唯一允许入库的图片形态。 */
        STORED
    }

    private final Kind kind;
    /** STORED: 相对 key；STATIC/ADMIN_STATIC/ADMIN_PATH/PATH: 以 / 开头的路径；ABSOLUTE: 完整原值。 */
    private final String key;
    /** 仅 ABSOLUTE：{@code scheme://host[:port]}。 */
    private final String origin;
    /** 仅 ABSOLUTE：origin 之后的路径部分（可能为空串）。 */
    private final String path;

    private AssetRef(Kind kind, String key, String origin, String path) {
        this.kind = kind;
        this.key = key;
        this.origin = origin;
        this.path = path;
    }

    public static AssetRef empty() {
        return new AssetRef(Kind.EMPTY, "", "", "");
    }

    /**
     * 识别一个地址的形态。不抛异常、不做 trim（与旧 {@code UrlUtils} 一致：调用方自己决定是否 trim）。
     */
    public static AssetRef parse(String raw, AssetBase base) {
        if (raw == null || raw.isEmpty()) {
            return empty();
        }
        if (isAbsolute(raw)) {
            return parseAbsolute(raw);
        }
        if (raw.charAt(0) == '/') {
            String publicPrefix = base.publicPrefix();
            String adminPrefix = base.adminPublicPrefix();
            if (!publicPrefix.isEmpty() && raw.startsWith("/" + publicPrefix + "/")) {
                return stored(raw.substring(publicPrefix.length() + 2));
            }
            if (!adminPrefix.isEmpty() && raw.startsWith("/" + adminPrefix + "/")) {
                return stored(raw.substring(adminPrefix.length() + 2));
            }
            if (raw.startsWith("/api/static/")) {
                return new AssetRef(Kind.STATIC, raw, "", "");
            }
            if (raw.startsWith("/adminapi/static/")) {
                return new AssetRef(Kind.ADMIN_STATIC, raw, "", "");
            }
            if (raw.startsWith("/adminapi/")) {
                return new AssetRef(Kind.ADMIN_PATH, raw, "", "");
            }
            return new AssetRef(Kind.PATH, raw, "", "");
        }
        return stored(raw);
    }

    private static AssetRef stored(String key) {
        return new AssetRef(Kind.STORED, key, "", "");
    }

    private static AssetRef parseAbsolute(String raw) {
        int scheme = raw.indexOf("://");
        int slash = raw.indexOf('/', scheme + 3);
        if (slash < 0) {
            return new AssetRef(Kind.ABSOLUTE, raw, raw, "");
        }
        return new AssetRef(Kind.ABSOLUTE, raw, raw.substring(0, slash), raw.substring(slash));
    }

    private static boolean isAbsolute(String raw) {
        return raw.regionMatches(true, 0, "http://", 0, 7)
                || raw.regionMatches(true, 0, "https://", 0, 8);
    }

    /**
     * 无需换算的输入：空值或已经是绝对地址——出网渲染原样返回。
     *
     * <p>保留旧实现的快路径：{@code UrlUtils.toAbsoluteUrl} 遇到绝对地址直接返回、
     * **不读配置**（配置读取是 Redis 往返）。调用方据此可以避免在热路径上解析 base。</p>
     */
    public static boolean isPassThrough(String raw) {
        return raw == null || raw.isEmpty() || isAbsolute(raw);
    }

    public Kind kind() {
        return kind;
    }

    public String key() {
        return key;
    }

    public String origin() {
        return origin;
    }

    /** 小程序端出网地址。 */
    public String render(AssetBase base) {
        switch (kind) {
            case EMPTY:
                return "";
            case ABSOLUTE:
                return key;
            case STATIC:
                return base.publicBase() + key;
            case ADMIN_STATIC:
            case ADMIN_PATH:
                // 后台路径在小程序端映射成 /api/...（既有行为）：
                // /adminapi/static/x 与 /api/static/x 是同一资源的两个别名，小程序端都出 /api/static/x
                return base.publicBase() + key.replace("/adminapi/", "/api/");
            case PATH:
                return base.publicBase() + "/" + base.publicPrefix() + key;
            case STORED:
            default:
                return renderStored(base, base.publicPrefix());
        }
    }

    /**
     * 后台端出网地址。
     *
     * <p>注意：{@code /api/static/} 在后台端历史上用「仅由请求推导的 origin」
     * （{@code RequestUtils.uri()}）而不是配置基址，本票原样保留，避免顺手改掉既有出网地址
     * （见 {@link AssetBase} 注释与 #122 残留风险）。</p>
     */
    public String renderAdmin(AssetBase base) {
        switch (kind) {
            case EMPTY:
                return "";
            case ABSOLUTE:
                return key;
            case STATIC:
                return base.requestBase() + key.replace("/api/static/", "/adminapi/static/");
            case ADMIN_STATIC:
            case ADMIN_PATH:
                return base.publicBase() + key;
            case PATH:
                return base.publicBase() + "/" + base.adminPublicPrefix() + key;
            case STORED:
            default:
                return renderStored(base, base.adminPublicPrefix());
        }
    }

    private String renderStored(AssetBase base, String prefix) {
        if (!base.isLocal()) {
            // 对象存储：key 即对象路径，直接拼存储域名（既有行为，不分前后台）
            return base.storageDomain() + "/" + key;
        }
        return base.publicBase() + "/" + prefix + "/" + key;
    }

    /**
     * 入库值：永远相对（除非本来就是外部绝对地址）。
     *
     * <p>与旧 {@code UrlUtils.toRelativeUrl} 的关键差别（本票的修复点）：旧实现只剥离
     * 「当前 base」，遇到别的域名（老域名、并存域名）会拼出
     * {@code https://oldhostimage/x.png} 这类脏值；这里按上传路径前缀识别「这是我们的资源」，
     * 与域名无关，因此换域不再需要逐表手工 SQL。</p>
     */
    public String store(AssetBase base) {
        switch (kind) {
            case EMPTY:
                return "";
            case ABSOLUTE:
                return storeAbsolute(base);
            case STATIC:
            case ADMIN_STATIC:
            case ADMIN_PATH:
            case PATH:
            case STORED:
            default:
                return key;
        }
    }

    private String storeAbsolute(AssetBase base) {
        String publicPrefix = base.publicPrefix();
        String adminPrefix = base.adminPublicPrefix();
        int publicIndex = publicPrefix.isEmpty() ? -1 : path.indexOf("/" + publicPrefix + "/");
        if (publicIndex >= 0) {
            return path.substring(publicIndex + publicPrefix.length() + 2);
        }
        int adminIndex = adminPrefix.isEmpty() ? -1 : path.indexOf("/" + adminPrefix + "/");
        if (adminIndex >= 0) {
            return path.substring(adminIndex + adminPrefix.length() + 2);
        }
        if (!base.storageDomain().isEmpty() && key.startsWith(base.storageDomain())) {
            String rest = key.substring(base.storageDomain().length());
            return rest.startsWith("/") ? rest.substring(1) : rest;
        }
        String baseOrigin = originOf(base.publicBase());
        if (!baseOrigin.isEmpty() && baseOrigin.equalsIgnoreCase(origin)) {
            return relativeOfBasePath(base, path);
        }
        String requestOrigin = originOf(base.requestBase());
        if (!requestOrigin.isEmpty() && requestOrigin.equalsIgnoreCase(origin)) {
            return relativeOfBasePath(base, path);
        }
        // 外部绝对地址：原样保留（不猜、不改写）
        return key;
    }

    /** 去掉 base 自带的部署前缀（如 {@code /lxt-v2}），再把路径归类。 */
    private String relativeOfBasePath(AssetBase base, String path) {
        String basePath = pathOf(base.publicBase());
        String trimmed = path;
        if (!basePath.isEmpty() && path.startsWith(basePath + "/")) {
            trimmed = path.substring(basePath.length());
        }
        if (trimmed.startsWith("/api/static/")) {
            return trimmed;
        }
        if (trimmed.startsWith("/adminapi/static/")) {
            return trimmed;
        }
        return trimmed;
    }

    private static String originOf(String url) {
        if (url == null || !isAbsolute(url)) {
            return "";
        }
        int scheme = url.indexOf("://");
        int slash = url.indexOf('/', scheme + 3);
        return slash < 0 ? url : url.substring(0, slash);
    }

    private static String pathOf(String url) {
        if (url == null || !isAbsolute(url)) {
            return "";
        }
        int scheme = url.indexOf("://");
        int slash = url.indexOf('/', scheme + 3);
        return slash < 0 ? "" : url.substring(slash);
    }

    /** 便捷方法：{@code parse(raw, base).render(base)}。 */
    public static String render(String raw, AssetBase base) {
        return parse(raw, base).render(base);
    }

    /** 便捷方法：{@code parse(raw, base).renderAdmin(base)}。 */
    public static String renderAdmin(String raw, AssetBase base) {
        return parse(raw, base).renderAdmin(base);
    }

    /** 便捷方法：{@code parse(raw, base).store(base)}。 */
    public static String store(String raw, AssetBase base) {
        return parse(raw, base).store(base);
    }

    @Override
    public String toString() {
        return "AssetRef{" + kind + ", key='" + key + "'}";
    }
}
