package com.mdd.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mdd.admin.service.ICouponTemplateService;
import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.validate.coupon.CouponTemplateCreateValidate;
import com.mdd.admin.vo.coupon.CouponTemplateListedVo;
import com.mdd.common.core.PageResult;
import com.mdd.common.entity.CouponTemplate;
import com.mdd.common.exception.OperateException;
import com.mdd.common.mapper.CouponTemplateMapper;
import com.mdd.common.util.StringUtils;
import com.mdd.common.util.TimeUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

/**
 * 券模板（la_coupon_template）后台服务。
 *
 * 有效期两种形态与线上表一致：
 *  - valid_days > 0：领取后 N 天有效，start_time/expire_time 为空；
 *  - valid_days = 0：使用固定 start_time ~ expire_time 窗口。
 */
@Service
public class CouponTemplateServiceImpl implements ICouponTemplateService {

    @Resource
    CouponTemplateMapper couponTemplateMapper;

    @Override
    public PageResult<CouponTemplateListedVo> list(PageValidate pageValidate, String name, Integer status) {
        Integer page = pageValidate.getPage_no();
        Integer limit = pageValidate.getPage_size();
        QueryWrapper<CouponTemplate> qw = new QueryWrapper<>();
        qw.isNull("delete_time").orderByDesc("id");
        if (StringUtils.isNotEmpty(name)) {
            qw.like("name", name.trim());
        }
        if (status != null && status != -1) {
            qw.eq("status", status);
        }
        IPage<CouponTemplate> iPage = couponTemplateMapper.selectPage(new Page<>(page, limit), qw);
        LinkedList<CouponTemplateListedVo> list = new LinkedList<>();
        for (CouponTemplate item : iPage.getRecords()) {
            list.add(toVo(item));
        }
        return PageResult.iPageHandle(iPage.getTotal(), iPage.getCurrent(), iPage.getSize(), list);
    }

    @Override
    public List<CouponTemplateListedVo> all() {
        QueryWrapper<CouponTemplate> qw = new QueryWrapper<>();
        qw.isNull("delete_time").eq("status", 0).orderByDesc("id");
        List<CouponTemplate> templates = couponTemplateMapper.selectList(qw);
        LinkedList<CouponTemplateListedVo> list = new LinkedList<>();
        for (CouponTemplate item : templates) {
            list.add(toVo(item));
        }
        return list;
    }

    @Override
    public void add(CouponTemplateCreateValidate cv, Integer adminId) {
        normalizeAndValidate(cv);
        long now = System.currentTimeMillis() / 1000L;
        CouponTemplate model = new CouponTemplate();
        model.setName(cv.getName().trim());
        model.setAmount(cv.getAmount());
        model.setMinAmount(cv.getMinAmount() != null ? cv.getMinAmount() : BigDecimal.ZERO);
        model.setValidDays(cv.getValidDays() != null ? cv.getValidDays() : 0);
        model.setStartTime(cv.getStartTime());
        model.setExpireTime(cv.getExpireTime());
        model.setRemark(cv.getRemark() != null ? cv.getRemark().trim() : "");
        model.setStatus(cv.getStatus() != null ? cv.getStatus() : 0);
        model.setAdminId(adminId);
        model.setCreateTime(now);
        model.setUpdateTime(now);
        couponTemplateMapper.insert(model);
    }

    @Override
    public void edit(CouponTemplateCreateValidate cv) {
        Assert.notNull(cv.getId(), "模板ID不能为空");
        normalizeAndValidate(cv);
        CouponTemplate model = couponTemplateMapper.selectById(cv.getId());
        if (model == null || model.getDeleteTime() != null) {
            throw new OperateException("模板不存在或已删除");
        }
        model.setName(cv.getName().trim());
        model.setAmount(cv.getAmount());
        model.setMinAmount(cv.getMinAmount() != null ? cv.getMinAmount() : BigDecimal.ZERO);
        model.setValidDays(cv.getValidDays() != null ? cv.getValidDays() : 0);
        model.setStartTime(cv.getStartTime());
        model.setExpireTime(cv.getExpireTime());
        model.setRemark(cv.getRemark() != null ? cv.getRemark().trim() : "");
        if (cv.getStatus() != null) {
            model.setStatus(cv.getStatus());
        }
        model.setUpdateTime(System.currentTimeMillis() / 1000L);
        couponTemplateMapper.updateById(model);
    }

    @Override
    public void del(Integer id) {
        CouponTemplate model = couponTemplateMapper.selectById(id);
        if (model == null || model.getDeleteTime() != null) {
            throw new OperateException("模板不存在或已删除");
        }
        long now = System.currentTimeMillis() / 1000L;
        model.setDeleteTime(now);
        model.setUpdateTime(now);
        couponTemplateMapper.updateById(model);
    }

    private void normalizeAndValidate(CouponTemplateCreateValidate cv) {
        if (StringUtils.isEmpty(cv.getName()) || cv.getName().trim().isEmpty()) {
            throw new OperateException("券名称不能为空");
        }
        if (cv.getAmount() == null || cv.getAmount().compareTo(new BigDecimal("0.01")) < 0) {
            throw new OperateException("抵扣金额必须大于0");
        }
        if (cv.getMinAmount() != null && cv.getMinAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new OperateException("满减门槛不能小于0");
        }
        if (cv.getValidDays() == null) {
            cv.setValidDays(0);
        }
        if (cv.getValidDays() < 0) {
            throw new OperateException("有效天数不能小于0");
        }
        if (cv.getValidDays() > 0) {
            // 动态有效期不落固定时间，避免两套规则同时生效
            cv.setStartTime(null);
            cv.setExpireTime(null);
        } else {
            if (cv.getStartTime() == null || cv.getExpireTime() == null) {
                throw new OperateException("固定有效期模板必须填写生效时间和过期时间");
            }
            if (cv.getExpireTime() <= cv.getStartTime()) {
                throw new OperateException("过期时间必须大于生效时间");
            }
        }
        if (cv.getStatus() != null && cv.getStatus() != 0 && cv.getStatus() != 1) {
            throw new OperateException("模板状态只能是启用或停用");
        }
    }

    private CouponTemplateListedVo toVo(CouponTemplate item) {
        CouponTemplateListedVo vo = new CouponTemplateListedVo();
        vo.setId(item.getId());
        vo.setName(item.getName());
        vo.setAmount(item.getAmount());
        vo.setMinAmount(item.getMinAmount());
        vo.setValidDays(item.getValidDays());
        vo.setStartTime(item.getStartTime());
        vo.setExpireTime(item.getExpireTime());
        vo.setStatus(item.getStatus());
        vo.setRemark(item.getRemark());
        vo.setCreateTime(item.getCreateTime() != null ? TimeUtils.timestampToDate(item.getCreateTime()) : "");
        if (item.getValidDays() != null && item.getValidDays() > 0) {
            vo.setValidDesc("领取后" + item.getValidDays() + "天有效");
        } else if (item.getStartTime() != null && item.getExpireTime() != null) {
            vo.setValidDesc(TimeUtils.timestampToDate(item.getStartTime())
                    + " ~ " + TimeUtils.timestampToDate(item.getExpireTime()));
        } else {
            vo.setValidDesc("-");
        }
        return vo;
    }
}
