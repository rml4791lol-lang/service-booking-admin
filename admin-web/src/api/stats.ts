import request from '@/utils/request'

// 数据统计（T36 #106，统计链公共骨架 —— #107/#108/#109 继续在此文件追加维度函数）
// 时间三种写法：month=yyyy-MM 优先，其次 startDate/endDate，缺省本月。

// 一级总览：5 类对象数量 + 期间订单/营业额/上课/人次 + 环比
export function getStatsOverview(params?: any) {
    return request.get({ url: '/stats/overview', params }, { ignoreCancelToken: true })
}

// 二级：营业额按日趋势（默认近 30 天）
export function getStatsTrend(params?: any) {
    return request.get({ url: '/stats/trend', params }, { ignoreCancelToken: true })
}

// 教练维度列表（复用 #102 教练月营业额：订单数/上课数/服务人次/总营业额）
export function getStatsInstructorList(params?: any) {
    return request.get({ url: '/stats/instructor/lists', params }, { ignoreCancelToken: true })
}

// 教练维度下钻（课节 + 订单明细）
export function getStatsInstructorDetail(params: any) {
    return request.get({ url: '/stats/instructor/detail', params }, { ignoreCancelToken: true })
}

// 导出教练维度统计：后端返回 code=2 + url，请求拦截器会打开下载地址
export function exportStatsInstructor(params?: any) {
    return request.get({ url: '/stats/instructor/export', params })
}

// 三级明细：订单（#107/#108/#109 复用，可按社区/小区/课程包筛选）
export function getStatsOrderList(params?: any) {
    return request.get({ url: '/stats/order/lists', params }, { ignoreCancelToken: true })
}

// 三级明细：课节（消课流水聚合到 班级+课节）
export function getStatsLessonList(params?: any) {
    return request.get({ url: '/stats/lesson/lists', params }, { ignoreCancelToken: true })
}

// 社区维度列表（#107）：社区 / 订单数 / 客流（到场人次）/ 营业额；communityId=0 即「未归属」
export function getStatsCommunityList(params?: any) {
    return request.get({ url: '/stats/community/lists', params }, { ignoreCancelToken: true })
}

// 社区维度导出：后端返回 code=2 + url，请求拦截器会打开下载地址
export function exportStatsCommunity(params?: any) {
    return request.get({ url: '/stats/community/export', params })
}

// 小区维度列表（#108）：小区 / 订单数 / 客流（到场人次）/ 营业额；areaId=0 即「未归属」
export function getStatsAreaList(params?: any) {
    return request.get({ url: '/stats/area/lists', params }, { ignoreCancelToken: true })
}

// 小区维度导出：后端返回 code=2 + url，请求拦截器会打开下载地址
export function exportStatsArea(params?: any) {
    return request.get({ url: '/stats/area/export', params })
}

// ------------------------------------------------------------------
// 老板视角 + 课程包销量（T36 #109）
// 老板视角 4 个查询的导出与查询共用同一 URI：传 export=1，后端返回 code=2 + url。
// orderBy/limit 走后端白名单（revenue|orderCount|attends，limit 默认 20、0=全部、最大 200）。
// ------------------------------------------------------------------

// 片区覆盖：片区 / 覆盖社区数 / 订单数 / 营业额（含「未归属」片区行）
export function getStatsOwnerRegion(params?: any) {
    return request.get({ url: '/stats/owner/region', params }, { ignoreCancelToken: true })
}

// 片区覆盖导出
export function exportStatsOwnerRegion(params?: any) {
    return request.get({ url: '/stats/owner/region', params: { ...params, export: 1 } })
}

// 社区效果排行：营业额 DESC、并列看订单数
export function getStatsOwnerCommunityTop(params?: any) {
    return request.get({ url: '/stats/owner/communityTop', params }, { ignoreCancelToken: true })
}

// 社区效果排行导出（全量行）
export function exportStatsOwnerCommunityTop(params?: any) {
    return request.get({ url: '/stats/owner/communityTop', params: { ...params, export: 1 } })
}

// 教练效果排行：营业额 DESC、并列看订单数（复用教练月营业额口径 + 未分配教练残差）
export function getStatsOwnerInstructorTop(params?: any) {
    return request.get({ url: '/stats/owner/instructorTop', params }, { ignoreCancelToken: true })
}

// 教练效果排行导出（全量行）
export function exportStatsOwnerInstructorTop(params?: any) {
    return request.get({ url: '/stats/owner/instructorTop', params: { ...params, export: 1 } })
}

// 上了什么课：课程班级 / 类目 / 订单数 / 营业额（含「未归属」课程行）
export function getStatsOwnerCourseMix(params?: any) {
    return request.get({ url: '/stats/owner/courseMix', params }, { ignoreCancelToken: true })
}

// 课程构成导出
export function exportStatsOwnerCourseMix(params?: any) {
    return request.get({ url: '/stats/owner/courseMix', params: { ...params, export: 1 } })
}

// 课程包销量：课程包 / 购买数 / 金额 / 报名数（列出全部有购买或订单的课程包）
export function getStatsCoursePackList(params?: any) {
    return request.get({ url: '/stats/coursePack/lists', params }, { ignoreCancelToken: true })
}

// 课程包销量导出
export function exportStatsCoursePack(params?: any) {
    return request.get({ url: '/stats/coursePack/export', params })
}
