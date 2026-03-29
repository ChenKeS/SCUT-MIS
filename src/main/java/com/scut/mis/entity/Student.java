package com.scut.mis.entity;

import lombok.Data;

@Data
public class Student {
    private String studentId;
    private String name;
    private String sex;
    private Integer entranceAge;
    private Integer entranceYear;
    private String studentClass;  // 改成 studentClass，避免 class 关键字
}
