package com.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.health.Constant.MessageConstant;
import com.health.Entity.PageResult;
import com.health.Entity.QueryPageBean;
import com.health.Entity.Result;
import com.health.interfaces.CheckItemService;
import com.health.pojo.CheckItem;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/checkItem")
public class CheckItemController {
    @Reference
    private CheckItemService checkItemService;

    /**
     * 新增检查项
     *
     * @param checkItem
     * @return
     */
    @RequestMapping("/addCheckItem")
    /*添加权限控制注解*/
    @PreAuthorize("hasAuthority('CHECKITEM_ADD')")
    public Result addCheckItem(@RequestBody CheckItem checkItem) {
        //调用业务服务处理请求
        checkItemService.AddCheckItem(checkItem);
        return new Result(true, MessageConstant.ADD_CHECKITEM_SUCCESS);
    }

    /**
     * 分页查询
     *
     * @param queryPageBean
     * @return
     */
    @RequestMapping("/findAll")
    @PreAuthorize("hasAuthority('CHECKITEM_QUERY')")
    public Result findAllPage(@RequestBody QueryPageBean queryPageBean) {
        //调用业务服务处理请求
        PageResult<CheckItem> pageResult = checkItemService.findAllPage(queryPageBean);
        return new Result(true, null, pageResult);
    }

    /**
     * 根据id删除检查项
     *
     * @param id
     * @return
     */
    @RequestMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('CHECKITEM_DELETE')")
    public Result delete(@PathVariable("id") int id) {
        //调用业务服务处理请求
        checkItemService.delete(id);
        return new Result(true, MessageConstant.DELETE_CHECKITEM_SUCCESS);
    }

    /**
     * 根据id查询检查项
     * @param id
     * @return
     */
    @RequestMapping("/findById/{id}")
    @PreAuthorize("hasAuthority('CHECKITEM_QUERY')")
    public Result findById(@PathVariable("id") int id) {
        CheckItem checkItem = checkItemService.findCheckItemById(id);
        return new Result(true,null,checkItem);
    }

    /**
     * 更新数据
     * @param checkItem
     * @return
     */
    @RequestMapping("/update")
    @PreAuthorize("hasAuthority('CHECKITEM_EDIT')")
    public Result update(@RequestBody CheckItem checkItem){
        checkItemService.update(checkItem);
        return new Result(true,MessageConstant.EDIT_CHECKITEM_SUCCESS);
    }

    /**
     * 回显检查项列表
     *
     * @return
     */
    @RequestMapping("/findAllCheckItems")
    @PreAuthorize("hasAuthority('CHECKITEM_QUERY')")
    public Result findAllCheckItems() {
        List<CheckItem> list = checkItemService.findAllCheckItems();
        return new Result(true, null, list);
    }
}
