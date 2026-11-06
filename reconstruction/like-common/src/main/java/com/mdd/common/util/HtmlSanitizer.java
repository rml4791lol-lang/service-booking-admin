package com.mdd.common.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Safelist;

/**
 * 课程介绍等后台富文本的统一白名单清洗。
 *
 * <p>只保留基础排版、链接和图片；脚本、事件属性、iframe、表单及 style 会被移除。</p>
 */
public final class HtmlSanitizer {
    private static final Safelist COURSE_INTRO = Safelist.relaxed()
            .addAttributes(":all", "class")
            .removeTags("style", "iframe", "object", "embed", "form", "input", "button", "textarea", "select")
            .removeProtocols("a", "href", "ftp")
            .removeProtocols("img", "src", "ftp");

    private HtmlSanitizer() {
    }

    public static String sanitizeCourseIntro(String html) {
        if (html == null || html.trim().isEmpty()) {
            return "";
        }
        Document.OutputSettings outputSettings = new Document.OutputSettings().prettyPrint(false);
        return Jsoup.clean(html, "", COURSE_INTRO, outputSettings);
    }
}
