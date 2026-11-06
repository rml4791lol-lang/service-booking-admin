<template>
    <div class="report-statistics">
        <el-card class="!border-none" shadow="never">
            <div class="flex flex-wrap items-start justify-between gap-4">
                <div>
                    <div class="text-lg font-medium">数据统计</div>
                    <div class="text-tx-secondary text-sm mt-1 leading-6">
                        教练 / 课程包 / 社区 / 小区对象数量与期间营业额总览，含营业额按日趋势、教练维度与社区维度的订单数、
                        客流（到场人次）与营业额明细。金额只统计已支付且未退款的订单实付金额，只做真实营业额与工作量展示。
                    </div>
                </div>
                <div class="flex shrink-0 flex-wrap items-center gap-2">
                    <el-button
                        size="small"
                        :type="isThisMonth ? 'primary' : ''"
                        @click="selectMonth('current')"
                    >
                        本月
                    </el-button>
                    <el-button
                        size="small"
                        :type="isLastMonth ? 'primary' : ''"
                        @click="selectMonth('last')"
                    >
                        上月
                    </el-button>
                    <el-date-picker
                        v-model="query.month"
                        type="month"
                        placeholder="选择月份"
                        value-format="YYYY-MM"
                        size="small"
                        style="width: 120px"
                        @change="onMonthChange"
                    />
                    <el-button
                        v-perms="['stats/instructor/export']"
                        type="primary"
                        size="small"
                        :loading="exporting"
                        @click="handleExport"
                    >
                        导出 Excel
                    </el-button>
                </div>
            </div>
            <div class="text-tx-secondary mt-3 text-sm">
                统计期间：{{ periodStart || '-' }} ~ {{ periodEnd || '-' }}
                <span v-if="previousPeriod">　对比期间：{{ previousPeriod }}</span>
            </div>
        </el-card>

        <div
            v-loading="overviewLoading"
            class="mt-4 grid grid-cols-2 gap-3 md:grid-cols-3 lg:grid-cols-5"
        >
            <el-card v-for="card in objectCards" :key="card.label" class="!border-none" shadow="never">
                <div class="text-xs text-tx-secondary">{{ card.label }}</div>
                <div class="mt-1 text-xl font-medium">{{ card.value }}</div>
                <div class="text-tx-secondary mt-1 text-xs">{{ card.hint }}</div>
            </el-card>
        </div>

        <el-card v-loading="overviewLoading" class="!border-none mt-4" shadow="never">
            <div class="grid grid-cols-2 gap-3 md:grid-cols-4">
                <div class="rounded bg-page p-3">
                    <div class="text-xs text-tx-secondary">期间订单数</div>
                    <div class="mt-1 text-lg font-medium">{{ numberText(objects.orderCount) }}</div>
                    <div class="text-tx-secondary mt-1 text-xs">
                        较上期 {{ signedNumber(previous?.orderDelta) }} 笔
                    </div>
                </div>
                <div class="rounded bg-page p-3">
                    <div class="text-xs text-tx-secondary">期间总营业额</div>
                    <div class="mt-1 text-lg font-medium">￥{{ money(amounts.revenue) }}</div>
                    <div class="text-tx-secondary mt-1 text-xs">
                        较上期 {{ signedMoney(previous?.revenueDelta) }}
                        <span v-if="previous?.revenueRatio !== null && previous?.revenueRatio !== undefined">
                            （{{ percent(previous.revenueRatio) }}）
                        </span>
                    </div>
                </div>
                <div class="rounded bg-page p-3">
                    <div class="text-xs text-tx-secondary">上课数(场次)</div>
                    <div class="mt-1 text-lg font-medium">{{ numberText(amounts.lessonCount) }}</div>
                </div>
                <div class="rounded bg-page p-3">
                    <div class="text-xs text-tx-secondary">服务人次</div>
                    <div class="mt-1 text-lg font-medium">{{ numberText(amounts.servicePersons) }}</div>
                </div>
            </div>
        </el-card>

        <el-card class="!border-none mt-4" shadow="never">
            <template #header>
                <div class="flex flex-wrap items-center justify-between gap-2">
                    <span>营业额趋势</span>
                    <div class="flex items-center gap-2">
                        <el-date-picker
                            v-model="trendRange"
                            type="daterange"
                            range-separator="至"
                            start-placeholder="开始日期"
                            end-placeholder="结束日期"
                            value-format="YYYY-MM-DD"
                            size="small"
                            style="width: 240px"
                            @change="getTrend"
                        />
                        <el-button size="small" @click="resetTrendRange">近 30 天</el-button>
                    </div>
                </div>
            </template>
            <div v-loading="trendLoading">
                <v-charts style="height: 320px" :option="trendOption" :autoresize="true" />
                <div class="text-tx-secondary mt-2 text-xs">
                    区间合计：订单 {{ numberText(trendSummary.orderCount) }} 笔、营业额 ￥{{
                        money(trendSummary.revenue)
                    }}；日均 ￥{{ money(trendSummary.avgRevenue) }}
                </div>
            </div>
        </el-card>

        <el-card class="!border-none mt-4" shadow="never">
            <el-tabs v-model="activeTab" @tab-change="onTabChange">
                <el-tab-pane label="教练维度" name="instructor">
                    <div class="flex flex-wrap items-center justify-between gap-2 mb-3">
                        <span class="text-tx-secondary text-xs">
                            订单数 / 上课数 / 服务人次 / 总营业额（自然月口径，可下钻明细）
                        </span>
                        <el-button
                            v-perms="['stats/instructor/export']"
                            type="primary"
                            size="small"
                            :loading="exporting"
                            @click="handleExport"
                        >
                            导出 Excel
                        </el-button>
                    </div>
                    <el-table size="large" table-layout="fixed" v-loading="pager.loading" :data="pager.lists">
                        <el-table-column label="教练" min-width="180" show-overflow-tooltip>
                            <template #default="{ row }">
                                <span :class="{ 'text-tx-secondary': !row.coachId }">
                                    {{ row.coachName || (row.coachId ? `教练#${row.coachId}` : '未分配教练') }}
                                </span>
                            </template>
                        </el-table-column>
                        <el-table-column label="订单数" prop="orderCount" width="96" align="center" />
                        <el-table-column label="总营业额" min-width="130" align="right">
                            <template #default="{ row }">￥{{ money(row.paidAmount) }}</template>
                        </el-table-column>
                        <el-table-column label="上课数(场次)" prop="lessonCount" width="130" align="center" />
                        <el-table-column label="服务人次" prop="servicePersons" width="110" align="center" />
                        <el-table-column label="操作" width="96" fixed="right" align="center">
                            <template #default="{ row }">
                                <el-button
                                    v-perms="['stats/instructor/detail']"
                                    type="primary"
                                    link
                                    @click="openDetail(row)"
                                >
                                    明细
                                </el-button>
                            </template>
                        </el-table-column>
                    </el-table>
                    <el-alert
                        v-if="couponNote"
                        class="mt-4"
                        type="warning"
                        :closable="false"
                        :title="couponNote"
                    />
                    <div class="flex justify-end mt-4">
                        <pagination v-model="pager" @change="getLists" />
                    </div>
                </el-tab-pane>

                <el-tab-pane label="社区维度" name="community">
                    <div class="community-tab">
                        <div class="flex flex-wrap items-center justify-between gap-2 mb-3">
                            <span class="text-tx-secondary text-xs">
                                社区 / 订单数 / 客流（到场人次）/ 营业额；点行可下钻订单与课节明细。
                                班级未填社区时按所属小区归属，两者都缺显示「未归属」。
                            </span>
                            <el-button
                                v-perms="['stats/community/export']"
                                type="primary"
                                size="small"
                                :loading="communityExporting"
                                @click="handleCommunityExport"
                            >
                                导出 Excel
                            </el-button>
                        </div>
                        <el-table
                            class="community-table"
                            size="large"
                            table-layout="fixed"
                            v-loading="communityPager.loading"
                            :data="communityPager.lists"
                            @row-click="openCommunityDetail"
                        >
                            <el-table-column label="社区" min-width="200" show-overflow-tooltip>
                                <template #default="{ row }">
                                    <span :class="{ 'text-tx-secondary': !row.communityId }">
                                        {{ row.communityName }}
                                    </span>
                                </template>
                            </el-table-column>
                            <el-table-column label="订单数" prop="orderCount" width="100" align="center" />
                            <el-table-column label="客流(人次)" prop="attends" width="120" align="center" />
                            <el-table-column label="营业额" min-width="140" align="right">
                                <template #default="{ row }">￥{{ money(row.revenue) }}</template>
                            </el-table-column>
                            <el-table-column label="操作" width="96" fixed="right" align="center">
                                <template #default="{ row }">
                                    <el-button type="primary" link @click.stop="openCommunityDetail(row)">
                                        明细
                                    </el-button>
                                </template>
                            </el-table-column>
                        </el-table>
                        <div class="flex justify-end mt-4">
                            <pagination v-model="communityPager" @change="getCommunityLists" />
                        </div>
                    </div>
                </el-tab-pane>

                <el-tab-pane label="小区维度" name="area">
                    <div class="area-tab">
                        <div class="flex flex-wrap items-center justify-between gap-2 mb-3">
                            <span class="text-tx-secondary text-xs">
                                小区 / 订单数 / 客流（到场人次）/ 营业额；点行可下钻订单与课节明细。
                                班级未填小区时显示「未归属」。
                            </span>
                            <el-button
                                v-perms="['stats/area/export']"
                                type="primary"
                                size="small"
                                :loading="areaExporting"
                                @click="handleAreaExport"
                            >
                                导出 Excel
                            </el-button>
                        </div>
                        <el-table
                            class="area-table"
                            size="large"
                            table-layout="fixed"
                            v-loading="areaPager.loading"
                            :data="areaPager.lists"
                            @row-click="openAreaDetail"
                        >
                            <el-table-column label="小区" min-width="200" show-overflow-tooltip>
                                <template #default="{ row }">
                                    <span :class="{ 'text-tx-secondary': !row.areaId }">
                                        {{ row.areaName }}
                                    </span>
                                </template>
                            </el-table-column>
                            <el-table-column label="订单数" prop="orderCount" width="100" align="center" />
                            <el-table-column label="客流(人次)" prop="attends" width="120" align="center" />
                            <el-table-column label="营业额" min-width="140" align="right">
                                <template #default="{ row }">￥{{ money(row.revenue) }}</template>
                            </el-table-column>
                            <el-table-column label="操作" width="96" fixed="right" align="center">
                                <template #default="{ row }">
                                    <el-button type="primary" link @click.stop="openAreaDetail(row)">
                                        明细
                                    </el-button>
                                </template>
                            </el-table-column>
                        </el-table>
                        <div class="flex justify-end mt-4">
                            <pagination v-model="areaPager" @change="getAreaLists" />
                        </div>
                    </div>
                </el-tab-pane>
                <el-tab-pane label="老板视角" name="owner">
                    <div class="owner-tab">
                        <div class="flex flex-wrap items-center justify-between gap-2 mb-3">
                            <span class="text-tx-secondary text-xs">
                                片区覆盖 / 社区最好 / 教练最好 / 上了什么课。期间由页面顶部月份选择器控制；
                                排序主键默认营业额（并列看订单数），「未归属 / 未分配」始终显式成行，
                                各表合计与总览一致。教练排行复用教练月营业额口径。
                            </span>
                            <div class="flex flex-wrap items-center gap-2">
                                <el-select
                                    v-model="ownerOrderBy"
                                    class="owner-order-by"
                                    size="small"
                                    style="width: 132px"
                                    @change="getOwnerData"
                                >
                                    <el-option label="按营业额" value="revenue" />
                                    <el-option label="按订单数" value="orderCount" />
                                    <el-option label="按客流" value="attends" />
                                </el-select>
                                <el-select
                                    v-model="ownerLimit"
                                    size="small"
                                    style="width: 116px"
                                    @change="getOwnerData"
                                >
                                    <el-option label="Top 10" :value="10" />
                                    <el-option label="Top 20" :value="20" />
                                    <el-option label="Top 50" :value="50" />
                                    <el-option label="全部" :value="0" />
                                </el-select>
                                <el-button size="small" :loading="ownerLoading" @click="getOwnerData">
                                    刷新
                                </el-button>
                            </div>
                        </div>

                        <el-card class="!border-none mt-2" shadow="never">
                            <template #header>
                                <div class="flex flex-wrap items-center justify-between gap-2">
                                    <span>片区覆盖（覆盖社区数 / 订单数 / 营业额）</span>
                                    <el-button
                                        v-perms="['stats/owner/region']"
                                        type="primary"
                                        size="small"
                                        :loading="ownerExporting === 'region'"
                                        @click="handleOwnerExport('region')"
                                    >
                                        导出 Excel
                                    </el-button>
                                </div>
                            </template>
                            <el-table
                                class="owner-region-table"
                                size="small"
                                table-layout="fixed"
                                v-loading="ownerLoading"
                                :data="ownerRegion.rows || []"
                            >
                                <el-table-column label="片区" min-width="180" show-overflow-tooltip>
                                    <template #default="{ row }">
                                        <span :class="{ 'text-tx-secondary': !row.regionId }">
                                            {{ row.regionName }}
                                        </span>
                                    </template>
                                </el-table-column>
                                <el-table-column
                                    label="覆盖社区数"
                                    prop="communityCount"
                                    width="120"
                                    align="center"
                                />
                                <el-table-column label="订单数" prop="orderCount" width="100" align="center" />
                                <el-table-column label="营业额" min-width="140" align="right">
                                    <template #default="{ row }">￥{{ money(row.revenue) }}</template>
                                </el-table-column>
                            </el-table>
                            <div class="text-tx-secondary mt-2 text-xs">
                                片区 {{ numberText(ownerRegion.summary?.regionCount) }} 个、社区
                                {{ numberText(ownerRegion.summary?.communityCount) }} 个（未归属
                                {{ numberText(ownerRegion.summary?.unassignedCommunityCount) }} 个）；
                                行合计 订单 {{ numberText(ownerRegion.summary?.orderCount) }} 笔 / ￥{{
                                    money(ownerRegion.summary?.revenue)
                                }}，总览 订单 {{ numberText(ownerRegion.summary?.totalOrderCount) }} 笔 / ￥{{
                                    money(ownerRegion.summary?.totalRevenue)
                                }}
                            </div>
                        </el-card>

                        <el-card class="!border-none mt-4" shadow="never">
                            <template #header>
                                <div class="flex flex-wrap items-center justify-between gap-2">
                                    <span>社区最好（营业额 DESC，并列看订单数）</span>
                                    <el-button
                                        v-perms="['stats/owner/communityTop']"
                                        type="primary"
                                        size="small"
                                        :loading="ownerExporting === 'communityTop'"
                                        @click="handleOwnerExport('communityTop')"
                                    >
                                        导出 Excel
                                    </el-button>
                                </div>
                            </template>
                            <el-table
                                class="owner-community-table"
                                size="small"
                                table-layout="fixed"
                                v-loading="ownerLoading"
                                :data="ownerCommunityTop.rows || []"
                            >
                                <el-table-column label="名次" prop="rank" width="72" align="center" />
                                <el-table-column label="社区" min-width="200" show-overflow-tooltip>
                                    <template #default="{ row }">
                                        <span :class="{ 'text-tx-secondary': !row.communityId }">
                                            {{ row.communityName }}
                                        </span>
                                    </template>
                                </el-table-column>
                                <el-table-column label="订单数" prop="orderCount" width="100" align="center" />
                                <el-table-column label="客流(人次)" prop="attends" width="116" align="center" />
                                <el-table-column label="营业额" min-width="140" align="right">
                                    <template #default="{ row }">￥{{ money(row.revenue) }}</template>
                                </el-table-column>
                            </el-table>
                            <div class="text-tx-secondary mt-2 text-xs">
                                共 {{ numberText(ownerCommunityTop.summary?.rowCount) }} 行，展示
                                {{ numberText(ownerCommunityTop.summary?.listedRowCount) }} 行（排序：{{
                                    ownerOrderLabel(ownerCommunityTop.summary?.orderBy)
                                }}）；行合计 订单 {{ numberText(ownerCommunityTop.summary?.orderCount) }} 笔 / ￥{{
                                    money(ownerCommunityTop.summary?.revenue)
                                }}，总览 订单 {{ numberText(ownerCommunityTop.summary?.totalOrderCount) }} 笔 / ￥{{
                                    money(ownerCommunityTop.summary?.totalRevenue)
                                }}
                            </div>
                        </el-card>

                        <el-card class="!border-none mt-4" shadow="never">
                            <template #header>
                                <div class="flex flex-wrap items-center justify-between gap-2">
                                    <span>教练最好（营业额 DESC，并列看订单数）</span>
                                    <el-button
                                        v-perms="['stats/owner/instructorTop']"
                                        type="primary"
                                        size="small"
                                        :loading="ownerExporting === 'instructorTop'"
                                        @click="handleOwnerExport('instructorTop')"
                                    >
                                        导出 Excel
                                    </el-button>
                                </div>
                            </template>
                            <el-table
                                class="owner-instructor-table"
                                size="small"
                                table-layout="fixed"
                                v-loading="ownerLoading"
                                :data="ownerInstructorTop.rows || []"
                            >
                                <el-table-column label="名次" prop="rank" width="72" align="center" />
                                <el-table-column label="教练" min-width="180" show-overflow-tooltip>
                                    <template #default="{ row }">
                                        <span :class="{ 'text-tx-secondary': !row.instructorId }">
                                            {{ row.instructorName }}
                                        </span>
                                    </template>
                                </el-table-column>
                                <el-table-column label="订单数" prop="orderCount" width="100" align="center" />
                                <el-table-column label="客流(人次)" prop="attends" width="116" align="center" />
                                <el-table-column label="营业额" min-width="140" align="right">
                                    <template #default="{ row }">￥{{ money(row.revenue) }}</template>
                                </el-table-column>
                            </el-table>
                            <div class="text-tx-secondary mt-2 text-xs">
                                共 {{ numberText(ownerInstructorTop.summary?.rowCount) }} 行，展示
                                {{ numberText(ownerInstructorTop.summary?.listedRowCount) }} 行（排序：{{
                                    ownerOrderLabel(ownerInstructorTop.summary?.orderBy)
                                }}）；行合计 订单 {{ numberText(ownerInstructorTop.summary?.orderCount) }} 笔 / ￥{{
                                    money(ownerInstructorTop.summary?.revenue)
                                }}，总览 订单 {{ numberText(ownerInstructorTop.summary?.totalOrderCount) }} 笔 / ￥{{
                                    money(ownerInstructorTop.summary?.totalRevenue)
                                }}
                            </div>
                        </el-card>

                        <el-card class="!border-none mt-4" shadow="never">
                            <template #header>
                                <div class="flex flex-wrap items-center justify-between gap-2">
                                    <span>上了什么课（课程班级 / 类目）</span>
                                    <el-button
                                        v-perms="['stats/owner/courseMix']"
                                        type="primary"
                                        size="small"
                                        :loading="ownerExporting === 'courseMix'"
                                        @click="handleOwnerExport('courseMix')"
                                    >
                                        导出 Excel
                                    </el-button>
                                </div>
                            </template>
                            <el-table
                                class="owner-course-mix-table"
                                size="small"
                                table-layout="fixed"
                                v-loading="ownerLoading"
                                :data="ownerCourseMix.rows || []"
                            >
                                <el-table-column label="课程班级" min-width="220" show-overflow-tooltip>
                                    <template #default="{ row }">
                                        <span :class="{ 'text-tx-secondary': !row.courseClassId }">
                                            {{ row.courseName }}
                                        </span>
                                    </template>
                                </el-table-column>
                                <el-table-column label="类目" min-width="140" show-overflow-tooltip>
                                    <template #default="{ row }">
                                        <span :class="{ 'text-tx-secondary': !row.categoryId }">
                                            {{ row.categoryName }}
                                        </span>
                                    </template>
                                </el-table-column>
                                <el-table-column label="订单数" prop="orderCount" width="100" align="center" />
                                <el-table-column label="营业额" min-width="140" align="right">
                                    <template #default="{ row }">￥{{ money(row.revenue) }}</template>
                                </el-table-column>
                            </el-table>
                            <div class="text-tx-secondary mt-2 text-xs">
                                共 {{ numberText(ownerCourseMix.summary?.rowCount) }} 门课；行合计 订单
                                {{ numberText(ownerCourseMix.summary?.orderCount) }} 笔 / ￥{{
                                    money(ownerCourseMix.summary?.revenue)
                                }}，总览 订单 {{ numberText(ownerCourseMix.summary?.totalOrderCount) }} 笔 / ￥{{
                                    money(ownerCourseMix.summary?.totalRevenue)
                                }}
                            </div>
                        </el-card>
                    </div>
                </el-tab-pane>

                <el-tab-pane label="课程包销量" name="coursePack">
                    <div class="course-pack-tab">
                        <div class="flex flex-wrap items-center justify-between gap-2 mb-3">
                            <span class="text-tx-secondary text-xs">
                                课程包 / 购买数 / 金额 / 报名数。购买数=报名记录条数、报名数=购买学员人数、
                                金额=期间已支付未退款订单实付金额；列出全部有购买或订单的课程包（不设固定数量）。
                            </span>
                            <el-button
                                v-perms="['stats/coursePack/export']"
                                type="primary"
                                size="small"
                                :loading="coursePackExporting"
                                @click="handleCoursePackExport"
                            >
                                导出 Excel
                            </el-button>
                        </div>
                        <el-table
                            class="course-pack-table"
                            size="large"
                            table-layout="fixed"
                            v-loading="coursePackPager.loading"
                            :data="coursePackPager.lists"
                        >
                            <el-table-column label="课程包" min-width="220" show-overflow-tooltip>
                                <template #default="{ row }">
                                    <span :class="{ 'text-tx-secondary': !row.coursePackId }">
                                        {{ row.coursePackName }}
                                    </span>
                                </template>
                            </el-table-column>
                            <el-table-column label="类目" min-width="130" show-overflow-tooltip prop="categoryName" />
                            <el-table-column label="购买数" prop="purchases" width="100" align="center" />
                            <el-table-column label="金额" min-width="140" align="right">
                                <template #default="{ row }">￥{{ money(row.revenue) }}</template>
                            </el-table-column>
                            <el-table-column label="报名数" prop="enrollCount" width="100" align="center" />
                        </el-table>
                        <div class="text-tx-secondary mt-2 text-xs">
                            共 {{ numberText(coursePackPager.extend?.summary?.coursePackCount) }} 个课程包；
                            购买 {{ numberText(coursePackPager.extend?.summary?.purchases) }} 次 / ￥{{
                                money(coursePackPager.extend?.summary?.revenue)
                            }} / 报名 {{ numberText(coursePackPager.extend?.summary?.enrollCount) }} 人
                        </div>
                        <div class="flex justify-end mt-4">
                            <pagination v-model="coursePackPager" @change="getCoursePackLists" />
                        </div>
                    </div>
                </el-tab-pane>
            </el-tabs>
        </el-card>

        <el-drawer v-model="detailVisible" title="教练统计下钻" size="820px">
            <template v-if="detail">
                <div class="mb-3 text-base font-medium">
                    {{ detail.coachName || '-' }}　
                    <span class="text-tx-secondary text-sm">
                        {{ periodStart || '' }} ~ {{ periodEnd || '' }}　订单
                        {{ detail.summary?.orderCount || 0 }} 笔　总营业额 ￥{{
                            money(detail.summary?.paidAmount)
                        }}　上课 {{ detail.summary?.lessonCount || 0 }} 场　服务
                        {{ detail.summary?.servicePersons || 0 }} 人次
                    </span>
                </div>

                <div class="mb-2 font-medium">课节明细</div>
                <el-table size="small" :data="detail.lessons || []" border max-height="240">
                    <el-table-column label="课程班级" min-width="180" show-overflow-tooltip>
                        <template #default="{ row }">
                            {{ row.courseName || `班级#${row.courseClassId}` }}
                        </template>
                    </el-table-column>
                    <el-table-column label="课节" prop="lessonNo" width="70" align="center" />
                    <el-table-column label="服务人次" prop="servicePersons" width="96" align="center" />
                    <el-table-column label="最近核销" prop="lastConsumeTime" width="160" />
                </el-table>
                <div v-if="detail.lessonsTruncated" class="text-tx-secondary mt-1 text-xs">
                    课节明细超过 500 行，已截断；完整数据请用导出。
                </div>

                <div class="mt-4 mb-2 font-medium">订单明细</div>
                <el-table size="small" :data="detail.orders || []" border max-height="240">
                    <el-table-column label="订单号" prop="sn" min-width="170" show-overflow-tooltip />
                    <el-table-column label="课程班级" min-width="150" show-overflow-tooltip>
                        <template #default="{ row }">
                            {{ row.courseName || `班级#${row.courseClassId}` }}
                        </template>
                    </el-table-column>
                    <el-table-column label="学员" min-width="100" show-overflow-tooltip>
                        <template #default="{ row }">
                            {{ row.studentName || (row.studentId ? `学员#${row.studentId}` : '-') }}
                        </template>
                    </el-table-column>
                    <el-table-column label="购买课时" prop="lessonCount" width="90" align="center" />
                    <el-table-column label="实付金额" width="110" align="right">
                        <template #default="{ row }">￥{{ money(row.orderAmount) }}</template>
                    </el-table-column>
                    <el-table-column label="支付时间" prop="payTime" width="160" />
                </el-table>
                <div v-if="detail.ordersTruncated" class="text-tx-secondary mt-1 text-xs">
                    订单明细超过 500 行，已截断；完整数据请用导出。
                </div>
            </template>
            <template #footer>
                <el-button @click="detailVisible = false">关闭</el-button>
            </template>
        </el-drawer>

        <el-drawer
            v-model="communityDetailVisible"
            :title="communityDetailTitle"
            size="900px"
            class="community-detail"
        >
            <template v-if="communityDetailRow">
                <div class="mb-3 text-base font-medium">
                    {{ communityDetailRow.communityName }}　
                    <span class="text-tx-secondary text-sm">
                        {{ periodStart || '' }} ~ {{ periodEnd || '' }}　订单
                        {{ numberText(communityDetailRow.orderCount) }} 笔　客流
                        {{ numberText(communityDetailRow.attends) }} 人次　营业额 ￥{{
                            money(communityDetailRow.revenue)
                        }}
                    </span>
                </div>

                <div class="mb-2 font-medium">订单明细</div>
                <el-table
                    v-loading="communityDetailLoading"
                    size="small"
                    :data="communityOrders"
                    border
                    max-height="260"
                >
                    <el-table-column label="订单号" prop="sn" min-width="170" show-overflow-tooltip />
                    <el-table-column label="课程班级" min-width="150" show-overflow-tooltip>
                        <template #default="{ row }">
                            {{ row.courseName || `班级#${row.courseClassId}` }}
                        </template>
                    </el-table-column>
                    <el-table-column label="学员" min-width="100" show-overflow-tooltip>
                        <template #default="{ row }">
                            {{ row.studentName || (row.studentId ? `学员#${row.studentId}` : '-') }}
                        </template>
                    </el-table-column>
                    <el-table-column label="实付金额" width="110" align="right">
                        <template #default="{ row }">￥{{ money(row.orderAmount) }}</template>
                    </el-table-column>
                    <el-table-column label="支付时间" prop="payTime" width="160" />
                </el-table>
                <div v-if="communityOrdersTruncated" class="text-tx-secondary mt-1 text-xs">
                    订单明细超过 {{ DETAIL_PAGE_SIZE }} 行，已截断；完整数据请用导出。
                </div>

                <div class="mt-4 mb-2 font-medium">课节明细</div>
                <el-table
                    v-loading="communityDetailLoading"
                    size="small"
                    :data="communityLessons"
                    border
                    max-height="260"
                >
                    <el-table-column label="课程班级" min-width="180" show-overflow-tooltip>
                        <template #default="{ row }">
                            {{ row.courseName || `班级#${row.courseClassId}` }}
                        </template>
                    </el-table-column>
                    <el-table-column label="课节" prop="lessonNo" width="70" align="center" />
                    <el-table-column label="客流(人次)" prop="servicePersons" width="110" align="center" />
                    <el-table-column label="最近核销" prop="lastConsumeTime" width="160" />
                </el-table>
                <div v-if="communityLessonsTruncated" class="text-tx-secondary mt-1 text-xs">
                    课节明细超过 {{ DETAIL_PAGE_SIZE }} 行，已截断；完整数据请用导出。
                </div>
            </template>
            <template #footer>
                <el-button @click="communityDetailVisible = false">关闭</el-button>
            </template>
        </el-drawer>

        <el-drawer v-model="areaDetailVisible" :title="areaDetailTitle" size="900px" class="area-detail">
            <template v-if="areaDetailRow">
                <div class="mb-3 text-base font-medium">
                    {{ areaDetailRow.areaName }}　
                    <span class="text-tx-secondary text-sm">
                        {{ periodStart || '' }} ~ {{ periodEnd || '' }}　订单
                        {{ numberText(areaDetailRow.orderCount) }} 笔　客流
                        {{ numberText(areaDetailRow.attends) }} 人次　营业额 ￥{{
                            money(areaDetailRow.revenue)
                        }}
                    </span>
                </div>

                <div class="mb-2 font-medium">订单明细</div>
                <el-table v-loading="areaDetailLoading" size="small" :data="areaOrders" border max-height="260">
                    <el-table-column label="订单号" prop="sn" min-width="170" show-overflow-tooltip />
                    <el-table-column label="课程班级" min-width="150" show-overflow-tooltip>
                        <template #default="{ row }">
                            {{ row.courseName || `班级#${row.courseClassId}` }}
                        </template>
                    </el-table-column>
                    <el-table-column label="学员" min-width="100" show-overflow-tooltip>
                        <template #default="{ row }">
                            {{ row.studentName || (row.studentId ? `学员#${row.studentId}` : '-') }}
                        </template>
                    </el-table-column>
                    <el-table-column label="实付金额" width="110" align="right">
                        <template #default="{ row }">￥{{ money(row.orderAmount) }}</template>
                    </el-table-column>
                    <el-table-column label="支付时间" prop="payTime" width="160" />
                </el-table>
                <div v-if="areaOrdersTruncated" class="text-tx-secondary mt-1 text-xs">
                    订单明细超过 {{ DETAIL_PAGE_SIZE }} 行，已截断；完整数据请用导出。
                </div>

                <div class="mt-4 mb-2 font-medium">课节明细</div>
                <el-table v-loading="areaDetailLoading" size="small" :data="areaLessons" border max-height="260">
                    <el-table-column label="课程班级" min-width="180" show-overflow-tooltip>
                        <template #default="{ row }">
                            {{ row.courseName || `班级#${row.courseClassId}` }}
                        </template>
                    </el-table-column>
                    <el-table-column label="课节" prop="lessonNo" width="70" align="center" />
                    <el-table-column label="客流(人次)" prop="servicePersons" width="110" align="center" />
                    <el-table-column label="最近核销" prop="lastConsumeTime" width="160" />
                </el-table>
                <div v-if="areaLessonsTruncated" class="text-tx-secondary mt-1 text-xs">
                    课节明细超过 {{ DETAIL_PAGE_SIZE }} 行，已截断；完整数据请用导出。
                </div>
            </template>
            <template #footer>
                <el-button @click="areaDetailVisible = false">关闭</el-button>
            </template>
        </el-drawer>
    </div>
</template>

<script lang="ts" setup name="reportStatistics">
import { computed, reactive, ref } from 'vue'
import vCharts from 'vue-echarts'

import {
    exportStatsArea,
    exportStatsCommunity,
    exportStatsCoursePack,
    exportStatsInstructor,
    exportStatsOwnerCommunityTop,
    exportStatsOwnerCourseMix,
    exportStatsOwnerInstructorTop,
    exportStatsOwnerRegion,
    getStatsAreaList,
    getStatsCommunityList,
    getStatsCoursePackList,
    getStatsInstructorDetail,
    getStatsInstructorList,
    getStatsLessonList,
    getStatsOrderList,
    getStatsOverview,
    getStatsOwnerCommunityTop,
    getStatsOwnerCourseMix,
    getStatsOwnerInstructorTop,
    getStatsOwnerRegion,
    getStatsTrend
} from '@/api/stats'
import { usePaging } from '@/hooks/usePaging'
import feedback from '@/utils/feedback'
import { compactParams, money, numberText } from '@/views/report/helpers'

function monthText(offset: number) {
    const date = new Date()
    const target = new Date(date.getFullYear(), date.getMonth() + offset, 1, 12, 0, 0)
    const month = String(target.getMonth() + 1).padStart(2, '0')
    return `${target.getFullYear()}-${month}`
}

function dateText(value: Date) {
    const pad = (item: number) => String(item).padStart(2, '0')
    return `${value.getFullYear()}-${pad(value.getMonth() + 1)}-${pad(value.getDate())}`
}

/** 趋势默认窗口：近 30 天（含今天）。 */
function last30Days(): string[] {
    const end = new Date()
    const start = new Date()
    start.setDate(start.getDate() - 29)
    return [dateText(start), dateText(end)]
}

const thisMonth = monthText(0)
const lastMonth = monthText(-1)

const query = reactive({ month: thisMonth })
const exporting = ref(false)
const overviewLoading = ref(false)
const overview = ref<any>({})
const trendLoading = ref(false)
const trend = ref<any>({})
const trendRange = ref<string[]>(last30Days())
const detailVisible = ref(false)
const detail = ref<any>(null)

// 维度 tab：默认教练维度（#106 行为），社区维度按需加载（#107）
const activeTab = ref('instructor')
const communityExporting = ref(false)
const communityDetailVisible = ref(false)
const communityDetailRow = ref<any>(null)
const communityDetailLoading = ref(false)
const communityOrders = ref<any[]>([])
const communityLessons = ref<any[]>([])
const communityOrdersTruncated = ref(false)
const communityLessonsTruncated = ref(false)
const areaExporting = ref(false)
const areaDetailVisible = ref(false)
const areaDetailRow = ref<any>(null)
const areaDetailLoading = ref(false)
const areaOrders = ref<any[]>([])
const areaLessons = ref<any[]>([])
const areaOrdersTruncated = ref(false)
const areaLessonsTruncated = ref(false)

// 老板视角（#109）：4 个查询 + 排序白名单（revenue|orderCount|attends）+ Top N；期间跟随页面月份
const ownerLoading = ref(false)
const ownerExporting = ref('')
const ownerOrderBy = ref('revenue')
const ownerLimit = ref(20)
const ownerRegion = ref<any>({})
const ownerCommunityTop = ref<any>({})
const ownerInstructorTop = ref<any>({})
const ownerCourseMix = ref<any>({})
const coursePackExporting = ref(false)

/** 社区/区域明细抽屉一次最多展示的行数，截断提示文案按此生成。 */
const DETAIL_PAGE_SIZE = 50

const { pager, getLists } = usePaging({
    fetchFun: (params: any) => getStatsInstructorList(compactParams({ ...params, month: query.month })),
    params: query,
    size: 20
})

const { pager: communityPager, getLists: getCommunityLists } = usePaging({
    fetchFun: (params: any) => getStatsCommunityList(compactParams({ ...params, month: query.month })),
    params: query,
    size: 50
})

const { pager: areaPager, getLists: getAreaLists } = usePaging({
    fetchFun: (params: any) => getStatsAreaList(compactParams({ ...params, month: query.month })),
    params: query,
    size: 50
})

const { pager: coursePackPager, getLists: getCoursePackLists } = usePaging({
    fetchFun: (params: any) => getStatsCoursePackList(compactParams({ ...params, month: query.month })),
    params: query,
    size: 20
})

const objects = computed<any>(() => overview.value?.objects || {})
const amounts = computed<any>(() => overview.value?.amounts || {})
const previous = computed<any>(() => overview.value?.previous || null)
const periodStart = computed(() => overview.value?.period?.startDate)
const periodEnd = computed(() => overview.value?.period?.endDate)
const isThisMonth = computed(() => query.month === thisMonth)
const isLastMonth = computed(() => query.month === lastMonth)

const previousPeriod = computed(() => {
    const item = previous.value
    return item ? `${item.startDate} ~ ${item.endDate}` : ''
})

const objectCards = computed(() => [
    { label: '教练', value: numberText(objects.value.instructorCount), hint: '有效教练' },
    {
        label: '课程包',
        value: numberText(objects.value.coursePackCount),
        hint: `在售 ${numberText(objects.value.coursePackOnSaleCount)}`
    },
    { label: '社区', value: numberText(objects.value.communityCount), hint: '有效社区' },
    { label: '小区', value: numberText(objects.value.areaCount), hint: '有效小区' },
    { label: '期间订单', value: numberText(objects.value.orderCount), hint: '已支付未退款' }
])

const trendSummary = computed<any>(() => trend.value?.summary || {})
const couponNote = computed(() => pager.extend?.couponNote || '')
const communityDetailTitle = computed(() =>
    communityDetailRow.value ? `社区明细 - ${communityDetailRow.value.communityName}` : '社区明细'
)
const areaDetailTitle = computed(() =>
    areaDetailRow.value ? `小区明细 - ${areaDetailRow.value.areaName}` : '小区明细'
)

const trendOption = computed<any>(() => {
    const points = trend.value?.points || []
    return {
        tooltip: { trigger: 'axis' },
        legend: { data: ['营业额', '订单数'] },
        grid: { left: 64, right: 56, top: 40, bottom: 36 },
        xAxis: {
            type: 'category',
            boundaryGap: true,
            data: points.map((item: any) => String(item.date).slice(5))
        },
        yAxis: [
            { type: 'value', name: '营业额(元)' },
            { type: 'value', name: '订单数', minInterval: 1 }
        ],
        series: [
            {
                name: '营业额',
                type: 'bar',
                barWidth: '42%',
                data: points.map((item: any) => Number(item.revenue || 0)),
                itemStyle: {
                    borderRadius: [6, 6, 0, 0],
                    color: {
                        type: 'linear',
                        x: 0,
                        y: 0,
                        x2: 0,
                        y2: 1,
                        colorStops: [
                            { offset: 0, color: '#4A5DFF' },
                            { offset: 1, color: '#5777ff' }
                        ]
                    }
                }
            },
            {
                name: '订单数',
                type: 'line',
                smooth: true,
                yAxisIndex: 1,
                symbolSize: 5,
                lineStyle: { color: '#ff8729', width: 2 },
                itemStyle: { color: '#ff8729' },
                data: points.map((item: any) => Number(item.orderCount || 0))
            }
        ]
    }
})

function signedNumber(value: any) {
    const number = Number(value || 0)
    return `${number > 0 ? '+' : ''}${numberText(number)}`
}

function signedMoney(value: any) {
    const number = Number(value || 0)
    return `${number > 0 ? '+' : number < 0 ? '-' : ''}￥${money(Math.abs(number))}`
}

function percent(value: any) {
    const number = Number(value || 0) * 100
    return `${number > 0 ? '+' : ''}${number.toFixed(2)}%`
}

async function getOverview() {
    overviewLoading.value = true
    try {
        overview.value = await getStatsOverview(compactParams({ month: query.month }))
    } finally {
        overviewLoading.value = false
    }
}

async function getTrend() {
    trendLoading.value = true
    try {
        const [startDate, endDate] = trendRange.value || []
        trend.value = await getStatsTrend(compactParams({ startDate, endDate }))
    } finally {
        trendLoading.value = false
    }
}

function resetTrendRange() {
    trendRange.value = last30Days()
    getTrend()
}

function selectMonth(type: 'current' | 'last') {
    query.month = type === 'current' ? thisMonth : lastMonth
    onMonthChange()
}

function onMonthChange() {
    pager.page = 1
    communityPager.page = 1
    areaPager.page = 1
    coursePackPager.page = 1
    getOverview()
    getLists()
    if (activeTab.value === 'community') {
        getCommunityLists()
    }
    if (activeTab.value === 'area') {
        getAreaLists()
    }
    if (activeTab.value === 'owner') {
        getOwnerData()
    }
    if (activeTab.value === 'coursePack') {
        getCoursePackLists()
    }
}

/** 切到社区/小区维度时按当前期间重新取数，避免停留在旧月份的列表。 */
function onTabChange(name: any) {
    if (name === 'community') {
        getCommunityLists()
    }
    if (name === 'area') {
        getAreaLists()
    }
    if (name === 'owner') {
        getOwnerData()
    }
    if (name === 'coursePack') {
        getCoursePackLists()
    }
}

async function openDetail(row: any) {
    detail.value = null
    detailVisible.value = true
    try {
        detail.value = await getStatsInstructorDetail(
            compactParams({ month: query.month, coachId: row.coachId })
        )
    } catch (error) {
        detail.value = null
    }
}

async function openCommunityDetail(row: any) {
    if (!row) return
    communityDetailRow.value = row
    communityOrders.value = []
    communityLessons.value = []
    communityOrdersTruncated.value = false
    communityLessonsTruncated.value = false
    communityDetailVisible.value = true
    communityDetailLoading.value = true
    const params = compactParams({
        month: query.month,
        communityId: row.communityId,
        page_no: 1,
        page_size: DETAIL_PAGE_SIZE
    })
    try {
        const [orders, lessons] = await Promise.all([getStatsOrderList(params), getStatsLessonList(params)])
        communityOrders.value = orders?.lists || []
        communityLessons.value = lessons?.lists || []
        communityOrdersTruncated.value = Number(orders?.count || 0) > DETAIL_PAGE_SIZE
        communityLessonsTruncated.value = Number(lessons?.count || 0) > DETAIL_PAGE_SIZE
    } catch (error) {
        communityOrders.value = []
        communityLessons.value = []
    } finally {
        communityDetailLoading.value = false
    }
}

async function openAreaDetail(row: any) {
    if (!row) return
    areaDetailRow.value = row
    areaOrders.value = []
    areaLessons.value = []
    areaOrdersTruncated.value = false
    areaLessonsTruncated.value = false
    areaDetailVisible.value = true
    areaDetailLoading.value = true
    const params = compactParams({
        month: query.month,
        areaId: row.areaId,
        page_no: 1,
        page_size: DETAIL_PAGE_SIZE
    })
    try {
        const [orders, lessons] = await Promise.all([getStatsOrderList(params), getStatsLessonList(params)])
        areaOrders.value = orders?.lists || []
        areaLessons.value = lessons?.lists || []
        areaOrdersTruncated.value = Number(orders?.count || 0) > DETAIL_PAGE_SIZE
        areaLessonsTruncated.value = Number(lessons?.count || 0) > DETAIL_PAGE_SIZE
    } catch (error) {
        areaOrders.value = []
        areaLessons.value = []
    } finally {
        areaDetailLoading.value = false
    }
}

/** 老板视角 4 个查询共用期间与排序参数（排序键只传白名单枚举）。 */
async function getOwnerData() {
    ownerLoading.value = true
    const params = compactParams({
        month: query.month,
        orderBy: ownerOrderBy.value,
        limit: ownerLimit.value
    })
    try {
        const [region, communityTop, instructorTop, courseMix] = await Promise.all([
            getStatsOwnerRegion(params),
            getStatsOwnerCommunityTop(params),
            getStatsOwnerInstructorTop(params),
            getStatsOwnerCourseMix(params)
        ])
        ownerRegion.value = region || {}
        ownerCommunityTop.value = communityTop || {}
        ownerInstructorTop.value = instructorTop || {}
        ownerCourseMix.value = courseMix || {}
    } finally {
        ownerLoading.value = false
    }
}

function ownerOrderLabel(key: any) {
    if (key === 'orderCount') return '订单数'
    if (key === 'attends') return '客流'
    return '营业额'
}

const OWNER_EXPORTS: Record<string, (params: any) => Promise<any>> = {
    region: exportStatsOwnerRegion,
    communityTop: exportStatsOwnerCommunityTop,
    instructorTop: exportStatsOwnerInstructorTop,
    courseMix: exportStatsOwnerCourseMix
}

async function handleOwnerExport(kind: string) {
    const run = OWNER_EXPORTS[kind]
    if (!run) return
    ownerExporting.value = kind
    feedback.loading('正在导出老板视角统计...')
    try {
        await run(
            compactParams({
                month: query.month,
                orderBy: ownerOrderBy.value,
                limit: ownerLimit.value
            })
        )
    } finally {
        feedback.closeLoading()
        ownerExporting.value = ''
    }
}

async function handleCoursePackExport() {
    coursePackExporting.value = true
    feedback.loading('正在导出课程包销量...')
    try {
        await exportStatsCoursePack(compactParams({ month: query.month }))
    } finally {
        feedback.closeLoading()
        coursePackExporting.value = false
    }
}

async function handleExport() {
    exporting.value = true
    feedback.loading('正在导出教练维度统计...')
    try {
        await exportStatsInstructor(compactParams({ month: query.month }))
    } finally {
        feedback.closeLoading()
        exporting.value = false
    }
}

async function handleAreaExport() {
    areaExporting.value = true
    feedback.loading('正在导出小区维度统计...')
    try {
        await exportStatsArea(compactParams({ month: query.month }))
    } finally {
        feedback.closeLoading()
        areaExporting.value = false
    }
}

async function handleCommunityExport() {
    communityExporting.value = true
    feedback.loading('正在导出社区维度统计...')
    try {
        await exportStatsCommunity(compactParams({ month: query.month }))
    } finally {
        feedback.closeLoading()
        communityExporting.value = false
    }
}

getOverview()
getTrend()
getLists()
</script>
