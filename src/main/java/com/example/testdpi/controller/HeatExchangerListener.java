package com.example.testdpi.controller;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;

import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

/***
 * 监听器
 */
@Slf4j
public class HeatExchangerListener implements ReadListener<Map<Integer, String>> {

    Map<String, String> dataMap = new HashMap<>();
    Map<String, String> unitMap = new HashMap<>();
//    Map<String, String> dataMap = new LinkedHashMap<>();
//    Map<String, String> unitMap = new LinkedHashMap<>();
    String shellSideInletPhase = null;
    String shellSideExportPhase = null;
    String tubeSideInletPhase = null;
    String tubeSideExportPhase = null;

    @Override
    public void invoke(Map<Integer, String> data, AnalysisContext context) {
        // 获取当前行号,行号是从1开始的
        int rowIndex = context.readRowHolder().getRowIndex();
//        System.out.println(rowIndex);
//        System.out.println(data);
        if (rowIndex == 8) {
            dataMap.put("size_diameter", data.get(9));
            unitMap.put("size_diameter",data.get(17));
            // 3=data.get(9)
            dataMap.put("size_length", data.get(13));
            unitMap.put("size_length",data.get(17));
            // 4=data.get(13)
            dataMap.put("shell_type", data.get(24));
            // 7=data.get(24)
            dataMap.put("parallel_units", data.get(49));
            // 109=data.get(49)
            dataMap.put("series_units", data.get(58));
            // 110=data.get(58)
        } else if (rowIndex == 9) {
            System.out.println(data);
            // 118=data.get(10)
            dataMap.put("effective_area_per_unit", data.get(15));
            unitMap.put("size_length",data.get(19));
            // 总传热面积
            dataMap.put("total_heat_transfer_area", data.get(10));
            unitMap.put("size_length",data.get(19));
        } else if (rowIndex == 13) {
            // 10=data.get(19)
            dataMap.put("shell_side_total_flow", data.get(19));
            unitMap.put("shell_side_total_flow", data.get(12));
            // 23=data.get(43)
            dataMap.put("tube_side_total_flow", data.get(43));
            unitMap.put("tube_side_total_flow", data.get(12));
        } else if (rowIndex == 14) {
            // 13=data.get(19)
            dataMap.put("shell_side_inlet_vapor_gas_flow", data.get(19));
            // 14=data.get(31)
            dataMap.put("shell_side_outlet_vapor_gas_flow", data.get(31));
            // 26=data.get(43)
            dataMap.put("tube_side_inlet_vapor_gas_flow", data.get(43));
            // 27=data.get(55)
            dataMap.put("tube_side_outlet_vapor_gas_flow", data.get(55));
        } else if (rowIndex == 15) {
            // 11=data.get(19)
            dataMap.put("shell_side_inlet_liquid_flow", data.get(19));
            // 12=data.get(31)
            dataMap.put("shell_side_outlet_liquid_flow", data.get(31));
            // 24=data.get(43)
            dataMap.put("tube_side_inlet_liquid_flow", data.get(43));
            // 25=data.get(55)
            dataMap.put("tube_side_outlet_liquid_flow", data.get(55));
        } else if (rowIndex == 19) {
            // 20=data.get(19)
            dataMap.put("shell_side_inlet_operating_temperature", data.get(19));
            unitMap.put("shell_side_inlet_operating_temperature", data.get(12));
            // 21=data.get(31)
            dataMap.put("shell_side_outlet_operating_temperature", data.get(31));
            unitMap.put("shell_side_outlet_operating_temperature", data.get(12));
            // 33=data.get(43)
            dataMap.put("tube_side_inlet_operating_temperature", data.get(43));
            unitMap.put("tube_side_inlet_operating_temperature", data.get(12));
            // 34=data.get(55)
            dataMap.put("tube_side_outlet_operating_temperature", data.get(55));
            unitMap.put("tube_side_outlet_operating_temperature", data.get(12));
        } else if (rowIndex == 20) {
            // 首先是入口密度
            // 18 = data.get(19)
            dataMap.put("shell_side_density", data.get(19));
            unitMap.put("shell_side_density", data.get(12));
            // 31 = data.get(31)
            dataMap.put("tube_side_density", data.get(31));
            unitMap.put("tube_side_density", data.get(12));
            if (!ObjectUtil.isNull(shellSideInletPhase) && shellSideInletPhase.equals("v")) {
                // 37 壳气入
                dataMap.put("shell_side_inlet_gas_density", data.get(19));
                unitMap.put("shell_side_inlet_gas_density", data.get(12));
            } else if (!ObjectUtil.isNull(shellSideInletPhase) && shellSideInletPhase.equals("l")) {
                // 41 壳液入
                dataMap.put("shell_side_inlet_liquid_density", data.get(31));
                unitMap.put("shell_side_inlet_liquid_density", data.get(12));
            }
            if (!ObjectUtil.isNull(shellSideExportPhase) && shellSideExportPhase.equals("v")) {
                // 49 壳气出
                dataMap.put("shell_side_outlet_gas_density", data.get(43));
                unitMap.put("shell_side_outlet_gas_density", data.get(12));
            } else if (!ObjectUtil.isNull(shellSideExportPhase) && shellSideExportPhase.equals("l")) {
                // 53 壳液出
                dataMap.put("shell_side_outlet_liquid_density", data.get(55));
                unitMap.put("shell_side_outlet_liquid_density", data.get(12));
            }
            if (!ObjectUtil.isNull(tubeSideExportPhase) && tubeSideExportPhase.equals("v")) {
                // 64 管气入
                dataMap.put("tube_side_inlet_gas_density", data.get(19));
                unitMap.put("tube_side_inlet_gas_density", data.get(12));
            } else if (!ObjectUtil.isNull(tubeSideExportPhase) && tubeSideExportPhase.equals("l")) {
                // 68 管液入
                dataMap.put("tube_side_inlet_liquid_density", data.get(31));
                unitMap.put("tube_side_inlet_liquid_density", data.get(12));
            }
            if (!ObjectUtil.isNull(tubeSideExportPhase) && tubeSideExportPhase.equals("v")) {
                // 76 管气出
                dataMap.put("tube_side_outlet_gas_density", data.get(43));
                unitMap.put("tube_side_outlet_gas_density", data.get(12));
            } else if (!ObjectUtil.isNull(tubeSideExportPhase) && tubeSideExportPhase.equals("l")) {
                // 80 管液出
                dataMap.put("tube_side_outlet_liquid_density", data.get(55));
                unitMap.put("tube_side_outlet_liquid_density", data.get(12));
            }

        } else if (rowIndex == 21) {
            if (!ObjectUtil.isNull(shellSideInletPhase) && shellSideInletPhase.equals("v")) {
                // 34 气入
                dataMap.put("shell_side_inlet_gas_viscosity", data.get(19));
                unitMap.put("shell_side_inlet_gas_viscosity", data.get(12));
            } else if (!ObjectUtil.isNull(shellSideInletPhase) && shellSideInletPhase.equals("l")) {
                // 44 液入
                dataMap.put("shell_side_inlet_liquid_viscosity", data.get(31));
                unitMap.put("shell_side_inlet_liquid_viscosity", data.get(12));
            }
            if (!ObjectUtil.isNull(shellSideExportPhase) && shellSideExportPhase.equals("v")) {
                // 52 气出
                dataMap.put("shell_side_outlet_gas_viscosity", data.get(43));
                unitMap.put("shell_side_outlet_gas_viscosity", data.get(12));
            } else if (!ObjectUtil.isNull(shellSideExportPhase) && shellSideExportPhase.equals("l")) {
                // 56 液出
                dataMap.put("shell_side_outlet_liquid_viscosity", data.get(55));
                unitMap.put("shell_side_outlet_liquid_viscosity", data.get(12));
            }
            if (!ObjectUtil.isNull(tubeSideExportPhase) && tubeSideExportPhase.equals("v")) {
                // 67 气入
                dataMap.put("tube_side_inlet_gas_viscosity", data.get(19));
                unitMap.put("tube_side_inlet_gas_viscosity", data.get(12));
            } else if (!ObjectUtil.isNull(tubeSideExportPhase) && tubeSideExportPhase.equals("l")) {
                // 71 液入
                dataMap.put("tube_side_inlet_liquid_viscosity", data.get(31));
                unitMap.put("tube_side_inlet_liquid_viscosity", data.get(12));
            }
            if (!ObjectUtil.isNull(tubeSideExportPhase) && tubeSideExportPhase.equals("v")) {
                // 79 气出
                dataMap.put("tube_side_outlet_gas_viscosity", data.get(43));
                unitMap.put("tube_side_outlet_gas_viscosity", data.get(12));
            } else if (!ObjectUtil.isNull(tubeSideExportPhase) && tubeSideExportPhase.equals("l")) {
                // 83 液出
                dataMap.put("tube_side_outlet_liquid_viscosity", data.get(55));
                unitMap.put("tube_side_outlet_liquid_viscosity", data.get(12));
            }

        } else if (rowIndex == 24) {
            if (!ObjectUtil.isNull(shellSideInletPhase) && shellSideInletPhase.equals("v")) {
                // 38 气入
                dataMap.put("shell_side_inlet_gas_specific_heat_capacity", data.get(19));
                unitMap.put("shell_side_inlet_gas_specific_heat_capacity", data.get(12));
            } else if (!ObjectUtil.isNull(shellSideInletPhase) && shellSideInletPhase.equals("l")) {
                // 42 液入
                dataMap.put("shell_side_inlet_liquid_specific_heat_capacity", data.get(31));
                unitMap.put("shell_side_inlet_liquid_specific_heat_capacity", data.get(12));
            }
            if (!ObjectUtil.isNull(shellSideExportPhase) && shellSideExportPhase.equals("v")) {
                // 50 气出
                dataMap.put("shell_side_outlet_gas_specific_heat_capacity", data.get(43));
                unitMap.put("shell_side_outlet_gas_specific_heat_capacity", data.get(12));
            } else if (!ObjectUtil.isNull(shellSideExportPhase) && shellSideExportPhase.equals("l")) {
                // 54 液出
                dataMap.put("shell_side_outlet_liquid_specific_heat_capacity", data.get(55));
                unitMap.put("shell_side_outlet_liquid_specific_heat_capacity", data.get(12));
            }
            if (!ObjectUtil.isNull(tubeSideExportPhase) && tubeSideExportPhase.equals("v")) {
                // 65 气入
                dataMap.put("tube_side_inlet_gas_specific_heat_capacity", data.get(19));
                unitMap.put("tube_side_inlet_gas_specific_heat_capacity", data.get(12));
            } else if (!ObjectUtil.isNull(tubeSideExportPhase) && tubeSideExportPhase.equals("l")) {
                // 69 液入
                dataMap.put("tube_side_inlet_liquid_specific_heat_capacity", data.get(31));
                unitMap.put("tube_side_inlet_liquid_specific_heat_capacity", data.get(12));
            }
            if (!ObjectUtil.isNull(tubeSideExportPhase) && tubeSideExportPhase.equals("v")) {
                // 77 气出
                dataMap.put("tube_side_outlet_gas_specific_heat_capacity", data.get(43));
                unitMap.put("tube_side_outlet_gas_specific_heat_capacity", data.get(12));
            } else if (!ObjectUtil.isNull(tubeSideExportPhase) && tubeSideExportPhase.equals("l")) {
                // 81 液出
                dataMap.put("tube_side_outlet_liquid_specific_heat_capacity", data.get(55));
                unitMap.put("tube_side_outlet_liquid_specific_heat_capacity", data.get(12));
            }

        } else if (rowIndex == 25) {
            if (!ObjectUtil.isNull(shellSideInletPhase) && shellSideInletPhase.equals("v")) {
                // 39 气入
                dataMap.put("shell_side_inlet_gas_thermal_conductivity", data.get(19));
                unitMap.put("shell_side_inlet_gas_thermal_conductivity", data.get(12));
            } else if (!ObjectUtil.isNull(shellSideInletPhase) && shellSideInletPhase.equals("l")) {
                // 43 液入
                dataMap.put("shell_side_inlet_liquid_thermal_conductivity", data.get(31));
                unitMap.put("shell_side_inlet_liquid_thermal_conductivity", data.get(12));
            }
            if (!ObjectUtil.isNull(shellSideExportPhase) && shellSideExportPhase.equals("v")) {
                // 51 气出
                dataMap.put("shell_side_outlet_gas_thermal_conductivity", data.get(43));
                unitMap.put("shell_side_outlet_gas_thermal_conductivity", data.get(12));
            } else if (!ObjectUtil.isNull(shellSideExportPhase) && shellSideExportPhase.equals("l")) {
                // 55 液出
                dataMap.put("shell_side_outlet_liquid_thermal_conductivity", data.get(55));
                unitMap.put("shell_side_outlet_liquid_thermal_conductivity", data.get(12));
            }
            if (!ObjectUtil.isNull(tubeSideExportPhase) && tubeSideExportPhase.equals("v")) {
                // 66 气入
                dataMap.put("tube_side_inlet_gas_thermal_conductivity", data.get(19));
                unitMap.put("tube_side_inlet_gas_thermal_conductivity", data.get(12));
            } else if (!ObjectUtil.isNull(tubeSideExportPhase) && tubeSideExportPhase.equals("l")) {
                // 70 液入
                dataMap.put("tube_side_inlet_liquid_thermal_conductivity", data.get(31));
                unitMap.put("tube_side_inlet_liquid_thermal_conductivity", data.get(12));
            }
            if (!ObjectUtil.isNull(tubeSideExportPhase) && tubeSideExportPhase.equals("v")) {
                // 78 气出
                dataMap.put("tube_side_outlet_gas_thermal_conductivity", data.get(43));
                unitMap.put("tube_side_outlet_gas_thermal_conductivity", data.get(12));
            } else if (!ObjectUtil.isNull(tubeSideExportPhase) && tubeSideExportPhase.equals("l")) {
                // 82 液出
                dataMap.put("tube_side_outlet_liquid_thermal_conductivity", data.get(55));
                unitMap.put("tube_side_outlet_liquid_thermal_conductivity", data.get(12));
            }

        } else if (rowIndex == 27) {
            // 89=data.get(27)
            dataMap.put("shell_side_operating_pressure", data.get(27));
            unitMap.put("shell_side_operating_pressure", data.get(12));
            // 90=data.get(51)
            dataMap.put("tube_side_operating_pressure", data.get(51));
            unitMap.put("tube_side_operating_pressure", data.get(12));
        } else if (rowIndex == 28) {
            // 19=data.get(27)
            dataMap.put("shell_side_flow_velocity", data.get(27));
            unitMap.put("shell_side_flow_velocity", data.get(12));
            // 32=data.get(51)
            dataMap.put("tube_side_flow_velocity", data.get(51));
            unitMap.put("tube_side_flow_velocity", data.get(12));
        } else if (rowIndex == 29) {
            dataMap.put("shell_side_allowable_pressure_drop", data.get(19));
            unitMap.put("shell_side_allowable_pressure_drop", data.get(12));
            dataMap.put("shell_side_calculated_pressure_drop", data.get(31));
            unitMap.put("shell_side_calculated_pressure_drop", data.get(12));
            dataMap.put("tube_side_allowable_pressure_drop", data.get(43));
            unitMap.put("tube_side_allowable_pressure_drop", data.get(12));
            dataMap.put("tube_side_calculated_pressure_drop", data.get(55));
            unitMap.put("tube_side_calculated_pressure_drop", data.get(12));
        } else if (rowIndex == 30) {
            dataMap.put("shell_side_fouling_resistance", data.get(27));
            unitMap.put("shell_side_fouling_resistance", data.get(12));
            dataMap.put("tube_side_fouling_resistance", data.get(51));
            unitMap.put("tube_side_fouling_resistance", data.get(12));
        } else if (rowIndex == 31) {
            dataMap.put("heat_load", data.get(12));
            unitMap.put("heat_load", data.get(19));
            dataMap.put("effective_mean_temperature_difference", data.get(53));
            unitMap.put("effective_mean_temperature_difference", data.get(59));
        } else if (rowIndex == 32) {
            dataMap.put("overall_fouling_heat_transfer_coefficient", data.get(12));
            unitMap.put("overall_fouling_heat_transfer_coefficient", data.get(19));
            dataMap.put("overall_clean_heat_transfer_coefficient", data.get(33));
            unitMap.put("overall_clean_heat_transfer_coefficient", data.get(39));
            dataMap.put("actual_overall_heat_transfer_coefficient", data.get(53));
            unitMap.put("actual_overall_heat_transfer_coefficient", data.get(59));
        } else if (rowIndex == 35) {
            dataMap.put("shell_side_design_pressure", data.get(19));
            unitMap.put("shell_side_design_pressure", data.get(12));
            dataMap.put("tube_side_design_pressure", data.get(31));
            unitMap.put("tube_side_design_pressure", data.get(12));
        } else if (rowIndex == 36) {
            dataMap.put("shell_side_design_temperature", data.get(19));
            unitMap.put("shell_side_design_temperature", data.get(12));
            dataMap.put("tube_side_design_temperature", data.get(31));
            unitMap.put("tube_side_design_temperature", data.get(12));
        } else if (rowIndex == 37) {
            dataMap.put("number_of_passes_on_shell_side", data.get(19));
            dataMap.put("number_of_passes_on_tube_side", data.get(31));
        } else if (rowIndex == 38) {
            dataMap.put("shell_side_corrosion_allowance", data.get(19));
            unitMap.put("shell_side_corrosion_allowance", data.get(12));
            dataMap.put("tube_side_corrosion_allowance", data.get(31));
            unitMap.put("tube_side_corrosion_allowance", data.get(12));
        } else if (rowIndex == 42) {
            dataMap.put("number_of_heat_transfer_tubes_per_unit", data.get(5));
            dataMap.put("heat_transfer_tube_outer_diameter", data.get(13));
            unitMap.put("heat_transfer_tube_outer_diameter", data.get(17));
            dataMap.put("heat_transfer_tube_wall_thickness", data.get(28));
            unitMap.put("heat_transfer_tube_outer_diameter", data.get(33));
            dataMap.put("heat_transfer_tube_length", data.get(43));
            unitMap.put("heat_transfer_tube_length", data.get(48));
            dataMap.put("tube_pitch", data.get(58));
            unitMap.put("tube_pitch", data.get(63));
        } else if (rowIndex == 43) {

            if (!ObjectUtil.isNull(data.get(5))) {
                if (data.get(5).equals("Plain")) {
                    dataMap.put("heat_transfer_tube_type", "光管");
                } else if (data.get(5).equals("Finned")) {
                    dataMap.put("heat_transfer_tube_type", "翅片管");
                } else if (data.get(5).equals("Threaded")) {
                    dataMap.put("heat_transfer_tube_type", "螺纹管");
                }
            }
            // 管材质
            dataMap.put("tubes", data.get(33));
            // 换热管排列形式
            dataMap.put("heat_transfer_tube_arrangement", data.get(64));
        } else if (rowIndex == 44) {
            // 壳材质
            dataMap.put("shell", data.get(4));
            dataMap.put("shell_inner_diameter", data.get(20));
            unitMap.put("shell_inner_diameter", data.get(33));
        } else if (rowIndex == 47) {
            if (!ObjectUtil.isNull(data.get(48))) {
                dataMap.put("is_baffle_plate_installed_at_shell_inlet", data.get(48).equals("None") ? "不设" : "设");
            }
        } else if (rowIndex == 48) {
            // 折流板类型
            if (!ObjectUtil.isNull(data.get(21))) {
                if (data.get(21).equals("Single-Seg")) {
                    dataMap.put("baffle_plate_type", "单弓形");
                } else if (data.get(21).equals("Double-Seg")) {
                    dataMap.put("baffle_plate_type", "双弓形");
                } else if (data.get(21).equals("Square")) {
                    dataMap.put("baffle_plate_type", "环盘形");
                } else if (data.get(21).equals("Segmental") || data.get(21).equals("NTIW")) {
                    dataMap.put("is_tubes_sheeted_through_baffle_plates", "不布管");
                }
            }
            dataMap.put("baffle_plate_cut_ratio", data.get(38));
            dataMap.put("center_baffle_plate_spacing", data.get(49));
            unitMap.put("center_baffle_plate_spacing", data.get(63));
            dataMap.put("first_baffle_plate_spacing", data.get(58));
            unitMap.put("first_baffle_plate_spacing", data.get(63));

        }

    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        System.out.println("-----------------TEMA表所有数据解析完成！-----------------");
        // for (Map.Entry<String, String> entry : dataMap.entrySet()) {
        // String key = entry.getKey();
        // String value = entry.getValue();
        // System.out.println("Key: " + key + ", Value: " + value);
        // }
    }

    public Map<String, String> getData() {
        return dataMap;
    }
    public Map<String, String> getUnit() {
        return unitMap;
    }
}
