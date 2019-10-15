package com.health.interfaces;

import com.health.Entity.PageResult;
import com.health.Entity.QueryPageBean;
import com.health.pojo.Permission;

import java.util.List;
import java.util.Map;

public interface PermissionService {
    //    新增权限
    void addPermission(Permission permission, Integer[] roleIds);
    //    获取权限列表数据
    PageResult<Permission> findPage(QueryPageBean pagination);
    //    编辑框数据回显
    Map<String,Object> findPermission(int id);
    //    编辑框数据更改
    void updatePermission(Permission permission, Integer[] roleIds);
    //    删除权限
    void deletePermission(int id);
    //    查询所有权限
    List<Permission> findAll();
}
