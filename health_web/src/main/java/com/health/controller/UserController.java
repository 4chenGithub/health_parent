package com.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.health.Constant.MessageConstant;
import com.health.Entity.PageResult;
import com.health.Entity.QueryPageBean;
import com.health.Entity.Result;
import com.health.interfaces.UserService;
import com.health.pojo.Menu;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {
    @Reference
    private UserService userService;

    @GetMapping("/getUsername")
    public Result getUsername() {
        //获取spring-security的userDetail对象,spring-security认证成功后会把对象保存到上下文对象,供其他地方使用
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return new Result(true, MessageConstant.GET_USERNAME_SUCCESS, user.getUsername());
    }

    /**
     * 分页查询所有用户
     *
     * @param queryPageBean
     * @return
     */
    @RequestMapping("/findAllUser")
    public Result findAllUser(@RequestBody QueryPageBean queryPageBean) {
        //调用业务服务处理请求
        PageResult<com.health.pojo.User> pageResult = userService.findAllUser(queryPageBean);
        return new Result(true, null, pageResult);
    }

    /**
     * 添加用户
     *
     * @param user
     * @param childrenIds
     * @return
     */
    @RequestMapping("/saveUser")
    public Result saveUser(@Valid @RequestBody com.health.pojo.User user, Integer[] childrenIds) {
        //调用业务服务处理请求
        if (childrenIds != null && childrenIds.length > 0) {
            if (user != null) {
                userService.saveUser(user, childrenIds);
                return new Result(true, MessageConstant.ADD_USER_SUCCESS);
            }
        }
        return new Result(false, MessageConstant.ADD_USER_FAIL);
    }

    /**
     * 编辑用户数据回显
     *
     * @param id
     * @return
     */
    @RequestMapping("/findUserById/{id}")
    public Result findUserById(@PathVariable("id") Integer id) {
        //调用业务服务处理请求
        com.health.pojo.User user = userService.findUserById(id);
        return new Result(true, MessageConstant.QUERY_USER_SUCCESS, user);
    }

    /**
     * 更新用户信息
     *
     * @param user
     * @param childrenIds
     * @return
     */
    @RequestMapping("/updateUser")
    public Result updateUser(@Valid @RequestBody com.health.pojo.User user, Integer[] childrenIds) {
        //调用业务服务处理请求
        if (childrenIds != null && childrenIds.length > 0) {
            if (user != null) {
                userService.updateUser(user, childrenIds);
                return new Result(true, MessageConstant.EDIT_USER_SUCCESS);
            }
        }
        return new Result(false, MessageConstant.EDIT_USER_FAIL);
    }

    /**
     * 删除用户
     * @param id
     * @return
     */
    @RequestMapping("/deleteUser/{id}")
    public Result deleteUser(@PathVariable("id") Integer id) {
            //调用业务服务处理请求
            userService.deleteUser(id);
            return new Result(true,MessageConstant.DELETE_USER_SUCCESS);
    }
}
