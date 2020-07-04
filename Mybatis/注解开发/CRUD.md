首先是主配置文件，在mybatis注解开发中，只允许存在一个xml文件：配置数据库连接的xml。

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>

    <properties resource="jdbcConfig.properties"></properties>
    <!-- 配置实体类的别名，这个package和下面的不一样-->
    <typeAliases>
        <package name="cn.yang"></package>
    </typeAliases>
    <environments default="mysql">
        <environment id="mysql">
            <transactionManager type="JDBC"></transactionManager>
            <dataSource type="POOLED">
                <property name="driver" value="${driver}"/>
                <property name="url" value="${url}"/>
                <property name="username" value="${username}"/>
                <property name="password" value="${password}"/>
            </dataSource>
        </environment>
    </environments>
    <!-- 指定带有注解的UserMapper接口所在位置-->
    <mappers>
        <package name="cn.yang.map"></package>
    </mappers>
</configuration>
```

创建UserMapper接口

```java
/**
 * 在mybatis中针对CRUD一共有四个注解：
 * @Select、@Update、@Delete、@Insert
 */
public interface UserMapper {
    @Select("select * from user") //只有一个value属性
    List<User> findAll();

    @Insert("insert into user(username,address,sex,birthday)values(#{username},#{address},#{sex},#{birthday})")
    void saveUser(User user);

    @Update("update user set username=#{username},birthday=#{birthday},sex=#{sex},address=#{address} where id=#{id}")
    void updateUser(User user);

    @Delete("delete from user where id=#{id}")
    public void deleteUser(int id);
}
```

测试类

```java
public class TestClass {
    private InputStream in;
    private SqlSessionFactory sqlSessionFactory;
    private SqlSession session;
    private UserMapper userMapper;
    @Before
    public void init() throws IOException {
        in = Resources.getResourceAsStream("Config.xml");
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(in);
        session = sqlSessionFactory.openSession();
        userMapper = session.getMapper(UserMapper.class);
    }
    @After
    public void destroy() throws IOException {
        session.commit();
        session.close();
        in.close();
    }
    @Test
    public void findAll(){
        List<User> users = userMapper.findAll();
        System.out.println(users);
    }
    @Test
    public void saveUser(){
        User user = new User();
        user.setSex("fe");
        user.setBirthday(new Date());
        user.setUsername("ppp");
        user.setAddress("BeiJing");
        userMapper.saveUser(user);
    }
    @Test
    public void updateUser(){
        User user = new User();
        user.setId(1);
        user.setUsername("噢噢噢噢");
        userMapper.updateUser(user);
    }
    @Test
    public void udateUser(){
        userMapper.deleteUser(2);
    }
}
```

