package com.scut.mis;

import com.scut.mis.entity.Course;
import com.scut.mis.entity.CourseSelection;
import com.scut.mis.mapper.CourseMapper;
import com.scut.mis.mapper.CourseSelectionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/courseinfo")
public class CourseInfoController {

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private CourseSelectionMapper selectionMapper;

    /**
     * 根据课程ID或名称查询课程信息和选课信息
     * 如果不给参数，查询所有课程及其选课信息
     *
     * GET /courseinfo/query?courseId=xxx&courseName=xxx
     * GET /courseinfo/query (查询所有)
     */
    @GetMapping("/query")
    public Map<String, Object> queryCourseInfo(
            @RequestParam(required = false) String courseId,
            @RequestParam(required = false) String courseName) {

        Map<String, Object> result = new HashMap<>();

        // 情况1：根据课程ID查询
        if (courseId != null && !courseId.trim().isEmpty()) {
            Course course = courseMapper.findById(courseId);
            if (course == null) {
                result.put("success", false);
                result.put("message", "未找到课程ID为 " + courseId + " 的课程");
                return result;
            }
            return getCourseWithSelections(course);
        }

        // 情况2：根据课程名称查询
        if (courseName != null && !courseName.trim().isEmpty()) {
            List<Course> courses = courseMapper.findByName(courseName);
            if (courses.isEmpty()) {
                result.put("success", false);
                result.put("message", "未找到课程名称为 " + courseName + " 的课程");
                return result;
            }
            // 如果只有1门课程，直接返回该课程信息
            if (courses.size() == 1) {
                return getCourseWithSelections(courses.get(0));
            }
            // 如果有多个同名课程，返回列表
            return getMultipleCoursesWithSelections(courses);
        }

        // 情况3：没有参数，查询所有课程及其选课信息
        List<Course> allCourses = courseMapper.findAll();
        if (allCourses.isEmpty()) {
            result.put("success", true);
            result.put("message", "暂无课程数据");
            result.put("courses", null);
            return result;
        }

        return getAllCoursesWithSelections(allCourses);
    }

    /**
     * 获取单个课程及其选课信息
     */
    private Map<String, Object> getCourseWithSelections(Course course) {
        Map<String, Object> result = new HashMap<>();
        List<CourseSelection> selections = selectionMapper.findByCourseId(course.getCourseId());

        result.put("success", true);
        result.put("course", course);
        result.put("selections", selections);
        result.put("studentCount", selections.size());

        // 计算该课程的平均分
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
     * 获取多个课程及其选课信息（用于同名课程）
     */
    private Map<String, Object> getMultipleCoursesWithSelections(List<Course> courses) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "找到 " + courses.size() + " 门同名课程");

        List<Map<String, Object>> courseList = new java.util.ArrayList<>();
        for (Course course : courses) {
            Map<String, Object> courseInfo = new HashMap<>();
            List<CourseSelection> selections = selectionMapper.findByCourseId(course.getCourseId());

            courseInfo.put("course", course);
            courseInfo.put("selections", selections);
            courseInfo.put("studentCount", selections.size());

            // 计算平均分
            double sum = 0;
            int count = 0;
            for (CourseSelection cs : selections) {
                if (cs.getScore() != null) {
                    sum += cs.getScore();
                    count++;
                }
            }
            courseInfo.put("averageScore", count > 0 ? sum / count : 0);

            courseList.add(courseInfo);
        }

        result.put("courses", courseList);
        return result;
    }

    /**
     * 获取所有课程及其选课信息
     */
    private Map<String, Object> getAllCoursesWithSelections(List<Course> courses) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("totalCourses", courses.size());

        List<Map<String, Object>> courseList = new java.util.ArrayList<>();
        for (Course course : courses) {
            Map<String, Object> courseInfo = new HashMap<>();
            List<CourseSelection> selections = selectionMapper.findByCourseId(course.getCourseId());

            courseInfo.put("course", course);
            courseInfo.put("selections", selections);
            courseInfo.put("studentCount", selections.size());

            // 计算平均分
            double sum = 0;
            int count = 0;
            for (CourseSelection cs : selections) {
                if (cs.getScore() != null) {
                    sum += cs.getScore();
                    count++;
                }
            }
            courseInfo.put("averageScore", count > 0 ? sum / count : 0);

            courseList.add(courseInfo);
        }

        result.put("courses", courseList);
        return result;
    }
}