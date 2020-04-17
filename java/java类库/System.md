## System

 `System.currentTimeMillis()`：这个方法可以计算某一段程序执行时间

```java
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TestDemo {
	public static void main(String[] args) throws Exception{
		long start = System.currentTimeMillis(); //返回long类型
		String str = "";
		for (int x = 0; x < 2000; x++) {
			str += x;
		}
		long end = System.currentTimeMillis();
		System.out.println("花费总时间: " + (end-start));
	}


}
//输出
花费总时间: 32 //单位是毫秒
```

System类中也有一个gc()，但他是调用的Runtime中的gc()

**一个类对象创建时一定会调用构造方法，那么放一个对象被释放时应该也会有一个方法自动调用。如果此时要定义对象被释放的收尾操作，我们可以覆写Object类中的final()方法。**

Object中final的定义：`protected void finalize() thorws Throwable`   注意到抛出一个Throwable异常，表示在final()方法中无论出现任何异常或错误都会直接抛出不管，结果不会改变程序也不会出错。

```java
package cn.yang.demo;

class Person{
	public Person() {
		System.out.println("wawawa");
	}
	@Override
	protected void finalize() throws Throwable {
		System.out.println("我不想活了");
		throw new Exception("我还想活"); //尽管抛出异常程序还是会执行
	}
}

public class TestDemo {
	public static void main(String[] args) throws Exception{
		Person p = new Person();
		p = null; //引用置为空。此时JVM应该会自动回收
		System.gc();  //如果JVM没有回收则手动回收
		System.out.println("hahahahaha");
	}


}
//输出
wawawa
hahahahaha
我不想活了
```

`final`：final是一个关键字，用于定义不能被继承的类、不能被覆写的方法、不能被更改的常量

`finally`：是try/catch异常处理的统一出口

`finalize`：Object类中的一个方法，用于在对象回收时进行调用。

## 克隆

`protected Object clone() throws CloneNotSupprotedException`

protected表示允许不同包的子类访问，但非子类不能访问。

需要克隆的对象一定要实现Clonable接口。该接口没有任何方法。所以该接口只是一个标识接口，没有任何能力。