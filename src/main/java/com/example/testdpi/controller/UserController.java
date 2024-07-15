package com.example.testdpi.controller;


import com.example.testdpi.common.Result;
import com.example.testdpi.entity.User;
import com.example.testdpi.exception.CustomException;
import com.example.testdpi.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    @PostMapping("/login")
    public Result login(@RequestBody User user){
            User loginuser=userService.login(user);
            return Result.success(loginuser);
    }

    @GetMapping("/getall")
    public Result getAll(){
        List<User> list=userService.getAll();
        return Result.success(list);
    }

//    导出Excel
    @GetMapping("/export")
    public void exp(HttpServletResponse response) throws IOException {
        userService.exp1(response);
//        userService.exp2(response);
    }

//    导入Excel
    @PostMapping("/upload")
    public Result upload(MultipartFile file) throws IOException {
        userService.upload(file);
        return Result.success();
    }
//    测试导入excel
    @PostMapping("/uploadtest")
    public Result uploadTest(MultipartFile file) throws IOException {
//        userService.uploadtest(file);
        userService.uploadPipeline(file);
        return Result.success();
    }
}
