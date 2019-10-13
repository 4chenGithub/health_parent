package com.health.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.health.Constant.MessageConstant;
import com.health.Entity.PageResult;
import com.health.Entity.QueryPageBean;
import com.health.Exception.CheckItemBeUseException;
import com.health.dao.CheckItemDao;
import com.health.interfaces.CheckItemService;
import com.health.pojo.CheckItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(interfaceClass = CheckItemService.class)
@Transactional
public class CheckItemServiceImpl implements CheckItemService {
    @Autowired
    private CheckItemDao checkItemDao;

    /**
     * 添新增检查项
     * @param checkItem
     * @throws RuntimeException
     */
    @Override
    public void AddCheckItem(CheckItem checkItem) throws RuntimeException {
        checkItemDao.AddCheckItem(checkItem);
    }

    /**
     * 分页查询
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult<CheckItem> findAllPage(QueryPageBean queryPageBean) {
        if (queryPageBean.getQueryString() != null && !"".equals(queryPageBean.getQueryString())) {
            queryPageBean.setQueryString("%" + queryPageBean.getQueryString() + "%");
        }
        //调用分页插件查询,默认查询总数据条数
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        //调用dao层的方法进行模糊查询,并返回一个list<集合>,注意查询条件可能为空,此查询会被分页
        Page<CheckItem> page = (Page<CheckItem>) checkItemDao.findAllPage(queryPageBean.getQueryString());
        //将上两步查询的结果封装到pageResult并返回
        PageResult<CheckItem> pageResult = new PageResult<>();
        pageResult.setTotal(page.getTotal());
        pageResult.setPageList(page.getResult());
        System.out.println(pageResult);
        return pageResult;
    }

    /**
     * 根据id删除
     * @param id
     * @throws RuntimeException
     */
    @Override
    public void delete(int id)  throws CheckItemBeUseException {
        //先判断当前的检查项是否有被检查组引用,如果有被引用则终止当前程序,这里使用自定义异常终止程序
        //根据id统计中间表t_checkGroup_checkItem表中的总记录数
        int count = checkItemDao.findCountById(id);
        if (count > 0) {
            //说明已经被引用,则不能删除,抛出异常
            throw  new CheckItemBeUseException(MessageConstant.DELETE_CHECKITEM_FAIL);
        }
        //如果没有被引用,则根据id删除检查项
        checkItemDao.deleteById(id);

    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    @Override
    public CheckItem findCheckItemById(int id) {
        return checkItemDao.findCheckItemById(id);
    }

    /**
     * 更新检查项
     * @param checkItem
     */
    @Override
    public void update(CheckItem checkItem) {
        checkItemDao.update(checkItem);
    }


    /**
     *查询所有检查项
     * @return
     */
    @Override
    public List<CheckItem> findAllCheckItems() {
        return checkItemDao.findAllCheckItems();
    }

}
