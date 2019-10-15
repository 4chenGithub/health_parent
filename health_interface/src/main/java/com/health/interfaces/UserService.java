package com.health.interfaces;


import com.health.Entity.PageResult;
import com.health.Entity.QueryPageBean;
import com.health.Exception.TelephoneIsExistException;
import com.health.Exception.UsernameBeUseException;
import com.health.pojo.Menu;
import com.health.pojo.User;

import javax.validation.Valid;
import java.util.List;

public interface UserService {
    /**
     * 根据用户名查询用户信息
     *
     * @param username
     * @return
     */
    User findUserByUsername(String username);

    /**
     * 分页查询所有用户
     *
     * @param queryPageBean
     * @return
     */
    PageResult<User> findAllUser(QueryPageBean queryPageBean);

    /**
     * 保存用户信息
     *
     * @param user
     * @param childrenIds
     */
    void saveUser(User user, Integer[] childrenIds) throws UsernameBeUseException, TelephoneIsExistException;

    /**
     * 根据id查询用户
     *
     * @param id
     * @return
     */
    User findUserById(Integer id);

    /**
     * 更新用户数据
     *
     * @param user
     * @param childrenIds
     */
    void updateUser(User user, Integer[] childrenIds) throws UsernameBeUseException,TelephoneIsExistException;

    /**
     * 根据id删除用户信息
     *
     * @param id
     */
    void deleteUser(Integer id);

    /**
     * 根据用户名获取对应的菜单集合
     * @param name
     * @return
     */
    List<Menu> findAllMenuByUsername(String name);

    //    查询用户所有信息
    List<User> findAll();

}
