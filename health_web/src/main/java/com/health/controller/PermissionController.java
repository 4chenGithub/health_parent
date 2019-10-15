package com.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.health.Constant.MessageConstant;
import com.health.Entity.PageResult;
import com.health.Entity.QueryPageBean;
import com.health.Entity.Result;
import com.health.interfaces.PermissionService;
import com.health.pojo.Permission;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/permission")
public class PermissionController {
    @Reference
    private PermissionService permissionService;

    /**
     * 新增权限
     * @param permission
     * @param roleIds
     * @return
     */
    @PostMapping("/addPermission")
    public Result addPermission(@RequestBody Permission permission, Integer[] roleIds){
        permissionService.addPermission(permission,roleIds);
        return new Result(true, MessageConstant.ADD_PERMISSION_SUCCESS);
    }

    /**
     * 分页查询获取权限列表数据
     * @param pagination
     * @return
     */
    @PostMapping("/findPage")
    public Result findPage(@RequestBody QueryPageBean pagination){
        PageResult<Permission> pageResult=permissionService.findPage(pagination);
        return new Result(true, MessageConstant.GET_PERMISSION_SUCCESS,pageResult);
    }

    /**
     * 编辑框数据回显
     * @param id
     * @return
     */
    @GetMapping("/findPermission")
    public Result findPermission(int id){
        Map<String,Object> map=permissionService.findPermission(id);
        return new Result(true, MessageConstant.EDIT_PERMISSION_SUCCESS,map);
    }

    /**
     * 编辑框数据更改
     * @param permission
     * @param roleIds
     * @return
     */
    @PostMapping("/updatePermission")
    public Result updatePermission(@RequestBody Permission permission, Integer[] roleIds){
        permissionService.updatePermission(permission,roleIds);
        return new Result(true, MessageConstant.EDIT_PERMISSION_SUCCESS);
    }

    /**
     * 删除权限
     * @param id
     * @return
     */
    @PostMapping("/deletePermission")
    public Result deletePermission(int id){
        permissionService.deletePermission(id);
        return new Result(true, MessageConstant.DELETE_PERMISSION_SUCCESS);
    }

    /**
     * 查询所有权限
     * @return
     */
    @GetMapping("/findAll")
     public Result findAll(){
        List<Permission> permissionList=permissionService.findAll();
        return new Result(true, MessageConstant.GET_PERMISSION_SUCCESS,permissionList);
    }

}
