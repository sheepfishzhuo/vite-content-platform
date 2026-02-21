<template>
  <div class="article-detail-page">
    <div class="container">
      <div class="article-hero">
        <div class="hero-overlay"></div>
        <div class="hero-content">
          <div class="breadcrumb">
            <span @click="router.push('/')">首页</span>
            <el-icon><ArrowRight /></el-icon>
            <span @click="router.push('/list')">文章列表</span>
            <el-icon><ArrowRight /></el-icon>
            <span class="current">{{ article.title }}</span>
          </div>
          <h1>{{ article.title }}</h1>
          <div class="article-meta">
            <div class="meta-item">
              <el-icon><Calendar /></el-icon>
              <span>{{ formatDate(article.createdTime) }}</span>
            </div>
            <div class="meta-item">
              <el-icon><View /></el-icon>
              <span>{{ article.viewCount || 0 }} 阅读</span>
            </div>
            <div class="meta-item" v-if="article.menuName">
              <el-icon><FolderOpened /></el-icon>
              <span>{{ article.menuName }}</span>
            </div>
          </div>
        </div>
      </div>

      <div class="article-body">
        <div class="article-summary" v-if="article.summary">
          <div class="summary-icon">
            <el-icon><InfoFilled /></el-icon>
          </div>
          <div class="summary-content">
            <h3>摘要</h3>
            <p>{{ article.summary }}</p>
          </div>
        </div>

        <div class="article-cover" v-if="article.coverImage">
          <img :src="article.coverImage" :alt="article.title" />
        </div>

        <div class="article-content" v-html="article.content"></div>

        <div class="article-tags" v-if="article.tags && article.tags.length > 0">
          <el-tag
            v-for="tag in article.tags"
            :key="tag"
            effect="plain"
            class="tag-item"
          >
            {{ tag }}
          </el-tag>
        </div>
      </div>

      <div class="article-footer">
        <div class="footer-nav">
          <el-button 
            class="nav-btn prev" 
            v-if="prevArticle" 
            @click="goToArticle(prevArticle.id)"
          >
            <el-icon><ArrowLeft /></el-icon>
            <div class="nav-content">
              <span class="nav-label">上一篇</span>
              <span class="nav-title">{{ prevArticle.title }}</span>
            </div>
          </el-button>
          <div class="nav-placeholder" v-else></div>
          <el-button 
            class="nav-btn next" 
            v-if="nextArticle" 
            @click="goToArticle(nextArticle.id)"
          >
            <div class="nav-content">
              <span class="nav-label">下一篇</span>
              <span class="nav-title">{{ nextArticle.title }}</span>
            </div>
            <el-icon><ArrowRight /></el-icon>
          </el-button>
          <div class="nav-placeholder" v-else></div>
        </div>

        <div class="footer-actions">
          <el-button class="action-btn" @click="router.push('/list')">
            <el-icon><List /></el-icon>
            文章列表
          </el-button>
          <el-button type="primary" class="action-btn primary" @click="router.push('/')">
            <el-icon><HomeFilled /></el-icon>
            返回首页
          </el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getArticle, getArticles } from '@/api/article'
import { formatDate } from '@/utils/date'
import { ArrowRight, Calendar, View, FolderOpened, InfoFilled, ArrowLeft, List, HomeFilled } from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()

const article = ref({})
const prevArticle = ref(null)
const nextArticle = ref(null)

function goToArticle(id) {
  router.push(`/article/${id}`)
}

async function loadArticle(id) {
  const result = await getArticle(id)
  article.value = result

  const listResult = await getArticles({ page: 1, pageSize: 100 })
  const articles = listResult.records || []
  const currentIndex = articles.findIndex(a => a.id === Number(id))
  
  prevArticle.value = currentIndex > 0 ? articles[currentIndex - 1] : null
  nextArticle.value = currentIndex < articles.length - 1 ? articles[currentIndex + 1] : null
}

watch(() => route.params.id, (newId) => {
  if (newId) {
    loadArticle(newId)
    window.scrollTo({ top: 0, behavior: 'smooth' })
  }
})

onMounted(() => {
  loadArticle(route.params.id)
})
</script>

<style scoped>
.article-detail-page {
  min-height: calc(100vh - 180px);
}

.container {
  max-width: 1500px;
  margin: 0 auto;
}

.article-hero {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 16px;
  padding: 32px 40px;
  margin-bottom: 24px;
  position: relative;
  overflow: hidden;
  color: #fff;
}

.hero-overlay {
  position: absolute;
  inset: 0;
  background: url("data:image/svg+xml,%3Csvg width='60' height='60' viewBox='0 0 60 60' xmlns='http://www.w3.org/2000/svg'%3E%3Cg fill='none' fill-rule='evenodd'%3E%3Cg fill='%23ffffff' fill-opacity='0.05'%3E%3Cpath d='M36 34v-4h-2v4h-4v2h4v4h2v-4h4v-2h-4zm0-30V0h-2v4h-4v2h4v4h2V6h4V4h-4zM6 34v-4H4v4H0v2h4v4h2v-4h4v-2H6zM6 4V0H4v4H0v2h4v4h2V6h4V4H6z'/%3E%3C/g%3E%3C/g%3E%3C/svg%3E");
}

.hero-content {
  position: relative;
  z-index: 1;
}

.breadcrumb {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 13px;
  margin-bottom: 16px;
  opacity: 0.85;
}

.breadcrumb span {
  cursor: pointer;
  transition: opacity 0.3s;
}

.breadcrumb span:hover:not(.current) {
  opacity: 0.7;
}

.breadcrumb .current {
  cursor: default;
  max-width: 200px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.breadcrumb .el-icon {
  font-size: 12px;
  opacity: 0.6;
}

.article-hero h1 {
  margin: 0 0 16px 0;
  font-size: 26px;
  font-weight: 700;
  line-height: 1.4;
  text-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.article-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 13px;
  background: rgba(255, 255, 255, 0.15);
  padding: 6px 12px;
  border-radius: 16px;
  backdrop-filter: blur(10px);
}

.article-body {
  background: #fff;
  border-radius: 16px;
  padding: 64px 96px;
  margin-bottom: 24px;
  box-shadow: 0 4px 24px rgba(0, 0, 0, 0.06);
}

.article-summary {
  display: flex;
  gap: 12px;
  background: linear-gradient(135deg, rgba(102, 126, 234, 0.06) 0%, rgba(118, 75, 162, 0.06) 100%);
  border-radius: 12px;
  padding: 16px 20px;
  margin-bottom: 24px;
}

.summary-icon {
  width: 36px;
  height: 36px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 18px;
  flex-shrink: 0;
}

.summary-content h3 {
  margin: 0 0 4px 0;
  font-size: 14px;
  font-weight: 600;
  color: #303133;
}

.summary-content p {
  margin: 0;
  font-size: 14px;
  color: #606266;
  line-height: 1.6;
}

.article-cover {
  border-radius: 16px;
  overflow: hidden;
  margin-bottom: 32px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
}

.article-cover img {
  width: 100%;
  display: block;
}

.article-content {
  font-size: 16px;
  color: #303133;
  line-height: 1.9;
}

.article-content :deep(h1),
.article-content :deep(h2),
.article-content :deep(h3),
.article-content :deep(h4),
.article-content :deep(h5),
.article-content :deep(h6) {
  margin: 32px 0 16px 0;
  font-weight: 600;
  color: #303133;
  position: relative;
  padding-left: 16px;
}

.article-content :deep(h1)::before,
.article-content :deep(h2)::before,
.article-content :deep(h3)::before {
  content: '';
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 4px;
  height: 70%;
  background: linear-gradient(180deg, #667eea 0%, #764ba2 100%);
  border-radius: 2px;
}

.article-content :deep(h1) { font-size: 26px; }
.article-content :deep(h2) { font-size: 22px; }
.article-content :deep(h3) { font-size: 20px; }
.article-content :deep(h4) { font-size: 18px; }

.article-content :deep(p) {
  margin: 0 0 20px 0;
}

.article-content :deep(img) {
  max-width: 100%;
  border-radius: 12px;
  margin: 24px 0;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
}

.article-content :deep(pre) {
  background: #1a1f36;
  color: #e2e8f0;
  padding: 20px;
  border-radius: 12px;
  overflow-x: auto;
  margin: 24px 0;
  font-family: 'Fira Code', 'Monaco', monospace;
  font-size: 14px;
  line-height: 1.6;
}

.article-content :deep(code) {
  background: linear-gradient(135deg, rgba(102, 126, 234, 0.1) 0%, rgba(118, 75, 162, 0.1) 100%);
  padding: 3px 8px;
  border-radius: 6px;
  font-size: 14px;
  color: #764ba2;
  font-family: 'Fira Code', 'Monaco', monospace;
}

.article-content :deep(pre code) {
  background: transparent;
  padding: 0;
  color: inherit;
}

.article-content :deep(blockquote) {
  border-left: 4px solid #667eea;
  background: linear-gradient(135deg, rgba(102, 126, 234, 0.05) 0%, rgba(118, 75, 162, 0.05) 100%);
  padding: 16px 20px;
  margin: 24px 0;
  border-radius: 0 12px 12px 0;
  color: #606266;
  font-style: italic;
}

.article-content :deep(ul),
.article-content :deep(ol) {
  padding-left: 24px;
  margin: 20px 0;
}

.article-content :deep(li) {
  margin-bottom: 10px;
  line-height: 1.8;
}

.article-content :deep(a) {
  color: #667eea;
  text-decoration: none;
  border-bottom: 1px dashed #667eea;
  transition: all 0.3s;
}

.article-content :deep(a:hover) {
  color: #764ba2;
  border-bottom-color: #764ba2;
}

.article-content :deep(table) {
  width: 100%;
  border-collapse: collapse;
  margin: 24px 0;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
}

.article-content :deep(th),
.article-content :deep(td) {
  border: 1px solid #ebeef5;
  padding: 14px 16px;
  text-align: left;
}

.article-content :deep(th) {
  background: linear-gradient(135deg, rgba(102, 126, 234, 0.08) 0%, rgba(118, 75, 162, 0.08) 100%);
  font-weight: 600;
  color: #303133;
}

.article-content :deep(tr:hover td) {
  background: #fafbfc;
}

.article-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  margin-top: 32px;
  padding-top: 24px;
  border-top: 1px solid #f0f2f5;
}

.tag-item {
  border-radius: 16px;
  padding: 6px 14px;
  border-color: #667eea;
  color: #667eea;
}

.article-footer {
  background: #fff;
  border-radius: 16px;
  padding: 24px;
  box-shadow: 0 4px 24px rgba(0, 0, 0, 0.06);
}

.footer-nav {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 12px;
  margin-bottom: 16px;
}

.nav-placeholder {
  height: 48px;
}

.nav-btn {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 12px 16px;
  background: #fafbfc;
  border: 1px solid #ebeef5;
  border-radius: 10px;
  height: auto;
  width: 100%;
  transition: all 0.3s;
  justify-content: flex-start;
}

.nav-btn:hover {
  background: linear-gradient(135deg, rgba(102, 126, 234, 0.08) 0%, rgba(118, 75, 162, 0.08) 100%);
  border-color: #667eea;
  transform: translateY(-2px);
}

.nav-btn.next {
  justify-content: flex-end;
}

.nav-btn.next .nav-content {
  text-align: right;
  align-items: flex-end;
}

.nav-btn .el-icon {
  font-size: 18px;
  color: #667eea;
}

.nav-content {
  display: flex;
  flex-direction: column;
  gap: 2px;
  text-align: left;
  align-items: flex-start;
}

.nav-label {
  font-size: 11px;
  color: #909399;
}

.nav-title {
  font-size: 13px;
  font-weight: 500;
  color: #303133;
  max-width: 200px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.footer-actions {
  display: flex;
  justify-content: center;
  gap: 12px;
  padding-top: 16px;
  border-top: 1px solid #f0f2f5;
}

.action-btn {
  border-radius: 10px;
  padding: 10px 20px;
  font-weight: 500;
  font-size: 14px;
}

.action-btn.primary {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  box-shadow: 0 4px 16px rgba(102, 126, 234, 0.3);
}

.action-btn.primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(102, 126, 234, 0.4);
}
</style>
