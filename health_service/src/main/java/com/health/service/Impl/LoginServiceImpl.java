package com.health.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;

import com.health.dao.LoginDao;
import com.health.interfaces.LoginService;
import com.health.pojo.Member;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

@Service(interfaceClass = LoginService.class)
public class LoginServiceImpl implements LoginService {
    @Autowired
    private LoginDao loginDao;

    /**
     * 根据手机号码查询用户是否存在
     *
     * @param telephone
     * @return
     */
    @Override
    public Member LoginCheck(String telephone) {
        Member member = loginDao.FindMember(telephone);
        if (member == null) {
            //用户未注册,则自动注册
            member=new Member();
            member.setPhoneNumber(telephone);
            member.setRegTime(new Date());
            loginDao.AddMember(member);
        }
        //注册成功后
        return member;
    }
}
