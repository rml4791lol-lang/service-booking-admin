<template>
    <div>
        <el-card class="!border-none" shadow="never">
            <el-alert type="warning" title="温馨提示：H5设置" :closable="false" show-icon />
        </el-card>
        <el-card class="!border-none mt-4" shadow="never">
            <el-form ref="formRef" :model="formData" label-width="120px">
                <el-form-item label="渠道状态" required prop="status">
                    <div>
                        <el-radio-group v-model="formData.status">
                            <el-radio :label="1">开启</el-radio>
                            <el-radio :label="0">关闭</el-radio>
                        </el-radio-group>
                        <div class="form-tips">状态为关闭时，将不对外提供服务，请谨慎操作</div>
                    </div>
                </el-form-item>
                <el-form-item label="关闭后访问页面" prop="pageStatus">
                    <el-radio-group v-model="formData.pageStatus">
                        <el-radio :label="0">空页面</el-radio>
                        <el-radio :label="1">自定义链接</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="" prop="pageUrl" v-if="formData.pageStatus == 1">
                    <div class="w-80">
                        <el-input v-model="formData.pageUrl" placeholder="请输入完整的url" />
                    </div>
                </el-form-item>
                <el-form-item label="访问链接">
                    <div class="flex-1 min-w-0 break-words">
                        {{ formData.url }}
                        <el-button v-copy="formData.url">复制</el-button>
                    </div>
                </el-form-item>
            </el-form>
        </el-card>
        <footer-btns v-perms="['channel.web_page_setting/setConfig']">
            <el-button type="primary" @click="handelSave">保存</el-button>
        </footer-btns>
    </div>
</template>
<script lang="ts" setup name="h5Config">
import { getH5Config, setH5Config } from '@/api/channel/h5'

const formData = reactive({
    status: 0,
    pageStatus: 0,
    pageUrl: '',
    url: ''
})

const getDetail = async () => {
    const data: any = await getH5Config()
    // 后端 ChannelH5Validate / getConfig 均为 camelCase（pageStatus/pageUrl）
    formData.status = data?.status ?? 0
    formData.pageStatus = data?.pageStatus ?? 0
    formData.pageUrl = data?.pageUrl || ''
    formData.url = data?.url || ''
}

const handelSave = async () => {
    await setH5Config({
        status: formData.status,
        pageStatus: formData.pageStatus,
        pageUrl: formData.pageUrl
    })
    getDetail()
}

getDetail()
</script>
