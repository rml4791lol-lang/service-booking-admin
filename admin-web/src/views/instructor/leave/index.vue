<template>
    <div>
        <el-card class="!border-none" shadow="never">
            <el-form ref="formRef" class="mb-[-16px]" :model="queryParams" :inline="true">
                <el-form-item label="教练姓名">
                    <el-input
                        class="w-[220px]"
                        v-model="queryParams.name"
                        placeholder="请输入教练姓名"
                        clearable
                        @keyup.enter="resetPage"
                    />
                </el-form-item>
                <el-form-item label="手机号">
                    <el-input
                        class="w-[220px]"
                        v-model="queryParams.phone"
                        placeholder="请输入手机号"
                        clearable
                        @keyup.enter="resetPage"
                    />
                </el-form-item>
                <el-form-item label="审批状态">
                    <el-select class="w-[180px]" v-model="queryParams.status">
                        <el-option label="全部" value />
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
                <el-table-column label="ID" prop="id" width="70" />
                <el-table-column label="教练" min-width="130">
                    <template #default="{ row }">
                        <div>{{ row.instructorName || '-' }}</div>
                        <div class="text-gray-400 text-xs">{{ row.instructorPhone || '' }}</div>
                    </template>
                </el-table-column>
                <el-table-column label="请假时间" min-width="200">
                    <template #default="{ row }">
                        {{ formatTime(row.startTime) }} 至 {{ formatTime(row.endTime) }}
                    </template>
                </el-table-column>
                <el-table-column
                    label="请假原因"
                    prop="reason"
                    min-width="100"
                    show-tooltip-when-overflow
                />
                <el-table-column
                    label="详细说明"
                    prop="detail"
                    min-width="140"
                    show-tooltip-when-overflow
                />
                <el-table-column label="状态" width="90">
                    <template #default="{ row }">
                        <el-tag :type="statusTagType(row.status)">
                            {{ statusText(row.status) }}
                        </el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="审批人" min-width="90">
                    <template #default="{ row }">{{ row.auditName || '-' }}</template>
                </el-table-column>
                <el-table-column label="审批时间" min-width="150">
                    <template #default="{ row }">{{ formatTime(row.auditTime) }}</template>
                </el-table-column>
                <el-table-column
                    label="审批备注"
                    prop="auditRemark"
                    min-width="140"
                    show-tooltip-when-overflow
                >
                    <template #default="{ row }">{{ row.auditRemark || '-' }}</template>
                </el-table-column>
                <el-table-column label="操作" width="130" fixed="right">
                    <template #default="{ row }">
                        <el-button
                            v-if="row.status === 0"
                            v-perms="['instructor/leave/audit']"
                            type="primary"
                            link
                            @click="openAudit(row)"
                        >
                            审批
                        </el-button>
                        <el-button
                            v-if="row.status === 1"
                            v-perms="['instructor/leave/arrangeMakeup']"
                            type="primary"
                            link
                            @click="openMakeup(row)"
                        >
                            补课
                        </el-button>
                        <span
                            v-if="row.status !== 0 && row.status !== 1"
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
            title="教练请假审批"
            width="640px"
            @closed="resetForm"
        >
            <el-descriptions :column="2" border>
                <el-descriptions-item label="教练">
                    {{ current.instructorName || '-' }}
                </el-descriptions-item>
                <el-descriptions-item label="手机号">
                    {{ current.instructorPhone || '-' }}
                </el-descriptions-item>
                <el-descriptions-item label="请假原因">
                    {{ current.reason || '-' }}
                </el-descriptions-item>
                <el-descriptions-item label="状态">
                    {{ statusText(current.status) }}
                </el-descriptions-item>
                <el-descriptions-item label="开始时间" :span="2">
                    {{ formatTime(current.startTime) }}
                </el-descriptions-item>
                <el-descriptions-item label="结束时间" :span="2">
                    {{ formatTime(current.endTime) }}
                </el-descriptions-item>
                <el-descriptions-item label="详细说明" :span="2">
                    {{ current.detail || '-' }}
                </el-descriptions-item>
            </el-descriptions>

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
                <el-form-item label="审批备注" prop="remark">
                    <el-input
                        v-model="form.remark"
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

        <el-drawer
            v-model="makeupVisible"
            :title="`补课安排 - ${makeupLeave.instructorName || ''}`"
            size="72%"
            @closed="resetMakeup"
        >
            <el-alert
                class="mb-3"
                type="info"
                :closable="false"
                :title="`请假期间 ${makeupMeta.startDate || '-'} 至 ${makeupMeta.endDate || '-'}，期间的课节需安排补课`"
                description="填写补课日期与时段后保存；教练按安排完成上课后点「完成结算」，课时费计入教练余额，完成后不可撤销。"
            />
            <el-table
                size="large"
                :data="makeupLessons"
                v-loading="makeupLoading"
            >
                <el-table-column label="班级" prop="className" min-width="140" />
                <el-table-column label="课次" width="80">
                    <template #default="{ row }">
                        第 {{ row.lessonNo }} 次
                    </template>
                </el-table-column>
                <el-table-column label="原上课时间" min-width="170">
                    <template #default="{ row }">
                        {{ row.lessonDate }} {{ row.weekDay }} {{ row.timeSlot }}
                    </template>
                </el-table-column>
                <el-table-column label="补课安排" min-width="300">
                    <template #default="{ row }">
                        <template v-if="row.makeupStatus === -1">
                            <el-date-picker
                                class="!w-[150px]"
                                v-model="makeupForm[row.lessonId].date"
                                type="date"
                                value-format="YYYY-MM-DD"
                                placeholder="补课日期"
                            />
                            <el-input
                                class="!w-[140px] ml-2"
                                v-model="makeupForm[row.lessonId].slot"
                                placeholder="时段如 19:00-20:10"
                                maxlength="30"
                            />
                        </template>
                        <template v-else>
                            <span v-if="row.makeupStatus === 1" class="text-green-600">
                                已完成 {{ row.makeupDate }} {{ row.makeupTimeSlot }}
                            </span>
                            <template v-else>
                                <el-date-picker
                                    class="!w-[150px]"
                                    v-model="makeupForm[row.lessonId].date"
                                    type="date"
                                    value-format="YYYY-MM-DD"
                                />
                                <el-input
                                    class="!w-[140px] ml-2"
                                    v-model="makeupForm[row.lessonId].slot"
                                    maxlength="30"
                                />
                                <div class="text-xs text-gray-400 mt-1">
                                    待完成（可修改安排）
                                </div>
                            </template>
                        </template>
                    </template>
                </el-table-column>
                <el-table-column label="操作" width="120" fixed="right">
                    <template #default="{ row }">
                        <el-button
                            v-if="row.makeupStatus !== 1"
                            v-perms="['instructor/leave/arrangeMakeup']"
                            type="primary"
                            link
                            :loading="arranging === row.lessonId"
                            :disabled="!canArrange(row.lessonId)"
                            @click="arrangeOne(row)"
                        >
                            保存安排
                        </el-button>
                        <el-button
                            v-if="row.makeupStatus === 0"
                            v-perms="['instructor/leave/makeupComplete']"
                            type="success"
                            link
                            :loading="completing === row.lessonId"
                            @click="completeOne(row)"
                        >
                            完成结算
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
        </el-drawer>
    </div>
</template>

<script lang="ts" setup name="instructorLeaveReview">
import {
    arrangeInstructorLeaveMakeup,
    auditInstructorLeave,
    completeInstructorLeaveMakeup,
    getInstructorLeaveList,
    getInstructorLeaveMakeupLessons
} from '@/api/review'
import { usePaging } from '@/hooks/usePaging'
import feedback from '@/utils/feedback'
import { timeFormat } from '@/utils/util'

const queryParams = reactive({
    name: '',
    phone: '',
    status: ''
})

const { pager, getLists, resetPage, resetParams } = usePaging({
    fetchFun: getInstructorLeaveList,
    params: queryParams
})

const statusOptions = [
    { value: 0, label: '待审批' },
    { value: 1, label: '已通过' },
    { value: 2, label: '已拒绝' },
    { value: 4, label: '已销假' }
]

const statusTextMap: Record<number, string> = {
    0: '待审批',
    1: '已通过',
    2: '已拒绝',
    4: '已销假'
}
const statusTagMap: Record<number, string> = {
    0: 'warning',
    1: 'success',
    2: 'danger',
    4: 'info'
}

function statusText(status: number) {
    return statusTextMap[status] || `未知(${status})`
}

function statusTagType(status: number) {
    return statusTagMap[status] || 'info'
}

function formatTime(value: number | string) {
    if (!value) return '-'
    return timeFormat(Number(value), 'yyyy-mm-dd hh:MM')
}

const dialogVisible = ref(false)
const submitting = ref(false)
const auditFormRef = ref()
const current = ref<any>({})
const form = reactive({
    id: 0,
    status: 1,
    remark: ''
})

const rules = {
    status: [{ required: true, message: '请选择审批结果', trigger: 'change' }],
    remark: [
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
    form.remark = ''
    dialogVisible.value = true
    nextTick(() => auditFormRef.value?.clearValidate())
}

function resetForm() {
    form.id = 0
    form.status = 1
    form.remark = ''
    auditFormRef.value?.clearValidate()
}

function onStatusChange() {
    if (form.status === 2) {
        nextTick(() => auditFormRef.value?.validateField('remark').catch(() => {}))
    } else {
        auditFormRef.value?.clearValidate('remark')
    }
}

async function submit() {
    const valid = await auditFormRef.value?.validate().catch(() => false)
    if (!valid || submitting.value) return
    submitting.value = true
    try {
        await auditInstructorLeave({
            id: form.id,
            status: form.status,
            remark: form.remark.trim()
        })
        dialogVisible.value = false
        feedback.msgSuccess(form.status === 1 ? '审批已通过' : '审批已拒绝')
        // 立即刷新，状态/审批人/备注无需手动刷新页面
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

// ---- 补课安排（工单 #87，对齐老后台 makeup-drawer 调用面）----
const makeupVisible = ref(false)
const makeupLoading = ref(false)
const makeupLeave = ref<any>({})
const makeupMeta = reactive({ startDate: '', endDate: '' })
const makeupLessons = ref<any[]>([])
const makeupForm = reactive<Record<number, { date: string; slot: string }>>({})
const arranging = ref<number | null>(null)
const completing = ref<number | null>(null)

async function openMakeup(row: any) {
    makeupLeave.value = row
    makeupVisible.value = true
    await loadMakeup(row.id)
}

async function loadMakeup(leaveId: number) {
    makeupLoading.value = true
    try {
        const data: any = await getInstructorLeaveMakeupLessons({ leaveId })
        makeupLessons.value = data.lessons || []
        makeupMeta.startDate = data.startDate || ''
        makeupMeta.endDate = data.endDate || ''
        syncMakeupForm()
    } catch {
        // 请求层已提示错误
    } finally {
        makeupLoading.value = false
    }
}

// 已有安排的课节回填表单，便于修改
function syncMakeupForm() {
    for (const lesson of makeupLessons.value) {
        if (lesson.makeupStatus >= 0) {
            makeupForm[lesson.lessonId] = {
                date: lesson.makeupDate || '',
                slot: lesson.makeupTimeSlot || ''
            }
        } else if (!makeupForm[lesson.lessonId]) {
            makeupForm[lesson.lessonId] = { date: '', slot: '' }
        }
    }
}

function canArrange(lessonId: number) {
    const form = makeupForm[lessonId]
    return !!form && !!form.date && !!form.slot.trim()
}

async function arrangeOne(lesson: any) {
    if (!canArrange(lesson.lessonId) || arranging.value !== null) return
    arranging.value = lesson.lessonId
    try {
        await arrangeInstructorLeaveMakeup({
            leaveId: makeupLeave.value.id,
            lessonId: lesson.lessonId,
            makeupDate: makeupForm[lesson.lessonId].date,
            makeupTimeSlot: makeupForm[lesson.lessonId].slot.trim()
        })
        feedback.msgSuccess('补课安排已保存')
        await loadMakeup(makeupLeave.value.id)
    } catch {
        // 请求层已提示错误
    } finally {
        arranging.value = null
    }
}

async function completeOne(lesson: any) {
    if (completing.value !== null) return
    try {
        await feedback.confirm(
            `确认「${lesson.className}」第 ${lesson.lessonNo} 次课已完成补课？结算后课时费计入教练余额，不可撤销。`
        )
    } catch {
        return
    }
    completing.value = lesson.lessonId
    try {
        await completeInstructorLeaveMakeup({ makeupId: lesson.makeupId })
        feedback.msgSuccess('补课已完成结算')
        await loadMakeup(makeupLeave.value.id)
    } catch {
        // 请求层已提示错误
    } finally {
        completing.value = null
    }
}

function resetMakeup() {
    makeupLessons.value = []
    Object.keys(makeupForm).forEach((key) => delete makeupForm[Number(key)])
    makeupMeta.startDate = ''
    makeupMeta.endDate = ''
}
</script>
