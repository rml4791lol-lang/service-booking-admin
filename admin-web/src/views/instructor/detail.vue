<template>
    <popup
        ref="popupRef"
        title="教练详情"
        width="820px"
        :cancel-button-text="false"
        confirm-button-text="关闭"
        @close="emit('close')"
    >
        <div v-loading="loading">
            <div class="mb-4 flex items-center gap-4">
                <el-avatar :size="64" :src="detail.avatar">
                    {{ (detail.name || '').slice(0, 1) }}
                </el-avatar>
                <div>
                    <div class="text-lg font-semibold">{{ detail.name || '-' }}</div>
                    <div class="mt-1 flex items-center gap-2">
                        <el-tag :type="statusType(detail.status)" size="small">
                            {{ statusLabel(detail.status) }}
                        </el-tag>
                        <span class="text-sm text-gray-400">ID：{{ detail.id }}</span>
                    </div>
                </div>
            </div>

            <el-descriptions :column="2" border>
                <el-descriptions-item label="联系电话">
                    {{ detail.phone || '-' }}
                </el-descriptions-item>
                <el-descriptions-item label="性别">
                    {{ detail.gender === 2 ? '女' : '男' }}
                </el-descriptions-item>
                <el-descriptions-item label="身份证号">
                    {{ detail.idCard || '-' }}
                </el-descriptions-item>
                <el-descriptions-item label="专业领域">
                    {{ detail.specialty || '-' }}
                </el-descriptions-item>
                <el-descriptions-item label="从业年限">
                    {{ detail.experienceYears ?? 0 }} 年
                </el-descriptions-item>
                <el-descriptions-item label="负责班级">
                    {{ detail.classCount ?? 0 }} 个
                </el-descriptions-item>
                <el-descriptions-item label="课酬（元/课时）">
                    {{ detail.hourlyRate ?? 0 }}
                </el-descriptions-item>
                <el-descriptions-item label="评分">
                    {{ detail.ratingScore ?? 0 }}
                </el-descriptions-item>
                <el-descriptions-item label="排序">
                    {{ detail.sort ?? 0 }}
                </el-descriptions-item>
                <el-descriptions-item label="教练资质">
                    <template v-if="certificationList.length">
                        <el-tag
                            v-for="item in certificationList"
                            :key="item"
                            size="small"
                            class="mr-1 mb-1"
                        >
                            {{ item }}
                        </el-tag>
                    </template>
                    <span v-else>-</span>
                </el-descriptions-item>
                <el-descriptions-item label="可授课程" :span="2">
                    <template v-if="courseNames.length">
                        <el-tag
                            v-for="item in courseNames"
                            :key="item"
                            size="small"
                            class="mr-1 mb-1"
                        >
                            {{ item }}
                        </el-tag>
                    </template>
                    <span v-else>-</span>
                </el-descriptions-item>
                <el-descriptions-item label="服务区域" :span="2">
                    <template v-if="areaNames.length">
                        <el-tag
                            v-for="item in areaNames"
                            :key="item"
                            size="small"
                            type="success"
                            class="mr-1 mb-1"
                        >
                            {{ item }}
                        </el-tag>
                    </template>
                    <span v-else>-</span>
                </el-descriptions-item>
                <el-descriptions-item label="个人简介" :span="2">
                    {{ detail.introduction || '-' }}
                </el-descriptions-item>
            </el-descriptions>

            <div class="mt-5">
                <div class="mb-2 text-sm font-semibold">资质证书</div>
                <div v-if="certImageUrls.length" class="flex flex-wrap gap-2">
                    <el-image
                        v-for="(item, index) in certImageUrls"
                        :key="index"
                        :src="item"
                        :preview-src-list="certImageUrls"
                        :initial-index="index"
                        preview-teleported
                        fit="cover"
                        class="h-24 w-24 rounded border"
                    />
                </div>
                <div v-else class="text-sm text-gray-400">暂无证书图片</div>
            </div>

            <div class="mt-5">
                <div class="mb-2 text-sm font-semibold">钱包</div>
                <el-descriptions :column="2" border>
                    <el-descriptions-item label="可提现余额">
                        <span class="font-semibold text-primary">{{ money(detail.balance) }}</span>
                    </el-descriptions-item>
                    <el-descriptions-item label="累计已提现">
                        {{ money(detail.totalWithdrawn) }}
                    </el-descriptions-item>
                </el-descriptions>
                <el-alert
                    class="mt-3"
                    type="info"
                    :closable="false"
                    show-icon
                    title="钱包数据只读；余额与提现由财务流程维护，本页不提供提现操作。"
                />
            </div>
        </div>
    </popup>
</template>

<script lang="ts" setup name="instructorDetail">
import { getInstructorDetail, getInstructorOptions } from '@/api/instructor'
import Popup from '@/components/popup/index.vue'

const emit = defineEmits(['close'])
const popupRef = shallowRef<InstanceType<typeof Popup>>()
const loading = ref(false)
const detail = ref<any>({})
const categories = ref<any[]>([])
const communities = ref<any[]>([])

const certificationList = computed(() =>
    detail.value.certification
        ? String(detail.value.certification)
              .split(',')
              .filter((item: string) => item)
        : []
)

// 详情接口已把证书图片转换为可直接访问的绝对地址
const certImageUrls = computed(() =>
    detail.value.certImages ? String(detail.value.certImages).split(',').filter((item: string) => item) : []
)

const courseNames = computed(() => {
    const map = new Map<number, string>()
    categories.value.forEach((item) => map.set(item.id, item.name))
    return String(detail.value.teachableCourses || '')
        .split(',')
        .filter((item: string) => item)
        .map((item: string) => map.get(Number(item)) || `#${item}`)
})

const areaNames = computed(() => {
    const map = new Map<number, string>()
    communities.value.forEach((item) => map.set(item.id, item.name))
    return String(detail.value.serviceAreas || '')
        .split(',')
        .filter((item: string) => item)
        .map((item: string) => map.get(Number(item)) || `#${item}`)
})

function statusLabel(status: number) {
    return status === 1 ? '正常' : status === 2 ? '待审核' : '停用'
}

function statusType(status: number) {
    return status === 1 ? 'success' : status === 2 ? 'warning' : 'info'
}

function money(value: any) {
    return `¥${Number(value || 0).toFixed(2)}`
}

async function loadOptions() {
    try {
        const data: any = await getInstructorOptions()
        categories.value = Array.isArray(data?.categories) ? data.categories : []
        communities.value = Array.isArray(data?.communities) ? data.communities : []
    } catch {
        // 请求层已提示错误
    }
}

const open = async (row: any) => {
    if (!row?.id) return
    popupRef.value?.open()
    loading.value = true
    try {
        const data: any = await getInstructorDetail({ id: row.id })
        detail.value = data || {}
        await loadOptions()
    } catch {
        detail.value = {}
    } finally {
        loading.value = false
    }
}

defineExpose({ open })
</script>
