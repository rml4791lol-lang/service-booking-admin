<template>
    <div>
        <el-card class="!border-none" shadow="never">
            <div class="text-lg font-medium">代理列表（只读）</div>
            <div class="text-tx-secondary text-sm mt-1 leading-6">
                历史代理档案与分成比例、提现账户。代理体系是否继续由甲方书面确认；
                历史分成结清、余额兑付、提现打款等涉钱动作本期不提供入口，统一标注「待甲方确认」。
            </div>
            <el-alert
                class="mt-4"
                type="warning"
                :closable="false"
                title="只读页面：涉钱清结算（结清分成/余额兑付/提现打款）待甲方确认，本页不提供执行按钮。"
            />
        </el-card>

        <el-card class="!border-none mt-4" shadow="never">
            <el-form class="mb-[-16px]" :inline="true" :model="queryParams">
                <el-form-item label="代理名称">
                    <el-input v-model="queryParams.name" class="w-[200px]" placeholder="代理名称" clearable @keyup.enter="resetPage" />
                </el-form-item>
                <el-form-item label="联系电话">
                    <el-input v-model="queryParams.mobile" class="w-[180px]" placeholder="手机号" clearable @keyup.enter="resetPage" />
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
                <el-table-column label="代理名称" prop="name" min-width="140" show-overflow-tooltip />
                <el-table-column label="联系电话" prop="mobile" min-width="120" />
                <el-table-column label="分成比例" width="110" align="right">
                    <template #default="{ row }">{{ rateText(row.commissionRate) }}</template>
                </el-table-column>
                <el-table-column label="提现银行" min-width="120">
                    <template #default="{ row }">{{ row.bankName || '-' }}</template>
                </el-table-column>
                <el-table-column label="银行卡号" min-width="150">
                    <template #default="{ row }">{{ maskCard(row.bankCard) }}</template>
                </el-table-column>
                <el-table-column label="创建时间" min-width="150">
                    <template #default="{ row }">{{ row.createTime || '-' }}</template>
                </el-table-column>
                <el-table-column label="操作" width="160" fixed="right">
                    <template #default="{ row }">
                        <el-button v-perms="['agent/detail']" type="primary" link @click="openDetail(row)">
                            详情
                        </el-button>
                        <el-button type="primary" link @click="goEarning(row)">分成记录</el-button>
                        <el-button type="primary" link @click="goWithdrawal()">提现记录</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <div class="flex justify-end mt-4">
                <pagination v-model="pager" @change="getLists" />
            </div>
        </el-card>

        <el-dialog v-model="detailVisible" title="代理详情（只读）" width="640px">
            <el-descriptions :column="2" border>
                <el-descriptions-item label="代理名称">{{ current.name || '-' }}</el-descriptions-item>
                <el-descriptions-item label="联系电话">{{ current.mobile || '-' }}</el-descriptions-item>
                <el-descriptions-item label="分成比例">{{ rateText(current.commissionRate) }}</el-descriptions-item>
                <el-descriptions-item label="后台账号 ID">{{ current.adminId || '-' }}</el-descriptions-item>
                <el-descriptions-item label="提现银行">{{ current.bankName || '-' }}</el-descriptions-item>
                <el-descriptions-item label="银行卡号">{{ maskCard(current.bankCard) }}</el-descriptions-item>
                <el-descriptions-item label="创建时间" :span="2">{{ current.createTime || '-' }}</el-descriptions-item>
            </el-descriptions>
            <el-alert
                class="mt-4"
                type="warning"
                :closable="false"
                title="当前可提现余额、累计收益等资金口径及结清方案待甲方确认后再展示/处理。"
            />
            <template #footer>
                <el-button @click="detailVisible = false">关闭</el-button>
            </template>
        </el-dialog>
    </div>
</template>

<script lang="ts" setup name="agentList">
import { getAgentDetail, getAgentList } from '@/api/agent'
import { usePaging } from '@/hooks/usePaging'

const router = useRouter()
const queryParams = reactive({ name: '', mobile: '' })
const { pager, getLists, resetPage, resetParams } = usePaging({
    fetchFun: getAgentList,
    params: queryParams
})

const detailVisible = ref(false)
const current = ref<any>({})

function rateText(value: any) {
    const num = Number(value)
    if (!Number.isFinite(num)) return '-'
    return `${(num * 100).toFixed(2).replace(/\.?0+$/, '')}%`
}

function maskCard(card: any) {
    const text = String(card || '')
    if (text.length <= 8) return text || '-'
    return `${text.slice(0, 4)}****${text.slice(-4)}`
}

async function openDetail(row: any) {
    try {
        current.value = await getAgentDetail(row.id)
    } catch (e) {
        current.value = row
    }
    detailVisible.value = true
}

function goEarning(row: any) {
    router.push({ path: '/agent/earning', query: { agentId: String(row.id) } })
}

function goWithdrawal() {
    router.push({ path: '/agent/withdrawal' })
}

getLists()
</script>
