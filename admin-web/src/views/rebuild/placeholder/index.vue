<template>
  <div class="rebuild-placeholder">
    <el-card shadow="never">
      <h2 style="margin: 0 0 12px">重建管线占位页</h2>
      <p>能看到这个页面，说明「新建 .vue → 菜单/路由 → 页面」链路已打通。</p>
      <p>生成时间：{{ now }}</p>
    </el-card>
  </div>
</template>

<script setup lang="ts">
const now = new Date().toLocaleString()
</script>
