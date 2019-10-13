package com.health.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.health.dao.UserDao;
import com.health.interfaces.UserService;
import com.health.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service(interfaceClass = UserService.class)
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    /**
     * 根据用户名查询用户信息
     *
     * @param username
     * @return
     */
    @Override
    public User findUserByUsername(String username) {
        User user = userDao.findUserByUsername(username);
//        if (user != null) {
//            user.setPassword(encoder.encode(user.getPassword()));
//        }
        return user;
    }

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        //加密
        String encode = encoder.encode("666666");
        System.out.println(encode);
    }
}
