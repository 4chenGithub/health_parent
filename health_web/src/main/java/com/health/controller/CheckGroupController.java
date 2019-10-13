package com.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.health.Constant.MessageConstant;
import com.health.Entity.PageResult;
import com.health.Entity.QueryPageBean;
import com.health.Entity.Result;
import com.health.interfaces.CheckGroupService;
import com.health.pojo.CheckGroup;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/checkGroup")
public class CheckGroupController {
    @Reference
    private CheckGroupService checkGroupService;

    /**
     * 新增检查组
     *
     * @param checkGroup
     * @param checkitemIds
     * @return
     */
    @RequestMapping("/saveCheckGroup")
    //在需要开启校验的controller中添加注解
    public Result saveCheckGroup(@Valid @RequestBody CheckGroup checkGroup, @RequestParam Integer[] checkitemIds) {
        checkGroupService.saveCheckGroup(checkGroup, checkitemIds);
        return new Result(true, MessageConstant.ADD_CHECKGROUP_SUCCESS);
    }


    /**
     * 分页查询
     *
     * @param queryPageBean
     * @return
     */
    @RequestMapping("/findAllPage")
    public Result findAllPage(@RequestBody QueryPageBean queryPageBean) {
        //调用业务服务处理请求
        PageResult<CheckGroup> PageResult = checkGroupService.findAllPage(queryPageBean);
        return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS, PageResult);
    }


    /**
     * 根据id查询检查组信息
     *
     * @param id
     * @return
     */
    @RequestMapping("/findCheckGroupAndCheckItems/{id}")
    public Result findCheckGroupAndCheckItems(@PathVariable("id") int id) {
        CheckGroup checkGroup = checkGroupService.findCheckGroupAndCheckItems(id);
        return new Result(true, null, checkGroup);
    }

    /**
     * 根据id当前检查组的所有选中的检查项
     *
     * @param id
     * @return
     */
    @RequestMapping("/findChoseCheckItems/{id}")
    public Result findChoseCheckItems(@PathVariable("id") int id) {
        List<Integer> ids = checkGroupService.findChoseCheckItems(id);
        return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS, ids);
    }

    /**
     * 更新检查组及其检查项
     *
     * @param checkGroup
     * @param checkitemIds
     * @return
     */
    @RequestMapping("/update")
    public Result update(@RequestBody CheckGroup checkGroup, Integer[] checkitemIds) {
        //调用业务服务处理请求
        checkGroupService.update(checkGroup, checkitemIds);
        return new Result(true, MessageConstant.EDIT_CHECKGROUP_SUCCESS);
    }

    /**
     * 根据id删除检查组
     *
     * @param id
     * @return
     */
    @RequestMapping("/delete/{id}")
    public Result deleteCheckGroup(@PathVariable("id") int id) {
        //调用业务服务处理请求
        checkGroupService.deleteCheckGroup(id);
        return new Result(true, MessageConstant.DELETE_CHECKGROUP_FAIL);
    }

    @RequestMapping("/findAll")
    public Result findAll() {
        List<CheckGroup> checkGroups = checkGroupService.findAll();
        return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkGroups);
    }

}
