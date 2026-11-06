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
 *
 * <p>漏跑补偿（2026-11-07 定时任务审计）：Quartz 的 misfire 策略在进程重启后不生效——
 * {@code CrontabServiceImpl#init()} 启动时会 {@code scheduler.clear()} 并按
 * la_dev_crontab 重建触发器，新触发器从当前时间起算，停机期间错过的时间点不会补跑。
 * 因此本任务按「积压驱动」设计：每次执行用 id 游标连续处理多批过期订单，直到清空或
 * 达到 {@link #MAX_BATCHES} 上限；停摆数小时的积压可在一次执行内排空，而不是按
 * 200 单/3 分钟慢慢消化。失败订单不会占住队头：游标继续前进，剩余订单下一轮重试。</p>
 */
@Component(value = "orderAutoCancelJob")
public class OrderAutoCancelJob {
    private static final Logger log = LoggerFactory.getLogger(OrderAutoCancelJob.class);

    /** 单批扫描上限，避免单次 IN/回表过大。 */
    static final int BATCH_SIZE = 200;

    /** 单次执行最多处理的批数（默认 4000 单），避免异常积压时一次跑太久影响下一轮。 */
    static final int MAX_BATCHES = 20;

    @Resource
    RechargeOrderMapper rechargeOrderMapper;
    @Resource
    UnpaidOrderCancelService unpaidOrderCancelService;

    public void execute() {
        long now = TimeUtils.timestamp();
        long expireTime = now - 1800L;
        int cancelled = 0;
        int failed = 0;
        int scanned = 0;
        int batches = 0;
        long cursor = 0L;

        while (batches < MAX_BATCHES) {
            List<RechargeOrder> expired = rechargeOrderMapper.selectList(new QueryWrapper<RechargeOrder>()
                    .select("id")
                    .eq("pay_status", PaymentEnum.UN_PAID.getCode())
                    .le("create_time", expireTime)
                    .isNull("delete_time")
                    .gt("id", cursor)
                    .orderByAsc("id")
                    .last("limit " + BATCH_SIZE));
            if (expired.isEmpty()) {
                break;
            }
            batches++;
            scanned += expired.size();
            for (RechargeOrder order : expired) {
                Integer orderId = order.getId();
                if (orderId == null) {
                    continue;
                }
                cursor = orderId;
                try {
                    if (unpaidOrderCancelService.cancelExpired(orderId, expireTime)) {
                        cancelled++;
                    }
                } catch (Exception e) {
                    failed++;
                    log.error("超时未支付订单取消失败: orderId={}", orderId, e);
                }
            }
            if (expired.size() < BATCH_SIZE) {
                break;
            }
        }

        if (scanned > 0) {
            log.info("未支付订单自动取消任务执行完成，扫描：{}，批数：{}，取消：{}，失败：{}",
                    scanned, batches, cancelled, failed);
        }
    }
}
