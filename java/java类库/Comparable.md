## Comparable

如果想要进行数组的操作，Arrays类可以有很好的支持。在Arrays类中有一个排序方法`public static void sort(Object[] a)`。可以看到该方法接收一个对象数组。如：

```java
package cn.yang.demo;

import java.util.Arrays;

class Person {
	private String name;
	private int age;
	public Person(String name, int age) {
		super();
		this.name = name;
		this.age = age;
	}
	
}

public class TestDemo {
	public static void main(String[] args) throws Exception{
		Person p[] = new Person[] {
			new Person("杨",20),
			new Person("刘",19),
			new Person("李", 21),
		};
		Arrays.sort(p);
		System.out.print(Arrays.toString(p));
	}
}
//输出报错
Person cannot be cast to class java.lang.Comparable
```

Person对象不能变为Comparable对象

在Arrays.sort()方法说明中：All elements in the array must implement the Comparable interface

也就是必须设定一个逻辑，怎样排序对哪些数据进行排序

Arrays.sort()方法会将数组中的数据依次提出来变成Comparable接口对象，也就是说我们的数组中的对象必须得 implements Comparable接口根据多态才能算是Comparable类型的。

**Comparable接口定义如下：**

`public interface Comparable<T> { int compareTo(T o)} `

该接口只有一个方法：compareTo

```java
package cn.yang.demo;

import java.util.Arrays;

class Person implements Comparable<Person>{
	private String name;
	private int age;
	public Person(String name, int age) {
		super();
		this.name = name;
		this.age = age;
	}
	@Override
	public int compareTo(Person o) {
		if (this.age > o.age) {
		return 1;  //Returns:a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object.
		}else if (this.age < o.age) {
			return -1;
	}else {
		return 0;}
	}
	@Override
	public String toString() {
		return "Person [name=" + name + ", age=" + age + "]";
	}
}

public class TestDemo {
	public static void main(String[] args) throws Exception{
		Person p[] = new Person[] {
			new Person("杨",20),
			new Person("刘",19),
			new Person("李", 21),
		};
		Arrays.sort(p);
		System.out.println(Arrays.toString(p));
	}
}
//输出
[Person [name=刘, age=19],   //排序结果根据compareTo返回的结果来定
 Person [name=杨, age=20], 
 Person [name=李, age=21]]
```

## Comparator

对于Comparable，在我们类定义时就已经知道自己这个类是需要进行比较的。但是有可能有一个类在定义时并没有考虑到排序的需要。这时就用到了Comparator。

对于一个没有实现Comparable接口的类，我们想要用Arrays.sort()方法是不太可能的。这时为了挽救一下我们可以用到 `java.util.Comparator`接口。定义如下：

```java
@FunctionalInterface
public interface Comparator<T>{
    public int compare(T o1, To2);
    public boolead equals(Object obj);
}
```

可以看到这是一个函数式编程接口，意味着只会有一个方法 。我们知道`toString`和`equals`方法是Object的方法，Java基础告诉我们，Object是所有类的默认父类，也就是说任何对象都会包含Object里面的方法，即使是函数式接口的实现，也会有Object的默认方法，所以：**重写Object中的方法，不会计入接口方法中**，除了final不能重写的，Object中所能重写的方法，写到接口中，不会影响函数式接口的特性

想要使用comparator需要定义一个对象想要排序的对象具有排序功能的子类

**定义一个对于Person具有排序功能的类：**

```java
class Personcom implements Comparator<Person> {
	@Override
	public int compare(Person o1, Person o2) {
		if (o1.getAge > o2.getAge()) {
			return 1;
		}else if(o1.getAge() == o2.getAge()) {
			return 0;
		}else {
			return -1;
		}
	};
}
```

排序的操作还是在Arrays里面，不过换了一个方法：`public static <T> void sort(T[] a, Comparator<? super T> c)`

使用：

```java
public class TestDemo {
	public static void main(String[] args) throws Exception{
		Person per[] = new Person [] {
				new Person("杨", 20),
				new Person("李",19),
				new Person("刘",30)
		};
		Arrays.parallelSort(per, new Personcom());
		System.out.println(Arrays.deepToString(per));
	}
}
```

