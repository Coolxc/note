package cn.yang.sqlSession;

import cn.yang.config.Configuration;

//SqlSessionFactory接口的实现类
public class ISqlSessionFactory implements SqlSessionFactory{
    private Configuration cfg;
    public ISqlSessionFactory(Configuration cfg){
        this.cfg = cfg;
    }

    //创建一个数据库连接对象
    public SqlSession openSession() {
        return new ISqlSession(cfg);
    }
}
