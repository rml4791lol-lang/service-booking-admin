<template>
    <popup
        ref="popupRef"
        :title="popupTitle"
        :async="true"
        :loading="saving"
        width="760px"
        confirm-button-text="保存"
        @confirm="handleSubmit"
        @close="handleClose"
    >
        <el-form
            ref="formRef"
            v-loading="loading"
            :model="formData"
            :rules="formRules"
            label-width="100px"
        >
            <div class="form-section-title">基本信息</div>
            <el-row :gutter="16">
                <el-col :span="12">
                    <el-form-item label="教练姓名" prop="name">
                        <el-input v-model="formData.name" placeholder="请输入教练姓名" clearable />
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="联系电话" prop="phone">
                        <el-input v-model="formData.phone" placeholder="请输入联系电话" clearable />
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="身份证号" prop="idCard">
                        <el-input
                            v-model="formData.idCard"
                            placeholder="请输入身份证号码"
                            maxlength="18"
                            clearable
                        />
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="登录密码" prop="password">
                        <el-input
                            v-model="formData.password"
                            type="password"
                            show-password
                            :placeholder="mode === 'add' ? '请输入登录密码' : '不修改则留空'"
                        />
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="性别">
                        <el-radio-group v-model="formData.gender">
                            <el-radio :label="1">男</el-radio>
                            <el-radio :label="2">女</el-radio>
                        </el-radio-group>
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="头像">
                        <material-picker v-model="formData.avatar" :limit="1" />
                    </el-form-item>
                </el-col>
            </el-row>

            <div class="form-section-title">资质与执教范围</div>
            <el-row :gutter="16">
                <el-col :span="12">
                    <el-form-item label="专业领域" prop="specialty">
                        <el-input
                            v-model="formData.specialty"
                            placeholder="如：篮球、体适能"
                            clearable
                        />
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="从业年限" prop="experienceYears">
                        <el-input-number
                            v-model="formData.experienceYears"
                            :min="0"
                            :max="50"
                            controls-position="right"
                            class="!w-full"
                        />
                    </el-form-item>
                </el-col>
                <el-col :span="24">
                    <el-form-item label="教练资质">
                        <el-select
                            v-model="certificationList"
                            class="w-full"
                            multiple
                            filterable
                            allow-create
                            default-first-option
                            placeholder="输入资质后按回车添加，如：国家一级教练员"
                        />
                    </el-form-item>
                </el-col>
                <el-col :span="24">
                    <el-form-item label="资质证书">
                        <material-picker v-model="certImages" :limit="5" exclude-domain />
                    </el-form-item>
                </el-col>
                <el-col :span="24">
                    <el-form-item label="可授课程">
                        <el-select
                            v-model="teachableCourseIds"
                            class="w-full"
                            multiple
                            filterable
                            collapse-tags
                            collapse-tags-tooltip
                            placeholder="请选择可授运动项目"
                        >
                            <el-option
                                v-for="item in categoryOptions"
                                :key="item.id"
                                :label="item.label"
                                :value="item.id"
                            />
                        </el-select>
                    </el-form-item>
                </el-col>
                <el-col :span="24">
                    <el-form-item label="服务区域">
                        <el-select
                            v-model="serviceAreaIds"
                            class="w-full"
                            multiple
                            filterable
                            collapse-tags
                            collapse-tags-tooltip
                            placeholder="请选择可服务社区"
                        >
                            <el-option
                                v-for="item in options.communities"
                                :key="item.id"
                                :label="item.name"
                                :value="item.id"
                            />
                        </el-select>
                    </el-form-item>
                </el-col>
                <el-col :span="24">
                    <el-form-item label="个人简介">
                        <el-input
                            v-model="formData.introduction"
                            type="textarea"
                            :autosize="{ minRows: 3, maxRows: 6 }"
                            maxlength="2000"
                            show-word-limit
                            placeholder="请输入教练个人简介"
                        />
                    </el-form-item>
                </el-col>
            </el-row>

            <div class="form-section-title">其他设置</div>
            <el-row :gutter="16">
                <el-col :span="12">
                    <el-form-item label="状态" prop="status">
                        <el-radio-group v-model="formData.status">
                            <el-radio :label="1">正常</el-radio>
                            <el-radio :label="2">待审核</el-radio>
                            <el-radio :label="0">停用</el-radio>
                        </el-radio-group>
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="排序" prop="sort">
                        <el-input-number
                            v-model="formData.sort"
                            :min="0"
                            :max="99999"
                            controls-position="right"
                            class="!w-full"
                        />
                        <div class="form-tips">数值越大越排前</div>
                    </el-form-item>
                </el-col>
            </el-row>
        </el-form>
    </popup>
</template>

<script lang="ts" setup name="instructorEdit">
import type { FormInstance } from 'element-plus'

import { getInstructorDetail, getInstructorOptions, addInstructor, editInstructor } from '@/api/instructor'
import Popup from '@/components/popup/index.vue'
import feedback from '@/utils/feedback'

const emit = defineEmits(['success', 'close'])
const popupRef = shallowRef<InstanceType<typeof Popup>>()
const formRef = shallowRef<FormInstance>()
const mode = ref('add')
const loading = ref(false)
const saving = ref(false)
const optionsLoaded = ref(false)
const options = reactive<{ categories: any[]; communities: any[] }>({
    categories: [],
    communities: []
})

const popupTitle = computed(() => (mode.value === 'edit' ? '编辑教练' : '新增教练'))

const defaultForm = () => ({
    id: '',
    name: '',
    password: '',
    avatar: '',
    phone: '',
    gender: 1,
    specialty: '',
    experienceYears: 0,
    introduction: '',
    idCard: '',
    certification: '',
    certImages: '',
    teachableCourses: '',
    serviceAreas: '',
    classCount: 0,
    hourlyRate: 0,
    ratingScore: 0,
    status: 1,
    sort: 0
})

const formData = reactive<any>(defaultForm())
const certificationList = ref<string[]>([])
const teachableCourseIds = ref<number[]>([])
const serviceAreaIds = ref<number[]>([])

const certImages = computed({
    get: () =>
        formData.certImages
            ? String(formData.certImages)
                  .split(',')
                  .filter((item: string) => item)
            : [],
    set: (value: string[] | string) => {
        formData.certImages = Array.isArray(value) ? value.join(',') : value
    }
})

const categoryOptions = computed(() => {
    const map = new Map<number, any>()
    options.categories.forEach((item) => map.set(item.id, item))
    return options.categories.map((item) => {
        const parent = item.parentId ? map.get(item.parentId) : null
        return {
            ...item,
            label: parent ? `${parent.name} / ${item.name}` : item.name
        }
    })
})

const validatePassword = (_rule: any, value: string, callback: any) => {
    if (mode.value === 'add' && !value) {
        callback(new Error('请输入登录密码'))
        return
    }
    if (value && (value.length < 6 || value.length > 20)) {
        callback(new Error('密码必须在 6~20 个字符内'))
        return
    }
    callback()
}

const formRules = {
    name: [{ required: true, message: '请输入教练姓名', trigger: ['blur', 'change'] }],
    phone: [{ required: true, message: '请输入联系电话', trigger: ['blur', 'change'] }],
    specialty: [{ required: true, message: '请输入专业领域', trigger: ['blur', 'change'] }],
    experienceYears: [{ required: true, message: '请输入从业年限', trigger: ['blur', 'change'] }],
    password: [{ validator: validatePassword, trigger: ['blur'] }]
}

function parseIds(value: any) {
    if (!value) return []
    return String(value)
        .split(',')
        .map((item: string) => Number(item))
        .filter((item: number) => !Number.isNaN(item))
}

function resetForm() {
    Object.assign(formData, defaultForm())
    certificationList.value = []
    teachableCourseIds.value = []
    serviceAreaIds.value = []
    formRef.value?.clearValidate()
}

async function loadOptions() {
    if (optionsLoaded.value) return
    try {
        const data: any = await getInstructorOptions()
        options.categories = Array.isArray(data?.categories) ? data.categories : []
        options.communities = Array.isArray(data?.communities) ? data.communities : []
        optionsLoaded.value = true
    } catch {
        // 请求层已提示错误，选项保持为空
    }
}

const handleSubmit = async () => {
    if (saving.value) return
    const valid = await formRef.value?.validate().catch(() => false)
    if (!valid || saving.value) return
    const payload: any = {
        ...formData,
        certification: certificationList.value.join(','),
        teachableCourses: teachableCourseIds.value.join(','),
        serviceAreas: serviceAreaIds.value.join(',')
    }
    if (!payload.password) {
        delete payload.password
    }
    saving.value = true
    try {
        if (mode.value === 'edit') {
            await editInstructor(payload)
        } else {
            await addInstructor(payload)
        }
        feedback.msgSuccess('操作成功')
        popupRef.value?.close()
        emit('success')
    } catch {
        // 请求层已提示错误，保留弹窗便于修改
    } finally {
        saving.value = false
    }
}

const open = async (type = 'add') => {
    mode.value = type
    resetForm()
    popupRef.value?.open()
    await loadOptions()
}

const setFormData = (data: Record<string, any>) => {
    Object.assign(formData, defaultForm())
    Object.keys(formData).forEach((key) => {
        if (data[key] !== null && data[key] !== undefined) {
            formData[key] = data[key]
        }
    })
    formData.password = ''
    certificationList.value = data.certification
        ? String(data.certification)
              .split(',')
              .filter((item: string) => item)
        : []
    teachableCourseIds.value = parseIds(data.teachableCourses)
    serviceAreaIds.value = parseIds(data.serviceAreas)
}

const getDetail = async (row: any) => {
    if (!row?.id) return
    loading.value = true
    try {
        const data: any = await getInstructorDetail({ id: row.id })
        setFormData(data || {})
    } finally {
        loading.value = false
    }
}

const handleClose = () => {
    emit('close')
}

defineExpose({
    open,
    getDetail
})
</script>

<style scoped lang="scss">
.form-section-title {
    margin: 4px 0 16px;
    padding-left: 8px;
    border-left: 3px solid var(--el-color-primary);
    font-size: 14px;
    font-weight: 600;
    color: var(--el-text-color-primary);
}
</style>
