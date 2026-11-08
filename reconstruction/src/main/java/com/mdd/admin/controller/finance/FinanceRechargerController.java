package com.mdd.admin.controller.finance;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson2.JSONObject;
import com.mdd.admin.LikeAdminThreadLocal;
import com.mdd.admin.aop.Log;
import com.mdd.admin.service.IFinanceRechargerService;
import com.mdd.admin.validate.commons.IdValidate;
import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.validate.finance.FinanceRechargeEditValidate;
import com.mdd.admin.validate.finance.FinanceRechargeSearchValidate;
import com.mdd.admin.validate.finance.FinanceRefundValidate;
import com.mdd.admin.vo.finance.FinanceRechargeListVo;
import com.mdd.common.aop.NotPower;
import com.mdd.common.core.AjaxResult;
import com.mdd.common.core.PageResult;
import com.mdd.common.enums.ErrorEnum;
import com.mdd.common.exception.LoginException;
import com.mdd.common.util.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/adminapi/recharge/recharge", "/adminapi/recharge.recharge"})
@Api("缴费记录管理")
public class FinanceRechargerController {
   @Resource
   IFinanceRechargerService iFinanceRechargerService;

   public FinanceRechargerController() {
   }

   @GetMapping({"/lists"})
   @ApiOperation("缴费记录")
   public AjaxResult<Object> list(@Validated PageValidate pageValidate, @Validated FinanceRechargeSearchValidate searchValidate) {
      if (StringUtils.isNotNull(searchValidate.getExport()) && searchValidate.getExport().equals(1)) {
         this.checkExportPermission();
         JSONObject ret = this.iFinanceRechargerService.getExportData(pageValidate, searchValidate);
         return AjaxResult.success(ret);
      } else if (StringUtils.isNotNull(searchValidate.getExport()) && searchValidate.getExport().equals(2)) {
         this.checkExportPermission();
         final Map<String, Object> data = this.iFinanceRechargerService.export(searchValidate);
         return AjaxResult.success(2, data, ErrorEnum.SHOW_MSG.getCode());
      } else {
         PageResult<FinanceRechargeListVo> list = this.iFinanceRechargerService.list(pageValidate, searchValidate);
         return AjaxResult.success(list);
      }
   }

   @PostMapping({"/refund"})
   @Log(title = "发起缴费退款")
   @ApiOperation("发起退款")
   public AjaxResult<Object> refund(@Validated @RequestBody FinanceRefundValidate financeRefundValidate) {
      Integer adminId = LikeAdminThreadLocal.getAdminId();
      this.iFinanceRechargerService.refund(financeRefundValidate.getRecharge_id(), adminId);
      return AjaxResult.success();
   }

   @PostMapping({"/refundAgain"})
   @Log(title = "重新发起缴费退款")
   @ApiModelProperty("重新退款")
   public AjaxResult<Object> refundAgain(@Validated @RequestBody IdValidate idValidate) {
      Integer adminId = LikeAdminThreadLocal.getAdminId();
      this.iFinanceRechargerService.refundAgain(idValidate.getId(), adminId);
      return AjaxResult.success();
   }

   @GetMapping({"/stats"})
   @ApiOperation("充值统计")
   public AjaxResult<Map<String, Object>> stats() {
      return AjaxResult.success(this.iFinanceRechargerService.stats());
   }

   @Log(title = "编辑缴费记录")
   @NotPower
   @PostMapping({"/edit"})
   @ApiOperation("编辑充值记录")
   public AjaxResult<Object> edit(@Validated @RequestBody FinanceRechargeEditValidate editValidate) {
      this.checkEditPermission();
      this.iFinanceRechargerService.edit(editValidate.getId(), editValidate.getPayWay(),
            editValidate.getPayStatus(), editValidate.getIsAudit(), editValidate.getVoucher(),
            editValidate.getCouponId());
      return AjaxResult.success();
   }

   /**
    * 编辑接口同时暴露 /recharge/recharge/edit 与 /recharge.recharge/edit 两个别名；
    * LikeAdminInterceptor 按 URI 精确比对权限串，只有点号别名能命中现网菜单权限
    * recharge.recharge/edit。这里统一按业务权限点校验，避免同一个操作因别名不同而
    * 出现“有权限却被拒”；超级管理员（admin id=1）沿用框架口径直接放行。
    */
   private void checkEditPermission() {
      Integer adminId = LikeAdminThreadLocal.getAdminId();
      if (adminId != null && adminId == 1) {
         return;
      }
      if (!StpUtil.hasPermission("recharge.recharge/edit")) {
         throw new LoginException(ErrorEnum.NO_PERMISSION.getCode(), ErrorEnum.NO_PERMISSION.getMsg());
      }
   }

   private void checkExportPermission() {
      Integer adminId = LikeAdminThreadLocal.getAdminId();
      if (adminId != null && adminId == 1) {
         return;
      }
      if (!StpUtil.hasPermission("recharge.recharge/export")) {
         throw new LoginException(ErrorEnum.NO_PERMISSION.getCode(), ErrorEnum.NO_PERMISSION.getMsg());
      }
   }
}
