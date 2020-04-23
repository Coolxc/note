package cn.yang.cao.controller;

import cn.yang.cao.VO.ProductInfoVO;
import cn.yang.cao.VO.ProductVO;
import cn.yang.cao.VO.ResultVO;
import cn.yang.cao.dataobject.ProductCategory;
import cn.yang.cao.dataobject.ProductInfo;
import cn.yang.cao.service.CategoryService;
import cn.yang.cao.service.ProductInfoService;
import cn.yang.cao.utils.ResultVOUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 查询所有的类目所对应的商品
 * ProductVO(商品类别): CategoryName, CategoryType, List<ProductInfo>
 */
@RestController
@RequestMapping("/buyer/product")
public class BuyerProductController {

    @Autowired
    ProductInfoService productInfoService;

    @Autowired
    CategoryService categoryService;

    @GetMapping("/list")
    public ResultVO list(){
        //1. 查询所有的上架商品
        List<ProductInfo> productInfoList = productInfoService.findUpAll();

        //2. 查询类目的type，然后根据type查找所有类目
        List<Integer> categoryTypeList = productInfoList.stream().map(e->e.getCategoryType()).collect(Collectors.toList());
        //根据列表中每一个类目type查询所有类目对象
        List<ProductCategory> productCategoryList = categoryService.findByCategoryTypeIn(categoryTypeList);

        //3. 数据拼装。一个商品对应一个类别。遍历类别匹配商品
        List<ProductVO> productVOList = new ArrayList<>(); //建立商品总信息列表
        for (ProductCategory productCategory : productCategoryList){
            ProductVO productVO = new ProductVO();
            productVO.setCategoryName(productCategory.getCategoryName());
            productVO.setCategoryType(productCategory.getCategoryType());

            //遍历已上架商品列表，一个类别下有多个商品。与当前类别匹配时将商品详细信息添加到商品列表
            List<ProductInfoVO> productInfoVOList = new ArrayList<>();
            for (ProductInfo productInfo : productInfoList){
                if (productInfo.getCategoryType().equals(productCategory.getCategoryType())) {
                    ProductInfoVO productInfoVO = new ProductInfoVO();
                    BeanUtils.copyProperties(productInfo, productInfoVO);
                    //将属于该类目的商品添加到商品列表
                    productInfoVOList.add(productInfoVO);
                }
            }
            //将商品列表添加到ProductVO。再将该类商品添加到productVOList
            productVO.setProductInfoVOList(productInfoVOList);
            productVOList.add(productVO);
        }
        return ResultVOUtil.success(productVOList);
    }
}
