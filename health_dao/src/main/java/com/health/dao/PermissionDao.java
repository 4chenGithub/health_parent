package com.health.dao;

import com.github.pagehelper.Page;
import com.health.pojo.Permission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PermissionDao {
    //        保存新建权限基本信息
     void addPermission(Permission permission);
    //        保存权限角色关系
     void addPR(@Param("roleId") Integer roleId, @Param("permissionId") Integer permissionId);
    //    获取权限列表数据
    Page<Permission> findPage(String queryString);
    //        获取权限基本信息
    Permission findPermission(int id);
    //        获取权限角色关系
    Integer[] findRPIDs(int id);
    //      更新权限基本信息
    void updatePermission(Permission permission);
    //        删除角色权限关系
    void delRP(Integer pid);
    //        删除权限基本信息
    void deletePermission(int id);
    //    查询所有权限
    List<Permission> findAll();
}
