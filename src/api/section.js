import api from '@/utils/api'

export function getHomepageSections() {
  return api.get('/sections/homepage')
}

export function getAllSections() {
  return api.get('/sections/all')
}

export function getSectionItems(id) {
  return api.get(`/sections/${id}/items`)
}

export function createSection(data) {
  return api.post('/sections', data)
}

export function updateSection(id, data) {
  return api.put(`/sections/${id}`, data)
}

export function deleteSection(id) {
  return api.delete(`/sections/${id}`)
}
