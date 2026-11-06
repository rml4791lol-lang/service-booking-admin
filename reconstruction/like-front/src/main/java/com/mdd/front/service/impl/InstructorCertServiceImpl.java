package com.mdd.front.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mdd.common.entity.Instructor;
import com.mdd.common.entity.instructor.InstructorCert;
import com.mdd.common.entity.instructor.InstructorCertAudit;
import com.mdd.common.exception.OperateException;
import com.mdd.common.mapper.InstructorMapper;
import com.mdd.common.mapper.instructor.InstructorCertAuditMapper;
import com.mdd.common.mapper.instructor.InstructorCertMapper;
import com.mdd.common.util.StringUtils;
import com.mdd.common.util.UrlUtils;
import com.mdd.front.service.IInstructorCertService;
import com.mdd.front.validate.instructor.InstructorCertSaveValidate;
import com.mdd.front.vo.instructor.InstructorCertVo;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 教练端资质证件（#49）。
 *
 * <p>数据口径：
 * <ul>
 *   <li>一条资质 = la_instructor_cert 一行，字段完整持久化；</li>
 *   <li>草稿（0）可反复保存，提交后（1）教练不可编辑；</li>
 *   <li>后台通过（2）/驳回（3）后，驳回可修改并重新提交，历史意见在
 *       la_instructor_cert_audit 中保留；</li>
 *   <li>图片入库统一相对路径，输出转绝对地址，与教练资料图片口径一致。</li>
 * </ul>
 */
@Service
public class InstructorCertServiceImpl implements IInstructorCertService {

    private static final String ACTION_SUBMIT = "SUBMIT";
    private static final String SOURCE_COACH = "COACH";

    @Resource
    InstructorCertMapper certMapper;
    @Resource
    InstructorCertAuditMapper auditMapper;
    @Resource
    InstructorMapper instructorMapper;

    @Override
    public List<InstructorCertVo> list(Integer instructorId) {
        requireInstructor(instructorId);
        List<InstructorCert> certs = certMapper.selectList(new QueryWrapper<InstructorCert>()
                .eq("instructor_id", instructorId)
                .isNull("delete_time")
                .orderByDesc("id"));
        List<InstructorCertVo> list = new ArrayList<>();
        for (InstructorCert cert : certs) {
            list.add(toVo(cert));
        }
        return list;
    }

    @Override
    public InstructorCertVo save(Integer instructorId, InstructorCertSaveValidate validate) {
        Instructor instructor = requireInstructor(instructorId);
        Date now = new Date();
        InstructorCert cert;
        if (validate.getId() == null || validate.getId() <= 0) {
            cert = new InstructorCert();
            cert.setInstructorId(instructorId);
            cert.setStatus(InstructorCert.STATUS_DRAFT);
            cert.setCreateTime(now);
        } else {
            cert = requireOwnedCert(validate.getId(), instructorId);
            int status = cert.getStatus() == null ? InstructorCert.STATUS_DRAFT : cert.getStatus();
            if (status == InstructorCert.STATUS_PENDING) {
                throw new OperateException("该资质正在审核中，暂不可修改");
            }
            if (status == InstructorCert.STATUS_APPROVED) {
                throw new OperateException("该资质已通过审核，如有更新请新增一条资质");
            }
        }
        String type = trim(validate.getType());
        String name = trim(validate.getName());
        if (type.isEmpty()) {
            throw new OperateException("请选择证书类型");
        }
        if (name.isEmpty()) {
            throw new OperateException("请填写证书名称");
        }
        Date expireStart = parseDate(validate.getExpireStart(), "有效期起");
        Date expire = parseDate(validate.getExpire(), "有效期止");
        if (expireStart != null && expire != null && expire.before(expireStart)) {
            throw new OperateException("有效期止不能早于有效期起");
        }

        cert.setType(type);
        cert.setName(name);
        cert.setNo(trim(validate.getNo()));
        cert.setOrg(trim(validate.getOrg()));
        cert.setExpireStart(expireStart);
        cert.setExpire(expire);
        String front = trim(validate.getFront());
        cert.setFront(front.isEmpty() ? "" : UrlUtils.toRelativeUrl(front));
        cert.setUpdateTime(now);
        if (cert.getId() == null) {
            certMapper.insert(cert);
        } else {
            certMapper.updateById(cert);
        }
        return toVo(certMapper.selectById(cert.getId()));
    }

    @Override
    @Transactional
    public Map<String, Object> submit(Integer instructorId, List<Long> ids) {
        Instructor instructor = requireInstructor(instructorId);
        if (ids == null || ids.isEmpty()) {
            throw new OperateException("请选择要提交的资质");
        }
        Set<Long> uniqueIds = new LinkedHashSet<>();
        for (Long id : ids) {
            if (id != null && id > 0) {
                uniqueIds.add(id);
            }
        }
        if (uniqueIds.isEmpty()) {
            throw new OperateException("请选择要提交的资质");
        }
        if (uniqueIds.size() > 20) {
            throw new OperateException("一次最多提交20张资质");
        }

        Date now = new Date();
        List<Long> submitted = new ArrayList<>();
        String operatorName = StringUtils.isNotEmpty(instructor.getName()) ? instructor.getName() : "教练本人";
        for (Long id : uniqueIds) {
            InstructorCert cert = requireOwnedCert(id, instructorId);
            int status = cert.getStatus() == null ? InstructorCert.STATUS_DRAFT : cert.getStatus();
            if (status == InstructorCert.STATUS_PENDING) {
                throw new OperateException("「" + cert.getName() + "」正在审核中，请勿重复提交");
            }
            if (status == InstructorCert.STATUS_APPROVED) {
                throw new OperateException("「" + cert.getName() + "」已通过审核，无需重复提交");
            }
            validateComplete(cert);
            cert.setStatus(InstructorCert.STATUS_PENDING);
            cert.setSubmitTime(now);
            cert.setAuditAdminId(0);
            cert.setAuditAdminName("");
            cert.setAuditTime(null);
            cert.setAuditRemark("");
            cert.setUpdateTime(now);
            certMapper.updateById(cert);
            recordAudit(cert, ACTION_SUBMIT, SOURCE_COACH, instructorId, operatorName, "", "提交审核", status, InstructorCert.STATUS_PENDING);
            submitted.add(cert.getId());
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("count", submitted.size());
        result.put("ids", submitted);
        return result;
    }

    private Instructor requireInstructor(Integer instructorId) {
        if (instructorId == null || instructorId <= 0) {
            throw new OperateException("请先登录教练账号");
        }
        Instructor instructor = instructorMapper.selectOne(new QueryWrapper<Instructor>()
                .eq("id", instructorId)
                .isNull("delete_time")
                .last("limit 1"));
        if (instructor == null) {
            throw new OperateException("教练信息不存在");
        }
        return instructor;
    }

    private InstructorCert requireOwnedCert(Long certId, Integer instructorId) {
        InstructorCert cert = certMapper.selectOne(new QueryWrapper<InstructorCert>()
                .eq("id", certId)
                .eq("instructor_id", instructorId)
                .isNull("delete_time")
                .last("limit 1"));
        if (cert == null) {
            throw new OperateException("资质不存在或无权操作");
        }
        return cert;
    }

    private void validateComplete(InstructorCert cert) {
        if (trim(cert.getType()).isEmpty() || trim(cert.getName()).isEmpty()
                || trim(cert.getNo()).isEmpty() || trim(cert.getOrg()).isEmpty()
                || trim(cert.getFront()).isEmpty()) {
            throw new OperateException("「" + trim(cert.getName()) + "」信息不完整，请补全证书编号、发证机构、有效期和照片后再提交");
        }
        if (cert.getExpireStart() == null || cert.getExpire() == null) {
            throw new OperateException("「" + trim(cert.getName()) + "」请补全有效期起止后再提交");
        }
        if (cert.getExpire().before(cert.getExpireStart())) {
            throw new OperateException("「" + trim(cert.getName()) + "」有效期止不能早于有效期起");
        }
    }

    private void recordAudit(InstructorCert cert, String action, String source, Integer operatorId,
                             String operatorName, String ip, String remark,
                             Integer statusBefore, Integer statusAfter) {
        InstructorCertAudit audit = new InstructorCertAudit();
        audit.setCertId(cert.getId());
        audit.setInstructorId(cert.getInstructorId());
        audit.setCertName(trim(cert.getName()));
        audit.setAction(action);
        audit.setSource(source);
        audit.setOperatorId(operatorId == null ? 0 : operatorId);
        audit.setOperatorName(operatorName == null ? "" : operatorName);
        audit.setIp(ip == null ? "" : ip);
        audit.setRemark(remark == null ? "" : (remark.length() > 500 ? remark.substring(0, 500) : remark));
        audit.setStatusBefore(statusBefore == null ? 0 : statusBefore);
        audit.setStatusAfter(statusAfter == null ? 0 : statusAfter);
        audit.setCreateTime(new Date());
        auditMapper.insert(audit);
    }

    private InstructorCertVo toVo(InstructorCert cert) {
        if (cert == null) {
            return null;
        }
        InstructorCertVo vo = new InstructorCertVo();
        vo.setId(cert.getId());
        vo.setInstructorId(cert.getInstructorId());
        vo.setType(nvl(cert.getType()));
        vo.setName(nvl(cert.getName()));
        vo.setNo(nvl(cert.getNo()));
        vo.setOrg(nvl(cert.getOrg()));
        vo.setExpireStart(formatDate(cert.getExpireStart()));
        vo.setExpire(formatDate(cert.getExpire()));
        vo.setFront(UrlUtils.toAbsoluteUrl(cert.getFront()));
        int status = cert.getStatus() == null ? InstructorCert.STATUS_DRAFT : cert.getStatus();
        vo.setStatusCode(status);
        vo.setStatus(statusKey(status));
        vo.setStatusText(statusText(status));
        vo.setAuditRemark(nvl(cert.getAuditRemark()));
        vo.setAuditAdminName(nvl(cert.getAuditAdminName()));
        vo.setAuditTime(formatDateTime(cert.getAuditTime()));
        vo.setSubmitTime(formatDateTime(cert.getSubmitTime()));
        vo.setCreateTime(formatDateTime(cert.getCreateTime()));
        vo.setUpdateTime(formatDateTime(cert.getUpdateTime()));
        vo.setEditable(status == InstructorCert.STATUS_DRAFT || status == InstructorCert.STATUS_REJECTED);
        return vo;
    }

    static String statusKey(int status) {
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

    static String statusText(int status) {
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

    private Date parseDate(String text, String label) {
        if (StringUtils.isEmpty(text)) {
            return null;
        }
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            format.setLenient(false);
            return format.parse(text.trim());
        } catch (ParseException e) {
            throw new OperateException(label + "格式不正确，应为yyyy-MM-dd");
        }
    }

    private String formatDate(Date date) {
        return date == null ? "" : new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    private String formatDateTime(Date date) {
        return date == null ? "" : new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

    private String trim(String text) {
        return text == null ? "" : text.trim();
    }

    private String nvl(String text) {
        return text == null ? "" : text;
    }
}
