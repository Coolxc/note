#### volatile是啥

- volatile是一种同步机制，比sunchronized或者Lock等更加轻量，因为使用volatile并不会发生上下文切换等开销很大的行为

- 如果一个变量别修饰为volatile，那么JVM就知道了这个变量可能被并发的修改

- 开销小，响应的能力也就小。虽然说volatile是用来同步的保证线程安全的，但是volatile做不到synchronized那样的原子保护，volatile仅在**很有限**的场景下才能发挥作用

#### 不适用的场景（非原子操作）

不适用 `a++`：

> ```java
> package jmm;
> 
> import java.util.concurrent.atomic.AtomicInteger;
> 
> public class noVolatile implements Runnable {
> 
>     volatile int a;
>     AtomicInteger atomicInteger = new AtomicInteger();
> 
>     @Override
>     public void run() {
>         for (int i = 0; i < 10000 ; i++) {
>             a++;
>             atomicInteger.incrementAndGet();
>         }
>     }
> 
>     public static void main(String[] args) throws InterruptedException {
>         Runnable r = new noVolatile();
>         Thread thread1 = new Thread(r);
>         Thread thread2 = new Thread(r);
>         thread1.start();
>         thread2.start();
>         thread1.join();
>         thread2.join();
> 
>         System.out.println("a的值为：" + ((noVolatile) r).a); //类型转换
>         System.out.println("atomicInteger的值为：" + ((noVolatile) r).atomicInteger.get());
>     }
> }
> //输出
> a的值为：19780
> atomicInteger的值为：20000
> ```
>
> 可以看到volatile并不能保证a++的多线程安全性，由于a++实际上是分了三步，所以如果使用原子类就能保证a++包含的三个操作不可分割一气呵成

#### 适用场景

1. `boolean flag`或标记位或`int a= 3`这种一气呵成的操作：如果一个共享变量自始自终只被各个线程赋值，而没有其他的操作，那么就可以用volatile来代替synvhronized或代替原子变量。因为赋值**自身是有原子性**的，而volatile又保证了可见性，所以就足以保证线程安全。

2. **触发器：**近朱者赤，一旦触发，预想中的所有操作都将完成

   > ```java
   > int a = 0;
   > volatile int b = false;
   > int c = 3;
   > 
   > private void change(){
   >     a = 333;
   >     c = 666;
   >     b = true;
   > }
   > 
   > private void print(){
   >     if (b){
   >         System.out.println(a,c); //如果b为true了，那么ac的值一定是修改后的
   >     }
   > }
   > ```
   >
   > 一旦读取到b的值为true了，由于volatile满足Happend-Before原则。那么在b之前的所有修改都可以由其他线程看到，此时a的值一定是333，c的值一定是666
   >
   > 如果不用volatile，由于内存可见性原理，得到的结果将是未知的。有可能只读取到c的值为666而a的值还没写回主存读线程就会读到a旧的值：0

#### volatile的两点作用

- **可见性：**读取一个volatile变量之前，需要先使变量的本地缓存失效。这样就必须到主内存读取最新值，写一个volatile属性会立即刷新到主内存
- 禁止指令**重排序**优化：解决单例双重锁乱序问题

#### volatile和synchronized的关系

volatile在这方面可以看作是**轻量版**的synchronized：如果一个共享变量自始至终只被各个线程赋值，而没有其他的操作，那么就可以用volatile来代替synchronized或者代替原子变量，**因为赋值自身是有原子性的，而volatile又保证了可见性，所以就足以保证线程的安全**



#### volatile解决重排序问题

虽然重排序带来了性能的提升，但有的时候我们不希望发生重排序

如果语句中有使用volatile关键字修饰的变量，那么该语句将不会进行重排序

#### 小结

- volatile适用场景：某个属性被多个线程共享，其中有一个线程修改了此属性，其他线程可以立即得到修改后的值，比如`boolean flag`或者作为触发器，实现线程的轻量级同步
- volatile属性的读写操作都是无锁的，它不能替代synvhronized，因为它没有提供原子性和互斥性。因为无锁，所以不需要花费时间在获取锁和释放锁上，所以说它是低成本的
- volatile只能用于某个属性。不能用在方法或代码块上
- volatile提供了可见性，任何一个线程对其的修改将立马对其他线程可见。volatile属性不会被线程缓存，始终从主存读取
- volatile可以使得long和double的赋值是原子的

