package com.health.interfaces;


import com.health.pojo.Member;

public interface LoginService {
    /**
     * 根据手机号码查询用户是否存在
     * @param telephone
     * @return
     */
    Member LoginCheck(String telephone);
}
