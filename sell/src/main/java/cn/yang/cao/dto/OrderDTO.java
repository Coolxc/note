package cn.yang.cao.dto;

import cn.yang.cao.dataobject.OrderDetail;
import cn.yang.cao.enums.OrderStatus;
import cn.yang.cao.enums.PayStatus;
import cn.yang.cao.utils.EnumUtil;
import cn.yang.cao.utils.serializer.Date2Long;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

//DTO类似于数据库的中间表
@Data
//JsonInclude注解后将不返回前端值为null的字段，更方便前端处理。这里不需要单独注解了，已经修改配置文件全局作用了
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDTO {
    private String orderId;
    private String buyerName;
    private String buyerPhone;
    private String buyerAddress;
    private String buyerId;
    private BigDecimal orderAmount;
    //订单状态，默认为0，新订单
    private Integer orderStatus;
    //支付状态，默认为0，等待支付
    private Integer payStatus;
    @JsonSerialize(using = Date2Long.class)
    private Date createTime;
    @JsonSerialize(using = Date2Long.class)
    private Date updateTime;
    private List<OrderDetail> orderDetailList;

    @JsonIgnore
    //获取具体的OrderStatus枚举对象
    public OrderStatus getOrderStatusEnum() {
        return EnumUtil.getByCode(orderStatus, OrderStatus.class);
    }

    //@JsonIgnore 注解后在给前端对象转成json时就不会将该方法也按字段属性转
    @JsonIgnore
    //获取具体的OrderStatus枚举对象
    public PayStatus getPayStatusEnum() {
        return EnumUtil.getByCode(payStatus, PayStatus.class);
    }
}
