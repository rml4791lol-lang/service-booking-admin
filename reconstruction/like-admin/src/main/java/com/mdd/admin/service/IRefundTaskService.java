package com.mdd.admin.service;

import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.validate.refund.RefundTaskSearchValidate;
import com.mdd.admin.vo.refund.RefundTaskListVo;
import com.mdd.admin.vo.refund.RefundTaskLogVo;
import com.mdd.common.core.PageResult;
import com.mdd.common.entity.RechargeOrder;
import com.mdd.common.entity.course.CourseRefund;
import com.mdd.common.entity.refund.RefundTask;

import java.util.List;

/**
 * 退款任务 outbox 服务（#60）。
 */
public interface IRefundTaskService {

    /**
     * 审核通过时为「需要调用微信退款」的订单创建退款任务（同一退课申请只建一条），
     * 提交后异步执行。
     *
     * <p>钱包/零元/已退款等无需线上退款的订单不建任务，由审核流程同步处理，
     * 避免后台把「无需退款」误显示成「退款成功」（#60）。</p>
     *
     * @param refund 退课申请
     * @param order  命中的原微信支付订单（必须有 transactionId）
     */
    RefundTask createForRefund(CourseRefund refund, RechargeOrder order);

    PageResult<RefundTaskListVo> list(PageValidate pageValidate, RefundTaskSearchValidate searchValidate);

    List<RefundTaskLogVo> logs(Integer taskId);

    /**
     * 人工重试失败/卡死的任务：重置为待执行并异步再跑一次。
     */
    void retry(Integer refundId, Integer adminId);

    /**
     * 执行单条任务（异步线程 / 后台清扫共用）。通过条件更新抢占，重复调度只会执行一次。
     */
    void executeTask(Integer taskId);

    /**
     * 将卡在「执行中」超过阈值的任务重新排队（进程中断/超时保护）。
     */
    int requeueStale(int batchSize);

    /**
     * 查出待执行/到点的失败任务 ID。
     */
    List<Integer> listDueTaskIds(int batchSize);
}
