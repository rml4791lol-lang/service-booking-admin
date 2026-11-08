package com.mdd.admin.controller.coupon;

import cn.dev33.satoken.stp.StpUtil;
import com.mdd.admin.LikeAdminThreadLocal;
import com.mdd.admin.aop.Log;
import com.mdd.admin.service.ICouponGoodsService;
import com.mdd.admin.service.ICouponGrantBatchService;
import com.mdd.admin.validate.commons.IdValidate;
import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.validate.coupon.CouponGoodsCreateValidate;
import com.mdd.admin.validate.coupon.CouponGrantBatchRowValidate;
import com.mdd.admin.validate.coupon.CouponGoodsSearchValidate;
import com.mdd.admin.validate.coupon.CouponGoodsSortValidate;
import com.mdd.admin.validate.coupon.CouponGoodsStatusValidate;
import com.mdd.admin.validate.coupon.CouponGoodsUpdateValidate;
import com.mdd.admin.validate.coupon.CouponGrantedSearchValidate;
import com.mdd.admin.validate.coupon.CouponGrantValidate;
import com.mdd.admin.validate.coupon.CouponRevokeValidate;
import com.mdd.admin.vo.coupon.CouponGoodsListedVo;
import com.mdd.admin.vo.coupon.CouponGrantBatchResultVo;
import com.mdd.admin.vo.coupon.CouponGrantedVo;
import com.mdd.admin.vo.coupon.CouponGrantVo;
import com.mdd.common.aop.NotPower;
import com.mdd.common.core.AjaxResult;
import com.mdd.common.core.PageResult;
import com.mdd.common.enums.ErrorEnum;
import com.mdd.common.exception.LoginException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 次数券商品管理（#45 / T28.9）。
 *
 * <p>接口前缀使用点号形式 /adminapi/coupon.goods/*，权限字符与路由一一对应：
 * coupon.goods/list、add、edit、status、sort、del、options、grant。
 * 按钮侧由 admin-src 的 v-perms 控制，接口侧由 LikeAdminInterceptor 调
 * StpUtil.checkPermission 校验，形成双重校验。</p>
 */
@RestController
@RequestMapping("/adminapi/coupon.goods")
@Api(tags = "券商品管理")
public class CouponGoodsController {

    @Resource
    ICouponGoodsService iCouponGoodsService;

    @Resource
    ICouponGrantBatchService iCouponGrantBatchService;

    @GetMapping("/list")
    @ApiOperation(value = "券商品列表")
    public AjaxResult<PageResult<CouponGoodsListedVo>> list(@Validated PageValidate pageValidate,
                                                           @Validated CouponGoodsSearchValidate searchValidate) {
        return AjaxResult.success(iCouponGoodsService.list(pageValidate, searchValidate));
    }

    @PostMapping("/add")
    @ApiOperation(value = "新增券商品")
    @Log(title = "新增券商品")
    public AjaxResult<Object> add(@Validated @RequestBody CouponGoodsCreateValidate createValidate) {
        iCouponGoodsService.add(createValidate);
        return AjaxResult.success();
    }

    @PostMapping("/edit")
    @ApiOperation(value = "编辑券商品")
    @Log(title = "编辑券商品")
    public AjaxResult<Object> edit(@Validated @RequestBody CouponGoodsUpdateValidate updateValidate) {
        iCouponGoodsService.edit(updateValidate);
        return AjaxResult.success();
    }

    @PostMapping("/status")
    @ApiOperation(value = "券商品上下架")
    @Log(title = "券商品上下架")
    public AjaxResult<Object> status(@Validated @RequestBody CouponGoodsStatusValidate statusValidate) {
        iCouponGoodsService.status(statusValidate.getId(), statusValidate.getStatus());
        return AjaxResult.success();
    }

    @PostMapping("/sort")
    @ApiOperation(value = "券商品排序")
    @Log(title = "券商品排序")
    public AjaxResult<Object> sort(@Validated @RequestBody CouponGoodsSortValidate sortValidate) {
        iCouponGoodsService.sort(sortValidate.getId(), sortValidate.getSort());
        return AjaxResult.success();
    }

    @PostMapping("/del")
    @ApiOperation(value = "删除券商品（有券实例/订单引用时拒绝）")
    @Log(title = "删除券商品")
    public AjaxResult<Object> del(@Validated @RequestBody IdValidate idValidate) {
        iCouponGoodsService.del(idValidate.getId());
        return AjaxResult.success();
    }

    @GetMapping("/options")
    @ApiOperation(value = "券商品表单下拉（课程/小区）")
    public AjaxResult<Map<String, Object>> options() {
        return AjaxResult.success(iCouponGoodsService.options());
    }

    @PostMapping("/grant")
    @ApiOperation(value = "后台发次数券（按张发放到家长券包）")
    @Log(title = "后台发券")
    public AjaxResult<CouponGrantVo> grant(@Validated @RequestBody CouponGrantValidate grantValidate) {
        Integer adminId = LikeAdminThreadLocal.getAdminId();
        return AjaxResult.success(iCouponGoodsService.grant(grantValidate, adminId));
    }

    /**
     * 批量发券（#批量导入）：入参是前端解析 CSV 后的 JSON 行数组，1–500 行。
     *
     * <p>路由为 /coupon.goods/grant/batch，按 URI 派生的权限字符不存在；这里用
     * @NotPower 关掉拦截器默认校验，显式复用既有权限点 coupon.goods/grant，
     * 与页面「发券」按钮同一权限。</p>
     */
    @PostMapping("/grant/batch")
    @ApiOperation(value = "后台批量发券（逐行独立，汇总成功/失败）")
    @Log(title = "后台批量发券")
    @NotPower
    public AjaxResult<CouponGrantBatchResultVo> grantBatch(
            @RequestBody List<CouponGrantBatchRowValidate> rows) {
        checkPermission("coupon.goods/grant");
        Integer adminId = LikeAdminThreadLocal.getAdminId();
        return AjaxResult.success(iCouponGrantBatchService.grantBatch(rows, adminId));
    }

    private void checkPermission(String permission) {
        Integer adminId = LikeAdminThreadLocal.getAdminId();
        if (adminId != null && adminId == 1) {
            return;
        }
        boolean allowed;
        try {
            allowed = StpUtil.hasPermission(permission);
        } catch (Exception e) {
            allowed = false;
        }
        if (!allowed) {
            throw new LoginException(ErrorEnum.NO_PERMISSION.getCode(), ErrorEnum.NO_PERMISSION.getMsg());
        }
    }

    @GetMapping("/granted")
    @ApiOperation(value = "已发券查询（按券号或家长手机号）")
    public AjaxResult<PageResult<CouponGrantedVo>> granted(@Validated PageValidate pageValidate,
                                                           @Validated CouponGrantedSearchValidate searchValidate) {
        return AjaxResult.success(iCouponGoodsService.granted(pageValidate, searchValidate));
    }

    @PostMapping("/revoke")
    @ApiOperation(value = "发券撤回（仅未核销的券可撤回，置为已作废并写流水）")
    @Log(title = "发券撤回")
    public AjaxResult<Object> revoke(@Validated @RequestBody CouponRevokeValidate revokeValidate) {
        Integer adminId = LikeAdminThreadLocal.getAdminId();
        iCouponGoodsService.revoke(revokeValidate, adminId);
        return AjaxResult.success();
    }
}
