package com.health.dao;

import com.github.pagehelper.Page;
import com.health.pojo.Menu;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

public interface MenuDao {
    /**
     * 查询所有菜单集合
     * @return
     */
    List<Menu> findAllMenus();

    /**
     * 分页查询所有父菜单
     * @param queryString
     * @return
     */
    Page<Menu> findAllParentMenu(String queryString);

    /**
     * 根据path查询父菜单
     * @param path
     * @return
     */
    Menu findParentMenuByPath(String path);

    /**
     * 保存父菜单
     * @param menu
     * @return
     */
    void saveParentMenu(Menu menu);

    /**
     * 根据id查询子菜单
     * @param id
     * @return
     */
    Menu FindChildMenuById(Integer id);

    /**
     * 保存子菜单
     * @param menu2
     */
    void saveChildMenu(Menu menu2);

    /**
     * 根据id查询当前的菜单是否已经被引用
     * @param id
     * @return
     */
    Integer FindMenuCountById(int id);

    /**
     *根据id删除菜单
     * @param id
     */
    void deleteMenuById(int id);

    /**
     * 根据id查询父菜单
     * @param id
     * @return
     */
    Menu FindParentMenuById(int id);

    /**
     * 根据parentId查询所有子菜单
     * @param id
     * @return
     */
    List<Menu> FindChildMenuByParentId(int id);

    /**
     * 根据linkUrl查询对应的子菜单id
     * @param linkUrl
     * @return
     */
    Integer FindChildMenuByLinkUrl(String linkUrl);

    /**
     * 根据id删除父菜单下的所有子菜单
     * @param id
     */
    void deleteChildMenuByParentId(Integer id);

    /**
     * 更新父菜单
     * @param menu
     */
    void updateMenu(Menu menu);

    /**
     * 根据优先级查询父菜单
     * @param map
     * @return
     */
    Menu findParentMenuByPriority(Map<String,Object> map);

    //    查询所有菜单信息
    List<Menu> findAll();

}
