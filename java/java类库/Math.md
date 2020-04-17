## Math

提供基础的数学函数

Math所有的方法都使用了static定义，所以都可以通过类名称直接调用。

`Math.round(double a)`：四舍五入。如果用round直接算那么小数位就会直接进位只剩下整数部分。

```java
Math.round(15.51); //16
Math.round(15.5); //16
Math.round(-15.5); //-15
Math.round(-15.51); //-16
```

正数正常，负数的小数位如果没有大于0.5都不会进位。-15.51就会 进一位16.

**如果希望可以保存小数位进行处理**

我们发现，如果想要保存两位小数，先将数字乘以100也就是10的二次方在进行round再将得到的结果除以100就可以得到保留两位小数的round。`Math.pow(10, 2)`将得到10的二次方

```java
package cn.yang.demo;

class MyMath{
	/**
	 * 可以保留数据的小数位进行四舍五入操作
	 * @param num 接收需要round的数据
	 * @param scale  表示保留的小数位数
	 * @return 返回处理结果
	 */
	public static double round(double num, int scale) {
		return Math.round(num * Math.pow(10, scale))/Math.pow(10, 2);
	}
}

public class TestDemo {
	public static void main(String[] args) throws Exception{
		System.out.println(MyMath.round(65.324, 2));
	}
}

```

