package com.health.interfaces;



import com.health.Exception.OrderException;
import com.health.pojo.Order;

import java.util.Map;

public interface OrderService {
    /**
     * 提交订单
     * @param map
     * @return
     */
    Order submitOrder(Map<String, String> map) throws OrderException;

    /**
     * 根据id查询订单信息
     * @param id
     * @return
     */
    Map<String,Object> FindById(int id);

}
