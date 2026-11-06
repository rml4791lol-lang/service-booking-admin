<template>
    <div>
        <el-card class="!border-none" shadow="never">
            <div class="text-lg font-medium">课后总结</div>
            <div class="text-tx-secondary text-sm mt-1 leading-6">
                教练在核销时提交的课堂总结与照片，按班级/教练/关键词检索；含课节维度（第几节、上课日期、时段）。
                本页只读，用于客服核对与投诉举证。
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
                        <el-option v-for="item in classOptions" :key="item.id" :label="item.name" :value="item.id" />
                    </el-select>
                </el-form-item>
                <el-form-item label="教练">
                    <el-select v-model="queryParams.instructorId" class="w-[180px]" clearable filterable placeholder="全部">
                        <el-option v-for="item in instructorOptions" :key="item.id" :label="item.name" :value="item.id" />
                    </el-select>
                </el-form-item>
                <el-form-item label="总结内容">
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
                <el-table-column label="课程班级" min-width="160" show-overflow-tooltip>
                    <template #default="{ row }">{{ row.courseName || `班级#${row.courseClassId}` }}</template>
                </el-table-column>
                <el-table-column label="课次" width="80" align="center">
                    <template #default="{ row }">第 {{ row.lessonNo }} 节</template>
                </el-table-column>
                <el-table-column label="上课日期" min-width="110">
                    <template #default="{ row }">{{ row.lessonDate || '-' }}</template>
                </el-table-column>
                <el-table-column label="时段" min-width="120">
                    <template #default="{ row }">{{ row.timeSlot || '-' }}</template>
                </el-table-column>
                <el-table-column label="教练" min-width="100">
                    <template #default="{ row }">{{ row.instructorName || '-' }}</template>
                </el-table-column>
                <el-table-column label="课堂总结" min-width="240" show-overflow-tooltip>
                    <template #default="{ row }">{{ row.summary || '（未填写文字）' }}</template>
                </el-table-column>
                <el-table-column label="照片" width="86" align="center">
                    <template #default="{ row }">
                        <el-button v-if="imagesOf(row).length" type="primary" link @click="openDetail(row)">
                            {{ imagesOf(row).length }} 张
                        </el-button>
                        <span v-else class="text-gray-400">-</span>
                    </template>
                </el-table-column>
                <el-table-column label="提交时间" min-width="150">
                    <template #default="{ row }">{{ row.createTime || '-' }}</template>
                </el-table-column>
                <el-table-column label="操作" width="80" fixed="right">
                    <template #default="{ row }">
                        <el-button type="primary" link @click="openDetail(row)">查看</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <div class="flex justify-end mt-4">
                <pagination v-model="pager" @change="getLists" />
            </div>
        </el-card>

        <el-dialog v-model="detailVisible" title="课堂总结详情" width="720px">
            <el-descriptions :column="2" border>
                <el-descriptions-item label="课程班级">{{ current.courseName || '-' }}</el-descriptions-item>
                <el-descriptions-item label="教练">{{ current.instructorName || '-' }}</el-descriptions-item>
                <el-descriptions-item label="课次">第 {{ current.lessonNo }} 节</el-descriptions-item>
                <el-descriptions-item label="上课日期">{{ current.lessonDate || '-' }}</el-descriptions-item>
                <el-descriptions-item label="时段">{{ current.timeSlot || '-' }}</el-descriptions-item>
                <el-descriptions-item label="地点">{{ current.venue || '-' }}</el-descriptions-item>
                <el-descriptions-item label="提交时间">{{ current.createTime || '-' }}</el-descriptions-item>
                <el-descriptions-item label="照片数">{{ imagesOf(current).length }}</el-descriptions-item>
                <el-descriptions-item label="课堂总结" :span="2">
                    {{ current.summary || '（未填写文字）' }}
                </el-descriptions-item>
            </el-descriptions>
            <div v-if="imagesOf(current).length" class="mt-4">
                <div class="font-medium mb-2">课堂照片</div>
                <div class="flex flex-wrap gap-2">
                    <el-image
                        v-for="(img, index) in imagesOf(current)"
                        :key="index"
                        class="w-[96px] h-[96px] rounded"
                        :src="img"
                        :preview-src-list="imagesOf(current)"
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

<script lang="ts" setup name="courseSummary">
import { getCourseLessonLogList } from '@/api/courseLesson'
import { classList } from '@/api/courseClass'
import { getInstructorAll } from '@/api/instructor'
import { usePaging } from '@/hooks/usePaging'

const queryParams = reactive({
    courseClassId: '',
    instructorId: '',
    keyword: ''
})

const { pager, getLists, resetPage, resetParams } = usePaging({
    fetchFun: getCourseLessonLogList,
    params: queryParams
})

const classOptions = ref<any[]>([])
const classLoading = ref(false)
const instructorOptions = ref<any[]>([])
const detailVisible = ref(false)
const current = ref<any>({})

function imagesOf(row: any) {
    if (!row) return []
    if (Array.isArray(row.images)) return row.images.filter(Boolean)
    if (typeof row.images === 'string' && row.images.trim()) {
        return row.images.split(',').map((s: string) => s.trim()).filter(Boolean)
    }
    return []
}

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

function openDetail(row: any) {
    current.value = row
    detailVisible.value = true
}

loadOptions()
getLists()
</script>
