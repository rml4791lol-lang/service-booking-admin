<template>
    <div class="instructor-cert-page">
        <el-card class="!border-none" shadow="never">
            <div class="flex items-center justify-between">
                <div>
                    <div class="text-lg font-medium">教练资质审核</div>
                    <div class="mt-1 text-sm text-gray-400">
                        教练提交的证书需运营审核，驳回时必须填写原因；通过后对外展示。
                    </div>
                </div>
                <div class="text-sm text-gray-400">共 {{ pager.count }} 条资质</div>
            </div>
        </el-card>

        <el-card class="!border-none mt-4" shadow="never">
            <el-form ref="formRef" class="mb-[-16px]" :model="queryParams" :inline="true">
                <el-form-item label="教练">
                    <el-select
                        v-model="queryParams.instructorId"
                        class="w-[220px]"
                        placeholder="全部教练"
                        filterable
                        clearable
                    >
                        <el-option
                            v-for="item in instructorOptions"
                            :key="item.id"
                            :label="`${item.name || '未命名'}（${item.phone || '无手机号'}）`"
                            :value="item.id"
                        />
                    </el-select>
                </el-form-item>
                <el-form-item label="证书类型">
                    <el-select v-model="queryParams.type" class="w-[170px]" placeholder="全部类型" clearable>
                        <el-option v-for="item in typeOptions" :key="item" :label="item" :value="item" />
                    </el-select>
                </el-form-item>
                <el-form-item label="审核状态">
                    <el-select v-model="queryParams.status" class="w-[150px]" placeholder="全部状态" clearable>
                        <el-option label="草稿" :value="0" />
                        <el-option label="待审核" :value="1" />
                        <el-option label="已通过" :value="2" />
                        <el-option label="已驳回" :value="3" />
                    </el-select>
                </el-form-item>
                <el-form-item label="关键词">
                    <el-input
                        v-model="queryParams.keyword"
                        class="w-[220px]"
                        placeholder="证书名称/编号/机构"
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
            <el-table v-loading="pager.loading" :data="pager.lists" size="large">
                <el-table-column label="ID" prop="id" width="70" />
                <el-table-column label="教练" min-width="130">
                    <template #default="{ row }">
                        <div class="font-medium">{{ row.instructorName || '-' }}</div>
                        <div class="text-xs text-gray-400">{{ row.instructorPhone || '' }}</div>
                    </template>
                </el-table-column>
                <el-table-column label="证书" min-width="210">
                    <template #default="{ row }">
                        <div class="font-medium">{{ row.name || '-' }}</div>
                        <div class="text-xs text-gray-400">{{ row.type || '-' }}</div>
                    </template>
                </el-table-column>
                <el-table-column label="证书编号" prop="no" min-width="120" show-tooltip-when-overflow>
                    <template #default="{ row }">{{ row.no || '-' }}</template>
                </el-table-column>
                <el-table-column label="发证机构" prop="org" min-width="150" show-tooltip-when-overflow>
                    <template #default="{ row }">{{ row.org || '-' }}</template>
                </el-table-column>
                <el-table-column label="有效期" min-width="190">
                    <template #default="{ row }">
                        {{ row.expireStart || '-' }} 至 {{ row.expire || '-' }}
                    </template>
                </el-table-column>
                <el-table-column label="证书照片" width="90">
                    <template #default="{ row }">
                        <el-image
                            v-if="row.front"
                            :src="row.front"
                            :preview-src-list="[row.front]"
                            preview-teleported
                            fit="cover"
                            class="h-12 w-16 rounded"
                        />
                        <span v-else class="text-gray-400">-</span>
                    </template>
                </el-table-column>
                <el-table-column label="状态" width="90">
                    <template #default="{ row }">
                        <el-tag :type="statusTagType(row.status)">{{ row.statusLabel }}</el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="提交时间" min-width="160">
                    <template #default="{ row }">{{ row.submitTime || '-' }}</template>
                </el-table-column>
                <el-table-column label="审核" min-width="160">
                    <template #default="{ row }">
                        <template v-if="row.auditTime">
                            <div>{{ row.auditAdminName || '-' }}</div>
                            <div class="text-xs text-gray-400">{{ row.auditTime }}</div>
                        </template>
                        <span v-else class="text-gray-400">-</span>
                    </template>
                </el-table-column>
                <el-table-column label="操作" width="190" fixed="right">
                    <template #default="{ row }">
                        <el-button
                            v-perms="['instructor/cert/detail']"
                            type="primary"
                            link
                            @click="openDetail(row)"
                        >
                            详情
                        </el-button>
                        <template v-if="row.canAudit">
                            <el-button
                                v-perms="['instructor/cert/audit']"
                                type="success"
                                link
                                @click="openAudit(row, 'approve')"
                            >
                                通过
                            </el-button>
                            <el-button
                                v-perms="['instructor/cert/audit']"
                                type="danger"
                                link
                                @click="openAudit(row, 'reject')"
                            >
                                驳回
                            </el-button>
                        </template>
                    </template>
                </el-table-column>
            </el-table>
            <div class="mt-4 flex justify-end">
                <pagination v-model="pager" @change="getLists" />
            </div>
        </el-card>

        <el-dialog v-model="auditVisible" :title="auditForm.action === 'reject' ? '驳回资质' : '通过资质'" width="560px" @closed="resetAudit">
            <el-descriptions :column="1" border>
                <el-descriptions-item label="教练">
                    {{ auditCurrent.instructorName || '-' }}（{{ auditCurrent.instructorPhone || '-' }}）
                </el-descriptions-item>
                <el-descriptions-item label="证书">
                    {{ auditCurrent.name || '-' }} / {{ auditCurrent.type || '-' }}
                </el-descriptions-item>
                <el-descriptions-item label="编号 / 机构">
                    {{ auditCurrent.no || '-' }} / {{ auditCurrent.org || '-' }}
                </el-descriptions-item>
                <el-descriptions-item label="有效期">
                    {{ auditCurrent.expireStart || '-' }} 至 {{ auditCurrent.expire || '-' }}
                </el-descriptions-item>
            </el-descriptions>

            <el-form ref="auditFormRef" class="mt-4" :model="auditForm" :rules="auditRules" label-width="86px">
                <el-form-item label="审核结果" prop="action">
                    <el-radio-group v-model="auditForm.action">
                        <el-radio label="approve">通过</el-radio>
                        <el-radio label="reject">驳回</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="审核意见" prop="remark">
                    <el-input
                        v-model="auditForm.remark"
                        type="textarea"
                        :rows="4"
                        maxlength="500"
                        show-word-limit
                        :placeholder="auditForm.action === 'reject' ? '请填写驳回原因（必填）' : '可填写审核备注'"
                    />
                </el-form-item>
            </el-form>

            <template #footer>
                <el-button @click="auditVisible = false">取消</el-button>
                <el-button type="primary" :loading="auditSubmitting" @click="submitAudit">提交审核</el-button>
            </template>
        </el-dialog>

        <el-dialog v-model="detailVisible" title="资质详情与审核留痕" width="820px">
            <div v-loading="detailLoading">
                <el-descriptions :column="2" border>
                    <el-descriptions-item label="教练">
                        {{ detail.cert?.instructorName || '-' }}
                    </el-descriptions-item>
                    <el-descriptions-item label="手机号">
                        {{ detail.cert?.instructorPhone || '-' }}
                    </el-descriptions-item>
                    <el-descriptions-item label="证书类型">
                        {{ detail.cert?.type || '-' }}
                    </el-descriptions-item>
                    <el-descriptions-item label="证书名称">
                        {{ detail.cert?.name || '-' }}
                    </el-descriptions-item>
                    <el-descriptions-item label="证书编号">
                        {{ detail.cert?.no || '-' }}
                    </el-descriptions-item>
                    <el-descriptions-item label="发证机构">
                        {{ detail.cert?.org || '-' }}
                    </el-descriptions-item>
                    <el-descriptions-item label="有效期">
                        {{ detail.cert?.expireStart || '-' }} 至 {{ detail.cert?.expire || '-' }}
                    </el-descriptions-item>
                    <el-descriptions-item label="状态">
                        <el-tag :type="statusTagType(detail.cert?.status)">
                            {{ detail.cert?.statusLabel || '-' }}
                        </el-tag>
                    </el-descriptions-item>
                    <el-descriptions-item label="提交时间">
                        {{ detail.cert?.submitTime || '-' }}
                    </el-descriptions-item>
                    <el-descriptions-item label="审核人 / 时间">
                        {{ detail.cert?.auditAdminName || '-' }} / {{ detail.cert?.auditTime || '-' }}
                    </el-descriptions-item>
                    <el-descriptions-item label="审核意见" :span="2">
                        {{ detail.cert?.auditRemark || '-' }}
                    </el-descriptions-item>
                    <el-descriptions-item label="证书照片" :span="2">
                        <el-image
                            v-if="detail.cert?.front"
                            :src="detail.cert.front"
                            :preview-src-list="[detail.cert.front]"
                            preview-teleported
                            fit="contain"
                            class="h-40 w-64 rounded"
                        />
                        <span v-else class="text-gray-400">未上传</span>
                    </el-descriptions-item>
                </el-descriptions>

                <div class="mb-2 mt-5 text-sm font-semibold">审核留痕</div>
                <el-table :data="detail.audits" size="small">
                    <el-table-column label="时间" prop="createTime" width="170" />
                    <el-table-column label="动作" width="110">
                        <template #default="{ row }">
                            <el-tag size="small" :type="row.action === 'REJECT' ? 'danger' : row.action === 'APPROVE' ? 'success' : 'info'">
                                {{ row.actionLabel || row.action }}
                            </el-tag>
                        </template>
                    </el-table-column>
                    <el-table-column label="来源" width="100">
                        <template #default="{ row }">{{ row.sourceLabel || row.source }}</template>
                    </el-table-column>
                    <el-table-column label="操作人" width="120">
                        <template #default="{ row }">{{ row.operatorName || '-' }}</template>
                    </el-table-column>
                    <el-table-column label="意见" prop="remark" min-width="180" show-tooltip-when-overflow>
                        <template #default="{ row }">{{ row.remark || '-' }}</template>
                    </el-table-column>
                </el-table>
            </div>
        </el-dialog>
    </div>
</template>

<script lang="ts" setup name="instructorCertReview">
import {
    auditInstructorCert,
    getInstructorAll,
    getInstructorCertDetail,
    getInstructorCertList
} from '@/api/instructor'
import { usePaging } from '@/hooks/usePaging'
import feedback from '@/utils/feedback'
import { nextTick, reactive, ref } from 'vue'

const typeOptions = ['教练员证书', '教师资格证', '健康证明', '其他资质']

const queryParams = reactive({
    instructorId: '',
    type: '',
    status: '',
    keyword: ''
})

const { pager, getLists, resetPage, resetParams } = usePaging({
    fetchFun: getInstructorCertList,
    params: queryParams
})

const instructorOptions = ref<any[]>([])

async function loadInstructors() {
    try {
        const data: any = await getInstructorAll()
        instructorOptions.value = Array.isArray(data) ? data : []
    } catch {
        instructorOptions.value = []
    }
}

function statusTagType(status: number) {
    switch (status) {
        case 1:
            return 'warning'
        case 2:
            return 'success'
        case 3:
            return 'danger'
        default:
            return 'info'
    }
}

const auditVisible = ref(false)
const auditSubmitting = ref(false)
const auditFormRef = ref()
const auditCurrent = ref<any>({})
const auditForm = reactive({
    id: 0,
    action: 'approve',
    remark: ''
})

const auditRules = {
    action: [{ required: true, message: '请选择审核结果', trigger: 'change' }],
    remark: [
        {
            validator: (_rule: any, value: string, callback: any) => {
                if (auditForm.action === 'reject' && !String(value || '').trim()) {
                    callback(new Error('驳回时必须填写驳回原因'))
                    return
                }
                callback()
            },
            trigger: 'blur'
        }
    ]
}

function openAudit(row: any, action: 'approve' | 'reject') {
    auditCurrent.value = row
    auditForm.id = row.id
    auditForm.action = action
    auditForm.remark = ''
    auditVisible.value = true
    nextTick(() => auditFormRef.value?.clearValidate())
}

function resetAudit() {
    auditForm.id = 0
    auditForm.action = 'approve'
    auditForm.remark = ''
    auditFormRef.value?.clearValidate()
}

async function submitAudit() {
    const valid = await auditFormRef.value?.validate().catch(() => false)
    if (!valid || auditSubmitting.value) return
    auditSubmitting.value = true
    try {
        await auditInstructorCert({
            id: auditForm.id,
            action: auditForm.action,
            remark: String(auditForm.remark || '').trim()
        })
        auditVisible.value = false
        feedback.msgSuccess(auditForm.action === 'reject' ? '已驳回' : '已通过')
        await getLists()
    } catch {
        // 请求层已提示错误
    } finally {
        auditSubmitting.value = false
    }
}

const detailVisible = ref(false)
const detailLoading = ref(false)
const detail = ref<any>({ cert: null, audits: [] })

async function openDetail(row: any) {
    detailVisible.value = true
    detailLoading.value = true
    detail.value = { cert: row, audits: [] }
    try {
        const data: any = await getInstructorCertDetail({ id: row.id })
        detail.value = data || { cert: row, audits: [] }
    } catch {
        // 请求层已提示错误
    } finally {
        detailLoading.value = false
    }
}

onActivated(() => {
    getLists()
})
loadInstructors()
getLists()
</script>
