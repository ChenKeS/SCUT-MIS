App({
  onLaunch() {
    // 初始化角色
    if (!wx.getStorageSync('userRole')) {
      wx.setStorageSync('userRole', 'ADMIN')
    }
    console.log('小程序启动成功')
  },
  
  globalData: {
    baseUrl: 'http://192.168.43.163:8080'
  }
})