package com.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.StringUtil;
import com.health.Constant.MessageConstant;
import com.health.Constant.RedisMessageConstant;
import com.health.Entity.Result;
import com.health.interfaces.LoginService;
import com.health.pojo.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequestMapping("/login")
@SessionAttributes("member")
public class LoginController {
    @Reference
    private LoginService loginService;
    @Autowired
    private JedisPool jedisPool;

    @PostMapping("/check")
    public Result loginCheck(@RequestBody Map<String, String> loginInfo, HttpServletResponse response) {
        //对验证码进行非空判断
        String code = loginInfo.get("validateCode");
        if (StringUtil.isEmpty(code)) {
            //验证码为空时
            return new Result(false, MessageConstant.NEED_VALIDATECODE);
        }
        //取出redis中的验证码比对
        //根据key查询redis中是否已经存在该验证码
        Jedis jedis = jedisPool.getResource();
        //Login_002_13760856979
        String telephone = loginInfo.get("telephone");
        String key = "Login_" + RedisMessageConstant.SENDTYPE_LOGIN + "_" +telephone;
        String code4Login1redis = jedis.get(key);
        if (!code.equals(code4Login1redis)) {
            //输入验证码与redis中的验证码不一致
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        //到这里说明一致,调用业务服务进行查询,判断是否是会员
        Member member = loginService.LoginCheck(loginInfo.get("telephone"));
        //登录成功后,把redis中的验证码删除避免被复用
        jedis.del(key);
        //登录成功后要进行用户跟踪:将用户的手机号码存到客户端的cookie中,
        // 此后再次访问该页面,就会把信息携带过来,我们就可以根据不同的用户提供不同的需求
        Cookie cookie = new Cookie("member_telephone",telephone);
        //设置cookie,并响应cookie到浏览器
        cookie.setMaxAge(60*60*24*30);
        cookie.setPath("/");
        response.addCookie(cookie);

        return new Result(true,MessageConstant.LOGIN_SUCCESS);
    }
}
