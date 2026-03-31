import { studentApi } from '../../utils/request'

Page({
  data: {
    students: [],
    searchText: '',
    showModal: false,
    editMode: false,
    form: {
      studentId: '',
      name: '',
      sex: '',
      entranceAge: '',
      entranceYear: '',
      studentClass: ''
    },
    canEdit: true
  },

  onLoad() {
    this.checkPermission()
  },

  onShow() {
    this.loadStudents()
  },

  checkPermission() {
    const role = wx.getStorageSync('userRole') || 'ADMIN'
    this.setData({ canEdit: role === 'ADMIN' })
  },

  async loadStudents() {
    try {
      const data = await studentApi.list()
      this.setData({ students: data })
    } catch (err) {
      console.error('加载失败', err)
    }
  },

  onSearchInput(e) {
    const text = e.detail.value
    this.setData({ searchText: text })
    if (text.trim()) {
      this.searchStudents(text)
    } else {
      this.loadStudents()
    }
  },

  async searchStudents(keyword) {
    try {
      const data = await studentApi.search(keyword)
      this.setData({ students: data })
    } catch (err) {
      console.error('搜索失败', err)
    }
  },

  showAddDialog() {
    this.setData({
      showModal: true,
      editMode: false,
      form: {
        studentId: '',
        name: '',
        sex: '',
        entranceAge: '',
        entranceYear: '',
        studentClass: ''
      }
    })
  },

  editStudent(e) {
    const student = e.currentTarget.dataset.student
    this.setData({
      showModal: true,
      editMode: true,
      form: { ...student }
    })
  },

  onFormChange(e) {
    const field = e.currentTarget.dataset.field
    this.setData({
      [`form.${field}`]: e.detail.value
    })
  },

  async saveStudent() {
    const { form, editMode } = this.data
    
    if (!form.studentId || !form.name) {
      wx.showToast({ title: '请填写学号和姓名', icon: 'none' })
      return
    }
    
    try {
      if (editMode) {
        await studentApi.update(form)
        wx.showToast({ title: '更新成功' })
      } else {
        await studentApi.add(form)
        wx.showToast({ title: '添加成功' })
      }
      this.setData({ showModal: false })
      this.loadStudents()
    } catch (err) {
      wx.showToast({ title: '操作失败', icon: 'none' })
    }
  },

  async deleteStudent(e) {
    const id = e.currentTarget.dataset.id
    
    wx.showModal({
      title: '确认删除',
      content: '确定要删除该学生吗？',
      success: async (res) => {
        if (res.confirm) {
          try {
            await studentApi.delete(id)
            wx.showToast({ title: '删除成功' })
            this.loadStudents()
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