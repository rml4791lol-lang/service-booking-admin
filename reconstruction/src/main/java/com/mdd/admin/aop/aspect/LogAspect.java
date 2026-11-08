/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson2.JSON
 *  com.mdd.admin.LikeAdminThreadLocal
 *  com.mdd.admin.aop.Log
 *  com.mdd.admin.aop.aspect.LogAspect
 *  com.mdd.admin.aop.aspect.RequestType
 *  com.mdd.common.entity.admin.Admin
 *  com.mdd.common.entity.system.OperationLog
 *  com.mdd.common.mapper.admin.AdminMapper
 *  com.mdd.common.mapper.system.SystemLogOperateMapper
 *  com.mdd.common.util.IpUtils
 *  com.mdd.common.util.RequestUtils
 *  javax.annotation.Resource
 *  javax.servlet.http.HttpServletRequest
 *  org.aspectj.lang.JoinPoint
 *  org.aspectj.lang.ProceedingJoinPoint
 *  org.aspectj.lang.annotation.AfterThrowing
 *  org.aspectj.lang.annotation.Around
 *  org.aspectj.lang.annotation.Aspect
 *  org.aspectj.lang.annotation.Pointcut
 *  org.aspectj.lang.reflect.MethodSignature
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.stereotype.Component
 *  org.springframework.web.context.request.RequestContextHolder
 *  org.springframework.web.context.request.ServletRequestAttributes
 *  org.springframework.web.multipart.MultipartFile
 *  org.springframework.web.multipart.support.StandardMultipartHttpServletRequest
 */
package com.mdd.admin.aop.aspect;

import com.alibaba.fastjson2.JSON;
import com.mdd.admin.LikeAdminThreadLocal;
import com.mdd.admin.aop.Log;
import com.mdd.admin.aop.aspect.RequestType;
import com.mdd.common.entity.admin.Admin;
import com.mdd.common.entity.system.OperationLog;
import com.mdd.common.exception.BaseException;
import com.mdd.common.mapper.admin.AdminMapper;
import com.mdd.common.mapper.system.SystemLogOperateMapper;
import com.mdd.common.util.IpUtils;
import com.mdd.common.util.RequestUtils;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Aspect
@Component
public class LogAspect {
    @Resource
    SystemLogOperateMapper systemLogOperateMapper;
    @Resource
    AdminMapper adminMapper;
    private static final Logger log = LoggerFactory.getLogger(LogAspect.class);
    private static final ThreadLocal<Long> threadLocal = new ThreadLocal();
    private final ExecutorService executor = Executors.newSingleThreadExecutor(runnable -> {
        Thread thread = new Thread(runnable, "operation-log-writer");
        thread.setDaemon(true);
        return thread;
    });

    @Pointcut(value="@annotation(com.mdd.admin.aop.Log)")
    private void pointCutMethodController() {
    }

    @Around(value="pointCutMethodController()")
    public Object doAroundService(ProceedingJoinPoint joinPoint) throws Throwable {
        threadLocal.set(System.currentTimeMillis());
        Object result = joinPoint.proceed();
        this.recordLog((Object)joinPoint, null);
        return result;
    }

    @AfterThrowing(value="@annotation(controllerLog)", throwing="e")
    public void doAfterThrowing(JoinPoint joinPoint, Log controllerLog, Exception e) {
        this.recordLog((Object)joinPoint, e);
    }

    private void recordLog(Object joinPointObj, Exception e) {
        try {
            long beginTime = (Long)threadLocal.get();
            long endTime = System.currentTimeMillis();
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
            if (requestAttributes != null) {
                HttpServletRequest request = requestAttributes.getRequest();
                Integer adminId = LikeAdminThreadLocal.getAdminId();
                ProceedingJoinPoint joinPoint = (ProceedingJoinPoint)joinPointObj;
                MethodSignature signature = (MethodSignature)joinPoint.getSignature();
                Method method = signature.getMethod();
                Log logAnnotation = method.getAnnotation(Log.class);
                String className = joinPoint.getTarget().getClass().getName();
                String methodName = joinPoint.getSignature().getName();
                String queryString = request.getQueryString();
                Object[] args = joinPoint.getArgs();
                String params = "";
                if (args.length > 0) {
                    if ("POST".equals(request.getMethod())) {
                        if (RequestType.File.equals((Object)logAnnotation.requestType())) {
                            // 参数第 0 个是 MultipartFile[]，不是请求对象；此前强转 StandardMultipartHttpServletRequest
                            // 必然 ClassCastException，导致所有上传类操作的审计日志丢失（#58）。
                            MultipartHttpServletRequest multipartRequest = request instanceof MultipartHttpServletRequest
                                    ? (MultipartHttpServletRequest) request : null;
                            params = multipartRequest == null ? "" : multipartRequest.getMultiFileMap().values().stream().map(m -> m.stream().map(MultipartFile::getOriginalFilename).collect(Collectors.joining(","))).collect(Collectors.joining(","));
                        } else {
                            params = JSON.toJSONString((Object)args);
                        }
                    } else if ("GET".equals(request.getMethod())) {
                        params = queryString;
                    }
                }
                params = SensitiveDataMasker.maskJson(params);
                String error = "";
                int status = 1;
                if (e != null) {
                    error = e.getMessage();
                    // BaseException 只保存 msg、不调用 super(msg)，getMessage() 为 null；
                    // 审计留痕需要记录真实的业务拒绝原因（如删除被引用保护拦截）。
                    if ((error == null || error.isEmpty()) && e instanceof BaseException) {
                        error = ((BaseException)e).getMsg();
                    }
                    status = 2;
                }
                String adminName = "";
                Admin admin = (Admin)this.adminMapper.selectById((Serializable)adminId);
                if (admin != null) {
                    adminName = admin.getName();
                }
                OperationLog model = new OperationLog();
                model.setAdminId(adminId);
                // 兼容现网 la_operation_log.admin_name/account varchar(16)；超长时截断，
                // 完整身份仍由 admin_id 保留，避免严格模式下整条审计静默丢失。
                model.setAdminName(truncate(adminName, 16));
                model.setAccount(admin == null ? "" : truncate(admin.getAccount(), 16));
                model.setAction(logAnnotation.title());
                model.setIp(IpUtils.getIpAddress());
                model.setType(request.getMethod());
                model.setUrl(RequestUtils.route());
                model.setParams(params);
                model.setResult(SensitiveDataMasker.maskJson(error));
                model.setCreateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
                this.executor.submit(() -> {
                    try {
                        this.systemLogOperateMapper.insert(model);
                    } catch (Exception insertError) {
                        // 审计写入失败不能静默丢失，至少留下可检索的应用日志。
                        log.error("操作日志落库失败 adminId={} action={} url={}",
                                model.getAdminId(), model.getAction(), model.getUrl(), insertError);
                    }
                });
            }
        }
        catch (Exception ex) {
            log.error("\u5f02\u5e38\u4fe1\u606f:{}", (Object)ex.getMessage());
        }
    }

    private String truncate(String value, int maxLength) {
        if (value == null || value.length() <= maxLength) {
            return value;
        }
        return value.substring(0, maxLength);
    }
}
