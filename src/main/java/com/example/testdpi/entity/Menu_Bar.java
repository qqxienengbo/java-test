package com.example.testdpi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("menu_bar")
//@Data自动生成Java Bean的getters和setters方法、equals方法、hashCode方法以及toString方法
@Data
public class Menu_Bar {
    //菜单表
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;//名称
    private String title;//父级
    private String icon;//图标
    private String inde;//0-文件夹，1-跳转页面
    private String jurisdiction;//权限
    private String navigation;//路由导航
    private Integer menulevel;//菜单等级
    private Long list_id;

}
