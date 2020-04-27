## Bean

#### Spring对bean的管理细节：

1. **创建bean有三种方式**

> 第一种：使用默认构造函数创建：只有id和class属性那么就会采用默认构造函数的方式创建bean对象，如果没有默认的构造函数（只有带参的）那么就会报错
>
> `<bean id="account" class="an.yang.Acount"></bean>`
>
> 第二种：使用普通工厂中的方法创建对象（使用某个类中的方法创建对象并存入Spring中）
>
> ```xml
> <bean id="factory" class="cn.yang.Factory"></bean>
> <bean id="realInstance" factory-bean="factory" factory-method="getRealInstance"</bean>
> <!--在Factory普通工厂类中定义有getRealInstance方法返回realinstance，第一条语句加载了工厂类，第二条语句调用了工厂类的方法，其中factory-bean指的就是上一条语句的id，factory-method指的就是工厂类中的方法-->
> ```
>
> 第三种：使用静态工厂中的静态方法创建对象（使用某个类中的静态方法创建对象并存入spring容器）
>
> ```xml
> <bean id="realInstance" class="cn.yang.Factory" factory-method="getRealInstance"></bean>
> <!--这里我们在工厂类中定义了一个静态的getRealInstance方法，因为静态方法不需要先创建类对象，所以相对于第二种方法这种更加便捷-->
> ```
>
> 

2. **bean的作用范围**

> 默认情况下spring的bean对象是单例的，但是可以通过bean标签的scope属性来设置作用范围。scope可选值：
>
> ```python
> # singleton:单例模式（默认值）
> # prototype：多例模式
> <bean id="a" class="cn.yang.Aa" scope="prototype"></bean>
> # request：作用与web应用的请求范围
> # session：作用于web应用的会话范围
> # global-session：作用于集群环境的会话范围（全局会话范围）当不是集群时和session是一样的
> ```
>
> 

3. **bean对象的生命周期**

> 单例对象：
>
> > 出生：当容器创建时对象出生
> >
> > 生存：只要容器存在对象就一直生存
> >
> > 死亡：容器销毁，对象消亡
> >
> > 可以在bean中设置初始时调用的方法和销毁时调用的方法。销毁方法在调用close()后才会触发。
> >
> > ```xml
> > <bean id="a" class="cn.yang.Aa" init-method="ha" destroy-method="axiba"></bean>
> > ```

> 多例对象：
>
> > 出生：当我们使用对象调用它的方法时spring才会创建对象
> >
> > 生存：对象在使用中就会一直活着
> >
> > 消亡：当对象被JVM的gc垃圾回收时