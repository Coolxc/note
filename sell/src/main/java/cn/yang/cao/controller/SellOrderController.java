package cn.yang.cao.controller;

import cn.yang.cao.dataobject.OrderDetail;
import cn.yang.cao.dto.OrderDTO;
import cn.yang.cao.exception.SellException;
import cn.yang.cao.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

//卖家端订单
@Controller
@RequestMapping("/seller/order")
@Slf4j
public class SellOrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/list")
    /**
     * @page: 第几页，从第一页开始
     * @size：一页有多少条
     */
    public String list(@RequestParam(value = "page",defaultValue = "1") Integer page,
                             @RequestParam(value = "size", defaultValue = "10") Integer size,
                             Map<String, Object> map){
        PageRequest pageRequest = PageRequest.of(page-1, size);
        Page<OrderDTO> orderDTOPage = orderService.findList(pageRequest);
        map.put("orderDTOPage", orderDTOPage);
        map.put("currentPage", page);
        map.put("size", size);
        return "/order/list";
    }

    @GetMapping("/cancel")
    /**
     * 取消订单
     */
    public String cancel(@RequestParam("orderId") String orderId,
                         Map<String, Object> map){
        try{
            //捕获异常就可以直接跳转
            OrderDTO orderDTO = orderService.findOne(orderId);
            orderService.cancel(orderDTO);
        }catch (SellException e){
            log.error("卖家端取消订单失败，OrderId：{}",orderId);
            map.put("msg", e.getMessage());
            map.put("url", "/seller/order/list");
            return "/common/error";
        }
        //跳转到成功页面
        map.put("msg","取消订单成功");
        map.put("url","/seller/order/list");
        return "/common/success";
    }


    @GetMapping("/detail")
    /**
     * 查询订单详情
     */
    public String detail(@RequestParam("orderId") String orderId,
                         Map<String, Object> map){
        OrderDTO orderDTO = new OrderDTO();
        try{
            orderDTO = orderService.findOne(orderId);
        }catch (SellException e){
            log.error("查询订单失败，OrderId：{}",orderId);
            map.put("msg", e.getMessage());
            map.put("url", "/seller/order/list");
            return "/common/error";
        }
        map.put("orderDTO", orderDTO);
        return "/order/detail";
    }

    @GetMapping("/finish")
    public String finish(@RequestParam("orderId") String orderId,
                         Map<String, Object> map){
        OrderDTO orderDTO = new OrderDTO();
        try{
            orderDTO = orderService.findOne(orderId);
            orderService.finish(orderDTO);
        }catch (SellException e){
            log.error("取消订单失败，OrderId：{}",orderId);
            map.put("msg", e.getMessage());
            map.put("url", "/seller/order/list");
            return "/common/error";
        }
        map.put("url","/seller/order/detail?orderId=" + orderId);
        map.put("msg", "订单已完结");
        return "/common/success";
    }
}
