package cn.yang.cao.dataobject;

import cn.yang.cao.enums.OrderStatus;
import cn.yang.cao.enums.PayStatus;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Proxy;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Proxy(lazy = false)
@DynamicUpdate
public class OrderMaster { //单个用户总订单，根据orderId能查到订单详细内容
    @Id
    private String orderId;
    private String buyerName;
    private String buyerPhone;
    private String buyerAddress;
    private String buyerId;
    //订单总金额
    private BigDecimal orderAmount;
    //订单状态，默认为0，新订单
    private Integer orderStatus = OrderStatus.NEW.getCode();
    //支付状态，默认为0，等待支付
    private Integer payStatus = PayStatus.WAIT.getCode();
    private Date createTime;
    private Date updateTime;
}
