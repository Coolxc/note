## ThreadLocal

ThreadLocal可以帮我们减少一些重要的引用传递

没有使用ThreadLocal类时：

```java
package cn.yang.demo;


class Message {
	private String note;
	public void setNote(String note) {
		this.note = note;
	}
	public String getNote() {
		return note;
	}
}
 
class MessageConsumer{
	public void print(Message msg) {
		System.out.println(Thread.currentThread().getName() + "=" +msg.getNote()); 
	}
}

public class TestDemo {
	public static void main(String[] args) {
		new Thread( () ->  {
			Message msg = new Message();
			msg.setNote("杨优秀");
			new MessageConsumer().print(msg); //传入Message对象
		},"用户A").start();
		new Thread( () ->  {
			Message msg = new Message();
			msg.setNote("杨不优秀");
			new MessageConsumer().print(msg);//传入Message对象
		},"用户B").start();
	}
}
//输出
杨优秀
杨不优秀
```

我们现在想要实现不需要传入Message对象MessageConsumer的print方法就可以接收它

我们可以创建一个工具类，定义一个static的Message类型的message变量，然后将Message对象实例化后赋值给它，这样我们在Consumer类下的print函数中就不用再引用Message对象了直接用Mutil工具就好。

```java
package cn.yang.demo;


class Message {
	private String note;
	public void setNote(String note) {
		this.note = note;
	}
	public String getNote() {
		return note;
	}
}
 
class MessageConsumer{
	public void print() {
		System.out.println(Thread.currentThread().getName() + "=" +Mutil.message.getNote());
	}
}

class Mutil{
	public static Message message;
}

public class TestDemo {
	public static void main(String[] args) {
		new Thread( () ->  {
			Message msg = new Message();
			msg.setNote("杨优秀");
			Mutil.message = msg;
			new MessageConsumer().print();
		},"用户A").start();
		new Thread( () ->  {
			Message msg = new Message();
			msg.setNote("杨不优秀");
			Mutil.message = msg;
			new MessageConsumer().print();
		},"用户B").start();
	}
}
//输出
杨不优秀
杨不优秀
```

如果message变量不加static那么在静态方法Main函数中是无法调用非静态变量的。但是因为static类型的变量是所有实例共享的，所以我们会发现，只要有第一个线程赋值给了message变量，后一个线程再赋值时就会覆盖它，所以第一个线程赋值的杨优秀还没调用print方法下面的杨不优秀就覆盖了。

如果想要标识出每个线程对象的具体信息，我们就要用到ThreadLocal。这个类在数据保存时多保存了一个currentthread。`public class ThreadLocal<T> extends Object`

**在这个类里有以下几个重要方法：**

1. get数据：`public T get()`  只能get一个数据
2. 保存数据：`public void set(T value)`  只能存一个

3. 删除数据：`public void remove()`

**OK：**

```java
package cn.yang.demo;


class Message {
	private String note;
	public void setNote(String note) {
		this.note = note;
	}
	public String getNote() {
		return note;
	}
}
 
class MessageConsumer{
	public void print() {
		System.out.println(Thread.currentThread().getName() + "=" +Mutil.get().getNote());
	}
}

class Mutil{
	public static ThreadLocal<Message> threadLocal = new ThreadLocal<Message>(); 
	public static void set(Message msg) {
		threadLocal.set(msg);  
	}
	public static Message get() {
		return threadLocal.get();
	}
}

public class TestDemo {
	public static void main(String[] args) {
		new Thread( () ->  {
			Message msg = new Message();
			msg.setNote("杨优秀");
			Mutil.set(msg);
			new MessageConsumer().print();
		},"用户A").start();
		new Thread( () ->  {
			Message msg = new Message();
			msg.setNote("杨不优秀");
			Mutil.set(msg);
			new MessageConsumer().print();
		},"用户B").start();
	}
}

```

没错，ThreadLocal.set()会多保存一个currentThread对象添加到字典中

```java
   public void set(T value) {
        Thread t = Thread.currentThread();
        ThreadLocalMap map = getMap(t);
        if (map != null) {
            map.set(this, value);
        } else {
            createMap(t, value);
        }
    }

    public T get() {
        Thread t = Thread.currentThread();
        ThreadLocalMap map = getMap(t);
        if (map != null) {
            ThreadLocalMap.Entry e = map.getEntry(this);
            if (e != null) {
                @SuppressWarnings("unchecked")
                T result = (T)e.value;
                return result;
            }
        }
        return setInitialValue();
    }
```

