package com.health.dao;


import com.health.pojo.Member;

public interface LoginDao {
    /**
     * 根据手机号码查询用户
     * @param telephone
     * @return
     */
    Member FindMember(String telephone);

    /**
     * 添加用户
     * @param member
     */
    void AddMember(Member member);


}
