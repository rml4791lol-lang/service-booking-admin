package com.mdd.admin.controller;

import com.alibaba.fastjson2.JSONObject;
import com.mdd.admin.aop.Log;
import com.mdd.admin.service.IInstructorService;
import com.mdd.admin.validate.commons.IdValidate;
import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.validate.instructor.InstructorCreateValidate;
import com.mdd.admin.validate.instructor.InstructorSearchValidate;
import com.mdd.admin.validate.instructor.InstructorUpdateValidate;
import com.mdd.admin.vo.instructor.InstructorDetailVo;
import com.mdd.admin.vo.instructor.InstructorListedVo;
import com.mdd.common.core.AjaxResult;
import com.mdd.common.core.PageResult;
import com.mdd.common.enums.ErrorEnum;
import com.mdd.common.util.StringUtils;
import com.mdd.common.validator.annotation.IDMust;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.annotation.Resource;
import java.util.Map;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/adminapi/instructor"})
@Api(
   tags = {"教练管理"}
)
public class InstructorController {
   @Resource
   IInstructorService iInstructorService;

   public InstructorController() {
   }

   @GetMapping({"/list"})
   @ApiOperation("教练列表")
   public AjaxResult<Object> list(@Validated PageValidate pageValidate, @Validated InstructorSearchValidate searchValidate) {
      if (StringUtils.isNotNull(searchValidate.getExport()) && searchValidate.getExport().equals(1)) {
         JSONObject ret = this.iInstructorService.getExportData(pageValidate, searchValidate);
         return AjaxResult.success(ret);
      } else if (StringUtils.isNotNull(searchValidate.getExport()) && searchValidate.getExport().equals(2)) {
         final Map<String, Object> data = this.iInstructorService.export(searchValidate);
         return AjaxResult.success(2, data, ErrorEnum.SHOW_MSG.getCode());
      } else {
         PageResult<InstructorListedVo> list = this.iInstructorService.list(pageValidate, searchValidate);
         return AjaxResult.success(list);
      }
   }

   @GetMapping({"/all"})
   @ApiOperation("全部教练(下拉选择，不分页)")
   public AjaxResult<Object> all() {
      return AjaxResult.success(this.iInstructorService.listAllForSelect());
   }

   @GetMapping({"/options"})
   @ApiOperation("教练表单选项(运动项目/社区)")
   public AjaxResult<Object> options() {
      return AjaxResult.success(this.iInstructorService.options());
   }

   @GetMapping({"/detail"})
   @ApiOperation("教练详情")
   public AjaxResult<InstructorDetailVo> detail(@Validated @IDMust @RequestParam("id") Integer id) {
      InstructorDetailVo detail = this.iInstructorService.detail(id);
      return AjaxResult.success(detail);
   }

   @Log(
      title = "教练新增"
   )
   @PostMapping({"/add"})
   @ApiOperation("教练新增")
   public AjaxResult<Object> add(@Validated @RequestBody InstructorCreateValidate createValidate) {
      this.iInstructorService.add(createValidate);
      return AjaxResult.success();
   }

   @Log(
      title = "教练编辑"
   )
   @PostMapping({"/edit"})
   @ApiOperation("教练编辑")
   public AjaxResult<Object> edit(@Validated @RequestBody InstructorUpdateValidate updateValidate) {
      this.iInstructorService.edit(updateValidate);
      return AjaxResult.success();
   }

   @Log(
      title = "教练删除"
   )
   @PostMapping({"/del"})
   @ApiOperation("教练删除")
   public AjaxResult<Object> del(@Validated @RequestBody IdValidate idValidate) {
      this.iInstructorService.del(idValidate.getId());
      return AjaxResult.success();
   }
}
