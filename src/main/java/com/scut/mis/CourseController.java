package com.scut.mis;

import com.scut.mis.entity.Course;
import com.scut.mis.mapper.CourseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/course")
public class CourseController {

    @Autowired
    private CourseMapper courseMapper;

    @GetMapping("/list")
    public List<Course> list() {
        return courseMapper.findAll();
    }

    @GetMapping("/{id}")
    public Course getById(@PathVariable String id) {
        return courseMapper.findById(id);
    }

    @GetMapping("/search")
    public List<Course> searchByName(@RequestParam String name) {
        return courseMapper.findByName(name);
    }

    @PostMapping("/add")
    public String add(@RequestBody Course course) {
        int result = courseMapper.insert(course);
        return result > 0 ? "添加成功" : "添加失败";
    }

    @PutMapping("/update")
    public String update(@RequestBody Course course) {
        int result = courseMapper.update(course);
        return result > 0 ? "更新成功" : "更新失败";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable String id) {
        int result = courseMapper.delete(id);
        return result > 0 ? "删除成功" : "删除失败";
    }
}