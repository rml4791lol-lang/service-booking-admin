import request from '@/utils/request'

// 老课包列表（学员课时账户）
export function getUserCourseList(params: any) {
    return request.get({ url: '/user_course/list', params }, { ignoreCancelToken: true })
}

// 调整老课包总课时 / 剩余课时
export function adjustUserCourse(params: any) {
    return request.post({ url: '/user_course/adjust', params })
}
