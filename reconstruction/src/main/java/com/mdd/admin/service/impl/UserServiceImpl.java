/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.excel.EasyExcel
 *  com.alibaba.excel.support.ExcelTypeEnum
 *  com.alibaba.excel.write.builder.ExcelWriterBuilder
 *  com.alibaba.fastjson2.JSONObject
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
 *  com.baomidou.mybatisplus.core.metadata.IPage
 *  com.baomidou.mybatisplus.core.toolkit.Assert
 *  com.baomidou.mybatisplus.extension.plugins.pagination.Page
 *  com.mdd.admin.service.IUserService
 *  com.mdd.admin.service.impl.UserServiceImpl
 *  com.mdd.admin.validate.commons.PageValidate
 *  com.mdd.admin.validate.user.UserAddValidate
 *  com.mdd.admin.validate.user.UserSearchValidate
 *  com.mdd.admin.validate.user.UserUpdateValidate
 *  com.mdd.admin.validate.user.UserWalletValidate
 *  com.mdd.admin.vo.user.UserListExportVo
 *  com.mdd.admin.vo.user.UserVo
 *  com.mdd.common.core.PageResult
 *  com.mdd.common.entity.RechargeOrder
 *  com.mdd.common.entity.user.User
 *  com.mdd.common.enums.ClientEnum
 *  com.mdd.common.enums.LogMoneyEnum
 *  com.mdd.common.exception.OperateException
 *  com.mdd.common.mapper.RechargeOrderMapper
 *  com.mdd.common.mapper.log.UserAccountLogMapper
 *  com.mdd.common.mapper.user.UserMapper
 *  com.mdd.common.util.ConfigUtils
 *  com.mdd.common.util.StringUtils
 *  com.mdd.common.util.TimeUtils
 *  com.mdd.common.util.ToolUtils
 *  com.mdd.common.util.UrlUtils
 *  com.mdd.common.util.YmlUtils
 *  javax.annotation.Resource
 *  org.springframework.beans.BeanUtils
 *  org.springframework.stereotype.Service
 *  org.springframework.transaction.annotation.Transactional
 */
package com.mdd.admin.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mdd.admin.service.IUserService;
import com.mdd.admin.export.ExportFileService;
import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.validate.user.UserAddValidate;
import com.mdd.admin.validate.user.UserSearchValidate;
import com.mdd.admin.validate.user.UserUpdateValidate;
import com.mdd.admin.validate.user.UserWalletValidate;
import com.mdd.admin.vo.user.CouponDetailExportVo;
import com.mdd.admin.vo.user.UserListExportVo;
import com.mdd.admin.vo.user.UserVo;
import com.mdd.common.core.PageResult;
import com.mdd.common.entity.Coupon;
import com.mdd.common.entity.RechargeOrder;
import com.mdd.common.entity.coupon.CouponFlow;
import com.mdd.common.entity.coupon.CouponGoods;
import com.mdd.common.entity.coupon.CouponInstance;
import com.mdd.common.entity.course.UserCourse;
import com.mdd.common.entity.student.Student;
import com.mdd.common.entity.user.User;
import com.mdd.common.enums.ClientEnum;
import com.mdd.common.enums.LogMoneyEnum;
import com.mdd.common.exception.OperateException;
import com.mdd.common.mapper.CouponMapper;
import com.mdd.common.mapper.RechargeOrderMapper;
import com.mdd.common.mapper.coupon.CouponFlowMapper;
import com.mdd.common.mapper.coupon.CouponGoodsMapper;
import com.mdd.common.mapper.coupon.CouponInstanceMapper;
import com.mdd.common.mapper.course.UserCourseMapper;
import com.mdd.common.mapper.log.UserAccountLogMapper;
import com.mdd.common.mapper.student.StudentMapper;
import com.mdd.common.mapper.user.UserMapper;
import com.mdd.common.util.ConfigUtils;
import com.mdd.common.util.StringUtils;
import com.mdd.common.util.TimeUtils;
import com.mdd.common.util.ToolUtils;
import com.mdd.common.util.UrlUtils;
import com.mdd.common.util.YmlUtils;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl
implements IUserService {
    @Resource
    UserMapper userMapper;
    @Resource
    UserAccountLogMapper logMoneyMapper;
    @Resource
    RechargeOrderMapper rechargeOrderMapper;
    @Resource
    StudentMapper studentMapper;
    @Resource
    UserCourseMapper userCourseMapper;
    @Resource
    CouponMapper couponMapper;
    @Resource
    CouponInstanceMapper couponInstanceMapper;
    @Resource
    CouponGoodsMapper couponGoodsMapper;
    @Resource
    CouponFlowMapper couponFlowMapper;
    @Resource
    ExportFileService exportFileService;

    public PageResult<UserVo> list(PageValidate pageValidate, UserSearchValidate searchValidate) {
        Integer pageNo = pageValidate.getPage_no();
        Integer pageSize = pageValidate.getPage_size();
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper.isNull("delete_time");
        queryWrapper.orderByDesc("id");
        queryWrapper.select(User.class, info -> !info.getColumn().equals("delete_time") && !info.getColumn().equals("update_time") && !info.getColumn().equals("password") && !info.getColumn().equals("salt"));
        if (StringUtils.isNotNull(searchValidate.getKeyword()) && StringUtils.isNotEmpty((String)searchValidate.getKeyword())) {
            String keyword = searchValidate.getKeyword();
            queryWrapper.nested(wq -> wq.like("sn", (Object)keyword).or().like("account", (Object)keyword).or().like("nickname", (Object)keyword).or().like("mobile", (Object)keyword));
        }
        if (StringUtils.isNotNull(searchValidate.getMobile()) && StringUtils.isNotEmpty((String)searchValidate.getMobile())) {
            queryWrapper.like("mobile", (Object)searchValidate.getMobile());
        }
        this.applyStudentProfileFilters(queryWrapper, searchValidate);
        this.userMapper.setSearch(queryWrapper, (Object)searchValidate, new String[]{"=:channel:int", "datetime:create_time_start-create_time_end@create_time:str"});
        IPage<User> iPage = this.userMapper.selectPage((IPage)new Page((long)pageNo.intValue(), (long)pageSize.intValue()), (Wrapper)queryWrapper);
        List userIds = iPage.getRecords().stream().map(User::getId).collect(Collectors.toList());
        LinkedHashMap<Integer, Long> lastPurchaseTimes = new LinkedHashMap<Integer, Long>();
        if (!userIds.isEmpty()) {
            List<RechargeOrder> orders = this.rechargeOrderMapper.selectList(new QueryWrapper<RechargeOrder>().in("user_id", userIds).eq("pay_status", (Object)1).gt("course_class_id", (Object)0).orderByDesc("id"));
            for (RechargeOrder order : orders) {
                if (lastPurchaseTimes.containsKey(order.getUserId())) continue;
                lastPurchaseTimes.put(order.getUserId(), order.getCreateTime());
            }
        }
        LinkedList<UserVo> list = new LinkedList<UserVo>();
        for (User user : iPage.getRecords()) {
            UserVo vo = new UserVo();
            BeanUtils.copyProperties((Object)user, (Object)vo);
            vo.setSex(user.getSex());
            vo.setChannel(ClientEnum.getMsgByCode((Integer)user.getChannel()));
            vo.setAvatar(UrlUtils.toAdminAbsoluteUrl((String)user.getAvatar()));
            vo.setLoginTime(TimeUtils.timestampToDate((Long)user.getLoginTime()));
            vo.setCreateTime(TimeUtils.timestampToDate((Long)user.getCreateTime()));
            Long lastTime = (Long)lastPurchaseTimes.get(user.getId());
            vo.setLastPurchaseTime(lastTime != null ? TimeUtils.timestampToDate((Long)lastTime) : "\u65e0");
            list.add(vo);
        }
        return PageResult.iPageHandle((Long)iPage.getTotal(), (Long)iPage.getCurrent(), (Long)iPage.getSize(), list);
    }

    public UserVo detail(Integer id) {
        Assert.notNull((Object)this.userMapper.selectOne(new QueryWrapper<User>().select(new String[]{"id"}).eq("id", (Object)id).isNull("delete_time").last("limit 1")), (String)"\u6570\u636e\u4e0d\u5b58\u5728!", (Object[])new Object[0]);
        User user = (User)this.userMapper.selectOne(new QueryWrapper<User>().select(User.class, info -> !info.getColumn().equals("delete_time") && !info.getColumn().equals("update_time") && !info.getColumn().equals("password") && !info.getColumn().equals("salt")).eq("id", (Object)id).last("limit 1"));
        UserVo vo = new UserVo();
        BeanUtils.copyProperties((Object)user, (Object)vo);
        vo.setSex(user.getSex());
        vo.setAvatar(UrlUtils.toAdminAbsoluteUrl((String)user.getAvatar()));
        vo.setChannel(ClientEnum.getMsgByCode((Integer)user.getChannel()));
        vo.setCreateTime(TimeUtils.timestampToDate((Long)user.getCreateTime()));
        if (user.getLoginTime() <= 0L) {
            vo.setLoginTime("\u65e0");
        } else {
            vo.setLoginTime(TimeUtils.timestampToDate((Long)user.getLoginTime()));
        }
        return vo;
    }

    public void edit(UserUpdateValidate updateValidate) {
        Integer id = updateValidate.getId();
        String field = updateValidate.getField();
        String value = updateValidate.getValue();
        User user = (User)this.userMapper.selectOne(new QueryWrapper<User>().eq("id", (Object)id).isNull("delete_time").last("limit 1"));
        Assert.notNull((Object)user, (String)"\u7528\u6237\u4e0d\u5b58\u5728!", (Object[])new Object[0]);
        switch (field) {
            case "account": {
                User u;
                if (!user.getAccount().equals(value) && StringUtils.isNotNull((u = (User)this.userMapper.selectOne(new QueryWrapper<User>().eq("account", (Object)value).isNull("delete_time").last("limit 1")))) && !u.getId().equals(id)) {
                    throw new OperateException("\u5f53\u524d\u8d26\u53f7\u5df2\u5b58\u5728!");
                }
                Assert.isTrue((value.length() <= 32 ? 1 : 0) != 0, (String)"\u8d26\u53f7\u4e0d\u80fd\u8d85\u8fc732\u4e2a\u5b57\u7b26", (Object[])new Object[0]);
                user.setAccount(value);
                break;
            }
            case "real_name": {
                Assert.isTrue((value.length() <= 32 ? 1 : 0) != 0, (String)"\u771f\u5b9e\u59d3\u540d\u4e0d\u80fd\u8d85\u8fc732\u4e2a\u5b57\u7b26", (Object[])new Object[0]);
                user.setRealName(value);
                break;
            }
            case "sex": {
                user.setSex(Integer.valueOf(Integer.parseInt(value)));
                break;
            }
            case "mobile": {
                if (!Pattern.matches("^[1][3-9][0-9]{9}$", value)) {
                    throw new OperateException("\u624b\u673a\u53f7\u683c\u5f0f\u4e0d\u6b63\u786e!");
                }
                User uMobile = (User)this.userMapper.selectOne(new QueryWrapper<User>().eq("mobile", (Object)value).isNull("delete_time").last("limit 1"));
                if (StringUtils.isNotNull(uMobile) && !uMobile.getId().equals(id)) {
                    throw new OperateException("\u624b\u673a\u53f7\u5df2\u88ab\u5176\u5b83\u8d26\u53f7\u7ed1\u5b9a!");
                }
                user.setMobile(value);
                break;
            }
            default: {
                throw new OperateException("\u4e0d\u88ab\u652f\u6301\u7684\u5b57\u6bb5\u7c7b\u578b!");
            }
        }
        user.setUpdateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
        this.userMapper.updateById(user);
    }

    @Transactional(rollbackFor = Exception.class)
    public void adjustWallet(UserWalletValidate userWalletValidate) {
        this.adjustWallet(userWalletValidate, null);
    }

    /**
     * 余额调整（单行）：单条接口传 adminId=null 保持原流水口径；
     * 批量导入传当前管理员ID，写入流水 extra 便于追溯操作人（#批量导入）。
     *
     * <p>批量服务通过 IUserService 代理调用本方法，保证每一行独立事务、
     * 行锁生效，单行失败只回滚该行。</p>
     */
    @Transactional(rollbackFor = Exception.class)
    public void adjustWallet(UserWalletValidate userWalletValidate, Integer adminId) {
        BigDecimal surplusAmount;
        BigDecimal amount = userWalletValidate.getNum();
        // 金额必须为正数：负数会在“减少余额”分支被减成加法（并把流水金额记成负数），
        // 等于管理员可以反向加钱/穿透余额，属于资金口径 bug（#58）。这里再兜底一次，
        // 防止绕过 Bean Validation（例如内部调用/后续新增入口）。
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new OperateException("\u53d8\u52a8\u91d1\u989d\u5fc5\u987b\u5927\u4e8e0");
        }
        if (amount.scale() > 2) {
            amount = amount.setScale(2, RoundingMode.HALF_UP);
        }
        // 批量导入才记录操作人，单条接口保持原有流水（extra=null）。
        String extra = adminId == null ? null : "{\"adminId\":" + adminId + ",\"source\":\"batch\"}";
        // 行锁：余额调整是读-改-写，两个管理员并发调整会丢更新；锁住用户行串行化。
        User user = (User)this.userMapper.selectOne(new QueryWrapper<User>().eq("id", (Object)userWalletValidate.getUserId()).isNull("delete_time").last("limit 1 for update"));
        Assert.notNull((Object)user, (String)"\u7528\u6237\u4e0d\u5b58\u5728!", (Object[])new Object[0]);
        BigDecimal userMoney = user.getUserMoney() == null ? BigDecimal.ZERO : user.getUserMoney();
        if (userWalletValidate.getAction().equals(2)) {
            surplusAmount = userMoney.subtract(amount);
            if (surplusAmount.compareTo(BigDecimal.ZERO) < 0) {
                throw new OperateException("\u7528\u6237\u4f59\u989d\u4ec5\u5269\uff1a" + userMoney);
            }
            int changeType = LogMoneyEnum.UM_DEC_ADMIN.getCode();
            this.logMoneyMapper.dec(user.getId(), Integer.valueOf(changeType), amount, Integer.valueOf(0), "", userWalletValidate.getRemark(), extra);
        } else {
            surplusAmount = userMoney.add(amount);
            int changeType = LogMoneyEnum.UM_INC_ADMIN.getCode();
            this.logMoneyMapper.add(user.getId(), Integer.valueOf(changeType), amount, Integer.valueOf(0), "", userWalletValidate.getRemark(), extra);
        }
        user.setUserMoney(surplusAmount);
        this.userMapper.updateById(user);
    }

    public void add(UserAddValidate addValidate) {
        String mobile = addValidate.getMobile();
        String password = addValidate.getPassword();
        User model = (User)this.userMapper.selectOne(new QueryWrapper<User>().select(new String[]{"id,sn,mobile"}).eq("mobile", (Object)mobile).isNull("delete_time").last("limit 1"));
        Assert.isNull(model, (String)"\u8be5\u624b\u673a\u53f7\u5df2\u6ce8\u518c, \u6362\u4e00\u4e2a\u5427!", (Object[])new Object[0]);
        Integer sn = this.__generateSn();
        String pwd = ToolUtils.makePassword((String)password);
        User user = new User();
        user.setSn(sn);
        user.setNickname(StringUtils.isNotEmpty((String)addValidate.getNickname()) ? addValidate.getNickname() : "\u5b66\u5458" + sn);
        user.setAccount(mobile);
        user.setMobile(mobile);
        user.setPassword(pwd);
        String defaultAvatar = ConfigUtils.get((String)"default_image", (String)"user_avatar", (String)"/api/static/default_avatar.png");
        user.setAvatar(defaultAvatar);
        user.setChannel(Integer.valueOf(ClientEnum.PC.getCode()));
        user.setIsNewUser(Integer.valueOf(1));
        user.setCreateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
        user.setUpdateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
        this.userMapper.insert(user);
    }

    private Integer __generateSn() {
        Integer sn;
        User snModel;
        do {
            sn = Integer.parseInt(ToolUtils.randomInt((int)8));
        } while ((snModel = (User)this.userMapper.selectOne(new QueryWrapper<User>().select(new String[]{"id,sn"}).eq("sn", (Object)sn).last("limit 1"))) != null);
        return sn;
    }

    public Map<String, Object> stats() {
        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
        Long totalCount = this.userMapper.selectCount(new QueryWrapper<User>().isNull("delete_time"));
        Long activeCount = this.userMapper.selectCount(new QueryWrapper<User>().eq("is_disable", (Object)0).isNull("delete_time"));
        long monthStart = (Long)TimeUtils.month().get(0);
        Long monthCount = this.userMapper.selectCount(new QueryWrapper<User>().ge("create_time", (Object)monthStart).isNull("delete_time"));
        Long studyingCount = activeCount;
        map.put("totalCount", totalCount);
        map.put("activeCount", activeCount);
        map.put("monthCount", monthCount);
        map.put("studyingCount", studyingCount);
        return map;
    }

    public JSONObject getExportData(PageValidate pageValidate, UserSearchValidate searchValidate) {
        Integer page = pageValidate.getPage_no();
        Integer limit = pageValidate.getPage_size();
        PageResult userVoPageResult = this.list(pageValidate, searchValidate);
        JSONObject ret = ToolUtils.getExportData((Long)userVoPageResult.getCount(), (Integer)limit, (Integer)searchValidate.getPage_start(), (Integer)searchValidate.getPage_end(), (String)"\u7528\u6237\u8bb0\u5f55\u5217\u8868");
        return ret;
    }

    public Map<String, Object> export(UserSearchValidate searchValidate) {
        PageValidate pageValidate = new PageValidate();
        if (StringUtils.isNotNull(searchValidate.getPage_start())) {
            pageValidate.setPage_no(searchValidate.getPage_start());
        } else {
            pageValidate.setPage_no(Integer.valueOf(1));
        }
        if (StringUtils.isNotNull(searchValidate.getPage_end()) && StringUtils.isNotNull(searchValidate.getPage_size())) {
            pageValidate.setPage_size(Integer.valueOf(searchValidate.getPage_end() * searchValidate.getPage_size()));
        } else {
            pageValidate.setPage_size(Integer.valueOf(20));
        }
        Boolean isAll = StringUtils.isNull(searchValidate.getPage_type()) || searchValidate.getPage_type().equals(0);
        ExportBundle bundle = this.getExcellist(isAll.booleanValue(), pageValidate, searchValidate);
        String fileName = ToolUtils.safeExportFileName(searchValidate.getFile_name());
        File target = exportFileService.allocate(fileName);
        // #168 双 sheet：主 sheet 用户记录（含未核销券金额），第二 sheet 券明细（老抵扣券 + 次数券）。
        try (ExcelWriter writer = EasyExcel.write(target).excelType(ExcelTypeEnum.XLSX).build()) {
            writer.write((Collection)bundle.getUsers(),
                    EasyExcel.writerSheet((Integer)0).sheetName("\u7528\u6237\u8bb0\u5f55").head(UserListExportVo.class).build());
            writer.write((Collection)bundle.getDetails(),
                    EasyExcel.writerSheet((Integer)1).sheetName("\u5238\u660e\u7ec6").head(CouponDetailExportVo.class).build());
        }
        return exportFileService.issue(target, fileName + ".xlsx");
    }

    /**
     * #76 学员档案列表筛选：姓名（家长真实姓名/昵称或学员姓名）、所在班级。
     * 只做只读查询，不修改任何表结构。空结果用 1=0 明确返回空列表。
     */
    private void applyStudentProfileFilters(QueryWrapper<User> queryWrapper, UserSearchValidate searchValidate) {
        String name = searchValidate.getName();
        if (StringUtils.isNotNull((Object)name) && StringUtils.isNotEmpty((String)name.trim())) {
            String keyword = name.trim();
            List<Integer> studentUserIds = this.studentMapper.selectList(
                    (Wrapper<Student>)new QueryWrapper<Student>().like("name", (Object)keyword).isNull("delete_time")
            ).stream().map(Student::getUserId).filter(id -> id != null && id > 0).distinct().collect(Collectors.toList());
            queryWrapper.and(wq -> {
                wq.like("real_name", (Object)keyword).or().like("nickname", (Object)keyword);
                if (!studentUserIds.isEmpty()) {
                    wq.or().in("id", studentUserIds);
                }
            });
        }
        Integer classId = searchValidate.getClassId();
        if (StringUtils.isNotNull((Object)classId) && classId > 0) {
            List<Integer> enrolledUserIds = this.userCourseMapper.selectList(
                    (Wrapper<UserCourse>)new QueryWrapper<UserCourse>().eq("course_class_id", (Object)classId).isNull("delete_time")
            ).stream().map(UserCourse::getUserId).filter(id -> id != null && id > 0).distinct().collect(Collectors.toList());
            if (enrolledUserIds.isEmpty()) {
                queryWrapper.apply("1 = 0");
            } else {
                queryWrapper.in("id", enrolledUserIds);
            }
        }
    }

    private ExportBundle getExcellist(boolean isAll, PageValidate pageValidate, UserSearchValidate searchValidate) {
        Integer page = pageValidate.getPage_no();
        Integer limit = pageValidate.getPage_size();
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper.isNull("delete_time");
        queryWrapper.orderByDesc("id");
        queryWrapper.select(User.class, info -> !info.getColumn().equals("delete_time") && !info.getColumn().equals("update_time") && !info.getColumn().equals("password") && !info.getColumn().equals("salt"));
        if (StringUtils.isNotNull(searchValidate.getKeyword()) && StringUtils.isNotEmpty((String)searchValidate.getKeyword())) {
            String keyword = searchValidate.getKeyword();
            queryWrapper.nested(wq -> wq.like("sn", (Object)keyword).or().like("nickname", (Object)keyword).or().like("mobile", (Object)keyword));
        }
        if (StringUtils.isNotNull(searchValidate.getMobile()) && StringUtils.isNotEmpty((String)searchValidate.getMobile())) {
            queryWrapper.like("mobile", (Object)searchValidate.getMobile());
        }
        this.applyStudentProfileFilters(queryWrapper, searchValidate);
        this.userMapper.setSearch(queryWrapper, (Object)searchValidate, new String[]{"=:channel:int", "datetime:createTimeStart-createTimeEnd@create_time:str"});
        ArrayList<UserListExportVo> retList = new ArrayList<UserListExportVo>();
        List<User> userList = new ArrayList();
        if (isAll) {
            userList = this.userMapper.selectList((Wrapper)queryWrapper);
        } else {
            IPage<User> iPage = this.userMapper.selectPage((IPage)new Page((long)page.intValue(), (long)limit.intValue()), (Wrapper)queryWrapper);
            userList = iPage.getRecords();
        }
        Map<Integer, Map<String, Object>> couponAggMap = this.loadCouponAgg(userList);
        // #168 双 sheet 对账：主 sheet 金额 = 第二 sheet 该用户所有明细行金额之和（老券 + 次数券），
        // 券数两列仍只统计老抵扣券（见 UserListExportVo 注释）。
        List<CouponDetailExportVo> couponDetails = this.loadCouponDetails(userList);
        Map<Integer, BigDecimal[]> couponAmountMap = sumCouponAmounts(couponDetails);
        for (User user : userList) {
            UserListExportVo vo = new UserListExportVo();
            BeanUtils.copyProperties((Object)user, (Object)vo);
            vo.setSex(user.getSex());
            vo.setChannel(ClientEnum.getMsgByCode((Integer)user.getChannel()));
            vo.setAvatar(UrlUtils.toAdminAbsoluteUrl((String)user.getAvatar()));
            vo.setLoginTime(TimeUtils.timestampToDate((Long)user.getLoginTime()));
            vo.setCreateTime(TimeUtils.timestampToDate((Long)user.getCreateTime()));
            Map<String, Object> couponAgg = couponAggMap.get(user.getId());
            vo.setUsedCouponCount(couponAgg == null ? 0 : aggInt(couponAgg, "used_count"));
            vo.setUnusedCouponCount(couponAgg == null ? 0 : aggInt(couponAgg, "unused_count"));
            BigDecimal[] couponAmounts = couponAmountMap.get(user.getId());
            vo.setUsedCouponAmount(couponAmounts == null ? BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP) : couponAmounts[0]);
            vo.setUnusedCouponAmount(couponAmounts == null ? BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP) : couponAmounts[1]);
            retList.add(vo);
        }
        return new ExportBundle(retList, couponDetails);
    }

    /**
     * #168 主 sheet 金额口径：把第二 sheet「券明细」按用户汇总（老券 + 次数券），
     * 保证同一用户「主 sheet 金额 = 明细行金额合计」，结果四舍五入 2 位。
     * 返回 [已核销金额, 未核销金额]。
     */
    private static Map<Integer, BigDecimal[]> sumCouponAmounts(List<CouponDetailExportVo> details) {
        Map<Integer, BigDecimal[]> sums = new HashMap<>();
        if (details == null) {
            return sums;
        }
        for (CouponDetailExportVo detail : details) {
            if (detail.getUserId() == null) {
                continue;
            }
            BigDecimal[] pair = sums.computeIfAbsent(detail.getUserId(),
                    key -> new BigDecimal[]{BigDecimal.ZERO, BigDecimal.ZERO});
            if (detail.getUsedAmount() != null) {
                pair[0] = pair[0].add(detail.getUsedAmount());
            }
            if (detail.getUnusedAmount() != null) {
                pair[1] = pair[1].add(detail.getUnusedAmount());
            }
        }
        for (BigDecimal[] pair : sums.values()) {
            pair[0] = pair[0].setScale(2, RoundingMode.HALF_UP);
            pair[1] = pair[1].setScale(2, RoundingMode.HALF_UP);
        }
        return sums;
    }

    /**
     * #168 导出内部载体：主 sheet 用户记录 + 第二 sheet 券明细。
     */
    private static class ExportBundle {
        private final List<UserListExportVo> users;
        private final List<CouponDetailExportVo> details;

        ExportBundle(List<UserListExportVo> users, List<CouponDetailExportVo> details) {
            this.users = users;
            this.details = details;
        }

        List<UserListExportVo> getUsers() {
            return this.users;
        }

        List<CouponDetailExportVo> getDetails() {
            return this.details;
        }
    }

    /**
     * #168 学员导出券数两列（老抵扣券口径）：按学员聚合 la_coupon。
     * 已核销券数=status=1 计数；未核销券数=status=0 计数（status=2 已过期不计入任一列）；
     * 软删券（delete_time 非空）不计；不按券种拆分。
     * 金额两列不走这里，改由第二 sheet「券明细」汇总（见 sumCouponAmounts），保证双 sheet 可对账。
     * 关联键：la_coupon.user_id（绑定用户ID）。按页批量分组查询，避免逐学员 N+1。
     */
    private Map<Integer, Map<String, Object>> loadCouponAgg(List<User> userList) {
        Map<Integer, Map<String, Object>> aggMap = new HashMap<>();
        if (userList == null || userList.isEmpty()) {
            return aggMap;
        }
        List<Integer> userIds = userList.stream().map(User::getId)
                .filter(id -> id != null && id > 0).distinct().collect(Collectors.toList());
        if (userIds.isEmpty()) {
            return aggMap;
        }
        List<Map<String, Object>> rows = this.couponMapper.selectMaps(new QueryWrapper<Coupon>()
                .select("user_id",
                        "SUM(CASE WHEN status = 1 THEN 1 ELSE 0 END) AS used_count",
                        "SUM(CASE WHEN status = 0 THEN 1 ELSE 0 END) AS unused_count")
                .in("user_id", userIds)
                .isNull("delete_time")
                .groupBy("user_id"));
        if (rows == null) {
            return aggMap;
        }
        for (Map<String, Object> row : rows) {
            Object userId = row.containsKey("user_id") ? row.get("user_id") : row.get("USER_ID");
            if (userId instanceof Number) {
                aggMap.put(((Number) userId).intValue(), row);
            }
        }
        return aggMap;
    }

    /**
     * #168 第二 sheet「券明细」：老抵扣券（la_coupon，delete_time 为空）与
     * 次数券（la_coupon_instance + la_coupon_goods）并列。
     *
     * <p>老券：总次数=1，剩余=status=0?1:0，已核销金额=status=1?amount:0，
     * 未核销金额=status=0?amount:0，状态 0未使用/1已使用/2已过期，核销时间=used_time。</p>
     *
     * <p>次数券：unit = goods.price / goods.times，已核销=unit×(total-remaining)，
     * 未核销仅在 status=1（有效）时取 unit×remaining，status=2 已用完/3 已过期/4 已作废
     * 的未核销=0（过期、作废的剩余次数不算未核销）；均四舍五入 2 位。
     * 核销时间取 la_coupon_flow 最后一条 type=2 的 create_time（没有则空）。</p>
     *
     * <p>本 sheet 是该用户导出金额的唯一来源：主 sheet 金额由本列表按用户汇总而来。</p>
     *
     * <p>按用户批量查询，避免逐学员 N+1；返回按用户ID、券类型（老券在前）排序。</p>
     */
    private List<CouponDetailExportVo> loadCouponDetails(List<User> userList) {
        List<CouponDetailExportVo> details = new ArrayList<>();
        if (userList == null || userList.isEmpty()) {
            return details;
        }
        Map<Integer, String> mobileMap = new LinkedHashMap<>();
        for (User user : userList) {
            if (user.getId() != null) {
                mobileMap.putIfAbsent(user.getId(), user.getMobile() == null ? "" : user.getMobile());
            }
        }
        List<Integer> userIds = new ArrayList<>(mobileMap.keySet());
        if (userIds.isEmpty()) {
            return details;
        }

        // 1) 老抵扣券
        List<Coupon> coupons = this.couponMapper.selectList(new QueryWrapper<Coupon>()
                .in("user_id", userIds)
                .isNull("delete_time")
                .orderByAsc("user_id")
                .orderByAsc("id"));
        for (Coupon coupon : coupons) {
            Integer status = coupon.getStatus();
            BigDecimal amount = coupon.getAmount() == null ? BigDecimal.ZERO : coupon.getAmount();
            CouponDetailExportVo vo = new CouponDetailExportVo();
            vo.setUserId(coupon.getUserId());
            vo.setMobile(mobileMap.getOrDefault(coupon.getUserId(), ""));
            vo.setCouponType("\u8001\u62b5\u6263\u5238");
            vo.setCouponName(coupon.getName());
            vo.setTotalTimes(1);
            vo.setRemainingTimes(Integer.valueOf(status != null && status == 0 ? 1 : 0));
            vo.setUsedAmount((status != null && status == 1 ? amount : BigDecimal.ZERO).setScale(2, RoundingMode.HALF_UP));
            vo.setUnusedAmount((status != null && status == 0 ? amount : BigDecimal.ZERO).setScale(2, RoundingMode.HALF_UP));
            vo.setStatus(this.couponStatusText(status));
            vo.setExpireTime(this.formatExportTime(coupon.getExpireTime()));
            vo.setUsedTime(this.formatExportTime(coupon.getUsedTime()));
            details.add(vo);
        }

        // 2) 次数券
        List<CouponInstance> instances = this.couponInstanceMapper.selectList(new QueryWrapper<CouponInstance>()
                .in("user_id", userIds)
                .orderByAsc("user_id")
                .orderByAsc("id"));
        if (!instances.isEmpty()) {
            Set<Integer> goodsIds = instances.stream().map(CouponInstance::getGoodsId)
                    .filter(id -> id != null && id > 0).collect(Collectors.toSet());
            Map<Integer, CouponGoods> goodsMap = new HashMap<>();
            if (!goodsIds.isEmpty()) {
                // 商品可能已软删，但券实例的金额口径仍依赖 price/times，故不按 delete_time 过滤。
                for (CouponGoods goods : this.couponGoodsMapper.selectList(
                        new QueryWrapper<CouponGoods>().in("id", goodsIds))) {
                    goodsMap.put(goods.getId(), goods);
                }
            }

            List<Integer> instanceIds = instances.stream().map(CouponInstance::getId)
                    .filter(id -> id != null).collect(Collectors.toList());
            Map<Integer, Long> lastWriteOffTimes = new HashMap<>();
            if (!instanceIds.isEmpty()) {
                List<CouponFlow> flows = this.couponFlowMapper.selectList(new QueryWrapper<CouponFlow>()
                        .in("coupon_id", instanceIds)
                        .eq("type", 2)
                        .orderByAsc("create_time")
                        .orderByAsc("id"));
                for (CouponFlow flow : flows) {
                    lastWriteOffTimes.put(flow.getCouponId(), flow.getCreateTime());
                }
            }

            for (CouponInstance instance : instances) {
                int status = instance.getStatus() == null ? 0 : instance.getStatus();
                int total = instance.getTotal() == null ? 0 : instance.getTotal();
                int remaining = instance.getRemaining() == null ? 0 : instance.getRemaining();
                CouponGoods goods = goodsMap.get(instance.getGoodsId());
                BigDecimal unit = BigDecimal.ZERO;
                if (goods != null && goods.getPrice() != null && goods.getTimes() != null && goods.getTimes() > 0) {
                    unit = goods.getPrice().divide(BigDecimal.valueOf(goods.getTimes().longValue()), 10, RoundingMode.HALF_UP);
                }
                // 已核销按已用次数算；未核销只计「仍可用」（status=1 有效）的剩余，
                // status=2 已用完 remaining=0，status=3 已过期/4 已作废的剩余不算未核销。
                BigDecimal usedAmount = unit.multiply(BigDecimal.valueOf((long) total - remaining))
                        .setScale(2, RoundingMode.HALF_UP);
                BigDecimal unusedAmount = (status == 1
                        ? unit.multiply(BigDecimal.valueOf(remaining))
                        : BigDecimal.ZERO).setScale(2, RoundingMode.HALF_UP);

                CouponDetailExportVo vo = new CouponDetailExportVo();
                vo.setUserId(instance.getUserId());
                vo.setMobile(mobileMap.getOrDefault(instance.getUserId(), ""));
                vo.setCouponType("\u6b21\u6570\u5238");
                vo.setCouponName(instance.getTitle());
                vo.setTotalTimes(total);
                vo.setRemainingTimes(remaining);
                vo.setUsedAmount(usedAmount);
                vo.setUnusedAmount(unusedAmount);
                vo.setStatus(this.instanceStatusText(instance.getStatus()));
                vo.setExpireTime(this.formatExportTime(instance.getExpireTime()));
                vo.setUsedTime(this.formatExportTime(lastWriteOffTimes.get(instance.getId())));
                details.add(vo);
            }
        }

        Comparator<CouponDetailExportVo> detailOrder = Comparator
                .comparing(CouponDetailExportVo::getUserId, Comparator.nullsLast(Integer::compareTo))
                .thenComparingInt(vo -> "\u8001\u62b5\u6263\u5238".equals(vo.getCouponType()) ? 0 : 1)
                .thenComparing(CouponDetailExportVo::getCouponName, Comparator.nullsLast(String::compareTo));
        details.sort(detailOrder);
        return details;
    }

    private String couponStatusText(Integer status) {
        if (status == null) {
            return "";
        }
        switch (status) {
            case 0:
                return "\u672a\u4f7f\u7528";
            case 1:
                return "\u5df2\u4f7f\u7528";
            case 2:
                return "\u5df2\u8fc7\u671f";
            default:
                return "\u672a\u77e5";
        }
    }

    private String instanceStatusText(Integer status) {
        if (status == null) {
            return "";
        }
        switch (status) {
            case 1:
                return "\u6709\u6548";
            case 2:
                return "\u5df2\u7528\u5b8c";
            case 3:
                return "\u5df2\u8fc7\u671f";
            case 4:
                return "\u5df2\u4f5c\u5e9f";
            default:
                return "\u672a\u77e5";
        }
    }

    private String formatExportTime(Long time) {
        return time == null || time <= 0 ? "" : TimeUtils.timestampToDate(time);
    }

    private Object aggValue(Map<String, Object> row, String key) {
        if (row.containsKey(key)) {
            return row.get(key);
        }
        String[] parts = key.split("_");
        StringBuilder camel = new StringBuilder(parts[0]);
        for (int i = 1; i < parts.length; i++) {
            if (!parts[i].isEmpty()) {
                camel.append(Character.toUpperCase(parts[i].charAt(0))).append(parts[i].substring(1));
            }
        }
        return row.get(camel.toString());
    }

    private Integer aggInt(Map<String, Object> row, String key) {
        Object value = this.aggValue(row, key);
        return value instanceof Number ? ((Number) value).intValue() : 0;
    }
}

