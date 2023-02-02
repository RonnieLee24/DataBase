package com.llq.mhl.utils;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * @Author: Ronnie LEE
 * @Date: 2023/1/29 - 01 - 29 - 15:06
 * @Description: com.llq.jdbc.datasource
 * @version: 1.0
 * 基于 Druid 数据库连接池的工具类
 */
public class JDBCUtilsByDruid {
    private static DataSource ds;

    //  在静态代码块中完成 ds 初始化
    static {
        //  1. 加入 Druid jar 包
        //  2. 加入 配置文件，将该文件拷贝到项目的 src 目录
        //  3. 创建 Properties 对象，读取配置文件
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("src/druid.properties"));
            ds = DruidDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //  编写 getConnection 方法
    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    //  关闭连接，强调：在数据库连接池技术中，close 不是真正的断掉链接
    //  而是把使用的 Connection 对象放回连接池
    public static void close(ResultSet resultSet, Statement statement, Connection connection){
        try {
            if (resultSet != null){
                resultSet.close();
            }
            if (statement != null){
                statement.close();
            }
            if (connection != null){
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
