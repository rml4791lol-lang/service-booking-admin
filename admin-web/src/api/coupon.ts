import request from '@/utils/request'

// ---------- 券列表 ----------
export function getCouponList(params?: any) {
    return request.get({ url: '/coupon/list', params })
}

export function getCouponDetail(params: any) {
    return request.get({ url: '/coupon/detail', params })
}

export function getCouponStudents(params?: any) {
    return request.get({ url: '/coupon/students', params })
}

// 按模板批量发券（携带 requestId 幂等）
export function issueCoupon(params: any) {
    return request.post({ url: '/coupon/issue', params })
}

// 作废券（后端软删除）
export function voidCoupon(params: any) {
    return request.post({ url: '/coupon/del', params })
}

// 手工发券（按手机号，保留老接口）
export function addCoupon(params: any) {
    return request.post({ url: '/coupon/add', params })
}

// ---------- 券模板 ----------
export function getCouponTemplateList(params?: any) {
    return request.get({ url: '/coupon_template/list', params })
}

// 启用中的模板（发券下拉）
export function getCouponTemplateAll() {
    return request.get({ url: '/coupon_template/all' })
}

export function addCouponTemplate(params: any) {
    return request.post({ url: '/coupon_template/add', params })
}

export function editCouponTemplate(params: any) {
    return request.post({ url: '/coupon_template/edit', params })
}

export function delCouponTemplate(params: any) {
    return request.post({ url: '/coupon_template/del', params })
}
