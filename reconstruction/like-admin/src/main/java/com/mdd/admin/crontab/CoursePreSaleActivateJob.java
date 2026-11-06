package com.mdd.admin.crontab;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.mdd.common.entity.course.CourseClass;
import com.mdd.common.mapper.course.CourseClassMapper;
import com.mdd.common.util.TimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 预售课程转进行中（每天 3 点）。
 *
 * <p>漏跑补偿（2026-11-07 定时任务审计）：进程重启后 Quartz 触发器由
 * {@code CrontabServiceImpl#init()} 从 la_dev_crontab 重建，停机期间错过的触发点不会补跑；
 * 本任务每天只跑一次，漏跑一天会让预售班多停留在「预报名」状态 24 小时。查询条件
 * {@code start_date <= now} 本身是「补齐所有到期项」的扫描，所以下一次执行会把漏掉的
 * 班级一并激活——前提是更新必须幂等且不覆盖并发改动，因此这里：</p>
 * <ul>
 *   <li>只 select id，不再整行加载；</li>
 *   <li>更新走 {@code UPDATE ... SET status=1, update_time=? WHERE id=? AND status=2
 *       AND is_delete=0 AND start_date<=?} 的行级条件闸门：重复执行/并发激活都只影响 0 行，
 *       也不会把管理员并发改动的价格、教练、名称回写成旧快照。</li>
 * </ul>
 */
@Component(value = "coursePreSaleActivateJob")
public class CoursePreSaleActivateJob {
    private static final Logger log = LoggerFactory.getLogger(CoursePreSaleActivateJob.class);

    /** 班级状态：1=进行中。 */
    private static final int STATUS_ACTIVE = 1;
    /** 班级状态：2=预报名（预售）。 */
    private static final int STATUS_PRE_SALE = 2;

    @Resource
    CourseClassMapper courseClassMapper;

    public void execute() {
        long now = TimeUtils.timestamp();
        List<CourseClass> targets = courseClassMapper.selectList(new QueryWrapper<CourseClass>()
                .select("id")
                .eq("status", STATUS_PRE_SALE)
                .eq("is_delete", 0)
                .le("start_date", now)
                .orderByAsc("id"));
        if (targets == null || targets.isEmpty()) {
            log.debug("预售课程激活任务：今日无需激活的预售课程");
            return;
        }
        int activated = 0;
        for (CourseClass courseClass : targets) {
            if (courseClass.getId() == null) {
                continue;
            }
            int updated = courseClassMapper.update(null, new UpdateWrapper<CourseClass>()
                    .eq("id", courseClass.getId())
                    .eq("status", STATUS_PRE_SALE)
                    .eq("is_delete", 0)
                    .le("start_date", now)
                    .set("status", STATUS_ACTIVE)
                    .set("update_time", (int) now));
            if (updated > 0) {
                activated++;
            }
        }
        if (activated > 0) {
            log.info("预售课程激活任务执行完成，候选：{}，激活：{}", targets.size(), activated);
        }
    }
}
