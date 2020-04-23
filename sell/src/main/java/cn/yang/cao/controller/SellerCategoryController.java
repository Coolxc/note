package cn.yang.cao.controller;

import cn.yang.cao.dataobject.ProductCategory;
import cn.yang.cao.exception.SellException;
import cn.yang.cao.form.CategoryForm;
import cn.yang.cao.form.ProductForm;
import cn.yang.cao.service.CategoryService;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/seller/category")
public class SellerCategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/list")
    public String list(Map<String, Object> map){
        List<ProductCategory> categories = categoryService.findAll();
        map.put("categoryList", categories);
        return "/category/list";
    }

    @GetMapping("/index")
    public String index(Map<String, Object> map,
                        @RequestParam(value = "categoryId", required = false) Integer categoryId){
        if (categoryId != null){ //如果id不为空则是修改，反之为增加
            ProductCategory productCategory = categoryService.findOne(categoryId);
            map.put("category", productCategory);
        }
        return "category/index";
    }

    @PostMapping("/save")
    public String save(@Valid CategoryForm categoryForm,
                       BindingResult bindingResult,
                       Map<String, Object> map) {
        if (bindingResult.hasErrors()) {
            map.put("msg", bindingResult.getFieldError().getDefaultMessage());
            map.put("url", "/seller/category/list");
            return "/common/error";
        }
        ProductCategory productCategory = new ProductCategory();
        try { //如果没有id说明是新增
            if (categoryForm.getCategoryId() != null) {
                productCategory = categoryService.findOne(categoryForm.getCategoryId());
            }
            BeanUtils.copyProperties(categoryForm, productCategory);
            categoryService.save(productCategory);
        } catch (SellException e) {
            map.put("msg", e.getMessage());
            map.put("url", "/seller/category/list");
            return "/common/error";
        }catch (Exception e){
            map.put("msg", "此类目已存在");
            map.put("url", "/seller/category/list");
            return "/common/error";
        }
        map.put("url", "/seller/category/list");
        return "/common/success";
    }
}
