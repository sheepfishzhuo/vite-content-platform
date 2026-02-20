<template>
  <div class="article-list-page">
    <div class="container">
      <div class="page-header">
        <div class="header-content">
          <h1>{{ menuName || '全部文章' }}</h1>
          <p>共 {{ total }} 篇文章</p>
        </div>
        <div class="header-decoration"></div>
      </div>

      <div class="article-list" v-if="articles.length > 0">
        <div
          v-for="article in articles"
          :key="article.id"
          class="article-item"
          @click="goToArticle(article)"
        >
          <div class="article-cover" v-if="article.coverImage">
            <img :src="article.coverImage" :alt="article.title" />
          </div>
          <div class="article-cover placeholder" v-else>
            <el-icon size="40"><Document /></el-icon>
          </div>
          <div class="article-info">
            <h2 class="article-title">{{ article.title }}</h2>
            <p class="article-summary">{{ article.summary }}</p>
            <div class="article-meta">
              <span class="meta-item">
                <el-icon><Calendar /></el-icon>
                {{ formatDate(article.createdTime) }}
              </span>
              <span class="meta-item">
                <el-icon><View /></el-icon>
                {{ article.viewCount || 0 }} 阅读
              </span>
            </div>
          </div>
          <div class="article-arrow">
            <el-icon><ArrowRight /></el-icon>
          </div>
        </div>
      </div>

      <el-empty v-else description="暂无文章">
        <el-button type="primary" @click="router.push('/')">返回首页</el-button>
      </el-empty>

      <div class="pagination" v-if="total > pageSize">
        <el-pagination
          v-model:current-page="currentPage"
          :page-size="pageSize"
          :total="total"
          layout="prev, pager, next"
          background
          @current-change="handlePageChange"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getArticles } from '@/api/article'
import { useMenuStore } from '@/stores/menu'
import { formatDate } from '@/utils/date'
import { Document, Calendar, View, ArrowRight } from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()
const menuStore = useMenuStore()

const articles = ref([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)
const menuName = ref('')

function goToArticle(article) {
  router.push(`/article/${article.id}`)
}

async function fetchArticles() {
  const menuId = route.params.menuId
  const result = await getArticles({
    page: currentPage.value,
    pageSize: pageSize.value,
    menuId: menuId ? Number(menuId) : null
  })
  articles.value = result.records || []
  total.value = result.total || 0
}

function handlePageChange(page) {
  currentPage.value = page
  fetchArticles()
  window.scrollTo({ top: 0, behavior: 'smooth' })
}

function findMenuName(menus, menuId) {
  for (const menu of menus) {
    if (menu.id === Number(menuId)) return menu.name
    if (menu.children) {
      const found = findMenuName(menu.children, menuId)
      if (found) return found
    }
  }
  return null
}

watch(() => route.params.menuId, () => {
  currentPage.value = 1
  fetchArticles()
  const menuId = route.params.menuId
  if (menuId && menuStore.frontendMenuTree.length > 0) {
    menuName.value = findMenuName(menuStore.frontendMenuTree, menuId) || ''
  }
})

onMounted(() => {
  fetchArticles()
  const menuId = route.params.menuId
  if (menuId) {
    menuName.value = findMenuName(menuStore.frontendMenuTree, menuId) || ''
  }
})
</script>

<style scoped>
.article-list-page {
  min-height: calc(100vh - 180px);
}

.container {
  max-width: 900px;
  margin: 0 auto;
}

.page-header {
  background: linear-gradient(135deg, #8f000b 0%, #4a4a6a 100%);
  border-radius: 16px;
  padding: 40px 32px;
  margin-bottom: 32px;
  position: relative;
  overflow: hidden;
  color: #fff;
}

.header-content {
  position: relative;
  z-index: 1;
}

.header-content h1 {
  margin: 0 0 8px 0;
  font-size: 28px;
  font-weight: 700;
}

.header-content p {
  margin: 0;
  font-size: 15px;
  opacity: 0.85;
}

.header-decoration {
  position: absolute;
  right: -50px;
  top: -50px;
  width: 200px;
  height: 200px;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 50%;
}

.article-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.article-item {
  display: flex;
  gap: 24px;
  background: #fff;
  border-radius: 16px;
  padding: 24px;
  cursor: pointer;
  transition: all 0.3s;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
  align-items: center;
}

.article-item:hover {
  transform: translateX(8px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.08);
}

.article-cover {
  width: 180px;
  height: 120px;
  border-radius: 12px;
  overflow: hidden;
  flex-shrink: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #8f000b 0%, #4a4a6a 100%);
  color: #fff;
}

.article-cover img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.article-info {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.article-title {
  margin: 0 0 10px 0;
  font-size: 18px;
  font-weight: 600;
  color: #303133;
}

.article-summary {
  margin: 0 0 auto 0;
  font-size: 14px;
  color: #909399;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  line-height: 1.6;
}

.article-meta {
  display: flex;
  gap: 20px;
  margin-top: 12px;
  font-size: 13px;
  color: #c0c4cc;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 4px;
}

.article-arrow {
  color: #c0c4cc;
  font-size: 20px;
  transition: all 0.3s;
}

.article-item:hover .article-arrow {
  color: #667eea;
  transform: translateX(4px);
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 40px;
  padding: 24px;
  background: #fff;
  border-radius: 16px;
}
</style>
