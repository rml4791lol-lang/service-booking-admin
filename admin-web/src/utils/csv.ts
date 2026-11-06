/**
 * 批量导入的下载与并发工具（表格解析见 utils/importSheet.ts）。
 */

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
