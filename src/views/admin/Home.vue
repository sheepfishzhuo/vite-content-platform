<template>
  <div class="home">
    <div class="welcome-card">
      <div class="welcome-content">
        <div class="welcome-text">
          <h2>欢迎使用电信学院研究生奖学金评定专题网站管理后台</h2>
          <p>这是一个基于 Vue 3 + Spring Boot 构建的现代化管理系统</p>
        </div>
        <div class="welcome-icon">
          <el-icon :size="64"><Platform /></el-icon>
        </div>
      </div>
    </div>

    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <div class="stat-card stat-card-blue">
          <div class="stat-icon">
            <el-icon :size="32"><Document /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.articleCount }}</div>
            <div class="stat-label">文章总数</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card stat-card-green">
          <div class="stat-icon">
            <el-icon :size="32"><Menu /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.menuCount }}</div>
            <div class="stat-label">菜单数量</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card stat-card-orange">
          <div class="stat-icon">
            <el-icon :size="32"><Grid /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.sectionCount }}</div>
            <div class="stat-label">区块数量</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card stat-card-purple">
          <div class="stat-icon">
            <el-icon :size="32"><View /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.viewCount }}</div>
            <div class="stat-label">总浏览量</div>
          </div>
        </div>
      </el-col>
    </el-row>
    
    <el-row :gutter="20" class="section-row">
      <el-col
        v-for="section in sections"
        :key="section.id"
        :span="12"
      >
        <el-card class="section-card" shadow="hover">
          <template #header>
            <div class="section-header">
              <span class="section-title">{{ section.title }}</span>
              <el-button
                type="primary"
                link
                @click="goToSection(section.id)"
              >
                查看更多 →
              </el-button>
            </div>
          </template>
          <div v-if="sectionItems[section.id]" class="section-content">
            <div
              v-for="item in sectionItems[section.id]"
              :key="item.id"
              class="article-item"
              @click="goToArticle(item.id)"
            >
              <div class="article-title">{{ item.title }}</div>
              <div class="article-time">{{ formatDate(item.createdTime) }}</div>
            </div>
          </div>
          <el-empty v-else description="暂无内容" :image-size="80" />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { getHomepageSections, getSectionItems } from '@/api/section'
import { getArticles } from '@/api/article'
import { useMenuStore } from '@/stores/menu'
import { Platform, Document, Menu, Grid, View } from '@element-plus/icons-vue'

const router = useRouter()
const menuStore = useMenuStore()

const sections = ref([])
const sectionItems = reactive({})
const stats = reactive({
  articleCount: 0,
  menuCount: 0,
  sectionCount: 0,
  viewCount: 0
})

function formatDate(dateStr) {
  if (!dateStr) return ''
  let date
  if (Array.isArray(dateStr)) {
    const [year, month, day, hour = 0, minute = 0, second = 0] = dateStr
    date = new Date(year, month - 1, day, hour, minute, second)
  } else {
    date = new Date(dateStr)
  }
  if (isNaN(date.getTime())) return ''
  return date.toLocaleDateString('zh-CN')
}

function goToSection(sectionId) {
  router.push(`/admin/content/articles?sectionId=${sectionId}`)
}

function goToArticle(id) {
  router.push(`/admin/content/publish/${id}`)
}

onMounted(async () => {
  try {
    sections.value = await getHomepageSections()
    
    for (const section of sections.value) {
      const result = await getSectionItems(section.id)
      sectionItems[section.id] = result.items || []
    }
    
    const articleResult = await getArticles({ page: 1, pageSize: 1 })
    stats.articleCount = articleResult.total || 0
    
    stats.menuCount = menuStore.adminMenuTree.length || 0
    stats.sectionCount = sections.value.length || 0
  } catch (error) {
    console.error('Failed to load homepage data:', error)
  }
})
</script>

<style scoped>
.home {
  padding: 0;
}

.welcome-card {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 12px;
  padding: 32px;
  margin-bottom: 24px;
  box-shadow: 0 4px 20px rgba(102, 126, 234, 0.3);
}

.welcome-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.welcome-text h2 {
  margin: 0 0 8px 0;
  color: #fff;
  font-size: 28px;
  font-weight: 600;
}

.welcome-text p {
  margin: 0;
  color: rgba(255, 255, 255, 0.85);
  font-size: 15px;
}

.welcome-icon {
  color: rgba(255, 255, 255, 0.3);
}

.stats-row {
  margin-bottom: 24px;
}

.stat-card {
  display: flex;
  align-items: center;
  padding: 24px;
  border-radius: 12px;
  color: #fff;
  transition: transform 0.3s, box-shadow 0.3s;
}

.stat-card:hover {
  transform: translateY(-4px);
}

.stat-card-blue {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
  box-shadow: 0 4px 15px rgba(79, 172, 254, 0.4);
}

.stat-card-green {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
  box-shadow: 0 4px 15px rgba(67, 233, 123, 0.4);
}

.stat-card-orange {
  background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
  box-shadow: 0 4px 15px rgba(250, 112, 154, 0.4);
}

.stat-card-purple {
  background: linear-gradient(135deg, #a18cd1 0%, #fbc2eb 100%);
  box-shadow: 0 4px 15px rgba(161, 140, 209, 0.4);
}

.stat-icon {
  width: 64px;
  height: 64px;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 16px;
}

.stat-info {
  flex: 1;
}

.stat-value {
  font-size: 32px;
  font-weight: 700;
  line-height: 1.2;
}

.stat-label {
  font-size: 14px;
  opacity: 0.9;
  margin-top: 4px;
}

.section-row {
  margin-top: 0;
}

.section-card {
  margin-bottom: 20px;
  border-radius: 12px;
  border: none;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.section-title {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.section-content {
  min-height: 180px;
}

.article-item {
  padding: 14px 0;
  border-bottom: 1px solid #f0f0f0;
  cursor: pointer;
  display: flex;
  justify-content: space-between;
  align-items: center;
  transition: all 0.3s;
}

.article-item:hover {
  padding-left: 8px;
}

.article-item:hover .article-title {
  color: #667eea;
}

.article-item:last-child {
  border-bottom: none;
}

.article-title {
  color: #303133;
  font-size: 14px;
  transition: color 0.3s;
}

.article-time {
  color: #909399;
  font-size: 12px;
}
</style>
