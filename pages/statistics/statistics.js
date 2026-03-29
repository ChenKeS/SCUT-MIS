import { statisticsApi, studentApi, courseApi } from '../../utils/request'

Page({
  data: {
    loading: false,
    allAvg: null,
    classAvg: null,
    courseAvg: null,
    studentAvg: null,
    selectedStudentId: '',
    selectedCourseId: '',
    selectedClass: '',
    students: [],
    courses: [],
    classes: []
  },

  onLoad() {
    this.loadStudents()
    this.loadCourses()
    this.loadAllAvg()
  },

  async loadStudents() {
    try {
      const data = await studentApi.list()
      // 提取班级列表
      const classes = [...new Set(data.map(s => s.studentClass))]
      this.setData({ 
        students: data,
        classes: classes,
        selectedClass: classes[0] || ''
      })
      if (classes[0]) {
        this.loadClassAvg(classes[0])
      }
    } catch (err) {
      console.error('加载学生失败', err)
    }
  },

  async loadCourses() {
    try {
      const data = await courseApi.list()
      this.setData({ 
        courses: data,
        selectedCourseId: data[0]?.courseId || ''
      })
      if (data[0]) {
        this.loadCourseAvg(data[0].courseId)
      }
    } catch (err) {
      console.error('加载课程失败', err)
    }
  },

  async loadAllAvg() {
    this.setData({ loading: true })
    try {
      const data = await statisticsApi.allAvg()
      this.setData({ allAvg: data.averageScore })
    } catch (err) {
      console.error('加载全校平均分失败', err)
    }
    this.setData({ loading: false })
  },

  async loadClassAvg(className) {
    this.setData({ loading: true })
    try {
      const data = await statisticsApi.classAvg(className)
      this.setData({ classAvg: data.averageScore })
    } catch (err) {
      console.error('加载班级平均分失败', err)
      this.setData({ classAvg: null })
    }
    this.setData({ loading: false })
  },

  async loadCourseAvg(courseId) {
    this.setData({ loading: true })
    try {
      const data = await statisticsApi.courseAvg(courseId)
      this.setData({ courseAvg: data.averageScore })
    } catch (err) {
      console.error('加载课程平均分失败', err)
      this.setData({ courseAvg: null })
    }
    this.setData({ loading: false })
  },

  async loadStudentAvg() {
    if (!this.data.selectedStudentId) {
      wx.showToast({ title: '请选择学生', icon: 'none' })
      return
    }
    this.setData({ loading: true })
    try {
      const data = await statisticsApi.studentAvg(this.data.selectedStudentId)
      this.setData({ studentAvg: data.averageScore })
    } catch (err) {
      console.error('加载学生平均分失败', err)
      this.setData({ studentAvg: null })
    }
    this.setData({ loading: false })
  },

  onStudentChange(e) {
    const studentId = e.detail.value
    this.setData({ selectedStudentId: studentId })
    this.loadStudentAvg()
  },

  onClassChange(e) {
    const className = this.data.classes[e.detail.value]
    this.setData({ selectedClass: className })
    this.loadClassAvg(className)
  },

  onCourseChange(e) {
    const courseId = this.data.courses[e.detail.value]?.courseId
    this.setData({ selectedCourseId: courseId })
    this.loadCourseAvg(courseId)
  },

  refreshAll() {
    this.loadAllAvg()
    this.loadClassAvg(this.data.selectedClass)
    this.loadCourseAvg(this.data.selectedCourseId)
    if (this.data.selectedStudentId) {
      this.loadStudentAvg()
    }
    wx.showToast({ title: '已刷新', icon: 'success' })
  }
})