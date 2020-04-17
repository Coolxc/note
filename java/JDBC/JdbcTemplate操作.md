**利用Spring的IOC将配置写到XML文件中：**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/context
        https://www.springframework.org/schema/context/spring-context.xsd">
    <bean id="template" class="org.springframework.jdbc.core.JdbcTemplate">
        <!--调用Template中的setDataSource方法并设置值为ref引用的值-->
        <property name="dataSource" ref="Source"></property>
    </bean>
    <bean id="Source" class="org.springframework.jdbc.datasource.DriverManagerDataSource">
        <property name="driverClassName" value="com.mysql.jdbc.Driver"></property>
        <property name="url" value="jdbc:mysql://localhost:3306/douban"></property>
        <property name="password" value="123456"></property>
        <property name="username" value="yang"></property>
    </bean>
</beans>
```

**操作类：**

```java
public class JdbcMain {
    public static void main(String[] args) {
        ApplicationContext ac = new ClassPathXmlApplicationContext("Config.xml");
        JdbcTemplate jt = ac.getBean("template",JdbcTemplate.class);
        jt.update("insert into account(name,money)values(?,?)","ppppp",9783); //插入
        jt.update("update account set name=?,money=? where id=?","qqq",0,1); //更新
        jt.update("delete from account where id=?",3); //删除
        List<Account> accountList = jt.query("select * from account where money > ?", new AccoutMapper(),1000); //接收一个RowMapper接口对象
        //jt.query("select * from account where money > ?", new BeanPropertyRowMapper<Account>(Account.class),1000);   Spring给我们提供的RowMapper接口实现类
        for (Account account:accountList){
            System.out.println(account);
        }
        Account account = (Account) jt.query("select * from account where id = ?", new BeanPropertyRowMapper<Account>(Account.class),1); //使用提供的类查询一个
        System.out.println(account);

    }
}

//自己实现RowMapper接口，不使用Spring提供的实现类
class AccoutMapper implements RowMapper<Account>{
    /**
     * 把结果集的数据封装到Account对象中，然后由Spring把每个Account加到集合中
     * @param resultSet
     * @param i
     * @return
     * @throws SQLException
     */
    @Override
    public Account mapRow(ResultSet resultSet, int i) throws SQLException {
        Account account = new Account();
        account.setId(resultSet.getInt("id"));
        account.setMoney(resultSet.getFloat("money"));
        account.setName(resultSet.getString("name"));
        return account;
    }
}
```

**Account类：**

```java
package cn.yang;

import java.io.Serializable;

public class Account implements Serializable {
    private String name;
    private Integer id;
    private float money;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    @Override
    public String toString() {
        return "Account{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", money=" + money +
                '}';
    }
}

```

