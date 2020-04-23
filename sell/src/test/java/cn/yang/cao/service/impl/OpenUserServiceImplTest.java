package cn.yang.cao.service.impl;

import cn.yang.cao.dataobject.OpenUser;
import cn.yang.cao.dataobject.OrderMaster;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
class OpenUserServiceImplTest {

    @Autowired
    OpenUserServiceImpl service;

    private final static String OPEN_ID = "1";

    @Test
    void findAllSuccess() {
        List<OrderMaster> orderMasterList = service.findAllSuccess(OPEN_ID);
        Assert.assertNotNull(orderMasterList);
    }

    @Test
    void findAllWaitPay() {
        List<OrderMaster> orderMasterList = service.findAllWaitPay(OPEN_ID);
        Assert.assertNotNull(orderMasterList);
    }


    @Test
    void findOpenUser() {
        OpenUser openUser = service.findOpenUser(OPEN_ID);
        Assert.assertNotNull(openUser);
    }

    @Test
    void findAllCancel() {
        List<OrderMaster> result = service.findAllCancel(OPEN_ID);
        Assert.assertNotNull(result);
    }

    @Test
    void saveUser() {
        OpenUser openUser = new OpenUser();
        openUser.setPassword("111");
        openUser.setAddress("安阳");
        openUser.setPhone("189873113");
        OpenUser result = service.create(openUser);
        Assert.assertNotNull(result);
    }
}