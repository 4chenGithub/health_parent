package com.health.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.StringUtil;
import com.health.Entity.PageResult;
import com.health.Entity.QueryPageBean;
import com.health.Exception.TelephoneIsExistException;
import com.health.Exception.UsernameBeUseException;
import com.health.dao.UserDao;
import com.health.interfaces.UserService;
import com.health.pojo.Menu;
import com.health.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(interfaceClass = UserService.class)
public class UserServiceImpl implements UserService {

    private BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();

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

    /**
     * 分页查询所有用户
     *
     * @param queryPageBean
     * @return
     */
    @Override
    @Transactional
    public PageResult<User> findAllUser(QueryPageBean queryPageBean) {
        //使用mybatis插件进行查询
        if (!StringUtil.isEmpty(queryPageBean.getQueryString())) {
            //如果查询条件不为空
            queryPageBean.setQueryString("%" + queryPageBean.getQueryString() + "%");
        }
        //使用mybatis分页插件进行分页查询
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());

        Page<User> page = userDao.findAllUser(queryPageBean.getQueryString());
        PageResult<User> pageResult = new PageResult<>();

        pageResult.setTotal(page.getTotal());
        pageResult.setPageList(page.getResult());

        return pageResult;
    }

    /**
     * 保存用户
     *
     * @param user
     * @param childrenIds
     */
    @Transactional
    @Override
    public void saveUser(User user, Integer[] childrenIds) throws UsernameBeUseException, TelephoneIsExistException {
        //先从数据库中根据用户查询用户,保证用户的唯一
        User user1 = userDao.findOneUserByUsername(user.getUsername());
        if (user1 != null) {
            //说明用户已经存在
            throw new UsernameBeUseException("用户名已经存在");
        }

        //根据手机号码查询,保证号码唯一
        user1 = userDao.findUserByTelephone(user.getTelephone());
        if (user1 != null) {
            throw new TelephoneIsExistException("请检查手机号码是否正确");
        }
        //对密码进行加密
        user.setPassword(encoder.encode(user.getPassword()));
        //满足以上要求,则添加用户,并获取自增的id
        userDao.AddUser(user);
        //获取自增长的id
        Integer userId = user.getId();
        //添加用户与角色的关系
        if (childrenIds != null && childrenIds.length > 0) {
            for (Integer childrenId : childrenIds) {
                userDao.AddUserAndRole(userId, childrenId);
            }
        }
    }

    /**
     * 根据id查询用户
     *
     * @param id
     * @return
     */
    @Override
    public User findUserById(Integer id) {
        return userDao.findUserById(id);
    }

    /**
     * 更新用户数据
     *
     * @param user
     * @param childrenIds
     */
    @Override
    @Transactional
    public void updateUser(User user, Integer[] childrenIds) throws UsernameBeUseException, TelephoneIsExistException {
        //校验用户名是否重复
        //先从数据库中根据用户查询用户,保证用户的唯一
        Integer count = userDao.findUserCountByUsername(user.getUsername());
        if (count > 1) {
            //说明用户已经存在
            throw new UsernameBeUseException("用户名已经存在");
        }

        //根据手机号码查询,保证号码唯一
        count = userDao.findUserCountByTelephone(user.getTelephone());
        if (count > 1) {
            throw new TelephoneIsExistException("请检查手机号码是否正确");
        }
        //对密码进行加密
        user.setPassword(encoder.encode(user.getPassword()));
        //没问题则更新
        //获取用户id
        Integer userId = user.getId();
        if (userId != null) {
            userDao.updateUser(user);
            //再更新关系表
            //先删除原有的关系
            userDao.DeleteUserAndRole(userId);
            //再重新添加所有关系
            if (childrenIds != null && childrenIds.length > 0) {
                for (Integer childrenId : childrenIds) {
                    userDao.AddUserAndRole(userId, childrenId);
                }
            }
        }

    }

    /**
     * 根据id删除用户信息
     *
     * @param id
     */
    @Override
    @Transactional
    public void deleteUser(Integer id) {
        //先判断该用户的id是否被引用,如果是,终止程序
        Integer count = userDao.findUserCountById(id);
        if (count > 0) {
            //先删除用户角色关系表
            userDao.DeleteUserAndRole(id);
        }
        //如果没有,则删除用户信息
        userDao.deleteUserById(id);
    }

    /**
     * 根据用户名获取对应的菜单集合
     * @param name
     * @return
     */
    @Override
    public List<Menu> findAllMenuByUsername(String name) {
        return userDao.findAllMenuByUsername(name);
    }


    //    查询用户所有信息
    @Override
    public List<User> findAll() {
        List<User> userList= userDao.findAll();
        return userList;
    }
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        //加密
        String encode = encoder.encode("666666");
        System.out.println(encode);
    }
}
