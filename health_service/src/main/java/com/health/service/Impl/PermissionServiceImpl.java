package com.health.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.health.Entity.PageResult;
import com.health.Entity.QueryPageBean;
import com.health.dao.PermissionDao;
import com.health.interfaces.PermissionService;
import com.health.pojo.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = PermissionService.class)
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionDao permissionDao;

    /**
     * 新增权限
     *
     * @param permission
     * @param roleIds
     */
    @Transactional
    @Override
    public void addPermission(Permission permission, Integer[] roleIds) {

        //保存新建权限基本信息
        permissionDao.addPermission(permission);
        // 获取权限Id
        Integer permissionId = permission.getId();
        //保存权限角色关系
        for (Integer roleId : roleIds) {
            permissionDao.addPR(roleId, permissionId);
        }
    }

    /**
     * 分页查询获取权限列表数据
     *
     * @param pagination
     * @return
     */
    @Override
    public PageResult<Permission> findPage(QueryPageBean pagination) {
        //判断查询条件
        if (!StringUtils.isEmpty(pagination.getQueryString())) {
            pagination.setQueryString("%" + pagination.getQueryString() + "%");
        }
        //使用分页插件
        PageHelper.startPage(pagination.getCurrentPage(), pagination.getPageSize());
        Page<Permission> page = permissionDao.findPage(pagination.getQueryString());
        PageResult<Permission> pageResult = new PageResult<>();
        pageResult.setTotal(page.getTotal());
        pageResult.setPageList(page.getResult());
        return pageResult;
    }

    /**
     * 编辑框数据回显
     *
     * @param id
     * @return
     */
    @Override
    public Map<String, Object> findPermission(int id) {
        //获取权限基本信息
        Permission permission = permissionDao.findPermission(id);
        //获取权限角色关系
        Integer[] ids = permissionDao.findRPIDs(id);
        Map<String, Object> map = new HashMap<>();
        map.put("formData", permission);
        map.put("roleIds", ids);
        return map;
    }

    /**
     * 更新编辑框数据
     *
     * @param permission
     * @param roleIds
     */
    @Transactional
    @Override
    public void updatePermission(Permission permission, Integer[] roleIds) {
        //更新权限基本信息
        permissionDao.updatePermission(permission);
        Integer Pid = permission.getId();
        //删除角色权限关系
        permissionDao.delRP(Pid);
        //新增角色权限关系
        if (roleIds != null && roleIds.length > 0) {
            for (Integer roleId : roleIds) {
                permissionDao.addPR(roleId, Pid);
            }
        }
    }

    /**
     * 删除权限
     *
     * @param id
     */
    @Transactional
    @Override
    public void deletePermission(int id) {
        //删除角色权限关系
        permissionDao.delRP(id);
        //删除权限基本信息
        permissionDao.deletePermission(id);

    }

    /**
     * 查询所有权限
     *
     * @return
     */
    @Override
    public List<Permission> findAll() {
        List<Permission> permissionList = permissionDao.findAll();
        return permissionList;
    }
}
