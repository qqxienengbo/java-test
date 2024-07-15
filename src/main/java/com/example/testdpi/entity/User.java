package com.example.testdpi.entity;

import cn.hutool.core.annotation.Alias;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.poi.excel.ExcelUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.testdpi.dao.UserDao;
import jakarta.annotation.Resource;
import lombok.Data;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

//在SQL server中user是关键字所以需要加上[]
@TableName("[user]")
@Data
public class User {
    @Alias("userid")//导入Excel文件时，用于识别字段名，做好分类
    @TableId(type = IdType.AUTO)
    private Integer userid;
    @Alias("username")
    private String username;
    @Alias("password")
    private String password;
    @Alias("role")
    private String role;
    @TableField(exist = false)
    private String token;

}
