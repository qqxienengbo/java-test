package com.example.testdpi.controller;

import com.example.testdpi.common.Result;
import com.example.testdpi.entity.*;
import com.example.testdpi.service.LabelService;
import com.example.testdpi.service.Menu_BarService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/menu_bar")
public class Menu_BarController {
    @Resource
    private Menu_BarService menu_barService;
    @Resource
    private LabelService labelService;

    @GetMapping("/all")
    public Result test(){
        List<Menu_Bar> list=new ArrayList<>();
        list=menu_barService.SelectAll();
        return Result.success(list);
    }

    @PostMapping("/getmenu")
    private Result getmune(@RequestBody User user){
        List list=menu_barService.getmenu(user);
        System.out.println(list);
        return Result.success(list);
    }
//    重命名
    @PostMapping("/rename")
        private Result reName(@RequestBody MenuList menuList){
        menu_barService.rename(menuList);
        return Result.success();
    }
//    添加文件或文件夹
    @PostMapping("/addfile")
    private  Result addFile(@RequestBody Menu_File file){
        menu_barService.addFile(file);
        return Result.success();
    }
//    删除文件
    @DeleteMapping("/delete/{menu_barid}")
    private Result del(@PathVariable Integer menu_barid){
        menu_barService.del(menu_barid);
        return Result.success();
    }

//    新建label
    @PostMapping("/addlabel")
    private Result addLabel(@RequestBody Map<String,Map<String,Object>> addLabel){
//        执行添加label文件操作
        int menubarid=menu_barService.addlabelfile(addLabel.get("addlabelfile"));
        if (menubarid!=-1){
//            添加文件成功，则添加label的内容
            labelService.addlabel(addLabel.get("onelabel"),menubarid);
        }else {
            return Result.error("文件名已存在，请重新命名！");
        }
        return Result.success();
    }
}
