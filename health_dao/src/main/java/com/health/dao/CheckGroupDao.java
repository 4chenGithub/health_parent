package com.health.dao;


import com.health.pojo.CheckGroup;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface CheckGroupDao {


    void saveCheckGroup(CheckGroup checkGroup);

    void saveCheckGroupAndCheckItems(Map<String, Integer> map);

    List<CheckGroup> FindAllPage(String queryString);

    @Select("select * from t_checkGroup where id=#{id}")
    CheckGroup findCheckGroup(int id);

    @Select("select checkItem_id from t_checkGroup_checkItem where checkGroup_id =#{id}")
    List<Integer> findChoseCheckItems(int id);

    /**
     * 更新检查组
     *
     * @param checkGroup
     */
    void updateCheckGroup(CheckGroup checkGroup);

    /*
    删除当前检查组的所属检查项id
     */
    @Delete("delete from t_checkGroup_checkItem where checkGroup_id=#{checkGroupId}")
    void DeleteCheckItems(Integer checkGroupId);

    /**
     * 根据id查询当前的当前检查组的在t_Package_checkGroup中总记录数
     * @param id
     * @return
     */
    @Select("select count(0) from t_package_checkGroup where checkGroup_id=#{id}")
    int findCountById(int id);


    /**
     * 删除检查组信息
     * @param id
     */
    @Delete("delete from t_checkGroup where id=#{id}")
    void deleteCheckGroup(int id);

    /**
     * 查询所有检查组
     * @return
     */
    @Select("select * from t_checkGroup")
    List<CheckGroup> findAll();

}