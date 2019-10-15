package com.health.dao;


import com.github.pagehelper.Page;
import com.health.pojo.Menu;
import com.health.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserDao {
    /**
     * 根据用户名查询用户信息
     * @param username
     * @return
     */
    User findUserByUsername(String username);

    /**
     * 分页查询所有用户
     * @param queryString
     * @return
     */
    Page<User> findAllUser(String queryString);

    /**
     * 根据手机号码查询用户
     * @param telephone
     * @return
     */
    User findUserByTelephone(String telephone);

    /**
     * 添加用户
     * @param user
     */
    void AddUser(User user);

    /**
     * 添加用户与角色之间的关系
     * @param userId
     * @param childrenId
     */
    void AddUserAndRole(@Param("userId") Integer userId,@Param("childrenId") Integer childrenId);

    /**
     * 根据id查询用户
     * @param id
     * @return
     */
    User findUserById(Integer id);

    /**
     * 更新用户信息
     * @param user
     */
    void updateUser(User user);

    /**
     * 删除原有的关系
     * @param userId
     */
    void DeleteUserAndRole(Integer userId);

    /**
     * 根据id查询用户与角色的关系集合
     * @param id
     * @return
     */
    Integer findUserCountById(Integer id);

    /**
     * 删除用户
     * @param id
     */
    void deleteUserById(Integer id);

    /**
     * 根据用户名查询用户信息
     * @param username
     * @return
     */
    User findOneUserByUsername(String username);


    /**
     * 根据用户名统计用户
     * @param username
     * @return
     */
    Integer findUserCountByUsername(String username);

    /**
     * 根据手机号码统计用户
     * @param telephone
     * @return
     */
    Integer findUserCountByTelephone(String telephone);

    /**
     * 根据用户名获取对应的菜单集合
     * @param name
     * @return
     */
    List<Menu> findAllMenuByUsername(String name);


    //    查询用户所有信息
    List<User> findAll();
}
