package com.example.testdpi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("formdata")
@Data
public class FormData {

    @TableId(type = IdType.AUTO)
    private Integer formdataid;
    private String formdata;
    private Integer formid;
}
