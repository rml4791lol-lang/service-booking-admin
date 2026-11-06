<template>
    <div class="student-page">
        <el-card class="!border-none" shadow="never">
            <el-form ref="formRef" class="mb-[-16px]" :model="queryParams" :inline="true">
                <el-form-item label="姓名">
                    <el-input
                        data-test="filter-name"
                        class="w-[180px]"
                        v-model="queryParams.name"
                        placeholder="家长/学员姓名"
                        clearable
                        @keyup.enter="resetPage"
                    />
                </el-form-item>
                <el-form-item label="手机号">
                    <el-input
                        data-test="filter-mobile"
                        class="w-[180px]"
                        v-model="queryParams.mobile"
                        placeholder="请输入手机号"
                        clearable
                        @keyup.enter="resetPage"
                    />
                </el-form-item>
                <el-form-item label="所在班级">
                    <el-select
                        data-test="filter-class"
                        class="w-[220px]"
                        v-model="queryParams.classId"
                        placeholder="全部班级"
                        clearable
                        filterable
                    >
                        <el-option
                            v-for="item in classOptions"
                            :key="item.id"
                            :label="item.name"
                            :value="item.id"
                        />
                    </el-select>
                </el-form-item>
                <el-form-item label="用户信息">
                    <el-input
                        class="w-[180px]"
                        v-model="queryParams.keyword"
                        placeholder="账号/昵称/手机号码"
                        clearable
                        @keyup.enter="resetPage"
                    />
                </el-form-item>
                <el-form-item label="注册时间">
                    <daterange-picker
                        v-model:startTime="queryParams.create_time_start"
                        v-model:endTime="queryParams.create_time_end"
                    />
                </el-form-item>
                <el-form-item label="注册来源">
                    <el-select class="w-[160px]" v-model="queryParams.channel">
                        <el-option
                            v-for="(item, key) in ClientMap"
                            :key="key"
                            :label="item"
                            :value="key"
                        />
                    </el-select>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="resetPage">查询</el-button>
                    <el-button @click="resetParams">重置</el-button>
                    <export-data
                        class="ml-2.5"
                        v-perms="['user.user/export']"
                        :fetch-fun="getUserList"
                        :params="queryParams"
                        :page-size="pager.size"
                    />
                </el-form-item>
            </el-form>
        </el-card>
        <el-card class="!border-none mt-4" shadow="never">
            <div class="mb-4 flex items-center justify-between">
                <div>
                    <div class="text-lg font-medium">学员列表</div>
                    <div class="mt-1 text-sm text-tx-secondary">
                        支持按姓名/手机号/班级筛选；新增学员即后台为家长建号（手机号=登录账号）。
                    </div>
                </div>
                <div class="flex items-center gap-3">
                    <el-button
                        v-perms="['user.user/adjustMoney']"
                        @click="batchAdjustVisible = true"
                    >
                        批量调余额
                    </el-button>
                    <el-button
                        data-test="add-student"
                        v-perms="['user.user/add']"
                        type="primary"
                        @click="addState.show = true"
                    >
                        新增学员
                    </el-button>
                </div>
            </div>
            <el-table size="large" v-loading="pager.loading" :data="pager.lists">
                <el-table-column label="头像" min-width="90">
                    <template #default="{ row }">
                        <el-avatar :src="row.avatar" :size="50" />
                    </template>
                </el-table-column>
                <el-table-column label="昵称" prop="nickname" min-width="120" show-overflow-tooltip />
                <el-table-column label="真实姓名" prop="realName" min-width="100" show-overflow-tooltip>
                    <template #default="{ row }">{{ row.realName || '-' }}</template>
                </el-table-column>
                <el-table-column label="账号" prop="account" min-width="110" show-overflow-tooltip />
                <el-table-column label="手机号码" prop="mobile" min-width="110" />
                <el-table-column label="注册来源" prop="channel" min-width="90" />
                <el-table-column label="注册时间" prop="createTime" min-width="150" />
                <el-table-column label="操作" width="100" fixed="right">
                    <template #default="{ row }">
                        <el-button v-perms="['user.user/detail']" type="primary" link>
                            <router-link
                                :to="{
                                    path: getRoutePath('user.user/detail'),
                                    query: {
                                        id: row.id
                                    }
                                }"
                            >
                                详情
                            </router-link>
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
            <div class="flex justify-end mt-4">
                <pagination v-model="pager" @change="getLists" />
            </div>
        </el-card>

        <student-add v-model:show="addState.show" @success="handleAddSuccess" />

        <!-- 批量调余额（批量导入）：CSV 前端解析 → 预览校验 → 提交 → 失败行可下载 -->
        <batch-adjust v-model="batchAdjustVisible" @success="getLists" />
    </div>
</template>
<script lang="ts" setup name="consumerLists">
import { onMounted, ref } from 'vue'

import { getUserList } from '@/api/consumer'
import { classList } from '@/api/courseClass'
import { ClientMap } from '@/enums/appEnums'
import { usePaging } from '@/hooks/usePaging'
import { getRoutePath } from '@/router'

import BatchAdjust from './components/batch-adjust.vue'
import StudentAdd from '../components/student-add.vue'

const queryParams = reactive({
    name: '',
    mobile: '',
    classId: '',
    keyword: '',
    channel: '',
    create_time_start: '',
    create_time_end: ''
})

const { pager, getLists, resetPage, resetParams } = usePaging({
    fetchFun: getUserList,
    params: queryParams
})

const classOptions = ref<{ id: number; name: string }[]>([])
const addState = reactive({
    show: false
})
const batchAdjustVisible = ref(false)

const getClassOptions = async () => {
    const pageSize = 50
    const options: { id: number; name: string }[] = []
    let pageNo = 1
    let total = 0
    do {
        const data = await classList({ page_no: pageNo, page_size: pageSize })
        const lists = data?.lists || []
        options.push(...lists.map((item: any) => ({ id: item.id, name: item.name })))
        total = data?.count || 0
        pageNo += 1
    } while (options.length < total && pageNo <= 20)
    classOptions.value = options
}

const handleAddSuccess = () => {
    resetPage()
}

onActivated(() => {
    getLists()
})

onMounted(() => {
    getClassOptions()
})

getLists()
</script>
