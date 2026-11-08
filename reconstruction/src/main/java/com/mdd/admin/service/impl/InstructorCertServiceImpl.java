package com.mdd.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mdd.admin.service.IInstructorCertService;
import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.validate.instructor.InstructorCertSearchValidate;
import com.mdd.admin.vo.instructor.InstructorCertAuditVo;
import com.mdd.admin.vo.instructor.InstructorCertListedVo;
import com.mdd.common.core.PageResult;
import com.mdd.common.entity.Instructor;
import com.mdd.common.entity.instructor.InstructorCert;
import com.mdd.common.entity.instructor.InstructorCertAudit;
import com.mdd.common.exception.OperateException;
import com.mdd.common.mapper.InstructorMapper;
import com.mdd.common.mapper.instructor.InstructorCertAuditMapper;
import com.mdd.common.mapper.instructor.InstructorCertMapper;
import com.mdd.common.util.StringUtils;
import com.mdd.common.util.UrlUtils;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 后台教练资质审核（#49）。
 *
 * <p>审核只允许待审核（1）记录；通过后状态 2，驳回后状态 3 且原因必填。
 * 每次审核写 la_instructor_cert_audit，保留审核人、时间、意见与状态变化。</p>
 */
@Service
public class InstructorCertServiceImpl implements IInstructorCertService {

    private static final String ACTION_APPROVE = "APPROVE";
    private static final String ACTION_REJECT = "REJECT";
    private static final String SOURCE_ADMIN = "ADMIN";

    @Resource
    InstructorCertMapper certMapper;
    @Resource
    InstructorCertAuditMapper auditMapper;
    @Resource
    InstructorMapper instructorMapper;

    @Override
    public PageResult<InstructorCertListedVo> list(PageValidate pageValidate,
                                                   InstructorCertSearchValidate searchValidate) {
        QueryWrapper<InstructorCert> query = new QueryWrapper<>();
        query.isNull("delete_time");
        if (searchValidate != null) {
            if (searchValidate.getStatus() != null) {
                query.eq("status", searchValidate.getStatus());
            }
            if (searchValidate.getInstructorId() != null && searchValidate.getInstructorId() > 0) {
                query.eq("instructor_id", searchValidate.getInstructorId());
            }
            if (StringUtils.isNotEmpty(searchValidate.getType())) {
                query.eq("type", searchValidate.getType().trim());
            }
            if (StringUtils.isNotEmpty(searchValidate.getKeyword())) {
                String keyword = searchValidate.getKeyword().trim();
                query.and(w -> w.like("name", keyword).or().like("no", keyword).or().like("org", keyword));
            }
        }
        query.orderByDesc("id");

        int pageNo = pageValidate.getPage_no() == null ? 1 : pageValidate.getPage_no();
        int pageSize = pageValidate.getPage_size() == null ? 20 : pageValidate.getPage_size();
        IPage<InstructorCert> iPage = certMapper.selectPage(new Page<>(pageNo, pageSize), query);

        Map<Integer, Instructor> instructors = instructorMap(iPage.getRecords());
        List<InstructorCertListedVo> list = new ArrayList<>();
        for (InstructorCert cert : iPage.getRecords()) {
            Instructor instructor = instructors.get(cert.getInstructorId());
            list.add(toVo(cert, instructor));
        }
        return PageResult.iPageHandle(iPage.getTotal(), iPage.getCurrent(), iPage.getSize(), list);
    }

    @Override
    public Map<String, Object> detail(Long id) {
        InstructorCert cert = requireCert(id);
        Instructor instructor = instructorMapper.selectById(cert.getInstructorId());
        InstructorCertListedVo vo = toVo(cert, instructor);
        List<InstructorCertAudit> audits = auditMapper.selectList(new QueryWrapper<InstructorCertAudit>()
                .eq("cert_id", cert.getId())
                .orderByDesc("id"));
        List<InstructorCertAuditVo> auditVos = new ArrayList<>();
        for (InstructorCertAudit audit : audits) {
            auditVos.add(toAuditVo(audit));
        }
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("cert", vo);
        result.put("audits", auditVos);
        return result;
    }

    @Override
    @Transactional
    public void audit(Long id, String action, String remark, Integer adminId, String adminName, String ip) {
        InstructorCert cert = requireCert(id);
        int status = cert.getStatus() == null ? InstructorCert.STATUS_DRAFT : cert.getStatus();
        if (status != InstructorCert.STATUS_PENDING) {
            throw new OperateException("该资质不是待审核状态，无法审核");
        }
        String targetAction = StringUtils.isEmpty(action) ? "" : action.trim().toLowerCase();
        boolean reject;
        if ("approve".equals(targetAction)) {
            reject = false;
        } else if ("reject".equals(targetAction)) {
            reject = true;
        } else {
            throw new OperateException("审核结果只能是通过或驳回");
        }
        String auditRemark = remark == null ? "" : remark.trim();
        if (reject && auditRemark.isEmpty()) {
            throw new OperateException("驳回时必须填写驳回原因");
        }
        if (auditRemark.length() > 500) {
            throw new OperateException("审核意见不能超过500个字符");
        }

        Date now = new Date();
        int after = reject ? InstructorCert.STATUS_REJECTED : InstructorCert.STATUS_APPROVED;
        cert.setStatus(after);
        cert.setAuditAdminId(adminId == null ? 0 : adminId);
        cert.setAuditAdminName(adminName == null ? "" : adminName);
        cert.setAuditTime(now);
        cert.setAuditRemark(auditRemark);
        cert.setUpdateTime(now);
        certMapper.updateById(cert);

        InstructorCertAudit audit = new InstructorCertAudit();
        audit.setCertId(cert.getId());
        audit.setInstructorId(cert.getInstructorId());
        audit.setCertName(cert.getName() == null ? "" : cert.getName());
        audit.setAction(reject ? ACTION_REJECT : ACTION_APPROVE);
        audit.setSource(SOURCE_ADMIN);
        audit.setOperatorId(adminId == null ? 0 : adminId);
        audit.setOperatorName(adminName == null ? "" : adminName);
        audit.setIp(ip == null ? "" : ip);
        audit.setRemark(auditRemark);
        audit.setStatusBefore(status);
        audit.setStatusAfter(after);
        audit.setCreateTime(now);
        auditMapper.insert(audit);
    }

    private InstructorCert requireCert(Long id) {
        if (id == null || id <= 0) {
            throw new OperateException("资质ID不能为空");
        }
        InstructorCert cert = certMapper.selectOne(new QueryWrapper<InstructorCert>()
                .eq("id", id)
                .isNull("delete_time")
                .last("limit 1"));
        if (cert == null) {
            throw new OperateException("资质不存在");
        }
        return cert;
    }

    private Map<Integer, Instructor> instructorMap(List<InstructorCert> certs) {
        Map<Integer, Instructor> map = new HashMap<>();
        if (certs == null || certs.isEmpty()) {
            return map;
        }
        List<Integer> ids = new ArrayList<>();
        for (InstructorCert cert : certs) {
            if (cert.getInstructorId() != null && cert.getInstructorId() > 0 && !ids.contains(cert.getInstructorId())) {
                ids.add(cert.getInstructorId());
            }
        }
        if (ids.isEmpty()) {
            return map;
        }
        List<Instructor> instructors = instructorMapper.selectList(new QueryWrapper<Instructor>().in("id", ids));
        for (Instructor instructor : instructors) {
            map.put(instructor.getId(), instructor);
        }
        return map;
    }

    private InstructorCertListedVo toVo(InstructorCert cert, Instructor instructor) {
        InstructorCertListedVo vo = new InstructorCertListedVo();
        vo.setId(cert.getId());
        vo.setInstructorId(cert.getInstructorId());
        vo.setInstructorName(instructor == null || instructor.getName() == null ? "" : instructor.getName());
        vo.setInstructorPhone(instructor == null || instructor.getPhone() == null ? "" : instructor.getPhone());
        vo.setType(nvl(cert.getType()));
        vo.setName(nvl(cert.getName()));
        vo.setNo(nvl(cert.getNo()));
        vo.setOrg(nvl(cert.getOrg()));
        vo.setExpireStart(formatDate(cert.getExpireStart()));
        vo.setExpire(formatDate(cert.getExpire()));
        vo.setFront(UrlUtils.toAdminAbsoluteUrl(cert.getFront()));
        int status = cert.getStatus() == null ? InstructorCert.STATUS_DRAFT : cert.getStatus();
        vo.setStatus(status);
        vo.setStatusKey(statusKey(status));
        vo.setStatusLabel(statusText(status));
        vo.setAuditAdminId(cert.getAuditAdminId());
        vo.setAuditAdminName(nvl(cert.getAuditAdminName()));
        vo.setAuditTime(formatDateTime(cert.getAuditTime()));
        vo.setAuditRemark(nvl(cert.getAuditRemark()));
        vo.setSubmitTime(formatDateTime(cert.getSubmitTime()));
        vo.setCreateTime(formatDateTime(cert.getCreateTime()));
        vo.setUpdateTime(formatDateTime(cert.getUpdateTime()));
        vo.setCanAudit(status == InstructorCert.STATUS_PENDING);
        return vo;
    }

    private InstructorCertAuditVo toAuditVo(InstructorCertAudit item) {
        InstructorCertAuditVo vo = new InstructorCertAuditVo();
        vo.setId(item.getId());
        vo.setCertId(item.getCertId());
        vo.setCertName(nvl(item.getCertName()));
        vo.setAction(nvl(item.getAction()));
        vo.setActionLabel(actionLabel(item.getAction()));
        vo.setSource(nvl(item.getSource()));
        vo.setSourceLabel(sourceLabel(item.getSource()));
        vo.setOperatorId(item.getOperatorId());
        vo.setOperatorName(nvl(item.getOperatorName()));
        vo.setRemark(nvl(item.getRemark()));
        vo.setStatusBefore(item.getStatusBefore());
        vo.setStatusAfter(item.getStatusAfter());
        vo.setCreateTime(formatDateTime(item.getCreateTime()));
        return vo;
    }

    private String statusKey(int status) {
        switch (status) {
            case InstructorCert.STATUS_PENDING:
                return "pending";
            case InstructorCert.STATUS_APPROVED:
                return "approved";
            case InstructorCert.STATUS_REJECTED:
                return "rejected";
            case InstructorCert.STATUS_DRAFT:
            default:
                return "draft";
        }
    }

    private String statusText(int status) {
        switch (status) {
            case InstructorCert.STATUS_PENDING:
                return "待审核";
            case InstructorCert.STATUS_APPROVED:
                return "已通过";
            case InstructorCert.STATUS_REJECTED:
                return "已驳回";
            case InstructorCert.STATUS_DRAFT:
            default:
                return "草稿";
        }
    }

    private String actionLabel(String action) {
        if (ACTION_APPROVE.equals(action)) {
            return "审核通过";
        }
        if (ACTION_REJECT.equals(action)) {
            return "审核驳回";
        }
        if ("SUBMIT".equals(action)) {
            return "提交审核";
        }
        return action == null ? "" : action;
    }

    private String sourceLabel(String source) {
        if (SOURCE_ADMIN.equals(source)) {
            return "后台管理员";
        }
        if ("COACH".equals(source)) {
            return "教练本人";
        }
        return source == null ? "" : source;
    }

    private String formatDate(Date date) {
        return date == null ? "" : new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    private String formatDateTime(Date date) {
        return date == null ? "" : new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

    private String nvl(String text) {
        return text == null ? "" : text;
    }
}
