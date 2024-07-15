package com.example.testdpi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@TableName("label")
@Data
public class Label {
    @TableId(type = IdType.AUTO)
    private Integer labelid;
    private String type;
    private String labelname;
    private Integer rows;
    private String options;
    private Integer menubarid;
    private String checkboxs;
    @TableField(exist = false)
    private Menu_Bar menuBar;
}
