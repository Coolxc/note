package cn.yang.test;

import cn.yang.User;
import cn.yang.dao.UserDao;
import cn.yang.io.Resources;
import cn.yang.sqlSession.SqlSession;
import cn.yang.sqlSession.SqlSessionFactory;
import cn.yang.sqlSession.SqlSessionFactoryBuilder;

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
