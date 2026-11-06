import request from '@/utils/request'

/** 家长请假审批（#79）：沿用旧版 /adminapi/leave/{list,audit}。 */
export function getParentLeaveList(params: any) {
    return request.get({ url: '/leave/list', params }, { ignoreCancelToken: true })
}

export function auditParentLeave(params: any) {
    return request.post({ url: '/leave/audit', params })
}
