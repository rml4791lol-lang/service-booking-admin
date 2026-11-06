package com.mdd.front.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.mdd.common.exception.OperateException;
import com.mdd.common.util.ConfigUtils;
import com.mdd.common.util.StringUtils;
import com.mdd.front.service.IWechatLoginService;
import com.mdd.front.vo.login.WxSessionVo;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 微信小程序 code2session：真实实现（#23、#124）。
 *
 * <p>与老系统一致，从 la_config 的 mnp_setting 读取 app_id/app_secret，调微信
 * jscode2session 换 openid。本类<b>不再包含 mock 分支</b>：本地/测试的 mock 登录
 * 由 {@link WxMockLoginServiceImpl} 提供（{@code like.wechat.login.mock-enabled=true}
 * 且非 production/experience profile 时才装配），生产/体验结构上不具备伪造 openid 的能力。</p>
 *
 * <p>配置缺失或仍是占位值时明确报「微信登录未配置」，不静默降级 mock。</p>
 */
@Service
public class WxLoginServiceImpl implements IWechatLoginService {

    private static final Logger log = LoggerFactory.getLogger(WxLoginServiceImpl.class);

    private static final String CODE2SESSION_URL =
            "https://api.weixin.qq.com/sns/jscode2session"
                    + "?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code";

    @Value("${like.wechat.login.app-id:}")
    private String appIdOverride;

    @Value("${like.wechat.login.app-secret:}")
    private String appSecretOverride;

    @Value("${like.wechat.login.connect-timeout-ms:3000}")
    private int connectTimeoutMs;

    @Value("${like.wechat.login.read-timeout-ms:5000}")
    private int readTimeoutMs;

    @Override
    public WxSessionVo code2Session(String code) {
        String jsCode = code == null ? "" : code.trim();
        if (jsCode.isEmpty()) {
            throw new OperateException("code参数缺失");
        }

        String appId = firstNonBlank(appIdOverride, configValue("app_id"));
        String appSecret = firstNonBlank(appSecretOverride, configValue("app_secret"));
        if (isPlaceholder(appId) || isPlaceholder(appSecret)) {
            throw new OperateException("微信登录未配置，请联系管理员");
        }

        String body;
        try {
            String url = String.format(CODE2SESSION_URL,
                    URLEncoder.encode(appId, "UTF-8"),
                    URLEncoder.encode(appSecret, "UTF-8"),
                    URLEncoder.encode(jsCode, "UTF-8"));
            body = httpGet(url);
        } catch (Exception e) {
            log.warn("微信 code2session 请求失败: {}", e.getClass().getSimpleName());
            throw new OperateException("微信登录服务暂不可用，请稍后重试");
        }

        JSONObject json;
        try {
            json = JSON.parseObject(body);
        } catch (Exception e) {
            log.warn("微信 code2session 返回非 JSON");
            throw new OperateException("微信登录服务暂不可用，请稍后重试");
        }
        if (json == null) {
            throw new OperateException("微信登录服务暂不可用，请稍后重试");
        }

        Integer errcode = json.getInteger("errcode");
        if (errcode != null && errcode != 0) {
            log.warn("微信 code2session 失败 errcode={}", errcode);
            throw new OperateException(WxLoginErrors.messageFor(errcode));
        }

        String openid = trimToEmpty(json.getString("openid"));
        if (openid.isEmpty()) {
            log.warn("微信 code2session 返回缺少 openid");
            throw new OperateException("微信登录失败，请稍后重试");
        }
        return new WxSessionVo(openid, trimToEmpty(json.getString("unionid")));
    }

    private String configValue(String name) {
        Map<String, String> config = ConfigUtils.get("mnp_setting");
        return config == null ? "" : trimToEmpty(config.get(name));
    }

    private String httpGet(String url) throws IOException {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(connectTimeoutMs);
            connection.setReadTimeout(readTimeoutMs);
            connection.setRequestProperty("Accept", "application/json");
            int status = connection.getResponseCode();
            InputStream stream = status >= 200 && status < 300
                    ? connection.getInputStream()
                    : connection.getErrorStream();
            if (stream == null) {
                throw new IOException("empty response");
            }
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int read;
            while ((read = stream.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            stream.close();
            return new String(out.toByteArray(), StandardCharsets.UTF_8);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private boolean isPlaceholder(String value) {
        String text = trimToEmpty(value).toLowerCase();
        return text.isEmpty()
                || text.contains("__unset__")
                || text.contains("placeholder")
                || text.contains("your_")
                || text.contains("changeme")
                || text.contains("example");
    }

    private String firstNonBlank(String first, String second) {
        if (StringUtils.isNotBlank(first)) {
            return first.trim();
        }
        return second == null ? "" : second.trim();
    }

    private String trimToEmpty(String value) {
        return value == null ? "" : value.trim();
    }
}
