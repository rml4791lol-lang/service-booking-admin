<template>
    <div class="report-course">
        <el-card class="!border-none" shadow="never">
            <div class="flex items-start justify-between">
                <div>
                    <div class="text-lg font-medium">课程报表</div>
                    <div class="text-tx-secondary text-sm mt-1">
                        按课程班级汇总真实营业额：订单实收、购买/已消课时、到场人次与场次。
                        金额只统计已支付且未退款的订单，不做任何二次分配计算。
                    </div>
                </div>
                <div class="flex items-center gap-2">
                    <el-button @click="caliberVisible = true">口径说明</el-button>
                    <el-button
                        v-perms="['report/course/export']"
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
                    <el-select
                        v-model="query.communityId"
                        class="w-[170px]"
                        clearable
                        filterable
                        placeholder="全部社区"
                    >
                        <el-option
                            v-for="item in options.communities"
                            :key="item.id"
                            :label="item.name"
                            :value="item.id"
                        />
                    </el-select>
                </el-form-item>
                <el-form-item label="课程">
                    <el-select
                        v-model="query.courseId"
                        class="w-[220px]"
                        clearable
                        filterable
                        placeholder="全部课程"
                    >
                        <el-option
                            v-for="item in options.courses"
                            :key="item.id"
                            :label="item.name"
                            :value="item.id"
                        />
                    </el-select>
                </el-form-item>
                <el-form-item label="教练">
                    <el-select
                        v-model="query.instructorId"
                        class="w-[160px]"
                        clearable
                        filterable
                        placeholder="全部教练"
                    >
                        <el-option
                            v-for="item in options.instructors"
                            :key="item.id"
                            :label="item.name"
                            :value="item.id"
                        />
                    </el-select>
                </el-form-item>
                <el-form-item label="关键词">
                    <el-input
                        v-model="query.keyword"
                        class="w-[190px]"
                        placeholder="课程/社区/教练"
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
            <div class="mb-4 grid grid-cols-2 md:grid-cols-4 xl:grid-cols-5 gap-3">
                <div v-for="item in summaryItems" :key="item.label" class="rounded bg-page p-3">
                    <div class="text-xs text-tx-secondary">{{ item.label }}</div>
                    <div class="mt-1 text-lg font-medium">{{ item.value }}</div>
                </div>
            </div>
            <el-table size="large" table-layout="fixed" v-loading="pager.loading" :data="pager.lists">
                <el-table-column label="课程班级" min-width="190" show-overflow-tooltip>
                    <template #default="{ row }">{{ row.courseName || '-' }}</template>
                </el-table-column>
                <el-table-column label="社区" min-width="135" show-overflow-tooltip>
                    <template #default="{ row }">{{ row.communityName || '未绑定社区' }}</template>
                </el-table-column>
                <el-table-column label="教练" min-width="100" show-overflow-tooltip>
                    <template #default="{ row }">{{ row.instructorName || '-' }}</template>
                </el-table-column>
                <el-table-column label="订单数" prop="orderCount" width="86" align="center" />
                <el-table-column label="实收金额" min-width="112" align="right">
                    <template #default="{ row }">￥{{ money(row.paidAmount) }}</template>
                </el-table-column>
                <el-table-column label="购买/已消课时" width="128" align="center">
                    <template #default="{ row }">
                        {{ row.purchasedLessons }} / {{ row.consumedLessons }}
                    </template>
                </el-table-column>
                <el-table-column label="到场人次/场次" width="120" align="center">
                    <template #default="{ row }">
                        {{ row.attendedStudents }} / {{ row.attendedLessons }}
                    </template>
                </el-table-column>
                <el-table-column label="操作" width="90" fixed="right" align="center">
                    <template #default="{ row }">
                        <el-button type="primary" link @click="openDetail(row)">明细</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <div class="flex justify-end mt-4">
                <pagination v-model="pager" @change="getLists" />
            </div>
        </el-card>

        <el-dialog v-model="caliberVisible" title="课程报表口径说明" width="760px">
            <div class="whitespace-pre-wrap leading-7">{{ pager.extend.caliber || '暂无口径说明' }}</div>
            <template #footer>
                <el-button @click="caliberVisible = false">关闭</el-button>
            </template>
        </el-dialog>

        <el-dialog v-model="detailVisible" title="班级收益明细" width="720px">
            <template v-if="detailRow">
                <div class="mb-3 text-base font-medium">{{ detailRow.courseName }}</div>
                <el-descriptions :column="2" border size="small">
                    <el-descriptions-item label="社区">{{ detailRow.communityName || '未绑定社区' }}</el-descriptions-item>
                    <el-descriptions-item label="教练">{{ detailRow.instructorName || '-' }}</el-descriptions-item>
                    <el-descriptions-item label="订单数">{{ detailRow.orderCount }}</el-descriptions-item>
                    <el-descriptions-item label="实收金额">￥{{ money(detailRow.paidAmount) }}</el-descriptions-item>
                    <el-descriptions-item label="购买课时">{{ detailRow.purchasedLessons }}</el-descriptions-item>
                    <el-descriptions-item label="已消课时">{{ detailRow.consumedLessons }}</el-descriptions-item>
                    <el-descriptions-item label="到场人次">{{ detailRow.attendedStudents }}</el-descriptions-item>
                    <el-descriptions-item label="上课场次">{{ detailRow.attendedLessons }}</el-descriptions-item>
                </el-descriptions>
                <div class="mt-4 rounded bg-page p-3 text-sm leading-6">
                    <div>① 实收金额 = 区间内已支付、未删除、未退款订单的实付金额合计（按支付时间归期）。</div>
                    <div>② 已消课时 = 区间内核销流水行数；到场人次/场次取预约状态为「已上课」的记录。</div>
                    <div>③ 本报表只做真实营业额统计，不做任何二次分配计算；教练维度营业额见「教练管理 → 教练月营业额」。</div>
                </div>
            </template>
            <template #footer>
                <el-button @click="detailVisible = false">关闭</el-button>
            </template>
        </el-dialog>
    </div>
</template>

<script lang="ts" setup name="reportCourse">
import { exportCourseReport, getCourseReport } from '@/api/report'
import { usePaging } from '@/hooks/usePaging'
import feedback from '@/utils/feedback'
import { compactParams, money, numberText } from '../helpers'

const query = reactive({
    startDate: '',
    endDate: '',
    communityId: '' as number | string,
    courseId: '' as number | string,
    instructorId: '' as number | string,
    keyword: ''
})

const caliberVisible = ref(false)
const detailVisible = ref(false)
const detailRow = ref<any>(null)
const exporting = ref(false)

const { pager, getLists, resetPage, resetParams } = usePaging({
    fetchFun: (params: any) => getCourseReport(compactParams({ ...params, ...query })),
    params: query,
    size: 20
})

const options = computed<any>(() => pager.extend?.options || {})
const summary = computed<any>(() => pager.extend?.summary || {})

const summaryItems = computed(() => [
    { label: '课程数', value: numberText(summary.value.classCount) },
    { label: '订单数', value: numberText(summary.value.orderCount) },
    { label: '实收金额', value: `￥${money(summary.value.paidAmount)}` },
    { label: '已消课时', value: numberText(summary.value.consumedLessons) },
    { label: '到场人次', value: numberText(summary.value.attendedStudents) }
])

function openDetail(row: any) {
    detailRow.value = row
    detailVisible.value = true
}

async function handleExport() {
    exporting.value = true
    feedback.loading('正在导出课程报表...')
    try {
        await exportCourseReport(compactParams({ ...query }))
    } finally {
        feedback.closeLoading()
        exporting.value = false
    }
}

getLists()
</script>
