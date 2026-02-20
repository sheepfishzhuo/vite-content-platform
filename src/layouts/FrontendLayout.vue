<template>
  <div class="frontend-layout">
    <div class="top-bar">
      <div class="top-bar-content">
        <span class="page-top">{{ dateInfo }}</span>
        <span class="page-top">{{ weekInfo }}</span>
        <span class="page-top">{{ greeting }}</span>
      </div>
    </div>
    
    <div class="banner">
      <div class="banner-bg"></div>
      <div class="banner-content">
        <div class="banner-left">
          <div class="banner-logo">
            <img src="/xh.png" alt="logo" style="width: 80px;" />
          </div>
          <h1>辽宁工程技术大学</h1>
        </div>
        <div class="banner-right">
          <h2>电子与信息工程学院</h2>
          <h3>2026年度奖学金评定专题</h3>
        </div>
      </div>
    </div>
    
    <el-header class="header">
      <div class="header-content">
        <el-menu
          :default-active="activeMenu"
          mode="horizontal"
          :ellipsis="false"
          class="nav-menu"
        >
          <el-menu-item index="/" @click="router.push('/')">
            <el-icon><HomeFilled /></el-icon>
            首页
          </el-menu-item>
          <el-menu-item 
            v-for="section in sections" 
            :key="section.id" 
            :index="`/section/${section.id}`"
            @click="router.push(`/section/${section.id}`)"
          >
            <el-icon><FolderOpened /></el-icon>
            {{ section.title }}
          </el-menu-item>
        </el-menu>
      </div>
    </el-header>
    <el-main class="main">
      <router-view />
    </el-main>
    <el-footer class="footer">
      <div class="footer-content">
        <div class="footer-links">
          <a href="#">关于我们</a>
          <a href="#">联系方式</a>
          <a href="#">隐私政策</a>
          <a href="#">使用条款</a>
        </div>
        <p>© 2026年度奖学金评定专题网站</p>
      </div>
    </el-footer>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getHomepageSections } from '@/api/section'
import { Platform, HomeFilled, FolderOpened } from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()
const sections = ref([])

const activeMenu = computed(() => route.path)

const greeting = computed(() => {
  const hour = new Date().getHours()
  if (hour >= 5 && hour < 9) return '早上好！'
  if (hour >= 9 && hour < 11) return '上午好！'
  if (hour >= 11 && hour < 14) return '中午好！'
  if (hour >= 14 && hour < 18) return '下午好！'
  return '晚上好！' 
})

const dateInfo = computed(() => {
  const now = new Date()
  const year = now.getFullYear()
  const month = String(now.getMonth() + 1).padStart(2, '0')
  const day = String(now.getDate()).padStart(2, '0')
  return `${year}年${month}月${day}日`
})

const weekInfo = computed(() => {
  const weeks = ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六']
  return weeks[new Date().getDay()]
})

onMounted(async () => {
  sections.value = await getHomepageSections()
})
</script>

<style scoped>
.frontend-layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background: linear-gradient(180deg, #f8fafc 0%, #e2e8f0 100%);
}

.top-bar {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 8px 24px;
}

.top-bar-content {
  max-width: 1280px;
  margin: 0 auto;
  display: flex;
  justify-content: flex-start;
  align-items: center;
  gap: 12px;
  color: rgba(255, 255, 255, 0.9);
  font-size: 13px;
}

.top-bar-content .page-top {
  font-weight: 600;
  font-size: 14px;
}

.banner {
  background: linear-gradient(180deg, #fffbd9 0%, #f5e6b8 100%);
  padding: 32px 24px;
  position: relative;
  overflow: hidden;
}

.banner-bg {
  position: absolute;
  inset: 0;
  background: url('bj.jpg') center/cover;
  opacity: 0.15;
}

.banner-content {
  max-width: 1280px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  gap: 20px;
  position: relative;
  z-index: 1;
}

.banner-left {
  display: flex;
  align-items: center;
  gap: 20px;
}

.banner-logo {
  width: 80px;
  height: 80px;

}

.banner-left h1 {
  margin: 0;
  font-size: 38px;
  font-weight: 700;
  color: #1a1f36;
  letter-spacing: 2px;
}

.banner-right {
  text-align: left;
}

.banner-right h2 {
  margin: 0 0 8px 0;
  font-size: 20px;
  font-weight: 700;
  color: #1a1f36;
}

.banner-right h3 {
  margin: 0;
  font-size: 25px;
  font-weight: 700;
  color: #1a1f36;
}

.header {
  background: rgba(255, 255, 255, 0.98);
  padding: 0;
  height: 56px;
  position: sticky;
  top: 0;
  z-index: 100;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
}

.header-content {
  max-width: 1280px;
  margin: 0 auto;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0 24px;
}

.nav-menu {
  border-bottom: none;
  background-color: transparent;
  --el-menu-bg-color: transparent;
  --el-menu-hover-bg-color: rgba(102, 126, 234, 0.08);
  --el-menu-text-color: #303133;
  --el-menu-active-color: #667eea;
}

.nav-menu :deep(.el-menu-item) {
  font-size: 15px;
  font-weight: 500;
  border-bottom: 3px solid transparent;
  transition: all 0.3s;
  color: #303133;
  padding: 0 24px;
  display: flex;
  align-items: center;
  gap: 6px;
}

.nav-menu :deep(.el-menu-item .el-icon) {
  font-size: 18px;
}

.nav-menu :deep(.el-menu-item:hover) {
  color: #667eea;
  background-color: rgba(102, 126, 234, 0.08);
}

.nav-menu :deep(.el-menu-item.is-active) {
  color: #667eea;
  border-bottom-color: #667eea;
  background-color: rgba(102, 126, 234, 0.05);
}

.main {
  flex: 1;
  padding: 24px;
}

.footer {
  background: linear-gradient(180deg, #1a1f36 0%, #252b48 100%);
  color: rgba(255, 255, 255, 0.7);
  padding: 40px 24px 24px;
  height: auto;
}

.footer-content {
  max-width: 1280px;
  margin: 0 auto;
  text-align: center;
}

.footer-links {
  display: flex;
  justify-content: center;
  gap: 32px;
  margin-bottom: 20px;
}

.footer-links a {
  color: rgba(255, 255, 255, 0.7);
  text-decoration: none;
  font-size: 14px;
  transition: color 0.3s;
}

.footer-links a:hover {
  color: #fff;
}

.footer p {
  margin: 0;
  font-size: 13px;
  opacity: 0.6;
}
</style>
