import { statisticsApi, studentApi, courseApi } from '../../utils/request'

Page({
  data: {
    loading: false,
    allAvg: null,
    classAvg: null,
    courseAvg: null,
    studentAvg: null,
    selectedStudentId: '',
    selectedStudentName: '',
    selectedCourseId: '',
    selectedCourseName: '',
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
        selectedCourseId: data[0]?.courseId || '',
        selectedCourseName: data[0]?.name || ''
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
      const avg = data.averageScore
      this.setData({ allAvg: avg !== null && avg !== undefined ? avg.toFixed(1) : null })
    } catch (err) {
      console.error('加载全校平均分失败', err)
      this.setData({ allAvg: null })
    }
    this.setData({ loading: false })
  },

  async loadClassAvg(className) {
    this.setData({ loading: true })
    try {
      const data = await statisticsApi.classAvg(className)
      const avg = data.averageScore
      this.setData({ classAvg: avg !== null && avg !== undefined ? avg.toFixed(1) : null })
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
      const avg = data.averageScore
      this.setData({ courseAvg: avg !== null && avg !== undefined ? avg.toFixed(1) : null })
    } catch (err) {
      console.error('加载课程平均分失败', err)
      this.setData({ courseAvg: null })
    }
    this.setData({ loading: false })
  },

  async loadStudentAvg() {
    if (!this.data.selectedStudentId) return
    this.setData({ loading: true })
    try {
      const data = await statisticsApi.studentAvg(this.data.selectedStudentId)
      const avg = data.averageScore
      this.setData({ studentAvg: avg !== null && avg !== undefined ? avg.toFixed(1) : null })
    } catch (err) {
      console.error('加载学生平均分失败', err)
      this.setData({ studentAvg: null })
    }
    this.setData({ loading: false })
  },

  onStudentChange(e) {
    const index = e.detail.value
    const student = this.data.students[index]
    if (student) {
      this.setData({ 
        selectedStudentId: student.studentId,
        selectedStudentName: student.name
      })
      this.loadStudentAvg()
    }
  },

  onClassChange(e) {
    const index = e.detail.value
    const className = this.data.classes[index]
    this.setData({ selectedClass: className })
    this.loadClassAvg(className)
  },

  onCourseChange(e) {
    const index = e.detail.value
    const course = this.data.courses[index]
    if (course) {
      this.setData({ 
        selectedCourseId: course.courseId,
        selectedCourseName: course.name
      })
      this.loadCourseAvg(course.courseId)
    }
  },

  refreshAll() {
    this.loadAllAvg()
    if (this.data.selectedClass) {
      this.loadClassAvg(this.data.selectedClass)
    }
    if (this.data.selectedCourseId) {
      this.loadCourseAvg(this.data.selectedCourseId)
    }
    if (this.data.selectedStudentId) {
      this.loadStudentAvg()
    }
    wx.showToast({ title: '已刷新', icon: 'success' })
  }
})