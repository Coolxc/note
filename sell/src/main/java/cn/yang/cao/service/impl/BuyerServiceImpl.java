package cn.yang.cao.service.impl;

import cn.yang.cao.dto.OrderDTO;
import cn.yang.cao.enums.ResultEnum;
import cn.yang.cao.exception.SellException;
import cn.yang.cao.repository.OrderMasterRepository;
import cn.yang.cao.service.BuyerService;
import cn.yang.cao.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BuyerServiceImpl implements BuyerService {

    @Autowired
    private OrderService orderService;

    @Override
    public OrderDTO findOrderOne(String id, String orderId) {
        return checkOrderOwner(id, orderId);
    }

    @Override
    public OrderDTO cancelOrder(String id, String orderId) {
        OrderDTO orderDTO = checkOrderOwner(id, orderId);
        if (orderDTO == null) {
            log.error("【取消订单】查不到该订单, orderId={}", orderId);
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }

        return orderService.cancel(orderDTO);
    }

    private OrderDTO checkOrderOwner(String id, String orderId) {
        OrderDTO orderDTO = orderService.findOne(orderId);
        if (orderDTO == null) {
            return null;
        }
        //判断是否是自己的订单
        if (!orderDTO.getBuyerId().equalsIgnoreCase(id)) {
            log.error("【查询订单】订单的openid不一致. id={}, orderDTO={}", id, orderDTO);
            throw new SellException(ResultEnum.ORDER_OWNER_ERROR);
        }
        return orderDTO;
    }
}
