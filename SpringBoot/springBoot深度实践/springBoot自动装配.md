**模式注解：**

> `@Compoent`：
>
> > 通用组件模式注解。以下模式注解都派生自该注解
> >
> > 只要根注解是`@Component`，那么它都属于spring的模式注解。如`@SpringBootApplication`
>
> `@Repository`：仓库模式注解
>
> `@Service`：服务模式注解
>
> `@Controller`：Web控制模式注解
>
> `@Configuration`：配置类模式注解

## @Enable模块装配

通过组合注解和@Import注解，可以实现模块装配。将多个功能赋予一个注解身上形成一个模块，如Web MVC模块

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(HelloWorldImportSelector.class)
public @interface EnableHelloWorld {}
```

该`@EnableHelloWorld`注解引用了一个`ImportSelector`来进行一些中间操作，该类会读取一个spring.factories下key为EnableAutoConfiguration对应的全限定名的值。

```java
/**
 * 该类用来返回一个HelloWorld Bean
 * 实现ImportSelector接口
 */
public class HelloWorldImportSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata annotationMetadata) {
        //返回一个String数组，可以含有多个Bean
        return new String[]{HelloWorldConfiguration.class.getName()};
    }
}
```

`HelloWorldConfiguration`是我们定义的一个Bean

```java
@Configuration
public class HelloWorldConfiguration {

    @Bean
    public String helloWorld(){
        return "Hello World 2020";
    }
}
```

`@Configuration`继承自`@Component`，他们俩本质上是一样的

现在就将一个中间的控制插入到了我们定义的`@EnableHelloWorld`注解中，被这个注解标注过的类将可以得到`helloWorld`这个Bean

**测试类**

```java
/**
 * 虽然只有一个注解，但是他会在背后做很多事。可以看到这里并不需要扫描包
 * @EnableHelloWorld会加载HelloWorldImportSelector初始化HelloWorldConfiguration再将helloWorld这个Bean注册
 */
@EnableHelloWorld
public class EnableHelloWorldBootstrap {

    public static void main(String[] args) {
        //WebApplicationType.NONE标志这不是一个web应用
        ConfigurableApplicationContext context = new SpringApplicationBuilder(EnableHelloWorldBootstrap.class)
                .web(WebApplicationType.NONE)
                .run(args);

        //拿出来HelloWorld Bean
        String helloWorld = context.getBean("helloWorld", String.class);

        System.out.println("Hello World Bean : " + helloWorld);

        //关闭上下文
        context.close();
    }
}
```

## 条件装配

#### @Profile

`Service`接口，只有一个sum方法。将所有参数相加

```java
public interface CalculateService {

    Integer sum(Integer... values);
}
```

为了体现`@Profile`，这里实现了一个Java7版本一个Java8版本

```java
@Service
@Profile("Java7")
public class Java7CalculateService implements CalculateService {
    @Override
    public Integer sum(Integer... values) {
        int sum = 0;

        for (int i = 0; i < values.length; i++) {
            sum += values[i];
        }

        return sum;
    }
```

```java
@Service
@Profile("Java8") //Java8使用lambda表达式来计算sum
public class Java8CalculateService implements CalculateService {
    @Override
    public Integer sum(Integer... values) {
        //或写成 integers.reduce(0, (a, b) -> a+b); reduce第一个参数是初始值
        //初始值可以不加，那么reduce返回一个Optional，它是一个容器，可以使用get来获得内容
        //容器的好处就是可能Stream没有返回值，使用了容器后最多是空的，但是如果不用容器就会返回NullPointerException
        System.out.println("Java 8");
        //reduce将一连串数据拼在一块
        return (Integer) Stream.of(values).reduce(0, Integer::sum);
    }
```

**测试类**

```java
/**
 * 使用Profile的方式实现条件装配
 */
//@SpringBootApplication和@ConponentScan可以看作他们作用一样，都是扫描包
@SpringBootApplication(scanBasePackages = "com.yang.demo.service")
public class CalculateServiceBootstrap {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = new SpringApplicationBuilder(CalculateServiceBootstrap.class)
                .web(WebApplicationType.NONE)
                .profiles("Java8") //指定配置
                .run(args);

        //通过指定profile的值来指定装载哪一个CalculateService实现类
        //因为有两个实现类，所以不指定配置将会出错
        CalculateService calculateService = context.getBean(CalculateService.class);

        System.out.println(calculateService.sum(1, 2, 3, 4));


    }
}
```

#### @Conditional

定义一个注解，该注解由`@Conditional`组合

```java
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
//@Conditional注解需要一个实现了Condition接口的判断类，根据注解继承，自定义的注解也会有@Conditional功能
//然后就能根据这个实现类来判断该注解注解的类
@Conditional(OnSystemPropertyCondition.class)
public @interface ConditionalOnSystemProperty {

    String name(); //两个属性，用来条件判断

    String value();
}
```

`OnSystemPropertyCondition`就是我们的继承了`Condition`的判断类

```java
public class OnSystemPropertyCondition implements Condition {
    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {

        //getAnnotationAttributes获得注解的源信息，也就是注解体内的属性
        Map<String, Object> attributes = annotatedTypeMetadata.getAnnotationAttributes(ConditionalOnSystemProperty.class.getName());

        String propertyName = String.valueOf(attributes.get("name"));

        String propertyValue = String.valueOf(attributes.get("value"));

        String javaPropertyValue = System.getProperty(propertyName);

        //如果注解中的value和系统属性相等，那么返回true
        return javaPropertyValue.equals(propertyValue);
    }
}
```

**测试类**

```java
/**
 * 使用Condition的方式实现条件装配
 */
public class ConditionOnSystemPropertyBootstrap {

    //条件装配：当不满足条件时将不会把该bean注册为Spring组件
    //具体的判断在实现Condition接口的类中，该注解使用Conditional注册了该判断类
    @Bean
    @ConditionalOnSystemProperty(name = "user.name", value = "Administrator")
    public void hello(){
        System.out.println("Hello World!");
    }

    public static void main(String[] args) {
        //WebApplicationType.NONE标志这不是一个web应用
        ConfigurableApplicationContext context = new SpringApplicationBuilder(ConditionOnSystemPropertyBootstrap.class)
                .web(WebApplicationType.NONE)
                .run(args);

        //如果上面注解的value属性的值不是Administrator那么将没有hello这个Bean
        context.getBean("hello");

        //关闭上下文
        context.close();
    }
}
```

## SpringBoot的自动装配

#### [spring-factory](https://juejin.im/post/5d47af2de51d453bc648016f)

#### [感觉讲的比较好的自动配置原理](https://www.jianshu.com/p/346cac67bfcc)

spring-factories文件的读取是通过`SpringFactoriesLoader`这个类来实现的，这个类类似于SPI（服务发现机制），为某个接口寻找服务实现的机制。有一种IOC的思想。

spring-core包内定义了`SpringFactoriesLoader`类，这个类实现了检索`META-INF/spring.factories`文件，并获取指定接口的配置的功能。在这个类中定义有两个`public`的方法：

> `loadFactories`：根据接口类获取其实现类的实例，这个方法返回的是对象列表
>
> `loadFactoryNames`：根据接口获取其接口类的名称，这个方法返回的是类名的列表
>
> ```java
> public static List<String> loadFactoryNames(Class<?> factoryClass, ClassLoader classLoader) {
>     String factoryClassName = factoryClass.getName();
>     try {
>         Enumeration<URL> urls = (classLoader != null ? classLoader.getResources(FACTORIES_RESOURCE_LOCATION) :
>                 ClassLoader.getSystemResources(FACTORIES_RESOURCE_LOCATION));
>         List<String> result = new ArrayList<String>();
>         while (urls.hasMoreElements()) {
>             URL url = urls.nextElement();
>             Properties properties = PropertiesLoaderUtils.loadProperties(new UrlResource(url));
>             String factoryClassNames = properties.getProperty(factoryClassName);
>             result.addAll(Arrays.asList(StringUtils.commaDelimitedListToStringArray(factoryClassNames)));
>         }
>         return result;
>     }
>     catch (IOException ex) {
>         throw new IllegalArgumentException("Unable to load [" + factoryClass.getName() +
>                 "] factories from location [" + FACTORIES_RESOURCE_LOCATION + "]", ex);
>     }
> 
> ```
>
> 

`@EnableConfiguration`会寻找classpath下的META-INF/spring.factories文件。该文件里可以自定义自动装配的类

```properties
#自动装配HelloWorldAutoConfiguration配置类
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
com.yang.demo.configuration.HelloWorldAutoConfiguration
```

`HelloWorldAutoConfiguration`类

该类会进行条件判断、注册Bean等。用到的都是上面自定义的注解

```java
@Configuration //模式注解。也就是@Component
@ConditionalOnSystemProperty(name = "user.name", value = "Administrator") //条件装配
@EnableHelloWorld //Enable 模块装配
public class HelloWorldAutoConfiguration {
    HelloWorldAutoConfiguration(){
        System.out.println("自动装配========================");
    }
}
```

**测试类**

```java
@EnableAutoConfiguration //激活自动装配
public class AutoConfigurationBootstrap {

    public static void main(String[] args) {
        
        ConfigurableApplicationContext context = new SpringApplicationBuilder(AutoConfigurationBootstrap.class)
                .web(WebApplicationType.NONE)
                .run(args);

        String helloWorld = context.getBean("helloWorld", String.class);

        System.out.println("Hello World Bean : " + helloWorld);

        context.close();
    }
}
//输出
进入@Conditional条件判断
进入ImportSelector获得helloWorld Bean
进入@Conditional条件判断
自动装配========================
2020-04-30 15:51:34.571  INFO 3056 --- [           main] c.y.d.b.AutoConfigurationBootstrap       : Started AutoConfigurationBootstrap in 1.087 seconds (JVM running for 2.381)
Hello World Bean : Hello World 2020
```

由此我们就自定义了springBoot的`@EnableAutoConfiguration`自动装配~

## 总结

springBoot的自动装配也是基于spring Framework来实现的

只需定义一个装配类即可

springBoot使用`@EnableAutoCOnfiguration`来扫描需要装配的类。

需要扫描的类在classpath下的META-INF下的spring.factories定义