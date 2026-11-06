package com.mdd.common.asset;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

/**
 * {@link AssetRef} 的命令行适配器（#122 P3）。
 *
 * <p>唯一消费者是一次性迁移脚本 {@code 重建工程/tools/assetref/normalize-legacy-urls.mjs}：
 * 脚本负责数据库读写、台账与回滚，但「什么算自家资源、归一成什么」的判定必须由
 * 同一份 Java 实现给出——否则 JS 与 Java 两套规则迟早漂移。</p>
 *
 * <p>协议（行式，制表符分隔，UTF-8）：</p>
 * <pre>
 *   输入：id \t raw
 *   输出：id \t kind \t value
 * </pre>
 * <p>空值与含空格/逗号的地址都能安全传输；输出行数与输入一一对应。</p>
 *
 * <p>子命令：</p>
 * <ul>
 *   <li>{@code normalize} —— 迁移口径：base 全空（不依赖任何域名），
 *       把自家绝对地址归一成相对 key，外部绝对地址原样保留；</li>
 *   <li>{@code render <base> [engine] [domain]} —— 把入库值按给定 base 渲染成出网地址，
 *       用于演练「改 base 后地址随之变化」。</li>
 * </ul>
 */
public final class AssetRefCli {

    private AssetRefCli() {
    }

    public static void main(String[] args) throws Exception {
        String mode = args.length > 0 ? args[0] : "normalize";
        AssetBase base;
        if ("render".equals(mode)) {
            if (args.length < 2) {
                System.err.println("用法：render <publicBase> [engine] [storageDomain]");
                System.exit(2);
                return;
            }
            String publicBase = args[1];
            String engine = args.length > 2 ? args[2] : "local";
            String domain = args.length > 3 ? args[3] : "";
            base = new AssetBase(engine, publicBase, publicBase, domain, "api/uploads", "adminapi/uploads");
        } else if ("normalize".equals(mode)) {
            // 迁移口径：不绑定任何域名/前缀之外的假设，纯按上传路径识别
            base = new AssetBase("local", "", "", "", "api/uploads", "adminapi/uploads");
        } else {
            System.err.println("未知子命令：" + mode);
            System.exit(2);
            return;
        }

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in, StandardCharsets.UTF_8));
        PrintWriter writer = new PrintWriter(
                new java.io.OutputStreamWriter(System.out, StandardCharsets.UTF_8));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.isEmpty()) {
                continue;
            }
            int tab = line.indexOf('\t');
            String id = tab < 0 ? line : line.substring(0, tab);
            String raw = tab < 0 ? "" : line.substring(tab + 1);
            AssetRef ref = AssetRef.parse(raw, base);
            String value = "render".equals(mode) ? ref.render(base) : ref.store(base);
            writer.println(id + "\t" + ref.kind() + "\t" + value);
        }
        writer.flush();
    }
}
