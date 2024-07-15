package com.example.testdpi.controller;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/***
 * 监听器
 */
@Slf4j
public class PipelineListener implements ReadListener<Map<Integer, String>> {

    List<Map<String,String>> list=new ArrayList<>();

    @Override
    public void invoke(Map<Integer, String> data, AnalysisContext context) {
        // 获取当前行号,行号是从1开始的
//        int rowIndex = context.readRowHolder().getRowIndex();
//        System.out.println(rowIndex);
//        System.out.println(data);
        Map<String,String> dataMap=new HashMap<>();
        dataMap.put("pipeline_number",data.get(1));
        dataMap.put("nominal_diameter",data.get(2));
        dataMap.put("pipeline_grade",data.get(3));
        dataMap.put("starting_point_of_pipeline",data.get(4));
        dataMap.put("pipeline_endpoint",data.get(5));
        dataMap.put("process_diagram_number",data.get(6));
        dataMap.put("normal_operating_temperature",data.get(7));
        dataMap.put("normal_operating_pressure",data.get(8));
        dataMap.put("maximum_operating_temperature",data.get(9));
        dataMap.put("maximum_operating_pressure",data.get(10));
        dataMap.put("design_temperature",data.get(11));
        dataMap.put("design_pressure",data.get(12));
        dataMap.put("media_name",data.get(13));
        dataMap.put("medium_phase_state",data.get(14));
        dataMap.put("medium_fire_hazard",data.get(15));
        dataMap.put("category_of_acute_toxicity_of_media",data.get(16));
        dataMap.put("insulation_code",data.get(17));
        dataMap.put("insulation_thickness",data.get(18));
        dataMap.put("painting_code",data.get(19));
        dataMap.put("pressure_testing_medium",data.get(20));
        dataMap.put("test_pressure",data.get(21));
        dataMap.put("sealing_medium",data.get(22));
        dataMap.put("sealing_pressure",data.get(23));
        dataMap.put("inspection_ratio",data.get(24));
        dataMap.put("other",data.get(25));
        dataMap.put("weld_qualification_level",data.get(26));
        dataMap.put("pipeline_category",data.get(27));
        dataMap.put("remark",data.get(28));

        list.add(dataMap);
//        dataMap.clear();
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        System.out.println("-----------------TEMA表所有数据解析完成！-----------------");

    }

    public List<Map<String,String>> getList(){return list;}

}
