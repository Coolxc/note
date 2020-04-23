package cn.yang.cao.controller;

import cn.yang.cao.VO.ResultVO;
import cn.yang.cao.dataobject.OrderMaster;
import cn.yang.cao.dto.OrderDTO;
import cn.yang.cao.enums.ResultEnum;
import cn.yang.cao.exception.SellException;
import cn.yang.cao.form.OrderForm;
import cn.yang.cao.service.BuyerService;
import cn.yang.cao.service.OrderService;
import cn.yang.cao.service.impl.BuyerServiceImpl;
import cn.yang.cao.utils.OrderForm2OrderDTO;
import cn.yang.cao.utils.ResultVOUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/buyer/order")
@Slf4j
public class BuyerOrderController {

    @Autowired
    OrderService orderService;

    @Autowired
    BuyerService buyerService;

    //创建订单。   @Valid 和 BindingResult 是一一对应的，如果有多个@Valid，那么每个@Valid后面跟着的BindingResult就是这个@Valid的验证结果，顺序不能乱
    @PostMapping("/creat")
    public ResultVO<Map<String, String>> create(@Valid OrderForm orderForm, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            log.error("订单参数错误：orderForm = {}",orderForm);
            throw new SellException(ResultEnum.PARAM_ERROR.getCode(),
                    bindingResult.getFieldError().getDefaultMessage());
        }

        OrderDTO orderDTO = OrderForm2OrderDTO.convert(orderForm);

        if (CollectionUtils.isEmpty(orderDTO.getOrderDetailList())){
            log.error("【创建订单】 购物车不能为空 Buyer_name = {}",orderDTO.getBuyerName());
            throw new SellException(ResultEnum.CART_IS_NONE);
        }

        OrderDTO creatResult = orderService.create(orderDTO);
        Map<String, String> map = new HashMap<>();
        //data中含有一个orderId字段
        map.put("orderId", creatResult.getOrderId());

        return ResultVOUtil.success(map);
    }

    //订单列表
    @GetMapping("/list")
    public ResultVO<List<OrderDTO>> list(@RequestParam("phone") String phone,
                                         @RequestParam(value = "page", defaultValue = "0") Integer page,
                                         @RequestParam(value = "size", defaultValue = "5") Integer size) {
        if (phone == null){
            log.error("【查询订单列表】 open_id为空");
            throw new SellException(ResultEnum.PARAM_ERROR);
        }
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<OrderDTO> orderDTOPage = orderService.findList(phone, pageRequest);
        //返回该页的所有内容
        return ResultVOUtil.success(orderDTOPage.getContent());
    }

    //订单详情
    @GetMapping("/detail")
    //需要传入订单id和用户id以验证身份
    public ResultVO<OrderDTO> detail(@RequestParam("orderid") String orderid,
                                     @RequestParam("id") String id){
        OrderDTO result = buyerService.findOrderOne(id, orderid);
        return ResultVOUtil.success(result);
    }

    //取消订单
    @GetMapping("/cancel")
    public ResultVO cancel(@RequestParam("orderId") String orderid,
                           @RequestParam("buyerId") String id){
        buyerService.cancelOrder(id, orderid);
        return ResultVOUtil.success();
    }
}
