<template>
    <el-dialog
        v-model="visible"
        title="批量调余额"
        width="1000px"
        top="6vh"
        :close-on-click-modal="false"
        @closed="resetAll"
    >
        <!-- 第一步：上传 Excel/CSV -->
        <div v-if="step === 'upload'">
            <el-alert
                title="支持 Excel（.xlsx/.xls）或 CSV，中文表头需包含 手机号、金额、原因；金额正数增加、负数扣减，单次最多 500 行。"
                type="info"
                :closable="false"
                show-icon
                class="mb-4"
            />
            <div class="rounded border border-dashed border-gray-300 px-6 py-10 text-center">
                <input
                    ref="fileInputRef"
                    type="file"
                    accept=".xlsx,.xls,.csv,text/csv"
                    class="hidden"
                    @change="handleFileChange"
                >
                <el-button type="primary" @click="fileInputRef?.click()">选择 Excel/CSV 文件</el-button>
                <el-button class="ml-2" @click="downloadTemplate">下载模板</el-button>
                <div class="mt-3 text-sm text-gray-500">
                    表头示例：手机号、金额（正数增加/负数扣减）、原因
                </div>
                <div class="mt-1 text-xs text-gray-400">
                    Excel 直接上传；CSV 请用 UTF-8 编码；扣减不能把余额扣成负数。
                </div>
            </div>
            <el-alert
                v-if="parseError"
                class="mt-4"
                :title="parseError"
                type="error"
                :closable="false"
                show-icon
            />
        </div>

        <!-- 第二步：预览校验 -->
        <div v-else-if="step === 'preview'" v-loading="validating">
            <div class="mb-3 flex items-center justify-between text-sm">
                <div class="text-gray-500">
                    共 {{ previewRows.length }} 行；逐行校验账号/金额/备注/扣减后余额。
                    <span v-if="validating" class="ml-2 text-primary">
                        校验中 {{ checkedCount }}/{{ pendingMobileCount }} 个手机号…
                    </span>
                </div>
                <el-button @click="step = 'upload'">重新选择文件</el-button>
            </div>
            <el-table
                size="small"
                border
                max-height="460"
                :data="previewRows"
                :row-class-name="previewRowClass"
            >
                <el-table-column label="行号" prop="rowNo" width="60" align="center" />
                <el-table-column label="手机号" min-width="130">
                    <template #default="{ row }">
                        <span :class="{ 'text-red-500': !row.mobileOk }">{{ row.mobile || '-' }}</span>
                        <div v-if="row.accountLabel" class="text-xs text-gray-400">
                            {{ row.accountLabel }}
                        </div>
                    </template>
                </el-table-column>
                <el-table-column label="调整金额" width="115" align="right">
                    <template #default="{ row }">
                        <span
                            :class="{
                                'text-red-500': !row.amountOk,
                                'text-green-600': row.amountOk && row.amount > 0,
                                'text-orange-500': row.amountOk && row.amount < 0
                            }"
                        >
                            {{ row.amountOk ? formatAmount(row.amount) : row.amountText || '-' }}
                        </span>
                    </template>
                </el-table-column>
                <el-table-column label="备注" prop="remark" min-width="150" show-overflow-tooltip />
                <el-table-column label="当前余额" width="110" align="right">
                    <template #default="{ row }">
                        {{ row.currentBalance === null ? '-' : `¥${row.currentBalance.toFixed(2)}` }}
                    </template>
                </el-table-column>
                <el-table-column label="调整后余额" width="120" align="right">
                    <template #default="{ row }">
                        <span :class="{ 'text-red-500': !row.balanceOk }">
                            {{
                                row.projectedBalance === null
                                    ? '-'
                                    : `¥${row.projectedBalance.toFixed(2)}`
                            }}
                        </span>
                    </template>
                </el-table-column>
                <el-table-column label="校验结果" min-width="180">
                    <template #default="{ row }">
                        <el-tag
                            v-for="issue in rowIssues(row)"
                            :key="issue"
                            type="danger"
                            size="small"
                            effect="plain"
                            class="mr-1 mb-1"
                        >
                            {{ issue }}
                        </el-tag>
                        <el-tag v-if="!rowIssues(row).length" type="success" size="small" effect="plain">
                            可调整
                        </el-tag>
                    </template>
                </el-table-column>
            </el-table>
        </div>

        <!-- 第三步：结果报告 -->
        <div v-else>
            <el-row :gutter="12" class="mb-4">
                <el-col :span="8">
                    <div class="rounded bg-gray-50 px-4 py-3">
                        <div class="text-xs text-gray-500">提交行数</div>
                        <div class="text-xl font-medium">{{ result?.total || 0 }}</div>
                    </div>
                </el-col>
                <el-col :span="8">
                    <div class="rounded bg-green-50 px-4 py-3">
                        <div class="text-xs text-gray-500">成功</div>
                        <div class="text-xl font-medium text-green-600">{{ result?.success || 0 }}</div>
                    </div>
                </el-col>
                <el-col :span="8">
                    <div class="rounded bg-red-50 px-4 py-3">
                        <div class="text-xs text-gray-500">失败</div>
                        <div class="text-xl font-medium text-red-500">{{ result?.failed || 0 }}</div>
                    </div>
                </el-col>
            </el-row>
            <el-table
                v-if="result?.errors?.length"
                size="small"
                border
                max-height="360"
                :data="result.errors"
            >
                <el-table-column label="行号" prop="row" width="70" align="center" />
                <el-table-column label="手机号" prop="mobile" width="130" />
                <el-table-column label="失败原因" prop="reason" min-width="300" show-overflow-tooltip />
            </el-table>
            <el-empty v-else description="全部调整成功" :image-size="80" />
            <div class="mt-4 flex justify-end">
                <el-button :disabled="!result?.errors?.length" @click="downloadFailures">
                    下载失败行 CSV
                </el-button>
            </div>
        </div>

        <template #footer>
            <template v-if="step === 'preview'">
                <el-button @click="step = 'upload'">上一步</el-button>
                <el-button type="primary" :loading="submitting" @click="submit">
                    确认提交（{{ previewRows.length }} 行）
                </el-button>
            </template>
            <template v-else-if="step === 'result'">
                <el-button @click="resetAll">继续导入</el-button>
                <el-button type="primary" @click="close">完成</el-button>
            </template>
            <el-button v-else @click="close">取消</el-button>
        </template>
    </el-dialog>
</template>

<script lang="ts" setup name="consumerBatchAdjust">
import { adjustMoneyBatch, getUserList } from '@/api/consumer'
import feedback from '@/utils/feedback'
import { downloadCsv, runWithConcurrency } from '@/utils/csv'
import { parseWalletSheet } from '@/utils/importSheet'

const props = defineProps({
    modelValue: {
        type: Boolean,
        default: false
    }
})
const emit = defineEmits<{
    (event: 'update:modelValue', value: boolean): void
    (event: 'success'): void
}>()

const visible = computed({
    get: () => props.modelValue,
    set: (value: boolean) => emit('update:modelValue', value)
})

type AccountState = 'idle' | 'checking' | 'found' | 'missing' | 'unknown'

interface WalletRow {
    rowNo: number
    mobile: string
    mobileOk: boolean
    amountText: string
    amount: number
    amountOk: boolean
    remark: string
    remarkOk: boolean
    accountState: AccountState
    accountLabel: string
    currentBalance: number | null
    projectedBalance: number | null
    balanceOk: boolean
}

interface BatchError {
    row: number
    mobile: string
    reason: string
}

interface BatchResult {
    total: number
    success: number
    failed: number
    errors: BatchError[]
}

const MOBILE_PATTERN = /^1\d{10}$/
const AMOUNT_PATTERN = /^[+-]?\d+(\.\d{1,2})?$/
const MAX_AMOUNT = 10000000000
const MAX_ROWS = 500

const step = ref<'upload' | 'preview' | 'result'>('upload')
const parseError = ref('')
const previewRows = ref<WalletRow[]>([])
const fileInputRef = ref<HTMLInputElement>()
const checking = ref(false)
const checkedCount = ref(0)
const pendingMobileCount = ref(0)
const submitting = ref(false)
const result = ref<BatchResult | null>(null)

const validating = computed(() => checking.value)

function rowIssues(row: WalletRow): string[] {
    const issues: string[] = []
    if (!row.mobileOk) issues.push('手机号格式不正确')
    if (row.accountState === 'checking') issues.push('校验中')
    if (row.accountState === 'missing') issues.push('手机号未注册')
    if (!row.amountOk) issues.push('金额需为非 0 数字，最多两位小数')
    if (!row.remarkOk) issues.push('备注必填')
    if (!row.balanceOk) issues.push('余额不足')
    return issues
}

function previewRowClass({ row }: { row: WalletRow }) {
    return rowIssues(row).length ? 'batch-row-invalid' : ''
}

function formatAmount(amount: number) {
    const sign = amount > 0 ? '+' : ''
    return `${sign}${amount.toFixed(2)}`
}

function downloadTemplate() {
    const link = document.createElement('a')
    link.href = '/templates/批量调余额模板.xlsx'
    link.download = '批量调余额模板.xlsx'
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
}

async function handleFileChange(event: Event) {
    const input = event.target as HTMLInputElement
    const file = input.files?.[0]
    input.value = ''
    if (!file) return
    if (file.size > 4 * 1024 * 1024) {
        parseError.value = '文件超过 4MB，请拆分后再导入'
        return
    }
    await parseFile(await file.arrayBuffer(), file.name)
}

async function parseFile(data: ArrayBuffer, filename: string) {
    parseError.value = ''
    let table: ReturnType<typeof parseWalletSheet>
    try {
        table = parseWalletSheet(data, filename)
    } catch (error) {
        parseError.value = error instanceof Error ? error.message : '文件解析失败，请检查格式'
        return
    }
    if (!table.headers.length) {
        parseError.value = '文件为空，请检查内容'
        return
    }
    if (table.missing.length) {
        parseError.value = `表头缺少 ${table.missing.join('、')}；需包含 手机号、金额、原因`
        return
    }
    if (!table.rows.length) {
        parseError.value = '文件没有数据行'
        return
    }
    if (table.rows.length > MAX_ROWS) {
        parseError.value = `单次最多导入 ${MAX_ROWS} 行，当前 ${table.rows.length} 行`
        return
    }

    previewRows.value = table.rows.map((cells, index) => {
        const mobile = cells.mobile
        const amountText = cells.amount
        const remark = cells.remark
        const amount = Number(amountText)
        const amountOk =
            AMOUNT_PATTERN.test(amountText) &&
            Number.isFinite(amount) &&
            amount !== 0 &&
            Math.abs(amount) < MAX_AMOUNT
        return {
            rowNo: index + 1,
            mobile,
            mobileOk: MOBILE_PATTERN.test(mobile),
            amountText,
            amount,
            amountOk,
            remark,
            remarkOk: remark !== '',
            accountState: 'idle',
            accountLabel: '',
            currentBalance: null,
            projectedBalance: null,
            balanceOk: true
        }
    })

    step.value = 'preview'
    await resolveAccounts()
    calculateProjectedBalances()
}

async function resolveAccounts() {
    const mobiles = Array.from(
        new Set(previewRows.value.filter((row) => row.mobileOk).map((row) => row.mobile))
    )
    if (!mobiles.length) return
    checking.value = true
    checkedCount.value = 0
    pendingMobileCount.value = mobiles.length
    await runWithConcurrency(mobiles, 4, async (mobile) => {
        const targets = previewRows.value.filter((row) => row.mobile === mobile)
        targets.forEach((row) => {
            row.accountState = 'checking'
        })
        try {
            const data: any = await getUserList({ mobile, page_no: 1, page_size: 20 })
            const list: any[] = data?.lists || []
            const account = list.find((item: any) => String(item.mobile || '') === mobile)
            targets.forEach((row) => {
                row.accountState = account ? 'found' : 'missing'
                row.accountLabel = account
                    ? account.nickname || account.realName || account.account || `#${account.id}`
                    : ''
                row.currentBalance = account ? Number(account.userMoney || 0) : null
            })
        } catch {
            // 查询失败不判死：提交后以后端结果为准
            targets.forEach((row) => {
                row.accountState = 'unknown'
            })
        } finally {
            checkedCount.value += 1
        }
    })
    checking.value = false
}

/**
 * 按行顺序推算「调整后余额」：同一手机号多行时累计计算，
 * 只有账号存在、金额/备注合法且未扣成负数的行才计入推算。
 */
function calculateProjectedBalances() {
    const projected = new Map<string, number>()
    previewRows.value.forEach((row) => {
        row.projectedBalance = null
        row.balanceOk = true
        if (
            !row.mobileOk ||
            row.accountState !== 'found' ||
            !row.amountOk ||
            !row.remarkOk ||
            row.currentBalance === null
        ) {
            return
        }
        const base = projected.has(row.mobile)
            ? Number(projected.get(row.mobile))
            : row.currentBalance
        const next = Math.round((base + row.amount) * 100) / 100
        row.projectedBalance = next
        if (next < 0) {
            row.balanceOk = false
            return
        }
        projected.set(row.mobile, next)
    })
}

async function submit() {
    if (submitting.value) return
    const problemRows = previewRows.value.filter((row) => rowIssues(row).length)
    if (problemRows.length) {
        try {
            await feedback.confirm(
                `有 ${problemRows.length} 行存在问题，是否仍要提交？失败行会列在结果中，可用失败行 CSV 修正后重导。`
            )
        } catch {
            return
        }
    }
    submitting.value = true
    try {
        const payload = previewRows.value.map((row) => ({
            mobile: row.mobile,
            amount: Number.isFinite(row.amount) ? row.amount : 0,
            remark: row.remark
        }))
        const data: any = await adjustMoneyBatch(payload)
        result.value = {
            total: Number(data?.total || 0),
            success: Number(data?.success || 0),
            failed: Number(data?.failed || 0),
            errors: Array.isArray(data?.errors) ? data.errors : []
        }
        step.value = 'result'
        emit('success')
    } catch {
        // 请求层已提示（超过 500 行/无权限等），保留预览便于修正
    } finally {
        submitting.value = false
    }
}

function downloadFailures() {
    const errors = result.value?.errors || []
    if (!errors.length) return
    const rowMap = new Map(previewRows.value.map((row) => [row.rowNo, row]))
    const lines = errors.map((error) => {
        const row = rowMap.get(Number(error.row))
        return [
            row?.mobile ?? error.mobile ?? '',
            row?.amountText ?? '',
            row?.remark ?? '',
            error.reason || ''
        ]
    })
    downloadCsv(`批量调余额失败行-${Date.now()}.csv`, [
        ['手机号', '金额', '原因', '失败原因'],
        ...lines
    ])
}

function resetAll() {
    step.value = 'upload'
    parseError.value = ''
    previewRows.value = []
    checkedCount.value = 0
    pendingMobileCount.value = 0
    result.value = null
    submitting.value = false
    checking.value = false
}

function close() {
    visible.value = false
}
</script>

<style lang="scss" scoped>
:deep(.batch-row-invalid) {
    background-color: #fef0f0;
}
</style>
