package com.example.testdpi.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.druid.util.StringUtils;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.util.ListUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.testdpi.Util.JwtTokenUtils;
import com.example.testdpi.controller.HeatExchangerListener;
import com.example.testdpi.controller.PipelineListener;
import com.example.testdpi.dao.UserDao;
import com.example.testdpi.entity.User;
import com.example.testdpi.entity.UserExcel;
import com.example.testdpi.exception.CustomException;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.security.Key;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {
    @Resource
    private UserDao userDao;

    public User login(User user) {
        if (StringUtils.isEmpty(user.getUsername())){
            throw new CustomException("用户名不能为空！");
        }
        if (StringUtils.isEmpty(user.getPassword())){
            throw new CustomException("密码不能为空！");
        }
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("username",user.getUsername());
        queryWrapper.eq("password",user.getPassword());
        User admin=userDao.selectOne(queryWrapper);
        if (admin==null){
            throw new CustomException("用户名或密码错误！");
        }
        String token= JwtTokenUtils.genToken(admin.getUserid().toString(),admin.getPassword());
        admin.setToken(token);
        return admin;
    }

    public User findById(int id) {
        return userDao.selectById(id);
    }

    public List<User> getAll() {
        return userDao.selectList(null);
    }


//    利用hutool-all依赖写出的导出操作
    public void exp1(HttpServletResponse response) throws IOException {
//        从数据库获取到想要导出的数据
        List<User> list=userDao.selectList(null);
        if (CollectionUtil.isEmpty(list)){
           throw new CustomException("导出失败！");
        }
//        把数据装到List<Map<String,Object>>中
        List<Map<String,Object>> excellist=new ArrayList<>(list.size());
        for (User user:list){
            Map<String,Object> row= new HashMap<>();
            row.put("userid",user.getUserid());
            row.put("username",user.getUsername());
            row.put("password",user.getPassword());
            row.put("role",user.getRole());
            excellist.add(row);
        }
//把excellist数据用Weiter写出来
        ExcelWriter excelWriter= ExcelUtil.getWriter(true);
        excelWriter.write(excellist,true);

//        把Excel在浏览器下载下来
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-Disposition","attachment;filename=模板.xlsx");
        ServletOutputStream outputStream=response.getOutputStream();
        excelWriter.flush(outputStream,true);
        excelWriter.close();
        IoUtil.close(System.out);
    }

//    利用easyexcel依赖写出的导出操作
    public void exp2(HttpServletResponse response) throws IOException {
        List<User> list = userDao.selectList(null);
        if (CollectionUtil.isEmpty(list)) {
            throw new CustomException("导出失败！");
        }
        List<UserExcel> explist = ListUtils.newArrayList();
        for (User user : list) {
            UserExcel userExcel = new UserExcel();
            userExcel.setUserid(user.getUserid());
            userExcel.setUsername(user.getUsername());
            userExcel.setPassword(user.getPassword());
            userExcel.setRole(user.getRole());
            explist.add(userExcel);
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("模板", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), UserExcel.class).sheet("模板").doWrite(explist);
    }

    public void upload(MultipartFile file) throws IOException {
//        读取Excel文件
        List<User> list=ExcelUtil.getReader(file.getInputStream()).readAll(User.class);
        if (!CollectionUtil.isEmpty(list)){
//            一个个添加到数据库
            for (User user:list){
                try {
                    userDao.insert(user);
                }catch (Exception e){
                    e.printStackTrace();//报错了继续进行后续的添加
                }
            }
        }
    }

    public void uploadtest(MultipartFile file) throws IOException {
        HeatExchangerListener heatExchangerListener=new HeatExchangerListener();
//        String fileName = TestFileUtil.getPath() + "demo" + File.separator + "demo.xlsx";
//        EasyExcel.read(fileName, Map.class, new HeatExchangerListener()).sheet().doRead();
        EasyExcel.read(file.getInputStream(), heatExchangerListener).sheet(0).headRowNumber(7).doRead();
        Map<String,String> dataMap=heatExchangerListener.getData();
//        Map<String,String> unitMap=heatExchangerListener.getUnit();
//        Map<String,String> unitMap=heatExchangerListener.getUtil();
//        dataMap.forEach((key,value)-> System.out.println(key+"="+value+"("+unitMap.get(key)+")"));
        dataMap.forEach((key,value)-> System.out.println(key+"="+value));
//        unitMap.forEach((key,value)-> System.out.println(key+"="+value));
    }

    public void uploadPipeline(MultipartFile file) throws IOException {
        PipelineListener pipelineListener=new PipelineListener();
        EasyExcel.read(file.getInputStream(),pipelineListener).sheet().headRowNumber(7).doRead();
        List<Map<String,String>> list=pipelineListener.getList();
        for(Map<String,String> map:list){
            System.out.println(map);
        }
    }
}
