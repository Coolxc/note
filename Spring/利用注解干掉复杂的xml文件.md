```java
@Configuration //指定当前类是一个配置类
@ComponentScan("cn.yang") //指定创建容器时扫描的包。两个属性value和basePackages作用一样
public class config {
    //此注解会将返回的对象放到spring容器中。
    //此注解中有一个属性name，用来指定bean id。默认值是方法名称
   @Bean(name="runner")
    public QueryRunner creatQueryRunner(DataSource dataSource){
        return new QueryRunner(dataSource);
    }

    //@Bean 将返回的对象放入到容器中，id为name
    @Bean(name="dataSource")
    public DataSource creatDataSource(){
        ComboPooledDataSource ds = new ComboPooledDataSource();
        try {
            ds.setDriverClass("com.mysql.jdbc.Driver");
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
        ds.setJdbcUrl("jdbc:mysql://localhost:3306/douban");
        ds.setUser("yang");
        ds.setPassword("123456");
        return ds;
    }
}
```

值得注意的是第一个被bean注解的类。spring在实例化它并放入容器中时是怎么知道他的参数在哪里呢。spring会在容器中找与他的参数类型匹配的bean，刚好我们在下面就写了一个bean类正好符合。如果没有下面的dataSource类那么就会报错。

此时修改获取容器的代码就可以将xml文件删了`ApplicationContext ac = new AnnotationConfigApplicationContext(config.class);`

**现在的mysql配置都写在了类中，但是我们想要写到配置文件中。然后通过Properties类获取，但是获取过程有点麻烦，于是有了`@PropertySource`注解。**

```java
@Configuration //指定当前类是一个配置类
@ComponentScan("cn.yang") //指定创建容器时扫描的包。两个属性value和basPackages作用一样
@PropertySource("classpath:jsbc.properties") //扫描配置文件
public class config {
    @Value("${driver}") //@Value注解注入基本数据类型值
    private String driver;
    @Value("${url}")
    private String url;
    @Value("${name}")
    private String name;
    @Value("${password}")
    private String password;
```

