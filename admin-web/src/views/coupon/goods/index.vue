<template>
    <div class="coupon-goods-page">
        <el-card class="!border-none" shadow="never">
            <div class="flex items-center justify-between">
                <div>
                    <div class="text-lg font-medium">券商品管理</div>
                    <div class="text-tx-secondary text-sm mt-1">
                        维护次数券商品：价格、次数、有效期、适用课程/小区、限购、角标与排序。
                        下架后小程序券商城不再展示；已售出的券和订单不受影响。
                    </div>
                </div>
                <div class="flex items-center gap-3">
                    <el-button v-perms="['coupon.goods/grant']" @click="batchGrantVisible = true">
                        <template #icon><icon name="el-icon-Upload" /></template>
                        批量发券
                    </el-button>
                    <el-button v-perms="['coupon.goods/granted']" @click="grantedVisible = true">
                        <template #icon><icon name="el-icon-Tickets" /></template>
                        已发券
                    </el-button>
                    <el-button v-perms="['coupon.goods/add']" type="primary" @click="openForm()">
                        <template #icon><icon name="el-icon-Plus" /></template>
                        新增券商品
                    </el-button>
                </div>
            </div>
        </el-card>

        <el-card class="!border-none mt-4" shadow="never">
            <el-form class="mb-[-16px]" :inline="true" :model="query">
                <el-form-item label="名称/编码">
                    <el-input
                        v-model="query.keyword"
                        class="w-[200px]"
                        clearable
                        placeholder="券名称或商品编码"
                        @keyup.enter="resetPage"
                    />
                </el-form-item>
                <el-form-item label="适用范围">
                    <el-select
                        v-model="query.scopeType"
                        class="w-[150px]"
                        clearable
                        placeholder="全部类型"
                    >
                        <el-option
                            v-for="item in SCOPE_TYPE_OPTIONS"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value"
                        />
                    </el-select>
                </el-form-item>
                <el-form-item label="状态">
                    <el-select
                        v-model="query.status"
                        class="w-[130px]"
                        clearable
                        placeholder="全部"
                    >
                        <el-option label="上架" :value="1" />
                        <el-option label="下架" :value="0" />
                    </el-select>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="resetPage">查询</el-button>
                    <el-button @click="resetParams">重置</el-button>
                </el-form-item>
            </el-form>
        </el-card>

        <el-card class="!border-none mt-4" shadow="never">
            <div class="mb-3 text-sm text-gray-400">
                共 {{ pager.count }} 个券商品；列表与小程序券商城均按「排序」升序展示。
            </div>
            <el-table
                size="large"
                table-layout="fixed"
                v-loading="pager.loading"
                :data="pager.lists"
            >
                <el-table-column label="ID" prop="id" width="55" />
                <el-table-column label="券名称 / 编码" min-width="172" show-overflow-tooltip>
                    <template #default="{ row }">
                        <div class="font-medium">{{ row.title }}</div>
                        <div class="text-xs text-gray-400">{{ row.code }}</div>
                    </template>
                </el-table-column>
                <el-table-column label="售价" width="95">
                    <template #default="{ row }">
                        <span class="font-medium text-red-500">{{ money(row.price) }}</span>
                    </template>
                </el-table-column>
                <el-table-column label="次数" width="100">
                    <template #default="{ row }">
                        <div>{{ row.times || 0 }} 次</div>
                        <div class="text-xs text-gray-400">
                            {{ validDaysText(row.validDays) }}
                        </div>
                    </template>
                </el-table-column>
                <el-table-column label="适用范围" min-width="210">
                    <template #default="{ row }">
                        <div class="scope-main">
                            <el-tag :type="scopeTypeTag(row.scopeType)" size="small" effect="plain">
                                {{ scopeTypeLabel(row.scopeType) }}
                            </el-tag>
                            <span class="scope-sub">{{ row.scope || '-' }}</span>
                        </div>
                        <div
                            class="scope-sub text-xs text-gray-400"
                            :title="row.courseNameText || '不限'"
                        >
                            课程：{{ row.courseNameText || '不限' }}
                        </div>
                        <div
                            class="scope-sub text-xs text-gray-400"
                            :title="row.areaNameText || '不限'"
                        >
                            小区：{{ row.areaNameText || '不限' }}
                        </div>
                    </template>
                </el-table-column>
                <el-table-column label="限购" width="110">
                    <template #default="{ row }">{{ limitText(row) }}</template>
                </el-table-column>
                <el-table-column label="角标" width="85" show-overflow-tooltip>
                    <template #default="{ row }">
                        <el-tag v-if="row.tag" type="warning" size="small" effect="plain">
                            {{ row.tag }}
                        </el-tag>
                        <span v-else class="text-gray-400">-</span>
                    </template>
                </el-table-column>
                <el-table-column label="排序" width="80">
                    <template #default="{ row }">
                        <span class="mr-1">{{ row.sort }}</span>
                        <el-button
                            v-perms="['coupon.goods/sort']"
                            type="primary"
                            link
                            @click="openSort(row)"
                        >
                            调整
                        </el-button>
                    </template>
                </el-table-column>
                <el-table-column label="状态" width="75">
                    <template #default="{ row }">
                        <el-tag :type="Number(row.status) === 1 ? 'success' : 'info'" size="small">
                            {{ Number(row.status) === 1 ? '上架' : '下架' }}
                        </el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="更新时间" width="165" show-overflow-tooltip>
                    <template #default="{ row }">{{ formatSeconds(row.updateTime) }}</template>
                </el-table-column>
                <el-table-column label="操作" width="235" fixed="right">
                    <template #default="{ row }">
                        <el-button
                            v-perms="['coupon.goods/grant']"
                            type="success"
                            link
                            :disabled="Number(row.status) !== 1"
                            title="下架商品不能发券"
                            @click="openGrant(row)"
                        >
                            发券
                        </el-button>
                        <el-button
                            v-perms="['coupon.goods/edit']"
                            type="primary"
                            link
                            @click="openForm(row)"
                        >
                            编辑
                        </el-button>
                        <el-button
                            v-perms="['coupon.goods/status']"
                            :type="Number(row.status) === 1 ? 'warning' : 'success'"
                            link
                            @click="toggleStatus(row)"
                        >
                            {{ Number(row.status) === 1 ? '下架' : '上架' }}
                        </el-button>
                        <el-button
                            v-perms="['coupon.goods/del']"
                            type="danger"
                            link
                            :loading="deletingId === row.id"
                            @click="handleDelete(row)"
                        >
                            删除
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
            <div class="flex justify-end mt-4">
                <pagination v-model="pager" @change="getLists" />
            </div>
        </el-card>

        <!-- 新增 / 编辑 -->
        <el-dialog
            v-model="formVisible"
            :title="form.id ? '编辑券商品' : '新增券商品'"
            width="820px"
            top="5vh"
            :close-on-click-modal="false"
            @closed="resetForm"
        >
            <el-form ref="formRef" :model="form" :rules="formRules" label-width="110px">
                <div class="form-section-title">基本信息</div>
                <el-row :gutter="12">
                    <el-col :span="12">
                        <el-form-item label="商品编码" prop="code">
                            <el-input
                                v-model="form.code"
                                :disabled="!!form.id"
                                maxlength="50"
                                placeholder="如 group_single"
                            />
                            <div class="text-xs text-gray-400 mt-1">
                                {{
                                    form.id
                                        ? '商品编码是数据种子幂等键，创建后不可修改'
                                        : '小写字母/数字/下划线，创建后不可修改'
                                }}
                            </div>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="券名称" prop="title">
                            <el-input
                                v-model="form.title"
                                maxlength="100"
                                show-word-limit
                                placeholder="如 团课单次券"
                            />
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-form-item label="商品简介" prop="description">
                    <el-input
                        v-model="form.description"
                        type="textarea"
                        :rows="2"
                        maxlength="255"
                        show-word-limit
                        placeholder="展示在小程序商品卡片上的简短说明"
                    />
                </el-form-item>

                <div class="form-section-title">价格与有效期</div>
                <el-row :gutter="12">
                    <el-col :span="8">
                        <el-form-item label="售价" prop="price">
                            <el-input-number
                                v-model="form.price"
                                class="w-full"
                                :min="0"
                                :max="99999999"
                                :precision="2"
                                :step="1"
                            />
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="包含次数" prop="times">
                            <el-input-number
                                v-model="form.times"
                                class="w-full"
                                :min="1"
                                :max="9999"
                                :step="1"
                            />
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="有效天数" prop="validDays">
                            <el-input-number
                                v-model="form.validDays"
                                class="w-full"
                                :min="0"
                                :max="3650"
                                :step="1"
                            />
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="12">
                    <el-col :span="8">
                        <el-form-item label="排序">
                            <el-input-number
                                v-model="form.sort"
                                class="w-full"
                                :min="0"
                                :max="9999"
                                :step="1"
                            />
                        </el-form-item>
                    </el-col>
                    <el-col :span="16">
                        <div class="text-xs text-gray-400 leading-6">
                            有效天数填 0 表示长期有效。
                        </div>
                        <div class="text-xs text-gray-400 leading-6">
                            排序值越小越靠前，保存后小程序券商城按此顺序刷新。
                        </div>
                    </el-col>
                </el-row>

                <div class="form-section-title">适用范围</div>
                <el-row :gutter="12">
                    <el-col :span="10">
                        <el-form-item label="范围类型" prop="scopeType">
                            <el-select v-model="form.scopeType" class="w-full" placeholder="请选择">
                                <el-option
                                    v-for="item in SCOPE_TYPE_OPTIONS"
                                    :key="item.value"
                                    :label="item.label"
                                    :value="item.value"
                                />
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="14">
                        <el-form-item label="范围文案" prop="scope">
                            <el-input
                                v-model="form.scope"
                                maxlength="100"
                                placeholder="留空自动生成，如 团课通用"
                            />
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="12">
                    <el-col :span="12">
                        <el-form-item label="限定课程">
                            <el-select
                                v-model="form.courseIds"
                                class="w-full"
                                multiple
                                filterable
                                collapse-tags
                                :disabled="form.scopeType === 'all'"
                                placeholder="留空=不限课程"
                            >
                                <el-option
                                    v-for="item in filteredCourseOptions"
                                    :key="item.id"
                                    :label="courseOptionLabel(item)"
                                    :value="item.id"
                                />
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="限定小区">
                            <el-select
                                v-model="form.areaIds"
                                class="w-full"
                                multiple
                                filterable
                                collapse-tags
                                placeholder="留空=不限小区"
                            >
                                <el-option
                                    v-for="item in areaOptions"
                                    :key="item.id"
                                    :label="areaOptionLabel(item)"
                                    :value="item.id"
                                />
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>
                <div class="-mt-2 mb-4 pl-[110px] text-xs text-gray-400 leading-5">
                    全部课程不能限定课程；团课/私教/体验只能选择同班型课程（体验券不能限定私教课）。
                    限定小区与范围类型互相独立，可留空。
                </div>

                <div class="form-section-title">限购与展示</div>
                <el-row :gutter="12">
                    <el-col :span="12">
                        <el-form-item label="单账号限购">
                            <el-input-number
                                v-model="form.perUser"
                                class="w-[180px]"
                                :min="0"
                                :max="9999"
                                :step="1"
                            />
                            <span class="ml-2 text-sm text-gray-400">份，0=不限</span>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="单学员限购">
                            <el-input-number
                                v-model="form.perStudent"
                                class="w-[180px]"
                                :min="0"
                                :max="9999"
                                :step="1"
                            />
                            <span class="ml-2 text-sm text-gray-400">份，0=不限</span>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="12">
                    <el-col :span="12">
                        <el-form-item label="角标">
                            <el-input
                                v-model="form.tag"
                                maxlength="30"
                                placeholder="如 新人专享 / 超值"
                            />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="状态">
                            <el-radio-group v-model="form.status">
                                <el-radio :label="1">上架</el-radio>
                                <el-radio :label="0">下架</el-radio>
                            </el-radio-group>
                        </el-form-item>
                    </el-col>
                </el-row>
            </el-form>
            <template #footer>
                <el-button @click="formVisible = false">取消</el-button>
                <el-button type="primary" :loading="saving" @click="submitForm">保存</el-button>
            </template>
        </el-dialog>

        <!-- 排序调整 -->
        <el-dialog v-model="sortVisible" title="调整排序" width="420px">
            <el-form label-width="90px">
                <el-form-item label="券商品">
                    <span>{{ sortRow.title }}（{{ sortRow.code }}）</span>
                </el-form-item>
                <el-form-item label="排序值">
                    <el-input-number
                        v-model="sortValue"
                        :min="0"
                        :max="9999"
                        :step="1"
                        class="w-[200px]"
                    />
                    <span class="ml-2 text-sm text-gray-400">数字越小越靠前</span>
                </el-form-item>
            </el-form>
            <template #footer>
                <el-button @click="sortVisible = false">取消</el-button>
                <el-button type="primary" :loading="sortSaving" @click="submitSort">保存</el-button>
            </template>
        </el-dialog>

        <!-- 发券（#207：次数券按家长手机号发放） -->
        <el-dialog
            v-model="grantVisible"
            title="发券"
            width="560px"
            :close-on-click-modal="false"
            @closed="resetGrantForm"
        >
            <el-form ref="grantFormRef" :model="grantForm" :rules="grantRules" label-width="100px">
                <el-form-item label="券商品">
                    <!-- 券商品固定为当前行：从行内「发券」进入，只读展示可避免误选其他商品；
                         需要换商品时关闭弹窗回到列表重新选择。 -->
                    <div class="w-full rounded bg-gray-50 px-3 py-2 text-sm leading-6">
                        <div class="font-medium">{{ grantRow.title }}</div>
                        <div class="text-gray-500">
                            共 {{ grantRow.times || 0 }} 次，发放时每张 1 次，按张数计 ·
                            {{ validDaysText(grantRow.validDays) }} ·
                            {{ scopeTypeLabel(grantRow.scopeType) }} · {{ grantRow.scope || '不限' }}
                        </div>
                    </div>
                </el-form-item>
                <el-form-item label="家长手机号" prop="mobile">
                    <el-input
                        v-model="grantForm.mobile"
                        maxlength="11"
                        clearable
                        placeholder="请输入家长手机号"
                        @blur="resolveGrantAccount"
                    />
                    <div v-if="grantAccountState === 'found'" class="mt-1 text-xs text-green-600">
                        账号：{{ grantAccountLabel }}
                    </div>
                    <div
                        v-else-if="grantAccountState === 'missing'"
                        class="mt-1 text-xs text-orange-500"
                    >
                        未找到该手机号账号，提交时会提示未注册
                    </div>
                </el-form-item>
                <el-form-item label="学员">
                    <el-select
                        v-model="grantForm.studentId"
                        class="w-full"
                        clearable
                        filterable
                        :loading="grantStudentLoading"
                        :disabled="!grantAccount"
                        placeholder="留空=账号级券"
                    >
                        <el-option
                            v-for="item in grantStudentOptions"
                            :key="item.id"
                            :label="item.name"
                            :value="item.id"
                        />
                    </el-select>
                    <div class="mt-1 text-xs text-gray-400">
                        留空表示账号级券，家长名下任一学员可用；指定学员后券只对该学员生效。
                    </div>
                </el-form-item>
                <el-form-item label="张数" prop="quantity">
                    <el-input-number v-model="grantForm.quantity" :min="1" :max="99" :step="1" />
                    <span class="ml-2 text-sm text-gray-400">1–99 张，每张券 1 次</span>
                </el-form-item>
                <el-form-item label="备注">
                    <el-input
                        v-model="grantForm.remark"
                        type="textarea"
                        :rows="2"
                        maxlength="200"
                        show-word-limit
                        placeholder="选填；用于记录发放原因"
                    />
                </el-form-item>
            </el-form>
            <template #footer>
                <el-button @click="grantVisible = false">取消</el-button>
                <el-button type="primary" :loading="grantSaving" @click="submitGrant">
                    确认发券
                </el-button>
            </template>
        </el-dialog>

        <!-- 已发券 / 撤回（#213 C2） -->
        <el-drawer v-model="grantedVisible" title="已发券" size="780px">
            <el-form class="mb-[-8px]" :inline="true">
                <el-form-item label="手机号">
                    <el-input
                        v-model="grantedQuery.mobile"
                        class="w-[170px]"
                        maxlength="11"
                        clearable
                        placeholder="家长手机号"
                        @keyup.enter="searchGranted"
                    />
                </el-form-item>
                <el-form-item label="券号">
                    <el-input
                        v-model="grantedQuery.sn"
                        class="w-[210px]"
                        maxlength="40"
                        clearable
                        placeholder="券号（精确）"
                        @keyup.enter="searchGranted"
                    />
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" :loading="grantedPager.loading" @click="searchGranted">
                        查询
                    </el-button>
                    <el-button @click="resetGrantedQuery">重置</el-button>
                </el-form-item>
            </el-form>
            <div class="mb-2 mt-4 text-xs text-gray-400">
                按券号或家长手机号查询；只有「可用且从未核销」的券可以撤回，撤回后券立即作废并记入券流水。
            </div>
            <el-table
                size="large"
                table-layout="fixed"
                v-loading="grantedPager.loading"
                :data="grantedPager.lists"
                max-height="560"
            >
                <el-table-column label="券号" prop="sn" min-width="160" show-overflow-tooltip />
                <el-table-column label="券名" prop="title" min-width="130" show-overflow-tooltip />
                <el-table-column label="家长" width="120">
                    <template #default="{ row }">{{ row.mobile || '-' }}</template>
                </el-table-column>
                <el-table-column label="学员" width="100" show-overflow-tooltip>
                    <template #default="{ row }">
                        {{ row.studentName || '账号级' }}
                    </template>
                </el-table-column>
                <el-table-column label="状态" width="90" align="center">
                    <template #default="{ row }">
                        <el-tag size="small" :type="couponStatusTag(row.status)" effect="plain">
                            {{ row.statusText }}
                        </el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="剩余" width="80" align="center">
                    <template #default="{ row }">{{ row.remaining }}/{{ row.total }}</template>
                </el-table-column>
                <el-table-column label="发放时间" prop="createTime" width="165" show-overflow-tooltip />
                <el-table-column label="操作" width="90" fixed="right" align="center">
                    <template #default="{ row }">
                        <el-button
                            v-perms="['coupon.goods/revoke']"
                            type="danger"
                            link
                            :disabled="!row.revocable"
                            :loading="revokingSn === row.sn"
                            @click="handleRevoke(row)"
                        >
                            撤回
                        </el-button>
                    </template>
                </el-table-column>
                <template #empty>
                    <el-empty description="输入手机号或券号后点「查询」" />
                </template>
            </el-table>
            <div class="flex justify-end mt-4">
                <pagination v-model="grantedPager" @change="getGranted" />
            </div>
        </el-drawer>

        <!-- 批量发券（批量导入）：CSV 前端解析 → 预览校验 → 提交 → 失败行可下载 -->
        <batch-grant v-model="batchGrantVisible" @success="getLists" />
    </div>
</template>

<script lang="ts" setup name="couponGoods">
import {
    addCouponGoods,
    delCouponGoods,
    editCouponGoods,
    getCouponGoodsList,
    getCouponGoodsOptions,
    getGrantedCoupons,
    grantCoupon,
    revokeCoupon,
    sortCouponGoods,
    statusCouponGoods
} from '@/api/couponGoods'
import { getCouponStudents } from '@/api/coupon'
import { getUserStudents } from '@/api/consumer'
import { usePaging } from '@/hooks/usePaging'
import feedback from '@/utils/feedback'

import BatchGrant from './components/batch-grant.vue'

const SCOPE_TYPE_OPTIONS = [
    { value: 'all', label: '全部课程' },
    { value: 'group', label: '团课' },
    { value: 'private', label: '私教' },
    { value: 'trial', label: '体验' }
]

const query = reactive<any>({
    keyword: '',
    scopeType: '',
    status: ''
})

const batchGrantVisible = ref(false)

const { pager, getLists, resetPage, resetParams } = usePaging({
    fetchFun: getCouponGoodsList,
    params: query
})

// ---------- 表单下拉（按需加载，避免只读角色请求无权限接口） ----------
const courseOptions = ref<any[]>([])
const areaOptions = ref<any[]>([])
const optionsLoaded = ref(false)

async function loadOptions() {
    if (optionsLoaded.value) return
    try {
        const data: any = await getCouponGoodsOptions()
        courseOptions.value = data?.courses || []
        areaOptions.value = data?.areas || []
        optionsLoaded.value = true
    } catch {
        // 请求层已提示；表单仍可打开，只是下拉为空
    }
}

// ---------- 新增 / 编辑 ----------
const formVisible = ref(false)
const saving = ref(false)
const formRef = ref()
const form = reactive<any>({
    id: 0,
    code: '',
    title: '',
    description: '',
    price: 0,
    times: 1,
    validDays: 30,
    scope: '',
    scopeType: 'group',
    courseIds: [],
    areaIds: [],
    perUser: 0,
    perStudent: 0,
    tag: '',
    sort: 0,
    status: 1
})

const filteredCourseOptions = computed(() => {
    const scopeType = form.scopeType
    if (scopeType === 'all') return []
    return courseOptions.value.filter((item: any) => {
        if (scopeType === 'trial') return item.type !== 'private'
        return item.type === scopeType
    })
})

const formRules = {
    code: [
        { required: true, message: '请输入商品编码', trigger: 'blur' },
        {
            pattern: /^[a-z0-9][a-z0-9_-]{0,49}$/,
            message: '商品编码只能包含小写字母、数字、下划线和短横线，且不能以符号开头',
            trigger: 'blur'
        }
    ],
    title: [
        { required: true, message: '请输入券名称', trigger: 'blur' },
        { max: 100, message: '券名称不能超过100个字符', trigger: 'blur' }
    ],
    description: [{ max: 255, message: '商品简介不能超过255个字符', trigger: 'blur' }],
    price: [{ required: true, message: '请输入售价', trigger: 'change' }],
    times: [{ required: true, message: '请输入包含次数', trigger: 'change' }],
    validDays: [{ required: true, message: '请输入有效天数', trigger: 'change' }],
    scopeType: [{ required: true, message: '请选择适用范围类型', trigger: 'change' }]
}

function resetForm() {
    form.id = 0
    form.code = ''
    form.title = ''
    form.description = ''
    form.price = 0
    form.times = 1
    form.validDays = 30
    form.scope = ''
    form.scopeType = 'group'
    form.courseIds = []
    form.areaIds = []
    form.perUser = 0
    form.perStudent = 0
    form.tag = ''
    form.sort = 0
    form.status = 1
    formRef.value?.clearValidate()
}

async function openForm(row?: any) {
    resetForm()
    await loadOptions()
    if (row) {
        form.id = row.id
        form.code = row.code || ''
        form.title = row.title || ''
        form.description = row.description || ''
        form.price = Number(row.price || 0)
        form.times = Number(row.times || 1)
        form.validDays = Number(row.validDays || 0)
        form.scope = row.scope || ''
        form.scopeType = row.scopeType || 'group'
        form.courseIds = splitIds(row.courseIds)
        form.areaIds = splitIds(row.areaIds)
        form.perUser = Number(row.perUser || 0)
        form.perStudent = Number(row.perStudent || 0)
        form.tag = row.tag || ''
        form.sort = Number(row.sort || 0)
        form.status = Number(row.status) === 0 ? 0 : 1
        formRef.value?.clearValidate()
    }
    formVisible.value = true
}

watch(
    () => form.scopeType,
    (scopeType) => {
        if (scopeType === 'all') {
            form.courseIds = []
            return
        }
        const allowed = new Set(filteredCourseOptions.value.map((item: any) => item.id))
        form.courseIds = form.courseIds.filter((id: number) => allowed.has(id))
    }
)

function formPayload() {
    return {
        id: form.id || undefined,
        code: String(form.code || '').trim(),
        title: String(form.title || '').trim(),
        description: String(form.description || '').trim(),
        price: Number(form.price || 0),
        times: Number(form.times || 0),
        validDays: Number(form.validDays || 0),
        scope: String(form.scope || '').trim(),
        scopeType: form.scopeType,
        courseIds: form.courseIds.join(','),
        areaIds: form.areaIds.join(','),
        perUser: Number(form.perUser || 0),
        perStudent: Number(form.perStudent || 0),
        tag: String(form.tag || '').trim(),
        sort: Number(form.sort || 0),
        status: Number(form.status)
    }
}

async function submitForm() {
    const valid = await formRef.value?.validate().catch(() => false)
    if (!valid || saving.value) return
    saving.value = true
    try {
        const payload = formPayload()
        if (form.id) await editCouponGoods(payload)
        else await addCouponGoods(payload)
        formVisible.value = false
        feedback.msgSuccess('保存成功')
        await getLists()
    } catch {
        // 请求层已提示；保留弹窗方便修正
    } finally {
        saving.value = false
    }
}

// ---------- 上下架 / 排序 / 删除 ----------
const deletingId = ref(0)

async function toggleStatus(row: any) {
    const target = Number(row.status) === 1 ? 0 : 1
    const action = target === 1 ? '上架' : '下架'
    try {
        await feedback.confirm(
            `确定${action}券商品「${row.title}」？${
                target === 0
                    ? '下架后小程序券商城不再展示该商品，已售出的券和订单不受影响。'
                    : '上架后小程序券商城恢复展示。'
            }`
        )
    } catch {
        return
    }
    try {
        await statusCouponGoods({ id: row.id, status: target })
        feedback.msgSuccess(`已${action}`)
        await getLists()
    } catch {
        // 请求层已提示
    }
}

async function handleDelete(row: any) {
    try {
        await feedback.confirm(
            `确定删除券商品「${row.title}」？删除为软删除；已有券实例或订单引用时会被拒绝，请改为下架。`
        )
    } catch {
        return
    }
    deletingId.value = row.id
    try {
        const res: any = await delCouponGoods({ id: row.id })
        if (res && Number(res.code) === 1) {
            feedback.msgSuccess('删除成功')
            await getLists()
            return
        }
        const message = res?.msg || '删除失败'
        if (message.includes('不能删除')) {
            try {
                await feedback.confirm(
                    `${message}\n\n是否改为下架？下架后小程序不再展示，历史券和订单保留。`
                )
                await statusCouponGoods({ id: row.id, status: 0 })
                feedback.msgSuccess('已下架')
                await getLists()
            } catch {
                // 用户取消下架
            }
            return
        }
        feedback.msgError(message)
    } catch (error: any) {
        feedback.msgError(error?.message || '删除失败')
    } finally {
        deletingId.value = 0
    }
}

const sortVisible = ref(false)
const sortSaving = ref(false)
const sortRow = ref<any>({})
const sortValue = ref(0)

function openSort(row: any) {
    sortRow.value = row
    sortValue.value = Number(row.sort || 0)
    sortVisible.value = true
}

async function submitSort() {
    if (sortSaving.value) return
    sortSaving.value = true
    try {
        await sortCouponGoods({ id: sortRow.value.id, sort: Number(sortValue.value || 0) })
        sortVisible.value = false
        feedback.msgSuccess('排序已更新')
        await getLists()
    } catch {
        // 请求层已提示
    } finally {
        sortSaving.value = false
    }
}

// ---------- 发券（#207） ----------
// 手机号 → 账号复用老接口 /coupon/students（按昵称/姓名/手机号/账号模糊匹配，
// 这里只取手机号完全相等的那条）；学员下拉复用 /user.user/students。
const grantVisible = ref(false)
const grantSaving = ref(false)
const grantFormRef = ref()
const grantRow = ref<any>({})
const grantAccount = ref<any>(null)
const grantAccountState = ref<'idle' | 'found' | 'missing'>('idle')
const grantStudentOptions = ref<any[]>([])
const grantStudentLoading = ref(false)
const grantForm = reactive<any>({
    mobile: '',
    studentId: 0,
    quantity: 1,
    remark: ''
})

const grantRules = {
    mobile: [
        { required: true, message: '请输入家长手机号', trigger: 'blur' },
        { pattern: /^1\d{10}$/, message: '请输入正确的 11 位手机号', trigger: 'blur' }
    ],
    quantity: [{ required: true, message: '请输入发放张数', trigger: 'change' }]
}

const grantAccountLabel = computed(() => {
    const account = grantAccount.value || {}
    return account.nickname || account.realName || account.account || `#${account.id}`
})

function resetGrantForm() {
    grantForm.mobile = ''
    grantForm.studentId = 0
    grantForm.quantity = 1
    grantForm.remark = ''
    grantAccount.value = null
    grantAccountState.value = 'idle'
    grantStudentOptions.value = []
    grantFormRef.value?.clearValidate()
}

function openGrant(row: any) {
    // 下架商品按钮已禁用；这里兜底，防止通过其他方式触发后走到后端才报错
    if (Number(row.status) !== 1) {
        feedback.msgWarning('券商品已下架，不能发放')
        return
    }
    resetGrantForm()
    grantRow.value = row
    grantVisible.value = true
}

async function resolveGrantAccount() {
    const mobile = String(grantForm.mobile || '').trim()
    // 手机号一变，旧账号与学员立即失效，避免券发给上一个号码名下的学员
    grantAccount.value = null
    grantStudentOptions.value = []
    grantForm.studentId = 0
    if (!/^1\d{10}$/.test(mobile)) {
        grantAccountState.value = 'idle'
        return
    }
    grantAccountState.value = 'idle'
    try {
        const list: any[] = (await getCouponStudents({ keyword: mobile })) || []
        const account = list.find((item: any) => String(item.mobile || '') === mobile)
        if (!account) {
            grantAccountState.value = 'missing'
            return
        }
        grantAccount.value = account
        grantAccountState.value = 'found'
        await loadGrantStudents(account.id)
    } catch {
        // 请求层已提示；保留手机号，提交时后端仍会给出准确文案
    }
}

async function loadGrantStudents(userId: number) {
    grantStudentLoading.value = true
    try {
        const list: any[] = (await getUserStudents({ userId })) || []
        grantStudentOptions.value = list
    } catch {
        grantStudentOptions.value = []
    } finally {
        grantStudentLoading.value = false
    }
}

async function submitGrant() {
    const valid = await grantFormRef.value?.validate().catch(() => false)
    if (!valid || grantSaving.value) return
    const mobile = String(grantForm.mobile || '').trim()
    if (grantAccount.value && String(grantAccount.value.mobile || '') !== mobile) {
        // 手机号在 blur 之后又被修改：重新解析，保证学员下拉与新号码一致
        await resolveGrantAccount()
    }
    grantSaving.value = true
    try {
        const data: any = await grantCoupon({
            goodsId: grantRow.value.id,
            mobile,
            studentId: Number(grantForm.studentId || 0),
            quantity: Number(grantForm.quantity || 1),
            remark: String(grantForm.remark || '').trim()
        })
        grantVisible.value = false
        const sns: string[] = data?.couponSns || []
        const total = Number(data?.couponSnTotal || data?.quantity || 0)
        const suffix = total > sns.length ? ` 等共 ${total} 张` : ''
        feedback.alertSuccess(`已发 ${data?.quantity ?? total} 张，券号：${sns.join('、')}${suffix}`)
    } catch {
        // 请求层已展示后端文案（未注册/禁用/下架等），保留弹窗便于修正
    } finally {
        grantSaving.value = false
    }
}

// ---------- 已发券 / 撤回（#213 C2） ----------
const grantedVisible = ref(false)
const grantedQuery = reactive({ mobile: '', sn: '' })
const { pager: grantedPager, getLists: getGranted, resetPage: resetGrantedPage } = usePaging({
    fetchFun: getGrantedCoupons,
    params: grantedQuery
})
const revokingSn = ref('')

function searchGranted() {
    const mobile = String(grantedQuery.mobile || '').trim()
    const sn = String(grantedQuery.sn || '').trim()
    if (!mobile && !sn) {
        feedback.msgWarning('请输入手机号或券号后再查询')
        return
    }
    grantedQuery.mobile = mobile
    grantedQuery.sn = sn
    resetGrantedPage()
}

function resetGrantedQuery() {
    grantedQuery.mobile = ''
    grantedQuery.sn = ''
    grantedPager.lists = []
    grantedPager.count = 0
}

function couponStatusTag(status: any) {
    const value = Number(status)
    if (value === 1) return 'success'
    if (value === 4) return 'danger'
    return 'info'
}

async function handleRevoke(row: any) {
    try {
        await feedback.confirm(
            `确定撤回券 ${row.sn}（${row.title || ''}）？撤回后立即作废，家长不能再使用，操作不可撤销。`
        )
    } catch {
        return
    }
    revokingSn.value = row.sn
    try {
        await revokeCoupon({ sn: row.sn })
        feedback.msgSuccess('已撤回该券')
        await getGranted()
    } catch {
        // 请求层已提示（已核销/已作废/已过期等）
    } finally {
        revokingSn.value = ''
    }
}

// ---------- 展示工具 ----------
function splitIds(text: any) {
    return String(text || '')
        .split(',')
        .map((item) => Number(item.trim()))
        .filter((id) => Number.isInteger(id) && id > 0)
}

function scopeTypeLabel(value: any) {
    const item = SCOPE_TYPE_OPTIONS.find((option) => option.value === value)
    return item ? item.label : value || '-'
}

function scopeTypeTag(value: any) {
    if (value === 'group') return 'success'
    if (value === 'private') return 'warning'
    if (value === 'trial') return ''
    return 'info'
}

function courseOptionLabel(item: any) {
    const parts = [item.type === 'private' ? '私教' : item.type === 'trial' ? '体验' : '团课']
    if (Number(item.isShow) === 0) parts.push('已下架')
    const area = item.areaName ? `·${item.areaName}` : ''
    return `${item.name}（${parts.join('·')}${area}）`
}

function areaOptionLabel(item: any) {
    return item.communityId ? `${item.name}（社区#${item.communityId}）` : item.name
}

function money(value: any) {
    if (value === null || value === undefined || value === '') return '-'
    return `¥${Number(value).toFixed(2)}`
}

function validDaysText(value: any) {
    const days = Number(value || 0)
    return days > 0 ? `${days} 天` : '长期有效'
}

function limitText(row: any) {
    const perStudent = Number(row.perStudent || 0)
    const perUser = Number(row.perUser || 0)
    if (perStudent > 0) return `每学员 ${perStudent} 份`
    if (perUser > 0) return `每账号 ${perUser} 份`
    return '不限购'
}

function formatSeconds(seconds: any) {
    if (!seconds) return '-'
    const date = new Date(Number(seconds) * 1000)
    if (Number.isNaN(date.getTime())) return '-'
    const pad = (value: number) => String(value).padStart(2, '0')
    return `${date.getFullYear()}-${pad(date.getMonth() + 1)}-${pad(date.getDate())} ${pad(
        date.getHours()
    )}:${pad(date.getMinutes())}`
}

getLists().catch(() => {})

// keep-alive 首次挂载也会触发 onActivated；跳过首次，避免同 URL 请求被取消
let pageActivated = false
onActivated(() => {
    if (!pageActivated) {
        pageActivated = true
        return
    }
    getLists().catch(() => {})
})
</script>

<style lang="scss" scoped>
.form-section-title {
    margin: 4px 0 16px;
    padding-left: 8px;
    border-left: 3px solid var(--el-color-primary);
    font-size: 14px;
    font-weight: 600;
    line-height: 16px;
}
.scope-main {
    display: flex;
    align-items: center;
    gap: 4px;
    line-height: 20px;
}
.scope-sub {
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
}
</style>
