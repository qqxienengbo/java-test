package com.example.testdpi.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.testdpi.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDao extends BaseMapper<User> {
}
