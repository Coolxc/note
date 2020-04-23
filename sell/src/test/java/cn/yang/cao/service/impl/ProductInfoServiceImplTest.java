package cn.yang.cao.service.impl;

import cn.yang.cao.dataobject.ProductInfo;
import cn.yang.cao.service.ProductInfoService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class ProductInfoServiceImplTest {

    @Autowired
    ProductInfoService productInfoService;

    @Test
    void findOne() {
    }

    @Test
    void findAll() {
    }

    @Test
    void save() {
    }

    @Test
    void findUpAll() {
    }

    @Test
    void increaseStock() {
    }

    @Test
    void decreaseStock() {
    }

    @Test
    void onSale() {
    }

    @Test
    void offSale() {
        ProductInfo productInfo = productInfoService.offSale("1");
    }
}