在System类中实际上定义有三个操作的常量。

标准输出（显示器）：`public static final PrintStream out`

错误输出：`public static final PrintStream err`

标准输入（键盘）：`public static final InputStream in`

原来一直使用的`System.out.print()`一直是属于IO操作的范畴。out是PrintSream类型的，所以`System.out.print()`就等于`PrintStream.print()`而PrintStream有一个print方法，并且print方法调用的String.valueOf()，而valueOf方法又调用了toString()。这也是为什么我们`System.out.print()`时调用的是对象的toString方法的原因了。

## 系统输出

关于系统输出有两个常量：out、err。并且这两个常量都是PrintStream类的对象对象。

out输出的是用户希望看见的内容，err是用户看不到的内容。这两种在我们的开发过程中并没有用。

由于System.out属于PrintStream类的实例化对象，又因为PrintStream是OutputStream的子类，所以System.out可以直接为OutputStream实例化。那么这个时候OutputStream输出的位置将变为屏幕。

**使用System.out为OutputStream实例化：**

```java
public class TestDemo {
	public static void main(String[] args) throws Exception {
		OutputStream out = System.out; //子类实例向上转型
		out.write("世界和平".getBytes());
	}
}
//命令行会输出：世界和平
```

**抽象类不同的子类针对于同一方法有不同的实现。**

## 系统输入：in

System.in对应的类型是InputStream。这种的输入流是指用户通过键盘进行输入。

**利用InputStream实现数据的输入：**

```java
package cn.yang.demo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

public class TestDemo {
	public static void main(String[] args) throws Exception {
		InputStream input = System.in; //为父类实例化
		byte data [] = new byte [10];
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		System.out.print("请输入信息：");
		int temp = 0;
		while((temp=input.read(data)) != -1) { //数据读取到字节数组中
			if (temp < data.length) {
				break;
			}
			out.write(data, 0, temp); //将data中0-temp的数据保存到内存输出流
		}
		System.out.println("【echo】输入内容为："+ new String(out.toByteArray()));
	}
}
```

上面虽然实现了获取用户输入，但是代码太过混乱了。如果不考虑中文只考虑中文那么代码可以简单一点：只限英文。。。

```java
public class TestDemo {
	public static void main(String[] args) throws Exception {
		InputStream input = System.in; //为父类实例化
        //缓存数组
		StringBuffer buf = new StringBuffer();
		byte data [] = new byte [10];
		System.out.print("请输入信息：");
		int temp = 0;
		while((temp=input.read()) != -1) { //单个读取字节返回值为字节值
			if (temp == '\n') {
				break;
			}
			buf.append((char) temp);
		}
		System.out.println("【echo】输入内容为："+ buf);
	}
	}
```

如果要想在IO中进行中文的处理，那么最好将所有的数据保存在一起再一起处理，防止出现乱码（一个中文是两个字节，可能某处只读了一个字节那么肯定会有乱码）