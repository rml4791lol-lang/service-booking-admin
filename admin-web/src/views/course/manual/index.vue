<template>
    <div class="manual-page">
        <el-card class="!border-none" shadow="never">
            <div class="text-lg font-medium">人工扣课</div>
            <div class="text-tx-secondary text-sm mt-1">
                教务补录线下课：选择进行中的班级发起人工扣课，按学员逐人扣 1
                课时并写消课流水/教练结费；剩余课时、核销报表与课节视图同步变化。
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
                        {{ classInfo.timeSlot || '-' }} · 单节结费
                        {{ classInfo.feePerLesson ?? '-' }}
                    </div>
                </div>
                <el-button
                    v-perms="['course.class/manual/submit']"
                    type="primary"
                    :disabled="Number(classInfo.status) !== 1"
                    @click="openForm"
                >
                    发起人工扣课
                </el-button>
            </div>

            <el-alert
                v-if="classInfo.id && Number(classInfo.status) !== 1"
                class="mt-3"
                type="warning"
                :closable="false"
                title="仅进行中的班级可发起人工扣课"
            />

            <el-table
                class="mt-3"
                size="large"
                table-layout="fixed"
                v-loading="listLoading"
                :data="manualRows"
            >
                <el-table-column label="上课日期" prop="lessonDate" width="112" />
                <el-table-column label="时段" prop="timeSlot" width="125" />
                <el-table-column label="实际上课教练" min-width="120" show-overflow-tooltip>
                    <template #default="{ row }">
                        {{ row.instructorName || '-' }}
                        <el-tag v-if="row.isSubstitute" class="ml-1" size="small" type="warning" effect="plain">
                            代课
                        </el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="地点" prop="venue" min-width="150" show-overflow-tooltip />
                <el-table-column label="结费金额" prop="feeAmount" width="100" align="right" />
                <el-table-column label="学员数" prop="studentCount" width="80" align="center" />
                <el-table-column label="备注" prop="remark" min-width="130" show-overflow-tooltip />
                <el-table-column label="操作人" prop="operatorName" width="95" show-overflow-tooltip />
                <el-table-column label="创建时间" prop="createTime" width="165" />
                <el-table-column label="操作" width="90" fixed="right" align="center">
                    <template #default="{ row }">
                        <el-button
                            v-perms="['course.class/manual/detail']"
                            type="primary"
                            link
                            @click="openDetail(row)"
                        >
                            详情
                        </el-button>
                    </template>
                </el-table-column>
                <template #empty>
                    <el-empty description="该班级还没有人工扣课记录" />
                </template>
            </el-table>
        </el-card>
        <el-card v-else class="!border-none mt-4" shadow="never">
            <el-empty description="请先在右上角选择班级，再查看扣课记录并发起人工扣课" />
        </el-card>

        <!-- 发起人工扣课 -->
        <el-dialog
            v-model="formVisible"
            title="发起人工扣课"
            width="900px"
            top="5vh"
            :close-on-click-modal="false"
            @closed="resetForm"
        >
            <el-form label-width="110px">
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
                    <el-form-item label="实际上课教练" required>
                        <el-select
                            v-model="form.instructorId"
                            class="w-full"
                            clearable
                            filterable
                            placeholder="选择实际上课教练"
                        >
                            <el-option
                                v-for="item in instructorOptions"
                                :key="item.id"
                                :label="item.name"
                                :value="item.id"
                            />
                        </el-select>
                    </el-form-item>
                    <el-form-item label="结费金额" required>
                        <el-input
                            v-model="form.feeAmount"
                            inputmode="decimal"
                            maxlength="10"
                            placeholder="结费金额，最多两位小数"
                            @input="onFeeAmountInput"
                        />
                    </el-form-item>
                    <el-form-item label="上课地点" required>
                        <el-input v-model="form.venue" placeholder="上课地点" />
                    </el-form-item>
                    <el-form-item label="备注">
                        <el-input v-model="form.remark" placeholder="扣课原因/备注" maxlength="500" />
                    </el-form-item>
                </div>
            </el-form>

            <div class="flex items-center justify-between mt-1 mb-2">
                <div class="font-medium">
                    选择学员
                    <span class="text-tx-secondary text-sm ml-1">
                        仅可选还有剩余课时的报名记录；每人扣 1 课时
                    </span>
                </div>
                <el-input
                    v-model="studentKeyword"
                    class="w-[220px]"
                    clearable
                    placeholder="筛选学员/家长"
                />
            </div>
            <el-table
                ref="studentTableRef"
                size="large"
                :data="filteredStudentOptions"
                row-key="id"
                max-height="280"
                @selection-change="onStudentSelectionChange"
            >
                <el-table-column type="selection" width="48" :selectable="isStudentSelectable" />
                <el-table-column label="学员" min-width="120">
                    <template #default="{ row }">{{ row.studentName || row.nickname || '-' }}</template>
                </el-table-column>
                <el-table-column label="家长" min-width="130" show-overflow-tooltip>
                    <template #default="{ row }">
                        {{ row.nickname || row.realName || '-' }}
                        <span class="text-tx-secondary text-xs">{{ row.mobile || '' }}</span>
                    </template>
                </el-table-column>
                <el-table-column label="剩余课时" width="95" align="center">
                    <template #default="{ row }">
                        <span :class="row.canConsume ? 'text-green-600' : 'text-orange-500'">
                            {{ row.remainLessons }} / {{ row.totalLessons }}
                        </span>
                    </template>
                </el-table-column>
                <el-table-column label="报名状态" width="95" align="center">
                    <template #default="{ row }">
                        <el-tag size="small" :type="row.canConsume ? 'success' : 'info'" effect="plain">
                            {{ row.statusText }}
                        </el-tag>
                    </template>
                </el-table-column>
                <template #empty>
                    <el-empty description="该班级暂无报名学员" />
                </template>
            </el-table>
            <div class="text-tx-secondary text-sm mt-2">
                已选 {{ selectedStudents.length }} 名学员
            </div>

            <template #footer>
                <el-button @click="formVisible = false">取消</el-button>
                <el-button type="primary" :loading="submitting" @click="submitForm">
                    提交人工扣课
                </el-button>
            </template>
        </el-dialog>

        <!-- 扣课详情 -->
        <el-dialog
            v-model="detailVisible"
            title="人工扣课详情"
            width="760px"
            top="8vh"
            :close-on-click-modal="false"
        >
            <template v-if="detail">
                <el-descriptions :column="2" border>
                    <el-descriptions-item label="班级">{{ detail.className || '-' }}</el-descriptions-item>
                    <el-descriptions-item label="上课日期">{{ detail.lessonDate || '-' }}</el-descriptions-item>
                    <el-descriptions-item label="时段">{{ detail.timeSlot || '-' }}</el-descriptions-item>
                    <el-descriptions-item label="实际上课教练">
                        {{ detail.instructorName || '-' }}
                    </el-descriptions-item>
                    <el-descriptions-item label="地点">{{ detail.venue || '-' }}</el-descriptions-item>
                    <el-descriptions-item label="结费金额">{{ detail.feeAmount ?? '-' }}</el-descriptions-item>
                    <el-descriptions-item label="学员数">{{ detail.studentCount ?? 0 }}</el-descriptions-item>
                    <el-descriptions-item label="操作人">{{ detail.operatorName || '-' }}</el-descriptions-item>
                    <el-descriptions-item label="创建时间">{{ detail.createTime || '-' }}</el-descriptions-item>
                    <el-descriptions-item label="备注">{{ detail.remark || '-' }}</el-descriptions-item>
                </el-descriptions>
                <el-table class="mt-4" size="large" :data="detail.students || []" max-height="320">
                    <el-table-column label="学员" min-width="110">
                        <template #default="{ row }">{{ row.studentName || row.nickname || '-' }}</template>
                    </el-table-column>
                    <el-table-column label="家长" min-width="120" show-overflow-tooltip>
                        <template #default="{ row }">{{ row.nickname || '-' }}</template>
                    </el-table-column>
                    <el-table-column label="手机号" prop="mobile" width="130" />
                </el-table>
            </template>
            <template #footer>
                <el-button @click="detailVisible = false">关闭</el-button>
            </template>
        </el-dialog>
    </div>
</template>

<script lang="ts" setup name="courseManual">
import {
    classDetail,
    classEnrolledStudents,
    classList,
    manualDetail,
    manualInstructors,
    manualList,
    manualSubmit
} from '@/api/courseClass'
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

const manualRows = ref<any[]>([])
const listLoading = ref(false)

async function loadList() {
    if (!query.classId) {
        manualRows.value = []
        return
    }
    listLoading.value = true
    try {
        manualRows.value = (await manualList({ classId: query.classId })) || []
    } catch {
        manualRows.value = []
    } finally {
        listLoading.value = false
    }
}

const studentOptions = ref<any[]>([])
const instructorOptions = ref<any[]>([])
const studentKeyword = ref('')
const selectedStudents = ref<any[]>([])
const studentTableRef = ref()

const filteredStudentOptions = computed(() => {
    const keyword = String(studentKeyword.value || '').trim()
    if (!keyword) return studentOptions.value
    return studentOptions.value.filter((item) =>
        [item.studentName, item.nickname, item.realName, item.mobile]
            .filter(Boolean)
            .some((text) => String(text).includes(keyword))
    )
})

function isStudentSelectable(row: any) {
    return Boolean(row.canConsume)
}

function onStudentSelectionChange(rows: any[]) {
    selectedStudents.value = rows
}

async function loadStudents() {
    if (!query.classId) {
        studentOptions.value = []
        return
    }
    try {
        studentOptions.value = (await classEnrolledStudents({ classId: query.classId })) || []
    } catch {
        studentOptions.value = []
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

async function loadInstructors() {
    try {
        instructorOptions.value = (await manualInstructors()) || []
    } catch {
        instructorOptions.value = []
    }
}

async function onClassChange() {
    selectedStudents.value = []
    await Promise.all([loadList(), loadStudents(), loadClassDetail()])
}

const formVisible = ref(false)
const submitting = ref(false)
const form = reactive({
    lessonDate: '',
    startTime: '',
    endTime: '',
    instructorId: '' as number | string,
    feeAmount: '',
    venue: '',
    remark: ''
})

function parseTimeSlot(value: string) {
    const text = String(value || '').trim()
    const matched = text.match(/(\d{1,2}:\d{2})\s*[-~—]\s*(\d{1,2}:\d{2})/)
    if (!matched) return null
    return { start: matched[1], end: matched[2] }
}

function openForm() {
    form.lessonDate = ''
    form.startTime = ''
    form.endTime = ''
    form.instructorId = classInfo.value.instructorId || ''
    form.feeAmount = classInfo.value.feePerLesson != null ? String(classInfo.value.feePerLesson) : ''
    form.venue = classInfo.value.venue || ''
    form.remark = ''
    selectedStudents.value = []
    studentKeyword.value = ''
    const parsed = parseTimeSlot(classInfo.value.timeSlot)
    if (parsed) {
        form.startTime = parsed.start
        form.endTime = parsed.end
    }
    formVisible.value = true
    nextTick(() => studentTableRef.value?.clearSelection())
}

function resetForm() {
    selectedStudents.value = []
    studentKeyword.value = ''
    submitting.value = false
    studentTableRef.value?.clearSelection()
}

// 只允许非负数字与一个小数点、最多两位小数；显式拒绝 1e3 / 负号 / 加号，
// 避免科学计数法进入后端 BigDecimal。
function onFeeAmountInput(value: string) {
    const raw = String(value ?? '')
    if (/[eE+\-]/.test(raw)) {
        feedback.msgError('结费金额请输入普通数字，不支持科学计数法或正负号')
        form.feeAmount = ''
        return
    }
    let next = raw.replace(/[^\d.]/g, '')
    const dot = next.indexOf('.')
    if (dot >= 0) {
        next = `${next.slice(0, dot + 1)}${next.slice(dot + 1).replace(/\./g, '').slice(0, 2)}`
    }
    form.feeAmount = next
}

function validateForm() {
    if (!form.lessonDate) return '请选择上课日期'
    if (!form.startTime || !form.endTime) return '请选择完整的上课开始/结束时间'
    if (form.startTime >= form.endTime) return '上课结束时间必须晚于开始时间'
    if (!form.instructorId) return '请选择实际上课教练'
    if (!String(form.venue || '').trim()) return '请填写上课地点'
    const feeText = String(form.feeAmount ?? '').trim()
    if (!/^\d+(\.\d{1,2})?$/.test(feeText)) return '结费金额仅支持数字，最多两位小数'
    const fee = Number(feeText)
    if (!Number.isFinite(fee) || fee <= 0) return '结费金额必须大于 0'
    if (!selectedStudents.value.length) return '请至少选择一名学员'
    return ''
}

async function submitForm() {
    const error = validateForm()
    if (error) {
        feedback.msgError(error)
        return
    }
    submitting.value = true
    try {
        await manualSubmit({
            classId: query.classId,
            lessonDate: form.lessonDate,
            timeSlot: `${form.startTime}-${form.endTime}`,
            instructorId: form.instructorId,
            venue: form.venue,
            // 传字符串，避免 Number 转浮点导致后端 BigDecimal 精度/科学计数法问题
            feeAmount: String(form.feeAmount).trim(),
            remark: form.remark,
            studentSelections: selectedStudents.value.map((item) => ({
                userId: item.userId,
                studentId: item.studentId,
                userCourseId: item.id
            }))
        })
        feedback.msgSuccess('人工扣课已提交')
        formVisible.value = false
        await Promise.all([loadList(), loadStudents(), loadClassDetail()])
    } catch {
        // 请求层已提示
    } finally {
        submitting.value = false
    }
}

const detailVisible = ref(false)
const detail = ref<any>(null)

async function openDetail(row: any) {
    try {
        detail.value = await manualDetail({ id: row.id })
        detailVisible.value = true
    } catch {
        // 请求层已提示
    }
}

onMounted(async () => {
    await Promise.all([searchClasses(''), loadInstructors()])
})
</script>
