<template>
    <popup
        ref="popupRef"
        title="调整课时"
        width="560px"
        :async="true"
        :loading="saving"
        @confirm="handleConfirm"
        @close="handleClose"
    >
        <div v-loading="saving">
            <el-form ref="formRef" :model="formData" label-width="120px" :rules="formRules">
                <el-form-item label="学员">
                    <span>{{ row?.studentName || '-' }}</span>
                    <span class="ml-3 text-tx-secondary">{{ row?.mobile || '' }}</span>
                </el-form-item>
                <el-form-item label="课程">
                    <span>{{ row?.className || '-' }}</span>
                </el-form-item>
                <el-form-item label="当前总/剩余">
                    <span>总课时 {{ row?.totalLessons ?? 0 }}，剩余 {{ row?.remainLessons ?? 0 }}</span>
                </el-form-item>
                <el-form-item label="总课时" prop="totalLessons">
                    <el-input
                        v-model="formData.totalLessons"
                        data-test="adjust-total"
                        type="number"
                        min="0"
                        :placeholder="`不修改请留空（当前 ${row?.totalLessons ?? 0}）`"
                    />
                </el-form-item>
                <el-form-item label="剩余课时" prop="remainLessons">
                    <el-input
                        v-model="formData.remainLessons"
                        data-test="adjust-remain"
                        type="number"
                        min="0"
                        :placeholder="`不修改请留空（当前 ${row?.remainLessons ?? 0}）`"
                    />
                </el-form-item>
                <el-form-item label="调整后">
                    <span data-test="adjust-preview">
                        已用 {{ preview.used }}，剩余 {{ preview.remain }}
                    </span>
                </el-form-item>
                <el-form-item label="调整备注" prop="remark">
                    <el-input
                        v-model="formData.remark"
                        data-test="adjust-remark"
                        type="textarea"
                        :rows="3"
                        maxlength="500"
                        show-word-limit
                        placeholder="必填，记录本次调整原因，写入操作日志"
                    />
                </el-form-item>
            </el-form>
        </div>
    </popup>
</template>

<script lang="ts" setup name="userCourseAdjust">
import type { FormInstance, FormRules } from 'element-plus'
import { computed, reactive, ref, shallowRef, watch } from 'vue'

import { adjustUserCourse } from '@/api/userCourse'
import Popup from '@/components/popup/index.vue'
import feedback from '@/utils/feedback'

const props = defineProps({
    show: {
        type: Boolean,
        required: true
    },
    row: {
        type: Object,
        default: () => ({})
    }
})
const emit = defineEmits<{
    (event: 'update:show', value: boolean): void
    (event: 'success'): void
}>()

const popupRef = shallowRef<InstanceType<typeof Popup>>()
const formRef = shallowRef<FormInstance>()
const saving = ref(false)
const formData = reactive({
    totalLessons: '',
    remainLessons: '',
    remark: ''
})

const toNumber = (value: string) => {
    if (value === '' || value === null || value === undefined) return null
    const num = Number(value)
    return Number.isFinite(num) ? num : NaN
}

const preview = computed(() => {
    const total = toNumber(formData.totalLessons)
    const remain = toNumber(formData.remainLessons)
    const nextTotal = total === null ? Number(props.row?.totalLessons ?? 0) : total
    const nextRemain = remain === null ? Number(props.row?.remainLessons ?? 0) : remain
    if (!Number.isFinite(nextTotal) || !Number.isFinite(nextRemain) || nextRemain > nextTotal) {
        return { used: '-', remain: '-' }
    }
    return { used: nextTotal - nextRemain, remain: nextRemain }
})

const validateLessons = (_rule: any, _value: any, callback: any) => {
    const total = toNumber(formData.totalLessons)
    const remain = toNumber(formData.remainLessons)
    if (total === null && remain === null) {
        return callback(new Error('请至少填写总课时或剩余课时'))
    }
    if ((total !== null && (!Number.isInteger(total) || total < 0)) ||
        (remain !== null && (!Number.isInteger(remain) || remain < 0))) {
        return callback(new Error('课时必须是非负整数'))
    }
    const nextTotal = total === null ? Number(props.row?.totalLessons ?? 0) : total
    const nextRemain = remain === null ? Number(props.row?.remainLessons ?? 0) : remain
    if (nextRemain > nextTotal) {
        return callback(new Error('剩余课时不能大于总课时'))
    }
    callback()
}

const formRules: FormRules = {
    totalLessons: [{ validator: validateLessons, trigger: 'blur' }],
    remainLessons: [{ validator: validateLessons, trigger: 'blur' }],
    remark: [{ required: true, message: '请填写调整备注', trigger: 'blur' }]
}

const handleConfirm = async () => {
    if (saving.value) return
    const valid = await formRef.value?.validate().catch(() => false)
    if (!valid || saving.value) return
    saving.value = true
    try {
        const total = toNumber(formData.totalLessons)
        const remain = toNumber(formData.remainLessons)
        await adjustUserCourse({
            id: props.row?.id,
            ...(total === null ? {} : { totalLessons: total }),
            ...(remain === null ? {} : { remainLessons: remain }),
            remark: formData.remark
        })
        feedback.msgSuccess('课时调整成功')
        emit('success')
        popupRef.value?.close()
    } catch {
        // 请求层已提示错误，保留弹窗便于修改
    } finally {
        saving.value = false
    }
}

const handleClose = () => {
    emit('update:show', false)
    formRef.value?.resetFields()
    formData.totalLessons = ''
    formData.remainLessons = ''
    formData.remark = ''
}

watch(
    () => props.show,
    (val) => {
        if (val) {
            popupRef.value?.open()
        } else {
            popupRef.value?.close()
        }
    }
)
</script>
