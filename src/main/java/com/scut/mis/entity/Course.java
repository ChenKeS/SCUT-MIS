package com.scut.mis.entity;

import lombok.Data;

@Data
public class Course {
    private String courseId;
    private String name;
    private String teacherId;
    private Double credit;
    private Integer grade;
    private Integer canceledYear;
}
