package cn.yang.cao.service;

import cn.yang.cao.dataobject.ProductInfo;
import cn.yang.cao.dto.CartDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductInfoService {
    ProductInfo findOne(String id);

    Page<ProductInfo> findAll(Pageable pageable);

    void save(ProductInfo productInfo);

    //查询所有已上架的商品
    List<ProductInfo> findUpAll();

    //加库存
    void increaseStock(List<CartDTO> cartDTOList);
    //减库存
    void decreaseStock(List<CartDTO> cartDTOList);

    //上架
    ProductInfo onSale(String productId);

    //下架
    ProductInfo offSale(String productId);
}
