import { defineStore } from 'pinia'
import { ref } from 'vue'
import { login as loginApi, logout as logoutApi, getCurrentUser } from '@/api/auth'

export const useUserStore = defineStore('user', () => {
  const token = ref(localStorage.getItem('token') || '')
  const user = ref(JSON.parse(localStorage.getItem('user') || 'null'))

  function setToken(newToken) {
    token.value = newToken
    localStorage.setItem('token', newToken)
  }

  function setUser(newUser) {
    user.value = newUser
    localStorage.setItem('user', JSON.stringify(newUser))
  }

  async function login(credentials) {
    const data = await loginApi(credentials)
    setToken(data.token)
    setUser(data.user)
    return data
  }

  async function logout() {
    try {
      await logoutApi()
    } finally {
      token.value = ''
      user.value = null
      localStorage.removeItem('token')
      localStorage.removeItem('user')
    }
  }

  async function fetchUser() {
    if (!token.value) return null
    try {
      const userData = await getCurrentUser()
      setUser(userData)
      return userData
    } catch {
      token.value = ''
      user.value = null
      localStorage.removeItem('token')
      localStorage.removeItem('user')
      return null
    }
  }

  function isLoggedIn() {
    return !!token.value
  }

  return {
    token,
    user,
    setToken,
    setUser,
    login,
    logout,
    fetchUser,
    isLoggedIn
  }
})
