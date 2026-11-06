<template>
    <div class="settlement-page">
        <el-card class="!border-none" shadow="never">
            <div class="flex items-start justify-between gap-4">
                <div>
                    <div class="text-lg font-medium">收益明细（只读）</div>
                    <div class="text-tx-secondary text-sm mt-1 leading-6">
                        按课程班级汇总区间内的订单数、实收营业额与购买课时，供线下核算；只展示真实营业额，
                        不做任何二次分配计算。本页只读，不触发上报或划款。
                    </div>
                </div>
                <div class="flex shrink-0 items-center gap-2">
                    <el-tag v-if="configInfo" :type="configInfo.enabled ? 'success' : 'info'">
                        适配器{{ configInfo.enabled ? '已开启' : '关闭' }} · {{ configInfo.mode || '-' }}
                    </el-tag>
                    <el-button v-perms="['finance.settlement/config']" @click="openConfig">
                        适配器状态
                    </el-button>
                    <el-button
                        v-perms="['finance.settlement/export']"
                        type="primary"
                        :loading="exporting"
                        @click="handleExport"
                    >
                        导出 Excel
                    </el-button>
                </div>
            </div>

            <el-alert
                class="mt-4"
                type="info"
                :closable="false"
                title="只读页面：不提供「上报/划款」按钮。上报由受控接口按批次执行，仅超级管理员可触发。"
            />
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
                <el-form-item label="社区 ID">
                    <el-input
                        v-model="query.communityId"
                        class="w-[130px]"
                        placeholder="全部"
                        clearable
                        @keyup.enter="search"
                    />
                </el-form-item>
                <el-form-item label="班级 ID">
                    <el-input
                        v-model="query.courseClassId"
                        class="w-[130px]"
                        placeholder="全部"
                        clearable
                        @keyup.enter="search"
                    />
                </el-form-item>
                <el-form-item label="教练 ID">
                    <el-input
                        v-model="query.instructorId"
                        class="w-[130px]"
                        placeholder="全部"
                        clearable
                        @keyup.enter="search"
                    />
                </el-form-item>
                <el-form-item>
                    <el-button
                        v-perms="['finance.settlement/lists']"
                        type="primary"
                        @click="search"
                    >
                        查询
                    </el-button>
                    <el-button @click="resetQuery">重置</el-button>
                </el-form-item>
            </el-form>
        </el-card>

        <el-card class="!border-none mt-4" shadow="never">
            <div class="mb-4 flex flex-wrap items-center justify-between gap-3">
                <div class="flex items-center gap-2">
                    <span class="font-medium">汇总维度</span>
                    <el-radio-group
                        v-model="dimension"
                        size="small"
                        @change="loadSummary"
                    >
                        <el-radio-button label="course">课程</el-radio-button>
                        <el-radio-button label="community">社区</el-radio-button>
                    </el-radio-group>
                </div>
                <div class="text-tx-secondary text-sm">
                    期间：{{ pager.extend.periodStart || '-' }} ~
                    {{ pager.extend.periodEnd || '-' }}
                </div>
            </div>

            <div class="mb-4 grid grid-cols-2 gap-3 md:grid-cols-4">
                <div v-for="item in summaryItems" :key="item.label" class="rounded bg-page p-3">
                    <div class="text-xs text-tx-secondary">{{ item.label }}</div>
                    <div class="mt-1 text-lg font-medium">{{ item.value }}</div>
                </div>
            </div>

            <el-table
                v-loading="summaryLoading"
                size="large"
                :data="summaryData.items || []"
                max-height="300"
            >
                <el-table-column label="维度" width="90">
                    <template #default>
                        <el-tag size="small">{{ dimensionText(dimension) }}</el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="名称" prop="name" min-width="180" show-overflow-tooltip />
                <el-table-column label="明细项" prop="itemCount" width="86" align="center" />
                <el-table-column label="订单数" prop="orderCount" width="86" align="center" />
                <el-table-column label="实收营业额" min-width="120" align="right">
                    <template #default="{ row }">￥{{ money(row.paidOrderAmount) }}</template>
                </el-table-column>
            </el-table>
        </el-card>

        <el-card class="!border-none mt-4" shadow="never">
            <el-table
                size="large"
                table-layout="fixed"
                v-loading="pager.loading"
                :data="pager.lists"
            >
                <el-table-column label="课程班级" min-width="180" show-overflow-tooltip>
                    <template #default="{ row }">
                        {{ row.courseClassName || `班级#${row.courseClassId}` }}
                    </template>
                </el-table-column>
                <el-table-column label="社区" min-width="130" show-overflow-tooltip>
                    <template #default="{ row }">{{ row.communityName || '未绑定社区' }}</template>
                </el-table-column>
                <el-table-column label="教练" min-width="100" show-overflow-tooltip>
                    <template #default="{ row }">{{ row.instructorName || '-' }}</template>
                </el-table-column>
                <el-table-column label="订单数" prop="orderCount" width="86" align="center" />
                <el-table-column label="实收金额" min-width="110" align="right">
                    <template #default="{ row }">￥{{ money(row.paidOrderAmount) }}</template>
                </el-table-column>
                <el-table-column label="购买课时" prop="purchasedLessons" width="96" align="center" />
                <el-table-column label="操作" width="96" fixed="right" align="center">
                    <template #default="{ row }">
                        <el-button type="primary" link @click="openDetail(row)">
                            查看明细
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
            <div class="flex justify-end mt-4">
                <pagination v-model="pager" @change="getLists" />
            </div>
        </el-card>

        <el-dialog v-model="detailVisible" title="收益明细" width="720px">
            <template v-if="detailRow">
                <el-descriptions :column="2" border size="small">
                    <el-descriptions-item label="课程班级">
                        {{ detailRow.courseClassName || '-' }}
                    </el-descriptions-item>
                    <el-descriptions-item label="社区">
                        {{ detailRow.communityName || '未绑定社区' }}
                    </el-descriptions-item>
                    <el-descriptions-item label="教练">
                        {{ detailRow.instructorName || '-' }}
                    </el-descriptions-item>
                    <el-descriptions-item label="订单数">
                        {{ numberText(detailRow.orderCount) }}
                    </el-descriptions-item>
                    <el-descriptions-item label="购买课时">
                        {{ numberText(detailRow.purchasedLessons) }}
                    </el-descriptions-item>
                    <el-descriptions-item label="实收金额">
                        ￥{{ money(detailRow.paidOrderAmount) }}
                    </el-descriptions-item>
                </el-descriptions>
                <div class="mt-4 rounded bg-page p-3 text-sm leading-6">
                    实收金额 = 区间内已支付、未删除、未退款订单的实付金额合计（按支付时间归期）。
                    本页只展示真实营业额，不做二次分配计算。
                </div>
            </template>
            <template #footer>
                <el-button @click="detailVisible = false">关闭</el-button>
            </template>
        </el-dialog>

        <el-dialog v-model="configVisible" title="上报适配器状态" width="760px">
            <div v-loading="configLoading">
                <template v-if="configInfo">
                    <el-alert
                        v-if="configInfo.configIssue"
                        class="mb-4"
                        type="warning"
                        :closable="false"
                        :title="configInfo.configIssue"
                    />
                    <el-descriptions :column="2" border size="small">
                        <el-descriptions-item label="启用">
                            {{ configInfo.enabled ? '是' : '否' }}
                        </el-descriptions-item>
                        <el-descriptions-item label="模式">
                            {{ configInfo.mode || '-' }} / {{ configInfo.clientMode || '-' }}
                        </el-descriptions-item>
                        <el-descriptions-item label="Endpoint 已配置">
                            {{ configInfo.endpointConfigured ? '是' : '否' }}
                        </el-descriptions-item>
                        <el-descriptions-item label="Endpoint 主机">
                            {{ configInfo.endpointHost || '-' }}
                        </el-descriptions-item>
                        <el-descriptions-item label="AppId 已配置">
                            {{ configInfo.appIdConfigured ? '是' : '否' }}
                        </el-descriptions-item>
                        <el-descriptions-item label="Secret 已配置">
                            {{ configInfo.secretConfigured ? '是' : '否' }}
                        </el-descriptions-item>
                        <el-descriptions-item label="连接/读取超时">
                            {{ configInfo.connectTimeoutMs }} / {{ configInfo.readTimeoutMs }} ms
                        </el-descriptions-item>
                        <el-descriptions-item label="重试次数/退避">
                            {{ configInfo.maxAttempts }} / {{ configInfo.retryBackoffMs }} ms
                        </el-descriptions-item>
                        <el-descriptions-item label="时间戳容差">
                            {{ configInfo.timestampToleranceSeconds }} 秒
                        </el-descriptions-item>
                        <el-descriptions-item label="校验响应签名">
                            {{ configInfo.verifyResponseSignature ? '是' : '否' }}
                        </el-descriptions-item>
                    </el-descriptions>
                    <div class="mt-4">
                        <div class="mb-2 font-medium">最近一次上报结果</div>
                        <pre class="max-h-[220px] overflow-auto rounded bg-page p-3 text-xs">{{
                            prettyJson(configInfo.lastPush)
                        }}</pre>
                    </div>
                </template>
                <div v-else class="py-6 text-center text-tx-secondary">暂无适配器状态</div>
            </div>
            <template #footer>
                <el-button @click="configVisible = false">关闭</el-button>
            </template>
        </el-dialog>
    </div>
</template>

<script lang="ts" setup name="financeSettlement">
import {
    exportSettlement,
    getSettlementConfig,
    getSettlementLists,
    getSettlementSummary
} from '@/api/settlement'
import { usePaging } from '@/hooks/usePaging'
import { openExportDownload } from '@/utils/exportDownload'
import feedback from '@/utils/feedback'
import { compactParams, money, numberText } from '@/views/report/helpers'

const query = reactive({
    startDate: '',
    endDate: '',
    communityId: '',
    courseClassId: '',
    instructorId: ''
})

const { pager, getLists, resetPage, resetParams } = usePaging({
    fetchFun: (params: any) => getSettlementLists(compactParams({ ...params, ...query })),
    params: query,
    size: 20
})

const dimension = ref('course')
const summaryLoading = ref(false)
const summaryData = ref<any>({ items: [], totals: {} })
const exporting = ref(false)
const detailVisible = ref(false)
const detailRow = ref<any>(null)
const configVisible = ref(false)
const configLoading = ref(false)
const configInfo = ref<any>(null)

const summaryItems = computed(() => {
    const totals = summaryData.value?.totals || {}
    return [
        { label: '明细项', value: numberText(totals.itemCount) },
        { label: '订单数', value: numberText(totals.orderCount) },
        { label: '总营业额', value: `￥${money(totals.paidOrderAmount)}` },
        { label: '购买课时', value: numberText(totals.purchasedLessons) }
    ]
})

function dimensionText(value: string) {
    return { course: '课程', community: '社区' }[value] || value
}

function prettyJson(value: any) {
    if (value === null || value === undefined) return '暂无'
    try {
        return JSON.stringify(value, null, 2)
    } catch (error) {
        return String(value)
    }
}

async function loadSummary() {
    summaryLoading.value = true
    try {
        summaryData.value =
            (await getSettlementSummary(
                compactParams({ ...query, dimension: dimension.value })
            )) || { items: [], totals: {} }
    } catch (error) {
        summaryData.value = { items: [], totals: {} }
    } finally {
        summaryLoading.value = false
    }
}

function search() {
    resetPage()
    loadSummary()
}

function resetQuery() {
    resetParams()
    loadSummary()
}

function openDetail(row: any) {
    detailRow.value = row
    detailVisible.value = true
}

async function loadConfig() {
    configLoading.value = true
    try {
        configInfo.value = await getSettlementConfig()
    } catch (error) {
        configInfo.value = null
    } finally {
        configLoading.value = false
    }
}

function openConfig() {
    configVisible.value = true
    if (!configInfo.value) loadConfig()
}

async function handleExport() {
    exporting.value = true
    feedback.loading('正在导出收益明细...')
    try {
        const result: any = await exportSettlement(compactParams({ ...query }))
        // #214：该导出接口返回 code=1 + {file, ticket, expiresIn}，显式打开票据下载地址。
        openExportDownload(result)
    } finally {
        feedback.closeLoading()
        exporting.value = false
    }
}

getLists()
loadSummary()
loadConfig()
</script>
