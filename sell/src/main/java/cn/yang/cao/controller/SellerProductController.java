package cn.yang.cao.controller;

import cn.yang.cao.dataobject.ProductCategory;
import cn.yang.cao.dataobject.ProductInfo;
import cn.yang.cao.exception.SellException;
import cn.yang.cao.form.ProductForm;
import cn.yang.cao.service.CategoryService;
import cn.yang.cao.service.ProductInfoService;
import cn.yang.cao.utils.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@RequestMapping("/seller/product")
public class SellerProductController {

    @Autowired
    private ProductInfoService productInfoService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/list")
    public String list(@RequestParam(value = "page",defaultValue = "1") Integer page,
                       @RequestParam(value = "size", defaultValue = "10") Integer size,
                       Map<String, Object> map){

        PageRequest pageRequest = PageRequest.of(page - 1, size);
        Page<ProductInfo> productInfos = productInfoService.findAll(pageRequest);
        map.put("productInfoPage",productInfos);
        map.put("currentPage", page);
        map.put("size", size);
        return "/product/list";
    }
    @GetMapping("/onSale")
    public String onSale(@RequestParam("productId") String productId,
                         Map<String, Object> map){
        try{
            productInfoService.onSale(productId);
        }catch (SellException e){
            map.put("msg", e.getMessage());
            map.put("url", "/seller/product/list");
            return "/common/error";
        }
        map.put("msg","上架成功");
        map.put("url", "/seller/product/list");
        return "/common/success";
    }

    @GetMapping("/offSale")
    public String offSale(@RequestParam("productId") String productId,
                         Map<String, Object> map){
        try{
            productInfoService.offSale(productId);
        }catch (SellException e){
            map.put("msg", e.getMessage());
            map.put("url", "/seller/product/list");
            return "/common/error";
        }
        map.put("msg","下架成功");
        map.put("url", "/seller/product/list");
        return "/common/success";
    }

    @GetMapping("/index")
    public String index(@RequestParam(value = "productId", required = false) String productId,
                          Map<String, Object> map){
        if (!StringUtils.isEmpty(productId)){
            map.put("productInfo", productInfoService.findOne(productId));
        }
        List<ProductCategory> productCategoryList = categoryService.findAll();
        map.put("categoryList", productCategoryList);
        return "/product/index";
    }

    /**
     * 保存和更新通用方法
     * @param productForm
     * @param bindingResult
     * @return
     */
    @PostMapping("/save")
    public String save(@Valid ProductForm productForm,
                       BindingResult bindingResult,
                       Map<String, Object> map) {
        if (bindingResult.hasErrors()) {
            map.put("msg", bindingResult.getFieldError().getDefaultMessage());
            map.put("url", "/seller/product/list");
            return "/common/error";
        }

        ProductInfo productInfo = new ProductInfo();
        try {
            //如果ProductId为空，说明是新增，否则直接查询
            if (!StringUtils.isEmpty(productForm.getProductId())){
            productInfo = productInfoService.findOne(productForm.getProductId());
            }else {
                productForm.setProductId(KeyUtil.genPriKey());
            }
            BeanUtils.copyProperties(productForm, productInfo);
            productInfoService.save(productInfo); //更新失败则转到错误页面
        }catch (SellException e){
            map.put("msg", e.getMessage());
            map.put("url", "/seller/product/list");
            return "/common/error";
        }
        map.put("msg", "添加/更新商品成功");
        map.put("url", "/seller/product/list");
        return "/common/success";
    }
}
