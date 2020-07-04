package cn.yang.dao;

import cn.yang.User;
import cn.yang.annotation.Select;
//import org.apache.ibatis.annotations.Select;

import java.util.List;

//用户使用接口
public interface UserDao {
    @Select("select * from user")
    List<User> findAll();

}
