<template>
  <div class="register-page">
    <div class="register-card">
      <div class="register-header">
        <h2>用户注册</h2>
      </div>
      
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        class="register-form"
        label-position="top"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item prop="studentId" label="学号">
              <el-input
                v-model="form.studentId"
                placeholder="请输入学号"
                size="large"
                :prefix-icon="Postcard"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="realName" label="姓名">
              <el-input
                v-model="form.realName"
                placeholder="请输入姓名"
                size="large"
                :prefix-icon="User"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item prop="grade" label="年级">
              <el-select
                v-model="form.grade"
                placeholder="请选择年级"
                size="large"
                style="width: 100%"
              >
                <el-option
                  v-for="year in gradeOptions"
                  :key="year"
                  :label="year"
                  :value="year"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item prop="graduateType" label="研究生类别">
              <el-select
                v-model="form.graduateType"
                placeholder="请选择研究生类别"
                size="large"
                style="width: 100%"
              >
                <el-option label="学术型" value="学术型" />
                <el-option label="专业型" value="专业型" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item prop="major" label="专业">
              <el-select
                v-model="form.major"
                placeholder="请选择专业"
                size="large"
                style="width: 100%"
              >
                <el-option label="通信" value="通信" />
                <el-option label="计算机" value="计算机" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item prop="phone" label="手机号">
              <el-input
                v-model="form.phone"
                placeholder="请输入手机号"
                size="large"
                :prefix-icon="Phone"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="email" label="邮箱">
              <el-input
                v-model="form.email"
                placeholder="请输入邮箱"
                size="large"
                :prefix-icon="Message"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item prop="password" label="密码">
              <el-input
                v-model="form.password"
                type="password"
                placeholder="请输入密码"
                size="large"
                :prefix-icon="Lock"
                show-password
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="confirmPassword" label="确认密码">
              <el-input
                v-model="form.confirmPassword"
                type="password"
                placeholder="请再次输入密码"
                size="large"
                :prefix-icon="Lock"
                show-password
              />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item>
          <el-button
            type="primary"
            size="large"
            class="register-btn"
            :loading="loading"
            @click="handleRegister"
          >
            注 册
          </el-button>
        </el-form-item>

        <div class="login-link">
          已有账号？<router-link to="/">返回首页</router-link>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { UserFilled, User, Lock, Postcard, Message, Phone } from '@element-plus/icons-vue'
import { register } from '@/api/member'

const router = useRouter()

const formRef = ref()
const loading = ref(false)

const currentYear = new Date().getFullYear()
const gradeOptions = computed(() => {
  const years = []
  for (let i = 0; i < 5; i++) {
    years.push(String(currentYear - i))
  }
  return years
})

const form = reactive({
  studentId: '',
  realName: '',
  grade: '',
  graduateType: '',
  major: '',
  email: '',
  phone: '',
  password: '',
  confirmPassword: ''
})

const validatePass = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请再次输入密码'))
  } else if (value !== form.password) {
    callback(new Error('两次输入密码不一致'))
  } else {
    callback()
  }
}

const rules = {
  studentId: [
    { required: true, message: '请输入学号', trigger: 'blur' }
  ],
  realName: [
    { required: true, message: '请输入姓名', trigger: 'blur' }
  ],
  grade: [
    { required: true, message: '请选择年级', trigger: 'change' }
  ],
  graduateType: [
    { required: true, message: '请选择研究生类别', trigger: 'change' }
  ],
  major: [
    { required: true, message: '请选择专业', trigger: 'change' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请再次输入密码', trigger: 'blur' },
    { validator: validatePass, trigger: 'blur' }
  ],
  email: [
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ],
  phone: [
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ]
}

async function handleRegister() {
  await formRef.value.validate()
  loading.value = true
  try {
    const data = await register(form)
    ElMessage.success('注册成功')
    router.push('/')
  } catch (error) {
    ElMessage.error(error.message || '注册失败')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.register-page {
  padding: 40px 20px;
  min-height: calc(100vh - 280px);
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f5f7fa;
}

.register-card {
  width: 100%;
  max-width: 900px;
  background: #fff;
  border-radius: 16px;
  padding: 50px 60px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.register-header {
  text-align: center;
  margin-bottom: 20px;
}

.register-header h2 {
  margin: 0;
  font-size: 22px;
  color: #303133;
  font-weight: 600;
}

.register-form {
  margin-top: 20px;
}

.register-form :deep(.el-form-item__label) {
  font-weight: 500;
  color: #606266;
}

.register-form :deep(.el-input__wrapper) {
  border-radius: 8px;
}

.register-form :deep(.el-select .el-input__wrapper) {
  border-radius: 8px;
}

.register-btn {
  width: 100%;
  border-radius: 8px;
  font-size: 16px;
  height: 44px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.4);
  transition: all 0.3s;
  margin-top: 10px;
}

.register-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(102, 126, 234, 0.5);
}

.login-link {
  text-align: center;
  margin-top: 16px;
  color: #909399;
  font-size: 14px;
}

.login-link a {
  color: #667eea;
  text-decoration: none;
  font-weight: 500;
}

.login-link a:hover {
  text-decoration: underline;
}
</style>
