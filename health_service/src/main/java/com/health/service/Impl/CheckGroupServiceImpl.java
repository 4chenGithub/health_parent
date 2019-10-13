package com.health.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.StringUtil;

import com.health.Constant.MessageConstant;
import com.health.Entity.PageResult;
import com.health.Entity.QueryPageBean;
import com.health.Exception.CheckGroupBeUseException;
import com.health.Exception.InsertCheckGroupException;
import com.health.dao.CheckGroupDao;
import com.health.interfaces.CheckGroupService;
import com.health.pojo.CheckGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = CheckGroupService.class)
public class CheckGroupServiceImpl implements CheckGroupService {
    @Autowired
    private CheckGroupDao checkGroupDao;

    /**
     * 保存检查组信息及其对应的检查项集合
     *
     * @param checkGroup
     * @param checkitemIds
     */
    @Transactional
    @Override
    public void saveCheckGroup(CheckGroup checkGroup, Integer[] checkitemIds) throws RuntimeException {

        if (checkitemIds != null && checkitemIds.length > 0) {
            //先保存检查组的数据,并把自增长的id值存到当前的对象中
            checkGroupDao.saveCheckGroup(checkGroup);
            Integer id = checkGroup.getId(); //获取对象中的id

            Map<String, Integer> map = new HashMap<>();
            map.put("id", id);
            for (Integer Id : checkitemIds) {
                map.put("Ids", Id);
                checkGroupDao.saveCheckGroupAndCheckItems(map);
            }
        } else {
            throw new InsertCheckGroupException(MessageConstant.ADD_CHECKGROUP_FAIL);
        }

    }

    /**
     * 分页查询
     *
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult<CheckGroup> findAllPage(QueryPageBean queryPageBean) {
        //使用mybatis插件进行查询
        if (!StringUtil.isEmpty(queryPageBean.getQueryString())) {
            //如果查询条件不为空
            queryPageBean.setQueryString("%" + queryPageBean.getQueryString() + "%");
        }
        //使用mybatis分页插件进行分页查询
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        Page<CheckGroup> page = (Page<CheckGroup>) checkGroupDao.FindAllPage(queryPageBean.getQueryString());
        PageResult<CheckGroup> pageResult = new PageResult<>();
        pageResult.setTotal(page.getTotal());
        pageResult.setPageList(page.getResult());

        return pageResult;
    }

    /**
     * 根据id查询当前检查组
     *
     * @param id
     * @return
     */
    @Override
    public CheckGroup findCheckGroupAndCheckItems(int id) {
        //调用dao层查询当前检查组的信息
        CheckGroup checkGroup = checkGroupDao.findCheckGroup(id);
        return checkGroup;
    }

    /**
     * 查询所有选中的检查项
     *
     * @param id
     * @return
     */
    @Override
    public List<Integer> findChoseCheckItems(int id) {
        return checkGroupDao.findChoseCheckItems(id);
    }

    /**
     * 更新检查组及其检查项
     *
     * @param checkGroup
     * @param checkitemIds
     */
    @Transactional//添加事务控制,表示当有一项的数据没有更新成功,则回滚事务
    @Override
    public void update(CheckGroup checkGroup, Integer[] checkitemIds) {
        //先进行检查组更新
        checkGroupDao.updateCheckGroup(checkGroup);
        //再根据检查组的id删除t_checkGroup_checkItem表中的其所有检查项
        Integer checkGroupId = checkGroup.getId();
        checkGroupDao.DeleteCheckItems(checkGroupId);
        //最后重新建立关系
        //1 先判断checkitemIds是否为空,以防止空指针异常
        if (checkitemIds != null) {
            //再进行遍历
            Map<String, Integer> map = new HashMap<>();
            map.put("id", checkGroupId);
            for (Integer checkitemId : checkitemIds) {
                map.put("Ids", checkitemId);
                checkGroupDao.saveCheckGroupAndCheckItems(map);
            }
        }

    }

    /**
     * 根据id删除检查组
     *
     * @param id
     */
    @Transactional
    @Override
    public void deleteCheckGroup(int id) throws CheckGroupBeUseException {
        //删除前要判断当前的检查组是否被检查套餐引用,如果有,则终止程序
        int count = checkGroupDao.findCountById(id);
        //判断count
        if (count > 0) {
            //如果被引用则终止程序
            throw new CheckGroupBeUseException(MessageConstant.DELETE_CHECKGROUP_FAIL);
        }
        //如果没有引用,则删除
        //删除检查组信息
        checkGroupDao.deleteCheckGroup(id);
        //删除检查组和检查项的关系
        checkGroupDao.DeleteCheckItems(id);
    }

    /**
     * 查询所有检查组集合
     * @return
     */
    @Override
    public List<CheckGroup> findAll() {
        return checkGroupDao.findAll();
    }


}
