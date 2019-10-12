package com.health.dao;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ReportDao {
    /**
     * 根据始末月份查询当前月份的会员数量集合
     *
     * @param endMonth
     * @return
     */
    int FindMemberCount(@Param("endMonth") String endMonth);

    /**
     * 查询套餐及其预约人数
     *
     * @return
     */
    List<Map<String, Object>> getPackageReport();


}
