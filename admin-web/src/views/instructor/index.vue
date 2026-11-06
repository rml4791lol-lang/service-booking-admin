<template>
    <div class="instructor-page">
        <el-card class="!border-none" shadow="never">
            <div class="flex items-center justify-between">
                <div class="text-lg font-medium">教练资料</div>
                <div class="text-sm text-gray-400">共 {{ pager.count }} 位教练</div>
            </div>
        </el-card>

        <el-card class="!border-none mt-4" shadow="never">
            <el-form ref="formRef" class="mb-[-16px]" :model="queryParams" :inline="true">
                <el-form-item label="教练姓名">
                    <el-input
                        v-model="queryParams.name"
                        class="w-[220px]"
                        placeholder="输入姓名"
                        clearable
                        @keyup.enter="resetPage"
                    />
                </el-form-item>
                <el-form-item label="手机号">
                    <el-input
                        v-model="queryParams.phone"
                        class="w-[220px]"
                        placeholder="输入手机号"
                        clearable
                        @keyup.enter="resetPage"
                    />
                </el-form-item>
                <el-form-item label="状态">
                    <el-select v-model="queryParams.status" class="w-[180px]">
                        <el-option label="全部" value="" />
                        <el-option label="正常" :value="1" />
                        <el-option label="待审核" :value="2" />
                        <el-option label="停用" :value="0" />
                    </el-select>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="resetPage">查询</el-button>
                    <el-button @click="resetParams">重置</el-button>
                </el-form-item>
            </el-form>
        </el-card>

        <el-card class="!border-none mt-4" shadow="never">
            <div class="mb-4">
                <el-button
                    v-perms="['instructor/add', 'instructor.instructor:add']"
                    type="primary"
                    @click="openEdit()"
                >
                    <template #icon>
                        <icon name="el-icon-Plus" />
                    </template>
                    新增教练
                </el-button>
            </div>
            <el-table v-loading="pager.loading" :data="pager.lists" size="large">
                <el-table-column label="教练" min-width="220">
                    <template #default="{ row }">
                        <div class="flex items-center gap-3">
                            <el-avatar :size="44" :src="row.avatar">
                                {{ (row.name || '').slice(0, 1) }}
                            </el-avatar>
                            <div class="min-w-0">
                                <div class="font-medium">{{ row.name || '-' }}</div>
                                <div class="line-clamp-1 text-xs text-gray-400">
                                    {{ row.introduction || '—' }}
                                </div>
                            </div>
                        </div>
                    </template>
                </el-table-column>
                <el-table-column label="手机号" prop="phone" min-width="130" />
                <el-table-column label="身份证" min-width="180">
                    <template #default="{ row }">{{ maskIdCard(row.idCard) }}</template>
                </el-table-column>
                <el-table-column label="专业领域" prop="specialty" min-width="120">
                    <template #default="{ row }">{{ row.specialty || '-' }}</template>
                </el-table-column>
                <el-table-column label="教练资质" min-width="200">
                    <template #default="{ row }">
                        <template v-if="splitTags(row.certification).length">
                            <el-tag
                                v-for="item in splitTags(row.certification)"
                                :key="item"
                                size="small"
                                class="mr-1 mb-1"
                            >
                                {{ item }}
                            </el-tag>
                        </template>
                        <span v-else class="text-gray-400">-</span>
                    </template>
                </el-table-column>
                <el-table-column label="可授课程" min-width="180">
                    <template #default="{ row }">
                        <template v-if="courseNames(row.teachableCourses).length">
                            <el-tag
                                v-for="item in courseNames(row.teachableCourses)"
                                :key="item"
                                size="small"
                                class="mr-1 mb-1"
                            >
                                {{ item }}
                            </el-tag>
                        </template>
                        <span v-else class="text-gray-400">-</span>
                    </template>
                </el-table-column>
                <el-table-column label="服务区域" min-width="160">
                    <template #default="{ row }">
                        <template v-if="areaNames(row.serviceAreas).length">
                            <el-tag
                                v-for="item in areaNames(row.serviceAreas)"
                                :key="item"
                                size="small"
                                type="success"
                                class="mr-1 mb-1"
                            >
                                {{ item }}
                            </el-tag>
                        </template>
                        <span v-else class="text-gray-400">-</span>
                    </template>
                </el-table-column>
                <el-table-column label="班级" width="90">
                    <template #default="{ row }">{{ row.classCount || 0 }}</template>
                </el-table-column>
                <el-table-column label="账号状态" width="130">
                    <template #default="{ row }">
                        <el-tag :type="accountStatusType(row.accountStatus)" size="small">
                            {{ row.accountStatusLabel || '未开通' }}
                        </el-tag>
                        <div v-if="row.lastLoginTime" class="mt-1 text-xs text-gray-400">
                            {{ row.lastLoginTime }}
                        </div>
                    </template>
                </el-table-column>
                <el-table-column label="状态" width="100">
                    <template #default="{ row }">
                        <el-tag :type="statusType(row.status)" size="small">
                            {{ statusLabel(row.status) }}
                        </el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="操作" width="260" fixed="right">
                    <template #default="{ row }">
                        <el-button
                            v-perms="['instructor/detail', 'instructor.instructor:detail']"
                            type="primary"
                            link
                            @click="openDetail(row)"
                        >
                            详情
                        </el-button>
                        <el-button
                            v-perms="['instructor/edit', 'instructor.instructor:edit']"
                            type="primary"
                            link
                            @click="openEdit(row)"
                        >
                            编辑
                        </el-button>
                        <el-button
                            v-perms="['instructor/account/status']"
                            type="warning"
                            link
                            @click="openAccount(row)"
                        >
                            账号
                        </el-button>
                        <el-button
                            v-perms="['instructor/del', 'instructor.instructor:del']"
                            type="danger"
                            link
                            @click="handleDelete(row)"
                        >
                            删除
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
            <div class="mt-4 flex justify-end">
                <pagination v-model="pager" @change="getLists" />
            </div>
        </el-card>

        <EditPopup v-if="showEdit" ref="editRef" @success="getLists" @close="showEdit = false" />
        <DetailPopup v-if="showDetail" ref="detailRef" @close="showDetail = false" />
        <AccountPopup v-if="showAccount" ref="accountRef" @success="getLists" @close="showAccount = false" />
    </div>
</template>

<script lang="ts" setup name="instructor">
import { delInstructor, getInstructorList, getInstructorOptions } from '@/api/instructor'
import { usePaging } from '@/hooks/usePaging'
import feedback from '@/utils/feedback'

import AccountPopup from './account.vue'
import DetailPopup from './detail.vue'
import EditPopup from './edit.vue'

const showEdit = ref(false)
const showDetail = ref(false)
const showAccount = ref(false)
const editRef = shallowRef<InstanceType<typeof EditPopup>>()
const detailRef = shallowRef<InstanceType<typeof DetailPopup>>()
const accountRef = shallowRef<InstanceType<typeof AccountPopup>>()
const categories = ref<any[]>([])
const communities = ref<any[]>([])

const queryParams = reactive<any>({
    name: '',
    phone: '',
    status: ''
})

const { pager, getLists, resetPage, resetParams } = usePaging({
    fetchFun: getInstructorList,
    params: queryParams
})

const categoryMap = computed(() => {
    const map = new Map<number, string>()
    categories.value.forEach((item) => map.set(item.id, item.name))
    return map
})

const communityMap = computed(() => {
    const map = new Map<number, string>()
    communities.value.forEach((item) => map.set(item.id, item.name))
    return map
})

function splitTags(value: string) {
    return value ? String(value).split(',').filter((item: string) => item) : []
}

function parseIds(value: any) {
    return String(value || '')
        .split(',')
        .filter((item: string) => item)
        .map((item: string) => Number(item))
}

function courseNames(value: any) {
    return parseIds(value).map((id: number) => categoryMap.value.get(id) || `#${id}`)
}

function areaNames(value: any) {
    return parseIds(value).map((id: number) => communityMap.value.get(id) || `#${id}`)
}

function maskIdCard(value: string) {
    if (!value) return '-'
    const text = String(value)
    if (text.length <= 8) return text
    return `${text.slice(0, 4)}**********${text.slice(-4)}`
}

function statusLabel(status: number) {
    return status === 1 ? '正常' : status === 2 ? '待审核' : '停用'
}

function statusType(status: number) {
    return status === 1 ? 'success' : status === 2 ? 'warning' : 'info'
}

async function loadOptions() {
    try {
        const data: any = await getInstructorOptions()
        categories.value = Array.isArray(data?.categories) ? data.categories : []
        communities.value = Array.isArray(data?.communities) ? data.communities : []
    } catch {
        // 请求层已提示错误
    }
}

const openEdit = async (row?: any) => {
    showEdit.value = true
    await nextTick()
    editRef.value?.open(row ? 'edit' : 'add')
    if (row) {
        await editRef.value?.getDetail(row)
    }
}

function openAccount(row: any) {
    showAccount.value = true
    nextTick(() => accountRef.value?.open(row))
}

function accountStatusType(status: string) {
    if (status === 'ACTIVE') return 'success'
    if (status === 'PENDING_CHANGE') return 'warning'
    if (status === 'BLOCKED' || status === 'DISABLED') return 'danger'
    return 'info'
}

const openDetail = async (row: any) => {
    showDetail.value = true
    await nextTick()
    detailRef.value?.open(row)
}

const handleDelete = async (row: any) => {
    await feedback.confirm(`确定要删除教练「${row.name}」？`)
    await delInstructor({ id: row.id })
    feedback.msgSuccess('删除成功')
    getLists()
}

loadOptions()
getLists()
</script>
