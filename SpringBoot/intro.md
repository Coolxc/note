```java
//开启组件扫描和自动配置
@SpringBootApplication
public class DemoApplication {
    //负责启动引导应用程序
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
```



在主配置文件中有一个注解：@SpringBootApplication

这个注解相当于三个注解：

> > 1. @Configuration：表明该类使用Spring基于JAVA的配置而不是XML
> > 2. @ComponentScan：启用组件扫描，Bean会被自动装入Spring容器
> > 3. @EnableAutoConfiguration：开启SpringBoot自动配置

可以看到主配置文件有main函数，这里的main可以让你直接将程序当作一个可执行的JAR文件来运行

#### 微服务：

一个应用应该是由一组小的应用组成，相互之间通过HTTP完成

#### @RestController

@RestController相当于@Controller加上@ResponseBody。@ResponseBody注解后的方法返回的String将会直接显示到网页上，而不会返回jsp页面

