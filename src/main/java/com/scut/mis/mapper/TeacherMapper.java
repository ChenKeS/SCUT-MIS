package com.scut.mis.mapper;

import com.scut.mis.entity.Teacher;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TeacherMapper {

    /**
     * 查询所有教师
     */
    @Select("SELECT * FROM teacher")
    @Results(id = "teacherResult", value = {
            @Result(column = "teacher_id", property = "teacherId"),
            @Result(column = "courses_taught", property = "coursesTaught")
    })
    List<Teacher> findAll();

    /**
     * 根据教师ID查询教师
     */
    @Select("SELECT * FROM teacher WHERE teacher_id = #{teacherId}")
    @ResultMap("teacherResult")
    Teacher findById(String teacherId);

    /**
     * 根据教师姓名查询教师（模糊查询）
     */
    @Select("SELECT * FROM teacher WHERE name LIKE CONCAT('%', #{name}, '%')")
    @ResultMap("teacherResult")
    List<Teacher> findByName(String name);

    /**
     * 插入教师
     */
    @Insert("INSERT INTO teacher(teacher_id, name, courses_taught) " +
            "VALUES(#{teacherId}, #{name}, #{coursesTaught})")
    int insert(Teacher teacher);

    /**
     * 更新教师信息
     */
    @Update("UPDATE teacher SET name=#{name}, courses_taught=#{coursesTaught} " +
            "WHERE teacher_id=#{teacherId}")
    int update(Teacher teacher);

    /**
     * 根据教师ID删除教师
     */
    @Delete("DELETE FROM teacher WHERE teacher_id=#{teacherId}")
    int delete(String teacherId);
}