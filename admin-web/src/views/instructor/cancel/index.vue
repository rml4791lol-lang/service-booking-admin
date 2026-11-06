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
                <el-table-column
                    label="注销原因"
                    prop="reason"
                    min-width="110"
                    show-tooltip-when-overflow
                />
                <el-table-column
                    label="详细说明"
                    prop="detail"
                    min-width="140"
                    show-tooltip-when-overflow
                />
                <el-table-column label="申请时间" min-width="150">
                    <template #default="{ row }">{{ formatTime(row.createTime) }}</template>
                </el-table-column>
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
                <el-table-column label="操作" width="70" fixed="right">
                    <template #default="{ row }">
                        <el-button
                            v-if="row.status === 0"
                            v-perms="['instructor/cancel/audit']"
                            type="primary"
                            link
                            @click="openAudit(row)"
                        >
                            审批
                        </el-button>
                        <span v-else class="text-gray-400">-</span>
                    </template>
                </el-table-column>
            </el-table>
            <div class="flex justify-end mt-4">
                <pagination v-model="pager" @change="getLists" />
            </div>
        </el-card>

        <el-dialog
            v-model="dialogVisible"
            title="教练注销审批"
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
                <el-descriptions-item label="注销原因">
                    {{ current.reason || '-' }}
                </el-descriptions-item>
                <el-descriptions-item label="申请时间">
                    {{ formatTime(current.createTime) }}
                </el-descriptions-item>
                <el-descriptions-item label="详细说明" :span="2">
                    {{ current.detail || '-' }}
                </el-descriptions-item>
            </el-descriptions>

            <el-alert
                class="mt-4"
                type="warning"
                :closable="false"
                title="通过后该教练账号将被删除，请确认已处理完课程与结算。"
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
    </div>
</template>

<script lang="ts" setup name="instructorCancelReview">
import { auditInstructorCancel, getInstructorCancelList } from '@/api/review'
import { usePaging } from '@/hooks/usePaging'
import feedback from '@/utils/feedback'
import { timeFormat } from '@/utils/util'

const queryParams = reactive({
    name: '',
    phone: '',
    status: ''
})

const { pager, getLists, resetPage, resetParams } = usePaging({
    fetchFun: getInstructorCancelList,
    params: queryParams
})

const statusOptions = [
    { value: 0, label: '待审核' },
    { value: 1, label: '已通过' },
    { value: 2, label: '已驳回' }
]

const statusTextMap: Record<number, string> = {
    0: '待审核',
    1: '已通过',
    2: '已驳回'
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
        await auditInstructorCancel({
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
</script>
