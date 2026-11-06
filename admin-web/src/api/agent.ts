import request from '@/utils/request'

/**
 * 代理管理（#79）：只读查询。
 * 历史分成结清、余额兑付、提现打款等涉钱动作不在本次范围，页面不提供执行入口。
 */
export function getAgentList(params: any) {
    return request.get({ url: '/agent/list', params }, { ignoreCancelToken: true })
}

export function getAgentDetail(id: number | string) {
    return request.get({ url: '/agent/detail', params: { id } })
}

export function getAgentEarningList(params: any) {
    return request.get({ url: '/agent/finance/earningList', params }, { ignoreCancelToken: true })
}

export function getAgentWithdrawalList(params: any) {
    return request.get({ url: '/agent/finance/withdrawalList', params }, { ignoreCancelToken: true })
}
