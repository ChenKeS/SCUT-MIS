package com.scut.mis;

import com.scut.mis.mapper.CourseSelectionMapper;
import com.scut.mis.mapper.StudentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/statistics")
public class StatisticsController {

    @Autowired
    private CourseSelectionMapper selectionMapper;

    @Autowired
    private StudentMapper studentMapper;

    /**
     * 1. 查询单个学生的平均分
     * GET /statistics/student/avg/{studentId}
     */
    @GetMapping("/student/avg/{studentId}")
    public Map<String, Object> getStudentAvgScore(@PathVariable String studentId) {
        Map<String, Object> result = new HashMap<>();

        // 查询该学生的所有选课成绩
        List<com.scut.mis.entity.CourseSelection> selections = selectionMapper.findByStudentId(studentId);

        if (selections.isEmpty()) {
            result.put("studentId", studentId);
            result.put("averageScore", null);
            result.put("message", "该学生没有选课记录");
            return result;
        }

        double sum = 0;
        int count = 0;
        for (com.scut.mis.entity.CourseSelection selection : selections) {
            if (selection.getScore() != null) {
                sum += selection.getScore();
                count++;
            }
        }

        double avg = count > 0 ? sum / count : 0;

        result.put("studentId", studentId);
        result.put("averageScore", avg);
        result.put("courseCount", count);

        return result;
    }

    /**
     * 2. 查询全校平均分
     * GET /statistics/all/avg
     */
    @GetMapping("/all/avg")
    public Map<String, Object> getAllStudentsAvgScore() {
        Map<String, Object> result = new HashMap<>();

        List<com.scut.mis.entity.CourseSelection> allSelections = selectionMapper.findAll();

        if (allSelections.isEmpty()) {
            result.put("averageScore", null);
            result.put("message", "没有成绩记录");
            return result;
        }

        double sum = 0;
        int count = 0;
        for (com.scut.mis.entity.CourseSelection selection : allSelections) {
            if (selection.getScore() != null) {
                sum += selection.getScore();
                count++;
            }
        }

        double avg = count > 0 ? sum / count : 0;

        result.put("averageScore", avg);
        result.put("totalScoreCount", count);

        return result;
    }

    /**
     * 3. 查询班级平均分
     * GET /statistics/class/avg/{className}
     */
    @GetMapping("/class/avg/{className}")
    public Map<String, Object> getClassAvgScore(@PathVariable String className) {
        Map<String, Object> result = new HashMap<>();

        // 查询该班级的所有学生
        List<com.scut.mis.entity.Student> students = studentMapper.findByClass(className);

        if (students.isEmpty()) {
            result.put("className", className);
            result.put("averageScore", null);
            result.put("message", "该班级没有学生");
            return result;
        }

        double totalSum = 0;
        int totalCount = 0;

        for (com.scut.mis.entity.Student student : students) {
            List<com.scut.mis.entity.CourseSelection> selections = selectionMapper.findByStudentId(student.getStudentId());

            for (com.scut.mis.entity.CourseSelection selection : selections) {
                if (selection.getScore() != null) {
                    totalSum += selection.getScore();
                    totalCount++;
                }
            }
        }

        double avg = totalCount > 0 ? totalSum / totalCount : 0;

        result.put("className", className);
        result.put("averageScore", avg);
        result.put("studentCount", students.size());
        result.put("totalScoreCount", totalCount);

        return result;
    }

    /**
     * 4. 查询课程平均分
     * GET /statistics/course/avg/{courseId}
     */
    @GetMapping("/course/avg/{courseId}")
    public Map<String, Object> getCourseAvgScore(@PathVariable String courseId) {
        Map<String, Object> result = new HashMap<>();

        List<com.scut.mis.entity.CourseSelection> selections = selectionMapper.findByCourseId(courseId);

        if (selections.isEmpty()) {
            result.put("courseId", courseId);
            result.put("averageScore", null);
            result.put("message", "该课程没有选课记录");
            return result;
        }

        double sum = 0;
        int count = 0;
        for (com.scut.mis.entity.CourseSelection selection : selections) {
            if (selection.getScore() != null) {
                sum += selection.getScore();
                count++;
            }
        }

        double avg = count > 0 ? sum / count : 0;

        result.put("courseId", courseId);
        result.put("averageScore", avg);
        result.put("studentCount", count);

        return result;
    }
}