import request from '@/utils/request'

// 用户列表
export function getUserList(params: any) {
    return request.get({ url: '/user.user/lists', params }, { ignoreCancelToken: true })
}

// 用户详情
export function getUserDetail(params: any) {
    return request.get({ url: '/user.user/detail', params })
}

// 用户编辑
export function userEdit(params: any) {
    return request.post({ url: '/user.user/edit', params })
}

// 用户编辑
export function adjustMoney(params: any) {
    return request.post({ url: '/user.user/adjustMoney', params })
}

// 批量调余额（批量导入）：入参为 CSV 解析后的 JSON 行数组，逐行独立、返回成功/失败汇总
export function adjustMoneyBatch(rows: any[]) {
    return request.post({ url: '/user.user/adjustMoney/batch', params: rows })
}

// 新增学员（后台建号，手机号即家长登录账号）
export function addUser(params: any) {
    return request.post({ url: '/user.user/add', params })
}

// 用户下的关联学员
export function getUserStudents(params: any) {
    return request.get({ url: '/user.user/students', params })
}

// 学员档案（购买/已上/剩余课时、所在班级）
export function getUserAccount(params: any) {
    return request.get({ url: '/user.user/account', params })
}

// 上课明细（出勤/请假/消课）
export function getLessonRecords(params: any) {
    return request.get({ url: '/user.user/lessonRecords', params })
}
