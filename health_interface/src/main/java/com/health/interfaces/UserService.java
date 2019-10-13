package com.health.interfaces;


import com.health.pojo.User;

public interface UserService {
    /**
     * 根据用户名查询用户信息
     * @param username
     * @return
     */
    User findUserByUsername(String username);

}
