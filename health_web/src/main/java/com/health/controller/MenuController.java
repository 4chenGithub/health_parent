package com.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.health.Constant.MessageConstant;
import com.health.Entity.PageResult;
import com.health.Entity.QueryPageBean;
import com.health.Entity.Result;
import com.health.interfaces.MenuService;
import com.health.pojo.Menu;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/menu")
public class MenuController {

    @Reference
    private MenuService menuService;

    /**
     * 查询所有菜单集合
     *
     * @return
     */
    @RequestMapping("/findAllMenus")
    public Result findAllMenus() {
        List<Menu> menus = menuService.findAllMenus();
        return new Result(true, MessageConstant.QUERY_MENUS_SUCCESS, menus);
    }

    /**
     * 分页查询所有父菜单
     *
     * @param queryPageBean
     * @return
     */
    @RequestMapping("/findAllParentMenu")
    public Result findAllParentMenu(@RequestBody QueryPageBean queryPageBean) {
        //调用业务服务处理请求
        PageResult<Menu> pageResult = menuService.findAllParentMenu(queryPageBean);
        return new Result(true, null, pageResult);
    }

    /**
     * 新增菜单
     * @param menu
     * @param childrenIds
     * @return
     */
    @RequestMapping("/saveMenu")
    public Result saveMenu(@Valid @RequestBody Menu menu, Integer[] childrenIds) {
        //调用业务服务处理请求
        if (childrenIds != null && childrenIds.length > 0) {
            if (menu != null) {
                menuService.saveMenu(menu, childrenIds);
                return new Result(true, MessageConstant.ADD_MENU_SUCCESS);
            }
        }
        return new Result(false, MessageConstant.ADD_MENU_FAIL);
    }

    /**
     * 根据id删除父菜单以及其所属的所有子菜单
     *
     * @param id
     * @return
     */
    @RequestMapping("/deleteMenu/{id}")
    public Result deleteMenu(@PathVariable("id") int id) {
        //调用业务服务处理请求
        menuService.deleteMenu(id);
        return new Result(true, MessageConstant.DELETE_MENU_SUCCESS);
    }

    /**
     * 根据id查询父菜单
     *
     * @param id
     * @return
     */
    @RequestMapping("/findParentMenu/{id}")
    public Result findParentMenu(@PathVariable("id") int id) {
        //调用业务服务处理请求
        Menu menu = menuService.findParentMenuById(id);
        if (menu != null) {
            return new Result(true, MessageConstant.QUERY_MENU_SUCCESS, menu);
        }
        return new Result(false, MessageConstant.QUERY_MENU_FAIL);
    }

    /**
     * 根据id查询所有子菜单,并返回id集合
     *
     * @param id
     * @return
     */
    @RequestMapping("/findAllCheckedChildMenu/{id}")
    public Result findAllCheckedChildMenu(@PathVariable("id") int id) {
        //调用业务服务处理请求
        List<Integer> CheckedChildMenuIds = menuService.findAllCheckedChildMenuById(id);
        if (CheckedChildMenuIds != null && CheckedChildMenuIds.size() > 0) {
            return new Result(true, MessageConstant.QUERY_MENUS_SUCCESS, CheckedChildMenuIds);
        }

        return new Result(false,MessageConstant.QUERY_MENUS_FAIL);
    }

    /**
     * 更新菜单
     * @param menu
     * @param childrenIds
     * @return
     */
    @RequestMapping("/updateMenu")
    public Result updateMenu(@Valid @RequestBody Menu menu, Integer[] childrenIds){
        //调用业务服务处理请求
        if (childrenIds != null && childrenIds.length > 0) {
            if (menu != null) {
                menuService.updateMenu(menu, childrenIds);
                return new Result(true, MessageConstant.EDIT_MENU_SUCCESS);
            }
        }

        return new Result(false, MessageConstant.EDIT_MENU_FAIL);
    }

    //    查询所有菜单信息
    @GetMapping("/findAllMenu")
    public Result findAll(){
        List<Menu> menuList=menuService.findAll();
        return new Result(true, MessageConstant.QUERY_MENUS_SUCCESS,menuList);
    }

}
