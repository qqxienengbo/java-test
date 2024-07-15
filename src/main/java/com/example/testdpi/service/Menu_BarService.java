package com.example.testdpi.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.testdpi.common.Result;
import com.example.testdpi.dao.FormDao;
import com.example.testdpi.dao.LabelDao;
import com.example.testdpi.dao.Menu_BarDao;
import com.example.testdpi.entity.*;
import com.example.testdpi.exception.CustomException;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class Menu_BarService {
    @Resource
    private Menu_BarDao menuBarDao;
    @Resource
    private LabelDao labelDao;
    @Resource
    private FormDao formDao;


    public List<Menu_Bar> SelectAll(){
        return menuBarDao.selectList(null);
    }

    public List getmenu(User user) {
        QueryWrapper<Menu_Bar> queryWrapper=new QueryWrapper<>();
//        查询该权限的最高级目录
        queryWrapper.like("jurisdiction",user.getRole()).eq("menulevel",0);
        List<Menu_Bar> menu_barlist=menuBarDao.selectList(queryWrapper);
        List list=new ArrayList();
        for (int i=0;i<menu_barlist.size();i++){
//            给每个最高级的目录赋值其信息
            MenuList menuList=new MenuList();
            menuList.setId(menu_barlist.get(i).getId());
            menuList.setName(menu_barlist.get(i).getName());
            menuList.setIcon(menu_barlist.get(i).getIcon());
            menuList.setInde(menu_barlist.get(i).getInde());
            menuList.setMenulevel(menu_barlist.get(i).getMenulevel());
            menuList.setJurisdiction(menu_barlist.get(i).getJurisdiction());
//            判断是导航还是文件夹,这里必须使用equale，使用==的判断结果完全相反
            if (menu_barlist.get(i).getInde().equals("0")){
//                System.out.println("一级菜单是文件夹"+"Inde="+menu_barlist.get(i).getInde());
//                如果=0，则是文件夹,添加子菜单
                menuList.setChildren(Menu(menu_barlist.get(i)));
            }else {
//                否则，跳转页面，给其赋值路由导航
                menuList.setNavigationlxj(menu_barlist.get(i).getNavigation());
//                System.out.println("一级菜单是导航"+"Inde="+menu_barlist.get(i).getInde());
            }
            list.add(menuList);
        }

        return list;
    }

//    添加字节点
    public List Menu(Menu_Bar menuBar){
        List list=new ArrayList();
        QueryWrapper<Menu_Bar> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("title",menuBar.getName());
        List<Menu_Bar> menu_barList=menuBarDao.selectList(queryWrapper);
        for (int i=0;i<menu_barList.size();i++){
            MenuList menuList=new MenuList();
            menuList.setId(menu_barList.get(i).getId());
            menuList.setName(menu_barList.get(i).getName());
            menuList.setIcon(menu_barList.get(i).getIcon());
            menuList.setInde(menu_barList.get(i).getInde());
            menuList.setMenulevel(menu_barList.get(i).getMenulevel());
            menuList.setJurisdiction(menu_barList.get(i).getJurisdiction());
//            判断是导航还是文件夹
            if (menu_barList.get(i).getInde().equals("0")){
//                如果=0，则是文件夹,添加子菜单
                menuList.setChildren(Menu(menu_barList.get(i)));//递归
            }else {
//                否则，是跳转页面，给其赋值路由导航
                menuList.setNavigationlxj(menu_barList.get(i).getNavigation());
            }
            list.add(menuList);
        }
        return list;
    }

//    重命名
    public void rename(MenuList menuList) {
//        先判断修改的名字是否有重复的
        QueryWrapper<Menu_Bar> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("name",menuList.getName());
        Menu_Bar menuBar=menuBarDao.selectOne(queryWrapper);
        if (menuBar!=null){
            throw new CustomException("文件名已存在，请重新命名！");
        }
//        获取原文件名
        String oname=menuBarDao.selectById(menuList.getId()).getName();
//        更新当前文件/文件夹
        UpdateWrapper<Menu_Bar> updateWrapper=new UpdateWrapper<>();
        updateWrapper.eq("id",menuList.getId()).set("name",menuList.getName());
        menuBarDao.update(updateWrapper);
//        更新子文件
        UpdateWrapper<Menu_Bar> cUpdateWrapper=new UpdateWrapper<>();
        cUpdateWrapper.eq("title",oname).set("title",menuList.getName());
        menuBarDao.update(cUpdateWrapper);
    }

//    添加文件或文件夹
    public void addFile(Menu_File file) {
        Menu_Bar addmenuBar=new Menu_Bar();
        QueryWrapper<Menu_Bar> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("name",file.getName());
        Menu_Bar menuBar=menuBarDao.selectOne(queryWrapper);
        if (menuBar!=null){
            throw new CustomException("文件名已存在，请重新命名！");
        }
        addmenuBar.setName(file.getName());
        addmenuBar.setTitle(file.getTitle());
        addmenuBar.setInde(file.getInde());
        addmenuBar.setMenulevel(file.getMenulevel());
        addmenuBar.setJurisdiction("HYEC_A;HYEC_B");
        if (file.getInde().equals("1")){
            addmenuBar.setNavigation("Property");
            addmenuBar.setIcon("document");
        }else{
            addmenuBar.setIcon("folder");
        }
        menuBarDao.insert(addmenuBar);
    }

//    删掉该节点中删除绑定在其节点的信息
    public void DeleteFile(Menu_Bar menuBar){
//        判断该节点是否绑定了label和form内容，是的话删掉,否则直接删除节点
        if(menuBar.getNavigation()!=null){
            if (menuBar.getNavigation().equals("Show_Label")){
//            删除label
                QueryWrapper<Label> queryWrapper=new QueryWrapper<>();
                queryWrapper.eq("menubarid",menuBar.getId());
                labelDao.delete(queryWrapper);
            }
            if (menuBar.getNavigation().equals("Show_Form")){
//            删除form
                QueryWrapper<Form> queryWrapper=new QueryWrapper<>();
                queryWrapper.eq("menubarid",menuBar.getId());
                formDao.delete(queryWrapper);
            }
        }
//        删掉该节点
        menuBarDao.deleteById(menuBar);
    }
//    删掉该节点的方法
    public void del(Integer menuBarid) {
        Menu_Bar menuBar=menuBarDao.selectById(menuBarid);
        if (menuBar!=null){
            if (menuBar.getInde().equals("0")){
//                是文件夹，删掉该文件夹及其所有子节点
                delChildren(menuBar);
            }
//            删掉该文件
            DeleteFile(menuBar);
        }
    }

//    删掉子节点
    public void delChildren(Menu_Bar menuBar){
        QueryWrapper<Menu_Bar> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("title",menuBar.getName());
        List<Menu_Bar> list=menuBarDao.selectList(queryWrapper);
        for (int i=0;i<list.size();i++){
            if (list.get(i).getInde().equals("0")){
//                是文件夹，继续递归删掉子节点
                delChildren(list.get(i));
            }
//            删掉该节点
            DeleteFile(menuBar);
        }
    }

//    添加label
    public Integer addlabelfile(Map<String,Object> labelfile){
        try {
            Menu_Bar addmenuBar=new Menu_Bar();
            QueryWrapper<Menu_Bar> queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("name",labelfile.get("name"));
            Menu_Bar menuBar=menuBarDao.selectOne(queryWrapper);
            if (menuBar!=null){
                return -1;
            }
            addmenuBar.setName(labelfile.get("name").toString());
            addmenuBar.setNavigation(labelfile.get("navigation").toString());
            addmenuBar.setTitle(labelfile.get("title").toString());
            addmenuBar.setInde(labelfile.get("inde").toString());
            addmenuBar.setMenulevel(Integer.parseInt(labelfile.get("menulevel").toString()));//转型
            addmenuBar.setJurisdiction("HYEC_A;HYEC_B");
            addmenuBar.setIcon("document");
            menuBarDao.insert(addmenuBar);
            return addmenuBar.getId();//成功添加时返回自增长的id
        }catch (Exception e){
            return -1;
        }
    }

//    添加form文件
public Integer AddFormFile(Map<String, Object> formfile){
        try {
            Menu_Bar addmenuBar=new Menu_Bar();
            QueryWrapper<Menu_Bar> queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("name",formfile.get("name"));
            Menu_Bar menuBar=menuBarDao.selectOne(queryWrapper);
            if (menuBar!=null){
                return -1;
            }
            addmenuBar.setName(formfile.get("name").toString());
            addmenuBar.setNavigation(formfile.get("navigation").toString());
            addmenuBar.setTitle(formfile.get("title").toString());
            addmenuBar.setInde(formfile.get("inde").toString());
            addmenuBar.setMenulevel(Integer.parseInt(formfile.get("menulevel").toString()));//转型
            addmenuBar.setJurisdiction("HYEC_A;HYEC_B");
            addmenuBar.setIcon("document");
            menuBarDao.insert(addmenuBar);
            return addmenuBar.getId();
        }catch (Exception e){
            return -1;
        }

    }
}
