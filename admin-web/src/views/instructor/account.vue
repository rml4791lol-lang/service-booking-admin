<template>
    <popup
        ref="popupRef"
        title="教练账号"
        width="860px"
        :cancel-button-text="false"
        confirm-button-text="关闭"
        @close="handleClose"
    >
        <div v-loading="loading">
            <div class="mb-4 flex items-start justify-between gap-4">
                <div>
                    <div class="flex items-center gap-2">
                        <span class="text-lg font-semibold">{{ account.name || '-' }}</span>
                        <el-tag :type="statusTagType(account.accountStatus)" size="small">
                            {{ account.accountStatusLabel || '-' }}
                        </el-tag>
                    </div>
                    <div class="mt-1 text-sm text-gray-500">
                        登录手机号：{{ account.phone || '-' }}
                        <span class="ml-2 text-xs text-gray-400">教练 ID：{{ account.instructorId }}</span>
                    </div>
                </div>
                <div class="flex flex-wrap justify-end gap-2">
                    <el-button
                        v-if="canOpen"
                        v-perms="['instructor/account/open']"
                        type="primary"
                        :loading="acting"
                        @click="handleOpen"
                    >
                        开通账号
                    </el-button>
                    <el-button
                        v-if="canReset"
                        v-perms="['instructor/account/reset']"
                        type="warning"
                        :loading="acting"
                        @click="handleReset"
                    >
                        重置密码
                    </el-button>
                    <el-button
                        v-if="canDisable"
                        v-perms="['instructor/account/disable']"
                        type="danger"
                        :loading="acting"
                        @click="handleDisable"
                    >
                        停用账号
                    </el-button>
                    <el-button
                        v-if="canEnable"
                        v-perms="['instructor/account/enable']"
                        type="success"
                        :loading="acting"
                        @click="handleEnable"
                    >
                        启用账号
                    </el-button>
                </div>
            </div>

            <el-alert
                v-if="account.phone && String(account.phone).length !== 11"
                class="mb-4"
                type="warning"
                :closable="false"
                show-icon
                title="该教练手机号格式异常，登录仍按此号码字符串校验；请先在教练资料中修正手机号。"
            />

            <el-alert
                v-if="account.loginAllowed === false && account.loginBlockReason"
                class="mb-4"
                type="error"
                :closable="false"
                show-icon
                :title="`当前不可登录：${account.loginBlockReason}`"
            />
            <el-alert
                v-else-if="account.mustChangePassword"
                class="mb-4"
                type="warning"
                :closable="false"
                show-icon
                title="该账号处于首次登录待改密状态：教练用初始口令登录后，必须先修改密码才能使用教练端其他功能。"
            />

            <el-descriptions :column="3" border>
                <el-descriptions-item label="登录账号">
                    {{ account.account || '未绑定' }}
                </el-descriptions-item>
                <el-descriptions-item label="账号绑定">
                    {{ account.accountLinked ? '已绑定 la_user' : '未绑定' }}
                </el-descriptions-item>
                <el-descriptions-item label="登录密码">
                    {{ account.hasPassword ? '已设置' : '未设置' }}
                </el-descriptions-item>
                <el-descriptions-item label="资质状态">
                    {{ account.instructorStatusLabel || '-' }}
                </el-descriptions-item>
                <el-descriptions-item label="账号开通时间">
                    {{ account.accountOpenedTime || '-' }}
                </el-descriptions-item>
                <el-descriptions-item label="密码更新时间">
                    {{ account.passwordUpdatedTime || '-' }}
                </el-descriptions-item>
                <el-descriptions-item label="最后登录时间">
                    {{ account.lastLoginTime || '从未登录' }}
                </el-descriptions-item>
                <el-descriptions-item label="账号 ID">
                    {{ account.userId || '-' }}
                </el-descriptions-item>
                <el-descriptions-item label="登录限制">
                    {{ account.loginAllowed ? '允许登录' : (account.loginBlockReason || '不可登录') }}
                </el-descriptions-item>
            </el-descriptions>

            <div v-if="issuedPassword" class="mt-4">
                <el-alert
                    type="success"
                    :closable="false"
                    show-icon
                    title="初始口令只显示这一次，请立即通过安全渠道发给教练；关闭弹窗后无法再次查看。"
                />
                <div class="mt-2 flex items-center gap-2">
                    <el-input :model-value="issuedPassword" readonly class="flex-1">
                        <template #prepend>初始口令</template>
                    </el-input>
                    <el-button type="primary" plain @click="copyPassword">复制</el-button>
                </div>
            </div>

            <div class="mt-5">
                <div class="mb-2 flex items-center justify-between">
                    <span class="text-sm font-semibold">操作审计</span>
                    <el-select
                        v-model="auditAction"
                        class="w-[180px]"
                        placeholder="全部动作"
                        clearable
                        @change="loadAudits(1)"
                    >
                        <el-option label="开通账号" value="OPEN" />
                        <el-option label="重置密码" value="RESET" />
                        <el-option label="停用账号" value="DISABLE" />
                        <el-option label="启用账号" value="ENABLE" />
                        <el-option label="修改密码" value="CHANGE_PASSWORD" />
                    </el-select>
                </div>
                <el-table v-loading="auditLoading" :data="audits" size="small">
                    <el-table-column label="时间" prop="createTime" width="170" />
                    <el-table-column label="动作" width="100">
                        <template #default="{ row }">
                            <el-tag size="small">{{ row.actionLabel || row.action }}</el-tag>
                        </template>
                    </el-table-column>
                    <el-table-column label="操作人" width="130">
                        <template #default="{ row }">
                            {{ row.operatorName || '-' }}
                            <span class="text-xs text-gray-400">
                                {{ row.source === 'COACH' ? '(教练)' : '(后台)' }}
                            </span>
                        </template>
                    </el-table-column>
                    <el-table-column label="IP" prop="ip" width="130" />
                    <el-table-column label="说明" prop="detail" min-width="240" show-overflow-tooltip />
                </el-table>
                <div class="mt-3 flex justify-end">
                    <el-pagination
                        layout="total, prev, pager, next"
                        :total="auditTotal"
                        :page-size="auditPageSize"
                        :current-page="auditPage"
                        @current-change="loadAudits"
                    />
                </div>
            </div>

            <el-alert
                class="mt-4"
                type="info"
                :closable="false"
                show-icon
                title="审计记录只登记操作人、时间、动作与说明，不保存任何明文口令或口令摘要。"
            />
        </div>
    </popup>
</template>

<script lang="ts" setup name="instructorAccount">
import {
    disableInstructorAccount,
    enableInstructorAccount,
    getInstructorAccountAudits,
    getInstructorAccountStatus,
    openInstructorAccount,
    resetInstructorPassword
} from '@/api/instructor'
import Popup from '@/components/popup/index.vue'
import feedback from '@/utils/feedback'

const emit = defineEmits(['close', 'success'])
const popupRef = shallowRef<InstanceType<typeof Popup>>()
const loading = ref(false)
const acting = ref(false)
const account = ref<any>({})
const issuedPassword = ref('')
const audits = ref<any[]>([])
const auditLoading = ref(false)
const auditTotal = ref(0)
const auditPage = ref(1)
const auditPageSize = 10
const auditAction = ref('')
let currentId = 0

const canOpen = computed(
    () => account.value.instructorId && (!account.value.accountLinked || !account.value.hasPassword)
)
const canReset = computed(() => !!(account.value.accountLinked && account.value.hasPassword))
const canDisable = computed(() => account.value.instructorStatus !== 0)
const canEnable = computed(() => account.value.instructorStatus === 0)

function statusTagType(status: string) {
    if (status === 'ACTIVE') return 'success'
    if (status === 'PENDING_CHANGE') return 'warning'
    if (status === 'BLOCKED' || status === 'DISABLED') return 'danger'
    return 'info'
}

async function loadStatus() {
    if (!currentId) return
    const data: any = await getInstructorAccountStatus({ id: currentId })
    account.value = data || {}
}

async function loadAudits(page = 1) {
    if (!currentId) return
    auditPage.value = page
    auditLoading.value = true
    try {
        const data: any = await getInstructorAccountAudits({
            id: currentId,
            action: auditAction.value || '',
            page_no: page,
            page_size: auditPageSize
        })
        audits.value = Array.isArray(data?.lists) ? data.lists : []
        auditTotal.value = Number(data?.count) || 0
    } finally {
        auditLoading.value = false
    }
}

async function refresh() {
    loading.value = true
    try {
        await loadStatus()
        await loadAudits(1)
    } catch {
        account.value = {}
    } finally {
        loading.value = false
    }
}

async function perform(kind: 'open' | 'reset') {
    if (acting.value) return
    try {
        await feedback.confirm(
            kind === 'open'
                ? '确认开通该教练的登录账号？系统将生成一次性初始口令，首次登录必须改密。'
                : '确认重置该教练的登录密码？旧密码和已登录会话会立即失效。'
        )
    } catch {
        return
    }
    acting.value = true
    try {
        const data: any =
            kind === 'open'
                ? await openInstructorAccount({ id: currentId })
                : await resetInstructorPassword({ id: currentId })
        issuedPassword.value = data?.initialPassword || ''
        feedback.msgSuccess(kind === 'open' ? '账号已开通' : '密码已重置')
        await loadStatus()
        await loadAudits(1)
        emit('success')
    } finally {
        acting.value = false
    }
}

function handleOpen() {
    perform('open')
}

function handleReset() {
    perform('reset')
}

async function handleDisable() {
    if (acting.value) return
    let reason = ''
    try {
        const result: any = await feedback.prompt('停用后该账号将无法登录，已登录会话会立即失效。可填写停用原因：', '停用教练账号', {
            inputPlaceholder: '选填，最多 200 字',
            inputValidator: (value: string) => (!value || value.length <= 200 ? true : '不能超过 200 字')
        })
        reason = result?.value || ''
    } catch {
        return
    }
    acting.value = true
    try {
        await disableInstructorAccount({ id: currentId, reason })
        feedback.msgSuccess('账号已停用')
        await loadStatus()
        await loadAudits(1)
        emit('success')
    } finally {
        acting.value = false
    }
}

async function handleEnable() {
    if (acting.value) return
    try {
        await feedback.confirm('确认启用该教练的登录账号？')
    } catch {
        return
    }
    acting.value = true
    try {
        await enableInstructorAccount({ id: currentId })
        feedback.msgSuccess('账号已启用')
        await loadStatus()
        await loadAudits(1)
        emit('success')
    } finally {
        acting.value = false
    }
}

async function copyPassword() {
    try {
        await navigator.clipboard.writeText(issuedPassword.value)
        feedback.msgSuccess('已复制')
    } catch {
        feedback.msgError('复制失败，请手动选中复制')
    }
}

function handleClose() {
    issuedPassword.value = ''
    audits.value = []
    account.value = {}
    currentId = 0
    emit('close')
}

const open = (row: any) => {
    if (!row?.id) return
    currentId = row.id
    issuedPassword.value = ''
    auditAction.value = ''
    popupRef.value?.open()
    refresh()
}

defineExpose({ open })
</script>
