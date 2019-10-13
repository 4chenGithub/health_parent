package com.health.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.StringUtil;
import com.health.Entity.PageResult;
import com.health.Entity.QueryPageBean;
import com.health.dao.PackageDao;
import com.health.interfaces.PackageService;
import com.health.pojo.Package;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(interfaceClass = PackageService.class)
public class PackageServiceImpl implements PackageService {
    @Autowired
    private PackageDao packageDao;

    /**
     * 新增套餐
     *
     * @param pkg
     * @param checkgroupIds
     */
    @Override
    @Transactional//事务控制
    public void addPackage(Package pkg, Integer[] checkgroupIds) {
        //先存储检查套餐信息,并把自增长的id封装到对象中
        packageDao.SavePackage(pkg);
        //获取自增长的id
        Integer id = pkg.getId();
        if (checkgroupIds != null) {
            //建立检查套餐和检查组的关系
            for (Integer checkgroupId : checkgroupIds) {
                packageDao.AddPackageAndCheckGroup(id, checkgroupId);
            }
        }
    }

    /**
     * 分页查询
     *
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult<Package> findAllPage(QueryPageBean queryPageBean) {
        //先对查询条件进行非空判断
        if (StringUtil.isNotEmpty(queryPageBean.getQueryString())) {
            queryPageBean.setQueryString("%" + queryPageBean.getQueryString() + "%");
        }
        //如果查询条件不为空
        //使用mybatis分页插件进行分页查询
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        //这句使用模糊查询的结果将被分页,且结果必须封装成page
        Page<Package> page = packageDao.findAllPage(queryPageBean.getQueryString());
        PageResult<Package> pageResult = new PageResult<>();
        pageResult.setTotal(page.getTotal());
        pageResult.setPageList(page.getResult());

        return pageResult;
    }

    /**
     * 查询所有套餐
     * @return
     */
    @Override
    public List<Package> findAllPackage() {
        return packageDao.findAllPackage();
    }

    /**
     * 根据id查询套餐信息
     * @param id
     * @return
     */
    @Override
    public Package findById(int id) {
        return packageDao.findById(id);
    }

    /**
     * 根据id查询套餐信息
     * @param id
     * @return
     */
    @Override
    public Package findByPackageId(int id) {
        return packageDao.findByPackageId(id);
    }
}
