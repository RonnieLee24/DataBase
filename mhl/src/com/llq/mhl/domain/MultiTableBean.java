package com.llq.mhl.domain;

import java.util.Date;

/**
 * @Author: Ronnie LEE
 * @Date: 2023/2/2 - 02 - 02 - 16:49
 * @Description: com.llq.mhl.domain
 * @version: 1.0
 * 这是一个 javaBean，可以和多张表进行对应
 */
public class MultiTableBean {
    private Integer id;
    private String  billId; // UUID
    private Integer menuId;
    private Integer nums;
    private Double money;
    private Integer diningTableId;
    private Date billDate;
    private String state; // 现金、支付宝未支付、已支付、挂单、霸王餐

    //  增加一个来自 menu 表的列 name
    private String name;
    private String price;

    public MultiTableBean() {
    }

    public MultiTableBean(Integer id, String billId, Integer menuId, Integer nums, Double money, Integer diningTableId, Date billDate, String state, String name, String price) {
        this.id = id;
        this.billId = billId;
        this.menuId = menuId;
        this.nums = nums;
        this.money = money;
        this.diningTableId = diningTableId;
        this.billDate = billDate;
        this.state = state;
        this.name = name;
        this.price = price;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }

    public Integer getNums() {
        return nums;
    }

    public void setNums(Integer nums) {
        this.nums = nums;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public Integer getDiningTableId() {
        return diningTableId;
    }

    public void setDiningTableId(Integer diningTableId) {
        this.diningTableId = diningTableId;
    }

    public Date getBillDate() {
        return billDate;
    }

    public void setBillDate(Date billDate) {
        this.billDate = billDate;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return id +
                "\t\t\t" + menuId +
                "\t\t\t" + nums +
                "\t\t\t" + money +
                "\t\t" + diningTableId +
                "\t\t\t" + billDate +
                "\t\t\t" + state +
                "\t\t" + name +
                "\t\t" + price;
    }
}
