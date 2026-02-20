<template>
  <div class="announcement-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>公告管理</span>
          <el-button type="primary" @click="handleCreate">
            <el-icon><Plus /></el-icon>
            新增公告
          </el-button>
        </div>
      </template>
      
      <el-table :data="announcements" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="title" label="公告标题" min-width="200" />
        <el-table-column prop="type" label="类型" width="100">
          <template #default="{ row }">
            <el-tag :color="getTypeColor(row.type)" size="small" style="color: #fff; border: none;">
              {{ getTypeLabel(row.type) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="isTop" label="置顶" width="80">
          <template #default="{ row }">
            <el-tag v-if="row.isTop" type="danger" size="small">置顶</el-tag>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="isEnabled" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.isEnabled ? 'success' : 'danger'">
              {{ row.isEnabled ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdTime" label="创建时间" width="180">
          <template #default="{ row }">
            {{ formatDate(row.createdTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" link @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑公告' : '新增公告'"
      width="1200px"
    >
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="100px"
      >
        <el-form-item label="公告标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入公告标题" />
        </el-form-item>
        
        <el-form-item label="公告内容" prop="content">
          <RichEditor v-model="form.content" :height="250" placeholder="请输入公告内容" />
        </el-form-item>
        
        <el-form-item label="公告类型" prop="type">
          <el-radio-group v-model="form.type">
            <el-radio-button value="notice" style="--el-radio-button-checked-bg-color: #409eff; --el-radio-button-checked-border-color: #409eff;">公告</el-radio-button>
            <el-radio-button value="update" style="--el-radio-button-checked-bg-color: #67c23a; --el-radio-button-checked-border-color: #67c23a;">更新</el-radio-button>
            <el-radio-button value="warning" style="--el-radio-button-checked-bg-color: #e6a23c; --el-radio-button-checked-border-color: #e6a23c;">警告</el-radio-button>
            <el-radio-button value="important" style="--el-radio-button-checked-bg-color: #f56c6c; --el-radio-button-checked-border-color: #f56c6c;">重要</el-radio-button>
            <el-radio-button value="activity" style="--el-radio-button-checked-bg-color: #9b59b6; --el-radio-button-checked-border-color: #9b59b6;">活动</el-radio-button>
            <el-radio-button value="maintenance" style="--el-radio-button-checked-bg-color: #909399; --el-radio-button-checked-border-color: #909399;">维护</el-radio-button>
            <el-radio-button value="security" style="--el-radio-button-checked-bg-color: #e74c3c; --el-radio-button-checked-border-color: #e74c3c;">安全</el-radio-button>
          </el-radio-group>
        </el-form-item>
        
        <el-form-item label="是否置顶" prop="isTop">
          <el-switch v-model="form.isTop" :active-value="1" :inactive-value="0" />
        </el-form-item>
        
        <el-form-item label="是否启用" prop="isEnabled">
          <el-switch v-model="form.isEnabled" :active-value="1" :inactive-value="0" />
        </el-form-item>
        
        <el-form-item label="排序" prop="sortOrder">
          <el-input-number v-model="form.sortOrder" :min="0" />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitting">
          确定
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { getAllAnnouncements, createAnnouncement, updateAnnouncement, deleteAnnouncement } from '@/api/announcement'
import RichEditor from '@/components/RichEditor.vue'

const loading = ref(false)
const submitting = ref(false)
const dialogVisible = ref(false)
const formRef = ref()
const announcements = ref([])

const isEdit = ref(false)
const editId = ref(null)

const form = reactive({
  title: '',
  content: '',
  type: 'notice',
  isTop: 0,
  isEnabled: 1,
  sortOrder: 0
})

const rules = {
  title: [{ required: true, message: '请输入公告标题', trigger: 'blur' }]
}

const typeMap = {
  notice: { label: '公告', color: '#409eff' },
  update: { label: '更新', color: '#67c23a' },
  warning: { label: '警告', color: '#e6a23c' },
  important: { label: '重要', color: '#f56c6c' },
  activity: { label: '活动', color: '#9b59b6' },
  maintenance: { label: '维护', color: '#909399' },
  security: { label: '安全', color: '#e74c3c' }
}

function formatDate(date) {
  if (!date) return '-'
  if (Array.isArray(date)) {
    const [year, month, day, hour = 0, minute = 0, second = 0] = date
    return `${year}-${String(month).padStart(2, '0')}-${String(day).padStart(2, '0')} ${String(hour).padStart(2, '0')}:${String(minute).padStart(2, '0')}:${String(second).padStart(2, '0')}`
  }
  return new Date(date).toLocaleString('zh-CN')
}

function getTypeLabel(type) {
  return typeMap[type]?.label || '公告'
}

function getTypeColor(type) {
  return typeMap[type]?.color || '#409eff'
}

async function fetchAnnouncements() {
  loading.value = true
  try {
    announcements.value = await getAllAnnouncements()
  } finally {
    loading.value = false
  }
}

function handleCreate() {
  isEdit.value = false
  editId.value = null
  Object.assign(form, {
    title: '',
    content: '',
    type: 'notice',
    isTop: 0,
    isEnabled: 1,
    sortOrder: 0
  })
  dialogVisible.value = true
}

function handleEdit(row) {
  isEdit.value = true
  editId.value = row.id
  Object.assign(form, {
    title: row.title,
    content: row.content,
    type: row.type || 'notice',
    isTop: row.isTop || 0,
    isEnabled: row.isEnabled,
    sortOrder: row.sortOrder || 0
  })
  dialogVisible.value = true
}

async function handleDelete(row) {
  await ElMessageBox.confirm('确定要删除该公告吗？', '提示', {
    type: 'warning'
  })
  await deleteAnnouncement(row.id)
  ElMessage.success('删除成功')
  fetchAnnouncements()
}

async function handleSubmit() {
  await formRef.value.validate()
  submitting.value = true
  try {
    if (isEdit.value) {
      await updateAnnouncement(editId.value, form)
      ElMessage.success('更新成功')
    } else {
      await createAnnouncement(form)
      ElMessage.success('创建成功')
    }
    dialogVisible.value = false
    fetchAnnouncements()
  } finally {
    submitting.value = false
  }
}

onMounted(() => {
  fetchAnnouncements()
})
</script>

<style scoped>
.announcement-management {
  padding: 0;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
