package com.example.testdpi.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.testdpi.dao.FormDao;
import com.example.testdpi.dao.FormDataDao;
import com.example.testdpi.dao.LabelDao;
import com.example.testdpi.dao.Menu_BarDao;
import com.example.testdpi.entity.*;
import com.example.testdpi.exception.CustomException;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;

@Service
public class FormService {
    @Resource
    private FormDao formDao;
    @Resource
    private LabelDao labelDao;
    @Resource
    private FormDataDao formDataDao;
    @Resource
    private Menu_BarDao menuBarDao;

    public void AddForm(Object labellist, int menubarid) {
        Form form=new Form();
        form.setMenubarid(menubarid);
        form.setLabellist(labellist.toString());
        formDao.insert(form);
    }

    public List<Label> GetLabelListByMenuId(Integer menuid) {
        QueryWrapper<Form> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("menubarid",menuid);
        Form form=formDao.selectOne(queryWrapper);
//        在这里继续，下面写获取到label的id，查询每一个label的数据再传回前端
        List<Label> list=new ArrayList<>();
        if(form!=null){
            String labellist[]=form.getLabellist().split(";");
            for (String label :labellist){
                Label label1=labelDao.selectById(label);
                if (label1!=null){
                    list.add(label1);
                }
            }
        }
        return list;
    }

//    导出表单
    public void Export(HttpServletResponse response, Integer menuid) throws IOException {
//        先获取所有label的id
        QueryWrapper<Form> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("menubarid",menuid);
        Form form=formDao.selectOne(queryWrapper);
//        根据labelid查询每个label的labelname
        List<LinkedHashMap<String,Object>> excelform=new ArrayList<>();
        if(form!=null){
            String labellist[]=form.getLabellist().split(";");
            LinkedHashMap<String,Object> row= new LinkedHashMap<>();
            for (String label :labellist){
                Label label1=labelDao.selectById(label);
                if (label1!=null){
                    row.put(label1.getLabelname(),null);
                }
            }
            excelform.add(row);
        }
        //把excellist数据用Weiter写出来
        ExcelWriter excelWriter= ExcelUtil.getWriter(true);
        excelWriter.write(excelform,true);

        QueryWrapper<Menu_Bar> menuqueryWrapper=new QueryWrapper<>();
        menuqueryWrapper.eq("id",menuid);
        Menu_Bar menuBar=menuBarDao.selectOne(menuqueryWrapper);
//        这里URLEncoder.encode可以防止中文乱码
        String fileName= URLEncoder.encode(menuBar.getName(), "UTF-8").replaceAll("\\+", "%20");
//        把Excel在浏览器下载下来
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-Disposition","attachment;filename="+fileName+".xlsx");
        ServletOutputStream outputStream=response.getOutputStream();
        excelWriter.flush(outputStream,true);
        excelWriter.close();
        IoUtil.close(System.out);
    }


//    导入form表单数据
    public void upload(MultipartFile file,Integer menuid) throws IOException {
        QueryWrapper<Form> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("menubarid",menuid);
        Form form=formDao.selectOne(queryWrapper);
        if (form!=null) {
//        读取Excel文件
            try {
                List<Map<String, Object>> list = ExcelUtil.getReader(file.getInputStream()).readAll();
                if (!CollectionUtil.isEmpty(list)) {
                    for (int i = 0; i < list.size(); i++) {
                        Map<String, Object> row = list.get(i);
                        StringBuilder stringBuilder = new StringBuilder();
                        for (Object value : row.values()) {
//                    把每行的信息变成:value1;value2;value3;
                            stringBuilder.append(value).append(";");
                        }
//                    把每条信息添加到数据库
                        FormData formData = new FormData();
                        formData.setFormdata(stringBuilder.toString());
                        formData.setFormid(form.getFormid());
                        formDataDao.insert(formData);
                    }
                }
            } catch (Exception e) {
            }
        }
    }

//    读取导入的excel文件内容，传回数据用于预览
    public List<Map<String,Object>> ReturnExcelData(MultipartFile file) throws IOException {
        List<Map<String, Object>> list = ExcelUtil.getReader(file.getInputStream()).readAll();
        return list;
    }

//    获取格式化好的excel文件内容,把数据一个个存入数据库
    public void newupload(List<Map<String, Object>> tableData, Integer menuid) {
        QueryWrapper<Form> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("menubarid",menuid);
        Form form=formDao.selectOne(queryWrapper);
        if (form!=null){
            try {
                if (!CollectionUtil.isEmpty(tableData)) {
                    for (int i = 0; i < tableData.size(); i++) {
                        Map<String, Object> row = tableData.get(i);
                        StringBuilder stringBuilder = new StringBuilder();
                        for (Object value : row.values()) {
//                    把每行的信息变成:value1;value2;value3;
                            stringBuilder.append(value).append(";");
                        }
//                    把每条信息添加到数据库
                        FormData formData = new FormData();
                        formData.setFormdata(stringBuilder.toString());
                        formData.setFormid(form.getFormid());
                        formDataDao.insert(formData);
                    }
                }
            }catch (Exception e){
                throw new CustomException("存储数据失败！");
            }
        }else {
            throw new CustomException("没有对应的表单模板!");
        }
    }

    public List<FormData> GetAllFormDataByMenuId(Integer menuid) {
//        根据menuid找到form
        QueryWrapper<Form> formqueryWrapper=new QueryWrapper<>();
        formqueryWrapper.eq("menubarid",menuid);
        Form form=formDao.selectOne(formqueryWrapper);
//        根据formid，获取这个form的所有数据
        QueryWrapper<FormData> formDataQueryWrapper=new QueryWrapper<>();
        formDataQueryWrapper.eq("formid",form.getFormid());
        List<FormData> list=formDataDao.selectList(formDataQueryWrapper);
        return list;
    }
}
