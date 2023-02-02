package com.llq.mhl.service;

import com.llq.mhl.dao.DiningTableDAO;
import com.llq.mhl.domain.DiningTable;

import java.util.List;

/**
 * @Author: Ronnie LEE
 * @Date: 2023/2/1 - 02 - 01 - 17:23
 * @Description: com.llq.mhl.service
 * @version: 1.0
 */
public class DiningTableService {   //  业务层
    private DiningTableDAO diningTableDAO = new DiningTableDAO();

    //  返回所有餐桌信息
    public List<DiningTable> list() {
        //  因为没有问号，所以就不需要带参数了
        List<DiningTable> diningTables = diningTableDAO.queryMulti("select id, state from diningTable", DiningTable.class);
        return diningTables;
    }

    //  根据 id，查询对应餐桌 DiningTable 对应
    //  如果返回 null，表明 id 对应餐桌不存在
    public DiningTable getDinDiningTable(int id) {
        //  小技巧，将 sql 语句放在查询分析器去测试一下
        DiningTable diningTable = diningTableDAO.querySingle("select * from diningTable where id = ?", DiningTable.class, id);
        return diningTable;
    }

    //  如果餐桌可以预定，调用方法，对其状态进行更新【包括：预定人的名字和电话】
    public boolean orderDiningTable(int id, String orderName, String orderTel) {
        int affectedRows = diningTableDAO.update("update diningTable set state = '已经预定', orderName = ?, orderTel = ? where id = ?", orderName, orderTel, id);
        return affectedRows > 0;
    }

    //  需要一个提供更新 餐桌状态的方法
    public boolean updateDiningTableStatus(int id, String state) {
        int affectedRows = diningTableDAO.update("update diningTable set state = ? where id = ?", state, id);
        return affectedRows > 0;
    }

    //  清空结完账的信息
    public boolean clearDiningTableInfo(int diningTableId, String state){
        int update = diningTableDAO.update("update diningTable set state = ?, orderName = '', orderTel = '' where id = ?", state, diningTableId);
        return update > 0;
    }
}
