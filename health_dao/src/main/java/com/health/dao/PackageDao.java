package com.health.dao;

import com.github.pagehelper.Page;
import com.health.pojo.Package;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PackageDao {
    /**
     * 保存检查套餐信息
     * @param pkg
     */
    void SavePackage(Package pkg);

    /**
     * 建立检查套餐和检查组的关系
     * @param id
     * @param checkgroupId
     */
    void AddPackageAndCheckGroup(@Param("id") Integer id, @Param("checkgroupId") Integer checkgroupId);

    /**
     * 分页查询
     * @return
     * @param queryString
     */
    Page<Package> findAllPage(String queryString);

    /**
     * 查询所有套餐信息
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
