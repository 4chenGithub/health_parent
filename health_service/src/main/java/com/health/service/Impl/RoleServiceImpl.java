package com.health.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.health.Entity.PageResult;
import com.health.Entity.QueryPageBean;
import com.health.dao.RoleDao;
import com.health.interfaces.RoleService;
import com.health.pojo.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * 根据id查询用户的所有角色列表id集合
     *
     * @param id
     * @return
     */
    @Override
    public List<Integer> findAllCheckedRoles(Integer id) {
        return roleDao.findAllCheckedRoles(id);
    }

    //    查询角色所有信息
    @Override
    public List<Role> findAll() {
        List<Role> list = roleDao.findAll();
        return list;
    }

    //    新增角色信息
    @Transactional
    @Override
    public void addRole(Integer[] userIds, Integer[] permissionIds, Integer[] menuIds, Role role) {
        //        保存角色信息
        roleDao.addRole(role);
        Integer Rid = role.getId();

//        保存权限角色关系
        for (Integer permissionId : permissionIds) {
            roleDao.addRP(Rid, permissionId);
        }
//        保存菜单角色关系
        for (Integer menuId : menuIds) {
            roleDao.addRM(Rid, menuId);
        }
        //        保存用户角色关系
        for (Integer userId : userIds) {
            roleDao.addUR(userId, Rid);
        }

    }

    //    分页查询角色数据
    @Override
    public PageResult<Role> findPage(QueryPageBean queryPageBean) {
//        判断查询条件
        if (!StringUtils.isEmpty(queryPageBean.getQueryString())) {
            queryPageBean.setQueryString("%" + queryPageBean.getQueryString() + "%");
        }
//        使用分页插件
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        Page<Role> page = roleDao.findPage(queryPageBean.getQueryString());
        PageResult<Role> pageResult = new PageResult<>();
        pageResult.setTotal(page.getTotal());
        pageResult.setPageList(page.getResult());
        return pageResult;
    }

    //    编辑框数据回显
    @Override
    public Map<String, Object> findRoleById(int id) {
//        获取角色基本信息
        Role role = roleDao.findRoleById(id);
//        获取用户角色关系
        Integer[] userIds = roleDao.findURIds(id);
//        获取角色权限关系
        Integer[] permissionIds = roleDao.findRPIds(id);
//        获取角色菜单关系
        Integer[] menuIds = roleDao.findRMIds(id);
        Map<String, Object> map = new HashMap<>();
        map.put("userIds", userIds);
        map.put("permissionIds", permissionIds);
        map.put("menuIds", menuIds);
        map.put("formData", role);
        return map;
    }

    //    编辑角色数据提交
    @Transactional
    @Override
    public void updateRole(Integer[] userIds, Integer[] permissionIds, Integer[] menuIds, Role role) {
//      更新角色基本信息
        roleDao.updateRole(role);
        Integer roleId = role.getId();
//        删除用户角色关系
        roleDao.delUR(roleId);
//        删除角色权限关系
        roleDao.delRP(roleId);
//        删除角色菜单关系
        roleDao.delRM(roleId);
//        添加用户角色关系
        for (Integer userId : userIds) {
            roleDao.addUR(userId, roleId);
        }
//        添加角色权限关系
        for (Integer permissionId : permissionIds) {
            roleDao.addRP(roleId, permissionId);
        }
//        添加角色菜单关系
        for (Integer menuId : menuIds) {
            roleDao.addRM(roleId, menuId);
        }
    }

    //    删除角色
    @Transactional
    @Override
    public void deleteRole(int id) {
//        删除用户角色关系
        roleDao.delUR(id);
//        删除角色权限关系
        roleDao.delRP(id);
//        删除角色菜单关系
        roleDao.delRM(id);
//        删除角色
        roleDao.delRole(id);
    }


}
