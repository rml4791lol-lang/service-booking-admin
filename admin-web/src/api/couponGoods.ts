import request from '@/utils/request'

// ---------- 券商品（次数券） ----------
export function getCouponGoodsList(params?: any) {
    return request.get({ url: '/coupon.goods/list', params })
}

export function addCouponGoods(params: any) {
    return request.post({ url: '/coupon.goods/add', params })
}

export function editCouponGoods(params: any) {
    return request.post({ url: '/coupon.goods/edit', params })
}

// 上架 / 下架
export function statusCouponGoods(params: any) {
    return request.post({ url: '/coupon.goods/status', params })
}

// 排序调整
export function sortCouponGoods(params: any) {
    return request.post({ url: '/coupon.goods/sort', params })
}

// 删除：返回完整响应体，页面据后端「有券实例/订单引用」的提示引导改为下架
export function delCouponGoods(params: any) {
    return request.post({ url: '/coupon.goods/del', params }, { isTransformResponse: false })
}

// 表单下拉：课程（团课/私教/体验）与小区
export function getCouponGoodsOptions() {
    return request.get({ url: '/coupon.goods/options' })
}

// 后台发次数券（#206）：按家长手机号逐张发放，学员选填（0=账号级）
export function grantCoupon(params: any) {
    return request.post({ url: '/coupon.goods/grant', params })
}

// 批量发券（批量导入）：入参为 CSV 解析后的 JSON 行数组，逐行独立、返回成功/失败汇总
export function grantCouponBatch(rows: any[]) {
    return request.post({ url: '/coupon.goods/grant/batch', params: rows })
}

// 已发券查询（#213 C2）：按券号或家长手机号查券实例，供撤回核对
export function getGrantedCoupons(params?: any) {
    return request.get({ url: '/coupon.goods/granted', params })
}

// 发券撤回（#213 C2）：仅未核销的券可撤回，成功后券置为已作废
export function revokeCoupon(params: any) {
    return request.post({ url: '/coupon.goods/revoke', params })
}
