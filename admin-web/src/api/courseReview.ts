import request from '@/utils/request'

/** 教练评价管理（#79）：列表 / 详情 / 删除。 */
export function getCourseReviewList(params: any) {
    // 后端沿用分页参数 pageNo/pageSize；管理端 usePaging 统一传 page_no/page_size。
    const data: any = { ...params }
    data.pageNo = data.page_no || data.pageNo || 1
    data.pageSize = data.page_size || data.pageSize || 15
    delete data.page_no
    delete data.page_size
    return request.get({ url: '/course_review/list', params: data }, { ignoreCancelToken: true })
}

export function getCourseReviewDetail(id: number | string) {
    return request.get({ url: '/course_review/detail', params: { id } })
}

export function delCourseReview(id: number | string) {
    return request.post({ url: '/course_review/del', params: { id } })
}
