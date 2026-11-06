/**
 * 批量导入表格解析（前端解析，后端只收 JSON 行数组）。
 *
 * 支持 Excel（.xlsx/.xls，读取第一个 sheet）与 CSV（UTF-8，含 BOM）。
 * 表头支持中文与旧版英文；中文按业务字段映射，忽略首尾空格、全角括号
 * 以及括号内的说明文字（如「学员ID（选填）」）。
 */
import * as XLSX from 'xlsx'

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

export interface ImportColumn {
    key: string
    label: string
    aliases: string[]
}

export interface ParsedImportTable {
    headers: string[]
    rows: Array<Record<string, string>>
    /** 缺失的必填列（中文列名） */
    missing: string[]
}

export const COUPON_COLUMNS: ImportColumn[] = [
    { key: 'mobile', label: '手机号', aliases: ['手机号', 'mobile'] },
    { key: 'goodsId', label: '券商品ID', aliases: ['券商品ID', 'goodsId'] },
    { key: 'quantity', label: '张数', aliases: ['张数', 'quantity'] },
    { key: 'studentId', label: '学员ID', aliases: ['学员ID', 'studentId'] },
    { key: 'remark', label: '备注', aliases: ['备注', 'remark'] }
]

export const WALLET_COLUMNS: ImportColumn[] = [
    { key: 'mobile', label: '手机号', aliases: ['手机号', 'mobile'] },
    { key: 'amount', label: '金额', aliases: ['金额', 'amount'] },
    { key: 'remark', label: '原因', aliases: ['原因', '备注', 'remark'] }
]

function cellText(value: unknown): string {
    if (value === null || value === undefined) return ''
    return String(value).trim()
}

function normalizeHeader(value: unknown): string {
    return cellText(value)
        .replace(/[（]/g, '(')
        .replace(/[）]/g, ')')
        .replace(/[\s\u3000]+/g, '')
        .toLowerCase()
        .replace(/\([^)]*\)$/, '')
}

/** 把上传文件读成二维字符串数组（第一个 sheet）。 */
export function readSheetMatrix(data: ArrayBuffer | Uint8Array, filename: string): string[][] {
    const extension = String(filename || '')
        .split('.')
        .pop()
        ?.toLowerCase()

    if (extension === 'csv') {
        return parseCsv(new TextDecoder('utf-8').decode(data))
    }
    if (extension !== 'xlsx' && extension !== 'xls') {
        throw new Error('仅支持 .xlsx、.xls 或 .csv 文件')
    }

    const workbook = XLSX.read(data, { type: 'array' })
    const sheetName = workbook.SheetNames[0]
    if (!sheetName) return []
    const sheet = workbook.Sheets[sheetName]
    const matrix = XLSX.utils.sheet_to_json<unknown[]>(sheet, {
        header: 1,
        raw: true,
        defval: '',
        blankrows: false
    })
    return matrix.map((row) => row.map(cellText)).filter((row) => row.some((cell) => cell !== ''))
}

/** 解析第一个 sheet，按列定义映射为字段行；缺失必填列时 missing 返回中文列名。 */
export function parseImportSheet(
    data: ArrayBuffer | Uint8Array,
    filename: string,
    columns: ImportColumn[],
    requiredKeys: string[]
): ParsedImportTable {
    const matrix = readSheetMatrix(data, filename)
    if (!matrix.length) {
        return {
            headers: [],
            rows: [],
            missing: requiredKeys.map(
                (key) => columns.find((column) => column.key === key)?.label || key
            )
        }
    }

    const headers = matrix[0].map((cell) => String(cell ?? '').trim())
    const indexByKey = new Map<string, number>()
    headers.forEach((header, index) => {
        const normalized = normalizeHeader(header)
        if (!normalized) return
        columns.forEach((column) => {
            if (indexByKey.has(column.key)) return
            const matched = column.aliases.some((alias) => normalizeHeader(alias) === normalized)
            if (matched) indexByKey.set(column.key, index)
        })
    })

    const missing = requiredKeys
        .filter((key) => !indexByKey.has(key))
        .map((key) => columns.find((column) => column.key === key)?.label || key)
    if (missing.length) return { headers, rows: [], missing }

    const rows = matrix.slice(1).map((cells) => {
        const row: Record<string, string> = {}
        columns.forEach((column) => {
            const index = indexByKey.get(column.key)
            row[column.key] = index === undefined ? '' : String(cells[index] ?? '').trim()
        })
        return row
    })
    return { headers, rows, missing: [] }
}

export function parseCouponSheet(
    data: ArrayBuffer | Uint8Array,
    filename: string
): ParsedImportTable {
    return parseImportSheet(data, filename, COUPON_COLUMNS, ['mobile', 'goodsId', 'quantity'])
}

export function parseWalletSheet(
    data: ArrayBuffer | Uint8Array,
    filename: string
): ParsedImportTable {
    return parseImportSheet(data, filename, WALLET_COLUMNS, ['mobile', 'amount', 'remark'])
}
