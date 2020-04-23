package cn.yang.cao.repository;

import cn.yang.cao.dataobject.OrderDetail;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@RunWith(SpringRunner.class)
@SpringBootTest
class OderDetailRepositoryTest {
    @Autowired
    OrderDetailRepository repository;

    @Test
    void findByOrderId() {
        List<OrderDetail> orderDetails = repository.findByOrderId("1");
        Assert.assertNotEquals(0, orderDetails.size());
    }
    @Test
    void saveTest(){
        OrderDetail order = new OrderDetail();
        order.setDetailId("1");
        order.setOrderId("1");
        order.setProductId("1");
        order.setProductName("大奶子");
        order.setProductPrice(new BigDecimal(999));
        order.setProductQuantity(333);
        OrderDetail result = repository.save(order);
        Assert.assertNotNull(result);
    }
}