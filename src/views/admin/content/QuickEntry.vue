<template>
  <div class="quick-entry-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>快速入口管理</span>
          <el-button type="primary" @click="handleCreate">
            <el-icon><Plus /></el-icon>
            新增入口
          </el-button>
        </div>
      </template>
      
      <el-table :data="entries" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="入口名称" min-width="120" />
        <el-table-column prop="path" label="跳转路径" min-width="180" />
        <el-table-column prop="icon" label="图标" width="200">
          <template #default="{ row }">
            <div class="icon-cell">
              <el-icon size="18"><component :is="row.icon" /></el-icon>
              <span>{{ row.icon }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="color" label="颜色" width="150">
          <template #default="{ row }">
            <div class="color-preview" :style="{ background: row.color }"></div>
          </template>
        </el-table-column>
        <el-table-column prop="backgroundImage" label="背景图" width="100">
          <template #default="{ row }">
            <div v-if="row.backgroundImage" class="bg-preview" :style="{ backgroundImage: `url(${row.backgroundImage})` }"></div>
            <span v-else style="color: #c0c4cc;">无</span>
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
      :title="isEdit ? '编辑入口' : '新增入口'"
      width="500px"
    >
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="100px"
      >
        <el-form-item label="入口名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入入口名称" />
        </el-form-item>
        
        <el-form-item label="跳转路径" prop="path">
          <el-input v-model="form.path" placeholder="请输入跳转路径，如 /articles" />
        </el-form-item>
        
        <el-form-item label="图标" prop="icon">
          <el-select v-model="form.icon" placeholder="请选择图标">
            <el-option v-for="icon in iconOptions" :key="icon" :label="icon" :value="icon">
              <el-icon><component :is="icon" /></el-icon>
              <span style="margin-left: 8px;">{{ icon }}</span>
            </el-option>
          </el-select>
        </el-form-item>
        
        <el-form-item label="背景颜色" prop="color">
          <el-select v-model="form.color" placeholder="请选择颜色">
            <el-option v-for="c in colorOptions" :key="c.value" :label="c.label" :value="c.value">
              <div class="color-option">
                <div class="color-preview" :style="{ background: c.value }"></div>
                <span>{{ c.label }}</span>
              </div>
            </el-option>
          </el-select>
        </el-form-item>
        
        <el-form-item label="背景图片" prop="backgroundImage">
          <el-input v-model="form.backgroundImage" placeholder="请输入背景图片URL（可选）" />
          <div class="form-tip">推荐使用 16:9 比例的图片，如 Unsplash 图片</div>
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
import { 
  Plus, Document, EditPen, DataAnalysis, User, Setting, FolderOpened, Calendar, Bell, Grid,
  HomeFilled, House, OfficeBuilding, School, Shop, ShoppingCart, Goods, Box, Files, Notebook,
  Reading, Collection, Ticket, TrendCharts, DataLine, PieChart, Histogram, Odometer, Timer, Clock,
  AlarmClock, Stopwatch, Message, ChatDotRound, Promotion, Notification, ChatLineRound, Star, StarFilled,
  Key, Lock, Unlock, Link, Connection, Share, Upload, Download, Picture, Camera, VideoCamera,
  Microphone, Headset, Monitor, Cellphone, Location, MapLocation, Guide, Compass, Position, Flag,
  Medal, Trophy, Aim, View, Hide, Search, ZoomIn, ZoomOut, FullScreen, Expand, Fold,
  CircleCheck, CircleClose, CirclePlus, Check, Close, Minus, Delete, Edit, Refresh,
  RefreshLeft, RefreshRight, ArrowUp, ArrowDown, ArrowLeft, ArrowRight, Back, Right, Top, Bottom,
  TopLeft, TopRight, BottomLeft, BottomRight, Rank, Sort, Menu, Operation, More, MoreFilled, Tools, SetUp
} from '@element-plus/icons-vue'
import { getAllQuickEntries, createQuickEntry, updateQuickEntry, deleteQuickEntry } from '@/api/quickEntry'

const loading = ref(false)
const submitting = ref(false)
const dialogVisible = ref(false)
const formRef = ref()
const entries = ref([])

const isEdit = ref(false)
const editId = ref(null)

const form = reactive({
  name: '',
  path: '',
  icon: 'Document',
  color: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
  backgroundImage: '',
  sortOrder: 0,
  isEnabled: 1
})

const rules = {
  name: [{ required: true, message: '请输入入口名称', trigger: 'blur' }],
  path: [{ required: true, message: '请输入跳转路径', trigger: 'blur' }]
}

const iconOptions = [
  'Document', 'EditPen', 'DataAnalysis', 'User', 'Setting', 'FolderOpened', 
  'Calendar', 'Bell', 'Grid', 'Plus', 'HomeFilled', 'House', 'OfficeBuilding',
  'School', 'Shop', 'ShoppingCart', 'Goods', 'Box', 'Files', 'Notebook',
  'Reading', 'Collection', 'Ticket', 'TrendCharts', 'DataLine', 'PieChart',
  'Histogram', 'Odometer', 'Timer', 'Clock', 'AlarmClock', 'Stopwatch',
  'Message', 'ChatDotRound', 'Promotion', 'Notification', 'ChatLineRound',
  'Star', 'StarFilled', 'Key', 'Lock', 'Unlock', 'Link', 'Connection',
  'Share', 'Upload', 'Download', 'Picture', 'Camera', 'VideoCamera',
  'Microphone', 'Headset', 'Monitor', 'Cellphone', 'Location', 'MapLocation',
  'Guide', 'Compass', 'Position', 'Flag', 'Medal', 'Trophy', 'Aim', 'View',
  'Hide', 'Search', 'ZoomIn', 'ZoomOut', 'FullScreen', 'Expand', 'Fold',
  'CircleCheck', 'CircleClose', 'CirclePlus', 'Check', 'Close',
  'Minus', 'Delete', 'Edit', 'Refresh', 'RefreshLeft', 'RefreshRight',
  'ArrowUp', 'ArrowDown', 'ArrowLeft', 'ArrowRight', 'Back', 'Right', 'Top',
  'Bottom', 'TopLeft', 'TopRight', 'BottomLeft', 'BottomRight', 'Rank',
  'Sort', 'Menu', 'Operation', 'More', 'MoreFilled', 'Tools', 'SetUp'
]

const colorOptions = [
  { label: '紫蓝渐变', value: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)' },
  { label: '粉红渐变', value: 'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)' },
  { label: '蓝青渐变', value: 'linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)' },
  { label: '绿青渐变', value: 'linear-gradient(135deg, #43e97b 0%, #38f9d7 100%)' },
  { label: '橙黄渐变', value: 'linear-gradient(135deg, #fa709a 0%, #fee140 100%)' },
  { label: '深蓝渐变', value: 'linear-gradient(135deg, #4481eb 0%, #04befe 100%)' },
  { label: '紫粉渐变', value: 'linear-gradient(135deg, #a18cd1 0%, #fbc2eb 100%)' },
  { label: '橙红渐变', value: 'linear-gradient(135deg, #ff9a9e 0%, #fecfef 100%)' },
  { label: '蓝紫渐变', value: 'linear-gradient(135deg, #a1c4fd 0%, #c2e9fb 100%)' },
  { label: '青绿渐变', value: 'linear-gradient(135deg, #89f7fe 0%, #66a6ff 100%)' },
  { label: '金橙渐变', value: 'linear-gradient(135deg, #f6d365 0%, #fda085 100%)' },
  { label: '薄荷渐变', value: 'linear-gradient(135deg, #96fbc4 0%, #f9f586 100%)' },
  { label: '玫瑰渐变', value: 'linear-gradient(135deg, #ffecd2 0%, #fcb69f 100%)' },
  { label: '天空渐变', value: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)' },
  { label: '珊瑚渐变', value: 'linear-gradient(135deg, #ff9966 0%, #ff5e62 100%)' },
  { label: '森林渐变', value: 'linear-gradient(135deg, #11998e 0%, #38ef7d 100%)' },
  { label: '午夜渐变', value: 'linear-gradient(135deg, #232526 0%, #414345 100%)' },
  { label: '日落渐变', value: 'linear-gradient(135deg, #ff512f 0%, #f09819 100%)' },
  { label: '海洋渐变', value: 'linear-gradient(135deg, #2193b0 0%, #6dd5ed 100%)' },
  { label: '葡萄渐变', value: 'linear-gradient(135deg, #360033 0%, #0b8793 100%)' }
]

async function fetchEntries() {
  loading.value = true
  try {
    entries.value = await getAllQuickEntries()
  } finally {
    loading.value = false
  }
}

function handleCreate() {
  isEdit.value = false
  editId.value = null
  Object.assign(form, {
    name: '',
    path: '',
    icon: 'Document',
    color: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
    backgroundImage: '',
    sortOrder: 0,
    isEnabled: 1
  })
  dialogVisible.value = true
}

function handleEdit(row) {
  isEdit.value = true
  editId.value = row.id
  Object.assign(form, {
    name: row.name,
    path: row.path,
    icon: row.icon || 'Document',
    color: row.color,
    backgroundImage: row.backgroundImage || '',
    sortOrder: row.sortOrder || 0,
    isEnabled: row.isEnabled
  })
  dialogVisible.value = true
}

async function handleDelete(row) {
  await ElMessageBox.confirm('确定要删除该入口吗？', '提示', {
    type: 'warning'
  })
  await deleteQuickEntry(row.id)
  ElMessage.success('删除成功')
  fetchEntries()
}

async function handleSubmit() {
  await formRef.value.validate()
  submitting.value = true
  try {
    if (isEdit.value) {
      await updateQuickEntry(editId.value, form)
      ElMessage.success('更新成功')
    } else {
      await createQuickEntry(form)
      ElMessage.success('创建成功')
    }
    dialogVisible.value = false
    fetchEntries()
  } finally {
    submitting.value = false
  }
}

onMounted(() => {
  fetchEntries()
})
</script>

<style scoped>
.quick-entry-management {
  padding: 0;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.color-preview {
  width: 60px;
  height: 20px;
  border-radius: 4px;
}

.color-option {
  display: flex;
  align-items: center;
  gap: 10px;
}

.color-option .color-preview {
  width: 40px;
  height: 20px;
}

.bg-preview {
  width: 60px;
  height: 40px;
  border-radius: 4px;
  background-size: cover;
  background-position: center;
}

.form-tip {
  font-size: 12px;
  color: #909399;
  margin-top: 4px;
}

.icon-cell {
  display: flex;
  align-items: center;
  gap: 8px;
}
</style>
