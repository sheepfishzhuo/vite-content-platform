<template>
  <div class="section-page">
    <div class="container">
      <div class="page-header">
        <h1>{{ sectionInfo.title }}</h1>
        <p>{{ sectionInfo.description || '浏览该分类下的所有文章' }}</p>
      </div>

      <div class="article-list" v-if="articles.length > 0">
        <div
          v-for="item in articles"
          :key="item.id"
          class="article-card"
          @click="goToArticle(item)"
        >
          <div class="article-cover" v-if="item.coverImage">
            <img :src="item.coverImage" :alt="item.title" />
          </div>
          <div class="article-cover placeholder" v-else>
            <el-icon size="48"><Document /></el-icon>
          </div>
          <div class="article-info">
            <h3 class="article-title">{{ item.title }}</h3>
            <p class="article-summary">{{ item.summary }}</p>
            <div class="article-meta">
              <span class="meta-item">
                <el-icon><Calendar /></el-icon>
                {{ formatDate(item.createdTime) }}
              </span>
              <span class="meta-item">
                <el-icon><View /></el-icon>
                {{ item.viewCount || 0 }}
              </span>
            </div>
          </div>
        </div>
      </div>

      <el-empty v-else description="暂无文章" />

      <div class="pagination" v-if="total > pageSize">
        <el-pagination
          v-model:current-page="currentPage"
          :page-size="pageSize"
          :total="total"
          layout="prev, pager, next"
          @current-change="handlePageChange"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getSectionItems } from '@/api/section'
import { formatDate } from '@/utils/date'
import { Document, Calendar, View } from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()

const sectionInfo = ref({
  title: '',
  description: ''
})
const articles = ref([])
const currentPage = ref(1)
const pageSize = ref(12)
const total = ref(0)

function goToArticle(article) {
  router.push(`/article/${article.id}`)
}

async function loadSectionData() {
  const sectionId = route.params.id
  const result = await getSectionItems(sectionId)
  articles.value = result.items || []
  total.value = result.total || articles.value.length
  sectionInfo.value = {
    title: result.title || '文章列表',
    description: result.description || ''
  }
}

function handlePageChange(page) {
  currentPage.value = page
  loadSectionData()
}

watch(() => route.params.id, () => {
  currentPage.value = 1
  loadSectionData()
})

onMounted(() => {
  loadSectionData()
})
</script>

<style scoped>
.section-page {
  min-height: calc(100vh - 180px);
}

.container {
  max-width: 1280px;
  margin: 0 auto;
}

.page-header {
  text-align: center;
  margin-bottom: 40px;
  padding: 40px 0;
}

.page-header h1 {
  font-size: 32px;
  font-weight: 700;
  color: #303133;
  margin: 0 0 12px 0;
}

.page-header p {
  font-size: 16px;
  color: #909399;
  margin: 0;
}

.article-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 24px;
  margin-bottom: 40px;
}

.article-card {
  background: #fff;
  border-radius: 12px;
  overflow: hidden;
  cursor: pointer;
  transition: all 0.3s;
  border: 1px solid #f0f2f5;
}

.article-card:hover {
  transform: translateY(-6px);
  box-shadow: 0 12px 30px rgba(0, 0, 0, 0.1);
  border-color: transparent;
}

.article-cover {
  height: 180px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
  position: relative;
  overflow: hidden;
}

.article-cover img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.article-cover.placeholder {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.article-info {
  padding: 20px;
}

.article-title {
  margin: 0 0 10px 0;
  font-size: 17px;
  font-weight: 600;
  color: #303133;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.article-summary {
  margin: 0 0 14px 0;
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
  justify-content: space-between;
  align-items: center;
  font-size: 13px;
  color: #c0c4cc;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 4px;
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 40px;
}
</style>
