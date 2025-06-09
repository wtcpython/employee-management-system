package com.company.pojo;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    private Integer id; // 编号
    private String name; // 姓名
    private String position; // 职位
    private LocalDate birthday; // 出生日期
    private String sex; // 性别
    private Integer deptId = 0; // 所属部门编号
    private Double salary = 0.0; // 工资
    private String phoneNumber; // 电话
}
