<template>
    <div>
        <el-card class="!border-none" shadow="never">
            <div class="text-lg font-medium">代理提现记录（只读）</div>
            <div class="text-tx-secondary text-sm mt-1 leading-6">
                历史代理提现申请与审核结果。提现审批、打款/退回等涉钱动作不在本期范围，
                统一标注「待甲方确认」；本页只读展示，不触发任何资金变动。
            </div>
            <el-alert
                class="mt-4"
                type="warning"
                :closable="false"
                title="只读页面：提现审批与打款/退回待甲方确认，本页不提供审批/打款按钮。"
            />
        </el-card>

        <el-card class="!border-none mt-4" shadow="never">
            <el-form class="mb-[-16px]" :inline="true" :model="queryParams">
                <el-form-item label="代理名称">
                    <el-input v-model="queryParams.agentName" class="w-[180px]" placeholder="代理名称" clearable @keyup.enter="resetPage" />
                </el-form-item>
                <el-form-item label="状态">
                    <el-select v-model="queryParams.status" class="w-[150px]">
                        <el-option label="全部" value="" />
                        <el-option label="待审核" :value="0" />
                        <el-option label="审核通过" :value="1" />
                        <el-option label="已拒绝" :value="2" />
                    </el-select>
                </el-form-item>
                <el-form-item label="申请时间">
                    <el-date-picker v-model="queryParams.startTime" class="w-[150px]" type="date" value-format="YYYY-MM-DD" placeholder="开始日期" clearable />
                    <span class="mx-2 text-gray-400">至</span>
                    <el-date-picker v-model="queryParams.endTime" class="w-[150px]" type="date" value-format="YYYY-MM-DD" placeholder="结束日期" clearable />
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
                <el-table-column label="代理" min-width="130">
                    <template #default="{ row }">{{ row.agentName || '-' }}</template>
                </el-table-column>
                <el-table-column label="提现金额" min-width="110" align="right">
                    <template #default="{ row }">¥ {{ money(row.withdrawAmount) }}</template>
                </el-table-column>
                <el-table-column label="收款信息" min-width="160">
                    <template #default="{ row }">
                        <div>{{ row.bankName || '-' }}</div>
                        <div class="text-gray-400 text-xs">{{ maskCard(row.bankCard) }}</div>
                    </template>
                </el-table-column>
                <el-table-column label="状态" width="100" align="center">
                    <template #default="{ row }">
                        <el-tag :type="statusTagType(row.status)">{{ statusText(row.status) }}</el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="申请时间" min-width="150">
                    <template #default="{ row }">{{ row.submitTime || '-' }}</template>
                </el-table-column>
                <el-table-column label="审核时间" min-width="150">
                    <template #default="{ row }">{{ row.auditTime || '-' }}</template>
                </el-table-column>
                <el-table-column label="审核备注" min-width="140" show-overflow-tooltip>
                    <template #default="{ row }">{{ row.auditRemark || '-' }}</template>
                </el-table-column>
                <el-table-column label="操作" width="110" fixed="right">
                    <template #default>
                        <span class="text-gray-400 text-xs">待甲方确认</span>
                    </template>
                </el-table-column>
            </el-table>
            <div class="flex justify-end mt-4">
                <pagination v-model="pager" @change="getLists" />
            </div>
        </el-card>
    </div>
</template>

<script lang="ts" setup name="agentWithdrawal">
import { getAgentWithdrawalList } from '@/api/agent'
import { usePaging } from '@/hooks/usePaging'

const queryParams = reactive({
    agentName: '',
    status: '',
    startTime: '',
    endTime: ''
})

const { pager, getLists, resetPage, resetParams } = usePaging({
    fetchFun: getAgentWithdrawalList,
    params: queryParams
})

function money(value: any) {
    const num = Number(value)
    return Number.isFinite(num) ? num.toFixed(2) : '0.00'
}

function maskCard(card: any) {
    const text = String(card || '')
    if (text.length <= 8) return text || '-'
    return `${text.slice(0, 4)}****${text.slice(-4)}`
}

function statusText(status: number) {
    return status === 1 ? '审核通过' : status === 2 ? '已拒绝' : status === 0 ? '待审核' : '未知'
}

function statusTagType(status: number) {
    return status === 1 ? 'success' : status === 2 ? 'danger' : 'warning'
}

getLists()
</script>
