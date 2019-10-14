package com.health.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.health.dao.RoleDao;
import com.health.interfaces.RoleService;
import com.health.pojo.Role;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service(interfaceClass = RoleService.class)
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleDao roleDao;

    /**
     * 查询所有角色项
     *
     * @return
     */
    @Override
    public List<Role> findRoles() {
        return roleDao.findRoles();
    }

    /**
     *  根据id查询用户的所有角色列表id集合
     * @param id
     * @return
     */
    @Override
    public List<Integer> findAllCheckedRoles(Integer id) {
        return roleDao.findAllCheckedRoles(id);
    }
}
