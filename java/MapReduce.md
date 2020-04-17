Map：处理数据

Reduce：分析数据

处理数据需要用到Stream数据流。

在Collection接口中除抽象方法还定义有一些default方法可以直接拿来用的：

1. foreach输出支持：`public default void foreach(Consumer<? super T> action)`
2. **取得Stream数据流对象：**`public default Stream<E> stream();`

**范例：**

```java
List<String> all = new ArrayList<String>();
all.add("java");
all.add("mysql");
all.add("python");
all.foreach(System.out::println);  //方法引用
//输出
java
mysql
python
```

相对来说还是用Iterator好用一点。

## Stream

Stream类中含有filter方法：`Stream<T> filter(Predicate<? super T> predicate)`

`filter`接收一个断言型函数式接口参数。

可以直接向方法中写lambda，编译器会帮我们识别是Predicate接口类型的然后将lambda的内容写到接口的test方法中，返回布尔值。filter方法中会调用predicate.test()。

**Stream实现数据过滤：**

```java
public class TestDemo {
	public static void main(String[] args) {
		List<String> all = new ArrayList<String>();
		all.add("java");
		all.add("javascript");
		all.add("mysql");
		all.add("jsp");
		Stream<String> stream = all.stream();
		//统计出这里面带有Java内容的个数
		System.out.println(stream.filter((x)->x.contains("java")).count());
	}
}
//输出
2
```

**当我们需要具体的数据时就需要用到Stream的collect方法了**

```java
public class TestDemo {
	public static void main(String[] args) {
		List<String> all = new ArrayList<String>();
		all.add("java");
		all.add("javascript");
		all.add("mysql");
		all.add("jsp");
		Stream<String> stream = all.stream();
		//统计出这里面带有Java内容的个数
		List<String> list = stream.filter((x)->x.contains("java")).collect(Collectors.toList());
		System.out.println(list);
	}
}
//输出：[java, javascript]
//collect接收一个Collectors类型参数
//将数据保存在数组中
```

Stream有两个重要的方法：

1. 设置取出的最大内容量：`public Stream<T> limit(long maxSize)`
2. 跳过的数据量：`public Stream<T> skip(long n)`

```java
//map方法可以进行简单的数据处理操作
<R> Stream<R> map(Function<? super T,? extends R> mapper)
//map接收一个功能型函数式接口，功能性接口将会有参数和返回值
```

```java
public class TestDemo {
	public static void main(String[] args) {
		List<String> all = new ArrayList<String>();
		all.add("java");
		all.add("javascript");
		all.add("mysql");
		all.add("jsp");
		all.add("jsp");
		all.add("jsp");
		Stream<String> stream = all.stream();
		//跳过两个最多取三个
        //map中使用lambda将字母都准换为大写
		List<String> list = stream.skip(2).limit(3).map((x)->x.toUpperCase()).collect(Collectors.toList());
		System.out.println(list);
	}
}
//输出：[MYSQL, JSP, JSP]
```

## MapReduce模型

MapReduce是整个Stream操作的核心所在。主要由两个阶段组成：

1. map()：进行数据的前期处理
2. reduce()：进行数据分析

**范例：**

```java
package cn.yang.demo;

import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestDemo {
	public static void main(String[] args) {
		List<Order> orderList = new ArrayList<>();
		orderList.add(new Order("iphone", 4999, 88));
		orderList.add(new Order("Java实战", 99.9, 211));
		orderList.add(new Order("pencil", 2, 9000));
		orderList.add(new Order("macbook", 19999, 33));
		orderList.add(new Order("honor", 3999.0, 112));
		Stream<Order> stream = orderList.stream(); 
		DoubleSummaryStatistics dss =  stream.mapToDouble((obj) -> obj.getPrice()*obj.getAmount()).summaryStatistics();
		System.out.println("总量：" + dss.getCount());
		System.out.println("平均值：" + dss.getAverage());
		System.out.println("最大值：" + dss.getMax());
		System.out.println("最小值：" + dss.getMin());
		System.out.println("总和：" + dss.getSum());
	}
}

class Order{
	private String title;
	private double price;
	private int amount;
	public Order(String title, double price, int amount) {
		super();
		this.title = title;
		this.price = price;
		this.amount = amount;
	}
	public double getPrice() {
		return price;
	}
	public int getAmount() {
		return amount;
	}
	public String getTitle() {
		return title;
	}
}
//输出
总量：5
平均值：317369.18
最大值：659967.0
最小值：18000.0
总和：1586845.9
```

