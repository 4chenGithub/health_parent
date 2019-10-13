package com.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.health.Constant.MessageConstant;
import com.health.Constant.RedisMessageConstant;
import com.health.Entity.Result;
import com.health.interfaces.OrderService;
import com.health.pojo.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.text.SimpleDateFormat;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Reference
    private OrderService orderService;

    @Autowired
    private JedisPool jedisPool;

    @PostMapping("/submit")
    public Result submitOrder(@RequestBody Map<String, String> map) {
        /*
        -- 验证码验证：
		controller：
		1、先判断前端发送过来的验证码是为空，如果是直接return false 提示结果
		2、不为空的话，再根据key取出redis中的验证码信息与发送过来的验证码进行比对
			如果不正确，则返回结果，提示验证码错误
		3、如果正确，调用业务服务处理请求，后清除redis中的验证码，防止被复用
		4、设置预约类型：微信预约 目的是不在业务服务中指定类型，让业务服务功能更加单一
         */
        String validateCode = map.get("validateCode");
        //判断提交过来的验证码是否为空
        if (validateCode == null) {
            //如果是,则终止程序,并返回提示信息
            return new Result(false, MessageConstant.NEED_VALIDATECODE);
        }
        //到这里说明验证码不为空
        //则取出redis中的验证码与之比对
        Jedis jedis = jedisPool.getResource();
        //Order_001_13760856979
        String key = "Order_" + RedisMessageConstant.SENDTYPE_ORDER + "_" + map.get("telephone");
        String code = jedis.get(key);
        if (!validateCode.equals(code)) {
            //说明验证码不一致
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }

        //在controller中设置好预约类型,而不在service服务中设置是为了让服务功能更加单一
        map.put("orderType", Order.ORDERTYPE_WEIXIN);
        //调用业务服务处理请求
        Order order = orderService.submitOrder(map);
        //提交订单成功
        //把redis中的验证码删除掉,防止被重复使用
        jedis.del(key);
        return new Result(true, MessageConstant.ORDER_SUCCESS, order);
    }


    @RequestMapping("/findById")
    public Result findById(int id) {
        //调用业务服务查询
        Map<String, Object> resultMap = orderService.FindById(id);
        if (resultMap != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            resultMap.put("orderDate",dateFormat.format(resultMap.get("orderDate")));
            return new Result(true, MessageConstant.QUERY_ORDER_SUCCESS, resultMap);
        }else {
            return new Result(false,MessageConstant.QUERY_ORDER_FAIL);
        }
    }
}
