package com.mdd.front.service;

import java.util.List;
import java.util.Map;

/**
 * 开班申请（T29.7 / #27）。
 *
 * <p>合并计数口径：同一街道 + 同一课程类型只有一个聚合组，重复申请累加 joined；
 * 达到 5 人自动进入「客服跟进中」。只收集意向，不收款、不扣券、不生成预约。</p>
 */
public interface IClassApplyService {

    /** 当前账号的开班申请（含所在聚合组的合并人数）。 */
    List<Map<String, Object>> applications(Integer userId);

    /** 提交开班申请；同街道同课程类型合并计数。 */
    Map<String, Object> create(Integer userId, Map<String, Object> input);
}
