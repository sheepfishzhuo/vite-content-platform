import { defineStore } from 'pinia'
import { ref } from 'vue'
import { getFrontendMenuTree, getAdminMenuTree, getMenuTree } from '@/api/menu'

export const useMenuStore = defineStore('menu', () => {
  const frontendMenuTree = ref([])
  const adminMenuTree = ref([])
  const allMenuTree = ref([])
  const loading = ref(false)

  async function fetchFrontendMenuTree() {
    loading.value = true
    try {
      frontendMenuTree.value = await getFrontendMenuTree()
    } finally {
      loading.value = false
    }
  }

  async function fetchAdminMenuTree() {
    loading.value = true
    try {
      adminMenuTree.value = await getAdminMenuTree()
    } finally {
      loading.value = false
    }
  }

  async function fetchAllMenuTree() {
    loading.value = true
    try {
      allMenuTree.value = await getMenuTree()
    } finally {
      loading.value = false
    }
  }

  return {
    frontendMenuTree,
    adminMenuTree,
    allMenuTree,
    loading,
    fetchFrontendMenuTree,
    fetchAdminMenuTree,
    fetchAllMenuTree
  }
})
