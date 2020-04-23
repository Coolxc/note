package cn.yang.cao.service.impl;

import cn.yang.cao.dataobject.OpenUser;
import cn.yang.cao.dataobject.OrderMaster;
import cn.yang.cao.dto.OrderDTO;
import cn.yang.cao.enums.OrderStatus;
import cn.yang.cao.enums.PayStatus;
import cn.yang.cao.repository.OpenUserRepository;
import cn.yang.cao.repository.OrderMasterRepository;
import cn.yang.cao.service.OpenUserService;
import cn.yang.cao.utils.HashUtil;
import org.aspectj.apache.bcel.classfile.Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.ArrayList;
import java.util.List;

@Service
public class OpenUserServiceImpl implements OpenUserService {

    @Autowired
    OpenUserRepository openUserRepository;

    @Autowired
    OrderMasterRepository orderMasterRepository;

    private List<OrderMaster> AllOrder(String phone){
        List<OrderMaster> orderMasterList = orderMasterRepository.findByBuyerPhone(phone);
        return orderMasterList;
    }

    @Override
    public List<OrderMaster> findAllSuccess(String phone) {
        List<OrderMaster> allSuccess = new ArrayList<>();
        for (OrderMaster orderMaster : AllOrder(phone)){
            if (orderMaster.getOrderStatus().equals(OrderStatus.FINISHED)){
                allSuccess.add(orderMaster);
            }
        }
        return allSuccess;
    }

    @Override
    public List<OrderMaster> findAllWaitPay(String phone) {
        List<OrderMaster> allWaitPay = new ArrayList<>();
        for (OrderMaster orderMaster : AllOrder(phone)){
            if (orderMaster.getPayStatus().equals(PayStatus.WAIT.getCode())){
                allWaitPay.add(orderMaster);
            }
        }
        return allWaitPay;
    }

    @Override
    public List<OrderMaster> findAllRefund(String openid) {
        //TODO
        return null;
    }

    @Override
    public OpenUser findOpenUser(String phone) {
        return openUserRepository.findOpenUserByPhone(phone);
    }

    @Override
    public List<OrderMaster> findAllCancel(String phone) {
        List<OrderMaster> allCancel = new ArrayList<>();
        for (OrderMaster orderMaster : AllOrder(phone)){
            if (orderMaster.getPayStatus().equals(OrderStatus.CANCEL.getCode())){
                allCancel.add(orderMaster);
            }
        }
        return allCancel;
    }

    @Override
    public OpenUser create(OpenUser openUser) {
        openUser.setPassword(HashUtil.hash(openUser.getPassword()));
        OpenUser result = openUserRepository.save(openUser);
        return result;
    }

    @Override
    public OpenUser delete(String phone) {
        OpenUser openUser = findOpenUser(phone);
        openUserRepository.delete(openUser);
        return openUser;
    }
}
