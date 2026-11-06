<template>
    <div class="temp-lesson-page">
        <el-card class="!border-none" shadow="never">
            <div class="text-lg font-medium">临时加课</div>
            <div class="text-tx-secondary text-sm mt-1">
                为进行中的班级临时增加课节：选择日期/时段/授课教练与地图坐标后写入课节表，
                教练课表与课节明细立即可见；未上课的临时课可取消。
            </div>
        </el-card>

        <el-card class="!border-none mt-4" shadow="never">
            <el-form class="mb-[-16px]" :inline="true">
                <el-form-item label="班级">
                    <el-select
                        v-model="query.classId"
                        class="w-[300px]"
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
                <el-form-item>
                    <el-button type="primary" @click="loadList">查询</el-button>
                </el-form-item>
            </el-form>
        </el-card>

        <el-card v-if="query.classId" class="!border-none mt-4" shadow="never">
            <div class="flex items-start justify-between">
                <div>
                    <div class="font-medium">
                        {{ classInfo.name || selectedClassName }}
                        <el-tag class="ml-2" size="small" effect="plain">
                            {{ Number(classInfo.status) === 1 ? '进行中' : '非进行中' }}
                        </el-tag>
                    </div>
                    <div class="text-tx-secondary text-sm mt-1">
                        {{ classInfo.venue || '未维护地点' }} · 排课时段
                        {{ classInfo.timeSlot || '-' }}
                    </div>
                </div>
                <el-button
                    v-perms="['course.class/tempLesson/create']"
                    type="primary"
                    :disabled="Number(classInfo.status) !== 1"
                    @click="openForm"
                >
                    创建临时加课
                </el-button>
            </div>

            <el-alert
                v-if="classInfo.id && Number(classInfo.status) !== 1"
                class="mt-3"
                type="warning"
                :closable="false"
                title="仅进行中的班级可发起临时加课"
            />

            <el-table
                class="mt-3"
                size="large"
                table-layout="fixed"
                v-loading="listLoading"
                :data="tempRows"
            >
                <el-table-column label="上课日期" prop="lessonDate" width="112" />
                <el-table-column label="时段" prop="timeSlot" width="125" />
                <el-table-column label="课次" width="80" align="center">
                    <template #default="{ row }">第 {{ row.lessonNo }} 节</template>
                </el-table-column>
                <el-table-column label="授课教练" min-width="120" show-overflow-tooltip>
                    <template #default="{ row }">
                        {{ row.instructorName || '-' }}
                        <el-tag v-if="row.isSubstitute" class="ml-1" size="small" type="warning" effect="plain">
                            代课
                        </el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="地点" prop="venue" min-width="150" show-overflow-tooltip />
                <el-table-column label="状态" width="95" align="center">
                    <template #default="{ row }">
                        <el-tag :type="statusTagType(row.status)">{{ row.statusText }}</el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="备注" prop="remark" min-width="130" show-overflow-tooltip />
                <el-table-column label="操作人" prop="operatorName" width="95" show-overflow-tooltip />
                <el-table-column label="操作" width="160" fixed="right" align="center">
                    <template #default="{ row }">
                        <el-button
                            v-perms="['course.class/tempLesson/detail']"
                            type="primary"
                            link
                            @click="openDetail(row)"
                        >
                            详情
                        </el-button>
                        <el-button
                            v-if="Number(row.status) === 0"
                            v-perms="['course.class/tempLesson/cancel']"
                            type="warning"
                            link
                            @click="cancelLesson(row)"
                        >
                            取消加课
                        </el-button>
                        <span v-else class="text-tx-secondary text-xs">-</span>
                    </template>
                </el-table-column>
                <template #empty>
                    <el-empty description="该班级还没有临时加课记录" />
                </template>
            </el-table>
        </el-card>
        <el-card v-else class="!border-none mt-4" shadow="never">
            <el-empty description="请先在右上角选择班级，再查看/创建临时加课" />
        </el-card>

        <!-- 创建临时加课 -->
        <el-dialog
            v-model="formVisible"
            title="创建临时加课"
            width="820px"
            top="6vh"
            :close-on-click-modal="false"
            @closed="resetForm"
        >
            <el-form label-width="105px">
                <div class="grid grid-cols-2 gap-x-4">
                    <el-form-item label="上课日期" required>
                        <el-date-picker
                            v-model="form.lessonDate"
                            class="w-full"
                            type="date"
                            value-format="YYYY-MM-DD"
                            placeholder="选择上课日期"
                        />
                    </el-form-item>
                    <el-form-item label="上课时段" required>
                        <div class="flex items-center gap-2 w-full">
                            <el-time-select
                                v-model="form.startTime"
                                class="flex-1"
                                start="06:00"
                                step="00:10"
                                end="22:00"
                                placeholder="开始时间"
                            />
                            <span class="text-tx-secondary">-</span>
                            <el-time-select
                                v-model="form.endTime"
                                class="flex-1"
                                start="06:10"
                                step="00:10"
                                end="22:30"
                                placeholder="结束时间"
                            />
                        </div>
                    </el-form-item>
                    <el-form-item label="授课教练" required>
                        <el-select
                            v-model="form.instructorId"
                            class="w-full"
                            clearable
                            filterable
                            placeholder="选择授课教练"
                        >
                            <el-option
                                v-for="item in instructorOptions"
                                :key="item.id"
                                :label="item.name"
                                :value="item.id"
                            />
                        </el-select>
                    </el-form-item>
                    <el-form-item label="上课地点" required>
                        <el-input v-model="form.venue" placeholder="上课地点" />
                    </el-form-item>
                    <el-form-item label="纬度" required>
                        <el-input v-model="form.latitude" placeholder="纬度" />
                    </el-form-item>
                    <el-form-item label="经度" required>
                        <el-input v-model="form.longitude" placeholder="经度" />
                    </el-form-item>
                    <el-form-item label="备注">
                        <el-input
                            v-model="form.remark"
                            placeholder="临时加课原因/备注"
                            maxlength="500"
                        />
                    </el-form-item>
                </div>
            </el-form>
            <el-alert
                class="mt-2"
                type="info"
                :closable="false"
                title="坐标默认沿用班级地点；如临时更换场地，请按实际经纬度修改（小程序地图导航使用）。"
            />
            <template #footer>
                <el-button @click="formVisible = false">取消</el-button>
                <el-button type="primary" :loading="submitting" @click="submitForm">
                    确认创建
                </el-button>
            </template>
        </el-dialog>

        <!-- 临时加课详情 -->
        <el-dialog
            v-model="detailVisible"
            title="临时加课详情"
            width="720px"
            top="8vh"
            :close-on-click-modal="false"
        >
            <template v-if="detail">
                <el-descriptions :column="2" border>
                    <el-descriptions-item label="班级">{{ detail.className || '-' }}</el-descriptions-item>
                    <el-descriptions-item label="课次">第 {{ detail.lessonNo }} 节</el-descriptions-item>
                    <el-descriptions-item label="上课日期">{{ detail.lessonDate || '-' }}</el-descriptions-item>
                    <el-descriptions-item label="时段">{{ detail.timeSlot || '-' }}</el-descriptions-item>
                    <el-descriptions-item label="授课教练">
                        {{ detail.instructorName || '-' }}
                    </el-descriptions-item>
                    <el-descriptions-item label="地点">{{ detail.venue || '-' }}</el-descriptions-item>
                    <el-descriptions-item label="状态">
                        <el-tag :type="statusTagType(detail.status)">{{ detail.statusText }}</el-tag>
                    </el-descriptions-item>
                    <el-descriptions-item label="操作人">{{ detail.operatorName || '-' }}</el-descriptions-item>
                    <el-descriptions-item label="创建时间">{{ detail.createTime || '-' }}</el-descriptions-item>
                    <el-descriptions-item label="关联课节ID">{{ detail.lessonId || '-' }}</el-descriptions-item>
                    <el-descriptions-item label="备注" :span="2">{{ detail.remark || '-' }}</el-descriptions-item>
                </el-descriptions>
            </template>
            <template #footer>
                <el-button @click="detailVisible = false">关闭</el-button>
            </template>
        </el-dialog>
    </div>
</template>

<script lang="ts" setup name="courseTempLesson">
import {
    classDetail,
    classList,
    tempLessonCancel,
    tempLessonCreate,
    tempLessonDetail,
    tempLessonList
} from '@/api/courseClass'
import { getInstructorAll } from '@/api/instructor'
import feedback from '@/utils/feedback'

const query = reactive({
    classId: '' as number | string
})

const classOptions = ref<any[]>([])
const classLoading = ref(false)
const classInfo = ref<any>({})
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

const tempRows = ref<any[]>([])
const listLoading = ref(false)

async function loadList() {
    if (!query.classId) {
        tempRows.value = []
        return
    }
    listLoading.value = true
    try {
        tempRows.value = (await tempLessonList({ classId: query.classId })) || []
    } catch {
        tempRows.value = []
    } finally {
        listLoading.value = false
    }
}

const instructorOptions = ref<any[]>([])

async function loadInstructors() {
    try {
        instructorOptions.value = (await getInstructorAll()) || []
    } catch {
        instructorOptions.value = []
    }
}

async function loadClassDetail() {
    if (!query.classId) {
        classInfo.value = {}
        return
    }
    try {
        classInfo.value = (await classDetail({ id: query.classId })) || {}
    } catch {
        classInfo.value = {}
    }
}

async function onClassChange() {
    await Promise.all([loadList(), loadClassDetail()])
}

function statusTagType(status: number) {
    if (Number(status) === 1) return 'success'
    if (Number(status) === 2) return 'info'
    return 'warning'
}

function parseTimeSlot(value: string) {
    const text = String(value || '').trim()
    const matched = text.match(/(\d{1,2}:\d{2})\s*[-~—]\s*(\d{1,2}:\d{2})/)
    if (!matched) return null
    return { start: matched[1], end: matched[2] }
}

const formVisible = ref(false)
const submitting = ref(false)
const form = reactive({
    lessonDate: '',
    startTime: '',
    endTime: '',
    instructorId: '' as number | string,
    venue: '',
    latitude: '',
    longitude: '',
    remark: ''
})

async function openForm() {
    form.lessonDate = ''
    form.startTime = ''
    form.endTime = ''
    form.instructorId = classInfo.value.instructorId || ''
    form.venue = classInfo.value.venue || ''
    form.latitude = classInfo.value.latitude != null ? String(classInfo.value.latitude) : ''
    form.longitude = classInfo.value.longitude != null ? String(classInfo.value.longitude) : ''
    form.remark = ''
    const parsed = parseTimeSlot(classInfo.value.timeSlot)
    if (parsed) {
        form.startTime = parsed.start
        form.endTime = parsed.end
    }
    formVisible.value = true
}

function resetForm() {
    submitting.value = false
}

function validateForm() {
    if (!form.lessonDate) return '请选择上课日期'
    if (!form.startTime || !form.endTime) return '请选择完整的上课开始/结束时间'
    if (form.startTime >= form.endTime) return '上课结束时间必须晚于开始时间'
    if (!form.instructorId) return '请选择授课教练'
    if (!String(form.venue || '').trim()) return '请填写上课地点'
    if (parseCoordinate(form.latitude, -90, 90) === null) {
        return '纬度需为 -90 ~ 90 的数字（如 30.123456）'
    }
    if (parseCoordinate(form.longitude, -180, 180) === null) {
        return '经度需为 -180 ~ 180 的数字（如 120.123456）'
    }
    return ''
}

// 只接受普通十进制数字，拒绝 1e3 / 空串 / 超范围值
function parseCoordinate(value: string, min: number, max: number) {
    const text = String(value ?? '').trim()
    if (!/^-?\d+(\.\d+)?$/.test(text)) return null
    const number = Number(text)
    if (!Number.isFinite(number) || number < min || number > max) return null
    return number
}

async function submitForm() {
    const error = validateForm()
    if (error) {
        feedback.msgError(error)
        return
    }
    submitting.value = true
    try {
        await tempLessonCreate({
            classId: query.classId,
            lessonDate: form.lessonDate,
            timeSlot: `${form.startTime}-${form.endTime}`,
            instructorId: form.instructorId,
            venue: form.venue,
            latitude: form.latitude,
            longitude: form.longitude,
            remark: form.remark
        })
        feedback.msgSuccess('临时加课已创建')
        formVisible.value = false
        await loadList()
    } catch {
        // 请求层已提示
    } finally {
        submitting.value = false
    }
}

async function cancelLesson(row: any) {
    try {
        await feedback.confirm('取消后该课节在教练课表中标记为已取消，操作不可撤销。确认取消？')
    } catch {
        return
    }
    try {
        await tempLessonCancel({ id: row.id })
        feedback.msgSuccess('临时加课已取消')
        await loadList()
    } catch {
        // 请求层已提示
    }
}

const detailVisible = ref(false)
const detail = ref<any>(null)

async function openDetail(row: any) {
    try {
        detail.value = await tempLessonDetail({ id: row.id })
        detailVisible.value = true
    } catch {
        // 请求层已提示
    }
}

onMounted(async () => {
    await Promise.all([searchClasses(''), loadInstructors()])
})
</script>
