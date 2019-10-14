package com.health.interfaces;

import com.health.Entity.PageResult;
import com.health.Entity.QueryPageBean;
import com.health.Exception.MenuBeUseException;
import com.health.Exception.MenuIsExistException;
import com.health.pojo.Menu;

import javax.validation.Valid;
import java.util.List;

public interface MenuService {
    /**
     * 查询所有菜单集合
     * @return
     */
    List<Menu> findAllMenus();

    /**
     * 分页查询所有父菜单
     * @param queryPageBean
     * @return
     */
    PageResult<Menu> findAllParentMenu(QueryPageBean queryPageBean);

    /**
     * 新增菜单
     * @param menu
     * @param childrenIds
     */
    void saveMenu(@Valid Menu menu, Integer[] childrenIds) throws MenuIsExistException;

    /**
     * 根据id删除菜单
     * @param id
     */
    void deleteMenu(int id) throws MenuBeUseException;

    /**
     * 根据id查询菜单
     * @param id
     * @return
     */
    Menu findParentMenuById(int id);

    /**
     * 根据id查询所有子菜单id集合
     * @param id
     * @return
     */
    List<Integer> findAllCheckedChildMenuById(int id);

    /**
     * 更新菜单
     * @param menu
     * @param childrenIds
     */
    void updateMenu( Menu menu, Integer[] childrenIds);
}
