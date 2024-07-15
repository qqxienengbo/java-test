package com.example.testdpi.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.testdpi.entity.Label;
import com.example.testdpi.entity.Menu_Bar;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface LabelDao extends BaseMapper<Label> {
    @Select("select * from label")
    @Results({
        @Result(column = "labelid",property = "labelid"),
        @Result(column = "type",property = "type"),
        @Result(column = "labelname",property = "labelname"),
        @Result(column = "rows",property = "rows"),
        @Result(column = "options",property = "options"),
        @Result(column = "menubarid",property = "menubarid"),
        @Result(column = "checkboxs",property = "checkboxs"),
        @Result(column = "menubarid",property = "menuBar",javaType = Menu_Bar.class,
            one = @One(select = "com.example.testdpi.dao.Menu_BarDao.selectById")),

    })
    List<Label> getall();
}
