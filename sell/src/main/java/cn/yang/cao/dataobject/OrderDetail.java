package cn.yang.cao.dataobject;

import lombok.Data;
import org.hibernate.annotations.Proxy;
import org.springframework.data.util.Lazy;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
@Entity
@Proxy(lazy = false)
@Data
public class OrderDetail { //每个订单的内容,同一个orderId对应多个orderDetail
    @Id
    private String detailId;
    private String orderId;
    private String productId;
    private String productName;
    private BigDecimal productPrice;
    private Integer productQuantity;
    private String productIcon;
}
