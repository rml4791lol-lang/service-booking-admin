package com.mdd.admin.service.impl;

import com.mdd.admin.service.ICouponGoodsService;
import com.mdd.admin.service.ICouponGrantBatchService;
import com.mdd.admin.validate.coupon.CouponGrantBatchRowValidate;
import com.mdd.admin.validate.coupon.CouponGrantValidate;
import com.mdd.admin.vo.coupon.CouponGrantBatchErrorVo;
import com.mdd.admin.vo.coupon.CouponGrantBatchResultVo;
import com.mdd.common.exception.BaseException;
import com.mdd.common.exception.OperateException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 券批量发放（#批量导入）。
 *
 * <p>口径：</p>
 * <ul>
 *   <li>入参是前端解析 CSV 后的 JSON 行数组，单次 1–500 行；</li>
 *   <li>逐行复用 {@link ICouponGoodsService#grant}（券商品 + 手机号 + 可选学员 + 张数），
 *       每行进入 grant 的 @Transactional，失败只回滚该行；</li>
 *   <li>不做整单大事务：一行失败不影响其他行，最后返回成功/失败汇总；</li>
 *   <li>每行仍走 grant 的 60 秒幂等键（同管理员+商品+手机号+学员+张数），
 *       批量接口本身不额外做整单幂等；</li>
 *   <li>行号按数据行从 1 开始（不含 CSV 表头），错误返回行号 + 手机号 + 原因。</li>
 * </ul>
 */
@Service
public class CouponGrantBatchServiceImpl implements ICouponGrantBatchService {

    /** 单次批量上限：防止误传超大数组把后台拖死。 */
    private static final int MAX_ROWS = 500;
    private static final Pattern MOBILE_PATTERN = Pattern.compile("^1\\d{10}$");
    private static final int REMARK_MAX_LENGTH = 200;

    @Resource
    ICouponGoodsService couponGoodsService;

    @Override
    public CouponGrantBatchResultVo grantBatch(List<CouponGrantBatchRowValidate> rows, Integer adminId) {
        if (rows == null || rows.isEmpty()) {
            throw new OperateException("请至少提供一行发放数据");
        }
        if (rows.size() > MAX_ROWS) {
            throw new OperateException("单次最多导入 " + MAX_ROWS + " 行，当前 " + rows.size() + " 行");
        }

        CouponGrantBatchResultVo result = new CouponGrantBatchResultVo();
        result.setTotal(rows.size());
        List<CouponGrantBatchErrorVo> errors = new ArrayList<>();
        int success = 0;

        for (int i = 0; i < rows.size(); i++) {
            int rowNo = i + 1;
            CouponGrantBatchRowValidate row = rows.get(i);
            try {
                CouponGrantValidate grantValidate = toGrantValidate(row);
                couponGoodsService.grant(grantValidate, adminId);
                success++;
            } catch (Exception e) {
                errors.add(new CouponGrantBatchErrorVo(rowNo,
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
     * 行级参数校验：不依赖 Bean Validation，才能把「哪一行错了」回传给前端。
     * 校验通过后交给单条 grant 做业务校验（商品/账号/禁用/幂等）。
     */
    private CouponGrantValidate toGrantValidate(CouponGrantBatchRowValidate row) {
        if (row == null) {
            throw new OperateException("空行，无法发放");
        }
        String mobile = row.getMobile() == null ? "" : row.getMobile().trim();
        if (!MOBILE_PATTERN.matcher(mobile).matches()) {
            throw new OperateException("手机号为空或格式不正确（需 11 位手机号）");
        }
        Integer goodsId = row.getGoodsId();
        if (goodsId == null || goodsId < 1) {
            throw new OperateException("券商品ID不正确，请检查 goodsId 列");
        }
        Integer quantity = row.getQuantity();
        if (quantity == null || quantity < 1 || quantity > 99) {
            throw new OperateException("发放张数需在 1-99 之间");
        }
        Integer studentId = row.getStudentId() == null ? 0 : row.getStudentId();
        if (studentId < 0) {
            throw new OperateException("学员ID不正确");
        }
        String remark = row.getRemark() == null ? "" : row.getRemark().trim();
        if (remark.length() > REMARK_MAX_LENGTH) {
            remark = remark.substring(0, REMARK_MAX_LENGTH);
        }

        CouponGrantValidate validate = new CouponGrantValidate();
        validate.setMobile(mobile);
        validate.setGoodsId(goodsId);
        validate.setQuantity(quantity);
        validate.setStudentId(studentId);
        validate.setRemark(remark);
        return validate;
    }

    /** 业务异常直接用 msg（未注册/已下架/重复提交等）；非业务异常不把堆栈细节回传给前端。 */
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
