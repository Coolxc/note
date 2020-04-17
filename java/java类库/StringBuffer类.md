## StringBuffer

String类的特点：任何的字符串常量都是String对象，String常量一旦声名则不可改变。如果改变对象的内容那么改变的就是引用的指向，指向了堆上的另一个Strnig对象。

String这种不可更改的特点并不好，所以有了StringBuffer类。在String中，我们是用 +号来连接字符串。但在StringBUffer类中需要用到append方法来拼接字符串`public StringBuffer appen(数据类型 p)` 这个数据类型可以使任意类型，它重载了任何一个类型的参数。并这个方法返回的也是StringBuffer，所以可以在StringBuffer对象后无限添加append()

```java
package cn.yang.demo;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TestDemo {
	public static void main(String[] args) throws Exception{
		StringBuffer buf = new StringBuffer();
		buf.append("Hello").append("World");
		fun(buf);
	}
	public static void fun(StringBuffer temp) {
		temp.append("\n").append("Yang");
		System.out.println(temp);
	}
}
//输出
HelloWorld
Yang
```

StringBuffer可以不改变引用对象的状态下拼接字符串，但是在开发中优先选择的还是String类。StringBuffer只有在频繁修改的时候才会用到。

##### String类与StringBuffer类的继承结构

```java
//String
public final class String
extends Object
implements Serializable, Comparable<String>, CharSequence
    
//StirngBuffer类
public final class StringBuffer
extends Object
implements Serializable, CharSequence
```

可以发现两个类都是CharSequence接口的子类。而这个接口描述的是字符集，所以字符串就是字符集的子类。如果以后看见了CharSequence最简单的方法就应该想到他就是个字符串。

```java
CharSequence s = "hello"; //hello最为匿名的子类String对象为父类接口实例化（多态）
```

**StringBuffer类的构造方法**

```java
StringBuffer(CharSequence seq) //因为CharSequence是一个字符集包含了包括char,String,Sequence等等，所以StringBuffer可以接受任意字符
```

**String转变为StringBuffer**：

1. 通过StringBuffer的构造方法：`String str = "aaa"; StringBuffer b = new StringBuffer(str)`

2. 通过StringBuffer的append()方法：`String str = 'aaa'; StringBuffer b = new StringBuffer(); b.append(str)`

**StringBuffer转变为String:**任何对象都会有一个toString方法。`b.toString()`既可

**StringBuffer有一些方法String做不到：**

```java
StringBuffer.reverse(); //字符串反转
StringBuffer.delete(5,10);  //删除从5到10的字符
StringBuffer.insert(2, 'aaa'); //从2的位置开始追加aaa
```

