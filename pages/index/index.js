Page({
  data: {
    role: 'ADMIN',
    roleName: '管理员',
    roleDesc: '不能修改成绩'
  },
  
  onLoad() {
    const role = wx.getStorageSync('userRole') || 'ADMIN'
    this.setRole(role)
  },
  
  switchRole(e) {
    const role = e.currentTarget.dataset.role
    this.setRole(role)
    wx.setStorageSync('userRole', role)
    wx.showToast({
      title: `已切换为${this.data.roleName}`,
      icon: 'success'
    })
  },
  
  setRole(role) {
    const roleMap = {
      'STUDENT': { name: '学生', desc: '只能查询信息' },
      'TEACHER': { name: '教师', desc: '只能修改成绩' },
      'ADMIN': { name: '管理员', desc: '不能修改成绩' }
    }
    this.setData({
      role: role,
      roleName: roleMap[role].name,
      roleDesc: roleMap[role].desc
    })
  },
  
  goToPage(e) {
    const page = e.currentTarget.dataset.page
    wx.navigateTo({
      url: page,
      fail: (err) => {
        console.log('页面跳转失败', err)
        wx.showToast({
          title: '页面开发中',
          icon: 'none'
        })
      }
    })
  }
})