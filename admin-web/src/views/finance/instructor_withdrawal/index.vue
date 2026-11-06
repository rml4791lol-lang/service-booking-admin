<template>
    <div>
        <el-card class="!border-none" shadow="never">
            <el-form ref="formRef" class="mb-[-16px]" :model="queryParams" :inline="true">
                <el-form-item label="提现单号">
                    <el-input
                        class="w-[220px]"
                        v-model="queryParams.sn"
                        placeholder="请输入提现单号"
                        clearable
                        @keyup.enter="resetPage"
                    />
                </el-form-item>
                <el-form-item label="教练姓名">
                    <el-input
                        class="w-[220px]"
                        v-model="queryParams.instructorName"
                        placeholder="请输入教练姓名"
                        clearable
                        @keyup.enter="resetPage"
                    />
                </el-form-item>
                <el-form-item label="审核状态">
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
                <el-form-item label="申请时间">
                    <daterange-picker
                        v-model:startTime="queryParams.startTime"
                        v-model:endTime="queryParams.endTime"
                    />
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="resetPage">查询</el-button>
                    <el-button @click="resetParams">重置</el-button>
                </el-form-item>
            </el-form>
        </el-card>

        <el-card class="!border-none mt-4" shadow="never">
            <el-table size="large" v-loading="pager.loading" :data="pager.lists">
                <el-table-column label="提现单号" prop="sn" min-width="140" />
                <el-table-column label="教练" min-width="130">
                    <template #default="{ row }">
                        <div>{{ row.instructorName || '-' }}</div>
                        <div class="text-gray-400 text-xs">{{ row.instructorPhone || '' }}</div>
                    </template>
                </el-table-column>
                <el-table-column label="提现金额" min-width="100">
                    <template #default="{ row }">
                        <span class="font-medium">¥ {{ formatAmount(row.amount) }}</span>
                    </template>
                </el-table-column>
                <el-table-column label="收款信息" min-width="160">
                    <template #default="{ row }">
                        <div>{{ row.bankName || '-' }}</div>
                        <div class="text-gray-400 text-xs">
                            {{ row.cardHolder || '-' }} {{ maskCard(row.bankCard) }}
                        </div>
                    </template>
                </el-table-column>
                <el-table-column label="申请时间" min-width="140">
                    <template #default="{ row }">{{ formatTime(row.createTime) }}</template>
                </el-table-column>
                <el-table-column label="状态" width="100">
                    <template #default="{ row }">
                        <el-tag :type="statusTagType(row.status)">
                            {{ statusText(row.status) }}
                        </el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="审核人" min-width="90">
                    <template #default="{ row }">{{ row.auditName || '-' }}</template>
                </el-table-column>
                <el-table-column label="审核时间" min-width="140">
                    <template #default="{ row }">{{ formatTime(row.auditTime) }}</template>
                </el-table-column>
                <el-table-column
                    label="审核备注"
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
                            v-perms="['instructor/withdrawal/audit']"
                            type="primary"
                            link
                            @click="openAudit(row)"
                        >
                            审核
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
            title="教练提现审核"
            width="620px"
            @closed="resetForm"
        >
            <el-descriptions :column="2" border>
                <el-descriptions-item label="提现单号" :span="2">
                    {{ current.sn || '-' }}
                </el-descriptions-item>
                <el-descriptions-item label="教练">
                    {{ current.instructorName || '-' }}
                </el-descriptions-item>
                <el-descriptions-item label="手机号">
                    {{ current.instructorPhone || '-' }}
                </el-descriptions-item>
                <el-descriptions-item label="提现金额">
                    ¥ {{ formatAmount(current.amount) }}
                </el-descriptions-item>
                <el-descriptions-item label="收款银行">
                    {{ current.bankName || '-' }}
                </el-descriptions-item>
                <el-descriptions-item label="持卡人" :span="2">
                    {{ current.cardHolder || '-' }} {{ maskCard(current.bankCard) }}
                </el-descriptions-item>
                <el-descriptions-item label="申请时间" :span="2">
                    {{ formatTime(current.createTime) }}
                </el-descriptions-item>
            </el-descriptions>

            <el-alert
                class="mt-4"
                type="warning"
                :closable="false"
                title="通过表示已打款；拒绝后金额会退回教练余额。"
            />

            <el-form
                ref="auditFormRef"
                class="mt-5"
                :model="form"
                :rules="rules"
                label-width="90px"
            >
                <el-form-item label="审核结果" prop="status">
                    <el-radio-group v-model="form.status" @change="onStatusChange">
                        <el-radio :label="1">通过（已打款）</el-radio>
                        <el-radio :label="2">拒绝</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="审核备注" prop="auditRemark">
                    <el-input
                        v-model="form.auditRemark"
                        type="textarea"
                        :rows="3"
                        maxlength="500"
                        show-word-limit
                        :placeholder="form.status === 2 ? '拒绝时必须填写原因' : '可选，填写打款/审核说明'"
                    />
                </el-form-item>
            </el-form>

            <template #footer>
                <el-button @click="dialogVisible = false">取消</el-button>
                <el-button type="primary" :loading="submitting" @click="submit">
                    提交审核
                </el-button>
            </template>
        </el-dialog>
    </div>
</template>

<script lang="ts" setup name="instructorWithdrawalReview">
import { auditInstructorWithdrawal, getInstructorWithdrawalList } from '@/api/review'
import { usePaging } from '@/hooks/usePaging'
import feedback from '@/utils/feedback'
import { timeFormat } from '@/utils/util'

const queryParams = reactive({
    sn: '',
    instructorName: '',
    status: '',
    startTime: '',
    endTime: ''
})

// 提现接口使用 pageNo/pageSize，其他三类审批使用 page_no/page_size
function fetchList(params: any) {
    return getInstructorWithdrawalList({
        sn: params.sn,
        instructorName: params.instructorName,
        status: params.status,
        startTime: params.startTime,
        endTime: params.endTime,
        pageNo: params.page_no,
        pageSize: params.page_size
    })
}

const { pager, getLists, resetPage, resetParams } = usePaging({
    fetchFun: fetchList,
    params: queryParams
})

const statusOptions = [
    { value: 0, label: '待审核' },
    { value: 1, label: '已通过' },
    { value: 2, label: '已拒绝' }
]

const statusTextMap: Record<number, string> = {
    0: '待审核',
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

function formatAmount(amount: number | string) {
    const value = Number(amount || 0)
    return value.toFixed(2)
}

function maskCard(card: string) {
    const value = String(card || '')
    if (!value) return ''
    if (value.length <= 8) return value
    return `${value.slice(0, 4)} **** ${value.slice(-4)}`
}

function formatTime(value: number | string) {
    if (!value) return '-'
    if (typeof value === 'string') return value
    return timeFormat(Number(value), 'yyyy-mm-dd hh:MM')
}

const dialogVisible = ref(false)
const submitting = ref(false)
const auditFormRef = ref()
const current = ref<any>({})
const form = reactive({
    id: 0,
    status: 1,
    auditRemark: ''
})

const rules = {
    status: [{ required: true, message: '请选择审核结果', trigger: 'change' }],
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
        await auditInstructorWithdrawal({
            id: form.id,
            status: form.status,
            auditRemark: form.auditRemark.trim()
        })
        dialogVisible.value = false
        feedback.msgSuccess(form.status === 1 ? '审核已通过' : '审核已拒绝')
        // 立即刷新，状态/审核人/备注无需手动刷新页面
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
