package com.health.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.StringUtil;
import com.health.Constant.MessageConstant;
import com.health.Entity.PageResult;
import com.health.Entity.QueryPageBean;
import com.health.Exception.MenuBeUseException;
import com.health.Exception.MenuIsExistException;
import com.health.Exception.PriorityBeUseException;
import com.health.dao.MenuDao;
import com.health.interfaces.MenuService;
import com.health.pojo.CheckGroup;

import com.health.pojo.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = MenuService.class)
public class MenuServiceImpl implements MenuService {
    @Autowired
    private MenuDao menuDao;

    /**
     * 查询所有菜单集合
     *
     * @return
     */
    @Override
    public List<Menu> findAllMenus() {
        return menuDao.findAllMenus();
    }

    /**
     * 分页查询所有父菜单
     *
     * @param queryPageBean
     * @return
     */
    @Transactional
    @Override
    public PageResult<Menu> findAllParentMenu(QueryPageBean queryPageBean) {
        //使用mybatis插件进行查询
        if (!StringUtil.isEmpty(queryPageBean.getQueryString())) {
            //如果查询条件不为空
            queryPageBean.setQueryString("%" + queryPageBean.getQueryString() + "%");
        }
        //使用mybatis分页插件进行分页查询
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());

        Page<Menu> page = menuDao.findAllParentMenu(queryPageBean.getQueryString());
        PageResult<Menu> pageResult = new PageResult<>();
        pageResult.setTotal(page.getTotal());
        pageResult.setPageList(page.getResult());
        return pageResult;
    }

    /**
     * 新增菜单
     *
     * @param menu
     * @param childrenIds
     */
    @Override
    @Transactional
    public void saveMenu(Menu menu, Integer[] childrenIds) throws MenuIsExistException, PriorityBeUseException {
        //分两部分存储数据
        //判断当前的菜单是否已经存在
        Menu menu1 = menuDao.findParentMenuByPath(menu.getPath());
        if (menu1 != null) {
            //说明当前的父类菜单已经存在,则抛出异常,终止程序
            throw new MenuIsExistException(MessageConstant.MENU_PATH_IS_EXIST_FAIL);
        }

        //判断当前的优先级是否已经存在
        Map<String, Object> map = new HashMap<>();
        map.put("path", menu.getPath());
        map.put("priority", menu.getPriority());
        menu1 = menuDao.findParentMenuByPriority(map);
        if (menu1 != null) {
            throw new PriorityBeUseException("优先级已经存在");
        }

        //1、父菜单存储，并返回父菜单的id
        menuDao.saveParentMenu(menu);
        //获取自增长后的id
        Integer count = menu.getId();
        String MenuPath = menu.getPath();
        AddChildMenuByIds(childrenIds, MenuPath, count);
    }

    /**
     * 将重复代码抽取公共方法,根据ids集合添加子菜单
     *
     * @param childrenIds
     * @param MenuPath
     * @param count
     */
    public void AddChildMenuByIds(Integer[] childrenIds, String MenuPath, Integer count) {
        if (childrenIds != null && childrenIds.length > 0) {
            int endMenu = 1;
            for (Integer id : childrenIds) {
                //2、先根据id集合查询所有子菜单，再存储
                Menu menu2 = menuDao.FindChildMenuById(id);
                if (menu2 != null) {
                    //说明用户存在
                    String path = "/" + MenuPath + "-" + endMenu;
                    menu2.setPath(path);
                    menu2.setPriority(endMenu);
                    menu2.setParentMenuId(count);
                    menuDao.saveChildMenu(menu2);
                    endMenu++;
                }
            }
        }
    }

    /**
     * 根据id删除菜单
     *
     * @param id
     */
    @Transactional
    @Override
    public void deleteMenu(int id) throws MenuBeUseException {
        //先根据id查询当前的id是否被引用,如果引用则终止程序
        Integer count = menuDao.FindMenuCountById(id);
        if (count > 0) {
            //说明被引用,则终止程序
            throw new MenuBeUseException(MessageConstant.DELETE_MENU_BE_USER);
        }
        //如果没有被引用,则执行删除,删除的内容包括其子菜单
        //先删除所属的子菜单
        menuDao.deleteChildMenuByParentId(id);
        //成功后再删除父菜单
        menuDao.deleteMenuById(id);
    }

    /**
     * 根据id查询菜单回显
     *
     * @param id
     * @return
     */
    @Override
    public Menu findParentMenuById(int id) {
        return menuDao.FindParentMenuById(id);
    }

    /**
     * 根据id查询所有子菜单id集合
     *
     * @param id
     * @return
     */
    @Override
    public List<Integer> findAllCheckedChildMenuById(int id) {
        //先根据父菜单的id查询出所有所属的子菜单的linkUrl集合
        List<Menu> ChildMenu = menuDao.FindChildMenuByParentId(id);
        List<Integer> menuId = null;
        if (ChildMenu != null && ChildMenu.size() > 0) {
            //查询出其所属的所有子菜单
            menuId = new ArrayList<>();
            for (Menu childMenu : ChildMenu) {
                String linkUrl = childMenu.getLinkUrl();
                Integer ChildId = menuDao.FindChildMenuByLinkUrl(linkUrl);
                menuId.add(ChildId);
            }
        }

        return menuId;
    }

    /**
     * 更新菜单
     *
     * @param menu
     * @param childrenIds
     */
    @Transactional
    @Override
    public void updateMenu(Menu menu, Integer[] childrenIds) throws PriorityBeUseException {
        Map<String, Object> map = new HashMap<>();
        map.put("priority", menu.getPriority());
        map.put("path", menu.getPath());
        //判断优先级已经存在
        Menu Menu2 = menuDao.findParentMenuByPriority(map);
        if (Menu2 != null) {
            throw new PriorityBeUseException("修改的优先级已经存在");
        }
        //根据路径先查询菜单,并获取到id
        Menu2 = menuDao.findParentMenuByPath(menu.getPath());
        if (Menu2 != null) {
            Integer id = Menu2.getId();
            String path = Menu2.getPath();
            menu.setId(id);
            //更新父菜单
            menuDao.updateMenu(menu);
            //删除父菜单下的所有子菜单
            menuDao.deleteChildMenuByParentId(id);
            //删除成功后,再把修改后的子菜单添加到原有的表中
            AddChildMenuByIds(childrenIds, path, id);
        }
    }

    //    查询所有菜单信息
    @Override
    public List<Menu> findAll() {
        List<Menu> menuList = menuDao.findAll();
        return menuList;
    }
}
