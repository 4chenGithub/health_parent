package com.health.dao;


import com.health.pojo.OrderSetting;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface OrderSettingDao {
    /**
     * 添加预约信息
     * @param setting
     */
    void AddOrderSetting(OrderSetting setting);

    /**
     * 根据日期查询orderSetting对象
     * @param orderDate
     * @return
     */
    OrderSetting CheckOrderSettingByOrderDate(Date orderDate);

    /**
     * 根据日期更新数据
     * @param oldOrderSetting
     */
    void UpdateOrderSetting(OrderSetting oldOrderSetting);

    /**
     * 根据起始日期查询所有预约数据集合
     * @param beginDate
     * @param endDate
     * @return
     */
    List<OrderSetting> FindOrderSettingByDate(@Param("beginDate") String beginDate, @Param("endDate") String endDate);

    /**
     * 根据日期更新预约人数
     * @param date
     */
    void UpdateOrderSettingByOrderDate(Date date);
}
