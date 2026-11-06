import request from '@/utils/request'

// ---------- 课程班级 ----------
export function classList(params: any) {
    return request.get({ url: '/course.class/list', params })
}

export function classDetail(params: any) {
    return request.get({ url: '/course.class/detail', params })
}

export function classAdd(params: any) {
    return request.post({ url: '/course.class/add', params })
}

export function classEdit(params: any) {
    return request.post({ url: '/course.class/edit', params })
}

// 删除：返回完整响应体，页面据后端「有报名/订单引用」的提示引导改为下架
export function classDel(params: any) {
    return request.post({ url: '/course.class/del', params }, { isTransformResponse: false })
}

// 上架 / 下架
export function classStatus(params: any) {
    return request.post({ url: '/course.class/status', params })
}

// 按当前排课规则重建未来场次：删除今天之后无有效预约的场次，保留有预约场次
export function classRegenerateLessons(params: any) {
    return request.post({ url: '/course.class/regenerateLessons', params })
}

export function classStats() {
    return request.get({ url: '/course.class/stats' })
}

// 已报名学员
export function classEnrolledStudents(params: any) {
    return request.get({ url: '/course.class/enrolledStudents', params })
}

// ---------- 表单下拉依赖 ----------
export function instructorAll() {
    return request.get({ url: '/instructor/all' })
}

export function agentList(params?: any) {
    return request.get({ url: '/agent/list', params })
}

export function communityAll(params?: any) {
    return request.get({ url: '/community/all', params })
}

export function areaAll(params?: any) {
    return request.get({ url: '/area/all', params })
}

export function categoryAll(params?: any) {
    return request.get({ url: '/course.class/categoryAll', params })
}

// ---------- 人工扣课（#75） ----------
export function manualList(params: any) {
    return request.get({ url: '/course.class/manual/list', params })
}

export function manualInstructors() {
    return request.get({ url: '/course.class/manual/instructors' })
}

export function manualDetail(params: any) {
    return request.get({ url: '/course.class/manual/detail', params })
}

export function manualSubmit(params: any) {
    return request.post({ url: '/course.class/manual/submit', params })
}

// ---------- 临时加课（#75） ----------
export function tempLessonList(params: any) {
    return request.get({ url: '/course.class/tempLesson/list', params })
}

export function tempLessonDetail(params: any) {
    return request.get({ url: '/course.class/tempLesson/detail', params })
}

export function tempLessonCreate(params: any) {
    return request.post({ url: '/course.class/tempLesson/create', params })
}

export function tempLessonCancel(params: any) {
    return request.post({ url: '/course.class/tempLesson/cancel', params })
}
