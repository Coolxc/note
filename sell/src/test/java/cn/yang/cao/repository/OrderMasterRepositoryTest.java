package cn.yang.cao.repository;

import cn.yang.cao.dataobject.OrderMaster;
import cn.yang.cao.enums.PayStatus;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class OrderMasterRepositoryTest {
    @Autowired
    private OrderMasterRepository repository;

    @Test
    void findByBuyerOpenid() {
        PageRequest pageRequest = PageRequest.of(0, 2);
        Page page = repository.findByBuyerPhone("188", pageRequest);
        System.out.println(page.getTotalElements());
    }
    @Test
    public void saveTest(){
        OrderMaster orderMaster = new OrderMaster();
        orderMaster.setOrderId("1");
        orderMaster.setBuyerName("杨优秀");
        orderMaster.setBuyerAddress("安阳");
        orderMaster.setBuyerPhone("18537222222");
        orderMaster.setOrderAmount(new BigDecimal(33333));
        orderMaster.setPayStatus(PayStatus.SUCCESS.getCode());
        repository.save(orderMaster);
    }
}