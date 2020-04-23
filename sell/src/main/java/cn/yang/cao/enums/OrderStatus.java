package cn.yang.cao.enums;

import lombok.Getter;

@Getter
public enum OrderStatus implements CodeEnum{
    NEW(0, "新订单"), FINISHED(1, "完结"), CANCEL(2, "取消");
    private Integer code;
    private String msg;

    OrderStatus(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
