package com.mdd.admin.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mdd.admin.LikeAdminThreadLocal;
import com.mdd.admin.aop.Log;
import com.mdd.admin.service.IUserCourseAccountService;
import com.mdd.admin.service.IUserService;
import com.mdd.admin.service.IUserWalletBatchService;
import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.validate.user.UserAddValidate;
import com.mdd.admin.validate.user.UserSearchValidate;
import com.mdd.admin.validate.user.UserUpdateValidate;
import com.mdd.admin.validate.user.UserWalletBatchRowValidate;
import com.mdd.admin.validate.user.UserWalletValidate;
import com.mdd.admin.vo.user.UserVo;
import com.mdd.admin.vo.user.UserWalletBatchResultVo;
import com.mdd.common.aop.NotPower;
import com.mdd.common.core.AjaxResult;
import com.mdd.common.core.PageResult;
import com.mdd.common.entity.student.Student;
import com.mdd.common.enums.ErrorEnum;
import com.mdd.common.exception.LoginException;
import com.mdd.common.mapper.student.StudentMapper;
import com.mdd.common.util.StringUtils;
import com.mdd.common.validator.annotation.IDMust;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"adminapi/user/user", "adminapi/user.user"})
@Api(
   tags = {"用户数据管理"}
)
public class UserController {
   @Resource
   IUserService iUserService;
   @Resource
   StudentMapper studentMapper;
   @Resource
   IUserCourseAccountService userCourseAccountService;
   @Resource
   IUserWalletBatchService iUserWalletBatchService;

   public UserController() {
   }

   @GetMapping({"/stats"})
   @ApiOperation("学员统计")
   public AjaxResult<Object> stats() {
      return AjaxResult.success(this.iUserService.stats());
   }

   @GetMapping({"/lists"})
   @ApiOperation("用户列表")
   public AjaxResult<Object> list(@Validated PageValidate pageValidate, @Validated UserSearchValidate searchValidate) {
      if (StringUtils.isNotNull(searchValidate.getExport()) && searchValidate.getExport().equals(1)) {
         this.checkExportPermission();
         JSONObject ret = this.iUserService.getExportData(pageValidate, searchValidate);
         return AjaxResult.success(ret);
      } else if (StringUtils.isNotNull(searchValidate.getExport()) && searchValidate.getExport().equals(2)) {
         this.checkExportPermission();
         final String path = this.iUserService.export(searchValidate);
         return AjaxResult.success(2, new JSONObject() {
            {
               this.put("url", path);
            }
         }, ErrorEnum.SHOW_MSG.getCode());
      } else {
         PageResult<UserVo> list = this.iUserService.list(pageValidate, searchValidate);
         return AjaxResult.success(list);
      }
   }

   private void checkExportPermission() {
      Integer adminId = LikeAdminThreadLocal.getAdminId();
      if (adminId != null && adminId == 1) {
         return;
      }
      if (!StpUtil.hasPermission("user.user/export")) {
         throw new LoginException(ErrorEnum.NO_PERMISSION.getCode(), ErrorEnum.NO_PERMISSION.getMsg());
      }
   }

   @GetMapping({"/detail"})
   @ApiOperation("用户详情")
   public AjaxResult<UserVo> detail(@Validated @IDMust @RequestParam("id") Integer id) {
      UserVo vo = this.iUserService.detail(id);
      return AjaxResult.success(vo);
   }

   @Log(
      title = "新增学员"
   )
   @PostMapping({"/add"})
   @ApiOperation("新增学员")
   public AjaxResult<Object> add(@Validated @RequestBody UserAddValidate addValidate) {
      this.iUserService.add(addValidate);
      return AjaxResult.success();
   }

   @Log(
      title = "用户编辑"
   )
   @PostMapping({"/edit"})
   @ApiOperation("用户编辑")
   public AjaxResult<Object> edit(@Validated @RequestBody UserUpdateValidate updateValidate) {
      this.iUserService.edit(updateValidate);
      return AjaxResult.success();
   }

   @Log(
      title = "余额调整"
   )
   @PostMapping({"/adjustMoney"})
   @ApiOperation("余额调整")
   public AjaxResult<Object> adjustWallet(@Validated @RequestBody UserWalletValidate walletValidate) {
      this.iUserService.adjustWallet(walletValidate);
      return AjaxResult.success();
   }

   /**
    * 批量调整余额（#批量导入）：入参是前端解析 CSV 后的 JSON 行数组，1–500 行；
    * 每行正负金额表示增减，remark 必填；逐行独立事务，返回成功/失败汇总。
    *
    * <p>路由为 /user.user/adjustMoney/batch，按 URI 派生的权限字符不存在；这里用
    * @NotPower 关掉拦截器默认校验，显式复用既有权限点 user.user/adjustMoney。</p>
    */
   @Log(
      title = "批量调整余额"
   )
   @PostMapping({"/adjustMoney/batch"})
   @ApiOperation("批量调整余额（逐行独立，汇总成功/失败）")
   @NotPower
   public AjaxResult<UserWalletBatchResultVo> adjustWalletBatch(
      @RequestBody List<UserWalletBatchRowValidate> rows) {
      this.checkPermission("user.user/adjustMoney");
      Integer adminId = LikeAdminThreadLocal.getAdminId();
      return AjaxResult.success(this.iUserWalletBatchService.adjustWalletBatch(rows, adminId));
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

   @GetMapping({"/students"})
   @ApiOperation("查询用户下的学员列表")
   public AjaxResult<Object> students(@RequestParam("userId") Integer userId) {
      QueryWrapper<Student> qw = new QueryWrapper<Student>();
      qw.eq("user_id", userId).isNull("delete_time").orderByDesc("is_default").orderByAsc("id");
      List<Student> students = this.studentMapper.selectList(qw);
      List<Map<String, Object>> result = new ArrayList<>();

      for (Student s : students) {
         Map<String, Object> map = new LinkedHashMap<>();
         map.put("id", s.getId());
         map.put("name", s.getName());
         map.put("gender", s.getGender() == 1 ? "男" : (s.getGender() == 2 ? "女" : "未知"));
         map.put("birthday", s.getBirthday());
         map.put("isDefault", s.getIsDefault());
         result.add(map);
      }

      return AjaxResult.success(result);
   }

   @GetMapping({"/account"})
   @ApiOperation("用户客户对账")
   public AjaxResult<Object> account(@RequestParam("userId") Integer userId) {
      return AjaxResult.success(this.userCourseAccountService.getAccount(userId));
   }

   @GetMapping({"/lessonRecords"})
   @ApiOperation("用户上课明细")
   public AjaxResult<Object> lessonRecords(
      @RequestParam("userId") Integer userId,
      @RequestParam(value = "courseClassId",required = false) Integer courseClassId,
      @RequestParam(value = "pageNo",defaultValue = "1") Integer pageNo,
      @RequestParam(value = "pageSize",defaultValue = "20") Integer pageSize
   ) {
      return AjaxResult.success(this.userCourseAccountService.getLessonRecords(userId, courseClassId, pageNo, pageSize));
   }
}
