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

    /**
     * 根据id查询套餐信息
     * @param id
     * @return
     */
    Package findPackageById(Integer id);

    /**
     * 根据套餐id查询其所属的检查组id集合
     * @param id
     * @return
     */
    List<Integer> findPackageAndCheckedGroupIds(Integer id);

    /**
     * 更新套餐信息
     * @param pkg
     */
    void updatePackage(Package pkg);

    /**
     * 根据id删除套餐下的所有检查组集合
     * @param pkgId
     */
    void deletePackage(Integer pkgId);

    /**
     * 根据id查询套餐是否被引用
     * @param id
     * @return
     */
    Integer findPackageCount(Integer id);

    /**
     * 根据id删除套餐信息
     * @param id
     */
    void deletePackageById(Integer id);
}
