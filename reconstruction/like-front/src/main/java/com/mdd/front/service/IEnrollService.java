package com.mdd.front.service;

import java.util.Map;

/**
 * 客服报名链接与表单（T29.7 / #27）。
 *
 * <p>客服在后台生成带 token 的小程序路径 → 家长打开 {@code pages/enroll} →
 * 提交表单；同场次 + 手机号 + 学员名唯一，重复提交给出明确提示。</p>
 */
public interface IEnrollService {

    /**
     * 报名页场次信息：token 优先（防暴露场次 ID），无 token 时兼容 sessionId。
     * 链接失效/场次不存在返回 null。
     */
    Map<String, Object> detail(String token, String sessionId);

    /** 提交报名表单（公开，未登录也可提交）。 */
    Map<String, Object> submit(Integer userId, Map<String, Object> input);

    /** 当前账号在某场次已提交的报名（未登录返回空数组）。 */
    java.util.List<Map<String, Object>> mySubmissions(Integer userId, Integer sessionId);
}
