package com.health.interfaces;


import com.health.Entity.PageResult;
import com.health.Entity.QueryPageBean;
import com.health.Exception.CheckGroupBeUseException;
import com.health.pojo.CheckGroup;

import java.util.List;

public interface CheckGroupService {

    void saveCheckGroup(CheckGroup checkGroup, Integer[] checkitemIds) throws RuntimeException;

    PageResult<CheckGroup> findAllPage(QueryPageBean queryPageBean);

    CheckGroup findCheckGroupAndCheckItems(int id);

    List<Integer> findChoseCheckItems(int id);

    void update(CheckGroup checkGroup, Integer[] checkitemIds);

    void deleteCheckGroup(int id) throws CheckGroupBeUseException;

    List<CheckGroup> findAll();

}
