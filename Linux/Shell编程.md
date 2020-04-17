创建Shell文件，可以是任意名称，不一定非要.sh结尾。如：`a.txt; a; hello`甚至都不需要后缀。但一般的规定要以.sh结尾来标记这是一个Shell文件。

分配可执行权限：`chmod 744 hello.sh`

执行：

> 相对路径`./hello.sh`
>
> 绝对路径：`/home/yang/hello.sh`

没有执行权限时：`sh ./hello.sh`

#### Shell变量

**Linux Shell中的变量分为：系统变量和用户自定义变量**

> 系统变量：`$HOME ; $PWD ; $SHELL ; $USER`等....使用时可以`echo $HOME`输出用户目录
>
> 显示当前shell中所有变量：set
>
> ##### 自定义变量
>
> > 定义变量：变量名=值，等号左右不能有空格。变量名习惯大写
> >
> > 撤销变量：unset 变量名
> >
> > 声明静态变量：readonly 变量，不能对静态变量unset。静态变量如其名，声明后不能修改
> >
> > 将命令的返回值赋给变量
> >
> > > ```bash
> > > #将ls命令返回的文件列表赋值给A。
> > > A=`ls -l`  #反引号。
> > > #或
> > > A=$(ls -l)
> > > ```

##### 设置环境变量

1. `export 变量名=变量值`  ：将shell变量输出为环境变量
2. `source 配置文件`  ：使配置文件生效
3. `echo $变量名`  ：查询环境变量的值

##### 位置参数变量

当我们需要获取到命令行参数时，就可以使用位置参数变量。

如 ./hello.sh 100 aaa。我们想要获取到100和aaa这两个命令行参数，

**语法：**

> `$n`：n位数字，$0代表命令本身，`$1到$9`代表第1到第9个参数
>
> `$*`：这个变量代表命令行中所有的参数，`$*`把所有的参数看成一个整体
>
> `$@`：这个变量代表命令行中所有的参数，不过`$@`把每个参数区分对待
>
> `$#`：这个变量代表命令行中所有参数的个数

##### 预定义变量

**语法**

> `$$`：当前进程号（PID）
>
> `$!`：后台运行的最后一个进程的进程号（PID）
>
> > `./hello.sh &`：将以后台的形式运行
>
> `$?`：最后一次执行的命令的返回状态。如果这个变量的值位0，证明上一个命令正确执行。如果为非零（具体哪个数由自己来定），则证明上一个命令执行不正确

##### 运算符

`expr 3 - 3`：数字与运算符之间要有空格

或：`$[3+3]`

```bash
#计算命令行参数相加
#!/bin/bash
A=$(expr $1 + $2)  #$()来获取括号内表达式的返回值~或使用``
#或：A=$[$1+$2]
echo $A

./hello.sh 3 4
7
```

##### 条件判断

**语法：**

> `if [ condition ] then ... fi`

**判断语句：**

> `=`：用于字符串比较
>
> > ```bash
> > if [ "o"="o" ]
> > then
> >         echo "oK"
> > fi
> > ```
>
> `-lt`：小于
>
> >```bash
> >if [ 1 -lt 3 ]
> >then
> >        echo "oK"
> >fi
> >```
>
> `-le`：小于等于
>
> `-eq`：等于
>
> `-gt`：大于
>
> `-ge`：大于等于
>
> `-ne`：不等于
>
> `-w`：有写权限
>
> `-r`：有读权限
>
> `-x`：有执行权限
>
> `-f`：文件存在并且是一个常规的文件
>
> `-e`：文件存在
>
> >```bash
> >#!/bin/bash
> >if [ -e /home/yang/sell ]
> >then
> >        echo "oK"
> >fi
> >```
>
> `-d`：文件存在并且是一个目录

##### 流程判断

```bash
#!/bin/bash
#判断命令行参数
if [ $1 -lt 60 ]  #括号左右记得空格，记得then
then
        echo "及格"
elif [ $1 -gt 60 ] & [ $1 -lt 90 ]
then
        echo "良好"
elif [ $1 -gt 90 ]
then
        echo "优秀"
fi
```

**case语句**

```bash
#!/bin/bash

case $1 in
        "1")
                echo "周一"
                ;;
        "2")
                echo "周二"
                ;;
        *)
                echo "other"
                ;;
esac
```

**for循环**

```bash
#!/bin/bash
for i in "$*" #$*表示所有命令行参数
do
        echo "the num is $i"
done

#执行
./hello.sh 1 23 4
the num is 1 23 4
```

```bash
#将$*换成$@
for i in "$@"  #$@会将命令行参数分开
do
        echo "the num is $i"
done

#执行
yang@PC-20200329MWQL:~$ ./hello.sh 1 23 42
the num is 1
the num is 23
the num is 42
```

**另一种for循环**

```bash
yang@PC-20200329MWQL:~$ cat hello.sh
#!/bin/bash
for ((i=1;i<10;i++))
do
        echo "the current number is  $i"
done

yang@PC-20200329MWQL:~$ ./hello.sh
the current number is  1
the current number is  2
the current number is  3
the current number is  4
the current number is  5
the current number is  6
the current number is  7
the current number is  8
the current number is  9
```

**while循环**

```bash
yang@PC-20200329MWQL:~$ cat hello.sh
#!/bin/bash
A=1
while [ $A -lt 10 ]
do
        echo "A = $A"
        A=$[A+1]
done

yang@PC-20200329MWQL:~$ ./hello.sh
A = 1
A = 2
A = 3
A = 4
A = 5
A = 6
A = 7
A = 8
A = 9
```

##### Read

`-p`：指定读取值时的提示符

`-t`：指定读取值时等待的时间（秒），如果没有在指定的时间内输入则直接执行下一条语句

```bash
#!/bin/bash
#等待时间超过3秒则退出。
num=1
while [ $num ]  #如果num变量有值则一直循环。超过三秒read就会将num赋值为空字符串
        do
                read -t 3 -p "input somethind :" num
                echo "your input is $num"
        done
```

##### 函数

**系统函数**

> `basename`：返回完整路径最后 / 的部分，常用于获取文件名
>
> > `basename [pathname] [suffix]`
> >
> > `basename [string] [suffix]`：`basename`会删除所有的前缀包括最后一个 "/"字符，然后将字符串显示出来
> >
> > `suffix`为后缀，如果`suffix`被指定了，`basename`会将`pathname`或`string`中的`suffix`去掉。

**自定义函数**

```bash
yang@PC-20200329MWQL:~$ cat hello.sh
#!/bin/bash

function sum(){
        SUM=$[$n1+$n2]
        echo "The sum is $SUM"
}

read -p "input the first num : " n1   #n1 n2赋值
read -p "inout the second num : " n2

sum $n1 $n2  #调用函数

#执行
yang@PC-20200329MWQL:~$ ./hello.sh
input the first num : 1
inout the second num : 3
The sum is 4
```

## 综合数据备份案例