import request from '@/utils/request'

// 课程报表（真实营业额口径：订单/课时/核销，不做二次分配计算）
export function getCourseReport(params?: any) {
    return request.get({ url: '/report/course/lists', params }, { ignoreCancelToken: true })
}

// 导出课程报表：后端返回 code=2 + url，请求拦截器会打开下载地址
export function exportCourseReport(params?: any) {
    return request.get({ url: '/report/course/export', params })
}

// 教练月营业额（#102）：自然月上课数 / 服务人次 / 总营业额
export function getCoachMonthReport(params?: any) {
    return request.get({ url: '/report/course/coach_month/lists', params }, { ignoreCancelToken: true })
}

// 教练月营业额下钻（该教练当月课节 / 订单明细）
export function getCoachMonthDetail(params: any) {
    return request.get({ url: '/report/course/coach_month/detail', params }, { ignoreCancelToken: true })
}

// 导出教练月营业额：后端返回 code=2 + url，请求拦截器会打开下载地址
export function exportCoachMonthReport(params?: any) {
    return request.get({ url: '/report/course/coach_month/export', params })
}

// 核销报表
export function getCheckinReport(params?: any) {
    return request.get({ url: '/report/checkin/lists', params }, { ignoreCancelToken: true })
}

export function exportCheckinReport(params?: any) {
    return request.get({ url: '/report/checkin/export', params })
}

// 券流水报表
export function getCouponReport(params?: any) {
    return request.get({ url: '/report/coupon/lists', params }, { ignoreCancelToken: true })
}

export function exportCouponReport(params?: any) {
    return request.get({ url: '/report/coupon/export', params })
}

// 活动报名报表
export function getActivityReport(params?: any) {
    return request.get({ url: '/report/activity/lists', params }, { ignoreCancelToken: true })
}

export function exportActivityReport(params?: any) {
    return request.get({ url: '/report/activity/export', params })
}

// 开班申请报表
export function getClassApplyReport(params?: any) {
    return request.get({ url: '/report/class_apply/lists', params }, { ignoreCancelToken: true })
}

export function exportClassApplyReport(params?: any) {
    return request.get({ url: '/report/class_apply/export', params })
}
