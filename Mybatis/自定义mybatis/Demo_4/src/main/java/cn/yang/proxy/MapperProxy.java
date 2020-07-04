package cn.yang.proxy;

import cn.yang.User;
import cn.yang.config.Configuration;
import cn.yang.config.Mapper;
import cn.yang.utils.Executor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.Map;

public class MapperProxy implements InvocationHandler {
    private Connection con;
    /**
     * 用来增强被代理对象行为的动态代理类
     * 我们现在想要的增强就是执行SQL语句，那么就需要用到Configrution中的mappers属性，
     * 其中包装了sql和被包装类的全限定类名。通过构造方法获取。mappers的key为全限定类名加方法名，value为SQL语句和返回值的全限定类名
     */
    private Map<String, Mapper> map;
    public MapperProxy(Map<String, Mapper> map, Connection con){
        this.map = map;
    }
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        String methodName = method.getName();
        String className = method.getDeclaringClass().getName();
        String key = className + "." + methodName;

        Mapper mapper = map.get(key);

        if (mapper == null){
            throw new IllegalArgumentException("传入的参数有误");
        }

        return new Executor().selectList(mapper,con);
    }
}
