package com.scut.mis.mapper;

import com.scut.mis.entity.Student;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface StudentMapper {

    /**
     * 查询所有学生
     */
    @Select("SELECT * FROM student")
    @Results(id = "studentResult", value = {
            @Result(column = "student_id", property = "studentId"),
            @Result(column = "entrance_age", property = "entranceAge"),
            @Result(column = "entrance_year", property = "entranceYear"),
            @Result(column = "class", property = "studentClass")
    })
    List<Student> findAll();

    /**
     * 根据学号查询学生
     */
    @Select("SELECT * FROM student WHERE student_id = #{studentId}")
    @ResultMap("studentResult")
    Student findById(String studentId);

    /**
     * 根据姓名查询学生（模糊查询）
     */
    @Select("SELECT * FROM student WHERE name LIKE CONCAT('%', #{name}, '%')")
    @ResultMap("studentResult")
    List<Student> findByName(String name);

    /**
     * 根据班级查询学生
     */
    @Select("SELECT * FROM student WHERE class = #{className}")
    @ResultMap("studentResult")
    List<Student> findByClass(String className);

    /**
     * 插入学生
     */
    @Insert("INSERT INTO student(student_id, name, sex, entrance_age, entrance_year, class) " +
            "VALUES(#{studentId}, #{name}, #{sex}, #{entranceAge}, #{entranceYear}, #{studentClass})")
    int insert(Student student);

    /**
     * 更新学生信息
     */
    @Update("UPDATE student SET name=#{name}, sex=#{sex}, entrance_age=#{entranceAge}, " +
            "entrance_year=#{entranceYear}, class=#{studentClass} WHERE student_id=#{studentId}")
    int update(Student student);

    /**
     * 根据学号删除学生
     */
    @Delete("DELETE FROM student WHERE student_id=#{studentId}")
    int delete(String studentId);
}