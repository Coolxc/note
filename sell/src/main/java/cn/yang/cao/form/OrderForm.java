package cn.yang.cao.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
//买家表单
@Data
public class OrderForm {
    //买家姓名
    @NotEmpty(message = "姓名不要丢")
    private String name;

    //买家手机号
    @NotEmpty(message = "手机号必填")
    private String phone;

    //买家地址
    @NotEmpty(message = "地址必填")
    private String address;

    //买家微信openid
    @NotEmpty(message = "id必填")
    private String id;

    //购物车
    @NotEmpty(message = "购物车不能丢")
    private String items;
}
