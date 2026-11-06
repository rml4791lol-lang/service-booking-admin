<template>
    <div class="student-detail-page">
        <el-card class="!border-none" shadow="never">
            <el-page-header content="用户详情" @back="$router.back()" />
        </el-card>
        <el-card class="mt-4 !border-none" header="基本资料" shadow="never">
            <el-form ref="formRef" class="ls-form" :model="formData" label-width="120px">
                <div class="bg-page flex py-5 mb-10 items-center">
                    <div class="basis-40 flex flex-col justify-center items-center">
                        <div class="mb-2 text-tx-regular">用户头像</div>
                        <el-avatar :src="formData.avatar" :size="58" />
                    </div>
                    <div class="basis-40 flex flex-col justify-center items-center">
                        <div class="text-tx-regular">账户余额</div>
                        <div class="mt-2 flex items-center">
                            ¥{{ formData.user_money }}
                            <el-button
                                v-perms="['user.user/adjustMoney']"
                                type="primary"
                                link
                                @click="handleAdjust(formData.user_money)"
                            >
                                调整
                            </el-button>
                        </div>
                    </div>
                </div>
                <el-form-item label="用户昵称：">
                    {{ formData.nickname }}
                </el-form-item>
                <el-form-item label="账号：">
                    {{ formData.account }}
                    <popover-input
                        class="ml-[10px]"
                        @confirm="handleEdit($event, 'account')"
                        :limit="32"
                        v-perms="['user.user/edit']"
                    >
                        <el-button type="primary" link>
                            <icon name="el-icon-EditPen" />
                        </el-button>
                    </popover-input>
                </el-form-item>
                <el-form-item label="真实姓名：">
                    {{ formData.real_name || '-' }}
                    <popover-input
                        class="ml-[10px]"
                        @confirm="handleEdit($event, 'real_name')"
                        :limit="32"
                        v-perms="['user.user/edit']"
                    >
                        <el-button type="primary" link>
                            <icon name="el-icon-EditPen" />
                        </el-button>
                    </popover-input>
                </el-form-item>
                <el-form-item label="性别：">
                    {{ formData.sex }}
                    <popover-input
                        class="ml-[10px]"
                        type="select"
                        :options="[
                            {
                                label: '未知',
                                value: 0
                            },
                            {
                                label: '男',
                                value: 1
                            },
                            {
                                label: '女',
                                value: 2
                            }
                        ]"
                        @confirm="handleEdit($event, 'sex')"
                        v-perms="['user.user/edit']"
                    >
                        <el-button type="primary" link>
                            <icon name="el-icon-EditPen" />
                        </el-button>
                    </popover-input>
                </el-form-item>
                <el-form-item label="联系电话：">
                    {{ formData.mobile || '-' }}
                    <popover-input
                        class="ml-[10px]"
                        type="number"
                        @confirm="handleEdit($event, 'mobile')"
                        v-perms="['user.user/edit']"
                    >
                        <el-button type="primary" link>
                            <icon name="el-icon-EditPen" />
                        </el-button>
                    </popover-input>
                </el-form-item>
                <el-form-item label="注册来源："> {{ formData.channel }} </el-form-item>
                <el-form-item label="注册时间："> {{ formData.create_time }} </el-form-item>
                <el-form-item label="最近登录时间："> {{ formData.login_time }} </el-form-item>
            </el-form>
        </el-card>

        <el-card
            data-test="student-profile"
            class="mt-4 !border-none"
            shadow="never"
            v-loading="profileLoading"
        >
            <template #header>
                <div class="flex items-center justify-between">
                    <span class="text-lg font-medium">学员档案</span>
                    <span class="text-sm text-tx-secondary">
                        购买课程包 / 已上·剩余课时 / 所在班级 / 关联学员
                    </span>
                </div>
            </template>
            <div class="mb-5 grid grid-cols-3 gap-4">
                <div class="rounded bg-page px-4 py-3">
                    <div class="text-sm text-tx-secondary">购买课时</div>
                    <div data-test="summary-purchased" class="mt-2 text-2xl font-semibold">
                        {{ summary.purchasedLessons }}
                    </div>
                </div>
                <div class="rounded bg-page px-4 py-3">
                    <div class="text-sm text-tx-secondary">已上课时</div>
                    <div data-test="summary-used" class="mt-2 text-2xl font-semibold">
                        {{ summary.usedLessons }}
                    </div>
                </div>
                <div class="rounded bg-page px-4 py-3">
                    <div class="text-sm text-tx-secondary">剩余课时</div>
                    <div data-test="summary-remain" class="mt-2 text-2xl font-semibold">
                        {{ summary.remainLessons }}
                    </div>
                </div>
            </div>

            <div class="mb-4 flex flex-wrap items-center gap-2">
                <span class="text-tx-secondary">关联学员：</span>
                <template v-if="students.length">
                    <el-tag v-for="student in students" :key="student.id" type="info">
                        {{ student.name }}
                        <span v-if="student.gender && student.gender !== '未知'">
                            （{{ student.gender }}）
                        </span>
                    </el-tag>
                </template>
                <span v-else class="text-tx-secondary">暂无关联学员</span>
            </div>

            <el-table :data="courses" class="w-full">
                <el-table-column label="所在班级" prop="className" min-width="180" show-overflow-tooltip />
                <el-table-column label="教练" prop="instructorName" min-width="100" />
                <el-table-column label="购买课时" prop="totalLessons" width="100" />
                <el-table-column label="已上课时" prop="usedLessons" width="100" />
                <el-table-column label="剩余课时" prop="remainLessons" width="100" />
                <el-table-column label="状态" width="110">
                    <template #default="{ row }">
                        <el-tag :type="row.status === 2 ? 'danger' : row.status === 1 ? 'info' : 'success'">
                            {{ row.statusText }}
                        </el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="报名时间" prop="enrollTime" min-width="150" />
                <template #empty>
                    <el-empty description="暂无购买课程包" :image-size="80" />
                </template>
            </el-table>
        </el-card>

        <el-card class="mt-4 !border-none" shadow="never">
            <template #header>
                <div class="flex items-center justify-between">
                    <span class="text-lg font-medium">上课明细</span>
                    <span class="text-sm text-tx-secondary">出勤 / 请假 / 消课记录</span>
                </div>
            </template>
            <div class="mb-4">
                <el-select
                    class="w-[280px]"
                    v-model="lessonFilter.courseClassId"
                    placeholder="全部班级"
                    clearable
                    @change="handleLessonFilterChange"
                >
                    <el-option
                        v-for="item in courseClassOptions"
                        :key="item.id"
                        :label="item.name"
                        :value="item.id"
                    />
                </el-select>
            </div>
            <el-table
                data-test="lesson-records"
                v-loading="lessonPager.loading"
                :data="lessonPager.list"
            >
                <el-table-column label="上课日期" prop="lessonDate" min-width="110">
                    <template #default="{ row }">{{ row.lessonDate || '-' }}</template>
                </el-table-column>
                <el-table-column label="时间段" prop="timeSlot" min-width="130">
                    <template #default="{ row }">{{ row.timeSlot || '-' }}</template>
                </el-table-column>
                <el-table-column label="班级" prop="className" min-width="160" show-overflow-tooltip />
                <el-table-column label="教练" prop="instructorName" min-width="100" />
                <el-table-column label="课次" width="100">
                    <template #default="{ row }">
                        {{ row.lessonNo > 0 ? `第${row.lessonNo}节` : '人工课节' }}
                    </template>
                </el-table-column>
                <el-table-column label="类型" prop="sourceTypeText" width="110" />
                <el-table-column label="出勤状态" width="100">
                    <template #default="{ row }">
                        <el-tag v-if="row.attendanceStatusText" :type="row.attendanceStatus === 2 ? 'warning' : row.attendanceStatus === 3 ? 'danger' : 'success'">
                            {{ row.attendanceStatusText }}
                        </el-tag>
                        <span v-else class="text-tx-secondary">-</span>
                    </template>
                </el-table-column>
                <el-table-column label="消课时间" prop="consumeTime" min-width="160" />
                <template #empty>
                    <el-empty description="暂无上课明细" :image-size="80" />
                </template>
            </el-table>
            <div class="mt-4 flex justify-end">
                <pagination
                    v-model="lessonPager"
                    :page-sizes="[10, 20, 50]"
                    @change="loadLessonRecords"
                />
            </div>
        </el-card>

        <account-adjust
            v-model:show="adjustState.show"
            :value="adjustState.value"
            :user-id="route.query.id"
            @success="handleAdjustSuccess"
        />
    </div>
</template>

<script lang="ts" setup name="consumerDetail">
import type { FormInstance } from 'element-plus'
import { computed, reactive, ref, shallowRef } from 'vue'

import {
    getLessonRecords,
    getUserAccount,
    getUserDetail,
    getUserStudents,
    userEdit
} from '@/api/consumer'
import { isEmpty } from '@/utils/util'

import AccountAdjust from '../components/account-adjust.vue'

const route = useRoute()
const formData = reactive({
    avatar: '',
    channel: '',
    create_time: '',
    login_time: '',
    mobile: '',
    nickname: '',
    real_name: 0,
    sex: 0,
    sn: '',
    account: '',
    user_money: ''
})

const adjustState = reactive({
    show: false,
    value: ''
})
const formRef = shallowRef<FormInstance>()

const profileLoading = ref(false)
const account = ref<any>(null)
const students = ref<any[]>([])

const summary = computed(() => {
    return {
        purchasedLessons: account.value?.summary?.purchasedLessons ?? 0,
        usedLessons: account.value?.summary?.usedLessons ?? 0,
        remainLessons: account.value?.summary?.remainLessons ?? 0
    }
})
const courses = computed<any[]>(() => account.value?.courses || [])
const courseClassOptions = computed(() => {
    const map = new Map<number, { id: number; name: string }>()
    courses.value.forEach((item: any) => {
        if (item.courseClassId && !map.has(item.courseClassId)) {
            map.set(item.courseClassId, { id: item.courseClassId, name: item.className })
        }
    })
    return Array.from(map.values())
})

const lessonFilter = reactive({
    courseClassId: ''
})
const lessonPager = reactive({
    page: 1,
    size: 10,
    count: 0,
    list: [] as any[],
    loading: false
})

const getDetails = async () => {
    const data = await getUserDetail({
        id: route.query.id
    }).catch(() => null)
    if (!data) return
    Object.keys(formData).forEach((key) => {
        //@ts-ignore
        formData[key] = data[key]
    })
    // 后端 UserVo 返回 camelCase（realName/userMoney/createTime/loginTime），映射到模板字段
    formData.real_name = data.realName || ''
    formData.user_money = data.userMoney ?? ''
    formData.create_time = data.createTime || ''
    formData.login_time = data.loginTime || ''
}

const getProfile = async () => {
    profileLoading.value = true
    try {
        const [accountData, studentData] = await Promise.all([
            getUserAccount({ userId: route.query.id }),
            getUserStudents({ userId: route.query.id })
        ])
        account.value = accountData || null
        students.value = studentData || []
    } catch (error) {
        account.value = null
        students.value = []
    } finally {
        profileLoading.value = false
    }
}

const loadLessonRecords = async () => {
    lessonPager.loading = true
    try {
        const data = await getLessonRecords({
            userId: route.query.id,
            courseClassId: lessonFilter.courseClassId || undefined,
            pageNo: lessonPager.page,
            pageSize: lessonPager.size
        })
        lessonPager.count = data?.total || 0
        lessonPager.list = data?.list || []
    } catch (error) {
        lessonPager.count = 0
        lessonPager.list = []
    } finally {
        lessonPager.loading = false
    }
}

const handleLessonFilterChange = () => {
    lessonPager.page = 1
    loadLessonRecords()
}

const handleEdit = async (value: string, field: string) => {
    if (isEmpty(value)) return
    await userEdit({
        id: route.query.id,
        field,
        value
    })
    getDetails()
}

const handleAdjust = (value: string) => {
    adjustState.show = true
    adjustState.value = value
}
const handleAdjustSuccess = () => {
    // 余额调整成功后刷新用户余额（弹窗由 account-adjust 自己关闭）
    getDetails()
}

getDetails()
getProfile()
loadLessonRecords()
</script>
