package com.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.health.Constant.MessageConstant;
import com.health.Entity.PageResult;
import com.health.Entity.QueryPageBean;
import com.health.Entity.Result;
import com.health.interfaces.RoleService;
import com.health.pojo.Role;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/role")
public class RoleController {

    @Reference
    private RoleService roleService;

    /**
     * 查询所有角色项
     *
     * @return
     */
    @RequestMapping("/findRoles")
    public Result findRoles() {
        //调用业务服务处理请求
        List<Role> roleList = roleService.findRoles();
        return new Result(true, MessageConstant.QUERY_ROLES_SUCCESS, roleList);
    }

    /**
     * 查询对应用户的选中角色列表
     * @param id
     * @return
     */
    @RequestMapping("/findAllCheckedRoles/{id}")
    private Result findAllCheckedRoles(@PathVariable("id") Integer id) {
        //调用业务服务处理请求
        List<Integer> integerList = roleService.findAllCheckedRoles(id);
        return new Result(true,MessageConstant.QUERY_CHECKED_ROLES_SUCCESS,integerList);
    }

    /**
     *  查询角色所有信息
     * @return
     */
    @GetMapping("/findAll")
    public Result findAll() {
        List<Role> list = roleService.findAll();
        return new Result(true, MessageConstant.ADD_CHECKGROUP_SUCCESS, list);
    }


    /**
     * 新增角色信息
     * @param userIds
     * @param permissionIds
     * @param menuIds
     * @param role
     * @return
     */

    @PostMapping("/addRole")
    public Result addRole(Integer[] userIds, Integer[] permissionIds, Integer[] menuIds, @RequestBody Role role) {
        roleService.addRole(userIds, permissionIds, menuIds, role);
        return new Result(true, MessageConstant.ADD_ROLE_SUCCESS);
    }

    /**
     *  分页查询角色数据
     * @param queryPageBean
     * @return
     */

    @PostMapping("/findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult<Role> pageResult = roleService.findPage(queryPageBean);
        return new Result(true, MessageConstant.GET_ROLE_SUCCESS, pageResult);
    }

    /**
     * 编辑框数据回显
     * @param id
     * @return
     */
    @GetMapping("/findRoleById")
    public Result findRoleById(int id) {
        Map<String, Object> map = roleService.findRoleById(id);
        return new Result(true, MessageConstant.GET_ROLE_SUCCESS, map);
    }

    /**
     * 编辑角色数据提交
     * @param userIds
     * @param permissionIds
     * @param menuIds
     * @param role
     * @return
     */

    @PostMapping("/updateRole")
    public Result updateRole(Integer[] userIds, Integer[] permissionIds, Integer[] menuIds, @RequestBody Role role) {
        roleService.updateRole(userIds, permissionIds, menuIds, role);
        return new Result(true, MessageConstant.EDIT_ROLE_SUCCESS);
    }

    /**
     * 删除角色
     * @param id
     * @return
     */

    @PostMapping("/deleteRole")
    public Result deleteRole(int id) {
        roleService.deleteRole(id);
        return new Result(true, MessageConstant.DELETE_ROLE_SUCCESS);

    }



}
