package com.mdd.front.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mdd.common.entity.RechargeOrder;
import com.mdd.common.entity.coupon.CouponFlow;
import com.mdd.common.entity.coupon.CouponGoods;
import com.mdd.common.entity.coupon.CouponInstance;
import com.mdd.common.entity.student.Student;
import com.mdd.common.enums.AccountLogEnum;
import com.mdd.common.enums.PaymentEnum;
import com.mdd.common.exception.OperateException;
import com.mdd.common.mapper.RechargeOrderMapper;
import com.mdd.common.mapper.coupon.CouponFlowMapper;
import com.mdd.common.mapper.coupon.CouponGoodsMapper;
import com.mdd.common.mapper.coupon.CouponInstanceMapper;
import com.mdd.common.mapper.log.UserAccountLogMapper;
import com.mdd.common.mapper.student.StudentMapper;
import com.mdd.common.mapper.user.UserMapper;
import com.mdd.common.util.StringUtils;
import com.mdd.front.service.ICouponPurchaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 次数券购买 / 支付回调发券实现（T29.4 / #103）。
 *
 * <p>发券口径（决策清单 C1-A）：一个订单按商品次数 N 发 N 张独立券
 * （每张 total=1 / remaining=1，order_seq=1..N），用户端天然看到 N 张卡；
 * N=1 与免费券同样走这条路径。{@code like.coupon.pack-as-single=true}
 * 是仅用于应急回滚的开关，打开后恢复旧口径（1 行 total=N，order_seq=0）。</p>
 *
 * <p>幂等设计：</p>
 * <ol>
 *   <li>{@code la_recharge_order.coupon_goods_id} 区分券订单，回调只处理券订单；</li>
 *   <li>{@code uk_order_seq(order_id, order_seq)}：同一订单的同一张券最多一条实例，
 *       N 张券分片写入，并发重复回调只可能成功一次；</li>
 *   <li>回调查到该订单已有 N 条实例直接返回，不再写流水；并发下唯一键冲突也按幂等处理；</li>
 *   <li>订单 pay_status 已支付时不重复扣余额、不重复发券。</li>
 * </ol>
 */
@Service
public class CouponPurchaseServiceImpl implements ICouponPurchaseService {

    private static final Logger log = LoggerFactory.getLogger(CouponPurchaseServiceImpl.class);

    private static final DateTimeFormatter SN_FMT = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    private static final int FLOW_GRANT = 1;

    @Resource
    CouponGoodsMapper couponGoodsMapper;
    @Resource
    CouponInstanceMapper couponInstanceMapper;
    @Resource
    CouponFlowMapper couponFlowMapper;
    @Resource
    StudentMapper studentMapper;
    @Resource
    RechargeOrderMapper rechargeOrderMapper;
    @Resource
    UserMapper userMapper;
    @Resource
    UserAccountLogMapper userAccountLogMapper;

    /** 本地/测试可打开模拟支付回调；生产必须 false（见 front.application.yml.template）。 */
    @Value("${like.pay.mock-callback-enabled:false}")
    private boolean mockCallbackEnabled;

    /**
     * 应急回滚开关（默认 false = 按张发放）：
     * true 时恢复旧口径「1 条实例 total=N、order_seq=0」，仅用于上线后需要回退到 V021 之前模型的极端场景。
     */
    @Value("${like.coupon.pack-as-single:false}")
    private boolean packAsSingle;

    @Override
    public boolean isMockCallbackEnabled() {
        return mockCallbackEnabled;
    }

    // ---- 下单 --------------------------------------------------------------

    // #154：限购判定是 check-then-act（checkPurchaseLimit 先 count 后 insert），且依赖
    // 「能看到其他并发请求已提交的券实例/订单」。MySQL 默认 REPEATABLE READ 下事务快照在
    // 首次读时建立，排队的请求即便等到了商品行锁，count 仍读旧快照、判定失真。
    // 这里显式降为 READ_COMMITTED（仅本事务），使行锁等待结束后的 count 读到最新已提交数据。
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Map<String, Object> placeOrder(Integer userId, Integer terminal, String productId, String studentId) {
        Integer goodsId = toInt(productId);
        if (goodsId == null || goodsId <= 0) {
            throw new OperateException("券商品不存在");
        }
        Integer studentIdValue = toInt(studentId);
        if (studentIdValue == null || studentIdValue <= 0) {
            throw new OperateException("请选择持券学员");
        }

        CouponGoods goods = couponGoodsMapper.selectById(goodsId);
        if (goods == null || goods.getDeleteTime() != null) {
            throw new OperateException("券商品不存在");
        }
        if (goods.getStatus() == null || goods.getStatus() != 1) {
            throw new OperateException("该课程券已下架");
        }

        Student student = studentMapper.selectById(studentIdValue);
        if (student == null || student.getDeleteTime() != null
                || userId == null || !userId.equals(student.getUserId())) {
            throw new OperateException("学员不存在或不属于当前账号");
        }

        checkPurchaseLimit(userId, goods, student);

        BigDecimal price = goods.getPrice() == null ? BigDecimal.ZERO : goods.getPrice();
        if (price.setScale(2, RoundingMode.HALF_UP).compareTo(BigDecimal.ZERO) <= 0) {
            // 免费券：不生成支付订单，直接按商品次数发 N 张券并逐张写流水（8 档付费商品不走这里）。
            List<CouponInstance> instances = createInstances(userId, studentIdValue, goods, null, null, "免费领取");
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("orderId", null);
            result.put("orderSn", "");
            result.put("orderAmount", BigDecimal.ZERO);
            result.put("free", true);
            result.put("couponId", instances.isEmpty() ? null : String.valueOf(instances.get(0).getId()));
            result.put("couponIds", couponIds(instances));
            result.put("couponCount", instances.size());
            result.put("mockPay", false);
            return result;
        }

        long now = now();
        RechargeOrder order = new RechargeOrder();
        order.setUserId(userId);
        order.setStudentId(studentIdValue);
        order.setCouponGoodsId(goods.getId());
        order.setOrderTerminal(terminal);
        order.setSn(rechargeOrderMapper.randMakeOrderSn("sn"));
        order.setPayStatus(PaymentEnum.UN_PAID.getCode());
        order.setRefundStatus(0);
        order.setOrderAmount(price);
        order.setCreateTime(now);
        order.setUpdateTime(now);
        rechargeOrderMapper.insert(order);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("orderId", order.getId());
        result.put("orderSn", order.getSn());
        result.put("orderAmount", order.getOrderAmount());
        result.put("free", false);
        result.put("couponId", null);
        result.put("mockPay", mockCallbackEnabled);
        return result;
    }

    private void checkPurchaseLimit(Integer userId, CouponGoods goods, Student student) {
        int perUser = goods.getPerUser() == null ? 0 : goods.getPerUser();
        int perStudent = goods.getPerStudent() == null ? 0 : goods.getPerStudent();
        if (perUser <= 0 && perStudent <= 0) {
            return;
        }

        // #154：限购是 check-then-act，并发双发（免费券双击即超限）时两个请求都能读到
        // 「还没到限购」而双双通过。收口选型：不加唯一键——种子数据的限购是份数口径
        // （per_user=3/5、per_student=1），二元唯一键只能表达「至多 1 份」，强行表达份数
        // 需要 slot 认领表（双份记账 + 对账），得不偿失；改用与回调抢占同款的原子化手段：
        // 先取商品主档单行 X 锁（无间隙锁，不会死锁）把同一商品的下单串行化，
        // 排队请求在锁等待结束后（READ_COMMITTED）能读到先到请求已提交的实例/订单，
        // count 判定不再失真。后台发放（source=grant）不走本路径，按设计不受限购约束。
        couponGoodsMapper.selectObjs(new QueryWrapper<CouponGoods>()
                .select("id")
                .eq("id", goods.getId())
                .last("FOR UPDATE"));

        // 限购口径是「份数」，不是「张数」：券包按张落库后一单 N 行，
        // 只数每单的首张（order_seq=1；旧数据/单张为 0），后台发放（order_id 为空）逐行计数。
        // #154：付费券在支付回调后才落券实例，下单时只有订单行——把「未支付且未取消的券订单」
        // 也计入占用（每单 1 份），否则并发/连续下两单都读到 0 份，支付后双双超限；
        // 订单取消（refund_status=1）即释放占用。免费券不生成订单，天然计 0。
        Long pendingOrders = rechargeOrderMapper.selectCount(new QueryWrapper<RechargeOrder>()
                .eq("user_id", userId)
                .eq("coupon_goods_id", goods.getId())
                .eq("pay_status", 0)
                .eq("refund_status", 0)
                .isNull("delete_time"));
        Long pendingOrdersByStudent = rechargeOrderMapper.selectCount(new QueryWrapper<RechargeOrder>()
                .eq("user_id", userId)
                .eq("student_id", student.getId())
                .eq("coupon_goods_id", goods.getId())
                .eq("pay_status", 0)
                .eq("refund_status", 0)
                .isNull("delete_time"));
        int pendingAll = pendingOrders == null ? 0 : pendingOrders.intValue();
        int pendingStudent = pendingOrdersByStudent == null ? 0 : pendingOrdersByStudent.intValue();

        if (perUser > 0) {
            Long owned = couponInstanceMapper.selectCount(new QueryWrapper<CouponInstance>()
                    .eq("user_id", userId)
                    .eq("goods_id", goods.getId())
                    .and(wrapper -> wrapper.isNull("order_id").or().in("order_seq", 0, 1)));
            if (owned != null && owned.intValue() + pendingAll >= perUser) {
                throw new OperateException("每账号限购 " + perUser + " 份");
            }
        }
        if (perStudent > 0) {
            Long ownedByStudent = couponInstanceMapper.selectCount(new QueryWrapper<CouponInstance>()
                    .eq("user_id", userId)
                    .eq("student_id", student.getId())
                    .eq("goods_id", goods.getId())
                    .and(wrapper -> wrapper.isNull("order_id").or().in("order_seq", 0, 1)));
            if (ownedByStudent != null && ownedByStudent.intValue() + pendingStudent >= perStudent) {
                throw new OperateException(student.getName() + " 该课程券限领 " + perStudent + " 份，已达到上限");
            }
        }
    }

    // ---- 支付回调发券 ------------------------------------------------------

    @Override
    @Transactional
    public void issueForPaidOrder(String outTradeNo, String transactionId) {
        if (StringUtils.isEmpty(outTradeNo)) {
            log.warn("券支付回调缺少订单号，忽略");
            return;
        }
        RechargeOrder order = rechargeOrderMapper.selectOne(new QueryWrapper<RechargeOrder>()
                .eq("sn", outTradeNo)
                .isNull("delete_time")
                .last("limit 1"));
        if (order == null) {
            log.warn("券支付回调订单不存在: {}", outTradeNo);
            return;
        }
        Integer goodsId = order.getCouponGoodsId();
        if (goodsId == null || goodsId <= 0) {
            log.warn("非券订单收到券回调，忽略: {}", outTradeNo);
            return;
        }

        CouponGoods goods = couponGoodsMapper.selectById(goodsId);

        // 幂等基准是「该订单应有的券张数」而不是「是否已有任意一行」：
        // 按张落库后一单 N 行，重复回调只要实例数达到 N 就直接返回，不新增、不重复写流水。
        // 商品主档缺失时 expected=0，不参与幂等判断，沿用旧口径继续把订单置为已支付再人工补券。
        int expected = expectedInstanceCount(goods);
        Long existingCount = couponInstanceMapper.selectCount(new QueryWrapper<CouponInstance>()
                .eq("order_id", order.getId()));
        if (goods != null && existingCount != null && existingCount >= expected) {
            // 重复回调：券已发齐，只补记支付状态，不再发券/写流水。
            if (order.getPayStatus() == null || order.getPayStatus() != PaymentEnum.OK_PAID.getCode()) {
                markOrderPaid(order, transactionId, now());
            }
            log.info("券重复回调幂等命中: orderSn={} couponCount={} expected={}",
                    outTradeNo, existingCount, expected);
            return;
        }
        if (goods != null && existingCount != null && existingCount > 0) {
            // 事务保证不会出现「发了一半」；真出现说明有人绕过事务/人工改过库，留审计并拒绝自动加发。
            log.error("券实例数与商品次数不一致，需人工核对: orderSn={} couponCount={} expected={}",
                    outTradeNo, existingCount, expected);
            return;
        }

        // 并发/重复回调幂等：先用条件更新把订单从“未支付”原子改成“已支付”，
        // 只有真正抢到这次状态变更的回调才扣余额；否则同一笔支付重复通知会重复扣钱。
        // #154：与课程回调同款——已取消（refund_status=1）的未支付单不得被回调抢占入账。
        long now = now();
        int claimed = rechargeOrderMapper.update(null, new com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper<RechargeOrder>()
                .set("pay_status", PaymentEnum.OK_PAID.getCode())
                .set("transaction_id", transactionId)
                .set("pay_time", now)
                .set("update_time", now)
                .eq("id", order.getId())
                .ne("pay_status", PaymentEnum.OK_PAID.getCode())
                .ne("refund_status", 1));
        if (claimed <= 0) {
            // #154：抢占失败可能是订单已被取消（未支付即取消）——不得再扣款/发券。
            RechargeOrder latest = rechargeOrderMapper.selectById(order.getId());
            if (latest != null && latest.getRefundStatus() != null && latest.getRefundStatus() == 1) {
                log.warn("券订单已取消，忽略支付回调: orderSn={}", outTradeNo);
                return;
            }
            log.info("券订单已支付，跳过余额扣减: orderSn={}", outTradeNo);
        } else if (order.getPayWay() != null
                && order.getPayWay() == PaymentEnum.WALLET_PAY.getCode()) {
            deductWallet(order, now);
        }
        if (claimed > 0) {
            order.setPayStatus(PaymentEnum.OK_PAID.getCode());
            order.setPayTime(now);
            order.setUpdateTime(now);
            if (!StringUtils.isEmpty(transactionId)) {
                order.setTransactionId(transactionId);
            }
        } else if (order.getPayTime() == null) {
            order.setPayTime(now);
            order.setUpdateTime(now);
            rechargeOrderMapper.updateById(order);
        }

        if (goods == null) {
            // 已支付但商品主档缺失：保留订单待人工补券，不能静默丢单（与改动前一致：订单已置为已支付）。
            log.error("券订单已支付但商品不存在: orderSn={} goodsId={}", outTradeNo, goodsId);
            return;
        }

        List<CouponInstance> issued;
        try {
            issued = createInstances(order.getUserId(), order.getStudentId(), goods,
                    order.getId(), order.getSn(), "支付成功发券");
        } catch (DuplicateKeyException e) {
            // 并发回调：另一条已插入同一张券，唯一键兜底，按幂等返回。
            log.info("券并发回调唯一键命中，按幂等处理: orderSn={}", outTradeNo);
            return;
        }
        if (issued.size() != expected) {
            // 发券张数与商品次数不一致必须整体回滚，不能让用户少券。
            throw new OperateException("券发放张数异常，请重试");
        }
        log.info("券发放成功: orderSn={} couponCount={} studentId={} firstCouponId={}",
                outTradeNo, issued.size(), order.getStudentId(), issued.get(0).getId());
    }

    private void markOrderPaid(RechargeOrder order, String transactionId, long now) {
        order.setPayStatus(PaymentEnum.OK_PAID.getCode());
        if (order.getPayTime() == null) {
            order.setPayTime(now);
        }
        if (!StringUtils.isEmpty(transactionId)) {
            order.setTransactionId(transactionId);
        }
        order.setUpdateTime(now);
        rechargeOrderMapper.updateById(order);
    }

    private void deductWallet(RechargeOrder order, long now) {
        // #59：余额判断与扣减由条件 UPDATE 原子完成，并发下不会把余额扣成负数或覆盖他人扣减。
        int deducted = userMapper.deductMoney(order.getUserId(), order.getOrderAmount(), now);
        if (deducted <= 0) {
            if (userMapper.selectById(order.getUserId()) == null) {
                throw new OperateException("用户不存在");
            }
            throw new OperateException("余额不足，请先充值");
        }
        userAccountLogMapper.add(order.getUserId(), AccountLogEnum.UM_DEC_COURSE_PAY.getCode(),
                order.getOrderAmount(), order.getId(), order.getSn(), "课程券购买扣款", null);
    }

    // ---- mock 回调校验 -----------------------------------------------------

    @Override
    public RechargeOrder requireMockableOrder(Integer userId, String orderId) {
        Integer orderIdValue = toInt(orderId);
        if (orderIdValue == null || orderIdValue <= 0) {
            throw new OperateException("订单不存在");
        }
        RechargeOrder order = rechargeOrderMapper.selectById(orderIdValue);
        if (order == null || order.getDeleteTime() != null
                || userId == null || !userId.equals(order.getUserId())) {
            throw new OperateException("订单不存在");
        }
        if (order.getCouponGoodsId() == null || order.getCouponGoodsId() <= 0) {
            throw new OperateException("订单不是券订单");
        }
        return order;
    }

    // ---- 发券/流水 ---------------------------------------------------------

    /**
     * 按张发券：#103 起一个订单发 N 张独立券（每张 total=1 / remaining=1，order_seq=1..N），
     * 每张写一条 {@code FLOW_GRANT(times=1, balance=1)}，保证「实例数 = 发放流水数 = 张数」。
     *
     * <p>{@code like.coupon.pack-as-single=true} 时走旧口径：1 条 total=times 的实例（order_seq=0），
     * 仅用于应急回退，默认关闭。</p>
     */
    private List<CouponInstance> createInstances(Integer userId, Integer studentId, CouponGoods goods,
                                                 Integer orderId, String orderSn, String remark) {
        List<CouponInstance> instances = new ArrayList<>();
        if (packAsSingle) {
            instances.add(createInstance(userId, studentId, goods, orderId, orderSn, remark));
            return instances;
        }
        int times = goods.getTimes() == null || goods.getTimes() <= 0 ? 1 : goods.getTimes();
        long now = now();
        for (int seq = 1; seq <= times; seq++) {
            instances.add(createSingleInstance(userId, studentId, goods, orderId, orderSn, remark, seq, now));
        }
        return instances;
    }

    /** 旧口径兼容/应急回退：1 条 total=times、remaining=times、order_seq=0 的实例 + 1 条汇总发放流水。 */
    private CouponInstance createInstance(Integer userId, Integer studentId, CouponGoods goods,
                                          Integer orderId, String orderSn, String remark) {
        int total = goods.getTimes() == null || goods.getTimes() <= 0 ? 1 : goods.getTimes();
        return createSingleInstance(userId, studentId, goods, orderId, orderSn, remark, 0, now(), total);
    }

    /** 单张券：total 固定 1（旧口径传 times），写对应发放流水。 */
    private CouponInstance createSingleInstance(Integer userId, Integer studentId, CouponGoods goods,
                                                Integer orderId, String orderSn, String remark,
                                                int orderSeq, long now) {
        return createSingleInstance(userId, studentId, goods, orderId, orderSn, remark, orderSeq, now, 1);
    }

    private CouponInstance createSingleInstance(Integer userId, Integer studentId, CouponGoods goods,
                                                Integer orderId, String orderSn, String remark,
                                                int orderSeq, long now, int total) {
        CouponInstance instance = new CouponInstance();
        instance.setSn(nextCouponSn());
        instance.setUserId(userId);
        instance.setStudentId(studentId == null ? 0 : studentId);
        instance.setGoodsId(goods.getId());
        instance.setTitle(goods.getTitle());
        instance.setTotal(total);
        instance.setRemaining(total);
        instance.setScope(goods.getScope());
        instance.setScopeType(goods.getScopeType());
        instance.setCourseIds(goods.getCourseIds());
        instance.setAreaIds(goods.getAreaIds());
        instance.setStatus(1);
        instance.setStartTime(now);
        instance.setExpireTime(expireTime(goods.getValidDays(), now));
        instance.setOrderId(orderId);
        instance.setOrderSeq(orderSeq);
        instance.setSource("purchase");
        instance.setRemark(remark == null ? "" : remark);
        instance.setCreateTime(now);
        instance.setUpdateTime(now);
        couponInstanceMapper.insert(instance);

        CouponFlow flow = new CouponFlow();
        flow.setCouponId(instance.getId());
        flow.setUserId(userId);
        flow.setStudentId(studentId == null ? 0 : studentId);
        flow.setType(FLOW_GRANT);
        flow.setTimes(total);
        flow.setBalance(total);
        flow.setOrderId(orderId);
        flow.setOrderSn(orderSn == null ? "" : orderSn);
        flow.setRef("");
        flow.setOperatorType("system");
        flow.setOperatorId(0);
        flow.setRemark(remark == null ? "" : remark);
        flow.setCreateTime(now);
        couponFlowMapper.insert(flow);
        return instance;
    }

    /** 幂等基准：按张口径下等于商品次数；{@code pack-as-single} 旧口径下恒为 1。 */
    private int expectedInstanceCount(CouponGoods goods) {
        if (goods == null) {
            return 0;
        }
        if (packAsSingle) {
            return 1;
        }
        int times = goods.getTimes() == null ? 0 : goods.getTimes();
        return times > 0 ? times : 1;
    }

    private List<String> couponIds(List<CouponInstance> instances) {
        List<String> ids = new ArrayList<>();
        for (CouponInstance instance : instances) {
            ids.add(String.valueOf(instance.getId()));
        }
        return ids;
    }

    private String nextCouponSn() {
        String prefix = "CP" + Instant.now().atZone(ZoneId.systemDefault()).toLocalDateTime().format(SN_FMT);
        for (int i = 0; i < 10; i++) {
            String sn = prefix + ThreadLocalRandom.current().nextInt(100000, 1000000);
            Long count = couponInstanceMapper.selectCount(new QueryWrapper<CouponInstance>().eq("sn", sn));
            if (count == null || count == 0) {
                return sn;
            }
        }
        throw new OperateException("券编号生成失败，请重试");
    }

    /** 有效期：按自然日，到当天 23:59:59；validDays<=0 表示长期有效。 */
    private Long expireTime(Integer validDays, long now) {
        if (validDays == null || validDays <= 0) {
            return null;
        }
        LocalDate date = Instant.ofEpochSecond(now).atZone(ZoneId.systemDefault())
                .toLocalDate().plusDays(validDays);
        return date.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toEpochSecond();
    }

    private Integer toInt(String text) {
        if (text == null || text.trim().isEmpty()) {
            return null;
        }
        try {
            return Integer.parseInt(text.trim());
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    private long now() {
        return System.currentTimeMillis() / 1000L;
    }
}
