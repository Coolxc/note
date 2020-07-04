## 使用XML配置

```java
//用户使用接口
public interface UserDao {
    List<User> findAll();
}
```

**User类：**

```java
public class User implements Serializable {
    private Integer id;
    private String username;
    private Date date;
    private String sex;
    private String address;
    .............
```

**配置文件：**

主配置文件：

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<!-- 主配置文件-->
<configuration>
    <environments default="mysql">
        <environment id="mysql">
            <!-- 配置事务类型-->
            <transactionManager type="JDBC"></transactionManager>
            <!-- 配置数据源(连接池)-->
            <dataSource type="POOLED">
                <property name="driver" value="com.mysql.jdbc.Driver"/>
                <property name="url" value="jdbc:mysql://localhost:3306/douban"/>
                <property name="username" value="yang"/>
                <property name="password" value="123456"/>
            </dataSource>
        </environment>
    </environments>

    <!--指定映射配置文件的位置，映射配置文件指的是每个dao独立的配置文件-->
    <mappers>
        <mapper resource="cn/yang/dao/UserDao.xml"></mapper>
    </mappers>
</configuration>
```

UserDao的配置文件：

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<!-- namespace为dao接口的全限定类名-->
<mapper namespace="cn.yang.dao.UserDao">
    <!-- 配置查询所有，id为UserDao的方法名称，resultType表示将查询到的信息封装到User对象中-->
    <select id="findAll" resultType="cn.yang.User">
    select * from user
  </select>
</mapper>
```

**操作类：**

```java
package cn.yang.test;

import cn.yang.User;
import cn.yang.dao.UserDao;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;
import java.util.List;

public class MybatisTest {
    public static void main(String[] args) throws Exception {
        //读取配置文件
        InputStream in = Resources.getResourceAsStream("SqlConfig.xml");
        //创建SqlSessionFactory工厂对象，Mybatis提供有一个函数来创建工厂，接收配置文件信息
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(in);
        //使用工厂生产SqlSession对象
        SqlSession session = factory.openSession();
        //使用session创建UserDao接口的代理对象
        UserDao userDao = session.getMapper(UserDao.class);
        //使用代理对象执行方法
        List<User> users = userDao.findAll();
        for (User user:users){
            System.out.println(user);
        }
        session.close();
        in.close();
    }
}
```

## 使用注解配置

使用注解后就不需要UserDao的具体配置文件了，但是还是需要主配置文件提供数据库连接。另外，主配置中需要指定class

```xml
    <mappers>
        <mapper class="cn.yang.dao.UserDao"></mapper>
    </mappers>
```

然后只需在UserDao接口方法上添加一个注解即可

```java
//用户使用接口
public interface UserDao {
    @Select("select * from user")
    List<User> findAll();
}
```

以上两种方法都是不需要写UserDao实现类的。但是mybatis是支持写实现类的。