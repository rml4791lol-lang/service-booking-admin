<template>
    <div class="report-activity">
        <el-card class="!border-none" shadow="never">
            <div class="flex items-start justify-between">
                <div>
                    <div class="text-lg font-medium">活动报名报表</div>
                    <div class="text-tx-secondary text-sm mt-1">
                        一条报名明细一行：状态、订单、金额与活动/小区/社区关联；可按时间、社区、活动、状态筛选。
                        金额仅统计已支付订单。
                    </div>
                </div>
                <div class="flex items-center gap-2">
                    <el-button @click="caliberVisible = true">口径说明</el-button>
                    <el-button
                        v-perms="['report/activity/export']"
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
                <el-form-item label="活动">
                    <el-select v-model="query.activityId" class="w-[230px]" clearable filterable placeholder="全部活动">
                        <el-option
                            v-for="item in options.activities"
                            :key="item.id"
                            :label="`${item.name}（${item.activityDate || '-'}）`"
                            :value="item.id"
                        />
                    </el-select>
                </el-form-item>
                <el-form-item label="状态">
                    <el-select v-model="query.status" class="w-[130px]">
                        <el-option label="全部" value="" />
                        <el-option label="待支付" value="0" />
                        <el-option label="已支付" value="1" />
                        <el-option label="已取消" value="3" />
                    </el-select>
                </el-form-item>
                <el-form-item label="关键词">
                    <el-input
                        v-model="query.keyword"
                        class="w-[220px]"
                        placeholder="活动/小区/学员/家长/手机号"
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
                <el-table-column label="报名时间" prop="signupTime" width="166" />
                <el-table-column label="活动" min-width="190" show-overflow-tooltip>
                    <template #default="{ row }">{{ row.activityTitle || '-' }}</template>
                </el-table-column>
                <el-table-column label="活动日期 / 时段" min-width="150">
                    <template #default="{ row }">
                        {{ row.activityDate || '-' }}<span class="text-tx-secondary"> {{ row.timeSlot }}</span>
                    </template>
                </el-table-column>
                <el-table-column label="地点" min-width="160" show-overflow-tooltip>
                    <template #default="{ row }">{{ row.place || '-' }}</template>
                </el-table-column>
                <el-table-column label="小区 / 社区" min-width="150" show-overflow-tooltip>
                    <template #default="{ row }">
                        {{ row.areaName || '-' }}<span class="text-tx-secondary"> / {{ row.communityName || '-' }}</span>
                    </template>
                </el-table-column>
                <el-table-column label="学员" min-width="90" show-overflow-tooltip>
                    <template #default="{ row }">{{ row.studentName || '-' }}</template>
                </el-table-column>
                <el-table-column label="家长 / 手机号" min-width="150" show-overflow-tooltip>
                    <template #default="{ row }">
                        {{ row.parentName || '-' }}<span class="text-tx-secondary"> / {{ row.parentMobile || '-' }}</span>
                    </template>
                </el-table-column>
                <el-table-column label="状态" width="96" align="center">
                    <template #default="{ row }">
                        <el-tag :type="statusTagType(row.status)" size="small">{{ row.statusText }}</el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="订单号" min-width="200" show-overflow-tooltip>
                    <template #default="{ row }">{{ row.orderSn || '-' }}</template>
                </el-table-column>
                <el-table-column label="金额" width="104" align="right">
                    <template #default="{ row }">￥{{ money(row.orderAmount) }}</template>
                </el-table-column>
                <el-table-column label="支付状态" width="100" align="center">
                    <template #default="{ row }">{{ row.payStatusText || '-' }}</template>
                </el-table-column>
            </el-table>
            <div class="flex justify-end mt-4">
                <pagination v-model="pager" @change="getLists" />
            </div>
        </el-card>

        <el-dialog v-model="caliberVisible" title="活动报名报表口径说明" width="720px">
            <div class="whitespace-pre-wrap leading-7">{{ pager.extend.caliber || '暂无口径说明' }}</div>
            <template #footer>
                <el-button @click="caliberVisible = false">关闭</el-button>
            </template>
        </el-dialog>
    </div>
</template>

<script lang="ts" setup name="reportActivity">
import { exportActivityReport, getActivityReport } from '@/api/report'
import { usePaging } from '@/hooks/usePaging'
import feedback from '@/utils/feedback'
import { compactParams, money, numberText } from '../helpers'

const query = reactive({
    startDate: '',
    endDate: '',
    communityId: '' as number | string,
    activityId: '' as number | string,
    status: '',
    keyword: ''
})

const caliberVisible = ref(false)
const exporting = ref(false)

const { pager, getLists, resetPage, resetParams } = usePaging({
    fetchFun: (params: any) => getActivityReport(compactParams({ ...params, ...query })),
    params: query,
    size: 20
})

const options = computed<any>(() => pager.extend?.options || {})
const summary = computed<any>(() => pager.extend?.summary || {})

const summaryItems = computed(() => [
    { label: '报名总数', value: numberText(summary.value.total) },
    { label: '已支付', value: numberText(summary.value.paidCount) },
    { label: '待支付', value: numberText(summary.value.pendingCount) },
    { label: '已取消', value: numberText(summary.value.cancelledCount) },
    { label: '已支付金额', value: `￥${money(summary.value.paidAmount)}` },
    { label: '涉及学员', value: numberText(summary.value.studentCount) }
])

function statusTagType(status: number) {
    if (status === 1) return 'success'
    if (status === 3) return 'info'
    return 'warning'
}

async function handleExport() {
    exporting.value = true
    feedback.loading('正在导出活动报名报表...')
    try {
        await exportActivityReport(compactParams({ ...query }))
    } finally {
        feedback.closeLoading()
        exporting.value = false
    }
}

getLists()
</script>
