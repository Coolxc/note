package cn.yang.cao.service;

import cn.yang.cao.dataobject.ProductCategory;

import java.util.List;

public interface CategoryService {

    ProductCategory findOne(Integer id);

    List<ProductCategory> findAll();

    List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList);

    ProductCategory save(ProductCategory productCategory);
}
