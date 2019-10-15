package com.health.dao;

import com.github.pagehelper.Page;
import com.health.pojo.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleDao {
    /**
     * 查询所有角色项
     * @return
     */
    List<Role> findRoles();

    /**
     * 根据id查询用户的所有角色列表id集合
     * @param id
     * @return
     */
    List<Integer> findAllCheckedRoles(Integer id);
    //    查询角色所有信息
    List<Role> findAll();
    //        保存角色信息
    void addRole(Role role);
    //        保存用户角色关系
    void addUR(@Param("userId") Integer userId, @Param("rid") Integer rid);
    //        保存权限角色关系
    void addRP(@Param("rid")Integer rid,@Param("permissionId") Integer permissionId);
    //        保存菜单角色关系
    void addRM(@Param("rid")Integer rid,@Param("menuId") Integer menuId);
    //        使用分页插件
    Page<Role> findPage(String queryString);
    //        获取角色基本信息
    Role findRoleById(int id);
    //        获取用户角色关系
    Integer[] findURIds(int id);
    //        获取角色权限关系
    Integer[] findRPIds(int id);
    //        获取角色菜单关系
    Integer[] findRMIds(int id);
    //      更新角色基本信息
    void updateRole(Role role);
    //        删除用户角色关系
    void delUR(Integer roleId);
    //        删除角色权限关系
    void delRP(Integer roleId);
    //        删除角色菜单关系
    void delRM(Integer roleId);
    //        删除角色
    void delRole(Integer id);

}
