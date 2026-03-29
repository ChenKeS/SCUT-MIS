import { selectionApi, studentApi, courseApi } from '../../utils/request'

Page({
  data: {
    selections: [],
    students: [],
    courses: [],
    showAddModal: false,
    showScoreModal: false,
    currentSelection: null,
    role: 'ADMIN',
    form: {
      studentId: '',
      courseId: '',
      teacherId: '',
      chosenYear: new Date().getFullYear(),
      score: ''
    },
    scoreForm: {
      id: '',
      score: ''
    }
  },

  onLoad() {
    this.getRole()
    this.loadStudents()
    this.loadCourses()
    this.loadSelections()
  },

  onShow() {
    this.loadSelections()
  },

  getRole() {
    const role = wx.getStorageSync('userRole') || 'ADMIN'
    this.setData({ role })
  },

  async loadSelections() {
    try {
      const data = await selectionApi.list()
      // 补充学生名和课程名
      const enriched = await this.enrichSelections(data)
      this.setData({ selections: enriched })
    } catch (err) {
      console.error('加载失败', err)
    }
  },

  async enrichSelections(selections) {
    const students = this.data.students
    const courses = this.data.courses
    
    return selections.map(sel => {
      const student = students.find(s => s.studentId === sel.studentId)
      const course = courses.find(c => c.courseId === sel.courseId)
      return {
        ...sel,
        studentName: student ? student.name : sel.studentId,
        courseName: course ? course.name : sel.courseId
      }
    })
  },

  async loadStudents() {
    try {
      const data = await studentApi.list()
      this.setData({ students: data })
    } catch (err) {
      console.error('加载学生失败', err)
    }
  },

  async loadCourses() {
    try {
      const data = await courseApi.list()
      this.setData({ courses: data })
    } catch (err) {
      console.error('加载课程失败', err)
    }
  },

  showAddDialog() {
    this.setData({
      showAddModal: true,
      form: {
        studentId: '',
        courseId: '',
        teacherId: '',
        chosenYear: new Date().getFullYear(),
        score: ''
      }
    })
  },

  onFormChange(e) {
    const field = e.currentTarget.dataset.field
    this.setData({
      [`form.${field}`]: e.detail.value
    })
  },

  onStudentChange(e) {
    const studentId = e.detail.value
    this.setData({
      'form.studentId': studentId
    })
    // 自动获取该学生已选课程？可以后续优化
  },

  onCourseChange(e) {
    const courseId = e.detail.value
    const course = this.data.courses.find(c => c.courseId === courseId)
    this.setData({
      'form.courseId': courseId,
      'form.teacherId': course ? course.teacherId : ''
    })
  },

  async addSelection() {
    const { form } = this.data
    
    if (!form.studentId || !form.courseId) {
      wx.showToast({ title: '请选择学生和课程', icon: 'none' })
      return
    }
    
    try {
      await selectionApi.add({
        studentId: form.studentId,
        courseId: form.courseId,
        teacherId: form.teacherId,
        chosenYear: parseInt(form.chosenYear),
        score: form.score ? parseFloat(form.score) : null
      })
      wx.showToast({ title: '选课成功' })
      this.setData({ showAddModal: false })
      this.loadSelections()
    } catch (err) {
      wx.showToast({ title: '选课失败', icon: 'none' })
    }
  },

  showScoreDialog(e) {
    const selection = e.currentTarget.dataset.selection
    this.setData({
      showScoreModal: true,
      currentSelection: selection,
      scoreForm: {
        id: selection.id,
        score: selection.score || ''
      }
    })
  },

  onScoreChange(e) {
    this.setData({
      'scoreForm.score': e.detail.value
    })
  },

  async updateScore() {
    const { scoreForm } = this.data
    
    if (scoreForm.score === '') {
      wx.showToast({ title: '请输入成绩', icon: 'none' })
      return
    }
    
    try {
      await selectionApi.updateScore({
        id: scoreForm.id,
        score: parseFloat(scoreForm.score)
      })
      wx.showToast({ title: '成绩更新成功' })
      this.setData({ showScoreModal: false })
      this.loadSelections()
    } catch (err) {
      wx.showToast({ title: '更新失败', icon: 'none' })
    }
  },

  async deleteSelection(e) {
    const id = e.currentTarget.dataset.id
    
    wx.showModal({
      title: '确认删除',
      content: '确定要删除该选课记录吗？',
      success: async (res) => {
        if (res.confirm) {
          try {
            await selectionApi.delete(id)
            wx.showToast({ title: '删除成功' })
            this.loadSelections()
          } catch (err) {
            wx.showToast({ title: '删除失败', icon: 'none' })
          }
        }
      }
    })
  },

  closeModal() {
    this.setData({
      showAddModal: false,
      showScoreModal: false
    })
  }
})