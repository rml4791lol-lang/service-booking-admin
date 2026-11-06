<template>
    <div>
        <el-card class="!border-none" shadow="never">
            <div class="flex items-start justify-between gap-4">
                <div>
                    <div class="text-lg font-medium">教练评价管理</div>
                    <div class="text-tx-secondary text-sm mt-1 leading-6">
                        家长对已上课程的 3 维评分（教学质量/课堂氛围/教练态度）、文字与图片；
                        可查看教练回复并删除违规评价。评分只用于展示与运营核对，不影响结算。
                    </div>
                </div>
            </div>
        </el-card>

        <el-card class="!border-none mt-4" shadow="never">
            <el-form class="mb-[-16px]" :inline="true" :model="queryParams">
                <el-form-item label="课程班级">
                    <el-select
                        v-model="queryParams.courseClassId"
                        class="w-[260px]"
                        clearable
                        filterable
                        remote
                        reserve-keyword
                        :remote-method="searchClasses"
                        :loading="classLoading"
                        placeholder="输入班级名称搜索"
                    >
                        <el-option
                            v-for="item in classOptions"
                            :key="item.id"
                            :label="item.name"
                            :value="item.id"
                        />
                    </el-select>
                </el-form-item>
                <el-form-item label="教练">
                    <el-select v-model="queryParams.instructorId" class="w-[180px]" clearable filterable placeholder="全部">
                        <el-option v-for="item in instructorOptions" :key="item.id" :label="item.name" :value="item.id" />
                    </el-select>
                </el-form-item>
                <el-form-item label="评价内容">
                    <el-input v-model="queryParams.keyword" class="w-[200px]" placeholder="模糊搜索" clearable @keyup.enter="resetPage" />
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="resetPage">查询</el-button>
                    <el-button @click="resetParams">重置</el-button>
                </el-form-item>
            </el-form>
        </el-card>

        <el-card class="!border-none mt-4" shadow="never">
            <el-table size="large" table-layout="fixed" v-loading="pager.loading" :data="pager.lists">
                <el-table-column label="ID" prop="id" width="70" />
                <el-table-column label="学员/家长" min-width="130">
                    <template #default="{ row }">
                        <div>{{ row.userName || `用户#${row.userId}` }}</div>
                        <div class="text-gray-400 text-xs">{{ row.userMobile || '' }}</div>
                    </template>
                </el-table-column>
                <el-table-column label="课程班级" min-width="160" show-overflow-tooltip>
                    <template #default="{ row }">{{ row.courseName || `班级#${row.courseClassId}` }}</template>
                </el-table-column>
                <el-table-column label="教练" min-width="100">
                    <template #default="{ row }">{{ row.instructorName || `#${row.instructorId}` }}</template>
                </el-table-column>
                <el-table-column label="课次" prop="lessonNo" width="70" align="center" />
                <el-table-column label="教学质量" prop="ratingTeaching" width="86" align="center" />
                <el-table-column label="课堂氛围" prop="ratingAtmosphere" width="86" align="center" />
                <el-table-column label="教练态度" prop="ratingAttitude" width="86" align="center" />
                <el-table-column label="评价内容" min-width="180" show-overflow-tooltip>
                    <template #default="{ row }">{{ row.content || '-' }}</template>
                </el-table-column>
                <el-table-column label="图片" width="80" align="center">
                    <template #default="{ row }">
                        <el-button v-if="row.images && row.images.length" type="primary" link @click="preview(row)">                            
                            {{ row.images.length }} 张
                        </el-button>
                        <span v-else class="text-gray-400">-</span>
                    </template>
                </el-table-column>
                <el-table-column label="回复" width="90" align="center">
                    <template #default="{ row }">
                        <el-tag v-if="row.replyContent" type="success" size="small">已回复</el-tag>
                        <span v-else class="text-gray-400 text-xs">未回复</span>
                    </template>
                </el-table-column>
                <el-table-column label="评价时间" min-width="150">
                    <template #default="{ row }">{{ row.createTime || '-' }}</template>
                </el-table-column>
                <el-table-column label="操作" width="120" fixed="right">
                    <template #default="{ row }">
                        <el-button v-perms="['course_review/detail']" type="primary" link @click="openDetail(row)">
                            详情
                        </el-button>
                        <el-button v-perms="['course_review/del']" type="danger" link @click="handleDelete(row)">
                            删除
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
            <div class="flex justify-end mt-4">
                <pagination v-model="pager" @change="getLists" />
            </div>
        </el-card>

        <el-dialog v-model="detailVisible" title="评价详情" width="720px">
            <el-descriptions :column="2" border>
                <el-descriptions-item label="评价 ID">{{ current.id }}</el-descriptions-item>
                <el-descriptions-item label="学员/家长">{{ current.userName || '-' }}</el-descriptions-item>
                <el-descriptions-item label="课程班级">{{ current.courseName || '-' }}</el-descriptions-item>
                <el-descriptions-item label="教练">{{ current.instructorName || '-' }}</el-descriptions-item>
                <el-descriptions-item label="课次">第 {{ current.lessonNo }} 节</el-descriptions-item>
                <el-descriptions-item label="评价时间">{{ current.createTime || '-' }}</el-descriptions-item>
                <el-descriptions-item label="教学质量">{{ current.ratingTeaching }} / 5</el-descriptions-item>
                <el-descriptions-item label="课堂氛围">{{ current.ratingAtmosphere }} / 5</el-descriptions-item>
                <el-descriptions-item label="教练态度">{{ current.ratingAttitude }} / 5</el-descriptions-item>
                <el-descriptions-item label="手机号">{{ current.userMobile || '-' }}</el-descriptions-item>
                <el-descriptions-item label="评价内容" :span="2">{{ current.content || '（未填写文字）' }}</el-descriptions-item>
                <el-descriptions-item label="教练回复" :span="2">
                    <div v-if="current.replyContent">
                        <div>{{ current.replyContent }}</div>
                        <div class="text-gray-400 text-xs mt-1">{{ current.replyTime }}</div>
                    </div>
                    <span v-else class="text-gray-400">未回复</span>
                </el-descriptions-item>
            </el-descriptions>
            <div v-if="current.images && current.images.length" class="mt-4">
                <div class="font-medium mb-2">评价图片</div>
                <div class="flex flex-wrap gap-2">
                    <el-image
                        v-for="(img, index) in current.images"
                        :key="index"
                        class="w-[96px] h-[96px] rounded"
                        :src="img"
                        :preview-src-list="current.images"
                        fit="cover"
                        preview-teleported
                    />
                </div>
            </div>
            <template #footer>
                <el-button @click="detailVisible = false">关闭</el-button>
            </template>
        </el-dialog>
    </div>
</template>

<script lang="ts" setup name="courseReviewManage">
import { getCourseReviewDetail, getCourseReviewList, delCourseReview } from '@/api/courseReview'
import { classList } from '@/api/courseClass'
import { getInstructorAll } from '@/api/instructor'
import { usePaging } from '@/hooks/usePaging'
import feedback from '@/utils/feedback'

const queryParams = reactive({
    courseClassId: '',
    instructorId: '',
    keyword: ''
})

const { pager, getLists, resetPage, resetParams } = usePaging({
    fetchFun: getCourseReviewList,
    params: queryParams
})

const classOptions = ref<any[]>([])
const classLoading = ref(false)
const instructorOptions = ref<any[]>([])
const detailVisible = ref(false)
const current = ref<any>({})

async function loadOptions() {
    try {
        const list: any = await getInstructorAll()
        instructorOptions.value = Array.isArray(list) ? list : (list?.lists || [])
    } catch (e) {
        instructorOptions.value = []
    }
    await searchClasses('')
}

async function searchClasses(keyword: string) {
    classLoading.value = true
    try {
        const data: any = await classList({ page_no: 1, page_size: 50, name: keyword || '' })
        classOptions.value = data.lists || []
    } catch (e) {
        classOptions.value = []
    } finally {
        classLoading.value = false
    }
}

async function openDetail(row: any) {
    try {
        current.value = await getCourseReviewDetail(row.id)
    } catch (e) {
        current.value = row
    }
    detailVisible.value = true
}

function preview(row: any) {
    current.value = row
    detailVisible.value = true
}

async function handleDelete(row: any) {
    try {
        await feedback.confirm('删除后前台将不再展示该评价，确认删除？')
    } catch (e) {
        return
    }
    await delCourseReview(row.id)
    feedback.msgSuccess('已删除')
    getLists()
}

loadOptions()
getLists()
</script>
