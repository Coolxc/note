package cn.yang.cao.utils;

import cn.yang.cao.dataobject.OpenUser;
import cn.yang.cao.dataobject.OrderDetail;
import cn.yang.cao.dto.OrderDTO;
import cn.yang.cao.form.OrderForm;
import cn.yang.cao.form.UserForm;
import cn.yang.cao.form.UserRegisterForm;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class UserForm2User {
    public static OpenUser convert(UserRegisterForm userForm) {
        OpenUser user = new OpenUser();
        List<OrderDetail> orderDetailList = new ArrayList<>();

        user.setUsername(userForm.getUsername());
        user.setPhone(userForm.getPhone());
        user.setAddress(userForm.getAddress());
        user.setPassword(userForm.getPassword());
        return user;
    }
}
