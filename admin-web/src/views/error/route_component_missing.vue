<template>
    <div class="route-component-missing">
        <el-result
            icon="error"
            title="页面组件缺失"
            sub-title="菜单指向的页面文件不存在，请联系管理员检查菜单配置"
        >
            <template #extra>
                <div class="mb-4 text-tx-secondary">当前路径：{{ route.fullPath }}</div>
                <el-button type="primary" @click="router.go(-1)">返回上一页</el-button>
            </template>
        </el-result>
    </div>
</template>

<script lang="ts" setup>
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()
</script>

<style lang="scss" scoped>
.route-component-missing {
    display: flex;
    align-items: center;
    justify-content: center;
    min-height: calc(100vh - 120px);
}
</style>
