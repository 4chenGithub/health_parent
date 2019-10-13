package com.health.dao;

import com.health.pojo.CheckItem;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;


public interface CheckItemDao {
    @Insert("insert into t_checkItem values(null,#{code},#{name},#{sex},#{age},#{price},#{type},#{attention},#{remark})")
    void AddCheckItem(CheckItem checkItem) throws RuntimeException;

    List<CheckItem> findAllPage(String keyWord);

    @Select("select count(0) from t_checkGroup_checkItem where checkItem_id=#{id}")
    int findCountById(int id);

    @Delete("delete from t_checkItem where id=#{id}")
    void deleteById(int id);

    @Select("select * from t_checkItem where id=#{id}")
    CheckItem findCheckItemById(int id);

    @Update("update t_checkItem set code=#{code},name=#{name},sex=#{sex},age=#{age},price=#{price},type=#{type},remark=#{remark},attention=#{attention} where id=#{id}")
    void update(CheckItem checkItem);

    @Select("select * from t_checkItem")
    List<CheckItem> findAllCheckItems();
}
