package cn.yang.cao.service.impl;

import cn.yang.cao.dataobject.OrderDetail;
import cn.yang.cao.dataobject.OrderMaster;
import cn.yang.cao.dataobject.ProductInfo;
import cn.yang.cao.dto.CartDTO;
import cn.yang.cao.dto.OrderDTO;
import cn.yang.cao.enums.OrderStatus;
import cn.yang.cao.enums.PayStatus;
import cn.yang.cao.enums.ResultEnum;
import cn.yang.cao.exception.SellException;
import cn.yang.cao.repository.OrderDetailRepository;
import cn.yang.cao.repository.OrderMasterRepository;
import cn.yang.cao.service.OrderService;
import cn.yang.cao.service.ProductInfoService;
import cn.yang.cao.utils.KeyUtil;
import cn.yang.cao.utils.OrderMaster2OrderDTO;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private ProductInfoService productInfoService;

    @Autowired
    private OrderMasterRepository orderMasterRepository;

    @Override
    public OrderDTO findOne(String orderId) {
        OrderMaster orderMaster = orderMasterRepository.findById(orderId).orElse(null);
        if (orderMaster == null){
            throw new SellException(ResultEnum.ORDER_NOT_EXISTS);
        }
        List<OrderDetail> orderDetailList = orderDetailRepository.findByOrderId(orderId);
        if (orderDetailList == null){
            throw new SellException(ResultEnum.ORDERDETAIL_NOT_EXISTS);
        }
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOrderDetailList(orderDetailList);
        BeanUtils.copyProperties(orderMaster, orderDTO);
        return orderDTO;
    }

    @Override
    @Transactional //添加事务，发生异常时回滚
    public OrderDTO create(OrderDTO orderDTO) {

        String orderId = KeyUtil.genPriKey(); //生成orderId，orderMaster和orderDetail都要用到
        BigDecimal orderAmount = new BigDecimal(BigInteger.ZERO);

        //1. 查询商品(数量，价格)；
        for (OrderDetail orderDetail: orderDTO.getOrderDetailList()) {
            ProductInfo productInfo =  productInfoService.findOne(orderDetail.getProductId());
            if (productInfo == null) {
                throw new SellException(ResultEnum.PRODUCT_NOT_EXISTS);
            }

            //2. 计算订单总价
            orderAmount = productInfo.getProductPrice()
                    .multiply(new BigDecimal(orderDetail.getProductQuantity()))
                    .add(orderAmount);

            //订单详情入库
            orderDetail.setDetailId(KeyUtil.genPriKey());
            orderDetail.setOrderId(orderId);
            BeanUtils.copyProperties(productInfo, orderDetail);
            orderDetailRepository.save(orderDetail);
        }
        //写入订单数据库（orderMaster）
        OrderMaster orderMaster = new OrderMaster();
        //orderDTO包含用户总订单详细内容(name,address....)
        orderDTO.setOrderId(orderId);
        BeanUtils.copyProperties(orderDTO, orderMaster);

        orderMaster.setOrderAmount(orderAmount);
        orderMaster.setPayStatus(PayStatus.WAIT.getCode());
        orderMaster.setOrderStatus(OrderStatus.NEW.getCode());
        orderMasterRepository.save(orderMaster); //写入数据库

        //4. 减库存。collection.stream用法见note/java/mapReduce.md
        List<CartDTO> cartDTOList = orderDTO.getOrderDetailList().stream().map(e->
                new CartDTO(e.getProductId(), e.getProductQuantity())).collect(Collectors.toList());//得到购物车列表
        productInfoService.decreaseStock(cartDTOList);
        return orderDTO;
    }

    @Override
    @Transactional
    public Page<OrderDTO> findList(String phone, Pageable pageable) {
        //需要转换成OrderDTO返回,因为DTO还包含有订单详细信息
        Page<OrderMaster> orderMasters = orderMasterRepository.findByBuyerPhone(phone, pageable);
        List<OrderDTO> orderDTOList = OrderMaster2OrderDTO.convertList(orderMasters.getContent());
        //PageImpl参数（要分页的内容，分页信息，所有记录总数）
        return new PageImpl<OrderDTO>(orderDTOList, pageable, orderMasters.getTotalElements());
    }

    @Override
    @Transactional
    public OrderDTO paid(OrderDTO orderDTO) {
        //判断订单状态，是否为新订单。只有新订单才能支付
        if (!orderDTO.getOrderStatus().equals(OrderStatus.NEW.getCode())){
            log.error("【订单支付完成】但订单状态不对：OrderId={}, OrderStatus={}",orderDTO.getOrderId(), orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }

        //判断支付状态是否为等待支付
        if (!orderDTO.getPayStatus().equals(PayStatus.WAIT.getCode())){
            log.error("【订单支付完成】但订单支付状态错误：Order_id={}, Order_Pay_status={}", orderDTO.getOrderId(), orderDTO.getPayStatus());
            throw new SellException(ResultEnum.ORDER_PAY_ERROR);
        }
        //改变订单支付状态
        OrderMaster orderMaster = new OrderMaster();
        orderDTO.setPayStatus(PayStatus.SUCCESS.getCode());
        BeanUtils.copyProperties(orderDTO, orderMaster);
        OrderMaster result = orderMasterRepository.save(orderMaster);
        if (result == null){
            log.error("【订单支付完成】但订单支付状态更新失败：Order={}",orderMaster);
        }
        return orderDTO;
    }

    @Override
    @Transactional
    public OrderDTO finish(OrderDTO orderDTO) {
        //判断订单状态
        if (!orderDTO.getOrderStatus().equals(OrderStatus.NEW.getCode())){
            log.error("订单状态不对：OrderId={}, OrderStatus={}",orderDTO.getOrderId(), orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }

        //修改订单状态
        OrderMaster orderMaster = new OrderMaster();
        orderDTO.setOrderStatus(OrderStatus.FINISHED.getCode());
        BeanUtils.copyProperties(orderDTO, orderMaster);
        OrderMaster result = orderMasterRepository.save(orderMaster);
        if (result == null){
            log.error("订单更新失败：Order={}",orderMaster);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAILD);
        }
        return orderDTO;
    }

    @Override
    @Transactional
    public OrderDTO cancel(OrderDTO orderDTO) {
        //将DTO转换为OrderMaster
        OrderMaster orderMaster = new OrderMaster();
        //判断订单状态，如果已完结那么不可以cancel
        if (!orderDTO.getOrderStatus().equals(OrderStatus.NEW.getCode())){
            log.error("订单状态不对 CALCEL失败，OrderId={}, OrderStatus={}",orderDTO.getOrderId(),orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }

        //修改订单状态
        orderDTO.setOrderStatus(OrderStatus.CANCEL.getCode());
        BeanUtils.copyProperties(orderDTO, orderMaster);
        OrderMaster result = orderMasterRepository.save(orderMaster);
        if (result == null){
            throw new SellException(ResultEnum.ORDER_UPDATE_FAILD);
        }

        //将商品返回库存，先判断订单中有无商品
        if (CollectionUtils.isEmpty(orderDTO.getOrderDetailList())){
            throw new SellException(ResultEnum.ORDER_DETAIL_ERROR);
        }
        List<CartDTO> cartDTOList = orderDTO.getOrderDetailList().stream()
                .map(e->new CartDTO(e.getProductId(), e.getProductQuantity())).collect(Collectors.toList());

        productInfoService.increaseStock(cartDTOList);

        //如果已支付，需要退款
        if (orderDTO.getOrderStatus().equals(PayStatus.SUCCESS.getCode())){
            //TODO
        }
        return orderDTO;
    }

    @Override
    public Page<OrderDTO> findList(Pageable pageable) {
        Page<OrderMaster> orderMasterList = orderMasterRepository.findAll(pageable);
        List<OrderDTO> orderDTOList = OrderMaster2OrderDTO.convertList(orderMasterList.getContent());
        return new PageImpl(orderDTOList, pageable, orderMasterList.getTotalElements());
    }
}
