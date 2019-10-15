package com.health.job;

import com.alibaba.dubbo.config.annotation.Reference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.health.Constant.RedisConstant;
import com.health.Uitls.QiNiuUtil;
import com.health.interfaces.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Map;
import java.util.Set;

public class MyQuartz {
    @Autowired
    private JedisPool jedisPool;

    @Reference
    private ReportService reportService;

    /**
     * 定时处理不必要的数据，一般存在redis中的数据都清除。而存在七牛云上的只要清除垃圾文件即可
     */
    public void deletePhoto() {
        Jedis jedis = jedisPool.getResource();
        Set<String> pics = jedis.sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        if (pics != null) {
            //删除redis中所有文件
            jedis.srem(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);
            /*jedis.del(RedisConstant.SETMEAL_PIC_RESOURCES);
            jedis.del(RedisConstant.SETMEAL_PIC_DB_RESOURCES);*/
            //删除七牛云上多余的图片
            QiNiuUtil.removeFiles(pics.toArray(new String[]{}));
        }else {
            System.out.println("redis中无垃圾图片");
        }
    }

    /**
     *调用业务服务，先统计运营数据，并存放到redis中，以解决高并发。
     */
    public void getBusinessReportData() {
        Map<String, Object> reportData = reportService.getBusinessReportData();
        Jedis jedis = jedisPool.getResource();
        Gson gson = new Gson();
        if (reportData != null) {
            String reportDataJson = gson.toJson(reportData);
            jedis.set(RedisConstant.BUSINESS_DATA_RESOURCES,reportDataJson);
            System.out.println("存储成功");
        }else{
            System.out.println("获取运营数据失败");
        }
        jedis.close();
    }

    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-quartz.xml");
    }
}
