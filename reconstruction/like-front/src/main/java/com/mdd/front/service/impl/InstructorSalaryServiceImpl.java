/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
 *  com.baomidou.mybatisplus.core.metadata.IPage
 *  com.baomidou.mybatisplus.extension.plugins.pagination.Page
 *  com.mdd.common.entity.Instructor
 *  com.mdd.common.entity.Message
 *  com.mdd.common.entity.course.CourseClass
 *  com.mdd.common.entity.instructor.InstructorAccountLog
 *  com.mdd.common.entity.instructor.InstructorBank
 *  com.mdd.common.entity.instructor.InstructorWithdrawal
 *  com.mdd.common.exception.OperateException
 *  com.mdd.common.mapper.InstructorMapper
 *  com.mdd.common.mapper.MessageMapper
 *  com.mdd.common.mapper.course.CourseClassMapper
 *  com.mdd.common.mapper.course.CourseLessonLogMapper
 *  com.mdd.common.mapper.instructor.InstructorAccountLogMapper
 *  com.mdd.common.mapper.instructor.InstructorBankMapper
 *  com.mdd.common.mapper.instructor.InstructorWithdrawalMapper
 *  com.mdd.common.util.TimeUtils
 *  com.mdd.common.util.UrlUtils
 *  com.mdd.front.service.IInstructorSalaryService
 *  com.mdd.front.service.impl.InstructorSalaryServiceImpl
 *  javax.annotation.Resource
 *  org.springframework.stereotype.Service
 *  org.springframework.transaction.annotation.Transactional
 */
package com.mdd.front.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mdd.common.entity.Instructor;
import com.mdd.common.entity.Message;
import com.mdd.common.entity.course.CourseClass;
import com.mdd.common.entity.instructor.InstructorAccountLog;
import com.mdd.common.entity.instructor.InstructorBank;
import com.mdd.common.entity.instructor.InstructorWithdrawal;
import com.mdd.common.exception.OperateException;
import com.mdd.common.mapper.InstructorMapper;
import com.mdd.common.mapper.MessageMapper;
import com.mdd.common.mapper.course.CourseClassMapper;
import com.mdd.common.mapper.course.CourseLessonLogMapper;
import com.mdd.common.mapper.instructor.InstructorAccountLogMapper;
import com.mdd.common.mapper.instructor.InstructorBankMapper;
import com.mdd.common.mapper.instructor.InstructorWithdrawalMapper;
import com.mdd.common.util.TimeUtils;
import com.mdd.common.util.UrlUtils;
import com.mdd.front.service.IInstructorSalaryService;
import com.mdd.common.util.ParamUtils;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.mdd.common.entity.course.CourseLessonLog;

@Service
public class InstructorSalaryServiceImpl
implements IInstructorSalaryService {
    @Resource
    InstructorMapper instructorMapper;
    @Resource
    InstructorAccountLogMapper instructorAccountLogMapper;
    @Resource
    InstructorBankMapper instructorBankMapper;
    @Resource
    InstructorWithdrawalMapper instructorWithdrawalMapper;
    @Resource
    CourseClassMapper courseClassMapper;
    @Resource
    CourseLessonLogMapper courseLessonLogMapper;
    @Resource
    MessageMapper messageMapper;

    public Map<String, Object> classIncomeDetail(Integer userId, String month) {
        Instructor instructor = (Instructor)this.instructorMapper.selectById((Serializable)userId);
        if (instructor == null) {
            throw new OperateException("\u6559\u7ec3\u4fe1\u606f\u4e0d\u5b58\u5728");
        }
        LocalDate startDate = ParamUtils.monthStart(month);
        LocalDate endDate = startDate.plusMonths(1L).minusDays(1L);
        long startTimestamp = startDate.atStartOfDay(ZoneId.systemDefault()).toEpochSecond();
        long endTimestamp = endDate.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toEpochSecond();
        List<InstructorAccountLog> logs = this.instructorAccountLogMapper.selectList(new QueryWrapper<InstructorAccountLog>().eq("instructor_id", (Object)userId).eq("type", (Object)1).ge("create_time", (Object)startTimestamp).le("create_time", (Object)endTimestamp));
        LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>();
        BigDecimal totalIncome = BigDecimal.ZERO;
        ArrayList list = new ArrayList();
        if (logs.isEmpty()) {
            result.put("totalIncome", totalIncome);
            result.put("list", list);
            return result;
        }
        HashMap<Integer, BigDecimal> classAmountMap = new HashMap<Integer, BigDecimal>();
        HashMap<Integer, Integer> classHoursMap = new HashMap<Integer, Integer>();
        for (InstructorAccountLog log : logs) {
            Integer classId = log.getRelationId();
            if (classId == null || classId <= 0) continue;
            BigDecimal amount = log.getAmount() != null ? log.getAmount() : BigDecimal.ZERO;
            totalIncome = totalIncome.add(amount);
            classAmountMap.put(classId, classAmountMap.getOrDefault(classId, BigDecimal.ZERO).add(amount));
            classHoursMap.put(classId, classHoursMap.getOrDefault(classId, 0) + 1);
        }
        if (classAmountMap.isEmpty()) {
            result.put("totalIncome", totalIncome);
            result.put("list", list);
            return result;
        }
        List<CourseClass> classes = this.courseClassMapper.selectList(new QueryWrapper<CourseClass>().in("id", classAmountMap.keySet()));
        for (CourseClass cls : classes) {
            LinkedHashMap<String, Object> item = new LinkedHashMap<String, Object>();
            item.put("classId", cls.getId());
            item.put("name", cls.getName());
            item.put("image", UrlUtils.toAbsoluteUrl((String)cls.getImage()));
            item.put("hours", classHoursMap.getOrDefault(cls.getId(), 0));
            item.put("rate", cls.getFeePerLesson() != null ? cls.getFeePerLesson() : BigDecimal.ZERO);
            BigDecimal settled = classAmountMap.getOrDefault(cls.getId(), BigDecimal.ZERO);
            item.put("settled", settled);
            item.put("total", settled);
            list.add(item);
        }
        result.put("totalIncome", totalIncome);
        result.put("list", list);
        return result;
    }

    public Map<String, Object> incomeSummary(Integer userId, String month) {
        Instructor instructor = (Instructor)this.instructorMapper.selectById((Serializable)userId);
        if (instructor == null) {
            throw new OperateException("\u6559\u7ec3\u4fe1\u606f\u4e0d\u5b58\u5728");
        }
        LocalDate startDate = ParamUtils.monthStart(month);
        LocalDate endDate = startDate.plusMonths(1L).minusDays(1L);
        long startTimestamp = startDate.atStartOfDay(ZoneId.systemDefault()).toEpochSecond();
        long endTimestamp = endDate.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toEpochSecond();
        List<InstructorAccountLog> logs = this.instructorAccountLogMapper.selectList(new QueryWrapper<InstructorAccountLog>().eq("instructor_id", (Object)userId).in("type", Arrays.asList(1, 2, 3, 4, 5)).ge("create_time", (Object)startTimestamp).le("create_time", (Object)endTimestamp));
        BigDecimal classIncome = BigDecimal.ZERO;
        BigDecimal performanceBonus = BigDecimal.ZERO;
        BigDecimal subsidyBonus = BigDecimal.ZERO;
        BigDecimal otherIncome = BigDecimal.ZERO;
        BigDecimal deductions = BigDecimal.ZERO;
        for (InstructorAccountLog log : logs) {
            BigDecimal amt = log.getAmount() != null ? log.getAmount().abs() : BigDecimal.ZERO;
            switch (log.getType()) {
                case 1: {
                    classIncome = classIncome.add(amt);
                    break;
                }
                case 2: {
                    performanceBonus = performanceBonus.add(amt);
                    break;
                }
                case 3: {
                    subsidyBonus = subsidyBonus.add(amt);
                    break;
                }
                case 4: {
                    otherIncome = otherIncome.add(amt);
                    break;
                }
                case 5: {
                    deductions = deductions.add(amt);
                }
            }
        }
        BigDecimal grossIncome = classIncome.add(performanceBonus).add(subsidyBonus).add(otherIncome);
        BigDecimal netIncome = grossIncome.subtract(deductions);
        LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>();
        result.put("classIncome", classIncome);
        result.put("performanceBonus", performanceBonus);
        result.put("subsidyBonus", subsidyBonus);
        result.put("otherIncome", otherIncome);
        result.put("deductions", deductions);
        result.put("netIncome", netIncome);
        return result;
    }

    public Map<String, Object> overview(Integer userId) {
        Instructor instructor = (Instructor)this.instructorMapper.selectById((Serializable)userId);
        if (instructor == null) {
            throw new OperateException("\u6559\u7ec3\u4fe1\u606f\u4e0d\u5b58\u5728");
        }
        LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>();
        result.put("balance", instructor.getBalance() != null ? instructor.getBalance() : BigDecimal.ZERO);
        result.put("totalWithdrawn", instructor.getTotalWithdrawn() != null ? instructor.getTotalWithdrawn() : BigDecimal.ZERO);
        Long activeClassCount = this.courseClassMapper.selectCount(new QueryWrapper<CourseClass>().eq("instructor_id", (Object)instructor.getId()).eq("status", (Object)1));
        result.put("activeClassCount", activeClassCount != null ? activeClassCount.intValue() : 0);
        Long totalLessonCount = this.courseLessonLogMapper.selectCount(new QueryWrapper<CourseLessonLog>().eq("instructor_id", (Object)instructor.getId()).isNull("delete_time"));
        result.put("totalLessonCount", totalLessonCount != null ? totalLessonCount.intValue() : 0);
        result.put("ratingScore", instructor.getRatingScore() != null ? instructor.getRatingScore() : BigDecimal.ZERO);
        InstructorBank bank = (InstructorBank)this.instructorBankMapper.selectOne(new QueryWrapper<InstructorBank>().eq("instructor_id", (Object)instructor.getId()).eq("is_default", (Object)1).isNull("delete_time").last("limit 1"));
        if (bank != null) {
            LinkedHashMap<String, String> bankInfo = new LinkedHashMap<String, String>();
            bankInfo.put("bankName", bank.getBankName());
            bankInfo.put("cardNo", "***" + bank.getCardNo().substring(Math.max(0, bank.getCardNo().length() - 4)));
            bankInfo.put("cardHolder", bank.getCardHolder());
            result.put("bankInfo", bankInfo);
        } else {
            result.put("bankInfo", null);
        }
        return result;
    }

    public Map<String, Object> history(Integer userId, Integer pageNo, Integer pageSize, Integer type) {
        Instructor instructor = (Instructor)this.instructorMapper.selectById((Serializable)userId);
        if (instructor == null) {
            throw new OperateException("\u6559\u7ec3\u4fe1\u606f\u4e0d\u5b58\u5728");
        }
        IPage<InstructorAccountLog> page = new Page<InstructorAccountLog>((long)pageNo.intValue(), (long)pageSize.intValue());
        QueryWrapper<InstructorAccountLog> qw = new QueryWrapper<InstructorAccountLog>();
        qw.eq("instructor_id", (Object)instructor.getId());
        if (type != null && type > 0) {
            qw.eq("type", (Object)type);
        }
        qw.orderByDesc("id");
        page = this.instructorAccountLogMapper.selectPage((IPage)page, (Wrapper)qw);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ArrayList list = new ArrayList();
        for (InstructorAccountLog log : page.getRecords()) {
            LinkedHashMap<String, Object> item = new LinkedHashMap<String, Object>();
            item.put("id", log.getId());
            item.put("type", log.getType());
            item.put("typeName", this.getTypeName(log.getType()));
            item.put("amount", log.getAmount());
            item.put("beforeBalance", log.getBeforeBalance());
            item.put("afterBalance", log.getAfterBalance());
            item.put("remark", log.getRemark());
            item.put("createTime", log.getCreateTime() != null ? sdf.format(new Date(log.getCreateTime() * 1000L)) : "");
            list.add(item);
        }
        LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>();
        result.put("count", page.getTotal());
        result.put("lists", list);
        result.put("pageNo", pageNo);
        result.put("pageSize", pageSize);
        return result;
    }

    public Map<String, Object> getBankInfo(Integer userId) {
        Instructor instructor = (Instructor)this.instructorMapper.selectById((Serializable)userId);
        if (instructor == null) {
            throw new OperateException("\u6559\u7ec3\u4fe1\u606f\u4e0d\u5b58\u5728");
        }
        InstructorBank bank = (InstructorBank)this.instructorBankMapper.selectOne(new QueryWrapper<InstructorBank>().eq("instructor_id", (Object)instructor.getId()).eq("is_default", (Object)1).isNull("delete_time").last("limit 1"));
        LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>();
        if (bank != null) {
            result.put("id", bank.getId());
            result.put("bankName", bank.getBankName());
            result.put("bankBranch", bank.getBankBranch());
            result.put("cardNo", bank.getCardNo());
            result.put("cardHolder", bank.getCardHolder());
        }
        return result;
    }

    public void saveBankInfo(Integer userId, Map<String, String> params) {
        Instructor instructor = (Instructor)this.instructorMapper.selectById((Serializable)userId);
        if (instructor == null) {
            throw new OperateException("\u6559\u7ec3\u4fe1\u606f\u4e0d\u5b58\u5728");
        }
        String bankName = params.get("bankName");
        String bankBranch = params.getOrDefault("bankBranch", "");
        String cardNo = params.get("cardNo");
        String cardHolder = params.get("cardHolder");
        if (bankName == null || cardNo == null || cardHolder == null) {
            throw new OperateException("\u94f6\u884c\u540d\u79f0\u3001\u5361\u53f7\u3001\u6301\u5361\u4eba\u4e0d\u80fd\u4e3a\u7a7a");
        }
        InstructorBank existBank = (InstructorBank)this.instructorBankMapper.selectOne(new QueryWrapper<InstructorBank>().eq("instructor_id", (Object)instructor.getId()).isNull("delete_time").last("limit 1"));
        long now = TimeUtils.timestamp();
        if (existBank != null) {
            existBank.setBankName(bankName);
            existBank.setBankBranch(bankBranch);
            existBank.setCardNo(cardNo);
            existBank.setCardHolder(cardHolder);
            existBank.setUpdateTime(Long.valueOf(now));
            this.instructorBankMapper.updateById(existBank);
        } else {
            InstructorBank bank = new InstructorBank();
            bank.setInstructorId(instructor.getId());
            bank.setBankName(bankName);
            bank.setBankBranch(bankBranch);
            bank.setCardNo(cardNo);
            bank.setCardHolder(cardHolder);
            bank.setIsDefault(Integer.valueOf(1));
            bank.setCreateTime(Long.valueOf(now));
            bank.setUpdateTime(Long.valueOf(now));
            this.instructorBankMapper.insert(bank);
        }
    }

    @Transactional(rollbackFor={Exception.class})
    public void applyWithdrawal(Integer userId, BigDecimal amount) {
        BigDecimal balance;
        Instructor instructor = (Instructor)this.instructorMapper.selectById((Serializable)userId);
        if (instructor == null) {
            throw new OperateException("\u6559\u7ec3\u4fe1\u606f\u4e0d\u5b58\u5728");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new OperateException("\u63d0\u73b0\u91d1\u989d\u5fc5\u987b\u5927\u4e8e0");
        }
        BigDecimal bigDecimal = balance = instructor.getBalance() != null ? instructor.getBalance() : BigDecimal.ZERO;
        if (amount.compareTo(balance) > 0) {
            throw new OperateException("\u63d0\u73b0\u91d1\u989d\u4e0d\u80fd\u8d85\u8fc7\u53ef\u7528\u4f59\u989d");
        }
        InstructorBank bank = (InstructorBank)this.instructorBankMapper.selectOne(new QueryWrapper<InstructorBank>().eq("instructor_id", (Object)instructor.getId()).eq("is_default", (Object)1).isNull("delete_time").last("limit 1"));
        if (bank == null) {
            throw new OperateException("\u8bf7\u5148\u7ed1\u5b9a\u94f6\u884c\u5361");
        }
        long now = TimeUtils.timestamp();
        BigDecimal beforeBalance = balance;
        BigDecimal afterBalance = balance.subtract(amount);
        instructor.setBalance(afterBalance);
        this.instructorMapper.updateById(instructor);
        InstructorWithdrawal withdrawal = new InstructorWithdrawal();
        withdrawal.setInstructorId(instructor.getId());
        withdrawal.setSn("W" + System.currentTimeMillis());
        withdrawal.setAmount(amount);
        withdrawal.setBankName(bank.getBankName());
        withdrawal.setBankCard(bank.getCardNo());
        withdrawal.setCardHolder(bank.getCardHolder());
        withdrawal.setStatus(Integer.valueOf(0));
        withdrawal.setCreateTime(Long.valueOf(now));
        withdrawal.setUpdateTime(Long.valueOf(now));
        this.instructorWithdrawalMapper.insert(withdrawal);
        InstructorAccountLog accountLog = new InstructorAccountLog();
        accountLog.setInstructorId(instructor.getId());
        accountLog.setType(Integer.valueOf(6));
        accountLog.setAmount(amount.negate());
        accountLog.setBeforeBalance(beforeBalance);
        accountLog.setAfterBalance(afterBalance);
        accountLog.setRelationId(withdrawal.getId());
        accountLog.setRemark("\u7533\u8bf7\u63d0\u73b0: \u00a5" + amount);
        accountLog.setCreateTime(Long.valueOf(now));
        this.instructorAccountLogMapper.insert(accountLog);
        Message message = new Message();
        message.setType(Integer.valueOf(2));
        message.setTitle("\u6559\u7ec3\u63d0\u73b0\u7533\u8bf7");
        message.setContent("\u6559\u7ec3\u63d0\u73b0\u7533\u8bf7,\u91d1\u989d\u4e3a:" + amount + ",\u8bf7\u53ca\u65f6\u5904\u7406");
        message.setSenderId(instructor.getId());
        message.setSenderType("instructor");
        message.setSenderName(instructor.getName());
        message.setReceiverId(Integer.valueOf(0));
        message.setReceiverType("admin");
        message.setIsRead(Integer.valueOf(0));
        message.setCreateTime(Long.valueOf(now));
        message.setUpdateTime(Long.valueOf(now));
        this.messageMapper.insert(message);
    }

    public Map<String, Object> withdrawalList(Integer userId, Integer pageNo, Integer pageSize) {
        Instructor instructor = (Instructor)this.instructorMapper.selectById((Serializable)userId);
        if (instructor == null) {
            throw new OperateException("\u6559\u7ec3\u4fe1\u606f\u4e0d\u5b58\u5728");
        }
        IPage<InstructorWithdrawal> page = new Page<InstructorWithdrawal>((long)pageNo.intValue(), (long)pageSize.intValue());
        QueryWrapper<InstructorAccountLog> qw = new QueryWrapper<InstructorAccountLog>();
        qw.eq("instructor_id", (Object)instructor.getId());
        qw.orderByDesc("id");
        page = this.instructorWithdrawalMapper.selectPage((IPage)page, (Wrapper)qw);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ArrayList list = new ArrayList();
        for (InstructorWithdrawal w : page.getRecords()) {
            LinkedHashMap<String, Object> item = new LinkedHashMap<String, Object>();
            item.put("id", w.getId());
            item.put("sn", w.getSn());
            item.put("amount", w.getAmount());
            item.put("bankName", w.getBankName());
            item.put("bankCard", "***" + w.getBankCard().substring(Math.max(0, w.getBankCard().length() - 4)));
            item.put("status", w.getStatus());
            item.put("statusName", this.getStatusName(w.getStatus()));
            item.put("auditRemark", w.getAuditRemark());
            item.put("createTime", w.getCreateTime() != null ? sdf.format(new Date(w.getCreateTime() * 1000L)) : "");
            list.add(item);
        }
        LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>();
        result.put("count", page.getTotal());
        result.put("lists", list);
        result.put("pageNo", pageNo);
        result.put("pageSize", pageSize);
        return result;
    }

    private String getTypeName(Integer type) {
        if (type == null) {
            return "\u672a\u77e5";
        }
        switch (type) {
            case 1: {
                return "\u8bfe\u65f6\u6536\u5165";
            }
            case 2: {
                return "\u7ee9\u6548\u5956\u52b1";
            }
            case 3: {
                return "\u5956\u91d1\u8865\u8d34";
            }
            case 4: {
                return "\u5176\u4ed6\u6536\u5165";
            }
            case 5: {
                return "\u6263\u6b3e\u9879";
            }
            case 6: {
                return "\u7533\u8bf7\u63d0\u73b0";
            }
            case 7: {
                return "\u63d0\u73b0\u9000\u56de";
            }
        }
        return "\u672a\u77e5";
    }

    private String getStatusName(Integer status) {
        if (status == null) {
            return "\u672a\u77e5";
        }
        switch (status) {
            case 0: {
                return "\u5f85\u5ba1\u6838";
            }
            case 1: {
                return "\u5df2\u6253\u6b3e";
            }
            case 2: {
                return "\u5df2\u62d2\u7edd";
            }
        }
        return "\u672a\u77e5";
    }
}

