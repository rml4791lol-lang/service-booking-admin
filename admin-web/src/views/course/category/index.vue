<template>
    <div>
        <el-card class="!border-none" shadow="never">
            <div class="flex items-center justify-between">
                <div class="text-lg font-medium">运动项目分类</div>
                <el-button v-perms="['course.class/categoryAdd']" type="primary" @click="openAdd()">
                    新增分类
                </el-button>
            </div>
        </el-card>
        <el-card class="!border-none mt-4" shadow="never">
            <el-table
                v-loading="loading"
                :data="list"
                row-key="id"
                default-expand-all
                :tree-props="{ children: 'children' }"
            >
                <el-table-column label="分类名称" prop="name" min-width="220" />
                <el-table-column label="父级" min-width="140">
                    <template #default="{ row }">{{ parentName(row.parentId) }}</template>
                </el-table-column>
                <el-table-column label="排序" prop="sort" width="100" />
                <el-table-column label="状态" width="100">
                    <template #default="{ row }">
                        <el-tag :type="row.isShow === 1 ? 'success' : 'info'">
                            {{ row.isShow === 1 ? '显示' : '隐藏' }}
                        </el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="操作" width="340" fixed="right">
                    <template #default="{ row }">
                        <el-button
                            v-perms="['course.class/categoryEdit']"
                            type="primary"
                            link
                            :disabled="moving || !canMove(row, 'up')"
                            @click="move(row, 'up')"
                        >
                            上移
                        </el-button>
                        <el-button
                            v-perms="['course.class/categoryEdit']"
                            type="primary"
                            link
                            :disabled="moving || !canMove(row, 'down')"
                            @click="move(row, 'down')"
                        >
                            下移
                        </el-button>
                        <el-button
                            v-perms="['course.class/categoryEdit']"
                            type="primary"
                            link
                            @click="openEdit(row)"
                        >
                            编辑
                        </el-button>
                        <el-button
                            v-perms="['course.class/categoryEdit']"
                            type="warning"
                            link
                            @click="toggleShow(row)"
                        >
                            {{ row.isShow === 1 ? '隐藏' : '显示' }}
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
        </el-card>

        <el-dialog
            v-model="dialogVisible"
            :title="form.id ? '编辑分类' : '新增分类'"
            width="520px"
            @closed="resetForm"
        >
            <el-form ref="formRef" :model="form" :rules="rules" label-width="90px">
                <el-form-item label="分类名称" prop="name">
                    <el-input
                        v-model="form.name"
                        maxlength="20"
                        show-word-limit
                        placeholder="请输入分类名称"
                    />
                </el-form-item>
                <el-form-item label="父级分类">
                    <el-select v-model="form.parentId" class="w-full">
                        <el-option label="根级" :value="0" />
                        <el-option
                            v-for="item in parentOptions"
                            :key="item.id"
                            :label="item.label"
                            :value="item.id"
                            :disabled="!canChooseParent(item)"
                        />
                    </el-select>
                </el-form-item>
                <el-form-item label="排序">
                    <el-input-number v-model="form.sort" :min="0" :max="65535" />
                </el-form-item>
                <el-form-item label="显示">
                    <el-switch v-model="form.isShow" :active-value="1" :inactive-value="0" />
                </el-form-item>
            </el-form>
            <template #footer>
                <el-button @click="dialogVisible = false">取消</el-button>
                <el-button type="primary" :loading="saving" @click="submit">保存</el-button>
            </template>
        </el-dialog>
    </div>
</template>

<script lang="ts" setup name="courseCategory">
import { addCategory, editCategory, getCategoryAll } from '@/api/course'
import feedback from '@/utils/feedback'

const loading = ref(false)
const saving = ref(false)
const moving = ref(false)
const list = ref<any[]>([])
const dialogVisible = ref(false)
const formRef = ref()
const form = reactive({
    id: 0,
    name: '',
    parentId: 0,
    icon: '',
    sort: 0,
    isShow: 1
})

const rules = {
    name: [
        { required: true, message: '请输入分类名称', trigger: 'blur' },
        { min: 1, max: 20, message: '分类名称在 1-20 个字符之间', trigger: 'blur' }
    ]
}

const flatList = computed(() => {
    const out: any[] = []
    const walk = (items: any[]) => {
        items.forEach((item) => {
            out.push(item)
            if (item.children) walk(item.children)
        })
    }
    walk(list.value)
    return out
})

const parentOptions = computed(() => {
    const exclude = new Set<number>()
    if (form.id) {
        const mark = (id: number) => {
            exclude.add(id)
            flatList.value
                .filter((item) => normalizeParentId(item.parentId) === id)
                .forEach((item) => mark(item.id))
        }
        mark(form.id)
    }
    return flatList.value
        .filter((item) => !exclude.has(item.id))
        .map((item) => ({
            id: item.id,
            label: item.name,
            isShow: item.isShow === 0 ? 0 : 1
        }))
})

function normalizeParentId(parentId: any) {
    return parentId ? Number(parentId) : 0
}

function compareCategory(a: any, b: any) {
    return (b.sort || 0) - (a.sort || 0) || a.id - b.id
}

function parentName(parentId: number) {
    if (!parentId) return '根级'
    return flatList.value.find((item) => item.id === parentId)?.name || `#${parentId}`
}

// 同级展示顺序（sort 倒序、id 升序），上移/下移基于它寻找相邻项
function siblingList(row: any) {
    const parentId = normalizeParentId(row.parentId)
    return flatList.value
        .filter((item) => normalizeParentId(item.parentId) === parentId)
        .sort(compareCategory)
}

function canMove(row: any, direction: 'up' | 'down') {
    const siblings = siblingList(row)
    const index = siblings.findIndex((item) => item.id === row.id)
    if (index < 0) return false
    return direction === 'up' ? index > 0 : index < siblings.length - 1
}

// 可见子级只能挂到可见父级下；隐藏子级允许选择隐藏父级
function canChooseParent(item: any) {
    return form.isShow !== 1 || item.isShow === 1
}

function buildSortUpdates(siblings: any[], from: number, to: number) {
    const current = siblings[from]
    const target = siblings[to]
    const currentSort = current.sort || 0
    const targetSort = target.sort || 0
    if (currentSort !== targetSort) {
        return [
            { id: current.id, sort: targetSort },
            { id: target.id, sort: currentSort }
        ]
    }
    // 相邻项 sort 相同时交换数值无法改变顺序，按交换后的展示顺序重排整个同级
    const reordered = siblings.slice()
    const swapped = reordered[from]
    reordered[from] = reordered[to]
    reordered[to] = swapped
    const base = Math.max(...siblings.map((item) => item.sort || 0), siblings.length - 1)
    const nextSort = new Map<number, number>()
    reordered.forEach((item, index) => nextSort.set(item.id, base - index))
    return siblings
        .filter((item) => nextSort.get(item.id) !== (item.sort || 0))
        .map((item) => ({ id: item.id, sort: nextSort.get(item.id) as number }))
}

async function move(row: any, direction: 'up' | 'down') {
    if (moving.value) return
    const siblings = siblingList(row)
    const index = siblings.findIndex((item) => item.id === row.id)
    const targetIndex = direction === 'up' ? index - 1 : index + 1
    if (index < 0 || targetIndex < 0 || targetIndex >= siblings.length) return
    const updates = buildSortUpdates(siblings, index, targetIndex)
    if (!updates.length) return
    moving.value = true
    try {
        // 部分更新：每次只传 { id, sort }
        for (const update of updates) {
            await editCategory(update)
        }
        feedback.msgSuccess('排序已更新')
    } catch {
        // 请求层已经弹出错误提示，失败后重新拉取以还原真实顺序
    } finally {
        moving.value = false
        await load()
    }
}

async function load() {
    loading.value = true
    try {
        const data: any = await getCategoryAll({ all: 1, tree: 1 })
        list.value = Array.isArray(data) ? data : []
    } catch {
        list.value = []
    } finally {
        loading.value = false
    }
}

// onMounted / onActivated 首次进入只允许触发一次列表请求
let hasLoaded = false
let activationCount = 0
function loadOnce() {
    if (hasLoaded) return
    hasLoaded = true
    load()
}
onMounted(loadOnce)
onActivated(() => {
    activationCount++
    if (activationCount === 1) {
        loadOnce()
        return
    }
    load()
})

function resetForm() {
    form.id = 0
    form.name = ''
    form.parentId = 0
    form.icon = ''
    form.sort = 0
    form.isShow = 1
    formRef.value?.clearValidate()
}

function openAdd(parentId = 0) {
    resetForm()
    form.parentId = parentId
    dialogVisible.value = true
}

function openEdit(row: any) {
    form.id = row.id
    form.name = row.name
    form.parentId = normalizeParentId(row.parentId)
    form.icon = row.icon || ''
    form.sort = row.sort || 0
    form.isShow = row.isShow === 0 ? 0 : 1
    dialogVisible.value = true
}

// 表单提交只构造一次 payload
function toPayload(source: typeof form) {
    return {
        id: source.id,
        name: source.name.trim(),
        parentId: source.parentId || 0,
        icon: source.icon || '',
        sort: source.sort || 0,
        isShow: source.isShow
    }
}

async function submit() {
    const valid = await formRef.value?.validate().catch(() => false)
    if (!valid || saving.value) return
    saving.value = true
    try {
        const payload = toPayload(form)
        if (form.id) await editCategory(payload)
        else await addCategory(payload)
        dialogVisible.value = false
        feedback.msgSuccess('保存成功')
        await load()
    } catch {
        // 请求层已经弹出错误提示
    } finally {
        saving.value = false
    }
}

async function toggleShow(row: any) {
    const next = row.isShow === 1 ? 0 : 1
    try {
        // 显隐只传部分字段，避免覆盖表单外的数据
        await editCategory({ id: row.id, isShow: next })
        feedback.msgSuccess(next === 1 ? '已显示' : '已隐藏')
        await load()
    } catch {
        // 请求层已经弹出错误提示
    }
}
</script>
