package com.mdd.admin.export;

import com.mdd.admin.controller.ExportController;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * #214 P1：下载接口状态码与流式响应断言器。
 *
 * <p>seam：{@link ExportController#download(String, HttpServletResponse)}。用动态代理
 * 模拟响应，断言：无票据/错票据 403、未知/重复/过期 410、有效票据 200 + attachment +
 * 原始字节 + 下载后删除私有文件。</p>
 */
public final class ExportDownloadControllerTest {

    private static int passed;
    private static int failed;

    public static void main(String[] args) throws Exception {
        missingOrMalformedTicketIsForbidden();
        unknownTicketIsGone();
        validTicketStreamsOnceAndDeletesFile();

        System.out.println();
        System.out.println("passed=" + passed + " failed=" + failed);
        if (failed > 0) {
            System.exit(1);
        }
    }

    private static void missingOrMalformedTicketIsForbidden() throws Exception {
        System.out.println("[无 ticket / 格式错误 → 403]");
        Fixture fx = new Fixture();
        try {
            FakeResponse missing = new FakeResponse();
            fx.controller.download(null, missing.proxy());
            check("缺少 ticket 返回 403", missing.status == 403, String.valueOf(missing.status));

            FakeResponse blank = new FakeResponse();
            fx.controller.download("", blank.proxy());
            check("空 ticket 返回 403", blank.status == 403, String.valueOf(blank.status));

            FakeResponse malformed = new FakeResponse();
            fx.controller.download("../../etc/passwd", malformed.proxy());
            check("非 64hex ticket 返回 403", malformed.status == 403, String.valueOf(malformed.status));
        } finally {
            fx.close();
        }
    }

    private static void unknownTicketIsGone() throws Exception {
        System.out.println("[未知 ticket → 410]");
        Fixture fx = new Fixture();
        try {
            FakeResponse response = new FakeResponse();
            fx.controller.download(repeat('f', 64), response.proxy());
            check("未知 ticket 返回 410", response.status == 410, String.valueOf(response.status));
        } finally {
            fx.close();
        }
    }

    private static void validTicketStreamsOnceAndDeletesFile() throws Exception {
        System.out.println("[有效 ticket → 200 + attachment + 原始字节；重复用 → 410]");
        Fixture fx = new Fixture();
        try {
            byte[] content = "PK\u0003\u0004fake-xlsx-bytes".getBytes(StandardCharsets.UTF_8);
            File file = fx.service.allocate("学员导出");
            Files.write(file.toPath(), content);
            Map<String, Object> issued = fx.service.issue(file, "学员导出.xlsx");
            String ticket = String.valueOf(issued.get("ticket"));

            FakeResponse response = new FakeResponse();
            fx.controller.download(ticket, response.proxy());

            check("有效 ticket 返回 200", response.status == 200, String.valueOf(response.status));
            check("Content-Type 为 xlsx",
                    response.contentType != null && response.contentType.contains("spreadsheetml"),
                    String.valueOf(response.contentType));
            String disposition = response.headers.get("Content-Disposition");
            check("Content-Disposition 为 attachment",
                    disposition != null && disposition.startsWith("attachment;"), String.valueOf(disposition));
            check("Content-Disposition 含 RFC5987 中文文件名",
                    disposition != null && disposition.contains("filename*=UTF-8''"), String.valueOf(disposition));
            check("响应字节与写入文件一致", Arrays.equals(content, response.body.toByteArray()),
                    "expected=" + content.length + " actual=" + response.body.size());
            check("下载后私有文件已删除", !file.exists(), file.getAbsolutePath());

            FakeResponse replay = new FakeResponse();
            fx.controller.download(ticket, replay.proxy());
            check("同一 ticket 重复下载返回 410", replay.status == 410, String.valueOf(replay.status));
        } finally {
            fx.close();
        }
    }

    // ---- fixture --------------------------------------------------------------

    private static final class Fixture {
        final Path root;
        final Path uploads;
        final ExportFileServiceTest.MemoryStore store = new ExportFileServiceTest.MemoryStore();
        final ExportFileService service;
        final ExportController controller;

        Fixture() throws IOException {
            root = Files.createTempDirectory("lxt-export-controller-");
            uploads = Files.createTempDirectory("lxt-export-controller-uploads-");
            service = new ExportFileService(store, root.toString(), uploads.toString(), System::currentTimeMillis);
            controller = new ExportController(service);
        }

        void close() throws IOException {
            deleteTree(root);
            deleteTree(uploads);
        }
    }

    /** 只实现下载接口用到的响应方法；其余返回类型默认值。 */
    private static final class FakeResponse implements InvocationHandler {
        int status = 200;
        String contentType;
        final Map<String, String> headers = new LinkedHashMap<String, String>();
        final ByteArrayOutputStream body = new ByteArrayOutputStream();

        HttpServletResponse proxy() {
            return (HttpServletResponse) Proxy.newProxyInstance(
                    HttpServletResponse.class.getClassLoader(),
                    new Class<?>[]{HttpServletResponse.class}, this);
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) {
            String name = method.getName();
            if ("setStatus".equals(name)) {
                status = (Integer) args[0];
                return null;
            }
            if ("getStatus".equals(name)) {
                return status;
            }
            if ("setContentType".equals(name)) {
                contentType = (String) args[0];
                return null;
            }
            if ("getContentType".equals(name)) {
                return contentType;
            }
            if ("setCharacterEncoding".equals(name) || "setBufferSize".equals(name)) {
                return null;
            }
            if ("setHeader".equals(name) || "addHeader".equals(name)) {
                headers.put(String.valueOf(args[0]), String.valueOf(args[1]));
                return null;
            }
            if ("getHeader".equals(name)) {
                return headers.get(String.valueOf(args[0]));
            }
            if ("setContentLengthLong".equals(name)) {
                return null;
            }
            if ("setContentLength".equals(name)) {
                return null;
            }
            if ("getOutputStream".equals(name)) {
                return new ServletOutputStream() {
                    @Override
                    public boolean isReady() {
                        return true;
                    }

                    @Override
                    public void setWriteListener(WriteListener writeListener) {
                        // no-op
                    }

                    @Override
                    public void write(int b) {
                        body.write(b);
                    }
                };
            }
            if ("flushBuffer".equals(name) || "isCommitted".equals(name)) {
                return "isCommitted".equals(name) ? Boolean.FALSE : null;
            }
            if ("reset".equals(name)) {
                status = 200;
                contentType = null;
                headers.clear();
                body.reset();
                return null;
            }
            return defaultValue(method.getReturnType());
        }

        private static Object defaultValue(Class<?> type) {
            if (type == boolean.class) {
                return false;
            }
            if (type == int.class) {
                return 0;
            }
            if (type == long.class) {
                return 0L;
            }
            if (type == short.class) {
                return (short) 0;
            }
            if (type == byte.class) {
                return (byte) 0;
            }
            if (type == char.class) {
                return (char) 0;
            }
            if (type == float.class) {
                return 0F;
            }
            if (type == double.class) {
                return 0D;
            }
            return null;
        }
    }

    private static String repeat(char c, int count) {
        StringBuilder sb = new StringBuilder(count);
        for (int i = 0; i < count; i++) {
            sb.append(c);
        }
        return sb.toString();
    }

    private static void deleteTree(Path dir) throws IOException {
        if (dir == null || !Files.exists(dir)) {
            return;
        }
        try (Stream<Path> stream = Files.walk(dir)) {
            stream.sorted(Comparator.reverseOrder()).forEach(path -> {
                try {
                    Files.deleteIfExists(path);
                } catch (IOException ignored) {
                    // 清理失败不影响断言
                }
            });
        }
    }

    private static void check(String name, boolean ok, String detail) {
        if (ok) {
            passed += 1;
            System.out.println("  ✓ " + name + (detail.isEmpty() ? "" : "（" + detail + "）"));
        } else {
            failed += 1;
            System.out.println("  ✗ " + name + (detail.isEmpty() ? "" : "（" + detail + "）"));
        }
    }
}
