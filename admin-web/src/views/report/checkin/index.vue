<template>
    <div class="report-checkin">
        <el-card class="!border-none" shadow="never">
            <div class="flex items-start justify-between">
                <div>
                    <div class="text-lg font-medium">核销报表</div>
                    <div class="text-tx-secondary text-sm mt-1">
                        一条消课流水一行，按核销时间归期；来源区分正常核销 / 历史人工 / 后台人工补核销。
                        预约状态按学员 + 课节匹配，未匹配到预约的历史流水单独标出。
                    </div>
                </div>
                <div class="flex items-center gap-2">
                    <el-button @click="caliberVisible = true">口径说明</el-button>
                    <el-button
                        v-perms="['report/checkin/export']"
                        type="primary"
                        :loading="exporting"
                        @click="handleExport"
                    >
                        导出 Excel
                    </el-button>
                </div>
            </div>
        </el-card>

        <el-card class="!border-none mt-4" shadow="never">
            <el-form class="mb-[-16px]" :inline="true" :model="query">
                <el-form-item label="开始日期">
                    <el-date-picker
                        v-model="query.startDate"
                        class="w-[150px]"
                        type="date"
                        value-format="YYYY-MM-DD"
                        placeholder="不限"
                        clearable
                    />
                </el-form-item>
                <el-form-item label="结束日期">
                    <el-date-picker
                        v-model="query.endDate"
                        class="w-[150px]"
                        type="date"
                        value-format="YYYY-MM-DD"
                        placeholder="不限"
                        clearable
                    />
                </el-form-item>
                <el-form-item label="社区">
                    <el-select v-model="query.communityId" class="w-[170px]" clearable filterable placeholder="全部社区">
                        <el-option v-for="item in options.communities" :key="item.id" :label="item.name" :value="item.id" />
                    </el-select>
                </el-form-item>
                <el-form-item label="课程">
                    <el-select v-model="query.courseId" class="w-[220px]" clearable filterable placeholder="全部课程">
                        <el-option v-for="item in options.courses" :key="item.id" :label="item.name" :value="item.id" />
                    </el-select>
                </el-form-item>
                <el-form-item label="教练">
                    <el-select v-model="query.instructorId" class="w-[160px]" clearable filterable placeholder="全部教练">
                        <el-option v-for="item in options.instructors" :key="item.id" :label="item.name" :value="item.id" />
                    </el-select>
                </el-form-item>
                <el-form-item label="核销来源">
                    <el-select v-model="query.sourceType" class="w-[150px]">
                        <el-option label="全部" value="" />
                        <el-option label="正常核销" :value="0" />
                        <el-option label="历史人工" :value="1" />
                        <el-option label="后台人工补核销" :value="2" />
                    </el-select>
                </el-form-item>
                <el-form-item label="预约状态">
                    <el-select v-model="query.status" class="w-[150px]">
                        <el-option label="全部" value="" />
                        <el-option label="已上课" value="attended" />
                        <el-option label="已预约未上课" value="booked" />
                        <el-option label="无预约记录" value="none" />
                    </el-select>
                </el-form-item>
                <el-form-item label="关键词">
                    <el-input
                        v-model="query.keyword"
                        class="w-[190px]"
                        placeholder="课程/学员/家长/手机号"
                        clearable
                        @keyup.enter="resetPage"
                    />
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="resetPage">查询</el-button>
                    <el-button @click="resetParams">重置</el-button>
                </el-form-item>
            </el-form>
        </el-card>

        <el-card class="!border-none mt-4" shadow="never">
            <div class="mb-4 flex flex-wrap gap-6">
                <div v-for="item in summaryItems" :key="item.label" class="rounded bg-page px-4 py-2">
                    <span class="text-xs text-tx-secondary">{{ item.label }}</span>
                    <span class="ml-2 text-base font-medium">{{ item.value }}</span>
                </div>
            </div>
            <el-table size="large" table-layout="fixed" v-loading="pager.loading" :data="pager.lists">
                <el-table-column label="核销时间" prop="consumeTime" width="166" />
                <el-table-column label="课程班级" min-width="170" show-overflow-tooltip>
                    <template #default="{ row }">{{ row.courseName || '-' }}</template>
                </el-table-column>
                <el-table-column label="社区" min-width="125" show-overflow-tooltip>
                    <template #default="{ row }">{{ row.communityName || '-' }}</template>
                </el-table-column>
                <el-table-column label="教练" min-width="92" show-overflow-tooltip>
                    <template #default="{ row }">{{ row.instructorName || '-' }}</template>
                </el-table-column>
                <el-table-column label="学员" min-width="90" show-overflow-tooltip>
                    <template #default="{ row }">{{ row.studentName || '-' }}</template>
                </el-table-column>
                <el-table-column label="家长 / 手机号" min-width="150" show-overflow-tooltip>
                    <template #default="{ row }">
                        {{ row.parentName || '-' }}<span class="text-tx-secondary"> / {{ row.parentMobile || '-' }}</span>
                    </template>
                </el-table-column>
                <el-table-column label="课节" width="76" align="center">
                    <template #default="{ row }">第{{ row.lessonNo }}节</template>
                </el-table-column>
                <el-table-column label="上课日期" width="112">
                    <template #default="{ row }">{{ row.lessonDate || '-' }}</template>
                </el-table-column>
                <el-table-column label="时段" width="130">
                    <template #default="{ row }">{{ row.timeSlot || '-' }}</template>
                </el-table-column>
                <el-table-column label="核销来源" width="140" align="center">
                    <template #default="{ row }">
                        <el-tag :type="row.sourceType === 0 ? 'success' : 'warning'" size="small">
                            {{ row.sourceTypeText }}
                        </el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="预约状态" width="126" align="center">
                    <template #default="{ row }">
                        <span :class="{ 'text-tx-secondary': row.statusKey === 'none' }">
                            {{ row.bookingStatusText }}
                        </span>
                    </template>
                </el-table-column>
            </el-table>
            <div class="flex justify-end mt-4">
                <pagination v-model="pager" @change="getLists" />
            </div>
        </el-card>

        <el-dialog v-model="caliberVisible" title="核销报表口径说明" width="720px">
            <div class="whitespace-pre-wrap leading-7">{{ pager.extend.caliber || '暂无口径说明' }}</div>
            <template #footer>
                <el-button @click="caliberVisible = false">关闭</el-button>
            </template>
        </el-dialog>
    </div>
</template>

<script lang="ts" setup name="reportCheckin">
import { exportCheckinReport, getCheckinReport } from '@/api/report'
import { usePaging } from '@/hooks/usePaging'
import feedback from '@/utils/feedback'
import { compactParams, numberText } from '../helpers'

const query = reactive({
    startDate: '',
    endDate: '',
    communityId: '' as number | string,
    courseId: '' as number | string,
    instructorId: '' as number | string,
    sourceType: '' as number | string,
    status: '',
    keyword: ''
})

const caliberVisible = ref(false)
const exporting = ref(false)

const { pager, getLists, resetPage, resetParams } = usePaging({
    fetchFun: (params: any) => getCheckinReport(compactParams({ ...params, ...query })),
    params: query,
    size: 20
})

const options = computed<any>(() => pager.extend?.options || {})
const summary = computed<any>(() => pager.extend?.summary || {})

const summaryItems = computed(() => [
    { label: '核销总数', value: numberText(summary.value.total) },
    { label: '正常核销', value: numberText(summary.value.normalCount) },
    { label: '人工核销', value: numberText(summary.value.manualCount) },
    { label: '已上课', value: numberText(summary.value.attendedCount) },
    { label: '无预约记录', value: numberText(summary.value.noBookingCount) },
    { label: '涉及学员', value: numberText(summary.value.studentCount) }
])

async function handleExport() {
    exporting.value = true
    feedback.loading('正在导出核销报表...')
    try {
        await exportCheckinReport(compactParams({ ...query }))
    } finally {
        feedback.closeLoading()
        exporting.value = false
    }
}

getLists()
</script>
