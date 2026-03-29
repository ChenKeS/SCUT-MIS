package com.scut.mis;

import com.scut.mis.entity.Student;
import com.scut.mis.mapper.StudentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentMapper studentMapper;

    // 查询所有学生
    @GetMapping("/list")
    public List<Student> list() {
        return studentMapper.findAll();
    }

    // 根据学号查询学生
    @GetMapping("/{id}")
    public Student getById(@PathVariable String id) {
        return studentMapper.findById(id);
    }

    // 根据姓名查询学生
    @GetMapping("/search")
    public List<Student> searchByName(@RequestParam String name) {
        return studentMapper.findByName(name);
    }

    // 添加学生
    @PostMapping("/add")
    public String add(@RequestBody Student student) {
        int result = studentMapper.insert(student);
        return result > 0 ? "添加成功" : "添加失败";
    }

    // 更新学生
    @PutMapping("/update")
    public String update(@RequestBody Student student) {
        int result = studentMapper.update(student);
        return result > 0 ? "更新成功" : "更新失败";
    }

    // 删除学生
    @DeleteMapping("/{id}")
    public String delete(@PathVariable String id) {
        int result = studentMapper.delete(id);
        return result > 0 ? "删除成功" : "删除失败";
    }
}
