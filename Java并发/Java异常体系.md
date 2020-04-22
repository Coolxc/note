Error：Java运行时的内部错误，这些错误程序员没有能力捕获并处理它

Exception：

> RuntimeException
>
> > 一旦发生一定是程员的问题，如数组越界....
>
> 其他的所有Exception
>
> > 这些异常都被称为受检查异常
> >
> > 这些受检查异常编译器要求我们必须处理它或者抛出去

#### UncaughtExceptionHandler

主线程可以轻松的发现异常，而子线程却不行

> **在子线程中抛出异常后，主线程会打印异常但并不会停止运行。所以有时这些子线程的异常会被忽略**
>
> ```java
> public class CantCatchDirectly implements Runnable{
>     @Override
>     public void run() {
>         throw new RuntimeException();
>     }
> 
>     public static void main(String[] args) throws InterruptedException {
>         new Thread(new CantCatchDirectly(), "MyThread-1").start();
>         Thread.sleep(300);
> 
>         new Thread(new CantCatchDirectly(), "MyThread-2").start();
>         Thread.sleep(300);
> 
>         new Thread(new CantCatchDirectly(), "MyThread-3").start();
>         Thread.sleep(300);
> 
>         System.out.println("执行完毕");
>     }
> }
> //输出
> Exception in thread "MyThread-1" java.lang.RuntimeException
> 	at CantCatchDirectly.run(CantCatchDirectly.java:4)
> 	at java.base/java.lang.Thread.run(Thread.java:834)
> Exception in thread "MyThread-2" java.lang.RuntimeException
> 	at CantCatchDirectly.run(CantCatchDirectly.java:4)
> 	at java.base/java.lang.Thread.run(Thread.java:834)
> Exception in thread "MyThread-3" java.lang.RuntimeException
> 	at CantCatchDirectly.run(CantCatchDirectly.java:4)
> 	at java.base/java.lang.Thread.run(Thread.java:834)
> 执行完毕
> //可以看到只是输出了异常，并没有停止执行
> ```
>
> ##### 子线程的异常无法try catch捕获异常
>
> ```java
> public class CantCatchDirectly implements Runnable{
>     @Override
>     public void run() {
>         throw new RuntimeException();
>     }
> 
>     public static void main(String[] args) throws InterruptedException {
>         try {
>             new Thread(new CantCatchDirectly(), "MyThread-1").start();
>             Thread.sleep(300);
> 
>             new Thread(new CantCatchDirectly(), "MyThread-2").start();
>             Thread.sleep(300);
> 
>             new Thread(new CantCatchDirectly(), "MyThread-3").start();
>             Thread.sleep(300);
>         }catch (RuntimeException e){
>             System.out.println("抓住异常");
>         }
> 
>         System.out.println("执行完毕");
>     }
> }
> //输出
> Exception in thread "MyThread-1" java.lang.RuntimeException
> 	at CantCatchDirectly.run(CantCatchDirectly.java:4)
> 	at java.base/java.lang.Thread.run(Thread.java:834)
> Exception in thread "MyThread-2" java.lang.RuntimeException
> 	at CantCatchDirectly.run(CantCatchDirectly.java:4)
> 	at java.base/java.lang.Thread.run(Thread.java:834)
> Exception in thread "MyThread-3" java.lang.RuntimeException
> 	at CantCatchDirectly.run(CantCatchDirectly.java:4)
> 	at java.base/java.lang.Thread.run(Thread.java:834)
> 执行完毕
> ```
>
> 可以看到并没有捕获。这是因为try catch是在main，主线程中的。只能捕获到主线程发生的异常，而里面的子线程抛出的异常并不能捕获
>
> ##### 补救方法
>
> 方法一（不推荐）：手动在每个run方法中捕获
>
> ```java
> //修改run方法，在run中捕获异常
> public void run(){
>         try {
>             throw new RuntimeException();
>         }catch (RuntimeException e){
>             System.out.println(Thread.currentThread().getName() + " 异常");;
>         }
>     }
> //输出
> MyThread-1 异常
> MyThread-2 异常
> MyThread-3 异常
> 执行完毕
> ```
>
> 方法二：利用 `void UncaughtExceptionHandler(Thread t, Throwable e);`处理全局异常