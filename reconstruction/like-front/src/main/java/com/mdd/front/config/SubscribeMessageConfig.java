package com.mdd.front.config;

import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 微信订阅消息配置。模板 ID 必须与小程序侧使用的新主体模板一致，不能回退到历史模板。
 */
@Component
public class SubscribeMessageConfig {

    private static final Logger log = LoggerFactory.getLogger(SubscribeMessageConfig.class);
    private static final String DEFAULT_PAGE = "pages/records/index";

    private final String lessonReminderTemplateId;
    private final String lessonCancelTemplateId;
    private final String page;

    public SubscribeMessageConfig(
            @Value("${like.subscribe.lesson-reminder-template-id:}") String lessonReminderTemplateId,
            @Value("${like.subscribe.lesson-cancel-template-id:}") String lessonCancelTemplateId,
            @Value("${like.subscribe.page:pages/records/index}") String page) {
        this.lessonReminderTemplateId = normalize(lessonReminderTemplateId);
        this.lessonCancelTemplateId = normalize(lessonCancelTemplateId);
        this.page = normalizePage(page);
    }

    @PostConstruct
    public void warnIfUnconfigured() {
        if (!hasLessonReminderTemplate()) {
            log.warn("未配置 like.subscribe.lesson-reminder-template-id，课前提醒将跳过发送");
        }
        if (!hasLessonCancelTemplate()) {
            log.warn("未配置 like.subscribe.lesson-cancel-template-id，课程取消通知将跳过发送");
        }
    }

    public boolean hasLessonReminderTemplate() {
        return !lessonReminderTemplateId.isEmpty();
    }

    public boolean hasLessonCancelTemplate() {
        return !lessonCancelTemplateId.isEmpty();
    }

    public String getLessonReminderTemplateId() {
        return lessonReminderTemplateId;
    }

    public String getLessonCancelTemplateId() {
        return lessonCancelTemplateId;
    }

    public String getPage() {
        return page;
    }

    private static String normalize(String value) {
        return value == null ? "" : value.trim();
    }

    private static String normalizePage(String value) {
        String normalized = normalize(value);
        if (normalized.isEmpty()) {
            log.warn("未配置 like.subscribe.page，订阅消息跳转使用默认页面 {}", DEFAULT_PAGE);
            return DEFAULT_PAGE;
        }
        while (normalized.startsWith("/")) {
            normalized = normalized.substring(1);
        }
        if (!normalized.startsWith("pages/")) {
            log.warn("like.subscribe.page={} 不是合法小程序页面路径，订阅消息跳转使用默认页面 {}",
                    value, DEFAULT_PAGE);
            return DEFAULT_PAGE;
        }
        return normalized;
    }
}
