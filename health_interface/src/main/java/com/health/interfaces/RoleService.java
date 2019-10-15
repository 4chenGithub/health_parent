package com.health.interfaces;

import com.health.Entity.PageResult;
import com.health.Entity.QueryPageBean;
import com.health.pojo.Role;

import java.util.List;
import java.util.Map;

public interface RoleService {
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
    //    新增角色信息
    void addRole(Integer[] userIds, Integer[] permissionIds, Integer[] menuIds, Role role);
    //    分页查询角色数据
    PageResult<Role> findPage(QueryPageBean queryPageBean);
    //    编辑角色数据提交
    void updateRole(Integer[] userIds, Integer[] permissionIds, Integer[] menuIds, Role role);
    //    编辑框数据回显
    Map<String, Object> findRoleById(int id);
    //    删除角色
    void deleteRole(int id);

}
