<template>
  <div class="section-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>区块管理</span>
          <el-button type="primary" @click="handleCreate">
            <el-icon><Plus /></el-icon>
            新增区块
          </el-button>
        </div>
      </template>
      
      <el-table :data="sections" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="title" label="区块标题" min-width="150" />
        <el-table-column prop="description" label="描述" min-width="200" />
        <el-table-column prop="displayStyle" label="展示风格" width="120">
          <template #default="{ row }">
            <el-tag :type="getStyleType(row.displayStyle)">
              {{ getStyleLabel(row.displayStyle) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="sortOrder" label="排序" width="80" />
        <el-table-column prop="isEnabled" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.isEnabled ? 'success' : 'danger'">
              {{ row.isEnabled ? '启用' : '禁用' }}
            </el-tag>
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
      :title="isEdit ? '编辑区块' : '新增区块'"
      width="500px"
    >
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="100px"
      >
        <el-form-item label="区块标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入区块标题" />
        </el-form-item>
        
        <el-form-item label="描述" prop="description">
          <el-input
            v-model="form.description"
            type="textarea"
            :rows="3"
            placeholder="请输入区块描述"
          />
        </el-form-item>
        
        <el-form-item label="展示风格" prop="displayStyle">
          <el-radio-group v-model="form.displayStyle">
            <el-radio-button value="card">
              <el-icon><Grid /></el-icon> 卡片式
            </el-radio-button>
            <el-radio-button value="list">
              <el-icon><List /></el-icon> 列表式
            </el-radio-button>
            <el-radio-button value="compact">
              <el-icon><Menu /></el-icon> 紧凑式
            </el-radio-button>
          </el-radio-group>
        </el-form-item>
        
        <el-form-item label="排序" prop="sortOrder">
          <el-input-number v-model="form.sortOrder" :min="0" />
        </el-form-item>
        
        <el-form-item label="是否启用" prop="isEnabled">
          <el-switch v-model="form.isEnabled" :active-value="1" :inactive-value="0" />
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
import { Plus, Grid, List, Menu } from '@element-plus/icons-vue'
import { getAllSections, createSection, updateSection, deleteSection } from '@/api/section'

const loading = ref(false)
const submitting = ref(false)
const dialogVisible = ref(false)
const formRef = ref()
const sections = ref([])

const isEdit = ref(false)
const editId = ref(null)

const form = reactive({
  title: '',
  description: '',
  displayStyle: 'card',
  sortOrder: 0,
  isEnabled: 1
})

const rules = {
  title: [{ required: true, message: '请输入区块标题', trigger: 'blur' }]
}

const displayStyles = {
  card: { label: '卡片式', type: 'primary' },
  list: { label: '列表式', type: 'success' },
  compact: { label: '紧凑式', type: 'warning' }
}

function getStyleLabel(style) {
  return displayStyles[style]?.label || '卡片式'
}

function getStyleType(style) {
  return displayStyles[style]?.type || 'primary'
}

async function fetchSections() {
  loading.value = true
  try {
    sections.value = await getAllSections()
  } finally {
    loading.value = false
  }
}

function handleCreate() {
  isEdit.value = false
  editId.value = null
  Object.assign(form, {
    title: '',
    description: '',
    displayStyle: 'card',
    sortOrder: 0,
    isEnabled: 1
  })
  dialogVisible.value = true
}

function handleEdit(row) {
  isEdit.value = true
  editId.value = row.id
  Object.assign(form, {
    title: row.title,
    description: row.description,
    displayStyle: row.displayStyle || 'card',
    sortOrder: row.sortOrder,
    isEnabled: row.isEnabled
  })
  dialogVisible.value = true
}

async function handleDelete(row) {
  await ElMessageBox.confirm('确定要删除该区块吗？', '提示', {
    type: 'warning'
  })
  await deleteSection(row.id)
  ElMessage.success('删除成功')
  fetchSections()
}

async function handleSubmit() {
  await formRef.value.validate()
  submitting.value = true
  try {
    if (isEdit.value) {
      await updateSection(editId.value, form)
      ElMessage.success('更新成功')
    } else {
      await createSection(form)
      ElMessage.success('创建成功')
    }
    dialogVisible.value = false
    fetchSections()
  } finally {
    submitting.value = false
  }
}

onMounted(() => {
  fetchSections()
})
</script>

<style scoped>
.section-management {
  padding: 0;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
