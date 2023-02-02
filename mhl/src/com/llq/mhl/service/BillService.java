package com.llq.mhl.service;

import com.llq.mhl.dao.BasicDAO;
import com.llq.mhl.dao.BillDAO;
import com.llq.mhl.dao.MenuDAO;
import com.llq.mhl.dao.MultiTableDAO;
import com.llq.mhl.domain.Bill;
import com.llq.mhl.domain.DiningTable;
import com.llq.mhl.domain.Menu;
import com.llq.mhl.domain.MultiTableBean;

import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * @Author: Ronnie LEE
 * @Date: 2023/2/1 - 02 - 01 - 20:40
 * @Description: com.llq.mhl.service
 * @version: 1.0
 * 处理和订单相关的与业务逻辑
 */
public class BillService {
    //  定义 BillDAO 属性
    private BillDAO billDAO = new BillDAO();
    private MultiTableDAO multiTableDAO = new MultiTableDAO();
    private MenuService menuService = new MenuService();
    private DiningTableService diningTableService = new DiningTableService();
    //  编写点餐方法 ---> 目的是生成账单
    //  1. 生成账单
    //  2. 更新餐桌对应状态
    public boolean orderMenu(int menuId, int nums, int diningTableId ){
        //  生成一个账单号 UUID
        String uuId = UUID.randomUUID().toString();

        //  将账单生成到 bill 表
        //  第 4 个 ？:对应 money 【单价 * 数量】
        Menu menu = menuService.getMenuById(menuId);

        //  将相关数据写入账单
        int affectedRows = billDAO.update("insert into bill values(null, ?, ?, ?, ?, ?, now(), '未结账')",
                uuId, menuId, nums, menu.getPrice() * nums, diningTableId);

        if (affectedRows <= 0){
            return false;
        }
        //  更新餐桌状态
        return diningTableService.updateDiningTableStatus(diningTableId, "就餐中");
    }

    //  返回所有账单
    public List<Bill> getBillList(){
        return billDAO.queryMulti("select * from bill ", Bill.class);
    }

    //  查看某个餐桌是否有未结账的账单
    public boolean isPaid(int diningTableId){
        DiningTable dinDiningTable = diningTableService.getDinDiningTable(diningTableId);
        if (dinDiningTable == null){    //  餐桌不存在
            System.out.println("输入的餐桌不存在");
            return false;
        }
        List<Bill> list = billDAO.queryMulti("select * from bill where state='未结账'", Bill.class);

        if (list.size() == 0){
            System.out.println("===============该餐位没有未结算的账单===============");
            return false;
        }

        return true;
    }

    //  完成结账【如果餐桌存在，并且有未支付的账单】
    public boolean payBill(int diningTableId, String payMode){
        //  1. 修改 bill 表
        //  2. 修改 diningTable表
        if (isPaid(diningTableId)){    //  看是否可以支付
            int affectedRows = billDAO.update(" update bill set state = ? where diningTableId = ?", payMode, diningTableId);
            if (affectedRows > 0) {  //  支付成功后，还要修改餐桌状态为 空闲
                return diningTableService.clearDiningTableInfo(diningTableId, "空");
            }
        }
        return false;
    }

    //  显示账单
    //  返回所有账单
    public List<MultiTableBean> getBillListPlus(){
        return multiTableDAO.queryMulti("select bill.*, NAME, price from menu, bill where bill.menuId = menu.id", MultiTableBean.class);
    }

}
