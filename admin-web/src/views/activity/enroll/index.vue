<template>
    <div class="activity-page">
        <el-card class="!border-none" shadow="never">
            <div class="flex items-start justify-between gap-4">
                <div>
                    <div class="text-lg font-medium">活动与报名</div>
                    <div class="text-tx-secondary text-sm mt-1">
                        客服为课程场次生成报名链接（小程序路径），发给家长填写；链接可复制、停用/启用、设置有效期。
                        名单按链接/场次筛选，去重口径「同场次 + 手机号 + 学员名」，可导出 Excel。
                    </div>
                </div>
            </div>
        </el-card>

        <el-tabs v-model="activeTab" class="mt-4 activity-tabs">
            <el-tab-pane label="报名链接" name="links">
                <el-card class="!border-none" shadow="never">
                    <div class="text-base font-medium mb-3">生成报名链接</div>
                    <div class="flex flex-wrap items-end gap-4">
                        <el-form-item label="课程场次" class="!mb-0">
                            <el-select
                                v-model="sessionId"
                                class="w-[460px]"
                                filterable
                                clearable
                                placeholder="选择未来课程场次（可输入课程/小区/教练搜索）"
                            >
                                <el-option
                                    v-for="item in options"
                                    :key="item.id"
                                    :label="optionLabel(item)"
                                    :value="item.id"
                                />
                            </el-select>
                        </el-form-item>
                        <el-form-item label="有效期（天）" class="!mb-0">
                            <el-input-number v-model="expireDays" :min="1" :max="365" />
                        </el-form-item>
                        <el-button
                            v-perms="['activity/enroll/link']"
                            type="primary"
                            :loading="generating"
                            @click="generate"
                        >
                            生成报名链接
                        </el-button>
                    </div>

                    <div v-if="generated" class="mt-4 rounded bg-gray-50 p-4">
                        <div class="flex flex-wrap items-center gap-2">
                            <el-tag :type="generated.reused ? 'info' : 'success'" size="small">
                                {{ generated.reused ? '已复用有效链接' : '新链接' }}
                            </el-tag>
                            <span class="text-sm text-tx-secondary">
                                {{ generated.courseTitle }} · {{ generated.date }} {{ generated.time }}
                            </span>
                            <span class="text-xs text-gray-400">
                                有效期至 {{ generated.expireAt || '长期' }} · {{ generated.statusText }}
                            </span>
                        </div>
                        <div class="mt-2 flex items-center gap-2">
                            <code class="link-path">{{ generated.path }}</code>
                            <el-button type="primary" link @click="copyPath(generated.path)">
                                复制小程序路径
                            </el-button>
                        </div>
                        <div class="mt-1 text-xs text-gray-400">
                            把路径配置到小程序码/客服话术；家长打开后填写表单，无需登录。
                        </div>
                    </div>
                </el-card>

                <el-card class="!border-none mt-4" shadow="never">
                    <div class="flex items-center justify-between mb-3">
                        <div class="text-base font-medium">链接列表</div>
                        <div class="text-sm text-gray-400">
                            共 {{ linkPager.count }} 条 · 有效 {{ linkExtend.active || 0 }} ·
                            停用 {{ linkExtend.disabled || 0 }} · 过期 {{ linkExtend.expired || 0 }}
                        </div>
                    </div>
                    <el-form class="mb-[-16px]" :inline="true" :model="linkQuery">
                        <el-form-item label="场次">
                            <el-select
                                v-model="linkQuery.sessionId"
                                class="w-[320px]"
                                filterable
                                clearable
                                placeholder="全部场次"
                            >
                                <el-option
                                    v-for="item in options"
                                    :key="item.id"
                                    :label="optionLabel(item)"
                                    :value="item.id"
                                />
                            </el-select>
                        </el-form-item>
                        <el-form-item label="状态">
                            <el-select
                                v-model="linkQuery.status"
                                class="w-[140px]"
                                clearable
                                placeholder="全部"
                            >
                                <el-option label="有效" value="1" />
                                <el-option label="已停用" value="0" />
                                <el-option label="已过期" value="2" />
                            </el-select>
                        </el-form-item>
                        <el-form-item label="令牌">
                            <el-input
                                v-model="linkQuery.keyword"
                                class="w-[200px]"
                                clearable
                                placeholder="token 关键字"
                                @keyup.enter="resetLinkPage"
                            />
                        </el-form-item>
                        <el-form-item>
                            <el-button type="primary" @click="resetLinkPage">查询</el-button>
                            <el-button @click="resetLinkParams">重置</el-button>
                        </el-form-item>
                    </el-form>

                    <el-table
                        class="mt-4"
                        size="large"
                        table-layout="fixed"
                        v-loading="linkPager.loading"
                        :data="linkPager.lists"
                        empty-text="暂无报名链接"
                    >
                        <el-table-column label="ID" prop="id" width="60" />
                        <el-table-column label="课程 / 场次" min-width="220" show-overflow-tooltip>
                            <template #default="{ row }">
                                <div class="font-medium">{{ row.courseTitle || '-' }}</div>
                                <div class="text-xs text-gray-400">
                                    {{ row.date }} {{ row.time }} · {{ row.areaName || '-' }}
                                </div>
                            </template>
                        </el-table-column>
                        <el-table-column label="链接" min-width="230">
                            <template #default="{ row }">
                                <div class="token-line">
                                    <code class="token-text">{{ row.token }}</code>
                                    <el-button type="primary" link @click="copyPath(row.path)">
                                        复制
                                    </el-button>
                                </div>
                                <div class="text-xs text-gray-400">场次 #{{ row.sessionId }}</div>
                            </template>
                        </el-table-column>
                        <el-table-column label="有效期至" width="145">
                            <template #default="{ row }">
                                {{ row.expireAt || '长期有效' }}
                            </template>
                        </el-table-column>
                        <el-table-column label="状态" width="90">
                            <template #default="{ row }">
                                <el-tag :type="linkStatusTag(row)" size="small">
                                    {{ row.statusText }}
                                </el-tag>
                            </template>
                        </el-table-column>
                        <el-table-column label="提交数" width="90">
                            <template #default="{ row }">
                                <span>{{ row.submissionCount }}</span>
                                <span v-if="row.pendingCount" class="text-xs text-gray-400">
                                    （待确认 {{ row.pendingCount }}）
                                </span>
                            </template>
                        </el-table-column>
                        <el-table-column label="创建时间" prop="createdAt" width="150" />
                        <el-table-column label="操作" width="230" fixed="right">
                            <template #default="{ row }">
                                <el-button type="primary" link @click="copyPath(row.path)">
                                    复制
                                </el-button>
                                <el-button
                                    v-perms="['activity/enroll/link/status']"
                                    type="primary"
                                    link
                                    @click="toggleLink(row)"
                                >
                                    {{ row.status === 1 ? '停用' : '启用' }}
                                </el-button>
                                <el-button
                                    v-perms="['activity/enroll/link/expire']"
                                    type="primary"
                                    link
                                    @click="openExpire(row)"
                                >
                                    有效期
                                </el-button>
                            </template>
                        </el-table-column>
                    </el-table>
                    <div class="mt-4 flex justify-end">
                        <pagination v-model="linkPager" @change="getLinks" />
                    </div>
                </el-card>
            </el-tab-pane>

            <el-tab-pane label="报名名单" name="roster">
                <el-card class="!border-none" shadow="never">
                    <div class="flex flex-wrap items-center justify-between gap-3">
                        <div>
                            <div class="text-base font-medium">报名名单</div>
                            <div class="text-tx-secondary text-sm mt-1">
                                按链接或场次查看；同一场次 + 手机号 + 学员名只保留一条，重复提交会被拒绝。
                                本表由客服报名链接收集，不含收款；家长是否已付款见「活动报名名单」。
                            </div>
                        </div>
                        <div class="flex flex-wrap items-center justify-end gap-2">
                            <el-button
                                v-perms="['report/activity/export']"
                                plain
                                :loading="reportExporting"
                                @click="exportActivityReportExcel"
                            >
                                活动报名报表
                            </el-button>
                            <el-button
                                v-perms="['activity/enroll/export']"
                                type="primary"
                                :loading="exporting"
                                @click="exportRoster"
                            >
                                导出报名名单
                            </el-button>
                        </div>
                    </div>

                    <el-form class="mt-4 mb-[-16px]" :inline="true" :model="rosterQuery">
                        <el-form-item label="报名链接">
                            <el-select
                                v-model="rosterQuery.linkId"
                                class="w-[260px]"
                                filterable
                                clearable
                                placeholder="全部链接"
                            >
                                <el-option
                                    v-for="item in linkOptions"
                                    :key="item.id"
                                    :label="linkOptionLabel(item)"
                                    :value="item.id"
                                />
                            </el-select>
                        </el-form-item>
                        <el-form-item label="场次">
                            <el-select
                                v-model="rosterQuery.sessionId"
                                class="w-[260px]"
                                filterable
                                clearable
                                placeholder="全部场次"
                            >
                                <el-option
                                    v-for="item in options"
                                    :key="item.id"
                                    :label="optionLabel(item)"
                                    :value="item.id"
                                />
                            </el-select>
                        </el-form-item>
                        <el-form-item label="手机号">
                            <el-input
                                v-model="rosterQuery.phone"
                                class="w-[150px]"
                                clearable
                                placeholder="手机号"
                                @keyup.enter="resetRosterPage"
                            />
                        </el-form-item>
                        <el-form-item label="学员">
                            <el-input
                                v-model="rosterQuery.studentName"
                                class="w-[130px]"
                                clearable
                                placeholder="学员姓名"
                                @keyup.enter="resetRosterPage"
                            />
                        </el-form-item>
                        <el-form-item label="状态">
                            <el-select
                                v-model="rosterQuery.status"
                                class="w-[120px]"
                                clearable
                                placeholder="全部"
                            >
                                <el-option label="待确认" :value="0" />
                                <el-option label="已确认" :value="1" />
                                <el-option label="已拒绝" :value="2" />
                            </el-select>
                        </el-form-item>
                        <el-form-item label="提交时间">
                            <el-date-picker
                                v-model="rosterDateRange"
                                type="daterange"
                                value-format="YYYY-MM-DD"
                                start-placeholder="开始日期"
                                end-placeholder="结束日期"
                                class="!w-[240px]"
                            />
                        </el-form-item>
                        <el-form-item>
                            <el-button type="primary" @click="resetRosterPage">查询</el-button>
                            <el-button @click="resetRosterParams">重置</el-button>
                        </el-form-item>
                    </el-form>

                    <div class="dedupe-panel mt-4">
                        <div class="flex flex-wrap items-center gap-x-4 gap-y-1">
                            <span class="font-medium">去重结果</span>
                            <span>
                                共 <b>{{ rosterExtend.totalCount || 0 }}</b> 条，
                                去重后 <b>{{ rosterExtend.uniqueCount || 0 }}</b> 人，
                                重复 <b>{{ rosterExtend.duplicateCount || 0 }}</b> 条
                            </span>
                            <el-tag size="small" type="info">
                                规则：{{ rosterExtend.dedupeRule || '同场次 + 手机号 + 学员名' }}
                            </el-tag>
                            <span class="text-xs text-gray-400">
                                待确认 {{ rosterExtend.pendingCount || 0 }} ·
                                已确认 {{ rosterExtend.confirmedCount || 0 }} ·
                                已拒绝 {{ rosterExtend.rejectedCount || 0 }}
                            </span>
                        </div>
                    </div>

                    <el-table
                        class="mt-4"
                        size="large"
                        table-layout="fixed"
                        v-loading="rosterPager.loading"
                        :data="rosterPager.lists"
                        empty-text="暂无家长提交"
                    >
                        <el-table-column label="提交时间" prop="createdAt" width="145" />
                        <el-table-column label="课程 / 场次" min-width="220" show-overflow-tooltip>
                            <template #default="{ row }">
                                <div class="font-medium">{{ row.courseTitle || '-' }}</div>
                                <div class="text-xs text-gray-400">
                                    {{ row.date }} {{ row.time }} · {{ row.areaName || '-' }}
                                </div>
                            </template>
                        </el-table-column>
                        <el-table-column label="来源链接" min-width="180">
                            <template #default="{ row }">
                                <div class="token-line">
                                    <code class="token-text">{{ row.token || '未带链接' }}</code>
                                    <el-button
                                        v-if="row.linkPath"
                                        type="primary"
                                        link
                                        @click="copyPath(row.linkPath)"
                                    >
                                        复制
                                    </el-button>
                                </div>
                                <div class="text-xs text-gray-400">链接 #{{ row.linkId }}</div>
                            </template>
                        </el-table-column>
                        <el-table-column label="学员" width="120">
                            <template #default="{ row }">
                                <div>{{ row.studentName || '-' }}</div>
                                <div class="text-xs text-gray-400">
                                    {{ row.gender }} · {{ row.age }} 岁
                                </div>
                            </template>
                        </el-table-column>
                        <el-table-column label="家长" prop="parentName" width="100" show-overflow-tooltip />
                        <el-table-column label="手机号" prop="phone" width="125" />
                        <el-table-column label="备注" prop="note" min-width="130" show-overflow-tooltip>
                            <template #default="{ row }">
                                {{ row.note || '-' }}
                            </template>
                        </el-table-column>
                        <el-table-column label="状态" width="90">
                            <template #default="{ row }">
                                <el-tag :type="statusTag(row.status)" size="small">
                                    {{ row.statusText }}
                                </el-tag>
                            </template>
                        </el-table-column>
                    </el-table>
                    <div class="mt-4 flex justify-end">
                        <pagination v-model="rosterPager" @change="getRoster" />
                    </div>
                </el-card>
            </el-tab-pane>

            <el-tab-pane label="活动报名名单" name="signup">
                <el-card class="!border-none" shadow="never">
                    <div class="flex flex-wrap items-center justify-between gap-3">
                        <div>
                            <div class="text-base font-medium">活动报名名单（含支付状态）</div>
                            <div class="text-tx-secondary text-sm mt-1">
                                家长在小程序活动页报名，一人一行；支付状态取自关联订单：
                                已支付 / 待支付 / 已取消 / 免费（免费活动不生成订单）。
                                客服报名链接（「报名名单」）只收集信息、不收款，支付状态看这里。
                            </div>
                        </div>
                        <el-button
                            v-perms="['activity/enroll/export']"
                            type="primary"
                            :loading="signupExporting"
                            @click="exportSignupRoster"
                        >
                            导出活动报名名单
                        </el-button>
                    </div>

                    <el-form class="mt-4 mb-[-16px]" :inline="true" :model="signupQuery">
                        <el-form-item label="活动">
                            <el-select
                                v-model="signupQuery.activityId"
                                class="w-[260px]"
                                filterable
                                clearable
                                placeholder="全部活动"
                            >
                                <el-option
                                    v-for="item in signupActivityOptions"
                                    :key="item.id"
                                    :label="`#${item.id} ${item.title || ''}`"
                                    :value="item.id"
                                />
                            </el-select>
                        </el-form-item>
                        <el-form-item label="报名状态">
                            <el-select
                                v-model="signupQuery.status"
                                class="w-[120px]"
                                clearable
                                placeholder="全部"
                            >
                                <el-option label="待支付" :value="0" />
                                <el-option label="已支付" :value="1" />
                                <el-option label="已取消" :value="3" />
                            </el-select>
                        </el-form-item>
                        <el-form-item label="支付状态">
                            <el-select
                                v-model="signupQuery.payStatus"
                                class="w-[120px]"
                                clearable
                                placeholder="全部"
                            >
                                <el-option label="已支付" :value="1" />
                                <el-option label="待支付" :value="0" />
                                <el-option label="已取消" :value="4" />
                                <el-option label="免费" :value="-1" />
                            </el-select>
                        </el-form-item>
                        <el-form-item label="关键词">
                            <el-input
                                v-model="signupQuery.keyword"
                                class="w-[190px]"
                                clearable
                                placeholder="手机号/学员/订单号"
                                @keyup.enter="resetSignupPage"
                            />
                        </el-form-item>
                        <el-form-item label="报名时间">
                            <el-date-picker
                                v-model="signupDateRange"
                                type="daterange"
                                value-format="YYYY-MM-DD"
                                start-placeholder="开始日期"
                                end-placeholder="结束日期"
                                class="!w-[240px]"
                            />
                        </el-form-item>
                        <el-form-item>
                            <el-button type="primary" @click="resetSignupPage">查询</el-button>
                            <el-button @click="resetSignupParams">重置</el-button>
                        </el-form-item>
                    </el-form>

                    <div class="dedupe-panel mt-4">
                        <div class="flex flex-wrap items-center gap-x-4 gap-y-1">
                            <span class="font-medium">汇总</span>
                            <span>
                                共 <b>{{ signupExtend.totalCount || 0 }}</b> 条，
                                去重后 <b>{{ signupExtend.uniqueCount || 0 }}</b> 人
                            </span>
                            <el-tag size="small" type="success">
                                已支付 {{ signupExtend.paidCount || 0 }} 人
                            </el-tag>
                            <el-tag size="small" type="warning">
                                未支付 {{ signupExtend.unpaidCount || 0 }} 人
                            </el-tag>
                            <el-tag size="small" type="info" effect="plain">
                                免费 {{ signupExtend.freeCount || 0 }} 人
                            </el-tag>
                            <span class="text-xs text-gray-400">
                                已支付金额 {{ activityMoney(signupExtend.paidAmount) }} ·
                                规则：{{ signupExtend.dedupeRule || '同一活动 + 同一学员（一人一行）' }}
                            </span>
                        </div>
                    </div>

                    <el-table
                        class="mt-4"
                        size="large"
                        table-layout="fixed"
                        v-loading="signupPager.loading"
                        :data="signupPager.lists"
                        empty-text="暂无活动报名"
                    >
                        <el-table-column label="报名时间" prop="createdAt" width="145" />
                        <el-table-column label="活动 / 地点" min-width="220" show-overflow-tooltip>
                            <template #default="{ row }">
                                <div class="font-medium">{{ row.activityTitle || '-' }}</div>
                                <div class="text-xs text-gray-400">
                                    {{ row.activityDate }} {{ row.activityTime }} ·
                                    {{ row.activityPlace || row.areaName || '-' }}
                                </div>
                            </template>
                        </el-table-column>
                        <el-table-column label="家长 / 手机" min-width="150">
                            <template #default="{ row }">
                                <div>{{ row.parentName || '-' }}</div>
                                <div class="text-xs text-gray-400">{{ row.parentMobile || '-' }}</div>
                            </template>
                        </el-table-column>
                        <el-table-column label="学员" prop="studentName" width="120" />
                        <el-table-column label="报名状态" width="95">
                            <template #default="{ row }">
                                <el-tag :type="signupStatusTag(row.status)" size="small">
                                    {{ row.statusText }}
                                </el-tag>
                            </template>
                        </el-table-column>
                        <el-table-column label="支付状态" width="95">
                            <template #default="{ row }">
                                <el-tag
                                    :type="payStatusTag(row.payStatus)"
                                    :effect="Number(row.payStatus) === -1 ? 'plain' : 'light'"
                                    size="small"
                                >
                                    {{ row.payStatusText }}
                                </el-tag>
                            </template>
                        </el-table-column>
                        <el-table-column label="订单 / 金额" min-width="200">
                            <template #default="{ row }">
                                <template v-if="row.orderSn">
                                    <code class="token-text">{{ row.orderSn }}</code>
                                    <div class="text-xs text-gray-400">
                                        {{ activityMoney(row.orderAmount) }} ·
                                        {{ row.payWayText || '-' }}
                                        <span v-if="row.payTime"> · {{ row.payTime }}</span>
                                    </div>
                                </template>
                                <span v-else class="text-xs text-gray-400">
                                    {{ Number(row.payStatus) === -1 ? '免费活动，无订单' : '未生成订单' }}
                                </span>
                            </template>
                        </el-table-column>
                        <el-table-column label="操作" width="120" fixed="right">
                            <template #default="{ row }">
                                <el-button
                                    v-if="row.cancelable"
                                    v-perms="['activity/enroll/cancel']"
                                    type="danger"
                                    link
                                    :loading="cancelSignupSavingId === row.id"
                                    @click="cancelSignup(row)"
                                >
                                    取消报名
                                </el-button>
                                <el-tag v-else-if="row.refundPending" type="warning" size="small">
                                    退款审核中
                                </el-tag>
                                <span v-else class="text-xs text-gray-400">—</span>
                            </template>
                        </el-table-column>
                    </el-table>
                    <div class="mt-4 flex justify-end">
                        <pagination v-model="signupPager" @change="getSignups" />
                    </div>
                </el-card>
            </el-tab-pane>

            <el-tab-pane label="异常报名" name="exception">
                <el-card class="!border-none" shadow="never">
                    <div class="flex flex-wrap items-start justify-between gap-3">
                        <div>
                            <div class="text-base font-medium">已付款但满员（超员生效）</div>
                            <div class="text-tx-secondary text-sm mt-1">
                                活动名额被超时释放后支付回调成功时，默认策略为订单生效并允许超员，
                                同时生成这里的待处理告警。运营可标记已处理（保留名额）或发起退款。
                            </div>
                        </div>
                        <div class="text-sm text-gray-500">
                            共 {{ exceptionPager.count }} 条 · 待处理
                            <b class="text-orange-500">{{ exceptionExtend.pending || 0 }}</b> · 已处理
                            {{ exceptionExtend.handled || 0 }} · 已退款
                            {{ exceptionExtend.refunded || 0 }}
                        </div>
                    </div>
                </el-card>

                <el-card class="!border-none mt-4" shadow="never">
                    <el-form class="mb-[-16px]" :inline="true" :model="exceptionQuery">
                        <el-form-item label="状态">
                            <el-select
                                v-model="exceptionQuery.status"
                                class="w-[130px]"
                                clearable
                                placeholder="全部"
                            >
                                <el-option label="待处理" :value="0" />
                                <el-option label="已处理" :value="1" />
                                <el-option label="已退款" :value="2" />
                            </el-select>
                        </el-form-item>
                        <el-form-item label="订单/学员">
                            <el-input
                                v-model="exceptionQuery.keyword"
                                class="w-[210px]"
                                clearable
                                placeholder="订单号或学员ID"
                                @keyup.enter="resetExceptionPage"
                            />
                        </el-form-item>
                        <el-form-item>
                            <el-button type="primary" @click="resetExceptionPage">查询</el-button>
                            <el-button @click="resetExceptionParams">重置</el-button>
                        </el-form-item>
                    </el-form>

                    <el-table
                        class="mt-4"
                        size="large"
                        table-layout="fixed"
                        v-loading="exceptionPager.loading"
                        :data="exceptionPager.lists"
                        empty-text="暂无异常报名"
                    >
                        <el-table-column label="创建时间" prop="createdAt" width="145" />
                        <el-table-column label="活动 / 名额" min-width="210" show-overflow-tooltip>
                            <template #default="{ row }">
                                <div class="font-medium">{{ row.activityTitle || '-' }}</div>
                                <div class="text-xs text-gray-400">
                                    {{ row.activityDate }} {{ row.activityTime }} ·
                                    容量 {{ row.capacity }} · 已占 {{ row.joined }}
                                    <span v-if="row.joined > row.capacity" class="text-orange-500">
                                        （超员）
                                    </span>
                                </div>
                            </template>
                        </el-table-column>
                        <el-table-column label="订单 / 金额" min-width="180">
                            <template #default="{ row }">
                                <code class="token-text">{{ row.orderSn }}</code>
                                <div class="text-xs text-gray-400">
                                    ¥{{ row.amount }} · {{ row.payWayText || '-' }} ·
                                    订单{{ row.orderPayStatusText || '-' }}
                                    <span v-if="row.orderRefundStatus === 1" class="text-red-500">
                                        · 已退款
                                    </span>
                                </div>
                            </template>
                        </el-table-column>
                        <el-table-column label="家长 / 学员" min-width="170" show-overflow-tooltip>
                            <template #default="{ row }">
                                <div>{{ row.userName || '-' }} {{ row.userMobile }}</div>
                                <div class="text-xs text-gray-400">
                                    {{ row.studentNames || '学员 #' + row.studentIds }}
                                </div>
                            </template>
                        </el-table-column>
                        <el-table-column label="状态" width="95">
                            <template #default="{ row }">
                                <el-tag :type="exceptionStatusTag(row.status)" size="small">
                                    {{ row.statusText }}
                                </el-tag>
                            </template>
                        </el-table-column>
                        <el-table-column label="原因 / 备注" min-width="200" show-overflow-tooltip>
                            <template #default="{ row }">
                                <div class="text-xs text-gray-500">{{ row.reason }}</div>
                                <div v-if="row.remark" class="text-xs text-gray-400 mt-1">
                                    备注：{{ row.remark }}
                                </div>
                                <div v-if="row.handledAt" class="text-xs text-gray-400 mt-1">
                                    {{ row.handlerName }} · {{ row.handledAt }}
                                </div>
                            </template>
                        </el-table-column>
                        <el-table-column label="操作" width="190" fixed="right">
                            <template #default="{ row }">
                                <el-button type="primary" link @click="openException(row)">
                                    详情
                                </el-button>
                                <el-button
                                    v-if="row.status === 0"
                                    v-perms="['activity/enroll/handle']"
                                    type="primary"
                                    link
                                    @click="markExceptionHandled(row)"
                                >
                                    标记已处理
                                </el-button>
                                <el-button
                                    v-if="row.status !== 2 && row.orderRefundStatus !== 1"
                                    v-perms="['activity/enroll/refund']"
                                    type="danger"
                                    link
                                    @click="refundExceptionOrder(row)"
                                >
                                    发起退款
                                </el-button>
                            </template>
                        </el-table-column>
                    </el-table>
                    <div class="mt-4 flex justify-end">
                        <pagination v-model="exceptionPager" @change="getExceptions" />
                    </div>
                </el-card>
            </el-tab-pane>

            <el-tab-pane label="开班申请" name="apply">
                <el-card class="!border-none" shadow="never">
                    <div class="text-base font-medium">按街道 + 课程类型合并计数</div>
                    <div class="text-tx-secondary text-sm mt-1">
                        同一街道同一课程类型只有一行，joined 累加；达到 5 人自动转「客服跟进中」。
                        申请只收集意向，不收款、不扣券、不生成预约。
                    </div>
                </el-card>

                <el-card class="!border-none mt-4" shadow="never">
                    <div class="flex flex-wrap items-center justify-between gap-2 mb-3">
                        <div class="text-base font-medium">聚合列表</div>
                        <div class="flex flex-wrap items-center gap-2">
                            <span class="text-sm text-gray-400">共 {{ applyPager.count }} 组</span>
                            <el-date-picker
                                v-model="applyExportDateRange"
                                class="!w-[240px]"
                                type="daterange"
                                value-format="YYYY-MM-DD"
                                :clearable="false"
                                start-placeholder="导出开始日期"
                                end-placeholder="导出结束日期"
                            />
                            <el-button
                                v-perms="['report/class_apply/export']"
                                type="primary"
                                plain
                                :loading="applyExporting"
                                @click="exportClassApplyExcel"
                            >
                                导出开班申请报表
                            </el-button>
                        </div>
                    </div>
                    <el-form class="mb-[-16px]" :inline="true" :model="applyQuery">
                        <el-form-item label="课程类型">
                            <el-select
                                v-model="applyQuery.courseType"
                                class="w-[150px]"
                                clearable
                                placeholder="全部"
                            >
                                <el-option
                                    v-for="item in courseTypes"
                                    :key="item"
                                    :label="item"
                                    :value="item"
                                />
                            </el-select>
                        </el-form-item>
                        <el-form-item label="状态">
                            <el-select
                                v-model="applyQuery.status"
                                class="w-[160px]"
                                clearable
                                placeholder="全部"
                            >
                                <el-option label="招募中" value="pending" />
                                <el-option label="客服跟进中" value="following" />
                                <el-option label="已开班" value="closed" />
                            </el-select>
                        </el-form-item>
                        <el-form-item>
                            <el-button type="primary" @click="resetApplyPage">查询</el-button>
                            <el-button @click="resetApplyParams">重置</el-button>
                        </el-form-item>
                    </el-form>

                    <el-table
                        class="mt-4"
                        size="large"
                        table-layout="fixed"
                        v-loading="applyPager.loading"
                        :data="applyPager.lists"
                        empty-text="暂无开班申请"
                    >
                        <el-table-column label="街道 / 分区" min-width="170">
                            <template #default="{ row }">
                                <div class="font-medium">{{ row.streetName || '-' }}</div>
                                <div class="text-xs text-gray-400">{{ row.districtName || '-' }}</div>
                            </template>
                        </el-table-column>
                        <el-table-column label="课程类型" prop="courseType" width="110" />
                        <el-table-column label="合并人数" min-width="190">
                            <template #default="{ row }">
                                <div class="flex items-center gap-2">
                                    <el-progress
                                        class="flex-1"
                                        :percentage="progress(row)"
                                        :stroke-width="10"
                                        :show-text="false"
                                    />
                                    <span class="text-sm whitespace-nowrap">
                                        {{ row.joined }}/{{ row.target }}
                                    </span>
                                </div>
                                <div class="text-xs text-gray-400 mt-1">
                                    {{ row.need > 0 ? `还差 ${row.need} 人` : '已达开班人数' }}
                                </div>
                            </template>
                        </el-table-column>
                        <el-table-column label="状态" width="110">
                            <template #default="{ row }">
                                <el-tag :type="applyStatusTag(row.status)" size="small">
                                    {{ row.statusText }}
                                </el-tag>
                            </template>
                        </el-table-column>
                        <el-table-column label="申请条数" prop="applyCount" width="100" />
                        <el-table-column label="最近更新" prop="updatedAt" width="150" />
                    </el-table>
                    <div class="mt-4 flex justify-end">
                        <pagination v-model="applyPager" @change="getApplies" />
                    </div>
                </el-card>
            </el-tab-pane>

            <el-tab-pane label="活动管理" name="manage">
                <el-card class="!border-none activity-manage" shadow="never">
                    <div class="flex flex-wrap items-center justify-between gap-3">
                        <div>
                            <div class="text-base font-medium">活动管理</div>
                            <div class="text-tx-secondary text-sm mt-1">
                                维护小程序活动主档：新增、编辑、上下架、删除。已有报名的活动不能删除，请改为下架。
                            </div>
                        </div>
                        <el-button
                            v-perms="['activity/activity/add']"
                            type="primary"
                            @click="openActivityAdd"
                        >
                            新增活动
                        </el-button>
                    </div>

                    <el-form class="mt-4 mb-[-16px]" :inline="true" :model="activityQuery">
                        <el-form-item label="关键词">
                            <el-input
                                v-model="activityQuery.keyword"
                                class="w-[220px]"
                                clearable
                                placeholder="标题/地点"
                                @keyup.enter="resetActivityPage"
                            />
                        </el-form-item>
                        <el-form-item label="状态">
                            <el-select
                                v-model="activityQuery.status"
                                class="w-[130px]"
                                clearable
                                placeholder="全部"
                            >
                                <el-option label="上架" :value="1" />
                                <el-option label="下架" :value="0" />
                            </el-select>
                        </el-form-item>
                        <el-form-item>
                            <el-button type="primary" @click="resetActivityPage">查询</el-button>
                            <el-button @click="resetActivityParams">重置</el-button>
                        </el-form-item>
                    </el-form>

                    <el-table
                        class="mt-4"
                        size="large"
                        table-layout="fixed"
                        v-loading="activityPager.loading"
                        :data="activityPager.lists"
                        empty-text="暂无活动"
                    >
                        <el-table-column label="ID" prop="id" width="60" />
                        <el-table-column
                            label="标题"
                            prop="title"
                            min-width="180"
                            show-overflow-tooltip
                        />
                        <el-table-column label="日期" prop="activityDate" width="110" />
                        <el-table-column label="时段" prop="timeSlot" width="120" />
                        <el-table-column label="地点" min-width="170" show-overflow-tooltip>
                            <template #default="{ row }">
                                <span>{{ row.place || '-' }}</span>
                                <span v-if="areaName(row.areaId) !== '-'" class="text-xs text-gray-400">
                                    · {{ areaName(row.areaId) }}
                                </span>
                            </template>
                        </el-table-column>
                        <el-table-column label="费用" width="100">
                            <template #default="{ row }">{{ activityMoney(row.fee) }}</template>
                        </el-table-column>
                        <el-table-column label="名额" prop="capacity" width="80" />
                        <el-table-column label="已报" prop="joined" width="80" />
                        <el-table-column label="陪同上限" prop="companionLimit" width="95" />
                        <el-table-column label="状态" width="80">
                            <template #default="{ row }">
                                <el-tag :type="activityStatusTag(row.status)" size="small">
                                    {{ Number(row.status) === 1 ? '上架' : '下架' }}
                                </el-tag>
                            </template>
                        </el-table-column>
                        <el-table-column label="排序" prop="sort" width="70" />
                        <el-table-column label="操作" width="200" fixed="right">
                            <template #default="{ row }">
                                <el-button
                                    v-perms="['activity/activity/edit']"
                                    type="primary"
                                    link
                                    @click="openActivityEdit(row)"
                                >
                                    编辑
                                </el-button>
                                <el-button
                                    v-perms="['activity/activity/status']"
                                    type="warning"
                                    link
                                    :loading="activityStatusSavingId === row.id"
                                    @click="toggleActivity(row)"
                                >
                                    {{ Number(row.status) === 1 ? '下架' : '上架' }}
                                </el-button>
                                <el-button
                                    v-perms="['activity/activity/del']"
                                    type="danger"
                                    link
                                    :loading="activityDeletingId === row.id"
                                    @click="handleActivityDelete(row)"
                                >
                                    删除
                                </el-button>
                            </template>
                        </el-table-column>
                    </el-table>
                    <div class="mt-4 flex justify-end">
                        <pagination v-model="activityPager" @change="getActivities" />
                    </div>
                </el-card>
            </el-tab-pane>
        </el-tabs>

        <el-dialog
            v-model="activityDialogVisible"
            :title="activityForm.id > 0 ? '编辑活动' : '新增活动'"
            width="860px"
            top="5vh"
            :close-on-click-modal="false"
            @closed="resetActivityForm"
        >
            <el-form
                ref="activityFormRef"
                :model="activityForm"
                :rules="activityFormRules"
                label-width="110px"
            >
                <el-tabs v-model="activityFormTab" class="activity-form-tabs">
                    <el-tab-pane label="基础信息" name="base">
                        <div class="form-section-title">基础信息</div>
                        <el-row :gutter="12">
                            <el-col :span="12">
                                <el-form-item label="活动标题" prop="title">
                                    <el-input
                                        v-model="activityForm.title"
                                        maxlength="100"
                                        show-word-limit
                                        placeholder="请输入活动标题"
                                    />
                                </el-form-item>
                            </el-col>
                            <el-col :span="12">
                                <el-form-item label="活动日期" prop="activityDate">
                                    <el-date-picker
                                        v-model="activityForm.activityDate"
                                        class="w-full"
                                        type="date"
                                        value-format="YYYY-MM-DD"
                                        placeholder="请选择活动日期"
                                    />
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row :gutter="12">
                            <el-col :span="12">
                                <el-form-item label="活动时段" prop="timeSlot">
                                    <el-input
                                        v-model="activityForm.timeSlot"
                                        maxlength="50"
                                        placeholder="如 09:30-11:30"
                                    />
                                </el-form-item>
                            </el-col>
                            <el-col :span="12">
                                <el-form-item label="活动地点" prop="place">
                                    <el-input
                                        v-model="activityForm.place"
                                        maxlength="255"
                                        placeholder="请输入活动地点"
                                    />
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row :gutter="12">
                            <el-col :span="12">
                                <el-form-item label="小区">
                                    <el-select
                                        v-model="activityForm.areaId"
                                        class="w-full"
                                        clearable
                                        filterable
                                        placeholder="不关联小区"
                                    >
                                        <el-option label="不关联" :value="0" />
                                        <el-option
                                            v-for="item in areaOptions"
                                            :key="item.id"
                                            :label="item.name"
                                            :value="item.id"
                                        />
                                    </el-select>
                                </el-form-item>
                            </el-col>
                            <el-col :span="12">
                                <el-form-item label="报名截止" prop="deadline">
                                    <el-date-picker
                                        v-model="activityForm.deadline"
                                        class="w-full"
                                        type="date"
                                        value-format="YYYY-MM-DD"
                                        placeholder="请选择报名截止日期"
                                    />
                                </el-form-item>
                            </el-col>
                        </el-row>

                        <div class="form-section-title">名额与费用</div>
                        <el-row :gutter="12">
                            <el-col :span="8">
                                <el-form-item label="每人费用" prop="fee">
                                    <el-input-number
                                        v-model="activityForm.fee"
                                        class="w-full"
                                        :min="0"
                                        :max="999999"
                                        :precision="2"
                                        :step="10"
                                    />
                                </el-form-item>
                            </el-col>
                            <el-col :span="8">
                                <el-form-item label="名额上限" prop="capacity">
                                    <el-input-number
                                        v-model="activityForm.capacity"
                                        class="w-full"
                                        :min="1"
                                        :max="9999"
                                    />
                                </el-form-item>
                            </el-col>
                            <el-col :span="8">
                                <el-form-item label="陪同上限" prop="companionLimit">
                                    <el-input-number
                                        v-model="activityForm.companionLimit"
                                        class="w-full"
                                        :min="0"
                                        :max="20"
                                    />
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row :gutter="12">
                            <el-col :span="8">
                                <el-form-item label="排序">
                                    <el-input-number
                                        v-model="activityForm.sort"
                                        class="w-full"
                                        :min="0"
                                        :max="9999"
                                    />
                                </el-form-item>
                            </el-col>
                            <el-col :span="12">
                                <el-form-item label="上架状态">
                                    <el-switch
                                        v-model="activityForm.status"
                                        :active-value="1"
                                        :inactive-value="0"
                                        active-text="上架"
                                        inactive-text="下架"
                                    />
                                </el-form-item>
                            </el-col>
                        </el-row>
                    </el-tab-pane>

                    <el-tab-pane label="活动介绍" name="intro">
                        <div class="form-section-title">封面与介绍</div>
                        <el-form-item label="活动封面">
                            <div>
                                <material-picker v-model="activityForm.cover" :limit="1" />
                                <div class="text-tx-secondary text-xs mt-1">
                                    建议 16:9 横图；作为小程序活动详情头图展示。
                                </div>
                            </div>
                        </el-form-item>
                        <el-form-item label="活动介绍">
                            <el-input
                                v-model="activityForm.description"
                                type="textarea"
                                :rows="4"
                                maxlength="2000"
                                show-word-limit
                                placeholder="两人三足、亲子接力等玩法与亮点"
                            />
                        </el-form-item>
                        <el-form-item label="标签">
                            <el-input
                                v-model="activityForm.tags"
                                maxlength="255"
                                placeholder="逗号分隔，如：亲子,户外"
                            />
                        </el-form-item>
                        <el-form-item label="报名须知">
                            <el-input
                                v-model="activityForm.notice"
                                type="textarea"
                                :rows="3"
                                maxlength="255"
                                show-word-limit
                                placeholder="计费口径、签到时间等报名须知"
                            />
                        </el-form-item>
                    </el-tab-pane>
                </el-tabs>
            </el-form>
            <template #footer>
                <el-button @click="activityDialogVisible = false">取消</el-button>
                <el-button type="primary" :loading="activitySaving" @click="submitActivity">
                    保存
                </el-button>
            </template>
        </el-dialog>

        <el-dialog v-model="exceptionDialog.visible" title="异常报名详情" width="720px">
            <div v-loading="exceptionDialog.loading">
                <el-descriptions v-if="exceptionDialog.detail" :column="2" border>
                    <el-descriptions-item label="活动">
                        {{ exceptionDialog.detail.activityTitle || '-' }}
                    </el-descriptions-item>
                    <el-descriptions-item label="场次">
                        {{ exceptionDialog.detail.activityDate }}
                        {{ exceptionDialog.detail.activityTime }}
                    </el-descriptions-item>
                    <el-descriptions-item label="地点">
                        {{ exceptionDialog.detail.activityPlace || '-' }}
                    </el-descriptions-item>
                    <el-descriptions-item label="名额">
                        容量 {{ exceptionDialog.detail.capacity }} · 已占
                        {{ exceptionDialog.detail.joined }}
                    </el-descriptions-item>
                    <el-descriptions-item label="订单号">
                        {{ exceptionDialog.detail.orderSn }}
                    </el-descriptions-item>
                    <el-descriptions-item label="金额 / 支付">
                        ¥{{ exceptionDialog.detail.amount }} ·
                        {{ exceptionDialog.detail.payWayText || '-' }}
                    </el-descriptions-item>
                    <el-descriptions-item label="订单状态">
                        {{ exceptionDialog.detail.orderPayStatusText || '-' }}
                        <span v-if="exceptionDialog.detail.orderRefundStatus === 1">
                            · 已退款
                        </span>
                    </el-descriptions-item>
                    <el-descriptions-item label="家长">
                        {{ exceptionDialog.detail.userName }}
                        {{ exceptionDialog.detail.userMobile }}
                    </el-descriptions-item>
                    <el-descriptions-item label="异常原因" :span="2">
                        {{ exceptionDialog.detail.reason || '-' }}
                    </el-descriptions-item>
                    <el-descriptions-item label="处理状态">
                        {{ exceptionDialog.detail.statusText }}
                    </el-descriptions-item>
                    <el-descriptions-item label="处理人 / 时间">
                        {{ exceptionDialog.detail.handlerName || '-' }}
                        {{ exceptionDialog.detail.handledAt || '' }}
                    </el-descriptions-item>
                    <el-descriptions-item label="备注" :span="2">
                        {{ exceptionDialog.detail.remark || '-' }}
                    </el-descriptions-item>
                </el-descriptions>
                <div class="text-base font-medium mt-4 mb-2">报名明细</div>
                <el-table :data="exceptionDialog.detail?.signups || []" size="small" border>
                    <el-table-column label="学员ID" prop="studentId" width="100" />
                    <el-table-column label="学员姓名" prop="studentName" min-width="140" />
                    <el-table-column label="报名状态" prop="statusText" width="100" />
                </el-table>
            </div>
            <template #footer>
                <el-button @click="exceptionDialog.visible = false">关闭</el-button>
                <el-button
                    v-if="exceptionDialog.detail && exceptionDialog.detail.status === 0"
                    v-perms="['activity/enroll/handle']"
                    type="primary"
                    @click="markExceptionHandled(exceptionDialog.detail)"
                >
                    标记已处理
                </el-button>
            </template>
        </el-dialog>

        <el-dialog v-model="expireDialog.visible" title="设置链接有效期" width="420px">
            <el-form label-width="100px">
                <el-form-item label="链接">
                    <div class="text-sm break-all">{{ expireDialog.token }}</div>
                </el-form-item>
                <el-form-item label="有效期">
                    <el-input-number v-model="expireDialog.days" :min="0" :max="365" />
                    <span class="ml-2 text-xs text-gray-400">0 = 长期有效，1-365 天从当前时间起算</span>
                </el-form-item>
            </el-form>
            <template #footer>
                <el-button @click="expireDialog.visible = false">取消</el-button>
                <el-button type="primary" :loading="expireDialog.saving" @click="saveExpire">
                    保存
                </el-button>
            </template>
        </el-dialog>
    </div>
</template>

<script lang="ts" setup name="activityEnroll">
import { computed, onMounted, reactive, ref, watch } from 'vue'
import useClipboard from 'vue-clipboard3'
import {
    activityAdd,
    activityDel,
    activityDetail,
    activityEdit,
    activityList,
    activityStatus,
    cancelActivitySignup,
    createEnrollLink,
    exportActivitySignups,
    exportEnrollments,
    getActivitySignupList,
    getApplyGroups,
    getEnrollExceptionDetail,
    getEnrollExceptions,
    getEnrollLinks,
    getEnrollOptions,
    getEnrollmentList,
    handleEnrollException,
    refundEnrollException,
    updateEnrollLinkExpire,
    updateEnrollLinkStatus
} from '@/api/activity'
import { areaAll } from '@/api/courseClass'
import { exportActivityReport, exportClassApplyReport } from '@/api/report'
import { compactParams, currentMonthRange } from '@/views/report/helpers'
import { usePaging } from '@/hooks/usePaging'
import feedback from '@/utils/feedback'

const { toClipboard } = useClipboard()

const COURSE_TYPES = ['篮球', '足球', '体能训练', '跳绳', '羽毛球', '其他']
const courseTypes = COURSE_TYPES
const activeTab = ref('links')

const options = ref<any[]>([])
const linkOptions = ref<any[]>([])
const sessionId = ref<any>('')
const expireDays = ref(30)
const generating = ref(false)
const generated = ref<any>(null)
const exporting = ref(false)

const linkQuery = reactive<any>({
    sessionId: '',
    status: '',
    keyword: ''
})

const {
    pager: linkPager,
    getLists: getLinks,
    resetPage: resetLinkPage,
    resetParams: resetLinkParams
} = usePaging({
    fetchFun: getEnrollLinks,
    params: linkQuery
})

const linkExtend = computed<any>(() => linkPager.extend || {})

const rosterQuery = reactive<any>({
    linkId: '',
    sessionId: '',
    phone: '',
    studentName: '',
    status: '',
    dateStart: '',
    dateEnd: ''
})
const rosterDateRange = ref<string[]>([])

const {
    pager: rosterPager,
    getLists: getRoster,
    resetPage: resetRosterPage,
    resetParams: resetRosterParamsBase
} = usePaging({
    fetchFun: getEnrollmentList,
    params: rosterQuery
})

const rosterExtend = computed<any>(() => rosterPager.extend || {})

// ---------- 活动报名名单（含支付状态） ----------
// payStatus：-1 免费 / 0 待支付 / 1 已支付 / 4 已取消；报名状态沿用 signup.status（0/1/3）。
const signupQuery = reactive<any>({
    activityId: '',
    status: '',
    payStatus: '',
    keyword: '',
    dateStart: '',
    dateEnd: ''
})
const signupDateRange = ref<string[]>([])
const signupActivityOptions = ref<any[]>([])
const signupExporting = ref(false)
const cancelSignupSavingId = ref(0)

const {
    pager: signupPager,
    getLists: getSignups,
    resetPage: resetSignupPage,
    resetParams: resetSignupParamsBase
} = usePaging({
    fetchFun: getActivitySignupList,
    params: signupQuery
})

const signupExtend = computed<any>(() => signupPager.extend || {})

const applyQuery = reactive<any>({
    courseType: '',
    status: ''
})

const {
    pager: applyPager,
    getLists: getApplies,
    resetPage: resetApplyPage,
    resetParams: resetApplyParams
} = usePaging({
    fetchFun: getApplyGroups,
    params: applyQuery
})

const exceptionQuery = reactive<any>({
    status: 0,
    keyword: ''
})

const {
    pager: exceptionPager,
    getLists: getExceptions,
    resetPage: resetExceptionPage,
    resetParams: resetExceptionParams
} = usePaging({
    fetchFun: getEnrollExceptions,
    params: exceptionQuery
})

const exceptionExtend = computed<any>(() => exceptionPager.extend || {})

const exceptionDialog = reactive<any>({
    visible: false,
    loading: false,
    detail: null
})

let exceptionLoaded = false

const expireDialog = reactive<any>({
    visible: false,
    saving: false,
    id: 0,
    token: '',
    days: 30
})

let rosterLoaded = false
let signupLoaded = false
let applyLoaded = false
let activityLoaded = false

// ---------- 活动管理（第 5 个 tab） ----------
const activityQuery = reactive<any>({
    keyword: '',
    status: ''
})

const {
    pager: activityPager,
    getLists: getActivities,
    resetPage: resetActivityPage,
    resetParams: resetActivityParams
} = usePaging({
    fetchFun: activityList,
    params: activityQuery
})

const areaOptions = ref<any[]>([])
let activityAreaLoaded = false

const activityDialogVisible = ref(false)
const activitySaving = ref(false)
const activityFormRef = ref()
const activityFormTab = ref('base')
const activityStatusSavingId = ref(0)
const activityDeletingId = ref(0)

const activityForm = reactive<any>({
    id: 0,
    title: '',
    cover: '',
    activityDate: '',
    timeSlot: '',
    place: '',
    areaId: 0,
    fee: 0,
    capacity: 20,
    companionLimit: 0,
    deadline: '',
    description: '',
    tags: '',
    notice: '',
    status: 1,
    sort: 0
})

const activityFormRules = {
    title: [{ required: true, message: '请输入活动标题', trigger: 'blur' }],
    activityDate: [{ required: true, message: '请选择活动日期', trigger: 'change' }],
    timeSlot: [{ required: true, message: '请输入活动时段', trigger: 'blur' }],
    place: [{ required: true, message: '请输入活动地点', trigger: 'blur' }],
    fee: [{ required: true, message: '请输入每人费用', trigger: 'blur' }],
    capacity: [{ required: true, message: '请输入名额上限', trigger: 'blur' }],
    companionLimit: [{ required: true, message: '请输入陪同上限', trigger: 'blur' }],
    deadline: [{ required: true, message: '请选择报名截止日期', trigger: 'change' }]
}

function todayText() {
    const today = new Date()
    const month = String(today.getMonth() + 1).padStart(2, '0')
    const day = String(today.getDate()).padStart(2, '0')
    return `${today.getFullYear()}-${month}-${day}`
}

async function loadActivityAreas() {
    if (activityAreaLoaded) return
    activityAreaLoaded = true
    try {
        const data: any = await areaAll()
        areaOptions.value = Array.isArray(data) ? data : []
    } catch {
        areaOptions.value = []
        activityAreaLoaded = false
    }
}

function areaName(id: any) {
    return areaOptions.value.find((item) => Number(item.id) === Number(id))?.name || '-'
}

function activityMoney(value: any) {
    if (value === null || value === undefined || value === '') return '¥0.00'
    return `¥${Number(value).toFixed(2)}`
}

function activityStatusTag(status: any) {
    return Number(status) === 1 ? 'success' : 'info'
}

function resetActivityForm() {
    Object.assign(activityForm, {
        id: 0,
        title: '',
        cover: '',
        activityDate: '',
        timeSlot: '',
        place: '',
        areaId: 0,
        fee: 0,
        capacity: 20,
        companionLimit: 0,
        deadline: '',
        description: '',
        tags: '',
        notice: '',
        status: 1,
        sort: 0
    })
    activityFormTab.value = 'base'
    activityFormRef.value?.clearValidate()
}

async function openActivityAdd() {
    resetActivityForm()
    await loadActivityAreas()
    activityForm.activityDate = todayText()
    activityForm.deadline = todayText()
    activityDialogVisible.value = true
}

async function openActivityEdit(row: any) {
    resetActivityForm()
    await loadActivityAreas()
    try {
        const data: any = await activityDetail({ id: row.id })
        if (!data) {
            feedback.msgError('活动不存在')
            return
        }
        fillActivityForm(data)
        activityDialogVisible.value = true
    } catch {
        // 请求层已提示
    }
}

function fillActivityForm(data: any) {
    activityForm.id = Number(data.id) || 0
    activityForm.title = data.title || ''
    activityForm.cover = data.cover || data.coverPath || ''
    activityForm.activityDate = data.activityDate || ''
    activityForm.timeSlot = data.timeSlot || ''
    activityForm.place = data.place || ''
    activityForm.areaId = data.areaId ? Number(data.areaId) : 0
    activityForm.fee = Number(data.fee) || 0
    activityForm.capacity = Number(data.capacity) || 0
    activityForm.companionLimit =
        data.companionLimit === null || data.companionLimit === undefined
            ? 0
            : Number(data.companionLimit)
    activityForm.deadline = data.deadline || ''
    activityForm.description = data.description || ''
    activityForm.tags = data.tags || ''
    activityForm.notice = data.notice || ''
    activityForm.status = Number(data.status) === 0 ? 0 : 1
    activityForm.sort = Number(data.sort) || 0
}

function activityPayload() {
    return {
        id: activityForm.id || undefined,
        title: String(activityForm.title || '').trim(),
        cover: activityForm.cover || '',
        activityDate: activityForm.activityDate,
        timeSlot: String(activityForm.timeSlot || '').trim(),
        place: String(activityForm.place || '').trim(),
        areaId: Number(activityForm.areaId) || 0,
        fee: Number(activityForm.fee) || 0,
        capacity: Number(activityForm.capacity) || 0,
        companionLimit: Number(activityForm.companionLimit) || 0,
        deadline: activityForm.deadline,
        description: activityForm.description || '',
        tags: String(activityForm.tags || '').trim(),
        notice: String(activityForm.notice || '').trim(),
        status: Number(activityForm.status) === 0 ? 0 : 1,
        sort: Number(activityForm.sort) || 0
    }
}

async function submitActivity() {
    const valid = await activityFormRef.value?.validate().catch(() => false)
    if (!valid || activitySaving.value) return
    activitySaving.value = true
    try {
        const payload = activityPayload()
        const editing = activityForm.id > 0
        if (editing) {
            await activityEdit(payload)
        } else {
            await activityAdd(payload)
        }
        activityDialogVisible.value = false
        feedback.msgSuccess('保存成功')
        if (editing) await getActivities()
        else await resetActivityPage()
    } catch {
        // 请求层已提示
    } finally {
        activitySaving.value = false
    }
}

async function toggleActivity(row: any) {
    const target = Number(row.status) === 1 ? 0 : 1
    const action = target === 1 ? '上架' : '下架'
    try {
        await feedback.confirm(
            `确定${action}活动「${row.title}」？${
                target === 0
                    ? '下架后小程序不再展示，历史报名保留。'
                    : '上架后小程序恢复展示。'
            }`
        )
    } catch {
        return
    }
    activityStatusSavingId.value = row.id
    try {
        await activityStatus({ id: row.id, status: target })
        feedback.msgSuccess(`${action}成功`)
        await getActivities()
    } catch {
        // 请求层已提示
    } finally {
        activityStatusSavingId.value = 0
    }
}

async function handleActivityDelete(row: any) {
    try {
        await feedback.confirm(
            `确定删除活动「${row.title}」？删除后小程序不再展示；已有报名的活动会被拒绝删除，请先下架。`
        )
    } catch {
        return
    }
    activityDeletingId.value = row.id
    try {
        await activityDel({ id: row.id })
        feedback.msgSuccess('删除成功')
        await getActivities()
    } catch {
        // 请求层已提示
    } finally {
        activityDeletingId.value = 0
    }
}

watch(rosterDateRange, (value: string[]) => {
    rosterQuery.dateStart = value?.[0] || ''
    rosterQuery.dateEnd = value?.[1] || ''
})

watch(signupDateRange, (value: string[]) => {
    signupQuery.dateStart = value?.[0] || ''
    signupQuery.dateEnd = value?.[1] || ''
})

watch(
    () => rosterQuery.sessionId,
    () => {
        rosterQuery.linkId = ''
        loadLinkOptions()
    }
)

onMounted(async () => {
    await loadOptions()
    getLinks()
})

watch(activeTab, (tab) => {
    if (tab === 'roster' && !rosterLoaded) {
        rosterLoaded = true
        loadLinkOptions()
        getRoster()
    }
    if (tab === 'signup' && !signupLoaded) {
        signupLoaded = true
        loadSignupActivities()
        getSignups()
    }
    if (tab === 'apply' && !applyLoaded) {
        applyLoaded = true
        getApplies()
    }
    if (tab === 'exception' && !exceptionLoaded) {
        exceptionLoaded = true
        getExceptions()
    }
    if (tab === 'manage' && !activityLoaded) {
        activityLoaded = true
        loadActivityAreas()
        getActivities()
    }
})

async function loadOptions() {
    try {
        const data: any = await getEnrollOptions()
        options.value = data || []
    } catch {
        options.value = []
    }
}

async function loadLinkOptions() {
    try {
        const data: any = await getEnrollLinks({
            page_no: 1,
            page_size: 60,
            sessionId: rosterQuery.sessionId || ''
        })
        linkOptions.value = data?.lists || []
    } catch {
        linkOptions.value = []
    }
}

function optionLabel(item: any) {
    const parts = [item.courseTitle, `${item.date || ''} ${item.time || ''}`.trim(), item.areaName]
    if (item.coachName) parts.push(`教练 ${item.coachName}`)
    return parts.filter(Boolean).join(' · ')
}

function linkOptionLabel(item: any) {
    return `${item.token} · ${item.courseTitle || ''} · ${item.date || ''} ${item.time || ''}`.trim()
}

async function generate() {
    if (!sessionId.value) {
        feedback.msgWarning('请先选择课程场次')
        return
    }
    generating.value = true
    try {
        const data: any = await createEnrollLink({
            sessionId: sessionId.value,
            expireDays: expireDays.value
        })
        generated.value = data
        linkQuery.sessionId = data.sessionId
        linkPager.page = 1
        await getLinks()
        await loadLinkOptions()
        feedback.msgSuccess(data.reused ? '已复用该场次的有效链接' : '报名链接已生成')
    } finally {
        generating.value = false
    }
}

async function copyPath(path: string) {
    if (!path) return
    try {
        await toClipboard(path)
        feedback.msgSuccess('小程序路径已复制')
    } catch {
        feedback.msgError('复制失败，请手动选择复制')
    }
}

async function toggleLink(row: any) {
    const next = row.status === 1 ? 0 : 1
    const action = next === 1 ? '启用' : '停用'
    try {
        await feedback.confirm(`确定${action}链接 ${row.token} 吗？`)
    } catch {
        return
    }
    const data: any = await updateEnrollLinkStatus({ id: row.id, status: next })
    if (data?.notice) {
        feedback.msgWarning(data.notice)
    } else {
        feedback.msgSuccess(`链接已${action}`)
    }
    await getLinks()
    await loadLinkOptions()
}

/** 剩余有效期（天，向上取整）；0/缺失=长期返回 0；负值属脏数据兜底 1，
 * 防保存误设为永久 expireDays=0=长期（#178 对抗审查发现）。 */
function remainExpireDays(expireTime: any) {
    const seconds = Number(expireTime)
    if (!Number.isFinite(seconds)) return 0
    if (seconds < 0) return 1
    if (seconds === 0) return 0
    const days = Math.ceil((seconds * 1000 - Date.now()) / 86400000)
    return Math.min(365, Math.max(1, days))
}

function openExpire(row: any) {
    expireDialog.visible = true
    expireDialog.id = row.id
    expireDialog.token = row.token
    expireDialog.days = remainExpireDays(row.expireTime)
}

async function saveExpire() {
    expireDialog.saving = true
    try {
        const data: any = await updateEnrollLinkExpire({
            id: expireDialog.id,
            expireDays: expireDialog.days
        })
        expireDialog.visible = false
        if (data?.notice) {
            feedback.msgWarning(data.notice)
        } else {
            feedback.msgSuccess('有效期已更新')
        }
        await getLinks()
        await loadLinkOptions()
    } finally {
        expireDialog.saving = false
    }
}

function rosterParams() {
    return {
        page_no: rosterPager.page,
        page_size: rosterPager.size,
        linkId: rosterQuery.linkId,
        sessionId: rosterQuery.sessionId,
        phone: rosterQuery.phone,
        studentName: rosterQuery.studentName,
        status: rosterQuery.status,
        dateStart: rosterQuery.dateStart,
        dateEnd: rosterQuery.dateEnd
    }
}

function resetRosterParams() {
    rosterDateRange.value = []
    rosterQuery.dateStart = ''
    rosterQuery.dateEnd = ''
    resetRosterParamsBase()
}

async function exportRoster() {
    exporting.value = true
    try {
        const data: any = await exportEnrollments(rosterParams())
        if (data?.ticket) {
            feedback.msgSuccess(`已生成 ${data.rowCount || 0} 条报名记录，开始下载`)
        }
    } finally {
        exporting.value = false
    }
}

// ---------- 活动报名名单（含支付状态） ----------
async function loadSignupActivities() {
    try {
        const data: any = await activityList({ page_no: 1, page_size: 60 })
        signupActivityOptions.value = data?.lists || []
    } catch {
        signupActivityOptions.value = []
    }
}

function signupParams() {
    return {
        page_no: signupPager.page,
        page_size: signupPager.size,
        activityId: signupQuery.activityId,
        status: signupQuery.status,
        payStatus: signupQuery.payStatus,
        keyword: signupQuery.keyword,
        dateStart: signupQuery.dateStart,
        dateEnd: signupQuery.dateEnd
    }
}

function resetSignupParams() {
    signupDateRange.value = []
    signupQuery.dateStart = ''
    signupQuery.dateEnd = ''
    resetSignupParamsBase()
}

async function exportSignupRoster() {
    signupExporting.value = true
    try {
        const data: any = await exportActivitySignups(signupParams())
        if (data?.ticket) {
            feedback.msgSuccess(`已生成 ${data.rowCount || 0} 条活动报名记录，开始下载`)
        }
    } finally {
        signupExporting.value = false
    }
}

// 后台取消活动报名（#213 C3）：免费直接释放名额；已支付只生成退款申请，
// 由「退款管理」审核通过后才退款并释放名额（不在本入口直接调资金通道）。
async function cancelSignup(row: any) {
    const paid = Number(row.payStatus) === 1
    let remark = ''
    try {
        const result: any = await feedback.prompt(
            paid
                ? '该报名已支付：提交后生成退款申请，需在「退款管理」审核通过后按原支付方式退回并释放名额。请填写取消原因（选填）。'
                : '取消后立即释放该报名占用的名额。请填写取消原因（选填）。',
            '取消报名',
            {
                inputPlaceholder: '取消原因（选填，最多 500 字）',
                inputValidator: (value: string) =>
                    !value || String(value).length <= 500 || '取消原因最多 500 字'
            }
        )
        remark = result?.value || ''
    } catch {
        return
    }
    cancelSignupSavingId.value = row.id
    try {
        const data: any = await cancelActivitySignup({ signupId: row.id, remark })
        if (data?.mode === 'refunding') {
            feedback.msgSuccess('已提交退款申请，请在「退款管理」审核')
        } else {
            feedback.msgSuccess('已取消报名，名额已释放')
        }
        await getSignups()
    } catch {
        // 请求层已提示
    } finally {
        cancelSignupSavingId.value = 0
    }
}

// ---------- 导出活动报名 / 开班申请报表（#105：旧报表菜单退场，导出搬到业务页）----------
// 两个入口数据源不同，文案已区分：
//   「导出报名名单」= activity/enroll/export，导 la_enrollment 报名名单（本页表格数据）；
//   「活动报名报表」= report/activity/export，导旧报表 la_activity_signup 报名报表；
//   「导出开班申请报表」= report/class_apply/export，导旧报表 la_class_apply。
const reportExporting = ref(false)
const applyExporting = ref(false)
const applyExportDateRange = ref<string[]>(currentMonthRange())

async function exportActivityReportExcel() {
    reportExporting.value = true
    feedback.loading('正在导出活动报名报表...')
    try {
        // 报表维度只有活动/社区/状态/关键词；本页能映射的是提交时间范围、
        // 手机号与学员名（报表关键词同时匹配两者），链接/场次是名单维度，报表不认。
        await exportActivityReport(
            compactParams({
                startDate: rosterQuery.dateStart,
                endDate: rosterQuery.dateEnd,
                keyword: rosterQuery.phone || rosterQuery.studentName
            })
        )
    } finally {
        feedback.closeLoading()
        reportExporting.value = false
    }
}

async function exportClassApplyExcel() {
    applyExporting.value = true
    feedback.loading('正在导出开班申请报表...')
    try {
        await exportClassApplyReport(
            compactParams({
                startDate: applyExportDateRange.value?.[0] || '',
                endDate: applyExportDateRange.value?.[1] || '',
                courseType: applyQuery.courseType,
                status: applyQuery.status
            })
        )
    } finally {
        feedback.closeLoading()
        applyExporting.value = false
    }
}

async function openException(row: any) {
    exceptionDialog.visible = true
    exceptionDialog.loading = true
    exceptionDialog.detail = null
    try {
        const data: any = await getEnrollExceptionDetail({ id: row.id })
        exceptionDialog.detail = data
    } finally {
        exceptionDialog.loading = false
    }
}

async function markExceptionHandled(row: any) {
    let remark = ''
    try {
        const result: any = await feedback.prompt('可填写处理备注（选填）：', '标记异常报名已处理', {
            inputPlaceholder: '例如：已与家长确认保留超员名额',
            inputValue: row.remark || ''
        })
        remark = result?.value || ''
    } catch {
        return
    }
    await handleEnrollException({ id: row.id, remark })
    feedback.msgSuccess('已标记处理')
    exceptionDialog.visible = false
    await refreshExceptionData()
}

async function refundExceptionOrder(row: any) {
    try {
        await feedback.confirm(
            `确定对订单 ${row.orderSn} 发起退款吗？退款成功后将取消该学员报名并释放名额。`
        )
    } catch {
        return
    }
    let remark = ''
    try {
        const result: any = await feedback.prompt('可填写退款备注（选填）：', '异常报名退款', {
            inputPlaceholder: '例如：满员补偿退款',
            inputValue: row.remark || ''
        })
        remark = result?.value || ''
    } catch {
        return
    }
    await refundEnrollException({ id: row.id, remark })
    feedback.msgSuccess('退款已完成，报名已取消并释放名额')
    exceptionDialog.visible = false
    await refreshExceptionData()
}

async function refreshExceptionData() {
    exceptionLoaded = true
    await getExceptions()
    if (activeTab.value === 'links') await getLinks()
}

function exceptionStatusTag(status: number) {
    if (status === 1) return 'success'
    if (status === 2) return 'info'
    return 'warning'
}

function linkStatusTag(row: any) {
    if (row.status === 0) return 'info'
    if (row.statusText === '已过期') return 'warning'
    return 'success'
}

function statusTag(status: number) {
    if (status === 1) return 'success'
    if (status === 2) return 'danger'
    return 'warning'
}

// 活动报名明细状态：0 待支付 / 1 已支付 / 3 已取消（与支付状态分开显示）
function signupStatusTag(status: any) {
    const value = Number(status)
    if (value === 1) return 'success'
    if (value === 3) return 'info'
    return 'warning'
}

// 支付状态：1 已支付 / 0 待支付 / 4 已取消 / -1 免费（plain）
function payStatusTag(payStatus: any) {
    const value = Number(payStatus)
    if (value === 1) return 'success'
    if (value === 0) return 'warning'
    return 'info'
}

function applyStatusTag(status: string) {
    if (status === 'following') return 'warning'
    if (status === 'closed') return 'success'
    return 'info'
}

function progress(row: any) {
    const target = Number(row.target) || 5
    return Math.min(100, Math.round((Number(row.joined) || 0) / target * 100))
}
</script>

<style scoped>
.link-path {
    display: inline-block;
    max-width: 640px;
    padding: 4px 10px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    background: #fff;
    border: 1px solid #e5e7eb;
    border-radius: 4px;
    font-size: 13px;
    color: #374151;
}

.token-line {
    display: flex;
    align-items: center;
    gap: 6px;
    min-width: 0;
}

.token-text {
    display: inline-block;
    max-width: 150px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    font-size: 12px;
    color: #374151;
}

.dedupe-panel {
    padding: 10px 14px;
    background: #f8fafc;
    border: 1px solid #e5e7eb;
    border-radius: 6px;
    font-size: 13px;
    color: #374151;
}

.activity-tabs :deep(.el-tabs__header) {
    margin-bottom: 16px;
}

.form-section-title {
    margin: 4px 0 16px;
    padding-left: 8px;
    border-left: 3px solid var(--el-color-primary);
    font-size: 14px;
    font-weight: 600;
    line-height: 16px;
}

.activity-form-tabs :deep(.el-tabs__header) {
    margin-bottom: 12px;
}
</style>
