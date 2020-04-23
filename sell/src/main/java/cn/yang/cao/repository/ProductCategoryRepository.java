package cn.yang.cao.repository;
//编写Dao接口来操作实体类对应的数据表
import cn.yang.cao.dataobject.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

//继承JpaRepository来完成对数据库的操作。JpaRepository实现了JPA规范接口，能够进行CRUD和分页
//该类有泛型，第一个泛型为要操作的实体类，第二个泛型为实体类的主键类型
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Integer> {
    List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList);
}
