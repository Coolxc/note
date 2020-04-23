package cn.yang.cao.service;

import cn.yang.cao.dataobject.OrderMaster;
import cn.yang.cao.dto.OrderDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.jaxb.SpringDataJaxb;

public interface OrderService {
    //查询单个订单
    OrderDTO findOne(String orderId);

    //创建订单DTO: data transfer object
    OrderDTO create(OrderDTO orderDTO);

    //查询订单列表
    Page<OrderDTO> findList(String buyerOpenid, Pageable pageable);

    //支付订单
    OrderDTO paid(OrderDTO orderDTO);

    //完结订单
    OrderDTO finish(OrderDTO orderDTO);

    //cancel订单
    OrderDTO cancel(OrderDTO orderDTO);

    //查询所有用户订单
    Page<OrderDTO> findList(Pageable pageable);
}
