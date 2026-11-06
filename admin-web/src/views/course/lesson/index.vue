<template>
    <div class="course-lesson-page">
        <el-card class="!border-none" shadow="never">
            <div class="text-lg font-medium">课节明细</div>
            <div class="text-tx-secondary text-sm mt-1">
                按班级/日期查看课节与学员名单：课节状态、教练签到、点名/消课状态与课堂总结一屏核对；
                学员名单与消课流水来自预约/消课表，可直接定位漏点名与异常扣课。
            </div>
        </el-card>

        <el-card class="!border-none mt-4" shadow="never">
            <el-form class="mb-[-16px]" :inline="true">
                <el-form-item label="班级">
                    <el-select
                        v-model="query.classId"
                        class="w-[280px]"
                        clearable
                        filterable
                        remote
                        reserve-keyword
                        :remote-method="searchClasses"
                        :loading="classLoading"
                        placeholder="输入班级名称搜索"
                        @change="onClassChange"
                    >
                        <el-option
                            v-for="item in classOptions"
                            :key="item.id"
                            :label="item.name"
                            :value="item.id"
                        />
                    </el-select>
                </el-form-item>
                <el-form-item label="日期">
                    <el-date-picker
                        v-model="query.date"
                        class="w-[160px]"
                        type="date"
                        value-format="YYYY-MM-DD"
                        placeholder="选择课节日期"
                        clearable
                    />
                </el-form-item>
                <el-form-item label="状态">
                    <el-select v-model="query.status" class="w-[130px]" placeholder="全部状态">
                        <el-option label="全部状态" value="" />
                        <el-option
                            v-for="item in STATUS_OPTIONS"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value"
                        />
                    </el-select>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="loadLessons">查询</el-button>
                    <el-button @click="resetQuery">重置</el-button>
                </el-form-item>
            </el-form>
        </el-card>

        <el-card v-if="query.classId" class="!border-none mt-4" shadow="never">
            <div class="flex items-center justify-between mb-3">
                <div class="font-medium">
                    {{ className || selectedClassName }}
                    <span class="text-tx-secondary text-sm ml-2">
                        共 {{ lessons.length }} 节课<template v-if="query.date">
                            ，{{ query.date }} 筛选 {{ filteredLessons.length }} 节</template
                        >
                    </span>
                </div>
                <div class="text-tx-secondary text-sm">
                    教练签到：已签到 {{ checkedInCount }} 节 · 有课堂总结 {{ loggedCount }} 节
                </div>
            </div>
            <el-table
                size="large"
                table-layout="fixed"
                v-loading="listLoading"
                :data="filteredLessons"
                max-height="640"
            >
                <el-table-column label="课次" width="80" align="center">
                    <template #default="{ row }">第 {{ row.lessonNo }} 节</template>
                </el-table-column>
                <el-table-column label="上课日期" prop="lessonDate" width="112" />
                <el-table-column label="星期" prop="weekDay" width="72" align="center" />
                <el-table-column label="时段" prop="timeSlot" width="125" />
                <el-table-column label="类型" width="92" align="center">
                    <template #default="{ row }">
                        <el-tag
                            size="small"
                            :type="Number(row.lessonType) === 1 ? 'warning' : 'info'"
                            effect="plain"
                        >
                            {{ row.lessonTypeLabel }}
                        </el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="地点" prop="venue" min-width="150" show-overflow-tooltip />
                <el-table-column label="教练签到" width="95" align="center">
                    <template #default="{ row }">
                        <el-tag v-if="Number(row.coachCheckIn) === 1" size="small" type="success">
                            已签到
                        </el-tag>
                        <el-tag v-else size="small" type="info" effect="plain">未签到</el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="总结" width="80" align="center">
                    <template #default="{ row }">
                        <el-tag v-if="row.hasLog" size="small" type="success" effect="plain">已提交</el-tag>
                        <span v-else class="text-tx-secondary text-xs">未提交</span>
                    </template>
                </el-table-column>
                <el-table-column label="状态" width="90" align="center">
                    <template #default="{ row }">
                        <el-tag :type="statusTagType(row.status)">{{ row.statusLabel }}</el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="操作" width="100" fixed="right" align="center">
                    <template #default="{ row }">
                        <el-button
                            v-perms="['course_lesson/detail']"
                            type="primary"
                            link
                            @click="openDetail(row)"
                        >
                            课节明细
                        </el-button>
                    </template>
                </el-table-column>
                <template #empty>
                    <el-empty description="该班级在所选条件下没有课节" />
                </template>
            </el-table>
        </el-card>
        <el-card v-else class="!border-none mt-4" shadow="never">
            <el-empty description="请先在右上角选择班级，再按日期查看课节与学员名单" />
        </el-card>

        <!-- 课节明细 -->
        <el-dialog
            v-model="detailVisible"
            title="课节明细"
            width="980px"
            top="5vh"
            :close-on-click-modal="false"
        >
            <template v-if="detail">
                <el-descriptions :column="3" border>
                    <el-descriptions-item label="班级">{{ detail.lessonInfo.className || '-' }}</el-descriptions-item>
                    <el-descriptions-item label="课次">
                        第 {{ detail.lessonInfo.lessonNo }} 节
                    </el-descriptions-item>
                    <el-descriptions-item label="类型">
                        {{ detail.lessonInfo.lessonTypeLabel || '-' }}
                    </el-descriptions-item>
                    <el-descriptions-item label="日期">
                        {{ detail.lessonInfo.lessonDate || '-' }}
                    </el-descriptions-item>
                    <el-descriptions-item label="时段">
                        {{ detail.lessonInfo.timeSlot || '-' }}
                    </el-descriptions-item>
                    <el-descriptions-item label="状态">
                        <el-tag :type="statusTagType(detail.lessonInfo.status)">
                            {{ detail.lessonInfo.statusLabel }}
                        </el-tag>
                    </el-descriptions-item>
                    <el-descriptions-item label="教练">
                        {{ detail.teacherInfo.name || '-' }}
                    </el-descriptions-item>
                    <el-descriptions-item label="教练签到">
                        <el-tag
                            size="small"
                            :type="Number(detail.teacherInfo.coachCheckIn) === 1 ? 'success' : 'info'"
                            effect="plain"
                        >
                            {{ detail.teacherInfo.coachCheckInLabel }}
                        </el-tag>
                    </el-descriptions-item>
                    <el-descriptions-item label="地点">
                        {{ detail.lessonInfo.venue || '-' }}
                    </el-descriptions-item>
                </el-descriptions>

                <div class="grid grid-cols-4 gap-3 mt-4">
                    <el-card shadow="never" class="!border-none bg-page">
                        <div class="text-tx-secondary text-sm">有效预约</div>
                        <div class="text-2xl font-medium">{{ detail.stats.bookedCount }}</div>
                    </el-card>
                    <el-card shadow="never" class="!border-none bg-page">
                        <div class="text-tx-secondary text-sm">已点名</div>
                        <div class="text-2xl font-medium text-green-600">
                            {{ detail.stats.attendedCount }}
                        </div>
                    </el-card>
                    <el-card shadow="never" class="!border-none bg-page">
                        <div class="text-tx-secondary text-sm">未点名</div>
                        <div class="text-2xl font-medium text-orange-500">
                            {{ detail.stats.notAttendedCount }}
                        </div>
                    </el-card>
                    <el-card shadow="never" class="!border-none bg-page">
                        <div class="text-tx-secondary text-sm">已取消预约</div>
                        <div class="text-2xl font-medium">{{ detail.stats.cancelledCount }}</div>
                    </el-card>
                </div>

                <div class="font-medium mt-4 mb-2">学员名单与点名/消课状态</div>
                <el-table size="large" :data="detail.rollCalls || []" max-height="300">
                    <el-table-column label="学员" min-width="110">
                        <template #default="{ row }">
                            {{ row.studentName || row.displayName || '-' }}
                        </template>
                    </el-table-column>
                    <el-table-column label="家长" min-width="120" show-overflow-tooltip>
                        <template #default="{ row }">
                            {{ row.parentName || '-' }}
                            <span class="text-tx-secondary text-xs">{{ row.mobile || '' }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="点名状态" width="130" align="center">
                        <template #default="{ row }">
                            <el-tag
                                size="small"
                                :type="Number(row.status) === 10 ? 'success' : 'info'"
                                effect="plain"
                            >
                                {{ row.rollCallStatus || row.statusLabel }}
                            </el-tag>
                        </template>
                    </el-table-column>
                    <el-table-column label="消课流水" width="120" align="center">
                        <template #default="{ row }">
                            <el-tag v-if="row.consumeLogId" size="small" type="success" effect="plain">
                                已写流水
                            </el-tag>
                            <span v-else class="text-tx-secondary text-xs">无</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="更新时间" prop="updateTime" width="165" />
                    <el-table-column label="操作" width="110" align="center">
                        <template #default="{ row }">
                            <el-button
                                v-if="Number(row.status) === 1 && row.bookingId"
                                v-perms="['course_lesson/cancelBooking']"
                                type="danger"
                                link
                                :loading="cancellingId === Number(row.bookingId)"
                                @click="handleCancelBooking(row)"
                            >
                                取消预约
                            </el-button>
                            <span v-else class="text-tx-secondary text-xs">-</span>
                        </template>
                    </el-table-column>
                    <template #empty>
                        <el-empty description="本课节暂无有效预约学员" />
                    </template>
                </el-table>

                <div v-if="detail.lessonLog && detail.lessonLog.summary" class="mt-4">
                    <div class="font-medium mb-2">课堂总结</div>
                    <el-card shadow="never" class="!border-none bg-page">
                        <div class="text-sm whitespace-pre-wrap">{{ detail.lessonLog.summary }}</div>
                        <div class="text-tx-secondary text-xs mt-2">
                            {{ detail.lessonLog.createTime }}
                        </div>
                    </el-card>
                </div>
                <div v-else class="text-tx-secondary text-sm mt-4">
                    本课节尚未提交课堂总结<template v-if="detail.lessonInfo.remark">
                        · 备注：{{ detail.lessonInfo.remark }}</template
                    >
                </div>
            </template>
            <template #footer>
                <el-button @click="detailVisible = false">关闭</el-button>
            </template>
        </el-dialog>
    </div>
</template>

<script lang="ts" setup name="courseLesson">
import { classList } from '@/api/courseClass'
import { cancelLessonBooking, getLessonDetail, getLessonList } from '@/api/courseLesson'
import feedback from '@/utils/feedback'

const STATUS_OPTIONS = [
    { value: 0, label: '待上课' },
    { value: 1, label: '已结课' },
    { value: 2, label: '已取消' }
]

const query = reactive({
    classId: '' as number | string,
    date: '',
    status: '' as number | string
})

const classOptions = ref<any[]>([])
const classLoading = ref(false)
const className = ref('')

const selectedClassName = computed(() => {
    const matched = classOptions.value.find((item) => Number(item.id) === Number(query.classId))
    return matched ? matched.name : ''
})

async function searchClasses(keyword: string) {
    classLoading.value = true
    try {
        const data: any = await classList({
            page_no: 1,
            page_size: 50,
            name: keyword || ''
        })
        classOptions.value = data.lists || []
    } catch {
        // 请求层已提示
    } finally {
        classLoading.value = false
    }
}

const lessons = ref<any[]>([])
const listLoading = ref(false)

const filteredLessons = computed(() =>
    lessons.value.filter((item) => {
        if (query.date && item.lessonDate !== query.date) return false
        if (query.status !== '' && Number(item.status) !== Number(query.status)) return false
        return true
    })
)

const checkedInCount = computed(
    () => lessons.value.filter((item) => Number(item.coachCheckIn) === 1).length
)
const loggedCount = computed(() => lessons.value.filter((item) => item.hasLog).length)

async function loadLessons() {
    if (!query.classId) {
        lessons.value = []
        return
    }
    listLoading.value = true
    try {
        let page = 1
        let total = 0
        const all: any[] = []
        do {
            const data: any = await getLessonList({
                classId: query.classId,
                page_no: page,
                page_size: 200
            })
            className.value = data.className || ''
            total = Number(data.count || 0)
            all.push(...(data.lists || []))
            page += 1
            if (!data.lists || data.lists.length === 0) break
        } while (all.length < total && page <= 50)
        lessons.value = all
    } catch {
        lessons.value = []
    } finally {
        listLoading.value = false
    }
}

async function onClassChange() {
    className.value = ''
    await loadLessons()
}

function resetQuery() {
    query.date = ''
    query.status = ''
    loadLessons()
}

function statusTagType(status: number) {
    if (Number(status) === 1) return 'success'
    if (Number(status) === 2) return 'info'
    return 'warning'
}

const detailVisible = ref(false)
const detail = ref<any>(null)
const cancellingId = ref(0)

async function loadDetail(lessonId: number) {
    detail.value = await getLessonDetail({ lessonId })
}

async function openDetail(row: any) {
    try {
        await loadDetail(Number(row.id))
        detailVisible.value = true
    } catch {
        // 请求层已提示
    }
}

// 后台取消预约（#213 C1）：只对 status=1 的预约展示按钮，二次确认后提交并刷新名单
async function handleCancelBooking(row: any) {
    const lessonInfo = detail.value?.lessonInfo || {}
    const name = row.studentName || row.displayName || '该学员'
    try {
        await feedback.confirm(
            `确定取消 ${name} 在 ${lessonInfo.lessonDate || ''} 第 ${
                lessonInfo.lessonNo || ''
            } 节的预约？取消后名额释放，已上课的预约不能取消。`
        )
    } catch {
        return
    }
    cancellingId.value = Number(row.bookingId)
    try {
        await cancelLessonBooking({ id: row.bookingId })
        feedback.msgSuccess('已取消预约')
        await loadDetail(Number(lessonInfo.lessonId))
    } catch {
        // 请求层已提示（已上课/状态变化等）
    } finally {
        cancellingId.value = 0
    }
}

onMounted(async () => {
    await searchClasses('')
})
</script>
