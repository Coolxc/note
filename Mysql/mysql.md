`craete table 学生(xh int primary key,xm varchar(8),id int,hm int);`

`craete table 课程(id int primary key,'科目' varchar(10));  `

## 查询

```mysql
select distinct  xm  from 学生;                #查询学生表中叫xm字段的同学 去掉重复 distinct（排重）

select *from 学生 where id between 2 and 4;             #查询学生表中id 在2和4之间的数据

select * from 学生 where  id in(2,3,4)  order by xh desc;           #查询学生表中id 为 2 3 4 的所有数据降序

select * from 学生 where  id=3 or id=4;                 #查询学生表中id 为3或者为4的数据

select count（*） from 学生 where id=3;                #查询学生表中id为3共多少条数据；

select *from 学生 where id=(select max(id) from 学生);    #  查询学生表中id最大值的信息

select *from 学生 order by id desc limit 0,2;     #查询学生表中id降序的前两条数据

select  avg(id) from   学生  ;#查询学生表中id的平均值

select id,avg(id) from 学生 group by id;#查询学生表中每个id的平均值

select xh,科目 from 学生,课程 where 学生.id=课程.id;#查询学生表，课程表中的姓名和科目条件是id；
```



## 索引

**（排好序的快速查找数据结构，“是一种高效获取数据数据结构” ，提高查找数据的效率）**

```mysql
insert into 学生 values(66,张院,3,1232323232);

#inner jion   left jion  right join  full jion  union 练习     索引练习

show index from 学生;  #查看来自学生表的索引

select * from 学生  left join 课程 on 学生.id=课程.id;      #查看学生表和课程表的左连接，无对应显示空

select * from 学生  right join 课程 on 学生.id=课程.id;      #查看学生表和课程表的右连接，无对应显示空

select * from 学生  full join  on 学生.id=课程.id;           #查看学生表和课程表的右连接，无对应显示空

select * from 学生  left join 课程 on 学生.id=课程.id union select * from 学生  right join 课程 on 学生.id=课程.id;   #查看左右连接并去重复

select * from 学生  left join 课程 on 学生.id=课程.id left join 银行 on 课程.id=银行.id; #查看3个表中的左连接
```





## 创建索引

  ```mysql
create index  xh on 学生(“字段”，“字段” );#在表创建一个名为index_name的索引定义字段；    

alter table '学生' add index yh(id);

show index from 学生;#查询已各表中的索引

drop index xh on 学生;#删除学生表中的xh索引
  ```



####  索引失败的原因

> 1：全职匹配我最爱
>
>  2：最佳左前缀法则
>
>  3：不在索引列上做任何操作
>
> 4：存储引擎不能使用索引中范围条件右边的列
>
> 5：尽量使用覆盖索引，不用*
>
> 6：mysql 在使用不等于！=或者<>时候无法使用索引
>
> 7：is null     is not null 无法使用索引
>
> 8：like%写右边
>
> 9：字符串不加单引号索引失败
>
> 10：扫用or 用他连接会导致索引失败
>
> 11：当A表小于B表数据的时候 用 exists 优于in



## 事务

#### 事务的隔离性

> **A原子性（Atomicity）：**事务是最下的单位， 不可以分割。      
>
> **B一致性（Consistent）：**事务要求，同一事件中必须保证一致s
>
> **C隔离性（isolation）：**事务1 和事务2之间具有隔离性。
>
> **D持久性（Durable）：**事务一旦结束（commit，rollback）不可使用回滚。 
>
>  
>
> **read umcommitted  读未提交的                            **
>
> **read committed  读已经提交的**           
>
> **repeatable read        可以重复读                             **
>
> **serializable     串行化（两个事务不能同时进行 当一个事务在进行时，另一个事务等待执行）**



```mysql
select @@global.transaction_isolation;#查看事务的隔离性（select @@transaction_isolation;）mysql8.0

select @@global.tx_isolation;#查看事务的隔离性（select @@global.tx_isolation;）mysq5.x

set global  transaction  isolation level repeatable read ;#修改隔离级别为repeatable read

select  @@autocommit;#查看数据的提交属性

set autocommit=0 # 0等于手动提交 开启事务

commit；#手动提交

rollback；#撤销上一条语句，数据回滚

begin;#开启事务start transaction4

update 金钱 set id=id+100 where xm=张3;# 金钱表中张3的id字段里的内容-100
```

##  表示锁

```mysql
lock table 学生 read;#A给学生表加读锁,本人只可以读学生表，库里其他表无法操作，B只可以读学生表，                  插入数据堵塞，可以操作其他表

lock table 学生 write;#A给学生表加入写锁,本人只可以读、写学生表，不可以操作其他表，B可以操作其他表，不 可以读、写学生表

unlock table;#关闭所有锁

show open tables;#查看锁情况 1:锁 0：无锁

show status like'table%';#immediate表示锁的次数，waited表示锁的等待次数，过高表示严重锁的竞争情况
```



## 行锁

```mysql
begin；

select  * from 学生   xh=21 where id=5 for update; #锁定学生表中的ID=5的行 不允许其他人操作
```



## explain  sql语句分析优化

> **type：**system>const>ep_ref> ref (最好)> range （至少）>index>all（必须优化）
>
> **extra:** using temporary(创建临时表，必须优化)
>
> > using filesort (排序没有用到索引，文件类型排序须优化)
>>
> > using index  （覆盖索引）

?       

**优化SQL语句的步骤：**

> 1：观察至少跑一天，看看生产的慢SQL情况。
>
> 2：开启慢查询日志，‘设置阀值’，比如超过5秒钟就是慢SQL，并把它抓住取出来分析。
>
> 3：explain + 慢SQL 分析
>
> 4：show profile
>
> 5：运维经理or DBA 进行服务器参数调优。



**总结：** 

> 慢日志查询的开启捕获
>
> explain +慢SQL分析
>
> show profile 查询sql在mysql服务器的执行细节和生命周期情况
>
> SQL 数据库服务器的参数调优。

?       

#### 慢日志查询：

```mysql
show variables like '%slow_query_log%'; #查看慢日志运行状态

set global slow_query_log=1;#开启慢日志记录只对当前有效

?      【mysqld】	show_query_log=1 （永久开启）

?                               slow_query_log_file=运行状态上查看文件地址(   show variables like '%slow_query_log%'; )

show variables like 'long_query_time%';#查看慢日志阀 值，超过多次时间做记录

set global long_query_time=3; #超过3秒的SQL语句做记录为慢SQL

cat 文件名字_slow.log;

show global status like'%slow_queries%';#查看慢日志共计多少条   

mysqldumpslow -s c -t c:\program files\mysql-8.0.18-winx64\data\pceysj-slow.log
show profiles ;#查看最近15条信息

show variables like 'profiling'; 查看show profile 状态

 set profiling=on;  #开启profile

 show profile ALL for query 38;              #查看id为38的sql cpu 及block io 的运行轨迹
```



 1、`converting heap to myisam;` # 查询结果太大，内存都不够用往磁盘上搬

2、 `creating tmp table;              `    #创建临时表：拷贝到临时表，用完再删除

3、 `copying to tmp table on disk;` #把内存中的临时表复制到网盘危险

4、` locked              `                         出现以上5点表示SQL必须优化

**参数：**ALL 显示所有的开销信息

**常用**   

> block io           显示io相关开销
>
> cpu                   显示cpu相关开销
>
> ipc                     显示发送和接收相关开销
>
> memorg            显示内存相关开销
>
> pagefaults          显示页面错误相关开销
>
> source                   显示source function，file，fi le，line相关
>
> swaps                   显示交换次数相关开 



## 批量脚本

插入大量数据如报错：functio 那么给fun给定一个参数

```mysql
show variables like'log_bin_trust_function_creators';查看functio是否给定

set global  log_bin_trust_function_creators=1 生效一次

[mysqld] log_bin_trust_function  永久生效
```



## 约束

```mysql
create table 测试(xh int auto_increment,id primary key,xm not null,xb default nan,sh int unique，);

alter table 单词 drop primary key;删除学生表的主键

alter table 单词 add primary key(id);#给单词表字段ID 添加主键

create table r(id int ,xm int, constraint WJ foreignkey (id) references e(id));#R表的id 来着E表的ID数值外键名WJ

alter table 学生 add  constraint WJ  foreign(id) references 课程(id);学生表中添加一个外键名为WJ，ID取值课程表的ID；

alter table 课程 drop foreign key WJ;删除课程表中的外键WJ
```

## 修改

字段信息修改：

```mysql
alter table 金钱 change xm je int;#修改金钱表xm字段名为je是int类型

alter table 金钱 rename to 银行; #把金钱表的名字改为银行

alter table 金钱 add  kh int primary key; #添加金钱表中已各字段为kh

alter table  金钱 modify  xm int; # 修改金钱表中的xm字段的类型为int

字段内容修改：

update 银行 set je=je+200 where id=1;#银行表中的je字段加200 条件id=1
```

## 单词



> desc 表；    可查看详细表的字段
>
> delimitep 符号； 可改变结束符号
>
> primary key; 主键
>
> set character_set_字段 _=utf-8 修改字体样式
>
> drop 删除
>
> create 创建
>
> mysql_secare_installation;安全检测设置
>
> \h 帮助
>
> default 默认 







