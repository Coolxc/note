## Data

`import java.util.Date`

`new Data`可以直接输出date数据

在Date类中需要关注一个核心问题：long 可以描述date，可以通过Date类中提供的方法来观察。

在Date类的构造函数中：只有两个是现在赞成使用的。一个是无参，一个是`public Date(long date)`

也就是long类型可以变成Date类型。同样的也可以将date变为long类型：`public long getTime()`

**观察转换：**

```java
import java.util.Date;
//long转换为Date
public class TestDemo {
	public static void main(String[] args) throws Exception{
		long n = System.currentTimeMillis();
		System.out.println(new Date(n));
	}
}
//输出
Sat Feb 29 11:10:18 CST 2020
    
long n = 4133423353L;
System.out.println(new Date(n));
//输出：Wed Feb 18 04:10:23 CST 1970

//Date转换为long
import java.util.Date;

public class TestDemo {
	public static void main(String[] args) throws Exception{
		System.out.println(new Date().getTime());
	}
}
//输出
1582945908081
```

