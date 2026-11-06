/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.aop.Log
 *  com.mdd.admin.controller.CrontabController
 *  com.mdd.admin.service.ICrontabService
 *  com.mdd.admin.validate.commons.IdValidate
 *  com.mdd.admin.validate.commons.PageValidate
 *  com.mdd.admin.validate.crontab.CrontabCreateValidate
 *  com.mdd.admin.validate.crontab.CrontabUpdateValidate
 *  com.mdd.admin.vo.CrontabDetailVo
 *  com.mdd.admin.vo.CrontabListedVo
 *  com.mdd.common.core.AjaxResult
 *  com.mdd.common.core.PageResult
 *  com.mdd.common.exception.OperateException
 *  com.mdd.common.validator.annotation.IDMust
 *  io.swagger.annotations.Api
 *  io.swagger.annotations.ApiOperation
 *  javax.annotation.Resource
 *  org.quartz.CronExpression
 *  org.quartz.SchedulerException
 *  org.springframework.validation.annotation.Validated
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.RequestBody
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RequestParam
 *  org.springframework.web.bind.annotation.RestController
 */
package com.mdd.admin.controller;

import com.mdd.admin.aop.Log;
import com.mdd.admin.service.ICrontabService;
import com.mdd.admin.validate.commons.IdValidate;
import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.validate.crontab.CrontabCreateValidate;
import com.mdd.admin.validate.crontab.CrontabUpdateValidate;
import com.mdd.admin.vo.CrontabDetailVo;
import com.mdd.admin.vo.CrontabListedVo;
import com.mdd.common.core.AjaxResult;
import com.mdd.common.core.PageResult;
import com.mdd.common.exception.OperateException;
import com.mdd.common.validator.annotation.IDMust;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.quartz.CronExpression;
import org.quartz.SchedulerException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/*
 * Exception performing whole class analysis ignored.
 */
@RestController
@RequestMapping(value={"/adminapi/crontab.crontab"})
@Api(tags={"\u8ba1\u5212\u4efb\u52a1\u7ba1\u7406"})
public class CrontabController {
    @Resource
    ICrontabService iCrontabService;

    @GetMapping(value={"/lists"})
    @ApiOperation(value="\u8ba1\u5212\u4efb\u52a1\u5217\u8868")
    public AjaxResult<PageResult<CrontabListedVo>> list(@Validated PageValidate pageValidate) {
        PageResult list = this.iCrontabService.list(pageValidate);
        return AjaxResult.success(list);
    }

    @GetMapping(value={"/detail"})
    @ApiOperation(value="\u8ba1\u5212\u4efb\u52a1\u8be6\u60c5")
    public AjaxResult<Object> detail(@Validated @IDMust @RequestParam(value="id") Integer id) {
        CrontabDetailVo vo = this.iCrontabService.detail(id);
        return AjaxResult.success(vo);
    }

    @Log(title="\u8ba1\u5212\u4efb\u52a1\u65b0\u589e")
    @PostMapping(value={"/add"})
    @ApiOperation(value="\u8ba1\u5212\u4efb\u52a1\u65b0\u589e")
    public AjaxResult<Object> add(@Validated @RequestBody CrontabCreateValidate createValidate) throws SchedulerException {
        this.iCrontabService.add(createValidate);
        return AjaxResult.success();
    }

    @Log(title="\u8ba1\u5212\u4efb\u52a1\u7f16\u8f91")
    @PostMapping(value={"/edit"})
    @ApiOperation(value="\u8ba1\u5212\u4efb\u52a1\u7f16\u8f91")
    public AjaxResult<Object> edit(@Validated @RequestBody CrontabUpdateValidate updateValidate) throws SchedulerException {
        this.iCrontabService.edit(updateValidate);
        return AjaxResult.success();
    }

    @Log(title="\u8ba1\u5212\u4efb\u52a1\u5220\u9664")
    @PostMapping(value={"/delete"})
    @ApiOperation(value="\u8ba1\u5212\u4efb\u52a1\u5220\u9664")
    public AjaxResult<Object> del(@Validated @RequestBody IdValidate idValidate) throws SchedulerException {
        this.iCrontabService.del(idValidate.getId());
        return AjaxResult.success();
    }

    @GetMapping(value={"/expression"})
    @ApiOperation(value="\u8ba1\u5212\u4efb\u52a1\u5217\u8868")
    public AjaxResult<List<Map<String, String>>> expression(@Validated @RequestParam(value="expression") String expression) {
        return AjaxResult.success(CrontabController.getExpression((String)expression));
    }

    public static List<Map<String, String>> getExpression(String cronExpression) {
        try {
            Date nextValidTimeAfter;
            CronExpression cron = new CronExpression(cronExpression);
            ArrayList<Date> dates = new ArrayList<Date>();
            Date date = new Date();
            for (int i = 0; i < 5 && (nextValidTimeAfter = cron.getNextValidTimeAfter(date)) != null; ++i) {
                dates.add(nextValidTimeAfter);
                date = nextValidTimeAfter;
            }
            ArrayList<Map<String, String>> lists = new ArrayList<Map<String, String>>();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (int i = 0; i < dates.size(); ++i) {
                HashMap<String, String> entry = new HashMap<String, String>();
                entry.put("time", String.valueOf(i + 1));
                entry.put("date", sdf.format((Date)dates.get(i)));
                lists.add(entry);
            }
            HashMap<String, String> extraEntry = new HashMap<String, String>();
            extraEntry.put("time", "x");
            extraEntry.put("date", "\u2026\u2026");
            lists.add(extraEntry);
            return lists;
        }
        catch (Exception e) {
            throw new OperateException(e.getMessage());
        }
    }
}

