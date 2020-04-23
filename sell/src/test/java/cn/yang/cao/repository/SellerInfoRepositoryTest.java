package cn.yang.cao.repository;

import cn.yang.cao.dataobject.SellerInfo;
import cn.yang.cao.utils.KeyUtil;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class SellerInfoRepositoryTest {

    @Autowired
    SellerInfoRepository repository;

    @Test
    public void testSave(){
        SellerInfo sellerInfo = new SellerInfo();
        sellerInfo.setId(KeyUtil.genPriKey());
        sellerInfo.setUsername("admin");
        sellerInfo.setPassword("123456");
        repository.save(sellerInfo);
    }

}