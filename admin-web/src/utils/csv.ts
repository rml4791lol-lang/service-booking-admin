/**
 * 轻量 CSV 工具（批量导入用：前端解析，后端只收 JSON 行数组）。
 *
 * 只覆盖后台批量导入需要的场景：UTF-8（含 BOM）、逗号分隔、双引号转义、
 * CRLF/LF 换行。不处理分号分隔等区域变体。
 */

/** 解析 CSV 文本为二维数组；自动去掉 BOM，忽略末尾空行。 */
export function parseCsv(text: string): string[][] {
    const content = String(text || '').replace(/^\uFEFF/, '')
    if (!content.trim()) return []

    const rows: string[][] = []
    let row: string[] = []
    let field = ''
    let inQuotes = false

    for (let i = 0; i < content.length; i += 1) {
        const char = content[i]
        if (inQuotes) {
            if (char === '"') {
                if (content[i + 1] === '"') {
                    field += '"'
                    i += 1
                } else {
                    inQuotes = false
                }
            } else {
                field += char
            }
            continue
        }
        if (char === '"') {
            inQuotes = true
        } else if (char === ',') {
            row.push(field)
            field = ''
        } else if (char === '\n') {
            row.push(field)
            rows.push(row)
            row = []
            field = ''
        } else if (char === '\r') {
            // 兼容 CRLF 与单独的 CR
            if (content[i + 1] !== '\n') {
                row.push(field)
                rows.push(row)
                row = []
                field = ''
            }
        } else {
            field += char
        }
    }
    row.push(field)
    rows.push(row)

    // 去掉末尾空行（Excel 导出常带一行空记录）
    while (rows.length) {
        const last = rows[rows.length - 1]
        if (last.every((cell) => String(cell).trim() === '')) rows.pop()
        else break
    }
    return rows
}

export interface ParsedCsvTable {
    headers: string[]
    rows: string[][]
}

/**
 * 解析带表头的 CSV：首行视为表头（去空格、转小写），其余行按列宽补齐/截断。
 * 返回的 rows 只包含非全空行。
 */
export function parseCsvTable(text: string): ParsedCsvTable {
    const matrix = parseCsv(text)
    if (!matrix.length) return { headers: [], rows: [] }
    const headers = matrix[0].map((cell) => String(cell || '').trim().toLowerCase())
    const rows = matrix
        .slice(1)
        .filter((row) => row.some((cell) => String(cell).trim() !== ''))
        .map((row) => headers.map((_, index) => String(row[index] ?? '').trim()))
    return { headers, rows }
}

/** 生成 CSV 单元格：含逗号/引号/换行时加引号并转义。 */
function csvCell(value: string | number | null | undefined): string {
    const text = value === null || value === undefined ? '' : String(value)
    if (/[",\r\n]/.test(text)) return `"${text.replace(/"/g, '""')}"`
    return text
}

/** 下载 CSV（带 UTF-8 BOM，避免 Excel 打开中文乱码）。 */
export function downloadCsv(
    filename: string,
    rows: Array<Array<string | number | null | undefined>>
): void {
    const content = rows.map((row) => row.map(csvCell).join(',')).join('\r\n')
    const blob = new Blob([`\uFEFF${content}`], { type: 'text/csv;charset=utf-8' })
    const url = URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    link.download = filename
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
    URL.revokeObjectURL(url)
}

/** 并发受限地执行任务（批量校验手机号时避免一次打出几百个请求）。 */
export async function runWithConcurrency<T>(
    items: T[],
    limit: number,
    task: (item: T) => Promise<void>
): Promise<void> {
    let cursor = 0
    const workers = Array.from({ length: Math.min(Math.max(limit, 1), items.length) }, async () => {
        while (cursor < items.length) {
            const current = items[cursor]
            cursor += 1
            await task(current)
        }
    })
    await Promise.all(workers)
}
