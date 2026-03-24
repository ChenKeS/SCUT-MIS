package com.scut.mis;

import com.scut.mis.entity.Course;
import com.scut.mis.entity.Teacher;
import com.scut.mis.mapper.CourseMapper;
import com.scut.mis.mapper.TeacherMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/teacherinfo")
public class TeacherInfoController {

    @Autowired
    private TeacherMapper teacherMapper;

    @Autowired
    private CourseMapper courseMapper;

    /**
     * 根据教师ID或姓名查询教师信息和所教课程
     * 如果不给参数，查询所有教师及其所教课程
     *
     * GET /teacherinfo/query?teacherId=xxx&teacherName=xxx
     * GET /teacherinfo/query (查询所有)
     */
    @GetMapping("/query")
    public Map<String, Object> queryTeacherInfo(
            @RequestParam(required = false) String teacherId,
            @RequestParam(required = false) String teacherName) {

        Map<String, Object> result = new HashMap<>();

        // 情况1：根据教师ID查询
        if (teacherId != null && !teacherId.trim().isEmpty()) {
            Teacher teacher = teacherMapper.findById(teacherId);
            if (teacher == null) {
                result.put("success", false);
                result.put("message", "未找到教师ID为 " + teacherId + " 的教师");
                return result;
            }
            return getTeacherWithCourses(teacher);
        }

        // 情况2：根据教师姓名查询
        if (teacherName != null && !teacherName.trim().isEmpty()) {
            List<Teacher> teachers = teacherMapper.findByName(teacherName);
            if (teachers.isEmpty()) {
                result.put("success", false);
                result.put("message", "未找到教师姓名为 " + teacherName + " 的教师");
                return result;
            }
            // 如果只有1位教师，直接返回该教师信息
            if (teachers.size() == 1) {
                return getTeacherWithCourses(teachers.get(0));
            }
            // 如果有多个同名教师，返回列表
            return getMultipleTeachersWithCourses(teachers);
        }

        // 情况3：没有参数，查询所有教师及其所教课程
        List<Teacher> allTeachers = teacherMapper.findAll();
        if (allTeachers.isEmpty()) {
            result.put("success", true);
            result.put("message", "暂无教师数据");
            result.put("teachers", null);
            return result;
        }

        return getAllTeachersWithCourses(allTeachers);
    }

    /**
     * 获取单个教师及其所教课程
     */
    private Map<String, Object> getTeacherWithCourses(Teacher teacher) {
        Map<String, Object> result = new HashMap<>();

        // 查询该教师所教的所有课程
        List<Course> courses = courseMapper.findByTeacherId(teacher.getTeacherId());

        result.put("success", true);
        result.put("teacher", teacher);
        result.put("courses", courses);
        result.put("courseCount", courses.size());

        return result;
    }

    /**
     * 获取多个教师及其所教课程（用于同名教师）
     */
    private Map<String, Object> getMultipleTeachersWithCourses(List<Teacher> teachers) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "找到 " + teachers.size() + " 位同名教师");

        List<Map<String, Object>> teacherList = new java.util.ArrayList<>();
        for (Teacher teacher : teachers) {
            Map<String, Object> teacherInfo = new HashMap<>();
            List<Course> courses = courseMapper.findByTeacherId(teacher.getTeacherId());

            teacherInfo.put("teacher", teacher);
            teacherInfo.put("courses", courses);
            teacherInfo.put("courseCount", courses.size());

            teacherList.add(teacherInfo);
        }

        result.put("teachers", teacherList);
        return result;
    }

    /**
     * 获取所有教师及其所教课程
     */
    private Map<String, Object> getAllTeachersWithCourses(List<Teacher> teachers) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("totalTeachers", teachers.size());

        List<Map<String, Object>> teacherList = new java.util.ArrayList<>();
        for (Teacher teacher : teachers) {
            Map<String, Object> teacherInfo = new HashMap<>();
            List<Course> courses = courseMapper.findByTeacherId(teacher.getTeacherId());

            teacherInfo.put("teacher", teacher);
            teacherInfo.put("courses", courses);
            teacherInfo.put("courseCount", courses.size());

            teacherList.add(teacherInfo);
        }

        result.put("teachers", teacherList);
        return result;
    }
}