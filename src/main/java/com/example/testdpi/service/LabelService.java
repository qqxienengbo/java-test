package com.example.testdpi.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.testdpi.dao.LabelDao;
import com.example.testdpi.entity.Label;
import com.example.testdpi.exception.CustomException;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class LabelService {
    @Resource
    private LabelDao labelDao;

    public void addlabel(Map<String, Object> onelabel, int menuBarid) {

        try {
            Label label=new Label();
//            下面的数据一定不为空
            label.setLabelname(onelabel.get("labelname").toString());
            label.setType(onelabel.get("type").toString());
            label.setMenubarid(menuBarid);
//            下面的数据有可能为空，需要做判断
            label.setRows(Integer.parseInt(onelabel.get("rows").toString()));
            label.setOptions(onelabel.get("options").toString());
            label.setCheckboxs(onelabel.get("checkboxs").toString());
            labelDao.insert(label);
        }catch (Exception e){
            throw new CustomException("创建label失败！");
        }

    }

    public Label getbymid(Integer menuBarid) {
        QueryWrapper<Label> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("menubarid",menuBarid);
        System.out.println(labelDao.selectOne(queryWrapper));
        return labelDao.selectOne(queryWrapper);
    }

    public List<Label> getall() {
        return labelDao.getall();
    }

}
