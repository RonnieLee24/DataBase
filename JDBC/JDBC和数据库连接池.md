#  JDBC和数据库连接池

##  1.  JDBC概述
1）为访问不同的数据库【mysql, oracle, sqlserver等等】提供了统一的接口，为使用者屏蔽了细节问题
2）Java 程序员使用JDBC，可以连接任何提供了JDBC驱动程序的数据库系统，从而完成对数据库的各种操作
3）JDBC的基本原理
![在这里插入图片描述](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202211111711478.png)
4）模拟JDBC
![在这里插入图片描述](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202211111711221.png)··
定义接口规范：

```java
package com.llq.jdbc.myjdbc;
/**
 * 我们规定的 jdbc 接口 （方法）
 */
public interface JdbcInterface {
    //  连接
    public Object getConnection();

    //  CRUD
    public void crud();

    //  关闭连接
    public void close();
}
```

mysql 数据库实现了 jdbc 接口【模拟】【mysql 厂商开发】

```java
package com.llq.jdbc.myjdbc;
/**
 * mysql 数据库实现了 jdbc接口 【模拟】【mysql 厂商开发】
 */
public class MysqlJdbcImpl implements JdbcInterface {
    @Override
    public Object getConnection() {
        System.out.println("得到 mysql 的连接");
        return null;
    }

    @Override
    public void crud() {
        System.out.println("完成 mysql 增删改查");
    }

    @Override
    public void close() {
        System.out.println("关闭 mysql 的连接");
    }
}
```
```java
public class TestJdbc {
    //这是一个main方法，是程序的入口
    public static void main(String[] args) {
        //  完成对 mysql 的操作
        JdbcInterface mysqlJdbc = new MysqlJdbcImpl();  //  用接口来引用它，因为这样可以统一起来
        mysqlJdbc.getConnection();  //  动态绑定
        mysqlJdbc.crud();
        mysqlJdbc.close();

        System.out.println();
        JdbcInterface oracleJdbc = new OracleJdbcImpl();
        oracleJdbc.getConnection();
        oracleJdbc.crud();
        oracleJdbc.close();
    }
}
```

![image-20230117161943726](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301171619813.png)

```java
package com.llq.jdbc.myjdbc;
/**
 * 模拟 oracle 数据库连接
 */
public class OracleJdbcImpl implements JdbcInterface {

    @Override
    public Object getConnection() {
        System.out.println("ORACLE数据库 连接");
        return null;
    }

    @Override
    public void crud() {
        System.out.println("ORACLE数据库 增删改查");
    }

    @Override
    public void close() {
        System.out.println("ORACLE数据库 关闭");
    }
}
```

JDBC带来的好处：

1. JDBC是java提供一套用于数据库操作的接口API，**<font color="color">JAVA程序员只需要面向这套接口编程即可</font>**
2. 不同的数据库厂商，需要针对这套接口，提供不同的实现

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202211111856502.png)

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202211111857014.png)

- javax：扩展
- JDBC驱动：实现接口的类

## 2.  JDBC快速入门（编写步骤）

Connection：客户端（或者说 java 程序）到数据库的连接
![在这里插入图片描述](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202211111711312.png)

### 2.1 注册驱动 --- 加载 Driver类

### 2.2 获取连接 --- 得到 Connection

### 2.3 执行 CRUD --- 发送 SQL 语句给 mysql 执行【statement 对象来执行！！！】

### 2.4 释放资源 --- 关闭相关连接【statement.close()、connect.close()】

## 3.  JDBC 第一个程序

```sql
CREATE table actor( # 演员表
				id int PRIMARY key auto_increment,
				name VARCHAR(32) not null DEFAULT '',
				sex char(1) not null DEFAULT '女',
				borndate datetime,
				phone varchar(12));
```
JDBC第一个程序：

mysql厂商实现的JDBC规范的一系列的类

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202211111900070.png)

![在这里插入图片描述](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202211111711459.png)

```java
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//
package com.mysql.jdbc;

import java.sql.DriverManager;
import java.sql.SQLException;

public class Driver extends NonRegisteringDriver implements java.sql.Driver {
    public Driver() throws SQLException {
    }

    static {
        try {
            DriverManager.registerDriver(new Driver());	//	静态代码块中，已经帮我们完成注册了
        } catch (SQLException var1) {
            throw new RuntimeException("Can't register driver!");
        }
    }
}

```

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202211111904315.png)



mysql 本身是一个服务，它会在这里进行监听（3306 端口）

```java
//	jdbc:mysql:// 是协议，固定的
//	通过 jdbc 的方式连接 mysql
//	localhost: 主机 / ip 地址 
//	3306：监听的端口
//	db02：；连接到 mysql 的哪个数据库
//	mysql 连接本质就是 前面学过的 socket 连接
String url = "jdbc:mysql://localhost:3306/db02";
```

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202211111905951.png)

将用户名 和 密码放入到 Properties 对象中

```java
//  将用户名和密码 放入到 Properties 对象
Properties properties = new Properties();
//  说明 user 和 password 是定死的，后面根据实际情况填写
properties.setProperty("user", "root");
properties.setProperty("password", "352420kobe24llq");
```

![image-20230117165551901](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301171655950.png)

下面该获取连接了：

```java
//	获取连接
//	参数为 Url 和 Poperties 对象
Connection connect = driver.connect(url, properties);
```

![image-20230117170323451](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301171703508.png)

现在连接是有了，但是真正帮我们执行 sql 语句 是要靠 java statement 对象的

- 当于帮我们把 sql 语句 通过 连接，发送到 mysql dbms
- 执行完之后，也可以通过 java statement对象 得到返回的结果

```java
//	statement 用于执行静态 SQL 语句，并返回其生成的结果！！！
Statement statement = connect.createStatement();
int rows = statement executeUpate(String sql);	//	如果是 dml 语句，返回的是影响的行数
System.out.println(rows > 0? "成功": "失败");
```

![image-20230117170839446](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301171708515.png)

![image-20230117173047514](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301171730580.png)

最后关闭连接！！！

```java
//  4.  释放资源 --- 关闭相关连接（资源）
statement.close();
connect.close();
//	不关闭的话，连接是一直存在的
//	会造成后面的 java 程序连接不到 mysql 的情况！！！
//	所以，与哦那
```

完整程序如下：

```java
/**
 * 这是第一个 JDBC 程序，完成简单的操作
 * 前置工作：在项目下创建一个文件夹，比如：lib
 * 将 mysql.jar f拷贝到该目录下，点击 add as libraries    
 */
public class Jdbc01 {
    public static void main(String[] args) throws SQLException {

        //  1.  注册驱动 --- 加载 Driver 类
        Driver driver = new Driver();

        //  2.  获取连接 --- 得到 Connection 客户端（java程序） 到 数据库的连接 
        String url = "jdbc:mysql://localhost:3306/db02";

        //  将用户名和密码 放入到 Properties 对象
        //  说明 user 和 password 是定死的，后面根据实际情况填写
        Properties properties = new Properties();
        properties.setProperty("user", "root");
        properties.setProperty("password", "352420kobe24llq");

        Connection connect = driver.connect(url, properties);

        //  3.  执行增删改查 --- 发送 SQL 给 Mysql 执行
//        String sql = "insert into actor values (null, '刘德华', '男', '1970-11-11', '110')";
//        String sql = "update actor set name='周星驰' where id = 1";
        String sql = "delete from actor where id = 1";
        //  用于执行静态 SQL 语句并返回其生成的结果的对象
        Statement statement = connect.createStatement();
        int rows = statement.executeUpdate(sql);    //  如果是 dml 语句，返回的就是影响的行数
        System.out.println(rows > 0 ? "成功" : "失败");

        //  4.  释放资源 --- 关闭相关连接（资源）
        statement.close();
        connect.close();
    }
}
```
![在这里插入图片描述](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202211111711565.png)

## 获取数据库连接的  5种 方式：
```java
/**
 * 分析 java 连接 mysql 的 5种方式
 */
public class JdbcConn {
    public static void main(String[] args) {

    }
    //  方式1
    @Test
    public void connect01() throws SQLException {
        //  1.  注册驱动 --- 加载 Driver 类
        Driver driver = new Driver();
        String url = "jdbc:mysql://localhost:3306/db02";
        //  将用户名和密码 放入到 Properties 对象
        //  说明 user 和 password 是定死的，后面根据实际情况填写
        Properties properties = new Properties();
        properties.setProperty("user", "root");
        properties.setProperty("password", "352420kobe24llq");
        Connection connect = driver.connect(url, properties);
        System.out.println(connect);
    }
    //  方式2
    @Test
    public void connect02() throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        //  使用反射加载 Driver类，动态加载，更加的灵活，减少依赖性
        Class<?> aClass = Class.forName("com.mysql.jdbc.Driver");
        Driver driver = (Driver) aClass.newInstance();	//	类型转换
        String url = "jdbc:mysql://localhost:3306/db02";
        //  将用户名和密码 放入到 Properties 对象
        //  说明 user 和 password 是定死的，后面根据实际情况填写
        Properties properties = new Properties();
        properties.setProperty("user", "root");
        properties.setProperty("password", "352420kobe24llq");
        Connection connect = driver.connect(url, properties);
        System.out.println("方式2 = " + connect);
    }
    //  方式3 使用 DriverManager 替代 Driver 进行统一管理
    @Test
    public void connect03() throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        //  使用反射加载 Driver
        Class<?> aClass = Class.forName("com.mysql.jdbc.Driver");
        Driver driver = (Driver) aClass.newInstance();

        //  创建 url 和 user 和 password
        String url = "jdbc:mysql://localhost:3306/db02";
        String user = "root";
        String password = "352420kobe24llq";
        DriverManager.registerDriver(driver);   //  注册 Driver 驱动
        Connection connection = DriverManager.getConnection(url, user, password);
        System.out.println("第三种方式 = " + connection);
    }

    //  方式4：使用 Class.forName 自动完成注册驱动，简化代码
    //  这种方式获取连接是使用得最多的，推荐使用
    @Test
    public void connect04() throws ClassNotFoundException, SQLException {
        //  使用反射加载 Driver 类
        //  在加载 Driver 类时，完成注册
	   Class.forName("com.mysql.jdbc.Driver");
        //  创建 url 和 user 和 password
        String url = "jdbc:mysql://localhost:3306/db02";
        String user = "root";
        String password = "352420kobe24llq";
        Connection connection = DriverManager.getConnection(url, user, password);

        System.out.println("第4种方式 " + connection);
    }

    //  方式5：在 4 的基础上改进，增加配置文件，让来凝结 mysql 更加灵活
    @Test
    public void connect05() throws IOException, ClassNotFoundException, SQLException {
        //  通过 properties 对象 获取配置文件的信息
        Properties properties = new Properties();
        properties.load(new FileInputStream("src/mysql.properties"));	//	IO 流
        String user = properties.getProperty("user");
        String password = properties.getProperty("password");
        String url = properties.getProperty("url");
        String driver = properties.getProperty("driver");
        Class.forName(driver); //  建议写上
        Connection connection = DriverManager.getConnection(url, user, password);

        System.out.println("方式5 = " + connection);
    }
}
```

### 1）com.mysql.jdbc.Driver()：静态加载
dirver ---> driver.connect(url, info)
这个 driver 是第三方的，而且是静态加载，灵活性不够强，依赖性比较高

### 2）反射机制：Class.forName()
动态加载，更加灵活，减少依赖性，这些信息还可以写在配置文件上就会更加方便

```java
//  公有构造器
Constructor<?> constructor = aClass.getConstructor(String.class);
Object o2 = constructor.newInstance("11");
System.out.println(o2);

//	调用无参构造器的写法
aClass.getConstructor().newInstance();
```



### 3）使用 DriverManager 替换 Driver
![在这里插入图片描述](https://img-blog.csdnimg.cn/222151f5872a45bc90b5b835e3d7b732.png)
![在这里插入图片描述](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202211111711587.png)
统一的 DriverManager 来进行驱动的注册与管理
![在这里插入图片描述](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202211111711696.png)
![在这里插入图片描述](https://img-blog.csdnimg.cn/5242a575cfd74611bb9a72141920eff8.png)

### 4）使用 Class.forName 自动完成注册驱动，简化代码 ===> 分析源码
 在加载 Driver 类时，完成注册
```java
public class Driver extends NonRegisteringDriver implements java.sql.Driver {
    public Driver() throws SQLException {
    }
	//	1.  静态代码块，在类加载时，会执行一次
	//	2.	DriverManager.registerDriver(new Driver());
	//	3.	因此注册 driver 的工作已经完成
    static {
        try {
            DriverManager.registerDriver(new Driver());	//	注册 driver 的工作已经完成 
        } catch (SQLException var1) {
            throw new RuntimeException("Can't register driver!");
        }
    }
}
```

细节：Class.forName("com.mysql,jdbc.Driver")这句话也可以省略![在这里插入图片描述](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202211111711779.png)
![在这里插入图片描述](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202211111711722.png)

建议还是写上 Class.forName("com.mysql.jdbc.Driver")，因为更加明确，假如说你的驱动变化了，如果里面有好几个驱动，会造成混乱
![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202211111908882.png)

### 5）使用配置文件，连接数据库更加灵活
- 端口、数据库、用户名、密码
- 为了方便都可以将信息写入到 .properties 文件中，方便操作
- 然后用 properties 类来进行控制
  ![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202211111909717.png)

```properties
# 注意 = 之间没有空格！！！
# 在 src 目录下创建 mysql.properties 文件
user=root
password=352420kobe24llq
url=jdbc:mysql://localhost:3306/db02
driver=com.mysql.jdbc.Driver
```
## 4. ResultSet【结果集】

1. 表示数据库结果集的数据表，通常通过查询数据库的语句生成
2. ResultSet 对象保持一个光标指向当前的数据行。最初，光标位于第一行之前
3. next 方法将光标移动到下一行，并且由于在 ResultSet 对象中没有更多行时返回 false，因此可以在 whiile 循环中使用循环来遍历结果集

![image-20230119181049939](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301191810010.png)

![image-20230119181520044](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301191815133.png)

在原来基础上，再添加 2 条数据：

```sql
# 插入 2 条 数据
INSERT INTO actor VALUES
	(NULL, '小白', '女', '1999-11-11', '888'),
	(NULL, '小黑', '男', '1998-08-08', '666');
```

![jjhhkk](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301191900710.png)

```java
/**
 * 演示 select 语句，返回 ResultSet，并取出结果
 */
public class resutlSet_ {
    public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {
        //  通过 properties 对象 获取配置文件的信息
        Properties properties = new Properties();
        properties.load(new FileInputStream("src/mysql.properties"));
        String user = properties.getProperty("user");
        String password = properties.getProperty("password");
        String url = properties.getProperty("url");
        String driver = properties.getProperty("driver");
        //  1. 注册驱动
        Class.forName(driver);
        //  2. 得到连接
        Connection connection = DriverManager.getConnection(url, user, password);
        //  3. 得到 statement
        Statement statement = connection.createStatement();
        //  4. 组织 sql
        String sql = "select id, name, sex, borndate from actor";
        ResultSet resultSet = statement.executeQuery(sql);
        //  5. 使用 while 取出数据
        while (resultSet.next()){
            int id = resultSet.getInt(1);   //  获取该行的第一列
            String name = resultSet.getString(2); //  获取该行的第2列
            String sex = resultSet.getString(3); //  获取该行的第3列
            Date date = resultSet.getDate(4);
            System.out.println(id + "\t" + name + "\t" + sex + "\t" + date);
 
        }

        //  6. 关闭连接
        resultSet.close();
        statement.close();
        connection.close();
        System.out.println("方式5 = " + connection);
    }
}
```

![image-20230119190100958](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301191901006.png)

ResultSet 源码分析：

ResultSet 是一个接口，它真正的类型是：

![image-20230119190547520](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301191905613.png)

真正的数据是放在对象数组里面的

![image-20230119190830839](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301191908897.png)

## 5. SQL 注入

![123](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301192324051.jpg)

```sql
# 正常查询流程

CREATE TABLE admin ( -- 管理员表
NAME VARCHAR(32) NOT NULL UNIQUE,
pwd VARCHAR(32) NOT NULL DEFAULT '') CHARACTER SET utf8;

INSERT INTO admin VALUES('tom', '123');

SELECT * FROM admin
		WHERE NAME = 'tom' AND pwd = '123'
```

![image-20230119233128149](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301192331198.png)

```java
-- SQL
-- 输入用户名为 1' or
-- 输入万能密码为or '1'= '1
SELECT *
FROM admin
WHERE NAME = '1' OR' AND pwd = 'OR '1'= '1'
SELECT * FROM admin
```

WHERE NAME = '<font color="yellow">1' OR</font>' AND pwd = ' <font color="yellow">OR '1'= '1</font>'

```java
/**
 * 演示 SQL 注入
 */
public class Statement_ {
    public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {
        Scanner in = new Scanner(System.in);
        //  让用户输入管理员名和密码
        System.out.println("用户为：");
        String admin_name = in.nextLine();
        System.out.println("密码为：");
        String admin_pwd = in.nextLine();

        //  通过 properties 对象 获取配置文件的信息
        Properties properties = new Properties();
        properties.load(new FileInputStream("src/mysql.properties"));
        String user = properties.getProperty("user");
        String password = properties.getProperty("password");
        String url = properties.getProperty("url");
        String driver = properties.getProperty("driver");
        //  1. 注册驱动
        Class.forName(driver);
        //  2. 得到连接
        Connection connection = DriverManager.getConnection(url, user, password);
        //  3. 得到 statement
        Statement statement = connection.createStatement();
        //  4. 组织 sql
        String sql = "select name, pwd from admin where name="
                + "'"+admin_name+"' and pwd='"+ admin_pwd + "'";
        ResultSet resultSet = statement.executeQuery(sql);
        //  5. 使用 while 取出数据
        if (resultSet.next()){
            System.out.println("HACK!!!!");
        }else {
            System.out.println("sorry!!!");
        }

        //  6. 关闭连接
        resultSet.close();
        statement.close();
        connection.close();
    }
}
```

![image-20230120000151018](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301200001092.png)

## 5. PreparedStatement

1. PreparedStatement 执行的 SQL 语句中的参数用问好（?）来表示，调用 PreparedStatement 对象的 <font color="yellow">setXxx()方法 【解决 sql 注入的关键】</font>来设置这些参数，setXxx() 方法有 2 个参数：

   ```sql
   String sql = "SELECT COUNT(*) FROM admin WHERE username=? AND PASSWORD=?";
   ```

   - 第一个：要设置的 SQL 语句中的参数的索引（从 1 开始）

   - 第二个：设置的 SQL 语句中的参数的值

     ![image-20230120122337624](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301201223697.png)

2. 调用 executeQuery()，返回 ResutlSet 对象

3. 调用 executeUpdate()：执行更新，包括：增、删、修改

   ![image-20230120122600835](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301201226898.png)

![image-20230120121425342](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301201214497.png)

预处理的好处：

1. 不再使用 + 拼接 sql 语句，减少语法错误
2. 有效地解决了 sql 注入问题i！
3. 大大减少了编译次数，效率较高

```java
/**
 * 演示 PreparedStatement 使用
 */
public class PreparedStatement_ {
    public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {
        Scanner in = new Scanner(System.in);
        //  让用户输入管理员名和密码
        System.out.print("用户为：");
        String admin_name = in.nextLine();
        System.out.print("密码为：");
        String admin_pwd = in.nextLine();

        //  通过 properties 对象 获取配置文件的信息
        Properties properties = new Properties();
        properties.load(new FileInputStream("src/mysql.properties"));
        String user = properties.getProperty("user");
        String password = properties.getProperty("password");
        String url = properties.getProperty("url");
        String driver = properties.getProperty("driver");
        //  1. 注册驱动
        Class.forName(driver);
        //  2. 得到连接
        Connection connection = DriverManager.getConnection(url, user, password);
        //  3. 得到 preparedStatement
        //     组织 sql
        //  sql 语句的 ？相当于 占位符！！！
        String sql = "select name, pwd from admin where name= ? and pwd = ?";
        //  preparedStatement 对象实现了 PreparedStatement 接口的实现类的对象
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        //  给 ? 赋值【站位符 从 1 开始计算！！!】
        preparedStatement.setString(1, admin_name);
        preparedStatement.setString(2, admin_pwd);

        //  执行 select 语句使用 executeQuery
        //  如果执行的是 dml(update, insert, delete) 使用 executeUpdate()
        //  这里执行 executeQuery 时，不要再写 sql 了，因为前面已经绑定过了
        ResultSet resultSet = preparedStatement.executeQuery();
        //  5. 使用 while 取出数据
        if (resultSet.next()){
            System.out.println("HACK!!!!");
        }else {
            System.out.println("sorry!!!");
        }

        //  6. 关闭连接
        resultSet.close();
        preparedStatement.close();
        connection.close();
    }
}
```

![image-20230120124638014](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301201246081.png)

预处理 DML：

添加用户：

```java
public class DML {
    public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException {
        Scanner in = new Scanner(System.in);
        //  让用户输入管理员名和密码
        System.out.print("用户为：");
        String admin_name = in.nextLine();
        System.out.print("密码为：");
        String admin_pwd = in.nextLine();

        //  通过 properties 对象 获取配置文件的信息
        Properties properties = new Properties();
        properties.load(new FileInputStream("src/mysql.properties"));
        String user = properties.getProperty("user");
        String password = properties.getProperty("password");
        String url = properties.getProperty("url");
        String driver = properties.getProperty("driver");
        //  1. 注册驱动
        Class.forName(driver);
        //  2. 得到连接
        Connection connection = DriverManager.getConnection(url, user, password);
        //  3. 得到 preparedStatement
        //     添加 记录
        String sql = "insert into admin values(?, ?)";
        //  preparedStatement 对象实现了 PreparedStatement 接口的实现类的对象
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        //  给 ? 赋值【站位符 从 1 开始计算！！!】
        preparedStatement.setString(1, admin_name);
        preparedStatement.setString(2, admin_pwd);

        //  执行 dml 语句，使用 executeUpdate
        int rows = preparedStatement.executeUpdate();
        System.out.println(rows>0?"执行成功":"执行失败");
        //  6. 关闭连接
        preparedStatement.close();
        connection.close();
    }
}
```

![image-20230120155332281](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301201553344.png)

![image-20230120155352760](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301201553821.png)

修改用户  

```java
//  3. 得到 preparedStatement
//     修改 记录
String sql = "update admin set pwd=? where name=?";
//  preparedStatement 对象实现了 PreparedStatement 接口的实现类的对象
PreparedStatement preparedStatement = connection.prepareStatement(sql);
//  给 ? 赋值【站位符 从 1 开始计算！！!】
preparedStatement.setString(2, admin_name);
preparedStatement.setString(1, admin_pwd);	//	注意与 ？的对应关系！！！
```

删除用户

```java
//     删除 记录
String sql = "delete from admin where name=?";
//  preparedStatement 对象实现了 PreparedStatement 接口的实现类的对象
PreparedStatement preparedStatement = connection.prepareStatement(sql);
//  给 ? 赋值【站位符 从 1 开始计算！！!】
preparedStatement.setString(1, admin_name);
```

## 6. JDBC API 小结

![ffgg](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301201617841.png)

## 7.  JDBC Utils

在 JDBC 操作中，获取连接 和 释放资源 是经常使用到的，可以将其封装到 JDBC连接工具类 JDBCUtils

![image-20230120162232693](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301201622786.png)

![image-20230120162333312](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301201623412.png)

![image-20230120164411086](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301201644162.png)

![image-20230120164553427](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301201645496.png)

```java
/**
 * 这是一个工具类，完成 mysql的连接和关闭资源
 */
public class JDBCUtils {
    //  定义相关属性（4个），因为只需要一份，因此，我们做成 static
    private static String user;
    private static String password;
    private static String url;
    private static String driver;

    //  在 static 代码块中初始化
    static {
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream("src/mysql.properties"));
            //  读取相关属性
            user = properties.getProperty("user");
            password = properties.getProperty("password");
            url = properties.getProperty("url");
            driver = properties.getProperty("driver");
        } catch (IOException e) {
            //  在实际开发中，我们可以这样处理
            //  1. 将编译异常转成 运行异常
            //  2. 这时，调用者，可以选择捕获该异常，也可以选择默认处理该异常，比较方便
            throw  new RuntimeException(e);
        }
    }

    //  连接数据库，返回 Connection
    public static Connection getConnection(){
        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            //  1. 将编译异常转成 运行异常
            //  2. 这时，调用者，可以选择捕获该异常，也可以选择默认处理该异常，比较方便
            throw new RuntimeException(e);
        }
    }

    //  关闭相关资源
    //  1. ResultSet 结果集
    //  2. Statement 或者 PreparedStatement 【Statement 是 PreparedStatement 的父接口】
    //  3. Connection
    //  4. 如果需要关闭资源，就传入对象，否则传入 null
    public static void close(ResultSet set, Statement statement, Connection connection){
        try {
            if (set != null){
                set.close();
            }
            if (statement!=null){
                statement.close();
            }
            if (connection!=null){
                connection.close();
            }
        } catch (SQLException e) {
            //  将编译异常转为运行异常抛出
            throw new RuntimeException(e);
        }
    }
}
```

```java
/**
 * 该类演示如何使用 JDBCUtils 工具类，完成 dml 和 select
 */
public class JDBCUtils_Use {
    public static void main(String[] args) {
    }

    @Test
    public void testDML(){  //  insert、update、delete
        //  1. 得到连接
        Connection connection = null;
        //  2. 组织一个 sql
        String sql = "update actor set name = ? where id = ?";
        //  3. 创建 PreparedStatement 对象
        PreparedStatement preparedStatement = null; //  扩大作用域！！！
        try {
            connection = JDBCUtils.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            //  给占位符赋值
            preparedStatement.setString(1, "周星驰");
            preparedStatement.setInt(2, 4);
            //  执行
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //  关闭资源
            JDBCUtils.close(null, preparedStatement, connection);
        }
    }
}
```

![image-20230120175545159](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301201755226.png)

查询

```java
@Test
public void testSelect(){  //  insert、update、delete
    //  1. 得到连接
    Connection connection = null;
    //  2. 组织一个 sql
    String sql = "select * from actor";
    //  3. 创建 PreparedStatement 对象
    PreparedStatement preparedStatement = null; //  扩大作用域！！！
    ResultSet set = null;
    try {
        connection = JDBCUtils.getConnection();
        preparedStatement = connection.prepareStatement(sql);
        //  执行，得到结果集
        set = preparedStatement.executeQuery();
        //  遍历结果集
        while (set.next()){
            //  注意 get 后面接的是 返回类型！！！
            int id = set.getInt(1);   //  获取该行的第一列
            String name = set.getString(2); //  获取该行的第2列
            String sex = set.getString(3); //  获取该行的第3列
            Date date = set.getDate(4);
            String phone = set.getString(5);
            System.out.println(id + "\t" + name + "\t" + sex + "\t" + date + "\t" + phone);
        }
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        //  关闭资源
        JDBCUtils.close(set, preparedStatement, connection);
    }
}
```

## 5.  事务

### 5.1 基本介绍

1. JDBC程序中当一个 Connection对象创建时，默认情况下自动提交事务，每次执行一个 SQL 语句时，如果执行成功，就会向数据库自动提交，而不能回滚
2. JDBC程序中让多个 SQL 语句作为一个整体执行，需要 <font color="yellow">**使用事务**</font>
3. 调用 Connection 的 setAutoCommit(false) 可以取消自动提交事务
4. 在所有 SQL 语句都执行成功后，调用 Connection 的 commit() 方法提交事务
5. 在其中某个操作失败或出现异常时，调用 Connection 的 rollback() 方法回滚事务

```sql
create table account(
	id int primary key auto_increment,
	name varchar(32) not null default '',
	balance DOUBLE not null default 0) character set utf8;

insert into account values(null, '马云', 3000);
insert into account values(null, '马化腾',10000);
```

### 5.2 不使用事务

先不使用事务 ---> 会出现数据不一致的问题！！！

默认情况下，connection 默认自动提交

```java
/**
 * 演示 JDBC 中如何使用事务
 */
public class transaction_ {
    public static void main(String[] args) {

    }

    @Test
    public void noTransaction() {
        //  操作转账的业务
        //  1. 得到连接
        Connection connection = null;
        //  2. 组织一个 sql
        String sql1 = "update account set balance = balance - 100 where id = 1";
        String sql2 = "update account set balance = balance + 100 where id = 3";
        //  3. 创建 PreparedStatement 对象
        PreparedStatement preparedStatement = null; //  扩大作用域！！！
        try {
            connection = JDBCUtils.getConnection();
            preparedStatement = connection.prepareStatement(sql1);
            //  执行第一条 sql
            preparedStatement.executeUpdate();

            int i = 1 / 0;  //  抛出异常 --> 下面代码不会再执行了！！！
            //  执行第二条 sql
            preparedStatement = connection.prepareStatement(sql2);
            preparedStatement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //  关闭资源
            JDBCUtils.close(null, preparedStatement, connection);
        }
    }
}
```

![image-20230128180715540](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301281807660.png)

### 5.3 使用事务解决

```java
public void UseTransaction() {
    //  操作转账的业务
    //  1. 得到连接
    Connection connection = null;
    //  2. 组织一个 sql
    String sql1 = "update account set balance = balance - 100 where id = 1";
    String sql2 = "update account set balance = balance + 100 where id = 3";
    //  3. 创建 PreparedStatement 对象
    PreparedStatement preparedStatement = null; //  扩大作用域！！！
    try {
        connection = JDBCUtils.getConnection();
        //  将 connection 设置为不自动提交
        connection.setAutoCommit(false);
        preparedStatement = connection.prepareStatement(sql1);
        //  执行第一条 sql
        preparedStatement.executeUpdate();

        int i = 1 / 0;  //  抛出异常 --> 下面代码不会再执行了！！！
        //  执行第二条 sql
        preparedStatement = connection.prepareStatement(sql2);
        preparedStatement.executeUpdate();

        //  这里提交事务
        connection.commit();
    } catch (Exception e) {
        //  这里我们可以进行回滚，即：撤销执行的 sql 语句
        System.out.println("执行发生了异常，撤销执行的sql");
        try {
            connection.rollback();         //  默认回滚到事务开始的状态
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        e.printStackTrace();
    } finally {
        //  关闭资源
        JDBCUtils.close(null, preparedStatement, connection);
    }
}
```

![image-20230128181646579](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301281816673.png)

下面将人为制造的异常拿掉！！！---> 转账成功！！！

![image-20230128181904158](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301281819234.png)

## 6.  批处理

### 6.1 基本介绍

1. 当需要成批插入或者更新记录时，可以采用 Java 的批量更新机制，这一机制允许多条语句一次性提交给数据库批量处理。通常情况下比单独提交处理更有效率
2. JDBC 的批量处理语句包括如下方法：
   - addBatch()：添加需要批量处理的 SQL 语句或参数
   - executeBatch()：执行批量处理语句
   - clearBatch()：清空批处理包的语句
3. JDBC连接 MySQL时，如果要使用批处理功能，在 url 中添加参数 <font color="yellow">?rewriteBatchedStatements=true</font>
4. 批处理往往和 PreparedStatement一起搭配使用，可以既减少编译次数，又减少运行次数，提高效率

创建测试表：

```sql
create table admin2(
	id int primary key auto_increment,
	username varchar(32) not NULL,
	password varchar(32) not NULL);
```

### 6.2 传统方式

```java
//	6414 ms
public void noBatch() throws Exception {
    Connection connection = JDBCUtils.getConnection();
    String sql = "insert into admin2 values(null, ?, ?)";
    PreparedStatement preparedStatement = connection.prepareStatement(sql);
    System.out.println("开始执行");
    long start = System.currentTimeMillis();
    for (int i = 0; i < 5000; i++) {
        preparedStatement.setString(1, "jacck" + i);
        preparedStatement.setString(2, "666");
        preparedStatement.executeUpdate();
    }
    long end = System.currentTimeMillis();
    System.out.println("传统方式耗时：" + (end - start));
    //  关闭连接
    JDBCUtils.close(null, preparedStatement, connection);
}
```

### 6.3 使用批量方式添加数据

注意在 url中添加语句

```java
?rewriteBatchedStatements=true
```

![image-20230128185955639](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301281859739.png)

```java
//  使用批量方式添加数据
@Test
public void useBatch() throws Exception {
    Connection connection = JDBCUtils.getConnection();
    String sql = "insert into admin2 values(null, ?, ?)";
    PreparedStatement preparedStatement = connection.prepareStatement(sql);
    System.out.println("开始执行");
    long start = System.currentTimeMillis();
    for (int i = 0; i < 5000; i++) {
        preparedStatement.setString(1, "jacck" + i);
        preparedStatement.setString(2, "666");
        //  将 sql 语句加入到 批处理包中 ---> 看源码
        preparedStatement.addBatch();
        //  每当有 1000 条记录时，再批量执行
        if ((i + 1) % 1000 == 0) {
            preparedStatement.executeBatch();
            //  清空一把
            preparedStatement.clearBatch();
        }
    }
    long end = System.currentTimeMillis();
    System.out.println("批量方式 耗时：" + (end - start));
    //  关闭连接
    JDBCUtils.close(null, preparedStatement, connection);
}
```

![image-20230128190658328](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301281906426.png)

### 6.4 批处理源码分析

```java
//  将 sql 语句加入到 批处理包中 ---> 看源码
preparedStatement.addBatch();
```

下断点执行：

```java
public void addBatch() throws SQLException {
    synchronized(this.checkClosed().getConnectionMutex()) {
        if (this.batchedArgs == null) {
   	// 1. 第一次就创建 ArrayList --- elementData => Object[] 
    // 2. 就会存放我们预处理的 sql 语句
    // 3. 当 elementData 满后，就按照 1.5 倍扩容
    // 4. 当添加到指定值后，就 executeBatch
    // 5. 批量处理会减少我们发送 sql语句的网络开销，而且减少编译次数，因此提升效率！！！
            this.batchedArgs = new ArrayList();
        }

        for(int i = 0; i < this.parameterValues.length; ++i) {
            this.checkAllParametersSet(this.parameterValues[i], this.parameterStreams[i], i);
        }

        this.batchedArgs.add(new PreparedStatement.BatchParams(this.parameterValues, this.parameterStreams, this.isStream, this.streamLengths, this.isNull));
    }
}
```

![image-20230128191716219](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301281917305.png)

## 7.  连接池

### 7.1 传统连接弊端分析

```java
//  连接数据库 5000 次
@Test
public void testCon() {
    for (int i = 0; i < 5000; i++) {
        //  使用传统 JDBC方式，得到连接
        Connection connection = JDBCUtils.getConnection();
        //  做一些工作，比如得到 PrepareStatement，发送 sql
        //  故意不关闭连接，看是否会出现问题
    }
}
```

![image-20230128193307583](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301281933662.png)

现在如果及时关闭，看看 连接 --- 关闭 会耗时多少！！！

```java
//  连接数据库 5000 次
@Test
public void testCon() {
    long start = System.currentTimeMillis();
    for (int i = 0; i < 5000; i++) {
        //  使用传统 JDBC方式，得到连接
        Connection connection = JDBCUtils.getConnection();
        //  做一些工作，比如得到 PrepareStatement，发送 sql
        //  及时关闭
        JDBCUtils.close(null, null, connection);
    }
    long end = System.currentTimeMillis();
    System.out.println("耗时：" + (end - start));
}

//	耗时：8172 ms
```

问题分析：

1. 传统的 JDBC 数据库连接使用 DriverManager 来获取
   - 每次向数据库建立连接的时候都将 Connection 加载到内存中
   - 再验证 <font color="yellow">IP 地址</font>，<font color="yellow">用户名 </font>和 <font color="yellow">密码</font>【0.05s ~ 1s 时间】。
   - 需要数据库连接的时候，就向数据库要求一个
   - 频繁进行数据库连接操作将占用很多的系统资源，容易造成服务器崩溃
2. 每一次数据库连接，使用完后逗得断开，如果程序出现异常而未能关闭，将导致数据库<font color="yellow">内存泄漏</font>，最终将导致重启数据库。
3. 传统获取连接的方式，不能控制创建的连接数量，如连接过多，也可能导致内存泄漏，Mysql 崩溃
4. 解决传统开发中的 数据库连接问题，可以采用数据库连接池技术（connectino pool）

### 7.2 数据库连接池

给张三打电话，打完了，说：不要挂电话，你让李四接电话！！！

可以理解成电话一直都没挂断

春节售票窗口 ---> 等待队列

![cchhuu](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301282010039.png)

数据库连接池种类

1. JDBC 的数据库连接池使用 javax.sql.DataSource 来表示，<font color="yellow">DataSource</font> 只是一个 <font color="yellow">接口</font>，该接口由第三方提供实现【提供 .jar包】
2. <font color="yellow">CP30</font> 数据库连接池，速度相对较慢，稳定性不错（hihernate, spring）
3. DBCP数据库连接池，速度相对 cp30 较快，但不稳定
4. Proxool 数据库连接池，有监控连接池状态的功能，稳定性较 c3p0 差一点
5. BoneCP 数据库连接池，速度快
6. <font color="yellow">Druid</font>（德鲁伊）是阿里提供的数据库连接池，集 DBCP，C3P0，Proxool，优点于一身的数据库连接池

![image-20230129110153369](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301291101472.png)

### 7.3 C3P0 应用实例

![image-20230129110916753](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301291109855.png)

```java
//  方式1：相关参数，在程序中指定 user，url，password 等
@Test
public void testC3P0_01() throws Exception {
    //  1. 创建一个数据源对象
    ComboPooledDataSource comboPooledDataSource = new ComboPooledDataSource();
    //  2. 通过配置文件 mysql.properties 获取相关连接信息
    Properties properties = new Properties();
    properties.load(new FileInputStream("src/mysql.properties"));
    //  读取相关属性
    String user = properties.getProperty("user");
    String password = properties.getProperty("password");
    String url = properties.getProperty("url");
    String driver = properties.getProperty("driver");
    //  3. 给数据源（连接池） comboPooledDataSource 设置相关的参数
    //  注意：连接管理是由 comboPooledDataSource 来管理
    comboPooledDataSource.setDriverClass(driver);
    comboPooledDataSource.setJdbcUrl(url);
    comboPooledDataSource.setUser(user);
    comboPooledDataSource.setPassword(password);

    //  4. 设置初始化连接数
    comboPooledDataSource.setInitialPoolSize(10);
    //  5. 最大连接数
    comboPooledDataSource.setMaxPoolSize(50);
    //  测试连接池的效率，测试对 mysql 5000 次操作
    long start = System.currentTimeMillis();
    for (int i = 0; i < 5000; i++) {
        Connection connection = comboPooledDataSource.getConnection();//  这个方法就是从 DataSource 接口实现的
        connection.close();
    }
    long end = System.currentTimeMillis();
    System.out.println("c3p0 5000次 耗时：" + (end - start));
}
```

![image-20230129112702071](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301291127155.png)

```xml
<c3p0-config>
	<!-- 数据源名称代表连接池 -->
  <named-config name="hello"> 
<!-- 驱动类 -->
  <property name="driverClass">com.mysql.jdbc.Driver</property>
  <!-- url-->
  	<property name="jdbcUrl">jdbc:mysql://127.0.0.1:3306/girls</property>
  <!-- 用户名 -->
  		<property name="user">root</property>
  		<!-- 密码 -->
  	<property name="password">root</property>
  	<!-- 每次增长的连接数-->
    <property name="acquireIncrement">5</property>
    <!-- 初始的连接数 -->
    <property name="initialPoolSize">10</property>
    <!-- 最小连接数 -->
    <property name="minPoolSize">5</property>
   <!-- 最大连接数 -->
    <property name="maxPoolSize">10</property>

	<!-- 可连接的最多的命令对象数 -->
    <property name="maxStatements">5</property> 
    
    <!-- 每个连接对象可连接的最多的命令对象数 -->
    <property name="maxStatementsPerConnection">2</property>
  </named-config>
</c3p0-config>
```

```java
//  第二种方式 使用配置文件模板来完成
//  1. 将 c3p0 提供的 c3p0.config.xml 拷贝到 src 目录下
//  2. 该文件指定了连接数据库和连接池的相关参数
@Test
public void testC3P0_02() throws SQLException {
    ComboPooledDataSource ac_lq = new ComboPooledDataSource("ac_lq");
    //  测试连接池的效率，测试对 mysql 5000 次操作
    long start = System.currentTimeMillis();
    for (int i = 0; i < 5000; i++) {
        Connection connection = ac_lq.getConnection();
        connection.close();
    }
    long end = System.currentTimeMillis();
    System.out.println("c3p0 第二种方式 5000次 耗时：" + (end - start));
}
```

### 7.4 Druid（德鲁伊） 应用实例

```properties
#key=value
driverClassName=com.mysql.jdbc.Driver
url=jdbc:mysql://localhost:3306/girls?rewriteBatchedStatements=true
#url=jdbc:mysql://localhost:3306/girls
username=root
password=root
#initial connection Size
initialSize=10
#min idle connecton size
minIdle=5
#max active connection size
maxActive=20
#max wait time (5000 mil seconds)
maxWait=5000
```

```java
public class druid_ {
    @Test
    public void testDruid() throws Exception {
        //  1. 加入 Druid jar 包
        //  2. 加入 配置文件，将该文件拷贝到项目的 src 目录
        //  3. 创建 Properties 对象，读取配置文件
        Properties properties = new Properties();
        properties.load(new FileInputStream("src/druid.properties"));
        //  4. 创建一个指定参数的数据库连接池，Druid 连接池
        DataSource dataSource = DruidDataSourceFactory.createDataSource(properties);
        long start = System.currentTimeMillis();
        for (int i = 0; i < 5000; i++) {
            Connection connection = dataSource.getConnection();
            connection.close();
        }
        long end = System.currentTimeMillis();
        System.out.println("druid 连接池 耗时：" + (end - start));
    }
}
```

### 7.5 Druid 德鲁伊 工具类

![jjiijj](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301291521730.png)

```java
/**
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
```

```java
public void testDML(){  //  insert、update、delete
    System.out.println("使用德鲁伊方式完成！！！");
    //  1. 得到连接
    Connection connection = null;
    //  2. 组织一个 sql
    String sql = "update actor set name = ? where id = ?";
    //  3. 创建 PreparedStatement 对象
    PreparedStatement preparedStatement = null; //  扩大作用域！！！
    try {
        connection = JDBCUtilsByDruid.getConnection();
        System.out.println(connection.getClass() );
        preparedStatement = connection.prepareStatement(sql);
        //  给占位符赋值
        preparedStatement.setString(1, "周星驰");
        preparedStatement.setInt(2, 4);
        //  执行
        preparedStatement.executeUpdate();
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        //  关闭资源
        JDBCUtilsByDruid.close(null, preparedStatement, connection);
    }
}
```

![image-20230129153014730](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301291530832.png)

再来看下普通 JDBC 的 Connection

![image-20230129153146583](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301291531676.png)

## 8.  Apache-DBUtils

### 8.1 问题分析

1. 关闭 Connection 后，resultSet 结果集无法使用了
2. resultSet 不利于数据的管理
3. 示意图

![image-20230129154235356](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301291542485.png)

解决方式：

![image-20230129154437669](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301291544813.png)

![image-20230129154728734](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301291547892.png)

![image-20230129154807383](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301291548520.png)

### 8.2 土方法解决

```java
/**
 * Actor 对象和 actor 表的记录对应
 */
public class Actor {    //  Javabean、POJO、Domain 对象
    private Integer id;
    private String name;
    private String sex;
    private Date bornDate;
    private String phone;

    //  一定要给一个无参构造器 【反射需要】
    public Actor() {
    }

    public Actor(Integer id, String name, String sex, Date bornDate, String phone) {
        this.id = id;
        this.name = name;
        this.sex = sex;
        this.bornDate = bornDate;
        this.phone = phone;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Date getBornDate() {
        return bornDate;
    }

    public void setBornDate(Date bornDate) {
        this.bornDate = bornDate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "\nActor{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", bornDate=" + bornDate +
                ", phone='" + phone + '\'' +
                '}';
    }
}
```

注意：如果 JUnit 有具体的返回值，那么就不会输出东西了！！！

即：JUnit 的返回方式必须为：void

```java
//  使用土方法来解决 ResultSet 封装到 ArrayList
@Test
public ArrayList testSelectToArrayList(){  //  insert、update、delete
    //  1. 得到连接
    Connection connection = null;
    //  2. 组织一个 sql
    String sql = "select * from actor where id >=?";
    //  3. 创建 PreparedStatement 对象
    PreparedStatement preparedStatement = null; //  扩大作用域！！！
    ResultSet set = null;
    ArrayList<Actor> list = new ArrayList<>();
    try {
        connection = JDBCUtilsByDruid.getConnection();
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, 2); //  给 ？ 赋值
        //  执行，得到结果集
        set = preparedStatement.executeQuery();
        //  遍历结果集
        while (set.next()){
            //  注意 get 后面接的是 返回类型！！！
            int id = set.getInt(1);   //  获取该行的第一列
            String name = set.getString(2); //  获取该行的第2列
            String sex = set.getString(3); //  获取该行的第3列
            Date date = set.getDate(4);
            String phone = set.getString(5);
            //  将得到的 resultSet 的记录封装到 Actor 对象，放入到 list 集合
            Actor actor = new Actor(id, name, sex, date, phone);
            list.add(actor);
        }
        System.out.println("list 集合数据：" + list);
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        //  关闭资源
        JDBCUtilsByDruid.close(set, preparedStatement, connection);
    }
    //  因为 ArrayList 和 Connection 没有任何关联，所以这个集合可以被复用
    return list;
}
```

### 8.3 APDBUtils 工具类

#### 1. 基本介绍

1. commons-dbutils 是 Apache 组织提供的一个开源 JDBC 工具类库，它是堆 JDBC 的封装，使用 dbutils 能极大简化 jdbc 编码的工作量
2. QueryRunner类：封装 SQL 执行，线程安全，可以实现增、删、改、查、批处理
3. 使用 QueryRunner 类实现查询
4. ResultSetHandler接口：该接口用于处理 java.sql.ResultSet，将数据按照要求转换为另一种形式

| Handler            | 作用                                                         |
| ------------------ | ------------------------------------------------------------ |
| ArrayHandler       | 把结果集中的第一行数据转成对象数组                           |
| ArrayListHandler   | 把结果集中的每一行数据都转成一个数组，再存放到List中         |
| BeanHandler        | 将结果集中的第一行数据封装到一个对应的JavaBean实例中         |
| BeanListHandler    | 将结果集中的 <font color="yellow">每一行数据 </font>都封装到一个对应的JavaBean实例中，存放到List里 |
| ColumnListHandler  | 将结果集中某一列的数据存放到List中                           |
| KeyedHandler(name) | 将结果集中的每行数据都封装到Map里，再把这些map再存到一个map里，其key为指定的key |
| MapHandler         | 将结果集中的第一行数据封装到一个Map里，key是列名，value就是对应的值 |
| MapListHandler     | 将结果集中的每一行数据都封装到一个Map里，然后再存放到List    |

#### 2. 应用实例

使用 DBUtils + 数据连接池（德鲁伊）方式，完成对表 actor 的 CRUD

```java
//  使用 APACHE-DBUtils 工具类 + druid 完成对表的 CRUD操作
@Test
public void testQueryMany() throws SQLException {    //  返回结果是多行的情况
    //  1. 得到连接（druid）
    Connection connection = JDBCUtilsByDruid.getConnection();
    //  2. 使用 DBUtils 类和接口，先引入 DBUtils 相关 jar
    //  3. 创建 QueryRunner
    QueryRunner queryRunner = new QueryRunner();
    //  4. 就可以执行相关的方法，返回 ArrayList 结果集
    String sql = "select * from actor where id >=?";
    //  解读：
    //  1) query 方法就是执行 sql 语句，得到 resultSet --封装到--> ArrayList 集合中
    //  2) 然后返回集合
    //  3) connection；连接
    //  4) sql：执行的 sql 语句
    //  5) new BeanListHandler<>(Actor.class)：将 resultSet ---> Actor 对象 ---> 封装到 ArrayList
    //  底层使用：反射机制 去获取 Actor 类的属性，然后进行封装
    //  6）1：就是给sql 语句中的 ? 赋值，可以有多个值，因为是可变参数
    // public <T> T query(Connection conn, String sql, ResultSetHandler<T> rsh, Object... params)
    //  7）底层得到的 resultSet，会在 query方法中 关闭，还会关闭 preparedStatement
    List<Actor> list = queryRunner.query(connection, sql, new BeanListHandler<>(Actor.class), 1);
    System.out.println("输出集合的信息");
    for (Actor actor : list) {
        System.out.println(actor);
    }
    //  释放资源

    JDBCUtilsByDruid.close(null, null, connection);
}
```

#### 3. 源码分析（反射）

```java
List<Actor> list = queryRunner.query(connection, sql, new BeanListHandler<>(Actor.class), 1);
```

```java
public <T> T query(Connection conn, String sql, ResultSetHandler<T> rsh, Object... params) throws SQLException {
    PreparedStatement stmt = null;
    ResultSet rs = null;
    Object result = null;

    try {
        stmt = this.prepareStatement(conn, sql);
        this.fillStatement(stmt, params);	//	给 ? 赋值
        rs = this.wrap(stmt.executeQuery());	//	执行 sql 语句，将结果封装
        result = rsh.handle(rs); //	返回的 resultSet ---> arrayList 应用反射（对传入的 class 对象处理）
    } catch (SQLException var33) {
        this.rethrow(var33, sql, params);
    } finally {
        try {
            this.close(rs);	//	关闭 resultSet
        } finally {
            this.close((Statement)stmt);	//	关闭 preparedStatement 对象
        }
    }

    return result;	//	最后返回结果集
}
```

#### 4. 单行记录

```java
@Test
//  演示 apache-dbUtils + druid 完成 返回的结果是单行记录（单个对象）
public void testQuerySingle() throws SQLException {
    //  1. 得到连接（druid）
    Connection connection = JDBCUtilsByDruid.getConnection();
    //  2. 使用 DBUtils 类和接口，先引入 DBUtils 相关 jar
    //  3. 创建 QueryRunner
    QueryRunner queryRunner = new QueryRunner();
    //  4. 就可以执行相关的方法，返回 单个对象
    String sql = "select * from actor where id = ?";
    //  因为我们返回的单行记录 <---> 单个对象，使用的  Handler 是 BeanHandler
    Actor actor = queryRunner.query(connection, sql, new BeanHandler<>(Actor.class), 4);
    System.out.println(actor);
    //  释放连接
    JDBCUtilsByDruid.close(null, null, connection);
}
```

#### 5. 单行单列【返回的就是对象 Object】

Scalar：标量【单一】

```java
@Test
//  查询结果是单行单列：返回的就是 object
public void testScalar() throws SQLException {
    //  1. 得到连接（druid）
    Connection connection = JDBCUtilsByDruid.getConnection();
    //  2. 使用 DBUtils 类和接口，先引入 DBUtils 相关 jar
    //  3. 创建 QueryRunner
    QueryRunner queryRunner = new QueryRunner();
    //  4. 就可以执行相关的方法，返回 单行单列，返回的就是 Object
    String sql = "select name from actor where id = ?";
    // 解读：因为返回的是一个对象，使用的 handler 就是 ScalarHandler

    Object obj = queryRunner.query(connection, sql, new ScalarHandler(), 4);
    System.out.println(obj);
    //  释放连接
    JDBCUtilsByDruid.close(null, null, connection);
}
```

#### 6. DML 操作

```java
@Test
public void testDML() throws SQLException {
    //  1. 得到连接（druid）
    Connection connection = JDBCUtilsByDruid.getConnection();
    //  2. 使用 DBUtils 类和接口，先引入 DBUtils 相关 jar
    //  3. 创建 QueryRunner
    QueryRunner queryRunner = new QueryRunner();
    //  4. 组织 sql 完成 update、insert、delete
    String sql = "update actor set name  = ? where id = ?";
    // 解读：
    //  1. 执行 DML 操作，queryRunner.update()
    //  2. 返回的值是受影响的行数
    int affectedRow = queryRunner.update(connection, sql, "张三丰", 4);
    System.out.println(affectedRow > 0 ? "执行成功":"没有影响");
    //  释放连接
    JDBCUtilsByDruid.close(null, null, connection);
}
```

#### 7. 表和 JavaBean 的类型映射关系

int、double等在 Java 中都用 <font color="yellow">包装类</font>，因为 Mysql 中的所有类型都可能为 null，而 <font color="Yellow">Java 只有引用类型才有 NULL值</font>

同时 mySql 中的 char 和 varchar 对应着 String

日期：java.utils.Date

![112233](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301301104563.jpg)

## 9.  DAO增删改查 - BasicDao 

### 9.1 问题分析

apache-dbutils + Druid 简化了 JDBC 开发，但还有不足：

1. SQL 语句是固定的，不能通过 <font color="yellow">参数传入</font>，通用性不好，需要进行改进，更方便执行 CRUD
2. 对于 select 操作，如果有返回值，返回类型不能固定，需要使用 <font color="yellow">**泛型**</font>
3. 将来的表很多，业务需求复杂，不能只靠一个 Java 类完成
4. 引出 ===> BasicDAO 画出示意图

![778899](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301301152480.png)

### 9.2 基本说明

1. DAO：data access object 数据访问对象
2. 这样的通用类，称为 BasicDao，是专门与数据库交互的，即完成对数据库（表）的 crud操作
3. 在 BasicDao 基础上，实现一张表 对应一个 Dao，更好地完成功能，比如：Cunstomer表 --> Customer.java类(javabean)-->CustomerDao.java

### 9.3 应用实例

完成一个简单的设计

com.llq.dao_

- utils：工具类
- domain：javabean
- dao：存放XxxDAO 和 BasicDAO
- test：写测试类

![image-20230130164727786](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301301647855.png)

#### 1. utils

```java
/**
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
```

#### 2. domain

```java
/**
 * Actor 对象和 actor 表的记录对应
 */
public class Actor {    //  Javabean、POJO、Domain 对象
    private Integer id;
    private String name;
    private String sex;
    private Date bornDate;
    private String phone;

    //  一定要给一个无参构造器 【反射需要】
    public Actor() {
    }

    public Actor(Integer id, String name, String sex, Date bornDate, String phone) {
        this.id = id;
        this.name = name;
        this.sex = sex;
        this.bornDate = bornDate;
        this.phone = phone;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Date getBornDate() {
        return bornDate;
    }

    public void setBornDate(Date bornDate) {
        this.bornDate = bornDate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "\nActor{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", bornDate=" + bornDate +
                ", phone='" + phone + '\'' +
                '}';
    }
}
```

#### 3. DAO

```java
/**
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
```

```java
public class ActorDAO extends BasicDAO<Actor> {
    //  1. 就有 BasicDAO 的所有方法
    //  2. 根据业务需求，可以编写特有的方法
}
```

#### 4. test

```java
public class TestDAO {
    //  测试 ActorDAO 对 actor表的 CRUD操作
    @Test
    public void testActorDAO(){
        ActorDAO actorDAO = new ActorDAO();
        //  1. 查询
        List<Actor> actors = actorDAO.queryMulti("select * from actor where id >=?", Actor.class, 1);
        System.out.println("查询结果");
        for (Actor actor : actors) {
            System.out.println(actor);
        }

        //  2. 单行查询
        Actor actor = actorDAO.querySingle("select * from actor where id = ?", Actor.class, 2);
        System.out.println(actor);

        //  3. 查询单行单列
        Object o = actorDAO.queryScalar("select name from actor where id = ?",  1);
        System.out.println(o);

        //  4. dml 操作（Insert）
        int affectedRows = actorDAO.update("Insert into actor values(null, '张无忌', '男', '2000-11-11','999')");
        System.out.println("受影响的行数：" + affectedRows);
    }
}
```

![2132132](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301301657999.jpg)



## 10. 满汉楼

### 10.1 页面设计

#### 用户登录

![image-20230130173508595](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301301735709.png)

#### 显示餐桌状态

![image-20230130173557782](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301301735891.png)

#### 预定

![image-20230130173622118](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301301736233.png)

#### 显示菜品

![image-20230130173701397](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301301737519.png)

#### 点餐

![image-20230130173727482](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301301737603.png)

#### 查看账单

![image-20230130174410674](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301301744810.png)

#### 结账

![image-20230130174810383](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301301748500.png)

1. 对结账餐桌编号进行校验
   - 餐桌存在
   - 有账单需要结算 
2.  结账：
   - Select sum(money) as sum_money from bill where diningTableId = tableId and state = "未支付"; 【支付金额】
   - update bill set state = "已支付" where diningTableId = tableId;【修改订单状态为已支付】
   - update diningTable set state = "空闲", orderName = '', orderTel = '', where id = tableId;

### 10.2 满汉楼分层设计

![fefasd](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301301756642.jpg)

### 10.3 满汉楼工具类

![image-20230131174955258](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301311749332.png)

### 10.4 满汉楼菜单

### 10.5 满汉楼登录

#### domain --> DAO -->Service --> View

```sql
#用户表
create table employee (
	id int primary key auto_increment, #自增
	empId varchar(50) unique not null default '',#员工号
	pwd char(32) not null default '',#密码md5
	name varchar(50) not null default '',#姓名
	job varchar(50) not null default '' #岗位
)charset=utf8; 

insert into employee values(null, '6668612', md5('123456'), '张三丰', '经理');
insert into employee values(null, '6668622', md5('123456'),'小龙女', '服务员');
insert into employee values(null, '6668633', md5('123456'), '张无忌', '收银员');
insert into employee values(null, '666', md5('123456'), '老韩', '经理');
```

```java
/**
 * domain
 * 这是一个 JavaBean，与 employee 对应
 */
public class Employee {
    private Integer id;
    private String empId;
    private String pwd;
    private String name;
    private String job;

    //  无参构造器，底层 apache-dbutils 反射需要
    public Employee() {
    }

    public Employee(Integer id, String empId, String pwd, String name, String job) {
        this.id = id;
        this.empId = empId;
        this.pwd = pwd;
        this.name = name;
        this.job = job;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }
}
```

```java
//	DAO
public class EmployeeDAO extends BasicDAO<Employee> {
    //  这里还可以写特有的操作
}
```

```java
/**
 * service
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
```

### 10.6 餐桌状态

```sql
#餐桌表
create table diningTable (
	id int primary key auto_increment, #自增, 表示餐桌编号
	state varchar(20) not null default '',#餐桌的状态
	orderName varchar(50) not null default '',#预订人的名字
	orderTel varchar(20) not null default ''
)charset=utf8; 

insert into diningTable values(null, '空','','');
insert into diningTable values(null, '空','','');
insert into diningTable values(null, '空','','');
```

```java
package com.llq.mhl.domain;
/**
 * 这是一个 javaBean，和 diningTable 表对应
 */
public class DiningTable {
    private Integer id;
    private String state;
    private String orderName;
    private String orderTel;

    public DiningTable() {
    }

    public DiningTable(Integer id, String state, String orderName, String orderTel) {
        this.id = id;
        this.state = state;
        this.orderName = orderName;
        this.orderTel = orderTel;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getOrderTel() {
        return orderTel;
    }

    public void setOrderTel(String orderTel) {
        this.orderTel = orderTel;
    }

    @Override
    public String toString() {
        return id +"\t\t\t" + state;
    }
}
```

```java
package com.llq.mhl.dao;

public class DiningTableDAO extends BasicDAO<DiningTable> {
    //  如果有特别的操作，可以写在 DiningTableDAO
}
```

```java
package com.llq.mhl.service;

public class DiningTableService {   //  业务层
    private DiningTableDAO diningTableDAO = new DiningTableDAO();

    //  返回所有餐桌信息
    public List<DiningTable> list(){
        //  因为没有问号，所以就不需要带参数了
        List<DiningTable> diningTables = diningTableDAO.queryMulti("select id, state from diningTable", DiningTable.class);
        return diningTables;
    }
}
```

```java
//  显示所有餐桌状态
public void listDiningTable(){
    List<DiningTable> list = diningTableService.list();
    System.out.println("\n餐桌编号\t\t餐桌状态");
    for (DiningTable diningTable : list) {
        System.out.println(diningTable);
    }
    System.out.println("===============显示完毕===============");
}
```

### 10.7 预定餐桌

1. 检查餐桌是否存在
2. 餐桌状态为空 

在 DiningTableService 中新增方法即可

```java
//  根据 id，查询对应餐桌 DiningTable 对应
//  如果返回 null，表明 id 对应餐桌不存在
public DiningTable getDinDiningTable(int id){
    //  小技巧，将 sql 语句放在查询分析器去测试一下
    DiningTable diningTable = diningTableDAO.querySingle("select * from diningTable where id = ?", DiningTable.class, id);
    return diningTable;
}

//  如果餐桌可以预定，调用方法，对其状态进行更新【包括：预定人的名字和电话】
public boolean orderDiningTable(int id, String orderName, String orderTel){
    int affectedRows = diningTableDAO.update("update diningTable set state = '已经预定', orderName = ?, orderTel = ? where id = ?",  orderName, orderTel, id);
    return affectedRows > 0;
}
```

在 View 中新建方法逻辑：

```java
//  预定餐桌
public void OrderTable(){
    System.out.println("===============预定餐桌===============");
    System.out.println("请选择要预定的餐桌编号（-1退出）");
    int orderId = Utility.readInt();
    if (orderId == -1){
        System.out.println("===============取消预定餐桌===============");
        return;
    }
    //  该方法得到的是 Y 或 N
    char key = Utility.readConfirmSelection();
    if (key == 'Y'){
        //  根据 orderId 返回对应的 DiningTable 对象，如果为 null，说明对象不存在
        DiningTable dinDiningTable = diningTableService.getDinDiningTable(orderId);
        if (dinDiningTable!=null){	//	对象不为 null
            if ("空".equals(dinDiningTable.getState())) {

                //  接收预定信息
                System.out.println("预定人名字：");
                String orderName = Utility.readString(50);
                System.out.println("预定人电话：");
                String orderTel = Utility.readString(50);
                if (diningTableService.orderDiningTable(orderId, orderName, orderTel)){
                    System.out.println("===============预定成功===============");
                }else {
                    System.out.println("===============位置错误===============");
                }
            }else {
                System.out.println("===============该餐桌已被预定或就餐中===============");
            }
        }else {
            System.out.println("===============预定的餐桌不存在===============");
        }

    }else {
        System.out.println("===============取消预定餐桌===============");
    }
}
```

### 10.8 显示菜品

```sql
package com.llq.mhl.domain;
/**
 * javaBean 和 menu 表对应
 */
public class Menu {
    private Integer id;
    private String name;
    private String type;
    private Double price;

    public Menu() {
    }

    public Menu(Integer id, String name, String type, Double price) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.price = price;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return id + "\t\t\t" + name + "\t\t" + type + "\t\t" + price;
    }
}
```

```java
package com.llq.mhl.dao;

public class MenuDAO extends BasicDAO<Menu> {

}
```

```java
package com.llq.mhl.service;

/**
 * 完成对 menu 表的各种操作（通过调用 MenuDAO）
 */
public class MenuService {
    //  定义 MenuDAO 属性
    private MenuDAO menuDAO = new MenuDAO();

    //  返回所有的菜品，返回给界面使用
    public List<Menu> list(){
        return menuDAO.queryMulti("select * from menu", Menu.class);
    }
}
```

```java
//	View
//  展示所有菜品
public void listMenu(){
    List<Menu> list = menuService.list();
    System.out.println("\n菜品编号\t\t菜名名\t\t类别\t\t价格");

    for (Menu menu : list) {
        System.out.println(menu);
    }
    System.out.println();
    System.out.println("===============菜品显示完毕===============");
}
```

### 10.9 点餐【涉及多张表】

要求对：

- 餐桌号
- 菜品编号

做出合理性校验，如果不合理，给出提示信息

1. 点餐成功，餐桌状态会发生变化
2. 生成账单 ---> 账单表
   - 现金
   - 支付宝
   - 未支付
   - 已支付
   - 挂单
   - 霸王餐

```sql
#账单流水, 考虑可以分开结账, 并考虑将来分别统计各个不同菜品的销售情况
create table bill (
	id int primary key auto_increment, #自增主键
	billId varchar(50) not null default '',#账单号可以按照自己规则生成 UUID
	menuId int not null default 0,#菜品的编号, 也可以使用外键
	nums SMALLINT not null default 0,#份数
	money double not null default 0, #金额
	diningTableId int not null default 0, #餐桌
	billDate datetime not null ,#订单日期
	state varchar(50) not null default '' # 状态 '未结账' , '已经结账', '挂单'
)charset=utf8;

insert into menu values(null,?,?,?,0,?,now(),'未结账')
drop table bill;
```

```sql
# mysql 返回系统时间
SELECT NOW() FROM DUAL;
```

![image-20230201205152003](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202302012051095.png)

<font color="yellow">Service</font> 之间可以 <font color="yellow">**相互协作** </font>来完成相关业务！！！

![image-20230201212839649](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202302012128776.png)

```java
package com.llq.mhl.service;

/**
 * 处理和订单相关的与业务逻辑
 */
public class BillService {
    //  定义 BillDAO 属性
    private BillDAO billDAO = new BillDAO();
    private MenuService menuService = new MenuService();	//	【互相协作】
    private DiningTableService diningTableService = new DiningTableService();  //【互相协作】
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
}
```

```java
//	MenuService 中 新增方法！！!
//  根据 menuId 返回 Menu 对象

public Menu getMenuById(int menuId) {
    return menuDAO.querySingle("select * from menu where menuId = ?", Menu.class, menuId);
}
```

```java
//	 DiningTableService 中新增方法！！！
//  需要一个提供更新 餐桌状态的方法
public boolean updateDiningTableStatus(int id, String state) {
    int affectedRows = diningTableDAO.update("update diningTable status = ? where id = ?", state, id);
    return affectedRows > 0;
}
```

View 进行对 餐桌号、菜品编号 校验！！！

```java
//  点菜
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
```

### 10.10  查看账单

```java
//	BillService中添加方法
//  返回所有账单
public List<Bill> getBillList(){
    return billDAO.queryMulti("select * from bill ", Bill.class);
}
```

```java
//	View 中添加方法
//  查看账单
public void getOrderBillList(){
    System.out.println("===============查看账单===============");
    System.out.println("\n编号\t\t菜名号\t\t菜品量\t\t金额\t\t桌号\t\t日期\t\t\t\t\t\t\t状态");
    List<Bill> list = billService.getBillList();
    for (Bill bill : list) {
        System.out.println(bill);
    }
    System.out.println("===============显示完毕===============");
}
```

### 10.11 结账

```java
//	BillService
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
```

```java
//	View
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
```

### 10.12 多表处理

![image-20230202164101536](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202302021641744.png)

#### 方案1

新建一个 MultiTableBean 类 ---> MultiTableBeanDAO

```java
package com.llq.mhl.domain;

/**
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

    public MultiTableBean() {
    }

    public MultiTableBean(Integer id, String billId, Integer menuId, Integer nums, Double money, Integer diningTableId, Date billDate, String state, String name) {
        this.id = id;
        this.billId = billId;
        this.menuId = menuId;
        this.nums = nums;
        this.money = money;
        this.diningTableId = diningTableId;
        this.billDate = billDate;
        this.state = state;
        this.name = name;
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
                "\t\t\t" + state;
    }
}
```

```java
package com.llq.mhl.dao;

public class MultiTableDAO extends BasicDAO<MultiTableBean> {

}
```

```java
//	View
//  查看账单
public void getOrderBillList(){
    System.out.println("===============查看账单===============");
    System.out.println("\n编号\t\t菜名号\t\t菜品量\t\t金额\t\t桌号\t\t日期\t\t\t\t\t\t\t状态\t\t菜品名");
    List<MultiTableBean> billListPlus = billService.getBillListPlus();
    for (MultiTableBean listPlus : billListPlus) {
        System.out.println(listPlus);
    }
    System.out.println("===============显示完毕===============");
}
```

![image-20230202173612089](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202302021736255.png)

多表查询时，列名可能存在一样的情况！！！

解决方式：

![image-20230202174221057](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202302021742213.png)

#### 方案2

https://blog.csdn.net/m1913843179/article/details/103170360

### 10.13 扩展功能

![image-20230202174700248](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202302021747587.png)

![image-20230202174811587](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202302021748754.png)

![image-20230202174843682](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202302021748875.png)

![image-20230202174857611](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202302021748997.png)
