## XML配置AOP

我们新建了一个IAccount接口，然后新建accountService实现它。

新建Logger类打印日志。

现在想要在调用accountService的方法时添加上Logger类的`printLog`打印日志方法，一般都会通过JDK提供的动态代理接口类实现该功能，现在可以通过spring的AOP特性来实现。

**配置xml文件：**

```xml
<!--将被代理类对象配置进来-->
<bean id="accountService" calss="cn.yang.accountService"></bean>
<!--spring中基于XML的AOP配置步骤
	1.把通知bean也交给spring来管理。  通知(Advice)bean指的是要切入的类
	2.使用aop.config标签表明开始AOP的配置
	3.使用aop:aspect标签表明配置切面
		id属性：是给切面提供一个唯一标识
		ref属性：是指通知类bean的id
	4.在aop:aspect标签内部使用对应的标签来配置通知的类型
		我们现在的示例是让printLog方法在切入点方法执行前执行，所以是前置通知
		aop:before 就表示前置通知。
			method属性用于指定哪个方法
			pointcut属性：用于指定切入点表达式，该表达式的含义指的是对业务层中哪些方法增强。
		切入点表达式的写法：
			关键字：execution(表达式)
			表达式：访问修饰符  返回值  包名.类名.方法名(参数列表)
					public void cn.yang.accountService.save()
					修饰符(public)可省略，void可用通配符*
					包名可以使用通配符*。二级包为*..
					类名和方法名都可以使用*来实现通配
					参数可以使用..来通配任意。指定时为如int或引用名
-->
<!--配置Logger类-->
<bean id="logger" class="cn.yang.Logger"></bean>
<!--配置AOP-->
<aop:config>
	<aop:aspect id="logAdvice", ref="logger">
        <!--前置通知-->
    	<aop:before method="beforePrintLog" pointcut="execution(* cn.yang.accountService.save())"></aop:before>
        <!--后置通知-->
    	<aop:after-returning method="afterRturnPrintLog" pointcut="execution(* cn.yang.*.*(..))"></aop:after-returning>
        <!--异常通知。与后置通知只会执行一个-->
    	<aop:after-throwing method="afterThrowPrintLog" pointcut="execution(* cn.yang.*.*(..))"></after-throwing>
        <!--最终通知-->
    	<aop:after method="afterPrintLog" pointcut="execution(* cn.yang.*.*(..))"></aop:after>
     </aop:aspect>
</aop:config>
```

因为在execution中只写了一个save方法，所以accountService的其他方法是不会被增强的。有一种通配的方法能够将所有方法都增强而不用一个一个写`execution( * *.*.*(..))`

## 注解配置AOP

配置XML支持注解：

```xml
<context:component-scan base-package="cn.yang"></context:component-scan>
<aop:aspectj-autoproxy></aop:aspectj-autoproxy>
```

切入类Logger：

```java
@Component("logger") // == <bean id="logger" class="cn.yang.Logger"></bean>
@Aspect //表示当前类是一个切面类
public class Logger {
    
    @Pointcut(execution(* cn.yang.*.*(..)))
    private void pt()P{} //定义通用切入点方法
    
    @Before("pt()")  //前置通知。  pt必须加括号
    public void beforePrint(){}
    
    @AfterReturning("pt()")  //后置通知
    public void afterReturningPrint(){}
    
    @AfterThrowing("pt()") //异常通知
    public void afterThrwingPrint(){}
    
    @After("pt()") //后置通知
    public void agterPrint(){}
    
    @Around("pt()") //环绕通知
    public Object aroundPrint(ProceedingJoinPoint pjp){
        Object reValue = null;
        try{
            Object[] args = pjp.getArgs();
            System.out.println("前置");
            reValue = pjp.proceed(args); //明确调用业务层(切入点)方法
            System.out.println("后置");
            return reValue;
        }catch (Throwable t){
            System.println("异常")
        }
    }
}
```

**环绕通知是以php.proceed()的位置为中心判断通知位置的。**