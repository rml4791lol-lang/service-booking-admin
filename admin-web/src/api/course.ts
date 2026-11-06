import request from '@/utils/request'

// 运动项目分类列表（all=1 时包含隐藏项）
export function getCategoryAll(params: any) {
    return request.get({ url: '/course.class/categoryAll', params })
}

// 新增运动项目分类
export function addCategory(params: any) {
    return request.post({ url: '/course.class/categoryAdd', params })
}

// 编辑运动项目分类
export function editCategory(params: any) {
    return request.post({ url: '/course.class/categoryEdit', params })
}
