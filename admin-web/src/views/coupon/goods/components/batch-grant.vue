<template>
    <el-dialog
        v-model="visible"
        title="批量发券"
        width="1020px"
        top="6vh"
        :close-on-click-modal="false"
        @closed="resetAll"
    >
        <!-- 第一步：上传 CSV -->
        <div v-if="step === 'upload'">
            <el-alert
                title="CSV 表头需包含 mobile、goodsId、quantity；studentId、remark 可选。单次最多 500 行。"
                type="info"
                :closable="false"
                show-icon
                class="mb-4"
            />
            <div class="rounded border border-dashed border-gray-300 px-6 py-10 text-center">
                <input
                    ref="fileInputRef"
                    type="file"
                    accept=".csv,text/csv"
                    class="hidden"
                    @change="handleFileChange"
                >
                <el-button type="primary" @click="fileInputRef?.click()">选择 CSV 文件</el-button>
                <el-button class="ml-2" @click="downloadTemplate">下载模板</el-button>
                <div class="mt-3 text-sm text-gray-500">
                    表头示例：mobile,goodsId,quantity,studentId,remark
                </div>
                <div class="mt-1 text-xs text-gray-400">
                    文件请用 UTF-8 编码；学员 ID 留空表示账号级券。
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
                    共 {{ previewRows.length }} 行；已提交前会逐行校验券商品/手机号/张数。
                    <span v-if="validating" class="ml-2 text-primary">
                        校验中 {{ checkedCount }}/{{ pendingMobileCount }} 个手机号…
                    </span>
                </div>
                <div>
                    <el-button @click="step = 'upload'">重新选择文件</el-button>
                </div>
            </div>
            <el-table
                size="small"
                border
                max-height="460"
                :data="previewRows"
                :row-class-name="previewRowClass"
            >
                <el-table-column label="行号" prop="rowNo" width="60" align="center" />
                <el-table-column label="手机号" min-width="125">
                    <template #default="{ row }">
                        <span :class="{ 'text-red-500': !row.mobileOk }">{{ row.mobile || '-' }}</span>
                        <div v-if="row.accountLabel" class="text-xs text-gray-400">
                            {{ row.accountLabel }}
                        </div>
                    </template>
                </el-table-column>
                <el-table-column label="券商品" min-width="180">
                    <template #default="{ row }">
                        <span :class="{ 'text-red-500': !row.goodsOk }">
                            {{ row.goodsTitle || `#${row.goodsIdText || '?'}` }}
                        </span>
                        <span class="ml-1 text-xs text-gray-400">ID {{ row.goodsIdText || '-' }}</span>
                    </template>
                </el-table-column>
                <el-table-column label="学员ID" prop="studentIdText" width="85" align="center">
                    <template #default="{ row }">{{ row.studentIdText || '账号级' }}</template>
                </el-table-column>
                <el-table-column label="张数" width="80" align="center">
                    <template #default="{ row }">
                        <span :class="{ 'text-red-500': !row.quantityOk }">
                            {{ row.quantityText || '-' }}
                        </span>
                    </template>
                </el-table-column>
                <el-table-column label="备注" prop="remark" min-width="150" show-overflow-tooltip />
                <el-table-column label="校验结果" min-width="190">
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
                            可发放
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
            <el-empty v-else description="全部发放成功" :image-size="80" />
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

<script lang="ts" setup name="couponGoodsBatchGrant">
import { getCouponGoodsList, grantCouponBatch } from '@/api/couponGoods'
import { getCouponStudents } from '@/api/coupon'
import feedback from '@/utils/feedback'
import { downloadCsv, parseCsvTable, runWithConcurrency } from '@/utils/csv'

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

interface GrantRow {
    rowNo: number
    mobile: string
    mobileOk: boolean
    goodsIdText: string
    goodsId: number
    goodsTitle: string
    goodsOk: boolean
    studentIdText: string
    studentId: number
    quantityText: string
    quantity: number
    quantityOk: boolean
    remark: string
    accountState: AccountState
    accountLabel: string
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
const MAX_ROWS = 500

const step = ref<'upload' | 'preview' | 'result'>('upload')
const parseError = ref('')
const previewRows = ref<GrantRow[]>([])
const fileInputRef = ref<HTMLInputElement>()
const goodsMap = ref<Map<number, any>>(new Map())
const goodsLoaded = ref(false)
const checking = ref(false)
const checkedCount = ref(0)
const pendingMobileCount = ref(0)
const submitting = ref(false)
const result = ref<BatchResult | null>(null)

const validating = computed(() => checking.value)

function rowIssues(row: GrantRow): string[] {
    const issues: string[] = []
    if (!row.mobileOk) issues.push('手机号格式不正确')
    if (!row.goodsOk) issues.push('券商品不存在')
    if (!row.quantityOk) issues.push('张数需为 1-99')
    if (row.accountState === 'missing') issues.push('手机号未注册')
    if (row.accountState === 'checking') issues.push('校验中')
    return issues
}

function previewRowClass({ row }: { row: GrantRow }) {
    return rowIssues(row).length ? 'batch-row-invalid' : ''
}

function downloadTemplate() {
    downloadCsv('批量发券模板.csv', [
        ['mobile', 'goodsId', 'quantity', 'studentId', 'remark'],
        ['13800000000', '1', '1', '', '老课包转化']
    ])
}

async function handleFileChange(event: Event) {
    const input = event.target as HTMLInputElement
    const file = input.files?.[0]
    // 清空 value，允许再次选择同一个文件
    input.value = ''
    if (!file) return
    if (file.size > 4 * 1024 * 1024) {
        parseError.value = '文件超过 4MB，请拆分后再导入'
        return
    }
    const text = await file.text()
    await parseFile(text)
}

async function parseFile(text: string) {
    parseError.value = ''
    const table = parseCsvTable(text)
    if (!table.headers.length) {
        parseError.value = 'CSV 为空，请检查文件内容'
        return
    }
    const required = ['mobile', 'goodsid', 'quantity']
    const missing = required.filter((name) => !table.headers.includes(name))
    if (missing.length) {
        parseError.value = `CSV 表头缺少 ${missing.join('、')}；需包含 mobile、goodsId、quantity（studentId、remark 可选）`
        return
    }
    if (!table.rows.length) {
        parseError.value = 'CSV 没有数据行'
        return
    }
    if (table.rows.length > MAX_ROWS) {
        parseError.value = `单次最多导入 ${MAX_ROWS} 行，当前 ${table.rows.length} 行`
        return
    }

    const column = (name: string) => table.headers.indexOf(name)
    const mobileIndex = column('mobile')
    const goodsIndex = column('goodsid')
    const quantityIndex = column('quantity')
    const studentIndex = column('studentid')
    const remarkIndex = column('remark')

    previewRows.value = table.rows.map((cells, index) => {
        const mobile = String(cells[mobileIndex] || '').trim()
        const goodsIdText = String(cells[goodsIndex] || '').trim()
        const quantityText = String(cells[quantityIndex] || '').trim()
        const studentIdText = studentIndex >= 0 ? String(cells[studentIndex] || '').trim() : ''
        const goodsId = Number(goodsIdText)
        const quantity = Number(quantityText)
        const studentId = studentIdText === '' ? 0 : Number(studentIdText)
        return {
            rowNo: index + 1,
            mobile,
            mobileOk: MOBILE_PATTERN.test(mobile),
            goodsIdText,
            goodsId,
            goodsTitle: '',
            goodsOk: true,
            studentIdText,
            studentId,
            quantityText,
            quantity,
            quantityOk: Number.isInteger(quantity) && quantity >= 1 && quantity <= 99,
            remark: remarkIndex >= 0 ? String(cells[remarkIndex] || '').trim() : '',
            accountState: 'idle',
            accountLabel: ''
        }
    })

    await loadGoods()
    markGoodsRows()
    step.value = 'preview'
    await resolveAccounts()
}

async function loadGoods() {
    try {
        const map = new Map<number, any>()
        let pageNo = 1
        let total = 0
        // 分页拉全量券商品：预览要判断「商品不存在」，只取第一页会误标
        do {
            const data: any = await getCouponGoodsList({ page_no: pageNo, page_size: 200 })
            const list: any[] = data?.lists || []
            list.forEach((item: any) => map.set(Number(item.id), item))
            total = Number(data?.count || 0)
            pageNo += 1
        } while (map.size < total && pageNo <= 10)
        goodsMap.value = map
        goodsLoaded.value = true
    } catch {
        // 商品下拉加载失败不阻断导入：后端仍会逐行校验并返回准确原因
        goodsLoaded.value = false
    }
}

function markGoodsRows() {
    previewRows.value.forEach((row) => {
        if (!goodsLoaded.value || !Number.isFinite(row.goodsId)) {
            row.goodsOk = true
            return
        }
        const goods = goodsMap.value.get(row.goodsId)
        row.goodsOk = !!goods
        row.goodsTitle = goods ? String(goods.title || '') : ''
    })
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
            const list: any[] = (await getCouponStudents({ keyword: mobile })) || []
            const account = list.find((item: any) => String(item.mobile || '') === mobile)
            targets.forEach((row) => {
                row.accountState = account ? 'found' : 'missing'
                row.accountLabel = account
                    ? account.nickname || account.realName || account.account || `#${account.id}`
                    : ''
            })
        } catch {
            // 查询失败不判死：标为未知，提交后以后端结果为准
            targets.forEach((row) => {
                row.accountState = 'unknown'
            })
        } finally {
            checkedCount.value += 1
        }
    })
    checking.value = false
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
            goodsId: Number.isFinite(row.goodsId) ? row.goodsId : 0,
            studentId: Number.isFinite(row.studentId) ? row.studentId : 0,
            quantity: Number.isFinite(row.quantity) ? row.quantity : 0,
            remark: row.remark
        }))
        const data: any = await grantCouponBatch(payload)
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
            row?.goodsIdText ?? '',
            row?.quantityText ?? '',
            row?.studentIdText ?? '',
            row?.remark ?? '',
            error.reason || ''
        ]
    })
    downloadCsv(`批量发券失败行-${Date.now()}.csv`, [
        ['mobile', 'goodsId', 'quantity', 'studentId', 'remark', 'reason'],
        ...lines
    ])
}

function resetAll() {
    step.value = 'upload'
    parseError.value = ''
    previewRows.value = []
    goodsMap.value = new Map()
    goodsLoaded.value = false
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
