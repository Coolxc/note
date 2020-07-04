package cn.yang.sqlSession;

import cn.yang.User;
import cn.yang.config.Configuration;
import cn.yang.dao.UserDao;
import cn.yang.proxy.MapperProxy;
import cn.yang.utils.DataSourceUtil;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

//SqlSession接口的实现类
public class ISqlSession implements SqlSession{
    private Connection con;
    private Configuration cfg;
    public ISqlSession(Configuration cfg) {
        this.cfg = cfg;
        con = DataSourceUtil.getConnection(cfg); //获取数据库连接

    }
    //创建代理对象
    public <T> T getMapper(Class<T> daoInterfaceClass) {
        return (T)Proxy.newProxyInstance(daoInterfaceClass.getClassLoader(), new Class<?>[]{daoInterfaceClass},new MapperProxy(cfg.getMappers(),con));
    }

    //释放资源
    public void close() {
        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
