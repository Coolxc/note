**apt安装的软件目录如下**

> 系统安装软件一般在/usr/share
> 可执行的文件在/usr/bin
> 配置文件可能安装到了/etc下
> lib文件 /usr/lib
> 保存系统或者某个应用程序运行过程中的状态信息  /var/lib

配置文件：` /etc/redis/redis.conf`

可执行文件：`/usr/bin/redis-***`

日志文件：`/var/lib/redis`

持久化文件：`dir ./`，也就是在哪个目录下开启的redis就会在哪里生成，可修改配置文件指定路径

`config set requirepass "111"`来设置密码。设置完后打任何命令前都需要输入`auth 111`来验证身份，但是重启服务后无效。永久设置需要在配置文件中设置。

指定配置文件启动Redis：`redis-server redis.cnf`

**配置文件中的maxmemory**

> 设置Redis可以使用的内存量，一旦内存使用达到上限，Redis将会试图移除内部数据，移除策略由maxmemory-policy来指定。如果移除策略设定了不允许移除，那么一些`set、lpush`指令将会报错
>
> **Maxmemory-policy 内存策略**
>
> > volatile-lru：使用LRU（Least Recently Used）算法移除KEY，只对设置了过期时间的键有效
> >
> > allkeys-lru：对所有key都有效
> >
> > volatile-random：在过期集合中随机的删除key（过期的键并不会立即删除，只是get不了），只对设置了过期时间的key有效
> >
> > volatile-ttl：移除那些TTL（Time to live）最小的key，即过期时间剩余最短的key
> >
> > noeviction：不进行移除，针对写操作只是返回错误信息
>
> **Maxmemory-samples 设置样本大小**
>
> > 设置样本数量，因为LRU和TTL算法并非完全精确，所以可以设置一个样本值来让他们从中选出一个最适合被删除的。一般设置3到7，样本数量越小越不准确，但性能消耗相对越小，所以也不能设置太高。

## 持久化

##### RDB（Redis DataBase）

> 在指定的时间间隔内将内存中的数据集快照 (Snapshot)写入磁盘，它恢复时是将快照文件写入到内存
>
> 在使用`BGSAVE`命令或达到持久化条件时，Redis会单独fork一个子进程来进行持久化，子进程会将内存中的数据以RDB的格式保存到磁盘中，在这期间Redis服务仍然可以处理来自客户端的请求
>
> 整个过程中，主进程是不进行任何IO操作的，这就确保了极高的性能。如果需要进行大规模数据的恢复，且对于数据恢复的完整性不是特别敏感，那么RDB方式要比AOF方式更加高效。
>
> **Fork**
>
> > 通过fork生成的父子进程会共享包括内存空间的资源
> >
> > fork并不会带来明显的性能开销，尤其是对内存进行大量的拷贝，它能通过 写时拷贝 将拷贝工作推迟到真正需要的时候
> >
> > fork后的父子进程会运行在不同的内存空间中，当fork发生时两者的内存空间有着完全相同的内容，对内存的写入和修改、文件的映射都是独立的，两个进程不会相互影响
> >
> > 关键在于父子进程的内存在fork时是完全相同的，在fork之后进行写入和修改也不会相互影响。这也完美解决的如何生成快照，父进程在fork后只管修改自己的，子进程保留有fork时的内容。
> >
> > **Copy on write**
> >
> > > 必须实现在fork时子进程不会对父进程进行全量拷贝，如果全量拷贝那么在大量数据下将会是灾难性的，**写时拷贝**完美的解决了这一问题
> > >
> > > 写时拷贝将拷贝推迟到写操作真正发生时，awesome
> > >
> > > > 在fork函数调用时，父进程与子进程分配到不同的虚拟内存空间中，所以在俩个进程来看他们访问的是不同的内存。事实上他们共享了同一块物理内存
> > > >
> > > > > 在真正访问虚拟内存空间时，内核会将虚拟内存映射到物理内存上，所以父子进程可以共享同一块物理内存
> > > > >
> > > > > 当父进程或子进程对共享的内存进行修改时，共享的内存才会以页为单位进行拷贝，父进程会保留原有的物理空间，而子进程会使用拷贝后的新物理空间
> > > > >
> > > > > 以此，在没有进行写操作时子进程完全不需要再复制一份内存空间直接持久化到磁盘。并且Redis本身就是一个读请求远大于写操作的数据库，所以这种机制效率贼高。
>
> **.rdb**
>
> > RDB快照保存的是dump.rdb文件
> >
> > 保存策略
> >
> > ```python
> > ################################ SNAPSHOTTING  ################################
> > #
> > # Save the DB on disk:
> > #
> > #   save <seconds> <changes>
> > #
> > #   Will save the DB if both the given number of seconds and the given
> > #   number of write operations against the DB occurred.
> > #
> > #   In the example below the behaviour will be to save:
> > #   after 900 sec (15 min) if at least 1 key changed
> > #   after 300 sec (5 min) if at least 10 keys changed
> > #   after 60 sec if at least 10000 keys changed
> > #
> > #   Note: you can disable saving completely by commenting out all "save" lines.
> > #
> > #   It is also possible to remove all the previously configured save
> > #   points by adding a save directive with a single empty string argument
> > #   like in the following example:
> > #
> > #   save ""
> > 
> > save 900 1
> > save 300 10 #五分钟内修改10次数据，那么在五分钟时会生成dump.rdb文件
> > save 60 10000
> > ```
> >
> > **从dump.rcb恢复环境**
> >
> > > 根据上面的策略，比如`save 300 10`，达到修改条件后redis会每隔5分钟自动生成rdb文件，也可以自己修改条件值
> > >
> > > 启动redis时会自动加载安装目录下的dump.rdb文件
> > >
> > > 但是当输入flushall时redis会马上生成新的dump.rdb文件，此时的rdb文件无疑是空的。。。
> > >
> > > 所以我们备份的dump.rdb文件应该复制一份MyDump.rdb到其他地方，在误清库后再将复制的那一份MyDump.rdb复制回来并改为dump.rdb成为真正想要的备份文件~
> >
> > **RDB手动持久化：**只要在redis命令行输入`save`   即可就会生成rdb文件。但是这样做在save过程中中redis是阻塞的，专注于save。。所以还有一个**bgsave**，redis会异步的保存快照，主进程依然处理请求。当正常关机shotdown时也会自动生成.rdb文件。
>
> **优势**
>
> > 适合大规模的数据恢复
> >
> > 对数据的完整性和一致性要求不是太高
>
> **劣势**
>
> > 备份时间间隔中redis意外挂了的话，就会丢失自上一次备份到挂掉这段时间的修改
> >
> > RDB的缺点是最后一次持久化后的数据可能丢失

#### AOF（Append Only File）

> 不记录状态而是记录命令。弥补了RDB会丢失最后一次修改的缺陷
>
> 以日志的形式来记录每一个写操作，将Redis执行过的所有写指令记录下来。只追加文件但不可以改文件，redis启动之初会读取改文件重新构建数据，换言之，redis重启的话就根据日志文件的内容将写指令从前到后执行一次以完成数据的恢复工作
>
> redis启动时自动加载redis目录下的appendonly.aof文件
>
> AOF默认是关：`appendonly no`
>
> 默默人.aof文件名
>
> > ```python
> > # The name of the append only file (default: "appendonly.aof"); 
> > appendfilename "appendonly.aof"
> > ```
> >
> > 
>
> 需要注意的是如果在redis中输入了flushall那么也会记录在.aof文件中。。
>
> 开启aof后.rdb和.aof可以同时存在，但会加载.aof。
>
> > ```python
> > # AOF and RDB persistence can be enabled at the same time without problems.
> > # If the AOF is enabled on startup Redis will load the AOF, that is the file
> > # with the better durability guarantees.
> > ```
>
> **appendfsync**
>
> > Always：同步持久化，每次发生数据变更立即同步记录到磁盘，性能较差但数据完整性比较好
> >
> > Everysec：默认推荐值，异步操作，每秒记录一次，如果一秒内发生宕机就会发生数据丢失
> >
> > No：关闭同步
> >
> > ```python
> > # The fsync() call tells the Operating System to actually write data on disk
> > # instead of waiting for more data in the output buffer. Some OS will really flush
> > # data on disk, some other OS will just try to do it ASAP.
> > #
> > # Redis supports three different modes:
> > #
> > # no: don't fsync, just let the OS flush the data when it wants. Faster.
> > # always: fsync after every write to the append only log. Slow, Safest.
> > # everysec: fsync only one time every second. Compromise.
> > #
> > # The default is "everysec", as that's usually the right compromise between
> > # speed and data safety. It's up to you to understand if you can relax this to
> > # "no" that will let the operating system flush the output buffer when
> > # it wants, for better performances (but if you can live with the idea of
> > # some data loss consider the default persistence mode that's snapshotting),
> > # or on the contrary, use "always" that's very slow but a bit safer than
> > # everysec.
> > #
> > # More details please check the following article:
> > # http://antirez.com/post/redis-persistence-demystified.html
> > #
> > # If unsure, use "everysec".
> > 
> > # appendfsync always
> > appendfsync everysec
> > # appendfsync no
> > ```
>
> **Rewrite**
>
> > 由于AOF文件采用的是追加的形式，所以AOF文件很容易过大。所以新增了重写机制，当AOF文件的大小超过阙值时，Redis就会启动AOF文件的荣荣压缩，只保留可以恢复数据的最小指令集，可以使用命令`bgrewriteaof`
> >
> > **原理**
> >
> > > AOF文件增长超过阙值时，会Fork出一条新进程来将文件重写（也是先写临时文件最后再rename），遍历新进程内存中的数据，每条记录有一条set语句。重写aof文件的操作，并没有读取旧的aof文件，而是将整个内存中的数据库内容用命令的方式重写了一个新的aof文件，这点和快照有点类似
> > >
> > > Redis会记录上一次重写时的AOF大小，默认配置是当AOF文件大小是上一次rewrite后大小的一倍且文件大于64M时触发
> > >
> > > ```python
> > > no-appendfsync-on-rewrite no
> > > 
> > > # Automatic rewrite of the append only file.
> > > # Redis is able to automatically rewrite the log file implicitly calling
> > > # BGREWRITEAOF when the AOF log size grows by the specified percentage.
> > > #
> > > # This is how it works: Redis remembers the size of the AOF file after the
> > > # latest rewrite (if no rewrite has happened since the restart, the size of
> > > # the AOF at startup is used).
> > > #
> > > # This base size is compared to the current size. If the current size is
> > > # bigger than the specified percentage, the rewrite is triggered. Also
> > > # you need to specify a minimal size for the AOF file to be rewritten, this
> > > # is useful to avoid rewriting the AOF file even if the percentage increase
> > > # is reached but it is still pretty small.
> > > #
> > > # Specify a percentage of zero in order to disable the automatic AOF
> > > # rewrite feature.
> > > 
> > > auto-aof-rewrite-percentage 100
> > > auto-aof-rewrite-min-size 64mb
> > > ```
>
> **劣势**
>
> > aof同步数据户集所产生的.aof文件要远大于rdb文件，恢复速度也慢
> >
> > aof运行效率要慢于rdb，每秒同步策略效率稍好，不同步效率和rdb相同

**aof或rdb文件内容出错那么可以用redis的`redis-check-aof`、`redis-check-dump`来修复，它会将有错误语法的语句都删除**

#### 总结

1. RDB持久化方式能够在指定的时间间隔对数据进行快照存储，重启时会加载.rdb文件，flushall和shotdown时也会生成新的dump.rdb文件
2. AOF持久化记录了每次对服务器的写操作，当服务器重启时会重新执行这些命令。会存储filushall
3. Redis能Fork一个子进程对AOF文件进行重写压缩
4. 如果只是做缓存，那么可以不用任何持久化方式
5. 如果同时开启了两种持久化方式，会加载AOF文件，因为通常AOF更加能保证数据的完整性

## RDB的写时拷贝

如果子进程需要对父进程的内存进行全量的拷贝，对于Redis服务来说基本是灾难性的。

**写时拷贝（Copy-on-Write）**的出现就是为了解决这一问题，写时拷贝的主要作用就是将拷贝推迟到写操作真正发生时，这也就避免了大量无意义的拷贝操作。

在fork函数调用时，父进程和子进程会被Kernel分配到不同的虚拟内存空间中，所以在两个进程看来它们访问的是不同的内存：

> 在真正访问虚拟内存空间时，Kernel会将虚拟机内存映射到物理内存上，所以父子进程共享了物理上的内存空间
>
> 当父进程或子进程对共享的内存进行修改时，共享的内存才会以页为单位进行拷贝，父进程会保留原有的物理空间，而子进程会使用拷贝后的新物理空间

在Redis服务中，子进程只会读取共享内存中的数据，它并不会执行任何写操作，只有父进程在写入时才会触发这一机制，而对于大多数的Redis服务或者数据库，写请求往往都是远小于读请求的，所以使用fork加上写时拷贝机制能够带来非常好的性能，也让BGSAVE这一操作的实现变得非常简单。