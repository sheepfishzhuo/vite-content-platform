import api from '@/utils/api'

export function getMenuTree(type) {
  if (type) {
    return api.get('/menus', { params: { type } })
  }
  return api.get('/menus')
}

export function getFrontendMenuTree() {
  return api.get('/menus/frontend')
}

export function getAdminMenuTree() {
  return api.get('/menus/admin')
}

export function getMenuChildren(id) {
  return api.get(`/menus/${id}/children`)
}

export function createMenu(data) {
  return api.post('/menus', data)
}

export function updateMenu(id, data) {
  return api.put(`/menus/${id}`, data)
}

export function deleteMenu(id) {
  return api.delete(`/menus/${id}`)
}
