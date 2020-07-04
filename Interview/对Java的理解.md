# 平台无关性

## Compile Once, Run antwhere如何实现

`javap`是JDK自带的一款反编译器，`javap -c xxx.class`对class文件进行反汇编

GC

CLass文件在不同平台都是一样的，不同平台的虚拟机不同，但都能够解析Class文件，将Class文件解析成机器码得以执行

其他语言也可以生成Class格式的字节码，只要是合法的Class文件那么就一定能运行在JVM上