关系型数据库只有行和列来描述关系，复杂的关系要用到外键等，关系越复杂就越麻烦。Nosql可以很好处理很复杂的关系数据。

**NoSQL(Not Only SQL)，**意即 不仅仅是SQL。泛指非关系型数据库

#### NoSQL特点

> 易扩展：数据之间没有关系，非常容易扩展属性，不用考虑动一发而牵全身
>
> 由于数据库结构简单，对于大数据也是高性能
>
> 多样灵活的数据模型：无需事先建立字段
>
> 键值对的存储
>
> 最终一致性，而不是ACID

**NoSQL数据库的四大分类**

> KV键值对：Redis
>
> 文档型数据库：MongoDB
>
> 列存储数据库：Cassandra,  HBase,  分布式文件系统
>
> 图数据库：用来存放关系，如社交关系，广告推荐系统：Neo4j, InfoGrid
>
> ![image-20200409204942949](\图片\image-20200409204942949.png)

### Redis：Remote Dictionary Server（远程字典服务器）

**Redis的特点**

> Redis支持数据的持久化，可以将内存中的数据保存在磁盘中，重启后可以再加载进内存
>
> Redis不仅仅支持简单的key-value类型的数据，还支持list, set, zset, hash等数据结构的存储
>
> Redis支持数据的备份，即master-slave模式的数据备份

**基本知识**

> 默认16个数据库，下标从0开始
>
> select 命令切换数据库
>
> dbsize 查看当前数据库的key数量
>
> filushdb  清除当前库所有信息
>
> flushall   清楚所有库的所有信息
>
> 统一密码管理，所有的16个库都是同一个密码
>
> 默认端口  6379
>
> Redis索引都是从零开始

#### Redis数据类型

> **String**
>
> > string是redis最基本的类型，一个key对应一个value。
> >
> > 并且string是二进制安全的，意思就是可以存储任何数据，包括图片或序列化的对象。
> >
> > redis中的字符串value最多可以是512M
>
> **Hash**
>
> > 是一个键值对集合
> >
> > 是一个string类型的field和value的映射表
> >
> > 特别适合存储对象
>
> **List**
>
> > 是一种简单的字符串列表，按照插入顺序排序。你可以添加一个元素到列表的头部或尾部。
> >
> > 他的底层实际是个链表
>
> **set**
>
> > 是string类型的无序集合，它是通过HashTable实现的
>
> **Zset**
>
> > 有序集合，不同于set的是Zset会在每个元素前添加一个double类型的分数。redis正是通过这个分数来为集合中的成员进行从小到大的排序。
> >
> > Zset的成员是唯一的，但分数(score)却是可以重复的

### 数据类型操作命令

> #### key
>
> > **exists keyName：**判断是否存在这个key
> >
> > **move key db号：**将key移到指定库
> >
> > **expire key 时间（秒）：**指定过期时间
> >
> > **ttl keyName：**(Time to live) 查看还有多久过期，-1表示永不过期，-2表示已过期
> >
> > **type key：**查看key是什么类型
> >
> > **del key：**删除key
>
> #### String
>
> > **set/get/del/append/strlen：**append向指定key对应的value后添加字符串
> >
> > **incr keyName：**每次递增；incrby keyName num，每次增加num个数
> >
> > **decr keyName：**每次递减；decrby keyName num，每次减少num个数
> >
> > **getrange/setrange：**获取/设置指定区间内的值
> >
> > **setex(set with expire)键时间：**setex k1 10 v1（设置k1的过期时间为10秒）。
> >
> > **setnx(set if not exists)：**setnx k1 v3：如果这时k1已经存在，这个v3不会覆盖原来的v1。
> >
> > **mset/mget/msetnx：**可以同时设置/取多个值。mset k1 v1 k2 v2... mget k1 k2 ...
>
> #### List   类似于双端队列
>
> > **lpush/rpush/lrange：**列表不能用get set
> >
> > ```java
> > //lpush每次都会从左边插入
> > 127.0.0.1:6379> lpush list01 1 2 3 4
> > (integer) 4
> > 127.0.0.1:6379> lrange list01 0 -1  //0到-1会输出所有元素
> > 1) "4"
> > 2) "3"
> > 3) "2"
> > 4) "1"
> > //rpush每次都会从右边插入
> > ```
> >
> > **lpop/rpop：**从左边或右边弹出一个
> >
> > **lindex：**按照索引下标获取元素
> >
> > **llen：**列表长度
> >
> > **lrem key n value：**删除n个value  `lrem list01 2 a`：在列表中删除两个a，如果a的数量少于2则有几个删几个
> >
> > **ltrim key start stop：**截取指定范围的值后再赋值给key，执行后key这个list只剩截取后的元素
> >
> > **rpoplpush 源列表 目的列表：**rpop源列表的元素再lpush到目的列表
> >
> > **lset list01 1 xx：**从左数，设置list01数组中下标为1的值为xx
> >
> > **linsert keyName before/after xx**
>
> #### set  单key多value
>
> > **sadd keyName values...**：向集合添加元素，重复的元素只会添加进去一个
> >
> > **smembers keyName：**显示所有元素
> >
> > **scard：**获取集合里有多少个元素
> >
> > **srem keyName value：**删除指定集合内的指定元素
> >
> > **srandmember keyName num：**随机出num个整数
> >
> > **spop keyName：**随机出栈一个元素
> >
> > **smove key1 key2 key1中的某个值：**将key1里的某个值移给集合key2
> >
> > 差集：sdiff set01 set02：set01独有的
> >
> > 交集：sinter set01 set02：set01与set02共有的
> >
> > 并集：sunion set01 set02：并他个喜羊羊
>
> #### Hash
>
> > kv模式不变，但是v是一个单独的键值对。所以k就像是指向该Hash的变量
> >
> > 下面的keyName都是kv中的k，而后面的key是v中的key
> >
> > `keyName : {key1:v1, key2:v2}`
> >
> > **hset/hget/hmset/hmget/hgetall/hdel**
> >
> > ```java
> > user为kv中的k，name是kv中v中的key
> > 127.0.0.1:6379> hset user name zzz
> > (integer) 1
> > 127.0.0.1:6379> hget user name
> > "zzz"
> > 127.0.0.1:6379> hmset user name yyy email 333
> > OK
> > 127.0.0.1:6379> hmget user name email
> > 1) "yyy"
> > 2) "333"
> > 127.0.0.1:6379> hgetall user
> > 1) "id"
> > 2) "11"
> > 3) "email"
> > 4) "333"
> > 5) "name"
> > 6) "yyy"
> > > 127.0.0.1:6379> hdel user name 
> > (integer) 1
> > ```
> >
> > **hexists keyName key里面的某个值**
> >
> > **hkeys/hvals：显示所有键/值**
> >
> > ```java
> > 127.0.0.1:6379> hkeys user
> > 1) "id"
> > 2) "email"
> > 127.0.0.1:6379> hvals user
> > 1) "11"
> > 2) "333"
> > ```
> >
> > **hincrby keyName val中的key num：**增加某一个key的值，增量为num
> >
> > **hincrbyfloat keyName val中的key num：**增加某一个key的值，增量为num
> >
> > **hsetnx keyName key value：**如果key不存在则复制，存在则什么也不做
>
> #### Zset（有序集合）
>
> > 在set的基础上，添加了一个score的值
> >
> > set是 `k1 v1 v2 v3`，而zset是`k1 score1 v1 score2 v2`
> >
> > **zadd/zrange**
> >
> > > ```java
> > > 127.0.0.1:6379> zadd z 5 yy 7 kk
> > > (integer) 2
> > > 127.0.0.1:6379> zrange z 0 -1
> > > 1) "yy"
> > > 2) "kk"
> > > ```
> >
> > **zrangebyscore key start stop：**只输出start到stop之间的score所指的值
> >
> > > "("号表示开区间，如`zrangebyscore z 6 (10`表示`[6, 10)`
> > >
> > > limit start num：后面再加一个limit表示从从开始截取num个记录
> >
> > **zrem keyName val值：**删除key中的value值
> >
> > **zcard/zcount key score区间/zrank kkey values值：**获取下标值/ zscore key 对应值，获得分数