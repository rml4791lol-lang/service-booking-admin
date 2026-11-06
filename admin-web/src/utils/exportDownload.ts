import configs from '@/config'

/** #214：导出接口返回的一次性下载票据载荷。 */
export interface ExportTicketPayload {
    /** 下载文件名（服务端已安全化） */
    file?: string
    /** 一次性票据（64 位 hex） */
    ticket?: string
    /** 票据有效期（秒） */
    expiresIn?: number
    /** 兼容旧后端：公开下载地址（新后端不再返回） */
    url?: string
}

/**
 * 把导出接口返回的票据换成下载地址。
 *
 * 下载接口 /adminapi/export/download 只认票据（window.open/location 不带 token），
 * 票据由已登录的导出接口签发，5 分钟且单次有效。
 */
export function buildExportDownloadUrl(data?: ExportTicketPayload | null): string {
    if (data?.ticket) {
        const baseUrl = (configs.baseUrl || '').replace(/\/+$/, '')
        return `${baseUrl}/${configs.urlPrefix}/export/download?ticket=${encodeURIComponent(data.ticket)}`
    }
    // 灰度期兼容：老后端仍返回 data.url
    return data?.url || ''
}

/** 打开导出下载（票据优先，旧 url 兜底）。 */
export function openExportDownload(data?: ExportTicketPayload | null): void {
    const url = buildExportDownloadUrl(data)
    if (url) {
        window.location.href = url
    }
}
