<template>
    <div class="coach-revenue">
        <el-card class="!border-none" shadow="never">
            <div class="flex flex-wrap items-start justify-between gap-4">
                <div>
                    <div class="text-lg font-medium">教练月营业额</div>
                    <div class="text-tx-secondary text-sm mt-1 leading-6">
                        按自然月统计每位教练的订单数、上课数、服务人次与总营业额，可下钻到当月课节与订单明细。
                        金额只统计已支付且未退款的订单，只做真实营业额展示，不做任何二次分配计算。
                    </div>
                </div>
                <div class="flex shrink-0 items-center gap-2">
                    <el-button
                        size="small"
                        :type="isThisMonth ? 'primary' : ''"
                        @click="selectMonth('current')"
                    >
                        本月
                    </el-button>
                    <el-button
                        size="small"
                        :type="isLastMonth ? 'primary' : ''"
                        @click="selectMonth('last')"
                    >
                        上月
                    </el-button>
                    <el-button
                        v-perms="['report/course/coach_month/export']"
                        type="primary"
                        :loading="exporting"
                        @click="handleExport"
                    >
                        导出 Excel
                    </el-button>
                </div>
            </div>
            <div class="text-tx-secondary mt-3 text-sm">
                统计月份：{{ month }}　期间：{{ periodStart || '-' }} ~ {{ periodEnd || '-' }}
            </div>
        </el-card>

        <el-card class="!border-none mt-4" shadow="never">
            <div class="grid grid-cols-2 gap-3 md:grid-cols-4">
                <div v-for="item in summaryItems" :key="item.label" class="rounded bg-page p-3">
                    <div class="text-xs text-tx-secondary">{{ item.label }}</div>
                    <div class="mt-1 text-lg font-medium">{{ item.value }}</div>
                </div>
            </div>
            <el-alert
                v-if="couponNote"
                class="mt-4"
                type="warning"
                :closable="false"
                :title="couponNote"
            />
        </el-card>

        <el-card class="!border-none mt-4" shadow="never">
            <el-table size="large" table-layout="fixed" v-loading="pager.loading" :data="pager.lists">
                <el-table-column label="教练" min-width="180" show-overflow-tooltip>
                    <template #default="{ row }">
                        <span :class="{ 'text-tx-secondary': !row.coachId }">
                            {{ row.coachName || (row.coachId ? `教练#${row.coachId}` : '未分配教练') }}
                        </span>
                    </template>
                </el-table-column>
                <el-table-column label="订单数" prop="orderCount" width="96" align="center" />
                <el-table-column label="总营业额" min-width="130" align="right">
                    <template #default="{ row }">￥{{ money(row.paidAmount) }}</template>
                </el-table-column>
                <el-table-column label="上课数(场次)" prop="lessonCount" width="130" align="center" />
                <el-table-column label="服务人次" prop="servicePersons" width="110" align="center" />
                <el-table-column label="操作" width="96" fixed="right" align="center">
                    <template #default="{ row }">
                        <el-button type="primary" link @click="openDetail(row)">明细</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <div class="flex justify-end mt-4">
                <pagination v-model="pager" @change="getLists" />
            </div>
        </el-card>

        <el-dialog v-model="detailVisible" title="教练月营业额明细" width="880px">
            <template v-if="detail">
                <div class="mb-3 text-base font-medium">
                    {{ detail.coachName || '-' }}　
                    <span class="text-tx-secondary text-sm">
                        {{ detail.month }}　订单 {{ detail.summary?.orderCount || 0 }} 笔　
                        总营业额 ￥{{ money(detail.summary?.paidAmount) }}　
                        上课 {{ detail.summary?.lessonCount || 0 }} 场　
                        服务 {{ detail.summary?.servicePersons || 0 }} 人次
                    </span>
                </div>

                <div class="mb-2 font-medium">课节明细</div>
                <el-table size="small" :data="detail.lessons || []" border max-height="260">
                    <el-table-column label="课程班级" min-width="180" show-overflow-tooltip>
                        <template #default="{ row }">
                            {{ row.courseName || `班级#${row.courseClassId}` }}
                        </template>
                    </el-table-column>
                    <el-table-column label="课节" prop="lessonNo" width="80" align="center" />
                    <el-table-column label="服务人次" prop="servicePersons" width="100" align="center" />
                    <el-table-column label="最近核销" prop="lastConsumeTime" width="170" />
                </el-table>
                <div v-if="detail.lessonsTruncated" class="text-tx-secondary mt-1 text-xs">
                    课节明细超过 500 行，已截断；完整数据请用导出。
                </div>

                <div class="mt-4 mb-2 font-medium">订单明细</div>
                <el-table size="small" :data="detail.orders || []" border max-height="260">
                    <el-table-column label="订单号" prop="sn" min-width="180" show-overflow-tooltip />
                    <el-table-column label="课程班级" min-width="160" show-overflow-tooltip>
                        <template #default="{ row }">
                            {{ row.courseName || `班级#${row.courseClassId}` }}
                        </template>
                    </el-table-column>
                    <el-table-column label="学员" min-width="110" show-overflow-tooltip>
                        <template #default="{ row }">
                            {{ row.studentName || (row.studentId ? `学员#${row.studentId}` : '-') }}
                        </template>
                    </el-table-column>
                    <el-table-column label="购买课时" prop="lessonCount" width="96" align="center" />
                    <el-table-column label="实付金额" width="120" align="right">
                        <template #default="{ row }">￥{{ money(row.orderAmount) }}</template>
                    </el-table-column>
                    <el-table-column label="支付时间" prop="payTime" width="170" />
                </el-table>
                <div v-if="detail.ordersTruncated" class="text-tx-secondary mt-1 text-xs">
                    订单明细超过 500 行，已截断；完整数据请用导出。
                </div>

                <div class="text-tx-secondary mt-3 text-xs leading-5">
                    {{ detail.caliber || '' }}
                </div>
            </template>
            <template #footer>
                <el-button @click="detailVisible = false">关闭</el-button>
            </template>
        </el-dialog>
    </div>
</template>

<script lang="ts" setup name="instructorRevenue">
import {
    exportCoachMonthReport,
    getCoachMonthDetail,
    getCoachMonthReport
} from '@/api/report'
import { usePaging } from '@/hooks/usePaging'
import feedback from '@/utils/feedback'
import { compactParams, money, numberText } from '@/views/report/helpers'

function monthText(offset: number) {
    const date = new Date()
    const target = new Date(date.getFullYear(), date.getMonth() + offset, 1, 12, 0, 0)
    const month = String(target.getMonth() + 1).padStart(2, '0')
    return `${target.getFullYear()}-${month}`
}

const thisMonth = monthText(0)
const lastMonth = monthText(-1)

const query = reactive({ month: thisMonth })
const exporting = ref(false)
const detailVisible = ref(false)
const detail = ref<any>(null)

const { pager, getLists } = usePaging({
    fetchFun: (params: any) => getCoachMonthReport(compactParams({ ...params, ...query })),
    params: query,
    size: 20
})

const month = computed(() => pager.extend?.month || query.month)
const periodStart = computed(() => pager.extend?.periodStart)
const periodEnd = computed(() => pager.extend?.periodEnd)
const couponNote = computed(() => pager.extend?.couponNote || '')
const isThisMonth = computed(() => month.value === thisMonth)
const isLastMonth = computed(() => month.value === lastMonth)

const summary = computed<any>(() => pager.extend?.summary || {})

const summaryItems = computed(() => [
    { label: '教练数', value: numberText(summary.value.coachCount) },
    { label: '订单数', value: numberText(summary.value.orderCount) },
    { label: '总营业额', value: `￥${money(summary.value.paidAmount)}` },
    { label: '上课数(场次)', value: numberText(summary.value.lessonCount) },
    { label: '服务人次', value: numberText(summary.value.servicePersons) }
])

function selectMonth(type: 'current' | 'last') {
    query.month = type === 'current' ? thisMonth : lastMonth
    pager.page = 1
    getLists()
}

async function openDetail(row: any) {
    detail.value = null
    detailVisible.value = true
    try {
        detail.value = await getCoachMonthDetail({
            month: month.value,
            coachId: row.coachId
        })
    } catch (error) {
        detail.value = null
    }
}

async function handleExport() {
    exporting.value = true
    feedback.loading('正在导出教练月营业额...')
    try {
        await exportCoachMonthReport(compactParams({ ...query }))
    } finally {
        feedback.closeLoading()
        exporting.value = false
    }
}

getLists()
</script>
