<template>
    <div class="report-coupon">
        <el-card class="!border-none" shadow="never">
            <div class="flex items-start justify-between">
                <div>
                    <div class="text-lg font-medium">券流水报表</div>
                    <div class="text-tx-secondary text-sm mt-1">
                        券流水只追加、不删除：发放、核销扣减、补发、作废、退回逐条留痕；
                        可按时间、券商品、流水类型与学员/订单关键词筛选。
                    </div>
                </div>
                <div class="flex items-center gap-2">
                    <el-button @click="caliberVisible = true">口径说明</el-button>
                    <el-button
                        v-perms="['report/coupon/export']"
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
                <el-form-item label="券商品">
                    <el-select v-model="query.couponGoodsId" class="w-[220px]" clearable filterable placeholder="全部券商品">
                        <el-option v-for="item in options.couponGoods" :key="item.id" :label="item.name" :value="item.id" />
                    </el-select>
                </el-form-item>
                <el-form-item label="流水类型">
                    <el-select v-model="query.flowType" class="w-[150px]">
                        <el-option label="全部" value="" />
                        <el-option label="发放" :value="1" />
                        <el-option label="核销扣减" :value="2" />
                        <el-option label="补发" :value="3" />
                        <el-option label="作废" :value="4" />
                        <el-option label="退回" :value="5" />
                    </el-select>
                </el-form-item>
                <el-form-item label="关键词">
                    <el-input
                        v-model="query.keyword"
                        class="w-[220px]"
                        placeholder="券号/券名/学员/订单号/备注"
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
                <el-table-column label="时间" prop="createTime" width="166" />
                <el-table-column label="类型" width="104" align="center">
                    <template #default="{ row }">
                        <el-tag :type="flowTagType(row.type)" size="small">{{ row.typeText }}</el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="券编号" min-width="190" show-overflow-tooltip>
                    <template #default="{ row }">{{ row.couponSn || '-' }}</template>
                </el-table-column>
                <el-table-column label="券名称 / 券商品" min-width="190" show-overflow-tooltip>
                    <template #default="{ row }">
                        {{ row.couponTitle || '-' }}
                        <span v-if="row.goodsTitle && row.goodsTitle !== row.couponTitle" class="text-tx-secondary">
                            / {{ row.goodsTitle }}
                        </span>
                    </template>
                </el-table-column>
                <el-table-column label="学员" min-width="90" show-overflow-tooltip>
                    <template #default="{ row }">{{ row.studentName || '-' }}</template>
                </el-table-column>
                <el-table-column label="家长" min-width="90" show-overflow-tooltip>
                    <template #default="{ row }">{{ row.parentName || '-' }}</template>
                </el-table-column>
                <el-table-column label="变动次数" width="92" align="center">
                    <template #default="{ row }">
                        <span :class="row.times < 0 ? 'text-error' : 'text-success'">{{ row.times }}</span>
                    </template>
                </el-table-column>
                <el-table-column label="变动后余额" prop="balance" width="102" align="center" />
                <el-table-column label="关联订单号" min-width="200" show-overflow-tooltip>
                    <template #default="{ row }">{{ row.orderSn || '-' }}</template>
                </el-table-column>
                <el-table-column label="操作人" width="120" align="center">
                    <template #default="{ row }">
                        {{ row.operatorTypeText }}<span v-if="row.operatorId"> #{{ row.operatorId }}</span>
                    </template>
                </el-table-column>
                <el-table-column label="备注" min-width="180" show-overflow-tooltip>
                    <template #default="{ row }">{{ row.remark || '-' }}</template>
                </el-table-column>
            </el-table>
            <div class="flex justify-end mt-4">
                <pagination v-model="pager" @change="getLists" />
            </div>
        </el-card>

        <el-dialog v-model="caliberVisible" title="券流水报表口径说明" width="720px">
            <div class="whitespace-pre-wrap leading-7">{{ pager.extend.caliber || '暂无口径说明' }}</div>
            <template #footer>
                <el-button @click="caliberVisible = false">关闭</el-button>
            </template>
        </el-dialog>
    </div>
</template>

<script lang="ts" setup name="reportCoupon">
import { exportCouponReport, getCouponReport } from '@/api/report'
import { usePaging } from '@/hooks/usePaging'
import feedback from '@/utils/feedback'
import { compactParams, numberText } from '../helpers'

const query = reactive({
    startDate: '',
    endDate: '',
    couponGoodsId: '' as number | string,
    flowType: '' as number | string,
    keyword: ''
})

const caliberVisible = ref(false)
const exporting = ref(false)

const { pager, getLists, resetPage, resetParams } = usePaging({
    fetchFun: (params: any) => getCouponReport(compactParams({ ...params, ...query })),
    params: query,
    size: 20
})

const options = computed<any>(() => pager.extend?.options || {})
const summary = computed<any>(() => pager.extend?.summary || {})

const summaryItems = computed(() => [
    { label: '流水总数', value: numberText(summary.value.total) },
    { label: '发放', value: numberText(summary.value.grantCount) },
    { label: '核销扣减', value: numberText(summary.value.consumeCount) },
    { label: '补发', value: numberText(summary.value.reissueCount) },
    { label: '作废', value: numberText(summary.value.cancelCount) },
    { label: '退回', value: numberText(summary.value.refundCount) },
    { label: '入账次数', value: numberText(summary.value.timesIn) },
    { label: '出账次数', value: numberText(summary.value.timesOut) }
])

function flowTagType(type: number) {
    if (type === 1) return 'success'
    if (type === 2) return 'danger'
    if (type === 3) return 'warning'
    return 'info'
}

async function handleExport() {
    exporting.value = true
    feedback.loading('正在导出券流水报表...')
    try {
        await exportCouponReport(compactParams({ ...query }))
    } finally {
        feedback.closeLoading()
        exporting.value = false
    }
}

getLists()
</script>
