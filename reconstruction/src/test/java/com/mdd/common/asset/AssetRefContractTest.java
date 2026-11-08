package com.mdd.common.asset;

/**
 * #122 P3 契约测试：{@code store(render(x)) == x}，以及「委托改造不改变既有出网地址」。
 *
 * <p>本仓库离线 Maven 仓库里没有 junit / spring-boot-starter-test，因此用「带 main 的
 * 纯 Java 断言器」实现：既能直接跑，也不新增任何依赖。</p>
 *
 * <p>运行：{@code bash tools/assetref/run-contract-test.sh}（mvn test-compile + java）。</p>
 *
 * <p>覆盖：</p>
 * <ol>
 *   <li>契约：规范存储值 x 满足 {@code store(render(x)) == x}（小程序端）、
 *       {@code store(renderAdmin(x)) == x}（后台端）；</li>
 *   <li>三种 base：无前缀本地 / 带部署前缀（{@code /lxt-v2}）/ 对象存储 / 无 base；</li>
 *   <li>静态资源别名 {@code /api/static/} ↔ {@code /adminapi/static/} 的收敛语义；</li>
 *   <li>改 base → 同一 key 出网地址随之变化，且都能存回同一个 key；</li>
 *   <li>旧域绝对地址归一成相对 key（{@code zzty02/adminapi/uploads}、{@code zzty03/api/uploads}）；</li>
 *   <li>与「旧 UrlUtils 逐字转写」的对照实现逐项对齐（相对地址行为不变）；</li>
 *   <li>三处**有意的行为变化**单独断言（旧输出必然 404 / 必然脏值的那三类）。</li>
 * </ol>
 */
public final class AssetRefContractTest {

    // ---- 四种 base ----------------------------------------------------------
    /** 无部署前缀的本地存储。 */
    private static final AssetBase LOCAL =
            new AssetBase("local", "https://api.example.com", "https://api.example.com", "",
                    "api/uploads", "adminapi/uploads");
    /** 带部署前缀（体验环境 an520.xin/lxt-v2 的形态）。 */
    private static final AssetBase PREFIXED =
            new AssetBase("local", "https://an520.xin/lxt-v2", "https://an520.xin", "",
                    "api/uploads", "adminapi/uploads");
    /** 对象存储（key 即对象路径）。 */
    private static final AssetBase OSS =
            new AssetBase("aliyun", "https://api.example.com", "https://api.example.com",
                    "https://cdn.example.com", "api/uploads", "adminapi/uploads");
    /** 非请求线程且未配置 like.url：只出路径，不产生 {@code null/...}。 */
    private static final AssetBase NO_BASE =
            new AssetBase("local", "", "", "", "api/uploads", "adminapi/uploads");

    private static final AssetBase[] BASES = {LOCAL, PREFIXED, OSS};

    /** 两端共用的规范存储值。 */
    private static final String[] SHARED_KEYS = {
            "image/20260101/abc.png",
            "image/20260101/9f8e7d6c.jpeg",
            "image/x.png",
            "https://beian.miit.gov.cn/",
            "https://cdn.thirdparty.com/assets/a.png"
    };

    /** 小程序端的规范存储值：静态资源以 {@code /api/static/} 为正名。 */
    private static final String[] FRONT_KEYS = concat(SHARED_KEYS, new String[]{
            "/api/static/default_avatar.png",
            "/api/static/logo.png"
    });

    /** 后台端的规范存储值：静态资源以 {@code /adminapi/static/} 为正名。 */
    private static final String[] ADMIN_KEYS = concat(SHARED_KEYS, new String[]{
            "/adminapi/static/backend_avatar.png",
            "/adminapi/static/logo.png"
    });

    private static int passed;
    private static int failed;

    public static void main(String[] args) {
        contractFrontStoreRenderIdentity();
        contractAdminStoreRenderIdentity();
        staticAliasCollapses();
        baseChangeFollows();
        legacyAbsoluteNormalized();
        prefixSpecialCasesPreserved();
        parityFrontAndStore();
        parityAdmin();
        intendedDifferences();
        nonCanonicalConvergesIdempotently();
        edgeCases();

        System.out.println();
        System.out.println("通过 " + passed + " 项，失败 " + failed + " 项");
        if (failed > 0) {
            System.exit(1);
        }
        System.out.println("契约测试通过（#122 P3 AssetRef）");
    }

    // ---- 1) 小程序端契约 ----------------------------------------------------
    private static void contractFrontStoreRenderIdentity() {
        section("契约 store(render(x)) == x（小程序端）");
        for (AssetBase base : BASES) {
            for (String key : FRONT_KEYS) {
                String rendered = AssetRef.render(key, base);
                eq("store(render(" + key + ")) @ " + label(base), key, AssetRef.store(rendered, base));
            }
        }
        for (String key : FRONT_KEYS) {
            eq("store(render(" + key + ")) @ no-base", key,
                    AssetRef.store(AssetRef.render(key, NO_BASE), NO_BASE));
        }
    }

    // ---- 2) 后台端契约 ------------------------------------------------------
    private static void contractAdminStoreRenderIdentity() {
        section("契约 store(renderAdmin(x)) == x（后台端）");
        for (AssetBase base : BASES) {
            for (String key : ADMIN_KEYS) {
                String rendered = AssetRef.renderAdmin(key, base);
                eq("store(renderAdmin(" + key + ")) @ " + label(base), key,
                        AssetRef.store(rendered, base));
            }
        }
    }

    // ---- 3) 静态资源别名 ----------------------------------------------------
    private static void staticAliasCollapses() {
        section("静态资源别名：/adminapi/static/ ↔ /api/static/ 指向同一资源");
        // 小程序端：两个别名都出 /api/static/（既有行为）
        eq("front(/api/static/x)", "https://api.example.com/api/static/x.png",
                AssetRef.render("/api/static/x.png", LOCAL));
        eq("front(/adminapi/static/x)", "https://api.example.com/api/static/x.png",
                AssetRef.render("/adminapi/static/x.png", LOCAL));
        // 存回时收敛到 /api/static/ 正名
        eq("front 存回收敛", "/api/static/x.png",
                AssetRef.store(AssetRef.render("/adminapi/static/x.png", LOCAL), LOCAL));
        // 后台端：两个别名都出 /adminapi/static/
        eq("admin(/api/static/x)", "https://api.example.com/adminapi/static/x.png",
                AssetRef.renderAdmin("/api/static/x.png", LOCAL));
        eq("admin(/adminapi/static/x)", "https://api.example.com/adminapi/static/x.png",
                AssetRef.renderAdmin("/adminapi/static/x.png", LOCAL));
        eq("admin 存回收敛", "/adminapi/static/x.png",
                AssetRef.store(AssetRef.renderAdmin("/api/static/x.png", LOCAL), LOCAL));
        // 两个别名本身都是 store 的不动点
        eq("/api/static 不动点", "/api/static/x.png", AssetRef.store("/api/static/x.png", LOCAL));
        eq("/adminapi/static 不动点", "/adminapi/static/x.png",
                AssetRef.store("/adminapi/static/x.png", LOCAL));
    }

    // ---- 4) 改 base --------------------------------------------------------
    private static void baseChangeFollows() {
        section("改 base → 出网地址随之变化（同 key 可存回）");
        String key = "image/20260101/abc.png";
        eq("LOCAL 渲染", "https://api.example.com/api/uploads/image/20260101/abc.png",
                AssetRef.render(key, LOCAL));
        eq("PREFIXED 渲染", "https://an520.xin/lxt-v2/api/uploads/image/20260101/abc.png",
                AssetRef.render(key, PREFIXED));
        eq("OSS 渲染", "https://cdn.example.com/image/20260101/abc.png", AssetRef.render(key, OSS));
        eq("换 base 后仍存回同一 key",
                AssetRef.store(AssetRef.render(key, LOCAL), LOCAL),
                AssetRef.store(AssetRef.render(key, PREFIXED), PREFIXED));

        // 老域名地址读出来即变成新域名地址（归一化的目的）
        String legacy = "https://zzty03.starvitech.com/api/uploads/image/20260101/abc.png";
        eq("老域名地址在 PREFIXED base 下渲染成新域名",
                "https://an520.xin/lxt-v2/api/uploads/image/20260101/abc.png",
                AssetRef.render(AssetRef.store(legacy, PREFIXED), PREFIXED));
        // 后台端同理
        eq("老域名地址后台端渲染成新域名",
                "https://an520.xin/lxt-v2/adminapi/uploads/image/20260101/abc.png",
                AssetRef.renderAdmin(AssetRef.store(legacy, PREFIXED), PREFIXED));
    }

    // ---- 5) 旧域绝对地址归一 ------------------------------------------------
    private static void legacyAbsoluteNormalized() {
        section("旧域绝对地址归一成相对 key");
        String[] legacyInputs = {
                "https://zzty02.starvitech.com/adminapi/uploads/image/20260304/de9f3736.png",
                "https://zzty03.starvitech.com/api/uploads/image/20260315/620e4501.jpeg",
                "http://zzty02.starvitech.com/adminapi/uploads/image/20260129/b3e36c64.png",
                "https://an520.xin/lxt-v2/adminapi/uploads/image/20261023/7db233cc.png",
                "/api/uploads/image/20260101/x.png",
                "/adminapi/uploads/image/20260101/x.png"
        };
        for (String input : legacyInputs) {
            String stored = AssetRef.store(input, LOCAL);
            eq("归一 " + input, expectedKeyOf(input), stored);
            eq("归一后再归一不变 " + input, stored, AssetRef.store(stored, LOCAL));
            String rendered = AssetRef.render(stored, PREFIXED);
            check("归一后渲染指向新 base 且不含老域名：" + input,
                    rendered.startsWith("https://an520.xin/lxt-v2/")
                            && !rendered.contains("starvitech"));
        }
    }

    /** 期望 key：取上传前缀之后的部分。 */
    private static String expectedKeyOf(String raw) {
        int i = raw.indexOf("/api/uploads/");
        if (i >= 0) {
            return raw.substring(i + "/api/uploads/".length());
        }
        i = raw.indexOf("/adminapi/uploads/");
        return raw.substring(i + "/adminapi/uploads/".length());
    }

    // ---- 6) 前缀特例 --------------------------------------------------------
    private static void prefixSpecialCasesPreserved() {
        section("前缀特例保留 /api/static/ 与 /adminapi/");
        eq("/api/static/ 小程序端", "https://api.example.com/api/static/logo.png",
                AssetRef.render("/api/static/logo.png", LOCAL));
        eq("/api/static/ 后台端（沿用请求 origin + /adminapi/static/）",
                "https://api.example.com/adminapi/static/logo.png",
                AssetRef.renderAdmin("/api/static/logo.png", LOCAL));
        eq("/api/static/ 后台端在带前缀 base 下仍用请求 origin（既有怪癖，原样保留）",
                "https://an520.xin/adminapi/static/logo.png",
                AssetRef.renderAdmin("/api/static/logo.png", PREFIXED));
        eq("/adminapi/static/ 后台端", "https://api.example.com/adminapi/static/logo.png",
                AssetRef.renderAdmin("/adminapi/static/logo.png", LOCAL));
        eq("/adminapi/ 小程序端映射成 /api/", "https://api.example.com/api/foo/bar",
                AssetRef.render("/adminapi/foo/bar", LOCAL));
        eq("/adminapi/ 后台端原样", "https://api.example.com/adminapi/foo/bar",
                AssetRef.renderAdmin("/adminapi/foo/bar", LOCAL));
        eq("/api/static/ 存回不变", "/api/static/logo.png",
                AssetRef.store("/api/static/logo.png", LOCAL));
    }

    // ---- 7) 小程序端 / 入库 与旧实现对齐 ------------------------------------
    private static void parityFrontAndStore() {
        section("与旧 UrlUtils 逐字对照（相对地址行为不变）");
        String[] inputs = {
                "", null,
                "image/20260101/abc.png",
                "image/x.jpeg",
                "/api/static/logo.png",
                "/api/static/default_avatar.png",
                "/adminapi/static/logo.png",
                "/adminapi/foo/bar",
                "/adminapi/uploads/image/20260101/x.png",
                "http://external.example.org/a.png",
                "https://beian.miit.gov.cn/"
        };
        for (AssetBase base : BASES) {
            String domain = base.isLocal() ? null : base.storageDomain();
            for (String input : inputs) {
                // 对象存储下 /adminapi/uploads/ 的旧输出是「本地前缀 + OSS 域名混拼」，
                // 属于有意的行为变化，见 intendedDifferences()。
                if (!base.isLocal() && input != null && input.contains("/adminapi/uploads/")) {
                    continue;
                }
                eq("toAbsoluteUrl " + input + " @ " + label(base),
                        legacyToAbsoluteUrl(input, base.publicBase(), base.engine(), domain),
                        AssetRef.render(input, base));
                eq("toRelativeUrl " + input + " @ " + label(base),
                        legacyToRelativeUrl(input, base.publicBase(), base.engine(), domain),
                        AssetRef.store(input, base));
            }
        }
    }

    // ---- 8) 后台端与旧实现对齐 ----------------------------------------------
    private static void parityAdmin() {
        section("与旧 UrlUtils 逐字对照（后台端）");
        String[] inputs = {
                "", null,
                "image/20260101/abc.png",
                "/api/static/logo.png",
                "/adminapi/static/logo.png",
                "/adminapi/uploads/image/20260101/x.png",
                "http://external.example.org/a.png"
        };
        for (AssetBase base : BASES) {
            String domain = base.isLocal() ? null : base.storageDomain();
            for (String input : inputs) {
                if (!base.isLocal() && input != null && input.contains("/adminapi/uploads/")) {
                    continue;
                }
                eq("toAdminAbsoluteUrl " + input + " @ " + label(base),
                        legacyToAdminAbsoluteUrl(input, base.publicBase(), base.requestBase(),
                                base.engine(), domain),
                        AssetRef.renderAdmin(input, base));
            }
        }
    }

    // ---- 9) 有意的行为变化 --------------------------------------------------
    private static void intendedDifferences() {
        section("有意的行为变化（旧输出必然 404 或必然脏值）");

        // (a) 老域名绝对地址：旧实现拼出 https://oldhostimage/x.png（脏值，且换域后仍指向老域名）
        String legacy = "https://zzty02.starvitech.com/adminapi/uploads/image/x.png";
        eq("旧实现会拼出脏值（对照）", "https://zzty02.starvitech.comimage/x.png",
                legacyToRelativeUrl(legacy, "https://api.example.com", "local", null));
        eq("新实现归一成相对 key", "image/x.png", AssetRef.store(legacy, LOCAL));

        // (b) 对象存储下后台上传路径：旧实现把本地路径前缀拼到 OSS 域名后（对象 key 里没有该前缀）
        eq("旧实现在 OSS 下输出不存在的对象路径",
                "https://cdn.example.com/adminapi/uploads/image/x.png",
                legacyToAdminAbsoluteUrl("/adminapi/uploads/image/x.png", "https://api.example.com",
                        "https://api.example.com", "aliyun", "https://cdn.example.com"));
        eq("新实现按对象 key 输出", "https://cdn.example.com/image/x.png",
                AssetRef.renderAdmin("/adminapi/uploads/image/x.png", OSS));
        eq("新实现 OSS 入库归一", "image/x.png",
                AssetRef.store("/adminapi/uploads/image/x.png", OSS));

        // (c) 后台端渲染 /adminapi/ 非上传路径：旧实现重复拼前缀
        eq("旧实现重复前缀", "https://api.example.com/adminapi/uploads/adminapi/foo/bar",
                legacyToAdminAbsoluteUrl("/adminapi/foo/bar", "https://api.example.com",
                        "https://api.example.com", "local", null));
        eq("新实现原样输出", "https://api.example.com/adminapi/foo/bar",
                AssetRef.renderAdmin("/adminapi/foo/bar", LOCAL));
    }

    // ---- 10) 幂等收敛 -------------------------------------------------------
    private static void nonCanonicalConvergesIdempotently() {
        section("非规范输入一次收敛且不再变化");
        String[] nonCanonical = {
                "https://api.example.com/api/uploads/image/20260101/x.png",
                "https://api.example.com/adminapi/uploads/image/20260101/x.png",
                "/api/uploads/image/20260101/x.png",
                "/adminapi/uploads/image/20260101/x.png",
                "https://zzty02.starvitech.com/adminapi/uploads/image/20260101/x.png",
                "https://an520.xin/lxt-v2/api/uploads/image/20260101/x.png"
        };
        for (String input : nonCanonical) {
            String once = AssetRef.store(input, LOCAL);
            eq("store 幂等 " + input, once, AssetRef.store(once, LOCAL));
            eq("render→store 稳定 " + input, once,
                    AssetRef.store(AssetRef.render(once, LOCAL), LOCAL));
            check("收敛结果不含协议头 " + input, !once.startsWith("http"));
        }
    }

    // ---- 11) 边界 -----------------------------------------------------------
    private static void edgeCases() {
        section("边界：空值、外部链接、非请求线程");
        eq("null 渲染", "", AssetRef.render((String) null, LOCAL));
        eq("空串渲染", "", AssetRef.render("", LOCAL));
        eq("null 入库", "", AssetRef.store((String) null, LOCAL));
        eq("空串入库", "", AssetRef.store("", LOCAL));
        eq("外部链接不被改写", "https://beian.miit.gov.cn/",
                AssetRef.store("https://beian.miit.gov.cn/", LOCAL));
        eq("外部链接渲染原样", "https://beian.miit.gov.cn/",
                AssetRef.render("https://beian.miit.gov.cn/", LOCAL));
        eq("无 base 只出路径", "/api/uploads/image/x.png", AssetRef.render("image/x.png", NO_BASE));
        eq("无 base 不产生 null 前缀", "/api/static/logo.png",
                AssetRef.render("/api/static/logo.png", NO_BASE));
        eq("外部绝对地址不因无 base 被误判", "https://cdn.thirdparty.com/assets/a.png",
                AssetRef.store("https://cdn.thirdparty.com/assets/a.png", NO_BASE));
        eq("domain 概念不参与换算（OSS 域名只拼 key）", "https://cdn.example.com/image/x.png",
                AssetRef.render("image/x.png", OSS));
    }

    // ---- 旧实现对照（逐字转写，仅把配置来源换成入参） -----------------------
    private static String legacyToAbsoluteUrl(String url, String base, String engine, String domain) {
        if (url == null || url.equals("")) {
            return "";
        }
        if (url.startsWith("http://") || url.startsWith("https://")) {
            return url;
        }
        if (url.indexOf("/") != 0) {
            url = "/" + url;
        }
        if (url.startsWith("/api/static/")) {
            return base + url;
        }
        if (url.startsWith("/adminapi/")) {
            return base + url.replace("/adminapi/", "/api/");
        }
        if ("local".equals(engine)) {
            return base + "/" + "api/uploads" + url;
        }
        if (domain != null) {
            return domain + url;
        }
        return url;
    }

    private static String legacyToAdminAbsoluteUrl(String url, String base, String requestBase,
                                                   String engine, String domain) {
        if (url == null || url.equals("")) {
            return "";
        }
        if (url.startsWith("http://") || url.startsWith("https://")) {
            return url;
        }
        if (url.indexOf("/") != 0) {
            url = "/" + url;
        }
        if (url.startsWith("/adminapi/static/")) {
            return base + url;
        }
        if (url.startsWith("/api/static/")) {
            return requestBase + url.replace("/api/static/", "/adminapi/static/");
        }
        if ("local".equals(engine)) {
            if (url.startsWith("/adminapi/uploads/")) {
                return base + url;
            }
            return base + "/" + "adminapi/uploads" + url;
        }
        if (domain != null) {
            return domain + url;
        }
        return url;
    }

    private static String legacyToRelativeUrl(String url, String base, String engine, String domain) {
        if (url == null || url.equals("")) {
            return "";
        }
        if ("local".equals(engine)) {
            return url.replace(base, "")
                    .replace("/api/uploads/", "")
                    .replace("/adminapi/uploads/", "");
        }
        if (domain != null) {
            return url.replace(domain, "").replace("/api/uploads/", "");
        }
        return url;
    }

    // ---- 断言器 ------------------------------------------------------------
    private static void section(String title) {
        System.out.println();
        System.out.println("== " + title + " ==");
    }

    private static void eq(String name, String expected, String actual) {
        if (expected == null ? actual == null : expected.equals(actual)) {
            passed++;
            return;
        }
        failed++;
        System.out.println("  ✗ " + name);
        System.out.println("      期望: " + expected);
        System.out.println("      实际: " + actual);
    }

    private static void check(String name, boolean condition) {
        if (condition) {
            passed++;
            return;
        }
        failed++;
        System.out.println("  ✗ " + name);
    }

    private static String label(AssetBase base) {
        if (base.isLocal()) {
            return base.publicBase().isEmpty() ? "local-no-base" : "local:" + base.publicBase();
        }
        return base.engine() + ":" + base.storageDomain();
    }

    private static String[] concat(String[] left, String[] right) {
        String[] out = new String[left.length + right.length];
        System.arraycopy(left, 0, out, 0, left.length);
        System.arraycopy(right, 0, out, left.length, right.length);
        return out;
    }
}
