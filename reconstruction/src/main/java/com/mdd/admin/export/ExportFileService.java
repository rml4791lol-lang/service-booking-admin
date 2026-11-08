package com.mdd.admin.export;

import com.mdd.admin.LikeAdminThreadLocal;
import com.mdd.common.exception.OperateException;
import com.mdd.common.util.ToolUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.LongSupplier;
import java.util.regex.Pattern;

/**
 * #214 P1：导出文件私有落盘 + 一次性下载票据（所有导出点的统一收口）。
 *
 * <p>此前各导出 service 把 xlsx 写到 {@code like.upload-directory/excel/export/...}，
 * 再由 {@code /adminapi/uploads/...} 静态直出——UUID 文件名挡不住遍历/泄露，且含手机号。
 * 现在改为：</p>
 * <ol>
 *   <li>{@link #allocate(String)} 在容器内私有目录（{@code like.export-directory}，
 *       默认 {@code /tmp/lxt-export}）分配一个随机物理文件；</li>
 *   <li>调用方用 EasyExcel 写入该文件；</li>
 *   <li>{@link #issue(File, String)} 签发 5 分钟、单次使用、绑定 adminId 的下载票据，
 *       返回 {@code {file, ticket, expiresIn}}；</li>
 *   <li>下载接口凭票据取文件，取走即删。</li>
 * </ol>
 *
 * <p>私有目录与公开 uploads 分离；若配置误把导出目录放进 uploads 内，分配时直接拒绝。</p>
 */
@Service
public class ExportFileService {

    /** 票据有效期（秒）。 */
    public static final int TICKET_TTL_SECONDS = 300;

    private static final String TICKET_KEY_PREFIX = "export:ticket:";
    private static final Pattern TICKET_PATTERN = Pattern.compile("[a-f0-9]{64}");
    private static final char[] HEX = "0123456789abcdef".toCharArray();
    private static final SecureRandom RANDOM = new SecureRandom();

    private final ExportTicketStore ticketStore;
    private final String exportDirectory;
    private final String uploadDirectory;
    private final LongSupplier clock;

    @Autowired
    public ExportFileService(ExportTicketStore ticketStore,
                             @Value("${like.export-directory:/tmp/lxt-export}") String exportDirectory,
                             @Value("${like.upload-directory:}") String uploadDirectory) {
        this(ticketStore, exportDirectory, uploadDirectory, System::currentTimeMillis);
    }

    /** 测试入口：可注入时钟，避免 sleep。 */
    ExportFileService(ExportTicketStore ticketStore, String exportDirectory,
                      String uploadDirectory, LongSupplier clock) {
        this.ticketStore = ticketStore;
        this.exportDirectory = exportDirectory == null || exportDirectory.trim().isEmpty()
                ? "/tmp/lxt-export" : exportDirectory.trim();
        this.uploadDirectory = uploadDirectory == null ? "" : uploadDirectory.trim();
        this.clock = clock;
    }

    /**
     * 在私有导出目录下分配一个 xlsx 物理文件（随机 UUID 文件名）。
     *
     * @param requestedName 业务侧期望的下载文件名（仅用于日志/校验，物理名不受其影响）
     * @return 待写入的 File
     */
    public File allocate(String requestedName) {
        File root = exportRoot();
        assertSeparatedFromPublicUploads(root);
        File dayDir = new File(root, new SimpleDateFormat("yyyyMMdd", Locale.ROOT).format(new Date()));
        if (!dayDir.exists() && !dayDir.mkdirs() && !dayDir.isDirectory()) {
            throw new OperateException("创建导出目录失败");
        }
        File target = new File(dayDir, ToolUtils.makeUUID() + ".xlsx");
        if (!isUnderRoot(target, root)) {
            throw new OperateException("导出文件路径非法");
        }
        return target;
    }

    /**
     * 为已写好的私有文件签发一次性下载票据。
     *
     * @param file         已写入的 xlsx（必须位于私有导出根目录内）
     * @param downloadName 下载时呈现的文件名（会做路径字符清洗并补齐 .xlsx）
     * @return {@code {file, ticket, expiresIn}}
     */
    public Map<String, Object> issue(File file, String downloadName) {
        if (file == null) {
            throw new OperateException("导出文件不存在");
        }
        File root = exportRoot();
        assertSeparatedFromPublicUploads(root);
        if (!file.isFile() || !isUnderRoot(file, root)) {
            throw new OperateException("导出文件路径非法");
        }

        String ticket = newTicket();
        long expireAt = clock.getAsLong() + TICKET_TTL_SECONDS * 1000L;

        ExportTicket record = new ExportTicket();
        record.setTicket(ticket);
        record.setAdminId(currentAdminId());
        record.setFilePath(file.getAbsolutePath());
        record.setFileName(normalizeDownloadName(downloadName));
        record.setExpireAt(expireAt);
        ticketStore.put(record, TICKET_TTL_SECONDS);

        Map<String, Object> result = new LinkedHashMap<String, Object>();
        result.put("file", record.getFileName());
        result.put("ticket", ticket);
        result.put("expiresIn", TICKET_TTL_SECONDS);
        return result;
    }

    /**
     * 消费票据（原子取出并删除）。票据不存在、已使用、已过期或文件越界均返回 null。
     *
     * @param ticket 票据字符串
     * @return 票据记录或 null
     */
    public ExportTicket consume(String ticket) {
        if (ticket == null || !TICKET_PATTERN.matcher(ticket).matches()) {
            return null;
        }
        ExportTicket record = ticketStore.take(ticket);
        if (record == null) {
            return null;
        }
        if (record.getExpireAt() == null || record.getExpireAt() < clock.getAsLong()) {
            return null;
        }
        File root = exportRoot();
        File file;
        try {
            file = new File(record.getFilePath());
            if (!isUnderRoot(file, root) || !file.isFile()
                    || !file.getName().toLowerCase(Locale.ROOT).endsWith(".xlsx")) {
                return null;
            }
        } catch (RuntimeException error) {
            return null;
        }
        return record;
    }

    /** 票据 key 前缀（供运维排查；实际读写由 {@link ExportTicketStore} 负责）。 */
    public static String ticketKey(String ticket) {
        return TICKET_KEY_PREFIX + ticket;
    }

    // ---------------------------------------------------------------- internals

    private File exportRoot() {
        File root = new File(exportDirectory);
        if (!root.exists() && !root.mkdirs() && !root.isDirectory()) {
            throw new OperateException("创建导出目录失败");
        }
        return root;
    }

    /**
     * 私有导出目录不得位于公开 uploads 内，否则又会被静态资源处理器直出。
     * 注意比较 canonical path，防止 {@code /www/uploads/../uploads/x} 这类绕过。
     */
    private void assertSeparatedFromPublicUploads(File root) {
        if (uploadDirectory.isEmpty()) {
            return;
        }
        try {
            String rootPath = root.getCanonicalPath();
            String uploadPath = new File(uploadDirectory).getCanonicalPath();
            if (rootPath.equals(uploadPath) || rootPath.startsWith(uploadPath + File.separator)) {
                throw new OperateException("导出目录不能位于公开上传目录内");
            }
        } catch (IOException error) {
            throw new OperateException("导出目录配置无效");
        }
    }

    private boolean isUnderRoot(File file, File root) {
        try {
            String rootPath = root.getCanonicalPath();
            String filePath = file.getCanonicalPath();
            return filePath.startsWith(rootPath + File.separator);
        } catch (IOException error) {
            return false;
        }
    }

    private String normalizeDownloadName(String name) {
        String base = name == null ? "" : name.trim();
        if (base.toLowerCase(Locale.ROOT).endsWith(".xlsx")) {
            base = base.substring(0, base.length() - 5);
        }
        return ToolUtils.safeExportFileName(base) + ".xlsx";
    }

    private Integer currentAdminId() {
        try {
            Integer adminId = LikeAdminThreadLocal.getAdminId();
            return adminId == null ? 0 : adminId;
        } catch (RuntimeException ignored) {
            return 0;
        }
    }

    private String newTicket() {
        byte[] bytes = new byte[32];
        RANDOM.nextBytes(bytes);
        StringBuilder sb = new StringBuilder(64);
        for (byte item : bytes) {
            sb.append(HEX[(item >> 4) & 0x0F]).append(HEX[item & 0x0F]);
        }
        return sb.toString();
    }
}
