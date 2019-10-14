package com.health.interfaces;

import com.health.pojo.Role;

import java.util.List;

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
}
