package com.mdd.admin.controller.finance;

import com.mdd.admin.service.IFinanceSettlementService;
import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.validate.finance.FinanceSettlementPushValidate;
import com.mdd.admin.validate.finance.FinanceSettlementSearchValidate;
import com.mdd.common.core.AjaxResult;
import com.mdd.common.core.PageResult;
import com.mdd.common.settlement.SettlementItem;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 分账数据对接（#47 / #56）。
 *
 * <p>接口权限按路由校验：{@code finance.settlement/lists|summary|export|config|push}，
 * 菜单/权限 seed 见 {@code tools/ddl/migrations/20261024-add-profit-share-menu.sql}
 * 与 {@code 20261024-add-settlement-push-perm.sql}；超级管理员（admin id=1）由
 * {@code LikeAdminInterceptor} 放行。{@code push} 另有服务层「仅超级管理员」限制。</p>
 */
@RestController
@RequestMapping({"/adminapi/finance.settlement"})
@Api(tags = {"分账数据对接（只读查询/导出/受控上报）"})
public class FinanceSettlementController {

    @Resource
    IFinanceSettlementService financeSettlementService;

    @GetMapping({"/lists"})
    @ApiOperation("分账数据列表（按课程/社区/教练聚合的课时费与分成）")
    public AjaxResult<PageResult<SettlementItem>> lists(@Validated PageValidate pageValidate,
                                                        @Validated FinanceSettlementSearchValidate searchValidate) {
        return AjaxResult.success(financeSettlementService.lists(pageValidate, searchValidate));
    }

    @GetMapping({"/summary"})
    @ApiOperation("分账数据汇总（community / course / coach 三个维度）")
    public AjaxResult<Map<String, Object>> summary(@Validated FinanceSettlementSearchValidate searchValidate) {
        return AjaxResult.success(financeSettlementService.summary(searchValidate));
    }

    @GetMapping({"/export"})
    @ApiOperation("导出分账数据（xlsx，只读）")
    public AjaxResult<Map<String, Object>> export(@Validated FinanceSettlementSearchValidate searchValidate) {
        return AjaxResult.success(financeSettlementService.export(searchValidate));
    }

    @GetMapping({"/config"})
    @ApiOperation("分账上报适配器状态（脱敏，不含 secret）")
    public AjaxResult<Map<String, Object>> config() {
        return AjaxResult.success(financeSettlementService.config());
    }

    @PostMapping({"/push"})
    @ApiOperation("受控上报分账批次（默认 dryRun=true；仅超级管理员）")
    public AjaxResult<Map<String, Object>> push(
            @Validated @RequestBody(required = false) FinanceSettlementPushValidate validate) {
        return AjaxResult.success(financeSettlementService.push(validate));
    }
}
