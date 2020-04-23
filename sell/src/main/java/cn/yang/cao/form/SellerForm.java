package cn.yang.cao.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class SellerForm {

    @NotEmpty(message = "电话未填")
    private String phone;

    @NotEmpty(message = "密码未填")
    private String password;
}
