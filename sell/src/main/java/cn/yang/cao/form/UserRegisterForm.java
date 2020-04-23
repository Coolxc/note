package cn.yang.cao.form;

import lombok.Data;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
public class UserRegisterForm {
    //买家姓名
    @NotEmpty(message = "姓名不要丢")
    private String username;

    @NotEmpty(message = "密码必填")
    private String password;

    //买家手机号
    @Pattern(regexp = "\\d{11}", message = "手机号格式不正确")
    @NotEmpty(message = "手机号必填")
    private String phone;

    //买家地址
    @NotEmpty(message = "地址必填")
    private String address;
}
