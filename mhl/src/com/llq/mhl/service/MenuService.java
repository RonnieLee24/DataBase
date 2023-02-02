package com.llq.mhl.service;

import com.llq.mhl.dao.MenuDAO;
import com.llq.mhl.domain.Menu;

import java.util.List;

/**
 * @Author: Ronnie LEE
 * @Date: 2023/2/1 - 02 - 01 - 19:07
 * @Description: com.llq.mhl.service
 * @version: 1.0
 * 完成对 menu 表的各种操作（通过调用 MenuDAO）
 */
public class MenuService {
    //  定义 MenuDAO 属性
    private MenuDAO menuDAO = new MenuDAO();

    //  返回所有的菜品，返回给界面使用
    public List<Menu> list() {
        return menuDAO.queryMulti("select * from menu", Menu.class);
    }

    //  根据 menuId 返回 Menu 对象

    public Menu getMenuById(int menuId) {
        return menuDAO.querySingle("select * from menu where id = ?", Menu.class, menuId);
    }
}
