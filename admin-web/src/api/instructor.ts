import request from '@/utils/request'

// 教练列表
export function getInstructorList(params: any) {
    return request.get({ url: '/instructor/list', params })
}

// 全部教练（下拉选择，不分页）
export function getInstructorAll(params?: any) {
    return request.get({ url: '/instructor/all', params })
}

// 教练详情（含钱包只读字段）
export function getInstructorDetail(params: any) {
    return request.get({ url: '/instructor/detail', params })
}

// 教练表单选项（运动项目/社区）
export function getInstructorOptions() {
    return request.get({ url: '/instructor/options' })
}

// 新增教练
export function addInstructor(params: any) {
    return request.post({ url: '/instructor/add', params })
}

// 编辑教练
export function editInstructor(params: any) {
    return request.post({ url: '/instructor/edit', params })
}

// 删除教练
export function delInstructor(params: any) {
    return request.post({ url: '/instructor/del', params })
}

// 教练账号状态（#31）
export function getInstructorAccountStatus(params: any) {
    return request.get({ url: '/instructor/account/status', params })
}

// 开通教练账号：返回 initialPassword + account，口令只显示一次
export function openInstructorAccount(params: any) {
    return request.post({ url: '/instructor/account/open', params })
}

// 重置教练密码：返回 initialPassword + account
export function resetInstructorPassword(params: any) {
    return request.post({ url: '/instructor/account/reset', params })
}

// 停用教练账号
export function disableInstructorAccount(params: any) {
    return request.post({ url: '/instructor/account/disable', params })
}

// 启用教练账号
export function enableInstructorAccount(params: any) {
    return request.post({ url: '/instructor/account/enable', params })
}

// 教练账号操作审计
export function getInstructorAccountAudits(params: any) {
    return request.get({ url: '/instructor/account/audits', params })
}

// 教练资质审核列表（#49）
export function getInstructorCertList(params: any) {
    return request.get({ url: '/instructor/cert/list', params })
}

// 教练资质详情与审核留痕（#49）
export function getInstructorCertDetail(params: any) {
    return request.get({ url: '/instructor/cert/detail', params })
}

// 教练资质审核通过/驳回（#49）
export function auditInstructorCert(params: any) {
    return request.post({ url: '/instructor/cert/audit', params })
}
