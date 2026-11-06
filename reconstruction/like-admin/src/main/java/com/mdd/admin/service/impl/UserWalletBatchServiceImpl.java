package com.mdd.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mdd.admin.service.IUserService;
import com.mdd.admin.service.IUserWalletBatchService;
import com.mdd.admin.validate.user.UserWalletBatchRowValidate;
import com.mdd.admin.validate.user.UserWalletValidate;
import com.mdd.admin.vo.user.UserWalletBatchErrorVo;
import com.mdd.admin.vo.user.UserWalletBatchResultVo;
import com.mdd.common.entity.user.User;
import com.mdd.common.exception.BaseException;
import com.mdd.common.exception.OperateException;
import com.mdd.common.mapper.user.UserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 余额批量调整（#批量导入）。
 *
 * <p>口径：</p>
 * <ul>
 *   <li>入参是前端解析 CSV 后的 JSON 行数组，单次 1–500 行；</li>
 *   <li>amount 正负表示增减：正数增加、负数扣减；remark 必填；</li>
 *   <li>逐行复用 {@link IUserService#adjustWallet(UserWalletValidate, Integer)}：
 *       行锁 + 原子改余额 + 写 la_user_account_log，行级 @Transactional，
 *       单行失败只回滚该行；</li>
 *   <li>扣减前先校验余额，禁止扣成负数；</li>
 *   <li>操作人写入流水 extra（{"adminId":N,"source":"batch"}），操作日志另由 @Log 记录。</li>
 * </ul>
 */
@Service
public class UserWalletBatchServiceImpl implements IUserWalletBatchService {

    /** 单次批量上限。 */
    private static final int MAX_ROWS = 500;
    private static final Pattern MOBILE_PATTERN = Pattern.compile("^1\\d{10}$");
    /** 与单条校验 @Digits(integer=10, fraction=2) 对齐：绝对值必须小于 1e10。 */
    private static final BigDecimal MAX_AMOUNT = new BigDecimal("10000000000");
    private static final int REMARK_MAX_LENGTH = 255;

    @Resource
    UserMapper userMapper;
    @Resource
    IUserService iUserService;

    @Override
    public UserWalletBatchResultVo adjustWalletBatch(List<UserWalletBatchRowValidate> rows, Integer adminId) {
        if (rows == null || rows.isEmpty()) {
            throw new OperateException("请至少提供一行调整数据");
        }
        if (rows.size() > MAX_ROWS) {
            throw new OperateException("单次最多导入 " + MAX_ROWS + " 行，当前 " + rows.size() + " 行");
        }

        UserWalletBatchResultVo result = new UserWalletBatchResultVo();
        result.setTotal(rows.size());
        List<UserWalletBatchErrorVo> errors = new ArrayList<>();
        int success = 0;

        for (int i = 0; i < rows.size(); i++) {
            int rowNo = i + 1;
            UserWalletBatchRowValidate row = rows.get(i);
            try {
                adjustRow(row, adminId);
                success++;
            } catch (Exception e) {
                errors.add(new UserWalletBatchErrorVo(rowNo,
                        row == null || row.getMobile() == null ? "" : row.getMobile().trim(),
                        failureReason(e)));
            }
        }

        result.setSuccess(success);
        result.setFailed(errors.size());
        result.setErrors(errors);
        return result;
    }

    /**
     * 单行处理：参数/账号/余额预检后调用现有单条调整逻辑。
     * 这里不吞异常，交给上层收集行号与原因。
     */
    private void adjustRow(UserWalletBatchRowValidate row, Integer adminId) {
        if (row == null) {
            throw new OperateException("空行，无法调整");
        }
        String mobile = row.getMobile() == null ? "" : row.getMobile().trim();
        if (!MOBILE_PATTERN.matcher(mobile).matches()) {
            throw new OperateException("手机号为空或格式不正确（需 11 位手机号）");
        }

        BigDecimal amount = row.getAmount();
        if (amount == null) {
            throw new OperateException("调整金额不能为空");
        }
        if (amount.scale() > 2) {
            throw new OperateException("调整金额最多两位小数");
        }
        if (amount.compareTo(BigDecimal.ZERO) == 0) {
            throw new OperateException("调整金额不能为 0");
        }
        if (amount.abs().compareTo(MAX_AMOUNT) >= 0) {
            throw new OperateException("调整金额超出范围");
        }

        String remark = row.getRemark() == null ? "" : row.getRemark().trim();
        if (remark.isEmpty()) {
            throw new OperateException("备注必填，用于记录调整原因");
        }
        if (remark.length() > REMARK_MAX_LENGTH) {
            remark = remark.substring(0, REMARK_MAX_LENGTH);
        }

        User user = userMapper.selectOne(new QueryWrapper<User>()
                .eq("mobile", mobile)
                .isNull("delete_time")
                .orderByAsc("id")
                .last("limit 1"));
        if (user == null) {
            throw new OperateException("手机号未注册，请确认家长已在小程序注册");
        }

        BigDecimal balance = user.getUserMoney() == null ? BigDecimal.ZERO : user.getUserMoney();
        if (amount.signum() < 0 && balance.add(amount).signum() < 0) {
            throw new OperateException("余额不足：当前余额 " + balance + "，本次扣减 " + amount.abs());
        }

        UserWalletValidate validate = new UserWalletValidate();
        validate.setUserId(user.getId());
        validate.setAction(amount.signum() > 0 ? 1 : 2);
        validate.setNum(amount.abs());
        validate.setRemark(remark);
        iUserService.adjustWallet(validate, adminId);
    }

    /** 业务异常直接用 msg；非业务异常不把堆栈细节回传给前端。 */
    private String failureReason(Exception e) {
        if (e instanceof BaseException) {
            String msg = ((BaseException) e).getMsg();
            if (msg != null && !msg.trim().isEmpty()) {
                return msg;
            }
        }
        return "系统异常，请稍后重试";
    }
}
