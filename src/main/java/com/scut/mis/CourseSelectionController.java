package com.scut.mis;

import com.scut.mis.entity.CourseSelection;
import com.scut.mis.mapper.CourseSelectionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/selection")
public class CourseSelectionController {

    @Autowired
    private CourseSelectionMapper selectionMapper;

    @GetMapping("/list")
    public List<CourseSelection> list() {
        return selectionMapper.findAll();
    }

    @GetMapping("/student/{studentId}")
    public List<CourseSelection> getByStudent(@PathVariable String studentId) {
        return selectionMapper.findByStudentId(studentId);
    }

    @GetMapping("/course/{courseId}")
    public List<CourseSelection> getByCourse(@PathVariable String courseId) {
        return selectionMapper.findByCourseId(courseId);
    }

    @PostMapping("/add")
    public String add(@RequestBody CourseSelection selection) {
        int result = selectionMapper.insert(selection);
        return result > 0 ? "选课成功" : "选课失败";
    }

    @PutMapping("/score")
    public String updateScore(@RequestBody CourseSelection selection) {
        int result = selectionMapper.updateScore(selection);
        return result > 0 ? "成绩更新成功" : "成绩更新失败";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Integer id) {
        int result = selectionMapper.delete(id);
        return result > 0 ? "删除成功" : "删除失败";
    }
}