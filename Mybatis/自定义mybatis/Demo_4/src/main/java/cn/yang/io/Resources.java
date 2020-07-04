package cn.yang.io;

import java.io.InputStream;

public class Resources {
    //根据传入的参数获取字节输入流
    public static InputStream getResourceAsStream(String name){
        //ClassLoader的getResourceStream方法会在classPath下寻找对应的资源文件，返回字节流
        return Resources.class.getClassLoader().getResourceAsStream(name);
    }
}
