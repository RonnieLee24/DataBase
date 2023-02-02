package com.llq.mhl.dao;

import com.llq.mhl.utils.JDBCUtilsByDruid;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * @Author: Ronnie LEE
 * @Date: 2023/1/30 - 01 - 30 - 15:12
 * @Description: com.llq.dao_.dao
 * @version: 1.0
 * 开发 BasicDAO，是其它 DAO 的父类
 */
public class BasicDAO<T> {  //  泛型指定具体类型
    private QueryRunner qr = new QueryRunner();

    //  开发通用的 dml 方法，针对任意的表
    public int update(String sql, Object... parameters){
        Connection connection = null;

        try {
            connection = JDBCUtilsByDruid.getConnection();
            int affectedRows = qr.update(connection, sql, parameters);
            return affectedRows;
        } catch (SQLException e) {
            throw new RuntimeException(e);  // 将编译异常 ---> 运行异常，抛出
        } finally {
            JDBCUtilsByDruid.close(null, null, connection);
        }
    }
    // 返回多个对象（即：查询的结果是多行），针日任意表

    /**
     * @param sql：sql 语句，可以有 ?
     * @param tClass：传入一个类的 Class 对象，比如 Actor.class
     * @param parameters：传入 ? 的具体的值，可以是多个
     * @return：根据 Actor.class 返回 ArrayList 集合
     */
    public List<T> queryMulti(String sql, Class<T> tClass, Object... parameters){
        Connection connection = null;

        try {
            connection = JDBCUtilsByDruid.getConnection();
            List<T> list = qr.query(connection, sql, new BeanListHandler<T>(tClass), parameters);
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);  // 将编译异常 ---> 运行异常，抛出
        } finally {
            JDBCUtilsByDruid.close(null, null, connection);
        }
    }

    //  查询单行结果的通用方法
    public T querySingle(String sql, Class<T> tClass, Object... parameters){
        Connection connection = null;

        try {
            connection = JDBCUtilsByDruid.getConnection();
            T t = qr.query(connection, sql, new BeanHandler<T>(tClass), parameters);
            return t;
        } catch (SQLException e) {
            throw new RuntimeException(e);  // 将编译异常 ---> 运行异常，抛出
        } finally {
            JDBCUtilsByDruid.close(null, null, connection);
        }
    }

    //  查询单行单列的方法，即：返回单值的方法
    public Object queryScalar(String sql, Object... parameters){
        Connection connection = null;

        try {
            connection = JDBCUtilsByDruid.getConnection();
            Object query = qr.query(connection, sql, new ScalarHandler(), parameters);
            return query;
        } catch (SQLException e) {
            throw new RuntimeException(e);  // 将编译异常 ---> 运行异常，抛出
        } finally {
            JDBCUtilsByDruid.close(null, null, connection);
        }
    }
}
