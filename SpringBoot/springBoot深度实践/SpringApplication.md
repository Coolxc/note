### run

```java
//只需用@SpringBootApplication指定一个类，spring会将用该类来start
public class MySpringBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApplicationConfiguration.class);
    }

    /**
     * 记得把在自动装配笔记中配置的spring.factories文件内容注释掉，因为@SpringBootApplication继承了@EnableAutoConfiguration。而@EnableAutoConfiguration就会自动记载spring.factories里的自动装配接口实现类
     */
    @SpringBootApplication
    public static class ApplicationConfiguration{}
}

//main也可以写成这种方式，这种方式可以配置一些东西
public static void main(String[] args) {
        Set<String> sources = new HashSet<>(); //源（引导类）需要放到一个集合中，所以这里也可以配置多个源
        sources.add(ApplicationConfiguration.class.getName());

        SpringApplication springApplication = new SpringApplication();
        springApplication.setSources(sources); //设置引导类
        springApplication.setWebApplicationType(WebApplicationType.NONE); //设置非web类型
        ConfigurableApplicationContext context = springApplication.run(args);
    }
```

### 推断web类型

根据当前应用classpath中是否存在相关的实现类来推断web应用的类型，包括：

- Web Reactive：`webApplicationType.REACTIVE`
- Web Servlet：`webApplicationType.SERVLET`
- 非Web：`webApplicationType.NONE`

### 应用上下文初始器（ApplicationContextInitializer）

利用Spring工厂加载机制，实例化`ApplicationContextInitializer`接口的实现类并将实现类的类名添加到`META-INF/spring.factories`文件中，springContext在初始化的时候就会回调我们自定义类的`initial`方法。

[`SpringfactoriesLoader`](https://www.jianshu.com/p/85c11624c2b9)

> ```java
> public final class SpringFactoriesLoader {
>     public static final String FACTORIES_RESOURCE_LOCATION = "META-INF/spring.factories";
>     ......
> ```
>
> 这个类会加载`META-INF/spring.factories`文件里的类
>
> 加载完类资源后会将这些资源进行排序
>
> 排序类：`AnnotationAwareOrderComparator`
>
> > 该类中有一个方法来判断需要加载的类是否实现了`Ordered`接口
> >
> > ```java
> > @Nullable
> >     protected Integer findOrder(Object obj) {
> >         Integer order = super.findOrder(obj);
> >         return order != null ? order : this.findOrderFromAnnotation(obj);
> >     }
> > ```
> >
> > 如果实现了Ordered接口那么就会有`getOrder()`方法，就会对这个类的加载时机进行排序（设置优先级）
> >
> > 如果没有实现那么就正常加载
> >
> > 还有一个方式是判断是否被`@Order`