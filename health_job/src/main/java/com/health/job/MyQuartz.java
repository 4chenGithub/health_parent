package com.health.job;

import com.health.Constant.RedisConstant;
import com.health.Uitls.QiNiuUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Set;

public class MyQuartz {
    @Autowired
    private JedisPool jedisPool;

    /**
     * 定时处理不必要的数据，一般存在redis中的数据都清除。而存在七牛云上的只要清除垃圾文件即可
     */
    public void deletePhoto() {
        System.out.println("00000");
        Jedis jedis = jedisPool.getResource();
        Set<String> pics = jedis.sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        if (pics != null) {
            //删除redis中所有文件
            jedis.srem(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);
            //删除七牛云上多余的图片
            QiNiuUtil.removeFiles(pics.toArray(new String[]{}));
        }
    }

    public static void main(String[] args) {
        ApplicationContext applicationContext=new ClassPathXmlApplicationContext("spring-quartz.xml");
    }
}
