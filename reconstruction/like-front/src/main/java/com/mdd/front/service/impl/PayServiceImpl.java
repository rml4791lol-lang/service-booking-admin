/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  cn.binarywang.wx.miniapp.api.WxMaService
 *  cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult
 *  com.alibaba.fastjson2.JSONObject
 *  com.alibaba.fastjson2.JSONWriter$Feature
 *  com.alipay.api.AlipayApiException
 *  com.alipay.api.AlipayRequest
 *  com.alipay.api.DefaultAlipayClient
 *  com.alipay.api.request.AlipayTradePagePayRequest
 *  com.alipay.api.request.AlipayTradeWapPayRequest
 *  com.alipay.api.response.AlipayTradePagePayResponse
 *  com.alipay.api.response.AlipayTradeWapPayResponse
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
 *  com.mdd.common.config.AlipayConfig
 *  com.mdd.common.entity.RechargeOrder
 *  com.mdd.common.entity.course.CourseClass
 *  com.mdd.common.entity.course.UserCourse
 *  com.mdd.common.entity.setting.DevPayConfig
 *  com.mdd.common.entity.setting.DevPayWay
 *  com.mdd.common.entity.student.Student
 *  com.mdd.common.entity.user.User
 *  com.mdd.common.entity.user.UserAuth
 *  com.mdd.common.enums.AccountLogEnum
 *  com.mdd.common.enums.ClientEnum
 *  com.mdd.common.enums.PaymentEnum
 *  com.mdd.common.enums.YesNoEnum
 *  com.mdd.common.exception.OperateException
 *  com.mdd.common.exception.PaymentException
 *  com.mdd.common.mapper.RechargeOrderMapper
 *  com.mdd.common.mapper.course.CourseClassMapper
 *  com.mdd.common.mapper.course.UserCourseMapper
 *  com.mdd.common.mapper.log.UserAccountLogMapper
 *  com.mdd.common.mapper.setting.DevPayConfigMapper
 *  com.mdd.common.mapper.setting.DevPayWayMapper
 *  com.mdd.common.mapper.student.StudentMapper
 *  com.mdd.common.mapper.user.UserAuthMapper
 *  com.mdd.common.mapper.user.UserMapper
 *  com.mdd.common.plugin.wechat.WxMnpDriver
 *  com.mdd.common.plugin.wechat.WxPayDriver
 *  com.mdd.common.plugin.wechat.request.PaymentRequestV3
 *  com.mdd.common.util.ConfigUtils
 *  com.mdd.common.util.MessageUtil
 *  com.mdd.common.util.StringUtils
 *  com.mdd.common.util.TimeUtils
 *  com.mdd.common.util.UrlUtils
 *  com.mdd.common.util.YmlUtils
 *  com.mdd.front.service.IPayService
 *  com.mdd.front.service.impl.PayServiceImpl
 *  com.mdd.front.validate.PaymentValidate
 *  com.mdd.front.vo.pay.PayStatusVo
 *  com.mdd.front.vo.pay.PayWayInfoVo
 *  com.mdd.front.vo.pay.PayWayListVo
 *  javax.annotation.Resource
 *  me.chanjar.weixin.common.bean.oauth2.WxOAuth2AccessToken
 *  me.chanjar.weixin.mp.api.WxMpService
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.beans.factory.annotation.Value
 *  org.springframework.stereotype.Service
 *  org.springframework.transaction.annotation.Transactional
 *  org.springframework.util.Assert
 */
package com.mdd.front.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayRequest;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.alipay.api.response.AlipayTradeWapPayResponse;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mdd.common.config.AlipayConfig;
import com.mdd.common.entity.RechargeOrder;
import com.mdd.common.entity.course.CourseClass;
import com.mdd.common.entity.course.UserCourse;
import com.mdd.common.entity.coupon.CouponGoods;
import com.mdd.common.entity.setting.DevPayConfig;
import com.mdd.common.entity.setting.DevPayWay;
import com.mdd.common.entity.student.Student;
import com.mdd.common.entity.user.User;
import com.mdd.common.entity.user.UserAuth;
import com.mdd.common.enums.AccountLogEnum;
import com.mdd.common.enums.ClientEnum;
import com.mdd.common.enums.PaymentEnum;
import com.mdd.common.enums.YesNoEnum;
import com.mdd.common.exception.BaseException;
import com.mdd.common.exception.OperateException;
import com.mdd.common.exception.PaymentException;
import com.mdd.common.mapper.RechargeOrderMapper;
import com.mdd.common.mapper.coupon.CouponGoodsMapper;
import com.mdd.common.mapper.course.CourseClassMapper;
import com.mdd.common.mapper.course.UserCourseMapper;
import com.mdd.common.mapper.log.UserAccountLogMapper;
import com.mdd.common.mapper.setting.DevPayConfigMapper;
import com.mdd.common.mapper.setting.DevPayWayMapper;
import com.mdd.common.mapper.student.StudentMapper;
import com.mdd.common.mapper.user.UserAuthMapper;
import com.mdd.common.mapper.user.UserMapper;
import com.mdd.common.plugin.wechat.WxMnpDriver;
import com.mdd.common.plugin.wechat.WxPayDriver;
import com.mdd.common.plugin.wechat.request.PaymentRequestV3;
import com.mdd.common.util.AmountUtil;
import com.mdd.common.util.ConfigUtils;
import com.mdd.common.util.MessageUtil;
import com.mdd.common.util.SensitiveUtils;
import org.springframework.dao.DuplicateKeyException;
import com.mdd.common.util.StringUtils;
import com.mdd.common.util.TimeUtils;
import com.mdd.common.util.UrlUtils;
import com.mdd.common.util.YmlUtils;
import com.mdd.front.service.IActivityService;
import com.mdd.front.service.ICouponPurchaseService;
import com.mdd.front.service.IPayService;
import com.mdd.front.validate.PaymentValidate;
import com.mdd.front.vo.pay.PayStatusVo;
import com.mdd.front.vo.pay.PayWayInfoVo;
import com.mdd.front.vo.pay.PayWayListVo;
import java.io.Serializable;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.Resource;
import me.chanjar.weixin.common.bean.oauth2.WxOAuth2AccessToken;
import me.chanjar.weixin.mp.api.WxMpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.Assert;

@Service
public class PayServiceImpl
implements IPayService {
    private static final Logger log = LoggerFactory.getLogger(PayServiceImpl.class);
    @Resource
    UserMapper userMapper;
    @Resource
    UserAuthMapper userAuthMapper;
    @Resource
    DevPayWayMapper devPayWayMapper;
    @Resource
    DevPayConfigMapper devPayConfigMapper;
    @Resource
    RechargeOrderMapper rechargeOrderMapper;
    @Resource
    UserAccountLogMapper logMoneyMapper;
    @Resource
    UserCourseMapper userCourseMapper;
    @Resource
    CourseClassMapper courseClassMapper;
    @Resource
    StudentMapper studentMapper;
    @Resource
    CouponGoodsMapper couponGoodsMapper;
    @Resource
    ICouponPurchaseService iCouponPurchaseService;
    @Resource
    IActivityService iActivityService;
    @Resource
    TransactionTemplate transactionTemplate;
    @Value(value="${ali.pay.order-notify-url}")
    private String aliPayNotifyUrl;
    private static final int WECHAT_PAY_DESCRIPTION_MAX_BYTES = 127;

    public PayWayListVo payWay(String from, Integer orderId, Integer terminal, Integer userId) {
        List<DevPayWay> devPayWays = this.devPayWayMapper.selectList(new QueryWrapper<DevPayWay>().eq("scene", (Object)terminal).eq("status", (Object)YesNoEnum.YES.getCode()).orderByDesc("id"));
        PayWayListVo vo = new PayWayListVo();
        if (from.equals("recharge") || from.equals("course") || from.equals("coupon") || from.equals("activity")) {
            RechargeOrder order = this.requireOwnOrder(orderId, userId);
            vo.setOrderAmount(order.getOrderAmount());
        }
        Integer walletType = PaymentEnum.WALLET_PAY.getCode();
        LinkedList<PayWayInfoVo> list = new LinkedList<PayWayInfoVo>();
        for (DevPayWay way : devPayWays) {
            if (from.equals("recharge") && way.getPayConfigId().equals(walletType)) continue;
            DevPayConfig devPayConfig = (DevPayConfig)this.devPayConfigMapper.selectById((Serializable)way.getPayConfigId());
            PayWayInfoVo infoVo = new PayWayInfoVo();
            infoVo.setId(devPayConfig.getId());
            infoVo.setName(devPayConfig.getName());
            infoVo.setIcon(UrlUtils.toAbsoluteUrl((String)devPayConfig.getIcon()));
            infoVo.setIsDefault(way.getIsDefault());
            infoVo.setSort(devPayConfig.getSort());
            infoVo.setRemark(devPayConfig.getRemark());
            infoVo.setPayWay(devPayConfig.getPayWay());
            if (devPayConfig.getPayWay().equals(PaymentEnum.WX_PAY.getCode())) {
                infoVo.setExtra("\u5fae\u4fe1\u5feb\u6377\u652f\u4ed8");
            }
            if (devPayConfig.getPayWay().equals(PaymentEnum.ALI_PAY.getCode())) {
                infoVo.setExtra("\u652f\u4ed8\u5b9d\u5feb\u6377\u652f\u4ed8");
            }
            list.add(infoVo);
        }
        Collections.sort(list, Comparator.comparing(PayWayInfoVo::getSort).reversed().thenComparing(Comparator.comparingInt(PayWayInfoVo::getId).reversed()));
        vo.setLists(list);
        return vo;
    }

    public PayStatusVo payStatus(String from, Integer orderId, Integer userId) {
        PayStatusVo vo = new PayStatusVo();
        boolean orderExist = false;
        switch (from) {
            case "recharge": 
            case "course": 
            case "coupon": 
            case "activity": {
                RechargeOrder order = this.requireOwnOrder(orderId, userId);
                orderExist = true;
                vo.setPayStatus(order.getPayStatus());
                vo.setOrderId(order.getId());
                JSONObject orderObj = new JSONObject();
                orderObj.put("order_amount", (Object)order.getOrderAmount());
                orderObj.put("order_sn", (Object)order.getSn());
                orderObj.put("pay_time", (Object)(StringUtils.isNotNull(order.getPayTime()) ? TimeUtils.timestampToDate((Long)order.getPayTime()) : ""));
                orderObj.put("pay_way", (Object)PaymentEnum.getPayWayMsg((Integer)order.getPayWay()));
                vo.setOrder(orderObj);
            }
        }
        if (!orderExist) {
            throw new OperateException("\u8ba2\u5355\u4e0d\u5b58\u5728!");
        }
        return vo;
    }

    /**
     * 订单归属校验：小程序端只允许查看/操作当前登录账号自己的订单。
     * 查不到与他人订单统一返回“订单不存在”，避免按 id 遍历泄露订单金额/流水号。
     */
    private RechargeOrder requireOwnOrder(Integer orderId, Integer userId) {
        RechargeOrder order = orderId == null || orderId <= 0
                ? null : (RechargeOrder)this.rechargeOrderMapper.selectById((Serializable)orderId);
        if (order == null || order.getDeleteTime() != null
                || userId == null || !userId.equals(order.getUserId())) {
            throw new OperateException("\u8ba2\u5355\u4e0d\u5b58\u5728!");
        }
        return order;
    }

    public Object prepay(PaymentValidate params, Integer terminal, String code) {
        try {
            params.setTerminal(terminal);
            String openId = null;
            UserAuth userAuth = (UserAuth)this.userAuthMapper.selectOne(new QueryWrapper<UserAuth>().eq("user_id", (Object)params.getUserId()).last("limit 1"));
            if (StringUtils.isNotNull(userAuth)) {
                openId = userAuth.getOpenid();
            } else if (terminal.intValue() != ClientEnum.PC.getCode() && StringUtils.isNotEmpty((String)code)) {
                if (ClientEnum.OA.getCode() == terminal.intValue()) {
                    WxMpService wxMpService = WxMnpDriver.oa();
                    WxOAuth2AccessToken wxOAuth2AccessToken = wxMpService.getOAuth2Service().getAccessToken(code);
                    openId = wxOAuth2AccessToken.getOpenId();
                } else if (ClientEnum.MNP.getCode() == terminal.intValue()) {
                    WxMaService wxMaService = WxMnpDriver.mnp();
                    WxMaJscode2SessionResult sessionResult = wxMaService.getUserService().getSessionInfo(code);
                    openId = sessionResult.getOpenid();
                }
            }
            switch (params.getPayWay()) {
                case 1: {
                    User user = (User)this.userMapper.selectById((Serializable)params.getUserId());
                    if (user == null) {
                        throw new PaymentException("\u7528\u6237\u4e0d\u5b58\u5728");
                    }
                    if (user.getUserMoney() == null || user.getUserMoney().compareTo(params.getOrderAmount()) < 0) {
                        throw new PaymentException("\u4f59\u989d\u4e0d\u8db3\uff0c\u8bf7\u5148\u5145\u503c");
                    }
                    String attach = params.getAttach();
                    String orderSn = params.getOutTradeNo();
                    // #59：钱包扣减在回调内改为条件 UPDATE。这里把“订单置已支付 + 扣余额 + 发课/发券”
                    // 收进同一个事务：并发抢不到余额时整体回滚，订单保持待支付，充值后可重试。
                    this.transactionTemplate.executeWithoutResult(status -> {
                        try {
                            this.handlePaidNotify(attach, orderSn, null);
                        } catch (WxPayException e) {
                            throw new PaymentException("\u652f\u4ed8\u56de\u8c03\u5904\u7406\u5931\u8d25");
                        }
                    });
                    return Collections.emptyList();
                }
                case 2: {
                    PaymentRequestV3 requestV3 = new PaymentRequestV3();
                    requestV3.setTerminal(terminal);
                    requestV3.setOpenId(openId);
                    requestV3.setAttach(params.getAttach());
                    requestV3.setOutTradeNo(params.getOutTradeNo());
                    requestV3.setOrderAmount(params.getOrderAmount());
                    requestV3.setDescription(params.getDescription());
                    if (terminal.intValue() == ClientEnum.H5.getCode()) {
                        Assert.notNull((Object)params.getRedirect(), (String)"redirectUrl\u53c2\u6570\u7f3a\u5931");
                        JSONObject ret = new JSONObject();
                        ret.put("config", WxPayDriver.unifiedOrder((PaymentRequestV3)requestV3));
                        ret.put("pay_way", (Object)2);
                        return ret;
                    }
                    JSONObject ret = new JSONObject();
                    JSONObject config = JSONObject.parseObject((String)JSONObject.toJSONString((Object)WxPayDriver.unifiedOrder((PaymentRequestV3)requestV3), (JSONWriter.Feature[])new JSONWriter.Feature[0]));
                    config.put("package", (Object)config.getString("packageValue"));
                    config.remove((Object)"packageValue");
                    ret.put("config", (Object)config);
                    ret.put("pay_way", (Object)2);
                    return ret;
                }
                case 3: {
                    return this.createAliH5Order(params);
                }
            }
        }
        catch (WxPayException e) {
            // SDK 原始请求/响应日志已在 log4j2 关闭；这里只记录微信错误码，避免 openid/商户配置进日志。
            log.warn("微信支付下单失败 errCode={} errCodeDes={}", e.getErrCode(), e.getErrCodeDes());
            throw new PaymentException("支付下单失败，请检查微信支付配置");
        }
        catch (BaseException e) {
            // 业务异常（如余额不足）原样上抛，避免被包成实现细节后脱敏成“系统繁忙”。
            throw e;
        }
        catch (Exception e) {
            // 非业务异常（NPE/SQL 等）的 message 可能含表名、类名等内部细节，
            // 全栈已落日志供排查，客户端只给统一文案，不透传实现信息。
            log.error("支付发起异常", e);
            throw new OperateException("支付发起异常，请稍后重试");
        }
        throw new PaymentException("\u652f\u4ed8\u53d1\u8d77\u5f02\u5e38");
    }

    @Override
    @Transactional
    public void handlePaidNotify(String attach, String outTradeNo, String transactionId) throws WxPayException {
        this.handlePaidNotify(attach, outTradeNo, transactionId, null);
    }

    @Override
    @Transactional
    public void handlePaidNotify(String attach, String outTradeNo, String transactionId,
                                 Integer paidAmountFen) throws WxPayException {
        this.verifyPaidAmount(outTradeNo, paidAmountFen);
        if (attach == null) {
            log.warn("支付回调缺少 attach，忽略 orderSn={}", SensitiveUtils.maskOrderSn(outTradeNo));
            return;
        }
        switch (attach) {
            case "order": {
                break;
            }
            case "recharge": {
                this.rechargeCallback(outTradeNo, transactionId);
                break;
            }
            case "course": {
                this.courseCallback(outTradeNo, transactionId);
                break;
            }
            case "coupon": {
                // T29.4/#103 次数券：按张发券，幂等由 la_coupon_instance.uk_order_seq(order_id, order_seq) 保证。
                this.iCouponPurchaseService.issueForPaidOrder(outTradeNo, transactionId);
                break;
            }
            case "activity": {
                // T29.7 活动报名：回调把待支付报名明细置为已支付，重复回调幂等。
                this.iActivityService.issueForPaidOrder(outTradeNo, transactionId);
                break;
            }
            default: {
                log.warn("未知支付回调 attach，忽略 attach={} orderSn={}",
                        attach, SensitiveUtils.maskOrderSn(outTradeNo));
            }
        }
    }

    /**
     * 真实回调的金额校验：微信金额单位是分，订单金额是元。
     * 金额不一致时拒绝发券/记账，让微信重试并暴露配置或篡改问题。
     */
    private void verifyPaidAmount(String outTradeNo, Integer paidAmountFen) {
        if (paidAmountFen == null) {
            return;
        }
        RechargeOrder order = this.rechargeOrderMapper.selectOne(new QueryWrapper<RechargeOrder>()
                .eq("sn", outTradeNo).isNull("delete_time").last("limit 1"));
        if (order == null) {
            log.error("支付金额校验失败：订单不存在 orderSn={}", SensitiveUtils.maskOrderSn(outTradeNo));
            throw new PaymentException("支付订单不存在");
        }
        if (order.getOrderAmount() == null) {
            log.error("支付金额校验失败：订单金额为空 orderSn={}", SensitiveUtils.maskOrderSn(outTradeNo));
            throw new PaymentException("支付金额校验失败");
        }
        int expectedFen = AmountUtil.yuan2Fen(order.getOrderAmount().toPlainString());
        if (expectedFen != paidAmountFen.intValue()) {
            log.error("支付金额校验失败 orderSn={} expectedFen={} paidFen={}",
                    SensitiveUtils.maskOrderSn(outTradeNo),
                    SensitiveUtils.maskAmount(expectedFen),
                    SensitiveUtils.maskAmount(paidAmountFen));
            throw new PaymentException("支付金额校验失败");
        }
    }

    public JSONObject createAliH5Order(PaymentValidate params) throws AlipayApiException {
        JSONObject ret = new JSONObject();
        String orderSn = "";
        Integer orderStatus = 0;
        BigDecimal orderAmount = BigDecimal.ZERO;
        String payReturnUrl = "";
        switch (params.getScene()) {
            case "member": {
                break;
            }
            case "recharge": 
            case "course": 
            case "coupon": 
            case "activity": {
                RechargeOrder order = (RechargeOrder)this.rechargeOrderMapper.selectOne(new QueryWrapper<RechargeOrder>().eq("sn", (Object)params.getOutTradeNo()).eq("pay_status", (Object)PaymentEnum.UN_PAID.getCode()).eq("user_id", (Object)params.getUserId()).last("limit 1"));
                Assert.notNull((Object)order, (String)"\u8ba2\u5355\u4e0d\u5b58\u5728");
                orderSn = order.getSn();
                orderStatus = order.getPayStatus();
                orderAmount = order.getOrderAmount();
                payReturnUrl = UrlUtils.localDomain((String)params.getRedirect());
                break;
            }
            default: {
                throw new OperateException("\u4e0d\u652f\u6301\u5f53\u524d\u8ba2\u5355\u7c7b\u578b");
            }
        }
        Assert.isTrue((orderStatus != null && orderStatus.intValue() == PaymentEnum.UN_PAID.getCode() ? 1 : 0) != 0, (String)"\u8ba2\u5355\u975e\u5f85\u652f\u4ed8\u72b6\u6001,\u4e0d\u80fd\u521b\u5efa\u652f\u4ed8\u5355");
        String gateWay = StringUtils.isNotNull(YmlUtils.get((String)"like.alidebug")) && YmlUtils.get((String)"like.alidebug").equals("true") ? AlipayConfig.GATEWAY_URL_DEBUG : AlipayConfig.GATEWAY_URL;
        DefaultAlipayClient alipayClient = new DefaultAlipayClient(gateWay, ConfigUtils.getAliDevPay((String)"app_id"), ConfigUtils.getAliDevPay((String)"private_key"), "json", AlipayConfig.CHARSET, ConfigUtils.getAliDevPay((String)"ali_public_key"), AlipayConfig.SIGN_TYPE);
        String form = "";
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", (Object)orderSn);
        bizContent.put("total_amount", (Object)orderAmount);
        bizContent.put("subject", (Object)params.getDescription());
        bizContent.put("product_code", (Object)"FAST_INSTANT_TRADE_PAY");
        bizContent.put("app_pay", (Object)"Y");
        bizContent.put("passback_params", (Object)JSONObject.toJSONString(new JSONObject(){
            {
                this.put("from", params.getScene());
            }
        }, (JSONWriter.Feature[])new JSONWriter.Feature[0]));
        if (params.getTerminal().equals(ClientEnum.H5.getCode()) || params.getTerminal().equals(ClientEnum.OA.getCode())) {
            AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();
            alipayRequest.setNotifyUrl(UrlUtils.getRequestUrl() + this.aliPayNotifyUrl);
            alipayRequest.setReturnUrl(payReturnUrl);
            alipayRequest.setBizContent(bizContent.toString());
            try {
                form = ((AlipayTradeWapPayResponse)alipayClient.pageExecute((AlipayRequest)alipayRequest)).getBody();
                ret.put("config", (Object)form);
                ret.put("pay_way", (Object)params.getPayWay());
                return ret;
            }
            catch (Exception e) {
                throw new PaymentException(e.getMessage());
            }
        }
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setNotifyUrl(UrlUtils.getRequestUrl() + this.aliPayNotifyUrl);
        alipayRequest.setReturnUrl(payReturnUrl);
        alipayRequest.setBizContent(bizContent.toString());
        try {
            form = ((AlipayTradePagePayResponse)alipayClient.pageExecute((AlipayRequest)alipayRequest)).getBody();
            ret.put("config", (Object)form);
            ret.put("pay_way", (Object)params.getPayWay());
            return ret;
        }
        catch (Exception e) {
            throw new OperateException(e.getMessage());
        }
    }

    private void rechargeCallback(String outTradeNo, String transactionId) {
        for (int i = 0; i <= 0; ++i) {
            RechargeOrder rechargeOrder = (RechargeOrder)this.rechargeOrderMapper.selectOne(new QueryWrapper<RechargeOrder>().eq("sn", (Object)outTradeNo).isNull("delete_time").last("limit 1"));
            if (StringUtils.isNull(rechargeOrder)) {
                log.error("\u5145\u503c\u8ba2\u5355\u4e0d\u5b58\u5728: {} : {}", (Object)outTradeNo, (Object)transactionId);
                break;
            }
            // 并发/重复回调幂等：只有把订单从“未支付”原子改成“已支付”的那一次回调才能真正入账，
            // 否则微信重复通知并发到达时会把同一笔充值记两次余额。
            long now = System.currentTimeMillis() / 1000L;
            int claimed = this.rechargeOrderMapper.update(null, new com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper<RechargeOrder>()
                    .set("pay_status", PaymentEnum.OK_PAID.getCode())
                    .set("transaction_id", transactionId)
                    .set("pay_time", now)
                    .set("update_time", now)
                    .eq("id", rechargeOrder.getId())
                    .ne("pay_status", PaymentEnum.OK_PAID.getCode()));
            if (claimed <= 0) {
                log.error("\u5145\u503c\u8ba2\u5355\u5df2\u652f\u4ed8(\u5e42\u7b49\u5ffd\u7565): {} : {}", (Object)outTradeNo, (Object)transactionId);
                break;
            }
            this.logMoneyMapper.add(rechargeOrder.getUserId(), Integer.valueOf(AccountLogEnum.UM_INC_RECHARGE.getCode()), rechargeOrder.getOrderAmount(), rechargeOrder.getId(), rechargeOrder.getSn(), "\u7528\u6237\u5145\u503c\u4f59\u989d", null);
            // #150：入账改用原子 addMoney（user_money = user_money + ?），不再查出来加好再整行写回；
            // 与钱包支付/购券的原子扣款（#59）并发时由数据库行锁累加，不会互相覆盖丢更新。
            int added = this.userMapper.addMoney(rechargeOrder.getUserId(), rechargeOrder.getOrderAmount(), Long.valueOf(System.currentTimeMillis() / 1000L));
            if (added <= 0) {
                // 用户不存在/已删除：抛异常回滚整个回调事务（含订单置已付与流水），由支付渠道重试。
                throw new OperateException("\u7528\u6237\u4e0d\u5b58\u5728");
            }
        }
    }

    private void courseCallback(String outTradeNo, String transactionId) {
        CourseClass cls;
        RechargeOrder order = (RechargeOrder)this.rechargeOrderMapper.selectOne(new QueryWrapper<RechargeOrder>().eq("sn", (Object)outTradeNo).isNull("delete_time").last("limit 1"));
        if (StringUtils.isNull(order)) {
            log.error("\u8bfe\u7a0b\u8ba2\u5355\u4e0d\u5b58\u5728: {} : {}", (Object)outTradeNo, (Object)transactionId);
            return;
        }
        if (order.getPayStatus() != null && order.getPayStatus().equals(PaymentEnum.OK_PAID.getCode())) {
            // #153：重复回调不再无条件短路。订单已付但缺报名记录（历史吞异常产物）在此补偿发放，
            // 使「已付款必有课」可被支付渠道重试触达；已有记录则真正幂等成功。
            log.warn("\u8bfe\u7a0b\u8ba2\u5355\u5df2\u652f\u4ed8(\u91cd\u590d\u56de\u8c03\uff0c\u6821\u9a8c\u53d1\u8bfe\u7ed3\u679c): {} : {}", (Object)outTradeNo, (Object)transactionId);
            this.grantUserCourseForOrder(order, outTradeNo, transactionId);
            return;
        }
        // 并发/重复回调幂等：只有成功把订单从“未支付”改成“已支付”的回调才发课/扣余额，
        // 否则同一笔支付被重复通知会重复发放课时或重复扣减钱包。
        // #154：取消（refund_status=1）与回调并发时，回调也不得再抢占已取消的未支付单，
        // 否则会出现「已取消又被支付」的矛盾态（钱包被扣/课程发放，占用券可能已被取消退回）。
        long paidAt = System.currentTimeMillis() / 1000L;
        int claimed = this.rechargeOrderMapper.update(null, new com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper<RechargeOrder>()
                .set("pay_status", PaymentEnum.OK_PAID.getCode())
                .set("transaction_id", transactionId)
                .set("pay_time", paidAt)
                .set("update_time", paidAt)
                .eq("id", order.getId())
                .ne("pay_status", PaymentEnum.OK_PAID.getCode())
                .ne("refund_status", 1));
        if (claimed <= 0) {
            // #154：抢占失败若因订单已取消（未支付即被取消），回调直接忽略，不发课不扣款。
            // 处置约定：真实支付渠道对已取消订单的回调这里仅 warn 不自动退款——钱停在渠道侧，
            // 由运营侧对账处理（如需退款走人工/渠道后台），不要误以为是遗漏。
            // 对账口径：扫描「pay_status=1 且无对应 la_user_course」的订单（与 #153 补偿扫描同口径），
            // 已取消未付单本分支不会置 pay_status=1，故不在其中；漏发课则会被该扫描兜住。
            RechargeOrder latest = (RechargeOrder)this.rechargeOrderMapper.selectById((Serializable)order.getId());
            if (latest != null && latest.getRefundStatus() != null && latest.getRefundStatus() == 1) {
                log.warn("\u8bfe\u7a0b\u8ba2\u5355\u5df2\u53d6\u6d88\uff0c\u5ffd\u7565\u652f\u4ed8\u56de\u8c03: {} : {}", (Object)outTradeNo, (Object)transactionId);
                return;
            }
            // #153：抢占失败（并发或重复回调）也不再无条件短路，同样校验并补偿发课。
            log.warn("\u8bfe\u7a0b\u8ba2\u5355\u62a2\u5360\u5931\u8d25(\u91cd\u590d\u56de\u8c03\uff0c\u6821\u9a8c\u53d1\u8bfe\u7ed3\u679c): {} : {}", (Object)outTradeNo, (Object)transactionId);
            this.grantUserCourseForOrder(order, outTradeNo, transactionId);
            return;
        }
        order.setPayStatus(Integer.valueOf(PaymentEnum.OK_PAID.getCode()));
        order.setTransactionId(transactionId);
        order.setPayTime(Long.valueOf(paidAt));
        order.setUpdateTime(Long.valueOf(paidAt));
        if (order.getPayWay() != null && order.getPayWay().equals(PaymentEnum.WALLET_PAY.getCode())) {
            // #59：余额判断与扣减由条件 UPDATE 原子完成，并发下不会把余额扣成负数、也不会丢失扣减。
            int deducted = this.userMapper.deductMoney(order.getUserId(), order.getOrderAmount(), Long.valueOf(paidAt));
            if (deducted <= 0) {
                if (this.userMapper.selectById((Serializable)order.getUserId()) == null) {
                    throw new OperateException("\u7528\u6237\u4e0d\u5b58\u5728");
                }
                throw new PaymentException("\u4f59\u989d\u4e0d\u8db3\uff0c\u8bf7\u5148\u5145\u503c");
            }
            this.logMoneyMapper.add(order.getUserId(), Integer.valueOf(AccountLogEnum.UM_DEC_COURSE_PAY.getCode()), order.getOrderAmount(), order.getId(), order.getSn(), "\u8bfe\u7a0b\u652f\u4ed8\u6263\u6b3e", null);
        }
        // #153：发课不再吞异常。la_user_course 插入失败会抛出，使回调事务整体回滚
        // （订单抢占、钱包扣款、流水一并回滚，订单回到未支付），notifyMnp 返回 500
        // 让微信按策略重试；重试时订单仍是未支付，可重新抢占发课，达成「已付款必有课」。
        this.grantUserCourseForOrder(order, outTradeNo, transactionId);
        String courseName = "\u8bfe\u7a0b";
        if (order.getCourseClassId() != null && (cls = (CourseClass)this.courseClassMapper.selectById((Serializable)order.getCourseClassId())) != null) {
            courseName = cls.getName();
        }
        MessageUtil.sendPaymentSuccess((Integer)order.getUserId(), (String)courseName, (String)order.getSn());
        log.info("\u8bfe\u7a0b\u8ba2\u5355\u652f\u4ed8\u6210\u529f: {} : {}", (Object)outTradeNo, (Object)transactionId);
    }

    /**
     * #153 幂等发课：同单已有 la_user_course 报名记录则跳过，否则插入一条。
     *
     * <p>两个调用方：</p>
     * <ul>
     *   <li>抢占成功后的正常发课——插入失败异常上抛，回调事务整体回滚，由支付渠道重试；</li>
     *   <li>重复回调的补偿路径——订单已付但记录缺失（历史吞异常产物）时补发，
     *       使「已付款必有课」可被重试触达。</li>
     * </ul>
     *
     * <p>同单唯一性由 select-then-insert + V024 {@code uk_order_course}（生成列守卫
     * order_id&gt;0 且未软删）双重保证：并发插入撞键时 {@link DuplicateKeyException}
     * 视为「已被并发回调发放」，不算失败；其余异常一律上抛回滚重试。</p>
     */
    private void grantUserCourseForOrder(RechargeOrder order, String outTradeNo, String transactionId) {
        Long existingUserCourse = this.userCourseMapper.selectCount(new QueryWrapper<UserCourse>()
                .eq("order_id", (Object)order.getId()));
        if (existingUserCourse != null && existingUserCourse > 0L) {
            log.info("\u8bfe\u7a0b\u62a5\u540d\u8bb0\u5f55\u5df2\u5b58\u5728\uff0c\u8df3\u8fc7\u53d1\u653e(\u5e42\u7b49): orderId={} orderSn={}",
                    (Object)order.getId(), (Object)SensitiveUtils.maskOrderSn(outTradeNo));
            return;
        }
        UserCourse userCourse = new UserCourse();
        userCourse.setUserId(order.getUserId());
        userCourse.setCourseClassId(order.getCourseClassId());
        userCourse.setOrderId(order.getId());
        userCourse.setStudentId(order.getStudentId());
        userCourse.setStatus(Integer.valueOf(0));
        Integer courseTotalLessons = 0;
        if (order.getLessonCount() != null && order.getLessonCount() > 0) {
            courseTotalLessons = order.getLessonCount();
        } else if (order.getCourseClassId() != null) {
            CourseClass courseClass = (CourseClass)this.courseClassMapper.selectById((Serializable)order.getCourseClassId());
            if (courseClass != null && courseClass.getTotalLessons() != null) {
                courseTotalLessons = courseClass.getTotalLessons();
            }
        }
        userCourse.setTotalLessons(courseTotalLessons);
        userCourse.setRemainLessons(courseTotalLessons);
        userCourse.setUsedLessons(Integer.valueOf(0));
        userCourse.setCreateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
        userCourse.setUpdateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
        try {
            this.userCourseMapper.insert(userCourse);
            log.info("\u53d1\u653e\u8bfe\u7a0b\u62a5\u540d\u8bb0\u5f55: orderId={} orderSn={} userId={} classId={} lessons={}",
                    (Object)order.getId(), (Object)SensitiveUtils.maskOrderSn(outTradeNo),
                    (Object)order.getUserId(), (Object)order.getCourseClassId(), (Object)courseTotalLessons);
        }
        catch (DuplicateKeyException e) {
            // V024 唯一键兜底：并发回调刚好已插入同单记录（未提交的行锁等待后撞键），
            // 记录已存在即发放成功；不允许其它异常在这里被吞。
            log.warn("\u8bfe\u7a0b\u62a5\u540d\u8bb0\u5f55\u5e76\u53d1\u63d2\u5165\u51b2\u7a81(\u540c\u5355\u8bb0\u5f55\u5df2\u5b58\u5728\uff0c\u89c6\u4e3a\u5df2\u53d1\u653e): orderId={} orderSn={}",
                    (Object)order.getId(), (Object)SensitiveUtils.maskOrderSn(outTradeNo));
        }
    }

    public String buildCoursePayDescription(RechargeOrder order) {
        String result;
        Student student;
        CourseClass courseClass;
        if (order == null) {
            return "\u8bfe\u7a0b\u652f\u4ed8";
        }
        String className = "\u8bfe\u7a0b";
        if (order.getCourseClassId() != null && order.getCourseClassId() > 0 && (courseClass = (CourseClass)this.courseClassMapper.selectById((Serializable)order.getCourseClassId())) != null && StringUtils.isNotEmpty((String)courseClass.getName())) {
            className = courseClass.getName().trim();
        }
        String studentName = "\u5b66\u5458";
        if (order.getStudentId() != null && order.getStudentId() > 0 && (student = (Student)this.studentMapper.selectById((Serializable)order.getStudentId())) != null && StringUtils.isNotEmpty((String)student.getName())) {
            studentName = student.getName().trim();
        }
        StringBuilder description = new StringBuilder();
        description.append(className).append("-").append(studentName);
        if (order.getLessonCount() != null && order.getLessonCount() > 0) {
            description.append("-").append(order.getLessonCount()).append("\u8bfe\u65f6");
        }
        if (StringUtils.isEmpty((String)(result = this.truncateUtf8ByBytes(description.toString(), 127)))) {
            return "\u8bfe\u7a0b\u652f\u4ed8";
        }
        return result;
    }

    @Override
    public String buildCouponPayDescription(RechargeOrder order) {
        if (order == null) {
            return "课程券支付";
        }
        String goodsTitle = "课程券";
        if (order.getCouponGoodsId() != null && order.getCouponGoodsId() > 0) {
            CouponGoods goods = (CouponGoods)this.couponGoodsMapper.selectById((Serializable)order.getCouponGoodsId());
            if (goods != null && StringUtils.isNotEmpty((String)goods.getTitle())) {
                goodsTitle = goods.getTitle().trim();
            }
        }
        String studentName = "学员";
        if (order.getStudentId() != null && order.getStudentId() > 0) {
            Student student = (Student)this.studentMapper.selectById((Serializable)order.getStudentId());
            if (student != null && StringUtils.isNotEmpty((String)student.getName())) {
                studentName = student.getName().trim();
            }
        }
        String result = this.truncateUtf8ByBytes(goodsTitle + "-" + studentName,
                WECHAT_PAY_DESCRIPTION_MAX_BYTES);
        return StringUtils.isEmpty((String)result) ? "课程券支付" : result;
    }

    @Override
    public String buildActivityPayDescription(RechargeOrder order) {
        return this.iActivityService.payDescription(order);
    }

    private String truncateUtf8ByBytes(String text, int maxBytes) {
        if (StringUtils.isEmpty((String)text) || maxBytes <= 0) {
            return text;
        }
        if (text.getBytes(StandardCharsets.UTF_8).length <= maxBytes) {
            return text;
        }
        for (int end = text.length(); end > 0; --end) {
            String candidate = text.substring(0, end);
            if (candidate.getBytes(StandardCharsets.UTF_8).length > maxBytes) continue;
            return candidate;
        }
        return "";
    }
}

