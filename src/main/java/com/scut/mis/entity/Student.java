package com.scut.mis.entity;

import lombok.Data;

@Data
public class Student {
    private String studentId;
    private String name;
    private String sex;
    private Integer entranceAge;
    private Integer entranceYear;
    private String studentClass;
}
