<template>
    <div>
        <el-card class="!border-none" shadow="never">
            <el-alert
                type="warning"
                title="温馨提示：用户充值记录"
                :closable="false"
                show-icon
            ></el-alert>
            <el-form ref="formRef" class="mb-[-16px] mt-[16px]" :model="queryParams" :inline="true">
                <el-form-item label="充值单号">
                    <el-input
                        class="w-[280px]"
                        v-model="queryParams.sn"
                        placeholder="请输入充值单号"
                        clearable
                        @keyup.enter="resetPage"
                    />
                </el-form-item>
                <el-form-item label="用户信息">
                    <el-input
                        class="w-[280px]"
                        v-model="queryParams.user_info"
                        placeholder="请输入用户账号/昵称/手机号"
                        clearable
                        @keyup.enter="resetPage"
                    />
                </el-form-item>
                <el-form-item label="支付方式">
                    <el-select class="w-[280px]" v-model="queryParams.pay_way">
                        <el-option label="全部" value />
                        <el-option label="微信支付" :value="2" />
                        <el-option label="支付宝支付" :value="3" />
                    </el-select>
                </el-form-item>
                <el-form-item label="支付状态">
                    <el-select class="w-[280px]" v-model="queryParams.pay_status">
                        <el-option label="全部" value />
                        <el-option label="未支付" :value="0" />
                        <el-option label="已支付" :value="1" />
                    </el-select>
                </el-form-item>
                <el-form-item label="下单时间">
                    <daterange-picker
                        v-model:startTime="queryParams.start_time"
                        v-model:endTime="queryParams.end_time"
                    />
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="resetPage">查询</el-button>
                    <el-button @click="resetParams">重置</el-button>
                    <export-data
                        class="ml-2.5"
                        v-perms="['recharge.recharge/export']"
                        :fetch-fun="rechargeLists"
                        :params="queryParams"
                        :page-size="pager.size"
                    />
                </el-form-item>
            </el-form>
        </el-card>
        <el-card class="!border-none mt-4" shadow="never">
            <el-table size="large" v-loading="pager.loading" :data="pager.lists">
                <el-table-column label="用户信息" min-width="160">
                    <template #default="{ row }">
                        <div class="flex items-center">
                            <image-contain
                                class="flex-none mr-2"
                                :src="row.avatar"
                                :width="40"
                                :height="40"
                                preview-teleported
                                fit="contain"
                            />
                            {{ row.nickname }}
                        </div>
                    </template>
                </el-table-column>
                <el-table-column label="充值单号" prop="sn" min-width="190" />
                <el-table-column label="充值金额" prop="orderAmount" min-width="100">
                </el-table-column>
                <el-table-column label="支付方式" prop="payWayText" min-width="100" />
                <el-table-column label="支付状态" prop="" min-width="100">
                    <template #default="{ row }">
                        <span
                            :class="{
                                'text-error': row.payStatus == 0
                            }"
                        >
                            {{ row.payStatusText }}
                        </span>
                    </template>
                </el-table-column>
                <el-table-column label="提交时间" prop="createTime" min-width="180" />
                <el-table-column label="支付时间" prop="payTime" min-width="180" />
                <el-table-column label="操作" width="160" fixed="right">
                    <template #default="{ row }">
                        <el-button
                            v-perms="['recharge.recharge/edit']"
                            type="primary"
                            link
                            @click="openEdit(row)"
                        >
                            编辑
                        </el-button>
                        <el-button
                            v-if="row.payStatus == 1"
                            v-perms="['recharge.recharge/refund']"
                            type="primary"
                            link
                            :disabled="row.refundStatus == 1"
                            @click="handleRefund(row.id)"
                        >
                            退款
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
            <div class="flex justify-end mt-4">
                <pagination v-model="pager" @change="getLists" />
            </div>
        </el-card>

        <el-dialog
            v-model="editVisible"
            title="编辑缴费记录"
            width="620px"
            :close-on-click-modal="false"
            @closed="resetEditForm"
        >
            <el-descriptions :column="2" border>
                <el-descriptions-item label="订单编号">
                    {{ editingRow.sn || '-' }}
                </el-descriptions-item>
                <el-descriptions-item label="支付金额">
                    ¥{{ editingRow.orderAmount }}
                </el-descriptions-item>
            </el-descriptions>
            <el-alert
                class="mt-3"
                type="info"
                :closable="false"
                title="订单编号、金额、支付通道、支付状态和抵扣券均已锁定；如需纠正请走支付/退款状态机。"
            />
            <el-form
                ref="editFormRef"
                class="mt-5"
                :model="editForm"
                :rules="editRules"
                label-width="100px"
            >
                <el-form-item label="支付方式" prop="payWay">
                    <el-select
                        v-model="editForm.payWay"
                        class="w-full"
                        placeholder="请选择支付方式"
                        disabled
                    >
                        <el-option label="余额支付" :value="1" />
                        <el-option label="微信支付" :value="2" />
                        <el-option label="支付宝支付" :value="3" />
                        <el-option label="线下支付" :value="4" />
                    </el-select>
                </el-form-item>
                <el-form-item v-if="editForm.payWay === 4" label="支付凭证" prop="voucher">
                    <div class="w-full">
                        <material-picker v-model="editForm.voucher" :limit="1" />
                        <div class="text-tx-secondary text-xs mt-1">线下支付必须上传支付凭证</div>
                    </div>
                </el-form-item>
                <el-form-item label="支付状态" prop="payStatus">
                    <el-select
                        v-model="editForm.payStatus"
                        class="w-full"
                        placeholder="请选择支付状态"
                        disabled
                    >
                        <el-option label="未支付" :value="0" />
                        <el-option label="已支付" :value="1" />
                        <el-option label="已取消" :value="4" />
                    </el-select>
                </el-form-item>
                <el-form-item label="对账状态" prop="isAudit">
                    <el-select
                        v-model="editForm.isAudit"
                        class="w-full"
                        placeholder="请选择对账状态"
                    >
                        <el-option label="未对账" :value="0" />
                        <el-option label="已对账" :value="1" />
                    </el-select>
                </el-form-item>
                <el-form-item label="抵扣券" prop="couponId">
                    <el-select
                        v-model="editForm.couponId"
                        class="w-full"
                        filterable
                        clearable
                        disabled
                        :loading="couponLoading"
                        placeholder="选择抵扣券（可选）"
                    >
                        <el-option label="不使用抵扣券" :value="0" />
                        <el-option
                            v-for="item in couponOptions"
                            :key="item.id"
                            :label="`${item.name} (¥${item.amount})`"
                            :value="item.id"
                        />
                    </el-select>
                </el-form-item>
            </el-form>
            <template #footer>
                <el-button @click="editVisible = false">取消</el-button>
                <el-button type="primary" :loading="submitting" @click="submitEdit">
                    保存
                </el-button>
            </template>
        </el-dialog>
    </div>
</template>
<script lang="ts" setup name="rechargeRecord">
import type { FormInstance } from 'element-plus'

import { getCouponList } from '@/api/coupon'
import { editRecharge, rechargeLists, refund } from '@/api/finance'
import { usePaging } from '@/hooks/usePaging'
import feedback from '@/utils/feedback'

const queryParams = reactive({
    sn: '',
    user_info: '',
    pay_way: '',
    pay_status: '',
    start_time: '',
    end_time: ''
})

const { pager, getLists, resetPage, resetParams } = usePaging({
    fetchFun: rechargeLists,
    params: queryParams
})
const handleRefund = async (id: number) => {
    await feedback.confirm('确认退款？')
    await refund({
        recharge_id: id
    })
    getLists()
}

// ---------- 编辑缴费记录（#77） ----------
const editVisible = ref(false)
const editFormRef = ref<FormInstance>()
const editingRow = ref<any>({})
const submitting = ref(false)
const couponLoading = ref(false)
const couponOptions = ref<any[]>([])
const editForm = reactive<any>({
    id: 0,
    payWay: 2,
    payStatus: 0,
    isAudit: 0,
    voucher: '',
    couponId: 0
})
const editRules = {
    payWay: [{ required: true, message: '请选择支付方式', trigger: 'change' }],
    payStatus: [{ required: true, message: '请选择支付状态', trigger: 'change' }],
    isAudit: [{ required: true, message: '请选择对账状态', trigger: 'change' }]
}

async function loadCoupons(mobile?: string) {
    couponLoading.value = true
    try {
        if (!mobile) {
            couponOptions.value = []
            return
        }
        const data = await getCouponList({
            page_no: 1,
            page_size: 50,
            status: 0,
            mobile
        })
        couponOptions.value = (data?.lists || []).map((item: any) => ({
            id: item.id,
            name: item.name,
            amount: item.amount
        }))
    } catch {
        couponOptions.value = []
    } finally {
        couponLoading.value = false
    }
}

async function openEdit(row: any) {
    editingRow.value = row
    editForm.id = row.id
    editForm.payWay = Number(row.payWay) || 2
    editForm.payStatus = Number(row.payStatus) || 0
    editForm.isAudit = Number(row.isAudit) || 0
    editForm.voucher = row.voucher || ''
    editForm.couponId = Number(row.couponId) || 0
    couponOptions.value = []
    editVisible.value = true
    await nextTick()
    editFormRef.value?.clearValidate()
    await loadCoupons(row.mobile)
    if (row.couponId > 0 && !couponOptions.value.some((item) => item.id === row.couponId)) {
        couponOptions.value.unshift({
            id: row.couponId,
            name: row.couponName || `券#${row.couponId}`,
            amount: row.couponAmount
        })
    }
}

function resetEditForm() {
    editingRow.value = {}
    editForm.id = 0
    editForm.payWay = 2
    editForm.payStatus = 0
    editForm.isAudit = 0
    editForm.voucher = ''
    editForm.couponId = 0
    couponOptions.value = []
    editFormRef.value?.clearValidate()
}

async function submitEdit() {
    if (!editFormRef.value) return
    const valid = await editFormRef.value.validate().catch(() => false)
    if (!valid) return
    if (editForm.payWay === 4 && !editForm.voucher) {
        feedback.msgWarning('线下支付请先上传支付凭证')
        return
    }
    submitting.value = true
    try {
        await editRecharge({
            id: editForm.id,
            payWay: Number(editForm.payWay),
            payStatus: Number(editForm.payStatus),
            isAudit: Number(editForm.isAudit),
            voucher: editForm.voucher || '',
            couponId: Number(editForm.couponId) || 0
        })
        feedback.msgSuccess('保存成功')
        editVisible.value = false
        getLists()
    } finally {
        submitting.value = false
    }
}

getLists()
</script>
