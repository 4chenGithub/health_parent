package com.health.interfaces;

import java.util.List;
import java.util.Map;

public interface ReportService {
    /**
     * 查询当前月份集合每个月份对应的会员数量
     * @param months
     * @return
     */
    List<Integer> getMemberCount(List<String> months);

    /**
     * 查询套餐及其预约人数
     * @return
     */
    Map<String,Object> getPackageReport();

    /**
     * 获取运营统计数据
     * @return
     */
    Map<String,Object> getBusinessReportData();


    /**
     * 男女占比
     */
    List<Map<String, Object>> findSexCount();



    /**
     * 年龄段占比
     */
    List<Map<String, Object>> findAgeCount();

}
