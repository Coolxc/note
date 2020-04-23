package cn.yang.cao.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
public class SellerRegisterForm {

    @NotEmpty(message = "用户名不能为空")
    private String username;

    @NotEmpty(message = "密码不能为空")
    private String password;

    @Pattern(regexp = "\\d{11}", message = "电话格式不正确")
    @NotEmpty(message = "电话不能为空")
    private String phone;
}
