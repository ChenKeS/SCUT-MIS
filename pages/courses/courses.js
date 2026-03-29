import { courseApi } from '../../utils/request'

Page({
  data: {
    courses: [],
    searchText: '',
    showModal: false,
    editMode: false,
    form: {
      courseId: '',
      name: '',
      teacherId: '',
      credit: '',
      grade: '',
      canceledYear: ''
    },
    canEdit: true
  },

  onLoad() {
    this.checkPermission()
  },

  onShow() {
    this.loadCourses()
  },

  checkPermission() {
    const role = wx.getStorageSync('userRole') || 'ADMIN'
    this.setData({ canEdit: role === 'ADMIN' })
  },

  async loadCourses() {
    try {
      const data = await courseApi.list()
      this.setData({ courses: data })
    } catch (err) {
      console.error('加载失败', err)
      wx.showToast({ title: '加载失败', icon: 'none' })
    }
  },

  onSearchInput(e) {
    const text = e.detail.value
    this.setData({ searchText: text })
    
    if (text.trim()) {
      this.searchCourses(text)
    } else {
      this.loadCourses()
    }
  },

  async searchCourses(keyword) {
    try {
      const data = await courseApi.search(keyword)
      this.setData({ courses: data })
    } catch (err) {
      console.error('搜索失败', err)
    }
  },

  showAddDialog() {
    this.setData({
      showModal: true,
      editMode: false,
      form: {
        courseId: '',
        name: '',
        teacherId: '',
        credit: '',
        grade: '',
        canceledYear: ''
      }
    })
  },

  editCourse(e) {
    const course = e.currentTarget.dataset.course
    this.setData({
      showModal: true,
      editMode: true,
      form: { ...course }
    })
  },

  onFormChange(e) {
    const field = e.currentTarget.dataset.field
    this.setData({
      [`form.${field}`]: e.detail.value
    })
  },

  async saveCourse() {
    const { form, editMode } = this.data
    
    if (!form.courseId || !form.name) {
      wx.showToast({ title: '请填写课程号和课程名', icon: 'none' })
      return
    }
    
    try {
      if (editMode) {
        await courseApi.update(form)
        wx.showToast({ title: '更新成功' })
      } else {
        await courseApi.add(form)
        wx.showToast({ title: '添加成功' })
      }
      this.setData({ showModal: false })
      this.loadCourses()
    } catch (err) {
      wx.showToast({ title: '操作失败', icon: 'none' })
    }
  },

  async deleteCourse(e) {
    const id = e.currentTarget.dataset.id
    
    wx.showModal({
      title: '确认删除',
      content: '确定要删除该课程吗？',
      success: async (res) => {
        if (res.confirm) {
          try {
            await courseApi.delete(id)
            wx.showToast({ title: '删除成功' })
            this.loadCourses()
          } catch (err) {
            wx.showToast({ title: '删除失败', icon: 'none' })
          }
        }
      }
    })
  },

  closeModal() {
    this.setData({ showModal: false })
  }
})