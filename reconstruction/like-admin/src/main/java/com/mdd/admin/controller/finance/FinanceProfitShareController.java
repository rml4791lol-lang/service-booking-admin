package com.mdd.admin.controller.finance;

import com.alibaba.fastjson2.JSONObject;
import com.mdd.admin.service.ILessonProfitShareService;
import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.validate.finance.LessonProfitShareSearchValidate;
import com.mdd.admin.vo.finance.LessonProfitShareListVo;
import com.mdd.common.aop.NotPower;
import com.mdd.common.core.AjaxResult;
import com.mdd.common.core.PageResult;
import com.mdd.common.enums.ErrorEnum;
import com.mdd.common.util.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/adminapi/finance.profitShare"})
@Api(
   tags = {"分账明细"}
)
public class FinanceProfitShareController {
   @Resource
   ILessonProfitShareService iLessonProfitShareService;

   public FinanceProfitShareController() {
   }

   @GetMapping({"/lists"})
   @ApiOperation("分账明细列表")
   public AjaxResult<Object> lists(@Validated PageValidate pageValidate, @Validated LessonProfitShareSearchValidate searchValidate) {
      if (StringUtils.isNotNull(searchValidate.getExport()) && searchValidate.getExport().equals(1)) {
         JSONObject ret = this.iLessonProfitShareService.getExportData(pageValidate, searchValidate);
         return AjaxResult.success(ret);
      } else if (StringUtils.isNotNull(searchValidate.getExport()) && searchValidate.getExport().equals(2)) {
         final String path = this.iLessonProfitShareService.export(searchValidate);
         return AjaxResult.success(2, new JSONObject() {
            {
               this.put("url", path);
            }
         }, ErrorEnum.SHOW_MSG.getCode());
      } else {
         PageResult<LessonProfitShareListVo> result = this.iLessonProfitShareService.lists(pageValidate, searchValidate);
         return AjaxResult.success(result);
      }
   }

   @GetMapping({"/communityOptions"})
   @ApiOperation("社区下拉(管理员)")
   @NotPower
   public AjaxResult<List<Map<String, Object>>> communityOptions() {
      return AjaxResult.success(this.iLessonProfitShareService.communityOptions());
   }
}
