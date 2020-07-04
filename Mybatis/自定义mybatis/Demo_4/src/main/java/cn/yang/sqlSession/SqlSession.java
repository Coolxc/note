package cn.yang.sqlSession;

public interface SqlSession {
    //根据参数创建一个代理对象，参数为dao的Class字节码
    <T>T getMapper(Class<T> daoInterfaceClass);
    //释放资源
    void close();

}
