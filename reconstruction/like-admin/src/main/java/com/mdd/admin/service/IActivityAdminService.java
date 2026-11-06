package com.mdd.admin.service;

import com.mdd.admin.validate.activity.ActivityCreateValidate;
import com.mdd.admin.validate.activity.ActivityUpdateValidate;
import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.vo.activity.ActivityEnrollAlertDetailVo;
import com.mdd.admin.vo.activity.ActivityEnrollAlertListVo;
import com.mdd.common.core.PageResult;
import com.mdd.common.entity.activity.Activity;

import java.util.List;
import java.util.Map;

/**
 * 活动 / 报名链接 / 报名名单 / 开班申请后台（T29.7 #27、T30.6 #38）。
 *
 * <p>#27 交付链接生成、复用与提交查询；#38 补齐链接列表/停用启用/有效期、
 * 名单筛选（手机号/学员/时间/状态）、去重汇总与 Excel 导出；#61 补活动报名
 * 「已付款但满员」的待处理告警查询、标记已处理与发起退款。</p>
 */
public interface IActivityAdminService {

    /** 可生成报名链接的课程场次（未来场次，最多 300 条）。 */
    List<Map<String, Object>> enrollOptions(String keyword);

    /** 生成/复用某场次的报名链接。 */
    Map<String, Object> createEnrollLink(Integer adminId, Integer sessionId, Integer expireDays);

    /** 报名链接列表（按场次/状态/token 筛选，含提交数）。 */
    PageResult<Map<String, Object>> enrollLinks(PageValidate pageValidate, Integer sessionId,
                                                String status, String keyword);

    /** 停用/启用报名链接；status 只接受 0=停用、1=启用。 */
    Map<String, Object> updateEnrollLinkStatus(Integer adminId, Integer id, Integer status);

    /** 设置报名链接有效期；expireDays=0 表示长期有效，1-365 天从当前时间起算。 */
    Map<String, Object> updateEnrollLinkExpire(Integer adminId, Integer id, Integer expireDays);

    /** 报名名单（按链接/场次/手机号/学员/时间/状态筛选，返回去重汇总）。 */
    PageResult<Map<String, Object>> enrollmentList(PageValidate pageValidate, Integer sessionId,
                                                   Integer linkId, String token, String keyword,
                                                   String phone, String studentName, Integer status,
                                                   String dateStart, String dateEnd);

    /** 报名名单导出 Excel（筛选条件与列表一致），返回文件 URL/文件名/行数。 */
    Map<String, Object> exportEnrollments(Integer sessionId, Integer linkId, String token,
                                          String keyword, String phone, String studentName,
                                          Integer status, String dateStart, String dateEnd);

    /** 活动报名名单（含支付状态）：一人一行，支持活动/报名状态/支付状态/关键词/时间筛选。 */
    PageResult<Map<String, Object>> activitySignupList(PageValidate pageValidate, Integer activityId,
                                                       Integer status, Integer payStatus, String keyword,
                                                       String dateStart, String dateEnd);

    /** 活动报名名单导出 Excel（列含支付状态），返回文件 URL/文件名/行数。 */
    Map<String, Object> exportActivitySignups(Integer activityId, Integer status, Integer payStatus,
                                              String keyword, String dateStart, String dateEnd);

    /**
     * 后台按报名行取消（审计 #213 C3）：活动未开始且已报名才可操作；
     * 免费报名直接取消并释放名额，付费报名只生成待审核退款申请（mode=refunding）。
     */
    Map<String, Object> cancelActivitySignup(Integer adminId, Integer signupId, String remark);

    /** 开班申请聚合列表（按街道+课程类型合并）。 */
    PageResult<Map<String, Object>> applyGroups(PageValidate pageValidate, Integer streetId,
                                                String courseType, String status);

    // ---- 活动报名超员待处理告警（#61） ------------------------------------

    /** 异常报名列表（按状态/活动/订单号/手机号筛选）。 */
    PageResult<ActivityEnrollAlertListVo> enrollExceptions(PageValidate pageValidate, Integer status,
                                                           Integer activityId, String keyword);

    /** 异常报名详情（订单、活动、报名明细与处理记录）。 */
    ActivityEnrollAlertDetailVo enrollExceptionDetail(Integer id);

    /** 标记异常报名已处理（保留超员名额），记录处理人/备注。 */
    Map<String, Object> handleEnrollException(Integer adminId, Integer id, String remark);

    /** 对异常报名发起退款；成功后取消报名明细、释放名额并把告警置为已退款（幂等）。 */
    Map<String, Object> refundEnrollException(Integer adminId, Integer id, String remark);

    // ---- 活动管理 CRUD（#186） --------------------------------------------

    /** 活动列表（软删过滤，keyword 匹配标题/地点，status 可选）。 */
    PageResult<Activity> activityList(PageValidate pageValidate, String keyword, Integer status);

    /** 活动详情；不存在或已软删抛「活动不存在」。 */
    Map<String, Object> activityDetail(Integer id);

    /** 新增活动（joined=0）。 */
    void addActivity(ActivityCreateValidate createValidate);

    /** 编辑活动表单字段；不覆盖 joined。 */
    void editActivity(ActivityUpdateValidate updateValidate);

    /** 上下架活动（0=下架，1=上架）。 */
    void updateActivityStatus(Integer id, Integer status);

    /** 软删活动；已有已支付报名时拒绝（先下架）。 */
    void deleteActivity(Integer id);
}
