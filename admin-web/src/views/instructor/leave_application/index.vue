<template>
    <div>
        <el-card class="!border-none" shadow="never">
            <div class="text-lg font-medium">家长请假审批</div>
            <div class="text-tx-secondary text-sm mt-1 leading-6">
                家长在小程序提交的请假申请（按班级/学员/时间）；审批通过或拒绝后家长端可看到结果。
                历史数据来自旧版请假表，操作会记入后台日志。
            </div>
        </el-card>

        <el-card class="!border-none mt-4" shadow="never">
            <el-form class="mb-[-16px]" :inline="true" :model="queryParams">
                <el-form-item label="申请人">
                    <el-input v-model="queryParams.applicantName" class="w-[180px]" placeholder="家长姓名" clearable @keyup.enter="resetPage" />
                </el-form-item>
                <el-form-item label="课程班级">
                    <el-select
                        v-model="queryParams.classId"
                        class="w-[260px]"
                        clearable
                        filterable
                        remote
                        reserve-keyword
                        :remote-method="searchClasses"
                        :loading="classLoading"
                        placeholder="输入班级名称搜索"
                    >
                        <el-option v-for="item in classOptions" :key="item.id" :label="item.name" :value="item.id" />
                    </el-select>
                </el-form-item>
                <el-form-item label="审批状态">
                    <el-select v-model="queryParams.status" class="w-[150px]">
                        <el-option label="全部" value="" />
                        <el-option label="待审批" :value="0" />
                        <el-option label="已批准" :value="1" />
                        <el-option label="已拒绝" :value="2" />
                    </el-select>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="resetPage">查询</el-button>
                    <el-button @click="resetParams">重置</el-button>
                </el-form-item>
            </el-form>
        </el-card>

        <el-card class="!border-none mt-4" shadow="never">
            <el-table size="large" table-layout="fixed" v-loading="pager.loading" :data="pager.lists">
                <el-table-column label="ID" prop="id" width="70" />
                <el-table-column label="申请人" min-width="110">
                    <template #default="{ row }">{{ row.applicantName || '-' }}</template>
                </el-table-column>
                <el-table-column label="课程班级" min-width="160" show-overflow-tooltip>
                    <template #default="{ row }">{{ row.className || '-' }}</template>
                </el-table-column>
                <el-table-column label="请假时间" min-width="190">
                    <template #default="{ row }">{{ row.startTime }} 至 {{ row.endTime }}</template>
                </el-table-column>
                <el-table-column label="原因" min-width="100">
                    <template #default="{ row }">{{ row.reason || '-' }}</template>
                </el-table-column>
                <el-table-column label="详细说明" min-width="180" show-overflow-tooltip>
                    <template #default="{ row }">{{ row.detail || '-' }}</template>
                </el-table-column>
                <el-table-column label="状态" width="90" align="center">
                    <template #default="{ row }">
                        <el-tag :type="statusTagType(row.status)">{{ row.statusDesc }}</el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="审批人" min-width="90">
                    <template #default="{ row }">{{ row.auditName || '-' }}</template>
                </el-table-column>
                <el-table-column label="审批备注" min-width="140" show-overflow-tooltip>
                    <template #default="{ row }">{{ row.auditRemark || '-' }}</template>
                </el-table-column>
                <el-table-column label="提交时间" min-width="150">
                    <template #default="{ row }">{{ row.createTime || '-' }}</template>
                </el-table-column>
                <el-table-column label="操作" width="80" fixed="right">
                    <template #default="{ row }">
                        <el-button
                            v-if="row.status === 0"
                            v-perms="['leave/audit']"
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

        <el-dialog v-model="dialogVisible" title="家长请假审批" width="640px" @closed="resetForm">
            <el-descriptions :column="2" border>
                <el-descriptions-item label="申请人">{{ current.applicantName || '-' }}</el-descriptions-item>
                <el-descriptions-item label="课程班级">{{ current.className || '-' }}</el-descriptions-item>
                <el-descriptions-item label="开始时间">{{ current.startTime || '-' }}</el-descriptions-item>
                <el-descriptions-item label="结束时间">{{ current.endTime || '-' }}</el-descriptions-item>
                <el-descriptions-item label="原因">{{ current.reason || '-' }}</el-descriptions-item>
                <el-descriptions-item label="状态">{{ current.statusDesc || '-' }}</el-descriptions-item>
                <el-descriptions-item label="详细说明" :span="2">{{ current.detail || '-' }}</el-descriptions-item>
            </el-descriptions>

            <el-form ref="auditFormRef" class="mt-5" :model="form" :rules="rules" label-width="90px">
                <el-form-item label="审批结果" prop="status">
                    <el-radio-group v-model="form.status">
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
                <el-button type="primary" :loading="submitting" @click="submit">提交审批</el-button>
            </template>
        </el-dialog>
    </div>
</template>

<script lang="ts" setup name="parentLeaveAudit">
import { auditParentLeave, getParentLeaveList } from '@/api/leave'
import { classList } from '@/api/courseClass'
import { usePaging } from '@/hooks/usePaging'
import feedback from '@/utils/feedback'

const queryParams = reactive({
    applicantName: '',
    classId: '',
    status: ''
})

const { pager, getLists, resetPage, resetParams } = usePaging({
    fetchFun: getParentLeaveList,
    params: queryParams
})

const classOptions = ref<any[]>([])
const classLoading = ref(false)
const dialogVisible = ref(false)
const submitting = ref(false)
const current = ref<any>({})
const auditFormRef = ref()
const form = reactive({ id: 0, status: 1, auditRemark: '' })

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

function statusTagType(status: number) {
    return status === 1 ? 'success' : status === 2 ? 'danger' : 'warning'
}

async function searchClasses(keyword: string) {
    classLoading.value = true
    try {
        const data: any = await classList({ page_no: 1, page_size: 50, name: keyword || '' })
        classOptions.value = data.lists || []
    } catch (e) {
        classOptions.value = []
    } finally {
        classLoading.value = false
    }
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

async function submit() {
    if (submitting.value) return
    const valid = await auditFormRef.value?.validate().catch(() => false)
    if (!valid || submitting.value) return
    submitting.value = true
    try {
        await auditParentLeave({ id: form.id, status: form.status, auditRemark: form.auditRemark })
        feedback.msgSuccess('审批已提交')
        dialogVisible.value = false
        getLists()
    } catch {
        // 请求层已提示错误，保留弹窗便于重试
    } finally {
        submitting.value = false
    }
}

searchClasses('')
getLists()
</script>
