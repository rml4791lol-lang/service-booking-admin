<template>
    <div class="report-class-apply">
        <el-card class="!border-none" shadow="never">
            <div class="flex items-start justify-between">
                <div>
                    <div class="text-lg font-medium">开班申请报表</div>
                    <div class="text-tx-secondary text-sm mt-1">
                        一条申请明细一行，按「街道 + 课程类型」聚合进度；状态 pending=招募中、
                        following=客服跟进中、closed=已开班。申请不生成订单、不扣课时。
                    </div>
                </div>
                <div class="flex items-center gap-2">
                    <el-button @click="caliberVisible = true">口径说明</el-button>
                    <el-button
                        v-perms="['report/class_apply/export']"
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
                <el-form-item label="街道">
                    <el-select v-model="query.streetId" class="w-[180px]" clearable filterable placeholder="全部街道">
                        <el-option v-for="item in options.streets" :key="item.id" :label="item.name" :value="item.id" />
                    </el-select>
                </el-form-item>
                <el-form-item label="课程类型">
                    <el-select v-model="query.courseType" class="w-[160px]" clearable placeholder="全部类型">
                        <el-option v-for="item in options.courseTypes" :key="item" :label="item" :value="item" />
                    </el-select>
                </el-form-item>
                <el-form-item label="组状态">
                    <el-select v-model="query.status" class="w-[150px]">
                        <el-option label="全部" value="" />
                        <el-option label="招募中" value="pending" />
                        <el-option label="客服跟进中" value="following" />
                        <el-option label="已开班" value="closed" />
                    </el-select>
                </el-form-item>
                <el-form-item label="关键词">
                    <el-input
                        v-model="query.keyword"
                        class="w-[220px]"
                        placeholder="街道/课程类型/小区/学员/手机号"
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
                <el-table-column label="申请时间" prop="createTime" width="166" />
                <el-table-column label="街道" min-width="120" show-overflow-tooltip>
                    <template #default="{ row }">{{ row.streetName || '-' }}</template>
                </el-table-column>
                <el-table-column label="课程类型" min-width="110" show-overflow-tooltip>
                    <template #default="{ row }">{{ row.courseType || '-' }}</template>
                </el-table-column>
                <el-table-column label="聚合进度" width="100" align="center">
                    <template #default="{ row }">
                        <span class="font-medium">{{ row.progress }}</span>
                    </template>
                </el-table-column>
                <el-table-column label="组状态" width="120" align="center">
                    <template #default="{ row }">
                        <el-tag :type="statusTagType(row.groupStatus)" size="small">{{ row.groupStatusText }}</el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="小区" min-width="150" show-overflow-tooltip>
                    <template #default="{ row }">{{ row.areaName || '-' }}</template>
                </el-table-column>
                <el-table-column label="学员" min-width="90" show-overflow-tooltip>
                    <template #default="{ row }">{{ row.studentName || '-' }}</template>
                </el-table-column>
                <el-table-column label="家长 / 手机号" min-width="150" show-overflow-tooltip>
                    <template #default="{ row }">
                        {{ row.parentName || '-' }}<span class="text-tx-secondary"> / {{ row.phone || row.parentMobile || '-' }}</span>
                    </template>
                </el-table-column>
                <el-table-column label="期望时间" min-width="150" show-overflow-tooltip>
                    <template #default="{ row }">{{ row.expectTime || '-' }}</template>
                </el-table-column>
                <el-table-column label="申请状态" width="120" align="center">
                    <template #default="{ row }">{{ row.statusText }}</template>
                </el-table-column>
                <el-table-column label="备注" min-width="180" show-overflow-tooltip>
                    <template #default="{ row }">{{ row.note || '-' }}</template>
                </el-table-column>
            </el-table>
            <div class="flex justify-end mt-4">
                <pagination v-model="pager" @change="getLists" />
            </div>
        </el-card>

        <el-dialog v-model="caliberVisible" title="开班申请报表口径说明" width="720px">
            <div class="whitespace-pre-wrap leading-7">{{ pager.extend.caliber || '暂无口径说明' }}</div>
            <template #footer>
                <el-button @click="caliberVisible = false">关闭</el-button>
            </template>
        </el-dialog>
    </div>
</template>

<script lang="ts" setup name="reportClassApply">
import { exportClassApplyReport, getClassApplyReport } from '@/api/report'
import { usePaging } from '@/hooks/usePaging'
import feedback from '@/utils/feedback'
import { compactParams, numberText } from '../helpers'

const query = reactive({
    startDate: '',
    endDate: '',
    streetId: '' as number | string,
    courseType: '',
    status: '',
    keyword: ''
})

const caliberVisible = ref(false)
const exporting = ref(false)

const { pager, getLists, resetPage, resetParams } = usePaging({
    fetchFun: (params: any) => getClassApplyReport(compactParams({ ...params, ...query })),
    params: query,
    size: 20
})

const options = computed<any>(() => pager.extend?.options || {})
const summary = computed<any>(() => pager.extend?.summary || {})

const summaryItems = computed(() => [
    { label: '申请总数', value: numberText(summary.value.total) },
    { label: '聚合组数', value: numberText(summary.value.groupCount) },
    { label: '涉及学员', value: numberText(summary.value.studentCount) },
    { label: '招募中', value: numberText(summary.value.pendingCount) },
    { label: '客服跟进中', value: numberText(summary.value.followingCount) },
    { label: '已开班', value: numberText(summary.value.closedCount) }
])

function statusTagType(status: string) {
    if (status === 'following') return 'warning'
    if (status === 'closed') return 'success'
    return 'info'
}

async function handleExport() {
    exporting.value = true
    feedback.loading('正在导出开班申请报表...')
    try {
        await exportClassApplyReport(compactParams({ ...query }))
    } finally {
        feedback.closeLoading()
        exporting.value = false
    }
}

getLists()
</script>
