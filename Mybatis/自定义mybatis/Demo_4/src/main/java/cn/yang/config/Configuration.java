package cn.yang.config;

import java.util.HashMap;
import java.util.Map;

//自定义mybatic的配置类
public class Configuration {
    private String driver;
    private String url;
    private String username;
    private String password;
    //Map的key为全限定类名加方法名，value为SQL语句和返回值的全限定类名
    private Map<String,Mapper> mapppers = new HashMap<String, Mapper>();

    public Map<String, Mapper> getMappers() {
        return mapppers;
    }

    public void setMappers(Map<String, Mapper> map) {
        this.mapppers.putAll(map);  //putAll会将传入的map复制一份到mappers中。
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Configuration{" +
                "driver='" + driver + '\'' +
                ", url='" + url + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", mapppers=" + mapppers +
                '}';
    }
}
