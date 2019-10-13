package com.health.interfaces;


import com.health.Entity.PageResult;
import com.health.Entity.QueryPageBean;
import com.health.Exception.CheckItemBeUseException;
import com.health.pojo.CheckItem;

import java.util.List;

public interface CheckItemService {
    void AddCheckItem(CheckItem checkItem) throws RuntimeException;

    PageResult<CheckItem> findAllPage(QueryPageBean queryPageBean);

    void delete(int id) throws CheckItemBeUseException;

    CheckItem findCheckItemById(int id);

    void update(CheckItem checkItem);

    List<CheckItem> findAllCheckItems();

}
