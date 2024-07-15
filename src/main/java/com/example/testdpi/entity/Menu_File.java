package com.example.testdpi.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Menu_File {
    private String name;
    private String title;
    private String inde;
    private Integer menulevel;//菜单等级
    private String navigation;//导航
}
