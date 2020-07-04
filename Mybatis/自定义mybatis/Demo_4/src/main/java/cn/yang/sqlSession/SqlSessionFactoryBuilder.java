package cn.yang.sqlSession;

import cn.yang.config.Configuration;
import cn.yang.utils.XMLConfigBuilder;

import java.io.InputStream;

public class SqlSessionFactoryBuilder {
    public SqlSessionFactory build(InputStream config){
        Configuration cfg = XMLConfigBuilder.loadConfiguration(config);
        return new ISqlSessionFactory(cfg);
    }

    public SqlSession openSession(){
        return null;
    }
}
