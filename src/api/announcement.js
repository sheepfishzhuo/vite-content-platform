import api from '@/utils/api'

export function getAnnouncements() {
  return api.get('/announcements')
}

export function getAllAnnouncements() {
  return api.get('/announcements/all')
}

export function createAnnouncement(data) {
  return api.post('/announcements', data)
}

export function updateAnnouncement(id, data) {
  return api.put(`/announcements/${id}`, data)
}

export function deleteAnnouncement(id) {
  return api.delete(`/announcements/${id}`)
}
