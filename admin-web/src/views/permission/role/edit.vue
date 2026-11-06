<template>
    <div class="edit-popup">
        <popup
            ref="popupRef"
            :title="popupTitle"
            :async="true"
            width="550px"
            @confirm="handleSubmit"
            @close="handleClose"
        >
            <el-form
                class="ls-form"
                ref="formRef"
                :rules="rules"
                :model="formData"
                label-width="60px"
            >
                <el-form-item label="名称" prop="name">
                    <el-input
                        class="ls-input"
                        v-model="formData.name"
                        placeholder="请输入名称"
                        maxlength="16"
                        show-word-limit
                        clearable
                    />
                </el-form-item>
                <el-form-item label="备注" prop="desc">
                    <el-input
                        v-model="formData.desc"
                        type="textarea"
                        :autosize="{ minRows: 4, maxRows: 6 }"
                        placeholder="请输入备注"
                        maxlength="200"
                        show-word-limit
                    />
                </el-form-item>
                <el-form-item label="排序" prop="sort">
                    <el-input-number v-model="formData.sort" :min="0" :max="9999" />
                </el-form-item>
            </el-form>
        </popup>
    </div>
</template>
<script lang="ts" setup>
import type { FormInstance } from 'element-plus'

import { roleAdd, roleEdit } from '@/api/perms/role'
import Popup from '@/components/popup/index.vue'

const emit = defineEmits(['success', 'close'])
const formRef = shallowRef<FormInstance>()
const popupRef = shallowRef<InstanceType<typeof Popup>>()
const mode = ref('add')
const popupTitle = computed(() => {
    return mode.value == 'edit' ? '编辑角色' : '新增角色'
})
const formData = reactive({
    id: '',
    name: '',
    desc: '',
    sort: 0,
    menu_id: [] as any[]
})

const rules = {
    name: [
        {
            required: true,
            message: '请输入名称',
            trigger: ['blur']
        }
    ]
}

// 后端 SystemRoleCreate/UpdateValidate 字段为 menuId（camelCase），snake_case 会被静默忽略
const rolePayload = () => ({
    id: formData.id || undefined,
    name: formData.name,
    desc: formData.desc,
    sort: formData.sort,
    menuId: formData.menu_id || []
})

const handleSubmit = async () => {
    await formRef.value?.validate()
    const payload = rolePayload()
    mode.value == 'edit' ? await roleEdit(payload) : await roleAdd(payload)
    popupRef.value?.close()
    emit('success')
}

const handleClose = () => {
    emit('close')
}

const open = (type = 'add') => {
    mode.value = type
    popupRef.value?.open()
}

const setFormData = async (data: Record<any, any>) => {
    for (const key in formData) {
        if (data[key] != null && data[key] != undefined) {
            //@ts-ignore
            formData[key] = data[key]
        }
    }
    // 角色列表/详情返回 menusId（camelCase），回填到表单的 menu_id
    if (Array.isArray(data.menusId)) {
        formData.menu_id = data.menusId
    }
}

defineExpose({
    open,
    setFormData
})
</script>
