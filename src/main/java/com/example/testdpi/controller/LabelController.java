package com.example.testdpi.controller;

import com.example.testdpi.common.Result;
import com.example.testdpi.entity.Label;
import com.example.testdpi.service.LabelService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/label")
public class LabelController {
    @Resource
    private LabelService labelService;

    @GetMapping("/getlabel")
    private Result Getlabel(@RequestParam Integer menubarid){
        Label label=labelService.getbymid(menubarid);
        return Result.success(label);
    }

    @GetMapping("/getall")
    private Result GetAll(){
        List<Label> list=labelService.getall();
        return Result.success(list);
    }

}
