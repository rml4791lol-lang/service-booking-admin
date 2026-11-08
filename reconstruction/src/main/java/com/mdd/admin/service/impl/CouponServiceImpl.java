package com.mdd.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mdd.admin.service.ICouponService;
import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.validate.coupon.CouponCreateValidate;
import com.mdd.admin.validate.coupon.CouponIssueValidate;
import com.mdd.admin.validate.coupon.CouponSearchValidate;
import com.mdd.admin.vo.coupon.CouponDetailVo;
import com.mdd.admin.vo.coupon.CouponListedVo;
import com.mdd.admin.vo.coupon.CouponStudentVo;
import com.mdd.common.config.GlobalConfig;
import com.mdd.common.core.PageResult;
import com.mdd.common.entity.Coupon;
import com.mdd.common.entity.CouponTemplate;
import com.mdd.common.entity.user.User;
import com.mdd.common.exception.OperateException;
import com.mdd.common.mapper.CouponMapper;
import com.mdd.common.mapper.CouponTemplateMapper;
import com.mdd.common.mapper.user.UserMapper;
import com.mdd.common.util.RedisUtils;
import com.mdd.common.util.StringUtils;
import com.mdd.common.util.TimeUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 老券体系（la_coupon / la_coupon_template）后台服务。
 *
 * 字段严格对齐线上券表：face amount、min_amount、valid_days / start_time / expire_time，
 * 不引入次数券或新的券状态值（0=未使用、1=已使用、2=已过期）。
 */
@Service
public class CouponServiceImpl implements ICouponService {

    private static final int STATUS_UNUSED = 0;
    private static final int STATUS_USED = 1;
    private static final int STATUS_EXPIRED = 2;

    private static final int MAX_ISSUE_USERS = 200;
    private static final long ISSUE_IDEMPOTENT_SECONDS = 600L;

    @Resource
    CouponMapper couponMapper;
    @Resource
    UserMapper userMapper;
    @Resource
    CouponTemplateMapper couponTemplateMapper;

    @Override
    public PageResult<CouponListedVo> list(PageValidate pageValidate, CouponSearchValidate searchValidate) {
        Integer page = pageValidate.getPage_no();
        Integer limit = pageValidate.getPage_size();
        long now = System.currentTimeMillis() / 1000L;

        QueryWrapper<Coupon> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNull("delete_time");
        if (StringUtils.isNotEmpty(searchValidate.getMobile())) {
            queryWrapper.like("mobile", searchValidate.getMobile().trim());
        }
        if (StringUtils.isNotEmpty(searchValidate.getName())) {
            queryWrapper.like("name", searchValidate.getName().trim());
        }
        if (StringUtils.isNotEmpty(searchValidate.getSn())) {
            queryWrapper.like("sn", searchValidate.getSn().trim());
        }
        if (searchValidate.getUserId() != null) {
            queryWrapper.eq("user_id", searchValidate.getUserId());
        }
        if (searchValidate.getTemplateId() != null) {
            queryWrapper.eq("template_id", searchValidate.getTemplateId());
        }
        appendStatusFilter(queryWrapper, searchValidate.getStatus(), now);
        queryWrapper.orderByDesc("id");

        IPage<Coupon> iPage = couponMapper.selectPage(new Page<>(page, limit), queryWrapper);
        List<Coupon> records = iPage.getRecords();
        Map<Integer, User> userMap = loadUsers(records);

        LinkedList<CouponListedVo> list = new LinkedList<>();
        for (Coupon item : records) {
            CouponListedVo vo = new CouponListedVo();
            vo.setId(item.getId());
            vo.setTemplateId(item.getTemplateId());
            vo.setSn(item.getSn());
            vo.setName(item.getName());
            vo.setAmount(item.getAmount());
            vo.setMinAmount(item.getMinAmount());
            vo.setUserId(item.getUserId());
            User user = userMap.get(item.getUserId());
            if (user != null) {
                vo.setNickname(user.getNickname());
                vo.setRealName(user.getRealName());
            }
            vo.setMobile(item.getMobile());
            int effectiveStatus = effectiveStatus(item.getStatus(), item.getExpireTime(), now);
            vo.setStatus(effectiveStatus);
            vo.setStatusDesc(statusDesc(effectiveStatus));
            vo.setUsedOrderId(item.getUsedOrderId());
            vo.setUsedTime(item.getUsedTime() != null ? TimeUtils.timestampToDate(item.getUsedTime()) : "");
            vo.setStartTime(item.getStartTime() != null ? TimeUtils.timestampToDate(item.getStartTime()) : "");
            vo.setExpireTime(item.getExpireTime() != null ? TimeUtils.timestampToDate(item.getExpireTime()) : "");
            vo.setAdminId(item.getAdminId());
            vo.setRemark(item.getRemark());
            vo.setCreateTime(item.getCreateTime() != null ? TimeUtils.timestampToDate(item.getCreateTime()) : "");
            list.add(vo);
        }
        return PageResult.iPageHandle(iPage.getTotal(), iPage.getCurrent(), iPage.getSize(), list);
    }

    @Override
    public CouponDetailVo detail(Integer id) {
        Coupon model = couponMapper.selectOne(new QueryWrapper<Coupon>()
                .eq("id", id).isNull("delete_time").last("limit 1"));
        Assert.notNull(model, "数据不存在");

        CouponDetailVo vo = new CouponDetailVo();
        vo.setId(model.getId());
        vo.setTemplateId(model.getTemplateId());
        vo.setSn(model.getSn());
        vo.setName(model.getName());
        vo.setAmount(model.getAmount());
        vo.setMinAmount(model.getMinAmount());
        vo.setUserId(model.getUserId());
        User user = userMapper.selectById(model.getUserId());
        if (user != null) {
            vo.setNickname(user.getNickname());
            vo.setRealName(user.getRealName());
        }
        vo.setMobile(model.getMobile());
        int effectiveStatus = effectiveStatus(model.getStatus(), model.getExpireTime(), System.currentTimeMillis() / 1000L);
        vo.setStatus(effectiveStatus);
        vo.setStatusDesc(statusDesc(effectiveStatus));
        vo.setUsedOrderId(model.getUsedOrderId());
        vo.setUsedTime(model.getUsedTime() != null ? TimeUtils.timestampToDate(model.getUsedTime()) : "");
        vo.setStartTime(model.getStartTime());
        vo.setExpireTime(model.getExpireTime());
        vo.setAdminId(model.getAdminId());
        vo.setRemark(model.getRemark());
        vo.setCreateTime(model.getCreateTime() != null ? TimeUtils.timestampToDate(model.getCreateTime()) : "");
        return vo;
    }

    @Override
    public void add(CouponCreateValidate createValidate, Integer adminId) {
        Assert.isTrue(createValidate.getExpireTime() > createValidate.getStartTime(), "过期时间必须大于生效时间");

        Integer userId = 0;
        User user = userMapper.selectOne(new QueryWrapper<User>()
                .eq("mobile", createValidate.getMobile().trim()).isNull("delete_time").last("limit 1"));
        if (user != null) {
            userId = user.getId();
        }
        if (createValidate.getMinAmount() == null) {
            createValidate.setMinAmount(BigDecimal.ZERO);
        }
        long now = System.currentTimeMillis() / 1000L;
        Coupon model = new Coupon();
        model.setSn(couponMapper.randMakeCouponSn());
        model.setName(createValidate.getName().trim());
        model.setAmount(createValidate.getAmount());
        model.setMinAmount(createValidate.getMinAmount());
        model.setUserId(userId);
        model.setMobile(createValidate.getMobile().trim());
        model.setStatus(STATUS_UNUSED);
        model.setUsedOrderId(0);
        model.setStartTime(createValidate.getStartTime());
        model.setExpireTime(createValidate.getExpireTime());
        model.setAdminId(adminId);
        model.setRemark(StringUtils.isNotNull(createValidate.getRemark()) ? createValidate.getRemark().trim() : "");
        model.setCreateTime(now);
        model.setUpdateTime(now);
        couponMapper.insert(model);
    }

    @Override
    public void del(Integer id) {
        Coupon model = couponMapper.selectOne(new QueryWrapper<Coupon>()
                .eq("id", id).isNull("delete_time").last("limit 1"));
        Assert.notNull(model, "数据不存在!");
        if (model.getStatus() != null && model.getStatus() == STATUS_USED) {
            throw new OperateException("已使用的券不能作废，仅未使用/已过期券可作废");
        }
        long now = System.currentTimeMillis() / 1000L;
        model.setDeleteTime(now);
        model.setUpdateTime(now);
        couponMapper.updateById(model);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int issue(CouponIssueValidate issueValidate, Integer adminId) {
        String idemKey = buildIssueIdemKey(issueValidate.getRequestId(), adminId);
        if (idemKey != null) {
            Boolean first = RedisUtils.handler().opsForValue()
                    .setIfAbsent(idemKey, "processing", ISSUE_IDEMPOTENT_SECONDS, TimeUnit.SECONDS);
            if (Boolean.FALSE.equals(first)) {
                Object previous = RedisUtils.handler().opsForValue().get(idemKey);
                return previous instanceof Number ? ((Number) previous).intValue() : 0;
            }
        }

        try {
            int count = doIssue(issueValidate, adminId);
            if (idemKey != null) {
                RedisUtils.handler().opsForValue()
                        .set(idemKey, count, ISSUE_IDEMPOTENT_SECONDS, TimeUnit.SECONDS);
            }
            return count;
        } catch (RuntimeException e) {
            if (idemKey != null) {
                RedisUtils.handler().delete(idemKey);
            }
            throw e;
        }
    }

    private int doIssue(CouponIssueValidate issueValidate, Integer adminId) {
        CouponTemplate template = couponTemplateMapper.selectById(issueValidate.getTemplateId());
        if (template == null || template.getDeleteTime() != null) {
            throw new OperateException("优惠券模板不存在");
        }
        if (template.getStatus() == null || template.getStatus() != 0) {
            throw new OperateException("该模板已停用");
        }

        long now = System.currentTimeMillis() / 1000L;
        long startTime;
        long expireTime;
        if (template.getValidDays() != null && template.getValidDays() > 0) {
            startTime = now;
            expireTime = now + template.getValidDays() * 86400L;
        } else {
            if (template.getStartTime() == null || template.getExpireTime() == null) {
                throw new OperateException("模板缺少固定有效期，无法发放");
            }
            if (template.getExpireTime() <= template.getStartTime()) {
                throw new OperateException("模板有效期配置错误，请先修正模板");
            }
            if (template.getExpireTime() <= now) {
                throw new OperateException("该模板已过期，无法发放");
            }
            startTime = template.getStartTime();
            expireTime = template.getExpireTime();
        }

        List<Integer> userIds = normalizeUserIds(issueValidate.getUserIds());
        Map<Integer, User> userMap = loadUsersByIds(userIds);
        for (Integer userId : userIds) {
            User user = userMap.get(userId);
            if (user == null || user.getDeleteTime() != null) {
                throw new OperateException("学员不存在或已注销，请刷新后重试（用户ID：" + userId + "）");
            }
            if (user.getIsDisable() != null && user.getIsDisable() == 1) {
                throw new OperateException("学员已被禁用，不能发券（用户ID：" + userId + "）");
            }
        }

        String remark = StringUtils.isNotEmpty(issueValidate.getRemark())
                ? issueValidate.getRemark().trim()
                : "模板发放: " + template.getName();
        if (remark.length() > 255) {
            remark = remark.substring(0, 255);
        }
        for (Integer userId : userIds) {
            User user = userMap.get(userId);
            Coupon coupon = new Coupon();
            coupon.setTemplateId(template.getId());
            coupon.setSn(couponMapper.randMakeCouponSn());
            coupon.setName(template.getName());
            coupon.setAmount(template.getAmount());
            coupon.setMinAmount(template.getMinAmount() != null ? template.getMinAmount() : BigDecimal.ZERO);
            coupon.setUserId(userId);
            coupon.setMobile(user.getMobile() != null ? user.getMobile() : "");
            coupon.setStatus(STATUS_UNUSED);
            coupon.setUsedOrderId(0);
            coupon.setStartTime(startTime);
            coupon.setExpireTime(expireTime);
            coupon.setAdminId(adminId);
            coupon.setRemark(remark);
            coupon.setCreateTime(now);
            coupon.setUpdateTime(now);
            couponMapper.insert(coupon);
        }
        return userIds.size();
    }

    @Override
    public List<CouponStudentVo> students(String keyword) {
        String kw = keyword == null ? "" : keyword.trim();
        if (kw.length() > 32) {
            kw = kw.substring(0, 32);
        }
        QueryWrapper<User> qw = new QueryWrapper<User>()
                .select("id", "nickname", "real_name", "mobile", "account")
                .isNull("delete_time")
                .eq("is_disable", 0);
        if (StringUtils.isNotEmpty(kw)) {
            String finalKw = kw;
            qw.and(w -> w.like("nickname", finalKw)
                    .or().like("real_name", finalKw)
                    .or().like("mobile", finalKw)
                    .or().like("account", finalKw));
        }
        qw.orderByDesc("id").last("limit 20");

        List<CouponStudentVo> list = new ArrayList<>();
        for (User user : userMapper.selectList(qw)) {
            CouponStudentVo vo = new CouponStudentVo();
            vo.setId(user.getId());
            vo.setNickname(user.getNickname());
            vo.setRealName(user.getRealName());
            vo.setMobile(user.getMobile());
            vo.setAccount(user.getAccount());
            list.add(vo);
        }
        return list;
    }

    private void appendStatusFilter(QueryWrapper<Coupon> queryWrapper, Integer status, long now) {
        if (status == null) {
            return;
        }
        if (status == STATUS_EXPIRED) {
            // 线上存在未使用但已过期的券，按状态筛选时一并命中
            queryWrapper.and(w -> w.eq("status", STATUS_EXPIRED)
                    .or(x -> x.eq("status", STATUS_UNUSED).lt("expire_time", now)));
        } else if (status == STATUS_UNUSED) {
            queryWrapper.eq("status", STATUS_UNUSED)
                    .and(w -> w.isNull("expire_time").or().ge("expire_time", now));
        } else {
            queryWrapper.eq("status", status);
        }
    }

    private Map<Integer, User> loadUsers(List<Coupon> records) {
        List<Integer> userIds = new ArrayList<>();
        for (Coupon item : records) {
            Integer userId = item.getUserId();
            if (userId != null && userId > 0 && !userIds.contains(userId)) {
                userIds.add(userId);
            }
        }
        return loadUsersByIds(userIds);
    }

    private Map<Integer, User> loadUsersByIds(List<Integer> userIds) {
        Map<Integer, User> map = new HashMap<>();
        if (userIds == null || userIds.isEmpty()) {
            return map;
        }
        List<User> users = userMapper.selectList(new QueryWrapper<User>()
                .select("id", "nickname", "real_name", "mobile", "is_disable", "delete_time")
                .in("id", userIds));
        for (User user : users) {
            map.put(user.getId(), user);
        }
        return map;
    }

    private List<Integer> normalizeUserIds(List<Integer> userIds) {
        LinkedHashSet<Integer> unique = new LinkedHashSet<>();
        if (userIds != null) {
            for (Integer userId : userIds) {
                if (userId != null && userId > 0) {
                    unique.add(userId);
                }
            }
        }
        if (unique.isEmpty()) {
            throw new OperateException("请选择发放用户");
        }
        if (unique.size() > MAX_ISSUE_USERS) {
            throw new OperateException("单次最多发放" + MAX_ISSUE_USERS + "人");
        }
        return new ArrayList<>(unique);
    }

    private String buildIssueIdemKey(String requestId, Integer adminId) {
        if (StringUtils.isEmpty(requestId)) {
            return null;
        }
        String trimmed = requestId.trim();
        if (trimmed.length() > 64) {
            trimmed = trimmed.substring(0, 64);
        }
        return GlobalConfig.redisPrefix + "coupon:issue:idem:" + adminId + ":" + trimmed;
    }

    private int effectiveStatus(Integer status, Long expireTime, long now) {
        int value = status == null ? STATUS_UNUSED : status;
        if (value == STATUS_UNUSED && expireTime != null && expireTime > 0 && expireTime < now) {
            return STATUS_EXPIRED;
        }
        return value;
    }

    private String statusDesc(int status) {
        switch (status) {
            case STATUS_UNUSED:
                return "未使用";
            case STATUS_USED:
                return "已使用";
            case STATUS_EXPIRED:
                return "已过期";
            default:
                return "未知";
        }
    }
}
