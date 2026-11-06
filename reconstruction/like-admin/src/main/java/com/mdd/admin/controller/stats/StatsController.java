package com.mdd.admin.controller.stats;

import com.mdd.admin.aop.Log;
import com.mdd.admin.service.IStatsService;
import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.validate.stats.StatsSearchValidate;
import com.mdd.common.core.AjaxResult;
import com.mdd.common.core.PageResult;
import com.mdd.common.enums.ErrorEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 数据统计（T36 #106 骨架 + #107 社区维度 + #108 小区维度）。
 *
 * <p>一级 {@code stats/overview}：5 类对象数量 + 期间订单/营业额总览 + 环比；
 * 二级 {@code stats/trend}：营业额按日趋势；教练维度 {@code stats/instructor/*}
 * 直接复用 #102 的教练月营业额实现（同一份 SQL，不复制第二份口径）；
 * 社区维度 {@code stats/community/*}（#107）按
 * {@code COALESCE(NULLIF(class.community_id,0), area.community_id, 0)} 聚合订单数/客流/营业额；
 * 小区维度 {@code stats/area/*}（#108）与社区维度同构，分组键换成 {@code COALESCE(class.area_id, 0)}；
 * 三级 {@code stats/order/lists}、{@code stats/lesson/lists} 是 #107 社区 /
 * #108 小区 / #109 老板视角共用的明细下钻层（{@code communityId=0} 即「未归属」）。</p>
 *
 * <p>权限字符串与 URI 一一对应（LikeAdminInterceptor 规则）：
 * {@code stats/overview}、{@code stats/trend}、{@code stats/instructor/lists|export|detail}、
 * {@code stats/community/lists|export}、{@code stats/area/lists|export}、
 * {@code stats/order/lists}、{@code stats/lesson/lists}、
 * {@code stats/owner/region|communityTop|instructorTop|courseMix}（#109，导出走 export=1）、
 * {@code stats/coursePack/lists|export}（#109）；
 * 菜单/按钮 seed 见 {@code tools/ddl/migrations/20261028-t36-106-stats-menu.sql}、
 * {@code 20261028-t36-107-community-stats-menu.sql}、{@code 20261028-t36-108-area-stats-menu.sql}
 * 与 {@code 20261028-t36-109-boss-stats-menu.sql}。</p>
 *
 * <p>口径：只展示真实营业额与工作量，不查会话访问统计，不做二次分配计算。</p>
 */
@RestController
@RequestMapping(value = "/adminapi/stats")
@Api(tags = "数据统计（统计链公共骨架）")
public class StatsController {

    @Resource
    IStatsService iStatsService;

    // ------------------------------------------------------------------
    // 一级 / 二级
    // ------------------------------------------------------------------

    @GetMapping(value = "/overview")
    @ApiOperation(value = "统计总览：对象数量 + 期间订单/营业额/上课/人次 + 环比")
    public AjaxResult<Map<String, Object>> overview(@Validated StatsSearchValidate searchValidate) {
        return AjaxResult.success(iStatsService.overview(searchValidate));
    }

    @GetMapping(value = "/trend")
    @ApiOperation(value = "营业额按日趋势（默认近 30 天）")
    public AjaxResult<Map<String, Object>> trend(@Validated StatsSearchValidate searchValidate) {
        return AjaxResult.success(iStatsService.trend(searchValidate));
    }

    // ------------------------------------------------------------------
    // 教练维度（复用 #102 教练月营业额）
    // ------------------------------------------------------------------

    @GetMapping(value = "/instructor/lists")
    @ApiOperation(value = "教练维度列表（订单数/上课数/服务人次/总营业额）")
    public AjaxResult<PageResult<Map<String, Object>>> instructorList(
            @Validated PageValidate pageValidate,
            @Validated StatsSearchValidate searchValidate) {
        return AjaxResult.success(iStatsService.instructorList(pageValidate, searchValidate));
    }

    @Log(title = "导出教练统计")
    @GetMapping(value = "/instructor/export")
    @ApiOperation(value = "导出教练维度统计（xlsx）")
    public AjaxResult<Object> instructorExport(@Validated StatsSearchValidate searchValidate) {
        return exportResult(iStatsService.instructorExport(searchValidate));
    }

    @GetMapping(value = "/instructor/detail")
    @ApiOperation(value = "教练维度下钻（区间内课节 + 订单明细）")
    public AjaxResult<Map<String, Object>> instructorDetail(@Validated StatsSearchValidate searchValidate) {
        return AjaxResult.success(iStatsService.instructorDetail(searchValidate));
    }

    // ------------------------------------------------------------------
    // 社区维度（#107）
    // ------------------------------------------------------------------

    @GetMapping(value = "/community/lists")
    @ApiOperation(value = "社区维度列表（社区/订单数/客流到场人次/营业额，含「未归属」行）")
    public AjaxResult<PageResult<Map<String, Object>>> communityList(
            @Validated PageValidate pageValidate,
            @Validated StatsSearchValidate searchValidate) {
        return AjaxResult.success(iStatsService.communityList(pageValidate, searchValidate));
    }

    @Log(title = "导出社区统计")
    @GetMapping(value = "/community/export")
    @ApiOperation(value = "导出社区维度统计（xlsx）")
    public AjaxResult<Object> communityExport(@Validated StatsSearchValidate searchValidate) {
        return exportResult(iStatsService.communityExport(searchValidate));
    }

    // ------------------------------------------------------------------
    // 小区维度（#108，与社区维度同构，只换分组键）
    // ------------------------------------------------------------------

    @GetMapping(value = "/area/lists")
    @ApiOperation(value = "小区维度列表（小区/订单数/客流到场人次/营业额，含「未归属」行）")
    public AjaxResult<PageResult<Map<String, Object>>> areaList(
            @Validated PageValidate pageValidate,
            @Validated StatsSearchValidate searchValidate) {
        return AjaxResult.success(iStatsService.areaList(pageValidate, searchValidate));
    }

    @Log(title = "导出小区统计")
    @GetMapping(value = "/area/export")
    @ApiOperation(value = "导出小区维度统计（xlsx）")
    public AjaxResult<Object> areaExport(@Validated StatsSearchValidate searchValidate) {
        return exportResult(iStatsService.areaExport(searchValidate));
    }

    // ------------------------------------------------------------------
    // 老板视角（#109）：4 个查询 + 课程包销量
    //
    // 导出与查询共用同一 URI（export=1）：权限字符串按 URI 一一对应，
    // 因此 seed 只追加 6 个 A 行（stats/owner/region|communityTop|instructorTop|courseMix、
    // stats/coursePack/lists|export），不额外造 stats/owner/*/export 权限。
    // ------------------------------------------------------------------

    @GetMapping(value = "/owner/region")
    @ApiOperation(value = "老板视角：片区覆盖社区数 + 各片区订单数/营业额（含「未归属」片区行；export=1 导出）")
    public AjaxResult<Object> ownerRegion(@Validated StatsSearchValidate searchValidate) {
        if (isExport(searchValidate)) {
            return exportResult(iStatsService.ownerRegionExport(searchValidate));
        }
        return AjaxResult.success(iStatsService.ownerRegion(searchValidate));
    }

    @GetMapping(value = "/owner/communityTop")
    @ApiOperation(value = "老板视角：社区效果排行（营业额 DESC，并列看订单数；limit 默认 20，export=1 导出）")
    public AjaxResult<Object> ownerCommunityTop(@Validated StatsSearchValidate searchValidate) {
        if (isExport(searchValidate)) {
            return exportResult(iStatsService.ownerCommunityTopExport(searchValidate));
        }
        return AjaxResult.success(iStatsService.ownerCommunityTop(searchValidate));
    }

    @GetMapping(value = "/owner/instructorTop")
    @ApiOperation(value = "老板视角：教练效果排行（营业额 DESC，并列看订单数；复用 #102 教练口径 + 未分配教练残差）")
    public AjaxResult<Object> ownerInstructorTop(@Validated StatsSearchValidate searchValidate) {
        if (isExport(searchValidate)) {
            return exportResult(iStatsService.ownerInstructorTopExport(searchValidate));
        }
        return AjaxResult.success(iStatsService.ownerInstructorTop(searchValidate));
    }

    @GetMapping(value = "/owner/courseMix")
    @ApiOperation(value = "老板视角：上了什么课（课程班级/类目 × 订单数/营业额，含「未归属」课程行）")
    public AjaxResult<Object> ownerCourseMix(@Validated StatsSearchValidate searchValidate) {
        if (isExport(searchValidate)) {
            return exportResult(iStatsService.ownerCourseMixExport(searchValidate));
        }
        return AjaxResult.success(iStatsService.ownerCourseMix(searchValidate));
    }

    @GetMapping(value = "/coursePack/lists")
    @ApiOperation(value = "课程包销量（课程包/购买数/金额/报名数；不硬编码包数量）")
    public AjaxResult<PageResult<Map<String, Object>>> coursePackList(
            @Validated PageValidate pageValidate,
            @Validated StatsSearchValidate searchValidate) {
        return AjaxResult.success(iStatsService.coursePackList(pageValidate, searchValidate));
    }

    @Log(title = "导出课程包销量")
    @GetMapping(value = "/coursePack/export")
    @ApiOperation(value = "导出课程包销量（xlsx）")
    public AjaxResult<Object> coursePackExport(@Validated StatsSearchValidate searchValidate) {
        return exportResult(iStatsService.coursePackExport(searchValidate));
    }

    // ------------------------------------------------------------------
    // 三级明细（#107/#108/#109 复用；社区/小区维度在此下钻）
    // ------------------------------------------------------------------

    @GetMapping(value = "/order/lists")
    @ApiOperation(value = "订单明细（可按教练/社区/小区/课程包筛选）")
    public AjaxResult<PageResult<Map<String, Object>>> orderList(
            @Validated PageValidate pageValidate,
            @Validated StatsSearchValidate searchValidate) {
        return AjaxResult.success(iStatsService.orderList(pageValidate, searchValidate));
    }

    @GetMapping(value = "/lesson/lists")
    @ApiOperation(value = "课节明细（消课流水按 班级+课节 聚合）")
    public AjaxResult<PageResult<Map<String, Object>>> lessonList(
            @Validated PageValidate pageValidate,
            @Validated StatsSearchValidate searchValidate) {
        return AjaxResult.success(iStatsService.lessonList(pageValidate, searchValidate));
    }

    /**
     * code=2：前端 request 拦截器会打开 data.url 下载；与旧报表导出保持一致。
     */
    private AjaxResult<Object> exportResult(Map<String, Object> data) {
        return AjaxResult.success(2, data, ErrorEnum.SHOW_MSG.getCode());
    }

    /** 老板视角 4 个查询共用同一 URI 导出：export=1 时返回 code=2 + url（权限不变）。 */
    private boolean isExport(StatsSearchValidate searchValidate) {
        return searchValidate.getExport() != null && searchValidate.getExport().intValue() == 1;
    }
}
