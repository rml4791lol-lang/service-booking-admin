package com.mdd.admin.service;

import com.mdd.admin.validate.user.UserWalletBatchRowValidate;
import com.mdd.admin.vo.user.UserWalletBatchResultVo;

import java.util.List;

/**
 * 余额批量调整（#批量导入）。
 *
 * <p>逐行复用单条余额调整逻辑（原子调整 + 写 la_user_account_log + 禁止负余额），
 * 每行独立事务，单行失败不影响其他行。</p>
 */
public interface IUserWalletBatchService {

    /**
     * 批量调整余额。
     *
     * @param rows    前端解析 CSV 后的 JSON 行数组（1–500 行）
     * @param adminId 当前后台管理员ID（写入流水 extra，便于追溯操作人）
     * @return 汇总（total/success/failed/errors）
     */
    UserWalletBatchResultVo adjustWalletBatch(List<UserWalletBatchRowValidate> rows, Integer adminId);
}
