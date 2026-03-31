const app = getApp()

// 获取当前角色
const getRole = () => {
  return wx.getStorageSync('userRole') || 'ADMIN'
}

// 通用请求方法
const request = (url, method, data) => {
  return new Promise((resolve, reject) => {
    wx.showLoading({ title: '加载中...', mask: true })
    
    wx.request({
      url: app.globalData.baseUrl + url,
      method: method,
      data: data,
      header: {
        'Content-Type': 'application/json',
        'X-Role': getRole()
      },
      success: (res) => {
        wx.hideLoading()
        if (res.statusCode === 200) {
          resolve(res.data)
        } else if (res.statusCode === 403) {
          wx.showToast({
            title: res.data.message || '权限不足',
            icon: 'none'
          })
          reject(res)
        } else {
          wx.showToast({
            title: '请求失败',
            icon: 'none'
          })
          reject(res)
        }
      },
      fail: (err) => {
        wx.hideLoading()
        wx.showToast({
          title: '网络错误，请检查后端',
          icon: 'none',
          duration: 2000
        })
        reject(err)
      }
    })
  })
}

// ============ 学生 API ============
export const studentApi = {
  list: () => request('/student/list', 'GET'),
  add: (data) => request('/student/add', 'POST', data),
  update: (data) => request('/student/update', 'PUT', data),
  delete: (id) => request(`/student/${id}`, 'DELETE'),
  search: (name) => request(`/student/search?name=${name}`, 'GET'),
  findById: (id) => request(`/student/${id}`, 'GET')
}

// ============ 课程 API ============
export const courseApi = {
  list: () => request('/course/list', 'GET'),
  add: (data) => request('/course/add', 'POST', data),
  update: (data) => request('/course/update', 'PUT', data),
  delete: (id) => request(`/course/${id}`, 'DELETE'),
  search: (name) => request(`/course/search?name=${name}`, 'GET'),
  findById: (id) => request(`/course/${id}`, 'GET')
}

// ============ 教师 API ============
export const teacherApi = {
  list: () => request('/teacher/list', 'GET'),
  add: (data) => request('/teacher/add', 'POST', data),
  update: (data) => request('/teacher/update', 'PUT', data),
  delete: (id) => request(`/teacher/${id}`, 'DELETE'),
  findByName: (name) => request(`/teacher/search?name=${name}`, 'GET')
}

// ============ 选课 API ============
export const selectionApi = {
  list: () => request('/selection/list', 'GET'),
  add: (data) => request('/selection/add', 'POST', data),
  updateScore: (data) => request('/selection/score', 'PUT', data),
  delete: (id) => request(`/selection/${id}`, 'DELETE'),
  getByStudent: (studentId) => request(`/selection/student/${studentId}`, 'GET'),
  getByCourse: (courseId) => request(`/selection/course/${courseId}`, 'GET')
}

// ============ 统计 API ============
export const statisticsApi = {
  studentAvg: (studentId) => request(`/statistics/student/avg/${studentId}`, 'GET'),
  allAvg: () => request('/statistics/all/avg', 'GET'),
  classAvg: (className) => request(`/statistics/class/avg/${className}`, 'GET'),
  courseAvg: (courseId) => request(`/statistics/course/avg/${courseId}`, 'GET')
}

// ============ 综合查询 API ============
export const queryApi = {
  studentInfo: (studentId, name) => {
    let url = '/studentinfo/query'
    if (studentId) url += `?studentId=${studentId}`
    else if (name) url += `?name=${name}`
    return request(url, 'GET')
  },
  scoreQuery: (studentId, studentName, courseId, courseName) => {
    let params = []
    if (studentId) params.push(`studentId=${studentId}`)
    if (studentName) params.push(`studentName=${studentName}`)
    if (courseId) params.push(`courseId=${courseId}`)
    if (courseName) params.push(`courseName=${courseName}`)
    let url = '/score/query'
    if (params.length > 0) url += `?${params.join('&')}`
    return request(url, 'GET')
  },
  courseInfo: (courseId, courseName) => {
    let url = '/courseinfo/query'
    if (courseId) url += `?courseId=${courseId}`
    else if (courseName) url += `?courseName=${courseName}`
    return request(url, 'GET')
  },
  teacherInfo: (teacherId, teacherName) => {
    let url = '/teacherinfo/query'
    if (teacherId) url += `?teacherId=${teacherId}`
    else if (teacherName) url += `?teacherName=${teacherName}`
    return request(url, 'GET')
  }
}