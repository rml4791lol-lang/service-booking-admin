<template>
    <div class="class-page">
        <el-card class="!border-none" shadow="never">
            <div class="flex items-center justify-between">
                <div>
                    <div class="text-lg font-medium">课程班级</div>
                    <div class="text-tx-secondary text-sm mt-1">
                        管理线上班级：新增、编辑、上下架、软删除；维护封面/简介/图文介绍与排课。
                        有报名或订单引用的课程不能删除，请改为下架。
                    </div>
                </div>
                <div class="flex flex-wrap items-center justify-end gap-2">
                    <el-date-picker
                        v-model="exportDateRange"
                        class="!w-[240px]"
                        type="daterange"
                        value-format="YYYY-MM-DD"
                        :clearable="false"
                        start-placeholder="导出开始日期"
                        end-placeholder="导出结束日期"
                    />
                    <el-button
                        v-perms="['report/course/export']"
                        type="primary"
                        plain
                        :loading="exporting"
                        @click="exportCourseExcel"
                    >
                        导出课程报表
                    </el-button>
                    <el-button v-perms="['course.class/add']" type="primary" @click="openAdd">
                        <template #icon><icon name="el-icon-Plus" /></template>
                        新增班级
                    </el-button>
                </div>
            </div>
        </el-card>

        <el-card class="!border-none mt-4" shadow="never">
            <el-form class="mb-[-16px]" :inline="true" :model="query">
                <el-form-item label="班级名称">
                    <el-input
                        v-model="query.name"
                        class="w-[200px]"
                        clearable
                        placeholder="请输入班级名称"
                        @keyup.enter="resetPage"
                    />
                </el-form-item>
                <el-form-item label="运动类目">
                    <el-select
                        v-model="query.categoryId"
                        class="w-[220px]"
                        clearable
                        filterable
                        placeholder="全部类目"
                    >
                        <el-option
                            v-for="item in categoryOptions"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value"
                        />
                    </el-select>
                </el-form-item>
                <el-form-item label="社区">
                    <el-select
                        v-model="query.communityId"
                        class="w-[180px]"
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
                    <el-select
                        v-model="query.status"
                        class="w-[140px]"
                        clearable
                        placeholder="全部状态"
                    >
                        <el-option
                            v-for="item in STATUS_OPTIONS"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value"
                        />
                    </el-select>
                </el-form-item>
                <el-form-item label="上架">
                    <el-select
                        v-model="query.isShow"
                        class="w-[130px]"
                        clearable
                        placeholder="全部"
                    >
                        <el-option label="上架" :value="1" />
                        <el-option label="下架" :value="0" />
                    </el-select>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="resetPage">查询</el-button>
                    <el-button @click="resetParams">重置</el-button>
                </el-form-item>
            </el-form>
        </el-card>

        <el-card class="!border-none mt-4" shadow="never">
            <el-table
                size="large"
                table-layout="fixed"
                v-loading="pager.loading"
                :data="pager.lists"
            >
                <el-table-column label="ID" prop="id" width="55" />
                <el-table-column
                    label="班级名称"
                    prop="name"
                    min-width="140"
                    show-overflow-tooltip
                />
                <el-table-column
                    label="类目"
                    prop="categoryName"
                    min-width="90"
                    show-overflow-tooltip
                >
                    <template #default="{ row }">{{
                        row.categoryName || categoryName(row.categoryId)
                    }}</template>
                </el-table-column>
                <el-table-column
                    label="教练"
                    prop="instructorName"
                    min-width="90"
                    show-overflow-tooltip
                >
                    <template #default="{ row }">{{
                        row.instructorName || instructorName(row.instructorId)
                    }}</template>
                </el-table-column>
                <el-table-column label="社区 / 小区" min-width="140" show-overflow-tooltip>
                    <template #default="{ row }">{{ regionText(row) }}</template>
                </el-table-column>
                <el-table-column label="代理" min-width="80" show-overflow-tooltip>
                    <template #default="{ row }">{{ agentName(row.agentId) }}</template>
                </el-table-column>
                <el-table-column label="场馆" prop="venue" min-width="110" show-overflow-tooltip>
                    <template #default="{ row }">{{ row.venue || '-' }}</template>
                </el-table-column>
                <el-table-column label="排课" min-width="160" show-overflow-tooltip>
                    <template #default="{ row }">{{ scheduleText(row) }}</template>
                </el-table-column>
                <el-table-column label="课时/费用" width="140">
                    <template #default="{ row }">
                        <div>{{ row.totalLessons || 0 }} 节</div>
                        <div class="text-tx-secondary text-xs">
                            {{ money(row.feePerLesson) }}/节
                        </div>
                        <el-tooltip
                            v-if="lessonDataAbnormal(row)"
                            :content="lessonDataAbnormalReason(row)"
                            placement="top"
                        >
                            <el-tag type="danger" size="small" effect="plain" class="mt-1">
                                课时数据异常
                            </el-tag>
                        </el-tooltip>
                    </template>
                </el-table-column>
                <el-table-column label="适龄/难度" width="110" show-overflow-tooltip>
                    <template #default="{ row }">
                        <div>{{ ageRangeText(row) }}</div>
                        <div class="text-tx-secondary text-xs">{{ row.level || '-' }}</div>
                    </template>
                </el-table-column>
                <el-table-column label="报名 / 上限" width="90">
                    <template #default="{ row }">
                        <span
                            :class="
                                Number(row.studentCount) >= Number(row.maxStudents)
                                    ? 'text-red-500'
                                    : ''
                            "
                        >
                            {{ row.studentCount || 0 }} / {{ row.maxStudents || 0 }}
                        </span>
                    </template>
                </el-table-column>
                <el-table-column label="状态" width="80">
                    <template #default="{ row }">
                        <el-tag :type="statusTag(row.status)">{{ statusText(row.status) }}</el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="上架" width="80">
                    <template #default="{ row }">
                        <el-tag :type="Number(row.isShow) === 1 ? 'success' : 'info'">
                            {{ Number(row.isShow) === 1 ? '上架' : '下架' }}
                        </el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="介绍" width="80">
                    <template #default="{ row }">
                        <el-tag
                            :type="Number(row.hasIntro) === 1 ? 'success' : 'info'"
                            effect="plain"
                        >
                            {{ Number(row.hasIntro) === 1 ? '已维护' : '未维护' }}
                        </el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="操作" width="300" fixed="right">
                    <template #default="{ row }">
                        <el-button
                            v-perms="['course.class/detail']"
                            type="primary"
                            link
                            @click="openDetail(row)"
                        >
                            详情
                        </el-button>
                        <el-button
                            v-perms="['course.class/edit']"
                            type="primary"
                            link
                            @click="openEdit(row)"
                        >
                            编辑
                        </el-button>
                        <el-button
                            v-perms="['course.class/regenerateLessons']"
                            type="primary"
                            link
                            :loading="regeneratingId === row.id"
                            @click="handleRegenerate(row)"
                        >
                            重建场次
                        </el-button>
                        <el-button
                            v-perms="['course.class/status']"
                            type="warning"
                            link
                            :loading="statusSavingId === row.id"
                            @click="toggleShow(row)"
                        >
                            {{ Number(row.isShow) === 1 ? '下架' : '上架' }}
                        </el-button>
                        <el-button
                            v-perms="['course.class/del']"
                            type="danger"
                            link
                            :loading="deletingId === row.id"
                            @click="handleDelete(row)"
                        >
                            删除
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
            <div class="flex justify-end mt-4">
                <pagination v-model="pager" @change="getLists" />
            </div>
        </el-card>

        <el-dialog
            v-model="formVisible"
            :title="isEdit ? '编辑班级' : '新增班级'"
            width="860px"
            top="5vh"
            :close-on-click-modal="false"
            @closed="resetForm"
        >
            <el-form ref="formRef" :model="form" :rules="formRules" label-width="100px">
                <el-tabs v-model="formTab" class="class-form-tabs">
                    <el-tab-pane label="基本信息" name="base">
                        <div class="form-section-title">基本信息</div>
                        <el-row :gutter="12">
                            <el-col :span="12">
                                <el-form-item label="班级名称" prop="name">
                                    <el-input
                                        v-model="form.name"
                                        maxlength="100"
                                        show-word-limit
                                        placeholder="请输入班级名称"
                                    />
                                </el-form-item>
                            </el-col>
                            <el-col :span="12">
                                <el-form-item label="运动类目" prop="categoryId">
                                    <el-select
                                        v-model="form.categoryId"
                                        class="w-full"
                                        filterable
                                        placeholder="请选择运动类目"
                                    >
                                        <el-option
                                            v-for="item in categoryOptions"
                                            :key="item.value"
                                            :label="item.label"
                                            :value="item.value"
                                        />
                                    </el-select>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row :gutter="12">
                            <el-col :span="12">
                                <el-form-item label="教练" prop="instructorId">
                                    <el-select
                                        v-model="form.instructorId"
                                        class="w-full"
                                        filterable
                                        placeholder="请选择教练"
                                    >
                                        <el-option
                                            v-for="item in instructorOptions"
                                            :key="item.id"
                                            :label="
                                                item.name + (item.status === 1 ? '' : '（停用）')
                                            "
                                            :value="item.id"
                                        />
                                    </el-select>
                                </el-form-item>
                            </el-col>
                            <el-col :span="12">
                                <el-form-item label="代理">
                                    <el-select
                                        v-model="form.agentId"
                                        class="w-full"
                                        clearable
                                        filterable
                                        placeholder="不绑定代理"
                                    >
                                        <el-option label="不绑定" :value="0" />
                                        <el-option
                                            v-for="item in agentOptions"
                                            :key="item.id"
                                            :label="item.name"
                                            :value="item.id"
                                        />
                                    </el-select>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row :gutter="12">
                            <el-col :span="12">
                                <el-form-item label="班级类型">
                                    <el-radio-group v-model="form.classType">
                                        <el-radio :label="0">正常班</el-radio>
                                        <el-radio :label="1">体验班</el-radio>
                                    </el-radio-group>
                                </el-form-item>
                            </el-col>
                            <el-col :span="12">
                                <el-form-item label="状态" prop="status">
                                    <el-select v-model="form.status" class="w-full">
                                        <el-option
                                            v-for="item in STATUS_OPTIONS"
                                            :key="item.value"
                                            :label="item.label"
                                            :value="item.value"
                                        />
                                    </el-select>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row :gutter="12">
                            <el-col :span="8">
                                <el-form-item label="适龄下限">
                                    <el-input-number
                                        v-model="form.ageMin"
                                        class="w-full"
                                        :min="0"
                                        :max="18"
                                        :step="1"
                                        placeholder="不限"
                                    />
                                </el-form-item>
                            </el-col>
                            <el-col :span="8">
                                <el-form-item label="适龄上限">
                                    <el-input-number
                                        v-model="form.ageMax"
                                        class="w-full"
                                        :min="0"
                                        :max="18"
                                        :step="1"
                                        placeholder="不限"
                                    />
                                </el-form-item>
                            </el-col>
                            <el-col :span="8">
                                <el-form-item label="难度等级">
                                    <el-input
                                        v-model="form.level"
                                        maxlength="20"
                                        show-word-limit
                                        placeholder="如：零基础 / 进阶"
                                    />
                                </el-form-item>
                            </el-col>
                        </el-row>

                        <div class="form-section-title">社区、小区与场馆</div>
                        <el-row :gutter="12">
                            <el-col :span="12">
                                <el-form-item label="社区">
                                    <el-select
                                        v-model="form.communityId"
                                        class="w-full"
                                        clearable
                                        filterable
                                        placeholder="请选择社区"
                                        @change="onCommunityChange"
                                    >
                                        <el-option label="不绑定" :value="0" />
                                        <el-option
                                            v-for="item in communityOptions"
                                            :key="item.id"
                                            :label="item.name"
                                            :value="item.id"
                                        />
                                    </el-select>
                                </el-form-item>
                            </el-col>
                            <el-col :span="12">
                                <el-form-item label="小区">
                                    <el-select
                                        v-model="form.areaId"
                                        class="w-full"
                                        clearable
                                        filterable
                                        placeholder="请选择小区"
                                        @change="onAreaChange"
                                    >
                                        <el-option label="不绑定" :value="0" />
                                        <el-option
                                            v-for="item in areaOptionsForForm"
                                            :key="item.id"
                                            :label="item.name"
                                            :value="item.id"
                                        />
                                    </el-select>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-form-item label="场馆">
                            <el-input
                                v-model="form.venue"
                                maxlength="255"
                                placeholder="如：建发央著篮球场"
                            />
                        </el-form-item>
                        <el-row :gutter="12">
                            <el-col :span="12">
                                <el-form-item label="纬度">
                                    <el-input
                                        v-model="form.latitude"
                                        placeholder="如 27.834940，可留空"
                                    />
                                </el-form-item>
                            </el-col>
                            <el-col :span="12">
                                <el-form-item label="经度">
                                    <el-input
                                        v-model="form.longitude"
                                        placeholder="如 113.111184，可留空"
                                    />
                                </el-form-item>
                            </el-col>
                        </el-row>

                        <div class="form-section-title">周期与时段</div>
                        <el-row :gutter="12">
                            <el-col :span="8">
                                <el-form-item label="开课日期" prop="startDate">
                                    <el-date-picker
                                        v-model="form.startDate"
                                        class="w-full"
                                        type="date"
                                        value-format="YYYY-MM-DD"
                                        placeholder="请选择开课日期"
                                        :disabled="isEdit"
                                    />
                                </el-form-item>
                            </el-col>
                            <el-col :span="8">
                                <el-form-item label="总课节数" prop="totalLessons">
                                    <el-input-number
                                        v-model="form.totalLessons"
                                        class="w-full"
                                        :min="1"
                                        :max="9999"
                                        :disabled="isEdit"
                                    />
                                </el-form-item>
                            </el-col>
                            <el-col :span="8">
                                <el-form-item label="排课模式">
                                    <el-radio-group v-model="form.scheduleMode" :disabled="isEdit">
                                        <el-radio :label="0">统一时段</el-radio>
                                        <el-radio :label="1">多时段</el-radio>
                                    </el-radio-group>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-alert
                            v-if="isEdit"
                            class="mb-4"
                            type="info"
                            :closable="false"
                            show-icon
                            title="开课日期、上课周期、总课节数和排课模式创建后不可修改；本页可调整具体上课时段。"
                        />

                        <template v-if="form.scheduleMode === 0">
                            <el-form-item label="上课星期">
                                <el-checkbox-group v-model="form.weekDays" :disabled="isEdit">
                                    <el-checkbox
                                        v-for="item in WEEK_OPTIONS"
                                        :key="item.value"
                                        :label="item.value"
                                    >
                                        {{ item.label }}
                                    </el-checkbox>
                                </el-checkbox-group>
                            </el-form-item>
                            <el-form-item label="统一时段">
                                <el-time-picker
                                    v-model="form.singleTime"
                                    is-range
                                    format="HH:mm"
                                    value-format="HH:mm"
                                    range-separator="-"
                                    start-placeholder="开始时间"
                                    end-placeholder="结束时间"
                                    class="!w-[320px]"
                                />
                            </el-form-item>
                        </template>

                        <template v-else>
                            <el-form-item label="多时段规则">
                                <div class="w-full">
                                    <el-table :data="form.rules" size="small" border>
                                        <el-table-column label="上课日" width="160">
                                            <template #default="{ row }">
                                                <el-select
                                                    v-model="row.weekDay"
                                                    class="w-full"
                                                    placeholder="星期"
                                                    :disabled="isEdit"
                                                >
                                                    <el-option
                                                        v-for="item in availableWeekOptions(row)"
                                                        :key="item.value"
                                                        :label="item.label"
                                                        :value="item.value"
                                                    />
                                                </el-select>
                                            </template>
                                        </el-table-column>
                                        <el-table-column label="上课时段" min-width="260">
                                            <template #default="{ row }">
                                                <el-time-picker
                                                    v-model="row.timeRange"
                                                    is-range
                                                    format="HH:mm"
                                                    value-format="HH:mm"
                                                    range-separator="-"
                                                    start-placeholder="开始时间"
                                                    end-placeholder="结束时间"
                                                    class="!w-full"
                                                />
                                            </template>
                                        </el-table-column>
                                        <el-table-column
                                            v-if="!isEdit"
                                            label="操作"
                                            width="80"
                                            align="center"
                                        >
                                            <template #default="{ $index }">
                                                <el-button
                                                    type="danger"
                                                    link
                                                    @click="removeRule($index)"
                                                    >删除</el-button
                                                >
                                            </template>
                                        </el-table-column>
                                    </el-table>
                                    <el-button
                                        v-if="!isEdit"
                                        class="mt-2"
                                        type="primary"
                                        plain
                                        :disabled="form.rules.length >= 7"
                                        @click="addRule"
                                    >
                                        添加时段
                                    </el-button>
                                    <div v-if="!isEdit" class="text-tx-secondary text-xs mt-1">
                                        每个上课日只能配置一个时段，最多 7 条。
                                    </div>
                                </div>
                            </el-form-item>
                        </template>

                        <div class="form-section-title">课时与费用</div>
                        <el-row :gutter="12">
                            <el-col :span="8">
                                <el-form-item label="课时费" prop="feePerLesson">
                                    <el-input-number
                                        v-model="form.feePerLesson"
                                        class="w-full"
                                        :min="0"
                                        :max="999999"
                                        :precision="2"
                                        :step="10"
                                    />
                                </el-form-item>
                            </el-col>
                            <el-col :span="8">
                                <el-form-item label="总费用">
                                    <el-input-number
                                        v-model="form.totalFee"
                                        class="w-full"
                                        :min="0"
                                        :max="99999999"
                                        :precision="2"
                                        :step="100"
                                    />
                                </el-form-item>
                            </el-col>
                            <el-col :span="8">
                                <el-form-item label="人数上限" prop="maxStudents">
                                    <el-input-number
                                        v-model="form.maxStudents"
                                        class="w-full"
                                        :min="1"
                                        :max="9999"
                                    />
                                </el-form-item>
                            </el-col>
                        </el-row>
                    </el-tab-pane>
                    <el-tab-pane label="课程介绍" name="intro">
                        <div class="form-section-title">封面与简介</div>
                        <el-row :gutter="12">
                            <el-col :span="12">
                                <el-form-item label="课程封面">
                                    <div>
                                        <material-picker v-model="form.image" :limit="1" />
                                        <div class="text-tx-secondary text-xs mt-1">
                                            建议 16:9 横图；作为小程序详情头图展示。
                                        </div>
                                    </div>
                                </el-form-item>
                            </el-col>
                            <el-col :span="12">
                                <el-form-item label="课程简介">
                                    <el-input
                                        v-model="form.summary"
                                        maxlength="120"
                                        show-word-limit
                                        placeholder="一句话介绍课程亮点，展示在详情副标题"
                                    />
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-form-item label="图文介绍">
                            <div class="w-full">
                                <editor v-model="form.introHtml" :height="340" width="100%" />
                                <div class="text-tx-secondary text-xs mt-1">
                                    支持标题、列表、加粗、图片等富文本；小程序课程详情「课程介绍」直接展示。
                                </div>
                            </div>
                        </el-form-item>
                        <el-form-item label="介绍多图">
                            <div>
                                <material-picker v-model="form.introImages" :limit="6" />
                                <div class="text-tx-secondary text-xs">
                                    最多 6 张，作为课程介绍图集随详情接口返回。
                                </div>
                            </div>
                        </el-form-item>
                        <el-form-item label="上架状态">
                            <el-radio-group v-model="form.isShow">
                                <el-radio :label="1">上架（小程序可见）</el-radio>
                                <el-radio :label="0">下架（小程序列表与详情不展示）</el-radio>
                            </el-radio-group>
                        </el-form-item>
                    </el-tab-pane>
                </el-tabs>
            </el-form>
            <template #footer>
                <el-button @click="formVisible = false">取消</el-button>
                <el-button type="primary" :loading="saving" @click="submit">保存</el-button>
            </template>
        </el-dialog>

        <el-drawer v-model="detailVisible" title="班级详情" size="760px" destroy-on-close>
            <div v-loading="detailLoading">
                <template v-if="detail">
                    <el-descriptions :column="2" border>
                        <el-descriptions-item label="班级名称">{{
                            detail.name
                        }}</el-descriptions-item>
                        <el-descriptions-item label="状态">
                            <el-tag :type="statusTag(detail.status)">{{
                                statusText(detail.status)
                            }}</el-tag>
                        </el-descriptions-item>
                        <el-descriptions-item label="上架状态">
                            <el-tag :type="Number(detail.isShow) === 1 ? 'success' : 'info'">
                                {{ Number(detail.isShow) === 1 ? '上架' : '下架' }}
                            </el-tag>
                        </el-descriptions-item>
                        <el-descriptions-item label="运动类目">
                            {{ detail.categoryName || categoryName(detail.categoryId) }}
                        </el-descriptions-item>
                        <el-descriptions-item label="教练">
                            {{ detail.instructorName || instructorName(detail.instructorId) }}
                        </el-descriptions-item>
                        <el-descriptions-item label="社区">{{
                            detail.communityName || '-'
                        }}</el-descriptions-item>
                        <el-descriptions-item label="小区">{{
                            detail.areaName || '-'
                        }}</el-descriptions-item>
                        <el-descriptions-item label="代理">{{
                            detail.agentName || agentName(detail.agentId)
                        }}</el-descriptions-item>
                        <el-descriptions-item label="场馆">{{
                            detail.venue || '-'
                        }}</el-descriptions-item>
                        <el-descriptions-item label="坐标">{{
                            coordText(detail)
                        }}</el-descriptions-item>
                        <el-descriptions-item label="开课日期">{{
                            detail.startDate || '-'
                        }}</el-descriptions-item>
                        <el-descriptions-item label="总课节数"
                            >{{ detail.totalLessons || 0 }} 节</el-descriptions-item
                        >
                        <el-descriptions-item label="排课模式">
                            {{ detail.scheduleMode === 1 ? '多时段' : '统一时段' }}
                        </el-descriptions-item>
                        <el-descriptions-item label="课时费">{{
                            money(detail.feePerLesson)
                        }}</el-descriptions-item>
                        <el-descriptions-item label="总费用">{{
                            money(detail.totalFee)
                        }}</el-descriptions-item>
                        <el-descriptions-item label="报名 / 上限">
                            {{ detail.studentCount || 0 }} / {{ detail.maxStudents || 0 }}
                        </el-descriptions-item>
                        <el-descriptions-item label="班级类型">
                            {{ detail.classType === 1 ? '体验班' : '正常班' }}
                        </el-descriptions-item>
                        <el-descriptions-item label="适龄区间">
                            {{ ageRangeText(detail) }}
                        </el-descriptions-item>
                        <el-descriptions-item label="难度等级">
                            {{ detail.level || '-' }}
                        </el-descriptions-item>
                    </el-descriptions>

                    <div class="detail-section-title">课程介绍</div>
                    <el-descriptions :column="1" border>
                        <el-descriptions-item label="课程封面">
                            <el-image
                                v-if="detail.imagePath"
                                :src="detail.image"
                                fit="cover"
                                class="w-[180px] h-[110px] rounded"
                                :preview-src-list="[detail.image]"
                                preview-teleported
                            />
                            <span v-else>-</span>
                        </el-descriptions-item>
                        <el-descriptions-item label="课程简介">{{
                            detail.summary || '-'
                        }}</el-descriptions-item>
                    </el-descriptions>
                    <div
                        v-if="detail.introHtml"
                        class="intro-preview"
                        v-html="sanitizeIntroHtml(detail.introHtml)"
                    ></div>
                    <el-empty v-else description="未维护图文介绍" :image-size="60" />
                    <div
                        v-if="detail.introImageUrls && detail.introImageUrls.length"
                        class="intro-images"
                    >
                        <el-image
                            v-for="(url, index) in detail.introImageUrls"
                            :key="url"
                            :src="url"
                            fit="cover"
                            class="intro-image"
                            :preview-src-list="detail.introImageUrls"
                            :initial-index="index"
                            preview-teleported
                        />
                    </div>

                    <div class="detail-section-title">上课时段</div>
                    <el-table :data="detailScheduleRows" size="small" border>
                        <el-table-column label="上课日" prop="weekDayText" width="120" />
                        <el-table-column label="时段" prop="timeSlot" />
                    </el-table>
                    <el-empty
                        v-if="!detailScheduleRows.length"
                        description="未配置上课时段"
                        :image-size="60"
                    />

                    <div class="detail-section-title">
                        已报名学员
                        <span class="text-tx-secondary text-sm font-normal"
                            >（{{ students.length }} 人）</span
                        >
                    </div>
                    <el-table v-loading="studentsLoading" :data="students" size="small" border>
                        <el-table-column label="学员" min-width="130">
                            <template #default="{ row }">
                                {{
                                    row.studentName ||
                                    row.realName ||
                                    row.nickname ||
                                    `用户#${row.userId}`
                                }}
                            </template>
                        </el-table-column>
                        <el-table-column label="手机号" prop="mobile" min-width="120">
                            <template #default="{ row }">{{ row.mobile || '-' }}</template>
                        </el-table-column>
                        <el-table-column label="状态" width="90">
                            <template #default="{ row }">
                                <el-tag
                                    :type="
                                        row.status === 2
                                            ? 'info'
                                            : row.status === 1
                                            ? 'success'
                                            : 'primary'
                                    "
                                >
                                    {{ row.statusText || '-' }}
                                </el-tag>
                            </template>
                        </el-table-column>
                        <el-table-column label="课时（总/已用/剩余）" min-width="150">
                            <template #default="{ row }">
                                {{ row.totalLessons || 0 }} / {{ row.usedLessons || 0 }} /
                                {{ row.remainLessons || 0 }}
                            </template>
                        </el-table-column>
                        <el-table-column label="报名时间" prop="createTime" min-width="160">
                            <template #default="{ row }">{{ row.createTime || '-' }}</template>
                        </el-table-column>
                    </el-table>
                    <el-empty
                        v-if="!studentsLoading && !students.length"
                        description="暂无报名学员"
                        :image-size="60"
                    />
                </template>
                <el-empty v-else description="加载中" />
            </div>
        </el-drawer>
    </div>
</template>

<script lang="ts" setup name="courseClass">
import { ref } from 'vue'
import {
    agentList,
    areaAll,
    categoryAll,
    classAdd,
    classDel,
    classDetail,
    classEdit,
    classEnrolledStudents,
    classList,
    classRegenerateLessons,
    classStatus,
    communityAll,
    instructorAll
} from '@/api/courseClass'
import { exportCourseReport } from '@/api/report'
import { compactParams, currentMonthRange } from '@/views/report/helpers'
import { usePaging } from '@/hooks/usePaging'
import feedback from '@/utils/feedback'

const STATUS_OPTIONS = [
    { value: 0, label: '已结束' },
    { value: 1, label: '进行中' },
    { value: 2, label: '预报名' }
]
const WEEK_OPTIONS = [
    { value: 1, label: '周一' },
    { value: 2, label: '周二' },
    { value: 3, label: '周三' },
    { value: 4, label: '周四' },
    { value: 5, label: '周五' },
    { value: 6, label: '周六' },
    { value: 7, label: '周日' }
]
const WEEK_LABELS = ['', '周一', '周二', '周三', '周四', '周五', '周六', '周日']
const INTRO_ALLOWED_TAGS = new Set([
    'p', 'br', 'strong', 'b', 'em', 'i', 'u', 's', 'ul', 'ol', 'li', 'blockquote',
    'h1', 'h2', 'h3', 'h4', 'h5', 'h6', 'a', 'img', 'span', 'div', 'table', 'thead',
    'tbody', 'tr', 'th', 'td'
])
const INTRO_BLOCKED_TAGS = new Set([
    'script', 'style', 'iframe', 'object', 'embed', 'form', 'input', 'button',
    'textarea', 'select', 'meta', 'link', 'base'
])
const INTRO_ALLOWED_ATTRS = new Set(['href', 'src', 'alt', 'title', 'class', 'target', 'rel', 'colspan', 'rowspan'])

function sanitizeIntroHtml(html: string) {
    const documentNode = new DOMParser().parseFromString(String(html || ''), 'text/html')
    Array.from(documentNode.body.querySelectorAll('*')).forEach((node) => {
        const tag = node.tagName.toLowerCase()
        if (INTRO_BLOCKED_TAGS.has(tag)) {
            node.remove()
            return
        }
        if (!INTRO_ALLOWED_TAGS.has(tag)) {
            node.replaceWith(...Array.from(node.childNodes))
            return
        }
        Array.from(node.attributes).forEach((attr) => {
            const name = attr.name.toLowerCase()
            const value = attr.value.trim()
            if (!INTRO_ALLOWED_ATTRS.has(name) || name.startsWith('on')) {
                node.removeAttribute(attr.name)
                return
            }
            if ((name === 'href' || name === 'src') && !/^(https?:\/\/|\/|#)/i.test(value)) {
                node.removeAttribute(attr.name)
            }
        })
        if (tag === 'a' && node.getAttribute('target') === '_blank') {
            node.setAttribute('rel', 'noopener noreferrer')
        }
    })
    return documentNode.body.innerHTML
}

const query = reactive({
    name: '',
    categoryId: '',
    communityId: '',
    status: '',
    isShow: ''
})

const { pager, getLists, resetPage, resetParams } = usePaging({
    fetchFun: classList,
    params: query
})

// ---------- 导出课程报表（#105：旧报表菜单退场，导出搬到业务页）----------
// 复用旧报表导出接口（/report/course/export），字段口径与旧报表完全一致；
// 页面筛选只有班级名称/社区能映射到报表维度，报表状态 READY/HOLD 与本页班级状态不同源，故不传。
const exporting = ref(false)
const exportDateRange = ref<string[]>(currentMonthRange())

async function exportCourseExcel() {
    exporting.value = true
    feedback.loading('正在导出课程报表...')
    try {
        await exportCourseReport(
            compactParams({
                startDate: exportDateRange.value?.[0] || '',
                endDate: exportDateRange.value?.[1] || '',
                communityId: query.communityId,
                keyword: query.name
            })
        )
    } finally {
        feedback.closeLoading()
        exporting.value = false
    }
}

const categoryList = ref<any[]>([])
const communityOptions = ref<any[]>([])
const areaList = ref<any[]>([])
const instructorOptions = ref<any[]>([])
const agentOptions = ref<any[]>([])

const formVisible = ref(false)
const saving = ref(false)
const formRef = ref()
const formTab = ref('base')
const isEdit = computed(() => form.id > 0)

const form = reactive({
    id: 0,
    name: '',
    categoryId: null as number | null,
    instructorId: null as number | null,
    agentId: 0,
    communityId: 0,
    areaId: 0,
    venue: '',
    latitude: '',
    longitude: '',
    image: '',
    summary: '',
    introHtml: '',
    introImages: [] as string[],
    isShow: 1,
    classType: 0,
    // 适龄区间（岁，可空=未维护）与难度等级；字段由课程模板维护，小程序列表/详情展示
    ageMin: null as number | null,
    ageMax: null as number | null,
    level: '',
    startDate: '',
    totalLessons: 300,
    scheduleMode: 0,
    weekDays: [] as number[],
    singleTime: [] as string[],
    rules: [] as Array<{ weekDay: number | null; timeRange: string[] }>,
    feePerLesson: 0,
    totalFee: 0,
    maxStudents: 2,
    status: 1
})

const formRules = {
    name: [{ required: true, message: '请输入班级名称', trigger: 'blur' }],
    categoryId: [{ required: true, message: '请选择运动类目', trigger: 'change' }],
    instructorId: [{ required: true, message: '请选择教练', trigger: 'change' }],
    startDate: [{ required: true, message: '请选择开课日期', trigger: 'change' }],
    totalLessons: [{ required: true, message: '请输入总课节数', trigger: 'blur' }],
    feePerLesson: [{ required: true, message: '请输入课时费', trigger: 'blur' }],
    maxStudents: [{ required: true, message: '请输入人数上限', trigger: 'blur' }],
    status: [{ required: true, message: '请选择状态', trigger: 'change' }]
}

const categoryOptions = computed(() => {
    const map = new Map<number, any>()
    categoryList.value.forEach((item) => map.set(Number(item.id), item))
    return categoryList.value
        .map((item) => {
            const parent = item.parentId ? map.get(Number(item.parentId)) : null
            return {
                value: Number(item.id),
                label: `${parent ? `${parent.name} / ` : ''}${item.name}${
                    item.isShow === 0 ? '（隐藏）' : ''
                }`
            }
        })
        .sort((a, b) => a.value - b.value)
})

const areaOptionsForForm = computed(() => {
    const communityId = Number(form.communityId) || 0
    return areaList.value.filter((item) => !communityId || Number(item.communityId) === communityId)
})

const detailVisible = ref(false)
const detailLoading = ref(false)
const studentsLoading = ref(false)
const detail = ref<any>(null)
const students = ref<any[]>([])
const deletingId = ref(0)
const statusSavingId = ref(0)
const regeneratingId = ref(0)

const detailScheduleRows = computed(() => {
    if (!detail.value) return []
    if (Number(detail.value.scheduleMode) === 1) {
        const rules = parseRules(detail.value.scheduleRules)
        if (rules.length) {
            return rules.map((item) => ({
                weekDayText: WEEK_LABELS[item.weekDay] || `周${item.weekDay}`,
                timeSlot: item.timeSlot
            }))
        }
    }
    const days = parseWeekDays(detail.value.weekDays)
    const slot = detail.value.timeSlot || ''
    if (!days.length || !slot) return []
    return days.map((day) => ({ weekDayText: WEEK_LABELS[day] || `周${day}`, timeSlot: slot }))
})

function statusText(status: any) {
    return STATUS_OPTIONS.find((item) => item.value === Number(status))?.label || '未知'
}

function statusTag(status: any) {
    const value = Number(status)
    if (value === 1) return 'success'
    if (value === 2) return 'warning'
    return 'info'
}

function money(value: any) {
    if (value === null || value === undefined || value === '') return '¥0.00'
    return `¥${Number(value).toFixed(2)}`
}

// #62：课时费或总课时为 0 时退课退款会按全额/错误单价计算，列表直接标红提示回填
function lessonDataAbnormal(row: any) {
    return !(Number(row?.feePerLesson) > 0) || !(Number(row?.totalLessons) > 0)
}

function lessonDataAbnormalReason(row: any) {
    const feeMissing = !(Number(row?.feePerLesson) > 0)
    const lessonsMissing = !(Number(row?.totalLessons) > 0)
    if (feeMissing && lessonsMissing) {
        return '课时费与总课时均为 0，退课金额会按全额退款计算，请尽快补齐'
    }
    if (feeMissing) {
        return '单节课时费为 0，退课金额会按总费用/总课时估算，请尽快补齐'
    }
    return '总课时为 0，退课金额无法按课时计算，请尽快补齐'
}

// 适龄展示：两端都有 → 5-8岁；只有一端 → 5岁+ / ≤8岁；都没有 → 未维护
function ageRangeText(row: any) {
    const min = row?.ageMin
    const max = row?.ageMax
    const hasMin = min !== null && min !== undefined && min !== ''
    const hasMax = max !== null && max !== undefined && max !== ''
    if (hasMin && hasMax) return `${min}-${max}岁`
    if (hasMin) return `${min}岁+`
    if (hasMax) return `≤${max}岁`
    return '未维护'
}

function categoryName(id: any) {
    return categoryList.value.find((item) => Number(item.id) === Number(id))?.name || '-'
}

function instructorName(id: any) {
    return instructorOptions.value.find((item) => Number(item.id) === Number(id))?.name || '-'
}

function agentName(id: any) {
    if (!id) return '-'
    return agentOptions.value.find((item) => Number(item.id) === Number(id))?.name || `#${id}`
}

function regionText(row: any) {
    const community = row.communityName || ''
    const area = row.areaName || ''
    if (!community && !area) return '-'
    return [community, area].filter(Boolean).join(' / ')
}

function coordText(row: any) {
    if (!row) return '-'
    const lat = row.latitude ?? row.lat
    const lng = row.longitude ?? row.lng
    if (
        lat === null ||
        lat === undefined ||
        lat === '' ||
        lng === null ||
        lng === undefined ||
        lng === ''
    ) {
        return '-'
    }
    return `${lat}, ${lng}`
}

function parseWeekDays(value: any) {
    if (!value) return [] as number[]
    return String(value)
        .split(',')
        .map((item) => Number(item.trim()))
        .filter((item) => item >= 1 && item <= 7)
}

function parseRules(value: any) {
    if (!value) return [] as Array<{ weekDay: number; timeSlot: string }>
    try {
        const list = JSON.parse(value)
        if (!Array.isArray(list)) return []
        return list
            .map((item) => ({
                weekDay: Number(item.weekDay),
                timeSlot: String(item.timeSlot || '')
            }))
            .filter((item) => item.weekDay >= 1 && item.weekDay <= 7 && item.timeSlot.includes('-'))
            .sort((a, b) => a.weekDay - b.weekDay)
    } catch {
        return []
    }
}

function scheduleText(row: any) {
    if (Number(row.scheduleMode) === 1) {
        if (row.timeSlot) return row.timeSlot
        const rules = parseRules(row.scheduleRules)
        return rules.length
            ? rules
                  .map(
                      (item) =>
                          `${WEEK_LABELS[item.weekDay] || `周${item.weekDay}`}${item.timeSlot}`
                  )
                  .join('，')
            : '-'
    }
    const days = parseWeekDays(row.weekDays)
    if (!days.length) return row.timeSlot || '-'
    const dayText = days.map((day) => WEEK_LABELS[day] || `周${day}`).join('、')
    return `${dayText} ${row.timeSlot || ''}`.trim()
}

function rangeToSlot(range: any) {
    if (!Array.isArray(range) || range.length !== 2 || !range[0] || !range[1]) return ''
    return `${range[0]}-${range[1]}`
}

function minutesOf(text: string) {
    const parts = String(text).split(':')
    if (parts.length !== 2) return -1
    const hour = Number(parts[0])
    const minute = Number(parts[1])
    if (!Number.isFinite(hour) || !Number.isFinite(minute)) return -1
    return hour * 60 + minute
}

function validateTimeSlot(slot: string) {
    if (!slot || !slot.includes('-')) return '请选择完整的上课时段'
    const [start, end] = slot.split('-')
    const startMinutes = minutesOf(start)
    const endMinutes = minutesOf(end)
    if (startMinutes < 0 || endMinutes < 0) return '上课时段格式无效'
    if (startMinutes >= endMinutes) return '开始时间必须早于结束时间'
    return ''
}

function validateSchedule() {
    if (Number(form.scheduleMode) === 1) {
        if (!form.rules.length) return '请至少配置一个上课时段'
        const seen = new Set<number>()
        for (const rule of form.rules) {
            if (!rule.weekDay) return '请选择每条规则的上课日'
            if (seen.has(rule.weekDay)) return '每个上课日只能配置一个时段'
            seen.add(rule.weekDay)
            const error = validateTimeSlot(rangeToSlot(rule.timeRange))
            if (error) return `${WEEK_LABELS[rule.weekDay]}：${error}`
        }
        return ''
    }
    if (!form.weekDays.length) return '请选择上课星期'
    return validateTimeSlot(rangeToSlot(form.singleTime))
}

function validateCoords() {
    const latitude = String(form.latitude || '').trim()
    const longitude = String(form.longitude || '').trim()
    if (!latitude && !longitude) return ''
    if (!latitude || !longitude) return '纬度、经度需要同时填写或同时留空'
    const lat = Number(latitude)
    const lng = Number(longitude)
    if (!Number.isFinite(lat) || !Number.isFinite(lng)) return '坐标必须是数字'
    if (lat < -90 || lat > 90) return '纬度需在 -90 到 90 之间'
    if (lng < -180 || lng > 180) return '经度需在 -180 到 180 之间'
    return ''
}

function availableWeekOptions(row: any) {
    const used = new Set(
        form.rules
            .filter((item) => item !== row && item.weekDay)
            .map((item) => Number(item.weekDay))
    )
    return WEEK_OPTIONS.filter((item) => !used.has(item.value))
}

function addRule() {
    const used = new Set(form.rules.map((item) => Number(item.weekDay)).filter(Boolean))
    const next = WEEK_OPTIONS.find((item) => !used.has(item.value))
    if (!next) {
        feedback.msgWarning('7 个上课日已全部配置')
        return
    }
    form.rules.push({ weekDay: next.value, timeRange: ['09:00', '10:00'] })
}

function removeRule(index: number) {
    form.rules.splice(index, 1)
}

function onAreaChange(areaId: any) {
    if (!areaId) return
    const area = areaList.value.find((item) => Number(item.id) === Number(areaId))
    if (area && area.communityId) {
        form.communityId = Number(area.communityId)
    }
}

function onCommunityChange(communityId: any) {
    if (!form.areaId) return
    const area = areaList.value.find((item) => Number(item.id) === Number(form.areaId))
    if (communityId && area && Number(area.communityId) !== Number(communityId)) {
        form.areaId = 0
    }
}

async function loadOptions() {
    await Promise.all([
        (async () => {
            try {
                const data: any = await categoryAll({ all: 1 })
                categoryList.value = Array.isArray(data) ? data : []
            } catch {
                // 请求层已提示
            }
        })(),
        (async () => {
            try {
                const data: any = await communityAll()
                communityOptions.value = Array.isArray(data) ? data : []
            } catch {
                // 请求层已提示
            }
        })(),
        (async () => {
            try {
                const data: any = await areaAll()
                areaList.value = Array.isArray(data) ? data : []
            } catch {
                // 请求层已提示
            }
        })(),
        (async () => {
            try {
                const data: any = await instructorAll()
                instructorOptions.value = Array.isArray(data) ? data : []
            } catch {
                // 请求层已提示
            }
        })(),
        (async () => {
            try {
                const data: any = await agentList({ page_no: 1, page_size: 30 })
                agentOptions.value = Array.isArray(data?.lists) ? data.lists : []
            } catch {
                // 请求层已提示
            }
        })()
    ])
}

function resetForm() {
    form.id = 0
    form.name = ''
    form.categoryId = null
    form.instructorId = null
    form.agentId = 0
    form.communityId = 0
    form.areaId = 0
    form.venue = ''
    form.latitude = ''
    form.longitude = ''
    form.image = ''
    form.summary = ''
    form.introHtml = ''
    form.introImages = []
    form.isShow = 1
    form.classType = 0
    form.ageMin = null
    form.ageMax = null
    form.level = ''
    form.startDate = ''
    form.totalLessons = 300
    form.scheduleMode = 0
    form.weekDays = []
    form.singleTime = []
    form.rules = []
    form.feePerLesson = 0
    form.totalFee = 0
    form.maxStudents = 2
    form.status = 1
    formTab.value = 'base'
    formRef.value?.clearValidate()
}

function openAdd() {
    resetForm()
    const today = new Date()
    const month = String(today.getMonth() + 1).padStart(2, '0')
    const day = String(today.getDate()).padStart(2, '0')
    form.startDate = `${today.getFullYear()}-${month}-${day}`
    form.rules = [{ weekDay: 1, timeRange: ['09:00', '10:00'] }]
    formVisible.value = true
}

function fillForm(data: any) {
    form.id = Number(data.id) || 0
    form.name = data.name || ''
    form.categoryId = data.categoryId ? Number(data.categoryId) : null
    form.instructorId = data.instructorId ? Number(data.instructorId) : null
    form.agentId = data.agentId ? Number(data.agentId) : 0
    form.communityId = data.communityId ? Number(data.communityId) : 0
    form.areaId = data.areaId ? Number(data.areaId) : 0
    form.venue = data.venue || ''
    form.latitude =
        data.latitude === null || data.latitude === undefined ? '' : String(data.latitude)
    form.longitude =
        data.longitude === null || data.longitude === undefined ? '' : String(data.longitude)
    // 编辑回填用绝对地址（与详情页 image 同口径），保存时后端 AssetRef 会归一为相对路径
    form.image = data.image || data.imagePath || ''
    form.summary = data.summary || ''
    form.introHtml = data.introHtml || ''
    form.introImages = splitImages(
        Array.isArray(data.introImageUrls) && data.introImageUrls.length
            ? data.introImageUrls
            : data.introImages
    )
    form.isShow = data.isShow === null || data.isShow === undefined ? 1 : Number(data.isShow)
    form.classType = data.classType === 1 ? 1 : 0
    form.ageMin =
        data.ageMin === null || data.ageMin === undefined || data.ageMin === ''
            ? null
            : Number(data.ageMin)
    form.ageMax =
        data.ageMax === null || data.ageMax === undefined || data.ageMax === ''
            ? null
            : Number(data.ageMax)
    form.level = data.level || ''
    form.startDate = data.startDate || ''
    form.totalLessons = Number(data.totalLessons) || 300
    form.scheduleMode = Number(data.scheduleMode) === 1 ? 1 : 0
    form.weekDays = parseWeekDays(data.weekDays)
    form.singleTime = form.scheduleMode === 0 ? parseSingleTime(data.timeSlot) : []
    form.rules = form.scheduleMode === 1 ? toFormRules(data.scheduleRules) : []
    form.feePerLesson = Number(data.feePerLesson) || 0
    form.totalFee = Number(data.totalFee) || 0
    form.maxStudents = Number(data.maxStudents) || 2
    form.status = data.status === null || data.status === undefined ? 1 : Number(data.status)
}

function parseSingleTime(timeSlot: any) {
    if (!timeSlot) return []
    const parts = String(timeSlot).split('-')
    return parts.length === 2 ? parts : []
}

function splitImages(value: any) {
    if (!value) return [] as string[]
    if (Array.isArray(value)) return value.filter(Boolean).map((item) => String(item))
    return String(value)
        .split(',')
        .map((item) => item.trim())
        .filter(Boolean)
}

function toFormRules(scheduleRules: any) {
    return parseRules(scheduleRules).map((item) => ({
        weekDay: item.weekDay,
        timeRange: item.timeSlot.split('-')
    }))
}

async function openEdit(row: any) {
    resetForm()
    try {
        const data: any = await classDetail({ id: row.id })
        if (!data) {
            feedback.msgError('班级不存在')
            return
        }
        fillForm(data)
        formVisible.value = true
    } catch {
        // 请求层已提示
    }
}

function buildSchedulePayload() {
    if (Number(form.scheduleMode) === 1) {
        const rules = form.rules
            .map((item) => ({
                weekDay: Number(item.weekDay),
                timeSlot: rangeToSlot(item.timeRange)
            }))
            .filter((item) => item.weekDay && item.timeSlot)
            .sort((a, b) => a.weekDay - b.weekDay)
        return {
            scheduleMode: 1,
            scheduleRules: JSON.stringify(rules),
            weekDays: rules.map((item) => item.weekDay).join(','),
            timeSlot: rules.map((item) => `${WEEK_LABELS[item.weekDay]}${item.timeSlot}`).join(',')
        }
    }
    const timeSlot = rangeToSlot(form.singleTime)
    return {
        scheduleMode: 0,
        scheduleRules: '',
        weekDays: [...form.weekDays].sort((a, b) => a - b).join(','),
        timeSlot
    }
}

function toPayload() {
    return {
        id: form.id || undefined,
        name: form.name.trim(),
        categoryId: form.categoryId,
        instructorId: form.instructorId,
        agentId: form.agentId || 0,
        communityId: form.communityId || 0,
        areaId: form.areaId || 0,
        venue: form.venue ? form.venue.trim() : '',
        latitude: String(form.latitude || '').trim(),
        longitude: String(form.longitude || '').trim(),
        image: form.image || '',
        summary: String(form.summary || '').trim(),
        introHtml: form.introHtml || '',
        introImages: splitImages(form.introImages).join(','),
        isShow: Number(form.isShow) === 0 ? 0 : 1,
        classType: form.classType,
        ageMin: form.ageMin === null || form.ageMin === undefined ? null : Number(form.ageMin),
        ageMax: form.ageMax === null || form.ageMax === undefined ? null : Number(form.ageMax),
        level: String(form.level || '').trim(),
        startDate: form.startDate,
        totalLessons: form.totalLessons,
        feePerLesson: form.feePerLesson,
        totalFee: form.totalFee,
        maxStudents: form.maxStudents,
        status: form.status,
        ...buildSchedulePayload()
    }
}

// 适龄区间校验：0-18 岁，可留空（NULL=未维护）；两端都填时下限不能大于上限
function validateAgeRange() {
    const min = form.ageMin === null || form.ageMin === undefined ? null : Number(form.ageMin)
    const max = form.ageMax === null || form.ageMax === undefined ? null : Number(form.ageMax)
    if (min !== null && (!Number.isInteger(min) || min < 0 || min > 18)) {
        return '适龄下限需为 0~18 的整数'
    }
    if (max !== null && (!Number.isInteger(max) || max < 0 || max > 18)) {
        return '适龄上限需为 0~18 的整数'
    }
    if (min !== null && max !== null && min > max) {
        return '适龄下限不能大于上限'
    }
    if (String(form.level || '').trim().length > 20) {
        return '难度等级不能超过 20 个字符'
    }
    return ''
}

async function submit() {
    const valid = await formRef.value?.validate().catch(() => false)
    if (!valid || saving.value) return
    const scheduleError = validateSchedule()
    if (scheduleError) {
        feedback.msgError(scheduleError)
        return
    }
    const coordError = validateCoords()
    if (coordError) {
        feedback.msgError(coordError)
        return
    }
    const ageError = validateAgeRange()
    if (ageError) {
        feedback.msgError(ageError)
        return
    }
    saving.value = true
    try {
        const payload = toPayload()
        const editing = form.id > 0
        let result: any = null
        if (editing) {
            result = await classEdit(payload)
        } else {
            await classAdd(payload)
        }
        formVisible.value = false
        // 排课变化时后端会在同一事务内重建未来场次，直接展示统计
        if (editing && result?.regenerated) {
            feedback.msgSuccess(`保存成功；${regenerateStatsText(result)}`)
        } else {
            feedback.msgSuccess('保存成功')
        }
        if (editing) await getLists()
        else await resetPage()
    } catch {
        // 请求层已提示
    } finally {
        saving.value = false
    }
}

function isProtectedDeleteMessage(message: string) {
    return /报名|订单|预约/.test(message)
}

// 重建统计文案：删除 X / 新增 Y / 保留 Z（有预约）
function regenerateStatsText(stats: any) {
    const deleted = Number(stats?.deleted) || 0
    const created = Number(stats?.created) || 0
    const kept = Number(stats?.kept) || 0
    return `已重建场次：删除 ${deleted} 个，新增 ${created} 个，保留 ${kept} 个有预约场次`
}

// 按当前排课规则重建未来场次：只删除今天之后无有效预约的场次，有预约的保留不动
async function handleRegenerate(row: any) {
    try {
        await feedback.confirm(
            `确定按当前排课规则重建课程「${row.name}」的未来场次？\n\n将删除今天之后无有效预约的场次，并按当前排课规则重新生成；已有预约的场次会保留。`
        )
    } catch {
        return
    }
    regeneratingId.value = row.id
    try {
        const stats: any = await classRegenerateLessons({ id: row.id })
        feedback.msgSuccess(regenerateStatsText(stats))
        await getLists()
    } catch {
        // 请求层已提示
    } finally {
        regeneratingId.value = 0
    }
}

async function handleDelete(row: any) {
    try {
        await feedback.confirm(
            `确定删除课程「${row.name}」？删除为软删除，删除后列表不再展示；有报名或订单引用的课程会被拒绝删除。`
        )
    } catch {
        return
    }
    deletingId.value = row.id
    try {
        const res: any = await classDel({ id: row.id })
        if (res && Number(res.code) === 1) {
            feedback.msgSuccess('删除成功')
            await getLists()
            return
        }
        const message = res?.msg || '删除失败'
        if (isProtectedDeleteMessage(message)) {
            try {
                await feedback.confirm(
                    `${message}\n\n是否改为下架？下架后小程序不再展示，历史报名与订单保留。`
                )
                await classStatus({ id: row.id, isShow: 0 })
                feedback.msgSuccess('已下架')
                await getLists()
            } catch {
                // 用户取消下架
            }
            return
        }
        feedback.msgError(message)
    } catch (error: any) {
        feedback.msgError(error?.message || '删除失败')
    } finally {
        deletingId.value = 0
    }
}

async function toggleShow(row: any) {
    const target = Number(row.isShow) === 1 ? 0 : 1
    const action = target === 1 ? '上架' : '下架'
    try {
        await feedback.confirm(
            `确定${action}课程「${row.name}」？${
                target === 0
                    ? '下架后小程序列表与详情不再展示，历史报名与订单保留。'
                    : '上架后小程序恢复展示。'
            }`
        )
    } catch {
        return
    }
    statusSavingId.value = row.id
    try {
        await classStatus({ id: row.id, isShow: target })
        feedback.msgSuccess(`${action}成功`)
        await getLists()
        if (detailVisible.value && detail.value && Number(detail.value.id) === Number(row.id)) {
            detail.value.isShow = target
        }
    } catch {
        // 请求层已提示
    } finally {
        statusSavingId.value = 0
    }
}

async function openDetail(row: any) {
    detailVisible.value = true
    detailLoading.value = true
    studentsLoading.value = true
    detail.value = null
    students.value = []
    try {
        const [detailData, studentData] = await Promise.all([
            classDetail({ id: row.id }),
            classEnrolledStudents({ classId: row.id })
        ])
        detail.value = detailData
        students.value = Array.isArray(studentData) ? studentData : []
    } catch {
        // 请求层已提示
    } finally {
        detailLoading.value = false
        studentsLoading.value = false
    }
}

onMounted(() => {
    loadOptions()
    getLists()
})

// keep-alive 页面首次挂载已加载，激活时只刷新一次，避免重复请求
let activatedOnce = false
onActivated(() => {
    if (!activatedOnce) {
        activatedOnce = true
        return
    }
    loadOptions()
    getLists()
})
</script>

<style lang="scss" scoped>
.form-section-title {
    margin: 4px 0 16px;
    padding-left: 8px;
    border-left: 3px solid var(--el-color-primary);
    font-size: 14px;
    font-weight: 600;
    line-height: 16px;
}
.detail-section-title {
    margin: 20px 0 10px;
    font-size: 14px;
    font-weight: 600;
}
.intro-preview {
    margin-top: 12px;
    padding: 12px 16px;
    border: 1px solid var(--el-border-color-lighter);
    border-radius: 4px;
    line-height: 1.7;
    word-break: break-word;
    :deep(img) {
        max-width: 100%;
    }
}
.intro-images {
    display: flex;
    flex-wrap: wrap;
    gap: 8px;
    margin-top: 10px;
}
.intro-image {
    width: 120px;
    height: 90px;
    border-radius: 4px;
}
.class-form-tabs {
    :deep(.el-tabs__header) {
        margin-bottom: 12px;
    }
}
</style>
