package com.mdd.admin.controller;

import com.mdd.admin.export.ExportFileService;
import com.mdd.admin.export.ExportTicket;
import com.mdd.common.aop.NotLogin;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

/**
 * #214 P1：导出文件一次性下载。
 *
 * <p>后台前端 {@code window.open} 无法携带 token，因此本接口以票据为授权凭证：
 * 票据由已登录的导出接口签发（5 分钟、单次使用、绑定 adminId），下载时只校验票据。
 * 因此使用 {@link NotLogin} 跳过登录拦截；票据本身就是能力凭证。</p>
 *
 * <p>响应：200 流式返回 xlsx（{@code Content-Disposition: attachment}），下载后删除私有文件；
 * 票据缺失/格式错误 403；票据未知/已用/过期 410。</p>
 */
@RestController
@RequestMapping("/adminapi/export")
public class ExportController {

    private static final Pattern TICKET_PATTERN = Pattern.compile("[a-f0-9]{64}");
    private static final String XLSX_CONTENT_TYPE =
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    private final ExportFileService exportFileService;

    public ExportController(ExportFileService exportFileService) {
        this.exportFileService = exportFileService;
    }

    @NotLogin
    @GetMapping("/download")
    public void download(@RequestParam(value = "ticket", required = false) String ticket,
                         HttpServletResponse response) throws IOException {
        if (ticket == null || !TICKET_PATTERN.matcher(ticket).matches()) {
            writeError(response, HttpServletResponse.SC_FORBIDDEN, "下载票据无效");
            return;
        }

        ExportTicket record = exportFileService.consume(ticket);
        if (record == null) {
            writeError(response, HttpServletResponse.SC_GONE, "下载票据已失效，请重新导出");
            return;
        }

        File file = new File(record.getFilePath());
        response.reset();
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(XLSX_CONTENT_TYPE);
        response.setHeader("Content-Disposition", contentDisposition(record.getFileName()));
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("X-Content-Type-Options", "nosniff");
        response.setContentLengthLong(file.length());
        try (InputStream input = new FileInputStream(file)) {
            StreamUtils.copy(input, response.getOutputStream());
            response.flushBuffer();
        } finally {
            // 一次性文件：无论流是否完整都删除，避免含手机号的导出在私有目录堆积
            if (!file.delete() && file.exists()) {
                file.deleteOnExit();
            }
        }
    }

    private void writeError(HttpServletResponse response, int status, String message) throws IOException {
        response.reset();
        response.setStatus(status);
        response.setContentType("application/json;charset=UTF-8");
        String body = "{\"code\":" + status + ",\"msg\":\"" + message + "\"}";
        response.getOutputStream().write(body.getBytes(StandardCharsets.UTF_8));
        response.flushBuffer();
    }

    private String contentDisposition(String fileName) throws UnsupportedEncodingException {
        String safe = fileName == null || fileName.trim().isEmpty() ? "export.xlsx" : fileName.trim();
        String ascii = safe.replaceAll("[^\\x20-\\x7E]", "_").replace("\"", "_");
        String encoded = URLEncoder.encode(safe, "UTF-8").replace("+", "%20");
        return "attachment; filename=\"" + ascii + "\"; filename*=UTF-8''" + encoded;
    }
}
