<template>
  <div class="article-edit">
    <el-card class="edit-card">
      <template #header>
        <div class="card-header">
          <span class="title">{{ isEdit ? '编辑文章' : '发布文章' }}</span>
        </div>
      </template>
      
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="100px"
        class="edit-form"
      >
        <div class="form-section">
          <div class="section-title">基本信息</div>
          <el-row :gutter="24">
            <el-col :span="12">
              <el-form-item label="标题" prop="title">
                <el-input v-model="form.title" placeholder="请输入文章标题" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="所属区块" prop="sectionId">
                <el-select v-model="form.sectionId" placeholder="请选择所属区块" style="width: 100%">
                  <el-option
                    v-for="section in sections"
                    :key="section.id"
                    :label="section.title"
                    :value="section.id"
                  />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          
          <el-form-item label="摘要" prop="summary">
            <el-input
              v-model="form.summary"
              type="textarea"
              :rows="3"
              placeholder="请输入文章摘要"
            />
          </el-form-item>
          
          <el-form-item label="封面图" prop="coverImage">
            <el-input v-model="form.coverImage" placeholder="请输入封面图URL" />
          </el-form-item>
        </div>
        
        <div class="form-section">
          <div class="section-title">文章内容</div>
          <el-form-item label="内容" prop="content" label-width="100px">
            <RichEditor v-model="form.content" :height="450" placeholder="请输入文章内容" />
          </el-form-item>
        </div>
        
        <div class="form-section">
          <div class="section-title">发布设置</div>
          <el-form-item label="状态" prop="status">
            <el-radio-group v-model="form.status">
              <el-radio :value="0">
                <el-icon><Document /></el-icon>
                草稿
              </el-radio>
              <el-radio :value="1">
                <el-icon><Promotion /></el-icon>
                发布
              </el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="是否置顶" prop="isTop">
            <el-switch v-model="form.isTop" :active-value="1" :inactive-value="0" />
          </el-form-item>
        </div>
        
        <div class="form-actions">
          <el-button @click="handleCancel">
            <el-icon><Close /></el-icon>
            取消
          </el-button>
          <el-button type="primary" @click="handleSubmit" :loading="submitting">
            <el-icon><Check /></el-icon>
            {{ isEdit ? '保存' : '发布' }}
          </el-button>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Document, Promotion, Check, Close } from '@element-plus/icons-vue'
import { getArticle, createArticle, updateArticle } from '@/api/article'
import { getHomepageSections } from '@/api/section'
import RichEditor from '@/components/RichEditor.vue'

const router = useRouter()
const route = useRoute()

const formRef = ref()
const submitting = ref(false)
const sections = ref([])

const isEdit = computed(() => !!route.params.id)

const form = reactive({
  title: '',
  summary: '',
  content: '',
  coverImage: '',
  sectionId: null,
  status: 1,
  isTop: 0
})

const rules = {
  title: [{ required: true, message: '请输入文章标题', trigger: 'blur' }],
  sectionId: [{ required: true, message: '请选择所属区块', trigger: 'change' }],
  content: [{ required: true, message: '请输入文章内容', trigger: 'blur' }]
}

async function fetchSections() {
  sections.value = await getHomepageSections()
}

async function fetchArticle() {
  if (!route.params.id) return
  try {
    const data = await getArticle(route.params.id)
    Object.assign(form, data)
  } catch (error) {
    ElMessage.error('获取文章详情失败')
  }
}

async function handleSubmit() {
  await formRef.value.validate()
  submitting.value = true
  try {
    if (isEdit.value) {
      await updateArticle(route.params.id, form)
      ElMessage.success('保存成功')
    } else {
      await createArticle(form)
      ElMessage.success('发布成功')
    }
    router.push('/admin/content/articles')
  } finally {
    submitting.value = false
  }
}

function handleCancel() {
  router.back()
}

onMounted(() => {
  fetchSections()
  fetchArticle()
})
</script>

<style scoped>
.article-edit {
  padding: 0;
}

.edit-card {
  border-radius: 16px;
  box-shadow: 0 4px 24px rgba(0, 0, 0, 0.06);
  border: none;
}

.edit-card :deep(.el-card__header) {
  border-bottom: 1px solid #f0f2f5;
  padding: 20px 32px;
}

.card-header {
  display: flex;
  align-items: center;
}

.card-header .title {
  font-size: 18px;
  font-weight: 600;
  color: #303133;
}

.edit-card :deep(.el-card__body) {
  padding: 32px 40px;
}

.edit-form {
  max-width: 100%;
}

.form-section {
  margin-bottom: 32px;
  padding-bottom: 24px;
  border-bottom: 1px solid #f0f2f5;
}

.form-section:last-of-type {
  border-bottom: none;
  margin-bottom: 0;
  padding-bottom: 0;
}

.section-title {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 20px;
  padding-left: 12px;
  border-left: 3px solid #667eea;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  padding-top: 24px;
  margin-top: 16px;
  border-top: 1px solid #f0f2f5;
}

.form-actions .el-button {
  padding: 12px 28px;
  border-radius: 8px;
  font-weight: 500;
}

.form-actions .el-button--primary {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  box-shadow: 0 4px 16px rgba(102, 126, 234, 0.3);
}

.form-actions .el-button--primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(102, 126, 234, 0.4);
}

:deep(.el-input__wrapper),
:deep(.el-textarea__inner),
:deep(.el-select__wrapper) {
  border-radius: 8px;
}

:deep(.el-form-item__label) {
  font-weight: 500;
  color: #606266;
}

:deep(.el-radio) {
  margin-right: 24px;
}

:deep(.el-radio__label) {
  display: flex;
  align-items: center;
  gap: 6px;
}
</style>
