package com.example.testdpi.entity;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
//Tree控件节点的实体类
public class MenuList {
    private Integer id;//id
    private String name;//标题
    private String icon;//图标
    private String inde;//0-文件夹，1-跳转页面
    private String jurisdiction;//权限
    private String navigationlxj;//路由导航
    private Integer menulevel;//菜单等级
    private List children;//子集

}
