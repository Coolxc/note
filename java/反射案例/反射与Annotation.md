## 取得Annotation信息

**在java.lang.Accessible和java.lang.class类中定义有以下两个方法：**

1. 取得全部的Annotation：`public Annotaiton[] getAnnotations()`
2. 取得指定的Annotation：`public <T extends Annoation> T getAnnotation(Class<T> annotationClass)`

## 自定义Annotation

每一个注解都有自己的范围，这些范围就在`java.lang.annotation.RetentionPolicy`这个枚举类中定义。该类定义有如下字段：

`public static final RetentionPolicy SOURC`：在源代码中出现的Annotation

`public static final RetentionPolicy CLASS`：在类定义时出现的Annotation

`public static final RetentionPolicy RUNTIME`：在类定义时允许出现的Annotation

**自定义Annotation:**

```java
package cn.yang.demo;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME) //表示在运行时生效
@interface MyAnnotation{
	public String name(); //定义一个属性
	public int age(); //定义一个属性
}

@SuppressWarnings("serial")
@MyAnnotation(name="yang", age=22)
class Member implements Serializable{}

public class TestDemo {
	public static void main(String[] args) throws Exception {
		Annotation ant [] = Member.class.getAnnotations();
		for (int i = 0; i < ant.length; i++) {
			System.out.println(ant[i]);
		}
	}
}
//输出
@cn.yang.demo.MyAnnotation(name="yang", age=22)
//这里并没有出现压制的注解信息，因为他的级别是SOURCE，只在源码有效
```

我们写的注解这时是需要初始化name和age的，可以添加一个default关键字设置默认值

```java
public String name() default "yang";
public int age() default 22;
```

#### 确定具体的Annotation

```java
package cn.yang.demo;

import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME) //表示在运行时生效
@interface MyAnnotation{
	public String name() default "yang"; //定义一个属性
	public int age() default 22; //定义一个属性
}

@SuppressWarnings("serial")
@MyAnnotation
class Member implements Serializable{}

public class TestDemo {
	public static void main(String[] args) throws Exception {
        //返回的是具体的一定是MyAnnotation，所以类型不可以是通用的了
		MyAnnotation ant = Member.class.getDeclaredAnnotation(MyAnnotation.class);
		System.out.println("名字：" + ant.name());
		System.out.println("年龄：" + ant.age());
		
	}
}
//输出
名字：yang
年龄：22
```

## 工厂设计模式与Annotation

```java
package cn.yang.demo;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.InvocationTargetException;

interface IFruit{
	public void eat();
}

class Apple implements IFruit{
	@Override
	public void eat() {
		System.out.println("吃苹果");
	}
}
//getInstance方法不再需要参数，直接通过注解完成
@MyAnnotation(target=Apple.class)
class Factory{
	public static <T> T getInstance() throws Exception {
		MyAnnotation ant = Factory.class.getAnnotation(MyAnnotation.class);
		return (T)ant.target().getDeclaredConstructor().newInstance();
	}
}

@Retention(RetentionPolicy.RUNTIME) //表示在运行时生效
@interface MyAnnotation{
	public Class<?> target(); //需要一个Class类实例对象。
}


public class TestDemo {
	public static void main(String[] args) throws Exception {
		IFruit apple = Factory.getInstance();
		apple.eat();
	}
}
```

