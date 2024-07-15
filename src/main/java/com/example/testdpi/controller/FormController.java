package com.example.testdpi.controller;

import com.example.testdpi.common.Result;
import com.example.testdpi.entity.FormData;
import com.example.testdpi.entity.Label;
import com.example.testdpi.service.FormService;
import com.example.testdpi.service.Menu_BarService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/form")
public class FormController {
    @Resource
    private FormService formService;
    @Resource
    private Menu_BarService menu_barService;

    @PostMapping("/addform")
    private Result Addform(@RequestBody Map<String,Object> addform){
        int menubarid=menu_barService.AddFormFile(addform);
        if (menubarid!=-1){
//            添加文件成功，则添加form的内容
            formService.AddForm(addform.get("labellist"),menubarid);
        }else {
            return Result.error("文件名已存在，请重新命名！");
        }
        return Result.success();
    }

    @GetMapping("/getlabellistbymenuid")
    private Result GetLabelListByMenuId(@RequestParam Integer menuid){
        List<Label> list=formService.GetLabelListByMenuId(menuid);
        return Result.success(list);
    }

//导出excel模板
    @GetMapping("/export/{menuid}")
    private void FormExport(HttpServletResponse response, @PathVariable Integer menuid) throws IOException {
        formService.Export(response,menuid);
    }

//    批量导入excel
//    导入Excel
//    @PostMapping("/upload/{menuid}")
//    public Result upload(MultipartFile file,@PathVariable Integer menuid) throws IOException {
//        formService.upload(file,menuid);
//        return Result.success();
//    }

//传回excel的预览数据
    @GetMapping("/returenexceldata")
    public Result ReturenExcelData(MultipartFile file) throws IOException {
        formService.ReturnExcelData(file);
        return Result.success();
    }

//    导入excel，存储formdata数据
    @PostMapping("/upload/{menuid}")
    public Result upload(@RequestBody List<Map<String,Object>> tableData,@PathVariable Integer menuid){
        formService.newupload(tableData,menuid);
        return Result.success();
    }

//    获取这个form的所有数据
    @GetMapping("/getAllFormDataByMenuId")
    public Result GetAllFormDataByMenuId(@RequestParam Integer menuid){
        List<FormData> list=formService.GetAllFormDataByMenuId(menuid);
        return Result.success(list);
//        后端获取了数据，差前端页面的渲染
    }
}
