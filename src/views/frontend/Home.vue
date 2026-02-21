<template>
  <div class="frontend-home">
    <div class="container">
      <div class="top-section">
        <div class="announcement-panel">
          <div class="panel-header">
            <el-icon><Bell /></el-icon>
            <span>公告通知</span>
          </div>
          <div class="announcement-list">
            <div 
              v-for="item in announcements" 
              :key="item.id" 
              class="announcement-item"
              @click="showAnnouncement(item)"
            >
              <el-tag 
                :color="getAnnouncementType(item.type)" 
                size="small"
                style="color: #fff; border: none;"
              >
                {{ getAnnouncementLabel(item.type) }}
              </el-tag>
              <span class="announcement-title">{{ item.title }}</span>
              <span class="announcement-date">{{ formatDate(item.createdTime) }}</span>
            </div>
            <el-empty v-if="announcements.length === 0" description="暂无公告" :image-size="60" />
          </div>
        </div>
        
        <div class="quick-entry-panel">
          <div class="panel-header">
            <el-icon><Grid /></el-icon>
            <span>快速入口</span>
          </div>
          <div class="quick-entry-grid">
            <div 
              v-for="entry in quickEntries" 
              :key="entry.id" 
              class="entry-card"
              @click="router.push(entry.path)"
            >
              <div 
                class="entry-bg" 
                :style="{ backgroundImage: entry.backgroundImage ? `url(${entry.backgroundImage})` : entry.color }"
              >
                <div class="entry-overlay"></div>
                <div class="entry-content">
                  <el-icon class="entry-icon"><component :is="entry.icon" /></el-icon>
                  <span class="entry-name">{{ entry.name }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="sections-grid" ref="contentRef" v-if="sections.length > 0">
        <div
          v-for="section in sections"
          :key="section.id"
          class="section-block"
        >
          <div class="section-header">
            <div class="section-title">
              <el-icon class="section-icon"><FolderOpened /></el-icon>
              <h2>{{ section.title }}</h2>
            </div>
            <el-button type="primary" link class="more-btn" @click="viewMore(section)">
              查看更多
              <el-icon><ArrowRight /></el-icon>
            </el-button>
          </div>
          
          <div :class="['article-grid', `style-${section.displayStyle || 'card'}`]">
            <div
              v-for="item in sectionItems[section.id]"
              :key="item.id"
              class="article-card"
              @click="goToArticle(item)"
            >
              <template v-if="section.displayStyle === 'list'">
                <div class="list-item">
                  <div class="list-index">{{ sectionItems[section.id].indexOf(item) + 1 }}</div>
                  <div class="list-content">
                    <h3 class="article-title">
                      <el-tag v-if="item.isTop" type="danger" size="small" class="top-tag">置顶</el-tag>
                      {{ item.title }}
                    </h3>
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
              </template>
              
              <template v-else-if="section.displayStyle === 'compact'">
                <div class="compact-item">
                  <div class="compact-dot"></div>
                  <el-tag v-if="item.isTop" type="danger" size="small" class="top-tag-compact">置顶</el-tag>
                  <h3 class="article-title">{{ item.title }}</h3>
                  <span class="compact-date">{{ formatDate(item.createdTime) }}</span>
                </div>
              </template>
              
              <template v-else>
                <div class="article-cover" v-if="item.coverImage">
                  <img :src="item.coverImage" :alt="item.title" />
                </div>
                <div class="article-cover placeholder" v-else>
                  <el-icon size="48"><Document /></el-icon>
                </div>
                <div class="article-info">
                  <h3 class="article-title">
                    <el-tag v-if="item.isTop" type="danger" size="small" class="top-tag">置顶</el-tag>
                    {{ item.title }}
                  </h3>
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
              </template>
            </div>
          </div>
        </div>
      </div>

      <div class="cta-section" v-else>
        <el-empty description="暂无内容，请先在后台添加区块">
          <el-button type="primary" @click="router.push('/admin/system/sections')">
            前往区块管理
          </el-button>
        </el-empty>
      </div>
    </div>

    <el-dialog
      v-model="announcementDialogVisible"
      :title="currentAnnouncement?.title"
      width="800px"
    >
      <div class="announcement-detail">
        <div class="detail-meta">
          <el-tag :color="getAnnouncementType(currentAnnouncement?.type)" size="small" style="color: #fff; border: none;">
            {{ getAnnouncementLabel(currentAnnouncement?.type) }}
          </el-tag>
          <span class="detail-date">{{ formatDate(currentAnnouncement?.createdTime) }}</span>
        </div>
        <div class="detail-content" v-html="currentAnnouncement?.content || '暂无内容'"></div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getHomepageSections, getSectionItems } from '@/api/section'
import { getArticles } from '@/api/article'
import { getAnnouncements } from '@/api/announcement'
import { getQuickEntries } from '@/api/quickEntry'
import { formatDate } from '@/utils/date'
import { 
  Bell, Grid, FolderOpened, ArrowRight, Document, Calendar, View,
  Reading, EditPen, DataAnalysis, User
} from '@element-plus/icons-vue'

const router = useRouter()
const contentRef = ref()

const sections = ref([])
const sectionItems = reactive({})
const announcements = ref([])
const quickEntries = ref([])
const announcementDialogVisible = ref(false)
const currentAnnouncement = ref(null)

const announcementTypes = {
  notice: { label: '公告', color: '#409eff' },
  update: { label: '更新', color: '#67c23a' },
  warning: { label: '警告', color: '#e6a23c' },
  important: { label: '重要', color: '#f56c6c' },
  activity: { label: '活动', color: '#9b59b6' },
  maintenance: { label: '维护', color: '#909399' },
  security: { label: '安全', color: '#e74c3c' }
}

function getAnnouncementType(type) {
  return announcementTypes[type]?.color || '#409eff'
}

function getAnnouncementLabel(type) {
  return announcementTypes[type]?.label || '通知'
}

function showAnnouncement(item) {
  currentAnnouncement.value = item
  announcementDialogVisible.value = true
}

function viewMore(section) {
  router.push(`/section/${section.id}`)
}

function goToArticle(article) {
  router.push(`/article/${article.id}`)
}

onMounted(async () => {
  try {
    sections.value = await getHomepageSections()
    
    for (const section of sections.value) {
      const result = await getSectionItems(section.id)
      sectionItems[section.id] = (result.items || []).slice(0, 6)
    }
    
    announcements.value = await getAnnouncements()
    quickEntries.value = await getQuickEntries()
  } catch (error) {
    console.error('Failed to load homepage data:', error)
  }
})
</script>

<style scoped>
.frontend-home {
  min-height: calc(100vh - 180px);
}

.container {
  max-width: 1280px;
  margin: 0 auto;
}

.top-section {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 24px;
  margin-bottom: 32px;
}

.announcement-panel,
.quick-entry-panel {
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
  overflow: hidden;
}

.panel-header {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 16px 20px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
  font-size: 15px;
  font-weight: 500;
}

.announcement-list {
  padding: 12px 20px;
  max-height: 200px;
  overflow-y: auto;
}

.announcement-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 10px 0;
  border-bottom: 1px solid #f0f2f5;
  cursor: pointer;
  transition: all 0.2s;
}

.announcement-item:hover {
  background: #f5f7fa;
  margin: 0 -20px;
  padding: 10px 20px;
}

.announcement-item:last-child {
  border-bottom: none;
}

.announcement-title {
  flex: 1;
  min-width: 0;
  font-size: 14px;
  color: #303133;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.announcement-date {
  font-size: 12px;
  color: #c0c4cc;
  flex-shrink: 0;
}

.quick-entry-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 12px;
  padding: 16px;
}

.entry-card {
  border-radius: 10px;
  overflow: hidden;
  cursor: pointer;
  transition: all 0.3s;
}

.entry-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.15);
}

.entry-bg {
  height: 100px;
  background-size: cover;
  background-position: center;
  position: relative;
}

.entry-overlay {
  position: absolute;
  inset: 0;
  background: linear-gradient(180deg, rgba(0,0,0,0.1) 0%, rgba(0,0,0,0.5) 100%);
}

.entry-content {
  position: absolute;
  inset: 0;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 8px;
  color: #fff;
}

.entry-content .entry-icon {
  font-size: 28px;
  filter: drop-shadow(0 2px 4px rgba(0,0,0,0.2));
}

.entry-content .entry-name {
  font-size: 14px;
  font-weight: 500;
  text-shadow: 0 1px 3px rgba(0,0,0,0.3);
}

.announcement-detail {
  padding: 10px 0;
}

.detail-meta {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 16px;
}

.detail-date {
  font-size: 13px;
  color: #909399;
}

.detail-content {
  font-size: 14px;
  color: #606266;
  line-height: 1.8;
}

.detail-content :deep(p) {
  margin: 0.5em 0;
}

.detail-content :deep(img) {
  max-width: 100%;
  height: auto;
}

.sections-grid {
  margin-bottom: 40px;
}

.section-block {
  background: #fff;
  border-radius: 16px;
  padding: 28px;
  margin-bottom: 24px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.04);
  transition: all 0.3s;
}

.section-block:hover {
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.08);
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  padding-bottom: 16px;
  border-bottom: 1px solid #f0f2f5;
}

.section-title {
  display: flex;
  align-items: center;
  gap: 12px;
}

.section-icon {
  font-size: 24px;
  color: #667eea;
}

.section-header h2 {
  margin: 0;
  font-size: 20px;
  font-weight: 600;
  color: #303133;
}

.more-btn {
  font-weight: 500;
  display: flex;
  align-items: center;
  gap: 4px;
}

.article-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 24px;
}

.article-card {
  background: #fafbfc;
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

.article-cover::before {
  content: '';
  position: absolute;
  inset: 0;
  background: linear-gradient(45deg, rgba(0,0,0,0.1) 0%, transparent 100%);
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

.article-grid.style-list {
  grid-template-columns: 1fr;
  gap: 0;
}

.article-grid.style-list .article-card {
  border-radius: 0;
  border: none;
  border-bottom: 1px solid #f0f2f5;
  background: transparent;
}

.article-grid.style-list .article-card:last-child {
  border-bottom: none;
}

.article-grid.style-list .article-card:hover {
  transform: none;
  box-shadow: none;
  background: #f5f7fa;
}

.list-item {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 16px 0;
}

.list-index {
  width: 28px;
  height: 28px;
  border-radius: 6px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  font-weight: 600;
  flex-shrink: 0;
}

.list-content {
  flex: 1;
  min-width: 0;
}

.article-grid.style-list .article-title {
  font-size: 16px;
  margin-bottom: 6px;
}

.article-grid.style-list .article-meta {
  justify-content: flex-start;
  gap: 20px;
}

.article-grid.style-compact {
  grid-template-columns: repeat(2, 1fr);
  gap: 0 24px;
}

.article-grid.style-compact .article-card {
  border-radius: 0;
  border: none;
  background: transparent;
  padding: 10px 0;
}

.article-grid.style-compact .article-card:hover {
  transform: none;
  box-shadow: none;
}

.compact-item {
  display: flex;
  align-items: center;
  gap: 8px;
}

.compact-dot {
  width: 5px;
  height: 5px;
  border-radius: 50%;
  background: #667eea;
  flex-shrink: 0;
}

.article-grid.style-compact .article-title {
  flex: 1;
  font-size: 14px;
  margin: 0;
  font-weight: 400;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.compact-date {
  font-size: 12px;
  color: #c0c4cc;
  flex-shrink: 0;
  margin-left: 8px;
}

.cta-section {
  background: #fff;
  border-radius: 16px;
  padding: 60px;
  text-align: center;
}

.top-tag {
  margin-right: 8px;
  vertical-align: middle;
}

.top-tag-compact {
  flex-shrink: 0;
  margin-right: 4px;
}
</style>
