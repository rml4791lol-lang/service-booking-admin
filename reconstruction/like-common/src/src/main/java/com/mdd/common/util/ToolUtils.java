/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson2.JSONObject
 */
package com.mdd.common.util;

import com.alibaba.fastjson2.JSONObject;
import com.mdd.common.config.GlobalConfig;
import com.mdd.common.config.ProjectConfig;
import com.mdd.common.util.StringUtils;
import com.mdd.common.util.YmlUtils;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Random;
import java.util.UUID;

public class ToolUtils {
    /**
     * 口令盐配置键的唯一字面量来源（#182）：like.unique-identification。
     * 读盐一律引用本常量，避免 LoginServiceImpl 等处再抄一份字面量漂移。
     */
    public static final String SALT_CONFIG_KEY = "like.unique-identification";

    public static String makeUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 32);
    }

    /**
     * 导出文件名安全化（#58）：去掉路径分隔符与 Windows 保留字符，避免
     * file_name 参数把导出文件写到 upload-directory 之外（路径穿越）。
     * 返回空串时调用方应回退到 makeUUID()。
     */
    public static String safeFileName(String name) {
        if (name == null) {
            return "";
        }
        String safe = name.replaceAll("[\\\\/:*?\"<>|\r\n\t]+", "_");
        safe = safe.replaceAll("^[._ ]+|[._ ]+$", "");
        if (safe.length() > 80) {
            safe = safe.substring(0, 80);
        }
        return safe;
    }

    /**
     * 导出文件名入口：先安全化；为空（未传/全被过滤）时回退随机 UUID，避免写出 ".xlsx" 这类隐藏文件。
     */
    public static String safeExportFileName(String name) {
        String safe = safeFileName(name);
        return safe.isEmpty() ? makeUUID() : safe;
    }

    public static String makeMd5(String data) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] array = md5.digest(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte item : array) {
                sb.append(Integer.toHexString(item & 0xFF | 0x100), 1, 3);
            }
            return sb.toString();
        }
        catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 口令摘要：md5(md5(password + like.unique-identification))。
     *
     * <p>盐缺失时必须失败，不能把 null 拼成字面量 "null"：后台生成侧（like-admin）与
     * 校验侧（like-front）会各自算出不同但都"合法"的摘要，口令永远登不上且无日志
     * （2026-10-30 教练端事故，见 #158 / #159）。</p>
     *
     * @throws IllegalStateException 盐未配置或只有空白字符。处置：补齐配置后重启应用；
     *                               已按旧盐生成的口令需走重设流程（#161）。
     */
    public static String makePassword(String password) {
        String salt = YmlUtils.get(SALT_CONFIG_KEY);
        if (salt == null || salt.trim().isEmpty()) {
            throw new IllegalStateException(
                "口令加盐配置缺失：like.unique-identification 未配置或为空，拒绝生成/校验口令摘要");
        }
        String newPWd = password + salt;
        String md5Pwd = ToolUtils.makeMd5(ToolUtils.makeMd5(newPWd));
        return md5Pwd;
    }

    /**
     * 老系统兼容摘要（2026-11-04 割接后发现）：外包老系统的 application.yml 没有配置
     * like.unique-identification，YmlUtils.get 返回 null，字符串拼接退化为字面量 "null"，
     * 存量口令哈希全部按 md5(md5(password + "null")) 生成（已用老库 10 个社区账号
     * 密码 123456 反推验证），算法本身与新系统相同，仅盐不同。
     *
     * <p>过渡期用途：让老账号原密码在新系统继续可用，避免割接后甲方/教练/代理的口令
     * 全部失效。仅用于比对，不用于生成新口令；甲方改完密码后由后续版本删除。</p>
     */
    public static String makeLegacyPassword(String password) {
        return ToolUtils.makeMd5(ToolUtils.makeMd5(password + "null"));
    }

    /**
     * 口令比对（新盐优先、老系统盐兜底）：常量时间比较，避免时序侧信道。
     *
     * @throws IllegalStateException like.unique-identification 未配置（沿用 makePassword 的失败口径）
     */
    public static boolean passwordMatches(String rawPassword, String encodedPassword) {
        if (rawPassword == null || encodedPassword == null) {
            return false;
        }
        byte[] expected = encodedPassword.getBytes(StandardCharsets.UTF_8);
        if (MessageDigest.isEqual(expected, ToolUtils.makePassword(rawPassword).getBytes(StandardCharsets.UTF_8))) {
            return true;
        }
        return MessageDigest.isEqual(expected, ToolUtils.makeLegacyPassword(rawPassword).getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 手机号打码（#182 收编四处私有实现，收敛为一份）。
     *
     * <p>共同口径：11 位输入保留前 3 后 4（138****1234）。</p>
     * <ul>
     *   <li>strict=true（登录日志防泄露口径，原 LoginLogFields.maskPhone）：
     *       null 归 "-"，先 trim；非 11 位（含空串）整体打码为 "****"，确保不泄露原值。</li>
     *   <li>strict=false（展示脱敏口径，原 InstructorAccountServiceImpl 私有实现）：
     *       null 归 ""，不 trim；非 11 位原样返回（不改变既有展示输出）。</li>
     * </ul>
     */
    public static String maskPhone(String phone, boolean strict) {
        if (strict) {
            if (phone == null) {
                return "-";
            }
            String trimmed = phone.trim();
            if (trimmed.length() == 11) {
                return trimmed.substring(0, 3) + "****" + trimmed.substring(7);
            }
            return "****";
        }
        if (phone == null) {
            return "";
        }
        if (phone.length() == 11) {
            return phone.substring(0, 3) + "****" + phone.substring(7);
        }
        return phone;
    }

    public static String makeToken() {
        long millisecond = System.currentTimeMillis();
        String randStr = ToolUtils.randomString(8);
        String secret = GlobalConfig.secret;
        String token = ToolUtils.makeMd5(ToolUtils.makeUUID() + millisecond + randStr);
        return ToolUtils.makeMd5(token + secret) + ToolUtils.randomString(6);
    }

    public static String randomString(int length) {
        Random random = new Random();
        StringBuilder stringBuffer = new StringBuilder();
        String str = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        int strLength = str.length();
        for (int i = 0; i < length; ++i) {
            int index = random.nextInt(strLength);
            stringBuffer.append(str.charAt(index));
        }
        return stringBuffer.toString();
    }

    public static String randomInt(int length) {
        Random random = new Random();
        StringBuilder stringBuffer = new StringBuilder();
        String str = "0123456789";
        for (int i = 0; i < length; ++i) {
            int index = random.nextInt(10);
            stringBuffer.append(str.charAt(index));
        }
        return stringBuffer.toString();
    }

    public static String storageUnit(Long size) {
        if (size == null) {
            return "0B";
        }
        if (size < 1024L) {
            return size + "B";
        }
        if ((size = Long.valueOf(size / 1024L)) < 1024L) {
            return size + "KB";
        }
        if ((size = Long.valueOf(size / 1024L)) < 1024L) {
            size = size * 100L;
            return size / 100L + "." + size % 100L + "MB";
        }
        size = size * 100L / 1024L;
        return size / 100L + "." + size % 100L + "GB";
    }

    public static void download(String urlStr, String savePath) throws IOException {
        ByteArrayOutputStream bos = null;
        FileOutputStream fos = null;
        InputStream inputStream = null;
        try {
            int len;
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            inputStream = conn.getInputStream();
            byte[] buffer = new byte[4096];
            bos = new ByteArrayOutputStream();
            while ((len = inputStream.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
            byte[] getData = bos.toByteArray();
            String fileName = StringUtils.substringAfterLast((String)savePath, (String)"/");
            String path = savePath.replace("/" + fileName, "");
            File saveDir = new File(path);
            if (!saveDir.exists() && !saveDir.mkdirs()) {
                throw new IOException("\u521b\u5efa\u5b58\u50a8\u6587\u4ef6\u5939\u5931\u8d25");
            }
            File file = new File(savePath);
            fos = new FileOutputStream(file);
            fos.write(getData);
        }
        catch (Exception e) {
            throw new IOException(e.getMessage());
        }
        finally {
            if (bos != null) {
                try {
                    bos.close();
                }
                catch (IOException iOException) {}
            }
            if (fos != null) {
                try {
                    fos.close();
                }
                catch (IOException iOException) {}
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                }
                catch (IOException iOException) {}
            }
        }
    }

    public static JSONObject getExportData(Long total, Integer pageSize, Integer pageStart, Integer pageEnd, String fileName) {
        JSONObject ret = new JSONObject();
        Integer cpPageSize = StringUtils.isNull(pageSize) ? ProjectConfig.lists.get("pageSize") : pageSize;
        Integer sumPage = ToolUtils.getMaxCeil(total.intValue(), cpPageSize);
        Integer pageSizeMax = ProjectConfig.lists.get("pageSizeMax");
        ret.put("count", (Object)total);
        ret.put("page_size", (Object)cpPageSize);
        ret.put("sum_page", (Object)sumPage);
        ret.put("max_page", (Object)Math.ceil(pageSizeMax / cpPageSize));
        ret.put("all_max_size", (Object)pageSizeMax);
        ret.put("page_end", (Object)Math.min(sumPage, StringUtils.isNull(pageEnd) ? 200 : pageEnd));
        ret.put("page_start", (Object)(StringUtils.isNull(pageStart) ? 1 : pageStart));
        ret.put("file_name", (Object)fileName);
        return ret;
    }

    public static Integer getMaxCeil(Integer count, Integer pageSize) {
        return Math.max((int)Math.ceil((double)count.intValue() / (double)pageSize.intValue()), 1);
    }
}

