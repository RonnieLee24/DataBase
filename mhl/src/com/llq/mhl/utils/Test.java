package com.llq.mhl.utils;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @Author: Ronnie LEE
 * @Date: 2023/1/31 - 01 - 31 - 17:43
 * @Description: com.llq.mhl.utils
 * @version: 1.0
 */
public class Test {
    public static void main(String[] args) throws SQLException {
        //  测试 Utility 工具类
        System.out.println("请输入一个整数");
        int i = Utility.readInt();
        System.out.println("i= " + i);

        //  测试 JDBCUtilitiesByDruid
        Connection connection = JDBCUtilsByDruid.getConnection();
        System.out.println(connection.getClass());
    }
}
