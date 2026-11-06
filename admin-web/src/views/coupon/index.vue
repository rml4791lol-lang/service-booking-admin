<template>
    <div class="coupon-page">
        <el-card class="!border-none" shadow="never">
            <div class="flex items-center justify-between">
                <div>
                    <div class="text-lg font-medium">券与券模板</div>
                    <div class="text-tx-secondary text-sm mt-1">
                        老抵扣券体系已下线，发券请到「券商品管理」。本页仅保留历史券与模板的只读查询。
                    </div>
                </div>
            </div>
        </el-card>

        <el-tabs v-model="activeTab" class="mt-4">
            <el-tab-pane label="券列表" name="coupon">
                <el-card class="!border-none" shadow="never">
                    <el-form class="mb-[-16px]" :inline="true" :model="couponQuery">
                        <el-form-item label="状态">
                            <el-select class="w-[150px]" v-model="couponQuery.status" clearable placeholder="全部">
                                <el-option label="未使用" :value="0" />
                                <el-option label="已使用" :value="1" />
                                <el-option label="已过期" :value="2" />
                            </el-select>
                        </el-form-item>
                        <el-form-item label="学员">
                            <el-select
                                class="w-[220px]"
                                v-model="couponQuery.userId"
                                clearable
                                filterable
                                remote
                                :remote-method="searchFilterStudents"
                                :loading="filterStudentLoading"
                                placeholder="昵称 / 手机号"
                            >
                                <el-option
                                    v-for="item in filterStudentOptions"
                                    :key="item.id"
                                    :label="studentLabel(item)"
                                    :value="item.id"
                                />
                            </el-select>
                        </el-form-item>
                        <el-form-item label="手机号">
                            <el-input
                                class="w-[180px]"
                                v-model="couponQuery.mobile"
                                clearable
                                placeholder="输入手机号"
                                @keyup.enter="resetCouponPage"
                            />
                        </el-form-item>
                        <el-form-item label="券名称">
                            <el-input
                                class="w-[180px]"
                                v-model="couponQuery.name"
                                clearable
                                placeholder="输入券名称"
                                @keyup.enter="resetCouponPage"
                            />
                        </el-form-item>
                        <el-form-item>
                            <el-button type="primary" @click="resetCouponPage">查询</el-button>
                            <el-button @click="resetCouponParams">重置</el-button>
                        </el-form-item>
                        <el-form-item label="券流水导出">
                            <el-date-picker
                                v-model="exportDateRange"
                                class="!w-[240px]"
                                type="daterange"
                                value-format="YYYY-MM-DD"
                                :clearable="false"
                                start-placeholder="导出开始日期"
                                end-placeholder="导出结束日期"
                            />
                            <el-button
                                class="ml-2"
                                v-perms="['report/coupon/export']"
                                type="primary"
                                plain
                                :loading="exporting"
                                @click="exportCouponExcel"
                            >
                                导出券流水报表
                            </el-button>
                        </el-form-item>
                    </el-form>
                </el-card>

                <el-card class="!border-none mt-4" shadow="never">
                    <div class="mb-3 text-sm text-gray-400">共 {{ couponPager.count }} 张券</div>
                    <el-table size="large" v-loading="couponPager.loading" :data="couponPager.lists">
                        <el-table-column label="ID" prop="id" width="60" />
                        <el-table-column label="券编号" prop="sn" min-width="170" show-overflow-tooltip />
                        <el-table-column label="券名称" prop="name" min-width="130" show-overflow-tooltip />
                        <el-table-column label="面额" width="90">
                            <template #default="{ row }">{{ moneyText(row.amount) }}</template>
                        </el-table-column>
                        <el-table-column label="门槛" width="105">
                            <template #default="{ row }">
                                <template v-if="minAmountValue(row.minAmount) > 0">
                                    <div>满¥{{ thresholdAmountText(row.minAmount) }}</div>
                                    <div class="text-xs text-gray-400">可用</div>
                                </template>
                                <span v-else class="text-gray-400">无门槛</span>
                            </template>
                        </el-table-column>
                        <el-table-column label="学员" min-width="165">
                            <template #default="{ row }">
                                <div class="font-medium">{{ row.nickname || row.realName || '-' }}</div>
                                <div class="text-xs text-gray-400">
                                    {{ row.mobile || '-' }}
                                    <span v-if="row.userId">· #{{ row.userId }}</span>
                                </div>
                            </template>
                        </el-table-column>
                        <el-table-column label="状态" width="90">
                            <template #default="{ row }">
                                <el-tag :type="couponStatusType(row.status)" size="small">
                                    {{ row.statusDesc || couponStatusLabel(row.status) }}
                                </el-tag>
                            </template>
                        </el-table-column>
                        <el-table-column label="有效期" min-width="185">
                            <template #default="{ row }">
                                <div>{{ row.startTime || '-' }}</div>
                                <div class="text-xs text-gray-400">至 {{ row.expireTime || '-' }}</div>
                            </template>
                        </el-table-column>
                        <el-table-column label="操作" width="100" fixed="right">
                            <template #default="{ row }">
                                <el-button
                                    v-perms="['coupon/detail']"
                                    type="primary"
                                    link
                                    @click="openCouponDetail(row)"
                                >
                                    详情
                                </el-button>
                            </template>
                        </el-table-column>
                    </el-table>
                    <div class="flex justify-end mt-4">
                        <pagination v-model="couponPager" @change="getCouponLists" />
                    </div>
                </el-card>
            </el-tab-pane>

            <el-tab-pane label="券模板" name="template">
                <el-card class="!border-none" shadow="never">
                    <el-form class="mb-[-16px]" :inline="true" :model="templateQuery">
                        <el-form-item label="模板名称">
                            <el-input
                                class="w-[220px]"
                                v-model="templateQuery.name"
                                clearable
                                placeholder="输入模板名称"
                                @keyup.enter="resetTemplatePage"
                            />
                        </el-form-item>
                        <el-form-item label="状态">
                            <el-select class="w-[150px]" v-model="templateQuery.status" clearable placeholder="全部">
                                <el-option label="启用" :value="0" />
                                <el-option label="停用" :value="1" />
                            </el-select>
                        </el-form-item>
                        <el-form-item>
                            <el-button type="primary" @click="resetTemplatePage">查询</el-button>
                            <el-button @click="resetTemplateParams">重置</el-button>
                        </el-form-item>
                    </el-form>
                </el-card>

                <el-card class="!border-none mt-4" shadow="never">
                    <div class="mb-3 text-sm text-gray-400">共 {{ templatePager.count }} 个模板</div>
                    <el-table size="large" v-loading="templatePager.loading" :data="templatePager.lists">
                        <el-table-column label="ID" prop="id" width="60" />
                        <el-table-column label="模板名称" prop="name" min-width="170" show-overflow-tooltip />
                        <el-table-column label="面额" width="90">
                            <template #default="{ row }">{{ moneyText(row.amount) }}</template>
                        </el-table-column>
                        <el-table-column label="门槛" width="105">
                            <template #default="{ row }">
                                <template v-if="minAmountValue(row.minAmount) > 0">
                                    <div>满¥{{ thresholdAmountText(row.minAmount) }}</div>
                                    <div class="text-xs text-gray-400">可用</div>
                                </template>
                                <span v-else class="text-gray-400">无门槛</span>
                            </template>
                        </el-table-column>
                        <el-table-column label="有效期" min-width="210" show-overflow-tooltip>
                            <template #default="{ row }">{{ row.validDesc || '-' }}</template>
                        </el-table-column>
                        <el-table-column label="状态" width="90">
                            <template #default="{ row }">
                                <el-tag :type="row.status === 0 ? 'success' : 'info'" size="small">
                                    {{ row.status === 0 ? '启用' : '停用' }}
                                </el-tag>
                            </template>
                        </el-table-column>
                        <el-table-column label="备注" prop="remark" min-width="100" show-overflow-tooltip>
                            <template #default="{ row }">{{ row.remark || '-' }}</template>
                        </el-table-column>
                        <el-table-column label="创建时间" width="175" show-overflow-tooltip>
                            <template #default="{ row }">{{ shortTime(row.createTime) }}</template>
                        </el-table-column>
                    </el-table>
                    <div class="flex justify-end mt-4">
                        <pagination v-model="templatePager" @change="getTemplateLists" />
                    </div>
                </el-card>
            </el-tab-pane>
        </el-tabs>

        <!-- 券详情 -->
        <el-dialog v-model="detailDialogVisible" title="券详情" width="720px">
            <el-descriptions :column="2" border v-loading="detailLoading">
                <el-descriptions-item label="券编号" :span="2">{{ couponDetail.sn || '-' }}</el-descriptions-item>
                <el-descriptions-item label="券名称">{{ couponDetail.name || '-' }}</el-descriptions-item>
                <el-descriptions-item label="状态">
                    <el-tag :type="couponStatusType(couponDetail.status)" size="small">
                        {{ couponDetail.statusDesc || couponStatusLabel(couponDetail.status) }}
                    </el-tag>
                </el-descriptions-item>
                <el-descriptions-item label="学员">
                    {{ couponDetail.nickname || couponDetail.realName || '-' }}
                    <span class="text-gray-400">（#{{ couponDetail.userId || 0 }}）</span>
                </el-descriptions-item>
                <el-descriptions-item label="手机号">{{ couponDetail.mobile || '-' }}</el-descriptions-item>
                <el-descriptions-item label="面额">{{ moneyText(couponDetail.amount) }}</el-descriptions-item>
                <el-descriptions-item label="使用门槛">{{ thresholdText(couponDetail.minAmount) }}</el-descriptions-item>
                <el-descriptions-item label="来源模板">
                    {{ couponDetail.templateId ? `#${couponDetail.templateId}` : '手工创建' }}
                </el-descriptions-item>
                <el-descriptions-item label="发券管理员">#{{ couponDetail.adminId || 0 }}</el-descriptions-item>
                <el-descriptions-item label="生效时间">{{ formatSeconds(couponDetail.startTime) }}</el-descriptions-item>
                <el-descriptions-item label="过期时间">{{ formatSeconds(couponDetail.expireTime) }}</el-descriptions-item>
                <el-descriptions-item label="使用订单">
                    {{ couponDetail.usedOrderId ? `#${couponDetail.usedOrderId}` : '-' }}
                </el-descriptions-item>
                <el-descriptions-item label="使用时间">{{ couponDetail.usedTime || '-' }}</el-descriptions-item>
                <el-descriptions-item label="创建时间">{{ couponDetail.createTime || '-' }}</el-descriptions-item>
                <el-descriptions-item label="备注" :span="2">{{ couponDetail.remark || '-' }}</el-descriptions-item>
            </el-descriptions>
            <template #footer>
                <el-button @click="detailDialogVisible = false">关闭</el-button>
            </template>
        </el-dialog>
    </div>
</template>

<script lang="ts" setup name="coupon">
import { ref } from 'vue'
import {
    getCouponDetail,
    getCouponList,
    getCouponStudents,
    getCouponTemplateList
} from '@/api/coupon'
import { exportCouponReport } from '@/api/report'
import { compactParams, currentMonthRange } from '@/views/report/helpers'
import { usePaging } from '@/hooks/usePaging'
import feedback from '@/utils/feedback'

const activeTab = ref('coupon')

// ---------- 券列表 ----------
const couponQuery = reactive<any>({
    status: '',
    userId: '',
    mobile: '',
    name: ''
})

const {
    pager: couponPager,
    getLists: getCouponLists,
    resetPage: resetCouponPage,
    resetParams: resetCouponParams
} = usePaging({
    fetchFun: getCouponList,
    params: couponQuery
})

// ---------- 导出券流水报表（#105：旧报表菜单退场，导出搬到业务页）----------
// 复用旧报表导出接口（/report/coupon/export），导的是券流水 la_coupon_flow，与旧报表一致。
// 本页「状态」是券实例状态，报表「流水类型」是发放/核销扣减/补发/作废/退回；
// 可映射的只有前两态，映射关系写在下面常量里，未列出的状态不传类型（= 全部类型）：
//   未使用(0) → 发放(1)：券只被发放过、还没核销；
//   已使用(1) → 核销扣减(2)：用掉的券必然有一条核销扣减流水；
//   已过期(2) → 不传：过期不是流水事件，报表里没有对应类型。
const COUPON_STATUS_FLOW_TYPE: Record<number, number> = {
    0: 1,
    1: 2
}

const exporting = ref(false)
const exportDateRange = ref<string[]>(currentMonthRange())

async function exportCouponExcel() {
    // 「已过期」不是券流水事件：报表没有对应 flowType，静默丢弃会让导出结果与列表筛选不符。
    if (couponQuery.status !== '' && couponQuery.status !== null && couponQuery.status !== undefined) {
        const status = Number(couponQuery.status)
        if (!COUPON_STATUS_FLOW_TYPE[status]) {
            feedback.msgWarning(
                `「${couponStatusLabel(status)}」不是券流水事件，无法按该状态导出流水；请清除状态或改用「未使用/已使用」`
            )
            return
        }
    }
    exporting.value = true
    feedback.loading('正在导出券流水报表...')
    try {
        const status = Number(couponQuery.status)
        await exportCouponReport(
            compactParams({
                startDate: exportDateRange.value?.[0] || '',
                endDate: exportDateRange.value?.[1] || '',
                keyword: couponQuery.name,
                // 业务页的学员/手机号筛选同步透传，避免导出时静默丢筛选
                userId: couponQuery.userId,
                mobile: couponQuery.mobile,
                flowType:
                    couponQuery.status === '' ? '' : COUPON_STATUS_FLOW_TYPE[status]
            })
        )
    } finally {
        feedback.closeLoading()
        exporting.value = false
    }
}

const filterStudentOptions = ref<any[]>([])
const filterStudentLoading = ref(false)

async function searchFilterStudents(keyword = '') {
    filterStudentLoading.value = true
    try {
        filterStudentOptions.value = (await getCouponStudents({ keyword })) || []
    } catch {
        // 请求层已提示
    } finally {
        filterStudentLoading.value = false
    }
}

// ---------- 券模板 ----------
const templateQuery = reactive<any>({
    name: '',
    status: ''
})

const {
    pager: templatePager,
    getLists: getTemplateLists,
    resetPage: resetTemplatePage,
    resetParams: resetTemplateParams
} = usePaging({
    fetchFun: getCouponTemplateList,
    params: templateQuery
})

// ---------- 券详情 ----------
const detailDialogVisible = ref(false)
const detailLoading = ref(false)
const couponDetail = ref<any>({})

async function openCouponDetail(row: any) {
    detailDialogVisible.value = true
    detailLoading.value = true
    couponDetail.value = row
    try {
        couponDetail.value = (await getCouponDetail({ id: row.id })) || row
    } catch {
        // 请求层已提示，保留列表行数据
    } finally {
        detailLoading.value = false
    }
}

// ---------- 展示工具 ----------
function moneyText(value: any) {
    if (value === null || value === undefined || value === '') return '-'
    return `¥${Number(value).toFixed(2)}`
}

function minAmountValue(value: any) {
    return Number(value || 0)
}

function thresholdAmountText(value: any) {
    const fixed = minAmountValue(value).toFixed(2)
    return fixed.endsWith('.00') ? fixed.slice(0, -3) : fixed
}

function thresholdText(value: any) {
    const amount = minAmountValue(value)
    return amount > 0 ? `满¥${thresholdAmountText(value)}可用` : '无门槛'
}

function shortTime(value: any) {
    if (!value) return '-'
    return String(value).slice(0, 16)
}

function couponStatusLabel(status: any) {
    if (Number(status) === 0) return '未使用'
    if (Number(status) === 1) return '已使用'
    if (Number(status) === 2) return '已过期'
    return '未知'
}

function couponStatusType(status: any) {
    if (Number(status) === 0) return 'success'
    if (Number(status) === 1) return 'info'
    if (Number(status) === 2) return 'danger'
    return 'warning'
}

function studentLabel(item: any) {
    const name = item.nickname || item.realName || item.account || `#${item.id}`
    return `${name}（${item.mobile || '无手机号'}）`
}

function formatSeconds(seconds: any) {
    if (!seconds) return '-'
    const date = new Date(Number(seconds) * 1000)
    if (Number.isNaN(date.getTime())) return '-'
    const pad = (value: number) => String(value).padStart(2, '0')
    return `${date.getFullYear()}-${pad(date.getMonth() + 1)}-${pad(date.getDate())} ${pad(
        date.getHours()
    )}:${pad(date.getMinutes())}:${pad(date.getSeconds())}`
}

searchFilterStudents('')
getCouponLists().catch(() => {})
getTemplateLists().catch(() => {})

// keep-alive 首次挂载也会触发 onActivated；跳过首次，避免同 URL 请求被取消
let pageActivated = false
onActivated(() => {
    if (!pageActivated) {
        pageActivated = true
        return
    }
    getCouponLists().catch(() => {})
    getTemplateLists().catch(() => {})
})
</script>
