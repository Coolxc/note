## Annotation(注解)

**第一阶段：**在进行软件项目开发过程之中，会将使用到的第三方的信息或有关程序的操作都写到程序中

![image-20200226103914975](图片/image-20200226103914975.png)

地址信息都写在程序里

如果服务地址都更换了，那么需要在程序内挨个更改

**第二阶段：**设置配置文件，程序读配置文件连接服务

![image-20200226104144250](图片/image-20200226104144250.png)

可以在不修改源代码就可以更改服务地址。虽然维护方便了，但开发很不方便，需要查。另外这些配置文件如果是非专业人士很难修改，并一个项目的配置文件可能会非常多，一个配置文件套一个子配置文件。。。修改配置文件会一连串都得改

于是JDK提供了一个新的做法：将配置写回到程序里，但要与传统的程序不同，这样就形成了**注解**的概念。但是并不是写了注解就不用写配置文件了

**JDK内置的三个注解：@Overrride、@Deprecate、@SuppressWarnings**

## 精准覆写：@Override

方法覆写：发生在继承关系中，子类定义了与父类的方法名称相同、参数类型及个数、返回类型相同的方法就叫覆写。覆写的方法不能有比父类更严格的访问uan限。

**当覆写Object类的toString方法时，如果不小心写成了tostring小写了，此时编译是不会出错的，这时你自己新建的一个自己的方法。为了覆写方法的严格，可以使用一个@Override来检测是否成功覆写。**

![image-20200226110012493](图片/image-20200226110012493.png)

**错误提示：**

![image-20200226110047773](图片/image-20200226110047773.png)

所以一旦添加@Override注解，如果覆写后没出错，那么一定是完美的覆写。Eclips会自动帮你的覆写方法添加注解。

## 声名过i：@Deprecated

如果有一个程序类，从项目的1.0版本用到了3.0版本都没问题，但在4.0版本的时候出问题了。这时是不能直接删除这个类的，因为他还在为1.0-3.0版本工作。所以在新版本里，需要对那个不需要的类加一个过i的注解，表示不用它了

```java
package cn.yang.demo;

class Person {
	@Deprecated  //Deprecated注解表示下面方法已经不建议使用了，但用了也不会出错
	public Person() {}
	public Person(String name) {}
	@Deprecated
	public void fun() {}

}

public class TestDemo {
	public static void main(String[] args) {
        Person o = new Person();  //用注解过的方法时会有一道线,过i了
	}
}

```

![image-20200226111212739](图片/image-20200226111212739.png)

## 压制警告：@SuppressWarning

当调用了某操作可能会产生警告(警告不代表出错)，但你不想要看到这个警告，就可以通过注解压制它。



## 元注解

元注解指的是可以注解其他注解的注解。。。

**@Retention：**标识注解在那个阶段有用

> RetentionPolicy.SOURCE 注解只在源码阶段保留，在编译器进行编译时它将被丢弃忽视。
> RetentionPolicy.CLASS 注解只被保留到编译进行的时候，它并不会被加载到 JVM 中。
> RetentionPolicy.RUNTIME 注解可以保留到程序运行的时候，它会被加载进入到 JVM 中，所以在程序运行时可以获取到它们

**@Documented：**能够将注解中的元素包含到javadoc中

**@Target：**表明此注解可以运用在什么（方法、变量、类）上

> ElementType.ANNOTATION_TYPE 可以给一个注解进行注解
> ElementType.CONSTRUCTOR 可以给构造方法进行注解
> ElementType.FIELD 可以给属性进行注解
> ElementType.LOCAL_VARIABLE 可以给局部变量进行注解
> ElementType.METHOD 可以给方法进行注解
> ElementType.PACKAGE 可以给一个包进行注解
> ElementType.PARAMETER 可以给一个方法内的参数进行注解
> ElementType.TYPE 可以给一个类型进行注解，比如类、接口、枚举

**@Inherited：**这个类被这个注解注解了的话他的子类也会继承父类的所有注解

**@Repeatable：**同一个注解可以重复写多行

## 自定义注解



注解可以拥有自己的属性。但是只有成员变量并没有方法。

注解中的成员方法以“无参的普通方法”的形式来声名，方法名定义了该成员变量的名字，返回值定义了类型：

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MyAnnotation{
    public int id(); //定义有两个属性
    public String msg() default "Hello world";
}
//使用注解
@MyAnnotation(id=1);  //因为只有一个属性，可以直接写为@MyAnnotation(1)
public class Test{
    ....
}
```

## [提取注解内容](https://blog.csdn.net/briblue/article/details/73824058)