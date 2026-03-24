package com.scut.mis.entity;

import lombok.Data;

@Data
public class CourseSelection {
    private Integer id;
    private String studentId;
    private String courseId;
    private String teacherId;
    private Integer chosenYear;
    private Double score;
}
