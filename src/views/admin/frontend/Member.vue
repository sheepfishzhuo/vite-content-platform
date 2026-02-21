<template>
  <div class="member-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>前台用户管理</span>
          <el-input
            v-model="keyword"
            placeholder="搜索学号/姓名/邮箱/手机号"
            style="width: 250px"
            clearable
            @clear="fetchMembers"
            @keyup.enter="fetchMembers"
          >
            <template #append>
              <el-button @click="fetchMembers">搜索</el-button>
            </template>
          </el-input>
        </div>
      </template>
      
      <el-table :data="members" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="studentId" label="学号" min-width="100" />
        <el-table-column prop="realName" label="姓名" min-width="80" />
        <el-table-column prop="grade" label="年级" min-width="70" />
        <el-table-column prop="graduateType" label="研究生类别" min-width="100">
          <template #default="{ row }">
            <el-tag :type="row.graduateType === '学术型' ? 'primary' : 'success'" size="small">
              {{ row.graduateType || '-' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="major" label="专业" min-width="70">
          <template #default="{ row }">
            <el-tag type="info" size="small">{{ row.major || '-' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="email" label="邮箱" min-width="150" />
        <el-table-column prop="phone" label="手机号" min-width="110" />
        <el-table-column prop="status" label="状态" min-width="70">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'" size="small">
              {{ row.status === 1 ? '正常' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdTime" label="注册时间" min-width="140">
          <template #default="{ row }">
            {{ formatDate(row.createdTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleEdit(row)">编辑</el-button>
            <el-button 
              :type="row.status === 1 ? 'warning' : 'success'" 
              link 
              @click="handleToggleStatus(row)"
            >
              {{ row.status === 1 ? '禁用' : '启用' }}
            </el-button>
            <el-button type="danger" link @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        v-model:current-page="queryParams.page"
        v-model:page-size="queryParams.pageSize"
        :total="total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        class="pagination"
        @size-change="fetchMembers"
        @current-change="fetchMembers"
      />
    </el-card>

    <el-dialog
      v-model="dialogVisible"
      title="编辑用户信息"
      width="550px"
    >
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="100px"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="学号">
              <el-input v-model="form.studentId" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="姓名" prop="realName">
              <el-input v-model="form.realName" placeholder="请输入姓名" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="年级" prop="grade">
              <el-select v-model="form.grade" placeholder="请选择年级" style="width: 100%">
                <el-option v-for="year in gradeOptions" :key="year" :label="year" :value="year" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="研究生类别" prop="graduateType">
              <el-select v-model="form.graduateType" placeholder="请选择类别" style="width: 100%">
                <el-option label="学术型" value="学术型" />
                <el-option label="专业型" value="专业型" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="专业" prop="major">
              <el-select v-model="form.major" placeholder="请选择专业" style="width: 100%">
                <el-option label="通信" value="通信" />
                <el-option label="计算机" value="计算机" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-radio-group v-model="form.status">
                <el-radio :value="1">正常</el-radio>
                <el-radio :value="0">禁用</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入手机号" />
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
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getMemberList, updateMemberStatus, deleteMember, getMemberDetail, updateMember } from '@/api/member'

const loading = ref(false)
const submitting = ref(false)
const dialogVisible = ref(false)
const formRef = ref()
const members = ref([])
const total = ref(0)
const keyword = ref('')

const queryParams = reactive({
  page: 1,
  pageSize: 10
})

const currentYear = new Date().getFullYear()
const gradeOptions = computed(() => {
  const years = []
  for (let i = 0; i < 5; i++) {
    years.push(String(currentYear - i))
  }
  return years
})

const editId = ref(null)

const form = reactive({
  studentId: '',
  realName: '',
  grade: '',
  graduateType: '',
  major: '',
  email: '',
  phone: '',
  status: 1
})

const rules = {
  realName: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  email: [{ type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }],
  phone: [{ pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }]
}

function formatDate(dateStr) {
  if (!dateStr) return '-'
  const date = new Date(dateStr)
  if (isNaN(date.getTime())) return '-'
  return date.toLocaleString('zh-CN')
}

async function fetchMembers() {
  loading.value = true
  try {
    const params = { ...queryParams }
    if (keyword.value.trim()) {
      params.keyword = keyword.value.trim()
    }
    const result = await getMemberList(params)
    console.log('API result:', result)
    members.value = result.records || []
    total.value = result.total || 0
  } finally {
    loading.value = false
  }
}

async function handleEdit(row) {
  editId.value = row.id
  const detail = await getMemberDetail(row.id)
  Object.assign(form, {
    studentId: detail.studentId,
    realName: detail.realName,
    grade: detail.grade,
    graduateType: detail.graduateType,
    major: detail.major,
    email: detail.email,
    phone: detail.phone,
    status: detail.status
  })
  dialogVisible.value = true
}

async function handleToggleStatus(row) {
  const newStatus = row.status === 1 ? 0 : 1
  const action = newStatus === 0 ? '禁用' : '启用'
  await ElMessageBox.confirm(`确定要${action}该用户吗？`, '提示', { type: 'warning' })
  await updateMemberStatus(row.id, newStatus)
  ElMessage.success(`${action}成功`)
  fetchMembers()
}

async function handleDelete(row) {
  await ElMessageBox.confirm('确定要删除该用户吗？', '提示', { type: 'warning' })
  await deleteMember(row.id)
  ElMessage.success('删除成功')
  fetchMembers()
}

async function handleSubmit() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  
  submitting.value = true
  try {
    await updateMember(editId.value, form)
    ElMessage.success('更新成功')
    dialogVisible.value = false
    fetchMembers()
  } finally {
    submitting.value = false
  }
}

onMounted(() => {
  fetchMembers()
})
</script>

<style scoped>
.member-management {
  padding: 0;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.pagination {
  margin-top: 20px;
  justify-content: flex-end;
}
</style>
