import request from '@/utils/request'

/**
 * 分账数据（#47）只读接口。
 *
 * 后端路由：/adminapi/finance.settlement/*
 * 全部接口带 @NotPower，只读查询；页面不提供上报/划款入口。
 */

/** 分账明细列表（按课程班级聚合；export=1 返回导出数据，export=2 返回文件） */
export function getSettlementLists(params?: any) {
    return request.get({ url: '/finance.settlement/lists', params }, { ignoreCancelToken: true })
}

/** 分账汇总（dimension=community / course / coach） */
export function getSettlementSummary(params?: any) {
    return request.get({ url: '/finance.settlement/summary', params }, { ignoreCancelToken: true })
}

/** 导出分账数据（后端返回 code=2 + url，请求拦截器会打开下载地址） */
export function exportSettlement(params?: any) {
    return request.get({ url: '/finance.settlement/export', params })
}

/** 分账上报适配器状态（脱敏，不含 secret） */
export function getSettlementConfig() {
    return request.get({ url: '/finance.settlement/config' })
}
