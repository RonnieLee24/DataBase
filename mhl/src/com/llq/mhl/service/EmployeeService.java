package com.llq.mhl.service;

import com.llq.mhl.dao.EmployeeDAO;
import com.llq.mhl.domain.Employee;

/**
 * @Author: Ronnie LEE
 * @Date: 2023/2/1 - 02 - 01 - 16:16
 * @Description: com.llq.mhl.service
 * @version: 1.0
 * 该类完成对 employee表的各种操作（通过调用 EmployeeDAO对象完成完成）
 */
public class EmployeeService {
    //  定义一个 EmployeeDAO 属性
    private EmployeeDAO employeeDAO = new EmployeeDAO();

    //  根据 empId 和 pwd 返回一个 Employee 对象
    //  如果查询不到，就返回 null
    public Employee getEmployeeByIdAndPwd(String empId, String pwd){
        Employee employee = employeeDAO.querySingle("select * from employee where empId= ? and pwd = md5(?)", Employee.class, empId, pwd);

        return employee;
    }
}
