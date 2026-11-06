<template>
    <div>
        <el-card class="!border-none" shadow="never">
            <div class="text-lg font-medium">代理分成记录（只读）</div>
            <div class="text-tx-secondary text-sm mt-1 leading-6">
                历史订单产生的代理分成流水：订单金额、当时分成比例、分成收益、到账时间。
                本页只读，用于对账与历史数据核对；结清/补发等涉钱动作待甲方确认。
            </div>
            <el-alert
                class="mt-4"
                type="warning"
                :closable="false"
                title="只读页面：分成结清、余额兑付、提现打款待甲方确认，本页不提供执行按钮。"
            />
        </el-card>

        <el-card class="!border-none mt-4" shadow="never">
            <el-form class="mb-[-16px]" :inline="true" :model="queryParams">
                <el-form-item label="代理">
                    <el-select
                        v-model="queryParams.agentId"
                        class="w-[240px]"
                        filterable
                        remote
                        :remote-method="searchAgents"
                        :loading="agentLoading"
                        placeholder="选择代理"
                        @change="resetPage"
                    >
                        <el-option v-for="item in agentOptions" :key="item.id" :label="item.name" :value="item.id" />
                    </el-select>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="resetPage">查询</el-button>
                </el-form-item>
            </el-form>
        </el-card>

        <el-card class="!border-none mt-4" shadow="never">
            <el-table size="large" table-layout="fixed" v-loading="pager.loading" :data="pager.lists">
                <el-table-column label="记录 ID" prop="id" width="86" />
                <el-table-column label="课程班级" min-width="180" show-overflow-tooltip>
                    <template #default="{ row }">{{ row.courseName || `班级#${row.courseClassId}` }}</template>
                </el-table-column>
                <el-table-column label="订单金额" min-width="110" align="right">
                    <template #default="{ row }">¥ {{ money(row.orderAmount) }}</template>
                </el-table-column>
                <el-table-column label="分成比例" width="110" align="right">
                    <template #default="{ row }">{{ rateText(row.commissionRate) }}</template>
                </el-table-column>
                <el-table-column label="分成收益" min-width="110" align="right">
                    <template #default="{ row }">¥ {{ money(row.earningAmount) }}</template>
                </el-table-column>
                <el-table-column label="到账时间" min-width="150">
                    <template #default="{ row }">{{ row.createTime || '-' }}</template>
                </el-table-column>
            </el-table>
            <div class="flex justify-end mt-4">
                <pagination v-model="pager" @change="getLists" />
            </div>
        </el-card>
    </div>
</template>

<script lang="ts" setup name="agentEarning">
import { getAgentEarningList, getAgentList } from '@/api/agent'
import { usePaging } from '@/hooks/usePaging'

const route = useRoute()
const queryParams = reactive({
    agentId: route.query.agentId ? Number(route.query.agentId) : ''
})
const agentOptions = ref<any[]>([])
const { pager, getLists, resetPage } = usePaging({
    fetchFun: getAgentEarningList,
    params: queryParams
})

function money(value: any) {
    const num = Number(value)
    return Number.isFinite(num) ? num.toFixed(2) : '0.00'
}

function rateText(value: any) {
    const num = Number(value)
    if (!Number.isFinite(num)) return '-'
    return `${(num * 100).toFixed(2).replace(/\.?0+$/, '')}%`
}

const agentLoading = ref(false)

/** 代理下拉远程搜索：后端 page_size 上限 60（PageValidate @DecimalMax），靠名称搜索覆盖更多代理。 */
async function searchAgents(keyword: string) {
    agentLoading.value = true
    try {
        const params: any = { page_no: 1, page_size: 60 }
        const name = (keyword || '').trim()
        if (name) params.name = name
        const data: any = await getAgentList(params)
        agentOptions.value = data.lists || []
    } finally {
        agentLoading.value = false
    }
}

async function loadAgents() {
    await searchAgents('')
    if (!queryParams.agentId && agentOptions.value.length) {
        queryParams.agentId = agentOptions.value[0].id
        resetPage()
    }
}

loadAgents()
</script>
