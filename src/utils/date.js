export function formatDate(dateStr) {
  if (!dateStr) return ''
  let date
  if (Array.isArray(dateStr)) {
    const [year, month, day, hour = 0, minute = 0, second = 0] = dateStr
    date = new Date(year, month - 1, day, hour, minute, second)
  } else {
    date = new Date(dateStr)
  }
  if (isNaN(date.getTime())) return ''
  return date.toLocaleDateString('zh-CN')
}

export function formatDateTime(dateStr) {
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
