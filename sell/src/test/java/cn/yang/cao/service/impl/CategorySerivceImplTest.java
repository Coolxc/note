package cn.yang.cao.service.impl;

import cn.yang.cao.dataobject.ProductInfo;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class CategorySerivceImplTest {
    @Autowired
    private ProductInfoServiceImpl productInfoService;

    @Test
    void findOne() {
        ProductInfo productInfo = productInfoService.findOne("1");
        Assert.assertEquals("1",productInfo.getProductId());

    }

    @Test
    void findAll() {
        //PageRequest继承自实现Page接口的类。参数为第0页，一页有两个记录
        PageRequest pageRequest = PageRequest.of(0,2);
        Page<ProductInfo> productInfos = productInfoService.findAll(pageRequest);
        System.out.println(productInfos.getTotalElements());
    }

    @Test
    void findByCategoryTypeIn() {
        //查找所有上架的商品
        List<ProductInfo> productInfo = productInfoService.findUpAll();
        System.out.println(productInfo);
    }

    @Test
    void save() {
        ProductInfo productInfo = new ProductInfo();
        productInfo.setProductId("4");
        productInfo.setCategoryType(3);
        productInfo.setProductDescription("这是一个大大大大大大");
        productInfo.setProductName("大奶子");
        productInfo.setProductPrice(new BigDecimal(9999.2));
        productInfo.setProductStatus(0);
        productInfo.setProductStock(999);
        productInfoService.save(productInfo);
    }
}