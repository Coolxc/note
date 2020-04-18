`craete table ѧ��(xh int primary key,xm varchar(8),id int,hm int);`

`craete table �γ�(id int primary key,'��Ŀ' varchar(10));  `

## ��ѯ

```mysql
select distinct  xm  from ѧ��;                #��ѯѧ�����н�xm�ֶε�ͬѧ ȥ���ظ� distinct�����أ�

select *from ѧ�� where id between 2 and 4;             #��ѯѧ������id ��2��4֮�������

select * from ѧ�� where  id in(2,3,4)  order by xh desc;           #��ѯѧ������id Ϊ 2 3 4 ���������ݽ���

select * from ѧ�� where  id=3 or id=4;                 #��ѯѧ������id Ϊ3����Ϊ4������

select count��*�� from ѧ�� where id=3;                #��ѯѧ������idΪ3�����������ݣ�

select *from ѧ�� where id=(select max(id) from ѧ��);    #  ��ѯѧ������id���ֵ����Ϣ

select *from ѧ�� order by id desc limit 0,2;     #��ѯѧ������id�����ǰ��������

select  avg(id) from   ѧ��  ;#��ѯѧ������id��ƽ��ֵ

select id,avg(id) from ѧ�� group by id;#��ѯѧ������ÿ��id��ƽ��ֵ

select xh,��Ŀ from ѧ��,�γ� where ѧ��.id=�γ�.id;#��ѯѧ�����γ̱��е������Ϳ�Ŀ������id��
```



## ����

**���ź���Ŀ��ٲ������ݽṹ������һ�ָ�Ч��ȡ�������ݽṹ�� ����߲������ݵ�Ч�ʣ�**

```mysql
insert into ѧ�� values(66,��Ժ,3,1232323232);

#inner jion   left jion  right join  full jion  union ��ϰ     ������ϰ

show index from ѧ��;  #�鿴����ѧ���������

select * from ѧ��  left join �γ� on ѧ��.id=�γ�.id;      #�鿴ѧ����Ϳγ̱�������ӣ��޶�Ӧ��ʾ��

select * from ѧ��  right join �γ� on ѧ��.id=�γ�.id;      #�鿴ѧ����Ϳγ̱�������ӣ��޶�Ӧ��ʾ��

select * from ѧ��  full join  on ѧ��.id=�γ�.id;           #�鿴ѧ����Ϳγ̱�������ӣ��޶�Ӧ��ʾ��

select * from ѧ��  left join �γ� on ѧ��.id=�γ�.id union select * from ѧ��  right join �γ� on ѧ��.id=�γ�.id;   #�鿴�������Ӳ�ȥ�ظ�

select * from ѧ��  left join �γ� on ѧ��.id=�γ�.id left join ���� on �γ�.id=����.id; #�鿴3�����е�������
```





## ��������

  ```mysql
create index  xh on ѧ��(���ֶΡ������ֶΡ� );#�ڱ���һ����Ϊindex_name�����������ֶΣ�    

alter table 'ѧ��' add index yh(id);

show index from ѧ��;#��ѯ�Ѹ����е�����

drop index xh on ѧ��;#ɾ��ѧ�����е�xh����
  ```



####  ����ʧ�ܵ�ԭ��

> 1��ȫְƥ�����
>
>  2�������ǰ׺����
>
>  3�����������������κβ���
>
> 4���洢���治��ʹ�������з�Χ�����ұߵ���
>
> 5������ʹ�ø�������������*
>
> 6��mysql ��ʹ�ò����ڣ�=����<>ʱ���޷�ʹ������
>
> 7��is null     is not null �޷�ʹ������
>
> 8��like%д�ұ�
>
> 9���ַ������ӵ���������ʧ��
>
> 10��ɨ��or �������ӻᵼ������ʧ��
>
> 11����A��С��B�����ݵ�ʱ�� �� exists ����in



## ����

#### ����ĸ�����

> **Aԭ���ԣ�Atomicity����**���������µĵ�λ�� �����Էָ      
>
> **Bһ���ԣ�Consistent����**����Ҫ��ͬһ�¼��б��뱣֤һ��s
>
> **C�����ԣ�isolation����**����1 ������2֮����и����ԡ�
>
> **D�־��ԣ�Durable����**����һ��������commit��rollback������ʹ�ûع��� 
>
>  
>
> **read umcommitted  ��δ�ύ��                            **
>
> **read committed  ���Ѿ��ύ��**           
>
> **repeatable read        �����ظ���                             **
>
> **serializable     ���л�������������ͬʱ���� ��һ�������ڽ���ʱ����һ������ȴ�ִ�У�**



```mysql
select @@global.transaction_isolation;#�鿴����ĸ����ԣ�select @@transaction_isolation;��mysql8.0

select @@global.tx_isolation;#�鿴����ĸ����ԣ�select @@global.tx_isolation;��mysq5.x

set global  transaction  isolation level repeatable read ;#�޸ĸ��뼶��Ϊrepeatable read

select  @@autocommit;#�鿴���ݵ��ύ����

set autocommit=0 # 0�����ֶ��ύ ��������

commit��#�ֶ��ύ

rollback��#������һ����䣬���ݻع�

begin;#��������start transaction4

update ��Ǯ set id=id+100 where xm=��3;# ��Ǯ������3��id�ֶ��������-100
```

##  ��ʾ��

```mysql
lock table ѧ�� read;#A��ѧ����Ӷ���,����ֻ���Զ�ѧ���������������޷�������Bֻ���Զ�ѧ����                  �������ݶ��������Բ���������

lock table ѧ�� write;#A��ѧ�������д��,����ֻ���Զ���дѧ���������Բ���������B���Բ����������� ���Զ���дѧ����

unlock table;#�ر�������

show open tables;#�鿴����� 1:�� 0������

show status like'table%';#immediate��ʾ���Ĵ�����waited��ʾ���ĵȴ����������߱�ʾ�������ľ������
```



## ����

```mysql
begin��

select  * from ѧ��   xh=21 where id=5 for update; #����ѧ�����е�ID=5���� �����������˲���
```



## explain  sql�������Ż�

> **type��**system>const>ep_ref> ref (���)> range �����٣�>index>all�������Ż���
>
> **extra:** using temporary(������ʱ�������Ż�)
>
> > using filesort (����û���õ��������ļ������������Ż�)
>>
> > using index  ������������

?       

**�Ż�SQL���Ĳ��裺**

> 1���۲�������һ�죬������������SQL�����
>
> 2����������ѯ��־�������÷�ֵ�������糬��5���Ӿ�����SQL��������ץסȡ����������
>
> 3��explain + ��SQL ����
>
> 4��show profile
>
> 5����ά����or DBA ���з������������š�



**�ܽ᣺** 

> ����־��ѯ�Ŀ�������
>
> explain +��SQL����
>
> show profile ��ѯsql��mysql��������ִ��ϸ�ں������������
>
> SQL ���ݿ�������Ĳ������š�

?       

#### ����־��ѯ��

```mysql
show variables like '%slow_query_log%'; #�鿴����־����״̬

set global slow_query_log=1;#��������־��¼ֻ�Ե�ǰ��Ч

?      ��mysqld��	show_query_log=1 �����ÿ�����

?                               slow_query_log_file=����״̬�ϲ鿴�ļ���ַ(   show variables like '%slow_query_log%'; )

show variables like 'long_query_time%';#�鿴����־�� ֵ���������ʱ������¼

set global long_query_time=3; #����3���SQL�������¼Ϊ��SQL

cat �ļ�����_slow.log;

show global status like'%slow_queries%';#�鿴����־���ƶ�����   

mysqldumpslow -s c -t c:\program files\mysql-8.0.18-winx64\data\pceysj-slow.log
show profiles ;#�鿴���15����Ϣ

show variables like 'profiling'; �鿴show profile ״̬

 set profiling=on;  #����profile

 show profile ALL for query 38;              #�鿴idΪ38��sql cpu ��block io �����й켣
```



 1��`converting heap to myisam;` # ��ѯ���̫���ڴ涼�������������ϰ�

2�� `creating tmp table;              `    #������ʱ����������ʱ��������ɾ��

3�� `copying to tmp table on disk;` #���ڴ��е���ʱ���Ƶ�����Σ��

4��` locked              `                         ��������5���ʾSQL�����Ż�

**������**ALL ��ʾ���еĿ�����Ϣ

**����**   

> block io           ��ʾio��ؿ���
>
> cpu                   ��ʾcpu��ؿ���
>
> ipc                     ��ʾ���ͺͽ�����ؿ���
>
> memorg            ��ʾ�ڴ���ؿ���
>
> pagefaults          ��ʾҳ�������ؿ���
>
> source                   ��ʾsource function��file��fi le��line���
>
> swaps                   ��ʾ����������ؿ� 



## �����ű�

������������籨��functio ��ô��fun����һ������

```mysql
show variables like'log_bin_trust_function_creators';�鿴functio�Ƿ����

set global  log_bin_trust_function_creators=1 ��Чһ��

[mysqld] log_bin_trust_function  ������Ч
```



## Լ��

```mysql
create table ����(xh int auto_increment,id primary key,xm not null,xb default nan,sh int unique��);

alter table ���� drop primary key;ɾ��ѧ���������

alter table ���� add primary key(id);#�����ʱ��ֶ�ID �������

create table r(id int ,xm int, constraint WJ foreignkey (id) references e(id));#R���id ����E���ID��ֵ�����WJ

alter table ѧ�� add  constraint WJ  foreign(id) references �γ�(id);ѧ���������һ�������ΪWJ��IDȡֵ�γ̱��ID��

alter table �γ� drop foreign key WJ;ɾ���γ̱��е����WJ
```

## �޸�

�ֶ���Ϣ�޸ģ�

```mysql
alter table ��Ǯ change xm je int;#�޸Ľ�Ǯ��xm�ֶ���Ϊje��int����

alter table ��Ǯ rename to ����; #�ѽ�Ǯ������ָ�Ϊ����

alter table ��Ǯ add  kh int primary key; #��ӽ�Ǯ�����Ѹ��ֶ�Ϊkh

alter table  ��Ǯ modify  xm int; # �޸Ľ�Ǯ���е�xm�ֶε�����Ϊint

�ֶ������޸ģ�

update ���� set je=je+200 where id=1;#���б��е�je�ֶμ�200 ����id=1
```

## ����



> desc ��    �ɲ鿴��ϸ����ֶ�
>
> delimitep ���ţ� �ɸı��������
>
> primary key; ����
>
> set character_set_�ֶ� _=utf-8 �޸�������ʽ
>
> drop ɾ��
>
> create ����
>
> mysql_secare_installation;��ȫ�������
>
> \h ����
>
> default Ĭ�� 







