package com.mdd.front.controller;

import com.mdd.common.core.AjaxResult;
import com.mdd.common.exception.OperateException;
import com.mdd.front.LikeFrontThreadLocal;
import com.mdd.front.service.ICoachCheckinService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 教练核销闭环接口（T29.6 / #26）。
 *
 * <p>全部走 {@code /api/instructor/checkin/*}，由拦截器统一按教练身份鉴权：
 * token 主体是 {@code la_user.id}，再反查 {@code la_instructor} 得到教练 ID 注入
 * {@link LikeFrontThreadLocal#getUserId()}。家长 token 无法通过（返回既有 -1/0 错误码）。</p>
 *
 * <p>不带 {@code coachId} 入参：教练只能操作本人场次，忽略客户端传的教练 ID，
 * 避免越权。响应结构与 {@code services/db.js} 的 mock 契约保持一致。</p>
 */
@RestController
@RequestMapping(value = "/api/instructor/checkin")
@Api(tags = "教练核销闭环（场次/名单/提交/记录）")
public class CoachCheckinController {

    @Resource
    ICoachCheckinService iCoachCheckinService;

    @GetMapping(value = "/sessions")
    @ApiOperation(value = "我负责的场次列表（默认前后 30 天）")
    public AjaxResult<List<Map<String, Object>>> sessions(
            @RequestParam(value = "date", required = false) String date,
            @RequestParam(value = "pageNo", required = false) Integer pageNo,
            @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        Integer instructorId = LikeFrontThreadLocal.getUserId();
        return AjaxResult.success(iCoachCheckinService.sessions(instructorId, date, pageNo, pageSize));
    }

    @GetMapping(value = "/session")
    @ApiOperation(value = "场次详情（含预约名单与核销结果）")
    public AjaxResult<Map<String, Object>> session(@RequestParam(value = "id", required = false) String id) {
        Integer instructorId = LikeFrontThreadLocal.getUserId();
        return AjaxResult.success(iCoachCheckinService.session(instructorId, intValue(id)));
    }

    @GetMapping(value = "/roster")
    @ApiOperation(value = "点名名单（按预约）")
    public AjaxResult<List<Map<String, Object>>> roster(
            @RequestParam(value = "sessionId", required = false) String sessionId) {
        Integer instructorId = LikeFrontThreadLocal.getUserId();
        return AjaxResult.success(iCoachCheckinService.roster(instructorId, intValue(sessionId)));
    }

    @PostMapping(value = "/submit")
    @ApiOperation(value = "提交核销（逐人扣券、场次级幂等、至少一张照片）")
    public AjaxResult<Map<String, Object>> submit(@RequestBody Map<String, Object> params) {
        Integer instructorId = LikeFrontThreadLocal.getUserId();
        String role = stringValue(params.get("role"));
        if ("parent".equalsIgnoreCase(role)) {
            throw new OperateException("当前身份无权核销课程");
        }
        Map<String, Object> result = iCoachCheckinService.submit(
                instructorId,
                intValue(params.get("sessionId")),
                intList(params.get("attendedStudentIds")),
                stringList(params.get("photos")),
                stringValue(params.get("comment")));
        return AjaxResult.success(result);
    }

    @GetMapping(value = "/records")
    @ApiOperation(value = "教练上课记录（已核销场次的到场学员）")
    public AjaxResult<List<Map<String, Object>>> records(
            @RequestParam(value = "pageNo", required = false) Integer pageNo,
            @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        Integer instructorId = LikeFrontThreadLocal.getUserId();
        return AjaxResult.success(iCoachCheckinService.records(instructorId, pageNo, pageSize));
    }

    private Integer intValue(Object value) {
        if (value == null) {
            return null;
        }
        try {
            String text = String.valueOf(value).trim();
            return text.isEmpty() ? null : Integer.parseInt(text);
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    private String stringValue(Object value) {
        return value == null ? "" : String.valueOf(value).trim();
    }

    private List<Integer> intList(Object value) {
        List<Integer> list = new ArrayList<>();
        if (!(value instanceof List)) {
            return list;
        }
        for (Object item : (List<?>) value) {
            Integer parsed = intValue(item);
            if (parsed != null) {
                list.add(parsed);
            }
        }
        return list;
    }

    private List<String> stringList(Object value) {
        List<String> list = new ArrayList<>();
        if (!(value instanceof List)) {
            return list;
        }
        for (Object item : (List<?>) value) {
            if (item != null) {
                list.add(String.valueOf(item));
            }
        }
        return list;
    }
}
