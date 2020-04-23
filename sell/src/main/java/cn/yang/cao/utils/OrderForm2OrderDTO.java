package cn.yang.cao.utils;

import cn.yang.cao.dataobject.OrderDetail;
import cn.yang.cao.dto.OrderDTO;
import cn.yang.cao.enums.ResultEnum;
import cn.yang.cao.exception.SellException;
import cn.yang.cao.form.OrderForm;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * 将表单内容转换为OrderDTO对象实现数据封装
 */
@Slf4j
public class OrderForm2OrderDTO {
    public static OrderDTO convert(OrderForm orderForm){
        Gson gson = new Gson();
        OrderDTO orderDTO = new OrderDTO();
        List<OrderDetail> orderDetailList = new ArrayList<>();

        orderDTO.setBuyerName(orderForm.getName());
        orderDTO.setBuyerPhone(orderForm.getPhone());
        orderDTO.setBuyerAddress(orderForm.getAddress());
        orderDTO.setBuyerId(orderForm.getId());

        try {
            //new TypeToken时采用匿名内部类的形式，所以有大括号。将表单中的json格式购物车转换为List<OrderDetail>
            // 他的构造方法是私有化的，无法直接new,只能曹勇匿名内部类的方法
            orderDetailList = gson.fromJson(orderForm.getItems(), new TypeToken<List<OrderDetail>>() {
            }.getType());
        }catch (Exception e){
            log.error("【表单转换对象发生错误】  Form = {}", orderForm);
            throw new SellException(ResultEnum.PARAM_ERROR);
        }
        orderDTO.setOrderDetailList(orderDetailList);
        return orderDTO;
    }


}
