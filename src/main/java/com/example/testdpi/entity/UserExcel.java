package com.example.testdpi.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

//用于导出excel文件的类
@Data
@EqualsAndHashCode
public class UserExcel {
    @ExcelProperty("userid")
    private int userid;
    @ExcelProperty("username")
    private String username;
    @ExcelProperty("password")
    private String password;
    @ExcelProperty("role")
    private String role;
}
