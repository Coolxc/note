package cn.yang.cao;

import cn.yang.cao.dataobject.ProductCategory;
import cn.yang.cao.repository.ProductCategoryRepository;
import cn.yang.cao.service.impl.CategorySerivceImpl;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.criteria.CriteriaBuilder;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
class CaoApplicationTests {
    @Autowired
    private ProductCategoryRepository productCategoryRepository;
    @Autowired
    private CategorySerivceImpl categorySerivce;

    @Test
    void contextLoads() {
        ProductCategory productCategory = productCategoryRepository.getOne(1);
        System.out.println(productCategory);
    }
    @Test
    @Transactional //添加这个注解后对数据库的所有操作都将回滚，用于测试
    void saveTest() {
        ProductCategory product = new ProductCategory();
        product.setCategoryName("水果");
        product.setCategoryType(6);
        ProductCategory save = productCategoryRepository.save(product);
        System.out.println(save);
    }
    @Test
    public void findByType(){
        List<Integer> list = Arrays.asList(3,6);
        List<ProductCategory> productCategories = categorySerivce.findByCategoryTypeIn(list);
        System.out.println(productCategories);
    }

}
