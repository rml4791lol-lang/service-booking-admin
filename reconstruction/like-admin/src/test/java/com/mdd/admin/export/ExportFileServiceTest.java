package com.mdd.admin.export;

import com.mdd.admin.LikeAdminThreadLocal;
import com.mdd.common.exception.OperateException;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

/**
 * #214 P1：导出文件私有落盘 + 一次性下载票据。
 *
 * <p>本仓库离线 Maven 仓库没有 junit / surefire provider，按仓库惯例写成
 * 「带 main 的纯 Java 断言器」，由 tools/export/run-export-ticket-test.sh 执行。</p>
 *
 * <p>seam：{@link ExportFileService} 的公共方法（allocate / issue / consume）与
 * {@link com.mdd.admin.controller.ExportController#download(String, javax.servlet.http.HttpServletResponse)}。
 * 断言「私有目录、文件名安全化、票据随机/绑定 adminId/5 分钟/单次使用、越界路径拒绝」。</p>
 */
public final class ExportFileServiceTest {

    private static final long NOW = 1_700_000_000_000L;
    private static int passed;
    private static int failed;

    public static void main(String[] args) throws Exception {
        allocatesUnderPrivateRoot();
        sanitizesDownloadNameAndBlocksTraversal();
        issuesRandomSingleUseTicketBoundToAdmin();
        rejectsExpiredTicket();
        rejectsTamperedTicketPath();
        refusesExportRootInsidePublicUploads();

        System.out.println();
        System.out.println("passed=" + passed + " failed=" + failed);
        if (failed > 0) {
            System.exit(1);
        }
    }

    // ---- 1. 私有目录：物理文件不落到公开 uploads --------------------------------

    private static void allocatesUnderPrivateRoot() throws Exception {
        System.out.println("[私有目录：物理文件只落在 /tmp/lxt-export，不碰 uploads]");
        Path root = Files.createTempDirectory("lxt-export-private-");
        Path uploads = Files.createTempDirectory("lxt-export-uploads-");
        try {
            ExportFileService service = service(root, uploads, new AtomicLong(NOW));
            File target = service.allocate("学员导出");

            String rootCanonical = root.toFile().getCanonicalPath() + File.separator;
            check("文件位于私有导出根目录内",
                    target.getCanonicalPath().startsWith(rootCanonical), target.getCanonicalPath());
            check("物理文件名为随机 UUID（不暴露业务名）",
                    target.getName().matches("[a-f0-9]{32}\\.xlsx"), target.getName());
            check("父目录已创建", target.getParentFile().isDirectory(), target.getParent());
            check("未在公开 uploads 下产生文件",
                    countFiles(uploads) == 0, String.valueOf(countFiles(uploads)));
        } finally {
            deleteTree(root);
            deleteTree(uploads);
        }
    }

    // ---- 2. 下载文件名安全化：路径穿越字符被清洗 -------------------------------

    private static void sanitizesDownloadNameAndBlocksTraversal() throws Exception {
        System.out.println("[下载文件名：file_name 路径穿越被清洗]");
        Path root = Files.createTempDirectory("lxt-export-private-");
        Path uploads = Files.createTempDirectory("lxt-export-uploads-");
        try {
            ExportFileService service = service(root, uploads, new AtomicLong(NOW));
            File target = service.allocate("../../evil/name");
            Files.write(target.toPath(), "xlsx".getBytes(StandardCharsets.UTF_8));
            Map<String, Object> data = service.issue(target, "../../evil/name");

            String download = String.valueOf(data.get("file"));
            check("下载名不含路径分隔符", !download.contains("/") && !download.contains("\\"), download);
            check("下载名不含 ..", !download.contains(".."), download);
            check("下载名保留 .xlsx 后缀", download.endsWith(".xlsx"), download);
            check("下载名不为空", download.length() > ".xlsx".length(), download);
        } finally {
            deleteTree(root);
            deleteTree(uploads);
        }
    }

    // ---- 3. 票据：随机、绑定 adminId、5 分钟、单次使用 -------------------------

    private static void issuesRandomSingleUseTicketBoundToAdmin() throws Exception {
        System.out.println("[票据：随机 64 hex / 绑定 adminId / expiresIn=300 / 单次使用]");
        Path root = Files.createTempDirectory("lxt-export-private-");
        Path uploads = Files.createTempDirectory("lxt-export-uploads-");
        MemoryStore store = new MemoryStore();
        AtomicLong clock = new AtomicLong(NOW);
        try {
            ExportFileService service = new ExportFileService(store, root.toString(), uploads.toString(), clock::get);
            File first = service.allocate("报表");
            Files.write(first.toPath(), "xlsx-bytes".getBytes(StandardCharsets.UTF_8));

            LikeAdminThreadLocal.put("adminId", "42");
            Map<String, Object> issued = service.issue(first, "学员报表.xlsx");
            LikeAdminThreadLocal.remove();

            String ticket = String.valueOf(issued.get("ticket"));
            check("ticket 为 64 位 hex", ticket.matches("[a-f0-9]{64}"), ticket);
            check("file 为下载文件名", "学员报表.xlsx".equals(issued.get("file")), String.valueOf(issued.get("file")));
            check("expiresIn=300 秒", Integer.valueOf(300).equals(issued.get("expiresIn")),
                    String.valueOf(issued.get("expiresIn")));

            ExportTicket stored = store.map.get(ticket);
            check("票据记录绑定 adminId=42", stored != null && Integer.valueOf(42).equals(stored.getAdminId()),
                    stored == null ? "null" : String.valueOf(stored.getAdminId()));
            check("票据记录指向私有文件",
                    stored != null && first.getAbsolutePath().equals(stored.getFilePath()),
                    stored == null ? "null" : stored.getFilePath());

            ExportTicket consumed = service.consume(ticket);
            check("首次消费成功", consumed != null, consumed == null ? "null" : consumed.getFileName());
            check("消费后票据不可再用", service.consume(ticket) == null, "已再次消费");

            File second = service.allocate("报表2");
            Files.write(second.toPath(), "xlsx".getBytes(StandardCharsets.UTF_8));
            Map<String, Object> issued2 = service.issue(second, "报表2.xlsx");
            check("两次签发票据不同",
                    !ticket.equals(String.valueOf(issued2.get("ticket"))), String.valueOf(issued2.get("ticket")));
        } finally {
            LikeAdminThreadLocal.remove();
            deleteTree(root);
            deleteTree(uploads);
        }
    }

    // ---- 4. 过期票据拒绝 -------------------------------------------------------

    private static void rejectsExpiredTicket() throws Exception {
        System.out.println("[过期票据：超过 5 分钟拒绝]");
        Path root = Files.createTempDirectory("lxt-export-private-");
        Path uploads = Files.createTempDirectory("lxt-export-uploads-");
        MemoryStore store = new MemoryStore();
        AtomicLong clock = new AtomicLong(NOW);
        try {
            ExportFileService service = new ExportFileService(store, root.toString(), uploads.toString(), clock::get);
            File target = service.allocate("过期");
            Files.write(target.toPath(), "xlsx".getBytes(StandardCharsets.UTF_8));
            Map<String, Object> issued = service.issue(target, "过期.xlsx");
            clock.addAndGet(301_000L);
            check("过期后消费返回 null",
                    service.consume(String.valueOf(issued.get("ticket"))) == null, "已过期");
        } finally {
            deleteTree(root);
            deleteTree(uploads);
        }
    }

    // ---- 5. 票据被篡改成越界路径 → 拒绝 ---------------------------------------

    private static void rejectsTamperedTicketPath() throws Exception {
        System.out.println("[越界路径：票据记录的 filePath 不在私有根目录内 → 拒绝]");
        Path root = Files.createTempDirectory("lxt-export-private-");
        Path uploads = Files.createTempDirectory("lxt-export-uploads-");
        Path outside = Files.createTempFile("lxt-export-outside-", ".xlsx");
        MemoryStore store = new MemoryStore();
        try {
            ExportFileService service = service(root, uploads, new AtomicLong(NOW));
            String ticket = repeat('a', 64);
            ExportTicket forged = new ExportTicket();
            forged.setTicket(ticket);
            forged.setAdminId(42);
            forged.setFilePath(outside.toString());
            forged.setFileName("outside.xlsx");
            forged.setExpireAt(NOW + 60_000L);
            store.put(forged, 300);

            check("越界路径票据被拒绝", service.consume(ticket) == null, "路径已越界");
        } finally {
            deleteTree(root);
            deleteTree(uploads);
            Files.deleteIfExists(outside);
        }
    }

    // ---- 6. 配置误把私有目录放进公开 uploads → 拒绝启动导出 --------------------

    private static void refusesExportRootInsidePublicUploads() throws Exception {
        System.out.println("[配置防线：like.export-directory 不得位于 like.upload-directory 内]");
        Path uploads = Files.createTempDirectory("lxt-export-uploads-");
        try {
            ExportFileService service = new ExportFileService(
                    new MemoryStore(), uploads.resolve("excel").toString(), uploads.toString(), () -> NOW);
            boolean refused = false;
            try {
                service.allocate("x");
            } catch (OperateException expected) {
                refused = true;
            }
            check("导出根目录在公开 uploads 内时拒绝分配文件", refused, "已拒绝");
        } finally {
            deleteTree(uploads);
        }
    }

    // ---- helpers --------------------------------------------------------------

    private static ExportFileService service(Path root, Path uploads, AtomicLong clock) {
        return new ExportFileService(new MemoryStore(), root.toString(), uploads.toString(), clock::get);
    }

    private static long countFiles(Path dir) throws IOException {
        try (Stream<Path> stream = Files.walk(dir)) {
            return stream.filter(Files::isRegularFile).count();
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
                    // 测试清理失败不改变断言结果
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

    /** 测试用内存票据存储（生产走 Redis）。 */
    static final class MemoryStore implements ExportTicketStore {
        final Map<String, ExportTicket> map = new ConcurrentHashMap<String, ExportTicket>();

        @Override
        public void put(ExportTicket ticket, long ttlSeconds) {
            map.put(ticket.getTicket(), ticket);
        }

        @Override
        public ExportTicket take(String ticket) {
            return map.remove(ticket);
        }
    }
}
