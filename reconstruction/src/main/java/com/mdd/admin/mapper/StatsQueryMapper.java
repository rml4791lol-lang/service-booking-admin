package com.mdd.admin.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 数据统计 SQL 聚合/分页（T36 #106，统计链公共骨架）。
 *
 * <p>口径纪律（#107/#108/#109 追加维度时必须遵守）：</p>
 * <ul>
 *   <li>营业额 = 已支付 + 未删除 + 未退款（{@code pay_status=1 AND delete_time IS NULL
 *       AND (refund_status&lt;&gt;1 OR refund_status IS NULL)}），只认 {@code la_recharge_order.order_amount} 实付；</li>
 *   <li>营业额与消课流水各自先聚合再 JOIN，禁止同一条 GROUP BY 里同时 JOIN（会按流水行数放大金额）；</li>
 *   <li>社区归属统一 {@code COALESCE(NULLIF(c.community_id,0), a.community_id, 0)}，0 展示为「未归属」；</li>
 *   <li>不查会话访问统计表，也不引用收益类台账字段。</li>
 * </ul>
 */
@Mapper
public interface StatsQueryMapper {

    /** 对象存量数量：教练 / 课程包 / 在售课程包 / 社区 / 小区。 */
    Map<String, Object> objectCounts();

    /** 期间订单数 / 营业额 / 上课数 / 服务人次（四条独立聚合，避免 ×N 放大）。 */
    Map<String, Object> periodSummary(@Param("q") StatsQuery query);

    /** 营业额按日序列（缺日由 Service 补 0，保证图表连续）。 */
    List<Map<String, Object>> trendRows(@Param("q") StatsQuery query);

    /** 三级下钻：订单明细。 */
    long countOrders(@Param("q") StatsQuery query);

    List<Map<String, Object>> pageOrders(@Param("q") StatsQuery query);

    /** 三级下钻：课节明细（按 班级+课节 聚合消课流水）。 */
    long countLessons(@Param("q") StatsQuery query);

    List<Map<String, Object>> pageLessons(@Param("q") StatsQuery query);

    /**
     * 维度聚合：社区（#107）。
     *
     * <p>分组键 {@code COALESCE(NULLIF(class.community_id,0), area.community_id, 0)}：
     * 班级未填社区时用所属小区反查，两者都缺则归 0（对外显示「未归属」）。
     * 订单与消课各自先聚合、再按同一键合并，不会按流水行数放大营业额。</p>
     *
     * <p>返回有效社区（含期间零数据的社区，用于「全部社区」列表）与有数据的「未归属」行；
     * 分页在 Service 层按聚合结果切分，列表与导出复用同一份聚合，保证两者口径完全一致。</p>
     */
    List<Map<String, Object>> communityStats(@Param("q") StatsQuery query);

    /**
     * 维度聚合：小区（#108）。
     *
     * <p>分组键 {@code COALESCE(class.area_id, 0)}：班级未填小区时归 0（对外显示「未归属」）。
     * 与社区维度同构 —— 订单与消课各自先聚合、再按同一键合并，不会按流水行数放大营业额；
     * 返回有效小区（含期间零数据的小区，用于「全部小区」列表）与有数据的「未归属」行，
     * 分页在 Service 层按聚合结果切分，列表与导出复用同一份聚合，保证口径完全一致。</p>
     */
    List<Map<String, Object>> areaStats(@Param("q") StatsQuery query);

    /**
     * 片区节点（#109）：{@code la_region.level=2} 的分区即老板口径「片区」。
     * 社区表的 {@code region_id} 指向 level=3 街道，由 {@link #communityRegionRows()} 反查父分区；
     * 片区本身只提供 id/name/城市名/排序，不含任何口径计算。
     */
    List<Map<String, Object>> regionNodes();

    /** 社区 → 区划映射（#109）：只用于把社区聚合结果上卷到片区，不改社区口径。 */
    List<Map<String, Object>> communityRegionRows();

    /**
     * 课程构成（#109）：按 课程班级 + 类目 聚合期间订单数 / 营业额。
     *
     * <p>订单口径与 #106/#107/#108 完全一致；班级已删除/缺失的订单归 0 号键，
     * 由 Service 显示「未归属」并在合计中保留，保证「行合计 == 总览」。</p>
     */
    List<Map<String, Object>> courseMixRows(@Param("q") StatsQuery query);

    /**
     * 课程包销量（#109）：购买数 / 金额 / 报名数。
     *
     * <p>购买数与金额各自先聚合再 JOIN（禁止同一条 GROUP BY 里 JOIN 明细表，避免金额 ×N 放大）；
     * 键集合 = 购买记录 ∪ 期间订单，班级已删除/缺失的键归 0 号行（「未归属」显式成行）。</p>
     */
    List<Map<String, Object>> coursePackRows(@Param("q") StatsQuery query);

    /**
     * 期间内「无有效课程班级」的订单汇总（#109）：{@code course_class_id=0} / 班级已删除 / 班级不存在。
     *
     * <p>#102 教练口径从班级侧聚合，覆盖不到这些单；老板视角用它补出「未分配教练」残差，
     * 使 owner/instructorTop 行合计与 overview 完全相等（差异显式成行，不丢弃）。</p>
     */
    Map<String, Object> orphanOrders(@Param("q") StatsQuery query);
}
