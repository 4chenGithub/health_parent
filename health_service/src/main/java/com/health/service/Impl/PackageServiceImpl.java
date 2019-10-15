package com.health.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.StringUtil;
import com.health.Constant.MessageConstant;
import com.health.Entity.PageResult;
import com.health.Entity.QueryPageBean;
import com.health.Exception.PackageBeUseException;
import com.health.Uitls.QiNiuUtil;
import com.health.dao.PackageDao;
import com.health.interfaces.PackageService;
import com.health.pojo.Package;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     *
     * @return
     */
    @Override
    public List<Package> findAllPackage() {
        return packageDao.findAllPackage();
    }

    /**
     * 根据id查询套餐信息
     *
     * @param id
     * @return
     */
    @Override
    public Package findById(int id) {
        return packageDao.findById(id);
    }

    /**
     * 根据id查询套餐信息
     *
     * @param id
     * @return
     */
    @Override
    public Package findByPackageId(int id) {
        return packageDao.findByPackageId(id);
    }

    /**
     * 点击编辑窗口套餐数据的回显
     *
     * @param id
     * @return
     */
    @Override
    public Map<String, Object> findPackageByIdAndCheckedGroup(Integer id) {
        Map<String, Object> map = new HashMap<>();
        Package pkg = packageDao.findPackageById(id);
        if (pkg != null) {
            //处理图片路径
            //套餐不为空时,查询其选中的检查组的id集合
            List<Integer> integers = packageDao.findPackageAndCheckedGroupIds(id);
            map.put("package", pkg);
            map.put("checkGroupIds", integers);
        }
        return map;
    }

    /**
     * 更新套餐数据
     *
     * @param pkg
     * @param checkgroupIds
     */
    @Override
    @Transactional
    public void updatePackage(Package pkg, Integer[] checkgroupIds) {
        //更新套餐信息
        packageDao.updatePackage(pkg);
        //修改后,再把原来的套餐与检查组的关系全部删掉,再全部重新添加
        Integer pkgId = pkg.getId();
        packageDao.deletePackage(pkgId);
        //再添加所有关系
        if (checkgroupIds != null && checkgroupIds.length > 0) {
            for (Integer checkgroupId : checkgroupIds) {
                packageDao.AddPackageAndCheckGroup(pkgId, checkgroupId);
            }
        }
    }

    /**
     * 根据id删除套餐
     *
     * @param id
     */
    @Override
    public void deletePackage(Integer id) throws PackageBeUseException {
        Integer count = packageDao.findPackageCount(id);
        if (count > 0) {
            throw new PackageBeUseException(MessageConstant.PACKAGE_ID_IS_USED);
        }
        //没有被引用,则删除
        packageDao.deletePackageById(id);
    }
}
