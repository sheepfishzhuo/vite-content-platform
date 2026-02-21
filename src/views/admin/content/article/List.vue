<template>
  <div class="article-list">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>文章列表</span>
          <el-button type="primary" @click="handleCreate">
            <el-icon><Plus /></el-icon>
            发布文章
          </el-button>
        </div>
      </template>
      
      <el-form :inline="true" class="search-form">
        <el-form-item label="所属区块">
          <el-select v-model="searchParams.sectionId" placeholder="请选择" clearable style="width: 200px">
            <el-option
              v-for="section in sections"
              :key="section.id"
              :label="section.title"
              :value="section.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchParams.status" placeholder="请选择" clearable style="width: 120px">
            <el-option label="草稿" :value="0" />
            <el-option label="已发布" :value="1" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="fetchArticles">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="articles" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="title" label="标题" min-width="200" show-overflow-tooltip>
          <template #default="{ row }">
            <div class="title-cell">
              <el-tag v-if="row.isTop" type="danger" size="small" class="top-tag">置顶</el-tag>
              <span class="title-text">{{ row.title }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="summary" label="摘要" min-width="200" show-overflow-tooltip />
        <el-table-column prop="sectionId" label="所属区块" width="120">
          <template #default="{ row }">
            {{ getSectionName(row.sectionId) }}
          </template>
        </el-table-column>
        <el-table-column prop="viewCount" label="浏览量" width="100" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'info'">
              {{ row.status === 1 ? '已发布' : '草稿' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdTime" label="创建时间" width="180">
          <template #default="{ row }">
            {{ formatDate(row.createdTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" link @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        v-model:current-page="searchParams.page"
        v-model:page-size="searchParams.pageSize"
        :total="total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        class="pagination"
        @size-change="fetchArticles"
        @current-change="fetchArticles"
      />
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { getArticles, deleteArticle } from '@/api/article'
import { getHomepageSections } from '@/api/section'

const router = useRouter()
const route = useRoute()

const loading = ref(false)
const articles = ref([])
const total = ref(0)
const sections = ref([])

const searchParams = reactive({
  page: 1,
  pageSize: 10,
  sectionId: null,
  status: null
})

function getSectionName(sectionId) {
  if (!sectionId) return '-'
  const section = sections.value.find(s => s.id === sectionId)
  return section ? section.title : '-'
}

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
  return date.toLocaleString('zh-CN')
}

async function fetchSections() {
  sections.value = await getHomepageSections()
}

async function fetchArticles() {
  loading.value = true
  try {
    const params = { ...searchParams }
    const result = await getArticles(params)
    articles.value = result.records || []
    total.value = result.total || 0
  } finally {
    loading.value = false
  }
}

function resetSearch() {
  searchParams.sectionId = null
  searchParams.status = null
  searchParams.page = 1
  fetchArticles()
}

function handleCreate() {
  router.push('/admin/content/publish')
}

function handleEdit(row) {
  router.push(`/admin/content/publish/${row.id}`)
}

async function handleDelete(row) {
  await ElMessageBox.confirm('确定要删除该文章吗？', '提示', {
    type: 'warning'
  })
  await deleteArticle(row.id)
  ElMessage.success('删除成功')
  fetchArticles()
}

onMounted(async () => {
  await fetchSections()
  if (route.query.sectionId) {
    searchParams.sectionId = Number(route.query.sectionId)
  }
  fetchArticles()
})
</script>

<style scoped>
.article-list {
  padding: 0;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-form {
  margin-bottom: 20px;
}

.pagination {
  margin-top: 20px;
  justify-content: flex-end;
}

.title-cell {
  display: flex;
  align-items: center;
  gap: 8px;
  min-width: 0;
}

.top-tag {
  flex-shrink: 0;
}

.title-text {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  /* max-width: 280px; */
}
</style>
