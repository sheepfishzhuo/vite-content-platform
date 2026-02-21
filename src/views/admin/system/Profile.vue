<template>
  <div class="profile-page">
    <el-row :gutter="20">
      <el-col :span="8">
        <el-card class="profile-card">
          <template #header>
            <div class="card-header">
              <span>个人信息</span>
            </div>
          </template>
          <div class="avatar-section">
            <el-avatar :size="100" class="avatar">
              <el-icon :size="50"><UserFilled /></el-icon>
            </el-avatar>
            <h3 class="username">{{ userStore.user?.username }}</h3>
            <el-tag :type="userStore.user?.role === 'admin' ? 'danger' : 'primary'">
              {{ userStore.user?.role === 'admin' ? '管理员' : '普通用户' }}
            </el-tag>
          </div>
          <el-descriptions :column="1" border class="info-list">
            <el-descriptions-item label="用户名">
              {{ userStore.user?.username }}
            </el-descriptions-item>
            <el-descriptions-item label="邮箱">
              {{ userStore.user?.email || '未设置' }}
            </el-descriptions-item>
            <el-descriptions-item label="角色">
              {{ userStore.user?.role === 'admin' ? '管理员' : '普通用户' }}
            </el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-col>
      
      <el-col :span="16">
        <el-card class="settings-card">
          <template #header>
            <div class="card-header">
              <span>修改密码</span>
            </div>
          </template>
          <el-form
            ref="formRef"
            :model="passwordForm"
            :rules="rules"
            label-width="100px"
            class="password-form"
          >
            <el-form-item label="当前密码" prop="oldPassword">
              <el-input
                v-model="passwordForm.oldPassword"
                type="password"
                placeholder="请输入当前密码"
                show-password
              />
            </el-form-item>
            <el-form-item label="新密码" prop="newPassword">
              <el-input
                v-model="passwordForm.newPassword"
                type="password"
                placeholder="请输入新密码"
                show-password
              />
            </el-form-item>
            <el-form-item label="确认密码" prop="confirmPassword">
              <el-input
                v-model="passwordForm.confirmPassword"
                type="password"
                placeholder="请再次输入新密码"
                show-password
              />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleChangePassword" :loading="loading">
                修改密码
              </el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { UserFilled } from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()
const formRef = ref()
const loading = ref(false)

const passwordForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const validateConfirm = (rule, value, callback) => {
  if (value !== passwordForm.newPassword) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

const rules = {
  oldPassword: [
    { required: true, message: '请输入当前密码', trigger: 'blur' }
  ],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认新密码', trigger: 'blur' },
    { validator: validateConfirm, trigger: 'blur' }
  ]
}

async function handleChangePassword() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  
  loading.value = true
  try {
    ElMessage.success('密码修改成功，请重新登录')
    userStore.logout()
    window.location.href = '/login'
  } catch (error) {
    ElMessage.error(error.message || '修改失败')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.profile-page {
  padding: 0;
}

.profile-card,
.settings-card {
  height: 100%;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.avatar-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px 0;
}

.avatar {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  margin-bottom: 16px;
}

.username {
  margin: 0 0 8px 0;
  font-size: 18px;
  color: #303133;
}

.info-list {
  margin-top: 20px;
}

.password-form {
  max-width: 400px;
  padding: 20px 0;
}
</style>
