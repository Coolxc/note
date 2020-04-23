package cn.yang.cao.service.impl;

import cn.yang.cao.dataobject.SellerInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
class SellerServiceImplTest {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    SellerServiceImpl sellerService;

    @Test
    void findSellerInfo() {
        redisTemplate.opsForValue().set("a", "b");
    }
}