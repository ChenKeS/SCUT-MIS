package com.scut.mis.mapper;

import com.scut.mis.entity.Course;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CourseMapper {

    /**
     * 查询所有课程
     */
    @Select("SELECT * FROM course")
    @Results(id = "courseResult", value = {
            @Result(column = "course_id", property = "courseId"),
            @Result(column = "teacher_id", property = "teacherId"),
            @Result(column = "canceled_year", property = "canceledYear")
    })
    List<Course> findAll();

    /**
     * 根据课程ID查询课程
     */
    @Select("SELECT * FROM course WHERE course_id = #{courseId}")
    @ResultMap("courseResult")
    Course findById(String courseId);

    /**
     * 根据课程名称查询课程（模糊查询）
     */
    @Select("SELECT * FROM course WHERE name LIKE CONCAT('%', #{name}, '%')")
    @ResultMap("courseResult")
    List<Course> findByName(String name);

    /**
     * 根据教师ID查询课程
     */
    @Select("SELECT * FROM course WHERE teacher_id = #{teacherId}")
    @ResultMap("courseResult")
    List<Course> findByTeacherId(String teacherId);

    /**
     * 插入课程
     */
    @Insert("INSERT INTO course(course_id, name, teacher_id, credit, grade, canceled_year) " +
            "VALUES(#{courseId}, #{name}, #{teacherId}, #{credit}, #{grade}, #{canceledYear})")
    int insert(Course course);

    /**
     * 更新课程信息
     */
    @Update("UPDATE course SET name=#{name}, teacher_id=#{teacherId}, credit=#{credit}, " +
            "grade=#{grade}, canceled_year=#{canceledYear} WHERE course_id=#{courseId}")
    int update(Course course);

    /**
     * 根据课程ID删除课程
     */
    @Delete("DELETE FROM course WHERE course_id=#{courseId}")
    int delete(String courseId);
}