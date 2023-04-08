

# 牛客网 Mysql【入门】
- [牛客网 Mysql【入门】](#牛客网-mysql入门)
  - [3. 去重【distinct】](#3-去重distinct)
  - [8. 区间【between and】](#8-区间between-and)
  - [13. 符合条件【in】](#13-符合条件in)
  - [14. 操作符混合运用](#14-操作符混合运用)
  - [15. 名字带有北京的](#15-名字带有北京的)
  - [18. 分组【group by】](#18-分组group-by)
  - [19. 分组过滤 【having】](#19-分组过滤-having)
  - [20. 分组排序【order by】](#20-分组排序order-by)
  - [22. 统计每个学校的答过题的用户的平均答题数](#22-统计每个学校的答过题的用户的平均答题数)
  - [23.  **统计每个学校各难度的用户平均刷题数**](#23--统计每个学校各难度的用户平均刷题数)
  - [24. **统计每个用户的平均刷题数**](#24-统计每个用户的平均刷题数)
  - [25. **查找山东大学或者性别为男生的信息**](#25-查找山东大学或者性别为男生的信息)
  - [26. **计算25岁以上和以下的用户数量**](#26-计算25岁以上和以下的用户数量)
  - [27. **查看不同年龄段的用户明细**](#27-查看不同年龄段的用户明细)
  - [28. **计算用户8月每天的练题数量**【Date】](#28-计算用户8月每天的练题数量date)
  - [29.  **计算用户的平均次日留存率**【DATEDIFF】](#29--计算用户的平均次日留存率datediff)
  - [30. **统计每种性别的人数**](#30-统计每种性别的人数)

如果 select 语句同时包含有 group by，having，limit，order by

那么他们的 <font color="yellow">**顺序**</font> 是： 

- where（限制属性） 
- group by（分组）
- having（筛选）
- order by（排序）
- limit（分页【限制记录条数】）

Group By 操作注意事项：

1. 有group by后出现的字段
2. group by后出现的字段＋聚合函数的组合

ONLY_FULL_GROUP_BY限制：

```bash
# 我们在上面提到select中的列都出现在group by中
# 其实在MySQL5.7.5之前是没有此类限制的
# 5.7.5版本在sql_mode中增加了ONLY_FULL_GROUP_BY参数，用来开启或者关闭针对group by的限制。下面我们在分别开启和关闭ONLY_FULL_GROUP_BY限制的情况下分别进行验证。
```

```bash
这个实验可以看出group by中只保留id是可以正常执行的，为什么？id字段有什么特殊性呢？

大致的意思是：如果name列是主键或者是唯一的非空列，name上面的查询是有效的。这种情况下，MySQL能够识别出select中的列依赖于group by中的列。比如说，如果name是主键，它的值就决定了address的值，因为每个组只有一个主键值，分组中的每一行都具有唯一性，因此也不需要拒绝这个查询。
```

```bash
# 通过上面的例子也验证了，
# 对于有唯一性约束的字段，也可以不用在group by中把select中的字段全部列出来。不过针对主键或者唯一性字段进行分组查询意义并不是很大，因为他们的每一行都是唯一的。
```



<font color="yellow">精度</font>：保留4位小数round(x, 4)

where后限制的条件只能是原表中有的字段！！！

where VS having

- 若过滤条件中使用了聚合函数，就必须使用 having 来替换 where，否则报错
- WHERE 可以直接使用表中的字段作为筛选条件，但不能使用分组中的计算函数作为筛选条件
- HAVING 必须要与 GROUP BY 配合使用，可以把分组计算的函数和分组字段作为筛选条件。
- 这决定了，在需要对数据进行分组统计的时候，HAVING 可以完成 WHERE 不能完成的任务。这是因为，在查询语法结构中，WHERE 在 GROUP BY 之前，所以无法对分组结果进行筛选。HAVING 在 GROUP BY 之后，可以使用分组字段和分组中的计算函数，对分组的结果集进行筛选，这个功能是 WHERE 无法完成的。另外，WHERE排除的记录不再包括在分组中。
- 如果需要通过连接从关联表中获取需要的数据，WHERE 是先筛选后连接，而 HAVING 是先连接后筛选。
- 这一点，就决定了在关联查询中，WHERE 比 HAVING 更高效。因为 WHERE 可以先筛选，用一个筛选后的较小数据集和关联表进行连接，这样占用的资源比较少，执行效率也比较高。HAVING 则需要先把结果集准备好，也就是用未被筛选的数据集进行关联，然后对这个大的数据集进行筛选，这样占用的资源就比较多，执行效率也较低

|        | 优点                         | 缺点                                 |
| ------ | ---------------------------- | ------------------------------------ |
| Where  | 先筛选数据再关联，执行效率高 | 不能使用分组中的计算函数进行筛选     |
| Having | 可以使用分组中的计算函数     | 在最后的结果集中进行筛选，执行效率低 |

开发中选择：

WHERE 和 HAVING 也不是互相排斥的，我们可以在一个查询里面同时使用 WHERE 和 HAVING

- 包含分组统计函数【<font color="red">**聚合函数**</font>】的条件用HAVING
- 普通条件用 WHERE

这样，我们就既利用了 WHERE 条件的高效快速，又发挥了 HAVING 可以使用包含分组统计函数的查询条件的优点。当数据量特别大的时候，运行效率会有很大的差别。



Union用法

- 连接表，对行操作
  - union--将两个表做行拼接，同时自动删除重复的行。
  - union all---将两个表做行拼接，保留重复的行。

```sql
round(count(qpd.question_id) / count(distinct qpd.device_id), 4) as avg_answer_cnt
```



group_concat用法：【纵列拼接】

```sql
group_concat(distinct concat_ws(':',date(start_time),tag) order by start_time separator ';') detail
```



窗口函数【分析函数】

- Mysql8.0 支持窗口函数【Window Function】，也称分析函数
- 与分组聚合函数类似，但是 <font color="red">**每一行数据都生成一个结果**</font>
- 聚合窗口函数：SUM、AVG、COUNT、MAX、MIN等等

![image-20230323202236517](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303232022626.png)

传统聚合函数：【group by】

![image-20230323202305431](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303232023501.png)

窗口函数【分析函数】---> 可以显示出原始数据

```sql
select 
    year, 
    country, 
    product, 
    profit, 
	sum(profit) OVER (partition by country) as country_profit
from
	sales
order by
	country,
	year,
	product,
	profit
```

![image-20230323202816799](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303232028909.png)

专用窗口函数

1. 排名函数
   - ROW_NUMBER()
   - RANK()
   - DENSE_RANK()
   - PERCENT_RANK()
2. 分组内的第一或者最后一名
   - FIRST_VALUE()
   - LAST_VALUE()
   - LEAD() ：相对当前数据，向后查第几个
   - LAG()：相对当前数据，向前查第几个
3. 分析函数
   - CUME_DIST()：累积到现在，这个数据占了多少
   - NTH_VALUE()
   - NTILE()：百分位

举例子：

![image-20230324095848469](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303240958859.png)

```sql
select
	val,
	row_number() OVER (order by val) as 'row_number' from numbers
```

![image-20230324100051933](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303241000025.png)

```sql
select 
	val,
	first_value(val) over (order by val) as 'first',
	lead(val, 1) over (order by val) as 'lead' # 当前数据的后一个
from
	numbers
```

![image-20230324100624590](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303241006685.png)

```sql
select 
	val,
	ntile(4) over (order by val) as 'ntile4'
from	
	numbers
```

![image-20230324101100787](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303241011865.png)

窗口定义：

![image-20230324101342361](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303241013479.png)

![image-20230324101442761](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303241014898.png)

例题：

![image-20230324101540449](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303241015990.png)

```sql
select
	year,
	country,
	product,
	profit,
	sum(profit) over (partition by country order by profit rows unbounded preceding) as 'running_total'
from
	sales
order by
	country,
	profit
```

![image-20230324102110545](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303241021692.png)

```sql
select
	year,
	country,
	product,
	profit,
	# 当前值前一个 
	# 当前值
	# 当前值后一个
	avg(profit) over (partition by country order by profit rows between 1 preceding and 1 following) as 'running_avg'
from
	sales
order by
	country,
	profit
```

![image-20230324102742330](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303241027482.png)

```sql
# 定义公共窗口函数
select
	year,
	country,
	product,
	profit,
	first_value(profit) over w as 'first',
	last_value(profit) over w as 'last'
from
	sales
WINDOWS w as (partition by country order by profit rows unbounded preceding)
order by
	country,
	profit
```

![image-20230324103407169](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303241034300.png)



left join 

进行左连接时，就有涉及到主表，辅表

- 这时主表条件写在 where 后
- 辅表条件写在 on 后

![aqz](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303241148609.png)

时间戳相应操作：

- 格式化

  ```sql
  DATE_FORMAT(submit_time,'%Y%m')
  # Y：完整年
  # y：年份的后 2 位
  ```

- 2个日期差

  ```sql
  # 第二个参数 - 第一个参数
  SELECT TIMESTAMPDIFF(MONTH,'2012-10-01','2013-01-13');
  ```

  ```sql
  # 第一个参数 - 第二个参数
  SELECT DATEDIFF('2013-01-13','2012-10-01'); # 返回相差的天数
  ```

字符串截取

- left(str, length)：使用left()函数可以从左至右对字符串进行截取
- rightstr, length)：使用right()函数可以从右至左对字符串进行截取
- SUBSTR(str,startPosition,returnLength)：开始截取位置 + 截取长度
- REVERSE()：将字符串反转

统计长度：

- length：字节
  - utf8编码：一个汉字三个字节，一个数字或字母一个字节。
  - gbk编码：一个汉字两个字节，一个数字或字母一个字节。
- 不管汉字还是数字或者是字母都算是一个字符



大小写混乱时处理：

- lower
- upper



## 3. 去重【distinct】

```sql
select distinct university from user_profile;
```

## 8. 区间【between and】

```sql
select device_id, gender, age from user_profile where age between 20 and 23;
```

## 13. 符合条件【in】

```sql
select device_id, gender, age, university, gpa from user_profile where university in ('北京大学', '复旦大学', '山东大学');
```

## 14. 操作符混合运用

```sql
select device_id, gender, age, university, gpa from user_profile where (gpa > 3.5 and university='山东大学') or (gpa > 3.8 and university="复旦大学");
```

## 15. 名字带有北京的

like用法：

- %：0 到 多个字符
- _：单个字符

```sql
select
    device_id,
    age,
    university
from
    user_profile
where
    university like '%北京%';
```

## 18. 分组【group by】

- 每个学校
- 每种性别的用户数、30天内平均活跃天数和平均发帖数量。

```sql
select
    gender,
    university,
    count(device_id) user_num,
    avg(active_days_within_30) avg_active_day,
    avg(question_cnt) avg_question_cnt
from
    user_profile
group by
    gender,
    university;
```

## 19. 分组过滤 【having】

取出平均发贴数低于5的学校或平均回帖数小于20的学校

```sql
select
    university,
    avg(question_cnt) avg_question_cnt,
    avg(answer_cnt) avg_answer_cnt
from
    user_profile
group by
    university
having avg_question_cnt < 5 or avg_answer_cnt < 20;
```

## 20. 分组排序【order by】

```sql
select
    university,
    avg(question_cnt) avg_question_cnt
from
    user_profile
group by
    university
order by avg_question_cnt asc;
```

| id   | device_id                        | question_id | result |
| ---- | -------------------------------- | ----------- | ------ |
| 1    | 2138                             | 111         | wrong  |
| 2    | 3214                             | 112         | wrong  |
| 3    | 3214                             | 113         | wrong  |
| 4    | 6543                             | 114         | right  |
| 5    | <font color="yellow">2315</font> | 115         | right  |
| 6    | <font color="yellow">2315</font> | 116         | right  |
| 7    | <font color="yellow">2315</font> | 117         | wrong  |

| id   | device_id                        | gender | age  | university                           | gpa  | active_days_within_30 | question_cnt | answer_cnt |
| ---- | -------------------------------- | ------ | ---- | ------------------------------------ | ---- | --------------------- | ------------ | ---------- |
| 1    | 2138                             | male   | 21   | 北京大学                             | 3.4  | 7                     | 2            | 12         |
| 2    | 3214                             | male   |      | 复旦大学                             | 4.0  | 15                    | 5            | 25         |
| 3    | 6543                             | female | 20   | 北京大学                             | 3.2  | 12                    | 3            | 30         |
| 4    | <font color="yellow">2315</font> | female | 23   | <font color="yellow">浙江大学</font> | 3.6  | 5                     | 1            | 2          |
| 5    | 5432                             | male   | 25   | 山东大学                             | 3.8  | 20                    | 15           | 70         |
| 6    | 2131                             | male   | 28   | 山东大学                             | 3.3  | 15                    | 7            | 13         |
| 7    | 4321                             | female | 26   | 复旦大学                             | 3.6  | 9                     | 6            | 52         |

找出浙江大学对应的 device_id，然后再去第一个表中去搜索！！！

```sql
select
    device_id,
    question_id,
    result
from
    question_practice_detail
where
    device_id in ( # 浙江大学对应的 device_id
        select
            device_id
        from
            user_profile	#	通过 user_profile 表
        where
            university = '浙江大学'
    )
order by
    question_id asc;
```

## 22. 统计每个学校的答过题的用户的平均答题数

问题分析：

- 每个学校：按学校分组，`group by university`
- 平均答题数量：在每个学校的分组内，用总答题数量除以总人数即可得到平均答题数量`count(question_id) / count(distinct device_id)`
- 表连接：学校和答题信息在不同的表，需要做连接

左连接：

```sql
select
    *
from
    question_practice_detail
    left join user_profile on question_practice_detail.device_id = user_profile.device_id
```

| 2138 | 111  | wrong | 2138 | male   | 21   | 北京大学 | 3.400 | 7    |
| ---- | ---- | ----- | ---- | ------ | ---- | -------- | ----- | ---- |
| 3214 | 112  | wrong | 3214 | male   | None | 复旦大学 | 4.000 | 15   |
| 3214 | 113  | wrong | 3214 | male   | None | 复旦大学 | 4.000 | 15   |
| 6543 | 111  | right | 6543 | female | 20   | 北京大学 | 3.200 | 12   |
| 2315 | 115  | right | 2315 | female | 23   | 浙江大学 | 3.600 | 5    |
| 2315 | 116  | right | 2315 | female | 23   | 浙江大学 | 3.600 | 5    |
| 2315 | 117  | wrong | 2315 | female | 23   | 浙江大学 | 3.600 | 5    |
| 5432 | 118  | wrong | 5432 | male   | 25   | 山东大学 | 3.800 | 20   |
| 5432 | 112  | wrong | 5432 | male   | 25   | 山东大学 | 3.800 | 20   |
| 2131 | 114  | right | 2131 | male   | 28   | 山东大学 | 3.300 | 15   |
| 5432 | 113  | wrong | 5432 | male   | 25   | 山东大学 | 3.800 | 20   |

实现代码：

```sql
select
    university,
    (
        count(question_id) / count(distinct user_profile.device_id)
    ) avg_answer_cnt
from
    question_practice_detail
    left join user_profile on question_practice_detail.device_id = user_profile.device_id
group by
    university
```

## 23.  统计每个学校各难度的用户平均刷题数

- 每个学校：按学校分组`group by university`
- 不同难度：按难度分组`group by difficult_level`
- 平均答题数：总答题数除以总人数`count(qpd.question_id) / count(distinct qpd.device_id)`
- 来自上面信息三个表，需要联表，up与qpd用device_id连接，qd与qpd用question_id连接。

```sql
select
    university,
    difficult_level,
    (
        count(question_practice_detail.question_id) / count(distinct question_practice_detail.device_id)
    ) avg_answer_cnt
from
    user_profile,
    question_practice_detail,
    question_detail
where
    user_profile.device_id = question_practice_detail.device_id
    and question_practice_detail.question_id = question_detail.question_id
group by
    university,
    difficult_level
```

## 24. 统计每个用户的平均刷题数

- 山东大学的
- 不同难度下
- 平均答题题目数

```sql
select
    university,
    difficult_level,
    (
        count(question_practice_detail.question_id) / count(distinct question_practice_detail.device_id)
    ) avg_answer_cnt
from
    user_profile,
    question_practice_detail,
    question_detail
where
    user_profile.device_id = question_practice_detail.device_id
    and question_practice_detail.question_id = question_detail.question_id
    and university = '山东大学'
group by
    difficult_level
```

## 25. 查找山东大学或者性别为男生的信息

```sql
select
    device_id,
    gender,
    age,
    gpa
from
    user_profile
where
    university = '山东大学'
union all	# 结果并集，不去重！！！
select
    device_id,
    gender,
    age,
    gpa
from
    user_profile
where
    gender = 'male'
```

## 26. 计算25岁以上和以下的用户数量

记得 count 是 group by 之后计数

```sql
select
    (
        case
        when age >= 25 then '25岁及以上'	# 汉化！！！
        else '25岁以下'
        end
    ) as age_cut,
    count(*) as number
from
    user_profile
group by
    age_cut
```

## 27. 查看不同年龄段的用户明细

```sql
select
    device_id,
    gender,
    (
        case
        when age < 20 then '20岁以下'	# 汉化！！！
        when age >= 20 and age <= 24 then '20-24岁'
        when age >= 25 then '25岁及以上'
        else '其他'
        end
    ) as age_cut
from
    user_profile
```

## 28. 计算用户8月每天的练题数量【Date】

时间函数处理

2021-05-03【Date】

```sql
select DAY("2021-05-03")	# 日
select MONTH("2021-05-03")	# 月
select YEAR("2021-05-03") # 年
```

```sql
select
    DAY (`date`) `day`,	# 确立分组条件
    count(question_id) question_cnt
from
    question_practice_detail
where MONTH(`date`) = 8	# 8月份
group by `day`
```

## 29.  计算用户的平均次日留存率【DATEDIFF】

- 查看用户在某天刷题后
- 第二天还会再来刷题的平均概率

Mysql 计算2个日期差函数：

1. TIME <font color="yellow">**STAMP** </font>DIFF，需要传入三个参数

   - 比较的类型

     - FRAC_SECOND（毫秒）
     - SECOND
     - MINUTE
     - HOUR
     - DAY
     - WEEK
     - MONTH
     - QUARTER（季度：1 - 4）
     - YEAR

   - 第二个和第三个参数是待比较的两个时间

   - 比较是后一个时间减前一个时间

     ```sql
     # 第二个参数 - 第一个参数
     SELECT TIMESTAMPDIFF(MONTH,'2012-10-01','2013-01-13');
     ```

2.  DATEDIFF函数

   ```sql
   # 第一个参数 - 第二个参数
   SELECT DATEDIFF('2013-01-13','2012-10-01');
   ```

```sql
select count(qpd2.date)/count(qpd1.date) from
    (select distinct device_id,date from question_practice_detail) qpd1
    left join # 左连接
    (select distinct device_id,date from question_practice_detail) qpd2	# 只有符合条件的 qpd2 才会被连接
    on qpd1.device_id = qpd2.device_id 
    and DATEDIFF(qpd1.date, qpd2.date) = 1;
```

![image-20230307112853285](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303071129360.png)

注意：去重，有可能一天内刷了多次题

## 30. 统计每种性别的人数

知识点：

| device_id | profile                                        | blog_url            |
| --------- | ---------------------------------------------- | ------------------- |
| 2138      | 180cm,75kg,27,<font color="yellow">male</font> | http:/url/bigboy777 |
| 3214      | 165cm,45kg,26,female                           | http:/url/kittycc   |
| 6543      | 178cm,65kg,25,male                             | http:/url/tiger     |
| 4321      | 171cm,55kg,23,female                           | http:/url/uhksd     |
| 2131      | 168cm,45kg,22,female                           | http:/urlsydney     |

根据示例：

| gender | number |
| ------ | ------ |
| male   | 2      |
| female | 3      |



### 本文函数

Mysql 中文本函数：

1. CONCAT()：连接两个或多个字符串。
2. SUBSTRING()：返回字符串的一部分。
3. LEFT()：返回字符串左侧的指定长度的字符。
4. RIGHT()：返回字符串右侧的指定长度的字符。
5. LENGTH()：返回字符串的长度。
6. REPLACE()：替换字符串中的字符。
7. TRIM()：删除字符串开头或结尾的空格或指定字符。
8. LOWER()：将字符串转换为小写字母。
9. UPPER()：将字符串转换为大写字母。
10. CHAR_LENGTH()：返回字符串的字符数。
11. INSTR()：返回字符串中第一个匹配的子串的位置。
12. LOCATE()：返回字符串中第一个匹配的子串的位置。
13. REPEAT()：重复一个字符串指定的次数。
14. SUBSTRING_INDEX()：返回字符串中指定分隔符的第n个子串。
15. CONCAT_WS()：连接两个或多个字符串，并使用指定分隔符分隔它们。



### 按照分隔符划分【注意下标从 1 开始】

```sql
SELECT	
	SUBSTRING_INDEX(profile, ',', -1) gender,
	count(*) number
FROM
	user_submit
group by gender
```

1. SUBSTRING_INDEX(str,delim,count)
   - 字符串
   - 分割符
   - count
     - 正数：从左侧选取
     - 负数：从右侧选取

```sql
SELECT SUBSTRING_INDEX('My,Name,Is,John', ',', 1) AS part1,
       SUBSTRING_INDEX(SUBSTRING_INDEX('My,Name,Is,John', ',', 2), ',', -1) AS part2,	# 嵌套
       SUBSTRING_INDEX(SUBSTRING_INDEX('My,Name,Is,John', ',', 3), ',', -1) AS part3,
       SUBSTRING_INDEX(SUBSTRING_INDEX('My,Name,Is,John', ',', 4), ',', -1) AS part4;
```

```diff
+-------+-------+-------+-------+
| part1 | part2 | part3 | part4 |
+-------+-------+-------+-------+
| My    | Name  | Is    | John  |
+-------+-------+-------+-------+
```

2. REGEXP函数

   REGEXP函数可以按照指定的正则表达式将字符串拆分为多个子串。以下是一个例子：

   ```sql
   SELECT SUBSTRING('My,Name,Is,John', 1, REGEXP_INSTR('My,Name,Is,John', ',') - 1) AS part1,
          SUBSTRING('My,Name,Is,John', REGEXP_INSTR('My,Name,Is,John', ',') + 1, 
                    REGEXP_INSTR('My,Name,Is,John', ',', 1, 2) - REGEXP_INSTR('My,Name,Is,John', ',') - 1) AS part2,
          SUBSTRING('My,Name,Is,John', REGEXP_INSTR('My,Name,Is,John', ',', 1, 2) + 1, 
                    REGEXP_INSTR('My,Name,Is,John', ',', 1, 3) - REGEXP_INSTR('My,Name,Is,John', ',', 1, 2) - 1) AS part3,
          SUBSTRING('My,Name,Is,John', REGEXP_INSTR('My,Name,Is,John', ',', 1, 3) + 1) AS part4;
   ```

   ```diff
   +-------+-------+-------+-------+
   | part1 | part2 | part3 | part4 |
   +-------+-------+-------+-------+
   | My    | Name  | Is    | John  |
   +-------+-------+-------+-------+
   ```

## 31. 提取博客URL中的用户名

```sql
SELECT	
    device_id,
	SUBSTRING_INDEX(blog_url, '/', -1) user_name
FROM
	user_submit
```

## 32. 截取出年龄

```sql
SELECT	
    substring_index(SUBSTRING_INDEX(profile, ',', 3), ',', -1) age,
    count(*) number
FROM
	user_submit
group by age
```

## 33. 找出每个学校GPA 最低的同学

窗口函数

MySQL窗口函数是一种用于在查询结果中执行聚合和分析函数的函数。窗口函数基于一组行执行聚合，并根据指定的窗口规范（例如排名、分组、排序等）计算每个行的值。

以下是MySQL中常用的窗口函数：

1. ROW_NUMBER(): 返回按照指定的排序规则对行进行排序后的行号。
2. RANK(): 返回行的排名，相同值的行有相同的排名，排名相同的下一行将被跳过。
3. DENSE_RANK(): 与RANK()函数类似，但是如果两个行具有相同的值，则它们将被分配相同的排名。
4. LEAD(): 返回结果集中下一行的值。
5. LAG(): 返回结果集中上一行的值。
6. FIRST_VALUE(): 返回结果集中第一个行的值。
7. LAST_VALUE(): 返回结果集中最后一个行的值。
8. AVG(): 返回指定列的平均值。
9. SUM(): 返回指定列的总和。
10. COUNT(): 返回指定列的行数。
11. MAX(): 返回指定列的最大值。
12. MIN(): 返回指定列的最小值。

使用窗口函数可以轻松地计算行级别的聚合值，并在结果集中显示这些值，而无需在查询中使用GROUP BY子句。

```sql
select device_id,
university,
gpa
from
    (
        select
            *,
            rank() over (
                partition by
                    university
                order by
                    gpa
            ) as rn	# 等级
        from
            user_profile
    ) as univ_min
where
    rn = 1	# 筛选出等级为 1 的
order by
    university
```



## 34. 统计复旦用户8月练题情况

题目： 现在运营想要了解

- 复旦大学的每个用户
- 在8月份练习的总题目数和回答正确的题目数情况，请取出相应明细数据，
- 对于在8月份没有练习过的用户，答题数结果返回0.

```sql
select
    user_profile.device_id device_id,
    '复旦大学' university,
    count(result) question_cnt,
    count(if (result = 'right', 1, null)) right_question_cnt
from
    user_profile
    # 即使作答题目，也要保留学校的记录，所以使用左连接：left join
    left join question_practice_detail on user_profile.device_id = question_practice_detail.device_id
    and month (question_practice_detail.date) = 8	# on 后面起到筛选右表的作用！！！
where
    university = '复旦大学'
group by
    user_profile.device_id

```

## 35.  浙大不同难度题目的正确率

现在运营想要了解

- 浙江大学的用户
- 在不同难度题目下
- 答题的正确率情况

```sql
select
    difficult_level,
    (count(if(result='right', 1, null)) / count(result)) correct_rate
from
    user_profile
    join question_practice_detail using (device_id)
    join question_detail using(question_id)
where 
    university='浙江大学'
group by
    difficult_level
order by
    correct_rate asc
```



## 39. 21 年8月份练题总数

```sql
select 
    count(distinct device_id) did_cnt,
    count(question_id) question_cnt
from
    question_practice_detail
where month(`date`) = 8
```



## 78. 先后顺序出现

```sql
select
    prod_name,
    prod_desc
from    
    Products
where  prod_desc like '%toy%carrot%'
```

## 

大小写：

- lower
- upper

字符串截取

 substring(str, index, length) 函数

##  81. 顾客登录名

```sql
select
    cust_id,
    cust_name,
    upper(	# 后转成大写字母
        concat (	# 先拼接
            ((substring(cust_name, 1, 2))),
            ((substring(cust_city, 1, 3)))
        )
    ) user_login
from
    Customers
```



## 89. 计算总和

1. where后面不能直接加聚合函数（分组函数），我们也可以这样记忆：**where后面只能跟表中存在的字段。**
2. having 不能单独使用，必须跟 group by 联合使用。

```sql
select 
    order_num, 
    sum(item_price * quantity) total_price
from 
    OrderItems
group by
    order_num
having
    total_price >= 1000
```

## 93. 返回购买 prod_id 为 BR01 的产品的所有顾客的电子邮件【子查询】

```sql
select
    cust_email	# 3. 确定 email
from
    Customers
where
    cust_id in (
        select
            cust_id	# 2. 确定 cust_id
        from
            Orders
        where
            order_num in (
                select
                    order_num	# 1. 确定 order_num
                from
                    OrderItems
                where
                    prod_id = 'BR01'
            )
    )

```

## 94. 返回每个顾客不同订单的总金额

```sql
select
    cust_id,
    sum(item_price * quantity) total_ordered
from
    (
        select
            cust_id,
            Orders.order_num,
            item_price,
            quantity
        from
            Orders
            left join OrderItems on Orders.order_num = OrderItems.order_num
    ) alias
group by
    cust_id
order by
    total_ordered desc
```

## 97. 返回顾客名称和相关订单号以及每个订单的总价

```sql
select
    cust_name,
    Orders.order_num,
    (quantity * item_price) OrderTotal
from	# 三表连接
    Customers,
    Orders,
    OrderItems
where
    Customers.cust_id = Orders.cust_id
    and Orders.order_num = OrderItems.order_num
order by
    cust_name,
    Orders.order_num
```

## 100. 确定最佳顾客的另一种方式（二）

方式1：where

```sql
select
    cust_name,
    sum(item_price * quantity) total_price
from
    OrderItems,
    Orders,
    Customers
where
     OrderItems.order_num = Orders.order_num and
     Orders.cust_id = Customers.cust_id
group by
    cust_name
having
    total_price >= 1000
```

方式2：inner join

```sql
# inner join 使用
select
    cust_name,
    sum(item_price * quantity) total_price
from
    OrderItems
    inner join Orders on OrderItems.order_num = Orders.order_num
    inner join Customers on Orders.cust_id = Customers.cust_id
group by
    cust_name
having
    total_price >= 1000
```

## 101. 插入记录

```sql
insert into
    exam_record
values
    (
        null,
        1001,
        9001,
        "2021-09-01 22:11:12",
        "2021-09-01 23:01:12",
        90
    ),
    (
        null,
        1002,
        9002,
        "2021-09-04 07:01:02",
        null,
        null
    )
```

## 102. 将查询结果插入到表中

```sql
insert into
    exam_record_before_2021(id, uid, exam_id, start_time, submit_time, score) (
        select
            null,	# 自增字段传入 null 进去
            uid,
            exam_id,
            start_time,
            submit_time,
            score
        from
            exam_record
        where
            YEAR (submit_time) < '2021'
    )
```

## 103. 即使存在也要插入【insert into... values】

```sql
replace into
    examination_info values
    (null, 9003, 'SQL', 'hard', 90, '2021-01-01 00:00:00');
```

## 114. 更新记录【update...set...】

```sql
update  
    exam_record
set
    submit_time='2099-01-01 00:00:00',
    score=0
where 
    start_time<'2021-09-01 00:00:00' and
    isnull(submit_time)	# 判断字段是否为 null 【还可以写成：submit_time is null】
```

## 115. 删除记录【delete from..】

时间戳差值：

- TIMESTAMPDIFF (minute, start_time, submit_time) < 5	
- 后面 - 前面

```sql
delete from
    exam_record
where
    TIMESTAMPDIFF (minute, start_time, submit_time) < 5	
    and score < 60
```

## 116. 删除记录（二）

delete 是支持 limit 关键字的，但仅支持单个参数

```sql
# 执行顺序：from,where,order by,limit,delete
delete from
    exam_record
where
    submit_time is null or
    timestampdiff(minute, start_time, submit_time) < 5
order by
    start_time
limit 3
```

## 117. 删除所有记录，并重置自增主键

MySQL 允许各种方式来重置自动增量列值。这些方法是：

1. 使用 ALTER TABLE 语句
2. 使用 TRUNCATE TABLE 语句
3. 使用一对 DROP TABLE 和 CREATE TABLE 语句

方法1：

ALTER TABLE 语句用于更改表或任何表字段的名称。它还用于添加、删除或重置表中的现有列。MySQL 还允许此语句在我们需要时重置自动增量列值。

```sql
ALTER TABLE table_name AUTO_INCREMENT = value;  
```

需要注意的是，MySQL 不允许重置小于或等于已使用值的值。

- 如果我们使用 InnoDB，该值不应小于或等于自增列的当前最大值。

- 对于MyISAM，如果指定的值小于或等于 auto_increment 列的最大值，则该值将重置为当前最大值加一。

- 对于InnoDB，如果指定值小于 auto_increment 列的当前最大值，MySQL 不会发出任何错误，当前序列也不变。

方法2：

MySQL 中的 TRUNCATE TABLE 语句在不删除表结构的情况下完全删除表的数据，并且始终将自动增量列值重置为零。

```sql
truncate table exam_record
```

方法3：

我们还可以使用一对 DROP TABLE 和 CREATE TABLE 语句来重置自动增量列值。此方法从表中永久删除完整记录。

与 TRUNCATE TABLE 查询类似，这对 DROP TABLE 和 CREATE TABLE 语句首先删除表，然后重新创建它。因此，MySQL 将自动增量列值重置为零。

```sql
drop table if EXISTS exam_record;
CREATE TABLE IF NOT EXISTS exam_record (
id int PRIMARY KEY AUTO_INCREMENT COMMENT '自增ID',
uid int NOT NULL COMMENT '用户ID',
exam_id int NOT NULL COMMENT '试卷ID',
start_time datetime NOT NULL COMMENT '开始时间',
submit_time datetime COMMENT '提交时间',
score tinyint COMMENT '得分'
)CHARACTER SET utf8 COLLATE utf8_general_ci;
```

## 118. 创建一张新表【Create】

![image-20230313102215649](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303131022854.png)

```sql
# comment 注释
create table user_info_vip(
    id int(11) primary key auto_increment comment '自增ID',
    uid int(11) unique not null comment '用户ID', # not null
    nick_name varchar(64) comment '昵称',
    achievement int(11) default 0 comment '成就值',
    level int(11) comment '用户等级',
    job varchar(32) comment '职业方向',
    register_time datetime default CURRENT_TIMESTAMP comment '注册时间'
)default CHARSET=UTF8;
```

## 119. 修改表【alter】

- 增加列在某列之后
  - alter table 增加的表格 add 增加列的名称 数据类型 位置(after level 在level 之后)

- 更换列的名称及数据类型
  - alter table user_info change 原列名 修改列名 修改数据类型

- 更改数据类型
  - alter table 表名 modify 修改列名称 数据类型 默认值等

```sql
alter table
    user_info add school varchar(15) after level;   # 新增 add

alter table
    user_info change job profession varchar(10);    # 更换列的名称及数据类型 change

alter table
    user_info modify achievement int(11) default 0; #  数据类型 默认值 modify
```

## 120.  删除表（drop）

```sql
drop table if exists exam_record_2011;
drop table if exists exam_record_2012;
drop table if exists exam_record_2013;
drop table if exists exam_record_2014;
```

## 121. 创建索引

```sql
CREATE INDEX idx_duration ON examination_info(duration);
CREATE unique INDEX uniq_idx_exam_id on examination_info(exam_id);  # 唯一索引	unique index
CREATE fulltext INDEX full_idx_tag on examination_info(tag);    # 全文索引 fulltext index
```

查看索引：

```sql
# 查看索引
SHOW INDEX FROM examination_info
```

## 122. 删除索引

```sql
DROP INDEX uniq_idx_exam_id ON examination_info;
DROP INDEX full_idx_tag on examination_info;
```

## 123. 截断平均值

（去掉一个最大值和一个最小值后的平均值）。

```sql
select
    tag,
    difficulty,
    round(avg(score), 1)
from
    (
        select
            a2.exam_id, tag, a1.score, difficulty  # 2个表中都有 id，解决方式：要杀写啥！！！
        from
            exam_record a1
            inner join examination_info a2 on a1.exam_id = a2.exam_id
            and a2.tag = 'SQL'
            and a2.difficulty = 'hard'
            and score is not null
            and score != (select max(score) from exam_record)
            and score != (select min(score) from exam_record)
    ) as jk
```

```sql
select tag, difficulty,
    round((sum(score) - max(score) - min(score)) / (count(score) - 2), 1) as clip_avg_score
from exam_record
join examination_info using(exam_id)	# A join B using(字段) ---> 公共字段连接！！！
where tag="SQL" and difficulty="hard"
```

## 124. 统计做答次数

https://blog.csdn.net/weixin_31746149/article/details/113234175

比较运算符(=，!=，>，< 等)

- 成立时，返回 1
- 不成立时，返回 0

![image-20230313195159476](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303131951552.png)

```sql
select count(id=1) from actor # 5 个
```

```sql
select count(id=1 or null) from actor # 1个 
```

解读：id = 1 or null

- 当 id = 1 时，表达式等于：1 or null ===> 1
- 当 id = 2 时，表达式等于：0 or null ===> null 【弱类型】

count 有 2个作用：

- 统计行数【count（*）】
- 统计列值
  - COUNT这个函数其中传入的参数只要不是null, 都会造成最终结果+1

```sql
#  CASE WHEN 条件表达式函数实现
CASE WHEN SCORE = 'A' THEN '优'
     WHEN SCORE = 'B' THEN '良'
     WHEN SCORE = 'C' THEN '中' ELSE '不及格' END
```

```sql
select
    count(*) as total_pv,	# 行数
    count(score) as complete_pv,	# 有成绩的 ---> score 不为 null 的
    count(
        distinct case
            when score is null then null
            else exam_id	# 有分的 exam_id ---> 取 distinct
        end
    ) as complete_exam_cnt
from
    exam_record
```

## 125. 得分不小于平均分的最低分

```sql
select
    min(score) as min_score_over_avg
from
    exam_record
    join examination_info using (exam_id)
where
    tag = 'SQL' # 标签
    and score >= (  # 分数
        select
            avg(score)
        from
            exam_record
            join examination_info using (exam_id)
        where
            tag = 'SQL'
    )
```

## 126. 平均活跃天数和月活人数

1. 一个用户在一天不管提交多少个，只算1个
2. 统计不同用户在一天提交的



时间戳格式化输出

```sql
DATE_FORMAT(submit_time,'%Y%m')
# Y：完整年
# y：年份的后 2 位

# 自己的写法
replace (
        substring_index (substring_index (submit_time, ' ', 1), '-', 2),
        '-',
        ''
    )
```

- 本题陷阱在于九月份有个用户同一天做了两种卷子，直接count统计的话活跃天数会多一天，即用户ID和做题日期submit_time要**同时去重**才能得出正确的活跃天数.
- 为什么要对两个字段进行去重（为啥要加uid）count(distinct submit_time)
- 思路：如果不加uid
  - 那用户1、用户2，两人在同一天做了卷子
  - 这样submi_time只会被记录一次（因为用户1、2的日期一样）
  - 加了uid，是为了把不同的用户跟他的做题日期对应起来

```sql
select
    date_format (submit_time, '%Y%m') as month,
    round(
        (
            # 每个月用户的总活跃天数
	            # 1. 不同的日期
	            # 2. 不同的用户ID
            count(distinct uid, date_format (submit_time, '%y%m%d')) 
        ) / count(distinct uid),
        2
    ) as avg_active_days,
    count(distinct uid) as mau
from
    exam_record
where
    submit_time is not null
    and year (submit_time) = 2021
group by
    date_format (submit_time, '%Y%m')	# 按照 年月 分组 【一个月有 30 天】 
```



## 127. 月总刷题数和日均刷题数【any_value() ===> 解决 ONLY_FULL_GROUP_BY 问题！！！】

关于解决最新的SQL版本中 ONLY_FULL_GROUP_BY 报错的办法：

- ONLY_FULL_GROUP_BY的语义就是确定select 中的所有列的值要么是来自于聚合函数（sum、avg、max等）的结果，要么是来自于group by list中的表达式的值。MySQL提供了any_value()函数来抑制ONLY_FULL_GROUP_BY值被拒绝。
- 所以只需要在非group by的列上加any_value()就可以了

Mysql 聚合函数：

```sql
AVG()
SUM()
MAX()
MIN()
COUNT()
```

mysql last_day函数：取出某个月的最后一天

```sql
SELECT LAST_DAY('2019-04-01') 
```

![image-20230316113431980](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303161134043.png)

```sql
# round() 不是聚合函数
# 使用 any_value() 来破局
select
    date_format (submit_time, '%Y%m') submit_month,
    (count(question_id)) month_q_cnt,
    any_value (
        round(
            count(question_id) / day (LAST_DAY (submit_time)),
            3
        )
    ) avg_day_q_cnt
from
    practice_record
where
    date_format (submit_time, '%Y') = '2021'
group by
    submit_month
union all
select
    '2021汇总' as submit_month,
    count(question_id) month_q_cnt,
    round(count(id) / 31, 3) avg_day_q_cnt
from
    practice_record
where
    date_format (submit_time, '%Y') = '2021'
order by
    submit_month;

```



## 128. 未完成试卷数大于1的有效用户

| id   | uid  | exam_id | start_time          | submit_time         | score  |
| ---- | ---- | ------- | ------------------- | ------------------- | ------ |
| 1    | 1001 | 9001    | 2021-07-02 09:01:01 | 2021-07-02 09:21:01 | 80     |
| 2    | 1002 | 9001    | 2021-09-05 19:01:01 | 2021-09-05 19:40:01 | 81     |
| 3    | 1002 | 9002    | 2021-09-02 12:01:01 | (NULL)              | (NULL) |
| 4    | 1002 | 9003    | 2021-09-01 12:01:01 | (NULL)              | (NULL) |
| 5    | 1002 | 9001    | 2021-07-02 19:01:01 | 2021-07-02 19:30:01 | 82     |
| 6    | 1002 | 9002    | 2021-07-05 18:01:01 | 2021-07-05 18:59:02 | 90     |
| 7    | 1003 | 9002    | 2021-07-06 12:01:01 | (NULL)              | (NULL) |
| 8    | 1003 | 9003    | 2021-09-07 10:01:01 | 2021-09-07 10:31:01 | 86     |
| 9    | 1004 | 9003    | 2021-09-06 12:01:01 | (NULL)              | (NULL) |
| 10   | 1002 | 9003    | 2021-09-01 12:01:01 | 2021-09-01 12:31:01 | 81     |
| 11   | 1005 | 9001    | 2021-09-01 12:01:01 | 2021-09-01 12:31:01 | 88     |
| 12   | 1005 | 9002    | 2021-09-01 12:01:01 | 2021-09-01 12:31:01 | 88     |
| 13   | 1006 | 9002    | 2021-09-02 12:11:01 | 2021-09-02 12:31:01 | 89     |

日期 和 tag 用 : 连接

多元素间用 ; 连接

| uid  | incomplete_cnt | complete_cnt | detail                                                       |
| ---- | -------------- | ------------ | ------------------------------------------------------------ |
| 1002 | 2              | 4            | 2021-09-01:算法;2021-07-02:SQL;2021-09-02:SQL;2021-09-05:SQL;2021-07-05:SQL |

请统计2021年每个未完成试卷作答数大于1的有效用户的数据（有效用户指完成试卷作答数至少为1且未完成数小于5），输出

- 用户ID
- 未完成试卷作答数
- 完成试卷作答数
- 作答过的试卷tag集合
- 按未完成试卷数量由多到少排序。

detail中是1002作答过的试卷：

- {日期:tag}集合，日期和tag间用:连接，多元素间用;连接。

```sql
# 不能用 count(score) 来计算分数
# 极端情况：只有分数，没有提交时间！！！
select
    uid,
    sum(if(submit_time is null, 1, 0)) as incomplete_cnt,
    sum(if(submit_time is null, 0, 1)) as complete_cnt,
    group_concat(distinct concat_ws(':',date(start_time),tag) order by start_time separator ';') detail
from
    exam_record 
join
    examination_info
using(exam_id)
where year(start_time)=2021
group by uid
having  # having 中可以使用聚合函数来筛选【而 where 则不可以】
    complete_cnt >=1 and
    incomplete_cnt > 1 and
    incomplete_cnt < 5
order by
    incomplete_cnt desc
```

### concat(str1, str2, ...)

```sql
# 新建 奶茶 表
DROP TABLE IF EXISTS `milk_tea`;
CREATE TABLE `milk_tea`  (
  `prod_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `prod_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `net_w` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `pro_date` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `valid_month` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `in_price` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `sale_price` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;
 
INSERT INTO `milk_tea` VALUES ('1', '奶茶', '150g', '2018-09-11 00:00:00.000', '12', '10.8', '15');
INSERT INTO `milk_tea` VALUES ('2', '奶糖', '150g', '2019-05-13 00:00:00.000', '18', '12.3', '20');
INSERT INTO `milk_tea` VALUES ('3', '棒棒糖', '15g', '2019-04-29 00:00:00.000', '18', '2.1', '2.5');
INSERT INTO `milk_tea` VALUES ('4', '饼干', '200g', NULL, '12', '16.1', '23');
INSERT INTO `milk_tea` VALUES ('5', '薯片', '100g', '2018-08-27 00:00:00.000', '12', '9.3', '15');
INSERT INTO `milk_tea` VALUES ('6', '薯条', '100g', '2018-08-31 00:00:00.000', '12', '8.8', '15');
INSERT INTO `milk_tea` VALUES ('7', '火腿肠', '550g', '2019-02-04 00:00:00.000', '12', '15.5', NULL);
INSERT INTO `milk_tea` VALUES ('8', '方便面', '100g', '2018-12-09 00:00:00.000', '18', '3.6', '4');
```

![image-20230317102624425](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303171026828.png)

例题1：

```sql
# 简易拼接
SELECT *, CONCAT(prod_name, net_w) 产品信息 from milk_tea
```

![image-20230317103532689](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303171035769.png)

例题2：

```sql
# concat 中的参数可以有多个！！！
SELECT *, CONCAT(prod_name, net_w) 产品信息 from milk_tea
```

![image-20230317104558366](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303171045443.png)

例题3：

```sql
SELECT *, CONCAT(prod_name, '是', net_w, '是', sale_price) 产品信息 from milk_tea
```

![image-20230317104855774](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303171048845.png)

### concat_ws('拼接符', str1, str2, ...)

concat_ws【with separator】

```sql
SELECT CONCAT_WS(‘拼接符’,str1,str2,...) FROM [表名];

SELECT CONCAT_WS("_", "data", "frog", "study");
-- 结果是data_frog_study

SELECT CONCAT_WS("-", "SQL", "Tutorial", "is", "fun!")AS ConcatenatedString;
-- 结果是SQL-Tutorial-is-fun!
```

### date ：取出时间戳中 年月日

```sql
select date('2021-09-02 12:01:01')
# 结果为；2021-09-02
```

### group_concat

group_concat：将多行合并成一行

行转列运用的group_concat()函数，可以将多行拼接为列函数group_concat([DISTINCT] 要连接的字段 [Order BY ASC/DESC 排序字段] [Separator'分隔符'])，默认是 "," 分割

```sql
group_concat(distinct concat_ws(':',date(start_time),tag) order by start_time separator ';') detail
```

![image-20230317114940056](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303171149139.png)

group_concat

- distinct
- 要连接的字段
- order by
- separator

```sql
# 简易拼接！！！
SELECT GROUP_CONCAT(net_w SEPARATOR '|') FROM milk_tea;
```

## 129. 月均完成试卷数不小于3的用户爱作答的类别

```sql
select
    tag,
    count(tag) as tag_cnt
from
    exam_record
    join examination_info using (exam_id)
where
    uid in (
        select
            uid
        from
            exam_record
        where
            submit_time is not null
        group by
            uid
        having	# 统计当前用户完成试卷总数count(exam_id)；统计该用户有完成试卷的月份数count(distinct DATE_FORMAT(start_time, "%Y%m"))
            count(exam_id) / count(distinct DATE_FORMAT (start_time, "%Y%m")) >= 3
    )
group by
    tag
order by
    tag_cnt desc

```

## 130. 试卷发布当天作答人数和平均分

```sql
select
    exam_record.exam_id exam_id,
    count(distinct user_info.uid) uv,	# 统计答题人数：distinct
    round(avg(score), 1) avg_score
from
    user_info,
    examination_info,
    exam_record
where
    user_info.uid = exam_record.uid
    and exam_record.exam_id = examination_info.exam_id
    and tag='SQL'
    and level > 5 and date(submit_time) in (select date(release_time) from examination_info)	# 提交时间为准！！！
group by
    exam_record.exam_id
order by
    uv desc,
    avg_score asc
```

## 131. 作答试卷得分大于过80的人的用户等级分布【order by 有先后顺序】

```sql
select
    level,
    count(distinct t1.uid) level_cnt
from
    user_info t1,
    examination_info t2,
    exam_record t3
where
    t1.uid = t3.uid and
    t2.exam_id = t3.exam_id and
    tag='SQL' and
    score > 80
group by
    level
order by
    level_cnt desc,
    level desc	# 数据一致的时候，再按照 level 降序即可！！！
```

## 132.  每个题目和每份试卷被作答的人数和次数【合并查询】

<font color="yellow">order by出现在union的子句中会失效</font>，但是可以出现在子句的子句中，记住就行

请统计每个题目和每份试卷被作答的人数和次数，分别按照"试卷"和"题目"的 uv & pv 降序显示，示例数据结果输出如下：

- 先 exam_record 
- 后 practice_record 

| tid  | uv   | pv   |
| ---- | ---- | ---- |
| 9001 | 3    | 3    |
| 9002 | 1    | 3    |
| 8001 | 3    | 5    |
| 8002 | 2    | 2    |

```sql
# order by不能直接出现在union的子句中，但是可以出现在子句的子句中。所以在外面再套一层
select
    *
from
    (
        select
            exam_id tid,
            count(distinct uid) uv,
            count(uid) pv
        from
            exam_record
        group by
            exam_id
        order by
            uv desc,
            pv desc
    ) t1
union
select
    *
from
    (
        select
            question_id tid,
            count(distinct uid) uv,
            count(uid) pv
        from
            practice_record
        group by
            question_id
        order by
            uv desc,
            pv desc
    ) t2
```

## 133. 分别满足两个活动的人

假使以前我们有两拨运营活动：

- 给每次试卷得分都能到85分的人（activity1）
- 至少有一次用了一半时间就完成高难度试卷且分数大于80的人（activity2）发了福利券

```sql
(select 
    uid, if(uid is not null, 'activity1', null) activity
from
    examination_info join exam_record using(exam_id)
group by 
    uid
having
    min(score) >= 85) # 每次都能得到 85分以上！！！
union all
(select
     uid, if(uid is not null, 'activity2', null) activity
from
    examination_info join exam_record using(exam_id)
where uid in(
    select uid from exam_record
    where timestampdiff(minute, start_time, submit_time) < (duration / 2)) and
    score > 80 and
    difficulty='hard'
group by 
    uid
)
order by uid
```

简易写法：

```sql
(select uid,'activity1' as activity # 每次查询的都是定值 'activity1'
from exam_record er
where year(start_time)='2021'
group by uid
having min(score)>=85)
union ALL
(select uid,'activity2' as activity
from exam_record er left join examination_info ei on er.exam_id=ei.exam_id
where year(start_time)='2021' and ei.difficulty='hard' and score>=80 
and timestampdiff(second,er.start_time,er.submit_time)<= ei.duration*30
group by uid)
order by uid;
```

## 134.  满足条件的用户的试卷完成数和题目练习数【连接查询】

请你找到 

- 高难度 
- SQL试卷
- 得分 平均值大于80 
- 并且是7级 的红名大佬
- 统计他们的2021年试卷总完成次数和题目总练习次数，
- 只保留2021年有试卷完成记录的用户。
- 结果按试卷完成数升序，
- 按题目练习数降序。

## 135.



## 136. 每类试卷得分前 3 名【窗口函数排序】

窗口函数： 3种排序方式

1. rank() over() 1 2 2 4 4 6 (计数排名，跳过相同的几个，eg.没有3没有5)
2. row_number() over() 1 2 3 4 5 6 (赋予唯一排名)
3. dense_rank() over() 1 2 2 3 3 4 (不跳过排名，可以理解为对类别进行计数) 【dense：稠密】

| tid  | uid  | ranking |
| ---- | ---- | ------- |
| SQL  | 1003 | 1       |
| SQL  | 1004 | 2       |
| SQL  | 1002 | 3       |
| 算法 | 1005 | 1       |
| 算法 | 1006 | 2       |
| 算法 | 1003 | 3       |

本题坑点：

- 每类试卷得分的前3名，因此不包含一个用户独占一科前几名的情况，例如1003的SQL考试分别是89分和86分，只取分高的那条数据。本质就是对比某科目每个用户的最高分。
- 如果最高分一样，那就对比最低分:order by max(score) desc,min(score) desc,uid desc

```sql
SELECT tag,uid,ranking
FROM (
    SELECT b.tag,a.uid,
    ROW_NUMBER()OVER(PARTITION BY tag ORDER BY max(a.score) DESC,min(a.score) DESC,a.uid DESC) ranking
        /*先按照最高分，再按照最低分排，最后按照uid排序 */
    FROM exam_record a 
    LEFT JOIN examination_info b ON a.exam_id=b.exam_id
    GROUP BY b.tag,a.uid)t1 # 排名是以每个标签每个用户为组的
    
WHERE ranking<=3
```

## 137.  第二快/慢用时之差大于试卷时长一半的试卷



## 152. 注册当天就完成了试卷的名单第三页

```sql
select
    uid,
    level,
    register_time,
    max(score) max_score
from
    user_info join exam_record using(uid) join examination_info using(exam_id)
where
	#	注册当天就完成
    tag='算法' and job='算法' and Date_format(register_time, 'Y%m%d%')= Date_format(submit_time, 'Y%m%d%')
group by
    uid
order by   
    max_score desc
limit 6, 3 # 取第三页的3条，即偏移/跳过前6条，取三条：LIMIT 6,3
```

## 155. 大小写混乱时的筛选统计

```bash
/*结果：
算法|4
C++|6
c++|2
sql|1
SQL|0
*/
```

```bash
/*结果：
c++
sql
*/
```

```sql
select a.tag , cnt  
from (
    select tag
    from exam_record er
    join examination_info ei on er.exam_id=ei.exam_id
    group by ei.exam_id
    having count(start_time) <3
) a
join (
    select tag ,count(start_time) cnt
    from examination_info ei
    left join exam_record er on ei.exam_id=er.exam_id
    group by tag
) b 
on UPPER(a.tag)=b.tag 
where cnt != 0
and a.tag != upper(a.tag)   ##如果转换后tag并没有发生变化，不输出该条结果【要求 tag 是小写的】
```

