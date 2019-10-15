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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * 根据登陆成功后的用户名名获取对应的菜单
     *
     * @return
     */
    @RequestMapping("/getMenuList")
    public Result getMenuList() {
        //获取spring-security的userDetail对象,spring-security认证成功后会把对象保存到上下文对象,供其他地方使用
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        if (name != null) {
            //调用业务服务查询用户对应的菜单集合
            List<Menu> menuList = userService.findAllMenuByUsername(name);
            List<Map<String, Object>> list = new ArrayList<>();//menuList
            if (menuList != null && menuList.size() > 0) {
                for (Menu menu : menuList) {
                    //遍历父菜单
                    Map<String, Object> MenuMap = new HashMap<>();
                    String path = menu.getPath();
                    String title = menu.getName();
                    String icon = menu.getIcon();
                    MenuMap.put("path", path);
                    MenuMap.put("title", title);
                    MenuMap.put("icon", icon);
                    List<Menu> children = menu.getChildren();
                    List<Map<String, Object>> ChildList = new ArrayList<>();
                    MenuMap.put("children", ChildList);
                    if (children != null && children.size() > 0) {
                        //遍及子菜单
                        for (Menu child : children) {
                            Map<String, Object> ChildMap = new HashMap<>();
                            String childPath = child.getPath();
                            String childName = child.getName();
                            String childLinkUrl = child.getLinkUrl();
                            ChildMap.put("path",childPath);
                            ChildMap.put("title",childName);
                            ChildMap.put("linkUrl",childLinkUrl);
                            List<Menu> childChildren = child.getChildren();
                            ChildMap.put("children",childChildren);
                            if (childChildren != null && childChildren.size() > 0) {
                                //遍及子子菜单(暂时没有)
                                for (Menu child2 : childChildren) {
                                    String childPath2 = child2.getPath();
                                    String childName2 = child2.getName();
                                    String childLinkUr2 = child2.getLinkUrl();
                                    List<Menu> childChildren2 = child2.getChildren();
                                }
                            }
                            ChildList.add(ChildMap);
                        }
                    }
                    list.add(MenuMap);
                }
            }
            return new Result(true,MessageConstant.GET_MENUS_SUCCESS,list);
        }
        return new Result(false,MessageConstant.GET_MENUS_FAIL);
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
     *
     * @param id
     * @return
     */
    @RequestMapping("/deleteUser/{id}")
    public Result deleteUser(@PathVariable("id") Integer id) {
        //调用业务服务处理请求
        userService.deleteUser(id);
        return new Result(true, MessageConstant.DELETE_USER_SUCCESS);
    }

    //    查询用户所有信息
    @GetMapping("/findAll")
    public Result findAll(){
        List<com.health.pojo.User> userList =userService.findAll();
        return new Result(true,MessageConstant.QUERY_USER_SUCCESS,userList);
    }
}
