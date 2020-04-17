源代码：hello.java

执行javac命令编译：%javac hello.java得到由字节码组成的文件：hello.class

用java虚拟机(JVM)来运行hello.class文件：java hello   JVM将class文件的字节码转换成平台能够理解的形式来运行

javac编译后的字节码与平台无关，只要安装有JVM就可以解释字节码。

![image-20200221160212003](图片\image-20200221160212003.png)

![image-20200221160953726](C:\Users\vvv\AppData\Roaming\Typora\typora-user-images\image-20200221160953726.png)

![ ](图片\image-20200221162636762.png)

`int x = 3; foo.go(x);    void go(int z){...}`

x的值为一串字节组合，这里把x作为参数传入go函数中，x的字节组合是拷贝到了z中。所以go函数中对z的操作不会改变x的值。

```
for (String name : names){} 
for in 模型
(1)创建名称为name的String变量  (2)将names数组的第一个元素值赋值给name
(3)重复。
```



ArrayList<**>：括号中只能放类，所以不可以Int或bool.需要Integer或Boolean。