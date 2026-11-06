<template>
    <div class="legacy-course-page" data-test="legacy-course-page">
        <el-card class="!border-none" shadow="never">
            <el-form class="mb-[-16px]" :model="queryParams" :inline="true">
                <el-form-item label="学员姓名">
                    <el-input
                        v-model="queryParams.studentName"
                        data-test="filter-student-name"
                        class="w-[180px]"
                        placeholder="学员/家长姓名"
                        clearable
                        @keyup.enter="resetPage"
                    />
                </el-form-item>
                <el-form-item label="手机号">
                    <el-input
                        v-model="queryParams.mobile"
                        data-test="filter-mobile"
                        class="w-[180px]"
                        placeholder="请输入手机号"
                        clearable
                        @keyup.enter="resetPage"
                    />
                </el-form-item>
                <el-form-item label="课程名">
                    <el-input
                        v-model="queryParams.className"
                        data-test="filter-class-name"
                        class="w-[200px]"
                        placeholder="请输入课程名"
                        clearable
                        @keyup.enter="resetPage"
                    />
                </el-form-item>
                <el-form-item label="状态">
                    <el-select
                        v-model="queryParams.status"
                        data-test="filter-status"
                        class="w-[140px]"
                        placeholder="全部状态"
                        clearable
                    >
                        <el-option label="学习中" :value="0" />
                        <el-option label="已完成" :value="1" />
                        <el-option label="已退课" :value="2" />
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
                <div class="text-lg font-medium">课时账户（老课包）</div>
                <div class="mt-1 text-sm text-tx-secondary">
                    数据来自老系统 la_user_course，一行一个课包；「已用/剩余」按消课流水派生
                    （含调整流水，与学员详情/退款重算同口径），与账面字段不一致时标「差异」。
                </div>
            </div>
            <el-table v-loading="pager.loading" :data="pager.lists" data-test="legacy-course-table">
                <el-table-column
                    label="学员"
                    prop="studentName"
                    min-width="100"
                    show-overflow-tooltip
                >
                    <template #default="{ row }">{{ row.studentName || '-' }}</template>
                </el-table-column>
                <el-table-column label="手机号" prop="mobile" min-width="120">
                    <template #default="{ row }">{{ row.mobile || '-' }}</template>
                </el-table-column>
                <el-table-column
                    label="课程"
                    prop="className"
                    min-width="180"
                    show-overflow-tooltip
                >
                    <template #default="{ row }">{{ row.className || '-' }}</template>
                </el-table-column>
                <el-table-column label="总课时" prop="totalLessons" width="90" />
                <el-table-column label="已用" width="90">
                    <template #default="{ row }">
                        <span>{{ row.usedLessons }}</span>
                        <el-tag v-if="row.fieldMismatch" class="ml-1" type="warning" size="small"
                            >差异</el-tag
                        >
                    </template>
                </el-table-column>
                <el-table-column label="剩余" width="90">
                    <template #default="{ row }">
                        <span>{{ row.remainLessons }}</span>
                        <el-tag v-if="row.fieldMismatch" class="ml-1" type="warning" size="small"
                            >差异</el-tag
                        >
                    </template>
                </el-table-column>
                <el-table-column label="系统已上" prop="systemUsedLessons" width="100">
                    <template #header>
                        <el-tooltip content="按消课流水重算，口径与学员详情一致" placement="top">
                            <span class="cursor-help border-b border-dashed border-current"
                                >系统已上</span
                            >
                        </el-tooltip>
                    </template>
                </el-table-column>
                <el-table-column label="状态" width="100">
                    <template #default="{ row }">
                        <el-tag
                            :type="
                                row.status === 2 ? 'danger' : row.status === 1 ? 'info' : 'success'
                            "
                        >
                            {{ row.statusText }}
                        </el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="创建时间" prop="createTime" min-width="160" />
                <el-table-column label="更新时间" prop="updateTime" min-width="160" />
                <el-table-column label="操作" width="90" fixed="right">
                    <template #default="{ row }">
                        <el-button
                            v-perms="['user_course/adjust']"
                            type="primary"
                            link
                            data-test="adjust-btn"
                            @click="openAdjust(row)"
                        >
                            调整
                        </el-button>
                    </template>
                </el-table-column>
                <template #empty>
                    <el-empty description="暂无老课包数据" :image-size="80" />
                </template>
            </el-table>
            <div class="flex justify-end mt-4">
                <pagination v-model="pager" @change="getLists" />
            </div>
        </el-card>

        <course-adjust
            v-model:show="adjustState.show"
            :row="adjustState.row"
            @success="resetPage"
        />
    </div>
</template>

<script lang="ts" setup name="consumerLegacyCourse">
import { onActivated, reactive } from 'vue'

import { getUserCourseList } from '@/api/userCourse'
import { usePaging } from '@/hooks/usePaging'

import CourseAdjust from '../components/course-adjust.vue'

const queryParams = reactive({
    studentName: '',
    mobile: '',
    className: '',
    status: ''
})

const { pager, getLists, resetPage, resetParams } = usePaging({
    fetchFun: getUserCourseList,
    params: queryParams
})

const adjustState = reactive<{ show: boolean; row: Record<string, any> }>({
    show: false,
    row: {}
})

const openAdjust = (row: Record<string, any>) => {
    adjustState.row = row
    adjustState.show = true
}

onActivated(() => {
    getLists()
})

getLists()
</script>
