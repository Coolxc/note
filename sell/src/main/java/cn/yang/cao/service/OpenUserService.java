package cn.yang.cao.service;

import cn.yang.cao.dataobject.OpenUser;
import cn.yang.cao.dataobject.OrderMaster;

import java.util.List;

public interface OpenUserService {

    //查找所有完结的订单
    List<OrderMaster> findAllSuccess(String phone);

    //查找所有等待支付的订单
    List<OrderMaster> findAllWaitPay(String phone);

    //查找所有已退款的订单
    List<OrderMaster> findAllRefund(String phone);

    //查找用户信息
    OpenUser findOpenUser(String phone);

    //查找所有已取消的订单
    List<OrderMaster> findAllCancel(String phone);

    //创建用户
    OpenUser create(OpenUser openUser);

    //销毁账户
    OpenUser delete(String phone);
}
