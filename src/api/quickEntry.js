import api from '@/utils/api'

export function getQuickEntries() {
  return api.get('/quick-entries')
}

export function getAllQuickEntries() {
  return api.get('/quick-entries/all')
}

export function createQuickEntry(data) {
  return api.post('/quick-entries', data)
}

export function updateQuickEntry(id, data) {
  return api.put(`/quick-entries/${id}`, data)
}

export function deleteQuickEntry(id) {
  return api.delete(`/quick-entries/${id}`)
}
