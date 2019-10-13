package com.health.interfaces;



import com.health.Entity.PageResult;
import com.health.Entity.QueryPageBean;
import com.health.pojo.Package;

import java.util.List;

public interface PackageService {
    /**
     * 新增套餐
     * @param pkg
     * @param checkgroupIds
     */
    void addPackage(Package pkg, Integer[] checkgroupIds);

    /**
     * 分页查询
     * @param queryPageBean
     * @return
     */
    PageResult<Package> findAllPage(QueryPageBean queryPageBean);

    /**
     * 查询所有套餐
     * @return
     */
    List<Package> findAllPackage();

    /**
     * 根据id查询套餐详情
     * @param id
     * @return
     */
    Package findById(int id);

    /**
     * 根据id查询套餐信息
     * @param id
     * @return
     */
    Package findByPackageId(int id);
}
