package cn.yang.cao.service.impl;

import cn.yang.cao.dataobject.OrderDetail;
import cn.yang.cao.dto.OrderDTO;
import cn.yang.cao.enums.OrderStatus;
import cn.yang.cao.enums.PayStatus;
import cn.yang.cao.enums.ResultEnum;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
class OrderServiceImplTest {
    @Autowired
    private OrderServiceImpl orderService;

    private final static String ORDER_ID = "158512259761739509";

    private final static String BUYER_OPEN_ID = "1";

    @Test
    void findOne() {
        OrderDTO orderDTO = orderService.findOne(ORDER_ID);
        log.info("查询单个订单 ：{}",orderDTO);
        Assert.assertNotNull(orderDTO);
    }

    @Test
    void create() throws Exception{
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setBuyerAddress("安阳");
        orderDTO.setBuyerName("杨优秀");
        orderDTO.setBuyerId(BUYER_OPEN_ID);
        orderDTO.setBuyerPhone("18888888");
        //购物车列表
        List<OrderDetail> orderDetailList = new ArrayList<>();
        OrderDetail order1 = new OrderDetail();
        order1.setProductId("1");
        order1.setProductQuantity(2);

        OrderDetail order2 = new OrderDetail();
        order2.setProductId("2");
        order2.setProductQuantity(4);

        orderDetailList.add(order1);
        orderDetailList.add(order2);
        orderDTO.setOrderDetailList(orderDetailList);

        OrderDTO result = orderService.create(orderDTO);
        log.info("创建订单：",result);

    }

    @Test
    void findOneIdList() {
        PageRequest request = PageRequest.of(0, 2);
        Page<OrderDTO> orderDTOList = orderService.findList(BUYER_OPEN_ID, request);
        Assert.assertNotEquals(0, orderDTOList.getTotalElements());
    }

    @Test
    void paid() {
        OrderDTO orderDTO = orderService.findOne(ORDER_ID);
        OrderDTO result = orderService.paid(orderDTO);
        log.info("订单已支付：{}",result);
        Assert.assertEquals(PayStatus.SUCCESS.getCode(), result.getPayStatus());

    }

    @Test
    void finish() {
        OrderDTO orderDTO = orderService.findOne(ORDER_ID);
        OrderDTO result = orderService.finish(orderDTO);
        log.info("订单信息完结：{}",result);
        Assert.assertEquals(OrderStatus.FINISHED.getCode(), result.getOrderStatus());
    }

    @Test
    void cancel() {
        OrderDTO orderDTO = orderService.findOne(ORDER_ID);
        OrderDTO result = orderService.cancel(orderDTO);
        log.info("订单：{}",result);
        Assert.assertEquals(OrderStatus.CANCEL.getCode(), result.getOrderStatus());

    }
    @Test
    public void findAll(){
        PageRequest pageRequest = PageRequest.of(0, 2);
        Page<OrderDTO> orderDTOList = orderService.findList(pageRequest);
        Assert.assertNotEquals(0, orderDTOList.getTotalElements());
    }
}