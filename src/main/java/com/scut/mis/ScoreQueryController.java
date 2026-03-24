package com.scut.mis;

import com.scut.mis.entity.Course;
import com.scut.mis.entity.CourseSelection;
import com.scut.mis.entity.Student;
import com.scut.mis.mapper.CourseMapper;
import com.scut.mis.mapper.CourseSelectionMapper;
import com.scut.mis.mapper.StudentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/score")
public class ScoreQueryController {

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private CourseSelectionMapper selectionMapper;

    /**
     * 根据学生和课程查询成绩
     * GET /score/query?studentId=xxx&studentName=xxx&courseId=xxx&courseName=xxx
     * 如果不给任何参数，返回所有成绩
     */
    @GetMapping("/query")
    public Map<String, Object> queryScore(
            @RequestParam(required = false) String studentId,
            @RequestParam(required = false) String studentName,
            @RequestParam(required = false) String courseId,
            @RequestParam(required = false) String courseName) {

        Map<String, Object> result = new HashMap<>();

        // 如果没有参数，返回所有成绩
        if ((studentId == null || studentId.isEmpty()) &&
                (studentName == null || studentName.isEmpty()) &&
                (courseId == null || courseId.isEmpty()) &&
                (courseName == null || courseName.isEmpty())) {

            List<CourseSelection> allSelections = selectionMapper.findAll();
            result.put("success", true);
            result.put("totalCount", allSelections.size());
            result.put("scores", allSelections);
            return result;
        }

        // 根据学生ID或姓名查找学生
        Student student = null;
        if (studentId != null && !studentId.isEmpty()) {
            student = studentMapper.findById(studentId);
        } else if (studentName != null && !studentName.isEmpty()) {
            List<Student> students = studentMapper.findByName(studentName);
            if (!students.isEmpty()) {
                student = students.get(0);
            }
        }

        // 根据课程ID或名称查找课程
        Course course = null;
        if (courseId != null && !courseId.isEmpty()) {
            course = courseMapper.findById(courseId);
        } else if (courseName != null && !courseName.isEmpty()) {
            List<Course> courses = courseMapper.findByName(courseName);
            if (!courses.isEmpty()) {
                course = courses.get(0);
            }
        }

        // 情况1：只给了学生，查询该学生所有成绩
        if (student != null && course == null) {
            List<CourseSelection> selections = selectionMapper.findByStudentId(student.getStudentId());
            result.put("success", true);
            result.put("student", student);
            result.put("scores", selections);
            result.put("totalCount", selections.size());
            return result;
        }

        // 情况2：只给了课程，查询该课程所有成绩
        if (student == null && course != null) {
            List<CourseSelection> selections = selectionMapper.findByCourseId(course.getCourseId());
            result.put("success", true);
            result.put("course", course);
            result.put("scores", selections);
            result.put("totalCount", selections.size());
            return result;
        }

        // 情况3：同时给了学生和课程，查询特定成绩
        if (student != null && course != null) {
            CourseSelection selection = selectionMapper.findByStudentAndCourse(
                    student.getStudentId(), course.getCourseId());

            result.put("success", true);
            result.put("student", student);
            result.put("course", course);

            if (selection != null) {
                result.put("score", selection.getScore());
                result.put("chosenYear", selection.getChosenYear());
                result.put("teacherId", selection.getTeacherId());
            } else {
                result.put("score", null);
                result.put("message", "该学生未选这门课");
            }
            return result;
        }

        // 情况4：学生或课程没找到
        result.put("success", false);
        if (student == null && (studentId != null || studentName != null)) {
            result.put("message", "未找到学生");
        }
        if (course == null && (courseId != null || courseName != null)) {
            result.put("message", "未找到课程");
        }

        return result;
    }
}