package com.health.dao;

import com.health.pojo.Order;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface OrderDao {
    /**
     * 根据会员id、套餐id、预约日期查询是否已经预约
     *
     * @param
     * @return
     */
    Order FindOrderByOrderDateAndMemberIdAndPackageId(Map<String, Object> orderMap);

    /**
     * 添加下单信息
     *
     * @param order
     */
    void AddOrder(Order order);

    /**
     * 根据id查询订单信息
     *
     * @param id
     * @return
     */
    Map<String, Object> FindById(int id);


    /**
     * 今日预约数
     *
     * @param reportDate
     * @return
     */
    Integer getTodayOrderNumber(String reportDate);

    /**
     * 今日到诊数
     *
     * @param reportDate
     * @return
     */
    Integer getTodayVisitsNumber(String reportDate);

    /**
     * 本周预约数/本月预约数
     *
     * @param startDay
     * @param endDay
     * @return
     */
    Integer getThisWeekOrMonthOrderNumber(@Param("startDay") String startDay, @Param("endDay") String endDay);

    /**
     * 本周到诊数/本月到诊数
     *
     * @param startDay
     * @param endDay
     * @return
     */
    Integer getThisWeekOrMonthVisitsNumber(@Param("startDay") String startDay, @Param("endDay") String endDay);

    /**
     * 热门套餐：取出预约人数倒序排序前4个
     *
     * @return
     */
    List<Map<String, Object>> getHotPackage();
}
