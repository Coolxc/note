package cn.yang.cao.repository;

import cn.yang.cao.dataobject.ProductInfo;
import cn.yang.cao.enums.ProductStatus;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class ProductInfoRepositoryTest {
    @Autowired ProductInfoRepository repository;

    @Test
    void countProductInfoByProductStatus() {
        List<ProductInfo> productInfos = repository.findProductInfoByProductStatus(ProductStatus.UP.getCode());
        System.out.println(productInfos);
    }
}