<template>
    <div class="region-page">
        <el-card class="!border-none" shadow="never">
            <div class="flex items-center justify-between">
                <div>
                    <div class="text-lg font-medium">区域维护</div>
                    <div class="text-tx-secondary text-sm mt-1">
                        维护「社区 → 小区」两级区域；小区可绑定社区，课程班级再绑定小区。
                    </div>
                </div>
                <el-button v-if="activeTab === 'community'" v-perms="['community/add']" type="primary" @click="openCommunityAdd">
                    <template #icon><icon name="el-icon-Plus" /></template>
                    新增社区
                </el-button>
                <el-button v-else v-perms="['area/add']" type="primary" @click="openAreaAdd">
                    <template #icon><icon name="el-icon-Plus" /></template>
                    新增小区
                </el-button>
            </div>
        </el-card>

        <el-tabs v-model="activeTab" class="mt-4">
            <el-tab-pane label="社区" name="community">
                <el-card class="!border-none" shadow="never">
                    <el-form class="mb-[-16px]" :inline="true" :model="communityQuery">
                        <el-form-item label="社区名称">
                            <el-input
                                class="w-[220px]"
                                v-model="communityQuery.name"
                                clearable
                                placeholder="请输入社区名称"
                                @keyup.enter="resetCommunityPage"
                            />
                        </el-form-item>
                        <el-form-item label="联系电话">
                            <el-input
                                class="w-[200px]"
                                v-model="communityQuery.mobile"
                                clearable
                                placeholder="请输入联系电话"
                                @keyup.enter="resetCommunityPage"
                            />
                        </el-form-item>
                        <el-form-item label="所属街道">
                            <el-select
                                class="w-[240px]"
                                v-model="communityQuery.regionId"
                                clearable
                                filterable
                                placeholder="全部街道"
                            >
                                <el-option
                                    v-for="item in streets"
                                    :key="item.id"
                                    :label="item.fullName || item.name"
                                    :value="item.id"
                                />
                            </el-select>
                        </el-form-item>
                        <el-form-item>
                            <el-button type="primary" @click="resetCommunityPage">查询</el-button>
                            <el-button @click="resetCommunityParams">重置</el-button>
                        </el-form-item>
                    </el-form>
                </el-card>

                <el-card class="!border-none mt-4" shadow="never">
                    <el-table size="large" v-loading="communityPager.loading" :data="communityPager.lists">
                        <el-table-column label="ID" prop="id" width="70" />
                        <el-table-column label="社区名称" prop="name" min-width="140" show-overflow-tooltip />
                        <el-table-column label="所属街道" min-width="190" show-overflow-tooltip>
                            <template #default="{ row }">{{ regionText(row) }}</template>
                        </el-table-column>
                        <el-table-column label="地址" prop="address" min-width="220" show-overflow-tooltip>
                            <template #default="{ row }">{{ row.address || '-' }}</template>
                        </el-table-column>
                        <el-table-column label="坐标" min-width="150">
                            <template #default="{ row }">{{ coordText(row) }}</template>
                        </el-table-column>
                        <el-table-column label="联系电话" prop="mobile" min-width="130" />
                        <el-table-column label="小区数" prop="areaCount" width="90" />
                        <el-table-column label="操作" width="100" fixed="right">
                            <template #default="{ row }">
                                <el-button v-perms="['community/edit']" type="primary" link @click="openCommunityEdit(row)">
                                    编辑
                                </el-button>
                            </template>
                        </el-table-column>
                    </el-table>
                    <div class="flex justify-end mt-4">
                        <pagination v-model="communityPager" @change="getCommunityLists" />
                    </div>
                </el-card>
            </el-tab-pane>

            <el-tab-pane label="小区" name="area">
                <el-card class="!border-none" shadow="never">
                    <el-form class="mb-[-16px]" :inline="true" :model="areaQuery">
                        <el-form-item label="关键词">
                            <el-input
                                class="w-[220px]"
                                v-model="areaQuery.keyword"
                                clearable
                                placeholder="小区名称 / 地址"
                                @keyup.enter="resetAreaPage"
                            />
                        </el-form-item>
                        <el-form-item label="所属社区">
                            <el-select
                                class="w-[220px]"
                                v-model="areaQuery.communityId"
                                clearable
                                filterable
                                placeholder="全部社区"
                            >
                                <el-option
                                    v-for="item in communityOptions"
                                    :key="item.id"
                                    :label="item.name"
                                    :value="item.id"
                                />
                            </el-select>
                        </el-form-item>
                        <el-form-item label="状态">
                            <el-select class="w-[140px]" v-model="areaQuery.status" clearable placeholder="全部">
                                <el-option label="启用" :value="1" />
                                <el-option label="停用" :value="0" />
                            </el-select>
                        </el-form-item>
                        <el-form-item>
                            <el-button type="primary" @click="resetAreaPage">查询</el-button>
                            <el-button @click="resetAreaParams">重置</el-button>
                        </el-form-item>
                    </el-form>
                </el-card>

                <el-card class="!border-none mt-4" shadow="never">
                    <el-table size="large" v-loading="areaPager.loading" :data="areaPager.lists">
                        <el-table-column label="ID" prop="id" width="70" />
                        <el-table-column label="小区名称" prop="name" min-width="150" show-overflow-tooltip />
                        <el-table-column label="所属社区" min-width="150" show-overflow-tooltip>
                            <template #default="{ row }">{{ row.communityName || '未绑定' }}</template>
                        </el-table-column>
                        <el-table-column label="所属街道" min-width="150" show-overflow-tooltip>
                            <template #default="{ row }">{{ row.streetName || '-' }}</template>
                        </el-table-column>
                        <el-table-column label="地址" prop="address" min-width="240" show-overflow-tooltip>
                            <template #default="{ row }">{{ row.address || '-' }}</template>
                        </el-table-column>
                        <el-table-column label="坐标" min-width="140">
                            <template #default="{ row }">{{ coordText(row) }}</template>
                        </el-table-column>
                        <el-table-column label="班级数" prop="classCount" width="90" />
                        <el-table-column label="排序" prop="sort" width="80" />
                        <el-table-column label="状态" width="100">
                            <template #default="{ row }">
                                <el-switch
                                    v-perms="['area/edit']"
                                    v-model="row.status"
                                    :active-value="1"
                                    :inactive-value="0"
                                    @change="toggleAreaStatus(row)"
                                />
                            </template>
                        </el-table-column>
                        <el-table-column label="操作" width="150" fixed="right">
                            <template #default="{ row }">
                                <el-button v-perms="['area/edit']" type="primary" link @click="openAreaEdit(row)">
                                    编辑
                                </el-button>
                                <el-button v-perms="['area/del']" type="danger" link @click="handleAreaDelete(row)">
                                    删除
                                </el-button>
                            </template>
                        </el-table-column>
                    </el-table>
                    <div class="flex justify-end mt-4">
                        <pagination v-model="areaPager" @change="getAreaLists" />
                    </div>
                </el-card>
            </el-tab-pane>
        </el-tabs>

        <!-- 社区编辑 -->
        <el-dialog
            v-model="communityDialogVisible"
            :title="communityForm.id ? '编辑社区' : '新增社区'"
            width="640px"
            @closed="resetCommunityForm"
        >
            <el-form ref="communityFormRef" :model="communityForm" :rules="communityRules" label-width="100px">
                <el-row :gutter="12">
                    <el-col :span="12">
                        <el-form-item label="社区名称" prop="name">
                            <el-input v-model="communityForm.name" maxlength="64" show-word-limit placeholder="请输入社区名称" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="联系电话" prop="mobile">
                            <el-input v-model="communityForm.mobile" maxlength="20" placeholder="请输入联系电话" />
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="12">
                    <el-col :span="12">
                        <el-form-item label="所属街道">
                            <el-select
                                v-model="communityForm.regionId"
                                class="w-full"
                                clearable
                                filterable
                                placeholder="请选择街道"
                            >
                                <el-option
                                    v-for="item in streets"
                                    :key="item.id"
                                    :label="item.fullName || item.name"
                                    :value="item.id"
                                />
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-form-item label="详细地址">
                    <el-input v-model="communityForm.address" maxlength="255" show-word-limit placeholder="如：XX市XX区XX街道XX小区" />
                </el-form-item>
                <el-row :gutter="12">
                    <el-col :span="12">
                        <el-form-item label="纬度">
                            <el-input v-model="communityForm.latitude" placeholder="如 27.799500，可留空" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="经度">
                            <el-input v-model="communityForm.longitude" placeholder="如 113.122000，可留空" />
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="12">
                    <el-col :span="12">
                        <el-form-item label="开户银行">
                            <el-input v-model="communityForm.bankName" maxlength="64" placeholder="选填" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="银行卡号">
                            <el-input v-model="communityForm.bankCard" maxlength="64" placeholder="选填" />
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-form-item v-if="!communityForm.id" label="登录密码" prop="password">
                    <el-input v-model="communityForm.password" type="password" show-password maxlength="32" placeholder="社区主体后台登录初始密码" />
                </el-form-item>
            </el-form>
            <template #footer>
                <el-button @click="communityDialogVisible = false">取消</el-button>
                <el-button type="primary" :loading="communitySaving" @click="submitCommunity">保存</el-button>
            </template>
        </el-dialog>

        <!-- 小区编辑 -->
        <el-dialog
            v-model="areaDialogVisible"
            :title="areaForm.id ? '编辑小区' : '新增小区'"
            width="640px"
            @closed="resetAreaForm"
        >
            <el-form ref="areaFormRef" :model="areaForm" :rules="areaRules" label-width="100px">
                <el-row :gutter="12">
                    <el-col :span="12">
                        <el-form-item label="小区名称" prop="name">
                            <el-input v-model="areaForm.name" maxlength="64" show-word-limit placeholder="请输入小区名称" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="所属社区">
                            <el-select v-model="areaForm.communityId" class="w-full" filterable placeholder="请选择社区">
                                <el-option label="未绑定（待确认）" :value="0" />
                                <el-option
                                    v-for="item in communityOptions"
                                    :key="item.id"
                                    :label="item.name"
                                    :value="item.id"
                                />
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-form-item label="详细地址">
                    <el-input v-model="areaForm.address" maxlength="255" show-word-limit placeholder="如：XX市XX区XX街道XX小区" />
                </el-form-item>
                <el-row :gutter="12">
                    <el-col :span="12">
                        <el-form-item label="纬度">
                            <el-input v-model="areaForm.latitude" placeholder="如 27.834940，可留空" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="经度">
                            <el-input v-model="areaForm.longitude" placeholder="如 113.111184，可留空" />
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="12">
                    <el-col :span="12">
                        <el-form-item label="封面图">
                            <el-input v-model="areaForm.cover" maxlength="255" placeholder="图片地址，选填" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="距离文案">
                            <el-input v-model="areaForm.distance" maxlength="32" placeholder="如 480m，选填" />
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-form-item label="标签">
                    <el-input v-model="areaForm.tags" maxlength="255" placeholder="逗号分隔，如：篮球场,塑胶跑道" />
                </el-form-item>
                <el-row :gutter="12">
                    <el-col :span="8">
                        <el-form-item label="排序">
                            <el-input-number v-model="areaForm.sort" :min="0" :max="65535" class="w-full" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="状态">
                            <el-switch v-model="areaForm.status" :active-value="1" :inactive-value="0" />
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-form-item label="备注">
                    <el-input v-model="areaForm.remark" type="textarea" :rows="2" maxlength="255" show-word-limit placeholder="选填" />
                </el-form-item>
            </el-form>
            <template #footer>
                <el-button @click="areaDialogVisible = false">取消</el-button>
                <el-button type="primary" :loading="areaSaving" @click="submitArea">保存</el-button>
            </template>
        </el-dialog>
    </div>
</template>

<script lang="ts" setup name="regionIndex">
import {
    areaAdd,
    areaDelete,
    areaEdit,
    areaList,
    communityAdd,
    communityAll,
    communityDetail,
    communityEdit,
    communityList,
    regionStreets
} from '@/api/region'
import { usePaging } from '@/hooks/usePaging'
import feedback from '@/utils/feedback'

const activeTab = ref('community')
const streets = ref<any[]>([])
const communityOptions = ref<any[]>([])

const communityQuery = reactive({
    name: '',
    mobile: '',
    regionId: ''
})
const areaQuery = reactive({
    keyword: '',
    communityId: '',
    status: ''
})

const {
    pager: communityPager,
    getLists: getCommunityLists,
    resetPage: resetCommunityPage,
    resetParams: resetCommunityParams
} = usePaging({
    fetchFun: communityList,
    params: communityQuery
})

const {
    pager: areaPager,
    getLists: getAreaLists,
    resetPage: resetAreaPage,
    resetParams: resetAreaParams
} = usePaging({
    fetchFun: areaList,
    params: areaQuery
})

const communityDialogVisible = ref(false)
const communitySaving = ref(false)
const communityFormRef = ref()
const communityForm = reactive({
    id: 0,
    name: '',
    mobile: '',
    password: '',
    // 该比例字段已从后台页面下线（#102/#112），不再展示与校验；但后端
    // CommunityCreate/UpdateValidate 仍要求非空，故保留为内部回传值：
    // 编辑时原样带回详情中的比例；新增按 0（不做社区分成，兼容后端 @NotNull），
    // 避免无字段却静默写入历史 3% 默认值。
    commissionRate: 0,
    regionId: null as number | null,
    address: '',
    latitude: '' as string | number,
    longitude: '' as string | number,
    bankName: '',
    bankCard: ''
})

const communityRules = computed(() => ({
    name: [{ required: true, message: '请输入社区名称', trigger: 'blur' }],
    mobile: [{ required: true, message: '请输入联系电话', trigger: 'blur' }],
    ...(communityForm.id
        ? {}
        : {
              password: [{ required: true, message: '请输入登录密码', trigger: 'blur' }]
          })
}))

const areaDialogVisible = ref(false)
const areaSaving = ref(false)
const areaFormRef = ref()
const areaForm = reactive({
    id: 0,
    communityId: 0,
    name: '',
    address: '',
    latitude: '' as string | number,
    longitude: '' as string | number,
    cover: '',
    tags: '',
    distance: '',
    sort: 0,
    status: 1,
    remark: ''
})

const areaRules = {
    name: [{ required: true, message: '请输入小区名称', trigger: 'blur' }]
}

function regionText(row: any) {
    return [row.cityName, row.districtName, row.streetName].filter(Boolean).join(' / ') || '-'
}

function coordText(row: any) {
    if (row.latitude !== undefined) {
        if (row.latitude === null || row.longitude === null) return '-'
        return `${row.latitude}, ${row.longitude}`
    }
    if (row.lat === null || row.lat === undefined || row.lng === null || row.lng === undefined) return '-'
    return `${row.lat}, ${row.lng}`
}

function toCoord(value: any) {
    if (value === '' || value === null || value === undefined) return null
    const num = Number(value)
    return Number.isFinite(num) ? num : null
}

function coordPairOk(lat: any, lng: any) {
    const hasLat = lat !== '' && lat !== null && lat !== undefined
    const hasLng = lng !== '' && lng !== null && lng !== undefined
    return hasLat === hasLng
}

async function loadOptions() {
    try {
        const [streetData, communityData] = await Promise.all([regionStreets(), communityAll()])
        streets.value = streetData || []
        communityOptions.value = communityData || []
    } catch {
        // 请求层已提示
    }
}

function resetCommunityForm() {
    communityForm.id = 0
    communityForm.name = ''
    communityForm.mobile = ''
    communityForm.password = ''
    communityForm.commissionRate = 0
    communityForm.regionId = null
    communityForm.address = ''
    communityForm.latitude = ''
    communityForm.longitude = ''
    communityForm.bankName = ''
    communityForm.bankCard = ''
    communityFormRef.value?.clearValidate()
}

function openCommunityAdd() {
    resetCommunityForm()
    communityDialogVisible.value = true
}

async function openCommunityEdit(row: any) {
    resetCommunityForm()
    let detail: any = row
    try {
        detail = (await communityDetail({ id: row.id })) || row
    } catch {
        // 列表数据也可编辑基础字段
    }
    communityForm.id = detail.id
    communityForm.name = detail.name || ''
    communityForm.mobile = detail.mobile || ''
    communityForm.password = ''
    communityForm.commissionRate = Number(detail.commissionRate || 0)
    communityForm.regionId = detail.regionId > 0 ? detail.regionId : null
    communityForm.address = detail.address || ''
    communityForm.latitude = detail.latitude === null || detail.latitude === undefined ? '' : String(detail.latitude)
    communityForm.longitude = detail.longitude === null || detail.longitude === undefined ? '' : String(detail.longitude)
    communityForm.bankName = detail.bankName || ''
    communityForm.bankCard = detail.bankCard || ''
    communityDialogVisible.value = true
}

async function submitCommunity() {
    const valid = await communityFormRef.value?.validate().catch(() => false)
    if (!valid || communitySaving.value) return
    if (!coordPairOk(communityForm.latitude, communityForm.longitude)) {
        feedback.msgError('纬度、经度需要同时填写或同时留空')
        return
    }
    communitySaving.value = true
    try {
        const latitude = toCoord(communityForm.latitude)
        const longitude = toCoord(communityForm.longitude)
        const payload: any = {
            id: communityForm.id || undefined,
            name: communityForm.name.trim(),
            mobile: communityForm.mobile.trim(),
            commissionRate: communityForm.commissionRate,
            regionId: communityForm.regionId || 0,
            address: communityForm.address,
            latitude,
            longitude,
            bankName: communityForm.bankName,
            bankCard: communityForm.bankCard
        }
        if (communityForm.id) {
            // 后台只在该标记为 true 时清空坐标，避免老页面编辑社区误清空
            payload.clearLocation = latitude === null && longitude === null
            await communityEdit(payload)
        } else {
            payload.password = communityForm.password
            await communityAdd(payload)
        }
        communityDialogVisible.value = false
        feedback.msgSuccess('保存成功')
        await Promise.all([getCommunityLists(), loadOptions()])
    } catch {
        // 请求层已提示
    } finally {
        communitySaving.value = false
    }
}

function resetAreaForm() {
    areaForm.id = 0
    areaForm.communityId = 0
    areaForm.name = ''
    areaForm.address = ''
    areaForm.latitude = ''
    areaForm.longitude = ''
    areaForm.cover = ''
    areaForm.tags = ''
    areaForm.distance = ''
    areaForm.sort = 0
    areaForm.status = 1
    areaForm.remark = ''
    areaFormRef.value?.clearValidate()
}

function openAreaAdd() {
    resetAreaForm()
    areaDialogVisible.value = true
}

function openAreaEdit(row: any) {
    resetAreaForm()
    areaForm.id = row.id
    areaForm.communityId = row.communityId || 0
    areaForm.name = row.name || ''
    areaForm.address = row.address || ''
    areaForm.latitude = row.lat === null || row.lat === undefined ? '' : String(row.lat)
    areaForm.longitude = row.lng === null || row.lng === undefined ? '' : String(row.lng)
    areaForm.cover = row.cover || ''
    areaForm.tags = row.tagsText || ''
    areaForm.distance = row.distance || ''
    areaForm.sort = row.sort || 0
    areaForm.status = row.status === 0 ? 0 : 1
    areaForm.remark = row.remark || ''
    areaDialogVisible.value = true
}

function areaPayload() {
    return {
        id: areaForm.id || undefined,
        communityId: areaForm.communityId || 0,
        name: areaForm.name.trim(),
        address: areaForm.address,
        lat: toCoord(areaForm.latitude),
        lng: toCoord(areaForm.longitude),
        cover: areaForm.cover,
        tags: areaForm.tags,
        distance: areaForm.distance,
        sort: areaForm.sort,
        status: areaForm.status,
        remark: areaForm.remark
    }
}

async function submitArea() {
    const valid = await areaFormRef.value?.validate().catch(() => false)
    if (!valid || areaSaving.value) return
    if (!coordPairOk(areaForm.latitude, areaForm.longitude)) {
        feedback.msgError('纬度、经度需要同时填写或同时留空')
        return
    }
    areaSaving.value = true
    try {
        const payload = areaPayload()
        if (areaForm.id) await areaEdit(payload)
        else await areaAdd(payload)
        areaDialogVisible.value = false
        feedback.msgSuccess('保存成功')
        await getAreaLists()
    } catch {
        // 请求层已提示
    } finally {
        areaSaving.value = false
    }
}

async function toggleAreaStatus(row: any) {
    try {
        await areaEdit({
            id: row.id,
            communityId: row.communityId || 0,
            name: row.name,
            address: row.address || '',
            lat: row.lat,
            lng: row.lng,
            cover: row.cover || '',
            tags: row.tagsText || '',
            distance: row.distance || '',
            sort: row.sort || 0,
            status: row.status,
            remark: row.remark || ''
        })
        feedback.msgSuccess(row.status === 1 ? '已启用' : '已停用')
    } catch {
        await getAreaLists()
    }
}

async function handleAreaDelete(row: any) {
    await feedback.confirm('确定删除该小区？删除前请先解除课程班级绑定。')
    try {
        await areaDelete({ id: row.id })
        feedback.msgSuccess('删除成功')
        await getAreaLists()
    } catch {
        // 请求层已提示
    }
}

loadOptions()
getCommunityLists()
getAreaLists()

onActivated(() => {
    loadOptions()
    getCommunityLists()
    getAreaLists()
})
</script>
