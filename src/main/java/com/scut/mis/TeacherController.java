package com.scut.mis;

import com.scut.mis.entity.Teacher;
import com.scut.mis.mapper.TeacherMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teacher")
public class TeacherController {

    @Autowired
    private TeacherMapper teacherMapper;

    @GetMapping("/list")
    public List<Teacher> list() {
        return teacherMapper.findAll();
    }

    @GetMapping("/{id}")
    public Teacher getById(@PathVariable String id) {
        return teacherMapper.findById(id);
    }

    @GetMapping("/search")
    public List<Teacher> searchByName(@RequestParam String name) {
        return teacherMapper.findByName(name);
    }

    @PostMapping("/add")
    public String add(@RequestBody Teacher teacher) {
        int result = teacherMapper.insert(teacher);
        return result > 0 ? "添加成功" : "添加失败";
    }

    @PutMapping("/update")
    public String update(@RequestBody Teacher teacher) {
        int result = teacherMapper.update(teacher);
        return result > 0 ? "更新成功" : "更新失败";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable String id) {
        int result = teacherMapper.delete(id);
        return result > 0 ? "删除成功" : "删除失败";
    }
}