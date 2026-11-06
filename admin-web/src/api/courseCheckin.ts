import request from '@/utils/request'

// 待核销场次列表（社区/课程/日期/状态筛选）
export function getCheckinList(params: any) {
    return request.get({ url: '/course_checkin/list', params }, { ignoreCancelToken: true })
}

// 场次详情（学员名单/券预览/审计）
export function getCheckinDetail(params: any) {
    return request.get({ url: '/course_checkin/detail', params }, { ignoreCancelToken: true })
}

// 页面筛选下拉（课程 + 社区）
export function getCheckinOptions() {
    return request.get({ url: '/course_checkin/options' })
}

// 人工补核销（原因必填）
export function manualCheckin(params: any) {
    return request.post({ url: '/course_checkin/manual', params })
}

// 撤销核销（退回次数券/老课包课时，原因必填）
export function reverseCheckin(params: any) {
    return request.post({ url: '/course_checkin/reverse', params })
}

// 补核销审计查询
export function getCheckinAudit(params: any) {
    return request.get({ url: '/course_checkin/audit', params }, { ignoreCancelToken: true })
}
