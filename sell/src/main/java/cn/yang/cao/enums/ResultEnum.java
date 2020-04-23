package cn.yang.cao.enums;

import lombok.Getter;

@Getter
public enum ResultEnum {
    PRODUCT_NOT_EXISTS(1, "商品不存在"),
    PRODUCT_STOCK_ERROR(2, "商品库存不足"),
    ORDER_NOT_EXISTS(3, "订单不存在"),
    ORDERDETAIL_NOT_EXISTS(4, "订单详ing不存在"),
    ORDER_STATUS_ERROR(5, "订单状态不对"),
    ORDER_UPDATE_FAILD(6, "订单状态更新失败"),
    ORDER_DETAIL_ERROR(7, "订单中木有商品"),
    ORDER_PAY_ERROR(8, "订单支付出错"),
    PARAM_ERROR(9, "参数错误"),
    CART_IS_NONE(10,"购物车中木有商品"),
    ORDER_NOT_EXIST(11,"订单不存在"),
    ORDER_OWNER_ERROR(12,"订单不属于当前用户"),
    WECHAT_MP_ERROR(13, "微信公众平台错误"),
    ALLREADY_ON_SALE(14,"商品已上架"),
    ALLREADY_OFF_SALE(15, "商品已下架"),
    LOG_IN_FAIL(16, "登陆失败"),
    USER_EXISTS(17, "用户已存在"),
    ;

    private Integer code;
    private String message;

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
