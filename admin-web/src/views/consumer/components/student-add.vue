<template>
    <el-dialog
        v-model="visible"
        title="新增学员"
        width="520px"
        class="student-add-dialog"
        :close-on-click-modal="false"
        @closed="resetForm"
    >
        <el-form ref="formRef" :model="form" :rules="rules" label-width="90px">
            <el-form-item label="手机号" prop="mobile">
                <el-input
                    v-model="form.mobile"
                    placeholder="请输入手机号"
                    maxlength="11"
                    clearable
                />
            </el-form-item>
            <el-form-item label="密码" prop="password">
                <el-input
                    v-model="form.password"
                    type="password"
                    show-password
                    placeholder="请输入密码"
                    maxlength="20"
                />
            </el-form-item>
            <el-form-item label="昵称" prop="nickname">
                <el-input
                    v-model="form.nickname"
                    placeholder="请输入昵称"
                    maxlength="32"
                    clearable
                />
            </el-form-item>
            <div class="pl-[90px] text-sm leading-6 text-tx-secondary">
                手机号即家长登录账号；新增后可在「用户详情 - 学员档案」关联学员、查看课时。
            </div>
        </el-form>
        <template #footer>
            <el-button @click="visible = false">取消</el-button>
            <el-button type="primary" :loading="saving" @click="handleSave">保存</el-button>
        </template>
    </el-dialog>
</template>

<script lang="ts" setup name="StudentAdd">
import type { FormInstance } from 'element-plus'
import { computed, reactive, shallowRef } from 'vue'

import { addUser } from '@/api/consumer'
import feedback from '@/utils/feedback'

const props = defineProps<{
    show: boolean
}>()

const emit = defineEmits<{
    (event: 'update:show', value: boolean): void
    (event: 'success'): void
}>()

const visible = computed({
    get: () => props.show,
    set: (value: boolean) => emit('update:show', value)
})

const formRef = shallowRef<FormInstance>()
const saving = ref(false)
const form = reactive({
    mobile: '',
    password: '',
    nickname: ''
})

const rules = {
    mobile: [
        { required: true, message: '请输入手机号', trigger: ['blur', 'change'] },
        {
            pattern: /^1[3-9]\d{9}$/,
            message: '手机号格式不正确',
            trigger: ['blur', 'change']
        }
    ],
    password: [
        { required: true, message: '请输入密码', trigger: ['blur', 'change'] },
        { min: 6, max: 20, message: '密码必须在6~20个字符内', trigger: ['blur'] }
    ]
}

const resetForm = () => {
    form.mobile = ''
    form.password = ''
    form.nickname = ''
    formRef.value?.clearValidate()
}

const handleSave = async () => {
    await formRef.value?.validate()
    saving.value = true
    try {
        await addUser({
            mobile: form.mobile,
            password: form.password,
            nickname: form.nickname
        })
        feedback.msgSuccess('新增成功')
        visible.value = false
        emit('success')
    } catch (error) {
        // 错误提示由请求拦截器统一弹出（如「该手机号已注册」），保留弹窗便于修改
    } finally {
        saving.value = false
    }
}
</script>
