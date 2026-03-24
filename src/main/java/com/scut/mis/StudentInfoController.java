package com.scut.mis;

import com.scut.mis.entity.CourseSelection;
import com.scut.mis.entity.Student;
import com.scut.mis.mapper.CourseSelectionMapper;
import com.scut.mis.mapper.StudentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/studentinfo")
public class StudentInfoController {

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private CourseSelectionMapper courseSelectionMapper;

    /**
     * 根据学生ID或姓名查询学生信息和选课信息
     * 如果ID和Name都不给，则查询所有学生及其选课信息
     *
     * GET /studentinfo/query?studentId=xxx&name=xxx
     * GET /studentinfo/query (查询所有)
     */
    @GetMapping("/query")
    public Map<String, Object> queryStudentInfo(
            @RequestParam(required = false) String studentId,
            @RequestParam(required = false) String name) {

        Map<String, Object> result = new HashMap<>();

        // 情况1：根据学号查询
        if (studentId != null && !studentId.trim().isEmpty()) {
            Student student = studentMapper.findById(studentId);
            if (student == null) {
                result.put("success", false);
                result.put("message", "未找到学号为 " + studentId + " 的学生");
                return result;
            }
            return getStudentWithCourses(student);
        }

        // 情况2：根据姓名查询
        if (name != null && !name.trim().isEmpty()) {
            List<Student> students = studentMapper.findByName(name);
            if (students.isEmpty()) {
                result.put("success", false);
                result.put("message", "未找到姓名为 " + name + " 的学生");
                return result;
            }
            // 如果只有1个学生，直接返回该学生信息
            if (students.size() == 1) {
                return getStudentWithCourses(students.get(0));
            }
            // 如果有多个同名，返回列表
            return getMultipleStudentsWithCourses(students);
        }

        // 情况3：没有ID也没有姓名，查询所有学生及其选课信息
        List<Student> allStudents = studentMapper.findAll();
        if (allStudents.isEmpty()) {
            result.put("success", true);
            result.put("message", "暂无学生数据");
            result.put("students", null);
            return result;
        }

        return getAllStudentsWithCourses(allStudents);
    }

    /**
     * 获取单个学生及其选课信息
     */
    private Map<String, Object> getStudentWithCourses(Student student) {
        Map<String, Object> result = new HashMap<>();
        List<CourseSelection> selections = courseSelectionMapper.findByStudentId(student.getStudentId());

        result.put("success", true);
        result.put("student", student);
        result.put("courses", selections);
        result.put("courseCount", selections.size());

        // 计算平均分
        double sum = 0;
        int count = 0;
        for (CourseSelection cs : selections) {
            if (cs.getScore() != null) {
                sum += cs.getScore();
                count++;
            }
        }
        double avg = count > 0 ? sum / count : 0;
        result.put("averageScore", avg);

        return result;
    }

    /**
     * 获取多个学生及其选课信息（用于同名情况）
     */
    private Map<String, Object> getMultipleStudentsWithCourses(List<Student> students) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "找到 " + students.size() + " 位同名学生");

        List<Map<String, Object>> studentList = new java.util.ArrayList<>();
        for (Student student : students) {
            Map<String, Object> studentInfo = new HashMap<>();
            List<CourseSelection> selections = courseSelectionMapper.findByStudentId(student.getStudentId());

            studentInfo.put("student", student);
            studentInfo.put("courses", selections);
            studentInfo.put("courseCount", selections.size());

            // 计算平均分
            double sum = 0;
            int count = 0;
            for (CourseSelection cs : selections) {
                if (cs.getScore() != null) {
                    sum += cs.getScore();
                    count++;
                }
            }
            studentInfo.put("averageScore", count > 0 ? sum / count : 0);

            studentList.add(studentInfo);
        }

        result.put("students", studentList);
        return result;
    }

    /**
     * 获取所有学生及其选课信息
     */
    private Map<String, Object> getAllStudentsWithCourses(List<Student> students) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("totalStudents", students.size());

        List<Map<String, Object>> studentList = new java.util.ArrayList<>();
        for (Student student : students) {
            Map<String, Object> studentInfo = new HashMap<>();
            List<CourseSelection> selections = courseSelectionMapper.findByStudentId(student.getStudentId());

            studentInfo.put("student", student);
            studentInfo.put("courses", selections);
            studentInfo.put("courseCount", selections.size());

            // 计算平均分
            double sum = 0;
            int count = 0;
            for (CourseSelection cs : selections) {
                if (cs.getScore() != null) {
                    sum += cs.getScore();
                    count++;
                }
            }
            studentInfo.put("averageScore", count > 0 ? sum / count : 0);

            studentList.add(studentInfo);
        }

        result.put("students", studentList);
        return result;
    }
}