

# 牛客网 Mysql【入门】

- [牛客网 Mysql【入门】](#----mysql----)
  * [3. 去重【distinct】](#3----distinct-)
  * [8. 区间【between and】](#8----between-and-)
  * [13. 符合条件【in】](#13------in-)
  * [14. 操作符混合运用](#14--------)
  * [15. 名字带有北京的](#15--------)
  * [18. 分组【group by】](#18----group-by-)
  * [19. 分组过滤 【having】](#19-------having-)
  * [20. 分组排序【order by】](#20------order-by-)
  * [22. 统计每个学校的答过题的用户的平均答题数](#22--------------------)
  * [23.  **统计每个学校各难度的用户平均刷题数**](#23-----------------------)
  * [24. **统计每个用户的平均刷题数**](#24-----------------)
  * [25. **查找山东大学或者性别为男生的信息**](#25---------------------)
  * [26. **计算25岁以上和以下的用户数量**](#26-----25-------------)
  * [27. **查看不同年龄段的用户明细**](#27-----------------)
  * [28. **计算用户8月每天的练题数量**【Date】](#28-------8-----------date-)
  * [29.  **计算用户的平均次日留存率**【DATEDIFF】](#29-------------------datediff-)
  * [30. **统计每种性别的人数**](#30--------------)

<small><i><a href='http://ecotrust-canada.github.io/markdown-toc/'>Table of contents generated with markdown-toc</a></i></small>

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

<font color="yellow">精度</font>：保留4位小数round(x, 4)

```sql
round(count(qpd.question_id) / count(distinct qpd.device_id), 4) as avg_answer_cnt
```

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

## 23.  **统计每个学校各难度的用户平均刷题数**

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

## 24. **统计每个用户的平均刷题数**

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

## 25. **查找山东大学或者性别为男生的信息**

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

## 26. **计算25岁以上和以下的用户数量**

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

## 27. **查看不同年龄段的用户明细**

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

## 28. **计算用户8月每天的练题数量**【Date】

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

## 29.  **计算用户的平均次日留存率**【DATEDIFF】

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

## 30. **统计每种性别的人数**

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

