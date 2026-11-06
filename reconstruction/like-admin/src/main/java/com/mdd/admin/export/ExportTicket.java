package com.mdd.admin.export;

/**
 * #214 P1：一次性导出下载票据。
 *
 * <p>票据只由服务端签发，随机 64 位 hex；下载接口凭票据取文件，票据本身即授权凭证
 * （后台前端 window.open 不带 token）。记录绑定签发时的 adminId 便于审计。</p>
 */
public class ExportTicket {

    /** 随机票据（64 位 hex）。 */
    private String ticket;

    /** 签发时的后台管理员 id（0 表示无法识别）。 */
    private Integer adminId;

    /** 容器内私有导出文件的绝对路径。 */
    private String filePath;

    /** 下载时呈现给用户的文件名（已安全化，含 .xlsx）。 */
    private String fileName;

    /** 过期时间（epoch millis）。 */
    private Long expireAt;

    public ExportTicket() {
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getExpireAt() {
        return expireAt;
    }

    public void setExpireAt(Long expireAt) {
        this.expireAt = expireAt;
    }
}
