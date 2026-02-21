import api from '@/utils/api'

export function register(data) {
  return api.post('/member/register', data)
}

export function memberLogin(data) {
  return api.post('/member/login', data)
}

export function checkStudentId(studentId) {
  return api.get('/member/check-student-id', { params: { studentId } })
}

export function getMemberList(params) {
  return api.get('/member/list', { params })
}

export function updateMemberStatus(id, status) {
  return api.put(`/member/${id}/status`, { status })
}

export function deleteMember(id) {
  return api.delete(`/member/${id}`)
}

export function getMemberDetail(id) {
  return api.get(`/member/${id}`)
}

export function updateMember(id, data) {
  return api.put(`/member/${id}`, data)
}
