import request from '@/utils/request'

// 教练请假列表
export function getInstructorLeaveList(params: any) {
    return request.get({ url: '/instructor/leave/list', params }, { ignoreCancelToken: true })
}

// 教练请假审批
export function auditInstructorLeave(params: any) {
    return request.post({ url: '/instructor/leave/audit', params })
}

// 教练请假期内课节（补课安排）
export function getInstructorLeaveMakeupLessons(params: any) {
    return request.get(
        { url: '/instructor/leave/makeupLessons', params },
        { ignoreCancelToken: true }
    )
}

// 安排/修改补课时间
export function arrangeInstructorLeaveMakeup(params: any) {
    return request.post({ url: '/instructor/leave/arrangeMakeup', params })
}

// 完成补课结算（计入教练课时费，不可逆）
export function completeInstructorLeaveMakeup(params: any) {
    return request.post({ url: '/instructor/leave/makeupComplete', params })
}

// 教练注销列表
export function getInstructorCancelList(params: any) {
    return request.get({ url: '/instructor/cancel/list', params }, { ignoreCancelToken: true })
}

// 教练注销审批
export function auditInstructorCancel(params: any) {
    return request.post({ url: '/instructor/cancel/audit', params })
}

// 教练提现列表
export function getInstructorWithdrawalList(params: any) {
    return request.get({ url: '/instructor/withdrawal/list', params }, { ignoreCancelToken: true })
}

// 教练提现审核
export function auditInstructorWithdrawal(params: any) {
    return request.post({ url: '/instructor/withdrawal/audit', params })
}

// 退课申请列表
export function getCourseRefundList(params: any) {
    return request.get({ url: '/course_refund/list', params }, { ignoreCancelToken: true })
}

// 退课申请审批
export function auditCourseRefund(params: any) {
    return request.post({ url: '/course_refund/audit', params })
}

// 退款任务列表（#60 退款审核 outbox）
export function getCourseRefundTaskList(params: any) {
    return request.get({ url: '/course_refund/task/list', params }, { ignoreCancelToken: true })
}

// 退款任务执行流水
export function getCourseRefundTaskLogs(params: any) {
    return request.get({ url: '/course_refund/task/log', params })
}

// 重试退款任务
export function retryCourseRefundTask(params: any) {
    return request.post({ url: '/course_refund/task/retry', params })
}
