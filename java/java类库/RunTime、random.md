## Runtime

Runtime类是一个运行时的描述类，在每一个JAVM的进程中都会存在一个Runtime类的对象。

这个类在文档中看不到构造方法，因为在定义时将构造方法私有化了。

每一个进程都会有唯一的一个Runtime实例化对象，所以不能让他被自由实例化采用了私有化构造方法的形式，该类采用的是单例设计模式，那么在类的内部一定会有一个类对象的方法。

**get Runtime对象:**`public static Runtime getRuntime()`这个方法来get对象。这个方法一定是静态方法，因为静态方法无需实例化对象即可调用。

```java
private static final Runtime currentRuntime = new Runtime();
//在类内就实例化好对象
    private static Version version;

    /**
     * Returns the runtime object associated with the current Java application.
     * Most of the methods of class {@code Runtime} are instance
     * methods and must be invoked with respect to the current runtime object.
     *
     * @return  the {@code Runtime} object associated with the current
     *          Java application.
     */
    public static Runtime getRuntime() {
        return currentRuntime;
    }
```



**得到Runtime类后我们可以用它来观察current的内存状态**

1. `public long freeMemory()`：得到空余空间总大小
2. `public long totalMemory()`：得到current可以使用的总空间
3. `public long maxMemory()`：得到最大的可用内存空间

```java
package cn.yang.demo;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TestDemo {
	public static void main(String[] args) throws Exception{
		Runtime run = Runtime.getRuntime();
		System.out.println("MAX" + " = " + b(run.maxMemory())+"M");
		System.out.println("TOTAL" + " = " + b(run.totalMemory())+"M");
		System.out.println("FREE" + " = " + b(run.freeMemory())+"M");
	}
	public static double b(long num) {
		return (double)num/1024/1024;
	}

}

//输出
MAX = 2020.0M
TOTAL = 128.0M
FREE = 126.71798706054688M
```

Runtime类中有一个重要的方法：`gc()`。执行垃圾处理

一般的，JVM会自动的调用垃圾回收算法

## Random

使用`java.util.Random`来获得随机数

Random.nextInt(100)表示边界值为100 。它随即返回一个小于100的正整数

```java
//随机产生三个字母
char data[] = new char [] {'a', 'b', 'c', 'd'};
Random rand = new Random();
for (int x; x < 3; x++0{
    System.outprintln(data[rand.nextInt(data.length)]);
})
//随机产生三个字母，可用于验证码
```

