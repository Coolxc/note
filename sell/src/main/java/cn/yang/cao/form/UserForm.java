package cn.yang.cao.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class UserForm {

    @NotEmpty(message = "密码必填")
    private String password;

    //买家手机号
    @NotEmpty(message = "手机号必填")
    private String phone;
}
