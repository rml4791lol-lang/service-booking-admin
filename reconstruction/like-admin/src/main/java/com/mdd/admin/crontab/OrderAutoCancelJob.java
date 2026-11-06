package com.mdd.admin.crontab;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mdd.common.entity.RechargeOrder;
import com.mdd.common.enums.PaymentEnum;
import com.mdd.common.mapper.RechargeOrderMapper;
import com.mdd.common.service.UnpaidOrderCancelService;
import com.mdd.common.util.TimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 未支付订单自动取消（30 分钟）。
 *
 * <p>#214：关单必须与资源释放同事务。旧实现是一条批量 UPDATE 把 pay_status 置 4，
 * 订单上占用的次数券/活动报名名额不会回退；这里改为逐单条件关单，交给
 * {@link UnpaidOrderCancelService} 在同一事务里作废券实例并按批次释放活动名额。</p>
 *
 * <p>并发/幂等：每单的关单是 {@code pay_status=0 → 4} 条件更新，只有抢到状态变更的
 * 调用才释放资源；单笔失败只记日志不阻断其余订单。</p>
 */
@Component(value = "orderAutoCancelJob")
public class OrderAutoCancelJob {
    private static final Logger log = LoggerFactory.getLogger(OrderAutoCancelJob.class);

    /** 单次扫描上限：避免一次扫全表，剩余订单由下一轮任务继续处理。 */
    private static final int BATCH_SIZE = 200;

    @Resource
    RechargeOrderMapper rechargeOrderMapper;
    @Resource
    UnpaidOrderCancelService unpaidOrderCancelService;

    public void execute() {
        long now = TimeUtils.timestamp();
        long expireTime = now - 1800L;
        List<RechargeOrder> expired = rechargeOrderMapper.selectList(new QueryWrapper<RechargeOrder>()
                .select("id")
                .eq("pay_status", PaymentEnum.UN_PAID.getCode())
                .le("create_time", expireTime)
                .isNull("delete_time")
                .orderByAsc("id")
                .last("limit " + BATCH_SIZE));
        if (expired.isEmpty()) {
            return;
        }
        int cancelled = 0;
        int failed = 0;
        for (RechargeOrder order : expired) {
            try {
                if (unpaidOrderCancelService.cancelExpired(order.getId(), expireTime)) {
                    cancelled++;
                }
            } catch (Exception e) {
                failed++;
                log.error("超时未支付订单取消失败: orderId={}", order.getId(), e);
            }
        }
        log.info("未支付订单自动取消任务执行完成，共取消订单数量：{}，失败：{}", cancelled, failed);
    }
}
