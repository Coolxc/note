进程的寻址空间：进程的指针能够涵盖的范围。

32位：2的32次方，也就是4G

64位：2的64次方，这个数字是天文数字

这个寻址空间与实际的物理内存是没有关系的。如果是32位操作系统那么每个进程最大能分配的就是4G内存。当物理内存是8G时可用内存只有4G。但64位操作系统的寻址空间是一个天文数字。。。

## 数据类型

`1111`二进制转十进制时可以直接：用`10000 - 1 = 2^4 - 1`

**primitive类型（int,double...）** a = b 的比较都是比较值 

**Object类型（大写字母开头的Integer.....）** a =b 比较都是比较的是否为同一个对象，所以如果我们有两个`Array`，虽然这两个`Array`内容都是相等的但是在Java中使用等号运算符就认为他们是不相等的。

所以我们一般的都用`a.equals(b)`这个对象内部的比较函数来进行比较，但是如果a是空那么将返回`NullPOintException`异常，也可以用`Object.equals(a,b)`来进行比较（这个不会抛异常）。

## Boxing and Unboxing

> > `Integer a = 2; //Boxing`
> >
> > `Integer b = new Integer(2); //Boxing`
> >
> > `int v = a.intValue(); //Unboxing`



```java
1. new Integer(2) == 2;  //true
2. new Integer(2) == new Integer(2); //false
3. Integer.valueOf(2) == Integer.valueOf(2); //true
4. Integer.valueOf(2).intValue() == 2; //true
5. new Integer(2).equals(new Integer(2)); //true
```

1. 对象与`primitive`类型变量进行等号运算判断时Java会自动拆箱，所以为true
2. 两个不同的箱子，显然是false
3. `valueOf()`方法由`java`提供，缓存有-128-127的整数，超过这个范围将会返回false
4. `intValue()`方法将会拆箱，然后判断值是否相等，显然为`true`
5. `equals()`方法将会自动拆箱判断值是否相等，显然为 true

##### 自动装箱、拆箱与遍历循环

**代码清单1：**

```java
public static void main(String[] args) {
	List<Integer> list = Arrays.asList(1, 2, 3, 4);
	int sum = 0;
	for (int i : list) {
		sum += i;
	}
	System.out.println(sum);
}
```

**代码清单2：**

自动装箱、拆箱与遍历循环编译之后

```java
public static void main(String[] args) {
	List list = Arrays.asList( new Integer[] {
	Integer.valueOf(1), //int装箱为Integer
	Integer.valueOf(2),
	Integer.valueOf(3),
	Integer.valueOf(4) });
	int sum = 0;
	for (Iterator localIterator = list.iterator(); localIterator.hasNext(); ) {
	int i = ((Integer)localIterator.next()).intValue();
	sum += i;
	}
	System.out.println(sum);
}
```

代码清单1中一共包含了泛型、自动装箱、自动拆箱、遍历循环与变长参数5种语法糖，代码清单2则展示了它们在编译前后发生的变化。

泛型擦除转换成了裸类型。自动装箱、拆箱在编译之后被转化成了对应的包装和还原方法，如本例中的`Integer.valueOf()`与`Integer.intValue()`方法，

遍历循环则是把代码还原成了迭代器的实现，这也是为何遍历循环需要被遍历的类实现`Iterable`接口的原因。

最后再看看变长参数，它在调用的时候变成了一个数组类型的参数，在变长参数出现之前，程序员的确也就是使用数组来完成类似功能的。



## 数学归纳法与递归

<img src="图片\image-20200401143145905.png" alt="image-20200401143145905" style="zoom:80%;" />

假设`n=1`成立，那么如果我们假设`n=n-1`成立的情况下计算得出`n=n`也成立，那么我们可以说结论成立。将归纳法翻译成程序：

```java
int sum(int n){
    if (n == 1) return 1;
    return sum(n-1) + n;
}
//这里我们知道n=1时肯定是正确的，那么我们假设n-1也正确，并将n = n写成n = (n-1) + n返回。
```



#### 递归书写方法

> > 严格定义递归函数作用，包括参数、返回值、Side-effect
> >
> > 先考虑一般性，在考虑特殊情况
> >
> > 每次调用必须缩小问题规模
> >
> > 每次问题规模缩小成都必须为1



```java
//反转链表
public Node reverseLinkedList(Node head){
    //size == 0 or size == 1
    if (head == null || head.getNext() == null){ //递归结束条件
        return head;
    }
    
    //假设后面的都能成功，只看当前一个元素
    Node newHead = reverseLinkedList(head.getNext());
    head.getNext().setNext(head);
    head.setNext(null);
    return newHead;
}
```



我们只需考虑问题规模每次减少1的情况。假设`n-1`个规模的数据我们能够正确的反转完成，然后将反转完的数据头部指向当前头节点，头节点再指向空则完成反转。一定要考虑特殊情况防止程序死循环，当head为null或head的下一个为空时此时的`n-1`已经是0或1了，所以直接返回head。

# 面向对象

### 从用户（使用者）的方向思考问题。



#### 不可变性

> **不可变对象（Immutable Objects）**
>
> > 可以引用传递，可以缓存
> >
> > 线程安全

#### Java中使用final关键字标识不可变性

> **final关键字**
>
> > 声明在类上    =》   类不可以被继承
> >
> > 声明在函数上     =》 函数不可以在派生类中重写
> >
> > 声明在变量上     =》 变量不可以指向其他对象

其中对于变量的final：当变量是primitive类型时那么他的值是无法改变的。如果该变量指向的是一个对象，那么他只是不可再指向其他对象，该对象的属性还是可以进行修改的。并且被final修饰的变量一定要初始化，不然就没有机会再赋值给他了。

一般的我们尽量的都要将属性变为`private final`。使属性不可变。当要改变对象的属性值时可以new一个新的对象，再将之前的引用指向新的对象。

<img src="图片\image-20200401185306545.png" alt="image-20200401185306545" style="zoom:80%;" />

面向对象的语言中实例只是保存对象属性

<img src="图片\image-20200401185358206.png" alt="image-20200401185358206" style="zoom:80%;" />

在系统中只有类有方法代码，类的实例只是存有他自己的属性值。调用方法时将会调用同一份方法。那么怎么区分是哪个实例调用的`getPaid()`呢，不同的实例调用的`getPaid()`所收到的薪水都是不一样的。所以就有了`this`关键字，`this`和`python`中的`self`一样都是指向的当前实例对象，在我们运行`getPaid()`这样的方法时系统会隐式的在变量前添加上`thid.`来区分实例。这样在主体类上就能知道是哪个实例调用我的方法了。在构造函数中，一个构造函数可以通过`this`来调用另一个构造函数。

<img src="图片\image-20200401190034628.png" alt="image-20200401190034628" style="zoom:80%;" />

所以在类中我们可以不显式的写`this`也不会报错。当实例调用类方法时Java会隐式的在每个属性前加`this.`



#### 类的静态变量、静态函数

> > 没有this引用，静态变量全局唯一 一份
> >
> > 普通函数可以引用静态变量、函数；静态函数不可以使用非静态变量
> >
> > 对象上引用静态变量、函数？   编译器会警告
> >
> > 静态函数引用普通成员变量、函数？  会编译错误

<img src="图片\image-20200402102016655.png" alt="image-20200402102016655" style="zoom:80%;" />

为什么要将变量设为`private`并且用`Getter`和`Setter`方法：

> 变量设为私有的是为了封装，不让别人任意访问。但是有时候该变量还会有Setter方法，这是因为：
>
> 如果 `private String name`变成了`private String firstName`和`private String lastName`。那么这时候所有使用这个类的用户都要将`xx.name`改为`a = xx.firstName; b = xx.lastName; name = a+b`。而如果使用了Getter那么只需该这个类的Getter改为`return firstName + lastName`。其他调用该类的方法还是`name = xx.getName()`。

# 泛型

<img src="图片\image-20200402160105498.png" alt="image-20200402160105498" style="zoom:80%;" />

第一层的List提供了一个接口，他是一种逻辑结构：线性表

第二层的`ArrarList`和`LinkedList`是线性表的两种实现方式。第一种是数组，数组支持按照索引直接访问元素，他的增加和删除元素效率都比较慢，如删除后还要将后面的元素逐个向前移。而链表虽然访问某一个元素比较慢，但是他删除和新增元素比较快，首尾一接就行。

这张图是我们假象的，最下面一层是不正确的后面要修改。这里我们也能看出，一二层是结构上的继承，而第三层是关于数据类型的。这两种不同维度的东西`List`想要涵盖到所有数据类型无疑是要做乘法，那样得到的结构将会是非常大的。。。所以有了泛型的存在。



#### 从`List`到`List<T>`

> > 规定了List中的元素只能是类型T
> >
> > `ArrayList<Integer>`，`ArrayList<String>`
> >
> > `LinkedList<Integer>`，`LinkedList<String>`

在静态函数中，需要先声明泛型的存在`public static <T> T func(){}`。该方法返回类型为T的值。

```java
//语法
List<Integer> list = new ArrayList<>(); //java1.7以后

List<Integer> list = LinkedList.of(1,2,3);

List<Integer> list = LinkedList.newEmptyList();
```



需要注意的是`new ArrayList<>`这个尖括号中最然不用写具体的泛型了，但是这个尖括号是不能省略的。在Java中有尖括号和没有尖括号是两种完全不同的东西。没有尖括号那么他就不是泛型，这个数组元素类型默认是Object，那么他可以装任何东西。

```java
//泛型使用语法
Objects.equals(emptyIntList, LinkedList.<Integer>newEmptyList());

class ArrayList<T>{...}

public <V> void printList(List<V> list){...} //参数中有泛型应该先在方法中声明
```



#### Java Type Erasure (类型擦除)

> > 早期的Java是没有泛型的
> >
> > 所以为了兼容性，在编译时`JVM`会将所有的泛型擦除转换成相应的代码



#### Covariance

> > `ArrayList<Integer>`是`List<Integer>`吗？    //True
> >
> > `List<Integer>`是`List<Object>`吗？   //false
>
> 如果`List<Integer>`是`List<Object>`
>
> > `List<Object> obList = intList;`
> >
> > `obList.set(0, "hello");`
> >
> > `Integer firstElement = intList.get(0);` //此时取出的是`String`，所以会报错。
> >
> > 这就是为什么`List<Integer>`不是`List<Object>`
>
> 怎么将`List<Integer>`转换成`List<Object>`呢
>
> > `new ArrayList<Object>(intList);`  //OK
> >
> > `(List<Object>)(List)intList;`  //危险。
> >
> > 我们在转换时一定要想好为什么要转换。能不转就不转

# 关系型数据库隔离级别

**`read uncommitted`：**未提交读。一个事务可以读另一个没有提交的事务修改的值。如A事务修改`account`从100到90但并没有提交事务，事务B此时可以读到修改后的90，事务B将`account`加20也就是`90+20=110`。但是如果事务A选择了回滚事务那么事务B操作的`account`的初始值就是错误的。这就是脏读。`read uncommitted`隔离级别以上的都可以避免脏读。

**`read committed`：**提交读。相对于上一个未提交读，这个解决了上面的问题。事务B只可以读到事务A提交后的数据。也就是不同的会话在未提交事务时都是相互隔离的。（有种JVM线程从共享主存拿数据再写回的感觉，这种方式可能会造成线程间可见性问题。）这个问题也同样的出现在数据库上，如事务A读到`account`为100并加20为120然后不进行提交，此时事务B也读到`account`为100并减10为90.。。也就是事务B读步到最新的值。这样看反而可能`read uncommitted`好像更好。。。幸好有了一个更牛逼的隔离级别`repeatable read`

**`repeatable read`：**解决了`read committed`的问题（并发时可能出现数据混乱）。一个会话开始时它所读到的数据都是固定不变的，当修改数据时会从数据库读到最新的已提交数据然后进行操作。 可重复读相对于提交读主要是改进了对数据修改时的操作，`repeatable read`在对数据进行修改时就会重新读数据库中最新的数据。这种操作类似于`Volatile`关键字，保证了每次操作的都是最新的数据。

**幻读：**如事务A需要更新现有的一共三条信息的`account`为100，此时事务B插入了一条数据并设置了`account`为999并提交了。此时事务A才执行更新，那么一共收到影响的行数就是4行了。。。刚插入的那一条数据的`account`也成了100 。本来事务A只想更新三条，现在莫名的更新了4条。。。这就是幻读。通过设置隔离级别为`serializable`就可以避免幻读，新增数据时就会被阻塞等待读事务提交后才能插入数据。`repeatable read`好像也可以避免幻读。

隔离级别越高，并发性能越低。

# 设计模式

### 一般的我们都要用合理的设计模式来将继承关系变为组合关系

### Singleton pattern

#### Singleton的优缺点

> > 确保全局至多只有一个对象
> >
> > 用于：构造缓慢的对象；需要统一管理的资源
> >
> > 缺点：很多全局状态，线程安全性



#### Singleton的创建

> > 双重锁模式 Double checked locking
> >
> > 作为Java类的静态变量
> >
> > 使用框架提供的能力

**双重锁：**创建一个变量存放唯一的实例对象，初始值为null。使用时在get实例时先检查变量是否为null，如果为null那么就要初始化实例对象。这时需要上锁，然后创建，但是上的过程中也可能有别的人在初始化实例对象，所以在创建完锁之后还要再检查一次变量是否为Null。

**静态变量：**再Java中，静态变量是全局的。所以通过静态变量的方式可以实现单例模式。但是这种方式需要在程序初始化的时候就创建它，如果在运行时创建那么将会回到双重锁的问题上。但是使用单例模式一般的原因是对象构造缓慢，所以这种方法是解决不了单例模式所要解决的问题的。

**使用框架的能力：**我们应该使用框架来实现单例。如DI(Dependency inject)：Spring的依赖注入。Spring可以在它的容器中保存有唯一的一份对象。

### Role Pattern



#### 先来回顾一下`is-a`和`has-a`

> he is a people
>
> he has a head
>
> 我是一个男人：is-a
>
> 我有一个女人：has-a
>
> is-a用于继承；  has-a在于组合
>
> 一般我们认为继承可以分为两种基本的形式：实现继承和接口继承



#### 继承关系

>描述`is-a`关系
>
>不要用继承关系来实现复用
>
>使用设计模式来实现复用



**不要滥用继承：**如果有：`public class Manager extends Employee`  。经理`is a `员工，并且添加了一个员工列表的属性，记录了他可以管理的一组员工。但是当我们`new Employee("mary")`后，`mary`升职成了经理。这时候怎么将`Employee`变成`Manager`呢，很明显无法变，manager有他的特有方法。这是我们可以重新`new`一个`mary`：`new Manger("mary")`，这么做是可行的。但是显然不是优雅的。。。

**这时候就有了 state pattern。也叫角色模式（role pattern）**

<img src="图片\image-20200402224013358.png" alt="image-20200402224013358" style="zoom:80%;" />

放弃使用继承，而使用设计模式来区分经理与员工。在`Emploeyee`中添加一个role属性，根据role的不同执行不同的`doWork()`方法。

我们新建一个Role接口，该接口有一个`doWork()`方法。我们的`Engineer`和`Manager`都实现这个接口然后重写`doWork()`方法，当然他们俩的`doWork()`是不一样的。在`Employee`员工类中有一个role属性来指定员工的职位。当我们想提升某个员工的职位时只需`employee_mary.setRole(new Manager())`即可，当然我们在员工类中要统一行为：`public void doWork(){role.doWork()}` 。

这样就将继承变为了组合的方式，不同的行为继承同一接口进行不同的实现。然后在员工类中设置role属性来区分行为。这种设计模式也叫做 `Role Object Pattern`。

### Decorator Pattern

如果我们有一个任务类，现在需要一`LoggingRunnable`和`TransactionRunnable`类来打印日志和事务管理。日志类中有一个`run`来修饰`realRun`也就是要执行的任务。事务类中也有这两个方法。一般的我们如果只是想要一个功能那么直接继承日志类然后复写`realRun`然后调用父类的`run`即可打印日志。但是现在需要两个功能。而在Java中是不允许继承两个父类的，况且就算能继承，那么子类调用`run`时Java怎么知道调用的是打印日志的run还是事务管理的run。并且这样做系统的耦合度是非常高的。

那么就引出了装饰模式。任务类只是做他的任务，与其他类没有任何耦合。

<img src="图片\image-20200403112251189.png" alt="image-20200403112251189" style="zoom:80%;" />

```java
//装饰模式下的日志类，解开了与真正的任务类的耦合。
public class LoggingRunnable implements Runnable{
    private final Runnable innerRunnable; //类中的变量一般都要修饰成 private final
    
    public LoggingRunnable(Runnable innerRunnable){ //构造函数
        this.innerRunnable = innerRunnable; 
    }
    
    @Override
    public void run(){
        long startTime = System.currentTimeMillis();
        System.out.println("Task started at" + startTime);
        innerRunnable.run(); //调用真正的任务
        System.out.println("Task finished. Elapsed time: "
               + (System.currentTimeMillis() - startTime));
    }
}


//装饰模式下的事务类，解开了与真正的任务类的耦合。
public class LoggingRunnable implements Runnable{
    private final Runnable innerRunnable; //类中的变量一般都要修饰成 private final
    
    public LoggingRunnable(Runnable innerRunnable){ //构造函数
        this.innerRunnable = innerRunnable; 
    }
    
    @Override
    public void run(){
        boolean shouldRollBack = false;
        try{
            System.out.println("start transaction");
            innerRunnable.run();
        }catch(Exception e){
            shouldRollBack = true;
            throw e;
        }finally{
            if (shouldRollBack == true){
                rollBack();
            }else{
                commit();
            }
        }
    }
}
```



现在只要`LoggingRunnable`的run执行他构造函数传过来的`innerRunnable`就OK了。并且`TransactionRunnable`也是同样的解决办法。现在我们的任务类只是管做任务，没有任何的耦合。

```java
//运行任务  并且加上日志和事务控制
new TransactionRunnable(  //事务控制  传入的参数为 LoggingRunnable
	new LoggingRunnable(  //日志控制  传入的参数为任务
    	new MyTaskRunnable())
).run()  //调用TransactionRunnable的run即可一层一层执行下去
```



### 关于创建对象的设计模式

<img src="图片\image-20200403151247155.png" alt="image-20200403151247155" style="zoom:80%;" />

<img src="图片\image-20200403151426682.png" alt="image-20200403151426682" style="zoom:80%;" />

<img src="图片\image-20200403151725246.png" alt="image-20200403151725246" style="zoom:80%;" />

# 高级知识点

### 并行计算

理想的并行计算可以任意添加多个节点提高性能。可扩展性强。

#### 并行计算的方法

> > 将数据拆分到每个节点上   =》 如何拆分数据才能更高效
> >
> > 每个节点并行的计算出结果  =》 每个节点要计算什么结果
> >
> > 将结果汇总  =》   如何将零散的结果汇总

### 死锁分析

```java
void transfer(Account from, Account to, int amount){
    synchronized(from){
        synchronized(to){
            from.setAmount(from.getAmount() - amount);
            to.setAmount(to.getAmount() + amount);
        }
    }
}
```

面对一段需要考虑加锁的代码，我们需要注意

> > 在任何地方都可以进行线程切换，甚至在一句语句中间
> >
> > 要尽力设想对自己最不利的情况

**上面一段程序可能发生死锁：**`tansfer1(a, b, 100);  transfer2(b, a, 100);`

`transfer1`的a在from处拿到锁，`transfer2`的b在from处拿到锁。因为synchronized语句锁的是对象，所以`transfer1`的a在等他的to也就是`transfer2`的b释放锁；而`transfer2`的b在等他的to也就是`transfer1`的a释放锁。

#### 死锁条件，必须同时满足

> > 互斥等待
> >
> > hold and wait
> >
> > 循环等待
> >
> > 无法剥夺的等待

#### 死锁防止

> > 破除互斥等待  =》 一般无法破除
> >
> > 破除 hold and wait  =》  一次性获取所有资源
> >
> > 破除循环等待  =》  按顺序获取资源
> >
> > 破除无法剥夺的等待  =》  加入超时，超时后释放锁

### 线程池

#### 线程池

> > 创建线程开销大
> >
> > 线程池：预先建立好线程，等待任务派发

<img src="图片\image-20200403173518210.png" alt="image-20200403173518210" style="zoom:80%;" />

#### 线程池的参数

> > `corePollSize`：线程池中初始线程的数量，可能处于等待状态
> >
> > `maximumPollSize`：线程池中最大允许的线程数量
> >
> > `KeepAliveTime`：超出初始线程数量的线程如果处于等待状态将会被回收

### 资源管理

#### Java垃圾回收

> > 不被引用的对象会被回收
> >
> > 垃圾回收包括`Minor GC`和`Full GC`
> >
> > 垃圾回收时所有运行都会暂停
> >
> > 过多的`GC`也会导致程序变慢造成恶性循环 

#### Java资源管理

> > 内存会被回收，但是资源不会被释放
> >
> > `databaseConnection`需要`databaseConnection.close()`来释放资源
> >
> > ```java
> > //一般我们都要用try-finally来释放资源
> > try{
> >     Database databaseConnection = connect(...);
> >     databaseConnection.beginTransaction();
> >     ...
> > }catch(Exception e){
> >     databaseConnection.rollBack();
> > }finally{ //在finally中释放资源
> >     databaseConnection.close();
> > }
> > //finally中也会抛出异常，所以在finally中也要加try-catch
> > ```
> >
> > 在`Java1.7`之后加入了自动关闭资源的语法，只需将建立连接的语句写在try中即可
> >
> > ```java
> > try(Database databaseConnection = connect(...)){
> >     databaseConnection.beginTransaction();
> >     ...
> > }catch(Exception e){
> >     databaseConnection.rollBack();
> > }
> > ```
> >
> > 