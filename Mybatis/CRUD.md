OGNL表达式：Object Graphic Navigation Language：对象 图 导航 语言

他是通过对象的取值方法来获取数据。在写法上把get省略了

如我们获取用户的名称：

> > 类中的写法：`user.getUsername();`
> >
> > OGNL中的写法：`user.username;`
>
> mybatis中为什么能直接写`update user set username=#{username}`而不是`#{user.username}`呢
>
> > 那是因为在`parameterType`中已经提供了属性所属的类`User`
> >
> > 这也提醒我们在User类中一定要写规范的getUserName，或采用自动生成

**一般的我们需要将实体类User的属性名与数据库表中的字段名一一对应，但是有时候不想对应时就需要用到mybatis提供的配置方法：**

```xml
<resultMap id="userMap" type="cn.yang.User">
    <!--主键字段的对应-->
	<id property="userId" column="id"></id>
    <!--非主键字段的对应-->
	<property="userName" column="username"></id>
    .......
</resultMap>
```

**然后在下面的sql标签中需要将parameterType修改为resultMap属性**

```xml
<insert id="saveUser" resultMap="userMap">
```





**首先是创建UserMapper接口，该接口定义有各种数据库操作方法：**

```java
public interface UserMapper {
    List<User> findAll();

    void saveUser(User user);

    void updateUser(User user);

    void deleteUser(Integer id);

    User findById(Integer id);

    List<User> findByName(String name);
    //查询总用户数
    int findTotal();
}
```



**创建实体类User，有对应于user表的各个属性和对应的Getter和Setter**

**创建主配置文件 Config.xml：**

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <!--properties指定资源文件地址
        resource属性：用于指定配置文件的位置，按照类路径的写法来写，必须存在于类路径下
        URL(Uniform Resource Locator:统一资源定位符)：唯一的标识一个资源的位置
            写法：
                http://localhost:8080/mybaties/demolServlet
                协议      主机    端口     URI
                URI(Uniform Resource Identifier):统一资源标识符，它是在应用中定义唯一标识的一个资源
            而文件用的是file协议，所以如果将resource换城url的话需要一个资源定位URL：
                   <properties url="file:///E:/firefox/MyDemo/Demo_4/src/main/resources/"></properties>
    -->
    <properties resource="jdbcConfig.properties"></properties>
     <!--使用package标签指定要配置别名的包，指定后该包下的实体类都会自动配置别名，类名就是别名并且不区分大小写
    <typeAliases>
        <package name="cn.yang"></package>
    </typeAliases>
    配置后UserDao.xml中的resultType值就可以直接使用别名了,如：
    <select id="findAll" resultType="User">   可以是User也可以是user也可以是usEr
    -->
    <environments default="development">
        <environment id="development">
            <transactionManager type="JDBC"/>
            <!--properties属性值可以使用${}动态获取由properties标签定义的资源文件内容-->
            <dataSource type="POOLED">
                <property name="driver" value="${driver}"/>
                <property name="url" value="${url}"/>
                <property name="username" value="${username}"/>
                <property name="password" value="${password}"/>
            </dataSource>
        </environment>
    </environments>
    <mappers>
        <mapper resource="cn/yang/dao/UserMapper.xml"/>
        <!-- 可以使用package标签，指定UserDao接口所在的包，指定后就不需要再写<mapper resource="cn/yang/dao/UserMapper.xml"/>
        这里的package与UserMapper.xml中的package不同。
        <package name="cn.yang.dao"></package>
        -->
    </mappers>
</configuration>
```

**jdbcConfig.properties文件内容：**

```properties
driver=com.mysql.jdbc.Driver
url=jdbc:mysql://localhost:3306/douban
username=yang
password=123456
```



**创建UserMapper.xml具体配置文件：**

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<!--如果主配置类配置了别名那么这里的resultType可以直接是user-->
<mapper namespace="cn.yang.dao.UserMapper">
    <select id="findAll" resultType="cn.yang.User">
    select * from user
    </select>
    <insert id="saveUser" parameterType="cn.yang.User">
        <!--返回最后一条id， keyProperty对应类属性, keyColumn对应表列名，resultType返回类型，order选择相对于插入的执行时间-->
        <selectKey keyProperty="id" keyColumn="id" resultType="int" order="AFTER">
            select last_insert_id();
        </selectKey>
        insert into user(username, address, sex, birthday)values(#{username}, #{adderss}, #{sex}, #{birthday})
    </insert>
    <update id="updateUser" parameterType="cn.yang.User">
        <!--这里可以直接写#{username}得益于mybatis做了处理，其实应该写User.username-->
        update user set username=#{username}, sex=#{sex}, birthday=#{birthday}, address=#{adderss} where id=#{id}
    </update>
    <delete id="deleteUser" parameterType="Integer">
        delete from user where id=#{id}
    </delete>
    <select id="findById" parameterType="Integer" resultType="cn.yang.User">
        select * from user where id=#{id}
    </select>
    <select id="findByName" parameterType="String" resultType="cn.yang.User">
        select * from user where username like #{name}
    </select>
    <select id="findTotal" resultType="Integer">
        select count(id) from user
    </select>
</mapper>
```

**测试类：**

```java
public class MybatisTest {
    private SqlSessionFactory factory;
    private SqlSession session;
    private UserMapper userMapper;
    private InputStream in;

    @Before
    public void init() throws IOException {
        in = Resources.getResourceAsStream("Config.xml");
        factory = new SqlSessionFactoryBuilder().build(in);
        session = factory.openSession();
        userMapper = session.getMapper(UserMapper.class);
    }

    @After
    public void destroy() throws IOException {
        //mybatis会帮我们关闭自动提交，需要手动提交。否则事务会回滚
        session.commit();
        session.close();
        in.close();
    }

    @Test
    public void testFindAll(){

        List<User> users = userMapper.findAll();
        for (User user:users){
            System.out.println(user);
        }
    }

    @Test
    public void saveUser(){
        User u = new User();
        u.setUsername("ccccccccccccc");
        u.setBirthday(new Date());
        u.setSex("男");
        u.setAdderss("小关注国内");
        System.out.println("保存前的id：" + u.getId());
        userMapper.saveUser(u);
        System.out.println("保存后的id：" + u.getId());
    }

    @Test
    public void updateUser(){
        User u = userMapper.findById(1);
        u.setUsername("hahahahahaha");
        u.setBirthday(new Date());
        u.setSex("男");
        u.setAdderss("小asdasdasd");
        userMapper.updateUser(u);
    }
    @Test
    public void deleteUser(){
        userMapper.deleteUser(1);
    }
    @Test
    public void findUserById(){
        User u = userMapper.findById(4);
        System.out.println(u);
    }
    @Test
    public void findUserByName(){
        List<User> users = userMapper.findByName("杨%"); //需要在参数中设置模糊查询标识符%
        for (User user:users){
            System.out.println(user);
        }
    }
    @Test
    public void findTotal(){
        int u = userMapper.findTotal();
        System.out.println(u);
    }
}
```

