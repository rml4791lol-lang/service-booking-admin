import request from '@/utils/request'

// 课节列表（按班级；#75 课节明细页）
export function getLessonList(params: any) {
    return request.get({ url: '/course_lesson/lessons', params })
}

// 课节详情（学员名单/点名状态/课堂总结）
export function getLessonDetail(params: any) {
    return request.get({ url: '/course_lesson/detail', params })
}

// 后台取消预约（#213 C1）：按预约记录 id 置为已取消，已上课/不存在的记录会被拒绝
export function cancelLessonBooking(params: any) {
    return request.post({ url: '/course_lesson/cancelBooking', params })
}

// 课后总结列表（#79）：课节日志 + 班级/教练/关键词筛选
export function getCourseLessonLogList(params: any) {
    return request.get({ url: '/course_lesson/list', params }, { ignoreCancelToken: true })
}
