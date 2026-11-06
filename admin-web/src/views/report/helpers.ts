/**
 * 数据报表页面共用小工具（T30.5 / #40）。
 */

/** 去掉空字符串 / null / undefined，避免后端 Integer 参数收到空串。 */
export function compactParams(params: Record<string, any>) {
    const result: Record<string, any> = {}
    Object.keys(params || {}).forEach((key) => {
        const value = params[key]
        if (value === '' || value === null || value === undefined) return
        result[key] = value
    })
    return result
}

/**
 * 业务页导出用的默认日期范围：本月 1 日至今天（yyyy-MM-dd）。
 *
 * 旧报表的 startDate/endDate 缺省会查 1970 至今（全量）；业务页没有日期筛选时，
 * 直接在导出按钮旁放一个默认本月的日期范围，避免只能导全量，用户仍可自行改区间。
 */
export function currentMonthRange(): string[] {
    const now = new Date()
    const pad = (value: number) => String(value).padStart(2, '0')
    const year = now.getFullYear()
    const month = pad(now.getMonth() + 1)
    return [`${year}-${month}-01`, `${year}-${month}-${pad(now.getDate())}`]
}

/** 金额展示，后端已保留 2 位小数。 */
export function money(value: any) {
    const number = Number(value || 0)
    return number.toLocaleString('zh-CN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })
}

/** 千分位数量。 */
export function numberText(value: any) {
    return Number(value || 0).toLocaleString('zh-CN')
}
