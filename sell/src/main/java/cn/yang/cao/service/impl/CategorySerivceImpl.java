package cn.yang.cao.service.impl;

import cn.yang.cao.dataobject.ProductCategory;
import cn.yang.cao.repository.ProductCategoryRepository;
import cn.yang.cao.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategorySerivceImpl implements CategoryService {

    @Autowired
    ProductCategoryRepository repository;

    @Override
    public ProductCategory findOne(Integer id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<ProductCategory> findAll() {
        return repository.findAll();
    }

    @Override
    public List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList) {
        return repository.findByCategoryTypeIn(categoryTypeList);
    }

    @Override
    public ProductCategory save(ProductCategory productCategory) {
        return repository.save(productCategory);
    }
}
