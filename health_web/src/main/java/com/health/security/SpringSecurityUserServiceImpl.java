package com.health.security;

import com.alibaba.dubbo.config.annotation.Reference;

import com.health.interfaces.UserService;
import com.health.pojo.Permission;
import com.health.pojo.Role;
import com.health.pojo.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


//编写实现类实现UserDetailsService接口

public class SpringSecurityUserServiceImpl implements UserDetailsService {
    @Reference
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //调用业务服务根据用户名查询用户信息
        User user = userService.findUserByUsername(username);
        if (user == null) {
            //认证失败
            return null;
        }

        //创建权限集合
        List<GrantedAuthority> list = new ArrayList<>();
        //根据查询出来的数据进行授权
        Set<Role> roles = user.getRoles();
        for (Role role : roles) {
            //遍历出每一个角色
            //获取每一个角色所对应的多个权限
            Set<Permission> permissions = role.getPermissions();
            for (Permission permission : permissions) {
                //代表每一个权限
                String keyword = permission.getKeyword();
                //将当前用户的所有权限封装到集合中
                list.add(new SimpleGrantedAuthority(keyword));
            }
        }
       /* list.add(new SimpleGrantedAuthority("ROLE_ADMIN"));*//*"{noop}"+*/
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(username, user.getPassword(), list);
        return userDetails;
    }

  /*  private User findUserByUsername(String username) {
        User user = null;
        if ("admin".equals(username)) {
            user = new User();
            user.setUsername(username);
            user.setPassword(encoder.encode("123456"));
        }
        return user;
    }*/
}
