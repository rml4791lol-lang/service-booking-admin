package com.mdd.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mdd.admin.service.ICouponGoodsService;
import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.validate.coupon.CouponGoodsCreateValidate;
import com.mdd.admin.validate.coupon.CouponGoodsSearchValidate;
import com.mdd.admin.validate.coupon.CouponGoodsUpdateValidate;
import com.mdd.admin.validate.coupon.CouponGrantedSearchValidate;
import com.mdd.admin.validate.coupon.CouponGrantValidate;
import com.mdd.admin.validate.coupon.CouponRevokeValidate;
import com.mdd.admin.vo.coupon.CouponGoodsListedVo;
import com.mdd.admin.vo.coupon.CouponGrantedVo;
import com.mdd.admin.vo.coupon.CouponGrantVo;
import com.mdd.common.config.GlobalConfig;
import com.mdd.common.core.PageResult;
import com.mdd.common.entity.RechargeOrder;
import com.mdd.common.entity.coupon.CouponFlow;
import com.mdd.common.entity.coupon.CouponGoods;
import com.mdd.common.entity.coupon.CouponInstance;
import com.mdd.common.entity.course.CourseClass;
import com.mdd.common.entity.region.Area;
import com.mdd.common.entity.student.Student;
import com.mdd.common.entity.user.User;
import com.mdd.common.exception.OperateException;
import com.mdd.common.service.CouponEligibility;

import static com.mdd.common.service.CouponEligibility.SCOPE_ALL;
import static com.mdd.common.service.CouponEligibility.SCOPE_GROUP;
import static com.mdd.common.service.CouponEligibility.SCOPE_PRIVATE;
import static com.mdd.common.service.CouponEligibility.SCOPE_TRIAL;
import com.mdd.common.mapper.RechargeOrderMapper;
import com.mdd.common.mapper.coupon.CouponFlowMapper;
import com.mdd.common.mapper.coupon.CouponGoodsMapper;
import com.mdd.common.mapper.coupon.CouponInstanceMapper;
import com.mdd.common.mapper.course.CourseClassMapper;
import com.mdd.common.mapper.region.AreaMapper;
import com.mdd.common.mapper.student.StudentMapper;
import com.mdd.common.mapper.user.UserMapper;
import com.mdd.common.util.RedisUtils;
import com.mdd.common.util.TimeUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 次数券商品后台管理（#45 / T28.9）。
 *
 * <p>与小程序端 {@code GET /api/coupon/products} 共用 la_coupon_goods：
 * 后台改价/上下架/排序后，小程序下一次请求立即生效（前端服务不做商品缓存）。</p>
 *
 * <p>口径：</p>
 * <ul>
 *   <li>scopeType=all 表示全部课程，不能同时限定课程；</li>
 *   <li>group/private/trial 可留空限定课程（表示该类全部课程），也可指定白名单；</li>
 *   <li>指定课程的班型必须与 scopeType 一致（trial 不能限定私教课，与小程序适用校验一致）；</li>
 *   <li>areaIds 与 scopeType 正交，可单独限定小区；</li>
 *   <li>商品编码 code 是数据种子幂等键，创建后不可修改；</li>
 *   <li>有券实例/券订单引用时只允许下架，不允许删除。</li>
 * </ul>
 */
@Service
public class CouponGoodsServiceImpl implements ICouponGoodsService {

    // 适用范围常量（all/group/private/trial）自 #221 起引用 CouponEligibility（唯一来源），
    // 与预约凭证 / 核销选券 / 小程序 mock 共用同一份口径，不再本地定义。

    /** 发券幂等窗口（秒）：同操作人+同商品+同手机号+同学员+同张数在此窗口内只允许一次。 */
    private static final long GRANT_IDEMPOTENT_SECONDS = 60L;
    /** 回执最多回传的券编号数量。 */
    private static final int GRANT_SN_PREVIEW = 5;
    /** 券流水类型：1=发放。 */
    private static final int FLOW_GRANT = 1;
    /** 券流水类型：4=作废（#213 C2 后台撤回）。 */
    private static final int FLOW_VOID = 4;
    private static final String OPERATOR_ADMIN = "admin";

    /** 券实例状态：1=有效（引用 CouponEligibility，发券落库与判定同源），2=已用完，3=已过期，4=已作废。 */
    private static final int STATUS_VALID = CouponEligibility.STATUS_VALID;
    private static final int STATUS_EXPIRED = 3;
    private static final int STATUS_VOID = 4;

    /**
     * 发券幂等键。学员维度必须参与：同一手机号账号下给不同学员发同商品同张数
     * 是两次合法发放，不是重复提交；账号级（studentId=0）与指定学员也要区分。
     */
    static String grantIdempotentKey(int adminId, int goodsId, String mobile, int studentId, int quantity) {
        return GlobalConfig.redisPrefix + "coupon:grant:idem:" + adminId
                + ":" + goodsId + ":" + mobile + ":" + studentId + ":" + quantity;
    }

    @Resource
    CouponGoodsMapper couponGoodsMapper;
    @Resource
    CouponInstanceMapper couponInstanceMapper;
    @Resource
    CouponFlowMapper couponFlowMapper;
    @Resource
    RechargeOrderMapper rechargeOrderMapper;
    @Resource
    UserMapper userMapper;
    @Resource
    StudentMapper studentMapper;
    @Resource
    CourseClassMapper courseClassMapper;
    @Resource
    AreaMapper areaMapper;
    @Resource
    CouponEligibility couponEligibility;

    @Override
    public PageResult<CouponGoodsListedVo> list(PageValidate pageValidate, CouponGoodsSearchValidate searchValidate) {
        QueryWrapper<CouponGoods> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNull("delete_time");
        String keyword = trimToEmpty(searchValidate.getKeyword());
        if (!keyword.isEmpty()) {
            queryWrapper.and(wrapper -> wrapper.like("code", keyword).or().like("title", keyword));
        }
        if (searchValidate.getStatus() != null) {
            queryWrapper.eq("status", searchValidate.getStatus());
        }
        String scopeType = trimToEmpty(searchValidate.getScopeType());
        if (!scopeType.isEmpty()) {
            queryWrapper.eq("scope_type", scopeType);
        }
        queryWrapper.orderByAsc("sort").orderByAsc("id");

        IPage<CouponGoods> iPage = couponGoodsMapper.selectPage(
                new Page<>(pageValidate.getPage_no(), pageValidate.getPage_size()), queryWrapper);

        Set<Integer> courseIds = new HashSet<>();
        Set<Integer> areaIds = new HashSet<>();
        for (CouponGoods goods : iPage.getRecords()) {
            courseIds.addAll(parseIdsQuietly(goods.getCourseIds()));
            areaIds.addAll(parseIdsQuietly(goods.getAreaIds()));
        }
        Map<Integer, String> courseNames = courseNameMap(courseIds);
        Map<Integer, String> areaNames = areaNameMap(areaIds);

        List<CouponGoodsListedVo> list = new ArrayList<>();
        for (CouponGoods goods : iPage.getRecords()) {
            CouponGoodsListedVo vo = new CouponGoodsListedVo();
            BeanUtils.copyProperties(goods, vo);
            vo.setCourseNameText(joinNameText(parseIdsQuietly(goods.getCourseIds()), courseNames));
            vo.setAreaNameText(joinNameText(parseIdsQuietly(goods.getAreaIds()), areaNames));
            list.add(vo);
        }
        return PageResult.iPageHandle(iPage.getTotal(), iPage.getCurrent(), iPage.getSize(), list);
    }

    @Override
    public void add(CouponGoodsCreateValidate createValidate) {
        String code = normalizeCode(createValidate.getCode());
        assertCodeUnique(code, null);

        CouponGoods goods = new CouponGoods();
        applyFields(goods, createValidate);
        goods.setCode(code);
        goods.setStatus(createValidate.getStatus() == null ? 1 : createValidate.getStatus());
        long now = TimeUtils.timestamp();
        goods.setCreateTime(now);
        goods.setUpdateTime(now);
        couponGoodsMapper.insert(goods);
    }

    @Override
    public void edit(CouponGoodsUpdateValidate updateValidate) {
        CouponGoods goods = requireGoods(updateValidate.getId());
        String code = normalizeCode(updateValidate.getCode());
        if (!code.equals(goods.getCode())) {
            throw new OperateException("商品编码创建后不可修改（当前编码：" + goods.getCode() + "）");
        }
        assertCodeUnique(code, goods.getId());

        applyFields(goods, updateValidate);
        if (updateValidate.getStatus() != null) {
            goods.setStatus(updateValidate.getStatus());
        }
        goods.setUpdateTime(TimeUtils.timestamp());
        couponGoodsMapper.updateById(goods);
    }

    @Override
    public void status(Integer id, Integer status) {
        CouponGoods goods = requireGoods(id);
        goods.setStatus(status);
        goods.setUpdateTime(TimeUtils.timestamp());
        couponGoodsMapper.updateById(goods);
    }

    @Override
    public void sort(Integer id, Integer sort) {
        CouponGoods goods = requireGoods(id);
        goods.setSort(sort);
        goods.setUpdateTime(TimeUtils.timestamp());
        couponGoodsMapper.updateById(goods);
    }

    @Override
    public void del(Integer id) {
        CouponGoods goods = requireGoods(id);
        long instanceCount = countOrZero(couponInstanceMapper.selectCount(
                new QueryWrapper<CouponInstance>().eq("goods_id", id)));
        long orderCount = countOrZero(rechargeOrderMapper.selectCount(
                new QueryWrapper<RechargeOrder>().eq("coupon_goods_id", id)));
        if (instanceCount + orderCount > 0) {
            throw new OperateException("该券商品已有 " + instanceCount + " 张券实例、 " + orderCount
                    + " 笔券订单，不能删除；请改为下架（下架后小程序不再展示，历史券和订单保留）。");
        }
        long now = TimeUtils.timestamp();
        goods.setDeleteTime(now);
        goods.setStatus(0);
        goods.setUpdateTime(now);
        couponGoodsMapper.updateById(goods);
    }

    @Override
    public Map<String, Object> options() {
        List<CourseClass> courses = courseClassMapper.selectList(
                new QueryWrapper<CourseClass>().eq("is_delete", 0).orderByAsc("id"));
        Map<Integer, String> areaNames = areaNameMap(courses.stream()
                .map(CourseClass::getAreaId)
                .filter(areaId -> areaId != null && areaId > 0)
                .collect(Collectors.toSet()));

        List<Map<String, Object>> courseOptions = new ArrayList<>();
        for (CourseClass course : courses) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("id", course.getId());
            item.put("name", course.getName());
            item.put("type", courseType(course));
            item.put("classType", course.getClassType());
            item.put("areaId", course.getAreaId() == null ? 0 : course.getAreaId());
            item.put("areaName", areaNames.getOrDefault(course.getAreaId(), ""));
            item.put("isShow", course.getIsShow() == null ? 1 : course.getIsShow());
            courseOptions.add(item);
        }

        List<Area> areas = areaMapper.selectList(
                new QueryWrapper<Area>().isNull("delete_time").orderByAsc("id"));
        List<Map<String, Object>> areaOptions = new ArrayList<>();
        for (Area area : areas) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("id", area.getId());
            item.put("name", area.getName());
            item.put("communityId", area.getCommunityId());
            item.put("status", area.getStatus());
            areaOptions.add(item);
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("courses", courseOptions);
        result.put("areas", areaOptions);
        return result;
    }

    /**
     * 后台发次数券（#206）：券商品 → 逐张券实例（total=remaining=1）+ 发放流水。
     *
     * <p>口径：</p>
     * <ul>
     *   <li>按手机号定位家长账号，学员选填；账号级券 student_id=0，名下任一学员可用；</li>
     *   <li>每张券一条 FLOW_GRANT(times=1, balance=1)，与购买发券一致（实例数=流水条数=张数）；</li>
     *   <li>order_id 置 NULL：V021 的 uk_order_seq(order_id, order_seq) 下 0/0 只能存一行，
     *       NULL 可重复，且与既有后台发放数据口径一致；</li>
     *   <li>幂等：同操作人+同商品+同手机号+同学员+同张数 60 秒内重复提交直接拒绝，避免双击发爆；</li>
     *   <li>不做限购校验：后台发放是运营行为，不走小程序 per_user/per_student 限制。</li>
     * </ul>
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CouponGrantVo grant(CouponGrantValidate grantValidate, Integer adminId) {
        CouponGoods goods = couponGoodsMapper.selectById(grantValidate.getGoodsId());
        if (goods == null || goods.getDeleteTime() != null) {
            throw new OperateException("券商品不存在或已删除");
        }
        if (goods.getStatus() == null || goods.getStatus() != 1) {
            throw new OperateException("券商品已下架，不能发放");
        }

        String mobile = trimToEmpty(grantValidate.getMobile());
        User user = userMapper.selectOne(new QueryWrapper<User>()
                .eq("mobile", mobile)
                .isNull("delete_time")
                .orderByAsc("id")
                .last("limit 1"));
        if (user == null) {
            throw new OperateException("手机号未注册，请确认家长已在小程序注册");
        }
        if (user.getIsDisable() != null && user.getIsDisable() == 1) {
            throw new OperateException("该手机号账号已被禁用，不能发券");
        }

        int studentIdValue = grantValidate.getStudentId() == null ? 0 : grantValidate.getStudentId();
        if (studentIdValue < 0) {
            throw new OperateException("学员ID不正确");
        }
        Student student = null;
        if (studentIdValue > 0) {
            student = studentMapper.selectById(studentIdValue);
            if (student == null || student.getDeleteTime() != null
                    || !user.getId().equals(student.getUserId())) {
                throw new OperateException("学员不存在或不属于该手机号账号，请刷新后重试");
            }
        }

        int quantity = grantValidate.getQuantity() == null ? 0 : grantValidate.getQuantity();
        if (quantity < 1 || quantity > 99) {
            // 正常由 @Validated 拦截，这里兜底防止绕过校验直接调用服务。
            throw new OperateException("发放张数需在 1-99 之间");
        }

        String idemKey = grantIdempotentKey(adminId, goods.getId(), mobile, studentIdValue, quantity);
        Boolean acquired = RedisUtils.handler().opsForValue()
                .setIfAbsent(idemKey, "processing", GRANT_IDEMPOTENT_SECONDS, TimeUnit.SECONDS);
        if (Boolean.FALSE.equals(acquired)) {
            throw new OperateException("请勿重复提交：该券商品给此手机号的同张数发放正在处理或刚刚完成");
        }

        try {
            String remark = trimToEmpty(grantValidate.getRemark());
            if (remark.isEmpty()) {
                remark = "后台发放";
            }
            if (remark.length() > 255) {
                remark = remark.substring(0, 255);
            }
            long now = TimeUtils.timestamp();
            List<String> sns = new ArrayList<>();
            for (int i = 0; i < quantity; i++) {
                CouponInstance instance = buildGrantInstance(user.getId(), studentIdValue, goods, remark, now);
                couponInstanceMapper.insert(instance);
                couponFlowMapper.insert(buildGrantFlow(instance, adminId, remark, now));
                sns.add(instance.getSn());
            }

            CouponGrantVo vo = new CouponGrantVo();
            vo.setQuantity(quantity);
            vo.setCouponSnTotal(quantity);
            vo.setCouponSns(new ArrayList<>(sns.subList(0, Math.min(GRANT_SN_PREVIEW, sns.size()))));
            vo.setMobile(mobile);
            vo.setUserId(user.getId());
            vo.setStudentId(studentIdValue);
            vo.setStudentName(student == null || student.getName() == null ? "" : student.getName());
            vo.setGoodsId(goods.getId());
            vo.setGoodsTitle(goods.getTitle());
            return vo;
        } catch (RuntimeException e) {
            // 失败（含事务回滚）时释放幂等键，操作员可立即修正后重试；
            // 成功时保留 60 秒窗口，用于拦截重复点击。
            RedisUtils.handler().delete(idemKey);
            throw e;
        }
    }

    /**
     * 已发券查询（#213 C2）：券号精确 / 手机号精确，至少填一个；两者同时填按 AND。
     *
     * <p>只读，不做惰性状态刷新（过期/用尽的券仍按库里 status 展示），
     * 撤回按钮的可用性由 revocable 与后端撤回校验同口径给出。</p>
     */
    @Override
    public PageResult<CouponGrantedVo> granted(PageValidate pageValidate, CouponGrantedSearchValidate searchValidate) {
        String sn = trimToEmpty(searchValidate.getSn());
        String mobile = trimToEmpty(searchValidate.getMobile());
        if (sn.isEmpty() && mobile.isEmpty()) {
            throw new OperateException("请输入券号或家长手机号后再查询");
        }

        QueryWrapper<CouponInstance> queryWrapper = new QueryWrapper<>();
        if (!sn.isEmpty()) {
            queryWrapper.eq("sn", sn);
        }
        if (!mobile.isEmpty()) {
            List<User> users = userMapper.selectList(new QueryWrapper<User>()
                    .eq("mobile", mobile)
                    .isNull("delete_time"));
            if (users.isEmpty()) {
                return PageResult.iPageHandle(0L, pageValidate.getPage_no().longValue(),
                        pageValidate.getPage_size().longValue(), new ArrayList<>());
            }
            List<Integer> userIds = new ArrayList<>();
            for (User user : users) {
                userIds.add(user.getId());
            }
            queryWrapper.in("user_id", userIds);
        }
        queryWrapper.orderByDesc("id");

        IPage<CouponInstance> iPage = couponInstanceMapper.selectPage(
                new Page<>(pageValidate.getPage_no(), pageValidate.getPage_size()), queryWrapper);

        Set<Integer> userIds = new HashSet<>();
        Set<Integer> studentIds = new HashSet<>();
        for (CouponInstance instance : iPage.getRecords()) {
            if (instance.getUserId() != null && instance.getUserId() > 0) {
                userIds.add(instance.getUserId());
            }
            if (instance.getStudentId() != null && instance.getStudentId() > 0) {
                studentIds.add(instance.getStudentId());
            }
        }
        Map<Integer, User> userMap = new HashMap<>();
        if (!userIds.isEmpty()) {
            for (User user : userMapper.selectBatchIds(userIds)) {
                userMap.put(user.getId(), user);
            }
        }
        Map<Integer, Student> studentMap = new HashMap<>();
        if (!studentIds.isEmpty()) {
            for (Student student : studentMapper.selectBatchIds(studentIds)) {
                studentMap.put(student.getId(), student);
            }
        }

        List<CouponGrantedVo> list = new ArrayList<>();
        for (CouponInstance instance : iPage.getRecords()) {
            CouponGrantedVo vo = new CouponGrantedVo();
            vo.setId(instance.getId());
            vo.setSn(instance.getSn());
            vo.setTitle(instance.getTitle());
            vo.setUserId(instance.getUserId());
            User user = instance.getUserId() == null ? null : userMap.get(instance.getUserId());
            vo.setMobile(user == null || user.getMobile() == null ? "" : user.getMobile());
            vo.setStudentId(instance.getStudentId() == null ? 0 : instance.getStudentId());
            Student student = instance.getStudentId() == null ? null : studentMap.get(instance.getStudentId());
            vo.setStudentName(student == null || student.getName() == null ? "" : student.getName());
            vo.setStatus(instance.getStatus());
            vo.setStatusText(couponStatusText(instance.getStatus()));
            vo.setTotal(instance.getTotal() == null ? 0 : instance.getTotal());
            vo.setRemaining(instance.getRemaining() == null ? 0 : instance.getRemaining());
            vo.setRevocable(isRevocable(instance));
            vo.setSource(instance.getSource());
            vo.setRemark(instance.getRemark());
            vo.setCreateTime(TimeUtils.timestampToDate(instance.getCreateTime()));
            list.add(vo);
        }
        return PageResult.iPageHandle(iPage.getTotal(), iPage.getCurrent(), iPage.getSize(), list);
    }

    /**
     * 发券撤回（#213 C2）：只允许撤回「从未被核销」的券（status=1 且 remaining=total）。
     *
     * <p>幂等/并发：状态用条件更新（id + status=1）落 4，affected=0 视为并发已变化；
     * 流水只追加，作废写 type=4、times=1、balance=撤回时剩余次数，operator=当前管理员。
     * 已作废/已过期/已用完/部分核销均拒绝，不会重复写流水。</p>
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void revoke(CouponRevokeValidate revokeValidate, Integer adminId) {
        String sn = trimToEmpty(revokeValidate.getSn());
        CouponInstance instance = couponInstanceMapper.selectOne(new QueryWrapper<CouponInstance>()
                .eq("sn", sn)
                .last("limit 1"));
        if (instance == null) {
            throw new OperateException("券不存在，请核对券号");
        }
        Integer status = instance.getStatus();
        if (status != null && status == STATUS_VOID) {
            throw new OperateException("该券已作废，不能重复撤回");
        }
        if (status != null && status == STATUS_EXPIRED) {
            throw new OperateException("该券已过期，不能撤回");
        }
        if (status == null || status != STATUS_VALID) {
            throw new OperateException("该券已核销或已用完，不能撤回");
        }
        int total = instance.getTotal() == null ? 0 : instance.getTotal();
        int remaining = instance.getRemaining() == null ? 0 : instance.getRemaining();
        if (remaining != total) {
            throw new OperateException("该券已核销过（剩余 " + remaining + "/" + total + " 次），不能撤回");
        }

        long now = TimeUtils.timestamp();
        int affected = couponInstanceMapper.update(null, new UpdateWrapper<CouponInstance>()
                .eq("id", instance.getId())
                .eq("status", STATUS_VALID)
                .eq("remaining", total)
                .set("status", STATUS_VOID)
                .set("update_time", now));
        if (affected <= 0) {
            throw new OperateException("该券状态已变化，请刷新后重试");
        }

        String remark = trimToEmpty(revokeValidate.getRemark());
        if (remark.isEmpty()) {
            remark = "后台撤回";
        }
        if (remark.length() > 255) {
            remark = remark.substring(0, 255);
        }
        CouponFlow flow = new CouponFlow();
        flow.setCouponId(instance.getId());
        flow.setUserId(instance.getUserId());
        flow.setStudentId(instance.getStudentId());
        flow.setType(FLOW_VOID);
        flow.setTimes(1);
        flow.setBalance(remaining);
        flow.setOrderId(instance.getOrderId());
        flow.setOrderSn("");
        flow.setRef("admin:revoke");
        flow.setOperatorType(OPERATOR_ADMIN);
        flow.setOperatorId(adminId == null ? 0 : adminId);
        flow.setRemark(remark);
        flow.setCreateTime(now);
        couponFlowMapper.insert(flow);
    }

    /** 撤回口径：有效且剩余次数等于总次数（从未核销）。 */
    private boolean isRevocable(CouponInstance instance) {
        return instance.getStatus() != null && instance.getStatus() == STATUS_VALID
                && instance.getRemaining() != null && instance.getRemaining().equals(instance.getTotal());
    }

    /** 券实例状态文案：与小程序券包保持一致（1=可用，2=已用完，3=已过期，4=已作废）。 */
    private String couponStatusText(Integer status) {
        if (status == null) {
            return "未知";
        }
        switch (status) {
            case STATUS_VALID:
                return "可用";
            case 2:
                return "已用完";
            case STATUS_EXPIRED:
                return "已过期";
            case STATUS_VOID:
                return "已作废";
            default:
                return "未知";
        }
    }

    /** 单张后台发放券：total=remaining=1，范围/有效期快照取自商品。 */
    private CouponInstance buildGrantInstance(Integer userId, int studentId, CouponGoods goods,
                                              String remark, long now) {
        CouponInstance instance = new CouponInstance();
        instance.setSn(couponInstanceMapper.randMakeCouponInstanceSn());
        instance.setUserId(userId);
        instance.setStudentId(studentId);
        instance.setGoodsId(goods.getId());
        instance.setTitle(goods.getTitle());
        instance.setTotal(1);
        instance.setRemaining(1);
        instance.setScope(goods.getScope());
        instance.setScopeType(goods.getScopeType());
        instance.setCourseIds(goods.getCourseIds());
        instance.setAreaIds(goods.getAreaIds());
        instance.setStatus(1);
        instance.setStartTime(now);
        instance.setExpireTime(grantExpireTime(goods.getValidDays(), now));
        instance.setOrderId(null);
        instance.setOrderSeq(0);
        instance.setSource("grant");
        instance.setRemark(remark);
        instance.setCreateTime(now);
        instance.setUpdateTime(now);
        return instance;
    }

    /** 发放流水：type=1、times=1、balance=1，操作人=当前后台管理员。 */
    private CouponFlow buildGrantFlow(CouponInstance instance, Integer adminId, String remark, long now) {
        CouponFlow flow = new CouponFlow();
        flow.setCouponId(instance.getId());
        flow.setUserId(instance.getUserId());
        flow.setStudentId(instance.getStudentId());
        flow.setType(FLOW_GRANT);
        flow.setTimes(1);
        flow.setBalance(1);
        flow.setOrderId(null);
        flow.setOrderSn("");
        flow.setRef("admin:grant");
        flow.setOperatorType(OPERATOR_ADMIN);
        flow.setOperatorId(adminId == null ? 0 : adminId);
        flow.setRemark(remark);
        flow.setCreateTime(now);
        return flow;
    }

    /** 有效期口径与小程序购买发券一致：按自然日到当天 23:59:59；validDays<=0 表示长期有效。 */
    private Long grantExpireTime(Integer validDays, long now) {
        if (validDays == null || validDays <= 0) {
            return null;
        }
        LocalDate date = Instant.ofEpochSecond(now).atZone(ZoneId.systemDefault())
                .toLocalDate().plusDays(validDays);
        return date.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toEpochSecond();
    }

    /**
     * 把校验通过的字段写入实体，并完成适用范围一致性校验。
     */
    private void applyFields(CouponGoods goods, CouponGoodsCreateValidate validate) {
        goods.setTitle(validate.getTitle().trim());
        goods.setDescription(trimToEmpty(validate.getDescription()));
        goods.setPrice(validate.getPrice() == null ? BigDecimal.ZERO : validate.getPrice());
        goods.setTimes(validate.getTimes());
        goods.setValidDays(validate.getValidDays());
        goods.setScopeType(validate.getScopeType());
        goods.setPerUser(validate.getPerUser() == null ? 0 : validate.getPerUser());
        goods.setPerStudent(validate.getPerStudent() == null ? 0 : validate.getPerStudent());
        goods.setTag(trimToEmpty(validate.getTag()));
        goods.setSort(validate.getSort() == null ? 0 : validate.getSort());

        List<Integer> courseIdList = normalizeIds(validate.getCourseIds(), "限定课程");
        List<Integer> areaIdList = normalizeIds(validate.getAreaIds(), "限定小区");
        validateScope(validate.getScopeType(), courseIdList, areaIdList);

        goods.setCourseIds(joinIds(courseIdList));
        goods.setAreaIds(joinIds(areaIdList));
        goods.setScope(resolveScope(validate.getScope(), validate.getScopeType()));
    }

    /**
     * 适用范围与 scope_type 一致性：
     * all 不能限定课程；group/private/trial 的课程白名单必须与班型匹配；小区只校验存在性。
     */
    private void validateScope(String scopeType, List<Integer> courseIdList, List<Integer> areaIdList) {
        if (SCOPE_ALL.equals(scopeType) && !courseIdList.isEmpty()) {
            throw new OperateException("适用范围为「全部课程」时不能指定限定课程；请改选团课/私教/体验，或清空限定课程。");
        }
        if (!courseIdList.isEmpty()) {
            List<CourseClass> courses = courseClassMapper.selectBatchIds(courseIdList);
            Map<Integer, CourseClass> courseMap = new HashMap<>();
            for (CourseClass course : courses) {
                if (course.getIsDelete() == null || course.getIsDelete() == 0) {
                    courseMap.put(course.getId(), course);
                }
            }
            List<Integer> missing = new ArrayList<>();
            for (Integer courseId : courseIdList) {
                if (!courseMap.containsKey(courseId)) {
                    missing.add(courseId);
                }
            }
            if (!missing.isEmpty()) {
                throw new OperateException("限定课程不存在或已删除：" + joinIds(missing));
            }
            for (Integer courseId : courseIdList) {
                CourseClass course = courseMap.get(courseId);
                String type = courseType(course);
                if (SCOPE_GROUP.equals(scopeType) && !SCOPE_GROUP.equals(type)) {
                    throw new OperateException("限定课程「" + course.getName() + "」是"
                            + courseTypeText(type) + "，与适用范围「团课」不一致。");
                }
                if (SCOPE_PRIVATE.equals(scopeType) && !SCOPE_PRIVATE.equals(type)) {
                    throw new OperateException("限定课程「" + course.getName() + "」是"
                            + courseTypeText(type) + "，与适用范围「私教」不一致。");
                }
                if (SCOPE_TRIAL.equals(scopeType) && SCOPE_PRIVATE.equals(type)) {
                    throw new OperateException("体验券不能限定私教课程「" + course.getName() + "」。");
                }
            }
        }
        if (!areaIdList.isEmpty()) {
            List<Area> areas = areaMapper.selectBatchIds(areaIdList);
            Set<Integer> existing = new HashSet<>();
            for (Area area : areas) {
                if (area.getDeleteTime() == null) {
                    existing.add(area.getId());
                }
            }
            List<Integer> missing = new ArrayList<>();
            for (Integer areaId : areaIdList) {
                if (!existing.contains(areaId)) {
                    missing.add(areaId);
                }
            }
            if (!missing.isEmpty()) {
                throw new OperateException("限定小区不存在或已删除：" + joinIds(missing));
            }
        }
    }

    private String resolveScope(String scope, String scopeType) {
        String text = trimToEmpty(scope);
        if (!text.isEmpty()) {
            return text;
        }
        if (SCOPE_GROUP.equals(scopeType)) {
            return "团课通用";
        }
        if (SCOPE_PRIVATE.equals(scopeType)) {
            return "私教通用";
        }
        if (SCOPE_TRIAL.equals(scopeType)) {
            return "体验课";
        }
        return "全部课程";
    }

    private CouponGoods requireGoods(Integer id) {
        if (id == null || id <= 0) {
            throw new OperateException("商品ID不正确");
        }
        CouponGoods goods = couponGoodsMapper.selectOne(new QueryWrapper<CouponGoods>()
                .eq("id", id)
                .isNull("delete_time")
                .last("limit 1"));
        if (goods == null) {
            throw new OperateException("券商品不存在或已删除");
        }
        return goods;
    }

    private String normalizeCode(String code) {
        return trimToEmpty(code).toLowerCase();
    }

    private void assertCodeUnique(String code, Integer excludeId) {
        QueryWrapper<CouponGoods> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("code", code).isNull("delete_time");
        if (excludeId != null && excludeId > 0) {
            queryWrapper.ne("id", excludeId);
        }
        Long count = couponGoodsMapper.selectCount(queryWrapper);
        if (count != null && count > 0) {
            throw new OperateException("商品编码「" + code + "」已存在，请更换一个编码。");
        }
    }

    private List<Integer> normalizeIds(String text, String label) {
        LinkedHashSet<Integer> ids = new LinkedHashSet<>();
        String value = trimToEmpty(text);
        if (!value.isEmpty()) {
            for (String part : value.split(",")) {
                String item = part.trim();
                if (item.isEmpty()) {
                    continue;
                }
                if (!item.matches("\\d+")) {
                    throw new OperateException(label + "ID格式不正确，应为逗号分隔的数字。");
                }
                int id;
                try {
                    id = Integer.parseInt(item);
                } catch (NumberFormatException ex) {
                    throw new OperateException(label + "ID超出范围：" + item);
                }
                if (id <= 0) {
                    throw new OperateException(label + "ID必须大于0：" + item);
                }
                ids.add(id);
            }
        }
        if (ids.size() > 200) {
            throw new OperateException(label + "一次最多选择200项。");
        }
        return new ArrayList<>(ids);
    }

    private List<Integer> parseIdsQuietly(String text) {
        List<Integer> ids = new ArrayList<>();
        String value = trimToEmpty(text);
        if (value.isEmpty()) {
            return ids;
        }
        for (String part : value.split(",")) {
            String item = part.trim();
            if (item.isEmpty()) {
                continue;
            }
            try {
                int id = Integer.parseInt(item);
                if (id > 0 && !ids.contains(id)) {
                    ids.add(id);
                }
            } catch (NumberFormatException ignored) {
                // 历史脏数据不影响列表展示
            }
        }
        return ids;
    }

    private String joinIds(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return "";
        }
        List<String> text = new ArrayList<>();
        for (Integer id : ids) {
            text.add(String.valueOf(id));
        }
        return String.join(",", text);
    }

    private Map<Integer, String> courseNameMap(Set<Integer> ids) {
        Map<Integer, String> map = new HashMap<>();
        if (ids == null || ids.isEmpty()) {
            return map;
        }
        List<CourseClass> courses = courseClassMapper.selectBatchIds(ids);
        for (CourseClass course : courses) {
            map.put(course.getId(), course.getName());
        }
        return map;
    }

    private Map<Integer, String> areaNameMap(Set<Integer> ids) {
        Map<Integer, String> map = new HashMap<>();
        if (ids == null || ids.isEmpty()) {
            return map;
        }
        List<Area> areas = areaMapper.selectBatchIds(ids);
        for (Area area : areas) {
            map.put(area.getId(), area.getName());
        }
        return map;
    }

    private String joinNameText(List<Integer> ids, Map<Integer, String> names) {
        if (ids == null || ids.isEmpty()) {
            return "";
        }
        List<String> text = new ArrayList<>();
        for (Integer id : ids) {
            String name = names.get(id);
            if (name != null && !name.isEmpty()) {
                text.add(name);
            }
        }
        return String.join("、", text);
    }

    /**
     * 课程类型判定（#221 起委托 {@link CouponEligibility#courseType}，唯一实现）：
     * 名称含私教/一对一/一对二/一对三判私教，classType=1 判体验班，其余团课。
     */
    private String courseType(CourseClass course) {
        return couponEligibility.courseType(course);
    }

    private String courseTypeText(String type) {
        if (SCOPE_PRIVATE.equals(type)) {
            return "私教课";
        }
        if (SCOPE_TRIAL.equals(type)) {
            return "体验课";
        }
        return "团课";
    }

    private String trimToEmpty(String value) {
        return value == null ? "" : value.trim();
    }

    private long countOrZero(Long value) {
        return value == null ? 0L : value;
    }
}
