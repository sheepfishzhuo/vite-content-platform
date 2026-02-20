import { createRouter, createWebHistory } from 'vue-router'
import MainLayout from '@/layouts/MainLayout.vue'
import FrontendLayout from '@/layouts/FrontendLayout.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/login',
      name: 'Login',
      component: () => import('@/views/Login.vue'),
      meta: { title: '登录' }
    },
    {
      path: '/',
      component: FrontendLayout,
      children: [
        {
          path: '',
          name: 'FrontendHome',
          component: () => import('@/views/frontend/Home.vue'),
          meta: { title: '首页' }
        },
        {
          path: 'article/:id',
          name: 'ArticleDetail',
          component: () => import('@/views/frontend/ArticleDetail.vue'),
          meta: { title: '文章详情' }
        },
        {
          path: 'list/:menuId?',
          name: 'ArticleList',
          component: () => import('@/views/frontend/ArticleList.vue'),
          meta: { title: '文章列表' }
        },
        {
          path: 'section/:id',
          name: 'SectionPage',
          component: () => import('@/views/frontend/SectionPage.vue'),
          meta: { title: '区块详情' }
        }
      ]
    },
    {
      path: '/admin',
      component: MainLayout,
      meta: { requiresAuth: true },
      children: [
        {
          path: '',
          name: 'AdminHome',
          component: () => import('@/views/Home.vue'),
          meta: { title: '管理端' }
        },
        {
          path: 'content',
          children: [
            {
              path: 'articles',
              name: 'ArticleListAdmin',
              component: () => import('@/views/article/List.vue'),
              meta: { title: '文章列表' }
            },
            {
              path: 'publish',
              name: 'ArticlePublish',
              component: () => import('@/views/article/Edit.vue'),
              meta: { title: '发布文章' }
            },
            {
              path: 'publish/:id',
              name: 'ArticleEdit',
              component: () => import('@/views/article/Edit.vue'),
              meta: { title: '编辑文章' }
            }
          ]
        },
        {
          path: 'system',
          children: [
            {
              path: 'menus',
              name: 'MenuManagement',
              component: () => import('@/views/system/Menu.vue'),
              meta: { title: '菜单管理' }
            },
            {
              path: 'sections',
              name: 'SectionManagement',
              component: () => import('@/views/system/Section.vue'),
              meta: { title: '区块管理' }
            },
            {
              path: 'announcements',
              name: 'AnnouncementManagement',
              component: () => import('@/views/system/Announcement.vue'),
              meta: { title: '公告管理' }
            },
            {
              path: 'quick-entries',
              name: 'QuickEntryManagement',
              component: () => import('@/views/system/QuickEntry.vue'),
              meta: { title: '快速入口' }
            },
            {
              path: 'users',
              name: 'UserManagement',
              component: () => import('@/views/system/User.vue'),
              meta: { title: '用户管理' }
            },
            {
              path: 'profile',
              name: 'Profile',
              component: () => import('@/views/system/Profile.vue'),
              meta: { title: '个人中心' }
            }
          ]
        }
      ]
    }
  ]
})

router.beforeEach((to, from, next) => {
  document.title = to.meta.title ? `${to.meta.title} - 2026年度奖学金评定专题` : '2026年度奖学金评定专题'
  
  const token = localStorage.getItem('token')
  
  if (to.meta.requiresAuth && !token) {
    next('/login')
  } else if (to.path === '/login' && token) {
    next('/admin')
  } else {
    next()
  }
})

export default router
