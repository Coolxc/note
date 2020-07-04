package cn.yang.utils;

import cn.yang.config.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;

//创建数据连接类
public class DataSourceUtil {
    public static Connection getConnection(Configuration cfg) {
        try {
            Class.forName(cfg.getDriver());
            return DriverManager.getConnection(cfg.getUrl(), cfg.getUsername(), cfg.getPassword());
        } catch (Exception e) {
            System.out.println("数据库连接失败");
            e.printStackTrace();
        }
        return null;
    }
}
