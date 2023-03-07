## Mysql

报错原因：在 <font color="yellow">**SQL_mode**</font> 中开启了 <font color="yellow">only_full_group_by</font> 模式。

only_full_group_by的作用：使用这个就是使用和oracle一样的group 规则, select的列都要在group中,或者本身是聚合列(SUM,AVG,MAX,MIN) 才行

查看 SQL_mode

```sql
select @@global.sql_mode
```

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301102350408.png)

```bash
ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION
```

将 SQL_mode 中的only_full_group_by 删除。【重启后会失效】

```sql
set @@global.sql_mode ='STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';
```

本质解决方法：修改 my.ini，重启服务

```sql
sql_mode = STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION
```

```bash
[client]
port=3306
default-character-set=utf8
[mysqld]
# 设为自己Mysql 的安装目录
basedir=D:\Mysql\mysql-5.7.24-winx64
# 设置为Mysql的数据目录
datadir=D:\Mysql\mysql-5.7.24-winx64\data
port=3306
character_set_server=utf8
#跳过安全检查
#skip-grant-tables
 
# SQL_mode 关闭 only_full_group_by 模式
sql_mode=STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION
```

常用函数

```sql
#	IFNULL(expr1,expr2) 如果 expr1 不为空，则返回expr1，否则返回expr2
SELECT IFNULL('apple', 'banana')
```

如果 select 语句同时包含有 group by，having，limit，order by

那么他们的 <font color="yellow">**顺序** </font>是： where（限制属性） group by（分组），having（筛选），order by（排序），limit（分页【限制记录条数】）

查询表是否有索引：

```sql
SHOW INDEX FROM t25;
```

隔离级别：

查看 mysql 隔离级别：

- mysql 5.7

  ```sql
  SELECT @@tx_isolation;
  ```

  ![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301141022601.png)

- mysql 8.0 中

  ```sql
  SELECT @traction_isolation;
  ```

查看全局隔离级别 / 当前会话中的隔离级别

```sql
SELECT @@global.transaction_isolation;
 
SELECT @@session.transaction_isolation;
```

设置隔离级别

```sql
SET SESSION TRANSACTION ISOLATION LEVEL READ UNCOMMITTED;

SET SESSION TRANSACTION ISOLATION LEVEL READ COMMITTED;
```

## 1. Mysql 三层结构

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301102023243.png)

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301102024159.png)

### 1.1 DBMS（Database Manage System）

所谓安装Mysql 数据库，就是在主机安装一个数据库管理系统（DBMS），可以管理多个数据库

### 1.2 一个数据库可以创建多个表，以保存数据（信息）

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301102024824.png)

### 1.3 DBMS、数据库、表

- Mysql 数据库-表本质仍然是文件，只是在不同的存储引擎里面，它的数据可能会存放到内存里面去，比如memory数据库，他的数据会映射到一个内存里面去
- 还有 <font color="yellow">磁盘和内存相结合 </font>的：<font color="yellow">redis</font>
- 不管是什么数据库，最终都要落实到文件中去，才能实现 <font color="red">**持久化**</font>，即：不可能完全放在内存里面的，否则一开机就没了

## 2. 数据在数据库中得存储方式

表的一行称为一条记录 ---> 在 java 程序中，一行记录往往使用对象表示

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301102026014.png)

## 3. SQL 语句分类

- DDL（Define）数据定义语句
  - 【create 表，库...】
- DML（Manipulation）数据操纵语句
  - 增加 insert，修改 Update，删除 delete
- DQL（Query）数据查询语句
  - select
- DCL（Control）数据控制语句
  - 管理数据库：比如用户权限 grant，撤回权限 revoke

## 4. Java操作 Mysql

JDBC

## 5. 数据库操作

### 创建（CREATE）

-  【IF NOT EXISTS】：避免报错
- CHARACTER SET：数据库采用的字符集，默认 utf8
- COLLATE：数据库字符集***\*校对\****规则（utf8_bin：区分大小写），（***\*utf8_general_ci\****：不区分大小写）

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301102344857.png)

新建和关闭 查询

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301102346498.png)

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301102355005.png)

 用于查询的 sql 可以保存：（具体保存位置如下）

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301102356943.png)

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301102357989.png)

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301102358646.png)

 utf8_bin：是区分大小写的，所以只能查到一条记录：

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301102358897.png)

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301102359828.png)

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301102359454.png)

 因为不区分大小写，所以可以查到全部的 2 条语句

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301110001765.png)

DDL中可以看到 完整的建表 sql 语句！！！

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301110002212.png)

### 删除（Drop）

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301110002151.png)

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301110004156.png)

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301110005555.png)

### 备份与恢复

#### 1. 备份数据库【DOS中进行】

```bash
mysqldump -u 用户名 -p -B 数据库1 数据库2 数据库n > 文件名.sql
```

 输入 mysqldump --help 查看相关参数定义

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301110008282.png)

#### 2. 恢复数据库【Mysql 命令行中执行】

```sql
Source 文件名.sql
```

使用 db.sql 实现对 db02 和 db03 的备份，并恢复

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301110011445.png)

```sql
-- MySQL dump 10.13  Distrib 5.7.24, for Win64 (x86_64)
--
-- Host: localhost    Database: db02
-- ------------------------------------------------------
-- Server version	5.7.24
 
/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
 
--
-- Current Database: `db02`
--
 
CREATE DATABASE /*!32312 IF NOT EXISTS*/ `db02` /*!40100 DEFAULT CHARACTER SET utf8 */;
 
USE `db02`;
 
--
-- Table structure for table `t1`
--
 
DROP TABLE IF EXISTS `t1`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t1` (
  `id` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
 
--
-- Dumping data for table `t1`
--
 
LOCK TABLES `t1` WRITE;
/*!40000 ALTER TABLE `t1` DISABLE KEYS */;
INSERT INTO `t1` VALUES (1,'tom'),(2,'Tom');
/*!40000 ALTER TABLE `t1` ENABLE KEYS */;
UNLOCK TABLES;
 
--
-- Current Database: `db03`
--
 
CREATE DATABASE /*!32312 IF NOT EXISTS*/ `db03` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_bin */;
 
USE `db03`;
 
--
-- Table structure for table `t1`
--
 
DROP TABLE IF EXISTS `t1`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t1` (
  `id` int(11) DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;
 
--
-- Dumping data for table `t1`
--
 
LOCK TABLES `t1` WRITE;
/*!40000 ALTER TABLE `t1` DISABLE KEYS */;
INSERT INTO `t1` VALUES (1,'tom'),(2,'Tom');
/*!40000 ALTER TABLE `t1` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;
 
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
 
-- Dump completed on 2022-09-22 10:24:08
```

这个备份的文件，就是对应的 sql 语句

=====================================================================================================

恢复数据库，为了演示，先把后面2个数据库给删掉！！！

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301110013808.png)

#### 3.  备份恢复表

show tables：查询所有表名

```sql
mysqldump -u 用户名 -p 数据库 表1 表2 表n > F:/文件名.sql
```

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301110015564.png)

```sql
-- MySQL dump 10.13  Distrib 5.7.24, for Win64 (x86_64)
--
-- Host: localhost    Database: db02
-- ------------------------------------------------------
-- Server version	5.7.24
 
/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
 
--
-- Table structure for table `t1`
--
 
DROP TABLE IF EXISTS `t1`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t1` (
  `id` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
 
--
-- Dumping data for table `t1`
--
 
LOCK TABLES `t1` WRITE;
/*!40000 ALTER TABLE `t1` DISABLE KEYS */;
INSERT INTO `t1` VALUES (1,'tom'),(2,'Tom');
/*!40000 ALTER TABLE `t1` ENABLE KEYS */;
UNLOCK TABLES;
 
--
-- Table structure for table `t2`
--
 
DROP TABLE IF EXISTS `t2`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t2` (
  `sex` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
 
--
-- Dumping data for table `t2`
--
 
LOCK TABLES `t2` WRITE;
/*!40000 ALTER TABLE `t2` DISABLE KEYS */;
/*!40000 ALTER TABLE `t2` ENABLE KEYS */;
UNLOCK TABLES;
 
--
-- Table structure for table `t3`
--
 
DROP TABLE IF EXISTS `t3`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t3` (
  `age` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
 
--
-- Dumping data for table `t3`
--
 
LOCK TABLES `t3` WRITE;
/*!40000 ALTER TABLE `t3` DISABLE KEYS */;
/*!40000 ALTER TABLE `t3` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;
 
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
 
-- Dump completed on 2022-09-22 10:56:39
```

 =====================================================================

下面验证先删除 db02 数据库下全部的表：

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301110016501.png)

#### 安装 Ecshop 数据库

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301110017194.png)

## 6. 常用数据类型（列类型）

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301111111639.png)

### 1. 整型

1. 规范：在能够满足需求的情况下，尽量选择占用空间小的类型

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301111111761.png)

```sql
#	演示整型
#	使用 tinyint 来演示范围 -128 ~ 127 [如果没有符号：0 ~ 255]
#	1）如果没有指定 UNSIGNED ，则 TINYINT 就是有符号的
#	2）如果指定 UNSIGNED，则 TINYINT 就是没有符号的
 
CREATE TABLE t3(
					id TINYINT);
 
INSERT Into t3 VALUES(-128);	#	这是非常简单的添加语句
 
SELECT * FROM t3;
```

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301111118481.png)

### 2. 数值型（bit）的使用

```sql
#	演示bit类型
#	说明
#	1.	bit(m)m 在1 - 64
#	2.	添加数据 范围 按照你给的位数来确定，比如：m = 8，表示一个字节 0 ~ 255
#	3.	显示按照 bit
#	4.	查询时，仍然可以按照数来查询
CREATE TABLE t4(num BIT(8));
INSERT INTO t4 VALUES(1);
INSERT INTO t4 VALUES(255);
 
SELECT * FROM t4;
 
SELECT * FROM t4 WHERE num = 1;
```

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301111120369.png)

### 3. 小数类型（字符串类型）

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301111112418.png)

一般情况下 Double就够用了，但是如果需要的精度非常高的话，使用 Decimal

- M：小数位数（精度）的总数（默认：<font color="Red">10</font>）
- D：小数点（标度）后面的位数（默认是 <font color="red">0</font>）
- 如果D是0，则没有小数点或分属部分，M最大是65，D最大是30。

### 4. 字符串的基本使用(char, varchar) --- 内部填写 字符

- CHAR(size)：固定长度字符串 最大255字符
- VARCHAR(size) 本身这个可以填65535【**字节**】的，

可变长度字符串 最大 <font color="yellow">65532 字节</font> 【utf8 编码最大 21844字符，1 - 3个字节用于 <font color="yellow">记录大小</font>】

```bash
(65535 - 3) / 3 = 21844 (utf8编码下：1个字符 = 3个字节)
(65535 - 3) / 2 = 32766 (gbk编码下：1个字符 = 2个字节)
```

段落注释方式：

shift 选中，然后 Ctrl + / 和 HTML 一致

```sql
#	演示字符串类型使用 char VARCHAR
#	注释的快捷键	Ctrl + /
-- CHAR(size)
-- 固定长度字符串 最大255字符
-- VARCHAR(size)	0 ~ 65535
-- 可变长度字符串 最大65532字节 【utf8编码（1个字符 = 3个字节）最大21844字符，1 - 3个字节用于记录大小】
 
 
CREATE TABLE t09 (
					`name` CHAR(255));
 
CREATE TABLE t10 (
					`name` VARCHAR(21844));
 
```

字符串使用细节

<font color="yellow">char(4) </font>和 <font color="yellow">varchar(4) </font>表示的是 <font color="yellow">**字符**</font>，而不是字节

那么它到底占用多大空间呢？？？要取决于你的***\*编码方式\****（utf8：1个字符3个字节，gbk：1个字符2个字节）

```sql
#	演示字符串使用细节 
#	char(4)和 varchar(4)表示的是字符，而不是字节
#	不区分字符是汉字还是字母
CREATE TABLE t11(
				`name` CHAR(4));
 
INSERT INTO t11 VALUES('abcd');
INSERT INTO t11 VALUES('爱新觉罗');
 
SELECT * FROM t11;
 
CREATE TABLE t12(
				`name` VARCHAR(4));
INSERT INTO t12 VALUES('ACLQ');
 
SELECT * FROM t12;
```

char：固定

varchar：可伸缩的盒子（不会造成空间的浪费），本身还需要占用1 - 3 个字节来记录存放内容长度

```bash
L （varchar）= L（实际数据大小）+ (1 - 3) 字节
```

什么时候用 char 和 varchar？

1. 如果数据是定长，char

   md5密码（32位），邮编，手机号，身份证号码等 char(32)

2. 如果一个字段的长度不确定，我们使用 varchar，比如：留言，文章等

   查询速度：char > varchar

3. 在存放文本时，也可以使用 Text 数据类型，可以将 TEXT列视为 VARCHAR 列

   注意：Text不能有默认值，大小 0 - 2 ^ 16 字节

   如果希望存放更多字符，可以选择 ***\*MEDIUM\****TEXT 0 ~ 2^ 24 或者 ***\*LONG\****TEXT 0 ~ 2^32

mysql中字符串使用 单引号表示；''

```sql
#	如果 varchar 不够用，可以考虑使用 MEDIUMTEXT 或者 LONGTEXT
#	如果像简单点，可以直接使用 text
CREATE TABLE t13(content TEXT, content2 MEDIUMTEXT, content3 LONGTEXT);

SELECT * FROM t13;
 
INSERT INTO t13 VALUES('韩顺平教育', '韩顺平教育100', '韩顺平教育1000~~~');
```

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301111134556.png)

### 5. 二进制数据类型

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301111112060.png)

```sql
#	演示 DECIMAL 类型，float，double使用
 
#	创建表
CREATE TABLE t06(
				num1 FLOAT,
				num2 DOUBLE,
				num3 DECIMAL(30, 20));
 
SELECT * FROM t06;
 
#	添加数据
INSERT INTO t06 VALUES(88.12345678912345, 88.12345678912345, 88.12345678912345);
 
#	DECIMAL 可以存放很大的数
CREATE TABLE t07(
				num DECIMAL(65));
INSERT INTO t07 VALUES(8985646444444444444444444877777771341311215465879874365469578);
 
SELECT * FROM t07;
```

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301111121861.png)

### 6. 日期类型

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301111113808.png)

```sql
#	演示时间相关的类型
#	创建一张表，date, datetime, TIMESTAMP
#	如果我们更新了 t15 表 的某条记录，login_time列会自动地以当前时间进行更新
CREATE TABLE t15(
				birthday DATE,	-- 生日
				job_time DATETIME, -- 记录年月日 时分秒
				login_time TIMESTAMP 
								NOT NULL 
								DEFAULT CURRENT_TIMESTAMP 
								ON UPDATE CURRENT_TIMESTAMP); -- 登录时间，如果希望 login_time 列自动更新，需要配置
 
SELECT * FROM t15;
 
INSERT INTO t15(birthday, job_time) VALUES('2022-11-11','2022-11-11 10:10:10')
```

一行中内容过多，进行美化 sql

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301111135083.png)

 美化 sql 位置

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301111135922.png)

## 6. 表操作

### C

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301111135738.png)

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301111136995.png)

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301111136467.png)

创建表练习：

```sql
CREATE TABLE `emp` (
					id INT,
					`name` VARCHAR(3),
					sex	CHAR(1),
					birthday DATE,
					entry_date DATETIME,
					job VARCHAR(32),
					Salary DOUBLE,
					`resume` TEXT) CHARSET utf8 COLLATE utf8_bin ENGINE INNODB;
 
 
INSERT INTO `emp` 
				VALUES(100, '小妖怪', '男', '2000-11-11', '2010-11-10 11:11:11', '巡山的', 3000, '大王叫我来巡山');
```

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301111137510.png)

### D（ALTER table DROP 表名）

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301111141802.png)

```sql
#	删除 sex列
ALTER TABLE `emp`
				DROP sex;
```

### U（ALTER）

#### 添加列（Add）

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301111417963.png)

```sql
#	员工表增加一个 image 列，varchar类型（要求在 resume后面）
ALTER TABLE `emp`
				ADD `image` VARCHAR(32) NOT NULL DEFAULT ''
				AFTER resume;
 
#	显示表结构，可以查看表的所有列
DESC `emp`;
```

#### 修改列（Modify）--- 修改列长度

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301111419857.png)

```sql
#	修改 job列，使其长度为60
ALTER TABLE `emp`
				MODIFY job VARCHAR(60) NOT NULL DEFAULT '';
```

#### 修改列名（Change）

```sql
#	列名 name 修改为 user_name
ALTER TABLE `employee`
				CHANGE `name` `user_name` VARCHAR(32) NOT NULL DEFAULT '';
```

#### 修改表名（Rename）、字符集

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301111421776.png)

```sql
#	表名改为 employee
RENAME TABLE `emp` to `employee`;
 
#	显示表结构，可以查看表的所有列
DESC `employee`;
 
 
#	修改表的字符集为 utf-8
ALTER table `employee` CHARACTER set utf8;
```

## 7. 数据操作  增删改查（CRUD）create，retrieve，update，delete

#### Insert（Insert into Values）

```sql
CREATE TABLE goods(
					id INT,
					goods_name VARCHAR(10),
					price DOUBLE);
 
INSERT INTO goods (id, goods_name, price)
					VALUES(10, '华为手机', 2000),
								(20, '苹果手机', 3000);
 
SELECT * FROM goods;
```

练习，向 employee表中插入2条信息

```sql
INSERT INTO employee(id, user_name, birthday, entry_date, job, Salary, resume, image)
						VALUES(10, '爱新觉罗LQ', '1994-11-03', '2023-11-11 11:11:11', '高级Java开发', 40000, '史上最强', '1'),
									(11, '小白', '1995-5-1', '2024-11-11 11:11:11', 'wife', 20000, 'Pretty', '2');
```

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301111424938.png)

#### Insert 语句注意事项

1. 插入的数据应与字段的数据类型相同
2. 数据的长度应该在列的规定范围内，例如：不能将一个长度为80的字符串加入到长度为40的列中
3. 在values中列出的数据位置必须与被加入的列的排列位置相对应
4. 字符和日期类型数据应包含在单引号中
5. 列可以插入空值【前提是该字段允许为空】，Insert into tables value(null)
6. nsert into table_name （列名...）values()，()，() 形式添加多条记录
7. 如果是给表中所有字段添加数据，可以不写前面的字段名称
8. 如果某个列 没有指定 not null，那么当添加数据时，没有给定值，则会默认给一个null
9. 如果我们希望指定某个列的默认值，可以在创建表时指定

#### Update（Update Set Where）

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301111428975.png)

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301111428818.png)

```sql
#	将所有员工薪水修改为5000，如果没有带 where 条件，会修改所有的记录，因此要慎重
UPDATE employee	SET Salary = 5000;
```

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301111429566.png)

```sql
#	将小妖怪 薪水修改为 3000
UPDATE employee 
				SET Salary = 3000
				WHERE user_name = '小妖怪';
```

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301111430967.png)

```sql
#	当前工资基础 + 1000
UPDATE employee 
				SET Salary = Salary + 1000
				WHERE user_name = '爱新觉罗LQ';		
```

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301111430773.png)

#### Update 使用细节

1. UPDATE语法可以使用新值更新原有表行中的各列
2. SET子句指示要修改哪些列和要给予哪些值
3. WHERE子句指定应更新哪些行，如果没有 WHERE子句，则更新所有的行（慎用）
4. 如果需要修改多个字段，可以通过 set 字段1 = 值1，字段2 = 值2......

#### Delete（Delete from）

删除表中名称为 '老妖怪' 的记录

```sql
DELETE FROM employee
				WHERE `user_name` = '小妖怪';
```

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301111432490.png)

删除表中所有记录 

```sql
DELETE FROM employee
```

#### Delete 使用细节

1. 如果不使用where子句，将删除表中所有数据

2. Delete语句不能删除某一列的值（可使用update设为 null或者''）

3. 使用 delete 语句***\*仅删除记录\****，不能删除表本身。如果要删除表，使用drop table语句

   ```sql
   drop table 表名；
   ```

#### Select（Select from）

<font color="yellow">DISTINCT</font>：可选，指显示结果时，是否 <font color="yellow">去掉重复 </font>数据

```sql
#****创建新的表(student)********
create table student(
	id int not null default 1,
	name varchar(20) not null default '',
	chinese float not null default 0.0,
	english float not null default 0.0,
	math float not null default 0.0
);
 
insert into student(id,name,chinese,english,math) values(1,'韩顺平',89,78,90);
insert into student(id,name,chinese,english,math) values(2,'张飞',67,98,56);
insert into student(id,name,chinese,english,math) values(3,'宋江',87,78,77);
insert into student(id,name,chinese,english,math) values(4,'关羽',88,98,90);
insert into student(id,name,chinese,english,math) values(5,'赵云',82,84,67);
insert into student(id,name,chinese,english,math) values(6,'欧阳锋',55,85,45);
insert into student(id,name,chinese,english,math) values(7,'黄蓉',75,65,30);
```

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301111435589.png)

1. 查询表中所有学生信息

2. 查询表中所有学生的姓名和对应的英语成绩

   ```sql
   SELECT	`name`, english FROM student;
   ```

3. 过滤表中重复数据 DISTINCT

   ```sql
   SELECT DISTINCT english from student;
   ```

4. 要查询的记录，***\*每个字段\****都相同，才会去重

   ```sql
   select distinct name from student;
   select name from student;
   ```

使用表达式对查询的列进行运算

统计每个学生的总分

```sql
#	统计每个学生的总分
SELECT `name`, (chinese+english+math) FROM student;
```

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301111445979.png)

为所有学生总分 + 10分

```sql
#	在所有学生总分加10分的情况
SELECT `name`, (chinese+english+math+10) FROM student;
```

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301111446802.png)

使用别名表示学生分数（AS）

```sql
#	使用别名表示学生分数
SELECT `name`, (chinese+english+math+10) AS total_score FROM student;
```

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301111446705.png)



## 8. Where 子句中常用的运算符

| 比较运算符               | 说明                               |
| ------------------------ | ---------------------------------- |
| > < <= >= = > !=         | 大于、小于、大于(小于)等于、不等于 |
| BETWEEN...AND...         | 显示在某一区间的值                 |
| IN(set)                  | 显示在in列表中的值，例:in(100,200) |
| LIKE '张pattern' NOTLIKE | 模糊查询 模糊查询                  |
| IS NULL                  | 判断是否为空                       |

1. 查询姓名为赵云的学生成绩

   ```sql
   SELECT *from student	
   					WHERE `name`='赵云';
   ```

2. 查询英语成绩大于90 的同学

   ```sql
   SELECT *from student	
   					WHERE english>90;
   ```

3. 查询总分大于200 的所有同学

   ```sql
   SELECT `name`, (chinese+english+math+10) AS total_score FROM student
   				WHERE (chinese+english+math)>200;
   ```

4. math > 60 and id > 4

   ```sql
   SELECT *from student	
   					WHERE math>60 and id>4;
   ```

5. 英语成绩大于语文成绩

   ```sql
   SELECT *from student	
   					WHERE english>chinese;
   ```

6. 总分大于200，并且math<chinese，姓韩的

   ```sql
    SELECT * from student
   				WHERE (chinese+english+math)>200 AND 
   								math>chinese	AND
   								`name` LIKE '韩%';
   ```

   

| 逻辑运算符 | 说明                                             |
| ---------- | ------------------------------------------------ |
| and        | 多个条件同时成立                                 |
| or         | 多个条件任一成立                                 |
| not        | 不成立，例: where not(salary>100); werCsDN@sKrLq |

使用 <font color="yellow">order by 子句</font>排序查询结果

- asc
- desc
  - 指定排序的列，排序的列既可以是表中的列名，也可以是select语句后指定的列名

```sql
#	演示 ORDER BY 子句
 
#	对数学成绩排序后输出【升序】
SELECT * FROM student
					ORDER BY math ASC;
 
#	对总分按从高到低的顺序输出[降序] --- 使用别名排序
 
SELECT `name`, (chinese+english+math) AS total_score FROM student
					ORDER BY total_score DESC;
				
 
#	对姓李的学生成绩排序输出（升序）
SELECT `name`, (chinese+english+math) AS total_score FROM student
					WHERE `name` LIKE '韩%'
					ORDER BY total_score ASC;
```

## 9. 函数

### 9.1 Count（合计 / 统计）

返回查询的结果有多少行

```sql
select count(*) | count(别名) from table
                        WHERE 
```

```sql
#	统计一个班共有多少学生
SELECT COUNT(*) FROM student;
 
#	统计数学成绩大于90的学生有多少个
SELECT COUNT(*) FROM student
					WHERE math>90;
 
#	统计总分大于250的认数有多少个
SELECT COUNT(*) FROM student
					WHERE (chinese+english+math)>250;
 
#	count(*) 和 count(列)的区别
-- 解释：
-- count(*) 返回满足条件的记录的行数
-- count(列)：统计满足条件的某列有多少个，但是会排除为 null
 
CREATE TABLE t16(
				`name` VARCHAR(20));
 
INSERT INTO t16 VALUES('tom');
INSERT INTO t16 VALUES('jack');
INSERT INTO t16 VALUES('mary');
INSERT INTO t16 VALUES(NULL);
 
SELECT * FROM t16;
 
SELECT COUNT(*) FROM t16;	-- 4
 
SELECT COUNT(`name`) FROM t16;	-- 3
```

### 9.2 SUM

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301111503941.png)

返回满足 where 条件的行的和 --- 一般使用在数值列

```sql
SELECT sum(列名) from tablename
                Where
```

```sql
#	统计一个班级数学总成绩
SELECT SUM(math) from student;
 
#	统计一个班级语文、英语、数学各科的总成绩
SELECT SUM(chinese), SUM(english), SUM(math) from student;
 
SELECT SUM(chinese) AS chinese_total_score, SUM(english) AS english_total_score, SUM(math) AS math_total_score from student;
 
#	统计一个班级语文、英语、数学各科的成绩总和
SELECT SUM(chinese+english+math) from student;
 
#	统计一个班级语文成绩平均分
SELECT (SUM(chinese) / COUNT(chinese)) from student;
```

### 9.3 AVG

```sql
SELECT AVGf(列名) from table
                        WHERE
```

```sql
#	演示 AVG的使用
#	求一个班级数学平均分？
SELECT AVG(math) from student;
 
#	求一个班级总分平均分
SELECT AVG(chinese+math+english) FROM student;
```

### 9.4 MAX / MIN

```sql
SELECT MAX(列名) from table
                        WHERE
```

```sql
#	班级最高分和最低分
SELECT MIN(math+chinese+english) AS Low_score, MAX(math+chinese+english) AS High_score from student;
 
#	能否知道最高分和最低分是谁，可以 ---> 要用到查询
 
#	求出班级数学最高分
SELECT MAX(math), MIN(math) from student;
```

### 9.5 Group by（分组统计）

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301111507379.png)

#### 1. 使用 group by 子句对列进行分组

```sql
SELECT col1，col2，dol3 FROM tablfe
                        GROUP BY col
```

#### 2. 使用having 子句对分组后的结果进行过滤

```sql
SELECT col1，col2，dol3 FROM tablfe
                        GROUP BY col having...
```

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301111509083.png)

- 部门表

  ```sql
  CREATE TABLE dept( /*部门表*/
  deptno MEDIUMINT UNSIGNED NOT NULL DEFAULT 0,
  dname VARCHAR(20) NOT NULL DEFAULT "",
  loc VARCHAR(13) NOT NULL DEFAULT ""
  ) ;
   
  INSERT INTO dept VALUES(10, 'ACCOUNTING', 'NEW YORK'), 
                         (20, 'RESEARCH', 'DALLAS'), 
                         (30, 'SALES', 'CHICAGO'), 
                         (40, 'OPERATIONS', 'BOSTON');
  ```

  ![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301111511453.png)

- 员工表

   mgr：上级，comm：奖金，deptno：部门

  ```sql
  #创建表EMP雇员
  CREATE TABLE emp
  (empno  MEDIUMINT UNSIGNED  NOT NULL  DEFAULT 0, /*编号*/
  ename VARCHAR(20) NOT NULL DEFAULT "", /*名字*/
  job VARCHAR(9) NOT NULL DEFAULT "",/*工作*/
  mgr MEDIUMINT UNSIGNED ,/*上级编号*/
  hiredate DATE NOT NULL,/*入职时间*/
  sal DECIMAL(7,2)  NOT NULL,/*薪水*/
  comm DECIMAL(7,2) ,/*红利*/
  deptno MEDIUMINT UNSIGNED NOT NULL DEFAULT 0 /*部门编号*/
  );
   
   INSERT INTO emp VALUES(7369, 'SMITH', 'CLERK', 7902, '1990-12-17', 800.00,NULL , 20), 
  (7499, 'ALLEN', 'SALESMAN', 7698, '1991-2-20', 1600.00, 300.00, 30),  
  (7521, 'WARD', 'SALESMAN', 7698, '1991-2-22', 1250.00, 500.00, 30),  
  (7566, 'JONES', 'MANAGER', 7839, '1991-4-2', 2975.00,NULL,20),  
  (7654, 'MARTIN', 'SALESMAN', 7698, '1991-9-28',1250.00,1400.00,30),  
  (7698, 'BLAKE','MANAGER', 7839,'1991-5-1', 2850.00,NULL,30),  
  (7782, 'CLARK','MANAGER', 7839, '1991-6-9',2450.00,NULL,10),  
  (7788, 'SCOTT','ANALYST',7566, '1997-4-19',3000.00,NULL,20),  
  (7839, 'KING','PRESIDENT',NULL,'1991-11-17',5000.00,NULL,10),  
  (7844, 'TURNER', 'SALESMAN',7698, '1991-9-8', 1500.00, NULL,30),  
  (7900, 'JAMES','CLERK',7698, '1991-12-3',950.00,NULL,30),  
  (7902, 'FORD', 'ANALYST',7566,'1991-12-3',3000.00, NULL,20),  
  (7934,'MILLER','CLERK',7782,'1992-1-23', 1300.00, NULL,10);
  ```

  ![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301111512709.png)

- 工资表

   losal：该级别最低工资，hisal：该级别最高工资

  ```sql
  #工资级别表
  CREATE TABLE salgrade
  (
  grade MEDIUMINT UNSIGNED NOT NULL DEFAULT 0,
  losal DECIMAL(17,2)  NOT NULL,
  hisal DECIMAL(17,2)  NOT NULL
  );
   
  INSERT INTO salgrade VALUES (1,700,1200);
  INSERT INTO salgrade VALUES (2,1201,1400);
  INSERT INTO salgrade VALUES (3,1401,2000);
  INSERT INTO salgrade VALUES (4,2001,3000);
  INSERT INTO salgrade VALUES (5,3001,9999);
  ```

  ![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301111513152.png)



显示每个部门的 AVG 和 MAX

```sql
#	每个部门的平均工资和最低工资
SELECT AVG(sal), MAX(sal), deptno from emp
							GROUP BY deptno;
```

显示每个部门的每种岗位的 AVG 和 MIN

```sql
#	每个部门的每种岗位的平均工资和最低工资
SELECT AVG(sal), MAX(sal), deptno, job from emp
							GROUP BY deptno, job;
```

平均工资低于 2000 的部门号 和 它的平均工资 （别名）

```sql
# 在 1 的基础上，进行过滤，保留平均工资小于2000的
# 使用别名进行过滤（效率高些），因为上一个方法要进行2次函数计算
SELECT AVG(sal) as avg_sal, deptno from emp
							GROUP BY deptno,
							Having avg_sal < 2000;
```

### 9.6 字符串相关函数

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301111521867.png)

```sql
#	演示字符串相关函数使用，使用 emp 表来演示
 
-- CHARSET(str) 返回字符串字符集
SELECT CHARSET(ename) FROM emp;
 
-- CONCAT(str1,str2,...)连接字符串，将多个列拼接成一列
 SELECT CONCAT(ename, ' job is ', job) FROM emp;
 
-- INSTR(str,substr)返回substring在string中出现的位置，没有返回0 
-- DUAL：亚元表，系统表，可以作为测试表来使用
SELECT INSTR('hanshunping', 'ping') FROM DUAL;
 
 
-- UCASE(str)转成大写
SELECT UCASE(ename) FROM emp;
 
-- LCASE(str)转成小写
SELECT LCASE(ename) FROM emp;
 
-- LEFT(str,len)从string 中左边起取 length 个字符
SELECT LEFT(ename, 2) FROM emp;
 
 
-- LENGTH(str)string长度【按照字节】
SELECT LENGTH(ename) FROM emp;
SELECT LENGTH('爱新觉罗') FROM emp;
 
 
-- REPLACE(str, search_str, replace_str)，替换
-- 如果是 Manager 就替换成 经理
SELECT ename, REPLACE(job, 'MANAGER', '经理') FROM emp;
 
 
-- STRCMP(expr1,expr2)逐字符比较2个字符串大小 [-1, 0, 1]
SELECT STRCMP('hsp', 'hsp') FROM DUAL;
 
-- SUBSTRING(str,pos,len)
-- 从str的position开始【从1开始计算】，取出长度为 Length 的子串
-- 从ename 列的第一个位置开始取出2个字符
SELECT SUBSTRING(ename, 1, 2) FROM emp;
 
 
-- LTRIM(string)，RTRIM(string) trim：去除前端空格或后端空格
SELECT LTRIM('  Kobe Bryant  ') FROM DUAL;
SELECT RTRIM('  Kobe Bryant  ') FROM DUAL;
SELECT TRIM('  Kobe Bryant  ') FROM DUAL;
 
#	练习：以首字母小写显示：所有员工emp 姓名
-- 方法1
-- 先取出ename的第一个字符，转成小写
-- 把它和后面的字符串进行拼接
SELECT CONCAT(LCASE(LEFT(ename, 1)), SUBSTRING(ename, 2)) from emp;
 
SELECT REPLACE(ename, LEFT(ename, 1), LCASE(LEFT(ename, 1))) from emp;
```

### 9.7 日期函数

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301111526150.png)

```sql
-- `CURRENT_DATE`()：当前日期
SELECT CURRENT_DATE() FROM DUAL;
 
-- `CURRENT_TIME`()：当前时间
SELECT CURRENT_TIME() FROM DUAL;
 
-- `CURRENT_TIMESTAMP`()：当前时间戳
SELECT CURRENT_TIMESTAMP FROM DUAL;
 
-- DATE(datetime)：返回datetime的日期部分
 
-- DATE_ADD(date2,INTERVAL expr unit)
 
-- DATE_SUB(date2,INTERVAL expr unit)
 
-- DATEDIFF(expr1,expr2)：2个日期差（天）
 
-- TIMEDIFF(expr1,expr2)：2个时间差（时分秒）
 
-- NOW()：当前时间
SELECT NOW() FROM DUAL;
 
YEAR|MONTH|DATE(datetime) FRROM_UNIXTIME()
 
CREATE TABLE mes(
					id INT,
					content VARCHAR(30),
					send_time DATETIME);
 
-- 添加记录
INSERT INTO mes VALUES
						(1, '北京新闻', NOW()),
						(2, '上海新闻', NOW()),
						(3, '广州新闻', NOW());
 
SELECT * from mes;
 
-- 显示所有新闻信息，发布日期只显示日期，不显示时间
SELECT id, content, DATE(send_time) from mes;
 
-- 查询10分钟内发布的帖子
SELECT * FROM mes
					WHERE DATE_ADD(send_time,INTERVAL 10 MINUTE) >=NOW();
 
SELECT * FROM mes
					WHERE DATE_SUB(NOW(),INTERVAL 10 MINUTE) <=send_time;
 
-- 求出 2011-11-11 和 1990-1-1 相差多少天
SELECT DATEDIFF('2011-11-11 ', '1990-1-1') FROM DUAL;
 
-- 求出你活了多少天
SELECT DATEDIFF(NOW(),'1994-11-03') from dual;
 
-- 如果你能活到80岁，求出你还能活多少天
-- 先求出活到80岁，是什么日期
-- 然后再使用 datediff(x, NOW());
SELECT DATEDIFF(NOW(),'1994-11-03') from dual;
 
SELECT DATE_ADD('1994-11-03',INTERVAL 80 year) from dual;
 
 
#	INTERVAL 80 YEAR：year也可以是 年月日，时分秒
# '1994-11-03' 也可以是 data, datetime, timestamp
SELECT DATEDIFF(DATE_ADD('1994-11-03',INTERVAL 80 year), NOW()) FROM DUAL;
```

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301111528062.png)

```sql
#  年月日 时分秒
'%Y-%m-%d %H:%i:%s'
# 因为mysql函数，字段是不区分大小写的。因为月份month把m占用了。如果分钟再用minute的首字母的话，就无法区分是月还是分钟，所以把代表字母往后面移了一位，用第二个字母代替i代替了分钟。
```

```sql
-- YEAR|MONTH|DAY|	DATE(datetime)
-- 只显示当前年
SELECT YEAR(NOW()) FROM DUAL;
SELECT MONTH(NOW()) FROM DUAL;
SELECT DAY(NOW()) FROM DUAL;
 
 
-- 也可以对具体时间进行操作
SELECT MONTH("2013-11-10") FROM DUAL;
 
-- unix_timestamp()：返回的是 1970-1-1 到现在的秒数 [1665013264]
SELECT UNIX_TIMESTAMP() FROM DUAL;
SELECT UNIX_TIMESTAMP() / (365 * 24 * 3600) FROM DUAL;
 
-- SELECT FROM_UNIXTIME(UNIX_TIMESTAMP(),format) ：可以把一个 UNIX_TIMESTAMP() 秒数，转成指定格式的日期
-- %Y-%m-%d 格式是规定好的，表示年月日
-- 意义：在开发中，可以存放一个整数，然后表示时间，通过 FROM_UNIXTIME 在转换
SELECT FROM_UNIXTIME(1665013264,'%Y-%m-%d') FROM DUAL;
SELECT FROM_UNIXTIME(1665013264,'%Y-%m-%d %H:%i:%s') FROM DUAL;
```

### 9.8 加密函数

MYSQL8 移除了password() 方法

```sql
SELECT * FROM mysql.`user`;
```

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301111531079.png)

事件和触发器权限

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301111533314.png)

权限字符串 ---> 存的就是密码

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301111533083.png)

```sql
#	演示加密函数和系统函数
 
-- USER() 查询用户
-- 可以查看登录到 mysql 的有哪些用户，以及登录的IP
SELECT USER() FROM DUAL; -- 用户@ip
 
-- DATABASE() 查询当前使用数据库名称
SELECT DATABASE() FROM DUAL;
 
-- MD5(str) 为字符串算出一个 MD5 32 的字符串，常用（用户密码）加密
-- 存放密码时，不可能明文存放
-- root 密码是 hsp --> 加密 --> 在数据库中存放的是加密后的密码
 SELECT MD5('hsp') FROM DUAL;
 SELECT LENGTH(MD5('hsp')) FROM DUAL;
 
-- 演示用户表，粗放密码时，是md5
CREATE TABLE users106(
					id INT,
					`name` VARCHAR(32) NOT NULL default '',
					pwd CHAR(32) NOT NULL DEFAULT '');
 
INSERT INTO users106 VALUES
					(1, 'ACLQ', MD5('778899'));
 
SELECT * FROM users106;
 
SELECT * FROM users106
				WHERE `name`='ACLQ' and pwd=MD5('778899')
 
 
SELECT * FROM users106
				WHERE `name`='ACLQ' and pwd='778899';
 
-- PASSWORD(str) -- 加密函数 [*65693B54AE3C6F24627CAE7B5FD75AEA3764DEE8]
SELECT PASSWORD('352420kobe24llq')
 
-- SELECT * FROM mysql.user \G 从原文密码 str 计算并返回密码字符串
-- 通常用于对 mysql数据库的用户密码加密
-- mysql.user 表示数据库.表
SELECT * FROM mysql.`user`;
```

### 9.9 流程控制函数

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301111539982.png)

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301111539367.png)

```sql
#	演示流程控制语句

#	`IF`(expr1,expr2,expr3) 如果expr1 为 TRUE, 返回 expr2，否则返回 expr3
SELECT IF(TRUE, '北京', '上海') FROM DUAL;

#	IFNULL(expr1,expr2) 如果 expr1 不为空，则返回expr1，否则返回expr2
SELECT IFNULL('apple', 'banana')

#	多重分支
SELECT CASE
				WHEN expr1 THEN 'jack' --jack
				WHEN expr2 THEN 'tom'
				ELSE 'mary' END

-- 一、查询 emp 表，如果comm 是 Null,则显示 0.0
-- 判断是否为 null，使用 is null ，判断不为空 使用 is NOT

SELECT ename, IF(comm is NULL, 0.0, comm)
				FROM emp;

SELECT ename, IFNULL(comm, 0.0)
				FROM emp;

-- 汉化
SELECT ename, (SELECT CASE
               WHEN job='CLERK' THEN '职员'
               WHEN job='MANAGER' THEN '经理'
               WHEN job='SALESMAN' THEN '销售人员'
               ELSE job END), job
						from emp;
```



## 10. Mysql 表格查询 --- 加强

emp，dept，salgrade <font color="yellow">三表联查</font>

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301111542187.png)

### 10.1 单表加强

使用 where 子句：查找1992.1.1 后入职的员工

```sql
-- 在 mysql 中，日期类型可以直接比较
SELECT * FROM emp 
			WHERE DATEDIFF(hiredate, '1992.1.1') > 0;
```

如何使用 like 操作符

​    %：0到多个字符，_：表示单个字符

```sql
#	显示没有上级的员工
SELECT * FROM emp
				WHERE mgr is NULL;
```

如何显示首字符为 S 的员工姓名和工资

```sql
#	如何使用 like 操作符（模糊查询）
SELECT ename, sal FROM emp
				WHERE ename LIKE 'S%';
```

如何显示第三个字符为大写O的所有员工的姓名和工资

```sql
SELECT ename, sal from emp		
					WHERE ename LIKE '__O%'
```

如何显示没有上级的雇员的情况

```sql
#	显示没有上级的员工
SELECT * FROM emp
				WHERE mgr is NULL;
```

查询表结构 select

```sql
#	查询表结构
desc emp;
```

ORDER BY

按照工资顺序查询

```sql
SELECT * FROM emp	
					ORDER BY sal ASC;
```

部门号升序，工资降序 【类比 ：linux中，命令管道】

```sql
SELECT * FROM emp	
					ORDER BY deptno ASC, sal DESC;
```

### 10.2 分页查询【limit】

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301121627726.png)

 LIMIT (页数 - 1) * 行数，行数

```sql
#	第一页
SELECT * FROM emp
						ORDER BY empno ASC
						LIMIT 0, 3;
#	第二页
SELECT * FROM emp
						ORDER BY empno ASC
						LIMIT 3, 3;
#	第三页
SELECT * FROM emp
						ORDER BY empno ASC
						LIMIT 6, 3;		
```

### 10.3 分组增强

显示每种岗位的雇员总数、平均工资

```sql
SELECT job, COUNT(*), AVG(sal) FROM emp
											GROUP BY job;
```

显示雇员总数、以及获得补助的雇员数

```sql
#	获得补助的雇员数 就是 comm 列为非 null，就是 count(列)，如果该列的值为 null，是不会统计的
SELECT COUNT(ename), COUNT(comm) from emp;
```

显示管理者的总人数

```sql
#	显示管理者的总人数
SELECT COUNT(DISTINCT mgr) FROM emp;
```

显示雇员工资的最大差额

```sql
SELECT (MAX(sal) - MIN(sal)) AS 'max_gap' FROM emp;
```

### 10.4 多子句查询

如果 select 语句同时包含有 group by，having，limit，order by

那么他们的顺序是：group by，having，order by，limit

【分组 --》排序 --》分页】

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301121633528.png)

```sql
SELECT avg(sal),deptno FROM emp	
					GROUP BY deptno
					HAVING avg(sal)>1000
					ORDER BY avg(sal) DESC
					LIMIT 0, 2;
```

## 11. 多表查询

<font color="yellow">**多表查询的条件** </font>不能少于：（表的个数 - 1）

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301121637803.png)

 多表查询：基于2个或2个以上的表查询

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301121637026.png)

 直接查 2个表的话，会有 52 条记录：（13 * 4）

```sql
SELECT * FROM emp, dept;
```

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301121810165.png)

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301121810199.png)

 默认情况下，当2个表查询时，规则：

1. 从第一张表中，<font color="red">**取出一行**</font> 和 <font color="blue">**第二张表的每一行**</font> 进行组合，返回结果【含有2张表的所有列】

2. 一共返回：第一张表行数 * 第二张表行数 = 13 * 4 = 52

3. 这样多表查询默认处理返回的结果，称为 <font color="yellow">**笛卡尔集**</font>

4. 解决这个多表的关键就是要写出正确的过滤条件 where（程序员分析）

   ```sql
   SELECT * FROM emp, dept
   					WHERE emp.deptno = dept.deptno;
   ```

   ![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301121814211.png)

当我们需要指定显示某个表的列时，需要 <font color="yellow">**表.列名**</font> 【emp.deptno】

```sql
SELECT ename, sal, dname, emp.deptno FROM emp, dept
					WHERE emp.deptno = dept.deptno;
```

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301121816116.png)

=========================================================================================

写 sql 思路：

- 先写一个整体简单的
- 然后加入过滤条件

练习1：

显示部门号为10的部门名，员工名和工资

```sql
SELECT ename, sal, dname, emp.deptno FROM emp, dept
					WHERE emp.deptno = dept.deptno AND emp.deptno = 10;
```

练习2：

显示各个员工的姓名，工资，以及工资的级别（salgrade表）

```sql
select ename, sal, grade from emp, salgrade
								WHERE  salgrade.losal<=emp.sal and emp.sal<=salgrade.hisal;
```

表示区间时，也可以用 Between and

```sql
select ename, sal, grade from emp, salgrade
								WHERE sal BETWEEN losal AND hisal;
```

### 11.1 自连接（alias：别名）

在同一张表的连接查询【将同一张表看作2张表】

例：显示：公司员工和他上级的名字

- 员工的名字 在 emp，上级的名字也 在 emp

- 员工和上级是通过 emp 表的 <font color="yellow">**mgr列**</font> 关联

  ```sql
  #	显示公司员工和他上级的名字
  SELECT * FROM emp worker, emp boss	
  ```

可以给表 取别名，比如：worker，boss

```sql
#	显示公司员工和他上级的名字
SELECT worker.ename AS '员工' , boss.ename AS '领导' FROM emp worker, emp boss
								WHERE worker.mgr = boss.empno;
```

### 11.2 子查询（嵌套查询）

子查询是指嵌入在其它sql 语句中的 select 语句，也叫嵌套查询

#### 1. 单行子查询

只返回一行数据的子查询语句

例子：如何显示 与 SMITH 同一部门的所有员工

```sql
SELECT ename from emp	
		WHERE deptno=(	# 返回的只有一行，所以也叫做单行子查询
            SELECT deptno FROM emp
            WHERE ename='SMITH'
        )
```

#### 2. 多行子查询（in）

返回多行数据的子查询，使用关键字 in

例子：查询和 ***\*部门10的工作\**** 相同的雇员的名字、岗位、工资、部门号、但是不含10自己的

```sql
select job from emp
				WHERE deptno=10
```

 这里返回多行，所以是多行子查询

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301121835965.png)

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301121835397.png)

 注意：查出来的 job 有可能相同，要使用 DISTINCT 关键字来进行限制

```sql
SELECT ename, job, sal, deptno FROM emp
			WHERE job in(
                select DISTINCT job from emp
                WHERE deptno=10
            ) AND deptno!=10
```

#### 3. 子查询临时表 ---> 可以解决很多复杂问题

子查询当做 <font color="yellow">临时表</font> 来使用

（虽然这个表是不存在的，但是我可以把它当做一个表来使用）

例子：查询 ecshop 各个类别中，价格最高的商品

使用 group by，select 后面只能跟 分组字段，和聚合函数

group by是把***\*相同的组\**** 合并在一起，直接加 goods_name 的话怎么合并，它不知道给你保留哪一个名字

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301121839804.png)

 cat_id = 3 的类别有很多，但是只有一个 shop_price 可以对的上 = max(shop_price)

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301121840467.png)

 左侧的临时表如下：

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301121842055.png)

```sql
#	查询ecshop 中各个类别中，价格最高的商品
SELECT cat_id, MAX(shop_price)
				FROM ecs_goods
				GROUP BY cat_id

SELECT goods_id, ecs_goods.cat_id, goods_name, shop_price
			FROM(
                SELECT cat_id, MAX(shop_price) AS max_price
                FROM ecs_goods
                GROUP BY cat_id
            ) temp, ecs_goods
			WHERE temp.cat_id = ecs_goods.cat_id AND
						temp.max_price = ecs_goods.shop_price
```

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301121843762.png)

#### 4. all 和 any

查询：工资比部门30的所有员工WHERE中不能直接用聚合函数 MAX（），但是可以在子查询中使用

```sql
SELECT ename, sal, deptno FROM emp
					WHERE sal > (
                        SELECT MAX(sal) from emp
                        WHERE deptno = 30
                    )
```

 还可以 All：

```sql
SELECT ename, sal, deptno FROM emp
					WHERE sal > ALL(
                        SELECT sal from emp
                        WHERE deptno = 30
                    )
```

使用 any 操作符（MIN）

```sql
SELECT ename, sal, deptno FROM emp
					WHERE sal > (
                        SELECT MIN(sal) from emp
                        WHERE deptno = 30
                    )
```

#### 5. 多列子查询

查询返回多个 <font color="blue">**列数据**</font> 的子查询

例子：查询与 smith 的 ***\*部门\**** 和 ***\*岗位\**** 完全相同的所有雇员（并且不含 SMITH本人）

1. 先查出 SMITH 的 部门和岗位 【看到不止一列，所以是 多列子查询】

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301121928061.png)

```sql
SELECT deptno, job from emps
			WHERE ename='SMITH'
```

2. 找出其他人

   ```sql
   SELECT * FROM emp
   					WHERE (deptno, job) = (
                           SELECT deptno, job from emp
                           WHERE ename='ALLEN') AND ename!='ALLEN'
   ```

   ![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301121928471.png)

#### 6. 子查询练习题

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301121930139.png)

```sql
SELECT * from emp;

SELECT deptno, AVG(sal) from emp
						GROUP BY deptno


SELECT emp.deptno, ename, sal FROM emp, (SELECT deptno, AVG(sal) AS avg_sal from emp
                                         GROUP BY deptno) temp
		WHERE emp.deptno = temp.deptno AND	# 部门相同
					emp.sal > temp.avg_sal	# sal > avg_sal
```

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301121933041.png)

```sql
SELECT emp.deptno, ename, sal FROM emp, (SELECT deptno, MAX(sal) AS max_sal from emp
                                         GROUP BY deptno) temp
		WHERE emp.deptno = temp.deptno AND	#	部门相同
					emp.sal = temp.max_sal	#	sal = max_sal
```

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301121934822.png)

```sql
SELECT dname, emp.deptno, loc, COUNT(ename) from emp, dept
						WHERE emp.deptno = dept.deptno
						GROUP BY emp.deptno
```



## 12. 表复制和去重复

### 1. 自我复制数据（蠕虫复制）

有时，为了对某个sql语句进行效率测试，我们需要海量数据时，可以使用此方法为表创建海量数据

```sql
CREATE TABLE my_table1(
    id INT,
    `name` VARCHAR(32),
    sal DOUBLE,	
    job VARCHAR(32),
    deptno INT);

DESC my_table1

SELECT * FROM my_table1;

#	演示如何自我复制
-- 先把 emp 表的记录 复制到 my_table01
INSERT INTO my_table1
								(id, `name`, sal, job, deptno)
								SELECT empno, ename, sal, job, deptno FROM emp;

#	自我复制
INSERT INTO my_table1
					SELECT * FROM my_table1;


```

### 2. 去重

```sql
-- 思路：
-- 1）先创建一张临时表 my_tmp，该表的结构和 my_table2一样
-- 2）将 my_table2 的记录 通过 DISTINCT 关键字 处理后，把记录复制到 my_tmp
-- 3）清除掉 my_table2 记录
-- 4）把 my_tmp 表的记录 复制到 my_table02
-- 5）drop 掉 临时表 my_tmp
 
SELECT * FROM my_tmp
 
CREATE TABLE my_tmp LIKE my_table2
 
INSERT INTO my_tmp
		SELECT DISTINCT * FROM my_table2;	# 通过 DISTINCT 关键字 处理
 
DELETE FROM my_table2;
 
INSERT INTO my_table2
					SELECT * FROM my_tmp
 
DROP TABLE my_tmp
```

## 13. 合并查询

为了合并多个 Select 语句的结果，可以使用集合操作符号 union，union all

1. union all

   用于取得2个结果集的并集。当使用该操作符时，不会取消重复行

   ```sql
   SELECT ename, sal, job FROM emp 
   					WHERE sal > 2500
   UNION ALL
   SELECT ename, sal, job FROM emp 
   					WHERE job = 'MANAGER'
   ```

2. union

   与union all 类似，但是会自动去掉结果集中重复行

   ```sql
   SELECT ename, sal, job FROM emp 
   					WHERE sal > 2500
   UNION 
   SELECT ename, sal, job FROM emp 
   					WHERE job = 'MANAGER'
   ```

​	

## 14. 外连接

1. 前面学习的查询，是利用 where 子句对2张表或者多张表，形成笛卡尔集进行筛选，根据***\*关联条件\****，显示所有匹配的记录，***\*匹配不上\****的，***\*不显示\****
2. 比如：列出部门名称和这些部门的员工和工作，同时要求显示出那些没有员工的部门
3. 使用我们学过的多表查询的SQL，看看效果如何

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301121945073.png)

因为 emp 表中根本就没有 deptno 这个列，就不会匹配上！！！【emp 表中没有 deptno = 40 的记录】

所以就不会显示

### 1. 左外连接 【左侧完全显示】

![image-20230112195016928](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301121950060.png)

```sql
SELECT * FROM stu;
 
SELECT * FROM exam;
 
CREATE TABLE stu(
				id INT,
				name VARCHAR(32))
 
INSERT INTO stu VALUE
					(1, 'JACK'),
					(2, 'Tom'),
					(3, 'Kity'),
					(4, 'nono')
 
CREATE TABLE exam(
				id INT,
				grade INT)
 
 
INSERT INTO exam VALUES
					(1, 56),
					(2, 76),
					(11, 8)
 
SELECT `name`, stu.id, grade
					FROM stu, exam
					WHERE stu.id = exam.id;
 
-- 改成左外连接
SELECT `name`, stu.id, grade
					FROM stu LEFT JOIN exam
					ON stu.id = exam.id;
```

![image-20230112195140534](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301121951639.png)

### 2. 右外连接【右侧完全显示】

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301121952733.png)

```sql
-- 改成右外连接
SELECT `name`, stu.id, grade
					FROM stu RIGHT JOIN exam
					ON stu.id = exam.id;
```

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301121952478.png)

练习题：

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301121952132.png)

```sql
SELECT * FROM emp;
 
SELECT * FROM dept;
 
 
SELECT emp.deptno, dname, ename, job FROM 
								emp RIGHT JOIN dept
								ON emp.deptno=dept.deptno
 
SELECT emp.deptno, dname, ename, job FROM 
								dept LEFT JOIN emp
								ON emp.deptno=dept.deptno
```

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301121954847.png)

## 15. mysql 约束

约束用于确保数据库的数据满足特定的商业规则

在mysql中，约束包括 not null，unique，primary key, foreign key 和 check 五种

### 1. 主键（primary key）

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301131345785.png)

细节说明：

1. primary key 不能重复，而且 <font color="yellow">**不能为 null**</font>
2. 一张表最多有一个主键，但是可以是复合主键
3. 主键指定方式有2种：
   - 直接在字段名后指定：字段名 primary key
   - 在表定义最后写 primary key(列名)
4. 使用 desc 表名，可以看到 primary key 的情况
5. 在实际开发中，每个表往往都会设计一个主键

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301131350132.png)

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301131351375.png)

主键使用的细节讨论：

- primary key 不能重复而且不能为 null
- 一张表最多只能有一个主键，但可以是***\*复合\****主键【比如：id + name】

```sql
#	演示 复合主键(id 和 name 做成复合主键)
CREATE TABLE t18
					(id INT,
					`name` VARCHAR(32),
					 email VARCHAR(32),
					PRIMARY KEY(id, `name`));
 
INSERT INTO t18
					VALUES(1, 'jack', 'jack@sohu.com'),
								(1, 'tom', 'tom@sohu.com')
					
SELECT * FROM t18
```

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301131353142.png)

### 2. not null（非空）、unique（唯一）

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301131354632.png)

```sql
#	unique 使用
 
CREATE TABLE t22(
			id INT UNIQUE,
			`name` VARCHAR(32),
			email VARCHAR(32));
 
INSERT INTO t22 VALUES
			(1, 'jack', 'jack@souhu.com'),
			(1, 'tom', 'tom@souhu.com')
```

如果一个列（字段），是 unique not null，使用效果类似 primary key

### 3. 外键（foreign key）--- Innodb

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301131356626.png)

 做成外键约束后，就意味着你不能乱来了

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301131356666.png)

同时如果外键的关系已经形成了，这时如果你直接删掉这条记录也会失败

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301131359599.png)

 除非你先把 jack 这条记录删掉，再去班级表删除 对应的 id 记录

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301131401245.png)

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301131402066.png)

```sql
#	创建班级表
CREATE TABLE class(	# 主表
    id INT PRIMARY KEY,
    nam VARCHAR(32),
    `add` VARCHAR(32))

INSERT INTO class VALUES
							(100, 'java01', '北京'),
							(200, 'web2', '上海')

CREATE TABLE student109(	# 从表
    id VARCHAR(32) PRIMARY KEY,
    name VARCHAR(32) NOT NULL DEFAULT '',
    class_id INT,
    -- 					下面指定外键关系
    FOREIGN KEY (class_id) REFERENCES class(id))	# FOREIGN KEY 本表字段名 REFERENCES 主表名（主键名或 unique 字段名）

INSERT INTO student109 VALUES
					('sn_001', 'tom', 100),
					('sn_002', 'jack', 200)

SELECT * FROM class
SELECT * FROM student109

-- 这里会失败，因为 300 班级不存在
INSERT INTO student109 VALUES ('sn_003', 'tom', 300)

-- 可以，因为 外键 没有写 not null【因为 null 没有指向任何地方，就没有违反你的约束】
INSERT INTO student109 VALUES ('sn_003', 'tom', NULL)


#	一旦建立主外键的关系，数据不能随意删除主表的记录了
DELETE FROM class
				WHERE id = 100;
```

细节说明：

1. 外键指向的表的字段，要求是 primary key 或者是 unique
2. 表的类型是 innodb，这样的表才支持外键
3. 外键字段的类型要和主键字段的类型一致（长度可以不同）
4. 外键字段的值，必须在主键字段中出现过，或者为 null【前提是外键字段允许为 null】
5. 一旦建立主外键的关系，数据不能随意删除了

### 4. check【Mysql 8.0 生效】

强制行数据必须满足的条件

```sql
alter user 'root'@'localhost' identified with mysql_native_password by '********';
```

第二部分：授权远程使用

1. 如果直接授权的话，会报错

2. 因为之前修改root账号的秘密时，地址为localhost，所以这里不能直接授权其他主机访问，需要先把root账号的host修改为可以访问所有主机，再去授权。

   ```sql
   user mysql;
    
   update user set host='%' where user='root';
    
   grant all on *.* to 'root'@'%';
   
   flush privileges;
   ```

    查询端口号

   ```sql
   mysql> show global variables like 'port';
   ```

    cat /etc/sysctl.conf

   提前切换 root 用户

   ![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301131422481.png)

    保存生效

   ```sql
    sysctl -p
   ```

   https://blog.csdn.net/a1592326183/article/details/118313880

   ```sql
   #	演示 CHECK 使用
   #	mysql 5.7 目前还不支持 check，只做语法校验，但不会生效
    
   CREATE TABLE t24(
   				id INT PRIMARY KEY,
   				`name` VARCHAR(32),
   				sex VARCHAR(6) CHECK (sex IN('man', 'woman')),
   				sal DOUBLE CHECK (sal > 1000 AND sal < 2000));
    
   INSERT INTO t24 VALUES
   					(1, 'jack', 'mid', 1);
    
   SELECT * FROM t24;
   ```

   在 mysql 5.7中只是进行了 语法的校验，就还是可以插入进去

   ![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301131425794.png)

   可以在 mysql 8.0 中已经生效

   ![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301131426279.png)

   

练习题：

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301131427060.png)

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301131428556.png)

Linux修改 

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301131429257.png)

```bash
sql_mode=STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION
```

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301131430309.png)

重启 mysql 服务

systemctl restart mysql.service

https://blog.csdn.net/weixin_45877759/article/details/121912006

遇到的问题 【reference + 的字段，要求必须是主表的 ***\*主键\**** 或者是 ***\*unique\**** 字段】

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301131432412.png)

```sql
CREATE TABLE goods(
				goods_id INT PRIMARY KEY,
				goods_name VARCHAR(32) NOT NULL DEFAULT '',
				uniqueprice DOUBLE CHECK (uniqueprice BETWEEN 1.0 and 9999.99),
				category VARCHAR(32),
				provider VARCHAR(32))
 
CREATE TABLE customer(
				customer_id INT UNIQUE,
				`name` VARCHAR(32) NOT NULL DEFAULT '',
				address VARCHAR(32),
				email VARCHAR(32) UNIQUE,
				sex VARCHAR(6) CHECK(sex IN('man', 'woman')),
				card_id VARCHAR(32) PRIMARY KEY)
			
			
CREATE TABLE purchase(
				order_id INT PRIMARY KEY,
				customer_id INT,
				goods_id INT NOT NULL DEFAULT 0, 
				nums INT,
				FOREIGN KEY (customer_id) REFERENCES customer(customer_id),
				FOREIGN KEY (goods_id) REFERENCES goods(goods_id))
```

### 5. 自增长

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301131434106.png)

```sql
#	演示自增长的使用
CREATE TABLE t1(
    id INT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(32) NOT NULL DEFAULT '',
    `name` VARCHAR(32) NOT NULL DEFAULT '');

DESC t1
```

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301131434480.png)

 下面开始测试：

```sql
INSERT INTO t1 VALUES
	(NULL, 'jack@qq.com', 'jack');
 
SELECT * FROM t1;
 
INSERT INTO t1 (email, `name`) VALUES
	('llq@qq.com', 'jack');
```

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301131436242.png)

注意细节：

1. 一般来说自增长和 primary key 配合使用

2. 自增长也可以单独使用（但是需要配合一个 unique）

3. 子增长修饰的类型是整数型的

4. 自增长默认从 1 开始，你也可以通过如下命令修改：

   ```sql
   alter table 表名 auto_increment = 新的开始值;
   ```

5. 如果你添加数据时，给自增长字段（列）指定的有值，则以指定的值为准

6. 如果指定了自增长，一般来说，就按照自增长的规则来添加数据



## 16. 索引 【空间换时间】

说起数据库性能，索引是最物美价廉的东西了。

不用加内存，不用改程序，不用调sql，查询速度就可能提高百倍千倍

使用sql 语句构建海量表

```sql
CREATE TABLE dept( /*部门表*/
deptno MEDIUMINT   UNSIGNED  NOT NULL  DEFAULT 0,
dname VARCHAR(20)  NOT NULL  DEFAULT "",
loc VARCHAR(13) NOT NULL DEFAULT ""
) ;
 
#创建表EMP雇员
CREATE TABLE emp
(empno  MEDIUMINT UNSIGNED  NOT NULL  DEFAULT 0, /*编号*/
ename VARCHAR(20) NOT NULL DEFAULT "", /*名字*/
job VARCHAR(9) NOT NULL DEFAULT "",/*工作*/
mgr MEDIUMINT UNSIGNED NOT NULL DEFAULT 0,/*上级编号*/
hiredate DATE NOT NULL,/*入职时间*/
sal DECIMAL(7,2)  NOT NULL,/*薪水*/
comm DECIMAL(7,2) NOT NULL,/*红利*/
deptno MEDIUMINT UNSIGNED NOT NULL DEFAULT 0 /*部门编号*/
) ;
 
#工资级别表
CREATE TABLE salgrade
(
grade MEDIUMINT UNSIGNED NOT NULL DEFAULT 0,
losal DECIMAL(17,2)  NOT NULL,
hisal DECIMAL(17,2)  NOT NULL
);
 
#测试数据
INSERT INTO salgrade VALUES (1,700,1200);
INSERT INTO salgrade VALUES (2,1201,1400);
INSERT INTO salgrade VALUES (3,1401,2000);
INSERT INTO salgrade VALUES (4,2001,3000);
INSERT INTO salgrade VALUES (5,3001,9999);
 
delimiter $$
 
#创建一个函数，名字 rand_string，可以随机返回我指定的个数字符串
create function rand_string(n INT)
returns varchar(255) #该函数会返回一个字符串
begin
#定义了一个变量 chars_str， 类型  varchar(100)
#默认给 chars_str 初始值   'abcdefghijklmnopqrstuvwxyzABCDEFJHIJKLMNOPQRSTUVWXYZ'
 declare chars_str varchar(100) default
   'abcdefghijklmnopqrstuvwxyzABCDEFJHIJKLMNOPQRSTUVWXYZ'; 
 declare return_str varchar(255) default '';
 declare i int default 0; 
 while i < n do
    # concat 函数 : 连接函数mysql函数
   set return_str =concat(return_str,substring(chars_str,floor(1+rand()*52),1));
   set i = i + 1;
   end while;
  return return_str;
  end $$
 
 
 #这里我们又自定了一个函数,返回一个随机的部门号
create function rand_num( )
returns int(5)
begin
declare i int default 0;
set i = floor(10+rand()*500);
return i;
end $$
 
 #创建一个存储过程， 可以添加雇员
create procedure insert_emp(in start int(10),in max_num int(10))
begin
declare i int default 0;
#set autocommit =0 把autocommit设置成0
 #autocommit = 0 含义: 不要自动提交
 set autocommit = 0; #默认不提交sql语句
 repeat
 set i = i + 1;
 #通过前面写的函数随机产生字符串和部门编号，然后加入到emp表
 insert into emp values ((start+i) ,rand_string(6),'SALESMAN',0001,curdate(),2000,400,rand_num());
  until i = max_num
 end repeat;
 #commit整体提交所有sql语句，提高效率
   commit;
 end $$
 
 #添加8000000数据
call insert_emp(100001,8000000)$$
 
#命令结束符，再重新设置为;
delimiter ;
```

看下没有索引下查询一条记录要花多少时间，然后再创建索引再来查询下，看下时间效率是否提升得很多

```sql
-- 在没有创建索引时，我们查询一条记录
SELECT * FROM emp	
					WHERE empno = 1234567
 
 
-- 使用索引来优化一下，体验索引得牛逼
-- 
-- 在没有创建索引前，emp.ibd文件大小是 524 M
-- 
-- 在创建索引后 emp.ibd 文件大小是 655 M [索引本身也会占用空间] --- 空间换时间
 
 
-- 创建 ename 索引
-- ename_index 索引名称
-- ON emp (empno)：表示在 emp 表的 empno 列创建索引
 
CREATE INDEX empno_index ON emp (empno);
 
 
-- 创建索引后，查询的速度如何？
-- 
SELECT * FROM emp
						WHERE empno = 12345678 --- 0.0003s，原来是 4.5s
 
-- 创建索引后，只对创建了索引的列有效
 
SELECT * FROM emp	
					WHERE ename = 'PjDlwy'
 
 
CREATE INDEX ename_index ON emp(ename);
```

### 16.1 索引机制

如果没有索引的话，会进行全表扫描

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301131445290.png)

id创建索引后，会形成一个索引的数据结构，比如二叉树

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301131445155.png)

 对 dml（update，delete，insert）语句效率影响较大 【比如：把7删掉了】

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301131445716.png)

 那么这个二叉树的索引要重新构建，会导致整个数据结构重新改变

【如果有索引，意味着你删除一个数据过后，你要重新维护这个索引】

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301131446407.png)

### 16.2 索引的类型

1. 主键索引，主键自动的为主索引（类型 Primary key）

   ![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301131447904.png)

2. 唯一索引（UNIQUE）

   ![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301131447406.png)

3. 普通索引

4. 全文索引（FULLTE XT）【适用于 MyISAM】--- 不好用

   开发中考虑使用：全文搜索 Solr 和 ElasticSearch（ES）

#### 1. 添加索引

```sql
#	演示mysql索引 的使用
 
CREATE TABLE t25 (
					id INT,
					`name` VARCHAR(32));
 
-- 查询表是否有索引
SHOW INDEX FROM t25;
SHOW INDEX FROM t26;
 
 
-- 添加索引
-- 添加唯一索引
CREATE UNIQUE INDEX id_index ON t25 (id);
-- 添加普通索引
CREATE INDEX id_index ON t25 (id);
 
ALTER TABLE t25 ADD INDEX id_index (id);
-- 如何选择
-- 1. 如果某列的值，是不会重复的，则有线考虑使用 unique 索引，否则使用普通索引
 
-- 2. 添加主键索引
CREATE TABLE t26 (
					id INT,
					`name` VARCHAR(32));
ALTER TABLE t26 ADD PRIMARY KEY (id);
```

#### 2. 删除索引

```sql
#	删除索引
SHOW INDEX FROM t25;
SHOW INDEX FROM t26;
 
DROP INDEX id_index ON t25;
 
#	删除主键索引
ALTER TABLE t26 DROP PRIMARY KEY
 
#	修改索引，先删除，再添加新的索引
 
 
#	查询索引
1. 方式
SHOW INDEX FROM t25;
2. 方式
SHOW INDEXES FROM t25;
3. 方式
SHOW KEYS FROM t25;
4. 方式
DESC t25;
```

### 16.3 创建索引规则

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301131514483.png)

## 17. 事务（一组相关的 dml 语句构成，保持数据一致性）

事务：相当于开了一个规划区，在规划区中修改数据，但是只有 commit 后才会生效

细节：如果不开启事务，默认情况下，dml 操作是自动提交的，不能回滚

开启一个事务：

```sql
start transaction
set autocommit=off
```

四种隔离级别：

1. READ UNCOMMITTED【读 --- 未提交】
2. READ COMMITTED 【读 --- 已提交】
3. REPEATABLE READ【可重复读】--- 默认
4. SERIALIZABLE 【可串行化】

多个连接开启各自的事务操作数据库中数据时，数据库系统要负责隔离操作，以保证各个连接在获取数据时的准确性。

如果不考虑隔离性，可能会引发如下问题：

- 脏读（dirty read）

  - 当一个事务读取另一个事务未提交的修改

- 不可重复读（nonrepeatable read）

  - 同一查询在同一事务中多次进行，由于其它提交事务所做的 <font color="yellow">修改</font> 或者 <font color="yellow">删除</font>，每次返回不同的结果集

- 幻读（phantom read）

  ​	同一查询在同一事务中多次进行，由于其它提交事务所做的***\*插入\****操作，每次返回不同的结果集。

<font color="yellow">事务 </font>用于保持 <font color="yellow">数据的一致性</font>，它 <font color="yellow">由 </font>一组 <font color="yellow">相关的 dml 语句</font> 组成

该组 dml 语句要么全部成功，要么全部失败

例如：转账就要用事务来处理，用以保证数据的一致性

```sql
#	事务
CREATE TABLE t27(
    id INT,
    `name` VARCHAR(32));

#	开启事务
START TRANSACTION

#	设置保存点 a
SAVEPOINT a

-- 执行 dml 操作
INSERT INTO t27 VALUES
							(100, 'tom');

SELECT * FROM t27;

#	设置保存点 b
SAVEPOINT b

-- 执行 dml 操作
INSERT INTO t27 VALUES
							(200, 'jack');

#	回退到 b
ROLLBACK TO b

#	回退到 a
ROLLBACK TO a

-- 如果这样，表示直接回到事务开始的状态
ROLLBACK
```

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301132038897.png)

 前提是要开启事务，而且你要设置保存点

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301132042370.png)

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301132042248.png)

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301132044286.png)

### 17.1 事务管理

#### 事务和锁

当执行事务操作时，mysql 会在表上加锁，防止其它用户改表的数据，这对用户来讲是非常重要的

### 17.2 隔离级别（重难点）

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301141015307.png)

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301141016628.png)

C2 在10点登录数据库，想要盘点10点之前的数据，但是却读到了C1在10点之后对数据库的修改

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301141018473.png)

事务隔离级别：

概念：Mysql隔离级别定义了事务与事务之间的隔离程度

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301141019813.png)

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301141020684.png)

### 17.3 隔离级别演示

隔离级别是 <font color="yellow">**和事务相关** </font>的，所以你一定要启动事务

#### 1. READ-UNCOMMITTED 【脏读、不可重复读（修改和删除）、幻读（插入）】

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301141027414.png)

现在往表里 添加一条记录

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301141029945.png)

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301141032235.png)

#### 2. READ-COMMITTED 【解决脏读】

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301141034803.png)

修改完***\*隔离级别\****后，一定要 ***\*开启事务\**** 后，再查询数据

左侧再插入一条数据，先不提交 --- 此时右侧查不到了，说明不会发生脏读

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301141035895.png)

左侧再来修改一条数据后，再提交，此时右侧可以看到 修改和插入的数据

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301141036801.png)

 说明在当前： ***\*读-已提交\* ***的 隔离级别下 出现了 不可重复读取（<font color="yellow">修改</font>） 幻读（<font color="yellow">插入</font>）

#### 3. REPEATTABLE READ（默认）【解决不可重复读、幻读】

左侧插入一条数据，修改一条数据，先不提交，右侧查不到

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301141043917.png)

左侧提交，右侧还是查不到

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301141044281.png)

#### 4. SERIALIZABLE（串行化）

左侧插入、修改一条数据，先不提交

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301141044523.png)

右侧：

卡在这里（不停地闪烁）： 它发现有一个事务正在操作这个表，还没提交，它会卡在这里

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301141045175.png)

现在左侧提交，右侧显示超时，并重新开启一个新的事务，这时候可以查到数据了

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301141046089.png)

为何能够查到所有的数据，因为是当前事务是重新开启的一个新事务，所以可以查到所有的数据

### 17.4 设置隔离（系统隔离级别，当前会话隔离级别）

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301141048136.png)

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301141048271.png)

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301141048251.png)

### 17.5 ACID特性

#### 1. 原子性（Atomicity）【Now or Never】

事务是一个不可分割的工作单位，事务中的操作要么都发生，要么都不发生

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301141049058.png)

#### 2. 一致性（Consistency）

状态1：提交前

状态2：提交后

事务必须使数据库从一个 <font color="blue">一致性状态</font> 变换到 <font color="red">另外一个一致性状态</font>

- 事务一旦提交了过后呢，它会整体地进行一个改变，这个事务就结束了，开启一个新的事务。
- 也就是说事务是一个整体，提交了过后呢，才能变到另外一个状态，开启一个新的事务，

#### 3. 隔离性（Isolation）

事务的隔离性是 <font color="yellow">**多个用户并发访问** </font>数据库（多个连接的时候）时，数据库 <font color="yellow">为每一个用户开启的事务</font>，不能被其它事务的操作数据所干扰，多个并发事务之间要相互隔离。

#### 4. 持久性（Durability）

持久性是指一个事务一旦被提交，它对数据库中数据的改变就是永久性的，接下来即使数据库发生故障也不应该对其有任何影响

之前讲过，事务一旦提交了，想回滚也是回滚不了的，因为它是一个整体，一提交就持久化到数据库相应的文件里界面去了

#### 课后习题

注意： <font color="red">**先把条件都设置好**</font>，再开启事务

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301141105240.png)

1. 会 ---> 脏读
2. 不会，因为 ---> 当前隔离级别为：READ COMMITED：不会脏读！！！

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301141107551.png)

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301141107444.png)

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301141112877.png)

## 18. mysql 表类型和存储引擎

```sql
#	表类型和存储引擎
 
#	查看所有的存储引擎
SHOW ENGINES;
 
#	innodb 存储引擎，前面使用过
-- 1. 支持事务
-- 2. 支持外键
-- 3. 支持行级锁
 
#	myisam 存储引擎
CREATE TABLE t28(
    id INT,
    `name` VARCHAR(32)) ENGINE MYISAM;
-- 1. 添加速度快
-- 2. 不支持外键和事务
-- 3. 支持表级锁
 
START TRANSACTION;
 
SAVEPOINT t1;
 
INSERT INTO t28 VALUES			
					(1, 'tom');
 
SELECT * FROM t28;
-- 现在想要回滚
ROLLBACK TO t1;
 
#	memory 存储引擎
-- 1. 数据存储在内存中 【关闭 Mysql 服务，数据丢失，但是表结构还在】
-- 2. 执行速度很快（没有IO读写）
-- 3. 默认支持索引（hash表）
 
CREATE TABLE t29(
				id INT,
				`name` VARCHAR(32)) ENGINE MEMORY;
 
INSERT INTO t29 VALUES			
					(1, 'tom'),
					(2, 'jack'),
					(3, 'hsp');
 
SELECT * FROM t29;
-- 查看下表的结构，发现还在
desc t29;
```

### 18.1 基本介绍

1. MySQL的表类型由存储引擎（Storage Engines）决定，主要包括 MyISAM，innoDB，Memory等
2. MySQL数据表主要支持六种类型：
   - CSV、Memory、ARCHIVE、MRG_MYISAM、MYISAM、InnoDB
3. 这六种又分为2类：
   - 一类是 “事务安全型”（transaction-safe），比如：InnoDB
   - 其余都属于第二类，称为 “非事务安全型(non-trans action-safe)" 【myisam 和 memory】

### 18.2 查看索引引擎

SHOW ENGINES 

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301141115281.png)

### 18.3 主要的存储引擎 / 表类型特点

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301141115153.png)

### 18.4 细节说明

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301141116381.png)

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301141116541.png)

基于哈希的，存储在 ***\*内存\**** 中，对临时表有用

提高了数据的读取速度，它把整个数据直接放在内存里面，（在内存里面就没有IO操作的影响）

但是只要重启了mysql服务后，数据都没有了

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301141118390.png)

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301141118371.png)

 修改存储引擎

```sql
ALTER TABLE `表名` ENGINE = 存储引擎
```

## 19. 视图（View）

1. 视图是一个虚拟表，其内容由查询定义、同真实的表一样，视图包含列，<font color="red">**其数据来自对应的真实表**</font>（基表）
2. 视图和基表的示意图

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301141120115.png)

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301141128783.png)

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301141128157.png)

### 19.1 视图基本使用

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301141130572.png)

```sql
#	视图的使用
#	创建一个视图 emp_view01，只能查询 emp表的 （emp，ename，job 和 deptno）信息
 
#	创建视图
 
CREATE VIEW emp_view01 AS	
					SELECT empno, ename, job, deptno FROM emp;
 
-- 查看视图
DESC emp_view01;
 
SELECT * FROM emp_view01;
 
-- 查看创建视图的指令
SHOW CREATE VIEW emp_view01;
 
-- 删除视图
DROP VIEW emp_view01;
```

### 19.2 视图细节讨论

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301141130845.png)

.frm 结构文件

.ibd 数据文件

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301141131087.png)

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301141132461.png)

### 19.3 视图最佳实践

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301141132670.png)

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301141133184.png)

```sql
#	视图课堂练习
 
-- 针对 emp, dept, salgrade 三张表，创建一个视图 emp_view03
-- 可以显示雇员编号，雇员名，雇员部门名和 薪水级别【即：使用三张表，构建一个视图】
 
/*
		分析：使用三表联合查询，得到结果
		13 * 4 * 5
		将得到的结果构建成 视图
*/
 
SELECT empno, ename, dname, grade
			FROM emp, dept, salgrade
			WHERE emp.deptno = dept.deptno AND # emp 和 dept 表 靠 deptno 拼接
            	(sal BETWEEN losal AND hisal)	# emp 和 grade 表 靠 salgrade 拼接
 
 
CREATE VIEW emp_view04
				AS
				SELECT empno, ename, dname, grade
					FROM emp, dept, salgrade
					WHERE emp.deptno = dept.deptno AND 
						(sal BETWEEN losal AND hisal)
 
SELECT * FROM emp_view04;
```

## 20. Mysql 管理 （mysql.user 表中）

### 20.1 创建、修改用户，修改密码

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301141140016.png)

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301141142310.png)

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301141142280.png)

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301141142734.png)

```sql
#	创建新的用户
#	密码存放到 mysql.user 表时，是 password() 加密过的
 
CREATE USER 'llq'@'localhost' IDENTIFIED BY '123456'
 
SELECT `host`, `user`, authentication_string
						FROM mysql.user;
 
 
#	删除用户
DROP USER 'llq'@'localhost'
 
# 登录 
 
#	修改自己的密码，没问题
SET PASSWORD = PASSWORD('abcdef')
 
#	修改别人的密码，需要权限
#	root 用户修改 hsp_edu@localhost 密码，是可以成功的
SET PASSWORD FOR 'llq'@'localhost' = PASSWORD('123456');
```

### 20.2 Mysql 中的权限

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301141144445.png)

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301141145793.png)

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301141145524.png)

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301141145146.png)

练习题：

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301141145515.png)

![image-20230114120800927](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301141208089.png)

看能够修改？？？

![image-20230114120856391](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301141208497.png)

```sql
CREATE USER 'shunping'@'localhost' IDENTIFIED BY '123';
-- 默认情况下，shunping用户只能看到一个默认的系统数据库 information_schema

CREATE DATABASE testdb;

use testdb;

CREATE TABLE news(
    id INT,
    content VARCHAR(32));

-- 测试一条测试数据
INSERT INTO news VALUES
						(1, 'kakao');

SELECT * FROM news;

-- 给 shunping 分配 查看news 表 和添加数据的权限
GRANT SELECT, INSERT
				ON testdb.news
				TO 'shunping'@'localhost';

-- 可以增加 Update 权限
GRANT UPDATE
				ON testdb.news
				TO 'shunping'@'localhost';

-- 修改密码为 abc，要求：使用 root 用户完成
SET PASSWORD FOR 'shunping'@'localhost' = PASSWORD('abc');

-- 演示回收权限
REVOKE SELECT, INSERT, UPDATE 
					ON testdb.news
					FROM 'shunping'@'localhost';

删除用户 shunping
DROP USER 'shunping'@'localhost';
```

细节说明：

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301141209979.png)

![image-20230114121406448](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301141214586.png)

```sql
#	用户管理细节
-- 在创建用户的时候，如果不指定Host，则为 %，表示所有IP都可以连接（包括远程）
 
CREATE USER jack;
 
SELECT `host`, `user` FROM mysql.user;
 
-- 你也可以这样登录（指定ip段 可以登录）
-- create user 'xxx'@'192.168.1.%'，表示 xxx 用户 在 192.168.1.* 的ip可以登录 mysql
 
create user 'smith'@'192.168.1.%';
 
-- 在删除用户时，如果 host 不是 %，则必须明确指定 '用户'@'host值'
 
DROP USER jack;	-- 默认就是 DROP USER 'jack'@'%';
 
 
DROP USER smith;
 
DROP USER 'smith'@'192.168.1.%';
```

## 21. 课后作业

```sql
show tables;

DESC dept;

DESC emp;

SELECT * FROM emp;

SELECT * FROM dept;

SELECT dname FROM dept;

SELECT ename, (sal + IFNULL(comm, 0)) * 13 AS '年收入' FROM emp;


#	工资超过 2850 雇员和工资
SELECT ename, sal FROM emp
				WHERE sal > 2850;

#	显示工资不在1500到2850之间的所有雇员名及工资

SELECT ename, sal from emp
				WHERE  sal < 1500 || sal > 2850;


#	编号为 7566 的雇员姓名以及所在部门编号
SELECT * from emp, dept
				WHERE emp.deptno = dept.deptno AND
				empno=7566;

#	部门10和30工资超过 1500 的雇员名和工资

SELECT ename, sal from emp
				WHERE deptno IN (10, 30) AND
				sal > 1500;

#	显示无 管理者的雇员名及岗位

SELECT ename,job FROM emp
				WHERE mgr is NULL

#	排序数据
-- 1）显示 1991.2.1 到 1991.5.1 之间的雇佣的雇员名，岗位和雇佣日期，并以雇佣日期进行排序
-- 日期是可以进行比较的
SELECT ename, job, hiredate FROM emp
				WHERE hiredate >= '1991-02-01' AND hiredate <= '1991-05-01'
				ORDER BY hiredate;

-- 2）显示所有获得补助的雇员名，工资及补助，并以工资降序排序
SELECT ename, sal, comm FROM emp
				WHERE comm is NOT NULL;
				ORDER BY sal DESC;


-- 如果 select 语句同时包含有 group by，having，limit，order by
-- 
-- 那么他们的顺序是：where group by，having，order by，limit
```





