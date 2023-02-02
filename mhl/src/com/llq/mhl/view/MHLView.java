package com.llq.mhl.view;

import com.llq.mhl.domain.*;
import com.llq.mhl.service.BillService;
import com.llq.mhl.service.DiningTableService;
import com.llq.mhl.service.EmployeeService;
import com.llq.mhl.service.MenuService;
import com.llq.mhl.utils.Utility;
import jdk.jshell.execution.Util;

import java.util.List;
import java.util.Scanner;

/**
 * @Author: Ronnie LEE
 * @Date: 2023/1/31 - 01 - 31 - 17:51
 * @Description: com.llq.mhl.view
 * @version: 1.0
 * 这是主界面
 */
public class MHLView {
    private boolean flag = true;    //  显示主菜单
    private String key = "";    //  接收用户选择
    //  定义 EmployeeService 属性
    private EmployeeService employeeService = new EmployeeService();
    //  定义 DiningTableService 属性
    private DiningTableService diningTableService = new DiningTableService();
    //  定义 MenuService 属性
    private MenuService menuService = new MenuService();
    //  定义 billService 属性
    private BillService billService = new BillService();

    //  结账
    public void payBill(){
        System.out.println("===============结账服务===============");
        System.out.println("请选择要结账的餐桌号（-1）退出");
        int diningTableId = Utility.readInt();
        if (diningTableId == -1){
            System.out.println("===============取消结账===============");
            return;
        }
        System.out.println("结账方式（现金/支付宝/微信）回车表示退出");
        String payMode = Utility.readString(20, "");    //  如果回车，就是返回 ""
        if ("".equals(payMode)){
            System.out.println("===============未选择结账方式===============");
            return;
        }
        System.out.println("是否确认结账：Y / N");
        char key = Utility.readConfirmSelection();
        if (key == 'Y'){    //  结账
            if (billService.payBill(diningTableId, payMode)){
                System.out.println("===============完成结账===============");
            }else {
                System.out.println("===============结账失败===============");
            }
        }else {
            System.out.println("===============取消结账===============");
        }
    }

    //  查看账单
    public void getOrderBillList(){
        System.out.println("===============查看账单===============");
        System.out.println("\n编号\t\t菜名号\t\t菜品量\t\t金额\t\t桌号\t\t日期\t\t\t\t\t\t\t状态\t\t菜品名\t\t价格");
        List<MultiTableBean> billListPlus = billService.getBillListPlus();
        for (MultiTableBean listPlus : billListPlus) {
            System.out.println(listPlus);
        }
        System.out.println("===============显示完毕===============");
    }

    //  点菜：
    public void orderMenu() {
        System.out.println("===============点餐服务===============");
        System.out.println("请输入点餐桌号（-1 退出）");
        int orderDiningTableId = Utility.readInt();
        if (orderDiningTableId == -1) {
            System.out.println("===============取消点餐===============");
            return;
        }
        System.out.println("请输入点菜品号（-1 退出）");
        int orderMenuId = Utility.readInt();
        if (orderMenuId == -1) {
            System.out.println("===============取消点餐===============");
            return;
        }
        System.out.println("请输入点菜品量（-1 退出）");
        int orderNums = Utility.readInt();
        if (orderNums == -1) {
            System.out.println("===============取消点餐===============");
            return;
        }

        //  验证餐桌号是否存在
        DiningTable dinDiningTable = diningTableService.getDinDiningTable(orderDiningTableId);
        if (dinDiningTable == null) {
            System.out.println("===============餐桌号不存在===============");
            return;
        }

        //  验证菜品编号
        Menu menu = menuService.getMenuById(orderMenuId);
        if (menu == null) {
            System.out.println("===============菜品号不存在===============");
            return;
        }

        //  点餐
        if (billService.orderMenu(orderMenuId, orderNums, orderDiningTableId)){
            System.out.println("===============点餐成功===============");
        }else {
            System.out.println("===============点餐失败===============");
        }
    }

    //  展示所有菜品
    public void listMenu() {
        List<Menu> list = menuService.list();
        System.out.println("\n菜品编号\t\t菜名名\t\t类别\t\t价格");

        for (Menu menu : list) {
            System.out.println(menu);
        }
        System.out.println();
        System.out.println("===============菜品显示完毕===============");
    }

    public static void main(String[] args) {
        new MHLView().mainMenu();
    }

    //  显示所有餐桌状态
    public void listDiningTable() {
        List<DiningTable> list = diningTableService.list();
        System.out.println("\n餐桌编号\t\t餐桌状态");
        for (DiningTable diningTable : list) {
            System.out.println(diningTable);
        }
        System.out.println("===============显示完毕===============");
    }

    //  预定餐桌
    public void OrderTable() {
        System.out.println("===============预定餐桌===============");
        System.out.println("请选择要预定的餐桌编号（-1退出）");
        int orderId = Utility.readInt();
        if (orderId == -1) {
            System.out.println("===============取消预定餐桌===============");
            return;
        }
        //  该方法得到的是 Y 或 N
        char key = Utility.readConfirmSelection();
        if (key == 'Y') {
            //  根据 orderId 返回对应的 DiningTable 对象，如果为 null，说明对象不存在
            DiningTable dinDiningTable = diningTableService.getDinDiningTable(orderId);
            if (dinDiningTable != null) {
                if ("空".equals(dinDiningTable.getState())) {

                    //  接收预定信息
                    System.out.println("预定人名字：");
                    String orderName = Utility.readString(50);
                    System.out.println("预定人电话：");
                    String orderTel = Utility.readString(50);
                    if (diningTableService.orderDiningTable(orderId, orderName, orderTel)) {
                        System.out.println("===============预定成功===============");
                    } else {
                        System.out.println("===============位置错误===============");
                    }
                } else {
                    System.out.println("===============该餐桌已被预定或就餐中===============");
                }
            } else {
                System.out.println("===============预定的餐桌不存在===============");
            }

        } else {
            System.out.println("===============取消预定餐桌===============");
        }
    }

    public void mainMenu() {
        while (flag) {
            System.out.println("===============满汉楼===============");
            System.out.println("\t\t 1. 登录满汉楼");
            System.out.println("\t\t 2. 退出满汉楼");
            System.out.print("请输入你的选择：");
            key = Utility.readString(1);
            switch (key) {
                case "1":
                    System.out.println("输入员工号：");
                    String empId = Utility.readString(50);
                    System.out.println("输入密码：");
                    String pwd = Utility.readString(50);
                    //  到数据库判断
                    Employee employee = employeeService.getEmployeeByIdAndPwd(empId, pwd);
                    if (employee != null) {    //  说明用户存在
                        System.out.println("===============满汉楼登录成功[ " + employee.getName() + " ]===============\n");
                        //  显示二级菜单（循环操作）
                        while (flag) {
                            System.out.println("\n===============满汉楼二级菜单===============");
                            System.out.println("\t\t 1 显示餐桌状态");
                            System.out.println("\t\t 2 预定餐桌");
                            System.out.println("\t\t 3 显示所有菜品");
                            System.out.println("\t\t 4 点餐服务");
                            System.out.println("\t\t 5 查看账单");
                            System.out.println("\t\t 6 结账");
                            System.out.println("\t\t 9 退出满汉楼");
                            System.out.println("请输入你的选择");
                            key = Utility.readString(1);
                            switch (key) {
                                case "1":
                                    listDiningTable();  //  显示餐桌状态
                                    break;
                                case "2":
                                    OrderTable();   //  预定餐桌
                                    break;
                                case "3":
                                    listMenu(); //  显示所有菜品
                                    break;
                                case "4":
                                    orderMenu();    //  点餐服务
                                    break;
                                case "5":
                                    getOrderBillList(); //  查看订单
                                    break;
                                case "6":
                                    payBill();; //  结账
                                    break;
                                case "9":
                                    flag = false;
                                    break;
                                default:
                                    System.out.println("输入有误，请重新输入");
                                    break;
                            }
                        }
                    } else {
                        System.out.println("===============满汉楼登录失败===============");
                    }

                    break;
                case "2":
                    flag = false;
                    break;
                default:
                    System.out.println("你的输入有误，请重新输入");
            }
        }
        System.out.println("你退出了满汉楼系统~");
    }
}
