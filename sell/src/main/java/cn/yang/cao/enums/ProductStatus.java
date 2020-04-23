package cn.yang.cao.enums;

import lombok.Getter;

@Getter
public enum ProductStatus implements CodeEnum{
    UP(0,"在架"), DOWN(1,"下架")
    ;
    private Integer code;
    private String msg;

    ProductStatus(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
