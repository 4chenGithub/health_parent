package com.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.health.Constant.MessageConstant;
import com.health.Entity.Result;
import com.health.interfaces.RoleService;
import com.health.pojo.Role;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}
