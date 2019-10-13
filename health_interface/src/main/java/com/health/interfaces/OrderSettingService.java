package com.health.interfaces;


import com.health.Exception.ReadExcelException;
import com.health.pojo.OrderSetting;

import java.util.List;

public interface OrderSettingService {
    /**
     * 上传excel文件
     *
     * @param excel
     */
    void UploadFile(List<OrderSetting> excel) throws ReadExcelException;

    /**
     * 根据字符串模糊查询当月的所有预约信息
     * @param date
     * @return
     */
    List<OrderSetting> FindAllDate(String date);

    /**
     * 修改预约信息
     * @param orderSetting
     */
    void update(OrderSetting orderSetting);

}
