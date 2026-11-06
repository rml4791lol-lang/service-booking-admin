<template>
    <div>
        <el-card class="!border-none" shadow="never">
            <el-form ref="formRef" class="mb-[-16px]" :model="queryParams" :inline="true">
                <el-form-item label="用户姓名">
                    <el-input
                        class="w-[220px]"
                        v-model="queryParams.userName"
                        placeholder="请输入用户姓名"
                        clearable
                        @keyup.enter="resetPage"
                    />
                </el-form-item>
                <el-form-item label="来源">
                    <el-select class="w-[140px]" v-model="queryParams.source">
                        <el-option label="全部" value="" />
                        <el-option label="课程" value="course" />
                        <el-option label="券" value="coupon" />
                    </el-select>
                </el-form-item>
                <el-form-item label="审批状态">
                    <el-select class="w-[180px]" v-model="queryParams.status">
                        <el-option label="全部" value="" />
                        <el-option
                            v-for="item in statusOptions"
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
            <el-table size="large" v-loading="pager.loading" :data="pager.lists">
                <el-table-column label="ID" prop="id" width="55" />
                <!-- #110 T12：券退款复用本表（course_class_id=0），用来源列区分课程/券。 -->
                <el-table-column label="来源" width="80">
                    <template #default="{ row }">
                        <el-tag :type="row.source === 'coupon' ? 'warning' : 'info'" size="small">
                            {{ row.sourceText || (row.source === 'coupon' ? '券' : '课程') }}
                        </el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="用户" prop="userName" min-width="85" show-tooltip-when-overflow>
                    <template #default="{ row }">{{ row.userName || '-' }}</template>
                </el-table-column>
                <el-table-column label="班级 / 券名" prop="className" min-width="150" show-tooltip-when-overflow>
                    <template #default="{ row }">{{ row.className || '-' }}</template>
                </el-table-column>
                <el-table-column label="退课原因" min-width="160">
                    <template #default="{ row }">
                        <div>{{ row.reason || '-' }}</div>
                        <div class="text-gray-400 text-xs truncate">{{ row.detail || '-' }}</div>
                    </template>
                </el-table-column>
                <el-table-column label="金额" min-width="110">
                    <template #default="{ row }">
                        <div>已付 ¥{{ formatAmount(row.payAmount) }}</div>
                        <div class="text-gray-400 text-xs">应退 ¥{{ formatAmount(row.refundAmount) }}</div>
                    </template>
                </el-table-column>
                <el-table-column label="课时" width="75">
                    <template #default="{ row }">
                        {{ row.usedLessons ?? 0 }}/{{ row.totalLessons ?? 0 }}
                    </template>
                </el-table-column>
                <el-table-column label="状态" width="75">
                    <template #default="{ row }">
                        <el-tag :type="statusTagType(row.status)">
                            {{ statusText(row.status) }}
                        </el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="退款任务" min-width="190">
                    <template #default="{ row }">
                        <template v-if="row.refundTaskId">
                            <el-tag :type="refundTaskTagType(row.refundTaskStatus)" size="small">
                                {{ refundTaskStatusText(row.refundTaskStatus) }}
                            </el-tag>
                            <span v-if="row.refundRetryCount > 0" class="text-gray-400 text-xs ml-1">
                                重试 {{ row.refundRetryCount }} 次
                            </span>
                            <div
                                v-if="row.refundTaskStatus === 3"
                                class="text-xs text-red-500 truncate"
                                :title="row.refundLastError || '退款执行失败'"
                            >
                                {{ row.refundLastError || '退款执行失败' }}
                            </div>
                            <div
                                v-else-if="row.refundTaskResultMsg"
                                class="text-gray-400 text-xs truncate"
                                :title="row.refundTaskResultMsg"
                            >
                                {{ row.refundTaskResultMsg }}
                            </div>
                        </template>
                        <span v-else class="text-gray-400">-</span>
                    </template>
                </el-table-column>
                <el-table-column label="审批人 / 时间" min-width="150">
                    <template #default="{ row }">
                        <div>{{ row.auditName || '-' }}</div>
                        <div class="text-gray-400 text-xs">{{ row.auditTime || '-' }}</div>
                    </template>
                </el-table-column>
                <el-table-column
                    label="审批备注"
                    prop="auditRemark"
                    min-width="150"
                    show-tooltip-when-overflow
                >
                    <template #default="{ row }">{{ row.auditRemark || '-' }}</template>
                </el-table-column>
                <el-table-column label="操作" width="150" fixed="right">
                    <template #default="{ row }">
                        <el-button
                            v-if="row.status === 0"
                            v-perms="['course_refund/audit']"
                            type="primary"
                            link
                            @click="openAudit(row)"
                        >
                            审批
                        </el-button>
                        <template v-if="row.refundTaskId">
                            <el-button type="primary" link @click="openTask(row)">任务</el-button>
                            <el-button
                                v-if="row.refundTaskStatus === 3"
                                v-perms="['course_refund/task/retry']"
                                type="danger"
                                link
                                :loading="retryingTask"
                                @click="retryTask(row.id)"
                            >
                                重试
                            </el-button>
                        </template>
                        <span
                            v-if="row.status !== 0 && !row.refundTaskId"
                            class="text-gray-400"
                        >
                            -
                        </span>
                    </template>
                </el-table-column>
            </el-table>
            <div class="flex justify-end mt-4">
                <pagination v-model="pager" @change="getLists" />
            </div>
        </el-card>

        <el-dialog
            v-model="dialogVisible"
            title="退课审批"
            width="680px"
            @closed="resetForm"
        >
            <el-descriptions :column="2" border>
                <el-descriptions-item label="用户">
                    {{ current.userName || '-' }}
                </el-descriptions-item>
                <el-descriptions-item label="申请时间">
                    {{ current.createTime || '-' }}
                </el-descriptions-item>
                <el-descriptions-item label="来源">
                    {{ current.sourceText || (current.source === 'coupon' ? '券' : '课程') }}
                </el-descriptions-item>
                <el-descriptions-item label="班级 / 券名" :span="2">
                    {{ current.className || '-' }}
                </el-descriptions-item>
                <el-descriptions-item label="退课原因">
                    {{ current.reason || '-' }}
                </el-descriptions-item>
                <el-descriptions-item label="课时（已消/总）">
                    {{ current.usedLessons ?? 0 }}/{{ current.totalLessons ?? 0 }}
                </el-descriptions-item>
                <el-descriptions-item label="已付金额">
                    ¥{{ formatAmount(current.payAmount) }}
                </el-descriptions-item>
                <el-descriptions-item label="应退金额">
                    ¥{{ formatAmount(current.refundAmount) }}
                </el-descriptions-item>
                <el-descriptions-item label="详细说明" :span="2">
                    {{ current.detail || '-' }}
                </el-descriptions-item>
            </el-descriptions>

            <el-alert
                class="mt-4"
                type="warning"
                :closable="false"
                title="通过后退款将按原支付方式处理：课程订单标记为已退课并清零剩余课时；券订单作废其券实例并写退回流水；涉及微信支付的订单会发起原路退款（异步任务）。"
            />

            <el-form
                ref="auditFormRef"
                class="mt-5"
                :model="form"
                :rules="rules"
                label-width="90px"
            >
                <el-form-item label="审批结果" prop="status">
                    <el-radio-group v-model="form.status" @change="onStatusChange">
                        <el-radio :label="1">通过</el-radio>
                        <el-radio :label="2">拒绝</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="审批备注" prop="auditRemark">
                    <el-input
                        v-model="form.auditRemark"
                        type="textarea"
                        :rows="3"
                        maxlength="500"
                        show-word-limit
                        :placeholder="form.status === 2 ? '拒绝时必须填写原因' : '可选，填写审批说明'"
                    />
                </el-form-item>
            </el-form>

            <template #footer>
                <el-button @click="dialogVisible = false">取消</el-button>
                <el-button type="primary" :loading="submitting" @click="submit">
                    提交审批
                </el-button>
            </template>
        </el-dialog>

        <el-dialog v-model="taskDialogVisible" title="退款任务与对账流水" width="760px">
            <el-descriptions v-if="taskDetail" :column="2" border>
                <el-descriptions-item label="任务编号">
                    {{ taskDetail.taskNo || '-' }}
                </el-descriptions-item>
                <el-descriptions-item label="任务状态">
                    <el-tag :type="refundTaskTagType(taskDetail.status)" size="small">
                        {{ taskDetail.statusDesc || refundTaskStatusText(taskDetail.status) }}
                    </el-tag>
                </el-descriptions-item>
                <el-descriptions-item label="微信退款单号" :span="2">
                    {{ taskDetail.outRefundNo || '-' }}
                </el-descriptions-item>
                <el-descriptions-item label="订单总额">
                    ¥{{ formatAmount(taskDetail.totalFee) }}
                </el-descriptions-item>
                <el-descriptions-item label="退款金额">
                    ¥{{ formatAmount(taskDetail.refundFee) }}
                </el-descriptions-item>
                <el-descriptions-item label="重试次数">
                    {{ taskDetail.retryCount ?? 0 }} / {{ taskDetail.maxRetry ?? '-' }}
                </el-descriptions-item>
                <el-descriptions-item label="下次自动重试">
                    {{ taskDetail.nextRetryTime || '-' }}
                </el-descriptions-item>
                <el-descriptions-item label="执行结果" :span="2">
                    {{ taskDetail.resultMsg || '-' }}
                </el-descriptions-item>
                <el-descriptions-item label="最后错误" :span="2">
                    {{ taskDetail.lastError || '-' }}
                </el-descriptions-item>
                <el-descriptions-item label="创建时间">
                    {{ taskDetail.createTime || '-' }}
                </el-descriptions-item>
                <el-descriptions-item label="完成时间">
                    {{ taskDetail.finishTime || '-' }}
                </el-descriptions-item>
            </el-descriptions>
            <el-table
                class="mt-4"
                size="small"
                :data="taskLogs"
                v-loading="taskLogsLoading"
                max-height="260"
            >
                <el-table-column label="时间" prop="createTime" width="160" />
                <el-table-column label="动作" prop="actionDesc" width="100" />
                <el-table-column label="结果" width="80">
                    <template #default="{ row }">
                        <el-tag :type="refundLogTagType(row.status)" size="small">
                            {{ row.statusDesc }}
                        </el-tag>
                    </template>
                </el-table-column>
                <el-table-column
                    label="说明"
                    prop="message"
                    min-width="220"
                    show-tooltip-when-overflow
                />
                <el-table-column label="耗时" width="80">
                    <template #default="{ row }">{{ row.costMs ?? 0 }}ms</template>
                </el-table-column>
            </el-table>
            <template #footer>
                <el-button @click="taskDialogVisible = false">关闭</el-button>
                <el-button
                    v-if="taskDetail && (taskDetail.status === 3 || isTaskStale(taskDetail))"
                    v-perms="['course_refund/task/retry']"
                    type="primary"
                    :loading="retryingTask"
                    @click="retryTask(taskDetail.refundId)"
                >
                    重试退款
                </el-button>
            </template>
        </el-dialog>
    </div>
</template>

<script lang="ts" setup name="courseRefundReview">
import {
    auditCourseRefund,
    getCourseRefundList,
    getCourseRefundTaskList,
    getCourseRefundTaskLogs,
    retryCourseRefundTask
} from '@/api/review'
import { usePaging } from '@/hooks/usePaging'
import feedback from '@/utils/feedback'

const queryParams = reactive({
    userName: '',
    status: '',
    // #110 T12：来源筛选（空=全部，course=课程，coupon=课程券）
    source: ''
})

// 退课接口使用 page_no/page_size；状态为空时不传，避免空串转数字失败
function fetchList(params: any) {
    const query: Record<string, any> = {
        page_no: params.page_no,
        page_size: params.page_size,
        userName: params.userName
    }
    if (params.status !== '' && params.status !== null && params.status !== undefined) {
        query.status = params.status
    }
    if (params.source) {
        query.source = params.source
    }
    return getCourseRefundList(query)
}

const { pager, getLists, resetPage, resetParams } = usePaging({
    fetchFun: fetchList,
    params: queryParams
})

const statusOptions = [
    { value: 0, label: '待审批' },
    { value: 1, label: '已通过' },
    { value: 2, label: '已拒绝' }
]

const statusTextMap: Record<number, string> = {
    0: '待审批',
    1: '已通过',
    2: '已拒绝'
}
const statusTagMap: Record<number, string> = {
    0: 'warning',
    1: 'success',
    2: 'danger'
}

function statusText(status: number) {
    return statusTextMap[status] || `未知(${status})`
}

function statusTagType(status: number) {
    return statusTagMap[status] || 'info'
}

const refundTaskStatusTextMap: Record<number, string> = {
    0: '待执行',
    1: '执行中',
    2: '成功',
    3: '失败'
}
const refundTaskTagMap: Record<number, string> = {
    0: 'info',
    1: 'warning',
    2: 'success',
    3: 'danger'
}

function refundTaskStatusText(status: number | null | undefined) {
    if (status === null || status === undefined) return '-'
    return refundTaskStatusTextMap[status] || `未知(${status})`
}

function refundTaskTagType(status: number | null | undefined) {
    if (status === null || status === undefined) return 'info'
    return refundTaskTagMap[status] || 'info'
}

function refundLogTagType(status: number | null | undefined) {
    if (status === 1) return 'success'
    if (status === 2) return 'danger'
    return 'info'
}

function formatAmount(value: string | number | null | undefined) {
    if (value === null || value === undefined || value === '') return '0.00'
    const amount = Number(value)
    return Number.isFinite(amount) ? amount.toFixed(2) : String(value)
}

const dialogVisible = ref(false)
const submitting = ref(false)
const taskDialogVisible = ref(false)
const taskLogsLoading = ref(false)
const retryingTask = ref(false)
const taskDetail = ref<any>(null)
const taskLogs = ref<any[]>([])
const auditFormRef = ref()
const current = ref<any>({})
const form = reactive({
    id: 0,
    status: 1,
    auditRemark: ''
})

const rules = {
    status: [{ required: true, message: '请选择审批结果', trigger: 'change' }],
    auditRemark: [
        {
            validator: (_rule: any, value: string, callback: any) => {
                if (form.status === 2 && !String(value || '').trim()) {
                    callback(new Error('拒绝时必须填写原因'))
                    return
                }
                callback()
            },
            trigger: 'blur'
        }
    ]
}

function openAudit(row: any) {
    current.value = row
    form.id = row.id
    form.status = 1
    form.auditRemark = ''
    dialogVisible.value = true
    nextTick(() => auditFormRef.value?.clearValidate())
}

function resetForm() {
    form.id = 0
    form.status = 1
    form.auditRemark = ''
    auditFormRef.value?.clearValidate()
}

async function openTask(row: any) {
    if (!row || !row.refundTaskId) return
    taskDialogVisible.value = true
    taskDetail.value = null
    taskLogs.value = []
    taskLogsLoading.value = true
    try {
        const res: any = await getCourseRefundTaskList({
            page_no: 1,
            page_size: 1,
            // 后端 RefundTaskSearchValidate 字段为 refundId（camelCase）
            refundId: row.id
        })
        // 请求层已解包 AjaxResult.data，这里直接就是 PageResult
        const lists = res?.lists || []
        taskDetail.value = lists[0] || {
            status: row.refundTaskStatus,
            statusDesc: row.refundTaskStatusDesc,
            refundId: row.id,
            retryCount: row.refundRetryCount,
            lastError: row.refundLastError,
            resultMsg: row.refundTaskResultMsg
        }
        const logRes: any = await getCourseRefundTaskLogs({ task_id: row.refundTaskId })
        // task/log 返回 AjaxResult<List<RefundTaskLogVo>>，请求层解包后即为数组
        taskLogs.value = Array.isArray(logRes) ? logRes : logRes?.lists || []
    } catch {
        // 请求层已提示错误
    } finally {
        taskLogsLoading.value = false
    }
}

async function retryTask(refundId: number) {
    if (!refundId || retryingTask.value) return
    retryingTask.value = true
    try {
        await retryCourseRefundTask({ id: refundId })
        feedback.msgSuccess('退款任务已提交重试')
        taskDialogVisible.value = false
        await getLists()
    } catch {
        // 请求层已提示错误
    } finally {
        retryingTask.value = false
    }
}

// 退款任务「执行中」超过 5 分钟视为 stale，后端 retry 接口允许重试（RefundTaskServiceImpl.retry）
const STALE_PROCESSING_SECONDS = 300

function isTaskStale(task: any) {
    if (!task || Number(task.status) !== 1) return false
    const executeTime = String(task.executeTime || '').trim()
    if (!executeTime) return false
    // 后端格式 yyyy-MM-dd HH:mm:ss，替换 - 兼容 Safari
    const startedAt = new Date(executeTime.replace(/-/g, '/')).getTime()
    if (!Number.isFinite(startedAt)) return false
    return Date.now() - startedAt >= STALE_PROCESSING_SECONDS * 1000
}

function onStatusChange() {
    if (form.status === 2) {
        nextTick(() => auditFormRef.value?.validateField('auditRemark').catch(() => {}))
    } else {
        auditFormRef.value?.clearValidate('auditRemark')
    }
}

async function submit() {
    const valid = await auditFormRef.value?.validate().catch(() => false)
    if (!valid || submitting.value) return
    submitting.value = true
    try {
        await auditCourseRefund({
            id: form.id,
            status: form.status,
            auditRemark: String(form.auditRemark || '').trim()
        })
        dialogVisible.value = false
        feedback.msgSuccess(form.status === 1 ? '退课申请已通过' : '退课申请已拒绝')
        // 立即刷新，状态/审批人/审批时间无需手动刷新页面
        await getLists()
    } catch {
        // 请求层已提示错误
    } finally {
        submitting.value = false
    }
}

onActivated(() => {
    getLists()
})
getLists()
</script>
