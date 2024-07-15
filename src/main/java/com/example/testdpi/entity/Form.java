package com.example.testdpi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.lang.reflect.Type;

@Data
@TableName("form")
public class Form {

    @TableId(type = IdType.AUTO)
    private Integer formid;
    private String labellist;
    private Integer menubarid;
}
