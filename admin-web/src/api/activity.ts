import request from '@/utils/request'

// ---------- 活动报名链接（#27 / #38） ----------
// 可生成链接的场次（未来场次，最多 300 条）
export function getEnrollOptions(params?: any) {
    return request.get({ url: '/activity/enroll/options', params })
}

// 生成 / 复用某场次的报名链接
export function createEnrollLink(params: any) {
    return request.post({ url: '/activity/enroll/link', params })
}

// 报名链接列表（含状态/有效期/提交数）
export function getEnrollLinks(params?: any) {
    return request.get({ url: '/activity/enroll/links', params })
}

// 停用 / 启用报名链接
export function updateEnrollLinkStatus(params: any) {
    return request.post({ url: '/activity/enroll/link/status', params })
}

// 设置报名链接有效期（0=长期有效）
export function updateEnrollLinkExpire(params: any) {
    return request.post({ url: '/activity/enroll/link/expire', params })
}

// 报名名单（筛选 + 去重汇总）
export function getEnrollmentList(params: any) {
    return request.get({ url: '/activity/enroll/list', params })
}

// 导出报名名单（code=2 时由 request 钩子打开下载地址）
export function exportEnrollments(params: any) {
    return request.get({ url: '/activity/enroll/export', params })
}

// 开班申请聚合列表（街道 + 课程类型）
export function getApplyGroups(params: any) {
    return request.get({ url: '/activity/apply/list', params })
}

// ---------- 活动报名异常（满员后支付/超员生效，#61） ----------
// 异常报名列表（status: 0=待处理 1=已处理 2=已退款）
export function getEnrollExceptions(params?: any) {
    return request.get({ url: '/activity/exception/list', params })
}

// 异常报名详情（含订单、活动与报名明细分行）
export function getEnrollExceptionDetail(params: any) {
    return request.get({ url: '/activity/exception/detail', params })
}

// 标记异常报名已处理（保留超员名额）
export function handleEnrollException(params: any) {
    return request.post({ url: '/activity/exception/handle', params })
}

// 异常报名发起退款（复用缴费退款通道，成功后取消报名并释放名额）
export function refundEnrollException(params: any) {
    return request.post({ url: '/activity/exception/refund', params })
}

// ---------- 活动管理（正式上线：后台活动 CRUD） ----------
// 列表（page_no/page_size/keyword/status）
export function activityList(params: any) {
    return request.get({ url: '/activity/list', params })
}

// 活动详情
export function activityDetail(params: any) {
    return request.get({ url: '/activity/detail', params })
}

// 新增活动
export function activityAdd(params: any) {
    return request.post({ url: '/activity/add', params })
}

// 编辑活动
export function activityEdit(params: any) {
    return request.post({ url: '/activity/edit', params })
}

// 上下架（status：0=下架，1=上架）
export function activityStatus(params: any) {
    return request.post({ url: '/activity/status', params })
}

// 删除（已有报名会被后端拒绝，需先下架）
export function activityDel(params: any) {
    return request.post({ url: '/activity/del', params })
}
