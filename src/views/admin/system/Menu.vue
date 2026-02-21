<template>
  <div class="menu-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>菜单管理</span>
          <el-button type="primary" @click="handleCreate">
            <el-icon><Plus /></el-icon>
            新增菜单
          </el-button>
        </div>
      </template>
      
      <el-table
        :data="menuTree"
        v-loading="loading"
        row-key="id"
        border
        default-expand-all
      >
        <el-table-column prop="name" label="菜单名称" min-width="150" />
        <el-table-column prop="path" label="路由路径" min-width="150" />
        <el-table-column prop="icon" label="图标" width="120">
          <template #default="{ row }">
            <el-icon v-if="row.icon"><component :is="row.icon" /></el-icon>
          </template>
        </el-table-column>
        <el-table-column prop="component" label="组件路径" min-width="180" show-overflow-tooltip />
        <el-table-column prop="menuType" label="菜单类型" width="100">
          <template #default="{ row }">
            <el-tag :type="row.menuType === 'admin' ? 'primary' : 'success'">
              {{ row.menuType === 'admin' ? '后台' : '前台' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="sortOrder" label="排序" width="80" />
        <el-table-column prop="isHidden" label="是否隐藏" width="100">
          <template #default="{ row }">
            <el-tag :type="row.isHidden ? 'danger' : 'success'">
              {{ row.isHidden ? '隐藏' : '显示' }}
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
      :title="isEdit ? '编辑菜单' : '新增菜单'"
      width="500px"
    >
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="100px"
      >
        <el-form-item label="菜单名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入菜单名称" />
        </el-form-item>
        
        <el-form-item label="路由路径" prop="path">
          <el-input v-model="form.path" placeholder="请输入路由路径" />
        </el-form-item>
        
        <el-form-item label="父级菜单" prop="parentId">
          <el-tree-select
            v-model="form.parentId"
            :data="menuTreeOptions"
            :props="{ label: 'name', value: 'id' }"
            placeholder="请选择父级菜单"
            check-strictly
            clearable
          />
        </el-form-item>
        
        <el-form-item label="菜单类型" prop="menuType">
          <el-radio-group v-model="form.menuType">
            <el-radio value="admin">后台菜单</el-radio>
            <el-radio value="frontend">前台菜单</el-radio>
          </el-radio-group>
        </el-form-item>
        
        <el-form-item label="图标" prop="icon">
          <el-select v-model="form.icon" placeholder="请选择图标" clearable filterable>
            <el-option v-for="icon in iconOptions" :key="icon" :label="icon" :value="icon">
              <div class="icon-option">
                <el-icon><component :is="icon" /></el-icon>
                <span>{{ icon }}</span>
              </div>
            </el-option>
          </el-select>
        </el-form-item>
        
        <el-form-item label="组件路径" prop="component">
          <el-input v-model="form.component" placeholder="请输入组件路径" />
        </el-form-item>
        
        <el-form-item label="排序" prop="sortOrder">
          <el-input-number v-model="form.sortOrder" :min="0" />
        </el-form-item>
        
        <el-form-item label="是否隐藏" prop="isHidden">
          <el-switch v-model="form.isHidden" :active-value="1" :inactive-value="0" />
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
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { 
  Plus, HomeFilled, House, Document, Files, FolderOpened, Folder,
  EditPen, Edit, Delete, Check, Close, Setting, Tools, Menu, Grid, List, Operation,
  User, UserFilled, Avatar, Key, Lock, Bell, BellFilled, Message, ChatDotRound, Notification,
  Calendar, Clock, Timer, AlarmClock, Stopwatch, Location, MapLocation, Compass, Guide, Position,
  Picture, Camera, VideoCamera, Microphone, Headset, Monitor, Cellphone, Platform, OfficeBuilding, School, Shop,
  ShoppingCart, Goods, Box, Wallet, CreditCard, Coin, DataAnalysis, TrendCharts, DataLine, PieChart, Histogram, Odometer,
  Star, StarFilled, Medal, Trophy, Flag, Aim, Search, View, Hide, ZoomIn, ZoomOut, FullScreen,
  Upload, Download, Share, Link, Connection, Promotion, ArrowUp, ArrowDown, ArrowLeft, ArrowRight, Back, Right,
  Top, Bottom, TopLeft, TopRight, BottomLeft, BottomRight, Refresh, RefreshLeft, RefreshRight,
  CircleCheck, CircleClose, CirclePlus, InfoFilled, Warning, WarningFilled, QuestionFilled, SuccessFilled,
  More, MoreFilled, Rank, Sort, Expand, Fold
} from '@element-plus/icons-vue'
import { getMenuTree, createMenu, updateMenu, deleteMenu } from '@/api/menu'

const iconOptions = [
  'HomeFilled', 'House', 'Document', 'Files', 'FolderOpened', 'Folder',
  'EditPen', 'Edit', 'Delete', 'Plus', 'Check', 'Close',
  'Setting', 'Tools', 'Menu', 'Grid', 'List', 'Operation',
  'User', 'UserFilled', 'Avatar', 'Key', 'Lock',
  'Bell', 'BellFilled', 'Message', 'ChatDotRound', 'Notification',
  'Calendar', 'Clock', 'Timer', 'AlarmClock', 'Stopwatch',
  'Location', 'MapLocation', 'Compass', 'Guide', 'Position',
  'Picture', 'Camera', 'VideoCamera', 'Microphone', 'Headset',
  'Monitor', 'Cellphone', 'Platform', 'OfficeBuilding', 'School', 'Shop',
  'ShoppingCart', 'Goods', 'Box', 'Wallet', 'CreditCard', 'Coin',
  'DataAnalysis', 'TrendCharts', 'DataLine', 'PieChart', 'Histogram', 'Odometer',
  'Star', 'StarFilled', 'Medal', 'Trophy', 'Flag', 'Aim',
  'Search', 'View', 'Hide', 'ZoomIn', 'ZoomOut', 'FullScreen',
  'Upload', 'Download', 'Share', 'Link', 'Connection', 'Promotion',
  'ArrowUp', 'ArrowDown', 'ArrowLeft', 'ArrowRight', 'Back', 'Right',
  'Top', 'Bottom', 'TopLeft', 'TopRight', 'BottomLeft', 'BottomRight',
  'Refresh', 'RefreshLeft', 'RefreshRight', 'CircleCheck', 'CircleClose', 'CirclePlus',
  'InfoFilled', 'Warning', 'WarningFilled', 'QuestionFilled', 'SuccessFilled',
  'More', 'MoreFilled', 'Rank', 'Sort', 'Expand', 'Fold'
]

const loading = ref(false)
const submitting = ref(false)
const dialogVisible = ref(false)
const formRef = ref()
const menuTree = ref([])

const isEdit = ref(false)
const editId = ref(null)

const form = reactive({
  name: '',
  path: '',
  parentId: 0,
  icon: '',
  component: '',
  sortOrder: 0,
  isHidden: 0,
  menuType: 'admin'
})

const rules = {
  name: [{ required: true, message: '请输入菜单名称', trigger: 'blur' }],
  path: [{ required: true, message: '请输入路由路径', trigger: 'blur' }],
  menuType: [{ required: true, message: '请选择菜单类型', trigger: 'change' }]
}

const menuTreeOptions = computed(() => {
  function transform(menus) {
    return menus.map(menu => ({
      id: menu.id,
      name: menu.name,
      children: menu.children ? transform(menu.children) : []
    }))
  }
  return [{ id: 0, name: '顶级菜单', children: transform(menuTree.value) }]
})

async function fetchMenuTree() {
  loading.value = true
  try {
    menuTree.value = await getMenuTree()
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
    parentId: 0,
    icon: '',
    component: '',
    sortOrder: 0,
    isHidden: 0,
    menuType: 'admin'
  })
  dialogVisible.value = true
}

function handleEdit(row) {
  isEdit.value = true
  editId.value = row.id
  Object.assign(form, {
    name: row.name,
    path: row.path,
    parentId: row.parentId || 0,
    icon: row.icon,
    component: row.component,
    sortOrder: row.sortOrder,
    isHidden: row.isHidden,
    menuType: row.menuType || 'admin'
  })
  dialogVisible.value = true
}

async function handleDelete(row) {
  await ElMessageBox.confirm('确定要删除该菜单吗？', '提示', {
    type: 'warning'
  })
  await deleteMenu(row.id)
  ElMessage.success('删除成功')
  fetchMenuTree()
}

async function handleSubmit() {
  await formRef.value.validate()
  submitting.value = true
  try {
    if (isEdit.value) {
      await updateMenu(editId.value, form)
      ElMessage.success('更新成功')
    } else {
      await createMenu(form)
      ElMessage.success('创建成功')
    }
    dialogVisible.value = false
    fetchMenuTree()
  } finally {
    submitting.value = false
  }
}

onMounted(() => {
  fetchMenuTree()
})
</script>

<style scoped>
.menu-management {
  padding: 0;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.icon-option {
  display: flex;
  align-items: center;
  gap: 8px;
}
</style>
