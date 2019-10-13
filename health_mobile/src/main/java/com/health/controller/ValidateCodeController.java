package com.health.controller;

import com.health.Constant.MessageConstant;
import com.health.Constant.RedisMessageConstant;
import com.health.Entity.Result;
import com.health.Uitls.SMSUtils;
import com.health.Uitls.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {
    @Autowired
    private JedisPool jedisPool;

    /**
     * 发送预约验证码
     * @param telephone
     * @return
     */
    @RequestMapping("/send4Order")
    public Result send4Order(String telephone) {
        //根据key查询redis中是否已经存在该验证码
        Jedis jedis = jedisPool.getResource();
        //"Order_001_13760856979
        String key = "Order_" + RedisMessageConstant.SENDTYPE_ORDER + "_" + telephone;
        String code = jedis.get(key);
        //防止重复发送短信
        if (code != null) {
            //说明发送过
            return new Result(false, MessageConstant.ALREADY_SEND_VALIDATECODE);
        } else {
            try {
                //未发送过
                //调用工具类生成验证码
                String validateCode = ValidateCodeUtils.generateValidateCode4String(6);
                SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE, telephone, validateCode);
                //发送短信成功,把验证码存到redis中
                jedis.setex(key, 5 * 60, validateCode);
                jedis.close();
                return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }


    }


    /**
     * 发送手机快捷登录验证码
     * @param telephone
     * @return
     */
    @RequestMapping("/send4Login")
    public Result send4Login(String telephone) {
        //根据key查询redis中是否已经存在该验证码
        Jedis jedis = jedisPool.getResource();
        //Login_002_13760856979
        String key = "Login_" + RedisMessageConstant.SENDTYPE_LOGIN + "_" + telephone;
        String code = jedis.get(key);
        //防止重复发送短信
        if (code != null) {
            //说明验证码已经发送过了
            return new Result(false, MessageConstant.ALREADY_SEND_VALIDATECODE);
        } else {
            try {
                //未发送过
                //调用工具类生成验证码
                String validateCode = ValidateCodeUtils.generateValidateCode4String(6);
                SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE, telephone, validateCode);
                //发送短信成功,把验证码存到redis中
                jedis.setex(key, 5 * 60, validateCode);
                jedis.close();
                return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }


    }
}
