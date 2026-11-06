<template>
    <div class="checkin-page">
        <el-card class="!border-none" shadow="never">
            <div class="flex items-center justify-between">
                <div>
                    <div class="text-lg font-medium">待核销</div>
                    <div class="text-tx-secondary text-sm mt-1">
                        按社区/课程/日期查看场次核销进度；教练漏操作时可人工补核销，原因必填并留审计。
                        补核销按教练核销同一口径逐人扣 1 次可用券，无券学员仍记考勤并提示。
                    </div>
                </div>
                <div class="flex flex-wrap items-center justify-end gap-2">
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
                        v-perms="['report/checkin/export']"
                        type="primary"
                        plain
                        :loading="exporting"
                        @click="exportCheckinExcel"
                    >
                        导出核销报表
                    </el-button>
                    <el-button
                        v-perms="['course_checkin/audit']"
                        type="primary"
                        plain
                        @click="openAuditDialog"
                    >
                        补核销审计
                    </el-button>
                </div>
            </div>
        </el-card>

        <el-card class="!border-none mt-4" shadow="never">
            <el-form class="mb-[-16px]" :inline="true" :model="query">
                <el-form-item label="社区">
                    <el-select
                        v-model="query.communityId"
                        class="w-[180px]"
                        clearable
                        filterable
                        placeholder="全部社区"
                        @change="onCommunityChange"
                    >
                        <el-option
                            v-for="item in communityOptions"
                            :key="item.id"
                            :label="item.name"
                            :value="item.id"
                        />
                    </el-select>
                </el-form-item>
                <el-form-item label="课程">
                    <el-select
                        v-model="query.courseId"
                        class="w-[230px]"
                        clearable
                        filterable
                        placeholder="全部课程"
                    >
                        <el-option
                            v-for="item in courseOptionsForSelect"
                            :key="item.id"
                            :label="item.name"
                            :value="item.id"
                        />
                    </el-select>
                </el-form-item>
                <el-form-item label="日期">
                    <el-date-picker
                        v-model="query.date"
                        class="w-[160px]"
                        type="date"
                        value-format="YYYY-MM-DD"
                        placeholder="全部日期"
                        clearable
                    />
                </el-form-item>
                <el-form-item label="状态">
                    <el-select v-model="query.status" class="w-[140px]">
                        <el-option label="全部" value="" />
                        <el-option
                            v-for="item in STATUS_OPTIONS"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value"
                        />
                    </el-select>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="resetPage">查询</el-button>
                    <el-button @click="resetParams">重置</el-button>
                </el-form-item>
            </el-form>
        </el-card>

        <el-card class="!border-none mt-4" shadow="never">
            <el-table
                size="large"
                table-layout="fixed"
                v-loading="pager.loading"
                :data="pager.lists"
                row-key="sessionId"
            >
                <el-table-column label="场次" min-width="175">
                    <template #default="{ row }">
                        <div class="font-medium">{{ row.date }} {{ row.weekday }}</div>
                        <div class="text-tx-secondary text-xs">
                            {{ row.time }} · 第{{ row.lessonNo }}节
                        </div>
                    </template>
                </el-table-column>
                <el-table-column
                    label="课程"
                    prop="courseTitle"
                    min-width="160"
                    show-overflow-tooltip
                />
                <el-table-column label="社区 / 小区" min-width="145" show-overflow-tooltip>
                    <template #default="{ row }">{{ regionText(row) }}</template>
                </el-table-column>
                <el-table-column
                    label="教练"
                    prop="coachName"
                    min-width="90"
                    show-overflow-tooltip
                />
                <el-table-column label="预约人数" width="92" align="center">
                    <template #default="{ row }">{{ row.bookedCount }}</template>
                </el-table-column>
                <el-table-column label="已核销 / 待核销" width="130" align="center">
                    <template #default="{ row }">
                        <span :class="row.pendingCount > 0 ? 'text-orange-500' : 'text-green-600'">
                            {{ row.attendedCount }} / {{ row.pendingCount }}
                        </span>
                    </template>
                </el-table-column>
                <el-table-column label="照片" width="76" align="center">
                    <template #default="{ row }">
                        <el-tag v-if="row.photoCount > 0" size="small" type="success" effect="plain">
                            {{ row.photoCount }} 张
                        </el-tag>
                        <el-tag v-else size="small" type="info" effect="plain">未上传</el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="状态" width="100" align="center">
                    <template #default="{ row }">
                        <el-tag :type="statusTagType(row.statusCode)">
                            {{ row.statusText }}
                        </el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="操作" width="150" fixed="right">
                    <template #default="{ row }">
                        <el-button
                            v-perms="['course_checkin/detail']"
                            type="primary"
                            link
                            @click="openDetail(row)"
                        >
                            详情
                        </el-button>
                        <el-button
                            v-if="row.canManual"
                            v-perms="['course_checkin/manual']"
                            type="warning"
                            link
                            @click="openManual(row)"
                        >
                            补核销
                        </el-button>
                        <span v-else class="text-tx-secondary text-xs">-</span>
                    </template>
                </el-table-column>
            </el-table>
            <div class="flex justify-end mt-4">
                <pagination v-model="pager" @change="getLists" />
            </div>
        </el-card>

        <!-- 场次详情 -->
        <el-dialog
            v-model="detailVisible"
            title="场次详情"
            width="980px"
            top="5vh"
            :close-on-click-modal="false"
        >
            <template v-if="detail">
                <el-descriptions :column="2" border>
                    <el-descriptions-item label="课程">
                        {{ detail.courseTitle || '-' }}
                    </el-descriptions-item>
                    <el-descriptions-item label="场次">
                        {{ detail.date }} {{ detail.weekday }} {{ detail.time }} · 第{{ detail.lessonNo }}节
                    </el-descriptions-item>
                    <el-descriptions-item label="社区 / 小区">
                        {{ regionText(detail) }}
                    </el-descriptions-item>
                    <el-descriptions-item label="教练">
                        {{ detail.coachName || '-' }}
                    </el-descriptions-item>
                    <el-descriptions-item label="预约 / 已核销 / 待核销">
                        {{ detail.bookedCount }} / {{ detail.attendedCount }} / {{ detail.pendingCount }}
                    </el-descriptions-item>
                    <el-descriptions-item label="状态">
                        <el-tag :type="statusTagType(detail.statusCode)">
                            {{ detail.statusText }}
                        </el-tag>
                    </el-descriptions-item>
                    <el-descriptions-item label="照片" :span="2">
                        <template v-if="detail.photos && detail.photos.length">
                            <el-image
                                v-for="(photo, index) in detail.photos"
                                :key="index"
                                class="w-[64px] h-[64px] mr-2 rounded"
                                :src="photo"
                                :preview-src-list="detail.photos"
                                fit="cover"
                            />
                        </template>
                        <span v-else class="text-tx-secondary">未上传</span>
                    </el-descriptions-item>
                </el-descriptions>

                <el-tabs v-model="detailTab" class="mt-4">
                    <el-tab-pane label="学员名单" name="roster">
                        <el-table size="large" :data="detail.roster || []" max-height="360">
                            <el-table-column label="学员" min-width="110">
                                <template #default="{ row }">
                                    <div>{{ row.studentName }}</div>
                                    <div class="text-tx-secondary text-xs">
                                        {{ row.gender }}{{ row.age ? ' · ' + row.age + '岁' : '' }}
                                    </div>
                                </template>
                            </el-table-column>
                            <el-table-column label="家长" min-width="130" show-overflow-tooltip>
                                <template #default="{ row }">
                                    <div>{{ row.userName || '-' }}</div>
                                    <div class="text-tx-secondary text-xs">{{ row.userMobile || '-' }}</div>
                                </template>
                            </el-table-column>
                            <el-table-column label="预约状态" width="90" align="center">
                                <template #default="{ row }">
                                    <el-tag :type="row.attended ? 'success' : 'info'" effect="plain">
                                        {{ row.statusText }}
                                    </el-tag>
                                </template>
                            </el-table-column>
                            <el-table-column label="可用次数券" min-width="200">
                                <template #default="{ row }">
                                    <template v-if="row.attended">
                                        <span class="text-tx-secondary">已核销</span>
                                    </template>
                                    <template v-else-if="row.coupon && row.coupon.available">
                                        {{ row.coupon.title }}（余 {{ row.coupon.remaining }} 次）
                                    </template>
                                    <template v-else>
                                        <span class="text-orange-500">
                                            无可用券{{ row.coupon && row.coupon.reason ? '：' + row.coupon.reason : '' }}
                                        </span>
                                    </template>
                                </template>
                            </el-table-column>
                        </el-table>
                    </el-tab-pane>
                    <el-tab-pane v-if="detail.canViewAudit !== false" label="补核销记录" name="audit">
                        <el-table size="large" :data="detail.audits || []" max-height="360">
                            <el-table-column label="时间" prop="createTime" width="160" />
                            <el-table-column label="操作人" prop="operatorName" width="100" />
                            <el-table-column label="学员" prop="studentName" width="100" />
                            <el-table-column label="原因" prop="reason" min-width="180" show-overflow-tooltip />
                            <el-table-column label="扣券结果" min-width="190" show-overflow-tooltip>
                                <template #default="{ row }">{{ row.detail || '-' }}</template>
                            </el-table-column>
                        </el-table>
                    </el-tab-pane>
                </el-tabs>
            </template>
            <template #footer>
                <el-button @click="detailVisible = false">关闭</el-button>
            </template>
        </el-dialog>

        <!-- 人工补核销 -->
        <el-dialog
            v-model="manualVisible"
            title="人工补核销"
            width="820px"
            top="6vh"
            :close-on-click-modal="false"
            @closed="resetManual"
        >
            <el-descriptions v-if="manualSession" :column="2" border>
                <el-descriptions-item label="课程">
                    {{ manualSession.courseTitle }}
                </el-descriptions-item>
                <el-descriptions-item label="场次">
                    {{ manualSession.date }} {{ manualSession.time }} · 第{{ manualSession.lessonNo }}节
                </el-descriptions-item>
                <el-descriptions-item label="教练">
                    {{ manualSession.coachName || '-' }}
                </el-descriptions-item>
                <el-descriptions-item label="已核销 / 待核销">
                    {{ manualSession.attendedCount }} / {{ manualSession.pendingCount }}
                </el-descriptions-item>
            </el-descriptions>

            <el-alert
                class="mt-4"
                type="warning"
                :closable="false"
                title="补核销默认不强制照片。提交后按教练核销同一规则逐人扣 1 次可用券；无券学员仍记考勤并在结果中提示。"
            />

            <el-table
                ref="manualTableRef"
                class="mt-4"
                size="large"
                :data="manualRows"
                row-key="studentId"
                max-height="300"
                @selection-change="onManualSelectionChange"
            >
                <el-table-column type="selection" width="48" :selectable="isManualSelectable" />
                <el-table-column label="学员" min-width="120">
                    <template #default="{ row }">
                        <div>{{ row.studentName }}</div>
                        <div class="text-tx-secondary text-xs">{{ row.userName || '-' }}</div>
                    </template>
                </el-table-column>
                <el-table-column label="当前状态" width="100" align="center">
                    <template #default="{ row }">
                        <el-tag :type="row.attended ? 'success' : 'info'" effect="plain">
                            {{ row.statusText }}
                        </el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="本次可用次数券" min-width="220">
                    <template #default="{ row }">
                        <template v-if="row.attended">
                            <span class="text-tx-secondary">已核销，幂等跳过</span>
                        </template>
                        <template v-else-if="row.coupon && row.coupon.available">
                            <span class="text-green-600">
                                {{ row.coupon.title }}（余 {{ row.coupon.remaining }} 次）
                            </span>
                        </template>
                        <template v-else>
                            <span class="text-orange-500">
                                无可用券{{ row.coupon && row.coupon.reason ? '：' + row.coupon.reason : '' }}
                            </span>
                        </template>
                    </template>
                </el-table-column>
            </el-table>

            <el-form
                ref="manualFormRef"
                class="mt-4"
                :model="manualForm"
                :rules="manualRules"
                label-width="90px"
            >
                <el-form-item label="补核销原因" prop="reason">
                    <el-input
                        v-model="manualForm.reason"
                        type="textarea"
                        :rows="3"
                        maxlength="500"
                        show-word-limit
                        placeholder="请填写补核销原因（必填）"
                        @input="onReasonInput"
                    />
                </el-form-item>
            </el-form>

            <template #footer>
                <el-button @click="manualVisible = false">取消</el-button>
                <el-button type="primary" :loading="manualSubmitting" @click="submitManual">
                    提交补核销
                </el-button>
            </template>
        </el-dialog>

        <!-- 补核销审计 -->
        <el-dialog
            v-model="auditVisible"
            title="补核销审计"
            width="1050px"
            top="5vh"
            :close-on-click-modal="false"
        >
            <el-form class="mb-[-8px]" :inline="true" :model="auditQuery">
                <el-form-item label="场次ID">
                    <el-input
                        v-model="auditQuery.sessionId"
                        class="w-[120px]"
                        clearable
                        placeholder="全部"
                    />
                </el-form-item>
                <el-form-item label="学员ID">
                    <el-input
                        v-model="auditQuery.studentId"
                        class="w-[120px]"
                        clearable
                        placeholder="全部"
                    />
                </el-form-item>
                <el-form-item label="操作日期">
                    <el-date-picker
                        v-model="auditQuery.date"
                        class="w-[160px]"
                        type="date"
                        value-format="YYYY-MM-DD"
                        placeholder="全部日期"
                        clearable
                    />
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="searchAudits">查询</el-button>
                    <el-button @click="resetAudits">重置</el-button>
                </el-form-item>
            </el-form>
            <el-table
                class="mt-3"
                size="large"
                v-loading="auditPager.loading"
                :data="auditPager.lists"
                max-height="440"
            >
                <el-table-column label="时间" prop="createTime" width="160" />
                <el-table-column label="操作人" prop="operatorName" width="95" />
                <el-table-column label="场次" min-width="150">
                    <template #default="{ row }">
                        <div>{{ row.courseName }}</div>
                        <div class="text-tx-secondary text-xs">
                            {{ row.sessionDate }} {{ row.timeSlot }} · 第{{ row.lessonNo }}节
                        </div>
                    </template>
                </el-table-column>
                <el-table-column label="学员" prop="studentName" width="95" />
                <el-table-column label="原因" prop="reason" min-width="160" show-overflow-tooltip />
                <el-table-column label="扣券" min-width="180" show-overflow-tooltip>
                    <template #default="{ row }">{{ row.detail || '-' }}</template>
                </el-table-column>
            </el-table>
            <div class="flex justify-end mt-4">
                <el-pagination
                    v-model:current-page="auditPager.page"
                    v-model:page-size="auditPager.size"
                    :total="auditPager.count"
                    :page-sizes="[20, 50]"
                    layout="total, prev, pager, next"
                    @current-change="getAudits"
                />
            </div>
            <template #footer>
                <el-button @click="auditVisible = false">关闭</el-button>
            </template>
        </el-dialog>
    </div>
</template>

<script lang="ts" setup name="courseCheckin">
import { ref } from 'vue'
import {
    getCheckinAudit,
    getCheckinDetail,
    getCheckinList,
    getCheckinOptions,
    manualCheckin
} from '@/api/courseCheckin'
import { exportCheckinReport } from '@/api/report'
import { compactParams, currentMonthRange } from '@/views/report/helpers'
import { usePaging } from '@/hooks/usePaging'
import feedback from '@/utils/feedback'

const STATUS_OPTIONS = [
    { value: 0, label: '待核销' },
    { value: 3, label: '部分核销' },
    { value: 1, label: '已核销' },
    { value: 2, label: '已取消' }
]

const query = reactive({
    communityId: '' as number | string,
    courseId: '' as number | string,
    date: '',
    status: 0 as number | string
})

const courseOptions = ref<any[]>([])
const communityOptions = ref<any[]>([])

const filteredCourseOptions = computed(() => {
    if (!query.communityId) return courseOptions.value
    return courseOptions.value.filter((item) => Number(item.communityId) === Number(query.communityId))
})

// 暴露给模板的是按社区过滤后的课程；同时保留全量用于切换社区时清理无效选择
const courseOptionsForSelect = computed(() => filteredCourseOptions.value)

function cleanQuery(params: any) {
    const data: Record<string, any> = {
        page_no: params.page_no,
        page_size: params.page_size
    }
    if (params.communityId !== '' && params.communityId !== null && params.communityId !== undefined) {
        data.communityId = params.communityId
    }
    if (params.courseId !== '' && params.courseId !== null && params.courseId !== undefined) {
        data.courseId = params.courseId
    }
    if (params.date) data.date = params.date
    if (params.status !== '' && params.status !== null && params.status !== undefined) {
        data.status = params.status
    }
    return data
}

const { pager, getLists, resetPage, resetParams } = usePaging({
    fetchFun: (params: any) => getCheckinList(cleanQuery(params)),
    params: query,
    size: 20
})

// ---------- 导出核销报表（#105：旧报表菜单退场，导出搬到业务页）----------
// 复用旧报表导出接口（/report/checkin/export），与旧报表口径一致。
// 本页状态是场次核销状态（0 待核销 / 1 已核销 / 2 已取消 / 3 部分核销），
// 旧报表 status 是 attended/booked/none 三态，语义不同源，故只映射社区/课程 + 日期。
const exporting = ref(false)
const exportDateRange = ref<string[]>(currentMonthRange())

async function exportCheckinExcel() {
    exporting.value = true
    feedback.loading('正在导出核销报表...')
    try {
        await exportCheckinReport(
            compactParams({
                startDate: exportDateRange.value?.[0] || '',
                endDate: exportDateRange.value?.[1] || '',
                communityId: query.communityId,
                courseId: query.courseId
            })
        )
    } finally {
        feedback.closeLoading()
        exporting.value = false
    }
}

async function loadOptions() {
    try {
        const data = await getCheckinOptions()
        courseOptions.value = data.courses || []
        communityOptions.value = data.communities || []
    } catch {
        // 请求层已提示错误
    }
}

function onCommunityChange() {
    if (!query.communityId) return
    if (query.courseId) {
        const stillVisible = courseOptions.value.some(
            (item) =>
                Number(item.id) === Number(query.courseId) &&
                Number(item.communityId) === Number(query.communityId)
        )
        if (!stillVisible) query.courseId = ''
    }
}

function regionText(row: any) {
    const community = row.communityName || '-'
    const area = row.areaName || ''
    return area ? `${community} / ${area}` : community
}

function statusTagType(status: number) {
    if (Number(status) === 1) return 'success'
    if (Number(status) === 2) return 'info'
    if (Number(status) === 3) return 'warning'
    return 'danger'
}

// ---------- 详情 ----------
const detailVisible = ref(false)
const detail = ref<any>(null)
const detailTab = ref('roster')

async function openDetail(row: any) {
    try {
        detail.value = await getCheckinDetail({ sessionId: row.sessionId })
        detailTab.value = 'roster'
        detailVisible.value = true
    } catch {
        // 请求层已提示错误
    }
}

// ---------- 人工补核销 ----------
const manualVisible = ref(false)
const manualSubmitting = ref(false)
const manualSession = ref<any>(null)
const manualRows = ref<any[]>([])
const manualSelection = ref<any[]>([])
const manualTableRef = ref()
const manualFormRef = ref()
const manualForm = reactive({ reason: '' })
const manualRules = {
    reason: [
        { required: true, message: '补核销原因不能为空', trigger: 'blur' },
        {
            validator: (_rule: any, value: string, callback: any) => {
                if (!String(value || '').trim()) {
                    callback(new Error('补核销原因不能为空'))
                    return
                }
                callback()
            },
            trigger: 'blur'
        }
    ]
}

async function openManual(row: any) {
    try {
        const data = await getCheckinDetail({ sessionId: row.sessionId })
        manualSession.value = data
        manualRows.value = (data.roster || []).filter((item: any) => Number(item.status) !== 3)
        manualForm.reason = ''
        manualSelection.value = []
        manualVisible.value = true
        nextTick(() => {
            manualTableRef.value?.clearSelection()
            manualFormRef.value?.clearValidate()
        })
    } catch {
        // 请求层已提示错误
    }
}

function isManualSelectable(row: any) {
    return !row.attended
}

function onManualSelectionChange(rows: any[]) {
    manualSelection.value = rows
}

function resetManual() {
    manualSession.value = null
    manualRows.value = []
    manualSelection.value = []
    manualForm.reason = ''
    manualSubmitting.value = false
    manualFormRef.value?.clearValidate()
}

function onReasonInput() {
    if (String(manualForm.reason || '').trim()) {
        manualFormRef.value?.clearValidate('reason')
    }
}

async function submitManual() {
    if (manualSubmitting.value) return
    const valid = await manualFormRef.value?.validate().catch(() => false)
    if (!valid) return
    const reason = String(manualForm.reason || '').trim()
    if (!reason) {
        feedback.msgError('补核销原因不能为空')
        return
    }
    if (!manualSelection.value.length) {
        feedback.msgError('请至少选择一名待核销学员')
        return
    }
    manualSubmitting.value = true
    try {
        const data = await manualCheckin({
            sessionId: manualSession.value.sessionId,
            studentIds: manualSelection.value.map((item) => Number(item.studentId)),
            reason
        })
        manualVisible.value = false
        const warnings = data.warnings || []
        if (data.idempotent) {
            feedback.msgSuccess('所选学员均已核销，本次未重复扣券')
        } else if (warnings.length) {
            feedback.msgWarning(`补核销成功 ${data.count} 人；${warnings.length} 人未扣券：${warnings.join('；')}`)
        } else {
            feedback.msgSuccess(`补核销成功 ${data.count} 人，已按考勤扣券`)
        }
        await getLists()
    } catch {
        // 请求层已提示错误
    } finally {
        manualSubmitting.value = false
    }
}

// ---------- 审计 ----------
const auditVisible = ref(false)
const auditQuery = reactive({
    sessionId: '',
    studentId: '',
    date: ''
})
const auditPager = reactive({
    page: 1,
    size: 20,
    count: 0,
    lists: [] as any[],
    loading: false
})

function cleanAuditQuery() {
    const data: Record<string, any> = {
        page_no: auditPager.page,
        page_size: auditPager.size
    }
    if (auditQuery.sessionId) data.sessionId = auditQuery.sessionId
    if (auditQuery.studentId) data.studentId = auditQuery.studentId
    if (auditQuery.date) data.date = auditQuery.date
    return data
}

async function getAudits() {
    auditPager.loading = true
    try {
        const data = await getCheckinAudit(cleanAuditQuery())
        auditPager.count = data.count || 0
        auditPager.lists = data.lists || []
    } catch {
        // 请求层已提示错误
    } finally {
        auditPager.loading = false
    }
}

function openAuditDialog() {
    auditVisible.value = true
    auditPager.page = 1
    getAudits()
}

function searchAudits() {
    auditPager.page = 1
    getAudits()
}

function resetAudits() {
    auditQuery.sessionId = ''
    auditQuery.studentId = ''
    auditQuery.date = ''
    auditPager.page = 1
    getAudits()
}

onActivated(() => {
    loadOptions()
    getLists()
})
loadOptions()
getLists()
</script>
