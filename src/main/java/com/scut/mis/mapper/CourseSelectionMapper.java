package com.scut.mis.mapper;

import com.scut.mis.entity.CourseSelection;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CourseSelectionMapper {

    /**
     * 查询所有选课记录
     */
    @Select("SELECT * FROM course_selection")
    @Results(id = "courseSelectionResult", value = {
            @Result(column = "student_id", property = "studentId"),
            @Result(column = "course_id", property = "courseId"),
            @Result(column = "teacher_id", property = "teacherId"),
            @Result(column = "chosen_year", property = "chosenYear")
    })
    List<CourseSelection> findAll();

    /**
     * 根据学生ID查询选课记录
     */
    @Select("SELECT * FROM course_selection WHERE student_id = #{studentId}")
    @ResultMap("courseSelectionResult")
    List<CourseSelection> findByStudentId(String studentId);

    /**
     * 根据课程ID查询选课记录
     */
    @Select("SELECT * FROM course_selection WHERE course_id = #{courseId}")
    @ResultMap("courseSelectionResult")
    List<CourseSelection> findByCourseId(String courseId);

    /**
     * 根据学生ID和课程ID查询选课记录
     */
    @Select("SELECT * FROM course_selection WHERE student_id = #{studentId} AND course_id = #{courseId}")
    @ResultMap("courseSelectionResult")
    CourseSelection findByStudentAndCourse(@Param("studentId") String studentId, @Param("courseId") String courseId);

    /**
     * 插入选课记录
     */
    @Insert("INSERT INTO course_selection(student_id, course_id, teacher_id, chosen_year, score) " +
            "VALUES(#{studentId}, #{courseId}, #{teacherId}, #{chosenYear}, #{score})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(CourseSelection courseSelection);

    /**
     * 更新成绩
     */
    @Update("UPDATE course_selection SET score=#{score} WHERE id=#{id}")
    int updateScore(CourseSelection courseSelection);

    /**
     * 根据ID删除选课记录
     */
    @Delete("DELETE FROM course_selection WHERE id=#{id}")
    int delete(Integer id);

    /**
     * 根据学生ID删除所有选课记录（用于学生退学）
     */
    @Delete("DELETE FROM course_selection WHERE student_id = #{studentId}")
    int deleteByStudentId(String studentId);
}