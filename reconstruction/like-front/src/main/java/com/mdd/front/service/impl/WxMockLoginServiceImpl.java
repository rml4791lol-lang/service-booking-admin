package com.mdd.front.service.impl;

import com.mdd.common.exception.OperateException;
import com.mdd.common.util.SensitiveUtils;
import com.mdd.front.service.IWechatLoginService;
import com.mdd.front.vo.login.WxSessionVo;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * 微信小程序 mock 登录（#23、#124）：只在本地/测试环境存在。
 *
 * <p>装配条件：{@code like.wechat.login.mock-enabled=true} <b>且</b> profile 不是
 * production/experience。生产/体验（或未显式声明测试 profile）下打开该开关会由
 * {@code RuntimeModeGuard} 在启动时直接失败，不会带着伪造 openid 的能力运行。</p>
 *
 * <p>不发起任何微信请求。mock-openid 留空时按 code 生成稳定 openid，便于用不同 code
 * 验证“不同家长账号互相隔离”；填写后固定返回同一个 openid，便于固定账号联调。</p>
 *
 * <p>本地还需要验“无效 code / 微信错误码”分支：{@code like.wechat.login.mock-error-codes}
 * 保留 code 到微信 errcode 的映射，例如 {@code mock-invalid:40029,mock-error-40013:40013}；
 * 命中后走与真实微信完全相同的错误文案映射。该配置默认空，生产模板不配置。</p>
 */
@Component
@Primary
@Profile({"dev", "test", "local"})
@ConditionalOnProperty(prefix = "like.wechat.login", name = "mock-enabled", havingValue = "true")
public class WxMockLoginServiceImpl implements IWechatLoginService {

    private static final Logger log = LoggerFactory.getLogger(WxMockLoginServiceImpl.class);

    @Value("${like.wechat.login.mock-openid:}")
    private String mockOpenid;

    /** 本地 mock 专用：code -> 微信 errcode，逗号分隔，例如 mock-invalid:40029。 */
    @Value("${like.wechat.login.mock-error-codes:}")
    private String mockErrorCodes;

    @Override
    public WxSessionVo code2Session(String code) {
        String jsCode = code == null ? "" : code.trim();
        if (jsCode.isEmpty()) {
            throw new OperateException("code参数缺失");
        }
        Integer mockErrcode = mockErrorCodeFor(jsCode);
        if (mockErrcode != null) {
            log.warn("微信登录本地 mock 返回错误码 errcode={}", mockErrcode);
            throw new OperateException(WxLoginErrors.messageFor(mockErrcode.intValue()));
        }
        String openid = mockOpenid == null ? "" : mockOpenid.trim();
        if (openid.isEmpty()) {
            openid = "mock_" + md5(jsCode).substring(0, 24);
        }
        log.info("微信登录使用本地 mock，未调用 code2session，openid={}",
                SensitiveUtils.maskOpenid(openid));
        return new WxSessionVo(openid, "");
    }

    /**
     * 解析 mock 错误码映射。用最后一个冒号分隔，兼容 {@code mock-error--1:-1} 这类负数错误码。
     */
    private Integer mockErrorCodeFor(String code) {
        if (code == null || mockErrorCodes == null || mockErrorCodes.trim().isEmpty()) {
            return null;
        }
        for (String item : mockErrorCodes.split(",")) {
            int split = item.lastIndexOf(':');
            if (split <= 0 || split >= item.length() - 1) {
                continue;
            }
            String mockCode = item.substring(0, split).trim();
            String errcodeText = item.substring(split + 1).trim();
            if (!mockCode.equals(code)) {
                continue;
            }
            try {
                return Integer.valueOf(errcodeText);
            } catch (NumberFormatException e) {
                log.warn("mock-error-codes 配置的 errcode 非法: {}", errcodeText);
                return null;
            }
        }
        return null;
    }

    private String md5(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] bytes = digest.digest(value.getBytes(StandardCharsets.UTF_8));
            StringBuilder builder = new StringBuilder(bytes.length * 2);
            for (byte b : bytes) {
                builder.append(String.format("%02x", b & 0xff));
            }
            return builder.toString();
        } catch (Exception e) {
            return Integer.toHexString(value.hashCode());
        }
    }
}
