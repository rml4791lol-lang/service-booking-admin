import request from '@/utils/request'

// ---------- 区域基础数据 ----------
export function regionStreets() {
    return request.get({ url: '/region/streets' })
}

// ---------- 社区 ----------
export function communityList(params?: any) {
    return request.get({ url: '/community/list', params })
}

export function communityAll(params?: any) {
    return request.get({ url: '/community/all', params })
}

export function communityDetail(params: any) {
    return request.get({ url: '/community/detail', params })
}

export function communityAdd(params: any) {
    return request.post({ url: '/community/add', params })
}

export function communityEdit(params: any) {
    return request.post({ url: '/community/edit', params })
}

export function communityDelete(params: any) {
    return request.post({ url: '/community/del', params })
}

// ---------- 小区 ----------
export function areaList(params?: any) {
    return request.get({ url: '/area/list', params })
}

export function areaAll(params?: any) {
    return request.get({ url: '/area/all', params })
}

export function areaAdd(params: any) {
    return request.post({ url: '/area/add', params })
}

export function areaEdit(params: any) {
    return request.post({ url: '/area/edit', params })
}

export function areaDelete(params: any) {
    return request.post({ url: '/area/del', params })
}
