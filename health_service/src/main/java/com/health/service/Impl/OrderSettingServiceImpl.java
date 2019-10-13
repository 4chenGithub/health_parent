package com.health.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;

import com.health.Exception.ReadExcelException;
import com.health.dao.OrderSettingDao;
import com.health.interfaces.OrderSettingService;
import com.health.pojo.OrderSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(interfaceClass = OrderSettingService.class)
public class OrderSettingServiceImpl implements OrderSettingService {

    @Autowired
    private OrderSettingDao orderSettingDao;

    /**
     * 上传excel文件
     *
     * @param excel
     * @throws ReadExcelException
     */
    @Transactional//使用事务控制
    @Override
    public void UploadFile(List<OrderSetting> excel) throws ReadExcelException {
        //遍历集合
        for (OrderSetting orderSetting : excel) {
            //查询当前的数据是否存在与数据库中
            OrderSetting OldOrderSetting = orderSettingDao.CheckOrderSettingByOrderDate(orderSetting.getOrderDate());
            //判断是否存在
            if (OldOrderSetting == null) {
                //数据库中不存在当前的数据，则插入数据
                orderSettingDao.AddOrderSetting(orderSetting);
            } else {
                //数据库中已经存在当前的数据，则根据日期更新数据，因为表中日期是唯一的，而id我们无法获取
                orderSettingDao.UpdateOrderSetting(OldOrderSetting);
            }
        }
    }

    /**
     * 根据日期范围查询当前月的所有预约信息集合
     *
     * @param date
     * @return
     */
    @Override
    public List<OrderSetting> FindAllDate(String date) {
        String beginDate = date + "-01";
        String endDate = date + "-31";
        return orderSettingDao.FindOrderSettingByDate(beginDate, endDate);
    }

    /**
     * 修改预约信息
     *
     * @param orderSetting
     */
    @Transactional
    @Override
    public void update(OrderSetting orderSetting) {
        //1 先判断当前的日期是否已经存在
        OrderSetting NewOrderSetting = orderSettingDao.CheckOrderSettingByOrderDate(orderSetting.getOrderDate());
        if (NewOrderSetting != null) {
            //说明数据库中已经存在,则更新
            orderSettingDao.UpdateOrderSetting(orderSetting);
        }else {
            //不存在,则插入
            orderSettingDao.AddOrderSetting(orderSetting);
        }
    }
}
